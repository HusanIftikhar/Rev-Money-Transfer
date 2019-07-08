package com.revolut.transfers.enums;

public enum TransferStatus {

    SUCCESS(200);
    int code;

    TransferStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
