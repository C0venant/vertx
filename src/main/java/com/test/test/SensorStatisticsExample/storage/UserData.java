package com.test.test.SensorStatisticsExample.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.test.test.SensorStatisticsExample.BusAddressUtils;
import com.test.test.SensorStatisticsExample.entity.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.jackson.JacksonCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserData extends AbstractVerticle {

  private final List<User> users = new ArrayList<>();

  @Override
  public void start() {
    EventBus bus = vertx.eventBus();
    bus.consumer(BusAddressUtils.REGISTER_USER, this::register);
    bus.consumer(BusAddressUtils.FIND_USER, this::find);
  }

  private void register(Message <JsonObject> message) {
    JsonObject json = message.body();
    User user = JacksonCodec.decodeValue(json.encode(), new TypeReference<>(){});
    boolean present = users.stream().map(User::getUsername).anyMatch(name -> name.equals(user.getUsername()));
    if(present) {
      message.fail(409, "user exists");
    } else {
      users.add(user);
      message.reply(json);
    }
  }

  private void find(Message<JsonObject> message) {
    JsonObject json = message.body();
    String id = json.getString("id");
    Optional<User> user = users.stream().filter(u -> u.getId().equals(id)).findFirst();
    if(user.isPresent()){
      message.reply(JsonObject.mapFrom(user.get()));
    } else {
      message.fail(404, "user not found");
    }
  }
}
