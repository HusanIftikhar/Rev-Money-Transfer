package com.revolut.transfers.anotations;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@ExtendWith(EnabledIfReachableCondition.class)
@interface EnabledIfReachable {

    String host();
    int port();
    int timeoutMillis();

}