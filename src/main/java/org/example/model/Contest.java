package org.example.model;

import jakarta.persistence.*;

@Entity
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private PanelReviewUser creator;
    private String titleName;

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

