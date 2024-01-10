package com.pankaj.data_mesh_profiler.Mapper;


import com.pankaj.data_mesh_profiler.dto.Customer;
import com.pankaj.data_mesh_profiler.dto.CustomerDTO;
import org.apache.hadoop.yarn.util.Times;
import org.apache.spark.sql.Row;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CustomerMapper {
    public static Customer mapDtoToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setCompanyName(customerDTO.getCompanyName());
        customer.setIndustryType(customerDTO.getIndustryType());
        customer.setCustomerStatus(customerDTO.getCustomerStatus());
        customer.setAccountManagerId(customerDTO.getAccountManagerId());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        return customer;
    }

    public static Customer mapRowToCustomer(Row row) {
        Customer customer = new Customer();
        customer.setCustomerId(getLongValue(row, "customer_id"));
        customer.setName(getStringValue(row, "name"));
        customer.setEmail(getStringValue(row, "email"));
        customer.setPhone(getStringValue(row, "phone"));
        customer.setAddress(getStringValue(row, "address"));
        customer.setCompanyName(getStringValue(row, "company_name"));
        customer.setIndustryType(getStringValue(row, "industry_type"));
        customer.setCustomerStatus(getShortValue(row, "customer_status"));
        customer.setAccountManagerId(getLongValue(row, "account_manager_id"));
        customer.setCreatedAt(getLocalDateTimeValue(row, "created_at"));
        customer.setUpdatedAt(getLocalDateTimeValue(row, "updated_at"));
        return customer;
    }

    private static Long getLongValue(Row row, String columnName) {
        try {
            return row.isNullAt(row.fieldIndex(columnName)) ? null : row.getAs(columnName);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getStringValue(Row row, String columnName) {
        try {
            return row.isNullAt(row.fieldIndex(columnName)) ? null : row.getAs(columnName);
        } catch (Exception e) {
            return null;
        }
    }

    private static Short getShortValue(Row row, String columnName) {
        try {
            return row.isNullAt(row.fieldIndex(columnName)) ? null : row.getAs(columnName);
        } catch (Exception e) {
            return null;
        }
    }

    private static LocalDateTime getLocalDateTimeValue(Row row, String columnName) {
        try {
            return row.isNullAt(row.fieldIndex(columnName)) ? null : ((Timestamp) row.getAs(columnName)).toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }
}
