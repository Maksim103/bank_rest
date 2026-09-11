package com.example.bankcards.exception.blockRequest;

public class BlockRequestAlreadyExistsException extends RuntimeException {
    public BlockRequestAlreadyExistsException() {
        super();
    }

    public BlockRequestAlreadyExistsException(String message) {
        super(message);
    }
}
