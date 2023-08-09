package com.varma.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("api/v1/customer")
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("api/v1/customer/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping("api/v1/customer/new")
    public void addCustomer(@RequestBody CustomerRegistration customerRegistration) {
        customerService.addCustomer(customerRegistration);
    }

    @DeleteMapping("api/v1/customer/{id}")
    public void removeCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);

    }
    @PutMapping("api/v1/customer/{id}")
    public void updateCustomer(@PathVariable Long id,@RequestBody CustomerUpdate customerUpdate){
        customerService.updateCustomer(id,customerUpdate);
    }
}



