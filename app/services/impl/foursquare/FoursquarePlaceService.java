package services.impl.foursquare;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import models.Place;
import models.GeoPoint;
import org.springframework.stereotype.Service;
import play.Configuration;
import play.Logger;
import play.Play;
import services.PlaceService;
import util.GeoUtil;

import java.util.*;

/**
 * User: sergei
 */
@Service(value="foursquare")
public class FoursquarePlaceService implements PlaceService {

    @Override
    public List<Place> findByLocation(GeoPoint location, double distance) {
        List<Place> places = new LinkedList<>();

        Configuration config = Play.application().configuration();
        String clientId = config.getString("foursquare.clientId");
        String clientSecret = config.getString("foursquare.clientSecret");
        String placeType = config.getString("foursquare.placeType");

        // First we need a initialize FoursquareApi.
        FoursquareApi foursquareApi = new FoursquareApi(clientId, clientSecret, null);

        // After client has been initialized we can make queries.
        try {
            Map<String, String> params = new HashMap<>();
            params.put("ll", location.toText());
            params.put("radius", String.valueOf(distance));
            params.put("intent", "browse");
            params.put("query", placeType);

            Result<VenuesSearchResult> result = foursquareApi.venuesSearch(params);

            if (result.getMeta().getCode() == 200) {
                // if query was ok we can finally we do something with the data
                for (CompactVenue venue : result.getResult().getVenues()) {
                    Place place = barFromVenue(venue);
                    // Logger.info("Received " + place);
                    places.add(place);
                }
            } else {
                Logger.warn("  detail: " + result.getMeta().getErrorDetail());
            }
        } catch (FoursquareApiException e) {
            Logger.error("Could not talk to Foursquare at " + location, e);
        }

        return places;
    }

    private Place barFromVenue(CompactVenue venue) {
        Place place = new Place();
        place.setName(venue.getName());
        place.setPlaceId(venue.getId());
        place.setAddress(venue.getLocation().getAddress());
        place.setPhone(venue.getContact().getPhone());
        place.setLat(venue.getLocation().getLat());
        place.setLon(venue.getLocation().getLng());
        place.setRating(venue.getStats().getCheckinsCount());
        place.setUrl(venue.getUrl());
        place.setGeom(GeoUtil.fromLatLon(place.getLat(), place.getLon()));

        return place;
    }

}
