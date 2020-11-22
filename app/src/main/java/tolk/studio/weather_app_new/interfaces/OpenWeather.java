package tolk.studio.weather_app_new.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tolk.studio.weather_app_new.weather.WeatherRequest;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest>loadWeather(@Query("q") String cityCountry,@Query("appid") String keyApi);
}
