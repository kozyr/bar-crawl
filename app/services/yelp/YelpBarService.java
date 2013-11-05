package services.yelp;

import com.google.common.base.Optional;
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
            Optional<Bar> bar = fromBusiness(business);
            if (bar.isPresent()) {
                Logger.info("Found " + bar);
                bars.add(bar.get());
            }
        }

        return bars;
    }

    private Optional<Bar> fromBusiness(Business b) {
        Optional<Bar> result = Optional.absent();
        boolean isOk = false;
        double lat = 0.0;
        double lon = 0.0;
        String address = b.getLocation().getAddress().get(0);
        Coordinate coord = b.getLocation().getCoordinate();
        if (coord == null) {
            Optional<GeoPoint> point = GeocodeHelper.getGeoPoint(
                    address + ", " + Play.application().configuration().getString("city"));
            if (point.isPresent()) {
                lat = point.get().getLat();
                lon = point.get().getLon();
                isOk = !b.isClosed();
            }
        } else {
            lat = coord.getLatitude();
            lon = coord.getLongitude();
            isOk = !b.isClosed();
        }
        if (isOk) {
            Bar bar = new Bar();
            bar.setBarId(b.getId());
            bar.setAddress(address);
            bar.setLat(lat);
            bar.setLon(lon);
            bar.setName(b.getName());
            bar.setUrl(b.getUrl());

            bar.setPhone(b.getDisplayPhone());
            bar.setReviewCount(b.getReviewCount());
            bar.setRating(b.getRating());
            bar.setGeom(GeoUtil.fromLatLon(bar.getLat(), bar.getLon()));
            result = Optional.of(bar);
        }

        return result;
    }
}
