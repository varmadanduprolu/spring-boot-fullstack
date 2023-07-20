package com.varma.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id,name,age,email FROM customer
                """;
        List<Customer> customers=jdbcTemplate.query(sql, customerRowMapper);
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT id,name,age,email FROM customer WHERE id=?
                """;

        return jdbcTemplate.query(sql,customerRowMapper,id).stream().findFirst();
    }

    @Override
    public void addCustomer(Customer customer) {
      var sql= """
              INSERT INTO customer (name,age,email)
              VALUES(?,?,?)
              """;
        int update = jdbcTemplate.update(sql, customer.getName(), customer.getAge(), customer.getEmail());
        System.out.println(update);
    }

    @Override
    public boolean exitsPersonWithEmail(String email) {
        var sql = """
                SELECT COUNT(id) FROM customer WHERE email =?
                """;
        Integer count =jdbcTemplate.queryForObject(sql, Integer.class,email);
        return count!=null && count>0;
    }

    @Override
    public boolean existsPersonWithId(Long id) {
        var sql ="""
                SELECT COUNT(id) FROM customer WHERE id =?
                """;
        Integer count =jdbcTemplate.queryForObject(sql, Integer.class,id);
        return count!=null && count>0;
    }

    @Override
    public void deleteCustomer(Long id) {
        var sql ="""
                DELETE FROM customer WHERE id =?
                """;
        Integer results = jdbcTemplate.update(sql, id);
        System.out.println("number of customer deleted "+results);
    }

    @Override
    public void updateCustomer(Customer customer) {
       if(customer.getName()!=null){
           String sql = "UPDATE CUSTOMER SET name=? WHERE id=?";
            int results= jdbcTemplate.update(sql,customer.getName(),customer.getId());
       }
        if(customer.getAge()!=null){
            String sql = "UPDATE CUSTOMER SET age=? WHERE id=?";
            int results= jdbcTemplate.update(sql,customer.getAge(),customer.getId());
        }
        if(customer.getEmail()!=null){
            String sql = "UPDATE CUSTOMER SET email=? WHERE id=?";
            int results= jdbcTemplate.update(sql,customer.getEmail(),customer.getId());
        }

    }
}
