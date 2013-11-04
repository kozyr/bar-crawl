package services.yelp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"span"})
public class Region {
	private Center center;

	public Center getCenter() {
		return center;
	}

	public void setCenter(Center center) {
		this.center = center;
	}
}
