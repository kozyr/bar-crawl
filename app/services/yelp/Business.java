package services.yelp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Business {
	private String id;
	private String name;
    @JsonProperty("image_url")
	private String imageUrl;
	private String url;
    @JsonProperty("mobile_url")
	private String mobileUrl;
	private String phone;
    @JsonProperty("display_phone")
	private String displayPhone;
    @JsonProperty("review_count")
	private int reviewCount;
	private List<List<String>> categories;
	private double distance;
    private double rating;
    @JsonProperty("rating_img_url")
	private String ratingImgUrl;
    @JsonProperty("rating_img_url_small")
	private String ratingImgUrlSmall;
    @JsonProperty("snippet_text")
	private String snippetText;
    @JsonProperty("snippet_image_url")
	private String snippetImgUrl;
	private Location location;
    @JsonProperty("is_closed")
    private boolean isClosed;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMobileUrl() {
		return mobileUrl;
	}
	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDisplayPhone() {
		return displayPhone;
	}
	public void setDisplayPhone(String displayPhone) {
		this.displayPhone = displayPhone;
	}
	public int getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	public List<List<String>> getCategories() {
		return categories;
	}
	public void setCategories(List<List<String>> categories) {
		this.categories = categories;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getRatingImgUrl() {
		return ratingImgUrl;
	}
	public void setRatingImgUrl(String ratingImgUrl) {
		this.ratingImgUrl = ratingImgUrl;
	}
	public String getRatingImgUrlSmall() {
		return ratingImgUrlSmall;
	}
	public void setRatingImgUrlSmall(String ratingImgUrlSmall) {
		this.ratingImgUrlSmall = ratingImgUrlSmall;
	}
	public String getSnippetText() {
		return snippetText;
	}
	public void setSnippetText(String snippetText) {
		this.snippetText = snippetText;
	}
	public String getSnippetImgUrl() {
		return snippetImgUrl;
	}
	public void setSnippetImgUrl(String snippetImgUrl) {
		this.snippetImgUrl = snippetImgUrl;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
