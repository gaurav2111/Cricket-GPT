package com.cricket.matchdata.openai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DynamicQueryService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    public Object executeQuery(String sql) {
//        sql = sql.trim().toLowerCase();
//        System.out.println("Dynamic query :"+sql);
//
//        if (sql.startsWith("select count") || sql.contains("count(")) {
//            return jdbcTemplate.queryForObject(sql, Integer.class); // Return count as Integer
//
//        } else if (sql.contains("sum(") || sql.contains("max(") || sql.contains("min(")) {
//            return jdbcTemplate.queryForObject(sql, Object.class); // Could be Double or Integer for aggregates
//
//        } else if (sql.contains("limit 1") || sql.contains("where") && !sql.contains("group by")) {
//            // Likely a query to fetch a single row
//            try {
//                return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PlayerMatchStats.class));
//            } catch (Exception e) {
//                return "Could not fetch single row: " + e.getMessage();
//            }
//
//        } else {
//            // Assuming multiple rows for other queries
//            try {
//                return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PlayerMatchStats.class));
//            } catch (Exception e) {
//                return "Could not fetch multiple rows: " + e.getMessage();
//            }
//        }
//    }

    public List<Map<String, Object>> executeQuery(String sqlQuery) {
        return jdbcTemplate.queryForList(sqlQuery);
    }
}