package tolk.studio.weather_app_new;


import android.text.Editable;

public class GetUrl {

   public String getData(Editable city){

       final String url = String.format(
               "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",
               city,
               BuildConfig.WEATHER_API_KEY);


               return url;
   }

}
