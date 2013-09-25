package edu.sjsu.cmpe.library.api.resources;

import java.util.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Book.bookStatus;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.dto.AuthorsDto;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.dto.ReviewDto;
import edu.sjsu.cmpe.library.dto.ReviewsDto;
import edu.sjsu.cmpe.library.dto.AuthorDto;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;


@Path("/v1/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
	/** bookRepository instance */
	private final BookRepositoryInterface bookRepository;

	// private final ReviewRepositoryInterface ReviewRepository;

	/**
	 * BookResource constructor
	 * 
	 * @param bookRepository
	 *            a BookRepository instance
	 */
	public BookResource(BookRepositoryInterface bookRepository) {
		this.bookRepository = bookRepository;
	}

	
	// 3 : View Book API
	@GET
	@Path("/{isbn}")
	@Timed(name = "view-book")
	public Response getBookByIsbn(@PathParam("isbn") LongParam isbn) {
		Book book = bookRepository.getBookByISBN(isbn.get());
		
		String location = "/books/" + book.getIsbn();
		BookDto bookResponse = new BookDto(book);
		bookResponse.addLink(new LinkDto("view-book", "/books/"
				+ book.getIsbn(), "GET"));
		bookResponse.addLink(new LinkDto("update-book", "/books/"
				+ book.getIsbn(), "POST"));
		bookResponse.addLink(new LinkDto("delete-book", "/books/"
				+ book.getIsbn(), "POST"));
		bookResponse.addLink(new LinkDto("create-review", "/books/"
				+ book.getIsbn(), "POST"));
		if(book.getReviews() != null && !book.getReviews().isEmpty()){
			bookResponse.addLink(new LinkDto("view-all-reviews", location + "/reviews", "GET"));
		}
		
		// add more links
		return Response.status(200).entity(bookResponse).build();

	}

	// 2 : Create Book API
	@POST
	@Timed(name = "create-book")
	public Response createBook(@Valid Book request) {
		// Store the new book in the BookRepository so that we can retrieve it.
		Book savedBook = bookRepository.saveBook(request);
		String location = "/books/" + savedBook.getIsbn();
		
		BookDto bookResponse = new BookDto(savedBook);
		bookResponse.addLink(new LinkDto("view-book", location, "GET"));
		bookResponse.addLink(new LinkDto("update-book", location, "PUT"));
		bookResponse.addLink(new LinkDto("delete-book", location, "DELETE"));
		bookResponse.addLink(new LinkDto("create-review", location + "/reviews", "POST"));
		// Add other links if needed
		return Response.status(201).entity(bookResponse).build();
	}

	// 4 : Delete Book API
	@DELETE
	@Path("/{isbn}")
	@Timed(name = "delete-book")
	public Response deleteBook(@PathParam("isbn") LongParam isbn) {
		Book book = bookRepository.getBookByISBN(isbn.get());
		String location = "/books";
		BookDto bookResponse = new BookDto(book);		
		//bookResponse.addLink(new LinkDto("create-book", location, "POST"));
		//return Response.status(200).entity(bookResponse).build();
		
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("create-book", location, "POST"));
		bookRepository.deleteBook(isbn.get());
		
		return Response.ok(links).build();
	}

	// 5: Update Book API
	@POST
	@Path("/{isbn}")
	@Timed(name = "update-book")
	public Response updateBook(@QueryParam("status") String status,
			@PathParam("isbn") LongParam isbn) {		
		
		Book book = bookRepository.getBookByISBN(isbn.get());
		// book.setStatus(status);
		bookRepository.updateBook(isbn.get(), status);

		String location = "/books/" + book.getIsbn();
		BookDto bookResponse = new BookDto(book);

		/*bookResponse.addLink(new LinkDto("view-book", location, "GET"));
		bookResponse.addLink(new LinkDto("update-book", location, "PUT"));
		bookResponse.addLink(new LinkDto("delete-book", location, "DELETE"));
		bookResponse.addLink(new LinkDto("create-review", location, "POST"));
		if(book.getReviews() != null && !book.getReviews().isEmpty()){
			bookResponse.addLink(new LinkDto("view-all-reviews", location + "/reviews", "GET"));
		}*/
		
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("view-book", location, "GET"));
		links.addLink(new LinkDto("update-book", location, "PUT"));
		links.addLink(new LinkDto("delete-book", location, "DELETE"));
		links.addLink(new LinkDto("create-review", location, "POST"));
		if(book.getReviews() != null && !book.getReviews().isEmpty()){
			links.addLink(new LinkDto("view-all-reviews", location + "/reviews", "GET"));
		}		
		
		return Response.ok(links).build();
	}

	// 6 : Create Book Review API
	@POST
	@Path("{isbn}/reviews")
	@Timed(name = "create-review")
	public Response createReview(@PathParam("isbn") LongParam isbn,
			@Valid Review reviewObject) {
		Book book = bookRepository.getBookByISBN(isbn.get());
		Book b1 = bookRepository.createBookReview(book, reviewObject);
		
		bookRepository.saveBookWithReviews(isbn.get(), b1);
		BookDto bookResponse = new BookDto(b1);
		
		String location = "/books/" + b1.getIsbn() + "/reviews/" + reviewObject.getId();
		LinksDto links = new LinksDto();
		links.addLink(new LinkDto("view-review", location, "POST"));
		//return Response.status(201).entity(bookResponse).build();
		return Response.ok(links).build();
	}


	// 7 : View book review API
		@GET
		@Path("/{isbn}/reviews/{id}")
		@Timed(name = "view-review")
		public Response getBookReviewById(@PathParam("isbn") LongParam isbn,
				@PathParam("id") LongParam reviewId) {
			Book book = bookRepository.getBookByISBN(isbn.get());

			List<Review> bookreview = book.getReviews();
			Long tempReviewId = 12345678910L;
			Review reviewResult = new Review();
			for (Review reviewObject : bookreview) {
				tempReviewId = reviewObject.getId();
				if (tempReviewId == reviewId.get()) {
					reviewResult = reviewObject;
					break;
				}
			}

			ReviewDto reviewResponse = new ReviewDto(reviewResult);
			reviewResponse.addLink(new LinkDto("view-Review", "/books/"
					+ book.getIsbn() + "/reviews/" + tempReviewId, "GET"));

			return Response.status(200).entity(reviewResponse).build();
		}

	// 8 : view All reviews API
		@GET
		@Path("/{isbn}/reviews")
		@Timed(name = "view-all-reviews")
		public Response viewAllBookReviews(@PathParam("isbn") LongParam isbn) {
			Book book = bookRepository.getBookByISBN(isbn.get());
			List<Review> bookreview = book.getReviews();			
			ReviewsDto reviewsResponse = new ReviewsDto(bookreview);
			return Response.status(200).entity(reviewsResponse).build();
		}
		
	// 9 : View Book Author API
	@GET
	@Path("/{isbn}/authors/{id}")
	@Timed(name = "view-book-author")
	public Response getAuthorById(@PathParam("isbn") LongParam isbn,
			@PathParam("id") LongParam authorId) {
		Book book = bookRepository.getBookByISBN(isbn.get());

		Author author = bookRepository.viewBookAuthor(book, authorId.get());

		AuthorDto authorResponse = new AuthorDto(author);
		authorResponse.addLink(new LinkDto("view-author", "/books/"
				+ book.getIsbn() + "/authors/" + authorId.get(), "GET"));
		
		return Response.status(200).entity(authorResponse).build();
		
	}
	
	
	//10 : View All authors of the book API
	@GET
	@Path("/{isbn}/authors")
	@Timed(name = "view-all-author")
	public Response getAllAuthorsById(@PathParam("isbn") LongParam isbn) {
		Book book = bookRepository.getBookByISBN(isbn.get());
		//Author author = bookRepository.viewBookAllAuthors(book);
		List<Author> authReview = book.getAuthors();			
		AuthorsDto authorResponse = new AuthorsDto(authReview);
//		return authorResponse;
		return Response.status(200).entity(authorResponse).build();

	}

}
