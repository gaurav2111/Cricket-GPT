package com.cricket.matchdata.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeminiService {

    private String apiKey = "AIzaSyARNe12zvxXYTB0bcKz9AOV67kyRmwpdqE";

    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

    public String getGeminiResponse(String userInput) throws IOException {
        String url = GEMINI_URL + apiKey;
        String query = "You are an expert in converting English questions to SQL query.\n" +
                "the SQL database has the name defaultdb and table details :\n" +
                "table : player_match_stats , columns -\n" +
                "balls_bowled,balls_faced,catches,dismissal_type,dismissed,fours,has_batted,player_name,runs_conceded,runs_scored,,sixes,,stumpings,wickets,year,match_id\n" +
                "each row represents these stats of a player of a particular match\n" +
                "\n" +
                "table : delivery , columns -\n" +
                "batter,bowler,byes,legbyes,noballs,penalty,wides,non_striker,runs_batter,runs_extras,non_boundary,runs_total,over_id\n" +
                "each row represents a delivery in a match (represented by match_id)\n" +
                "for each delivery there is a batter, bowler,non_striker,\n" +
                "runs_batter are runs scored by batter, runs_extras are extra runs,non_boundary flag if 4/6 runs are scored of non boundary,\n" +
                "over_id is over number.\n" +
                "\n" +
                "Your output must: (compulsory)\n" +
                "- Return only a clean SQL query (no explanation, no markdown, no symbols like ``` or \"sql\").\n" +
                "- Avoid invalid SQL or incorrect column names.\n" +
                "- Keep it short and directly usable.\n" +
                "\n" +
                "Terms to remember :\n" +
                "- Orange cap is awarded to most runs in a season (by most i mean total of runs across all matches in a year ans not maximum of score)\n" +
                "- Purple cap is awarded to most wickets in a season(year) and it doesn't mean maximum wickets in a match or year, it is max(sum(wickets)).\n" +
                "- Most mean sum\n" +
                "- highest mean max\n" +
                "-if any year by year stat then mention in order."+
                "\n" +
                "examples :\n" +
                "\n" +
                "1. Q: What are the total runs scored by SC Ganguly?\n" +
                "   A: SELECT SUM(runs_scored) FROM defaultdb.player_match_stats WHERE player_name = 'SC Ganguly';\n" +
                "\n" +
                "2. Q: Which players have played in all IPL seasons from 2008 to 2025?\n" +
                "   A: SELECT player_name FROM defaultdb.player_match_stats WHERE year BETWEEN 2008 AND 2025 GROUP BY player_name HAVING COUNT(DISTINCT year) = 18;\n" +
                "\n" +
                "3. Q: V Kohli in Ipl 2025?\n" +
                "   A: Select   sum of these all :balls_bowled,balls_faced,catches,dismissal_type,dismissed,fours,has_batted,player_name,runs_conceded,runs_scored,,sixes,stumpings,wickets\n" +
                "\n" +
                "4. Q: Highest score in ipl?\n" +
                "   A: Select max(runs_scored) from defaultdb.player_match_stats;\n" +
                "\n" +
                "5. Q: V Kohli vs J Bumrah stat?\n" +
                "   A: Select sum(runs_batter) as runs, count(*) as balls where batter = 'V Kohli' and bowler = 'J Bumrah'.\n" +
                "   or if they specifically mention who is batter and who is bowler.\n" +
                "\n"+
                "6. C Gayle\n"+
                "SELECT player_name,sum(runs_scored) total_runs,sum(balls_faced ) as balls_faced,count(*) as innings from defaultdb.player_match_stats pms where player_name = 'C Gayle';"
                +"C gayle in 2024"+
                "A: SELECT player_name,sum(runs_scored) total_runs,sum(balls_faced ) as balls_faced,count(*) as innings from defaultdb.player_match_stats pms where player_name = 'C Gayle' and year=2024;"
                +userInput;
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> part = new HashMap<>();
        part.put("text", query);
        Map<String, Object> contents = new HashMap<>();
        contents.put("parts", new Object[]{part});
        requestBody.put("contents", new Object[]{contents});

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestBody);


        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json));

            return client.execute(post, response ->
                    new String(response.getEntity().getContent().readAllBytes())
            );
        }
    }
}