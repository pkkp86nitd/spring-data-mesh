package com.pankaj.data_mesh_profiler.config;


import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = SparkConfig.class)
public class SparkConfigTest {

   @Autowired
    private SparkSession sparkSession;

    @InjectMocks
    private SparkConfig sparkConfig;

    @Value("${spark.app.name}")
    private String appName;

    @Value("${spark.master}")
    private String master;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(sparkConfig, "appName", appName);
        ReflectionTestUtils.setField(sparkConfig, "master",master);
    }

    @Test
    public void testSparkSession() {
        assertNotNull(sparkSession);
        assertNotNull(sparkSession.sessionUUID());
        // Add more assertions about the SparkSession if needed
    }

    @Test
    public void testSparkConfig() {
        assertNotNull(sparkConfig);

        // Validate the properties are injected
        assertNotNull(appName);
        assertNotNull(master);

    }

    @Test
    public void testSparkConf() {
        assertNotNull(sparkConfig);
        // Validate the SparkConf is correctly configured
        SparkSession sparkSession = sparkConfig.sparkSession();
        assertNotNull(sparkSession);
    }
}

