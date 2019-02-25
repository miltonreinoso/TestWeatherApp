package miltonreinoso.com.testweatherapp.singleInstances;


import miltonreinoso.com.testweatherapp.models.Forecast;

public class LocationsInstances {

    private static LocationsInstances sLocationsInstances;
    private static Forecast sCurrentLocation;
    private static Forecast sIstanbulLocation;
    private static Forecast sNewYorkLocation;
    private static Forecast sTokyoLocation;
    private static Forecast sBeijingLocation;
    private static Forecast sHelsinkiLocation;

    private LocationsInstances(){
    }

    public static LocationsInstances get(){
        if(sLocationsInstances == null){
            sLocationsInstances = new LocationsInstances();
        }
        return sLocationsInstances;
    }

    public Forecast getCurrentLocation() {
        return sCurrentLocation;
    }

    public void setCurrentLocation(Forecast location){
        sCurrentLocation = location;
    }


    public Forecast getIstanbulLocation() {
        return sIstanbulLocation;
    }

    public void setIstanbulLocation(Forecast istanbulLocation) {
        sIstanbulLocation = istanbulLocation;
    }

    public Forecast getNewYorkLocation() {
        return sNewYorkLocation;
    }

    public void setNewYorkLocation(Forecast newYorkLocation) {
        sNewYorkLocation = newYorkLocation;
    }


    public Forecast getTokyoLocation() {
        return sTokyoLocation;
    }

    public void setTokyoLocation(Forecast tokyoLocation) {
        sTokyoLocation = tokyoLocation;
    }

    public Forecast getBeijingLocation() {
        return sBeijingLocation;
    }

    public void setBeijingLocation(Forecast beijingLocation){
        sBeijingLocation = beijingLocation;
    }

    public Forecast getHelsinkiLocation() {
        return sHelsinkiLocation;
    }

    public void setHelsinkiLocation(Forecast helsinkiLocation){
        sHelsinkiLocation = helsinkiLocation;
    }


}
