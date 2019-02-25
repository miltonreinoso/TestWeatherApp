package miltonreinoso.com.testweatherapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import miltonreinoso.com.testweatherapp.R;
import miltonreinoso.com.testweatherapp.adapters.CustomListAdapter;
import miltonreinoso.com.testweatherapp.models.Forecast;
import miltonreinoso.com.testweatherapp.singleInstances.LocationsInstances;


public class ForecastFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private TextView currentTemperatureTxtV;
    private TextView currentLocationTxtV;
    private ImageView currentWeatherIcon;
    private TextView currentWeatherSummaryTxtV;
    private int localPosition;
    private ListView fiveDaysForecastListView;


    public static ForecastFragment newInstance(int pagerPosition) {
        Bundle args = new Bundle();
        ForecastFragment fragment = new ForecastFragment();
        args.putInt(ARG_POSITION, pagerPosition);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        localPosition = getArguments().getInt(ARG_POSITION);

        fiveDaysForecastListView = view.findViewById(R.id.five_days_forecast_listview);
        currentTemperatureTxtV = view.findViewById(R.id.current_temp_txtv);
        currentLocationTxtV = view.findViewById(R.id.current_location_txtv);
        currentWeatherIcon = view.findViewById(R.id.weather_condition_imgv);
        currentWeatherSummaryTxtV = view.findViewById(R.id.current_summary_txtv);

        switch (localPosition){
            case 0:
                setDataToViews(LocationsInstances.get().getCurrentLocation());
                break;
            case 1:
                setDataToViews(LocationsInstances.get().getIstanbulLocation());
                break;
            case 2:
                setDataToViews(LocationsInstances.get().getNewYorkLocation());
                break;
            case 3:
                setDataToViews(LocationsInstances.get().getTokyoLocation());
                break;
            case 4:
                setDataToViews(LocationsInstances.get().getBeijingLocation());
                break;
            case 5:
                setDataToViews(LocationsInstances.get().getHelsinkiLocation());
                break;
            default:
                break;
        }
        return view;
    }


    public void setDataToViews (Forecast forecast){

        String currentTemperature = (int) forecast.getCurrentConditions().getTemperature() + " °C";
        String currentWeatherSummary = forecast.getCurrentConditions().getSummary();

        currentTemperatureTxtV.setText(currentTemperature);
        currentLocationTxtV.setText(adaptTitle(forecast.getTimezone()));
        currentWeatherIcon.setImageResource(getIconResource(forecast.getCurrentConditions().getIconString()));
        currentWeatherSummaryTxtV.setText(currentWeatherSummary);
        setFiveDaysForecastListView(forecast);

    }


    //  Adapting the title format based on TimeZone (API dind´t include geolocalization
    //  E.g: changing from "America/Argentina/Buenos_Aires" to "Buenos Aires"
    private String adaptTitle(String locationTitleRaw){

        String locationTitleAdapted = "";

        if(localPosition == 0) {
            locationTitleAdapted = locationTitleRaw.substring(locationTitleRaw.indexOf("/")+1);
            if (locationTitleAdapted.contains("/")){
                locationTitleAdapted = locationTitleAdapted.substring(locationTitleAdapted.indexOf("/")+1);
            }
            locationTitleAdapted = locationTitleAdapted.replace("_", " ");
        }

        switch (localPosition) {
            case 1: locationTitleAdapted = getString(R.string.istanbul);
            break;
            case 2: locationTitleAdapted = getString(R.string.new_york);
                break;
            case 3: locationTitleAdapted = getString(R.string.tokyo);
                break;
            case 4: locationTitleAdapted = getString(R.string.beijing);
                break;
            case 5: locationTitleAdapted = getString(R.string.helsinki);
                break;
        }
        return locationTitleAdapted;
    }


        public void setFiveDaysForecastListView (Forecast forecast) {

        String diasTestArray[] = {
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(1).getTime(), forecast),
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(2).getTime(), forecast),
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(3).getTime(), forecast),
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(4).getTime(), forecast),
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(5).getTime(), forecast)};

        Integer imagenTest[] = {
                getIconResource(forecast.getDailyForecast().getDailyConditionsList().get(1).getIconString()),
                getIconResource(forecast.getDailyForecast().getDailyConditionsList().get(2).getIconString()),
                getIconResource(forecast.getDailyForecast().getDailyConditionsList().get(3).getIconString()),
                getIconResource(forecast.getDailyForecast().getDailyConditionsList().get(4).getIconString()),
                getIconResource(forecast.getDailyForecast().getDailyConditionsList().get(5).getIconString())};

        String minTempTestArray[] = {

                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(1).getTemperatureMin(),
                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(2).getTemperatureMin(),
                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(3).getTemperatureMin(),
                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(4).getTemperatureMin(),
                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(5).getTemperatureMin() };

        String maxTempTestArray[] = {
                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(1).getTemperatureMax(),
                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(2).getTemperatureMax(),
                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(3).getTemperatureMax(),
                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(4).getTemperatureMax(),
                ""+ (int) forecast.getDailyForecast().getDailyConditionsList().get(5).getTemperatureMax()};

        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(), diasTestArray, imagenTest,minTempTestArray,maxTempTestArray);
        fiveDaysForecastListView.setAdapter(customListAdapter);
    }

    private Integer getIconResource (String iconType){

        switch (iconType) {
            case "clear-day":
                return R.drawable.ic_clear_day;
            case "clear-night":
                return R.drawable.ic_moon;
            case "rain":
                return R.drawable.ic_cloud_rain;
            case "sleet":
                return R.drawable.ic_cloud_sleet;
            case "snow":
                return R.drawable.ic_cloud_snow;
            case "wind":
                return R.drawable.ic_wind;
            case "fog":
                return R.drawable.ic_cloud_fog;
            case "cloudy":
                return R.drawable.ic_cloud;
            case "partly-cloudy-day":
                return R.drawable.ic_cloud_sun;
            case "partly-cloudy-night":
                return R.drawable.ic_cloud_moon;
                default:
                    return R.drawable.ic_cloud_refresh;

        }

    }


    private String convertTimestamp (long longTimeStamp, Forecast forecast){


        TimeZone timeZone = TimeZone.getTimeZone(forecast.getTimezone());

        Timestamp timestamp = new Timestamp(longTimeStamp*1000);
        java.util.Date date = new Date(timestamp.getTime());

        java.util.Calendar cal = Calendar.getInstance();

        cal.setTimeZone(timeZone);
        cal.setTime(date);

        String day = getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)) + "";

        return day;
    }


    private String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
    }

}
