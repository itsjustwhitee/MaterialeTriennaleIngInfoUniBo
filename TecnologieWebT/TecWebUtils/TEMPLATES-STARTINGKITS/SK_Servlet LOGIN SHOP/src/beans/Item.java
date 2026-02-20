package beans;

import java.io.Serializable;
import java.util.Objects;

public class Item implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String description, name, id;
	private double price;
	
	public Item() {
		super();
	}

	public Item(String id, String name, String description, double price) {
		super();
		this.description = description;
		this.name = name;
		this.id = id;
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		return this.id.equals(other.id) && this.name.equals(other.name);
	}

	@Override
	public String toString() {
		return "[" + id + "] " + name + " " + price + " €";
	}
}
