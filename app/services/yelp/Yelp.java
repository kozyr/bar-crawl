package services.yelp;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Example for accessing the Yelp API.
 */
public class Yelp {

    OAuthService service;
    Token accessToken;

    /**
     * Setup the Yelp API OAuth credentials.
     *
     * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
     *
     * @param consumerKey Consumer key
     * @param consumerSecret Consumer secret
     * @param token Token
     * @param tokenSecret Token secret
     */
    public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    /**
     * Search with term and location.
     *
     * @param term Search term
     * @param latitude Latitude
     * @param longitude Longitude
     * @return JSON string response
     */
    public String search(String term, double latitude, double longitude, double radius) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
        request.addQuerystringParameter("radius_filter", String.valueOf(radius));
        // sort by distance
        request.addQuerystringParameter("sort", "1");
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    // CLI
    public static void main(String[] args) {
        // Update tokens here from Yelp developers site, Manage API access.
        String consumerKey = "";
        String consumerSecret = "";
        String token = "";
        String tokenSecret = "";

        Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
        String response = yelp.search("burritos", 30.361471, -87.164326, 5 * 1609);

        System.out.println(response);
    }
}
