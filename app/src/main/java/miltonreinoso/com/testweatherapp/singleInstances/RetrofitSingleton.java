package miltonreinoso.com.testweatherapp.singleInstances;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.darksky.net";

    public static Retrofit getRetrofitInstance() {
        Gson gson = new Gson();
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    private RetrofitSingleton(){

    }
}




