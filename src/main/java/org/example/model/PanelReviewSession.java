package org.example.model;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "panel_review_sessions")
@ToString()
public class PanelReviewSession {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "review_cycle_id")
    private ReviewCycle reviewCycle;

    // Assuming each PanelReviewMeet has multiple evaluations:
    @OneToMany(mappedBy = "panelReviewSession", cascade = CascadeType.ALL)
    private List<Evaluation> evaluations = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "panel_review_session_panel_members",
        joinColumns = @JoinColumn(name = "panel_review_session_id"),
        inverseJoinColumns = @JoinColumn(name = "panel_member_id")
    )
    private Set<PanelMember> panelMembers = new HashSet<>();

    public ReviewCycle getReviewCycle() {
        return reviewCycle;
    }

    public void setReviewCycle(ReviewCycle reviewCycle) {
        this.reviewCycle = reviewCycle;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Set<PanelMember> getPanelMembers() {
		return panelMembers;
	}

	public void setPanelMembers(Set<PanelMember> panelMembers) {
		this.panelMembers = panelMembers;
	}

	public void addPanelMember(PanelMember panelMember) {
		this.panelMembers.add(panelMember);
		panelMember.getPanelReviewSessions().add(this);
	}

	public void removePanelMember(PanelMember panelMember) {
		this.panelMembers.remove(panelMember);
		panelMember.getPanelReviewSessions().remove(this);
	}
}