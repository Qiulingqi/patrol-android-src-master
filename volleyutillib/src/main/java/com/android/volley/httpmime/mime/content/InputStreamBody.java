/*     */ package com.android.volley.httpmime.mime.content;
/*     */ 
/*     */

import com.android.volley.AbstractContentBody;
import com.android.volley.httpcore.Args;
import com.android.volley.httpcore.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class InputStreamBody extends AbstractContentBody
/*     */ {
/*     */   private final InputStream in;
/*     */   private final String filename;
/*     */ 
/*     */   @Deprecated
/*     */   public InputStreamBody(InputStream in, String mimeType, String filename)
/*     */   {
/*  58 */     this(in, ContentType.create(mimeType), filename);
/*     */   }
/*     */ 
/*     */   public InputStreamBody(InputStream in, String filename) {
/*  62 */     this(in, ContentType.DEFAULT_BINARY, filename);
/*     */   }
/*     */ 
/*     */   public InputStreamBody(InputStream in, ContentType contentType, String filename)
/*     */   {
/*  69 */     super(contentType);
/*  70 */     Args.notNull(in, "Input stream");
/*  71 */     this.in = in;
/*  72 */     this.filename = filename;
/*     */   }
/*     */ 
/*     */   public InputStreamBody(InputStream in, ContentType contentType)
/*     */   {
/*  79 */     this(in, contentType, null);
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream() {
/*  83 */     return this.in;
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  87 */     Args.notNull(out, "Output stream");
/*     */     try {
/*  89 */       byte[] tmp = new byte[4096];
/*     */       int l;
/*  91 */       while ((l = this.in.read(tmp)) != -1) {
/*  92 */         out.write(tmp, 0, l);
/*     */       }
/*  94 */       out.flush();
/*     */     } finally {
/*  96 */       this.in.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getTransferEncoding() {
/* 101 */     return "binary";
/*     */   }
/*     */ 
/*     */   public long getContentLength() {
/* 105 */     return -1L;
/*     */   }
/*     */ 
/*     */   public String getFilename() {
/* 109 */     return this.filename;
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.content.InputStreamBody
 * JD-Core Version:    0.6.2
 */