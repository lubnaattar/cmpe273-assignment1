package edu.sjsu.cmpe.library.domain;
import java.util.*;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Book {
    private long isbn;
    @NotNull
    private String title; // Required field
    private String status;    
    @JsonProperty("publication-date") @NotNull
    private String publicationDate; //required field 
    private String language;
    @JsonProperty("num-pages")    
    private String numOfPages;
    
    public enum bookStatus { available, lost, inqueue, checkedout ;
    	
    	public String theStatus() {

    	    String value;

    	    switch (this) {

    	        case available:
    	            value = "available";
    	            break;

    	        case lost:
    	            value = "lost";
    	            break;

    	        case inqueue:
    	            value = "in-queue";
    	            break;

    	        case checkedout:
    	            value = "checked-out";
    	            break;

    	        default:
    	            value = "available";
    	            break;
    	    }

    	    return value;
    	}
    }
    
    List <Author> authors = new ArrayList<Author>();
    List <Review> reviews = new ArrayList<Review>();


	public String getPublicationDate() {
		return publicationDate;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNumOfPages() {
		return numOfPages;
	}

	public void setNumOfPages(String numOfPages) {
		this.numOfPages = numOfPages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {	
		
		if(!status.equalsIgnoreCase("Lost") && !status.equalsIgnoreCase("available") && !status.equalsIgnoreCase("in-queue") && !status.equalsIgnoreCase("checked-out")){
			status= "available";
		}
		this.status = status;
	}

	/**
     * @return the isbn
     */
    public long getIsbn() {
	return isbn;
    }

    /**
     * @param isbn
     *            the isbn to set
     */
    public void setIsbn(long isbn) {
	this.isbn = isbn;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }
}
