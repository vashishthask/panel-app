package org.example.service;

import org.example.model.Contest;
import org.example.model.PanelMember;
import org.example.model.ReviewCycle;
import org.example.model.Team;
import org.example.util.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ContestServiceTest {
    ContestService service;
    String reviewCycleName = "Review Cycle 1";
    String userId = "45556666";
    String contestName = "Test Contest 1";

    @BeforeAll
    public static void setup(){
        Map<String, String> props = new HashMap<>();
        JPAUtil.init("panel-app", props);
    }

    @BeforeEach
    public void setupTest(){
        service = new ContestService();
    }

    @AfterEach
    public void cleanup(){
        TestCleanup testCleanup = new TestCleanup(JPAUtil.getEntityManager());
        testCleanup.cleanUp();
    }

    @Test
    public void testSaveContest() {

        Contest contest = service.createContest(contestName, userId, reviewCycleName);

        assertNotNull(contest.getId(), "Contest ID should not be null after saving");

        Contest retrievedContest = service.getContestEager(contest.getId());
        assertEquals(contestName, retrievedContest.getTitleName(), "Contest title should match after saving");
        assertEquals(userId, retrievedContest.getCreator().getId());
        Set<ReviewCycle> reviewCycles = retrievedContest.getReviewCycles();
        List<ReviewCycle> reviewCycleList = new ArrayList<>(reviewCycles);
        assertEquals(reviewCycleName, reviewCycleList.get(0).getName());

        assertNotNull(retrievedContest, "Retrieved contest should not be null");
        assertEquals(contest.getId(), retrievedContest.getId(), "Retrieved contest ID should match saved contest ID");
        assertEquals(contest.getTitleName(), retrievedContest.getTitleName(), "Retrieved contest title should match saved contest title");
    }

    @Test
    public void testSaveContestWithTeamsAndPanelMembers() {
        String reviewCycleName = "Review Cycle 2";
        String userId = "45556666";
        String contestName = "Test Contest 2";
        Set<Team> teams = PanelReviewBuilder.populateTeams();
        Set<PanelMember> panelMembers = PanelReviewBuilder.populatePanelMembers();
        Contest contest = service.createContestWithTeamsAndPanelMembers(contestName, userId, reviewCycleName, teams, panelMembers);

        assertNotNull(contest.getId(), "Contest ID should not be null after saving");

        Contest retrievedContest = service.getContestWithTeamsAndPanelMembers(contest.getId());
        assertEquals(2, retrievedContest.getTeams().size());

        assertEquals(2, retrievedContest.getPanelMembers().size());
    }

    @Test
    public void testGetContestByNameByReviewCycleNameWithTeamsAndPanelMembers() {
        String reviewCycleName = "Review Cycle 2";
        String userId = "45556666";
        String contestName = "Test Contest 2";
        Set<Team> teams = PanelReviewBuilder.populateTeams();
        Set<PanelMember> panelMembers = PanelReviewBuilder.populatePanelMembers();
        Contest contest = service.createContestWithTeamsAndPanelMembers(contestName, userId, reviewCycleName, teams, panelMembers);

        assertNotNull(contest.getId(), "Contest ID should not be null after saving");

        Contest retrievedContest = service.getContestByNameByReviewCycleNameWithTeamsAndPanelMembers(contestName, reviewCycleName);
        for(ReviewCycle cycle: retrievedContest.getReviewCycles()){
            System.err.println("Id:"+ cycle.getId() + " Name:"+cycle.getName());
        }
        assertEquals(1, retrievedContest.getReviewCycles().size());
        assertEquals(2, retrievedContest.getTeams().size());

        assertEquals(2, retrievedContest.getPanelMembers().size());
    }

    @Test
    void testGetContestsByCreatorId() {
        service.createContest(contestName, userId, reviewCycleName);
        List<Contest> contests = service.getContestsByCreatorIdWithReviewCycles(userId);
        assertEquals(1, contests.size());
        assertEquals(1, contests.get(0).getReviewCycles().size());
        Contest contest = service.createContest("Another Contest", userId, reviewCycleName);
        contests = service.getContestsByCreatorIdWithReviewCycles(userId);
        assertEquals(2, contests.size());
        assertEquals(1, contests.get(0).getReviewCycles().size());

        ReviewCycle anotherReviewCycle = new ReviewCycle();
        anotherReviewCycle.setName("Review Cycle 2");
        service.addReviewCycle(contest.getId(), anotherReviewCycle);
        contests = service.getContestsByCreatorIdWithReviewCycles(userId);
        Contest result = null;
        for(Contest c: contests){
            if(c.getId().equals(contest.getId()))
                result = c;
        }
        assertEquals(2, result.getReviewCycles().size());
    }

    @Test
    public void testAddTeamAndPanelMemberToExistingContest() {
        String reviewCycleName = "Review Cycle";
        String userId = "45556666";
        String contestName = "Test Contest 3";
        Set<Team> teams = PanelReviewBuilder.populateTeams();
        Set<PanelMember> panelMembers = PanelReviewBuilder.populatePanelMembers();
        Contest contest = service.createContestWithTeamsAndPanelMembers(contestName, userId, reviewCycleName, teams, panelMembers);

        assertNotNull(contest.getId(), "Contest ID should not be null after saving");

        Contest retrievedContest = service.getContestWithTeamsAndPanelMembers(contest.getId());

        assertEquals(2, retrievedContest.getTeams().size());

        assertEquals(2, retrievedContest.getPanelMembers().size());

        Team newTeam = new Team(); newTeam.setName("Team 3");
        service.addTeam(retrievedContest.getId(), newTeam);

        Contest retrievedContest2 = service.getContestWithTeamsAndPanelMembers(contest.getId());

        PanelMember panelMember = new PanelMember(); panelMember.setName("Mike Smith");
        Contest contestWithPanel = service.addPanelMember(retrievedContest.getId(), panelMember);


        assertEquals(3, retrievedContest2.getTeams().size());

        assertEquals(3, contestWithPanel.getPanelMembers().size());
    }
    @Test
    public void testUniquenessOfContestTitle(){
        String reviewCycleName = "Review Cycle 4";
        String userId = "45556666";
        String contestName = "Test Contest 4";

        service.createContest(contestName, userId, reviewCycleName);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            service.createContest(contestName, userId, reviewCycleName);
        });

        assertTrue(exception.getMessage().contains(contestName));

    }



    @BeforeAll
    public static void tearDown(){
        JPAUtil.close();
    }
}
