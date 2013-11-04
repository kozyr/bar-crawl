package controllers;


import com.google.common.base.Stopwatch;
import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import repositories.BarCrawlRepository;
import services.RouteProvider;
import services.TourGuide;
import services.ZoneService;
import models.Zone;
import views.html.index;

import java.util.*;

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

    public Result barCrawl(double lat, double lon) {
        Stopwatch watch = new Stopwatch();
        watch.start();

        GeoPoint start = new GeoPoint(lat, lon);
        Zone zone = zoneService.buildZone(start, 2* MAX_BAR_CRAWL_DISTANCE);
        List<DirectedBlock> first = routeProvider.getBlockFromPoint(start, zone);
        Logger.info("Initial streets: "  + first);
        Path path = tourGuide.ratingBfs(first, zone, MAX_BAR_CRAWL_DISTANCE);

        watch.stop();
        Logger.info("Total time: " + watch);
        return ok(Json.toJson(path));
    }
}