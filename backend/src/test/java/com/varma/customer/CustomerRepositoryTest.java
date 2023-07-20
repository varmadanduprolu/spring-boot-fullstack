package com.varma.customer;

import com.varma.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainersUnitTest {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsCustomerByEmail() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)

        );
        underTest.save(customer);
     
        //when
        boolean actual = underTest.existsCustomerByEmail(email1);
        //then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerById() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)
        );
        underTest.save(customer);
        Long id = underTest.findAll().stream().filter(c -> c.getEmail().equals(email1))
                .map(Customer::getId).findFirst().orElseThrow();
        //when
        boolean actual = underTest.existsCustomerById(id);
        //then
        assertThat(actual).isTrue();
    }
}