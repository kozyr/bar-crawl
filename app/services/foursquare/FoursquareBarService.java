package services.foursquare;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import models.Bar;
import models.GeoPoint;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Foursquare2Api;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import org.springframework.stereotype.Service;
import play.Configuration;
import play.Logger;
import play.Play;
import services.BarService;
import util.GeoUtil;

import java.text.MessageFormat;
import java.util.*;

/**
 * User: sergei
 * Date: 1/18/14
 * Time: 8:06 PM
 */
@Service(value="foursquare")
public class FoursquareBarService implements BarService {

    @Override
    public List<Bar> findByLocation(GeoPoint location, double distance) {
        List<Bar> bars = new LinkedList<>();

        Configuration config = Play.application().configuration();
        String clientId = config.getString("foursquare.clientId");
        String clientSecret = config.getString("foursquare.clientSecret");

        // First we need a initialize FoursquareApi.
        FoursquareApi foursquareApi = new FoursquareApi(clientId, clientSecret, null);

        // After client has been initialized we can make queries.
        try {
            Map<String, String> params = new HashMap<>();
            params.put("ll", location.toText());
            params.put("radius", String.valueOf(1000));
            params.put("intent", "browse");
            params.put("query", "bar");

            Result<VenuesSearchResult> result = foursquareApi.venuesSearch(params);

            if (result.getMeta().getCode() == 200) {
                // if query was ok we can finally we do something with the data
                for (CompactVenue venue : result.getResult().getVenues()) {
                    Bar bar = barFromVenue(venue);
                    // Logger.info("Received " + bar);
                    bars.add(bar);
                }
            } else {
                Logger.warn("  detail: " + result.getMeta().getErrorDetail());
            }
        } catch (FoursquareApiException e) {
            Logger.error("Could not talk to Foursquare at " + location, e);
        }

        return bars;
    }

    private Bar barFromVenue(CompactVenue venue) {
        Bar bar = new Bar();
        bar.setName(venue.getName());
        bar.setBarId(venue.getId());
        bar.setAddress(venue.getLocation().getAddress());
        bar.setPhone(venue.getContact().getPhone());
        bar.setLat(venue.getLocation().getLat());
        bar.setLon(venue.getLocation().getLng());
        bar.setRating(venue.getStats().getCheckinsCount());
        bar.setUrl(venue.getUrl());
        bar.setGeom(GeoUtil.fromLatLon(bar.getLat(), bar.getLon()));

        return bar;
    }

}
