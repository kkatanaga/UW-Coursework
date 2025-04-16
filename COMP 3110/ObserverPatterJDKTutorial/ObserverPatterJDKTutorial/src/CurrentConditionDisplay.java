import java.util.Observable;
import java.util.Observer;

public class CurrentConditionDisplay implements Observer{

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if(o instanceof WeatherData) {
			WeatherData wd = (WeatherData)o;
			System.out.println("Current conditions:" + wd.getTemperature() + "F degrees " + wd.getHumidity() + "% humidity");
		}
		
	}
}

/*@Override
public void update(Observable o, Object arg) {
	// TODO Auto-generated method stub
	if(o instanceof WeatherData) {
		WeatherData wd = (WeatherData) o;
		System.out.println("Current conditions:" + wd.getTemperature() + "F degrees " + wd.getHumidity() + "% humidity");
	}
}*/