package tolk.studio.weather_app_new.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import tolk.studio.weather_app_new.dao.CityDao;
import tolk.studio.weather_app_new.model.City;

@Database(entities = {City.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CityDao cityDao();
}
