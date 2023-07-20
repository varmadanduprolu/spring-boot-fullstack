package com.varma.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRowMapperTest {



    @Test
    void mapRow() throws SQLException {

        //given
        ResultSet resultSet = mock(ResultSet.class);

        CustomerRowMapper customerRowMapper=new CustomerRowMapper();
        Long id=10l;
        //Customer customer=new Customer(id,"varma",23,"varma@gmail.com");
        when(resultSet.getLong("id")).thenReturn(id);
        when(resultSet.getString("name")).thenReturn("varma");
        when(resultSet.getInt("age")).thenReturn(23);
        when(resultSet.getString("email")).thenReturn("varma@gmail.com");
        //when
        Customer actual = customerRowMapper.mapRow(resultSet, 1);
        //then
        Customer expected=new Customer(id,"varma",23,"varma@gmail.com");
        assertThat(actual).isEqualTo(expected);

    }
}