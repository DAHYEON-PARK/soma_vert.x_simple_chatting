package io.vertx.example.util;

import io.vertx.core.DeploymentOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  private static final String WEB_EXAMPLES_DIR = "web-examples";
  private static final String WEB_EXAMPLES_JAVA_DIR = WEB_EXAMPLES_DIR + "/src/main/java/";
  //private static final String WEB_EXAMPLES_JS_DIR = WEB_EXAMPLES_DIR + "/src/main/js/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(WEB_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(WEB_EXAMPLES_JAVA_DIR, clazz, false);
  }

  public static void runExample(Class clazz, DeploymentOptions options) {
    ExampleRunner.runJavaExample(WEB_EXAMPLES_JAVA_DIR, clazz, options);
  }

  // JavaScript examples

//  public static void runJSExample(String scriptName) {
//    ExampleRunner.runScriptExample(WEB_EXAMPLES_JS_DIR, scriptName, false);
//  }
//
//  public static void runJSExampleClustered(String scriptName) {
//    ExampleRunner.runScriptExample(WEB_EXAMPLES_JS_DIR, scriptName, true);
//  }
//
//  static class JSAuthRunner {
//    public static void main(String[] args) {
//      Runner.runJSExample("io/vertx/example/web/auth/server.js");
//    }
//  }
//
//  static class JSAuthJDBC {
//    public static void main(String[] args) {
//      Runner.runJSExample("io/vertx/example/web/authjdbc/server.js");
//    }
//  }
//
//  static class JSHelloWorldRunner {
//    public static void main(String[] args) {
//      Runner.runJSExample("io/vertx/example/web/helloworld/server.js");
//    }
//  }
//
//  static class JSRealtimeRunner {
//    public static void main(String[] args) { Runner.runJSExample("io/vertx/example/web/realtime/server.js"); }
//  }
//
//  static class JSChatRunner {
//    public static void main(String[] args) {
//            Runner.runJSExample("io/vertx/example/web/chat/server.js");
//        }
//  }
//
//  static class JSSessionsRunner {
//    public static void main(String[] args) {
//      Runner.runJSExample("io/vertx/example/web/sessions/server.js");
//    }
//  }
//
//  static class JSTemplatingRunner {
//    public static void main(String[] args) {
//      Runner.runJSExample("io/vertx/example/web/templating/mvel/server.js");
//    }
//  }
}
