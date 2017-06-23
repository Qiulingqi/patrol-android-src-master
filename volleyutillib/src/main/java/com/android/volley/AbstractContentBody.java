//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.android.volley;

import com.android.volley.httpcore.Args;
import com.android.volley.httpcore.ContentType;
import com.android.volley.httpmime.mime.content.ContentBody;

import java.nio.charset.Charset;

public abstract class AbstractContentBody implements ContentBody {
    private final ContentType contentType;

    public AbstractContentBody(ContentType contentType) {
        Args.notNull(contentType, "Content type");
        this.contentType = contentType;
    }

    /** @deprecated */
    @Deprecated
    public AbstractContentBody(String mimeType) {
        this(ContentType.parse(mimeType));
    }

    public ContentType getContentType() {
        return this.contentType;
    }

    public String getMimeType() {
        return this.contentType.getMimeType();
    }

    public String getMediaType() {
        String mimeType = this.contentType.getMimeType();
        int i = mimeType.indexOf(47);
        return i != -1?mimeType.substring(0, i):mimeType;
    }

    public String getSubType() {
        String mimeType = this.contentType.getMimeType();
        int i = mimeType.indexOf(47);
        return i != -1?mimeType.substring(i + 1):null;
    }

    public String getCharset() {
        Charset charset = this.contentType.getCharset();
        return charset != null?charset.name():null;
    }
}
