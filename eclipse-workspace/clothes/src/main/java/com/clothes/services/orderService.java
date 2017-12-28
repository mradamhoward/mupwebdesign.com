package com.clothes.services;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.clothes.models.Order;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.model.Result;


public class orderService {

	GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
	GraphDatabaseService graphDb = graphDbFactory.newEmbeddedDatabase(new File("file.x"));
	
	
	
	public int makeOrder(Order o) {
		
				
		Node car = graphDb.createNode(Label.label("Order"));
		car.setProperty("products", o.productIDS());
		car.setProperty("card", o.payment());
		car.setProperty("owner", o.getCustomer().getName());
		
		graphDb.execute("MATCH (Order:Order)");
		
		return 1;
	}
}
