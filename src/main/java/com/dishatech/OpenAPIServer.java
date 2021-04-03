package com.dishatech;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.dishatech.services.CustomerService;
import com.dishatech.services.CustomerServiceImpl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.serviceproxy.ServiceBinder;

/**
*
* @author Dinesh Sharma
*/
@Component
public class OpenAPIServer {

	private static Logger logger = LoggerFactory.getLogger(OpenAPIServer.class);

	@Value("${api.port}")
	private int port;
	
	@Autowired
	Vertx vertx;

	@Autowired CustomerServiceImpl customerServiceImpl;
	
	HttpServer server;
	
	ServiceBinder serviceBinder;
	
	MessageConsumer<JsonObject> consumer;
	
	//register service on event bus
	private void startCustomerService() {		
		consumer = new ServiceBinder(vertx).setAddress(CustomerService.ADDRESS).register(CustomerService.class, customerServiceImpl);
	}

	private Future<Void> startHttpServer() {
		Promise<Void> promise = Promise.promise();
		OpenAPI3RouterFactory.create(this.vertx, "/openapispec.yaml", openAPI3RouterFactoryAsyncResult -> {
			if (openAPI3RouterFactoryAsyncResult.succeeded()) {
				OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();

				// Mount services on event bus based on extensions
				routerFactory.mountServicesFromExtensions();

				 // Add a security handler
                routerFactory.addSecurityHandler("api_key", routingContext -> {
                    // Handle security here and then call next()
                	logger.info("api_key security handler");
                    routingContext.next();
                });
                
				// Generate the router
				Router router = routerFactory.getRouter();
				server = vertx.createHttpServer(new HttpServerOptions().setPort(port).setHost("localhost"));
				server.requestHandler(router).listen(ar -> {
					if (ar.succeeded())
						promise.complete();
					else
						promise.fail(ar.cause());
				});
			} else {
				promise.fail(openAPI3RouterFactoryAsyncResult.cause());
			}
		});
		return promise.future();
	}

	@EventListener
	public void start(ApplicationReadyEvent event) {
		startCustomerService();
		startHttpServer().onSuccess(success -> logger.debug("HTTP Server started on port {0}", port))
				.onFailure(exception -> logger.debug("Server failed to start {0}", exception));
	}

	/**
	 * This method closes the http server and unregister all services loaded to
	 * Event Bus
	 */
	@PreDestroy
	public void stop() {
		this.server.close();
		consumer.unregister();
	}

}
