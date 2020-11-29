package tolk.studio.weather_app_new.fragment_home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tolk.studio.weather_app_new.App;
import tolk.studio.weather_app_new.BuildConfig;
import tolk.studio.weather_app_new.CustomAdapter;
import tolk.studio.weather_app_new.R;
import tolk.studio.weather_app_new.dao.CityDao;
import tolk.studio.weather_app_new.database.AppDatabase;
import tolk.studio.weather_app_new.interfaces.OpenWeather;
import tolk.studio.weather_app_new.model.City;
import tolk.studio.weather_app_new.weather.WeatherRequest;


public class FragmenHome extends Fragment  {

    private Marker currentMarker;
    private GoogleMap mMap;

    private static final String TAG = "WEATHER";
    private static OpenWeather openWeather;

    private static EditText cityName;
    private static EditText temperature;
    private static EditText pressure;
    private static EditText humidity;
    private static EditText windSpeed;
    private static ImageView imageCity;


    public static void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }
    public static void requestRetrofit(String city, String keyApi){
        openWeather.loadWeather(city, BuildConfig.WEATHER_API_KEY)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        final WeatherRequest request = response.body();
                         if(request != null){
                             cityName.setText(request.getName());

                             temperature.setText(String.format("%f2", request.getMain().getTemp()-273));
                             pressure.setText(String.format("%d", request.getMain().getPressure()));
                             humidity.setText(String.format("%d", request.getMain().getHumidity()));
                             windSpeed.setText(String.format("%d", request.getWind().getSpeed()));


                             AppDatabase db = App.getInstance().getDatabase();
                             CityDao cityDao = db.cityDao();
                             City city = new City();
                             city.city = request.getName();
                             city.temp = request.getMain().getTemp()-273;
                             cityDao.insert(city);

                         }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragmen_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cityName = view.findViewById(R.id.textCity);
        temperature = view.findViewById(R.id.textTemprature);
        pressure = view.findViewById(R.id.textPressure);
        humidity = view.findViewById(R.id.textHumidity);
        windSpeed = view.findViewById(R.id.textWindspeed);
        imageCity = view.findViewById(R.id.imageCity);

        Picasso.get()
                .load("https://cdn.lifehacker.ru/wp-content/uploads/2013/07/shutterstock_144625241.jpg")
                .into(imageCity);





    }

}