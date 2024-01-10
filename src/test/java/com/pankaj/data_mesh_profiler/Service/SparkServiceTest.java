package com.pankaj.data_mesh_profiler.Service;

import com.pankaj.data_mesh_profiler.dto.Customer;
import com.pankaj.data_mesh_profiler.repository.CustomerMockDataSet;
import com.pankaj.data_mesh_profiler.service.SparkService;
import org.apache.spark.sql.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class SparkServiceTest {


    @Autowired
    private SparkSession sparkSession;

    @Spy
    private SparkService sparkService;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private Dataset<Row> mockDataFrame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(sparkService, "dbUrl", dbUrl);
        ReflectionTestUtils.setField(sparkService, "dbUserName", dbUserName);
        ReflectionTestUtils.setField(sparkService, "dbPassword", dbPassword);

        mockDataFrame = CustomerMockDataSet.createMockCustomerDataFrame(sparkSession,1000);
    }


    @Test
    public void testGetCount() {

        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();


        Long result = sparkService.getCount();

        assertEquals(mockDataFrame.count(), result);
    }

    @Test
    public void testGetMissingValues() {
        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();


        Map<String, Long> result = sparkService.getMissingValues();


        assertEquals(0, result.get("customer_id"));
        assertEquals(429, result.get("industry_type"));
        // ... repeat for other columns
    }

    @Test
    void getValueFrequency() {

        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();

        Map<String, Map<Object, Long>> valueFrequency = sparkService.getValueFrequency();

        assertEquals(mockDataFrame.columns().length, valueFrequency.size());
        Map<Object,Long> expectedMap = new HashMap<>();
        expectedMap.put("IT",142L);
        expectedMap.put("MANUFACTURE",143L);
        expectedMap.put("SERVICE",143L);
        expectedMap.put("GOV",143L);
        assertEquals(valueFrequency.get("industry_type"),expectedMap);
//...repeat for other cols

    }
//
    @Test
    void filterData() {

        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();

        Map<String, Object> filters = new HashMap<>();
        filters.put("customer_status", 1);

        List<Customer> filteredData = sparkService.filterData(filters);
        assertEquals(filteredData.size(),334);
        //add more asserts

    }

    @Test
    void getDataDistribution() {

        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();

       Map<Object, Object> dataDistribution = sparkService.getDataDistribution("customer_status");
        assertEquals(dataDistribution.size(),5);
        assertEquals(dataDistribution.get("min"),"1");
        assertEquals(dataDistribution.get("max"),"2");
        assertEquals(dataDistribution.get("count"),"667");

        //add more asserts

    }

    @Test
    void getSummaryStatistics() {

        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();

        List<String> columns = Arrays.asList("customer_id", "customer_status");
        Map<String, Map<String, Object>> summaryStatistics = sparkService.getSummaryStatistics(columns);

        assertEquals(columns.size(), summaryStatistics.size());
        assertEquals(summaryStatistics.get("customer_status").get("min"), (short)1);
        assertEquals(summaryStatistics.get("customer_status").get("max"),(short)2);
        assertEquals(((Double)summaryStatistics.get("customer_status").get("mean")),1.499,0.001);

    }

    @Test
    void getCorrelation() {

        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();

        double correlation = sparkService.getCorrelation("customer_status", "account_manager_id");

        assertEquals(0.885, correlation,0.001);

    }

    @Test
    void getUniqueValues() {

        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();

        Map<String, List<String>> uniqueValues = sparkService.getUniqueValues("industry_type");

        assertEquals(5, uniqueValues.get("values").size());

    }

    @Test
    void getHistogram() {

        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();
         Map<Object, Long> histogram = sparkService.getHistogram("customer_status");

        assertEquals(3, histogram.size());
        assertEquals(334L, histogram.get((short)1));
        assertEquals(333L, histogram.get((short)2));
        assertEquals(333L, histogram.get(null));

    }

   @Test
    void getTopNValues() {

        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();

       Map<Object, Long> topNValues = sparkService.getTopNValues("industry_type", 3);

        assertTrue(3 >= topNValues.size());
        assertEquals(topNValues.values().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList())
                , new ArrayList<>(topNValues.values())
        );

    }
//
    @Test
    void getDataQualityMetrics() {

        // Mock the DataFrame
        doReturn(mockDataFrame).when(sparkService).loadCustomerDataFrame();

        List<String> columns = Arrays.asList("customer_id", "customer_status");
        Map<String, Double> dataQualityMetrics = sparkService.getDataQualityMetrics(columns);

        assertEquals(columns.size(), dataQualityMetrics.size());
        assertEquals(dataQualityMetrics.get("customer_id"),1.0);
        assertEquals(dataQualityMetrics.get("customer_status"),0.667,0.001);
    }

// ... (Additional code for other functions)

}
