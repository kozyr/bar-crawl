package util;

import java.io.BufferedReader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.GeoPoint;
import play.Logger;

public class GeocodeHelper {
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;

    private static final String GOOGLE_URL = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=";

    enum StatusCode {
        OK,
        ZERO_RESULTS,
        OVER_QUERY_LIMIT,
        REQUEST_DENIED,
        INVALID_REQUEST,
        UNKNOWN_ERROR;

        public boolean isFine() {
            return this == OK;
        }

        public boolean isRejected() {
            return this == OVER_QUERY_LIMIT || this == REQUEST_DENIED;
        }
    }

    public static GeoPoint getGeoPoint(String address)  {
        GeoPoint result = null;
        try {
            JsonNode root = getGeoData(address);
            if (isOK(root)) {
                result = getLatLng(root);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }

        return result;
    }

    private static GeoPoint getLatLng(JsonNode root) {
        GeoPoint result = null;
        JsonNode location = null;
        JsonNode results = root.get("results");

        //if we have a possible location, take the first one
        if (results != null && results.size() > 0){
            location = results.get(0).get("geometry").get("location");
        }

        if (location != null) {
            result = new GeoPoint(location.get("lat").asDouble(), location.get("lng").asDouble());
        }

        return result;
    }

    private static boolean isOK(JsonNode root) {
        String status = root.get("status").asText();
        StatusCode code = StatusCode.valueOf(status);
        return code.isFine();
    }


    private static JsonNode getGeoData(String address) throws GeocodeException {
        String json = null;
        Logger.info("Google Address: " + address);
        try {
            address = URLDecoder.decode(address, "UTF-8");
            String url = GOOGLE_URL + URLEncoder.encode(address, "UTF-8");
            json = fetchData(url);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readValue(json, JsonNode.class);
            return rootNode;
        } catch (Exception e) {
            throw new GeocodeException("Fetched " + json + " \nand could not parse it", e);
        }
    }

    private static String fetchData(String strUrl) throws IOException {
        Logger.info("Google URL: " + strUrl);
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(strUrl);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }

        return sb.toString();
    }



    public static void main(String[] args) {
        GeoPoint sf = getGeoPoint("1600 Amphitheatre Parkway,Mountain View,CA");
        System.out.println(sf);
    }
}
