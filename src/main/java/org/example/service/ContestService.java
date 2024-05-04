package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.dao.ContestDAO;
import org.example.dao.PanelReviewUserDao;
import org.example.dao.ReviewCycleDao;
import org.example.model.*;
import org.example.util.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ContestService {


    public ContestService() {
    }

    public Contest createContestWithTeamsAndPanelMembers(String titleName, String userId, String reviewCycleName, Set<Team> teams, Set<PanelMember> panelMembers) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            if(!isTitleNameUnique(titleName, em))
                throw new IllegalArgumentException("Contest with titleName " + titleName + " already exists");
            transaction.begin();
            // Create Creator
            //check if user exists
            PanelReviewUserDao userDao = new PanelReviewUserDao(em);
            PanelReviewUser creator = userDao.findById(userId);
            if(creator == null){
                creator = new PanelReviewUser(userId);
                userDao.save(creator);
            }

            // Create Contest
            Contest contest = new Contest();
            contest.setTitleName(titleName);
            contest.setCreator(creator);

            // Create ReviewCycle
            ReviewCycle reviewCycle = new ReviewCycle();
            reviewCycle.setName(reviewCycleName);
            reviewCycle.setContest(contest);

            List<ReviewCycle> reviewCycles = new ArrayList<>();
            reviewCycles.add(reviewCycle);

            contest.setReviewCycles(reviewCycles);

            for (Team team: teams){
                team.setContest(contest);
            }
            contest.setTeams(teams);

            for (PanelMember panelMember: panelMembers){
                panelMember.setContest(contest);
            }
            contest.setPanelMembers(panelMembers);

            // Save Contest
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

    public Contest createContest(String titleName, String userId, String reviewCycleName) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            if(!isTitleNameUnique(titleName, em))
                throw new IllegalArgumentException("Contest with titleName " + titleName + " already exists");

            transaction.begin();
            // Create Creator
            //check if user exists
            PanelReviewUserDao userDao = new PanelReviewUserDao(em);
            PanelReviewUser creator = userDao.findById(userId);
            if(creator == null){
                creator = new PanelReviewUser(userId);
                userDao.save(creator);
            }

            // Create Contest
            Contest contest = new Contest();
            contest.setTitleName(titleName);
            contest.setCreator(creator);

            // Create ReviewCycle
            ReviewCycle reviewCycle = new ReviewCycle();
            reviewCycle.setName(reviewCycleName);
            reviewCycle.setContest(contest);
            List<ReviewCycle> reviewCycles = new ArrayList<>();
            reviewCycles.add(reviewCycle);
            contest.setReviewCycles(reviewCycles);

            // Save Contest
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

    private ReviewCycle createReviewCycle(EntityManager em) {
        ReviewCycle reviewCycle = new ReviewCycle();
        reviewCycle.setName("Review Cycle 1");
        ReviewCycleDao reviewCycleDao = new ReviewCycleDao(em);
        ReviewCycle result = reviewCycleDao.save(reviewCycle);
        System.err.println("The review Cycle id is: " + result.getId());
        Optional<ReviewCycle> reviewCycle1 = reviewCycleDao.findById(result.getId());
        System.err.println("Found review cycle is:"+reviewCycle1.get().getName());

        return result;
    }

    private PanelReviewUser createUser(String userId, EntityManager em) {
        PanelReviewUser user = new PanelReviewUser(userId);
        return new PanelReviewUserDao(em).save(user);
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

    public Contest getContestWithTeamsAndPanelMembers(Long id) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ContestDAO(em).getContestWithTeamsAndPanelMembers(id);
        }
    }

    public Contest addTeam(Long id, Team newTeam) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ContestDAO contestDao = new ContestDAO(em);
            Contest contest = contestDao.getContestWithTeamsAndPanelMembers(id);
            newTeam.setContest(contest);
            contest.getTeams().add(newTeam);
            em.persist(newTeam);
            transaction.commit();
            return contest;
        } catch (Exception e){
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }

    }

    public Contest addPanelMember(Long id, PanelMember panelMember) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ContestDAO contestDao = new ContestDAO(em);
            Contest contest = contestDao.getContestWithTeamsAndPanelMembers(id);
            panelMember.setContest(contest);
            contest.getPanelMembers().add(panelMember);
            em.persist(panelMember);
            transaction.commit();
            return contest;
        } catch (Exception e){
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private boolean isTitleNameUnique(String titleName, EntityManager entityManager) {
        String jpql = "SELECT COUNT(c) FROM Contest c WHERE c.titleName = :titleName";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("titleName", titleName)
                .getSingleResult();
        return count == 0;
    }
    // other service methods
}
