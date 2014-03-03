package services.impl;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import de.cm.osm2po.routing.Graph;
import de.cm.osm2po.routing.OverlayGraph;
import de.cm.osm2po.routing.RoutingResultSegment;
import de.cm.osm2po.routing.TouchPoint;
import models.Block;
import models.DirectedBlock;
import models.GeoPoint;
import models.Zone;
import org.springframework.stereotype.Service;
import play.Logger;
import services.RouteProvider;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class RouteProviderImpl implements RouteProvider {
    private Graph walkGraph;

    @PostConstruct
    public void init() {
        Stopwatch graphLoader = new Stopwatch();
        graphLoader.start();

        File walkFile = new File("walk_2po.gph");

        walkGraph = new Graph(walkFile);

        graphLoader.stop();
        Logger.info(graphLoader + " to load the graph");
    }

    @Override
    public List<DirectedBlock> getBlockFromPoint(GeoPoint point, Zone zone) {
        List<DirectedBlock> result = new LinkedList<>();
        TouchPoint touchPoint = TouchPoint.create(walkGraph, (float) point.getLat(), (float) point.getLon());

        Stopwatch overlayLoader = new Stopwatch();
        overlayLoader.start();
        OverlayGraph overlay = new OverlayGraph(walkGraph);
        overlay.insertTouchPoints(touchPoint);
        int [] edgeIdxs = overlay.getEdgeIdxs();
        overlayLoader.stop();

        for (int edgeId : edgeIdxs) {
            Optional<DirectedBlock> block = getDirectedStreetFromEdgeId(walkGraph, edgeId, zone);
            if (block.isPresent()) {
                result.add(block.get());
            }
        }

        return result;
    }

    private Optional<DirectedBlock> getDirectedStreetFromEdgeId(Graph graph, int edgeId, Zone zone) {
        Optional<DirectedBlock> result = Optional.absent();
        RoutingResultSegment segment = graph.lookupSegment(edgeId);

        Optional<Block> block = zone.getBlock(segment.getId());
        if (block.isPresent()) {
            result = Optional.of(new DirectedBlock(segment, block.get()));
        } else {
            Logger.warn("Could not find the block for " + edgeId);
        }
        return result;
    }


    @Override
    public List<DirectedBlock> findOutgoingStreets(int sourceId, Zone zone) {
        List<DirectedBlock> result = new ArrayList<>();
        int [] edges = walkGraph.findOutgoingEdges(sourceId);
        for (int edgeId : edges) {
            Optional<DirectedBlock> street = getDirectedStreetFromEdgeId(walkGraph, edgeId, zone);
            if (street.isPresent()) {
                result.add(street.get());
            }
        }

        return result;
    }
}
