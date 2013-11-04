package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.List;

public class Block {
    private Street street;
    private List<Bar> bars;

    public Block(Street street) {
        this.street = street;
        bars = new LinkedList<>();
    }

    public List<Bar> getBars() {
        return bars;
    }

    public void setBars(List<Bar> businesses) {
        this.bars = businesses;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }
}
