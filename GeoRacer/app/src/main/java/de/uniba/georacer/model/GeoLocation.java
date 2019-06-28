package de.uniba.georacer.model;

public class GeoLocation {

    private double longitude;
    private double latitude;
    private static final int offsetAfterDecimalPoint = 3;

    public GeoLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getLongitudeOffset() {
        return getOffset(longitude);
    }

    public int getLatitudeOffset() {
        return getOffset(latitude);
    }

    public int getOffset(double value) {
        String[] values = String.valueOf(value).split("\\.");
        if (values[1].length() < offsetAfterDecimalPoint) {
            //TODO: not enough decimals points
            return Integer.parseInt(values[1]);
        }
        values[1] = values[1].substring(offsetAfterDecimalPoint);
        int decimals = Integer.parseInt(values[1]);
        return decimals;
    }


}
