package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;
import util.GeoUtil;

public final class GeoPoint {
    private final double lat;
    private final double lon;
    private final Point geom;

    public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;

        geom = GeoUtil.fromLatLon(lat, lon);
    }

    public double getLat() {
        return lat;
    }

    @JsonIgnore
    public Point getGeom() {
        return geom;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return " ( " + lat + ", " + lon + " ) ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoPoint geoPoint = (GeoPoint) o;

        if (Double.compare(geoPoint.lat, lat) != 0) return false;
        if (Double.compare(geoPoint.lon, lon) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
