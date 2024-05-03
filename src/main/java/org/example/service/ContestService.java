package org.example.service;

import jakarta.persistence.EntityManager;
import org.example.dao.ContestDAO;
import org.example.model.Contest;
import org.example.util.JPAUtil;

public class ContestService {


    public ContestService() {
    }

    public Contest createContest(String titleName) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Contest contest = new Contest();
            contest.setTitleName(titleName);
            Contest result = new ContestDAO(em).createContest(contest);
            em.getTransaction().commit();
            return result;
        } catch (Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return null;
    }

    public Contest getContest(Long id) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ContestDAO(em).getContest(id);
        }
    }

    // other service methods
}
