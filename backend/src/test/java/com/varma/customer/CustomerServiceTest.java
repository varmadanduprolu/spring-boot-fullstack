package com.varma.customer;

import com.varma.exception.DuplicateResourceException;
import com.varma.exception.RequestValidationException;
import com.varma.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {

        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest= new CustomerService(customerDAO);
    }

//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }



    @Test
    void getAllCustomers() {
        //given

        //when
        underTest.getAllCustomers();
        //then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //given
        Long id = 1l;
        Customer customer=new Customer(id," name",23,"enail");

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
         //when
        Customer actual = underTest.getCustomer(id);
        //then
        assertThat(actual).isEqualTo(customer);
        //verify(customerDAO).selectCustomerById(id);
    }

    @Test
    void willThrownWhenGetCustomerReturnEmptyOptional() {
        //given
        Long id = 1L;

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());
        //when
       // Customer actual = underTest.getCustomer(id);
        //then
         assertThatThrownBy(()->underTest.getCustomer(id)).isInstanceOf(ResourceNotFoundException.class)
                 .hasMessage("customer with  id %s not found".formatted(id));
        //assertThat(actual).isEqualTo(id);
        //verify(customerDAO).selectCustomerById(id);
    }

    @Test
    void addCustomer() {
        //given
         String email="varma@gamil.com";
         when(customerDAO.exitsPersonWithEmail(email)).thenReturn(false);
        //when
        CustomerRegistration customerRegistration=new CustomerRegistration("varma","varma@gamil.com",23);
            underTest.addCustomer(customerRegistration);
        //then
        ArgumentCaptor<Customer> customerArgumentCaptor=ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).addCustomer(customerArgumentCaptor.capture());
        Customer value = customerArgumentCaptor.getValue();
        assertThat(value.getId()).isNull();
        assertThat(value.getName()).isEqualTo(customerRegistration.name());
        assertThat(value.getAge()).isEqualTo(customerRegistration.age());
        assertThat(value.getEmail()).isEqualTo(customerRegistration.email());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        //given
        String email="varma@gamil.com";
        when(customerDAO.exitsPersonWithEmail(email)).thenReturn(true);
        //when
        CustomerRegistration customerRegistration=new CustomerRegistration("varma","varma@gamil.com",23);
       // underTest.addCustomer(customerRegistration);
        assertThatThrownBy(()->underTest.addCustomer(customerRegistration))
                .isInstanceOf(DuplicateResourceException.class)
                        .hasMessage("customer with  email already exists");
        //then
        verify(customerDAO,never()).addCustomer(any());

    }

    @Test
    void deleteCustomer() {
        //given
         Long id=1l;
        when(customerDAO.existsPersonWithId(id)).thenReturn(true);
        //then
          underTest.deleteCustomer(id);
        //when
        verify(customerDAO).deleteCustomer(id);
    }

    @Test
    void willThrownDeleteCustomerWhenNotExists() {
        //given
        Long id=1l;
        when(customerDAO.existsPersonWithId(id)).thenReturn(false);
        //then
        assertThatThrownBy(()->underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id %s is not found".formatted(id));
        //when
        verify(customerDAO,never()).deleteCustomer(any());
    }

    @Test
    void canUpdateAllCustomersProperties() {
        //given
        Long id =10l;
        Customer customer=new Customer(id,"varma",23,"varna@gmail.com");
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //when
        String email = "nadhav@gmail.com";
        CustomerUpdate customerUpdate = new CustomerUpdate("madhav",email , 24);
         when(customerDAO.existsPersonWithId(id)).thenReturn(true);
         when(customerDAO.exitsPersonWithEmail(email)).thenReturn(false);
        underTest.updateCustomer(id,customerUpdate);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor=ArgumentCaptor.forClass(Customer.class);
            verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer value = customerArgumentCaptor.getValue();
        assertThat(value.getName()).isEqualTo(customerUpdate.name());
        assertThat(value.getEmail()).isEqualTo(customerUpdate.email());
        assertThat(value.getAge()).isEqualTo(customerUpdate.age());

    }

    @Test
    void canUpdateOnlyCustomersName() {
        //given
        Long id =10l;
        Customer customer=new Customer(id,"varma",23,"varna@gmail.com");
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //when
        CustomerUpdate customerUpdate = new CustomerUpdate("madhav",null , null);
        when(customerDAO.existsPersonWithId(id)).thenReturn(true);
       // when(customerDAO.exitsPersonWithEmail(email)).thenReturn(false);
        underTest.updateCustomer(id,customerUpdate);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor=ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer value = customerArgumentCaptor.getValue();
        assertThat(value.getName()).isEqualTo(customerUpdate.name());
        assertThat(value.getEmail()).isEqualTo(customer.getEmail());
        assertThat(value.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void canUpdateOnlyCustomersEmail() {
        //given
        Long id =10l;
        Customer customer=new Customer(id,"varma",23,"varna@gmail.com");
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //when
        String email = "nadhav@gmail.com";
        CustomerUpdate customerUpdate = new CustomerUpdate(null,email , null);
        when(customerDAO.existsPersonWithId(id)).thenReturn(true);
        when(customerDAO.exitsPersonWithEmail(email)).thenReturn(false);
        underTest.updateCustomer(id,customerUpdate);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor=ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer value = customerArgumentCaptor.getValue();
        assertThat(value.getName()).isEqualTo(customer.getName());
        assertThat(value.getEmail()).isEqualTo(customerUpdate.email());
        assertThat(value.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void canUpdateOnlyCustomersAge() {
        //given
        Long id =10l;
        Customer customer=new Customer(id,"varma",23,"varna@gmail.com");
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //when
        //String email = "nadhav@gmail.com";
        CustomerUpdate customerUpdate = new CustomerUpdate(null,null , 24);
        when(customerDAO.existsPersonWithId(id)).thenReturn(true);
        //when(customerDAO.exitsPersonWithEmail(email)).thenReturn(false);
        underTest.updateCustomer(id,customerUpdate);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor=ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer value = customerArgumentCaptor.getValue();
        assertThat(value.getName()).isEqualTo(customer.getName());
        assertThat(value.getEmail()).isEqualTo(customer.getEmail());
        assertThat(value.getAge()).isEqualTo(customerUpdate.age());

    }

    @Test
    void willThrownWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        //given
        Long id =10l;
        Customer customer=new Customer(id,"varma",23,"varna@gmail.com");
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //when
        String email = "nadhav@gmail.com";
        CustomerUpdate customerUpdate = new CustomerUpdate(null,email , null);
        when(customerDAO.existsPersonWithId(id)).thenReturn(true);
        when(customerDAO.exitsPersonWithEmail(email)).thenReturn(true);

        assertThatThrownBy(()->underTest.updateCustomer(id,customerUpdate))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("customer with email already taken");

        //then
        verify(customerDAO,never()).updateCustomer(any());

    }
    @Test
    void willThrownWhenCustomerHasNoChanges() {
        //given
        Long id =10l;
        Customer customer=new Customer(id,"varma",23,"varna@gmail.com");
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //when
       // String email = "nadhav@gmail.com";
        CustomerUpdate customerUpdate = new CustomerUpdate(customer.getName(),customer.getEmail() , customer.getAge());
        when(customerDAO.existsPersonWithId(id)).thenReturn(true);
       // when(customerDAO.exitsPersonWithEmail(customer.getEmail())).thenReturn(false);
        assertThatThrownBy(()->underTest.updateCustomer(id,customerUpdate))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        //then
        verify(customerDAO,never()).updateCustomer(any());


    }

    @Test
    void willThrownWhenCustomerNotPresentWithId() {
        //given
        Long id=10l;
        //when
        //when(customerDAO.existsPersonWithId(id)).thenReturn(false);
        assertThatThrownBy(()->underTest.updateCustomer(id,null ))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with  id %s not found".formatted(id));
        //then
        verify(customerDAO,never()).updateCustomer(any());
    }
}