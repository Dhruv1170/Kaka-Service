package com.example.demo.repository;

import com.example.demo.dto.CustomerAccessor;
import com.example.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c.id as id, c.cusName as cusName from Customer c" +
            " left join Location l on l.customer.id = c.id " +
            " left join Zone z on l.id = z.location.id where z.id = :zoneId")
    Optional<CustomerAccessor> getCustomerByZoneId(Long zoneId);
}
