package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ContestWorkingMember {
    @Id
    private String id;

    public ContestWorkingMember(String id){
        this.id = id;
    }

    public ContestWorkingMember(){
    }

    private String emailId;

    @OneToMany(mappedBy = "creator")
    private List<Contest> createdContests = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "contest_collaborators",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Contest> collaboratedContests = new HashSet<>();

    public String getId() {
        return id;
    }

    public List<Contest> getCreatedContests() {
        return createdContests;
    }

    public void setCreatedContests(List<Contest> createdContests) {
        this.createdContests = createdContests;
    }

    public Set<Contest> getCollaboratedContests() {
        return collaboratedContests;
    }

    public void setCollaboratedContests(Set<Contest> collaboratedContests) {
        this.collaboratedContests = collaboratedContests;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    // getters and setters
}