package com.brix.Seller_Sync.lwa.exception;

public class LWAException extends Exception {

    private static final long serialVersionUID = 1L;

    private String errorMessage;

    private String errorCode;

    public LWAException() {
        super();
    }

    public LWAException(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public LWAException(String errorCode, String errorMessage, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public LWAException(String errorCode, String errorMessage, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public LWAException(String errorCode, String errorMessage, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

}
