package com.revolut.transfers.com.revolut.transfer.config;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.revolut.transfers.com.revolut.transfer.controllers.MoneyTransferRestResource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;


public class MoneyTransferApiLauncher extends AbstractVerticle {
    @Inject
    private MoneyTransferRestResource moneyTransferRestResource;

    @Inject
    @Named("http.port")
    private String port;

public static void main(String[] args) {


        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new MoneyTransferApiLauncher());

    }

    public  void start(){

        Guice.createInjector(new ApplicationConfig(vertx)).injectMembers(this);

        Router apiRouter = Router.router(vertx);

        apiRouter.get("/account/:id").handler(
                moneyTransferRestResource::getAccountById);

        vertx.createHttpServer().requestHandler(apiRouter::accept).listen(Integer.valueOf(port));


    }


}
