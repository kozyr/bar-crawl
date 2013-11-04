var BarCrawl= function(map) {
    this.crawlRoute = null;
    this.map = map;
    this.markers = new Array();
    this.lineSymbol = {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 8,
        strokeColor: '#393'
    };
};

BarCrawl.prototype.init = function() {
    var that = this;
    // this.findLocation();
    google.maps.event.addListener(this.map, 'dblclick', function(event) {
        that.getCrawlRoute(event.latLng);
    });
}

BarCrawl.prototype.findLocation = function() {
    var that = this;
    navigator.geolocation.getCurrentPosition(function(position) {
        var myLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
        that.changeLocation(myLocation);
    });
};

BarCrawl.prototype.changeLocation = function(myLocation) {
    this.map.setCenter(myLocation);
    this.map.setZoom(16);
    this.clearMap();
    this.addMarker(myLocation, "You are here");
};

BarCrawl.prototype.addMarker = function(position, title) {
    var marker = new google.maps.Marker({
        map: this.map,
        position: position,
        animation: google.maps.Animation.DROP,
        icon:  this.startIcon,
        title: title
    });

    this.markers.push(marker);
};

BarCrawl.prototype.clearMap = function() {
    for (var i = 0; i < this.markers.length; i++) {
        this.markers[i].setMap(null);
    }
    this.markers = new Array();
};

BarCrawl.prototype.animateCrawl = function() {
    var that = this;
    var count = 0;
    offsetId = window.setInterval(function() {
        count = (count + 1) % 200;

        var icons = that.crawlRoute.get('icons');
        icons[0].offset = (count / 2) + '%';
        that.crawlRoute.set('icons', icons);
    }, 40);
}


BarCrawl.prototype.getCrawlRoute = function(latLng) {
    var that = this;
    $.ajax({
        type:  "GET",
        dataType: 'json',
        url: "/bar-crawl",
        data: {
            lat: latLng.lat(),
            lon: latLng.lng()
        }
    }).success(function(response) {
            that.processRoute(latLng, response);
    });
}

BarCrawl.prototype.processRoute = function(startLatLng, jsonRoute) {
    var points = new Array();

    points.push(startLatLng);
    $.each(jsonRoute.edges, function(i, edge) {
        console.log(edge.block.street.osm_name);
        points.push(new google.maps.LatLng(edge.block.street.y2, edge.block.street.x2));
    });

    this.displayRoute(points);
}

BarCrawl.prototype.displayRoute = function(points) {
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

    var map = new google.maps.Map($("#bar-crawl-map")[0], mapOptions);

    var tourGuide = new BarCrawl(map);
    tourGuide.init();
});