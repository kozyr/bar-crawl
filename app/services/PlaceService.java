package services;

import models.Place;
import models.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaceService {
    public List<Place> findByLocation(GeoPoint location, double distance);
}
