package services;

import models.GeoPoint;
import models.Zone;

/**
 * Created by sergei on 3/3/14.
 */
public interface ZoneService {
    public Zone buildZone(GeoPoint center, int distance);
}
