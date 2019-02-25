package miltonreinoso.com.testweatherapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyForecast {

    private String summary;

    @SerializedName("icon")
    private String iconString;

    @SerializedName("data")
    private List<DayConditions> dailyConditionsList;


    public String getSummary() {
        return summary;
    }

    public String getIconString() {
        return iconString;
    }

    public List<DayConditions> getDailyConditionsList() {
        return dailyConditionsList;
    }

    @Override
    public String toString() {
        return "{DailyForecast: [Summary = " + summary + "][IconString = " + iconString +
                "][DailyConditionsList = " + dailyConditionsList.toString() + "]}";
    }
}
