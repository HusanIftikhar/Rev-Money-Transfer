package com.revolut.transfers.config;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.revolut.transfers.api.MoneyTransferRestResource;
import com.revolut.transfers.handlers.ExceptionHandler;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;


public class MoneyTransferApiLauncher extends AbstractVerticle {
    @Inject
    private MoneyTransferRestResource moneyTransferRestResource;
    @Inject
    private ExceptionHandler exceptionHandler;

    @Inject
    @Named("vertx.http.port")
    private String port;

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
//TODO: refactor start method
    public void start() {

        Guice.createInjector(new ApplicationConfig(vertx)).injectMembers(this);

        Router apiRouter = Router.router(vertx);
        apiRouter.route().handler(BodyHandler.create()).failureHandler(exceptionHandler::handle);
        apiRouter.get("/transactions/:accountId/history").handler(moneyTransferRestResource::getTransactionHistory);
        apiRouter.get("'/transactions/:accountId").handler(moneyTransferRestResource::getAccountById);
        apiRouter.put("/transactions/:sourceAccountId/:targetAccountId/transfers").handler(moneyTransferRestResource::transferAmountBetweenAccounts);
        apiRouter.put("/transactions/:accountId/withdrawals").handler(moneyTransferRestResource::withdrawal);
        apiRouter.put("/transactions/:accountId/deposits").handler(moneyTransferRestResource::deposit);
        vertx.createHttpServer().requestHandler(apiRouter::accept).listen(Integer.parseInt(port));

    }




}
