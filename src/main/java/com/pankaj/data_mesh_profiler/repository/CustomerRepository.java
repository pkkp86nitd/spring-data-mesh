package com.pankaj.data_mesh_profiler.repository;



import com.pankaj.data_mesh_profiler.dto.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    List<Customer> findByEmail(@Param("email") String email);

}
