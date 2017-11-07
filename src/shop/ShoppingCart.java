/* Liam O'Neill - 15349756 */

package shop;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShoppingCart {

	public static final String INVENTORY_LIST_FILE = "inventoryList.bin";
	private static List<Inventory> inventoryList;
	
	private String customerName;
	private String date;
	private List<Item> cart = new ArrayList<Item>();
	
	public ShoppingCart(String customerName, String date) {
		this.customerName = customerName;
		this.date = date;
	}
	
	public void addItem(String itemName, int quantity) {
		inventoryList = retrieveInventoryList();
		sortInventory();
		int inventoryIndex = searchInventory(itemName);
		int inventoryQuantity = inventoryList.get(inventoryIndex).getItem().getQuantity();
		if (inventoryQuantity == 0) {
			System.out.println(itemName + ": not available.");
			return;
		} else if (quantity > inventoryQuantity) {
			inventoryList.get(inventoryIndex).getItem().setQuantity(0);
			quantity = inventoryQuantity;
		} else {
			inventoryList.get(inventoryIndex).getItem().setQuantity(inventoryQuantity - quantity);
		}
		
		int cartIndex = searchCart(itemName);
		if (cartIndex < 0) {
			cart.add(-cartIndex - 1, new Item(itemName, inventoryList.get(inventoryIndex).getItem().getPrice() * quantity, quantity));
		} else {
			cart.get(cartIndex).setQuantity(cart.get(cartIndex).getQuantity() + quantity);
			cart.get(cartIndex).setPrice(cart.get(cartIndex).getPrice() + inventoryList.get(inventoryIndex).getItem().getPrice() * quantity);
		}
		serializeInventoryList();
	}
	
	public void removeItem(String itemName, int quantity) {
		// This way assumes this method will never get passed the
		// name of an item which is not in the inventoryList or cart.
		inventoryList = retrieveInventoryList();
		sortInventory();
		int inventoryIndex = searchInventory(itemName);
		Item cartItem = cart.get(searchCart(itemName));
		
		int inventoryQuantity = inventoryList.get(inventoryIndex).getItem().getQuantity();
		int cartQuantity = cartItem.getQuantity();
		if (quantity > cartQuantity) {
			cartItem.setQuantity(0);
			quantity = cartQuantity;
		} else {
			cartItem.setQuantity(cartQuantity - quantity);
			cartItem.setPrice(cartItem.getQuantity() * inventoryList.get(inventoryIndex).getItem().getPrice());
		}
		inventoryList.get(inventoryIndex).getItem().setQuantity(inventoryQuantity + quantity);
		
		if (cartItem.getQuantity() == 0) {
			cart.remove(cartItem);
		}
		serializeInventoryList();
	}
	
	public static List<Inventory> getInventoryList() {
		return inventoryList;
	}

	private List<Inventory> retrieveInventoryList() {
		ObjectInputStream file = null;
		List<Inventory> list = null;
		try {
			file = new ObjectInputStream(new FileInputStream(INVENTORY_LIST_FILE));
			list = (List<Inventory>) file.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	private void serializeInventoryList() {
		ObjectOutputStream file = null;
		try {
			file = new ObjectOutputStream(new FileOutputStream(INVENTORY_LIST_FILE));
			file.writeObject(inventoryList);
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public int searchInventory(String itemName) {
		// The Comparator means two Inventories will be considered the same if
		// they have the same itemName, so I do not need to fill in any other
		// arguments besides that in the constructor.
		//
		// Note that binarySearch returns an int which is
		// the index of the element in the collection.
		return Collections.binarySearch(inventoryList, new Inventory("", itemName, 0, 0, 0), new Comparator<Inventory>() {
			@Override
			public int compare(Inventory o1, Inventory o2) {
				return o1.getItem().getItemName().compareTo(o2.getItem().getItemName());
			}
		});
	}

	// Unlike searchInventory, this returns an index to an element of the list,
	// rather than the element itself. This is because it is possible that the
	// Item we are trying to find is not actually in the cart. If I tried to
	// access a negatively-indexed item in a list, an exception would be thrown.
	private int searchCart(String itemName) {
		Collections.sort(cart, new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				return o1.getItemName().compareTo(o2.getItemName());
			}
		});
		
		return Collections.binarySearch(cart, new Item(itemName, 0, 0), new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				return o1.getItemName().compareTo(o2.getItemName());
			}
		});
	}

	public void viewCart() {
		System.out.println(this); // will call toString automatically.
	}
	
	@Override
	public String toString() {
		// A StringBuilder is more efficient here than a String due
		// to the amount of concatenation that is done in the for loop.
		StringBuilder output = new StringBuilder(date + " Name: " + customerName + "\n");
		double total = 0;
		for (Item item : cart) {
			output.append(item + "\n");
			total += item.getPrice();
		}
		output.append(String.format("%20s%5.2f", "Total: €", total));
		return output.toString();
	}
	
	public void sortCartByPrice() {
		Collections.sort(cart, new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				// I need to cast the o1.getPrice() double primitive to a Double
				// object so I can call the compareTo method of that class.
				return ((Double) o1.getPrice()).compareTo(o2.getPrice());
			}
		});
	}

	private void sortInventory() {
		Collections.sort(inventoryList, new Comparator<Inventory>() {
			@Override
			public int compare(Inventory o1, Inventory o2) {
				// This line will sort the Inventories in alphabetical order of itemName.
				return o1.getItem().getItemName().compareTo(o2.getItem().getItemName());
			}
		});
	}
	
	public void shuffleCart() {
		Collections.shuffle(cart);
	}
}
