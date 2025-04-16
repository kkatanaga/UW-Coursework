
public class Driver {
	public static void main(String args[]) {
		
		WeatherData weatherData = new WeatherData();
		CurrentConditionDisplay currentDisplay = new CurrentConditionDisplay();
		CurrentConditionDisplay anotherDisplay = new CurrentConditionDisplay();
		
		weatherData.addObserver(currentDisplay);
		weatherData.addObserver(anotherDisplay);
		
		weatherData.setMeasurements(80, 65, 30.4f);
		weatherData.setMeasurements(82, 70, 29.2f);
		weatherData.setMeasurements(78, 90, 29.2f);
	}
}