package com.wtulich.photosupp.general.utils.annotations;

import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionRestrict {
    ApplicationPermissions[] permissions();
}

