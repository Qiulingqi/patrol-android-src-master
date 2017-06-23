/*     */ package com.android.volley.httpmime.mime;
/*     */
/*     */

import com.android.volley.AbstractContentBody;
import com.android.volley.httpcore.Args;
import com.android.volley.httpcore.ContentType;
import com.android.volley.httpmime.mime.content.ContentBody;

/*     */
/*     */ public class FormBodyPart
/*     */ {
/*     */   private final String name;
/*     */   private final Header header;
/*     */   private final ContentBody body;
/*     */ 
/*     */   public FormBodyPart(String name, ContentBody body)
/*     */   {
/*  51 */     Args.notNull(name, "Name");
/*  52 */     Args.notNull(body, "Body");
/*  53 */     this.name = name;
/*  54 */     this.body = body;
/*  55 */     this.header = new Header();
/*     */ 
/*  57 */     generateContentDisp(body);
/*  58 */     generateContentType(body);
/*  59 */     generateTransferEncoding(body);
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  63 */     return this.name;
/*     */   }
/*     */ 
/*     */   public ContentBody getBody() {
/*  67 */     return this.body;
/*     */   }
/*     */ 
/*     */   public Header getHeader() {
/*  71 */     return this.header;
/*     */   }
/*     */ 
/*     */   public void addField(String name, String value) {
/*  75 */     Args.notNull(name, "Field name");
/*  76 */     this.header.addField(new MinimalField(name, value));
/*     */   }
/*     */ 
/*     */   protected void generateContentDisp(ContentBody body) {
/*  80 */     StringBuilder buffer = new StringBuilder();
/*  81 */     buffer.append("form-data; name=\"");
/*  82 */     buffer.append(getName());
/*  83 */     buffer.append("\"");
/*  84 */     if (body.getFilename() != null) {
/*  85 */       buffer.append("; filename=\"");
/*  86 */       buffer.append(body.getFilename());
/*  87 */       buffer.append("\"");
/*     */     }
/*  89 */     addField("Content-Disposition", buffer.toString());
/*     */   }
/*     */ 
/*     */   protected void generateContentType(ContentBody body)
/*     */   {
/*     */     ContentType contentType;
/*  94 */     if ((body instanceof AbstractContentBody))
/*  95 */       contentType = ((AbstractContentBody)body).getContentType();
/*     */     else {
/*  97 */       contentType = null;
/*     */     }
/*  99 */     if (contentType != null) {
/* 100 */       addField("Content-Type", contentType.toString());
/*     */     } else {
/* 102 */       StringBuilder buffer = new StringBuilder();
/* 103 */       buffer.append(body.getMimeType());
/* 104 */       if (body.getCharset() != null) {
/* 105 */         buffer.append("; charset=");
/* 106 */         buffer.append(body.getCharset());
/*     */       }
/* 108 */       addField("Content-Type", buffer.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateTransferEncoding(ContentBody body) {
/* 113 */     addField("Content-Transfer-Encoding", body.getTransferEncoding());
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.FormBodyPart
 * JD-Core Version:    0.6.2
 */