package tolk.studio.weather_app_new.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;




@Entity
public class City {

    public final static String ID = "id";
    public final static String CITY_NAME = "City_NAme";
    public final static String  TEMP= "Temp";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public long id;

    @ColumnInfo(name = CITY_NAME)
    public String city;

    @ColumnInfo(name = TEMP)
    public float temp;
}
