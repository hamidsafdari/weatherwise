package sau.hw.ai.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WeatherActivity {

	private String name;
	private int minTemperature;
	private int maxTemperature;
	private List<String> desiredConditions = new ArrayList<>();

	public WeatherActivity(String name, int minTemperature, int maxTemperature, Collection<String> desiredConditions) {
		this.name = name;
		this.minTemperature = minTemperature;
		this.maxTemperature = maxTemperature;
		this.desiredConditions.addAll(desiredConditions);
	}

	public String getName() {
		return name;
	}

	public WeatherActivity setName(String name) {
		this.name = name;
		return this;
	}

	public int getMinTemperature() {
		return minTemperature;
	}

	public WeatherActivity setMinTemperature(int minTemperature) {
		this.minTemperature = minTemperature;
		return this;
	}

	public int getMaxTemperature() {
		return maxTemperature;
	}

	public WeatherActivity setMaxTemperature(int maxTemperature) {
		this.maxTemperature = maxTemperature;
		return this;
	}

	public List<String> getDesiredConditions() {
		return desiredConditions;
	}

	public WeatherActivity setDesiredConditions(Collection<String> desiredConditions) {
		this.desiredConditions.clear();
		this.desiredConditions.addAll(desiredConditions);
		return this;
	}

	public boolean isUndesiredCondition(String condition) {
		return desiredConditions.contains(condition);
	}

	@Override
	public String toString() {
		return "WeatherActivity{" +
				"name='" + name + '\'' +
				", minTemperature=" + minTemperature +
				", maxTemperature=" + maxTemperature +
				", desiredConditions=" + desiredConditions +
				'}';
	}
}
