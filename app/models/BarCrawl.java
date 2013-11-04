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
public class BarCrawl implements Serializable {

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
    private List<Bar> bars;
    private long createdOn;

    public BarCrawl() {
        bars = new ArrayList<>();
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

    public void addBar(Bar bar) {
        bars.add(bar);
    }

    public List<Bar> getBars() {
        return bars;
    }
    public void setBars(List<Bar> bars) {
        this.bars = bars;
    }



    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public Bar getStart() {
        Bar first = null;
        if (bars != null && bars.size() > 0) {
            first = bars.get(0);
        }

        return first;
    }

    public boolean isEmpty() {
        return bars.size() == 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BarCrawl{");
        sb.append("barCrawlId=").append(barCrawlId);
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", bars=").append(bars);
        sb.append(", createdOn=").append(createdOn);
        sb.append('}');
        return sb.toString();
    }
}
