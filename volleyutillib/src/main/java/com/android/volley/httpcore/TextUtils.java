/*    */ package com.android.volley.httpcore;
/*    */ 
/*    */ public final class TextUtils
/*    */ {
/*    */   public static boolean isEmpty(CharSequence s)
/*    */   {
/* 36 */     if (s == null) {
/* 37 */       return true;
/*    */     }
/* 39 */     return s.length() == 0;
/*    */   }
/*    */ 
/*    */   public static boolean isBlank(CharSequence s) {
/* 43 */     if (s == null) {
/* 44 */       return true;
/*    */     }
/* 46 */     for (int i = 0; i < s.length(); i++) {
/* 47 */       if (!Character.isWhitespace(s.charAt(i))) {
/* 48 */         return false;
/*    */       }
/*    */     }
/* 51 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpcore-4.3.2.jar
 * Qualified Name:     org.apache.http.util.TextUtils
 * JD-Core Version:    0.6.2
 */