/*     */ package com.android.volley.httpmime.mime.content;
/*     */ 
/*     */

import com.android.volley.AbstractContentBody;
import com.android.volley.httpcore.Args;
import com.android.volley.httpcore.ContentType;

import java.io.IOException;
import java.io.OutputStream;

/*     */
/*     */
/*     */
/*     */ 
/*     */ public class ByteArrayBody extends AbstractContentBody
/*     */ {
/*     */   private final byte[] data;
/*     */   private final String filename;
/*     */ 
/*     */   @Deprecated
/*     */   public ByteArrayBody(byte[] data, String mimeType, String filename)
/*     */   {
/*  67 */     this(data, ContentType.create(mimeType), filename);
/*     */   }
/*     */ 
/*     */   public ByteArrayBody(byte[] data, ContentType contentType, String filename)
/*     */   {
/*  74 */     super(contentType);
/*  75 */     Args.notNull(data, "byte[]");
/*  76 */     this.data = data;
/*  77 */     this.filename = filename;
/*     */   }
/*     */ 
/*     */   public ByteArrayBody(byte[] data, String filename)
/*     */   {
/*  87 */     this(data, "application/octet-stream", filename);
/*     */   }
/*     */ 
/*     */   public String getFilename() {
/*  91 */     return this.filename;
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  95 */     out.write(this.data);
/*     */   }
/*     */ 
/*     */   public String getCharset()
/*     */   {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTransferEncoding() {
/* 104 */     return "binary";
/*     */   }
/*     */ 
/*     */   public long getContentLength() {
/* 108 */     return this.data.length;
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.content.ByteArrayBody
 * JD-Core Version:    0.6.2
 */