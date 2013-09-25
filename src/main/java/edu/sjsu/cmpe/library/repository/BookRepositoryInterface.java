package edu.sjsu.cmpe.library.repository;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;

/**
 * Book repository interface.
 * 
 * What is repository pattern?
 * 
 * @see http://martinfowler.com/eaaCatalog/repository.html
 */
public interface BookRepositoryInterface {
    /**
     * Save a new book in the repository
     * 
     * @param newBook
     *            a book instance to be create in the repository
     * @return a newly created book instance with auto-generated ISBN
     */
    Book saveBook(Book newBook);

    /**
     * Retrieve an existing book by ISBN
     * 
     * @param isbn
     *            a valid ISBN
     * @return a book instance
     */
    Book getBookByISBN(Long isbn);
    
    void deleteBook(Long isbn);

    void updateBook(Long isbn, String status);
    Author viewBookAuthor(Book book, Long authorId);
    Author viewBookAllAuthors(Book book);	
    
    
   Book createBookReview(Book book,Review request);	

   void saveBookWithReviews(Long isbn, Book book);
    // TODO: add other operations here!
}
