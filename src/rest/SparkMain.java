package rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

import beans.Location;
import beans.Restaurant;
import beans.Restaurant.Status;
import beans.Restaurant.TypeOfRestaurant;
import beans.User;
import dao.RestaurantDAO;
import dto.UserRegistrationDTO;
import dto.SearchForRestaurantsParamsDTO;
import dto.UserLoginDTO;
import service.RestaurantService;
import service.UserService;
import spark.Session;

import static spark.Spark.*;

public class SparkMain {

	public static void main(String[] args) throws IOException {
		port(8080);
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		after((req,res) -> res.type("application/json"));
		
		UserService userService = new UserService();
		RestaurantService restaurantService = new RestaurantService();
		Gson g = new Gson();
		
		post("rest/CustomerReg/", (req, res) ->{
			res.type("application/json");
			res.status(200);
			UserRegistrationDTO params = g.fromJson(req.body(), UserRegistrationDTO.class);
			userService.registerUser(params);
		return "OK";
		});
		
		post("rest/usernameExists", (req, res) -> {
			res.type("application/json");
			res.status(200);
			String username = g.fromJson(req.body(), String.class);
		return userService.UsernameExists(username);
		});
		
		get("rest/restaurants", (req, res) -> {
			res.type("application/json");
			res.status(200);
			Location l1 = new Location("12.11","122","Dunavska","121bb", "Novi Sad", "21000");
			Location l2 = new Location("12.11","122","Marinikova","1111", "Kikina", "122");
			Location l3 = new Location("12.11","122","Main street","121bb", "Los Angeles", "21000");


			Restaurant r1 = new Restaurant("Andreina kuhinja", TypeOfRestaurant.ITALIAN , Status.OPEN, l1 , null, "../images/podrazumevani-logo-restorana.jpg", 4.5);
			Restaurant r2 = new Restaurant("Andreina kuhinja 2", TypeOfRestaurant.ITALIAN , Status.OPEN, l2 , null, "../images/podrazumevani-logo-restorana.jpg", 2.1);
			Restaurant r3 = new Restaurant("Andreina kuhinja 2", TypeOfRestaurant.CHINESE , Status.OPEN, l3 , null, null, 3.7);



			ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
			restaurants.add(r1);
			restaurants.add(r2);
			restaurants.add(r3);
			RestaurantDAO restDao = RestaurantDAO.getInstance();
			restDao.addAll(restaurants);
			System.out.println("haahaaj");
			return g.toJson(restaurants);
			});
		

		get("rest/login", (req, res) -> {
			res.type("application/json");
			res.status(200);
			
			UserLoginDTO user = new UserLoginDTO(); 
			user.userName = req.queryParams("userName");
		    user.password = req.queryParams("password");
		    if(!userService.isExistUser(user))
		    	return "YOUR ACCOUNT DOES NOT EXIST IN THE SYSTEM, PLEASE REGISTER!";
		    
		    User loginUser=userService.loginUser(user);
           res.cookie("userCOOKIE", loginUser.getUserName());             // set cookie with a value
			
			Session ss = req.session(true);
			ss.attribute("user", loginUser);	
			return  g.toJson(loginUser);
		});
		get("rest/testlogin", (req, res) -> {
			res.type("application/json");
			res.status(200);
			
			Session ss = req.session(true);
			User user = ss.attribute("user");	 
			if(user == null) {
				System.out.println("USER IS NULL");
				return "Err:UserIsNotLoggedIn";
			}
			return g.toJson(user);
		});
		
		get("rest/logout", (req, res) -> {
			res.type("application/json");
			res.status(200);
			Session ss = req.session(true);
			User loggedInUser = ss.attribute("user");
			ss.invalidate();
			System.out.println("See you soon "+loggedInUser.userName+"!");
			
			
			return "OK";
		});
		
		get("rest/searchRestaurants", (req, res) -> {
			res.type("application/json");
			res.status(200);
			String name = req.queryParams("name");
			String location= req.queryParams("location");
			String rating = req.queryParams("rating");
			String type = req.queryParams("type");
			String onlyopened = req.queryParams("onlyopened");
			SearchForRestaurantsParamsDTO parametres = new SearchForRestaurantsParamsDTO(name, location, rating, type, onlyopened);
			ArrayList<Restaurant> searchedRestaurants = restaurantService.searchRestaurants(parametres);
			return g.toJson(searchedRestaurants);
		});
		

			
			
}}
