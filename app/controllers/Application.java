package controllers;


import com.google.common.base.Optional;
import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.libs.F.Promise;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.Json;
import play.mvc.Result;
import services.TourGuide;
import services.ZoneService;
import services.RouteProvider;
import models.Zone;
import views.html.index;

import java.util.*;

import static play.mvc.Results.notFound;
import static play.mvc.Results.ok;

@Controller
public class Application {
    @Autowired
    private ZoneService zoneService;
    @Autowired
    private RouteProvider routeProvider;
    @Autowired
    private TourGuide tourGuide;

    private static final int MILE = 1609;
    private static final int MAX_PLACE_CRAWL_DISTANCE = 1000;

    public Result index() {
        return ok(index.render());
    }

    public Promise<Result> placeCrawl(final double lat, final double lon) {
        Promise<Optional<Path>> placePromise = Promise.promise(
                new Function0<Optional<Path>>() {
                    public Optional<Path> apply() {
                        return calcPlacePath(lat, lon);
                    }
                }
        );
        return placePromise.map(
                new Function<Optional<Path>, Result>() {
                    public Result apply(Optional<Path> path) {
                        if (path.isPresent()) {
                            return ok(Json.toJson(path.get()));
                        } else {
                            return notFound();
                        }
                    }
                }
        );
    }

    private Optional<Path> calcPlacePath(double lat, double lon) {
        GeoPoint start = new GeoPoint(lat, lon);
        Zone zone = zoneService.buildZone(start, 2* MAX_PLACE_CRAWL_DISTANCE);
        List<DirectedBlock> first = routeProvider.getBlockFromPoint(start, zone);
        Logger.info("Initial streets: "  + first);
        Optional<Path> path = tourGuide.ratingBfs(first, zone, MAX_PLACE_CRAWL_DISTANCE);

        return path;
    }
}