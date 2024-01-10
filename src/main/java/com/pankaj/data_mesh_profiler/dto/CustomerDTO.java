package com.pankaj.data_mesh_profiler.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String companyName;
    private String industryType;
    private Short customerStatus;
    private Long accountManagerId;
    // Constructors, getters, and setters
}

