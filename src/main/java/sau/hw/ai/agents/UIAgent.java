package sau.hw.ai.agents;

import jade.core.Agent;

public class UIAgent extends Agent {
	@Override
	protected void setup() {
		System.out.println("this is agent: " + getLocalName());
	}
}
