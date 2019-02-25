package miltonreinoso.com.testweatherapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import miltonreinoso.com.testweatherapp.R;
import miltonreinoso.com.testweatherapp.adapters.CustomListAdapter;
import miltonreinoso.com.testweatherapp.models.Forecast;
import miltonreinoso.com.testweatherapp.singleInstances.LocationsInstances;


public class ForecastFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private TextView currentTemperatureTxtV;
    private TextView currentLocationTxtV;
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
        currentTemperatureTxtV.setText(currentTemperature);
        currentLocationTxtV.setText(forecast.getTimezone());
        setFiveDaysForecastListView(forecast);

    }


        public void setFiveDaysForecastListView (Forecast forecast) {

        String diasTestArray[] = {
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(1).getTime()),
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(2).getTime()),
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(3).getTime()),
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(4).getTime()),
                convertTimestamp(forecast.getDailyForecast().getDailyConditionsList().get(5).getTime())};

        Integer imagenTest[] = {
//                ""+ forecast.getDailyForecast().getDailyConditionsList().get(0).getIconString(),
                R.drawable.ic_sun,
                R.drawable.ic_sun,
                R.drawable.ic_sun,
                R.drawable.ic_sun,
                R.drawable.ic_sun};

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


    private String convertTimestamp (long intTimestamp){

        Timestamp timestamp = new Timestamp(intTimestamp*1000);
        java.util.Date date = new Date(timestamp.getTime());

        java.util.Calendar cal = Calendar.getInstance();
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
