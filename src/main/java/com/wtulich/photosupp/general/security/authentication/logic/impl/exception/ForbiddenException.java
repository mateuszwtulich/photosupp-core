package com.wtulich.photosupp.general.security.authentication.logic.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "You dont have permissions for this action!")
public class ForbiddenException extends RuntimeException{
}
