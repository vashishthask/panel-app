package com.example.service;

import org.example.model.PanelMember;
import org.example.model.Team;

import java.util.HashSet;
import java.util.Set;

public class PanelReviewBuilder {
    public static Set<Team> populateTeams() {
        Team team1 = new Team();
        team1.setName("Team 1");
        Set<Team> teams = new HashSet<>();
        teams.add(team1);
        Team team2 = new Team();
        team2.setName("Team 2");

        teams.add(team2);
        return teams;
    }

    public static Set<PanelMember> populatePanelMembers() {
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
}
