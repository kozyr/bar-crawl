package services.impl;

import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import play.libs.F.Promise;
import play.libs.F.Function;
import play.libs.F.Function0;
import repositories.StreetRepository;
import services.PlaceService;
import services.ZoneService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ZoneServiceImpl implements ZoneService {
    @Autowired
    private StreetRepository streetRepository;
    @Qualifier("foursquare")
    @Autowired
    private PlaceService placeService;

    @Override
    public Zone buildZone(final GeoPoint center, final int distance) {
        Promise<List<Street>> streetPromise = Promise.promise(
                new Function0<List<Street>>() {
                    public List<Street> apply() {
                        return streetRepository.findByLocation(center.getGeom(), distance);
                    }
                }
        );

        Promise<List<Place>> placePromise = Promise.promise(
                new Function0<List<Place>>() {
                    public List<Place> apply() {
                        return placeService.findByLocation(center, distance);
                    }
                }
        );

        Promise<Zone> result = Promise.sequence(streetPromise, placePromise).map(
                new Function<List<List<? extends Object>>, Zone>() {
                    @Override
                    public Zone apply(List<List<? extends Object>> lists) throws Throwable {
                        return new Zone((List<Street>) lists.get(0), (List<Place>) lists.get(1));
                    }
                }
        );

        return result.get(10, TimeUnit.SECONDS);
    }

}
