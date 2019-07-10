package com.revolut.transfers.config;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.revolut.transfers.controllers.MoneyTransferRestResource;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;


public class MoneyTransferApiLauncher extends AbstractVerticle {
    @Inject
    private MoneyTransferRestResource moneyTransferRestResource;

   @Inject
    @Named("vertx.http.port")
    private String port;

public static void main(String[] args) {


        Vertx vertx = Vertx.vertx();
    ConfigRetriever configRetriv = ConfigRetriever.create(vertx);
    configRetriv.getConfig(handler->{


        if(handler.succeeded()) {

            JsonObject configResult = handler.result();
            DeploymentOptions options = new DeploymentOptions().setConfig(configResult);
            vertx.deployVerticle(new MoneyTransferApiLauncher(),options);
        }

    });



    }

    public  void start(){

        Guice.createInjector(new ApplicationConfig(vertx)).injectMembers(this);

        Router apiRouter = Router.router(vertx);

        apiRouter.get("/account/:id").handler(
                moneyTransferRestResource::getAccountById);

        vertx.createHttpServer().requestHandler(apiRouter::accept).listen(Integer.parseInt(port));


    }


}
