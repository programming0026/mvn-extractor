package com.juniverse.extractor.exception;

import java.io.Serial;

public class ExtractorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2614749875840355488L;

	public ExtractorException(String message) {
        super(message);
    }

    public ExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtractorException(Throwable cause) {
        super(cause);
    }
}
