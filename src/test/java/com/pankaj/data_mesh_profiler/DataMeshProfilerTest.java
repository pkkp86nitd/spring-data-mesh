package com.pankaj.data_mesh_profiler;

import com.pankaj.data_mesh_profiler.config.SparkConfig;
import com.pankaj.data_mesh_profiler.config.SparkConfigTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.pankaj.data_mesh_profiler.config")
public class DataMeshProfilerTest {

    @Test
    void contextLoads() {
    }
}
