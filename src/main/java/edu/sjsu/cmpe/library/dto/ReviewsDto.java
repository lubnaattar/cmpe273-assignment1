package edu.sjsu.cmpe.library.dto;

import java.util.ArrayList
;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.library.domain.Review;

@JsonPropertyOrder(alphabetic = true)
public class ReviewsDto extends LinksDto {
    private List <Review> reviews = new ArrayList<Review>();
    
    public ReviewsDto(List <Review> reviews) {
    	super();
    	this.reviews = reviews;
        }

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
    
}
