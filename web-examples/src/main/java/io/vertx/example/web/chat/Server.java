package io.vertx.example.web.chat;

import com.oracle.tools.packager.Log;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;

/**
 * A {@link io.vertx.core.Verticle} which implements a simple, realtime,
 * multiuser chat. Anyone can connect to the chat application on port
 * 8000 and type messages. The messages will be rebroadcast to all
 * connected users via the @{link EventBus} Websocket bridge.
 *
 * @author <a href="https://github.com/InfoSec812">Deven Phillips</a>
 */
public class Server extends AbstractVerticle {

  // shared Mem에 없다면 classLoader가 달라서 초기화가 될 위험이 존재하므로 이렇게 공유하면 안됨.
  // 확장성도 고려했을 때 그게 좋음
  // vertx.sharedData().get~~~
  ArrayList<String> arr = new ArrayList<>();


  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    // webSocket

    Router router = Router.router(vertx);

    // Allow events for the designated addresses in/out of the event bus bridge
    BridgeOptions opts = new BridgeOptions()
      .addInboundPermitted(new PermittedOptions().setAddress("chat.to.server"))
      .addOutboundPermitted(new PermittedOptions().setAddress("chat.to.client"));
    // 얘가 있어야 client가 보낸 msg를 server에서 인식할 수 있음. Inbound와 Outbound의 차이점을 알아야 함!!


    // Create the event bus bridge and add it to the router.
    SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
//    SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts, new Handler<BridgeEvent>() {
//      @Override
//      public void handle(BridgeEvent bridgeEvent) {
//        bridgeEvent.socket().writeHandlerID(); // 이건 client를 구별할 수 있는 ID!! = UserSession ---> 신규 접속자인지 아닌지 구별
//        // key : id, value : 내용 + 시간 ----> client 입장에서 msg를 뿌려줘야 함. id로 내껏과 남의 것을 구별
//      }
//    });
    // bridge(opts)만 할 수도 있는데, client가 접속할 때 원하는 행위를 하고 싶으면 handler를 등록해야 함.



    // client에서 보면 Eventbus를 등록해야 함. 그래서 /eventbus/를 인식해야 함!!
    router.route("/eventbus/*").handler(ebHandler);


    // Create a router endpoint for the static content.
    router.route().handler(StaticHandler.create());
    // StaticHandler.create(root)로 자세한 경로를 보여줄 수도 있음.
    // staticHandler는 정적 페이지를 보여주는 것!! 따라서 script 수정 후 새로고침해도 안바뀜. cache로 저장해서 가져옴.
    // 뒤에 new handler를 구현해서 우너하는 것을 실행해도 됨.


    // Start the web server and tell it to use the router to handle requests.
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    EventBus eb = vertx.eventBus();

    // Register to listen for messages coming IN to the server
    eb.consumer("chat.to.server").handler(message -> {
      // Create a timestamp string
      String timestamp = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(Date.from(Instant.now()));

      // Send the message back out to all clients with the timestamp prepended.
      String str = timestamp + ": " + message.body();
      arr.add(str);
      // index로 관리해서 가져온다고???

      // eb.publish(clientID, str); 로 개별로 알림 줄 수 있음.
      // 혹은 eb.send();도 가능한데 이는 asyn 기능을 제공. ---> DB내용을 가져오거나 다른 Server로부터 정보를 가져올 때 유용.
      Log.debug("** " + str);

      eb.publish("chat.to.client", str);
    });


  }
}