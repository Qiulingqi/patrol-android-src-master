/*     */ package com.android.volley.httpcore.message;
/*     */ 
/*     */

import com.android.volley.httpcore.Args;
import com.android.volley.httpcore.annotation.Immutable;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.message.HeaderValueFormatter;
import org.apache.http.util.CharArrayBuffer;

/*     */
/*     */
/*     */
/*     */ 
/*     */ @Immutable
/*     */ public class BasicHeaderValueFormatter
/*     */   implements HeaderValueFormatter
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  56 */   public static final BasicHeaderValueFormatter DEFAULT = new BasicHeaderValueFormatter();
/*     */ 
/*  58 */   public static final BasicHeaderValueFormatter INSTANCE = new BasicHeaderValueFormatter();
/*     */   public static final String SEPARATORS = " ;,:@()<>\\\"/[]?={}\t";
/*     */   public static final String UNSAFE_CHARS = "\"\\";
/*     */ 
/*     */   public static String formatElements(HeaderElement[] elems, boolean quote, HeaderValueFormatter formatter)
/*     */   {
/*  92 */     return (formatter != null ? formatter : INSTANCE).formatElements(null, elems, quote).toString();
/*     */   }
/*     */ 
/*     */   public CharArrayBuffer formatElements(CharArrayBuffer charBuffer, HeaderElement[] elems, boolean quote)
/*     */   {
/* 101 */     Args.notNull(elems, "Header element array");
/* 102 */     int len = estimateElementsLen(elems);
/* 103 */     CharArrayBuffer buffer = charBuffer;
/* 104 */     if (buffer == null)
/* 105 */       buffer = new CharArrayBuffer(len);
/*     */     else {
/* 107 */       buffer.ensureCapacity(len);
/*     */     }
/*     */ 
/* 110 */     for (int i = 0; i < elems.length; i++) {
/* 111 */       if (i > 0) {
/* 112 */         buffer.append(", ");
/*     */       }
/* 114 */       formatHeaderElement(buffer, elems[i], quote);
/*     */     }
/*     */ 
/* 117 */     return buffer;
/*     */   }
/*     */ 
/*     */   protected int estimateElementsLen(HeaderElement[] elems)
/*     */   {
/* 129 */     if ((elems == null) || (elems.length < 1)) {
/* 130 */       return 0;
/*     */     }
/*     */ 
/* 133 */     int result = (elems.length - 1) * 2;
/* 134 */     for (HeaderElement elem : elems) {
/* 135 */       result += estimateHeaderElementLen(elem);
/*     */     }
/*     */ 
/* 138 */     return result;
/*     */   }
/*     */ 
/*     */   public static String formatHeaderElement(HeaderElement elem, boolean quote, HeaderValueFormatter formatter)
/*     */   {
/* 158 */     return (formatter != null ? formatter : INSTANCE).formatHeaderElement(null, elem, quote).toString();
/*     */   }
/*     */ 
/*     */   public CharArrayBuffer formatHeaderElement(CharArrayBuffer charBuffer, HeaderElement elem, boolean quote)
/*     */   {
/* 167 */     Args.notNull(elem, "Header element");
/* 168 */     int len = estimateHeaderElementLen(elem);
/* 169 */     CharArrayBuffer buffer = charBuffer;
/* 170 */     if (buffer == null)
/* 171 */       buffer = new CharArrayBuffer(len);
/*     */     else {
/* 173 */       buffer.ensureCapacity(len);
/*     */     }
/*     */ 
/* 176 */     buffer.append(elem.getName());
/* 177 */     String value = elem.getValue();
/* 178 */     if (value != null) {
/* 179 */       buffer.append('=');
/* 180 */       doFormatValue(buffer, value, quote);
/*     */     }
/*     */ 
/* 183 */     int parcnt = elem.getParameterCount();
/* 184 */     if (parcnt > 0) {
/* 185 */       for (int i = 0; i < parcnt; i++) {
/* 186 */         buffer.append("; ");
/* 187 */         formatNameValuePair(buffer, elem.getParameter(i), quote);
/*     */       }
/*     */     }
/*     */ 
/* 191 */     return buffer;
/*     */   }
/*     */ 
/*     */   protected int estimateHeaderElementLen(HeaderElement elem)
/*     */   {
/* 203 */     if (elem == null) {
/* 204 */       return 0;
/*     */     }
/*     */ 
/* 207 */     int result = elem.getName().length();
/* 208 */     String value = elem.getValue();
/* 209 */     if (value != null)
/*     */     {
/* 211 */       result += 3 + value.length();
/*     */     }
/*     */ 
/* 214 */     int parcnt = elem.getParameterCount();
/* 215 */     if (parcnt > 0) {
/* 216 */       for (int i = 0; i < parcnt; i++) {
/* 217 */         result += 2 + estimateNameValuePairLen(elem.getParameter(i));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 222 */     return result;
/*     */   }
/*     */ 
/*     */   public static String formatParameters(NameValuePair[] nvps, boolean quote, HeaderValueFormatter formatter)
/*     */   {
/* 243 */     return (formatter != null ? formatter : INSTANCE).formatParameters(null, nvps, quote).toString();
/*     */   }
/*     */ 
/*     */   public CharArrayBuffer formatParameters(CharArrayBuffer charBuffer, NameValuePair[] nvps, boolean quote)
/*     */   {
/* 252 */     Args.notNull(nvps, "Header parameter array");
/* 253 */     int len = estimateParametersLen(nvps);
/* 254 */     CharArrayBuffer buffer = charBuffer;
/* 255 */     if (buffer == null)
/* 256 */       buffer = new CharArrayBuffer(len);
/*     */     else {
/* 258 */       buffer.ensureCapacity(len);
/*     */     }
/*     */ 
/* 261 */     for (int i = 0; i < nvps.length; i++) {
/* 262 */       if (i > 0) {
/* 263 */         buffer.append("; ");
/*     */       }
/* 265 */       formatNameValuePair(buffer, nvps[i], quote);
/*     */     }
/*     */ 
/* 268 */     return buffer;
/*     */   }
/*     */ 
/*     */   protected int estimateParametersLen(NameValuePair[] nvps)
/*     */   {
/* 280 */     if ((nvps == null) || (nvps.length < 1)) {
/* 281 */       return 0;
/*     */     }
/*     */ 
/* 284 */     int result = (nvps.length - 1) * 2;
/* 285 */     for (NameValuePair nvp : nvps) {
/* 286 */       result += estimateNameValuePairLen(nvp);
/*     */     }
/*     */ 
/* 289 */     return result;
/*     */   }
/*     */ 
/*     */   public static String formatNameValuePair(NameValuePair nvp, boolean quote, HeaderValueFormatter formatter)
/*     */   {
/* 308 */     return (formatter != null ? formatter : INSTANCE).formatNameValuePair(null, nvp, quote).toString();
/*     */   }
/*     */ 
/*     */   public CharArrayBuffer formatNameValuePair(CharArrayBuffer charBuffer, NameValuePair nvp, boolean quote)
/*     */   {
/* 317 */     Args.notNull(nvp, "Name / value pair");
/* 318 */     int len = estimateNameValuePairLen(nvp);
/* 319 */     CharArrayBuffer buffer = charBuffer;
/* 320 */     if (buffer == null)
/* 321 */       buffer = new CharArrayBuffer(len);
/*     */     else {
/* 323 */       buffer.ensureCapacity(len);
/*     */     }
/*     */ 
/* 326 */     buffer.append(nvp.getName());
/* 327 */     String value = nvp.getValue();
/* 328 */     if (value != null) {
/* 329 */       buffer.append('=');
/* 330 */       doFormatValue(buffer, value, quote);
/*     */     }
/*     */ 
/* 333 */     return buffer;
/*     */   }
/*     */ 
/*     */   protected int estimateNameValuePairLen(NameValuePair nvp)
/*     */   {
/* 345 */     if (nvp == null) {
/* 346 */       return 0;
/*     */     }
/*     */ 
/* 349 */     int result = nvp.getName().length();
/* 350 */     String value = nvp.getValue();
/* 351 */     if (value != null)
/*     */     {
/* 353 */       result += 3 + value.length();
/*     */     }
/* 355 */     return result;
/*     */   }
/*     */ 
/*     */   protected void doFormatValue(CharArrayBuffer buffer, String value, boolean quote)
/*     */   {
/* 373 */     boolean quoteFlag = quote;
/* 374 */     if (!quoteFlag) {
/* 375 */       for (int i = 0; (i < value.length()) && (!quoteFlag); i++) {
/* 376 */         quoteFlag = isSeparator(value.charAt(i));
/*     */       }
/*     */     }
/*     */ 
/* 380 */     if (quoteFlag) {
/* 381 */       buffer.append('"');
/*     */     }
/* 383 */     for (int i = 0; i < value.length(); i++) {
/* 384 */       char ch = value.charAt(i);
/* 385 */       if (isUnsafe(ch)) {
/* 386 */         buffer.append('\\');
/*     */       }
/* 388 */       buffer.append(ch);
/*     */     }
/* 390 */     if (quoteFlag)
/* 391 */       buffer.append('"');
/*     */   }
/*     */ 
/*     */   protected boolean isSeparator(char ch)
/*     */   {
/* 405 */     return " ;,:@()<>\\\"/[]?={}\t".indexOf(ch) >= 0;
/*     */   }
/*     */ 
/*     */   protected boolean isUnsafe(char ch)
/*     */   {
/* 418 */     return "\"\\".indexOf(ch) >= 0;
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpcore-4.3.2.jar
 * Qualified Name:     org.apache.http.message.BasicHeaderValueFormatter
 * JD-Core Version:    0.6.2
 */