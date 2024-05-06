package org.example.service;

import jakarta.persistence.EntityManager;
import org.example.dao.TeamDao;
import org.example.model.Contest;
import org.example.model.Team;
import org.example.util.JPAUtil;

public class TeamService {
    public Team getTeamByTeamNameAndContest(String teamName, Contest contest) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new TeamDao(em).getTeamByTeamNameAndContest(teamName, contest);
        }
    }
}
