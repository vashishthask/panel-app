package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review_cycles")
public class ReviewCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    @ManyToOne
    //@JoinColumn(name = "contest_id")
    private Contest contest;

    @OneToMany(mappedBy = "reviewCycle", cascade = CascadeType.ALL)
    private List<PanelReviewSession> panelReviewSessions = new ArrayList<>();



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }



    public List<PanelReviewSession> getPanelReviewMeets() {
        return panelReviewSessions;
    }

    public void setPanelReviewMeets(List<PanelReviewSession> panelReviewSessions) {
        this.panelReviewSessions = panelReviewSessions;
    }



    // constructors, getters, setters
}
