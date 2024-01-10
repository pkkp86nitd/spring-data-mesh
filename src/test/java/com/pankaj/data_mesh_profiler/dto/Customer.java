package com.pankaj.data_mesh_profiler.dto;

import lombok.*;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SpringBootTest
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerId")
    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private String address;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "industry_type")
    private String industryType;
    @Column(name = "customer_status")
    private Short customerStatus;
    @Column(name = "account_manager_id")
    private Long accountManagerId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
