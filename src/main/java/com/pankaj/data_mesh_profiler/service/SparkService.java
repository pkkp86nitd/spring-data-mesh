package com.pankaj.data_mesh_profiler.service;

import com.pankaj.data_mesh_profiler.Mapper.CustomerMapper;
import com.pankaj.data_mesh_profiler.dto.Customer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SparkService {

    @Autowired
    private SparkSession sparkSession;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    public Dataset<Row> loadCustomerDataFrame() {
        try {
            return sparkSession.read()
                    .format("jdbc")
                    .option("url", dbUrl)
                    .option("dbtable", "customer")
                    .option("user", dbUserName)
                    .option("password", dbPassword)
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public void performDataProfiling() {
        try {
            // Load customer data from PostgreSQL into Spark DataFrame
            Dataset<Row> customerDF = loadCustomerDataFrame();

            // Display the DataFrame
            System.out.println("****--------ALL CUSTOMER VIEW-------****");
            assert customerDF != null;
            customerDF.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Long getCount() {
        try {
            Dataset<Row> df = loadCustomerDataFrame();
            assert df != null;
            return df.count();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Long> getMissingValues() {
        try {
            Map<String, Long> missingValues = new HashMap<>();
            Dataset<Row> customerData = loadCustomerDataFrame();
            assert customerData != null;
            for (String column : customerData.columns()) {
                long count = customerData.filter(customerData.col(column).isNull()).count();
                missingValues.put(column, count);
            }
            return missingValues;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public Map<String, Map<Object, Long>> getValueFrequency() {
        try {
            Map<String, Map<Object, Long>> valueFrequency = new HashMap<>();
            Dataset<Row> customerData = loadCustomerDataFrame();

            assert customerData != null;
            for (String column : customerData.columns()) {
                Dataset<Row> nonNullData = customerData.filter(customerData.col(column).isNotNull());
                Dataset<Row> frequencyData = nonNullData.groupBy(column).count();
                Map<Object, Long> columnFrequency = frequencyData.collectAsList().stream()
                        .collect(Collectors.toMap(row -> row.get(0), row -> row.getLong(1)));
                valueFrequency.put(column, columnFrequency);
            }
            return valueFrequency;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public List<Customer> filterData(Map<String, Object> filters) {
        try {
            Dataset<Row> customerDF = loadCustomerDataFrame();
            assert customerDF != null;

            if (filters != null && !filters.isEmpty()) {
                for (Map.Entry<String, Object> entry : filters.entrySet()) {
                    String columnName = entry.getKey();
                    Object value = entry.getValue();

                    customerDF = customerDF.filter(customerDF.col(columnName).equalTo(value));
                }
            }

            return customerDF.collectAsList().stream()
                    .map(CustomerMapper::mapRowToCustomer).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public  Map<Object,Object> getDataDistribution(String column) {
        try {
            Dataset<Row> customerDF = loadCustomerDataFrame();


            assert customerDF != null;
            String[] columns = customerDF.columns();
            if (columns.length == 0 || !Arrays.asList(columns).contains(column)) {
                throw new IllegalArgumentException("Column does not exist in the DataFrame.");
            }
            if (customerDF.schema().apply(column).dataType() instanceof StringType)  {
                throw new IllegalArgumentException("Column Data Type should not be string");
            }

            Dataset<Row> distributionData = customerDF.describe(column);


            return distributionData.collectAsList().stream()
                    .collect(Collectors.toMap(row -> row.get(0), row -> row.get(1)));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public Map<String, Map<String, Object>> getSummaryStatistics(List<String> columns) {
        try {
            Dataset<Row> customerDF = loadCustomerDataFrame();
            Map<String, Map<String, Object>> statisticsMap = new HashMap<>();

            for (String column : columns) {
                Map<String, Object> columnStatistics = new HashMap<>();
                assert customerDF != null;
                columnStatistics.put("mean", customerDF.agg(functions.mean(column)).first().get(0));
                columnStatistics.put("min", customerDF.agg(functions.min(column)).first().get(0));
                columnStatistics.put("max", customerDF.agg(functions.max(column)).first().get(0));
                columnStatistics.put("stdDev", customerDF.agg(functions.stddev(column)).first().get(0));

                statisticsMap.put(column, columnStatistics);
            }

            return statisticsMap;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public double getCorrelation(String column1, String column2) {
        try {
            Dataset<Row> customerDF = loadCustomerDataFrame();
            assert customerDF != null;
            return customerDF.stat().corr(column1, column2);
        } catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }

    public Map<String, List<String>> getUniqueValues(String column) {
        try {
            Dataset<Row> customerDF = loadCustomerDataFrame();
            assert customerDF != null;
            List<String> uniqueValues = customerDF.select(column).distinct().as(Encoders.STRING()).collectAsList();
            Map<String, List<String>> uniqueValuesMap = new HashMap<>();
            uniqueValuesMap.put("values", uniqueValues);
            return uniqueValuesMap;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public  Map<Object,Long> getHistogram(String column) {
        try {
            Dataset<Row> customerDF = loadCustomerDataFrame();
            assert customerDF != null;
            Map<Object,Long> histogramData = customerDF.select(column).groupBy(column).count().collectAsList()
                    .stream()
                    .collect(Collectors.toMap(row -> row.get(0), row -> row.getLong(1)));

            return histogramData;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public   Map<Object,Long> getTopNValues(String column, int topN) {
        try {
            Dataset<Row> customerDF = loadCustomerDataFrame();
            assert customerDF != null;
            Map<Object,Long> topNData = customerDF.groupBy(column).count()
                    .orderBy(functions.desc("count"))
                    .limit(topN)
                    .collectAsList().stream()
                    .collect(Collectors.toMap(row -> row.get(0), row -> row.getLong(1), (a, b) -> a, LinkedHashMap::new));

            return topNData;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public Map<String, Double> getDataQualityMetrics(List<String> columns) {
        try {
            Dataset<Row> customerDF = loadCustomerDataFrame();
            assert customerDF != null;
            Map<String, Double> qualityMetricsMap = new HashMap<>();

            for (String column : columns) {
                double completeness = customerDF.filter(customerDF.col(column).isNotNull()).count() * 1.0 / customerDF.count();
                qualityMetricsMap.put(column, completeness);
            }

            return qualityMetricsMap;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }



}
