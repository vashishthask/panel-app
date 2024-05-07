package org.example.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.example.model.Contest;
import org.example.model.ContestWorkingMember;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ContestWorkingMemberDao {
    private final EntityManager em;

    public ContestWorkingMemberDao(EntityManager em) {
        this.em = em;
    }

    public ContestWorkingMember save(ContestWorkingMember newUser) {
        em.persist(newUser);
        return newUser;
    }

    public ContestWorkingMember findById(String id) {
        return em.find(ContestWorkingMember.class, id);
    }

    public List<Contest> findContestsByContestWorkingMemberId(String userId) {
        String jpql = "SELECT u FROM ContestWorkingMember u JOIN FETCH u.createdContests WHERE u.id = :userId";
        TypedQuery<ContestWorkingMember> query = em.createQuery(jpql, ContestWorkingMember.class);
        query.setParameter("userId", userId);
        List<ContestWorkingMember> users = query.getResultList();
        if(users != null && !users.isEmpty()){
            return users.get(0).getCreatedContests();
        }
        return new ArrayList<>();
    }
}
