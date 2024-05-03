package org.example.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.example.model.Contest;
import org.example.model.PanelReviewUser;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PanelReviewUserDao {
    private final EntityManager em;

    public PanelReviewUserDao(EntityManager em) {
        this.em = em;
    }

    public PanelReviewUser save(PanelReviewUser newUser) {
        em.persist(newUser);
        return newUser;
    }

    public PanelReviewUser findById(String id) {
        return em.find(PanelReviewUser.class, id);
    }

    public List<Contest> findContestsByPanelReviewUserId(String userId) {
        String jpql = "SELECT u FROM PanelReviewUser u JOIN FETCH u.createdContests WHERE u.id = :userId";
        TypedQuery<PanelReviewUser> query = em.createQuery(jpql, PanelReviewUser.class);
        query.setParameter("userId", userId);
        List<PanelReviewUser> users = query.getResultList();
        if(users != null && !users.isEmpty()){
            return users.get(0).getCreatedContests();
        }
        return new ArrayList<>();
    }
}
