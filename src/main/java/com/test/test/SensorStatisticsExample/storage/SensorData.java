package com.test.test.SensorStatisticsExample.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.test.test.SensorStatisticsExample.BusAddressUtils;
import com.test.test.SensorStatisticsExample.entity.DataStatistics;
import com.test.test.SensorStatisticsExample.entity.ReadData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.JacksonCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

public class SensorData extends AbstractVerticle {

  private final List<Double> allValues = new ArrayList<>();

  @Override
  public void start() {
    EventBus bus = vertx.eventBus();
    bus.consumer(BusAddressUtils.UPDATES, this::update);
    bus.consumer(BusAddressUtils.STATISTICS, this::statistics);
  }

  private void update(Message<JsonObject> message) {
    JsonObject json = message.body();
    ReadData data = JacksonCodec.decodeValue(json.encode(), new TypeReference<>(){});
    allValues.add(data.getTemp());
  }

  private void statistics(Message<JsonObject> message) {
    DataStatistics dataStatistics = generateDataStatistics();
    JsonObject json = JsonObject.mapFrom(dataStatistics);
    message.reply(json);
  }

  private DataStatistics generateDataStatistics(){
    double avg = allValues.stream()
      .collect(Collectors.averagingDouble(Double::doubleValue));
    double min = Collections.min(allValues);
    double max = Collections.max(allValues);
    return new DataStatistics(avg, max, min);
  }
}
