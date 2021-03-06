/**
 * Kapselt Buchdaten.
 */
package media.definitions;

import java.util.*;

/**
 * @author Silvio Paschke, Stefan Endrullis
 */
public class Book extends Product {

	private String isbn = "";	
	private Integer pages = null;	
	private Date pubDate = null;
	//private     List  tracks      = null;
	private Set<Publisher> publishers = new LinkedHashSet<Publisher>();
	private Set<Author> authors = new LinkedHashSet<Author>();

	public Book() {
		setType(Type.book); 
	}

	/**
	 * @return Returns the authors.
	 */
	public Set<Author> getAuthors() {
		return this.authors;
	}

	/**
	 * @param authors The authors to set.
	 */
	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	/**
	 * Adds an author to the author list.
	 *
	 * @param author author to add
	 */
	public void addAuthor(Author author) {
		authors.add(author);
		author.setProduct(this);
	}

	/**
	 * @return Returns the publishers.
	 */
	public Set<Publisher> getPublishers() {
		return this.publishers;
	}

	/**
	 * @param publishers The publishers to set.
	 */
	public void setPublishers(Set<Publisher> publishers) {
		this.publishers = publishers;
	}

	/**
	 * Adds an publisher to the publisher list.
	 *
	 * @param publisher publisher to add
	 */
	public void addPublisher(Publisher publisher) {
		publishers.add(publisher);
		publisher.setProduct(this);
	}

	/**
	 * @return Returns the isbn.
	 */
	public String getIsbn() {
		return this.isbn;
	}

	/**
	 * @param isbn The isbn to set.
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return Returns the pages.
	 */
	public Integer getPages() {
		return this.pages;
	}

	/**
	 * @param pages The pages to set.
	 */
	public void setPages(Integer pages) {
		this.pages = pages;
	}

	/**
	 * @return Returns the pubdate.
	 */
	public Date getPubDate() {
		return this.pubDate;
	}

	/**
	 * @param pubDate The pubdate to set.
	 */
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}



// inner classes

	public static class Publisher extends Person<Book> {
		public Publisher() { }
		public Publisher(String name) { super(name); }
	}

	public static class Author extends Person<Book> {
		public Author() { }
		public Author(String name) { super(name); }
	}
}
