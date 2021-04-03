package com.dishatech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import io.vertx.core.Vertx;

/**
 *
 * @author Dinesh Sharma
 */
@SpringBootApplication
public class MyVertxOpenAPIWebServiceMain  {

	@Autowired
	Vertx vertx = Vertx.vertx();
	
	public static void main(String[] args) {
		SpringApplication.run(MyVertxOpenAPIWebServiceMain.class, args);
	}

	@Bean(destroyMethod = "")
	public Vertx vertx() {
		return vertx;
	}
	
}
