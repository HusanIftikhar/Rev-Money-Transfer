package com.revolut.transfers.enums;

public enum TransferStatus {

    SUCCESS(200 , 200) , NOT_FOUND(404 , 404) , GENERAL_FAILURE(500 , 500),
    SAME_ACCOUNT_ERROR(90001, 400) , UNSUFFIECIENT_FUND_ERROR(90002,400),
    INVALID_CURRENCY_CODE(90003,400) , INVALID_AMOUNT_EXCEPTION(9004,400) , WRONG_ACCOUNT_OR_AMOUNT_FORMAT(400 , 400);

    ;

    int code; // this is custom codes for different exception that will be available in response body
    int httpCode; // HTTP ERROR CODES FOR API
    TransferStatus(int code, int httpCode)  {
        this.code = code;
        this.httpCode=httpCode;
    }

    public int getCode() {
        return code;
    }

    public int getHttpCode(){
        return httpCode;
    }
}
