//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.android.volley.httpmime.mime.content;

import com.android.volley.AbstractContentBody;
import com.android.volley.httpcore.Args;
import com.android.volley.httpcore.Consts;
import com.android.volley.httpcore.ContentType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class StringBody extends AbstractContentBody {
    private final byte[] content;

    /**
     * @deprecated
     */
    @Deprecated
    public static StringBody create(String text, String mimeType, Charset charset) throws IllegalArgumentException {
        try {
            return new StringBody(text, mimeType, charset);
        } catch (UnsupportedEncodingException var4) {
            throw new IllegalArgumentException("Charset " + charset + " is not supported", var4);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static StringBody create(String text, Charset charset) throws IllegalArgumentException {
        return create(text, (String) null, charset);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static StringBody create(String text) throws IllegalArgumentException {
        return create(text, (String) null, (Charset) null);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public StringBody(String text, String mimeType, Charset charset) throws UnsupportedEncodingException {
        this(text, ContentType.create(mimeType, charset));
    }

    /**
     * @deprecated
     */
    @Deprecated
    public StringBody(String text, Charset charset) throws UnsupportedEncodingException {
        this(text, "text/plain", charset);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public StringBody(String text) throws UnsupportedEncodingException {
        this(text, "text/plain", Consts.ASCII);
    }

    public StringBody(String text, ContentType contentType) {
        super(contentType);
        Args.notNull(text, "Text");
        Charset charset = contentType.getCharset();
        String csname = charset != null ? charset.name() : Consts.ASCII.name();

        try {
            this.content = text.getBytes(csname);
        } catch (UnsupportedEncodingException var6) {
            throw new UnsupportedCharsetException(csname);
        }
    }

    public Reader getReader() {
        Charset charset = this.getContentType().getCharset();
        return new InputStreamReader(new ByteArrayInputStream(this.content), charset != null ? charset : Consts.ASCII);
    }

    public void writeTo(OutputStream out) throws IOException {
        Args.notNull(out, "Output stream");
        ByteArrayInputStream in = new ByteArrayInputStream(this.content);
        byte[] tmp = new byte[4096];

        int l;
        while ((l = in.read(tmp)) != -1) {
            out.write(tmp, 0, l);
        }

        out.flush();
    }

    public String getTransferEncoding() {
        return "8bit";
    }

    public long getContentLength() {
        return (long) this.content.length;
    }

    public String getFilename() {
        return null;
    }
}
