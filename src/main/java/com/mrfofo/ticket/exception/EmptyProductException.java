package com.mrfofo.ticket.exception;

public class EmptyProductException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public EmptyProductException(String message) {
        super(message);
    }
}