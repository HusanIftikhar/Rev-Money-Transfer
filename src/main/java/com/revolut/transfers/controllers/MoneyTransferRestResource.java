package com.revolut.transfers.controllers;


import com.google.inject.Inject;
import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.services.TransferService;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class MoneyTransferRestResource {
    @Inject
    private TransferService transferService;


    public void getAccountById(RoutingContext routingContext){


            final long accountId =Long.parseLong(routingContext.request().getParam("id"));
            System.out.println("inside ");

            routingContext.response().setStatusCode(TransferStatus.SUCCESS.getCode())
        .putHeader("content-type","application/json").end(Json.encode(
                transferService.getAccountById(accountId)));





        }

    }

