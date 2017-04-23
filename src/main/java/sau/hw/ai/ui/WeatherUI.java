package sau.hw.ai.ui;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import sau.hw.ai.agents.WeatherAgent;

public class WeatherUI extends JFrame {
	private static final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
	private static final DateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("E, dd/MM");

	private static final String TITLE_WINDOW = "WeatherWise";
	private static final int WIDTH_WINDOW = 800;
	private static final int HEIGHT_WINDOW = 600;

	private JComboBox<String> cbActivities;
	private Agent agent;
	private JLabel labelTemp;
	private JLabel labelPicture;
	private static final int WEATHER_ICON_SIZE = 200;

	public WeatherUI(Agent agent) throws HeadlessException {
		this.agent = agent;
	}

	public void start() {

		setTitle(TITLE_WINDOW);
		setSize(WIDTH_WINDOW, HEIGHT_WINDOW);

		setLayout(new BorderLayout());
		JPanel panelContents = new JPanel();
		panelContents.setLayout(new BorderLayout(5, 5));
		panelContents.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		Font uiFont = panelContents.getFont();
		uiFont = new Font(uiFont.getName(), uiFont.getStyle(), uiFont.getSize() + 3);

		GridBagConstraints c = new GridBagConstraints();
		JPanel panelTop = new JPanel(new BorderLayout(5, 5));
		JPanel panelDisplayTop = new JPanel(new GridBagLayout());
		JPanel panelDisplayBottom = new JPanel(new GridBagLayout());
		panelTop.add(panelDisplayTop, BorderLayout.NORTH);
		panelTop.add(panelDisplayBottom, BorderLayout.CENTER);

		JPanel panelBottom = new JPanel(new BorderLayout());

		// add borders to panels
		//Border borderPadded = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(10, 10, 10, 10));
		Border borderPadded = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		//Border borderEmpty = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		panelDisplayTop.setBorder(borderPadded);
		panelDisplayBottom.setBorder(borderPadded);
		//panelLeftDropdown.setBorder(borderEmpty);
		panelBottom.setBorder(borderPadded);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = .8;
		c.fill = GridBagConstraints.HORIZONTAL;
		JLabel labelActivity = new JLabel("What do you want to do?");
		labelActivity.setFont(uiFont);
		panelDisplayTop.add(labelActivity, c);

		cbActivities = new JComboBox<>();
		cbActivities.setFont(uiFont);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = .8;
		c.fill = GridBagConstraints.HORIZONTAL;
		panelDisplayTop.add(cbActivities, c);

		c.gridx = 1;
		c.gridy = 1;
		c.weightx = .2;
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton buttonOK = new JButton("OK");
		buttonOK.setFont(uiFont);
		buttonOK.addActionListener(e -> {
			String activity = (String) cbActivities.getSelectedItem();

			if ("".equals(activity) || activity == null) {
				clearOutput();
				return;
			}

			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setOntology(WeatherAgent.ONTOLOGY_WEATHER_REPORT);
			message.addReceiver(new AID(WeatherAgent.class.getSimpleName(), AID.ISLOCALNAME));
			message.setContent(activity);
			agent.send(message);
		});

		panelDisplayTop.add(buttonOK, c);

		panelContents.add(panelTop, BorderLayout.CENTER);

		// results panel
		JPanel panelPicture = new JPanel(new BorderLayout());
		labelPicture = new JLabel();
		panelPicture.add(labelPicture, BorderLayout.CENTER);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = .5;
		panelDisplayBottom.add(panelPicture, c);

		JPanel panelDetails = new JPanel();
		panelDetails.setLayout(new BoxLayout(panelDetails, BoxLayout.Y_AXIS));
		panelDetails.setAlignmentY(Component.CENTER_ALIGNMENT);
		labelTemp = new JLabel();
		labelTemp.setFont(uiFont);
		panelDetails.add(labelTemp);

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = .5;
		panelDisplayBottom.add(panelDetails, c);

		JButton btnExit = new JButton("Close");
		btnExit.setFont(uiFont);
		btnExit.addActionListener(e -> {
			//System.out.println("exiting the app");
			try {
				Ontology jmo = JADEManagementOntology.getInstance();
				SLCodec codec = new SLCodec();
				agent.getContentManager().registerLanguage(codec);
				agent.getContentManager().registerOntology(jmo);
				ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
				message.addReceiver(agent.getAMS());
				message.setLanguage(codec.getName());
				message.setOntology(jmo.getName());
				agent.getContentManager().fillContent(message, new Action(agent.getAID(), new ShutdownPlatform()));
				agent.send(message);
			} catch (Codec.CodecException | OntologyException e1) {
				e1.printStackTrace();
			}
		});

		panelBottom.add(btnExit, BorderLayout.EAST);
		panelContents.add(panelBottom, BorderLayout.SOUTH);

		add(panelContents);
		setVisible(true);
	}

	private void clearOutput() {
		labelTemp.setText("");
		labelPicture.setIcon(null);
	}

	private void setMessageOutput(String message) {
		labelTemp.setText(message);
		labelPicture.setVisible(false);
	}

	public void updateActivities(String[] names) {
		Arrays.sort(names);

		cbActivities.removeAllItems();
		cbActivities.addItem("");
		for (String name : names) {
			cbActivities.addItem(name);
		}
	}

	public void updateOutput(JSONObject output) {
		if (output != null) {
			try {
				Date date = ISO_DATE_FORMAT.parse(output.getString("date"));
				int tempMin = output.getInt("tempMin");
				int tempMax = output.getInt("tempMax");
				String condition = output.getString("condition");

				labelTemp.setText("<html> <p style='text-align:center'> Temperature: down(" + tempMin + "c) | up(" + tempMax + "c) <br> " +
						DISPLAY_DATE_FORMAT.format(date) + " </p> </html>");

				if (condition != null) {
					condition = condition.toLowerCase();

					try {
						if (condition.contains("part") && condition.contains("cloud")) {
							setWeatherIcon("./rsc/images/partly_cloudy.png");
							return;
						}

						if (condition.contains("sun")) {
							setWeatherIcon("./rsc/images/sunny.png");
							return;
						}

						if (condition.contains("cloud")) {
							setWeatherIcon("./rsc/images/cloudy.png");
							return;
						}

						if (condition.contains("rain")) {
							setWeatherIcon("./rsc/images/raining.png");
							return;
						}

						if (condition.contains("snow")) {
							setWeatherIcon("./rsc/images/snowing.png");
							return;
						}

						if (condition.contains("thunder")) {
							setWeatherIcon("./rsc/images/thunder.png");
							return;
						}

						labelPicture.removeAll();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (ParseException | JSONException e) {
				e.printStackTrace();
			}
			return;
		}

		clearOutput();
		setMessageOutput("The next 5 days don't seem to be OK for that activity");
	}

	private void setWeatherIcon(String iconPath) throws IOException {
		BufferedImage image = ImageIO.read(new File(iconPath));
		labelPicture.removeAll();
		labelPicture.setVisible(true);
		labelPicture.setIcon(new ImageIcon(image.getScaledInstance(WEATHER_ICON_SIZE, WEATHER_ICON_SIZE, Image.SCALE_SMOOTH)));
		labelPicture.setSize(WEATHER_ICON_SIZE, WEATHER_ICON_SIZE);
	}
}
