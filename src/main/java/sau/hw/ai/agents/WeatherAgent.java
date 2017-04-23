package sau.hw.ai.agents;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

// http://dataservice.accuweather.com/forecasts/v1/daily/5day/202396?apikey=8h0llD7p1A9vSc7JTHXL6jrChAHzuGpo&metric=true
// 8h0llD7p1A9vSc7JTHXL6jrChAHzuGpo
// new delhi: 202396

public class WeatherAgent extends Agent {
	public static final String ONTOLOGY_WEATHER_REPORT = "weather-report";
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

	private Agent thisAgent;

	@Override
	protected void setup() {
		thisAgent = this;
		addBehaviour(new QueryReceiver());
	}

	private class QueryReceiver extends CyclicBehaviour {
		@Override
		public void action() {
			ACLMessage message = thisAgent.receive();
			if (message != null) {
				if (ONTOLOGY_WEATHER_REPORT.equals(message.getOntology())) {
					try {
						String testWeather = FileUtils.readFileToString(new File("./rsc/data/sample_weather.json"), "utf-8");
						JSONObject weatherJson = new JSONObject(testWeather);
						weatherJson.put("activity", message.getContent());
						String messageContent = weatherJson.toString();

						ACLMessage weatherMessage = new ACLMessage(ACLMessage.INFORM);
						weatherMessage.addReceiver(new AID(DatabaseAgent.class.getSimpleName(), AID.ISLOCALNAME));
						weatherMessage.setOntology(ONTOLOGY_WEATHER_REPORT);
						weatherMessage.setContent(messageContent);

						thisAgent.send(weatherMessage);
					} catch (IOException | JSONException e) {
						e.printStackTrace();
					}
				}

				return;
			}
			block();
		}
	}
}
