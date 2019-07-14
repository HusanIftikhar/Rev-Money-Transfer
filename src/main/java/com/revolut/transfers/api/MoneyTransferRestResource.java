package com.revolut.transfers.api;


import com.google.inject.Inject;
import com.revolut.transfers.dto.AccountDTO;
import com.revolut.transfers.dto.ResponseDTO;
import com.revolut.transfers.dto.TransactionHistoryDTO;
import com.revolut.transfers.model.Transaction;
import com.revolut.transfers.services.TransferService;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.stream.Collectors;

import static com.revolut.transfers.utils.ApplicationConstants.APPLICATION_JSON;

public class MoneyTransferRestResource {
    @Inject
    private TransferService transferService;


    public void getAccountById(RoutingContext routingContext) {


        final long accountId = Long.parseLong(routingContext.request().getParam("accountId"));

        AccountDTO account = AccountDTO.createAccountDTO(transferService.getAccountById(accountId));
        createResponse(account, "Account Found", routingContext);

    }


    public void transferAmountBetweenAccounts(RoutingContext routingContext) {
        Long sourceAccountId = Long.parseLong(routingContext.request().getParam("sourceAccountId"));
        Long targetAccountId = Long.parseLong(routingContext.request().getParam("targetAccountId"));

        JsonObject requestBody = routingContext.getBodyAsJson();
        transferService.transfer
                (sourceAccountId, targetAccountId, requestBody.getDouble("amount"), requestBody.getString("currencyCode"));
        createResponse("SUCCESS", "Balance Transferred Successfully", routingContext);
    }

    public void withdrawal(RoutingContext routingContext) {
        Long accountId = Long.parseLong(routingContext.request().getParam("accountId"));
        JsonObject requestBody = routingContext.getBodyAsJson();
        transferService.withdrawal(accountId, requestBody.getDouble("amount"), requestBody.getString("currencyCode"));
        createResponse("SUCCESS", "Withdrawal Successful", routingContext);
    }

    public void deposit(RoutingContext routingContext) {
        Long accountId = Long.parseLong(routingContext.request().getParam("accountId"));
        JsonObject requestBody = routingContext.getBodyAsJson();
        transferService.deposit(accountId, requestBody.getDouble("amount"),
                requestBody.getString("currencyCode"));
        createResponse("SUCCESS", "Deposit successful", routingContext);
    }


    public void getTransactionHistory(RoutingContext routingContext) {

        Long accountId = Long.parseLong(routingContext.request().getParam("accountId"));
        List<Transaction> transactions = transferService.getAccountById(accountId).getTransactionHistory();
        createResponse(transactions.stream().map(TransactionHistoryDTO::new).collect(Collectors.toList()),
                "Total Transactions: " + transactions.size(),
                routingContext);

    }


    private <T> void createResponse(final T result, final String message, final RoutingContext routingContext) {
        String responseBody = Json.encodePrettily(ResponseDTO.createResponse(result, message, HttpResponseStatus.OK.code()));
        routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                .putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .end(responseBody);
    }

}

