package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.dao.ContestDAO;
import org.example.dao.PanelReviewUserDao;
import org.example.model.Contest;
import org.example.model.PanelReviewUser;
import org.example.util.JPAUtil;

public class ContestService {


    public ContestService() {
    }

    public Contest createContest(String titleName, String userId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Contest contest = new Contest();
            contest.setTitleName(titleName);
            PanelReviewUser user = new PanelReviewUser(userId);
            PanelReviewUser creator = new PanelReviewUserDao(em).save(user);
            contest.setCreator(creator);
            Contest result = new ContestDAO(em).createContest(contest);
            transaction.commit();
            return result;
        } catch (Exception e){
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Contest getContest(Long id) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ContestDAO(em).getContest(id);
        }
    }

    public Contest getContestEager(Long id) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ContestDAO(em).getContestWithUser(id);
        }
    }
    // other service methods
}
