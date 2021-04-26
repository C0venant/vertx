package com.test.test.SensorStatisticsExample.metrics;

import com.test.test.SensorStatisticsExample.BusAddressUtils;
import io.vertx.core.AbstractVerticle;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;

import java.util.Set;


public class MetricVerticle extends AbstractVerticle {

    private MetricsService metricsService;

    @Override
    public void start(){
/*
        vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
                new DropwizardMetricsOptions().setEnabled(true)));

 */
        metricsService = MetricsService.create(vertx);



        EventBus bus = vertx.eventBus();

        bus.consumer(BusAddressUtils.METRICS, this::getMetric);

        /*
        Set<String> metricsNames = metricsService.metricsNames();
        for (String metricsName : metricsNames) {
            System.out.println(metricsName);
        }
         */

    }

    private void getMetric(Message<JsonObject> message){
        JsonObject metricInfo = metricsService.getMetricsSnapshot("vertx.eventbus.messages.sent-local");
        message.reply(metricInfo);
    }



}
