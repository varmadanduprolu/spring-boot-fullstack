package com.varma.customer;

import com.varma.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainersUnitTest {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper=new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest= new CustomerJDBCDataAccessService(
                getJdbcTemplate(),customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //given
        Customer customer=  new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID(),
                faker.random().nextInt(18,66)

        );
        underTest.addCustomer(customer);
        //when
        List<Customer> customers = underTest.selectAllCustomers();
        //then

        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {


        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)
        );
        underTest.addCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email1))
                .map(Customer::getId).findFirst().orElseThrow();

        //when
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //then
        assertThat(actual).isPresent().hasValueSatisfying(c->
        {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());

        });

    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //given
        Long id = -1l;
        //when
        var actual= underTest.selectCustomerById(id);
        //then
        //assertThat(actual).isEmpty();
        assertThat(actual).isEmpty();
       // System.out.println(assertThat(actual).isEmpty());

    }

    @Test
    void addCustomer() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)
        );
        //when
         underTest.addCustomer(customer);
        //then
        //assertThat()




    }

    @Test
    void exitsPersonWithEmail() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name= faker.name().fullName();
        Customer customer=  new Customer(
                name,
                email1,
                faker.random().nextInt(18,66)
        );
        underTest.addCustomer(customer);

        //when
        boolean actual = underTest.exitsPersonWithEmail(email1);
        //then
        assertThat(actual).isTrue();

    }


    @Test
    void existsPersonWithEmailReturnsFalseWhenEmailNotExists() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        //when
        boolean actual = underTest.exitsPersonWithEmail(email1);
        //then
        assertThat(actual).isFalse();

    }

    @Test
    void existsPersonWithId() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)
        );
        underTest.addCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email1))
                .map(Customer::getId).findFirst().orElseThrow();
        //when
        boolean actual = underTest.existsPersonWithId(id);
        //then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithIdWillReturnFalseWhenIdNotPresent() {
        //given
        Long id=-1l;
        //when
        boolean actual = underTest.existsPersonWithId(id);
        //then
        assertThat(actual).isFalse();

    }

    @Test
    void deleteCustomer() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)
        );
        underTest.addCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email1))
                .map(Customer::getId).findFirst().orElseThrow();

        //when
        underTest.deleteCustomer(id);
        boolean actual = underTest.existsPersonWithId(id);
        //then
        assertThat(actual).isFalse();


    }

    @Test
    void updateCustomerName() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)
        );
        underTest.addCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email1))
                .map(Customer::getId).findFirst().orElseThrow();
        //when
        var newName="Foo";
        Customer update=new Customer();
        update.setName(newName);
        update.setId(id);
        underTest.updateCustomer(update);

         //then
        var  actual= underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);//change
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });

    }

    @Test
    void updateCustomerAge() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)
        );
        underTest.addCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email1))
                .map(Customer::getId).findFirst().orElseThrow();

        //when age is change
        int age=99;
        Customer update=new Customer();
        update.setAge(age);
        update.setId(id);
        underTest.updateCustomer(update);
        //then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getAge()).isEqualTo(age);//change
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void updateCustomerEmail() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)
        );
        underTest.addCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email1))
                .map(Customer::getId).findFirst().orElseThrow();

        //when age is change
        String email=faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer update=new Customer();
        update.setEmail(email);
        update.setId(id);
        underTest.updateCustomer(update);
        //then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getEmail()).isEqualTo(email);//change
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willUpdateAllPropertiesOfCustomer() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)        );
        underTest.addCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email1))
                .map(Customer::getId).findFirst().orElseThrow();

        //then changes all
        Customer update=new Customer();
        update.setId(id);
        update.setName("new");
        update.setAge(99);
        update.setEmail("faker");
        underTest.updateCustomer(update);

       var actual= underTest.selectCustomerById(id);
        //then
//        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
//            assertThat(customer1.getId()).isEqualTo(id);
//            assertThat(customer1.getName()).isEqualTo(update.getName());//change
//            assertThat(customer1.getAge()).isEqualTo(update.getAge());//change
//            assertThat(customer1.getEmail()).isEqualTo(update.getEmail());//change
//        });
        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        //given
        String email1= faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=  new Customer(
                faker.name().fullName(),
                email1,
                faker.random().nextInt(18,66)
        );
        underTest.addCustomer(customer);
        Long id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email1))
                .map(Customer::getId).findFirst().orElseThrow();

        //when
        Customer update=new Customer();
        update.setId(id);
        underTest.updateCustomer(update);

        //then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
        });


    }
}