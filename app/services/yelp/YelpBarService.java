package services.yelp;

import models.Bar;
import models.GeoPoint;
import org.springframework.stereotype.Service;
import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.Json;
import services.BarService;
import util.GeoUtil;
import util.GeocodeHelper;

import java.util.LinkedList;
import java.util.List;

@Service
public class YelpBarService implements BarService {

    @Override
    public List<Bar> findByLocation(GeoPoint location, double distance) {
        List<Bar> bars = new LinkedList<>();
        Configuration config = Play.application().configuration();
        Yelp yelp = new Yelp(config.getString("yelp.consumerKey"),
                config.getString("yelp.consumerSecret"),
                config.getString("yelp.token"),
                config.getString("yelp.tokenSecret"));
        String json = yelp.search("bars", location.getLat(), location.getLon(), distance);
        YelpSearchResult yelpResult = Json.fromJson(Json.parse(json), YelpSearchResult.class);

        for (Business business : yelpResult.getBusinesses()) {
            Bar bar = fromBusiness(business);
            Logger.info("Found " + bar);
            bars.add(bar);
        }

        return bars;
    }

    private Bar fromBusiness(Business b) {
        Bar bar = new Bar();
        bar.setBarId(b.getId());
        bar.setAddress(b.getLocation().getAddress().get(0));
        Coordinate coord = b.getLocation().getCoordinate();
        if (coord != null) {
            bar.setLat(coord.getLatitude());
            bar.setLon(coord.getLongitude());
        } else {
            GeoPoint point = GeocodeHelper.getGeoPoint(
                    bar.getAddress() + ", " + Play.application().configuration().getString("city"));
            if (point != null) {
                bar.setLat(point.getLat());
                bar.setLon(point.getLon());
            }
        }
        bar.setName(b.getName());
        bar.setUrl(b.getUrl());

        bar.setPhone(b.getDisplayPhone());
        bar.setReviewCount(b.getReviewCount());
        bar.setRating(b.getRating());
        bar.setGeom(GeoUtil.fromLatLon(bar.getLat(), bar.getLon()));

        return bar;
    }
}
