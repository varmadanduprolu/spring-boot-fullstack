package com.varma.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.List;
import java.util.Optional;

public interface CustomerDAO  {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    void addCustomer(Customer customer);
    boolean exitsPersonWithEmail(String  email);
    boolean existsPersonWithId(Long id);
    void deleteCustomer(Long id);

    void updateCustomer(Customer customer);
}
