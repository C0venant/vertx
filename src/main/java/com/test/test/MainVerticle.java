package com.test.test;


import com.test.test.SensorStatisticsExample.requestHandling.RequestHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  private static final String VERTILCE_PREFIX = "java:";

  @Override
  public void start(Promise<Void> startPromise) {
    /*
    vertx.deployVerticle(VERTILCE_PREFIX + HeatSensor.class.getCanonicalName());
    vertx.deployVerticle(VERTILCE_PREFIX + SensorData.class.getCanonicalName());
    vertx.deployVerticle(VERTILCE_PREFIX + Server.class.getCanonicalName());
    vertx.deployVerticle(VERTILCE_PREFIX + UserData.class.getCanonicalName());
     */
    vertx.deployVerticle(VERTILCE_PREFIX + RequestHandler.class.getCanonicalName());
  }
}
