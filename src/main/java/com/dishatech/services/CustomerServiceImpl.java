package com.dishatech.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.serviceproxy.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dishatech.models.Customer;
import com.dishatech.persistence.CustomerDBService;

/**
*
* @author Dinesh Sharma
*/
@Component
public class CustomerServiceImpl implements CustomerService {

	private static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired private CustomerDBService customerService;
	
	@Autowired private Vertx vertx;

	@Override
	public void getCustomerList(List<String> id, List<String> firstName, List<String> lastName,
			OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
		
		logger.debug("getCustomerList");
		
		vertx.executeBlocking(promise -> {
			List<Customer> results = customerService.getFilteredCustomers(this.constructFilterPredicate(id, firstName, lastName));
			promise.complete(OperationResponse.completedWithJson(new JsonArray(results.stream().map(Customer::toJson).collect(Collectors.toList()))));
		}, false, resultHandler);
	}

	@Override
	public void createCustomer(Customer body, OperationRequest context,
			Handler<AsyncResult<OperationResponse>> resultHandler) {
		logger.debug("createCustomer");

		Customer customerAdded = customerService.addCustomer(body);
		resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(customerAdded.toJson())));
	}

	@Override
	public void getCustomer(String customerId, OperationRequest context,
			Handler<AsyncResult<OperationResponse>> resultHandler) {
		logger.debug("getCustomer");
		Optional<Customer> t = customerService.getCustomer(customerId);
		if (t.isPresent())
			resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(t.get().toJson())));
		else
			resultHandler.handle(Future.failedFuture(new ServiceException(404,"Not found")));
	}

	@Override
	public void updateCustomer(String customerId, Customer body, OperationRequest context,
			Handler<AsyncResult<OperationResponse>> resultHandler) {
		logger.debug("updateCustomer");
		if (customerService.updateCustomer(customerId, body))
			resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(body.toJson())));
		else
			resultHandler.handle(Future.failedFuture(new ServiceException(404,"Not found")));
	}

	@Override
	public void deleteCustomer(String customerId, OperationRequest context,
			Handler<AsyncResult<OperationResponse>> resultHandler) {
		logger.debug("deleteCustomer");
		if (customerService.removeCustomer(customerId))
			resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(200).setStatusMessage("OK")));
		else
			resultHandler.handle(Future.failedFuture(new ServiceException(404,"Not found")));
	}

	private Predicate<Customer> constructFilterPredicate(List<String> id, List<String> firstName,
			List<String> lastName) {
		logger.debug("constructFilterPredicate");
		List<Predicate<Customer>> predicates = new ArrayList<>();
		if (id != null) {
		      predicates.add(customer -> id.contains(customer.getId()));
		}
		if (firstName != null) {
		      predicates.add(customer -> firstName.contains(customer.getFirstName()));
		}
		if (lastName != null) {
			predicates.add(customer -> lastName.stream().filter(o -> ((String) o).contains(customer.getLastName()))
					.count() > 0);
		}
		// Elegant predicates combination
		return predicates.stream().reduce(customer -> true, Predicate::and);
	}

}
