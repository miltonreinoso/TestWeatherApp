package miltonreinoso.com.testweatherapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.johnhiott.darkskyandroidlib.ForecastApi;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import miltonreinoso.com.testweatherapp.R;
import miltonreinoso.com.testweatherapp.data.CitiesCoordinates;
import miltonreinoso.com.testweatherapp.interfaces.WeatherCalls;
import miltonreinoso.com.testweatherapp.models.Forecast;
import miltonreinoso.com.testweatherapp.singleInstances.LocationsInstances;
import miltonreinoso.com.testweatherapp.singleInstances.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements LocationListener {

    private WeatherCalls mService;

    private ImageView darkSkyLinkImgV;
    private ViewPager mViewPager;
    public String APIKey = "6663a070c7910a4153e460e7ac6823c1";

    private Timestamp currentTimestamp;
    private double currentTime;

    private Location uniqueLocation;
    private double currentLatitude;
    private double currentLongitude;
    private LocationManager mLocationManager;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1252;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ForecastApi.create(CitiesCoordinates.APIKey);

        mService = RetrofitSingleton.getRetrofitInstance().create(WeatherCalls.class);
        mService = RetrofitSingleton.getRetrofitInstance().create(WeatherCalls.class);
        currentTimestamp =  new Timestamp(System.currentTimeMillis());
        currentTime = currentTimestamp.getTime();

        /**
         * Setting GPS Location Service and Manager
         **/

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Request permissions for Localization and gps use.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= 23) { // Marshmallow
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 ,0 ,this);


        fetchCityData(1, CitiesCoordinates.istanbulLat, CitiesCoordinates.istanbulLon);
        fetchCityData(2,CitiesCoordinates.newYorkLat, CitiesCoordinates.newYorkLon);
        fetchCityData(3,CitiesCoordinates.tokyoLat, CitiesCoordinates.tokyoLon);
        fetchCityData(4,CitiesCoordinates.beijingLat, CitiesCoordinates.beijingLon);
        fetchCityData(5,CitiesCoordinates.helsinkiLat, CitiesCoordinates.helsinkiLon);

        fetchCurrentCityData();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(getBaseContext(), MainActivity.class);

                startActivity(i);
                finish();
            }
        }, 3000);
    }


        public void fetchCurrentCityData () {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put(Forecast.Units.HTTP_QUERY_KEY, Forecast.Units.SI.toString());
            queryMap.put(Forecast.Language.HTTP_QUERY_KEY, Forecast.Language.SPANISH.toString());
            queryMap.put(Forecast.Exclusion.HTTP_QUERY_KEY, Forecast.Exclusion.HOURLY.toString() + ","
                    + Forecast.Exclusion.MINUTELY.toString() + ","
                    + Forecast.Exclusion.FLAGS.toString());


            Call<Forecast> call = mService.getForecast(CitiesCoordinates.APIKey, currentLatitude, currentLongitude, queryMap);
            call.enqueue(new Callback<Forecast>() {

                @Override
                public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                    LocationsInstances.get().setCurrentLocation(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<Forecast> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(), "something went horribly wrong", Toast.LENGTH_SHORT).show();
                }

            });

        }

        public void fetchCityData ( final int cityNamePosition, double latitude, double longitude){

            Map<String, String> queryMap = new HashMap<>();
            queryMap.put(Forecast.Units.HTTP_QUERY_KEY, Forecast.Units.SI.toString());
            queryMap.put(Forecast.Language.HTTP_QUERY_KEY, Forecast.Language.SPANISH.toString());
            queryMap.put(Forecast.Exclusion.HTTP_QUERY_KEY, Forecast.Exclusion.HOURLY.toString() + ","
                    + Forecast.Exclusion.MINUTELY.toString() + ","
                    + Forecast.Exclusion.FLAGS.toString());

            Call<Forecast> callLocation = mService.getForecast(APIKey,
                    latitude,
                    longitude,
                    queryMap);
            callLocation.enqueue(new Callback<Forecast>() {
                @Override
                public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                    switch (cityNamePosition) {
                        case 0:
                            LocationsInstances.get().setCurrentLocation(response.body());
                            break;
                        case 1:
                            LocationsInstances.get().setIstanbulLocation(response.body());
                            break;
                        case 2:
                            LocationsInstances.get().setNewYorkLocation(response.body());
                            break;
                        case 3:
                            LocationsInstances.get().setTokyoLocation(response.body());
                            break;
                        case 4:
                            LocationsInstances.get().setBeijingLocation(response.body());
                            break;
                        case 5:
                            LocationsInstances.get().setHelsinkiLocation(response.body());
                            break;
                    }
                }

                @Override
                public void onFailure(Call<Forecast> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, "something went horribly wrong" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    @Override
    public void onLocationChanged(Location location) {
        uniqueLocation = location;
        currentLatitude = uniqueLocation.getLatitude();
        currentLongitude = uniqueLocation.getLongitude();
        mLocationManager.removeUpdates(this);
        fetchCurrentCityData();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}


