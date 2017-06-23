/*    */ package com.android.volley.httpmime.mime;
/*    */ 
/*    */ import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ class HttpBrowserCompatibleMultipart extends AbstractMultipartForm
/*    */ {
/*    */   private final List<FormBodyPart> parts;
/*    */ 
/*    */   public HttpBrowserCompatibleMultipart(String subType, Charset charset, String boundary, List<FormBodyPart> parts)
/*    */   {
/* 50 */     super(subType, charset, boundary);
/* 51 */     this.parts = parts;
/*    */   }
/*    */ 
/*    */   public List<FormBodyPart> getBodyParts()
/*    */   {
/* 56 */     return this.parts;
/*    */   }
/*    */ 
/*    */   protected void formatMultipartHeader(FormBodyPart part, OutputStream out)
/*    */     throws IOException
/*    */   {
/* 68 */     Header header = part.getHeader();
/* 69 */     MinimalField cd = header.getField("Content-Disposition");
/* 70 */     writeField(cd, this.charset, out);
/* 71 */     String filename = part.getBody().getFilename();
/* 72 */     if (filename != null) {
/* 73 */       MinimalField ct = header.getField("Content-Type");
/* 74 */       writeField(ct, this.charset, out);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.HttpBrowserCompatibleMultipart
 * JD-Core Version:    0.6.2
 */