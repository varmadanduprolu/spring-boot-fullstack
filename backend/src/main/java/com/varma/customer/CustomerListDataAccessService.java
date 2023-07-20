package com.varma.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO{


    private static List<Customer> customers;

    static {
        customers= new ArrayList<>();
        Customer madhava = new Customer(1l, "madhava", 24, "madhava@gmail.com");
        customers.add(madhava);
        Customer varma = new Customer(2l,"varma",23,"varma@gmail.com");
        customers.add(varma);
    }
    @Override
    public List<Customer> selectAllCustomers(){
         return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers.stream().filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public void addCustomer(Customer customer) {

    }

    @Override
    public boolean exitsPersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsPersonWithId(Long id) {
        return customers.stream().anyMatch(c-> c.getId().equals(id));
    }

    @Override
    public void deleteCustomer(Long id) {

    }

    @Override
    public void updateCustomer(Customer customer) {

    }


}
