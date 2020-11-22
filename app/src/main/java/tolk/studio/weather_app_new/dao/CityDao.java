package tolk.studio.weather_app_new.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tolk.studio.weather_app_new.model.City;


@Dao
public interface CityDao {

    @Query("SELECT * FROM city")
    List<City> getAll();

    @Query("SELECT * FROM city WHERE id = :id")
    City getById(long id);

    @Insert
    void insert(City city);

    @Update
    void update(City city);

    @Delete
    void delete(City city);

}