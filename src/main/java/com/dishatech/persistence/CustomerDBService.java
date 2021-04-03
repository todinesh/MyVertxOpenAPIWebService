package com.dishatech.persistence;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.dishatech.models.Customer;

/**
 * This interface represents a persistence layer of your application
 *
 * @author Dinesh Sharma
 */
@Service
public interface CustomerDBService {

  List<Customer> getFilteredCustomers(Predicate<Customer> p);

  Optional<Customer> getCustomer(String transactionId);

  Customer addCustomer(Customer t);

  boolean removeCustomer(String transactionId);

  boolean updateCustomer(String transactionId, Customer transaction);
}
