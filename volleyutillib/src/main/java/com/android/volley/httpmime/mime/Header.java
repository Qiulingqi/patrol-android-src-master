/*     */ package com.android.volley.httpmime.mime;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Header
/*     */   implements Iterable<MinimalField>
/*     */ {
/*     */   private final List<MinimalField> fields;
/*     */   private final Map<String, List<MinimalField>> fieldMap;
/*     */ 
/*     */   public Header()
/*     */   {
/*  49 */     this.fields = new LinkedList();
/*  50 */     this.fieldMap = new HashMap();
/*     */   }
/*     */ 
/*     */   public void addField(MinimalField field) {
/*  54 */     if (field == null) {
/*  55 */       return;
/*     */     }
/*  57 */     String key = field.getName().toLowerCase(Locale.ENGLISH);
/*  58 */     List values = (List)this.fieldMap.get(key);
/*  59 */     if (values == null) {
/*  60 */       values = new LinkedList();
/*  61 */       this.fieldMap.put(key, values);
/*     */     }
/*  63 */     values.add(field);
/*  64 */     this.fields.add(field);
/*     */   }
/*     */ 
/*     */   public List<MinimalField> getFields() {
/*  68 */     return new ArrayList(this.fields);
/*     */   }
/*     */ 
/*     */   public MinimalField getField(String name) {
/*  72 */     if (name == null) {
/*  73 */       return null;
/*     */     }
/*  75 */     String key = name.toLowerCase(Locale.ENGLISH);
/*  76 */     List list = (List)this.fieldMap.get(key);
/*  77 */     if ((list != null) && (!list.isEmpty())) {
/*  78 */       return (MinimalField)list.get(0);
/*     */     }
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   public List<MinimalField> getFields(String name) {
/*  84 */     if (name == null) {
/*  85 */       return null;
/*     */     }
/*  87 */     String key = name.toLowerCase(Locale.ENGLISH);
/*  88 */     List list = (List)this.fieldMap.get(key);
/*  89 */     if ((list == null) || (list.isEmpty())) {
/*  90 */       return Collections.emptyList();
/*     */     }
/*  92 */     return new ArrayList(list);
/*     */   }
/*     */ 
/*     */   public int removeFields(String name)
/*     */   {
/*  97 */     if (name == null) {
/*  98 */       return 0;
/*     */     }
/* 100 */     String key = name.toLowerCase(Locale.ENGLISH);
/* 101 */     List removed = (List)this.fieldMap.remove(key);
/* 102 */     if ((removed == null) || (removed.isEmpty())) {
/* 103 */       return 0;
/*     */     }
/* 105 */     this.fields.removeAll(removed);
/* 106 */     return removed.size();
/*     */   }
/*     */ 
/*     */   public void setField(MinimalField field) {
/* 110 */     if (field == null) {
/* 111 */       return;
/*     */     }
/* 113 */     String key = field.getName().toLowerCase(Locale.ENGLISH);
/* 114 */     List list = (List)this.fieldMap.get(key);
/* 115 */     if ((list == null) || (list.isEmpty())) {
/* 116 */       addField(field);
/* 117 */       return;
/*     */     }
/* 119 */     list.clear();
/* 120 */     list.add(field);
/* 121 */     int firstOccurrence = -1;
/* 122 */     int index = 0;
/* 123 */     for (Iterator it = this.fields.iterator(); it.hasNext(); index++) {
/* 124 */       MinimalField f = (MinimalField)it.next();
/* 125 */       if (f.getName().equalsIgnoreCase(field.getName())) {
/* 126 */         it.remove();
/* 127 */         if (firstOccurrence == -1) {
/* 128 */           firstOccurrence = index;
/*     */         }
/*     */       }
/*     */     }
/* 132 */     this.fields.add(firstOccurrence, field);
/*     */   }
/*     */ 
/*     */   public Iterator<MinimalField> iterator() {
/* 136 */     return Collections.unmodifiableList(this.fields).iterator();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 141 */     return this.fields.toString();
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\library--\httpmime-4.3.5.jar
 * Qualified Name:     org.apache.http.entity.mime.Header
 * JD-Core Version:    0.6.2
 */