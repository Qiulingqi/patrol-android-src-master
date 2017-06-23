package com.android.volley.httpcore.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface GuardedBy
{
  public abstract String value();
}

/* Location:           C:\Users\Administrator\Desktop\library--\httpcore-4.3.2.jar
 * Qualified Name:     org.apache.http.annotation.GuardedBy
 * JD-Core Version:    0.6.2
 */