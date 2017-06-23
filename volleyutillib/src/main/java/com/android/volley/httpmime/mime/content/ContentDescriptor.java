package com.android.volley.httpmime.mime.content;

public abstract interface ContentDescriptor
{
  public abstract String getMimeType();

  public abstract String getMediaType();

  public abstract String getSubType();

  public abstract String getCharset();

  public abstract String getTransferEncoding();

  public abstract long getContentLength();
}

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.content.ContentDescriptor
 * JD-Core Version:    0.6.2
 */