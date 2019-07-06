package de.uniba.georacer.model.json;

public class GeoLocation implements Comparable<GeoLocation> {

    private double longitude;
    private double latitude;


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

    public int getLongitudeOffset(int offsetAfterDecimalPoint) {
        return getOffset(longitude, offsetAfterDecimalPoint);
    }

    public int getLatitudeOffset(int offsetAfterDecimalPoint) {
        return getOffset(latitude, offsetAfterDecimalPoint);
    }

    public double getLongitudeBase(int offsetAfterDecimalPoint) {
        return getBase(longitude, offsetAfterDecimalPoint);
    }

    public double getLatitudeBase(int offsetAfterDecimalPoint) {
        return getBase(latitude, offsetAfterDecimalPoint);
    }

    private double getBase(double value, int offsetAfterDecimalPoint) {

        String valueString = String.valueOf(value);
        String[] values = String.valueOf(value).split("\\.");
        int index = +values[0].length() + 1 + offsetAfterDecimalPoint; // ad everything before and +1 for the decimal point
        if (index >= valueString.length()) {
            throw new IllegalArgumentException("Offset of " + offsetAfterDecimalPoint + " is too big for value " + value);
        }
        double base = Double.parseDouble(valueString.substring(0, index));
        return base;

    }


    public int getOffset(double value, int offsetAfterDecimalPoint) {
        String[] values = String.valueOf(value).split("\\.");
        if (values[1].length() < offsetAfterDecimalPoint) {
            //TODO: not enough decimals points
            return Integer.parseInt(values[1]);
        }
        values[1] = values[1].substring(offsetAfterDecimalPoint - 1);
        int decimals = Integer.parseInt(values[1]);
        return decimals;
    }

    @Override
    public int compareTo(GeoLocation location) {
        // We need some order to be able to test reliably
        return (int) (this.longitude - location.getLongitude());
    }


}
