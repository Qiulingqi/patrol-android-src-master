package com.android.volley.httpmime.mime.content;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface ContentBody extends ContentDescriptor
{
  public abstract String getFilename();

  public abstract void writeTo(OutputStream paramOutputStream)
    throws IOException;
}

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.content.ContentBody
 * JD-Core Version:    0.6.2
 */