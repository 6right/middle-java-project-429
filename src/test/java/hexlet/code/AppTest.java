package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.javalin.http.HttpStatus;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class AppTest {

  @Test
  void healthReturnsOk() throws Exception {
    var app = App.createApp().start("127.0.0.1", 0);
    try (var client = HttpClient.newHttpClient()) {
      var request =
          HttpRequest.newBuilder()
              .uri(URI.create("http://127.0.0.1:" + app.port() + "/api/health"))
              .GET()
              .build();
      var response = client.send(request, HttpResponse.BodyHandlers.ofString());
      var body = JsonParser.parseString(response.body()).getAsJsonObject();

      assertEquals(HttpStatus.OK.getCode(), response.statusCode());
      assertEquals(new JsonPrimitive("ok"), body.get("status"));
    } finally {
      app.stop();
    }
  }
}
