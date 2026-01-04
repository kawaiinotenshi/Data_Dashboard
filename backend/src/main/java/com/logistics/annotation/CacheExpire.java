package com.logistics.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheExpire {

    long value() default 60;

    TimeUnit timeUnit() default TimeUnit.MINUTES;

    String key() default "";

    String keyPrefix() default "";
}
