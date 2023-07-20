package com.varma.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {
    private CustomerJPADataAccessService underTest;
    private  AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;


    @BeforeEach
    void setUp() {
         autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //when
        underTest.selectAllCustomers();
        //then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        //given
        Long id=1l;
        //when
        underTest.selectCustomerById(id);
        //then
        verify(customerRepository).findById(id);
    }

    @Test
    void addCustomer() {
        //given
        Customer customer=new Customer(1l,"name",23,"email");

        //when
            underTest.addCustomer(customer);
        //then
        verify(customerRepository).save(customer);
    }

    @Test
    void exitsPersonWithEmail() {
        //given
        String email= "email";
        //when
        underTest.exitsPersonWithEmail(email);
        //then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        //given
        Long id = 1l;
        //when
        underTest.existsPersonWithId(id);
        //then
        verify(customerRepository).existsById(id);
    }

    @Test
    void deleteCustomer() {
        //given
        Long id= 1l;
        //when
        underTest.deleteCustomer(id);
        //then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //given
        Customer customer=new Customer("name","email",23);
        //when
        underTest.updateCustomer(customer);
        //then
        verify(customerRepository).save(customer);
    }
}