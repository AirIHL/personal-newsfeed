package com.example.personalnewsfeed.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)           // 어디가서 붙을래?
@Retention(RetentionPolicy.RUNTIME)     // 언제까지 살아있을래?
public @interface Auth {

}
