package tolk.studio.weather_app_new.fragment_home;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;


import tolk.studio.weather_app_new.GetUrl;
import tolk.studio.weather_app_new.R;
import tolk.studio.weather_app_new.weather.WeatherRequest;


public class FragmenHome extends Fragment {

    private static final String TAG = "WEATHER";

    private EditText city;
    private EditText temperature;
    private EditText pressure;
    private EditText humidity;
    private EditText windSpeed;
    private EditText enteredCity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmen_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        city = view.findViewById(R.id.textCity);
        temperature = view.findViewById(R.id.textTemprature);
        pressure = view.findViewById(R.id.textPressure);
        humidity = view.findViewById(R.id.textHumidity);
        windSpeed = view.findViewById(R.id.textWindspeed);
        enteredCity = view.findViewById(R.id.enteredCity);
        Button refresh = view.findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    final String url = String.format(
//                            "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",
//                            enteredCity.getText(),
//                            BuildConfig.WEATHER_API_KEY

                    final GetUrl getUrl = new GetUrl();
                    final String url = getUrl.getData(enteredCity.getText());

                    final URL uri = new URL(url);
                    final Handler handler = new Handler(); // Запоминаем основной поток

                    new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void run() {
                            HttpsURLConnection urlConnection = null;
                            try {
                                urlConnection = (HttpsURLConnection) uri.openConnection();
                                urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                                urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
                                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                                String result = getLines(in);
//                                // преобразование данных запроса в модель
                                Gson gson = new Gson();
                                final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                                // Возвращаемся к основному потоку
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        displayWeather(weatherRequest);
                                    }
                                });
                            } catch (Exception e) {
                                Log.e(TAG, "Fail connection", e);
                                e.printStackTrace();
                            } finally {
                                if (null != urlConnection) {
                                    urlConnection.disconnect();
                                }
                            }
                        }
                    }).start();
                } catch (MalformedURLException e) {
                    Log.e(TAG, "Fail URI", e);
                    e.printStackTrace();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            private String getLines(BufferedReader in) {
                return in.lines().collect(Collectors.joining("\n"));
            }

            public void displayWeather(WeatherRequest weatherRequest){
                city.setText(weatherRequest.getName());
                temperature.setText(String.format("%f2", weatherRequest.getMain().getTemp()-273));
                pressure.setText(String.format("%d", weatherRequest.getMain().getPressure()));
                humidity.setText(String.format("%d", weatherRequest.getMain().getHumidity()));
                windSpeed.setText(String.format("%d", weatherRequest.getWind().getSpeed()));
            }

        });

    }
}