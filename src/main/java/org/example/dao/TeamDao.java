package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.model.Contest;
import org.example.model.Team;

public class TeamDao {
    private final EntityManager entityManager;

    public TeamDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Team getTeamByTeamNameAndContest(String teamName, Contest contest) {
        String jpql = "SELECT t FROM Team t WHERE t.name = :teamName AND t.contest = :contest";
        TypedQuery<Team> query = entityManager.createQuery(jpql, Team.class);
        query.setParameter("teamName", teamName);
        query.setParameter("contest", contest);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            // Handle the case when no Team is found for the given teamName and Contest
            return null;
        }
    }

    public Team save(Team team){
        entityManager.persist(team);
        return team;
    }
}
