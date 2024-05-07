package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.dao.ContestDAO;
import org.example.dao.ContestWorkingMemberDao;
import org.example.dao.ReviewCycleDao;
import org.example.model.*;
import org.example.util.JPAUtil;

import java.util.HashSet;
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
            ContestWorkingMemberDao userDao = new ContestWorkingMemberDao(em);
            ContestWorkingMember creator = userDao.findById(userId);
            if(creator == null){
                creator = new ContestWorkingMember(userId);
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

            Set<ReviewCycle> reviewCycles = new HashSet<>();
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
            Contest result = new ContestDAO(em).saveContest(contest);



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
            ContestWorkingMemberDao userDao = new ContestWorkingMemberDao(em);
            ContestWorkingMember creator = userDao.findById(userId);
            if(creator == null){
                creator = new ContestWorkingMember(userId);
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
            Set<ReviewCycle> reviewCycles = new HashSet<>();
            reviewCycles.add(reviewCycle);
            contest.setReviewCycles(reviewCycles);

            // Save Contest
            Contest result = new ContestDAO(em).saveContest(contest);
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

    public Contest addCollaboratorToContest(Long contestId, ContestWorkingMember member) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ContestDAO contestDAO = new ContestDAO(em);
            Contest contest = contestDAO.getContest(contestId);

            // Check if the collaborator exists in the database
            ContestWorkingMemberDao workingMemberDao = new ContestWorkingMemberDao(em);
            ContestWorkingMember collaborator = workingMemberDao.findById(member.getId());

            if (collaborator == null) {
                collaborator = member;
                workingMemberDao.save(collaborator);
            }

            contest.getCollaborators().add(collaborator);
            collaborator.getCollaboratedContests().add(contest);
            Contest savedContest = contestDAO.saveContest(contest);
            transaction.commit();
            return savedContest;
        }catch (Exception e){
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

    private ContestWorkingMember createUser(String userId, EntityManager em) {
        ContestWorkingMember user = new ContestWorkingMember(userId);
        return new ContestWorkingMemberDao(em).save(user);
    }

    public Contest getContest(Long id) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ContestDAO(em).getContest(id);
        }
    }

    public List<Contest> getCreatedAndCollaboratedContestsByCreatorIdWithReviewCycles(String creatorId) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ContestDAO(em).getCreatedAndCollaboratedContestsByCreatorIdWithReviewCycles(creatorId);
        }
    }

    public Contest getContestWithReviewCycles(Long id) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ContestDAO(em).getContestWithReviewCycles(id);
        }
    }

    public Contest getContestWithTeamsAndPanelMembers(Long id) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ContestDAO(em).getContestWithReviewCyclesTeamsAndPanelMembers(id);
        }
    }

    public Contest addReviewCycle(long id, ReviewCycle reviewCycle){
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ContestDAO contestDao = new ContestDAO(em);
            Contest contest = contestDao.getContestWithReviewCyclesTeamsAndPanelMembers(id);
            reviewCycle.setContest(contest);
            contest.getReviewCycles().add(reviewCycle);
            em.persist(reviewCycle);
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

    public Contest getContestByNameByReviewCycleNameWithTeamsAndPanelMembers(String titleName, String reviewCycleName) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            return new ContestDAO(em).getContestByNameByReviewCycleNameWithTeamsAndPanelMembers(titleName, reviewCycleName);
        }
    }

    public Contest addTeam(Long id, Team newTeam) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ContestDAO contestDao = new ContestDAO(em);
            Contest contest = contestDao.getContestWithReviewCyclesTeamsAndPanelMembers(id);
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
            Contest contest = contestDao.getContestWithReviewCyclesTeamsAndPanelMembers(id);
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
