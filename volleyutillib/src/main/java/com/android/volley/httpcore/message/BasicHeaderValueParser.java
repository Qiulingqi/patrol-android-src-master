/*     */ package com.android.volley.httpcore.message;
/*     */
/*     */

import com.android.volley.httpcore.Args;
import com.android.volley.httpcore.annotation.Immutable;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;

import java.util.ArrayList;
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
/*     */ @Immutable
/*     */ public class BasicHeaderValueParser
/*     */   implements HeaderValueParser
/*     */ {
/*     */
/*     */   @Deprecated
/*  61 */   public static final BasicHeaderValueParser DEFAULT = new BasicHeaderValueParser();
/*     */
/*  63 */   public static final BasicHeaderValueParser INSTANCE = new BasicHeaderValueParser();
/*     */   private static final char PARAM_DELIMITER = ';';
/*     */   private static final char ELEM_DELIMITER = ',';
/*  67 */   private static final char[] ALL_DELIMITERS = { ';', ',' };
/*     */
/*     */   public static HeaderElement[] parseElements(String value, HeaderValueParser parser)
/*     */     throws ParseException
/*     */   {
/*  87 */     Args.notNull(value, "Value");
/*     */
/*  89 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/*  90 */     buffer.append(value);
/*  91 */     ParserCursor cursor = new ParserCursor(0, value.length());
/*  92 */     return (parser != null ? parser : INSTANCE).parseElements(buffer, cursor);
/*     */   }
/*     */
/*     */   public HeaderElement[] parseElements(CharArrayBuffer buffer, ParserCursor cursor)
/*     */   {
/* 100 */     Args.notNull(buffer, "Char array buffer");
/* 101 */     Args.notNull(cursor, "Parser cursor");
/* 102 */     List elements = new ArrayList();
/* 103 */     while (!cursor.atEnd()) {
/* 104 */       HeaderElement element = parseHeaderElement(buffer, cursor);
/* 105 */       if ((element.getName().length() != 0) || (element.getValue() != null)) {
/* 106 */         elements.add(element);
/*     */       }
/*     */     }
/* 109 */     return (HeaderElement[])elements.toArray(new HeaderElement[elements.size()]);
/*     */   }
/*     */
/*     */   public static HeaderElement parseHeaderElement(String value, HeaderValueParser parser)
/*     */     throws ParseException
/*     */   {
/* 124 */     Args.notNull(value, "Value");
/*     */
/* 126 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 127 */     buffer.append(value);
/* 128 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 129 */     return (parser != null ? parser : INSTANCE).parseHeaderElement(buffer, cursor);
/*     */   }
/*     */
/*     */   public HeaderElement parseHeaderElement(CharArrayBuffer buffer, ParserCursor cursor)
/*     */   {
/* 137 */     Args.notNull(buffer, "Char array buffer");
/* 138 */     Args.notNull(cursor, "Parser cursor");
/* 139 */     NameValuePair nvp = parseNameValuePair(buffer, cursor);
/* 140 */     NameValuePair[] params = null;
/* 141 */     if (!cursor.atEnd()) {
/* 142 */       char ch = buffer.charAt(cursor.getPos() - 1);
/* 143 */       if (ch != ',') {
/* 144 */         params = parseParameters(buffer, cursor);
/*     */       }
/*     */     }
/* 147 */     return createHeaderElement(nvp.getName(), nvp.getValue(), params);
/*     */   }
/*     */
/*     */   protected HeaderElement createHeaderElement(String name, String value, NameValuePair[] params)
/*     */   {
/* 161 */     return new BasicHeaderElement(name, value, params);
/*     */   }
/*     */
/*     */   public static NameValuePair[] parseParameters(String value, HeaderValueParser parser)
/*     */     throws ParseException
/*     */   {
/* 176 */     Args.notNull(value, "Value");
/*     */
/* 178 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 179 */     buffer.append(value);
/* 180 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 181 */     return (parser != null ? parser : INSTANCE).parseParameters(buffer, cursor);
/*     */   }
/*     */
/*     */   public NameValuePair[] parseParameters(CharArrayBuffer buffer, ParserCursor cursor)
/*     */   {
/* 190 */     Args.notNull(buffer, "Char array buffer");
/* 191 */     Args.notNull(cursor, "Parser cursor");
/* 192 */     int pos = cursor.getPos();
/* 193 */     int indexTo = cursor.getUpperBound();
/*     */
/* 195 */     while (pos < indexTo) {
/* 196 */       char ch = buffer.charAt(pos);
/* 197 */       if (!HTTP.isWhitespace(ch)) break;
/* 198 */       pos++;
/*     */     }
/*     */
/* 203 */     cursor.updatePos(pos);
/* 204 */     if (cursor.atEnd()) {
/* 205 */       return new NameValuePair[0];
/*     */     }
/*     */
/* 208 */     List params = new ArrayList();
/* 209 */     while (!cursor.atEnd()) {
/* 210 */       NameValuePair param = parseNameValuePair(buffer, cursor);
/* 211 */       params.add(param);
/* 212 */       char ch = buffer.charAt(cursor.getPos() - 1);
/* 213 */       if (ch == ',')
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 218 */     return (NameValuePair[])params.toArray(new NameValuePair[params.size()]);
/*     */   }
/*     */
/*     */   public static NameValuePair parseNameValuePair(String value, HeaderValueParser parser)
/*     */     throws ParseException
/*     */   {
/* 232 */     Args.notNull(value, "Value");
/*     */
/* 234 */     CharArrayBuffer buffer = new CharArrayBuffer(value.length());
/* 235 */     buffer.append(value);
/* 236 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 237 */     return (parser != null ? parser : INSTANCE).parseNameValuePair(buffer, cursor);
/*     */   }
/*     */
/*     */   public NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor)
/*     */   {
/* 245 */     return parseNameValuePair(buffer, cursor, ALL_DELIMITERS);
/*     */   }
/*     */
/*     */   private static boolean isOneOf(char ch, char[] chs) {
/* 249 */     if (chs != null) {
/* 250 */       for (char ch2 : chs) {
/* 251 */         if (ch == ch2) {
/* 252 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 256 */     return false;
/*     */   }
/*     */
/*     */   public NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor, char[] delimiters)
/*     */   {
/* 262 */     Args.notNull(buffer, "Char array buffer");
/* 263 */     Args.notNull(cursor, "Parser cursor");
/*     */
/* 265 */     boolean terminated = false;
/*     */
/* 267 */     int pos = cursor.getPos();
/* 268 */     int indexFrom = cursor.getPos();
/* 269 */     int indexTo = cursor.getUpperBound();
/*     */
/* 273 */     while (pos < indexTo) {
/* 274 */       char ch = buffer.charAt(pos);
/* 275 */       if (ch == '=') {
/*     */         break;
/*     */       }
/* 278 */       if (isOneOf(ch, delimiters)) {
/* 279 */         terminated = true;
/* 280 */         break;
/*     */       }
/* 282 */       pos++;
/*     */     }
/*     */     String name;
/* 285 */     if (pos == indexTo) {
/* 286 */       terminated = true;
/* 287 */       name = buffer.substringTrimmed(indexFrom, indexTo);
/*     */     } else {
/* 289 */       name = buffer.substringTrimmed(indexFrom, pos);
/* 290 */       pos++;
/*     */     }
/*     */
/* 293 */     if (terminated) {
/* 294 */       cursor.updatePos(pos);
/* 295 */       return createNameValuePair(name, null);
/*     */     }
/*     */
/* 300 */     int i1 = pos;
/*     */
/* 302 */     boolean qouted = false;
/* 303 */     boolean escaped = false;
/* 304 */     while (pos < indexTo) {
/* 305 */       char ch = buffer.charAt(pos);
/* 306 */       if ((ch == '"') && (!escaped)) {
/* 307 */         qouted = !qouted;
/*     */       }
/* 309 */       if ((!qouted) && (!escaped) && (isOneOf(ch, delimiters))) {
/* 310 */         terminated = true;
/* 311 */         break;
/*     */       }
/* 313 */       if (escaped)
/* 314 */         escaped = false;
/*     */       else {
/* 316 */         escaped = (qouted) && (ch == '\\');
/*     */       }
/* 318 */       pos++;
/*     */     }
/*     */
/* 321 */     int i2 = pos;
/*     */
/* 323 */     while ((i1 < i2) && (HTTP.isWhitespace(buffer.charAt(i1)))) {
/* 324 */       i1++;
/*     */     }
/*     */
/* 327 */     while ((i2 > i1) && (HTTP.isWhitespace(buffer.charAt(i2 - 1)))) {
/* 328 */       i2--;
/*     */     }
/*     */
/* 331 */     if ((i2 - i1 >= 2) && (buffer.charAt(i1) == '"') && (buffer.charAt(i2 - 1) == '"'))
/*     */     {
/* 334 */       i1++;
/* 335 */       i2--;
/*     */     }
/* 337 */     String value = buffer.substring(i1, i2);
/* 338 */     if (terminated) {
/* 339 */       pos++;
/*     */     }
/* 341 */     cursor.updatePos(pos);
/* 342 */     return createNameValuePair(name, value);
/*     */   }
/*     */
/*     */   protected NameValuePair createNameValuePair(String name, String value)
/*     */   {
/* 355 */     return new BasicNameValuePair(name, value);
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpcore-4.3.2.jar
 * Qualified Name:     org.apache.http.message.BasicHeaderValueParser
 * JD-Core Version:    0.6.2
 */