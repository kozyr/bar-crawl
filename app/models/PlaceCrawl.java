package models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("serial")
@Entity
public class PlaceCrawl implements Serializable {

    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID placeCrawlId;
    private String userId;
    @ManyToMany
    @Cascade({CascadeType.ALL})
    @JoinTable(name = "PLACE_CRAWL_BAR", joinColumns = { @JoinColumn(name = "placeCrawlId") },
            inverseJoinColumns = { @JoinColumn(name = "barId") })
    private List<Place> places;
    private long createdOn;

    public PlaceCrawl() {
        places = new ArrayList<>();
    }

    public UUID getPlaceCrawlId() {
        return placeCrawlId;
    }
    public void setBarCrawlId(UUID barCrawlId) {
        this.placeCrawlId = barCrawlId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addPlace(Place place) {
        places.add(place);
    }

    public List<Place> getPlaces() {
        return places;
    }
    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public Place getStart() {
        Place first = null;
        if (places != null && places.size() > 0) {
            first = places.get(0);
        }

        return first;
    }

    public boolean isEmpty() {
        return places.size() == 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlaceCrawl{");
        sb.append("placeCrawlId=").append(placeCrawlId);
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", places=").append(places);
        sb.append(", createdOn=").append(createdOn);
        sb.append('}');
        return sb.toString();
    }
}
