package com.revolut.transfers.handlers;

import com.google.common.base.Objects;
import com.revolut.transfers.dto.ResponseDTO;
import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.exceptions.InvalidAmountException;
import com.revolut.transfers.exceptions.SameAccountTransferRequestException;
import com.revolut.transfers.exceptions.UnSufficientFundException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import javax.money.MonetaryException;

public class ExceptionHandler {
    public  void handle(RoutingContext routingContext) {

        Throwable failure = routingContext.failure();
        TransferStatus status = resolveStatusCode(failure);
        HttpServerResponse response = routingContext.response();

        response.setStatusCode(status.getHttpCode()).end(createResponse(failure, status));


    }



    public TransferStatus resolveStatusCode(Throwable th){
        TransferStatus status ;
        if(th instanceof AccountNotFoundException){
            status = TransferStatus.NOT_FOUND;
        }else if(th instanceof InvalidAmountException){
            status = TransferStatus.INVALID_AMOUNT_EXCEPTION;
        } else if(th instanceof NumberFormatException ){
            status = TransferStatus.WRONG_ACCOUNT_OR_AMOUNT_FORMAT;

        }else if(th instanceof MonetaryException){
            status = TransferStatus.INVALID_CURRENCY_CODE;

        }else if(th instanceof SameAccountTransferRequestException){
            status = TransferStatus.SAME_ACCOUNT_ERROR;
        }else if(th instanceof UnSufficientFundException){
            status = TransferStatus.UNSUFFIECIENT_FUND_ERROR;
        } else{
            status = TransferStatus.GENERAL_FAILURE;
        }


    return status;
    }



    private String createResponse(Throwable failure, TransferStatus status) {
        return Json.encodePrettily(ResponseDTO
                .createResponse("Failure",failure.getMessage(),status.getCode()));
    }
}
