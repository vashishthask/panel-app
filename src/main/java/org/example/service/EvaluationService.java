package org.example.service;

import jakarta.persistence.EntityManager;
import org.example.dao.EvaluationDAO;
import org.example.model.Evaluation;
import org.example.model.PanelMember;
import org.example.model.PanelReviewSession;
import org.example.model.Team;
import org.example.util.JPAUtil;

import java.util.List;

public class EvaluationService {

    public Evaluation createEvaluation(Long panelReviewSessionId, Long panelMemberId, Long teamId) {
        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            EvaluationDAO evaluationDAO = new EvaluationDAO(entityManager);
            // Retrieve PanelReviewSession, PanelMember, and Team from the database
            PanelReviewSession panelReviewSession = evaluationDAO.findPanelReviewSessionById(panelReviewSessionId);
            PanelMember panelMember = evaluationDAO.findPanelMemberById(panelMemberId);
            Team team = evaluationDAO.findTeamById(teamId);

            if (panelReviewSession == null || panelMember == null || team == null) {
                throw new IllegalArgumentException("PanelReviewSession, PanelMember, or Team not found.");
            }

            // Create new Evaluation
            Evaluation evaluation = new Evaluation();
            evaluation.setPanelReviewSession(panelReviewSession);
            evaluation.setPanelMember(panelMember);
            evaluation.setTeam(team);

            // Save Evaluation to the database

            return evaluationDAO.saveEvaluation(evaluation);
        }
    }

    public List<PanelReviewSession> findRemainingPanelReviewSessionsByPanelMemberInReviewCycleAndContest(
            Long panelMemberId, Long contestId, Long reviewCycleId) {

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new EvaluationDAO(entityManager).findRemainingPanelReviewSessionsByPanelMemberInReviewCycleAndContest(panelMemberId, contestId, reviewCycleId);
        }
    }
}
