package com.revolut.transfers.handlers;

import com.google.common.base.Objects;
import com.revolut.transfers.dto.ResponseDTO;
import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.exceptions.InvalidAmountException;
import com.revolut.transfers.exceptions.SameAccountTransferRequestException;
import com.revolut.transfers.exceptions.UnSufficientFundException;
import com.revolut.transfers.utils.ApplicationConstants;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import javax.money.MonetaryException;
/**
 *
 * catches all thrown or other exception from the application
 * check the type of exception and create the response and
 * send it back to client
 *
 * @see com.revolut.transfers.handlers.ExceptionHandler#handle(io.vertx.ext.web.RoutingContext)
 * also
 * @see com.revolut.transfers.handlers.ExceptionHandler#resolveStatusCode(java.lang.Throwable)
 * also
 * @see com.revolut.transfers.handlers.ExceptionHandler#createResponse(java.lang.Throwable,
 * com.revolut.transfers.enums.TransferStatus)
 *
 */

public class ExceptionHandler {
    public  void handle(RoutingContext routingContext) {

        Throwable failure = routingContext.failure();
        TransferStatus status = resolveStatusCode(failure);
        HttpServerResponse response = routingContext.response();

        response.putHeader(HttpHeaders.CONTENT_TYPE, ApplicationConstants.APPLICATION_JSON)
                .setStatusCode(status.getHttpCode()).end(createResponse(failure, status));


    }


    /**
     *
     * @param th
     * check the throwable instance and identifies the exception
     *
     * @return @{@link TransferStatus} which contains the code and
     * http code to deliver to client
     */
    private TransferStatus resolveStatusCode(Throwable th){
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

    /**
     * creates the response using thrown exception message and TransferStatus code
     * in the body of response as {@link ResponseDTO}
     * also
     * @see ResponseDTO#createResponse(Object, String, int)
     */

    private String createResponse(Throwable failure, TransferStatus status) {
        return Json.encodePrettily(ResponseDTO
                .createResponse("Failure",failure.getMessage(),status.getCode()));
    }
}
