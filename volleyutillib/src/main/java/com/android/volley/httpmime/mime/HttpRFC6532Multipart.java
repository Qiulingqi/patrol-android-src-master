/*    */ package com.android.volley.httpmime.mime;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.List;
/*    */ 
/*    */ class HttpRFC6532Multipart extends AbstractMultipartForm
/*    */ {
/*    */   private final List<FormBodyPart> parts;
/*    */ 
/*    */   public HttpRFC6532Multipart(String subType, Charset charset, String boundary, List<FormBodyPart> parts)
/*    */   {
/* 51 */     super(subType, charset, boundary);
/* 52 */     this.parts = parts;
/*    */   }
/*    */ 
/*    */   public List<FormBodyPart> getBodyParts()
/*    */   {
/* 57 */     return this.parts;
/*    */   }
/*    */ 
/*    */   protected void formatMultipartHeader(FormBodyPart part, OutputStream out)
/*    */     throws IOException
/*    */   {
/* 66 */     Header header = part.getHeader();
/* 67 */     for (MinimalField field : header)
/* 68 */       writeField(field, MIME.UTF8_CHARSET, out);
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.HttpRFC6532Multipart
 * JD-Core Version:    0.6.2
 */