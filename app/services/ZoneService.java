package services;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.F.Function;
import play.libs.F.Function0;
import repositories.StreetRepository;
import scala.concurrent.impl.Future;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ZoneService {
    @Autowired
    private StreetRepository streetRepository;
    @Qualifier("foursquare")
    @Autowired
    private BarService barService;

    public Zone buildZone(final GeoPoint center, final int distance) {
        Promise<List<Street>> streetPromise = Promise.promise(
                new Function0<List<Street>>() {
                    public List<Street> apply() {
                        return streetRepository.findByLocation(center.getGeom(), distance);
                    }
                }
        );

        Promise<List<Bar>> barPromise = Promise.promise(
                new Function0<List<Bar>>() {
                    public List<Bar> apply() {
                        return barService.findByLocation(center, distance);
                    }
                }
        );

        Promise<Zone> result = Promise.sequence(streetPromise, barPromise).map(
                new Function<List<List<? extends Object>>, Zone>() {
                    @Override
                    public Zone apply(List<List<? extends Object>> lists) throws Throwable {
                        return new Zone((List<Street>) lists.get(0), (List<Bar>) lists.get(1));
                    }
                }
        );

        return result.get(10, TimeUnit.SECONDS);
    }

}
