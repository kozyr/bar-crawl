package models;

import com.google.common.base.Optional;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.linearref.LinearLocation;
import com.vividsolutions.jts.linearref.LocationIndexedLine;

import java.util.*;

public class Zone {
    private final SpatialIndex index;
    private final Map<Integer, Street> streetMap;
    private final Map<Integer, List<Bar>> barMap;

    private static final int AROUND = 50;

    public Zone() {
        index = new STRtree();
        streetMap = new HashMap<>();
        barMap = new HashMap<>();
    }

    public Zone(List<Street> streets, List<Bar> bars) {
        this();
        for (Street s : streets) {
            addStreet(s);
        }

        for (Bar b : bars) {
            mapBar(b);
        }
    }

    private void addStreet(Street street) {
        streetMap.put(street.getId(), street);
        LineString line = street.getGeom();
        index.insert(line.getEnvelopeInternal(), street);
    }

    private void mapBar(Bar bar) {
        Point point =  bar.getGeom();
        Street winner = mapPoint(point, bar.getName());

        if (winner != null) {
            List<Bar> mapped = barMap.get(winner.getId());
            if (mapped == null) {
                mapped = new LinkedList<>();
                barMap.put(winner.getId(), mapped);
            }
            mapped.add(bar);
        }
    }

    private Street mapPoint(Point point, String name) {
        Coordinate coord = new Coordinate(point.getX(), point.getY());
        Envelope search = point.getEnvelopeInternal();
        search.expandBy(AROUND);

        List<Street> streets = index.query(search);
        Street winner = null;

        double minDist = Double.MAX_VALUE;
        for (Street test : streets) {
            LocationIndexedLine line = new LocationIndexedLine(test.getGeom());
            LinearLocation here = line.project(coord);
            Coordinate snap = line.extractPoint(here);
            double dist = snap.distance(coord);
            if (dist < minDist) {
                minDist = dist;
                winner = test;
            }
        }

        return winner;
    }

    public Optional<Block> getBlock(int streetId) {
        Optional result = Optional.absent();
        Street s = streetMap.get(streetId);
        if (s != null) {
            Block block = new Block(s);
            block.setBars(queryNearbyBars(streetId));
            result = Optional.of(block);
        }

        return result;
    }

    private List<Bar> queryNearbyBars(int streetId) {
        List<Bar> result = barMap.get(streetId);
        if (result == null) {
            result = Collections.emptyList();
        }

        return result;
    }
}
