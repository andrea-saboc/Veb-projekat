package service;

import beans.Artical;
import beans.ShoppingChart;
import beans.ShoppingChartItem;
import dao.ShoppingChartDAO;
import dto.AddItemToChartDTO;

public class ShoppingChartService {
	public static ShoppingChartService shoppingChartService = null;
	private ShoppingChartDAO chartDAO;
	
	public static ShoppingChartService getInstance() {
		if(shoppingChartService == null) {
			shoppingChartService = new ShoppingChartService();
		}
		return shoppingChartService;
	}
	
	public ShoppingChartService() {
		shoppingChartService = this;
		this.chartDAO = ShoppingChartDAO.getInstance();
	}

	public void addNewShoppingChart(String userName) {
		ShoppingChart newShoppingChart = new ShoppingChart(userName);
		chartDAO.addShoppingChart(newShoppingChart);
		
	}

	public void changeUserName(String OldUserName, String NewUserName) {
		ShoppingChart oldShoppingChart = chartDAO.getByUsername(OldUserName);
		oldShoppingChart.setUser(NewUserName);
		chartDAO.changeShoppingChart(OldUserName, oldShoppingChart);
		
	}

	public void addItemsToShoppingChart(AddItemToChartDTO params) {
		
		ShoppingChartItem shoppingChartItem = new ShoppingChartItem();
		Artical newArtical = RestaurantService.getInstance().getArticalByName(params.restaurant, params.nameArtical);
		shoppingChartItem.setArtical(newArtical);
		shoppingChartItem.setQuantity(Integer.parseInt(params.quantity));
		
		ShoppingChart shoppingChart = chartDAO.getByUsername(params.username);
		if(shoppingChart == null) {
			addNewShoppingChart(params.username);
			shoppingChart = chartDAO.getByUsername(params.username);
		}
		boolean added = false;
		for(int i=0; i<shoppingChart.items.size(); i++) {
			if(shoppingChart.items.get(i).artical.nameArtical.equals(shoppingChartItem.artical.getNameArtical())
					&& shoppingChart.items.get(i).getArtical().getRestaurant().equals(shoppingChartItem.getArtical().getRestaurant())) {
				Integer newQuatity = shoppingChartItem.getQuantity() + shoppingChart.items.get(i).getQuantity();
				shoppingChartItem.setQuantity(newQuatity);
				shoppingChart.items.set(i, shoppingChartItem);
				added = true;
				break;
			}
		}
		if(!added) {
			shoppingChart.items.add(shoppingChartItem);
		}
		shoppingChart.setPrice(calculateTotalPrice(shoppingChart));
		chartDAO.changeShoppingChart(params.username, shoppingChart);
		
		
	}

	private double calculateTotalPrice(ShoppingChart shoppingChart) {
		double totalPrice = 0.0;
		for(ShoppingChartItem si : shoppingChart.items) {
			totalPrice+= si.artical.price*si.quantity;
		}
		return totalPrice;
	}

}