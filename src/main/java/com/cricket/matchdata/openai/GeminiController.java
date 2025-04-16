package com.cricket.matchdata.openai;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/gemini")
public class GeminiController {

    @Autowired
    private GeminiService geminiService;

//    @GetMapping("/ask")
//    public String askGemini(@RequestParam String prompt) {
//        try {
//            String query = "You are an expert in converting English questions to SQL query!\n\nThe SQL database has the name matchdata and table is player_match_stats and has the following columns - balls_bowled, balls_faced, runs_scored\n\nFor example,\nExample 1 - How many entries of records are present?,\nSQL command: SELECT COUNT(*) FROM matchdata.player_match_stats;\n\nExample 2 - Tell me total runs of SC Ganguly?,\nSQL command: SELECT sum(runs_scored) total_runs from matchdata.player_match_stats pms where player_name = 'SC Ganguly';\n\nAlso, the SQL output should not include the word 'sql' or use triple backticks.\n\nNow, convert this English question to SQL query:\n\total runs of V Kohli";
//            return geminiService.getGeminiResponse(prompt);
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }


    @Autowired
    private DynamicQueryService dynamicQueryService;

    @GetMapping("/ask")
    public ResponseEntity<?> askGemini(@RequestParam String prompt) {
        try {
            // Prepare the query for the prompt (including instructions for Gemini)
            String query = "You are an expert in converting English questions to SQL query!\n\n"
                    + "The SQL database has the name defaultdb and table is player_match_stats and has the following columns - balls_bowled, balls_faced, runs_scored\n\n"
                    + "For example,\nExample 1 - How many entries of records are present?,\nSQL command: SELECT COUNT(*) FROM defaultdb.player_match_stats;\n\n"
                    + "Example 2 - Tell me total runs of SC Ganguly?,\nSQL command: SELECT sum(runs_scored) total_runs from defaultdb.player_match_stats pms where player_name = 'SC Ganguly';\n\n"+

                    "now there are other tables in database too like table :delivery which has columns : batter\n" +
                    "bowler\n" +
                    "byes\n" +
                    "legbyes\n" +
                    "noballs\n" +
                    "penalty\n" +
                    "wides\n" +
                    "non_striker\n" +
                    "runs_batter\n" +
                    "runs_extras\n" +
                    "non_boundary\n" +
                    "runs_total\n" +
                    "over_id"+" here data is of each ball bowled where name describes what it means"+
                    "a season is nothing but a year"+
                    "orange cap is given to one player with max sum of runs in a year and purple cap is given to one player with most sum of wickets in all matches in year"
          +  "also remember : most means sum ans highest means max"+
                     "Do not include the word 'sql', any markdown formatting, or triple backticks (```).\n" +
                    "Only return raw SQL query as plain text with no explanations..\n\n"
                    + "Now, convert this English question to SQL query:\n" + prompt;

            // Get the SQL query from Gemini's response
            String jsonResponse = geminiService.getGeminiResponse(query);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            String sqlQuery = rootNode.path("candidates")
                    .get(0) // Get the first candidate
                    .path("content")
                    .path("parts")
                    .get(0) // Get the first part
                    .path("text")
                    .asText();

            sqlQuery.replace("matchdata","defaultdb");
            System.out.println("query :"+sqlQuery);
            if ( sqlQuery.toLowerCase().contains("show tables") )
            {
                List<Map<String, Object>> result = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();
                map.put("error","Not allowed");
                result.add(map);
                Map<String, Object> response = Map.of(
                        "sqlQuery", sqlQuery,
                        "result", result
                );
                return ResponseEntity.ok(response);
            }

            // Execute the SQL query dynamically
            List<Map<String, Object>> result = dynamicQueryService.executeQuery(sqlQuery);

            Map<String, Object> response = Map.of(
                    "sqlQuery", sqlQuery,
                    "result", result
            );



            return ResponseEntity.ok(response);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



}
