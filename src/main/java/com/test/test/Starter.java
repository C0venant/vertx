package com.test.test;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;

public class Starter {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
                new DropwizardMetricsOptions().setEnabled(true)));
        vertx.deployVerticle(new MainVerticle());
    }
}
