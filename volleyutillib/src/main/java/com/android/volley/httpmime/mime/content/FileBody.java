/*     */ package com.android.volley.httpmime.mime.content;
/*     */ 
/*     */

import com.android.volley.AbstractContentBody;
import com.android.volley.httpcore.Args;
import com.android.volley.httpcore.ContentType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class FileBody extends AbstractContentBody
/*     */ {
/*     */   private final File file;
/*     */   private final String filename;
/*     */ 
/*     */   @Deprecated
/*     */   public FileBody(File file, String filename, String mimeType, String charset)
/*     */   {
/*  63 */     this(file, ContentType.create(mimeType, charset), filename);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public FileBody(File file, String mimeType, String charset)
/*     */   {
/*  76 */     this(file, null, mimeType, charset);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public FileBody(File file, String mimeType)
/*     */   {
/*  85 */     this(file, ContentType.create(mimeType), null);
/*     */   }
/*     */ 
/*     */   public FileBody(File file) {
/*  89 */     this(file, ContentType.DEFAULT_BINARY, file != null ? file.getName() : null);
/*     */   }
/*     */ 
/*     */   public FileBody(File file, ContentType contentType, String filename)
/*     */   {
/*  96 */     super(contentType);
/*  97 */     Args.notNull(file, "File");
/*  98 */     this.file = file;
/*  99 */     this.filename = filename;
/*     */   }
/*     */ 
/*     */   public FileBody(File file, ContentType contentType)
/*     */   {
/* 106 */     this(file, contentType, null);
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream() throws IOException {
/* 110 */     return new FileInputStream(this.file);
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 114 */     Args.notNull(out, "Output stream");
/* 115 */     InputStream in = new FileInputStream(this.file);
/*     */     try {
/* 117 */       byte[] tmp = new byte[4096];
/*     */       int l;
/* 119 */       while ((l = in.read(tmp)) != -1) {
/* 120 */         out.write(tmp, 0, l);
/*     */       }
/* 122 */       out.flush();
/*     */     } finally {
/* 124 */       in.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getTransferEncoding() {
/* 129 */     return "binary";
/*     */   }
/*     */ 
/*     */   public long getContentLength() {
/* 133 */     return this.file.length();
/*     */   }
/*     */ 
/*     */   public String getFilename() {
/* 137 */     return this.filename;
/*     */   }
/*     */ 
/*     */   public File getFile() {
/* 141 */     return this.file;
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.content.FileBody
 * JD-Core Version:    0.6.2
 */