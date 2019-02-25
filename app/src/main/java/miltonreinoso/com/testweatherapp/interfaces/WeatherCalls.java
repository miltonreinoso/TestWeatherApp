package miltonreinoso.com.testweatherapp.interfaces;

import android.support.annotation.Nullable;

import java.util.Map;

import miltonreinoso.com.testweatherapp.models.Forecast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface WeatherCalls {

    @GET("/forecast/{apiKey}/{lat},{long}")
    Call<Forecast> getForecast(@Path("apiKey") String apiKey,
                               @Path("lat") double latitude,
                               @Path("long") double longitude,
                               @Nullable @QueryMap Map<String, String> options);

}

