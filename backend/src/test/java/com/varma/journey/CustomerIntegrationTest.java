package com.varma.journey;


import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.varma.customer.Customer;
import com.varma.customer.CustomerRegistration;
import com.varma.customer.CustomerUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM=new Random();

    @Test
    void canRegisterACustomer() {
        //create registration request
        Faker faker=new Faker();
        Name fakerName = faker.name();
       String name=fakerName.fullName();
       String email= fakerName.lastName()+ UUID.randomUUID()+"@gmail.com";
       int age= RANDOM.nextInt(1,100);
        CustomerRegistration registration= new CustomerRegistration(name,email,age);

        //send a post request
                webTestClient.post().uri("api/v1/customer/new")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(registration),CustomerRegistration.class)
                        .exchange()
                        .expectStatus()
                        .isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri("api/v1/customer")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        //make sure the customer is present
            Customer expected= new Customer(name,email,age);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        var id=allCustomers.stream().filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();
        expected.setId(id);

        //get customer id
        webTestClient.get()
                .uri("api/v1/customer/{customerId}",id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                }).isEqualTo(expected);

    }

    @Test
    void canDeleteCustomer() {
        //create registration request

        Faker faker=new Faker();
        Name fakerNmae = faker.name();
        String name=fakerNmae.fullName();
        String email= fakerNmae.lastName()+ UUID.randomUUID()+"@gmail.com";
        int age= RANDOM.nextInt(1,100);
        CustomerRegistration registration= new CustomerRegistration(name,email,age);

        //send a post request
        webTestClient.post().uri("api/v1/customer/new")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registration),CustomerRegistration.class)
                .exchange()
                .expectStatus()
                .isOk();
        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri("api/v1/customer")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        var id=allCustomers.stream().filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        //delete cusotmer

         webTestClient.delete()
                 .uri("api/v1/customer/{id}",id)
                 .accept(MediaType.APPLICATION_JSON)
                 .exchange()
                 .expectStatus()
                 .isOk();

        //get customer id
        webTestClient.get()
                .uri("api/v1/customer/{customerId}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        //create registration request

        Faker faker=new Faker();
        Name fakerNmae = faker.name();
        String name=fakerNmae.fullName();
        String email= fakerNmae.lastName()+ UUID.randomUUID()+"@gmail.com";
        int age= RANDOM.nextInt(1,100);
        CustomerRegistration registration= new CustomerRegistration(name,email,age);

        //send a post request
        webTestClient.post().uri("api/v1/customer/new")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registration),CustomerRegistration.class)
                .exchange()
                .expectStatus()
                .isOk();
        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri("api/v1/customer")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        var id=allCustomers.stream().filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();
        //update cusotmer
        String newName="newname";
        CustomerUpdate customerUpdate=new CustomerUpdate(newName,null,null);

        webTestClient.put()
                .uri("api/v1/customer/{id}",id )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerUpdate),CustomerUpdate.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get customer id
        Customer updateCustomer = webTestClient.get()
                .uri("api/v1/customer/{customerId}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer customer=new Customer(
                id,newName,age,email
        );
        assertThat(updateCustomer).isEqualTo(customer);

    }
}
