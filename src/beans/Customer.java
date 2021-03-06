package beans;

import java.util.ArrayList;
import java.util.Date;

public class Customer extends User{
	private ArrayList<Order> allOrders;
	private ShoppingChart shoppingChart;
	private int score;

	

	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Customer(String userName, String password, String name, String surname, String date,String gender,Roles role) {
		super(userName, password, name, surname, date,gender,role);
		role=Roles.CUSTOMER;
		this.allOrders=new ArrayList<Order>();
		this.shoppingChart=new ShoppingChart();
		this.score = 0;

	}
	public ArrayList<Order> getAllOrders() {
		return allOrders;
	}
	public void setAllOrders(ArrayList<Order> allOrders) {
		this.allOrders = allOrders;
	}
	public ShoppingChart getShoppingChart() {
		return shoppingChart;
	}
	public void setShoppingChart(ShoppingChart shoppingChart) {
		this.shoppingChart = shoppingChart;
	}
	
	
	

	
}
