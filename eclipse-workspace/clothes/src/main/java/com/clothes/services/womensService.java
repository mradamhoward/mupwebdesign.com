package com.clothes.services;
import java.lang.*;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class womensService extends Thread{ 
	 
	private final String DB_URL= "";
	
	public womensService() {
		super();
	}
	
	public void run() {
		MongoDatabase db;
		
		db = new MongoClient("127.0.0.1", 27017).getDatabase("clothes");
		

		
		MongoCollection<Document> mens = db.getCollection("mens");
		
	}

}
