/*    */ package com.android.volley.httpmime.mime;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.List;
/*    */ 
/*    */ class HttpStrictMultipart extends AbstractMultipartForm
/*    */ {
/*    */   private final List<FormBodyPart> parts;
/*    */ 
/*    */   public HttpStrictMultipart(String subType, Charset charset, String boundary, List<FormBodyPart> parts)
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
/* 68 */       writeField(field, out);
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.HttpStrictMultipart
 * JD-Core Version:    0.6.2
 */