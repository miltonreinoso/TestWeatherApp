package miltonreinoso.com.testweatherapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import miltonreinoso.com.testweatherapp.R;


public class CustomListAdapter extends ArrayAdapter {


    //to reference the Activity
    private final Activity context;

    //to store the animal images
    private final Integer[] imageIDarray;

    //to store the list of countries
    private final String[] daysFutureForecast;

    private final String[] minimumTemp;

    private final String[] maximumTemp;

    public CustomListAdapter(Activity context, String[] daysFutureForecast, Integer[] imageIDArrayParam, String[] minimumTemp, String[] maximumTemp){

        super(context, R.layout.day_forecast_item , daysFutureForecast);
        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.daysFutureForecast = daysFutureForecast;
        this.minimumTemp = minimumTemp;
        this.maximumTemp = maximumTemp;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.day_forecast_item, null,false);

        //this code gets references to objects in the listview_row.xml file
        TextView daysForecastTxtV = (TextView) rowView.findViewById(R.id.days_list_forecast_txtv);
        TextView minimumTemperatureTxtV = (TextView) rowView.findViewById(R.id.minimum_dayly_temperature_txtv);
        TextView maximumTemperatureTxtV = (TextView) rowView.findViewById(R.id.maximum_dayly_temperature_txtv);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.weather_list_icon_imgv);

        //this code sets the values of the objects to values from the arrays
        daysForecastTxtV.setText(daysFutureForecast[position]);
        minimumTemperatureTxtV.setText(minimumTemp[position]);
        maximumTemperatureTxtV.setText(maximumTemp[position]);
        imageView.setImageResource(imageIDarray[position]);

        return rowView;

    };
}