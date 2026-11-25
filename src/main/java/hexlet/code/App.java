package hexlet.code;

import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import io.javalin.http.staticfiles.Location;
import io.javalin.router.EndpointNotFound;

public class App {

  static void main(String[] args) {
    String portValue = System.getenv().getOrDefault("PORT", "8080");
    int port = Integer.parseInt(portValue);

    Javalin app =
        Javalin.create(
            config -> {
              if (App.class.getResource("/static/index.html") != null) {
                config.staticFiles.add("/static", Location.CLASSPATH);
              }

              config.routes.get(
                  "/api/health",
                  ctx -> {
                    ctx.contentType("application/json");
                    ctx.result("{\"status\":\"ok\"}");
                  });

              config.routes.exception(
                  EndpointNotFound.class,
                  (error, ctx) -> {
                    if (ctx.path().startsWith("/api/")) {
                      ctx.status(404)
                          .contentType("application/json")
                          .result("{\"error\":\"Not found\"}");
                      return;
                    }

                    if (ctx.method() == HandlerType.GET) {
                      var page = App.class.getResourceAsStream("/static/index.html");
                      if (page != null) {
                        ctx.status(200).contentType("text/html; charset=utf-8").result(page);
                        return;
                      }
                    }

                    ctx.status(404);
                  });
            });
    app.start("0.0.0.0", port);
  }
}
