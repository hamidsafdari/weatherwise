package sau.hw;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.junit.Test;

public class WebTests {
	@Test
	public void testHttpGet() throws UnirestException {
		HttpResponse<JsonNode> jsonResponse = Unirest.post("http://httpbin.org/post")
				.header("accept", "application/json")
				.queryString("apiKey", "123")
				.field("parameter", "value")
				.field("foo", "bar")
				.asJson();

		System.out.println(jsonResponse.getBody());
	}
}
