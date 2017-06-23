/*    */ package com.android.volley.httpmime.mime;
/*    */ 
/*    */ import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ class MultipartFormEntity
/*    */   implements HttpEntity
/*    */ {
/*    */   private final AbstractMultipartForm multipart;
/*    */   private final Header contentType;
/*    */   private final long contentLength;
/*    */ 
/*    */   MultipartFormEntity(AbstractMultipartForm multipart, String contentType, long contentLength)
/*    */   {
/* 50 */     this.multipart = multipart;
/* 51 */     this.contentType = new BasicHeader("Content-Type", contentType);
/* 52 */     this.contentLength = contentLength;
/*    */   }
/*    */ 
/*    */   AbstractMultipartForm getMultipart() {
/* 56 */     return this.multipart;
/*    */   }
/*    */ 
/*    */   public boolean isRepeatable() {
/* 60 */     return this.contentLength != -1L;
/*    */   }
/*    */ 
/*    */   public boolean isChunked() {
/* 64 */     return !isRepeatable();
/*    */   }
/*    */ 
/*    */   public boolean isStreaming() {
/* 68 */     return !isRepeatable();
/*    */   }
/*    */ 
/*    */   public long getContentLength() {
/* 72 */     return this.contentLength;
/*    */   }
/*    */ 
/*    */   public Header getContentType() {
/* 76 */     return this.contentType;
/*    */   }
/*    */ 
/*    */   public Header getContentEncoding() {
/* 80 */     return null;
/*    */   }
/*    */ 
/*    */   public void consumeContent() throws IOException, UnsupportedOperationException
/*    */   {
/* 85 */     if (isStreaming())
/* 86 */       throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
/*    */   }
/*    */ 
/*    */   public InputStream getContent()
/*    */     throws IOException
/*    */   {
/* 92 */     throw new UnsupportedOperationException("Multipart form entity does not implement #getContent()");
/*    */   }
/*    */ 
/*    */   public void writeTo(OutputStream outstream) throws IOException
/*    */   {
/* 97 */     this.multipart.writeTo(outstream);
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.MultipartFormEntity
 * JD-Core Version:    0.6.2
 */