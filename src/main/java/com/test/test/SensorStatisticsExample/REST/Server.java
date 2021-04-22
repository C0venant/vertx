package com.test.test.SensorStatisticsExample.REST;

import com.test.test.SensorStatisticsExample.BusAddressUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class Server extends AbstractVerticle {

  public static int PORT = 8080;

  private Router router;

  @Override
  public void start() {
    HttpServer httpServer = vertx.createHttpServer();
    router = Router.router(vertx);
    BodyHandler bodyHandler = BodyHandler.create();
    router.route().handler(bodyHandler);
    setupEndpoints();
    httpServer.requestHandler(router).listen(PORT);

  }

  private void setupEndpoints(){
    router.post("/statistics").handler(this::statisticsRequestHandler);
    router.post("/user/register").handler(this::registerRequestHandler);
    router.get("/user").handler(this::findRequestHandler);
  }

  private void statisticsRequestHandler(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    vertx.eventBus().<JsonObject>request(BusAddressUtils.STATISTICS, "", reply -> {
      if (reply.succeeded()) {
        response.putHeader("content-type", "application/json");
        response.end(reply.result().body().encode());
      }
    });
  }

  private void registerRequestHandler(RoutingContext routingContext) {

    HttpServerResponse response = routingContext.response();

    vertx.eventBus().<JsonObject>request(BusAddressUtils.REGISTER_USER, routingContext.getBody().toJsonObject(), reply -> {
      if (reply.succeeded()) {
        response.putHeader("content-type", "application/json");
        response.end(reply.result().body().encode());
      } else {
        System.out.println(reply.cause().getMessage());
        response.setStatusCode(409);
        response.end();
      }
    });
  }

  private void findRequestHandler(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    vertx.eventBus().<JsonObject>request(BusAddressUtils.FIND_USER, routingContext.getBody().toJsonObject(), reply -> {
      if (reply.succeeded()) {
        response.putHeader("content-type", "application/json");
        response.end(reply.result().body().encode());
      } else {
        response.setStatusCode(404);
        response.end();
      }
    });
  }

}
