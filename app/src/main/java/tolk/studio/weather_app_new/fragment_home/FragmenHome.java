package tolk.studio.weather_app_new.fragment_home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tolk.studio.weather_app_new.BuildConfig;
import tolk.studio.weather_app_new.R;
import tolk.studio.weather_app_new.interfaces.OpenWeather;
import tolk.studio.weather_app_new.weather.WeatherRequest;


public class FragmenHome extends Fragment {

    private static final String TAG = "WEATHER";

    private OpenWeather openWeather;
    private EditText cityName;
    private EditText temperature;
    private EditText pressure;
    private EditText humidity;
    private EditText windSpeed;
    private EditText enteredCity;

    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }
    private void requestRetrofit(String city, String keyApi){
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
        enteredCity = view.findViewById(R.id.enteredCity);
        Button refresh = view.findViewById(R.id.refresh);
        ImageView imageCity = view.findViewById(R.id.imageCity);
        Picasso.get()
                .load("https://cdn.lifehacker.ru/wp-content/uploads/2013/07/shutterstock_144625241.jpg")
                .into(imageCity);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRetrofit();
                requestRetrofit(enteredCity.getText().toString(),BuildConfig.WEATHER_API_KEY);

                Picasso.get()
                        .load("https://images.unsplash.com/photo-1600619023128-cba91b3300bb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80")
                        .into(imageCity);



            }
        });

    }
}