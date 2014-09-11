/**
 * Implementation des Interfaces MediaDbInterface. Dabei handelt es sich
 * um eine Middleware um das Frontend mit Daten aus dem Backend zu versorgen.
 * @author Silvio Paschke
 * @author Alrik Hausdorf (Aenderung zu voll funktionstuechtiger TestImplementierung) (10/2011)
 * @since 02/06
 */
package media.mediadbimpl;

import media.definitions.*;

import java.util.*;

public class DummyImpl implements MediaDbInterface {


	private Properties prop = null;

	private List<Product> products = new ArrayList<Product>();
	private Product Dvd, Musik;

	/**
	 * Konstruktor
	 */
	public DummyImpl() {
		Dvd = new Product("B0002TB60W", new Float(5), new Integer(1586), "Various Artists - Karaoke: Love Songs, Vol. 01",
				new Float(16.48), "EUR", true, "http://images.amazon.com/images/P/B0002TB60W.03._SCMZZZZZZZ_.jpg", null, Product.Type.dvd);
		
		Musik = new Product("B0002ISFNO", new Float(4), new Integer(13844), "The Ultimate Guitar Collection",
				new Float(13.52), "EUR", true, "http://images.amazon.com/images/P/B0002ISFNO.03._SCMZZZZZZZ_.jpg", null, Product.Type.music);

		Review dvdReview1 = new Review(Dvd, "David", Integer.valueOf(4),
								Integer.valueOf(2), new Date(System.currentTimeMillis()),
								"Die DVD ist einfach klasse!",
								"Inhalt des Reviews. Inhalt des Reviews. Inhalt des Reviews. " +
								"Inhalt des Reviews. Inhalt des Reviews. Inhalt des Reviews.");

		Review dvdReview2 = new Review(Dvd, "Andreas", Integer.valueOf(4),
								Integer.valueOf(2), new Date(System.currentTimeMillis()),
								"Die DVD ist leider nur Mittelmass!",
								"Inhalt des Reviews. Inhalt des Reviews. Inhalt des Reviews. " +
								"Inhalt des Reviews. Inhalt des Reviews. Inhalt des Reviews.");

		Review musikReview = new Review(Musik, "Andreas", Integer.valueOf(4),
								Integer.valueOf(2), new Date(System.currentTimeMillis()),
								"Die Musik ist einfach hervorragend!",
								"Inhalt des Reviews. Inhalt des Reviews. Inhalt des Reviews. " +
								"Inhalt des Reviews. Inhalt des Reviews. Inhalt des Reviews.");

		Dvd.addReview(dvdReview1);
		Dvd.addReview(dvdReview2);
		Musik.addReview(musikReview);

		products.add(Dvd);
		products.add(Musik);
	}

	/**
	 * Initialisierung der Zugriffsschicht. Das Property-Objekt
	 * enthaelt alle notwendigen Parameter.
	 *
	 * @param prop Property-Objekt mit Attribut-Wert-Paaren, die
	 *             zur Initialisierung dienen (Login-Name, Passwort,...).
	 */
	public void init(Properties prop) {

		this.prop = prop;

		System.out.println("Einstellungen:");
		prop.list(System.out);

		/*
		 * Herstellung der Datenbankverbindung.
		 */

	}

	/**
	 * Diese Methode wird bei Beendigung der Anwendung aufgerufen. Hier koennen
	 * alle Ressourcen freigegeben werden (Verbindung usw.)
	 */
	public void finish() {
	}

	/**
	 * Gibt das Ergebnis der Anfrage zurueck.
	 *
	 * @param query enthaelt die Anfrage
	 * @return Rueckgabeobjekt
	 */
	public SQLResult executeSqlQuery(String query) throws Exception {

		SQLResult sqlRes = new SQLResult();

		/*
		 * Spaltenueberschriften setzen.
		 */
		sqlRes.setHeader(new String[]{
				"Artikelnummer", "Bezeichnung", "Preis", "Durchschnittliche Bewertung"}
		);

		List<String[]> data = new ArrayList<String[]>();

		data.add(new String[]{this.Dvd.getAsin(),
				this.Dvd.getTitle(),
				this.Dvd.getPrice().toString() + " " + this.Dvd.getCurrency(),
				this.Dvd.getAvgRating().toString()});
		data.add(new String[]{this.Musik.getAsin(),
				this.Musik.getTitle(),
				this.Musik.getPrice().toString() + " " + this.Musik.getCurrency(),
				this.Musik.getAvgRating().toString()});

		/*
					 * Datenteil setzen.
					 */
		sqlRes.setBody(data);

		return sqlRes;
	}

	/**
	 * Gibt das Ergebnis der Anfrage zurueck.
	 *
	 * @param query enthaelt die Anfrage
	 * @param deref
	 * @return Rueckgabeobjekt
	 */
	public SQLResult executeHqlQuery(String query, boolean deref) {
		try {
			return executeSqlQuery("");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Diese Methode liefert eine Liste mit Produkten, die dem Pattern
	 * entsprechen. Der Pattern soll ein Teil des Titels sein.
	 * Attribute: Id, Title, ProduktGroup
	 *
	 * @param namePattern SQL-Pattern im Sinne des SQL-Like
	 * @return Liste mit den Produkten, die dem Pattern entsprechen
	 */
	public synchronized List<Product> getProducts(String namePattern) {
		return products;
	}


	/**
	 * Liefert diejenigen Produkte, denen auch tatsaechlich ein Review zugeordnet ist.
	 */
	public synchronized List<Product> getReviewProducts() {
		// in unserem Fall betrifft das alle Produkte
		return products;
	}


	/**
	 * fuegt ein neues Review ein. Die Daten sind dabei aus dem Objekt zu lesen
	 * und in der Datenbank einzufuegen.
	 *
	 * @param review Reviewdaten.
	 */
	public synchronized void addNewReview(Review review) {
		String user = review.getUsername();
		String summar = review.getSummary();
		String cont = review.getContent();

		Date date = review.getDate();

		// ist der User bestehendes Mitglied? ja: ermittle customerid, nein: user neu anlegen

		// speichere daten in der datenbank
	}


	/**
	 * Gibt die Wurzel des Kategorienbaumes zurueck.
	 *
	 * @return Kategorienbaum
	 */
	public synchronized Category getCategoryTree() {

		Category mainroot = new Category(-1, "+");
		mainroot.addChild(new Category(3, "DummyKategorie"));
		return mainroot;

	}


	/**
	 * Diese Methode liefert eine Liste mit Produkten vom Typ Product. Aufgrund des uebergebenen
	 * Pfades werden die Produkte ausgewaehlt. Hinweis: ueberlegen Sie sich, ob an dieser Stelle
	 * ein PreparedStatement sinnvoll ist und inwiefern dynamische Ansaetze bei der Query-
	 * Erstellung sinnvoll sind.
	 *
	 * @param categoriesPath Kategorien-Pfad
	 * @return Liste vom Typ Product
	 */
	public synchronized List<Product> getProductsByCategoryPath(Category[] categoriesPath) {
		List<Product> list = new ArrayList<Product>();
		/*
		 * Bestimmen Sie hier, welche Produkte zu dem Pfad gehoeren.
		 * Beachten Sie, dass "+" keine Kategorie ist. Diese dient nur der Zusammenfuehrung
		 * der Einzelbaeume.
		 */
		list.add(this.Dvd);
		list.add(this.Musik);

		return list;
	}

	/**
	 * Gibt zu einer Produkt-ID das passende Produkt zurueck.
	 *
	 * @param id Produktnummer
	 * @return passendes Produkt
	 */
	public Product getProduct(String id) {
		return (this.Dvd.getAsin().equals(id)) ? this.Dvd :
				(this.Musik.getAsin().equals(id)) ? this.Musik : null;
	}

	/**
	 * Liefert die detailierten Buchdaten eines Produkts, sofern es sich um ein Buch handelt.
	 *
	 * @param id Produkt-ID
	 * @return Datenstruktur vom Typ Book
	 */
	public Book getBook(String id) {
		Book book = new Book();
		book.setIsbn("4-123-432-9999");
		book.setPages(Integer.valueOf(267));
		book.setPubDate(new Date(System.currentTimeMillis()));

		return book;
	}

	/**
	 * siehe getBookData
	 */
	public DVD getDVD(String id) {
		/* Dummy - Implementation */
		DVD dummy = new DVD();

		dummy.setAsin("" + id);
		dummy.setFormat("AC-3,PAL");
		dummy.setRegionCode(Integer.valueOf(0));
		dummy.setRunningTime(Integer.valueOf(240));
		dummy.addActor(new DVD.Actor("Various Actors"));
		dummy.addCreator(new DVD.Creator("Universal"));
		dummy.addDirector(new DVD.Director("Various Directors"));
		return dummy;
	}


	/**
	 * siehe getBookData
	 */
	public Music getMusic(String id) {
		//da nur eine Musik/CD da ist, sonst dynamisch
		Music dummy = new Music();
		dummy.setAsin("" + id);
		dummy.setReleaseDate(new Date(System.currentTimeMillis()));
		dummy.addTrack(new Music.Track("No. 5 Asturias (Leyenda)"));
		dummy.addTrack(new Music.Track("Cavatina from The Deer Hunter"));
		dummy.addTrack(new Music.Track("I. Prelude from Suite for Lute (Guitar) No. 4 in E Major, BWV 1006a:"));
		dummy.addTrack(new Music.Track("Triangular Situations"));
		dummy.addTrack(new Music.Track("Recuerdos de la Alhambra"));
		dummy.addTrack(new Music.Track("Romance for Guitar and String Orchestra"));
		dummy.addTrack(new Music.Track("Dance from &quot;La Vida Breve&quot;"));
		dummy.addTrack(new Music.Track("Sonata in E Major, K. 380 (L. 23)"));
		dummy.addTrack(new Music.Track("Como Llora una Estrella"));
		dummy.addTrack(new Music.Track("The Mission (Gabriel's Oboe, Mission Theme, On Earth as it is in Heaven)"));
		dummy.addTrack(new Music.Track("El Colibri"));
		dummy.addTrack(new Music.Track("Gymnopédie No. 3 (for guitar and small orchestra)"));
		dummy.addTrack(new Music.Track("Courante"));
		dummy.addTrack(new Music.Track("La catedral"));
		dummy.addTrack(new Music.Track("El condor pasa - Traditional Peruvian Folksong"));
		dummy.addTrack(new Music.Track("Salut D'Amor"));
		dummy.addTrack(new Music.Track("Malinke Guitars"));
		dummy.addTrack(new Music.Track("I. Allegro giusto"));
		dummy.addTrack(new Music.Track("The Entertainer from The Sting"));		
		dummy.addLabel(new Music.Label("Sony"));
		dummy.addArtist(new Music.Artist("John Williams"));
		
		return dummy;
	}

	public List<Offer> getOffers(Product product) {
		List<Offer> ret = new ArrayList<Offer>();

		ret.add(new Offer(12.99f, "EUR", "Leipzig"));
		ret.add(new Offer(15.50f, "EUR", "Dresden"));

		return ret;
	}


}//class
  
