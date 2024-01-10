package com.pankaj.data_mesh_profiler.service;


import com.pankaj.data_mesh_profiler.Mapper.CustomerMapper;
import com.pankaj.data_mesh_profiler.dto.CustomException;
import com.pankaj.data_mesh_profiler.dto.Customer;
import com.pankaj.data_mesh_profiler.dto.CustomerDTO;
import com.pankaj.data_mesh_profiler.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    @Transactional
    public void addCustomer(CustomerDTO customer) {
        Customer customer1 = CustomerMapper.mapDtoToEntity(customer);
        customerRepository.save(customer1);
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    public void updateCustomerById(Long customerId, Customer existingCustomer) throws RuntimeException {
        Customer matchingExistingCustomer = getCustomerById(customerId);
        if(matchingExistingCustomer == null)
             throw new CustomException("UserId does not exist  ");

        existingCustomer.setUpdatedAt(LocalDateTime.now());
        existingCustomer.setCreatedAt(existingCustomer.getCreatedAt() == null ? matchingExistingCustomer.getCreatedAt() : existingCustomer.getCreatedAt());
        existingCustomer.setName(existingCustomer.getName() == null ? matchingExistingCustomer.getName() : existingCustomer.getName());
        existingCustomer.setCustomerStatus(existingCustomer.getCustomerStatus() == null ? matchingExistingCustomer.getCustomerStatus() : existingCustomer.getCustomerStatus());
        existingCustomer.setEmail(existingCustomer.getEmail() == null ? matchingExistingCustomer.getEmail() : existingCustomer.getEmail());
        existingCustomer.setPhone(existingCustomer.getPhone() == null ? matchingExistingCustomer.getPhone() : existingCustomer.getPhone());
        existingCustomer.setAddress(existingCustomer.getAddress() == null ? matchingExistingCustomer.getAddress() : existingCustomer.getAddress());
        existingCustomer.setCompanyName(existingCustomer.getCompanyName() == null ? matchingExistingCustomer.getCompanyName() : existingCustomer.getCompanyName());
        existingCustomer.setIndustryType(existingCustomer.getIndustryType() == null ? matchingExistingCustomer.getIndustryType() : existingCustomer.getIndustryType());
        existingCustomer.setAccountManagerId(existingCustomer.getAccountManagerId() == null ? matchingExistingCustomer.getAccountManagerId() : existingCustomer.getAccountManagerId());
        customerRepository.save(existingCustomer);
    }

    @Transactional
    public void deleteCustomerById(Long customerId) {
        customerRepository.deleteById(customerId);
    }


    @Transactional
    public void bulkLoadCustomers(List<CustomerDTO> customers) {

        List<Customer> customerList = customers.stream()
                .map(CustomerMapper::mapDtoToEntity)
                .collect(Collectors.toList());

        int batchSize = 50;
        for (int i = 0; i < customerList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, customerList.size());
            List<Customer> batch = customerList.subList(i, endIndex);
            customerRepository.saveAll(batch);
        }
    }

    @Transactional
    public void bulkUpdateCustomers(List<Customer> customerList) {

        int batchSize = 50;
        for (int i = 0; i < customerList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, customerList.size());
            List<Customer> batch = customerList.subList(i, endIndex);

            List<Customer> existingCustomers = customerRepository.findAllById(batch.stream().map(Customer::getCustomerId).collect(Collectors.toList()));
            List<Customer> eligibleCustomers = batch.stream()
                    .filter(c-> existingCustomers.stream().map(Customer::getCustomerId).collect(Collectors.toList()).contains(c.getCustomerId()))
                    .peek(existingCustomer -> {
                        existingCustomer.setUpdatedAt(LocalDateTime.now());

                        Customer matchingExistingCustomer = existingCustomers.stream()
                                .filter(c -> c.getCustomerId().equals(existingCustomer.getCustomerId()))
                                .findFirst()
                                .orElse(null);

                        if (matchingExistingCustomer != null) {
                            existingCustomer.setCreatedAt( matchingExistingCustomer.getCreatedAt()!=null ? matchingExistingCustomer.getCreatedAt():LocalDateTime.now());
                            existingCustomer.setName(existingCustomer.getName() == null ? matchingExistingCustomer.getName() : existingCustomer.getName());
                            existingCustomer.setCustomerStatus(existingCustomer.getCustomerStatus() == null ? matchingExistingCustomer.getCustomerStatus() : existingCustomer.getCustomerStatus());
                            existingCustomer.setEmail(existingCustomer.getEmail() == null ? matchingExistingCustomer.getEmail() : existingCustomer.getEmail());
                            existingCustomer.setPhone(existingCustomer.getPhone() == null ? matchingExistingCustomer.getPhone() : existingCustomer.getPhone());
                            existingCustomer.setAddress(existingCustomer.getAddress() == null ? matchingExistingCustomer.getAddress() : existingCustomer.getAddress());
                            existingCustomer.setCompanyName(existingCustomer.getCompanyName() == null ? matchingExistingCustomer.getCompanyName() : existingCustomer.getCompanyName());
                            existingCustomer.setIndustryType(existingCustomer.getIndustryType() == null ? matchingExistingCustomer.getIndustryType() : existingCustomer.getIndustryType());
                            existingCustomer.setAccountManagerId(existingCustomer.getAccountManagerId() == null ? matchingExistingCustomer.getAccountManagerId() : existingCustomer.getAccountManagerId());
                        }
                    })
                    .collect(Collectors.toList());

            if(!eligibleCustomers.isEmpty())
                customerRepository.saveAll(eligibleCustomers);

        }
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }



    public List<Customer> getCustomersByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}

