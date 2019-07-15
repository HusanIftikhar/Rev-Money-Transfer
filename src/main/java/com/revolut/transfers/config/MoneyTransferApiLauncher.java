package com.revolut.transfers.config;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.revolut.transfers.api.MoneyTransferRestResource;
import com.revolut.transfers.handlers.ExceptionHandler;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import static com.revolut.transfers.utils.ApplicationConstants.*;

public class MoneyTransferApiLauncher extends AbstractVerticle {


    @Inject
    private MoneyTransferRestResource moneyTransferRestResource;
    @Inject
    private ExceptionHandler exceptionHandler;

    private final  int port=8089;

    public static void main(String[] args) {


        Vertx vertx = Vertx.vertx();
        ConfigRetriever configRetriv = ConfigRetriever.create(vertx);
        configRetriv.getConfig(handler -> {


            if (handler.succeeded()) {

                JsonObject configResult = handler.result();
                DeploymentOptions options = new DeploymentOptions().setConfig(configResult);
                vertx.deployVerticle(new MoneyTransferApiLauncher(), options);
            }

        });


    }

    public void start() {

        Guice.createInjector(new ApplicationConfig(vertx)).injectMembers(this);
        Router apiRouter = prepareEndPoints();
        vertx.createHttpServer().requestHandler(apiRouter::accept).listen(port);

    }


    private Router prepareEndPoints() {
        Router apiRouter = Router.router(vertx);
        apiRouter.route().handler(BodyHandler.create()).failureHandler(exceptionHandler::handle);
        apiRouter.get(GET_ACCOUNT_SERVICE_END_POINT).handler(moneyTransferRestResource::getAccountById);
        apiRouter.put(ACCOUNT_WITHDRAWAL_SERVICE_END_POINT).handler(moneyTransferRestResource::withdrawal);
        apiRouter.put(ACCOUNT_DEPOSIT_SERVICE_END_POINT).handler(moneyTransferRestResource::deposit);
        apiRouter.get(GET_ACCOUNT_HISTORY_SERVICE_END_POINT).handler(moneyTransferRestResource::getTransactionHistory);
        apiRouter.put(ACCOUNT_TRANSFER_SERVICE_END_POINT).handler(moneyTransferRestResource::transferAmountBetweenAccounts);
        return apiRouter;
    }


}
