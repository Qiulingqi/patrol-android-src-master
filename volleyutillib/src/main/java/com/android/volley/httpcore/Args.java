/*     */ package com.android.volley.httpcore;
/*     */ 
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class Args
/*     */ {
/*     */   public static void check(boolean expression, String message)
/*     */   {
/*  35 */     if (!expression)
/*  36 */       throw new IllegalArgumentException(message);
/*     */   }
/*     */ 
/*     */   public static void check(boolean expression, String message, Object[] args)
/*     */   {
/*  41 */     if (!expression)
/*  42 */       throw new IllegalArgumentException(String.format(message, args));
/*     */   }
/*     */ 
/*     */   public static <T> T notNull(T argument, String name)
/*     */   {
/*  47 */     if (argument == null) {
/*  48 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*  50 */     return argument;
/*     */   }
/*     */ 
/*     */   public static <T extends CharSequence> T notEmpty(T argument, String name) {
/*  54 */     if (argument == null) {
/*  55 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*  57 */     if (TextUtils.isEmpty(argument)) {
/*  58 */       throw new IllegalArgumentException(name + " may not be empty");
/*     */     }
/*  60 */     return argument;
/*     */   }
/*     */ 
/*     */   public static <T extends CharSequence> T notBlank(T argument, String name) {
/*  64 */     if (argument == null) {
/*  65 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*  67 */     if (TextUtils.isBlank(argument)) {
/*  68 */       throw new IllegalArgumentException(name + " may not be blank");
/*     */     }
/*  70 */     return argument;
/*     */   }
/*     */ 
/*     */   public static <E, T extends Collection<E>> T notEmpty(T argument, String name) {
/*  74 */     if (argument == null) {
/*  75 */       throw new IllegalArgumentException(name + " may not be null");
/*     */     }
/*  77 */     if (argument.isEmpty()) {
/*  78 */       throw new IllegalArgumentException(name + " may not be empty");
/*     */     }
/*  80 */     return argument;
/*     */   }
/*     */ 
/*     */   public static int positive(int n, String name) {
/*  84 */     if (n <= 0) {
/*  85 */       throw new IllegalArgumentException(name + " may not be negative or zero");
/*     */     }
/*  87 */     return n;
/*     */   }
/*     */ 
/*     */   public static long positive(long n, String name) {
/*  91 */     if (n <= 0L) {
/*  92 */       throw new IllegalArgumentException(name + " may not be negative or zero");
/*     */     }
/*  94 */     return n;
/*     */   }
/*     */ 
/*     */   public static int notNegative(int n, String name) {
/*  98 */     if (n < 0) {
/*  99 */       throw new IllegalArgumentException(name + " may not be negative");
/*     */     }
/* 101 */     return n;
/*     */   }
/*     */ 
/*     */   public static long notNegative(long n, String name) {
/* 105 */     if (n < 0L) {
/* 106 */       throw new IllegalArgumentException(name + " may not be negative");
/*     */     }
/* 108 */     return n;
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpcore-4.3.2.jar
 * Qualified Name:     org.apache.http.util.Args
 * JD-Core Version:    0.6.2
 */