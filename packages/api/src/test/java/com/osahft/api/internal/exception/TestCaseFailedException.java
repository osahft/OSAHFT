package com.osahft.api.internal.exception;

import lombok.Getter;


@Getter
public class TestCaseFailedException extends RuntimeException {

    public TestCaseFailedException(Throwable t) {
        super(t);
    }

    public TestCaseFailedException() {
        super();
    }
}
