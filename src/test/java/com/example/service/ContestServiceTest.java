package com.example.service;

import org.example.model.Contest;
import org.example.model.PanelMember;
import org.example.model.Team;
import org.example.service.ContestService;
import org.example.util.JPAUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ContestServiceTest {
    ContestService service;

    @BeforeAll
    public static void setup(){
        Map<String, String> props = new HashMap<>();
        JPAUtil.init("panel-app", props);
    }

    @BeforeEach
    public void setupTest(){
        service = new ContestService();
    }

    @Test
    public void testSaveContest() {
        String reviewCycleName = "Review Cycle 1";
        String userId = "45556666";
        String contestName = "Test Contest 1";
        Contest contest = service.createContest(contestName, userId, reviewCycleName);

        assertNotNull(contest.getId(), "Contest ID should not be null after saving");

        Contest retrievedContest = service.getContestEager(contest.getId());
        assertEquals(contestName, retrievedContest.getTitleName(), "Contest title should match after saving");
        assertEquals(userId, retrievedContest.getCreator().getId());
        assertEquals(reviewCycleName, retrievedContest.getReviewCycles().get(0).getName());

        assertNotNull(retrievedContest, "Retrieved contest should not be null");
        assertEquals(contest.getId(), retrievedContest.getId(), "Retrieved contest ID should match saved contest ID");
        assertEquals(contest.getTitleName(), retrievedContest.getTitleName(), "Retrieved contest title should match saved contest title");
    }

    @Test
    public void testSaveContestWithTeamsAndPanelMembers() {
        String reviewCycleName = "Review Cycle 2";
        String userId = "45556666";
        String contestName = "Test Contest 2";
        Set<Team> teams = populateTeams();
        Set<PanelMember> panelMembers = populatePanelMembers();
        Contest contest = service.createContestWithTeamsAndPanelMembers(contestName, userId, reviewCycleName, teams, panelMembers);

        assertNotNull(contest.getId(), "Contest ID should not be null after saving");

        Contest retrievedContest = service.getContestWithTeamsAndPanelMembers(contest.getId());
        assertEquals(2, retrievedContest.getTeams().size());

        assertEquals(2, retrievedContest.getPanelMembers().size());
    }

    @Test
    public void testAddTeamAndPanelMemberToExistingContest() {
        String reviewCycleName = "Review Cycle";
        String userId = "45556666";
        String contestName = "Test Contest 3";
        Set<Team> teams = populateTeams();
        Set<PanelMember> panelMembers = populatePanelMembers();
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

    private Set<Team> populateTeams() {
        Team team1 = new Team();
        team1.setName("Team 1");
        Set<Team> teams = new HashSet<>();
        teams.add(team1);
        Team team2 = new Team();
        team2.setName("Team 2");

        teams.add(team2);
        return teams;
    }

    private Set<PanelMember> populatePanelMembers() {
        PanelMember panelMember = new PanelMember();
        panelMember.setName("John Smith");
        panelMember.setEmail("jsmith@email.com");
        Set<PanelMember> panelMembers = new HashSet<>();
        panelMembers.add(panelMember);
        PanelMember panelMember1 = new PanelMember();
        panelMember1.setName("Alok Kumar");
        panelMember1.setEmail("akumar@email.com");

        panelMembers.add(panelMember1);
        return panelMembers;
    }

    @BeforeAll
    public static void tearDown(){
        JPAUtil.close();
    }
}
