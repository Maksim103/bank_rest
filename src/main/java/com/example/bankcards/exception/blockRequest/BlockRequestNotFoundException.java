package com.example.bankcards.exception.blockRequest;

public class BlockRequestNotFoundException extends RuntimeException {
    public BlockRequestNotFoundException() {
    }

    public BlockRequestNotFoundException(String message) {
        super(message);
    }
}
