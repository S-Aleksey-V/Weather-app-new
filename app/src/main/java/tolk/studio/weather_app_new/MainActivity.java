package tolk.studio.weather_app_new;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import tolk.studio.weather_app_new.fragment_dialog.FragmentDialog;
import tolk.studio.weather_app_new.fragment_home.FragmenHome;
import tolk.studio.weather_app_new.fragment_week_weather.FragmentWeekWeather;
import tolk.studio.weather_app_new.receiver.PowerConnectedReceiver;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BroadcastReceiver powerConnectedReceiver =new PowerConnectedReceiver();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);

        registerReceiver(powerConnectedReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));


        initNotificationChannel();


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, new FragmenHome())
                    .commit();

        }
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Не удалось получить токен, произошла ошибка
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Получить токен
                        String token = task.getResult().getToken();
                        // Сохранить токен...
                        Log.d("TAGGGGGG",token);
                    }
                });



    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(powerConnectedReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(powerConnectedReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerConnectedReceiver);
    }

    private void initDrawer(Toolbar toolbar){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this,
                        drawer,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        final SearchView searchText = (SearchView) search.getActionView();

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new FragmenHome())
                        .commit();

                FragmenHome.initRetrofit();
                FragmenHome.requestRetrofit(searchText.getQuery().toString(),BuildConfig.WEATHER_API_KEY);
                FragmentWeekWeather.generateData(searchText.getQuery().toString());



                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new FragmenHome())
                        .commit();
                break;
            case R.id.nav_weekWeather:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new FragmentWeekWeather())
                        .commit();
                break;
            case R.id.developers:
                new FragmentDialog().show(getSupportFragmentManager(),"");
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2","name",importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

}