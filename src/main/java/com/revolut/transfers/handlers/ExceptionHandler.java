package com.revolut.transfers.handlers;

import com.google.common.base.Objects;
import com.revolut.transfers.dto.ResponseDTO;
import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.exceptions.InvalidAmountException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class ExceptionHandler {
    public  void handle(RoutingContext routingContext) {

        Throwable failure = routingContext.failure();
        TransferStatus status = resolveStatusCode(failure);
        HttpServerResponse response = routingContext.response();

        response.setStatusCode(status.getHttpCode()).end(Json.encode(ResponseDTO.createResponse("Failure",failure.getMessage(),status.getCode())));


    }

    public TransferStatus resolveStatusCode(Throwable th){
        TransferStatus status = TransferStatus.GENERAL_FAILURE;
        if(th instanceof AccountNotFoundException){
            status = TransferStatus.NOT_FOUND;
        }else if(th instanceof InvalidAmountException){
            status = TransferStatus.INVALID_AMOUNT_EXCEPTION;
        }


    return status;
    }


}
