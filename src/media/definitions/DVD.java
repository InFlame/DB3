/**
 * Kapselt DVD-Daten.
 *
 */
package media.definitions;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Where;
/**
 * @author Silvio Paschke, Stefan Endrullis
 */
@Entity
@Table(name = "DVD")
public class DVD extends Product {
	@Column(name="Format")
	private String format = "";
	@Column(name="RegionCode")
	private Integer regionCode = null;
	@Column(name="Laufzeit")
	private Integer runningTime = null;
	@OneToMany
    @JoinTable(
            name="TEAM",
            joinColumns = @JoinColumn( name="ASIN"),
            inverseJoinColumns = @JoinColumn( name="ASIN")
    )
	@Where(clause="Position ='actor'") 
	private Set<Actor> actors = new LinkedHashSet<Actor>();

	@OneToMany
    @JoinTable(
            name="TEAM",
            joinColumns = @JoinColumn( name="ASIN"),
            inverseJoinColumns = @JoinColumn( name="ASIN")
    )
	@Where(clause="Position ='creators'") 
	private Set<Creator> creators = new LinkedHashSet<Creator>();

	@OneToMany
    @JoinTable(
            name="TEAM",
            joinColumns = @JoinColumn( name="ASIN"),
            inverseJoinColumns = @JoinColumn( name="ASIN")
    )
	@Where(clause= "Position ='directors'") 
	private Set<Director> directors = new LinkedHashSet<Director>();

	public DVD() {
		setType(Type.dvd);
	}

	/**
	 * @return Returns the actors.
	 */
	public Set<Actor> getActors() {
		return this.actors;
	}

	/**
	 * @param actors The actors to set.
	 */
	public void setActors(Set<Actor> actors) {
		this.actors = actors;
	}

	/**
	 * Adds a actor to the actor list.
	 *
	 * @param actor actor to add
	 */
	public void addActor(Actor actor) {
		actors.add(actor);
		actor.setProduct(this);
	}

	/**
	 * @return Returns the creators.
	 */
	public Set<Creator> getCreators() {
		return this.creators;
	}

	/**
	 * @param creators The creators to set.
	 */
	public void setCreators(Set<Creator> creators) {
		this.creators = creators;
	}

	/**
	 * Adds a creator to the creator list.
	 *
	 * @param creator creator to add
	 */
	public void addCreator(Creator creator) {
		creators.add(creator);
		creator.setProduct(this);
	}

	/**
	 * @return Returns the directors.
	 */
	public Set<Director> getDirectors() {
		return this.directors;
	}

	/**
	 * @param directors The directors to set.
	 */
	public void setDirectors(Set<Director> directors) {
		this.directors = directors;
	}

	/**
	 * Adds a director to the director list.
	 *
	 * @param director director to add
	 */
	public void addDirector(Director director) {
		directors.add(director);
		director.setProduct(this);
	}

	/**
	 * @return Returns the format.
	 */
	public String getFormat() {
		return this.format;
	}

	/**
	 * @param format The format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return Returns the regioncode.
	 */
	public Integer getRegionCode() {
		return this.regionCode;
	}

	/**
	 * @param regionCode The regioncode to set.
	 */
	public void setRegionCode(Integer regionCode) {
		this.regionCode = regionCode;
	}

	/**
	 * @return Returns the runningtime.
	 */
	public Integer getRunningTime() {
		return this.runningTime;
	}

	/**
	 * @param runningTime The runningtime to set.
	 */
	public void setRunningTime(Integer runningTime) {
		this.runningTime = runningTime;
	}



// inner classes

	public static class Actor extends Person<DVD> {
		public Actor() { }
		public Actor(String name) { super(name); }
	}

	public static class Creator extends Person<DVD> {
		public Creator() { }
		public Creator(String name) { super(name); }
	}

	public static class Director extends Person<DVD> {
		public Director() { }
		public Director(String name) { super(name); }
	}
}
