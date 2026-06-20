package com.sohamtanpathak.fintech.payment_processing_system.common.exceptions;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException{

    private final String errorCode;

    public DuplicateResourceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
