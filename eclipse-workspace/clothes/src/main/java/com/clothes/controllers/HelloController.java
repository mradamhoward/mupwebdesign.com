package com.clothes.controllers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.clothes.models.Card;
import com.clothes.models.Order;
import com.clothes.models.Product;
import com.clothes.models.User;
import com.clothes.services.UserMapper;
import com.clothes.services.userService;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import antlr.collections.List;

import org.bson.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.data.jpa.*;



@Controller
public class HelloController {

	//Collections
	private ArrayList<Product> cart = new ArrayList<Product>();
	
	private ArrayList<Product> mensDB = new ArrayList<Product>();
	private ArrayList<Product> kidsDB = new ArrayList<Product>();
	private ArrayList<Product> womensDB = new ArrayList<Product>();
	
	private ArrayList<Order> orders = new ArrayList<Order>();
	
	private static DriverManagerDataSource dataSource = new DriverManagerDataSource();
	
	private ArrayList<User> users = new ArrayList<User>();
	
	JdbcTemplate jt;
	private RestTemplate rt;
     
    EmbeddedDatabase ed;
	
	final String DB_URL = "jdbc:h2:~/test";
	final String DB_USER = "sa";
	final String DB_PASS = "sa";
	
	final String Mongo_IP = "127.0.0.1";
	//Thread management
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	private int numOrders = 0;
	//ApplicationContext
	private ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	
	private boolean loggedIn;
	private String currentUser;
	private String password;
	
	String currentEmail = null;
	
	
	User user;
	
	String searchQuery;
	
	long visaDebitNumber;
	int cvv;
	String nameOnCard;
	String expiry;
	
	userService userService = new userService();
	
   
    
    @RequestMapping("/kids")
    public String kid(Model model) {
        model.addAttribute("kids", kidsDB);
        loadKids();
        return "kids";
    }
    

    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("mens", mensDB);
        loadWomen();
    	loadMens();
    	loadKids();
        return "hello";
    }
    //Account orders logoit
    @RequestMapping("/profile")
    public String pro(Model model) {
        model.addAttribute("orders", orders);
        return "profile";
    }
    
   
    @RequestMapping("/mens")
    public String mens(Model model) {
        model.addAttribute("mens", mensDB);
        return "mens";
    }
    //Landing
    @RequestMapping(value = "/")
    public String greet(@ModelAttribute("product") Product pe, @ModelAttribute("user") User user, BindingResult br,Model model) {
    	
    	loadWomen();
    	loadMens();

    	model.addAttribute("mens",mensDB);	
    	model.addAttribute("user", user);
        return "login";
    }
    @RequestMapping("/account")
    public String account(Model model) {
        model.addAttribute("orders", orders);
        return "profile";
    }
    @RequestMapping("/checkout")
    public String checkout(Model model) {
      
        
        model.addAttribute("cart", cart);
        
        return "checkout";
    }
    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public String search(@ModelAttribute("query") String q, Model model) {
        ArrayList<Product> res = new ArrayList<Product>();
        for(Product a: mensDB) {
        	if (a.getName() == q)
        		res.add(a);
        }
        model.addAttribute("search", res);
        
        return "search";
    }
    
    public double totalCart() {
    	double amount = 0;
    	for(Product a: cart) {
    		amount += a.getPrice();
    	}
    	return amount;
    }
    
    @RequestMapping(value = "/card", method = {RequestMethod.GET, RequestMethod.POST})
    public String card(@ModelAttribute("card") Card card, Model model) {
        
        model.addAttribute("cart", cart);
        model.addAttribute("total", totalCart());
        model.addAttribute("card", card);
        
        
        Card c = new Card();
        Order currentOrder = new Order();
        
        try {  	
        	
        	System.out.println(card.getVisaDebitNumber());
        	System.out.println(card.getCvv());
        	System.out.println(card.getExpiry());
        	c.setVisaDebitNumber((long)card.getVisaDebitNumber());
        	c.setCvv(card.getCvv());
        	c.setExpiry(card.getExpiry());

        	currentOrder.setPaymentInfo(c);
        	currentOrder.setThisOrder(cart);
        	currentOrder.setAmount(totalCart());
        	currentOrder.setCustomerName(currentUser);
        	currentOrder.setCustomerEmail(currentEmail);
        	currentOrder.setVisa(c.getVisaDebitNumber());

            if(currentOrder.getPaymentInfo().getVisaDebitNumber() > 1000)
             	orders.add(currentOrder);
        } catch(Exception e) {
        	System.out.println("NEED PAYMENT INFO");
        }
       
     
        
        for(Order o :orders) {
        	System.out.println(o.productIDS());
        	System.out.println(o.payment());
        }

        if(c.getVisaDebitNumber() > 1000)
        	return "checkout";
        else
        	return "card";
    }
    //Add item to cart
    @RequestMapping(value = "/addCart", method = {RequestMethod.GET, RequestMethod.POST})
    public String addToCart(@ModelAttribute("product") Product product, BindingResult br, Model model) {
    	boolean foundWomen = false;
    	model.addAttribute("product", product);
    	model.addAttribute("mens", mensDB);
    	model.addAttribute("womens", womensDB);
        try {

        	for (Product a : mensDB) {
        		if(a.getId() == product.getId()) {
        			cart.add(a);
        		}
        	}
        	for (Product a : womensDB) {
        		if(a.getId() == product.getId()) {
        			cart.add(a);
        			foundWomen = true;
        		}
        	}
        	
        } catch(Exception e) {
        }
        
        for(Product p : cart) {
        	System.out.println(p.getId());
        	System.out.println(p.getName());
        	System.out.println(p.getDesc());
        	System.out.println(p.getPrice());
        	System.out.println(p.getImage());
        }
        
        System.out.println("Cart Size " + cart.size());
        
        System.out.println(br.toString());
        
      
        
        if(foundWomen)
        	return "womens";
        else
        	return "hello";
    }
    
    public void makeOrder(Order o) {
    	orders.add(o);
    }
    // Remove Cart item
    @RequestMapping(value = "/remove", method = {RequestMethod.GET, RequestMethod.POST})
    public String remove(@ModelAttribute("product") Product product, BindingResult br, Model model) {
 
    	model.addAttribute("product", product);
    	model.addAttribute("mens", mensDB);
    	model.addAttribute("cart", cart);
        try {

        	for (Product a : cart) {
        		if(a.getId() == product.getId()) {
        			cart.remove(a);
        		}
        	}
        } catch(Exception e) {
        }
        
        for(Product p : cart) {
        	System.out.println(p.getId());
        	System.out.println(p.getName());
        	System.out.println(p.getDesc());
        	System.out.println(p.getPrice());
        	System.out.println(p.getImage());
        }
        
        System.out.println("Cart Size " + cart.size());
        
        System.out.println(br.toString());
        
        return "checkout";
    }
    
    @RequestMapping("/womens")
    public String women(Model model) {
    	syncWomens();
    	
        model.addAttribute("womens", womensDB);
     
        return "womens";
    }
    
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String log(@ModelAttribute("user") User user,  Model model) {
      
        try {
    		model.addAttribute("user", user);
    	} catch(Exception e) {
    		
    	}
    	
        model.addAttribute("mens", mensDB);
       
        
    	String email = user.getEmail();
    	String password = user.getPassword();
    	
    	currentUser = user.getName();
    	currentEmail = user.getEmail();
    	
    	System.out.println(user.getEmail());
    	System.out.println(user.getPassword());
    	
    	syncUsers();
    	
    	if(currentEmail != null)
    		return "hello";
    	else
    		return "login";
    }
    public boolean inUsers(User a) {
    	boolean isIn = false;
    	for(User u : users) {
    		if(u.getEmail()== a.getEmail()){
    			isIn = true;
    		}
    	}
    	System.out.println(users.size());
    	System.out.println(isIn);
    	return isIn;
    }
    //Account creation
    @RequestMapping(value = "/register",method = {RequestMethod.GET, RequestMethod.POST})
    public String reg(@ModelAttribute("user") User user,Model model) {
    	int userBefore = users.size();
    	try {
    		model.addAttribute("user", user);
    	} catch(Exception e) {
    		
    	}
    	
    	
    	String name = user.getName();
    	String email = user.getEmail();
    	String password = user.getPassword();
    	String confpass = user.getPassword();
    	if(name != null && email != null && password != null) {
    		createAccount(name, email, password);
    		users.add(user);
    		currentUser = name;
    		currentEmail = email;
    	}
    	
    	System.out.println(name);
    	System.out.println(email);
    	System.out.println(password);
    	
        if(users.size() > userBefore)
        	return "login";
        else
        	return "register";
    }
    
    public void createAccount(String name, String email, String pass) {
    	User u = new User(name, email, pass);
    	
    	String insert = "INSERT INTO USER (NAME, EMAIL, PASSWORD) VALUES (?, ?, ?)";
    
    	dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:~/test");
		dataSource.setUsername("sa");
		dataSource.setPassword("sa");
		jt = new JdbcTemplate(dataSource);
		
		jt.update(insert, u.getName(), u.getEmail(), u.getPassword());
    
    }
    //Sync Mens DB
    public void mens() {
    	
    	try {

    		MongoDatabase db;

    		db = new MongoClient("127.0.0.1", 27017).getDatabase("clothes");
    		MongoCollection<Document> mens = db.getCollection("mens");

    		Product schema = new Product();
    		schema.setId(1);
    		schema.setName("item1");
    		schema.setDesc("Nice jumper");
    		schema.setPrice(5);
    		schema.setImage("men1.jpg");

    		mensDB.add(schema);

    		BasicDBObject document = new BasicDBObject();
    		document.put("name", schema.getName());
    		document.put("id", schema.getId());
    		document.put("desc", schema.getDesc());
    		document.put("price", schema.getPrice());

    		mens.insertOne(new Document().parse(document.toJson()));


    		System.out.println(mens.find().iterator().next().toJson());

    	} catch (Exception e) {

    	}

    }
    
  
	public boolean validate(String email,String password) {
    	boolean validate = false;
    	syncUsers();
    	
    	User test = new User();
    	test.setEmail(email);
    	test.setPassword(password);
    	
    	System.out.println(password);
    	System.out.println(test.getPassword());
    	
    	for(User a : users) {
    		if(a.getEmail() == email)
    			validate = true;
    		
    	}
    	
    	System.out.println(validate);
    	
    	return validate;
    }
	
	public void syncUsers() {
		
		//ORM
		UserMapper mapper = new UserMapper();
		String get = "SELECT * FROM USER;";
		
		try {
			
			dataSource.setDriverClassName("org.h2.Driver");
			dataSource.setUrl("jdbc:h2:~/test");
			dataSource.setUsername("sa");
			dataSource.setPassword("sa");
			jt = new JdbcTemplate(dataSource);
			
			ArrayList<User> result = (ArrayList<User>) jt.query(get, mapper);
			
			for(User u : result) {
				System.out.println(u.getId());
				System.out.println(u.getName());
				System.out.println(u.getEmail());
				System.out.println(u.getPassword());
			}
			
			users.addAll(result);
			System.out.println("USERS SIZE" + users.size());
		} catch(Exception e) {
			
		}
	
	}
    
    public void syncWomens() {
    	//NoSQL
    	try {
    	MongoDatabase db;
    	
    	db = new MongoClient("127.0.0.1", 27017).getDatabase("clothes");
    	
    	
    	
    	MongoCollection<Document> womens = db.getCollection("womens");
    	
    	
    	
    	ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();
    	BasicDBObject document;
    	
    	for(Product p : womensDB) {
    		document = new BasicDBObject();
    		document.put("id", p.getId());
        	document.put("name", p.getName());
        	document.put("desc", p.getDesc());
        	document.put("price", p.getPrice());
        	document.put("image", p.getImage());
        	list.add(document);
    	}
    	
    	for(BasicDBObject o : list) {
    		womens.insertOne(new Document().parse(o.toJson()));
    	}
    	
    	System.out.println(womens.find().iterator().next().toJson());
    	} catch (Exception e) {
    		
    	}
    }
    
    public void syncMens() {
    	MongoDatabase db;
    	
    	db = new MongoClient("127.0.0.1", 27017).getDatabase("clothes");
    	
    	
    	
    	MongoCollection<Document>mens = db.getCollection("mens");
    	
    	
    	
    	ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();
    	BasicDBObject document;
    	
    	for(Product p : mensDB) {
    		document = new BasicDBObject();
    		document.put("id", p.getId());
        	document.put("name", p.getName());
        	document.put("desc", p.getDesc());
        	document.put("price", p.getPrice());
        	document.put("image", p.getImage());
        	list.add(document);
    	}
    	
    	for(BasicDBObject o : list) {
    		mens.insertOne(new Document().parse(o.toJson()));
    	}
    	
    	System.out.println(mens.find().iterator().next().toJson());
    }
    
    void loadMens() {
    	for(int i = 1; i < 7; i++) {
    		mensDB.add( (Product) context.getBean("product" + i));
    	}
    	
    	for(Product a: mensDB) {
    		System.out.println(a.getName());
    		System.out.println(a.getDesc());
    		System.out.println(a.getPrice());
    		System.out.println(a.getImage());
    	}
    }
    
    void loadWomen() {
    	for(int i = 7; i < 14; i++) {
    		womensDB.add( (Product) context.getBean("product" + i));
    	}
    	
    	for(Product a: womensDB) {
    		System.out.println(a.getName());
    		System.out.println(a.getDesc());
    		System.out.println(a.getPrice());
    		System.out.println(a.getImage());
    	}
    }
    
    void loadKids() {
    	for(int i = 14; i < 22; i++) {
    		kidsDB.add( (Product) context.getBean("product" + i));
    	}
    	
    	for(Product a: kidsDB) {
    		System.out.println(a.getName());
    		System.out.println(a.getDesc());
    		System.out.println(a.getPrice());
    		System.out.println(a.getImage());
    	}
    }
    
   
    
}
