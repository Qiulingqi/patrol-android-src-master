/**
 *
 */
package com.saic.visit.http;

/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in com.android.liance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.GsonRequest;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 *
 * @param <T> JSON type of response expected
 */
public class GsonBaseRequest<T extends ResponseSupport> extends Request<T> {
    /**
     * Default charset for JSON request.
     */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Class<T> mClazz;
    private Response.ListenerV2<T> mListener;
    private final RequestSupport mRequestBody;
    private Map<String, String> mResponseHeader;
    private Map<String, String> mResquestHeader = new HashMap<String, String>();
    private boolean bNeedDestory = true;
    private static List<GsonBaseRequest<? extends ResponseSupport>> requestWaiting =
            new ArrayList<GsonBaseRequest<? extends ResponseSupport>>();
    private int tryTimes = 0;
    private static int MAX_TRYTIMES = 5;

    public void addOneTry() {
        tryTimes++;
    }

    public boolean canTryAgain() { //disable always retry when possible wrong apis
        return tryTimes < MAX_TRYTIMES - 1;
    }

    public final static Gson gson = new Gson();

    public GsonBaseRequest(int method, Class<T> clazz, final RequestSupport requestBody, Response.ListenerV2<T> listener, Response.ErrorListener errorListener) {
        super(method, VolleyConfig.getUrl(requestBody.getMessageId()), errorListener);
        mListener = listener;
        mRequestBody = requestBody;
        mClazz = clazz;

        setRetryPolicy(new DefaultRetryPolicy(GsonRequest.JSONREQUEST_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.setShouldCache(false);//no cache, when needed to cache, you can override setShouldCache(true)
    }

    public GsonBaseRequest(int method, Class<T> clazz, final RequestSupport requestBody, Response.ListenerV2<T> listener, Response.ErrorListener errorListener,int i) {
        super(method, VolleyConfig.getUrl2(requestBody.getMessageId()), errorListener);
        mListener = listener;
        mRequestBody = requestBody;
        mClazz = clazz;

        setRetryPolicy(new DefaultRetryPolicy(GsonRequest.JSONREQUEST_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.setShouldCache(false);//no cache, when needed to cache, you can override setShouldCache(true)
    }




    public GsonBaseRequest(int method, Class<T> clazz, final String url, Response.ListenerV2<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mRequestBody = null;
        mClazz = clazz;

        this.setShouldCache(false);//no cache, when needed to cache, you can override setShouldCache(true)
    }

    /*
     * 1. object 先序列化成 json 字符串
       2. json字符串经过UTF8编码到 Bytes
       3. Bytes 进行GZIP 压缩
       4. 压缩后的数据进行 AES 加密
     * (non-Javadoc)
     * @see com.android.android.volley.Request#parseNetworkResponse(com.android.android.volley.NetworkResponse)
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            mResponseHeader = response.headers;

            if (response.data.length > 0) {
                String json = new String(response.data, "utf-8");
                Log.d("----liuhui", "----liuhui--GsonBaseResponse--json:" + json);
                return Response.success(gson.fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.success(mClazz.newInstance(), HttpHeaderParser.parseCacheHeaders(response));
            }

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mResquestHeader;
    }

    @Override
    protected void deliverResponse(final T response) {
        bNeedDestory = true;
        try {
            mListener.onResponse(response, mResponseHeader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeHeader() {
        mResquestHeader.put("Content-Type", VolleyConfig.content_type);//infrastructure api need!
    }

    /**
     * @deprecated Use {@link #getBodyContentType()}.
     */
    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    /**
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public byte[] getPostBody() {
        return getBody();
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            // after gson ==> then utf-8 => then gzip => then aes
            byte[] bytes = mRequestBody == null ? null : gson.toJson(mRequestBody).getBytes(PROTOCOL_CHARSET);
            Log.d("----liuhui", "----liuhui--GsonBaseRequest--json:" + gson.toJson(mRequestBody));
            return bytes;
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, PROTOCOL_CHARSET);
            return null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public GsonBaseRequest<T> appendHeader(String key, String value) {
        mResquestHeader.put(key, value);
        return this;
    }
    /**
     * Returns the URL of this request.
     */
    @Override
    public String getUrl() {
        try {
            if (this.getParams() != null) {
                return new StringBuilder().append(mUrl).append(new String(super.getBody())).toString();
            }
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }
        return mUrl;
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        if (mRequestBody != null) {
            return mRequestBody.qureyParameters();
        }
        return null;
    }

    @Override
    protected void onFinished() {
        if (bNeedDestory) {
            super.onFinished();

            this.mListener = null;
        }

        bNeedDestory = true;
    }
}