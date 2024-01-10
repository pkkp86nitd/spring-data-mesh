package com.pankaj.data_mesh_profiler.controller;

import com.pankaj.data_mesh_profiler.dto.ApiResponse;
import com.pankaj.data_mesh_profiler.dto.Customer;
import com.pankaj.data_mesh_profiler.service.SparkService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spark")
public class SparkController {

    @Autowired
    private SparkService sparkService;

    @GetMapping("/data-profiling")
    public ResponseEntity<ApiResponse<String>> performDataProfiling() {
        try {
            sparkService.performDataProfiling();
            return ResponseEntity.ok(ApiResponse.success("Data profiling completed. Check the logs for details."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error in data profiling: " + e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getCount() {
        try {
            Long count = sparkService.getCount();
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting count: " + e.getMessage()));
        }
    }

    @GetMapping("/missing-values")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getMissingValues() {
        try {
            Map<String, Long> missingValues = sparkService.getMissingValues();
            return ResponseEntity.ok(ApiResponse.success(missingValues));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting missing values: " + e.getMessage()));
        }
    }

    @GetMapping("/value-frequency")
    public ResponseEntity<ApiResponse<Map<String, Map<Object, Long>>>> getValueFrequency() {
        try {
            Map<String, Map<Object, Long>> valueFrequency = sparkService.getValueFrequency();
            return ResponseEntity.ok(ApiResponse.success(valueFrequency));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting value frequency: " + e.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<Customer>>> filterData(@RequestParam Map<String, Object> params) {
        try {
            List<Customer> result = sparkService.filterData(params);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error filtering data: " + e.getMessage()));
        }
    }

    @GetMapping("/data-distribution")
    public ResponseEntity<ApiResponse< Map<Object, Object>>> getDataDistribution(@RequestParam String column) {
        try {
            Map<Object, Object> distribution = sparkService.getDataDistribution(column);
            return ResponseEntity.ok(ApiResponse.success(distribution));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting data distribution: " + e.getMessage()));
        }
    }

    @GetMapping("/summary-statistics")
    public ResponseEntity<ApiResponse<Map<String, Map<String, Object>>>> getSummaryStatistics(@RequestParam List<String> columns) {
        try {
            Map<String, Map<String, Object>> statistics = sparkService.getSummaryStatistics(columns);
            return ResponseEntity.ok(ApiResponse.success(statistics));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting summary statistics: " + e.getMessage()));
        }
    }

    @GetMapping("/correlation")
    public ResponseEntity<ApiResponse<Double>> getCorrelation(
            @RequestParam String column1,
            @RequestParam String column2) {
        try {
            double correlation = sparkService.getCorrelation(column1, column2);
            return ResponseEntity.ok(ApiResponse.success(correlation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting correlation: " + e.getMessage()));
        }
    }

    @GetMapping("/unique-values")
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> getUniqueValues(@RequestParam String column) {
        try {
            Map<String, List<String>> uniqueValues = sparkService.getUniqueValues(column);
            return ResponseEntity.ok(ApiResponse.success(uniqueValues));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting unique values: " + e.getMessage()));
        }
    }

    @GetMapping("/histogram")
    public ResponseEntity<ApiResponse<Map<Object, Long>>> getHistogram(@RequestParam String column) {
        try {
             Map<Object, Long> histogram = sparkService.getHistogram(column);
            return ResponseEntity.ok(ApiResponse.success(histogram));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting histogram: " + e.getMessage()));
        }
    }

    @GetMapping("/top-values")
    public ResponseEntity<ApiResponse<Map<Object, Long>>> getTopNValues(
            @RequestParam String column,
            @RequestParam int topN) {
        try {
             Map<Object, Long> topNData = sparkService.getTopNValues(column, topN);
            return ResponseEntity.ok(ApiResponse.success(topNData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting top N values: " + e.getMessage()));
        }
    }

    @GetMapping("/data-quality")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getDataQualityMetrics(@RequestParam List<String> columns) {
        try {
            Map<String, Double> qualityMetrics = sparkService.getDataQualityMetrics(columns);
            return ResponseEntity.ok(ApiResponse.success(qualityMetrics));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting data quality metrics: " + e.getMessage()));
        }
    }
}
