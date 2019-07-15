package com.revolut.transfers.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.revolut.transfers.repositories.AccountRepositoryImpl;
import com.revolut.transfers.services.TransferService;
import com.revolut.transfers.repositories.AccountRepository;
import com.revolut.transfers.services.TransferServiceImpl;
import com.revolut.transfers.utils.AccountsDatabase;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Properties;
/**
 *
 * Configuration for google quice for DI
 *
 *
 *
 * */
public class ApplicationConfig extends AbstractModule {


    private final Context context;

     ApplicationConfig(Vertx vertx){
        this.context = vertx.getOrCreateContext();

    }


    @Override
    protected void configure() {

        Names.bindProperties(binder(),extractPropertiesFromVertxConfig(context.config()));
        bind(TransferService.class).to(TransferServiceImpl.class).in(Singleton.class);
        bind(AccountRepository.class).to(AccountRepositoryImpl.class).in(Singleton.class);

    }

    private Properties extractPropertiesFromVertxConfig(JsonObject vertexConfig) {

        Properties properties = new Properties();
        vertexConfig.getMap().keySet().stream().filter(key->key.contains("vertx")).forEach(key->
            properties.setProperty(key,(String) vertexConfig.getValue(key)));
            return properties;
    }

    @Provides
    public AccountsDatabase bankDatabase(){

         return AccountsDatabase.getInstance();
    }


}
