var PlaceCrawl= function(map) {
    this.crawlRoute = null;
    this.map = map;
    this.markers = new Array();
    this.placeIcon = "/assets/images/drink.png";
    this.startIcon = "/assets/images/group.png";
    this.endIcon = "/assets/images/finish.png";
    this.lineSymbol = {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 8,
        strokeColor: '#393'
    };
};

PlaceCrawl.prototype.init = function() {
    var that = this;
    // this.findLocation();
    google.maps.event.addListener(this.map, 'dblclick', function(event) {
        that.getCrawlRoute(event.latLng);
    });
}

PlaceCrawl.prototype.findLocation = function() {
    var that = this;
    navigator.geolocation.getCurrentPosition(function(position) {
        var myLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
        that.changeLocation(myLocation);
    });
};

PlaceCrawl.prototype.changeLocation = function(myLocation) {
    this.map.setCenter(myLocation);
    this.map.setZoom(16);
    this.clearMap();
    // this.addMarker(myLocation, "You are here");
};

PlaceCrawl.prototype.addMarker = function(position, title, icon) {
    var marker = new google.maps.Marker({
        map: this.map,
        position: position,
        animation: google.maps.Animation.DROP,
        icon:  icon,
        title: title
    });

    this.markers.push(marker);
};

PlaceCrawl.prototype.clearMap = function() {
    for (var i = 0; i < this.markers.length; i++) {
        this.markers[i].setMap(null);
    }
    this.markers = new Array();
};

PlaceCrawl.prototype.animateCrawl = function() {
    var that = this;
    var count = 0;
    offsetId = window.setInterval(function() {
        count = (count + 1) % 200;

        var icons = that.crawlRoute.get('icons');
        icons[0].offset = (count / 2) + '%';
        that.crawlRoute.set('icons', icons);
    }, 40);
}


PlaceCrawl.prototype.getCrawlRoute = function(latLng) {
    var that = this;
    $.ajax({
        type:  "GET",
        dataType: 'json',
        url: "/place-crawl",
        data: {
            lat: latLng.lat(),
            lon: latLng.lng()
        }
    }).success(function(response) {
        that.processRoute(latLng, response);
    });
}

PlaceCrawl.prototype.processRoute = function(startLatLng, jsonRoute) {
    var that = this;
    var points = new Array();

    this.clearMap();
    points.push(startLatLng);
    this.addMarker(startLatLng, "You are here", this.startIcon);

    var blockEnd = null;
    $.each(jsonRoute.edges, function(i, edge) {
        console.log(edge.block.street.osm_name + " (" + edge.start.lat + ", " +  edge.start.lon + ") -> (" + edge.end.lat + ", " + edge.end.lon + ")");
        blockEnd = new google.maps.LatLng(edge.end.lat, edge.end.lon);
        points.push(blockEnd);
        $.each(edge.block.places, function(j, place) {
            that.addMarker(new google.maps.LatLng(place.lat, place.lon), place.name, that.placeIcon);
        });
    });

    this.addMarker(blockEnd, "The End", this.endIcon);
    this.displayRoute(points);
}

PlaceCrawl.prototype.displayRoute = function(points) {
    this.map.setCenter(points[0]);
    if (this.crawlRoute) {
        this.crawlRoute.setPath(points);
    } else {
        this.crawlRoute = new google.maps.Polyline({
            path: points,
            icons: [{
                icon: this.lineSymbol,
                offset: '100%'
            }],
            strokeOpacity: 0.6,
            strokeWeight: 5,
            strokeColor: '#0000FF',
            map: this.map
        });
    }

    this.animateCrawl();
}

$(document).ready(function() {
    var mapOptions = {
        center: new google.maps.LatLng(41.947372, -87.655788),
        zoom: 17,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        disableDoubleClickZoom: true
    };

    var map = new google.maps.Map($("#place-crawl-map")[0], mapOptions);

    var tourGuide = new PlaceCrawl(map);
    tourGuide.init();
});