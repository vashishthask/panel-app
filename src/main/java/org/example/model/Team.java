package org.example.model;

import jakarta.persistence.*;

@Entity
public class Team {
    private String programmeName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Contest contest;

    public Team(String teamName, String programmeName) {
        this.name = teamName;
        this.programmeName = programmeName;
    }

    public Team(){

    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public Long getId() {
        return id;
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
}
