package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
@Entity
public class Place implements Serializable  {
    @Id
    private String placeId;
    private double lat;
    private double lon;
    private String name;
    private String url;
    private String address;
    private String phone;
    private double rating;
    private int reviewCount;
    @Type(type="org.hibernate.spatial.GeometryType")
    private Point geom;

    public Place() {
        placeId = UUID.randomUUID().toString();
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String barId) {
        this.placeId = barId;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Point getGeom() {
        return geom;
    }

    public void setGeom(Point geom) {
        this.geom = geom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Place{");
        sb.append("placeId=").append(placeId);
        sb.append(", lat=").append(lat);
        sb.append(", lon=").append(lon);
        sb.append(", name='").append(name).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", rating=").append(rating);
        sb.append(", reviewCount=").append(reviewCount);
        sb.append(", geom=").append(geom);
        sb.append('}');
        return sb.toString();
    }
}

