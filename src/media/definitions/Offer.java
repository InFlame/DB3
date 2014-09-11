package media.definitions;

/**
 * @author Stefan Endrullis
 */
public class Offer {
	private Integer id;
	private Product product;
	private double price;
	private String currency;
	private String location;

	public Offer() {
	}

	public Offer(float price, String currency, String location) {
		this.price = price;
		this.currency = currency;
		this.location = location;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/** Edit if you want. */
	public String toString() {
		return location;
	}
}
