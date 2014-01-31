package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Path {
    private List<DirectedBlock> edges = new ArrayList<>();

    public Path(DirectedBlock initial) {
        edges.add(initial);
    }

    public Path(Path other) {
        edges = new ArrayList<>(other.edges);
    }

    public Path extend(DirectedBlock edge) {
        Path copy = new Path(this);
        copy.edges.add(edge);

        return copy;
    }

    public double getAverageRating() {
        double average = 0;

        List<Double> ratings = new LinkedList<>();
        for (DirectedBlock edge : edges) {
            Block block = edge.getBlock();
            List<Place> places = block.getPlaces();

            for (Place b : places) {
                ratings.add(b.getRating());
            }
        }

        for (double rating : ratings) {
            average += rating;
        }
        if (ratings.size() > 0)
            average /= ratings.size();

        return average;
    }

    public int size() {
        return edges.size();
    }

    public List<DirectedBlock> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Path{");
        sb.append("edges=").append(edges);
        sb.append('}');
        return sb.toString();
    }

    public boolean alreadyHas(int edgeId) {
        boolean has = false;
        for (DirectedBlock e : edges) {
            if (e.getId() == edgeId) {
                has = true;
                break;
            }
        }

        return has;
    }

    @JsonIgnore
    public double getWalkingLength() {
        double length = 0;
        for (DirectedBlock edge : edges) {
            length += edge.getBlock().getStreet().getKm() * 1000;
        }

        return length;
    }
}
