//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.android.volley.httpmime.mime;

import com.android.volley.httpcore.Args;
import com.android.volley.httpcore.ContentType;
import com.android.volley.httpmime.mime.content.ByteArrayBody;
import com.android.volley.httpmime.mime.content.ContentBody;
import com.android.volley.httpmime.mime.content.FileBody;
import com.android.volley.httpmime.mime.content.InputStreamBody;
import com.android.volley.httpmime.mime.content.StringBody;

import org.apache.http.HttpEntity;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultipartEntityBuilder {
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final String DEFAULT_SUBTYPE = "form-data";
    private String subType = "form-data";
    private HttpMultipartMode mode;
    private String boundary;
    private Charset charset;
    private List<FormBodyPart> bodyParts;

    public static MultipartEntityBuilder create() {
        return new MultipartEntityBuilder();
    }

    MultipartEntityBuilder() {
        this.mode = HttpMultipartMode.STRICT;
        this.boundary = null;
        this.charset = null;
        this.bodyParts = null;
    }

    public MultipartEntityBuilder setMode(HttpMultipartMode mode) {
        this.mode = mode;
        return this;
    }

    public MultipartEntityBuilder setLaxMode() {
        this.mode = HttpMultipartMode.BROWSER_COMPATIBLE;
        return this;
    }

    public MultipartEntityBuilder setStrictMode() {
        this.mode = HttpMultipartMode.STRICT;
        return this;
    }

    public MultipartEntityBuilder setBoundary(String boundary) {
        this.boundary = boundary;
        return this;
    }

    public MultipartEntityBuilder setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    MultipartEntityBuilder addPart(FormBodyPart bodyPart) {
        if(bodyPart == null) {
            return this;
        } else {
            if(this.bodyParts == null) {
                this.bodyParts = new ArrayList();
            }

            this.bodyParts.add(bodyPart);
            return this;
        }
    }

    public MultipartEntityBuilder addPart(String name, ContentBody contentBody) {
        Args.notNull(name, "Name");
        Args.notNull(contentBody, "Content body");
        return this.addPart(new FormBodyPart(name, contentBody));
    }

    public MultipartEntityBuilder addTextBody(String name, String text, ContentType contentType) {
        return this.addPart(name, new StringBody(text, contentType));
    }

    public MultipartEntityBuilder addTextBody(String name, String text) {
        return this.addTextBody(name, text, ContentType.DEFAULT_TEXT);
    }

    public MultipartEntityBuilder addBinaryBody(String name, byte[] b, ContentType contentType, String filename) {
        return this.addPart(name, new ByteArrayBody(b, contentType, filename));
    }

    public MultipartEntityBuilder addBinaryBody(String name, byte[] b) {
        return this.addBinaryBody(name, (byte[])b, ContentType.DEFAULT_BINARY, (String)null);
    }

    public MultipartEntityBuilder addBinaryBody(String name, File file, ContentType contentType, String filename) {
        return this.addPart(name, new FileBody(file, contentType, filename));
    }

    public MultipartEntityBuilder addBinaryBody(String name, File file) {
        return this.addBinaryBody(name, file, ContentType.DEFAULT_BINARY, file != null?file.getName():null);
    }

    public MultipartEntityBuilder addBinaryBody(String name, InputStream stream, ContentType contentType, String filename) {
        return this.addPart(name, new InputStreamBody(stream, contentType, filename));
    }

    public MultipartEntityBuilder addBinaryBody(String name, InputStream stream) {
        return this.addBinaryBody(name, (InputStream)stream, ContentType.DEFAULT_BINARY, (String)null);
    }

    private String generateContentType(String boundary, Charset charset) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("multipart/form-data; boundary=");
        buffer.append(boundary);
        if(charset != null) {
            buffer.append("; charset=");
            buffer.append(charset.name());
        }

        return buffer.toString();
    }

    private String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30;

        for(int i = 0; i < count; ++i) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }

        return buffer.toString();
    }

    MultipartFormEntity buildEntity() {
        String st = this.subType != null?this.subType:"form-data";
        Charset cs = this.charset;
        String b = this.boundary != null?this.boundary:this.generateBoundary();
        Object bps = this.bodyParts != null?new ArrayList(this.bodyParts):Collections.emptyList();
        HttpMultipartMode m = this.mode != null?this.mode:HttpMultipartMode.STRICT;
        Object form;
        switch(m.ordinal()) {
            case 1:
                form = new HttpBrowserCompatibleMultipart(st, cs, b, (List)bps);
                break;
            case 2:
                form = new HttpRFC6532Multipart(st, cs, b, (List)bps);
                break;
            default:
                form = new HttpStrictMultipart(st, cs, b, (List)bps);
        }

        return new MultipartFormEntity((AbstractMultipartForm)form, this.generateContentType(b, cs), ((AbstractMultipartForm)form).getTotalLength());
    }

    public HttpEntity build() {
        return this.buildEntity();
    }
}
