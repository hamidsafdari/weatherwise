package sau.hw;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.junit.Test;

public class WebTests {
	@Test
	public void testHttpGet() throws UnirestException {
		HttpResponse<JsonNode> jsonResponse = Unirest.post("http://dataservice.accuweather.com/forecasts/v1/daily/5day/202396")
				.header("accept", "application/json")
				.queryString("apikey", "8h0llD7p1A9vSc7JTHXL6jrChAHzuGpo")
				.queryString("metric", true)
				.asJson();

		System.out.println(jsonResponse.getBody());
	}
}
