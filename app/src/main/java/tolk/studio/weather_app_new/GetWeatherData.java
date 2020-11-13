package tolk.studio.weather_app_new;

import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import tolk.studio.weather_app_new.fragment_home.FragmenHome;
import tolk.studio.weather_app_new.weather.WeatherRequest;

import static android.content.ContentValues.TAG;

public class GetWeatherData {

 public void  GetData(String string) throws MalformedURLException {
     final URL uri = new URL(string);
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
//                         displayWeather(weatherRequest);
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
 }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

}
