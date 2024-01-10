package com.pankaj.data_mesh_profiler.controller;


import com.pankaj.data_mesh_profiler.dto.ApiResponse;
import com.pankaj.data_mesh_profiler.dto.CustomException;
import com.pankaj.data_mesh_profiler.dto.Customer;
import com.pankaj.data_mesh_profiler.dto.CustomerDTO;
import com.pankaj.data_mesh_profiler.service.CustomerService;
import com.pankaj.data_mesh_profiler.service.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customerDTO) {

        try {
            customerService.addCustomer(customerDTO);
            return ResponseEntity.ok("Add successful");
        }catch (Exception error){
            return ResponseEntity.internalServerError().body("error " + error.getLocalizedMessage());
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Customer>> getCustomerById(@PathVariable Long customerId) {

        try {
            Customer customerDTO = customerService.getCustomerById(customerId);
            if(customerDTO != null)
                 return new ResponseEntity<>(ApiResponse.success(customerDTO), HttpStatus.OK);
            else return new ResponseEntity<>(ApiResponse.error("User not found"),HttpStatus.NOT_FOUND);
        }catch (Exception error){
            return new ResponseEntity<>(ApiResponse.error("Server Error " + error.getLocalizedMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long customerId) {

        try {
            customerService.deleteCustomerById(customerId);
            return new ResponseEntity<>(ApiResponse.success("User Deleted"),HttpStatus.OK);
        }catch (Exception error){
            return new ResponseEntity<>(ApiResponse.error("Server Error " + error.getLocalizedMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<String>> udpateCustomer(@PathVariable Long customerId,  @RequestBody Customer customer) {

        try {
            customerService.updateCustomerById(customerId, customer);
            return new ResponseEntity<>(ApiResponse.success("User updated"),HttpStatus.OK);
        }catch (CustomException error){
            return new ResponseEntity<>(ApiResponse.error("Request Error " + error.getLocalizedMessage()),HttpStatus.BAD_REQUEST);
        }catch (Exception error){
            return new ResponseEntity<>(ApiResponse.error("Server Error " + error.getLocalizedMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/bulk-load")
    public ResponseEntity<ApiResponse<String>> bulkLoadCustomers(@RequestBody List<CustomerDTO> customerDTOList) {

          try {
              customerService.bulkLoadCustomers(customerDTOList);
              return new ResponseEntity<>(ApiResponse.success("Bulk add successful"),HttpStatus.OK);
          }catch (Exception error){
              return new ResponseEntity<>(ApiResponse.success("Internal Server Error "+error.getLocalizedMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
          }
    }

    @PutMapping("/bulk-update")
    public ResponseEntity<ApiResponse<String>> bulkUpdateCustomer(@RequestBody List<Customer> customerDTOList) {

        try {
            customerService.bulkUpdateCustomers(customerDTOList);
            return new ResponseEntity<>(ApiResponse.success("Bulk update  successful"),HttpStatus.OK);
        }catch (Exception error){
            return new ResponseEntity<>(ApiResponse.success("error " + error.getLocalizedMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
