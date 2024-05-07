package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Contest;
import org.example.model.ContestWorkingMember;
import org.example.model.PanelMember;

import java.util.ArrayList;
import java.util.List;

public class ContestDAO {

    private final EntityManager entityManager;

    public ContestDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Contest saveContest(Contest contest) {
        entityManager.persist(contest);
        return contest;
    }

    public Contest getContest(Long id) {
        return entityManager.find(Contest.class, id);
    }

    public Contest getContestWithReviewCycles(Long id) {
        TypedQuery<Contest> query = entityManager.createQuery(
                "SELECT c FROM Contest c LEFT JOIN FETCH c.reviewCycles " +
                        "LEFT JOIN FETCH c.collaborators " +
                        "WHERE c.id = :id" , Contest.class);
        query.setParameter("id", id);
        List<Contest> results = query.getResultList();
        System.err.println("The results are:"+ results.get(0));
        return results == null ? null : results.get(0);
    }



    public Contest getContestWithReviewCyclesTeamsAndPanelMembers(Long id) {
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

    public Contest getContestByNameByReviewCycleNameWithTeamsAndPanelMembers(String titleName, String reviewCycleName) {
        TypedQuery<Contest> query = entityManager.createQuery(
                "SELECT c FROM Contest c JOIN FETCH c.reviewCycles rc " +
                        "LEFT JOIN FETCH c.teams " +
                        "LEFT JOIN FETCH c.panelMembers " +
                        "WHERE c.titleName = :titleName and rc.name = :reviewCycleName" , Contest.class);
        query.setParameter("titleName", titleName);
        query.setParameter("reviewCycleName", reviewCycleName);
        return query.getSingleResult();
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

    public List<Contest> getCreatedAndCollaboratedContestsByCreatorIdWithReviewCycles(String workingMemberId) {
        String jpql = "SELECT c FROM Contest c " +
                "LEFT JOIN FETCH c.reviewCycles " +
                "WHERE c.creator.id = :workingMemberId OR " +
                ":workingMemberId IN (SELECT cm.id FROM c.collaborators cm)";
        TypedQuery<Contest> query = entityManager.createQuery(jpql, Contest.class);
        query.setParameter("workingMemberId", workingMemberId);
        return query.getResultList();
    }

    public List<Contest> findContestsByContestWorkingMemberId(String userId) {
        String jpql = "SELECT u FROM ContestWorkingMember u JOIN FETCH u.createdContests WHERE u.id = :userId";
        TypedQuery<ContestWorkingMember> query = entityManager.createQuery(jpql, ContestWorkingMember.class);
        query.setParameter("userId", userId);
        List<ContestWorkingMember> users = query.getResultList();
        if(users != null && !users.isEmpty()){
            return users.get(0).getCreatedContests();
        }
        return new ArrayList<>();
    }
    // other CRUD operations
}