package sau.hw.ai.agents;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import sau.hw.ai.ui.WeatherUI;

public class UIAgent extends Agent {

	public static final String ONTOLOGY_UPDATE_UI = "upate-ui";
	public static final String ACTION_UI_READY = "ui-ready";

	private Agent thisAgent;
	private WeatherUI ui;
	private String[] activities;

	@Override
	protected void setup() {
		thisAgent = this;

		SwingUtilities.invokeLater(() -> {
			try {
				for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (Exception ignored) {}

			ui = new WeatherUI(thisAgent);
			ui.start();
			ui.updateActivities(activities);
		});

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
					activities = messageText.split(",");

					if (ui != null) {
						ui.updateActivities(activities);
					}
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
