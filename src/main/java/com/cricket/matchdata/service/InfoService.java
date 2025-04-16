package com.cricket.matchdata.service;

import com.cricket.matchdata.entity.Match;
import com.cricket.matchdata.entity.matchinfo.Info;
import com.cricket.matchdata.entity.matchinfo.Officials;
import com.cricket.matchdata.entity.matchinfo.Team;
import com.cricket.matchdata.model.exceptions.MatchNotFoundException;
import com.cricket.matchdata.repository.InfoRepository;
import com.cricket.matchdata.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoService {

    private final InfoRepository infoRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public InfoService(InfoRepository infoRepository,MatchRepository matchRepository ) {
        this.infoRepository = infoRepository;
        this.matchRepository = matchRepository;
    }

    public Info saveInfo(Info info) {
        return infoRepository.save(info);
    }
    public Match saveMatch(Match match)
    {
        return matchRepository.save(match);
    }
    public Info getInfo(Long matchId)
    {
        return infoRepository.findById(matchId)
                .orElseThrow(()->new MatchNotFoundException("Match not found"));
    }

    public Officials getOfficials(Long matchId) {
        return infoRepository.findById(matchId)
                .map(Info::getOfficials)
                .orElseThrow(()->new MatchNotFoundException("Match not found"));
    }

    public List<Team> getTeams(Long matchId) {
        return infoRepository.findById(matchId)
                .map(Info::getTeams)
                .orElseThrow(()->new MatchNotFoundException("Match not found"));
    }
}