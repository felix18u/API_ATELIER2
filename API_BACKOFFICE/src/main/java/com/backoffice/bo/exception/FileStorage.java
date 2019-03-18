package com.backoffice.bo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FileStorage extends RuntimeException {

    private static final long serialVersionUID = 12344567890L;

    public FileStorage(String message) {
        super(message);
    }

    public FileStorage(String message, Throwable cause) {
        super(message, cause);
    }

}