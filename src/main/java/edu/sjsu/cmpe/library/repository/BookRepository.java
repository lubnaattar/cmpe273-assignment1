package edu.sjsu.cmpe.library.repository;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;



import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.domain.Author;

public class BookRepository implements BookRepositoryInterface {
    /** In-memory map to store books. (Key, Value) -> (ISBN, Book) */
    private final ConcurrentHashMap<Long, Book> bookInMemoryMap;

   // private final ConcurrentHashMap<Long, Review> reviewMemoryMap;

    /** Never access this key directly; instead use generateISBNKey() */
    private long isbnKey;
    private long authorKey;
    private long reviewKey;

    public BookRepository(ConcurrentHashMap<Long, Book> bookMap) {
	checkNotNull(bookMap, "bookMap must not be null for BookRepository");
	bookInMemoryMap = bookMap;
	isbnKey = 0;
    }

    
    /**
     * This should be called if and only if you are adding new books to the
     * repository.
     * 
     * @return a new incremental ISBN number
     */
    private final Long generateISBNKey() {
	// increment existing isbnKey and return the new value
	return Long.valueOf(++isbnKey);
    }
    
    private final Long generateAuthorKey() {
    	// increment existing isbnKey and return the new value
    	return Long.valueOf(++authorKey);
        }
    private final Long generateReviewKey() {
    	// increment existing isbnKey and return the new value
    	return Long.valueOf(++reviewKey);
        }

    /**
     * This will auto-generate unique ISBN for new books.
     */
    @Override
    public Book saveBook(Book newBook) {
	checkNotNull(newBook, "newBook instance must not be null");
	// Generate new ISBN
	Long isbn = generateISBNKey();
	Long authorId;
	newBook.setIsbn(isbn);	
	
	//List<Author> list = new ArrayList<Author>();  
	List<Author> authorList = newBook.getAuthors();
	for (Author authorObj : authorList){
		authorId = generateAuthorKey();
		authorObj.setId(authorId);
	}
	
	//newBook.setAuthList(authList);
	
	// TODO: create and associate other fields such as author

	// Finally, save the new book into the map
	bookInMemoryMap.putIfAbsent(isbn, newBook);
	return newBook;
    }
    
    
    // **************
    @Override
    public void deleteBook(Long isbn) {	
	bookInMemoryMap.remove(isbn);	
    }
    //*********
    
    @Override
    public void updateBook(Long isbn, String status) {	
    Book book = getBookByISBN(isbn);
    book.setStatus(status);    	
    }

//
    
    @Override
    public Author viewBookAuthor(Book newBook, Long authorId) {
    	List<Author> authorList = newBook.getAuthors();
    	Long authorTempId;
    	Author authorObj2 = new Author();
    	// = (Author)newBook.getAuthors();
    	for (Author authorObj : authorList){    		
    		authorTempId =authorObj.getId();
    		if(authorTempId == authorId){
    			//authorObj.getName();
    			return authorObj;
    		}
    	}
    	return authorObj2;
    }
    //*********
    
    //Author viewBookAllAuthors(Book book);	
    @Override
    public Author viewBookAllAuthors(Book book){
    	List<Author> authorList = book.getAuthors();
    	
    	Author authorObj2 = new Author();
    	
    	for (Author authorObj : authorList){		
    			if(authorObj != null)
    			{
    				return authorObj;
    			}
    		}
    	//Author authResult = (Author)  authorList;
    	return authorObj2;
    }
    
    //****
    /*@Override
    public void updateBook(Long isbn, String status) {	
    Book book = getBookByISBN(isbn);
    book.setStatus(status);    	
    }
    //
*/    /**
     * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getBookByISBN(java.lang.Long)
     */
    @Override
    public Book getBookByISBN(Long isbn) {
	checkArgument(isbn > 0,"ISBN was %s but expected greater than zero value", isbn);
	return bookInMemoryMap.get(isbn);
    }
    
    //Reviews API
    // 1. Create book review api
    
    public Book createBookReview(Book newBook, Review reviewObj) {
    	checkNotNull(newBook, "newBook instance must not be null");
    	// Generate new ISBN
    	Long reviewId = generateReviewKey();
    	reviewObj.setId(reviewId);   	
    	if(getBookByISBN(newBook.getIsbn()).getReviews() != null){
    		List<Review> reviewList = getBookByISBN(newBook.getIsbn()).getReviews();
        	reviewList.add(reviewObj);    	
        	newBook.setReviews(reviewList);    	
        		
    	}
    	return newBook;
        }
    
	public void saveBookWithReviews(Long isbn, Book b1){
		bookInMemoryMap.put(isbn, b1);
	}

}
