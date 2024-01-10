package com.pankaj.data_mesh_profiler.repository;

import com.pankaj.data_mesh_profiler.dto.Customer;
import org.apache.spark.sql.*;
import org.apache.spark.sql.sources.In;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class CustomerMockDataSet {


    public static Dataset<Row> createMockCustomerDataFrame(SparkSession sparkSession,int numberOfCustomers) {

        Map<Integer,String> IndustryMap = new HashMap<>();
        IndustryMap.put(0,"IT");
        IndustryMap.put(1,"MANUFACTURE");
        IndustryMap.put(2,"SERVICE");
        IndustryMap.put(3,"GOV");
        IndustryMap.put(4,null);




        List<Customer> mockCustomers = new ArrayList<>();
        for (int i = 1; i <= numberOfCustomers; i++) {
            mockCustomers.add(createCustomer((long) i, "Mock Customer " + i,
                    "mock" + i + "@example.com", "123456789" + i, "Mock Address " + i,
                    "Mock Company " + i,
                    IndustryMap.get(i%7),
                    i%3 != 0 ?  (short) (i%3):null,
                    i%10 != 0 ?  (long) (i%3):null)
            );
        }
         try{
             Dataset<Row> mockCustomerDataFrame = sparkSession.createDataFrame(mockCustomers, Customer.class);

             mockCustomerDataFrame = mockCustomerDataFrame
                     .withColumnRenamed("customerId", "customer_id")
                     .withColumnRenamed("companyName", "company_name")
                     .withColumnRenamed("industryType", "industry_type")
                     .withColumnRenamed("customerStatus", "customer_status")
                     .withColumnRenamed("accountManagerId", "account_manager_id")
                     .withColumnRenamed("createdAt", "created_at")
                     .withColumnRenamed("updatedAt", "updated_at");
             return Mockito.spy(mockCustomerDataFrame);
         }catch (Exception error){
             System.out.println("ERROR: "+ error.getLocalizedMessage());
             return null;
         }

    }

    private static Customer createCustomer(Long id, String name, String email, String phone, String address,
                                           String companyName, String industryType, Short customerStatus, Long accountManagerId) {
        Customer customer = new Customer();
        customer.setCustomerId(id);
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setCompanyName(companyName);
        customer.setIndustryType(industryType);
        customer.setCustomerStatus(customerStatus);
        customer.setAccountManagerId(accountManagerId);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        return customer;
    }

}
