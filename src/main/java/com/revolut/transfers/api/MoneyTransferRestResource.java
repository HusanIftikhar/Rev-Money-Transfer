package com.revolut.transfers.api;


import com.google.inject.Inject;
import com.revolut.transfers.dto.AccountDTO;
import com.revolut.transfers.services.TransferService;
import io.vertx.core.json.Json;
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



    }

