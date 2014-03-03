package services;

import com.google.common.base.Optional;
import models.DirectedBlock;
import models.Path;
import models.Zone;

import java.util.List;

/**
 * Created by sergei on 3/3/14.
 */
public interface TourGuide {
    public Optional<Path> ratingBfs(List<DirectedBlock> start, Zone zone, int distance);
}
