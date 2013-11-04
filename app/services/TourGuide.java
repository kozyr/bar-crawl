package services;

import com.google.common.base.Optional;
import models.DirectedBlock;
import models.Path;
import models.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import play.Logger;

import java.util.*;

/**
 * Drives around the zone and calculates parking probabilities
 */
@Service
public class TourGuide {
    @Autowired
    private RouteProvider router;
    @Autowired
    private ZoneService zoneService;

    public Optional<Path> ratingBfs(List<DirectedBlock> start, Zone zone, int distance) {
        Queue<DirectedBlock> queue = new LinkedList<>();
        Set<DirectedBlock> visited = new HashSet<>();
        Map<DirectedBlock, Path> map = new HashMap<>();
        for (DirectedBlock one : start) {
            queue.add(one);
        }

        while (!queue.isEmpty()) {
            DirectedBlock node = queue.remove();
            if (!visited.contains(node)) {
                visited.add(node);

                Path path = map.get(node);
                if (path == null) {
                    path = new Path(node);
                } else {
                    if (path.alreadyHas(node.getId())) {
                        queue.remove(node);
                        map.remove(node);
                        continue;
                    }
                    path = path.extend(node);
                }

                List<DirectedBlock> out = router.findOutgoingStreets(node.getTargetId(), zone);

                for (DirectedBlock street : out) {
                    if (path.getWalkingLength() < distance) {
                        map.remove(node);
                        map.put(street, path);
                        queue.add(street);
                    }
                }
            }
        }

        Logger.info("Number of paths " + map.size());
        Optional<Path> winner = Optional.absent();
        double average = 0;
        for (DirectedBlock street : map.keySet()) {
            Path path = map.get(street);
            if (path.getAverageRating() > average) {
                winner = Optional.of(path);
                average = path.getAverageRating();
            }
        }

        Logger.info("And the winner is " + winner);

        return winner;
    }
}
