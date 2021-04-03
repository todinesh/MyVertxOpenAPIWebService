package com.dishatech.services;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.ext.web.api.generator.WebApiServiceGen;

import java.util.List;

import com.dishatech.models.Customer;

/**
*
* @author Dinesh Sharma
*/
@ProxyGen
@WebApiServiceGen
public interface CustomerService {

	public static final String ADDRESS = "customer.v1";

	static CustomerService createProxy(Vertx vertx) {
		return new CustomerServiceVertxEBProxy(vertx, ADDRESS);
	}

	void getCustomerList(List<String> id, List<String> firstName, List<String> lastName, OperationRequest context,
			Handler<AsyncResult<OperationResponse>> resultHandler);

	void createCustomer(Customer body, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

	void getCustomer(String customerId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

	void updateCustomer(String customerId, Customer body, OperationRequest context,	Handler<AsyncResult<OperationResponse>> resultHandler);

	void deleteCustomer(String customerId, OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

}
