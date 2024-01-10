package com.pankaj.data_mesh_profiler;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.stream.Collectors;
import java.util.stream.Stream;
@SpringBootApplication
@ComponentScan
@EntityScan(basePackages = "com.pankaj.data_mesh_profiler.dto")
@EnableJpaRepositories(basePackages = "com.pankaj.data_mesh_profiler.repository")
public class DataMeshProfiler {
    public static void main(String[] args) {
        SpringApplication.run(DataMeshProfiler.class, args);
        System.out.println("Hello world!");
    }
}