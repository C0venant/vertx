package com.test.test;

import com.test.test.SensorStatisticsExample.REST.Server;
import com.test.test.SensorStatisticsExample.metrics.MetricVerticle;

import com.test.test.SensorStatisticsExample.sensor.HeatSensor;
import com.test.test.SensorStatisticsExample.storage.SensorData;
import com.test.test.SensorStatisticsExample.storage.UserData;
import io.vertx.core.*;

import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MainVerticle extends AbstractVerticle {

    private static final String VERTILCE_PREFIX = "java:";


    private final Logger logger = LogManager.getLogger(MainVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.deployVerticle(VERTILCE_PREFIX + HeatSensor.class.getCanonicalName());
        vertx.deployVerticle(VERTILCE_PREFIX + SensorData.class.getCanonicalName());
        vertx.deployVerticle(VERTILCE_PREFIX + Server.class.getCanonicalName());
        vertx.deployVerticle(VERTILCE_PREFIX + UserData.class.getCanonicalName());
        vertx.deployVerticle(VERTILCE_PREFIX + MetricVerticle.class.getCanonicalName(), new DeploymentOptions());

    //vertx.deployVerticle(VERTILCE_PREFIX + RequestHandler.class.getCanonicalName());

        logger.info("all the verticles have been deployed");


    }
}
