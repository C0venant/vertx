package com.test.test.SensorStatisticsExample.sensor;

import com.test.test.SensorStatisticsExample.BusAddressUtils;
import com.test.test.SensorStatisticsExample.entity.ReadData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import java.util.Random;
import java.util.UUID;

public class HeatSensor extends AbstractVerticle {

  private final Random random = new Random();
  private final String sensorId = UUID.randomUUID().toString();
  private double temperature = 21.0;

  @Override
  public void start() {
    scheduleNextUpdate();
  }

  private void scheduleNextUpdate() {
    vertx.setTimer(random.nextInt(5000) + 1000, this::update);
  }

  private void update(long timerId) {
    temperature = temperature + (delta() / 10);
    ReadData readData = new ReadData(sensorId, temperature);
    JsonObject payload = JsonObject.mapFrom(readData);
    //we use event bus to send information
    vertx.eventBus().publish(BusAddressUtils.UPDATES, payload);
    scheduleNextUpdate();
  }

  private double delta() {
    if (random.nextInt() > 0) {
      return random.nextGaussian();
    } else {
      return -random.nextGaussian();
    }
  }
}
