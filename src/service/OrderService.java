package service;

import java.io.BufferedReader;

import java.util.UUID;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import beans.Order;
import beans.OrderState;
import beans.ShoppingChart;
import beans.ShoppingChartItem;
import beans.User;
import dao.OrdersDAO;

public class OrderService {
	
	public static OrderService orderService = null;
	public static OrderService getInstance() {
		if(orderService == null) {
			orderService = new OrderService();
		}
		return orderService;
	}
	private OrderService() {
		
	}
	public void makeAnOrder(String username) {
		User user = UserService.getInstance().getByUsername(username);
		ShoppingChart sc = ShoppingChartService.getInstance().getByUsername(username);
		HashMap<String, Order> orders = new HashMap<>();
		for(ShoppingChartItem si : sc.items) {
			if(orders.containsKey(si.artical.restaurant)) {
				Order newOrder = orders.get(si.artical.restaurant);
				newOrder.articles.add(si);
				orders.put(si.artical.restaurant, newOrder);
				
			}else {
				Order newOrder = new Order();
				newOrder.setId(UUID.randomUUID().toString());
				ArrayList<ShoppingChartItem> articles = new ArrayList<>();
				articles.add(si);
				newOrder.setArticles(articles);
				LocalDateTime now = LocalDateTime.now();
				newOrder.setDate(now.toString());
				newOrder.setFullName(user.getName()+' '+user.getSurname());
				newOrder.setOrderState(OrderState.PROCESSING);
				newOrder.setRestaurant(si.getArtical().getRestaurant());
				orders.put(si.artical.restaurant, newOrder);
			}
		}
		orders.forEach((k, v) -> {
		    CalculateAndSaveOrder(v, user.discount);
		});
		ShoppingChartService.getInstance().emptyShoppingCart(username);		
		}
	
	private void CalculateAndSaveOrder(Order v, double discount) {
		double price = 0.0;
		for(ShoppingChartItem si : v.articles) {
			price+= si.artical.price * si.quantity;	
	}
		double priceDiscounted = price * ((100-discount)/100);
		v.setPrice(price);
		v.setPriceWithDiscount(priceDiscounted);
		OrdersDAO.getInstance().addOrder(v);
	}

}