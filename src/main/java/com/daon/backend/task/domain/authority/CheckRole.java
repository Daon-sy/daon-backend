package com.daon.backend.task.domain.authority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {

    Authority[] authority();

    MembershipType membership();

    enum MembershipType {
        WORKSPACE,
        PROJECT,
    }
}
