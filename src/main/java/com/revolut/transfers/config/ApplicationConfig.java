package com.revolut.transfers.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.revolut.transfers.services.TransferService;
import com.revolut.transfers.services.TransferServiceImpl;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Properties;

public class ApplicationConfig extends AbstractModule {

    private final Vertx vertx;
    private final Context context;
     ApplicationConfig(Vertx vertx){
        this.vertx = vertx;
        this.context = vertx.getOrCreateContext();

    }

    @Override
    protected void configure() {

        Names.bindProperties(binder(),extractPropertiesFromVertxConfig(context.config()));
        bind(TransferService.class).to(TransferServiceImpl.class);
    }

    private Properties extractPropertiesFromVertxConfig(JsonObject vertexConfig) {

        Properties properties = new Properties();
        vertexConfig.getMap().keySet().stream().filter(key->key.contains("vertx")).forEach(key->
            properties.setProperty(key,(String) vertexConfig.getValue(key)));
            return properties;
    }
}
