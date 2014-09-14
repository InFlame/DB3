/**
 * Kapselt Buchdaten.
 */
package media.definitions;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Silvio Paschke, Stefan Endrullis
 */

@Entity
@Table(name = "Buch")
public class Book extends Product {

	@Column(name="ISBN")
	private String isbn = "";
	
	@Column(name="Seitenzahl")
	private Integer pages = null;
	
	@Temporal(TemporalType.DATE)
	@Column(name="PubDate")
	private Date pubDate = null;

	@Column(name="Publisher")
	//private     List  tracks      = null;
	private Set<Publisher> publishers = new LinkedHashSet<Publisher>();
	
	   
    @OneToMany
    @JoinTable(
            name="hatAutor",
            joinColumns = @JoinColumn( name="ASIN"),
            inverseJoinColumns = @JoinColumn( name="ASIN")
    )
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
