package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TestCleanup {

    private final EntityManager entityManager;

    public TestCleanup(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void cleanUp() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            entityManager.createQuery("DELETE FROM Evaluation").executeUpdate();
            entityManager.createQuery("DELETE FROM PanelMember").executeUpdate();
            entityManager.createQuery("DELETE FROM PanelReviewSession").executeUpdate();
            entityManager.createQuery("DELETE FROM ReviewCycle").executeUpdate();
            entityManager.createQuery("DELETE FROM Team").executeUpdate();
            entityManager.createQuery("DELETE FROM Contest").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}