package edu.sjsu.cmpe.library.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import edu.sjsu.cmpe.library.domain.Review;

@JsonPropertyOrder(alphabetic = true)
public class ReviewDto extends LinksDto {
    private Review review;

    /**
     * @param review
     */
    public ReviewDto(Review review) {
	super();
	this.review = review;
    }

    /**
     * @return the review
     */
    public Review getReview() {
	return review;
    }

    /**
     * @param review
     *            the review to set
     */
    public void setReview(Review review) {
	this.review = review;
    }
}
