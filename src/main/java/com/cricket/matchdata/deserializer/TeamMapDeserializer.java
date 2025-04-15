package com.cricket.matchdata.deserializer;

import com.cricket.matchdata.entity.matchinfo.Player;
import com.cricket.matchdata.entity.matchinfo.Team;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.util.*;

public class TeamMapDeserializer extends JsonDeserializer<List<Team>> {

    @Override
    public List<Team> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);

        List<Team> teams = new ArrayList<>();

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();

            String teamName = entry.getKey();
            JsonNode playerArray = entry.getValue();

            Team team = new Team();
            team.setName(teamName);

            List<Player> players = new ArrayList<>();
            for (JsonNode playerNode : playerArray) {
                Player player = new Player();
                player.setName(playerNode.asText());
                player.setTeam(team); // Set player->team
                players.add(player);
            }

            team.setPlayers(players);
            teams.add(team);
        }

        return teams;
    }
}