/**
 *
 */
package com.saic.visit.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.GsonRequest;
import com.android.volley.toolbox.MultiPartStringRequest;
import com.android.volley.toolbox.Volley;
import com.saic.visit.model.RequestVoRequest;
import com.saic.visit.utils.DialogUtil;
import com.saic.visit.utils.ToastUtil;

import java.io.File;
import java.util.Map;

/**
 * @author liuhui
 */
public class VolleyRequestManager {
    private RequestQueue mVolleyRequestQueue;
    private static volatile VolleyRequestManager instance;
    /*
     * singleTon use case
     */
    public static VolleyRequestManager getInstance(Context context) {
        if (instance == null) {
            synchronized (VolleyRequestManager.class) {
                if (instance == null) {
                    instance = new VolleyRequestManager(context);
                }
            }
        }

        return instance;
    }

    /*
     * Mutlple use case
     */
    public VolleyRequestManager(Context context) {
        mVolleyRequestQueue = Volley.newRequestQueue(context);
    }

    public void cancelAllRequest(Object tag) {
        mVolleyRequestQueue.cancelAll(tag);
    }

    /*
     * interface for inner error status code
     */
    public interface IErrorResponseCallBack {
        public void onFailure(ResponseSupport response);
    }

    /*
     * default disposal for Volley request exception
     *//*
    public static class DefaultErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            DialogUtil.dismisLoading(-1); //network error
        }

    }*/

    /*
     * default Http_Get request via Volley and Gson parser
     * Parameters:
     * Object tag : used it when cancelling request
     * RequestSupport requestSupport: request body Object
     * Class<?> clazz: response GSon class
     * Listener<?> listener: callback for Volley request succeeded
     * ErrorListener errorListener: callback for Volley request failed
     */
    public <T extends ResponseSupport> void startHttpGetRequest(final Object tag, final RequestSupport requestSupport, Class<T> clazz, Response.ListenerV2<T> listener, Response.ErrorListener errorListener) {
        this.startHttpRequest(tag, Request.Method.GET, requestSupport, clazz, listener, errorListener);
    }
    public <T extends ResponseSupport> void startHttpGetRequest2(final Object tag, final RequestSupport requestSupport, Class<T> clazz, Response.ListenerV2<T> listener, Response.ErrorListener errorListener) {
        this.startHttpRequest2(tag, Request.Method.GET, requestSupport, clazz, listener, errorListener);
    }

    /*
     * default Http_Post request via Volley and Gson parser with default Volley error disposal
     * Parameters:
     * Object tag : used it when cancelling request
     * RequestSupport requestSupport: request body Object
     * Class<?> clazz: response GSon class
     * Listener<?> listener: callback for Volley request succeeded
     */
    public <T extends ResponseSupport> void startHttpPostRequest(final Object tag, final RequestVoRequest requestSupport, Class<ResponseSupport> clazz, Response.ListenerV2<ResponseSupport> listener, Response.ErrorListener errorListener) {
        this.startHttpRequest(tag, Request.Method.POST, requestSupport, clazz, listener, errorListener);
    }

    public <T extends ResponseSupport> void startHttpPutRequest(final Object tag, final RequestSupport requestSupport, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this.startHttpRequest(tag, Request.Method.PUT, requestSupport, clazz, listener, errorListener);
    }

    public <T extends ResponseSupport> void startHttpDeleteRequest(final Object tag, final RequestSupport requestSupport, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this.startHttpRequest(tag, Request.Method.DELETE, requestSupport, clazz, listener, errorListener);
    }

    public <T extends ResponseSupport> void startHttpPostUploadFileRequest(RequestQueue requestQueue,final Object tag, final RequestSupport requestSupport, Class<T> clazz,
                                                final Map<String, File> files, final Map<String, String> params,
                                                final Response.Listener<T> responseListener, final Response.ErrorListener errorListener) {
        if (null == responseListener) {
            return;
        }
        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
                Request.Method.POST, clazz, VolleyConfig.getUrl(requestSupport.getMessageId()), responseListener, errorListener) {

            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }

            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }

        };
        multiPartRequest.setTag(tag.hashCode());
        requestQueue.add(multiPartRequest);
    }
//    public <T extends ResponseSupport> void startHttpPostRequest(final Object tag, final RequestSupport requestSupport, Class<T> clazz, Response.Listener<T> listener) {
//        this.startHttpRequest(tag, Request.Method.POST, requestSupport, clazz, listener, new DefaultErrorListener());
//    }
//
//    public <T extends ResponseSupport> void startHttpPostRequest(final Object tag, final RequestSupport requestSupport, Class<T> clazz, Response.ListenerV2<T> listener) {
//        this.startHttpRequest(tag, Request.Method.POST, requestSupport, clazz, listener, new DefaultErrorListener());
//    }

    /*
     * default Http_Post request via Volley and Gson parser
     * Parameters:
     * Object tag : used it when cancelling request
     * RequestSupport requestSupport: request body Object
     * Class<?> clazz: response GSon class
     * Listener<?> listener: callback for Volley request succeeded
     * ErrorListener errorListener: callback for Volley request failed  HttpMethod.POST
     */
    public <T extends ResponseSupport> void startHttpPostRequest(final Object tag, final RequestSupport requestSupport, Class<T> clazz, Response.ListenerV2<T> listener, Response.ErrorListener errorListener) {
        this.startHttpRequest(tag, Request.Method.POST, requestSupport, clazz, listener, errorListener);
    }


    /*
     * Volley Http request via Volley and Gson parser
     * Parameters:
     * Object tag : used it when cancelling request
     * HttpMethod httpMethod: request method
     * RequestSupport requestSupport: request body Object
     * Class<?> clazz: response GSon class
     * Listener<?> listener: callback for Volley request succeeded
     * ErrorListener errorListener: callback for Volley request failed
     */
    public <T extends ResponseSupport> void startHttpRequest(final Object tag, int httpMethod, final RequestSupport rs, Class<T> clazz, final Response.Listener<T> listener, final Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<T>(httpMethod, clazz, VolleyConfig.getUrl(rs.getMessageId()), GsonRequest.gson.toJson(rs), listener, errorListener);
        request.setTag(tag.hashCode());
        mVolleyRequestQueue.add(request);
    }

    public <T extends ResponseSupport> void startHttpRequest(final Object tag, int httpMethod, final RequestSupport rs, Class<T> clazz, final Response.ListenerV2<T> listener, final Response.ErrorListener errorListener) {
        addRequest(tag.hashCode(), new GsonBaseRequest<T>(httpMethod, clazz, rs, listener, errorListener));
    }

    public <T extends ResponseSupport> void startHttpRequest2(final Object tag, int httpMethod, final RequestSupport rs, Class<T> clazz, final Response.ListenerV2<T> listener, final Response.ErrorListener errorListener) {
        addRequest(tag.hashCode(), new GsonBaseRequest<T>(httpMethod, clazz, rs, listener, errorListener,1));
    }

    public <T extends ResponseSupport> void addRequest( Request<T> request) {
        addRequest(null, request);
    }

    public <T extends ResponseSupport> void addRequest(final Object tag, Request<T> request) {
        if ( tag != null ) {
            request.setTag(tag);
        }


        if ( request instanceof  GsonBaseRequest) {
            GsonBaseRequest gsonBaseRequest = (GsonBaseRequest)request;
            // add header
            gsonBaseRequest.makeHeader();
            mVolleyRequestQueue.add(gsonBaseRequest);
        }
    }

    /*
     * base response disposal when Volley response succeeded
     * Parameters:
     * Context context : application context for cache directory or package info
     * ResponseSupport responseSupport: response body Object
     * Listener<?> listener: callback for Volley request succeeded
     * IErrorResponseCallBack errorListener: callback for inner failure
     *  boolean hideLoading: whether or not to hide loading Dialog
     */
    public static boolean realResponseResultSupport(final Context context, ResponseSupport responseSupport, final IErrorResponseCallBack errorListener, boolean hideLoading) {
        if(hideLoading){
            DialogUtil.dismisLoading();
        }
        if (responseSupport.result==1) {
            return true;
        } else{
            String statusMessage = responseSupport.msg;
            ToastUtil.show(context,statusMessage);
            if (errorListener != null) {
                errorListener.onFailure(responseSupport);
            }


        }
        return false;
        }
        }
