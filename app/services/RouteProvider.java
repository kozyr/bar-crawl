package services;

import models.DirectedBlock;
import models.GeoPoint;
import models.Zone;

import java.util.List;

/**
 * Created by sergei on 3/3/14.
 */
public interface RouteProvider {
    public List<DirectedBlock> getBlockFromPoint(GeoPoint point, Zone zone);

    public List<DirectedBlock> findOutgoingStreets(int sourceId, Zone zone);
}
