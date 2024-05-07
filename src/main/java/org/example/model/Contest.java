package org.example.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private PanelReviewUser creator;

    @Column(unique = true)
    private String titleName;

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ReviewCycle> reviewCycles;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
    private Set<PanelMember> panelMembers = new HashSet<>();

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Set<PanelMember> getPanelMembers() {
        return panelMembers;
    }

    public void setPanelMembers(Set<PanelMember> panelMembers) {
        this.panelMembers = panelMembers;
    }

    public Set<ReviewCycle> getReviewCycles() {
        return reviewCycles;
    }

    public void setReviewCycles(Set<ReviewCycle> reviewCycles) {
        this.reviewCycles = reviewCycles;
    }

    public PanelReviewUser getCreator() {
        return creator;
    }

    public void setCreator(PanelReviewUser creator) {
        this.creator = creator;
    }

    public String getTitleName() {
        return titleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}

