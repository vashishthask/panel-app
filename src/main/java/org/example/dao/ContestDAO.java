package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Contest;
import org.example.model.PanelMember;

import java.util.List;

public class ContestDAO {

    private final EntityManager entityManager;

    public ContestDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Contest createContest(Contest contest) {
        entityManager.persist(contest);
        return contest;
    }

    public Contest getContest(Long id) {
        return entityManager.find(Contest.class, id);
    }

    public Contest getContestWithUser(Long id) {
        TypedQuery<Contest> query = entityManager.createQuery(
                "SELECT c FROM Contest c LEFT JOIN FETCH c.reviewCycles WHERE c.id = :id" , Contest.class);
        query.setParameter("id", id);
        List<Contest> results = query.getResultList();
        System.err.println("The results are:"+ results.get(0));
        return results == null ? null : results.get(0);
    }


    public Contest getContestWithTeamsAndPanelMembers(Long id) {
        TypedQuery<Contest> query = entityManager.createQuery(
                "SELECT c FROM Contest c LEFT JOIN FETCH c.reviewCycles " +
                        "LEFT JOIN FETCH c.teams " +
                        "LEFT JOIN FETCH c.panelMembers " +
                        "WHERE c.id = :id" , Contest.class);
        query.setParameter("id", id);
        List<Contest> results = query.getResultList();
        System.err.println("The results are:"+ results.get(0));
        return results == null ? null : results.get(0);
    }

    public List<PanelMember> getPanelMembersByContestTitleName(String titleName) {
        String jpql = "SELECT p FROM PanelMember p " +
                "JOIN p.contest c " +
                "WHERE c.titleName = :titleName";

        TypedQuery<PanelMember> query = entityManager.createQuery(jpql, PanelMember.class);
        query.setParameter("titleName", titleName);

        return query.getResultList();
    }

    public Contest merge(Contest contest) {
        entityManager.merge(contest);
        return contest;
    }

    // other CRUD operations
}