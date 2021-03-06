package miltonreinoso.com.testweatherapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import miltonreinoso.com.testweatherapp.R;
import miltonreinoso.com.testweatherapp.fragments.ForecastFragment;
import miltonreinoso.com.testweatherapp.interfaces.WeatherCalls;
import miltonreinoso.com.testweatherapp.models.Forecast;
import miltonreinoso.com.testweatherapp.singleInstances.LocationsInstances;
import miltonreinoso.com.testweatherapp.singleInstances.RetrofitSingleton;
import miltonreinoso.com.testweatherapp.utils.ConstantsKeys;
import miltonreinoso.com.testweatherapp.utils.ZoomOutPageTransformer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private ImageView darkSkyLinkImgV;
    public ViewPager mViewPager;

    private WeatherCalls mService;
    private Location uniqueLocation;
    private double currentLatitude;
    private double currentLongitude;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mService = RetrofitSingleton.getRetrofitInstance().create(WeatherCalls.class);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ConstantsKeys.LOCATION_PERMISSION_REQUEST_CODE);
            }
            else{
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 ,0 ,this);
            }
        }
        else{
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 ,0 ,this);
        }

        //ViewPager and Toolbar
        mViewPager = findViewById(R.id.view_pager_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Intent to DarkSky required for usage permission
        darkSkyLinkImgV = findViewById(R.id.dark_sky_powered_imgv);
        darkSkyLinkImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                darkSkyWebsiteIntent();
            }
        });


        // Setting navigation drawer

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fetchCityData(1, ConstantsKeys.ISTANBUL_LAT, ConstantsKeys.ISTANBUL_LON);
        fetchCityData(2, ConstantsKeys.NEW_YORK_LAT, ConstantsKeys.NEW_YORK_LON);
        fetchCityData(3, ConstantsKeys.TOKYO_LAT, ConstantsKeys.TOKYO_LON);
        fetchCityData(4, ConstantsKeys.BEIJING_LAT, ConstantsKeys.BEIJING_LON);
        fetchCityData(5, ConstantsKeys.HELSINKI_LAT, ConstantsKeys.HELSINKI_LON);

        fetchCurrentCityData();
    }

    public void darkSkyWebsiteIntent(){
        String url = "https://darksky.net/poweredby/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }


    public void fetchCurrentCityData(){
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(Forecast.Units.HTTP_QUERY_KEY, Forecast.Units.SI.toString());
        queryMap.put(Forecast.Units.HTTP_QUERY_KEY, Forecast.Units.SI.toString());
        queryMap.put(Forecast.Language.HTTP_QUERY_KEY, Forecast.Language.SPANISH.toString());
        queryMap.put(Forecast.Exclusion.HTTP_QUERY_KEY, Forecast.Exclusion.HOURLY.toString() + ","
                + Forecast.Exclusion.MINUTELY.toString() + ","
                + Forecast.Exclusion.FLAGS.toString());

        Call<Forecast> call = mService.getForecast(ConstantsKeys.APIKey,currentLatitude, currentLongitude, queryMap);
        call.enqueue(new Callback<Forecast>() {

            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                LocationsInstances.get().setCurrentLocation(response.body());

                mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {

                        switch (position){
                            case 0:
                                return ForecastFragment.newInstance(0);

                            case 1:
                                return ForecastFragment.newInstance(1);

                            case 2:
                                return ForecastFragment.newInstance(2);

                            case 3:
                                return ForecastFragment.newInstance(3);

                            case 4:
                                return ForecastFragment.newInstance(4);

                            case 5:
                                return ForecastFragment.newInstance(5);

                            default: return ForecastFragment.newInstance(0);
                        }
                    }
                    @Override
                    public int getCount() {
                        return 6;
                    }
                });

                mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            }

            @Override
            public void onFailure(@NonNull Call<Forecast> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"something went horribly wrong", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void fetchCityData(final int cityNamePosition, double latitude, double longitude){

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(Forecast.Units.HTTP_QUERY_KEY, Forecast.Units.SI.toString());
        queryMap.put(Forecast.Language.HTTP_QUERY_KEY, Forecast.Language.SPANISH.toString());
        queryMap.put(Forecast.Exclusion.HTTP_QUERY_KEY, Forecast.Exclusion.HOURLY.toString() + ","
                + Forecast.Exclusion.MINUTELY.toString() + ","
                + Forecast.Exclusion.FLAGS.toString());

        Call<Forecast> callLocation = mService.getForecast(ConstantsKeys.APIKey,
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
                Toast.makeText(MainActivity.this, "something went horribly wrong" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected (MenuItem item){

        int id = item.getItemId();

        switch (id) {
            case R.id.istanbul_nav_menu_item:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.new_york_nav_menu_item:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tokyio_nav_menu_item:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.beijing_nav_menu_item:
                mViewPager.setCurrentItem(4);
                break;
            case R.id.helsinski_nav_menu_item:
                mViewPager.setCurrentItem(5);
                break;
            default:
                mViewPager.setCurrentItem(0);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
