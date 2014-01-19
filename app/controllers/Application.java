package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.libs.F;
import static play.libs.F.Promise.promise;
import play.libs.Json;
import play.mvc.Result;
import repositories.BarCrawlRepository;
import services.RouteProvider;
import services.TourGuide;
import services.ZoneService;
import models.Zone;
import views.html.index;

import java.util.*;

import static play.mvc.Results.async;
import static play.mvc.Results.notFound;
import static play.mvc.Results.ok;

@Controller
public class Application {
    @Autowired
    private BarCrawlRepository trackingPathRepository;
    @Autowired
    private ZoneService zoneService;
    @Autowired
    private RouteProvider routeProvider;
    @Autowired
    private TourGuide tourGuide;

    private static final int MILE = 1609;
    private static final int MAX_BAR_CRAWL_DISTANCE = 1000;

    public Result index() {
        return ok(index.render());
    }

    public F.Promise<Result> barCrawl(final double lat, final double lon) {
        F.Promise<Optional<Path>> barPromise = F.Promise.promise(
                new F.Function0<Optional<Path>>() {
                    public Optional<Path> apply() {
                        return calcBarPath(lat, lon);
                    }
                }
        );
        return barPromise.map(
                new F.Function<Optional<Path>, Result>() {
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

    private Optional<Path> calcBarPath(double lat, double lon) {
        GeoPoint start = new GeoPoint(lat, lon);
        Zone zone = zoneService.buildZone(start, 2* MAX_BAR_CRAWL_DISTANCE);
        List<DirectedBlock> first = routeProvider.getBlockFromPoint(start, zone);
        Logger.info("Initial streets: "  + first);
        Optional<Path> path = tourGuide.ratingBfs(first, zone, MAX_BAR_CRAWL_DISTANCE);

        return path;
    }
}