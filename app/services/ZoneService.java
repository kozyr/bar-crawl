package services;

import com.google.common.base.Stopwatch;
import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import play.Logger;
import repositories.StreetRepository;

import java.util.List;

@Service
public class ZoneService {
    @Autowired
    private StreetRepository streetRepository;
    @Autowired
    private BarService barService;

    public Zone buildZone(GeoPoint center, int distance) {
        Stopwatch watch = new Stopwatch();
        watch.start();

        List<Street> streets = streetRepository.findByLocation(center.getGeom(), distance);
        if (Logger.isDebugEnabled()) {
            for (Street s : streets) {
                Logger.debug(s.toString());
            }
        }
        List<Bar> bars = barService.findByLocation(center, distance);
        if (Logger.isDebugEnabled()) {
            for (Bar b : bars) {
                Logger.debug(b.toString());
            }

        }

        watch.stop();
        Logger.info("Time to read in data: " + watch);

        Zone zone = new Zone(streets, bars);

        return zone;
    }

}
