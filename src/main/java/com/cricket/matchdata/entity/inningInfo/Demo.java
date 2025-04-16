package com.cricket.matchdata.entity.inningInfo;

import com.cricket.matchdata.entity.Match;
import com.cricket.matchdata.entity.stats.MatchTeamStats;
import com.cricket.matchdata.entity.stats.PlayerMatchStats;
import com.cricket.matchdata.repository.DeliveryRepository;
import com.cricket.matchdata.repository.PlayerMatchStatsRepository;
import com.cricket.matchdata.service.DeliveryService;
import com.cricket.matchdata.service.InfoService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//@Component
public class Demo {

    ObjectMapper mapper = new ObjectMapper();
    private final InfoService infoService;
    private final PlayerMatchStatsRepository playerMatchStatsRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;

    @Autowired
    public Demo(InfoService infoService, PlayerMatchStatsRepository playerMatchStatsRepository,
                DeliveryService deliveryService, DeliveryRepository deliveryRepository) throws IOException, URISyntaxException, InterruptedException {
        this.infoService = infoService;
        this.playerMatchStatsRepository = playerMatchStatsRepository;
        this.deliveryService = deliveryService;
        this.deliveryRepository = deliveryRepository;
//        getMatch();
        loadAllMatches();
    }



    public Match getMatch() throws IOException, URISyntaxException {
//to do non_boundaru reduce those 4 or sixes.
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

                    // Parse the JSON file into your target Java object (e.g., Match)
                    Match match =
                    extracted(file);
                    infoService.saveMatch(match);
                }
            }
        }




        return null;
    }

    private Match extracted(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Match match =objectMapper.readValue(file, Match.class);

        if (match.getInfo() != null) {
            match.getInfo().setMatch(match);
        }

        int matchYear = 9999;
        try {
//                        assert match.getInfo() != null;
            assert match.getInfo() != null;
            String dateStr = match.getInfo().getDates().get(0); // e.g., "2016-05-14"
            matchYear = Integer.parseInt(dateStr.substring(0, 4));
        } catch (Exception e) {
            System.out.println("⚠️ Error parsing year from date in file: " + file.getName());
//                        matchYear = 0;
        }

        Map<String, PlayerMatchStats> playerStatsMap = new HashMap<>();
        MatchTeamStats teamStats = new MatchTeamStats();
        int count=0;

        for (Inning inning : match.getInnings()) {
            if (count>1) break;
            count++;
            teamStats = new MatchTeamStats();
            inning.setMatch(match);


            // Link overs to inning
            for (OverDetail over : inning.getOvers()) {
                over.setInning(inning);

                // Link deliveries to over
                for (Delivery delivery : over.getDeliveries()) {

                    String striker = delivery.getBatter();
                    String nonStriker = delivery.getNonStriker();  // assuming your JSON has this

                    int finalMatchYear = matchYear;
                    PlayerMatchStats strikerStats = playerStatsMap.computeIfAbsent(striker, name -> {
                        PlayerMatchStats p = new PlayerMatchStats();
                        p.setPlayerName(name);
                        p.setMatch(match);
                        p.setYear(finalMatchYear);
                        return p;
                    });

                    if (!strikerStats.isHasBatted()) {
                        strikerStats.setHasBatted(true);
                    }

                    int finalMatchYear1 = matchYear;
                    PlayerMatchStats nonStrikerStats = playerStatsMap.computeIfAbsent(nonStriker, name -> {
                        PlayerMatchStats p = new PlayerMatchStats();
                        p.setPlayerName(name);
                        p.setMatch(match);
                        p.setYear(finalMatchYear1);
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

                    int finalMatchYear2 = matchYear;
                    PlayerMatchStats batsmanStats = playerStatsMap.computeIfAbsent(
                            batsman, name -> {
                                PlayerMatchStats p = new PlayerMatchStats();
                                p.setPlayerName(name);
                                p.setMatch(match);
                                p.setYear(finalMatchYear2);
                                return p;
                            }
                    );

//
//                    int wides = extras != null && extras.getWides() != null ? extras.getWides() : 0;
//                    int noBalls = extras != null && extras.getNoballs() != null ? extras.getNoballs() : 0;

                    boolean isLegalDelivery = (wides == 0 && noBalls == 0);

                    batsmanStats.addRuns(batsmanRuns,delivery.getRuns().isNonBoundary());
                    if (wides==0)
                    {   batsmanStats.incrementBallsFaced();}

                    int finalMatchYear3 = matchYear;
                    PlayerMatchStats bowlerStats = playerStatsMap.computeIfAbsent(
                            bowler, name -> {
                                PlayerMatchStats p = new PlayerMatchStats();
                                p.setPlayerName(name);
                                p.setMatch(match);
                                p.setYear(finalMatchYear3);
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

                            int finalMatchYear4 = matchYear;
                            PlayerMatchStats fielderStats = playerStatsMap.computeIfAbsent(
                                    fielder.getName(), name -> {
                                        PlayerMatchStats p = new PlayerMatchStats();
                                        p.setPlayerName(name);
                                        p.setMatch(match);
                                        p.setYear(finalMatchYear4);
                                        return p;
                                    }
                            );

                            if ("caught".equalsIgnoreCase(wicket.getKind())) {
                                fielderStats.incrementCatches();
                            } else if ("stumped".equalsIgnoreCase(wicket.getKind())) {
                                fielderStats.incrementStumpings();
                            }
                        }

                        int finalMatchYear5 = matchYear;
                        PlayerMatchStats dismissedBatsmanStats = playerStatsMap.computeIfAbsent(
                                wicket.getPlayerOut(), name -> {
                                    PlayerMatchStats p = new PlayerMatchStats();
                                    p.setPlayerName(name);
                                    p.setMatch(match);
                                    p.setYear(finalMatchYear5);
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


        // Link registry to match (if not already)
//        if (match.getRegistry() != null) {
//            match.getRegistry().setMatch(match);
//        }


        // Do whatever you need with the parsed match object
        System.out.println("Parsed match from: " + file.getName());
        // Save the match, process it, etc.
        return match;
    }

    public void loadAllMatches() throws IOException, URISyntaxException, InterruptedException {
        File dataFolder = new File(JsonParser.class.getClassLoader().getResource("data").toURI());
        File[] files = dataFolder.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("No files to process.");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(16); // Tune this based on your CPU

        for (File file : files) {
            if (file.length() == 0 || !file.getName().endsWith(".json")) {
                System.out.println("Skipping invalid file: " + file.getName());
                continue;
            }

            executor.submit(() -> {
                try {
                    Match match = extracted(file);
                    infoService.saveMatch(match);
                    System.out.println("✅ Saved: " + file.getName());
                } catch (Exception e) {
                    System.err.println("❌ Failed: " + file.getName() + " → " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }

}
