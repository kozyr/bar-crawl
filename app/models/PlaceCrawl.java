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
    private UUID barCrawlId;
    private String userId;
    @ManyToMany
    @Cascade({CascadeType.ALL})
    @JoinTable(name = "BAR_CRAWL_BAR", joinColumns = { @JoinColumn(name = "barCrawlId") },
            inverseJoinColumns = { @JoinColumn(name = "barId") })
    private List<Place> places;
    private long createdOn;

    public PlaceCrawl() {
        places = new ArrayList<>();
    }

    public UUID getBarCrawlId() {
        return barCrawlId;
    }
    public void setBarCrawlId(UUID barCrawlId) {
        this.barCrawlId = barCrawlId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addBar(Place place) {
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
        sb.append("barCrawlId=").append(barCrawlId);
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", places=").append(places);
        sb.append(", createdOn=").append(createdOn);
        sb.append('}');
        return sb.toString();
    }
}
