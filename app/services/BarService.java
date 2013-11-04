package services;

import models.Bar;
import models.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BarService {
    public List<Bar> findByLocation(GeoPoint location, double distance);
}
