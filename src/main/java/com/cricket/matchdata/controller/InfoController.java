package com.cricket.matchdata.controller;

import com.cricket.matchdata.dto.DeliveryService;
import com.cricket.matchdata.dto.PlayerCareerStats;
import com.cricket.matchdata.entity.Match;
import com.cricket.matchdata.entity.inningInfo.*;
import com.cricket.matchdata.entity.matchinfo.Info;
import com.cricket.matchdata.entity.matchinfo.Officials;
import com.cricket.matchdata.entity.matchinfo.Player;
import com.cricket.matchdata.entity.matchinfo.Team;
import com.cricket.matchdata.entity.stats.MatchTeamStats;
import com.cricket.matchdata.entity.stats.PlayerMatchStats;
import com.cricket.matchdata.repository.DeliveryRepository;
import com.cricket.matchdata.repository.PlayerMatchStatsRepository;
import com.cricket.matchdata.service.InfoService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    private final InfoService infoService;
    private final PlayerMatchStatsRepository playerMatchStatsRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;

    @Autowired
    public InfoController(InfoService infoService, PlayerMatchStatsRepository playerMatchStatsRepository,
                          DeliveryService deliveryService, DeliveryRepository deliveryRepository) {
        this.infoService = infoService;
        this.playerMatchStatsRepository = playerMatchStatsRepository;
        this.deliveryService = deliveryService;
        this.deliveryRepository = deliveryRepository;
    }

    @PostMapping
    public Info saveInfo(@RequestBody Info info) {

        for (Team team : info.getTeams()) {
            team.setInfo(info);

            for (Player player : team.getPlayers()) {
                player.setTeam(team); // Set team in each player (redundant if already set in deserializer)
            }
        }

        return infoService.saveInfo(info);
    }

    @GetMapping("/officials/{matchId}")
    public Officials saveInfo(@PathVariable Long matchId) {
        return infoService.getOfficials(matchId);
    }

    @GetMapping("/teams/{matchId}")
    public List<Team> getTeam(@PathVariable Long matchId) {
        return infoService.getTeams(matchId);
    }

    @GetMapping("/info/{matchId}")
    public Info getInfo(@PathVariable Long matchId) {
        return infoService.getInfo(matchId);
    }

    @GetMapping("/match")
    public Match getMatch() throws IOException, URISyntaxException {

        File dataFolder = new File(JsonParser.class.getClassLoader().getResource("data").toURI());
        File[] files = dataFolder.listFiles();

        if (files != null) {




            // Iterate through all the files in the "data" folder
            for (File file : files) {
                if (file.length() == 0) {
                    System.out.println("Skipping empty file: " + file.getName());
                    continue;
                }
                if (file.isFile() && file.getName().endsWith(".json")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    // Parse the JSON file into your target Java object (e.g., Match)
                    Match match = objectMapper.readValue(file, Match.class);

                    if (match.getInfo() != null) {
                        match.getInfo().setMatch(match);
                    }



                    Map<String, PlayerMatchStats> playerStatsMap = new HashMap<>();
                    MatchTeamStats teamStats = new MatchTeamStats();
                    for (Inning inning : match.getInnings()) {

                        teamStats = new MatchTeamStats();
                        inning.setMatch(match);


                        // Link overs to inning
                        for (OverDetail over : inning.getOvers()) {
                            over.setInning(inning);

                            // Link deliveries to over
                            for (Delivery delivery : over.getDeliveries()) {

                                String striker = delivery.getBatter();
                                String nonStriker = delivery.getNonStriker();  // assuming your JSON has this

                                PlayerMatchStats strikerStats = playerStatsMap.computeIfAbsent(striker, name -> {
                                    PlayerMatchStats p = new PlayerMatchStats();
                                    p.setPlayerName(name);
                                    p.setMatch(match);
                                    return p;
                                });

                                if (!strikerStats.isHasBatted()) {
                                    strikerStats.setHasBatted(true);
                                }

                                PlayerMatchStats nonStrikerStats = playerStatsMap.computeIfAbsent(nonStriker, name -> {
                                    PlayerMatchStats p = new PlayerMatchStats();
                                    p.setPlayerName(name);
                                    p.setMatch(match);
                                    return p;
                                });

                                if (!nonStrikerStats.isHasBatted()) {
                                    nonStrikerStats.setHasBatted(true);
                                }

                                delivery.setOverDetail(over);
                                int total = delivery.getRuns().getTotal();
                                int batsmanRuns = delivery.getRuns().getBatter();
                                String batsman = delivery.getBatter();
                                String bowler = delivery.getBowler();
                                Extras extras = delivery.getExtras();
                                int wides = extras != null && extras.getWides() != null ? extras.getWides() : 0;
                                int noBalls = extras != null && extras.getNoballs() != null ? extras.getNoballs() : 0;
                                int byes = extras != null && extras.getByes() != null ? extras.getByes() : 0;
                                int legByes = extras != null && extras.getLegbyes() != null ? extras.getLegbyes() : 0;

                                int runsConcededByBowler = batsmanRuns + wides + noBalls;

                                PlayerMatchStats batsmanStats = playerStatsMap.computeIfAbsent(
                                        batsman, name -> {
                                            PlayerMatchStats p = new PlayerMatchStats();
                                            p.setPlayerName(name);
                                            p.setMatch(match);
                                            return p;
                                        }
                                );

//
//                    int wides = extras != null && extras.getWides() != null ? extras.getWides() : 0;
//                    int noBalls = extras != null && extras.getNoballs() != null ? extras.getNoballs() : 0;

                                boolean isLegalDelivery = (wides == 0 && noBalls == 0);
//                    if (isLegalDelivery) {
                                batsmanStats.addRuns(batsmanRuns,delivery.getRuns().isNonBoundary());
                                if (wides==0)
                                    batsmanStats.incrementBallsFaced();
//                    }

                                PlayerMatchStats bowlerStats = playerStatsMap.computeIfAbsent(
                                        bowler, name -> {
                                            PlayerMatchStats p = new PlayerMatchStats();
                                            p.setPlayerName(name);
                                            p.setMatch(match);
                                            return p;
                                        }
                                );
                                bowlerStats.addRunsConceded(runsConcededByBowler);
                                if (isLegalDelivery) {
                                    bowlerStats.incrementBallsBowled();
                                }


                                // Link wickets to delivery
                                for (Wicket wicket : delivery.getWickets()) {
                                    wicket.setDelivery(delivery);

                                    teamStats.incrementWickets();

                                    if (!"run out".equalsIgnoreCase(wicket.getKind()) &&
                                            !"retired hurt".equalsIgnoreCase(wicket.getKind())) {
                                        bowlerStats.incrementWickets();
                                    }

                                    // Link fielders to wicket (if applicable)
                                    for (Fielder fielder : wicket.getFielders()) {
                                        fielder.setWicket(wicket);

                                        PlayerMatchStats fielderStats = playerStatsMap.computeIfAbsent(
                                                fielder.getName(), name -> {
                                                    PlayerMatchStats p = new PlayerMatchStats();
                                                    p.setPlayerName(name);
                                                    p.setMatch(match);
                                                    return p;
                                                }
                                        );

                                        if ("caught".equalsIgnoreCase(wicket.getKind())) {
                                            fielderStats.incrementCatches();
                                        } else if ("stumped".equalsIgnoreCase(wicket.getKind())) {
                                            fielderStats.incrementStumpings();
                                        }
                                    }

                                    PlayerMatchStats dismissedBatsmanStats = playerStatsMap.computeIfAbsent(
                                            wicket.getPlayerOut(), name -> {
                                                PlayerMatchStats p = new PlayerMatchStats();
                                                p.setPlayerName(name);
                                                p.setMatch(match);
                                                return p;
                                            }
                                    );
                                    dismissedBatsmanStats.setDismissed(true);
                                    dismissedBatsmanStats.setDismissalType(wicket.getKind());
                                }
                                teamStats.addRuns(total);
                            }
                        }

//            // Link powerplays if needed
//            if (inning.getPowerplays() != null) {
//                for (Powerplay powerplay : inning.getPowerplays()) {
//                    powerplay.setInning(inning);
//                }
//            }

//            teamStats.setTotalWickets(0);
                        inning.setMatchTeamStats(teamStats);
//            System.out.println("Inning runs : "+teamStats.getTotalRuns());
//            System.out.println("Player wise  runs : ");
//
//            for (PlayerMatchStats playerMatchStats : teamStats.getPlayerStats())
//            {
//                System.out.println("Runs by : "+playerMatchStats.getPlayerName()+" : "+playerMatchStats.getRunsScored());
//            }
                        // Set target's parent if needed (Embedded usually doesn't need this)
                    }
                    teamStats.setPlayerStats(new ArrayList<>(playerStatsMap.values()));
                    teamStats.getPlayerStats().forEach(playerMatchStats -> {
                        if (playerMatchStats.isHasBatted() && !playerMatchStats.isDismissed()) {
                            playerMatchStats.setDismissalType("Not out");
                        }
                    });
                    infoService.saveMatch(match);

                    // Link registry to match (if not already)
//        if (match.getRegistry() != null) {
//            match.getRegistry().setMatch(match);
//        }







                    // Do whatever you need with the parsed match object
                    System.out.println("Parsed match from: " + file.getName());
                    // Save the match, process it, etc.
                }
            }
        }




        return null;
    }

    @GetMapping("/inning")
    public Inning getInnings() throws JsonProcessingException {
        String json = "{\n" +
                "  \"team\": \"Kolkata Knight Riders\",\n" +
                "  \"overs\": [\n" +
                "    {\n" +
                "      \"over\": 0,\n" +
                "      \"deliveries\": [\n" +
                "        {\n" +
                "          \"batter\": \"SC Ganguly\",\n" +
                "          \"bowler\": \"P Kumar\",\n" +
                "          \"extras\": {\n" +
                "            \"legbyes\": 1\n" +
                "          },\n" +
                "          \"non_striker\": \"BB McCullum\",\n" +
                "          \"runs\": {\n" +
                "            \"batter\": 0,\n" +
                "            \"extras\": 1,\n" +
                "            \"total\": 1\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"batter\": \"BB McCullum\",\n" +
                "          \"bowler\": \"P Kumar\",\n" +
                "          \"non_striker\": \"SC Ganguly\",\n" +
                "          \"runs\": {\n" +
                "            \"batter\": 0,\n" +
                "            \"extras\": 0,\n" +
                "            \"total\": 0\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"batter\": \"BB McCullum\",\n" +
                "          \"bowler\": \"P Kumar\",\n" +
                "          \"extras\": {\n" +
                "            \"wides\": 1\n" +
                "          },\n" +
                "          \"non_striker\": \"SC Ganguly\",\n" +
                "          \"runs\": {\n" +
                "            \"batter\": 0,\n" +
                "            \"extras\": 1,\n" +
                "            \"total\": 1\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"batter\": \"BB McCullum\",\n" +
                "          \"bowler\": \"P Kumar\",\n" +
                "          \"non_striker\": \"SC Ganguly\",\n" +
                "          \"runs\": {\n" +
                "            \"batter\": 0,\n" +
                "            \"extras\": 0,\n" +
                "            \"total\": 0\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"batter\": \"BB McCullum\",\n" +
                "          \"bowler\": \"P Kumar\",\n" +
                "          \"non_striker\": \"SC Ganguly\",\n" +
                "          \"runs\": {\n" +
                "            \"batter\": 0,\n" +
                "            \"extras\": 0,\n" +
                "            \"total\": 0\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"batter\": \"BB McCullum\",\n" +
                "          \"bowler\": \"P Kumar\",\n" +
                "          \"non_striker\": \"SC Ganguly\",\n" +
                "          \"runs\": {\n" +
                "            \"batter\": 0,\n" +
                "            \"extras\": 0,\n" +
                "            \"total\": 0\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"batter\": \"BB McCullum\",\n" +
                "          \"bowler\": \"P Kumar\",\n" +
                "          \"extras\": {\n" +
                "            \"legbyes\": 1\n" +
                "          },\n" +
                "          \"non_striker\": \"SC Ganguly\",\n" +
                "          \"runs\": {\n" +
                "            \"batter\": 0,\n" +
                "            \"extras\": 1,\n" +
                "            \"total\": 1\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }]}";
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Inning.class);
    }

    @GetMapping("/player/{name}/innings")
    public List<PlayerMatchStats> getStatsForPlayer(@PathVariable String name) {
        return playerMatchStatsRepository.findByPlayerName(name);
    }

//    @GetMapping("/batsman-vs-bowler")
//    public BatsmanVsBowlerStats getBatsmanVsBowlerStats(@RequestParam String batter, @RequestParam String bowler) {
//        return deliveryService.getBatsmanVsBowlerStats(batter, bowler);
//    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(
            @RequestParam String batter,
            @RequestParam(required = false) String bowler,
            @RequestParam(defaultValue = "false") boolean matchwise
    ) {
        if (bowler == null || bowler.isEmpty()) {
            if (matchwise) {
                return ResponseEntity.ok(playerMatchStatsRepository.findByPlayerName(batter));
            } else {
                List<PlayerMatchStats> all = playerMatchStatsRepository.findByPlayerName(batter);
                return ResponseEntity.ok(mergeStats(all));
            }
        } else {
            return ResponseEntity.ok(deliveryRepository.getHeadToHeadAggregate(batter, bowler));
        }
    }

    private PlayerMatchStats mergeStats(List<PlayerMatchStats> statsList) {
        if (statsList == null || statsList.isEmpty()) return null;

        PlayerMatchStats merged = new PlayerMatchStats();
        merged.setPlayerName(statsList.get(0).getPlayerName());

        for (PlayerMatchStats stat : statsList) {
            merged.setRunsScored(merged.getRunsScored() + stat.getRunsScored());
            merged.setBallsFaced(merged.getBallsFaced() + stat.getBallsFaced());
            merged.setFours(merged.getFours() + stat.getFours());
            merged.setSixes(merged.getSixes() + stat.getSixes());

            merged.setBallsBowled(merged.getBallsBowled() + stat.getBallsBowled());
            merged.setRunsConceded(merged.getRunsConceded() + stat.getRunsConceded());
            merged.setWickets(merged.getWickets() + stat.getWickets());
        }

        return merged;
    }

    public PlayerCareerStats getCareerSummary(String playerName) {
        List<PlayerMatchStats> matches = playerMatchStatsRepository.findByPlayerName(playerName);
        int runs = 0, balls = 0, fours = 0, sixes = 0;

        for (PlayerMatchStats m : matches) {
            runs += m.getRunsScored();
            balls += m.getBallsFaced();
            fours += m.getFours();
            sixes += m.getSixes();
        }

        return new PlayerCareerStats(runs, balls, fours, sixes);
    }
    @GetMapping("/player/{name}/career")
    public PlayerCareerStats getCareerStats(@PathVariable String name) { return getCareerSummary(name); }

//    @GetMapping("/player/{name}/year-wise")
//    public List<PlayerYearStats> getYearWiseStats(@PathVariable String name) { ... }
//
//    @GetMapping("/player/{name}/year-inning-wise")
//    public Map<Integer, List<InningsStats>> getYearInningStats(@PathVariable String name) { ... }
}