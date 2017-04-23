/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sau.hw.ai.agents;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import sau.hw.ai.model.WeatherActivity;

import static sau.hw.ai.agents.UIAgent.ONTOLOGY_UPDATE_UI;

/**
 * @author Danish Kumar
 */
public class DatabaseAgent extends Agent {

	private static final String JSON_KEY_ACTIVITIES = "activities";
	private static final String JSON_KEY_NAME = "name";
	private static final String JSON_KEY_MIN_TEMP = "min-temp";
	private static final String JSON_KEY_MAX_TEMP = "max-temp";

	private static final String JSON_KEY_DESIRED_CONDITIONS = "desired-conditions";
	private static final String DATABASE_FILE = "./rsc/data/activities.json";

	public static final String ONTOLOGY_ACTIVITIES = "activities";

	private Set<WeatherActivity> activities = new HashSet<>();
	private JSONObject database;
	private Agent thisAgent;

	private AID uiAID = new AID("UIAgent", AID.ISLOCALNAME);

	@Override
	public void setup() {
		thisAgent = this;

		try {
			String string = FileUtils.readFileToString(new File(DATABASE_FILE), "utf-8");

			database = new JSONObject(string);
			JSONArray arrayWeather = database.getJSONArray(JSON_KEY_ACTIVITIES);
			for (int i = 0; i < arrayWeather.length(); i++) {
				JSONObject item = arrayWeather.getJSONObject(i);
				JSONArray arrayUndesiredConditions = item.getJSONArray(JSON_KEY_DESIRED_CONDITIONS);
				List<String> desiredConditions = new ArrayList<>();
				for (int j = 0; j < arrayUndesiredConditions.length(); j++) {
					desiredConditions.add(arrayUndesiredConditions.getString(j));
				}

				activities.add(new WeatherActivity(item.getString(JSON_KEY_NAME),
						item.getInt(JSON_KEY_MIN_TEMP),
						item.getInt(JSON_KEY_MAX_TEMP),
						desiredConditions));
			}
		} catch (JSONException | IOException ex) {
			ex.printStackTrace();
		}

		addBehaviour(new DataServer());

		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(uiAID);

		List<String> names = new LinkedList<>();
		for (WeatherActivity activity : activities) {
			names.add(activity.getName());
		}
		String messageText = StringUtils.join(names, ",");
		message.setOntology(ONTOLOGY_ACTIVITIES);
		message.setContent(messageText);
		send(message);
	}

	private WeatherActivity getActivity(String name) {
		for (WeatherActivity activity : activities) {
			if (name.equals(activity.getName())) {
				return activity;
			}
		}

		return null;
	}

	private class DataServer extends CyclicBehaviour {
		@Override
		public void action() {
			ACLMessage message = thisAgent.receive();
			if (message != null) {
				if (UIAgent.ACTION_UI_READY.equals(message.getOntology()) && database != null) {
					ACLMessage reply = message.createReply();

					List<String> names = new LinkedList<>();
					for (WeatherActivity activity : activities) {
						names.add(activity.getName());
					}
					String messageText = StringUtils.join(names, ",");
					reply.setOntology(ONTOLOGY_ACTIVITIES);
					reply.setContent(messageText);
					thisAgent.send(reply);
				}

				if (WeatherAgent.ONTOLOGY_WEATHER_REPORT.equals(message.getOntology()) && database != null) {
					ACLMessage uiMessage = new ACLMessage(ACLMessage.INFORM);
					uiMessage.setOntology(ONTOLOGY_UPDATE_UI);
					uiMessage.addReceiver(new AID(UIAgent.class.getSimpleName(), AID.ISLOCALNAME));
					uiMessage.setContent("");
					try {
						JSONObject jsonWeather = new JSONObject(message.getContent());
						String activityName = jsonWeather.getString("activity");
						WeatherActivity targetActivity = getActivity(activityName);

						JSONArray days = jsonWeather.getJSONArray("DailyForecasts");
						for (int i = 0; i < days.length(); i++) {
							JSONObject day = days.getJSONObject(i);

							String weatherCondition = day.getJSONObject("Day").getString("IconPhrase");

							int tempMin = day.getJSONObject("Temperature").getJSONObject("Minimum").getInt("Value");
							int tempMax = day.getJSONObject("Temperature").getJSONObject("Maximum").getInt("Value");

							if (tempMin >= targetActivity.getMinTemperature() && targetActivity.getMaxTemperature() >= tempMax) {
								for (String desiredCondition : targetActivity.getDesiredConditions()) {
									//System.out.println(weatherCondition + ", " + desiredCondition);

									if (weatherCondition.toLowerCase().contains(desiredCondition)) {
										JSONObject dayWeather = new JSONObject();
										dayWeather.put("tempMin", tempMin);
										dayWeather.put("tempMax", tempMax);
										dayWeather.put("condition", weatherCondition);
										dayWeather.put("date", day.getString("Date"));

										uiMessage.setContent(dayWeather.toString());

										break;
									}
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					thisAgent.send(uiMessage);
				}

				return;
			}
			block();
		}
	}
}
