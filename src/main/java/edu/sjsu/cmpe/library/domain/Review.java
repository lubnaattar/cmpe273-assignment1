package edu.sjsu.cmpe.library.domain;

import javax.validation.constraints.NotNull;

public class Review {

    private long id;
    @NotNull
    private String rating;
    @NotNull
    private String comment;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}   
        
}
