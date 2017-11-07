/* Liam O'Neill - 15349756 */

package shop;

import java.io.Serializable;

public class Item implements Serializable {

	private String itemName;
	private double price;
	private int quantity;
	
	public Item(String itemName, double price, int quantity) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		// Initially I forgot that the number before the '.' in the floating-point
		// specifier refers to the space taken up by all of the number, not
		// just the space taken up by the part before the decimal point. This
		// meant the price showed up wrong, as I was using %5.2f.
		return String.format("%2d %-15s €%5.2f", quantity, itemName, price);
	}

	public String getItemName() {
		return itemName;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
