package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.model.*;
import org.example.util.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class EvaluationServiceTest {

    private EvaluationService evaluationService;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @BeforeAll
    public static void setup(){
        Map<String, String> props = new HashMap<>();
        JPAUtil.init("panel-app", props);
    }

    @AfterEach
    public void cleanup(){
        TestCleanup testCleanup = new TestCleanup(JPAUtil.getEntityManager());

        testCleanup.cleanUp();
        entityManager.close();
    }

    @BeforeEach
    public void setUp() {
        entityManager = JPAUtil.getEntityManager();
        evaluationService = new EvaluationService();
        transaction = entityManager.getTransaction();
    }

    @Test
    public void testCreateEvaluation() {
        // Start transaction
        transaction.begin();

        // Create and persist entities
        Contest contest = createContest();
        ReviewCycle reviewCycle = createReviewCycle(contest);
        Team team = createTeam(contest);
        PanelMember panelMember = createPanelMember(contest);


        PanelReviewSession panelReviewSession = createPanelReviewSession(reviewCycle, team, panelMember);

        // Commit transaction
        transaction.commit();

        // Create Evaluation
        transaction.begin();
        List<PanelReviewSession> sessionsBefore = evaluationService.findRemainingPanelReviewSessionsByPanelMemberInReviewCycleAndContest(panelMember.getId(), contest.getId(), reviewCycle.getId());
        assertEquals(1, sessionsBefore.size());

        Evaluation evaluation = evaluationService.createEvaluation(panelReviewSession.getId(), panelMember.getId(), team.getId());
        transaction.commit();

        // Verify that Evaluation has been created
        assertNotNull(evaluation.getId());

        List<PanelReviewSession> remainingEvaluations = evaluationService.findRemainingPanelReviewSessionsByPanelMemberInReviewCycleAndContest(panelMember.getId(), contest.getId(), reviewCycle.getId());

        assertEquals(0, remainingEvaluations.size());

        // Add further assertions if needed
    }

    // Helper methods to create and persist entities

    private Contest createContest() {
        ContestWorkingMember creator = new ContestWorkingMember("4529929");
        entityManager.persist(creator);
        Contest contest = new Contest();
        contest.setTitleName("Contest 1");

        creator.getCreatedContests().add(contest);
        contest.setCreator(creator);
        // Set properties
        entityManager.persist(contest);
        return contest;
    }

    private ReviewCycle createReviewCycle(Contest contest) {
        ReviewCycle reviewCycle = new ReviewCycle();
        reviewCycle.setName("Review Cycle 1");
        reviewCycle.setContest(contest);
        contest.getReviewCycles().add(reviewCycle);
        // Set properties
        entityManager.persist(reviewCycle);
        return reviewCycle;
    }

    private Team createTeam(Contest contest) {
        Team team = new Team();
        team.setContest(contest);
        // Set properties
        entityManager.persist(team);
        return team;
    }

    private PanelMember createPanelMember(Contest contest) {
        PanelMember panelMember = new PanelMember();
        panelMember.setContest(contest);
        // Set properties
        entityManager.persist(panelMember);
        return panelMember;
    }

    private ReviewCycle createReviewCycle(Contest contest, ReviewCycle reviewCycle) {
        ReviewCycle panelReviewCycle = new ReviewCycle();
        panelReviewCycle.setContest(contest);
        return new ReviewCycleService().save(panelReviewCycle);
    }

    private PanelReviewSession createPanelReviewSession(ReviewCycle panelReviewCycle, Team team, PanelMember panelMember) {
        PanelReviewSession panelReviewSession = new PanelReviewSession();
        panelReviewSession.setReviewCycle(panelReviewCycle);
        panelReviewSession.setTeam(team);
        panelReviewSession.getPanelMembers().add(panelMember); // Assuming panelMember is added to the session
        // Set other properties
        entityManager.persist(panelReviewSession);
        return panelReviewSession;
    }
}
