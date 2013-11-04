package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.LineString;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="chicago_walking_2po_4pgr")
public class Street {
    protected long osm_id;
    protected String osm_name;
    protected String osm_meta;
    protected long osm_source_id;
    protected long osm_target_id;
    protected int clazz;
    protected int flags;
    protected int source;
    protected int target;
    protected double km;
    protected int kmh;
    protected double cost;
    protected double reverse_cost;
    protected double x1;
    protected double y1;
    protected double x2;
    protected double y2;

    @Type(type="org.hibernate.spatial.GeometryType")
    protected LineString geog;

    @Type(type="org.hibernate.spatial.GeometryType")
    protected LineString geom;

    @Id
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOsm_id() {
        return osm_id;
    }

    public String getOsm_name() {
        return osm_name;
    }

    public String getOsm_meta() {
        return osm_meta;
    }

    public long getOsm_source_id() {
        return osm_source_id;
    }

    public long getOsm_target_id() {
        return osm_target_id;
    }

    public int getClazz() {
        return clazz;
    }

    public int getFlags() {
        return flags;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }

    public double getKm() {
        return km;
    }

    public int getKmh() {
        return kmh;
    }

    public double getCost() {
        return cost;
    }

    public double getReverse_cost() {
        return reverse_cost;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    @JsonIgnore
    public LineString getGeog() {
        return geog;
    }



    public void setOsm_id(long osm_id) {
        this.osm_id = osm_id;
    }

    public void setOsm_name(String osm_name) {
        this.osm_name = osm_name;
    }

    public void setOsm_meta(String osm_meta) {
        this.osm_meta = osm_meta;
    }

    public void setOsm_source_id(long osm_source_id) {
        this.osm_source_id = osm_source_id;
    }

    public void setOsm_target_id(long osm_target_id) {
        this.osm_target_id = osm_target_id;
    }

    public void setClazz(int clazz) {
        this.clazz = clazz;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public void setKmh(int kmh) {
        this.kmh = kmh;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setReverse_cost(double reverse_cost) {
        this.reverse_cost = reverse_cost;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public void setGeog(LineString geog) {
        this.geog = geog;
    }

    @JsonIgnore
    public LineString getGeom() {
        return geom;
    }

    public void setGeom(LineString geom) {
        this.geom = geom;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Street{");
        sb.append("osm_id=").append(osm_id);
        sb.append(", osm_name='").append(osm_name).append('\'');
        sb.append(", osm_meta='").append(osm_meta).append('\'');
        sb.append(", osm_source_id=").append(osm_source_id);
        sb.append(", osm_target_id=").append(osm_target_id);
        sb.append(", clazz=").append(clazz);
        sb.append(", flags=").append(flags);
        sb.append(", source=").append(source);
        sb.append(", target=").append(target);
        sb.append(", km=").append(km);
        sb.append(", kmh=").append(kmh);
        sb.append(", cost=").append(cost);
        sb.append(", reverse_cost=").append(reverse_cost);
        sb.append(", x1=").append(x1);
        sb.append(", y1=").append(y1);
        sb.append(", x2=").append(x2);
        sb.append(", y2=").append(y2);
        sb.append(", geog=").append(geog);
        sb.append(", geom=").append(geom);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Street street = (Street) o;

        if (id != street.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
