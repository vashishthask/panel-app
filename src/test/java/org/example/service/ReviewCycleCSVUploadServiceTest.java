package org.example.service;

import org.example.model.Contest;
import org.example.model.PanelReviewSession;
import org.example.model.ReviewCycle;
import org.example.model.Team;
import org.example.util.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ReviewCycleCSVUploadServiceTest {
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
        String reviewCycleName = "Review Cycle 1";
        String userId = "45556666";
        String contestName = "Star Hackathon";
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
    void testAddSinglePanelReviewSessionsWithCSVFile() {
        ReviewCycleCSVUploadService uploadService = new ReviewCycleCSVUploadService();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("panel-review-sessions.csv");
        List<PanelReviewSession> reviewSessions = uploadService.savePanelReviewSessions(inputStream);

        Contest retrievedContest = contestService.getContestWithTeamsAndPanelMembers(contest.getId());
        List<PanelReviewSession> retrievedSessions = getPanelReviewSessions(retrievedContest);
        assertEquals(1, retrievedSessions.size());
        PanelReviewSession retrievedSession = retrievedSessions.get(0);
        assertEquals("Iron Men", retrievedSession.getTeam().getName() );
        PanelReviewSession retrievedSessionWithMembers = service.getPanelReviewSessionByIdWithEagerPanelMembers(retrievedSession.getId());
        assertEquals(2, retrievedSessionWithMembers.getPanelMembers().size() );
    }

    @Test
    void testAddMultiplePanelReviewSessionsWithCSVFile() {
        ReviewCycleCSVUploadService uploadService = new ReviewCycleCSVUploadService();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("panel-review-sessions-multiple.csv");
        List<PanelReviewSession> reviewSessions = uploadService.savePanelReviewSessions(inputStream);

        Contest retrievedContest = contestService.getContestWithTeamsAndPanelMembers(contest.getId());
        List<PanelReviewSession> retrievedSessions = getPanelReviewSessions(retrievedContest);
        assertEquals(33, retrievedSessions.size());
//        PanelReviewSession retrievedSession = retrievedSessions.get(0);
//        assertEquals("Iron Men", retrievedSession.getTeam().getName() );
//        PanelReviewSession retrievedSessionWithMembers = service.getPanelReviewSessionByIdWithEagerPanelMembers(retrievedSession.getId());
//        assertEquals(2, retrievedSessionWithMembers.getPanelMembers().size() );
    }

    @Test
    void testAddSinglePanelReviewSessionsWithCSVFileNoProgramme() {
        ReviewCycleCSVUploadService uploadService = new ReviewCycleCSVUploadService();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("panel-review-sessions-no-programme.csv");
        List<PanelReviewSession> reviewSessions = uploadService.savePanelReviewSessions(inputStream);

        Contest retrievedContest = contestService.getContestWithTeamsAndPanelMembers(contest.getId());
        List<PanelReviewSession> retrievedSessions = getPanelReviewSessions(retrievedContest);
        assertEquals(1, retrievedSessions.size());
        PanelReviewSession retrievedSession = retrievedSessions.get(0);
        Team sessionTeam = retrievedSession.getTeam();
        assertEquals("Iron Men", sessionTeam.getName() );
        assertEquals("", sessionTeam.getProgrammeName());
        PanelReviewSession retrievedSessionWithMembers = service.getPanelReviewSessionByIdWithEagerPanelMembers(retrievedSession.getId());
        assertEquals(2, retrievedSessionWithMembers.getPanelMembers().size() );
    }

    @Test
    void testAddSinglePanelReviewSessionsWithCSVFileEmailFormat() {
        ReviewCycleCSVUploadService uploadService = new ReviewCycleCSVUploadService();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("panel-review-sessions-email-format.csv");
        List<PanelReviewSession> reviewSessions = uploadService.savePanelReviewSessions(inputStream);

        Contest retrievedContest = contestService.getContestWithTeamsAndPanelMembers(contest.getId());
        List<PanelReviewSession> retrievedSessions = getPanelReviewSessions(retrievedContest);
        assertEquals(1, retrievedSessions.size());
        PanelReviewSession retrievedSession = retrievedSessions.get(0);
        Team sessionTeam = retrievedSession.getTeam();
        assertEquals("Iron Men", sessionTeam.getName() );
        assertEquals("Core Function", sessionTeam.getProgrammeName());
        PanelReviewSession retrievedSessionWithMembers = service.getPanelReviewSessionByIdWithEagerPanelMembers(retrievedSession.getId());
        assertEquals(3, retrievedSessionWithMembers.getPanelMembers().size() );
    }

    private static List<PanelReviewSession> getPanelReviewSessions(Contest retrievedContest) {
        Set<ReviewCycle> reviewCycles = retrievedContest.getReviewCycles();
        List<ReviewCycle> reviewCycleList = new ArrayList<>(reviewCycles);
        long reviewCycleId = reviewCycleList.get(0).getId();
        ReviewCycleService reviewCycleService = new ReviewCycleService();
        ReviewCycle reviewCycle = reviewCycleService.findByIdWithPanelReviewSeessions(reviewCycleId);

        return reviewCycleService.findByIdWithPanelReviewSeessions(reviewCycle.getId()).getPanelReviewMeets();
    }

}