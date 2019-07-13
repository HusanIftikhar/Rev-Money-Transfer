package com.revolut.transfers.api;


import com.google.inject.Inject;
import com.revolut.transfers.dto.AccountDTO;
import com.revolut.transfers.dto.ResponseDTO;
import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.services.TransferService;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static com.revolut.transfers.utils.ApplicationConstants.APPLICATION_JSON;
import static com.revolut.transfers.utils.ApplicationConstants.CONTENT_TYPE;

public class MoneyTransferRestResource {
    @Inject
    private TransferService transferService;


    public void getAccountById(RoutingContext routingContext){


            final long accountId =Long.parseLong(routingContext.request().getParam("accountId"));

                AccountDTO response = AccountDTO.createAccountDTO(transferService.getAccountById(accountId));

                routingContext.response().setStatusCode(200)
                        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .end(Json.encode(response));
    }


    public void transferAmountBetweenAccounts(RoutingContext routingContext) {
        Long sourceAccountId = Long.parseLong(routingContext.request().getParam("sourceAccountId"));
        Long targetAccountId = Long.parseLong(routingContext.request().getParam("targetAccountId"));

        JsonObject requestBody = routingContext.getBodyAsJson();
         transferService.transfer
                (sourceAccountId,targetAccountId,requestBody.getDouble("amount"),requestBody.getString("currencyCode"));
        createResponse("SUCCESS","Balance Transferred Successfully",routingContext);
    }





    public void withdrawal(RoutingContext routingContext) {
        Long accountId = Long.parseLong(routingContext.request().getParam("accountId"));
        JsonObject requestBody = routingContext.getBodyAsJson();
        transferService.withdrawal(accountId,requestBody.getDouble("amount"),requestBody.getString("currencyCode"));
        createResponse("SUCCESS","Withdrawal Successfull",routingContext);
    }
    public void deposit(RoutingContext routingContext) {
        Long accountId = Long.parseLong(routingContext.request().getParam("accountId"));
        JsonObject requestBody = routingContext.getBodyAsJson();
        transferService.deposit(accountId,requestBody.getDouble("amount"),requestBody.getString("currencyCode"));
        createResponse("SUCCESS","Deposit Successfull",routingContext);
    }





    private <T>void createResponse(final T result, final String message ,final RoutingContext routingContext){
        String responseBody =Json.encodePrettily(ResponseDTO.createResponse(result,message, HttpResponseStatus.OK.code()));
        routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE,APPLICATION_JSON).end(responseBody);
    }

}

