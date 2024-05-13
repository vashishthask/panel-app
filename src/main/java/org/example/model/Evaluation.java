package org.example.model;

import jakarta.persistence.*;

@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "panel_member_id")
    private PanelMember panelMember;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "panel_review_session_id")
    private PanelReviewSession panelReviewSession;

    public Long getId() {
        return id;
    }

    public PanelMember getPanelMember() {
        return panelMember;
    }

    public void setPanelMember(PanelMember panelMember) {
        this.panelMember = panelMember;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public PanelReviewSession getPanelReviewSession() {
        return panelReviewSession;
    }

    public void setPanelReviewSession(PanelReviewSession panelReviewSession) {
        this.panelReviewSession = panelReviewSession;
    }
}
