/*     */ package com.android.volley.httpmime.mime;
/*     */ 
/*     */

import com.android.volley.httpcore.Args;
import com.android.volley.httpmime.mime.content.ContentBody;

import org.apache.http.util.ByteArrayBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ abstract class AbstractMultipartForm
/*     */ {
/*  92 */   private static final ByteArrayBuffer FIELD_SEP = encode(MIME.DEFAULT_CHARSET, ": ");
/*  93 */   private static final ByteArrayBuffer CR_LF = encode(MIME.DEFAULT_CHARSET, "\r\n");
/*  94 */   private static final ByteArrayBuffer TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");
/*     */   private final String subType;
/*     */   protected final Charset charset;
/*     */   private final String boundary;
/*     */ 
/*     */   private static ByteArrayBuffer encode(Charset charset, String string)
/*     */   {
/*  53 */     ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
/*  54 */     ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
/*  55 */     bab.append(encoded.array(), encoded.position(), encoded.remaining());
/*  56 */     return bab;
/*     */   }
/*     */ 
/*     */   private static void writeBytes(ByteArrayBuffer b, OutputStream out) throws IOException
/*     */   {
/*  61 */     out.write(b.buffer(), 0, b.length());
/*     */   }
/*     */ 
/*     */   private static void writeBytes(String s, Charset charset, OutputStream out) throws IOException
/*     */   {
/*  66 */     ByteArrayBuffer b = encode(charset, s);
/*  67 */     writeBytes(b, out);
/*     */   }
/*     */ 
/*     */   private static void writeBytes(String s, OutputStream out) throws IOException
/*     */   {
/*  72 */     ByteArrayBuffer b = encode(MIME.DEFAULT_CHARSET, s);
/*  73 */     writeBytes(b, out);
/*     */   }
/*     */ 
/*     */   protected static void writeField(MinimalField field, OutputStream out) throws IOException
/*     */   {
/*  78 */     writeBytes(field.getName(), out);
/*  79 */     writeBytes(FIELD_SEP, out);
/*  80 */     writeBytes(field.getBody(), out);
/*  81 */     writeBytes(CR_LF, out);
/*     */   }
/*     */ 
/*     */   protected static void writeField(MinimalField field, Charset charset, OutputStream out) throws IOException
/*     */   {
/*  86 */     writeBytes(field.getName(), charset, out);
/*  87 */     writeBytes(FIELD_SEP, out);
/*  88 */     writeBytes(field.getBody(), charset, out);
/*  89 */     writeBytes(CR_LF, out);
/*     */   }
/*     */ 
/*     */   public AbstractMultipartForm(String subType, Charset charset, String boundary)
/*     */   {
/* 110 */     Args.notNull(subType, "Multipart subtype");
/* 111 */     Args.notNull(boundary, "Multipart boundary");
/* 112 */     this.subType = subType;
/* 113 */     this.charset = (charset != null ? charset : MIME.DEFAULT_CHARSET);
/* 114 */     this.boundary = boundary;
/*     */   }
/*     */ 
/*     */   public AbstractMultipartForm(String subType, String boundary) {
/* 118 */     this(subType, null, boundary);
/*     */   }
/*     */ 
/*     */   public String getSubType() {
/* 122 */     return this.subType;
/*     */   }
/*     */ 
/*     */   public Charset getCharset() {
/* 126 */     return this.charset;
/*     */   }
/*     */ 
/*     */   public abstract List<FormBodyPart> getBodyParts();
/*     */ 
/*     */   public String getBoundary() {
/* 132 */     return this.boundary;
/*     */   }
/*     */ 
/*     */   void doWriteTo(OutputStream out, boolean writeContent)
/*     */     throws IOException
/*     */   {
/* 139 */     ByteArrayBuffer boundary = encode(this.charset, getBoundary());
/* 140 */     for (FormBodyPart part : getBodyParts()) {
/* 141 */       writeBytes(TWO_DASHES, out);
/* 142 */       writeBytes(boundary, out);
/* 143 */       writeBytes(CR_LF, out);
/*     */ 
/* 145 */       formatMultipartHeader(part, out);
/*     */ 
/* 147 */       writeBytes(CR_LF, out);
/*     */ 
/* 149 */       if (writeContent) {
/* 150 */         part.getBody().writeTo(out);
/*     */       }
/* 152 */       writeBytes(CR_LF, out);
/*     */     }
/* 154 */     writeBytes(TWO_DASHES, out);
/* 155 */     writeBytes(boundary, out);
/* 156 */     writeBytes(TWO_DASHES, out);
/* 157 */     writeBytes(CR_LF, out);
/*     */   }
/*     */ 
/*     */   protected abstract void formatMultipartHeader(FormBodyPart paramFormBodyPart, OutputStream paramOutputStream)
/*     */     throws IOException;
/*     */ 
/*     */   public void writeTo(OutputStream out)
/*     */     throws IOException
/*     */   {
/* 173 */     doWriteTo(out, true);
/*     */   }
/*     */ 
/*     */   public long getTotalLength()
/*     */   {
/* 190 */     long contentLen = 0L;
/* 191 */     for (FormBodyPart part : getBodyParts()) {
/* 192 */       ContentBody body = part.getBody();
/* 193 */       long len = body.getContentLength();
/* 194 */       if (len >= 0L)
/* 195 */         contentLen += len;
/*     */       else {
/* 197 */         return -1L;
/*     */       }
/*     */     }
/* 200 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */     try {
/* 202 */       doWriteTo(out, false);
/* 203 */       byte[] extra = out.toByteArray();
/* 204 */       return contentLen + extra.length;
/*     */     } catch (IOException ex) {
/*     */     }
/* 207 */     return -1L;
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.AbstractMultipartForm
 * JD-Core Version:    0.6.2
 */