package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Evaluation;
import org.example.model.PanelMember;
import org.example.model.PanelReviewSession;
import org.example.model.Team;

import java.util.List;

public class EvaluationDAO {
    private final EntityManager entityManager;

    public EvaluationDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public PanelReviewSession findPanelReviewSessionById(Long panelReviewSessionId) {
        return entityManager.find(PanelReviewSession.class, panelReviewSessionId);
    }

    public PanelMember findPanelMemberById(Long panelMemberId) {
        return entityManager.find(PanelMember.class, panelMemberId);
    }

    public Team findTeamById(Long teamId) {
        return entityManager.find(Team.class, teamId);
    }

    public Evaluation saveEvaluation(Evaluation evaluation) {
        entityManager.getTransaction().begin();
        entityManager.persist(evaluation);
        entityManager.getTransaction().commit();
        return evaluation;
    }

    public List<PanelReviewSession> findRemainingPanelReviewSessionsByPanelMemberInReviewCycleAndContest(Long panelMemberId, Long reviewCycleId, Long contestId) {
        String jpql = "SELECT prs FROM PanelReviewSession prs " +
                "JOIN prs.panelMembers pm " +
                "JOIN prs.reviewCycle prc " +
                "JOIN prc.contest c " +
                "LEFT JOIN prs.evaluations e " +
                "WHERE pm.id = :panelMemberId " +
                "AND prc.id = :reviewCycleId " +
                "AND c.id = :contestId " +
                "AND e IS NULL"; // Check that there is no associated Evaluation
        TypedQuery<PanelReviewSession> query = entityManager.createQuery(jpql, PanelReviewSession.class);
        query.setParameter("panelMemberId", panelMemberId);
        query.setParameter("reviewCycleId", reviewCycleId);
        query.setParameter("contestId", contestId);
        return query.getResultList();
    }
}
