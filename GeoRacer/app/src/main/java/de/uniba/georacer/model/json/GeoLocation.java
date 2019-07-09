package de.uniba.georacer.model.json;

import de.uniba.georacer.service.positioning.UTM;
import de.uniba.georacer.service.positioning.WGS84;

public class GeoLocation {

    private double longitude;
    private double latitude;

    private double northling;
    private double eastling;

    private GeoLocation(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;

    }

    /**
     * Builds a GeoLocation from WGS84 coordinates
     *
     * @param latitude  wgs84 latitude
     * @param longitude wgs84 longitude
     * @return an instance of GeoLocation
     */
    public static GeoLocation fromWGS84(double latitude, double longitude) {
        GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        WGS84 wgs84 = new WGS84(latitude, longitude);
        UTM utm = new UTM(wgs84);
        geoLocation.setNorthling(utm.getNorthing());
        geoLocation.setEastling(utm.getEasting());
        return geoLocation;
    }

    /**
     * Builds a GeoLocation from UTM coordinates. Simplification: Always in Zone 32U
     *
     * @param eastling  east in utm
     * @param northling north in utm
     * @return an instance of GeoLocation
     */
    public static GeoLocation fromUTM(double eastling, double northling) {
        UTM utm = new UTM(32, 'U', eastling, northling);
        WGS84 wgs84 = new WGS84(utm);
        GeoLocation geoLocation = new GeoLocation(wgs84.getLatitude(), wgs84.getLongitude());
        geoLocation.setEastling(utm.getEasting());
        geoLocation.setNorthling(utm.getNorthing());
        return geoLocation;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getEastling() {
        return eastling;
    }

    public double getNorthling() {
        return northling;
    }

    public void setNorthling(double northling) {
        this.northling = northling;
    }

    public void setEastling(double eastling) {
        this.eastling = eastling;
    }
}
