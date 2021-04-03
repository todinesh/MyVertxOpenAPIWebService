package com.dishatech.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dishatech.models.Customer;

/**
 * In memory implementation of customer persistence
 *
 * @author Dinesh Sharma
 */
@Service
public class CustomerDBServiceImpl implements CustomerDBService {

    private Map<String, Customer> customer = new HashMap<>();

    @Override
    public List<Customer> getFilteredCustomers(Predicate<Customer> p) {
        return customer.values().stream().filter(p).collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> getCustomer(String transactionId) {
      return Optional.ofNullable(customer.get(transactionId));
    }

    @Override
    public Customer addCustomer(Customer t) {
      customer.put(t.getId(), t);
      return t;
    }

    @Override
    public boolean removeCustomer(String transactionId) {
      Customer t = customer.remove(transactionId);
      if (t != null) return true;
      else return false;
    }

    @Override
    public boolean updateCustomer(String transactionId, Customer transaction) {
      Customer t = customer.replace(transactionId, transaction);
      if (t != null) return true;
      else return false;
    }
}
