package com.example.service;


import org.example.model.*;
import org.example.service.ContestService;
import org.example.service.PanelReviewSessionService;
import org.example.service.ReviewCycleService;
import org.example.service.TeamService;
import org.example.util.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PanelReviewSessionServiceTest {
    PanelReviewSessionService service;
    Contest contest;

    ContestService contestService;

    @BeforeAll
    public static void setup(){
        Map<String, String> props = new HashMap<>();
        JPAUtil.init("panel-app", props);
    }

    @BeforeEach
    public void setupTest(){
        String reviewCycleName = "Review Cycle 2";
        String userId = "45556666";
        String contestName = "Test Contest 2";
        contestService = new ContestService();
        contest = contestService.createContestWithTeamsAndPanelMembers(contestName,
                userId, reviewCycleName, PanelReviewBuilder.populateTeams(), PanelReviewBuilder.populatePanelMembers());
        service = new PanelReviewSessionService();
    }



    @AfterEach
    public void cleanup(){
        TestCleanup testCleanup = new TestCleanup(JPAUtil.getEntityManager());
        testCleanup.cleanUp();
    }

    @Test
    void testAddPanelReviewSessionWithReviewCycle() {
        Contest retrievedContest = contestService.getContestWithTeamsAndPanelMembers(contest.getId());
        long reviewCycleId = retrievedContest.getReviewCycles().get(0).getId();
        Team team = new TeamService().getTeamByTeamNameAndContest("Team 1", contest);

        PanelReviewSession panelReviewSession = new PanelReviewSession();
        ReviewCycleService reviewCycleService = new ReviewCycleService();
        ReviewCycle reviewCycle = reviewCycleService.findByIdWithPanelReviewSeessions(reviewCycleId);

        panelReviewSession.setReviewCycle(reviewCycle);

        Set<PanelMember> panelMembers = retrievedContest.getPanelMembers();

        panelReviewSession.setPanelMembers(panelMembers);

        panelReviewSession.setTeam(team);

        reviewCycle.getPanelReviewMeets().add(panelReviewSession);

        ReviewCycle savedReviewCycle = reviewCycleService.saveWithPanelReviewSessions(reviewCycleId, List.of(panelReviewSession) );


        List<PanelReviewSession> retrievedSessions = reviewCycleService.findByIdWithPanelReviewSeessions(reviewCycle.getId()).getPanelReviewMeets();
        assertEquals(1, retrievedSessions.size());
        PanelReviewSession retrievedSession = retrievedSessions.get(0);
        assertEquals("Team 1", retrievedSession.getTeam().getName() );
        PanelReviewSession retrievedSessionWithMembers = service.getPanelReviewSessionByIdWithEagerPanelMembers(retrievedSession.getId());
        assertEquals(2, retrievedSessionWithMembers.getPanelMembers().size() );
    }

    @Test
    void testAddPanelReviewSessionsWithReviewCycle() {
        PanelMember panelMember = new PanelMember();
        panelMember.setEmail("svashi@gmail.com");
        panelMember.setName("Shreya Vashishtha");

        //add a panel member to existing Contest
        contestService.addPanelMember(contest.getId(), panelMember);

        //retrieve the Contest with teams and panelMembers
        Contest retrievedContest = contestService.getContestWithTeamsAndPanelMembers(contest.getId());
        long reviewCycleId = retrievedContest.getReviewCycles().get(0).getId();
        Set<PanelMember> panelMembers = retrievedContest.getPanelMembers();
        assertEquals(3, panelMembers.size());

        ReviewCycleService reviewCycleService = new ReviewCycleService();
        ReviewCycle reviewCycle = reviewCycleService.findByIdWithPanelReviewSeessions(reviewCycleId);

        PanelReviewSession panelReviewSession1 = new PanelReviewSession();
        panelReviewSession1.setPanelMembers(new HashSet<>(panelMembers));
        TeamService teamService = new TeamService();
        Team team1 = teamService.getTeamByTeamNameAndContest("Team 1", retrievedContest);
        panelReviewSession1.setTeam(team1);

        PanelReviewSession panelReviewSession2 = new PanelReviewSession();
        panelReviewSession2.setPanelMembers(new HashSet<>(panelMembers));
        Team team2 = teamService.getTeamByTeamNameAndContest("Team 2", retrievedContest);
        panelReviewSession2.setTeam(team2);

        List<PanelReviewSession> panelReviewSessions = new ArrayList<>();
        panelReviewSessions.add(panelReviewSession1);
        panelReviewSessions.add(panelReviewSession2);

        //save PanelReviewSessions
        reviewCycle.getPanelReviewMeets().add(panelReviewSession1);
        reviewCycleService.saveWithPanelReviewSessions(reviewCycleId, panelReviewSessions );

        List<PanelReviewSession> retrievedSessions = reviewCycleService.findByIdWithPanelReviewSeessions(reviewCycle.getId()).getPanelReviewMeets();
        assertEquals(2, retrievedSessions.size());
        PanelReviewSession retrievedSession = retrievedSessions.get(0);
        assertEquals("Team 1", retrievedSession.getTeam().getName() );
        PanelReviewSession retrievedSessionWithMembers = service.getPanelReviewSessionByIdWithEagerPanelMembers(retrievedSession.getId());
        assertEquals(3, retrievedSessionWithMembers.getPanelMembers().size() );
    }
}
