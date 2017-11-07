/* Liam O'Neill - 15349756 */

package shop;

public class Main_ShoppingCart {

	public static void main(String[] args) {
		
		ShoppingCart cart1 = new ShoppingCart("Dervla", "12/10/2017");
		ShoppingCart cart2 = new ShoppingCart("Mark", "14/10/2017");
		
		cart1.addItem("Apple", 2);
		cart1.addItem("Orange", 5);
		cart1.addItem("Milk", 2);
		cart1.addItem("Blue Cheese", 4);
		cart1.addItem("Candy", 25);
		cart1.removeItem("Candy", 5);
		
		cart1.viewCart();
		System.out.println(); // Add a newline to make output more readable.
		
		Main_Inventory.printInventoryList(ShoppingCart.getInventoryList());
		System.out.println();
		
		cart2.addItem("Apple", 2);
		cart2.addItem("Orange", 5);
		cart2.addItem("Milk", 2);
		cart2.addItem("Blue Cheese", 4);
		cart2.addItem("Cheddar", 3);
		cart2.addItem("Beef", 6);
		cart2.addItem("Candy", 20);
		cart2.addItem("Chocolate", 10);
		cart2.addItem("Chicken", 2);
		cart2.removeItem("Chocolate", 5);
		cart2.removeItem("Blue Cheese", 1);
		
		cart2.viewCart();
		System.out.println();
		
		Main_Inventory.printInventoryList(ShoppingCart.getInventoryList());
		System.out.println();
		
		cart2.shuffleCart();
		cart2.viewCart();
		System.out.println();
		
		cart2.sortCartByPrice();
		cart2.viewCart();
		System.out.println();
	}
}
