package models;

import java.util.LinkedList;
import java.util.List;

public class Block {
    private Street street;
    private List<Place> places;

    public Block(Street street) {
        this.street = street;
        places = new LinkedList<>();
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> businesses) {
        this.places = businesses;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }
}
