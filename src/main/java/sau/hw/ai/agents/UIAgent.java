package sau.hw.ai.agents;

import org.json.JSONException;
import org.json.JSONObject;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import sau.hw.ai.ui.WeatherUI;

public class UIAgent extends Agent {

	public static final String ONTOLOGY_UPDATE_UI = "upate-ui";
	public static final String ACTION_UI_READY = "ui-ready";

	private Agent thisAgent;
	private WeatherUI ui;

	@Override
	protected void setup() {
		thisAgent = this;
		System.out.println("==============");
		System.out.println("starting agent: " + getLocalName());
		System.out.println("==============");

		// todo: removing after testing
		ui = new WeatherUI(this);
		ui.start();

		addBehaviour(new UIActionsServer());
	}

	private class UIActionsServer extends CyclicBehaviour {
		@Override
		public void action() {
			ACLMessage message = thisAgent.receive();
			if (message != null) {
				if (DatabaseAgent.ONTOLOGY_ACTIVITIES.equals(message.getOntology())) {
					System.out.println("received the activities");

					String messageText = message.getContent();
					String[] activities = messageText.split(",");

					if (ui != null) {
						ui.updateActivities(activities);
					}

					// todo: remove after testing
					//ACLMessage testMessage = new ACLMessage(ACLMessage.INFORM);
					//testMessage.addReceiver(new AID(WeatherAgent.class.getSimpleName(), AID.ISLOCALNAME));
					//testMessage.setContent("Swimming" + ":" + "Today");
					//thisAgent.send(testMessage);
				}

				if (ONTOLOGY_UPDATE_UI.equals(message.getOntology())) {
					if (ui != null) {
						try {
							ui.updateOutput(message.getContent() != null && !"".equals(message.getContent()) ? new JSONObject(message.getContent()) : null);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				return;
			}
			block();
		}
	}
}
