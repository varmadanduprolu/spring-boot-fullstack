package com.varma.customer;

import com.varma.exception.DuplicateResourceException;
import com.varma.exception.RequestValidationException;
import com.varma.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {

        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDAO.selectCustomerById(id).orElseThrow(
                () -> new ResourceNotFoundException("customer with  id %s not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistration customerRegistration) {
        //check if email exists already
        if (customerDAO.exitsPersonWithEmail(customerRegistration.email())) {
            throw new DuplicateResourceException("customer with  email already exists");
        }
        Customer customer = new Customer(customerRegistration.name(), customerRegistration.email(), customerRegistration.age());

        //add
        customerDAO.addCustomer(customer);
    }

    public void deleteCustomer(Long id) {
        //check customer with id present or not
        if (!customerDAO.existsPersonWithId(id)) {
            throw new ResourceNotFoundException("customer with id %s is not found".formatted(id));
        }
        customerDAO.deleteCustomer(id);

        // delete
    }



    public void updateCustomer(Long id,CustomerUpdate customerUpdate) {
        Customer customer=getCustomer(id);

        boolean change= false;

        // check either customer present or not
        if (!customerDAO.existsPersonWithId(id)) {
            throw new ResourceNotFoundException("customer with  id %s not found".formatted(id));
                 }
        //then update the customer


        if(customerUpdate.name()!=null && !customer.getName().equals(customerUpdate.name())){
           customer.setName(customerUpdate.name());
           change =true;
        }
        if(customerUpdate.email()!=null && !customer.getEmail().equals(customerUpdate.email())){
            if(customerDAO.exitsPersonWithEmail(customerUpdate.email())){
                throw new DuplicateResourceException("customer with email already taken");
            }
            customer.setEmail(customerUpdate.email());
            change =true;
        }
        if(customerUpdate.age()!=null && !customer.getAge().equals(customerUpdate.age())){
            customer.setAge(customerUpdate.age());
            change =true;

        }
        if(!change) {
            throw new RequestValidationException("no data changes found");
        }
        customerDAO.updateCustomer(customer);


    }
}
