package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.example.model.PanelMember;

import java.util.List;
import java.util.Optional;

public class PanelMemberDao {
    private final EntityManager entityManager;

    public PanelMemberDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public PanelMember save(PanelMember panelMember){
        entityManager.persist(panelMember);
        return panelMember;
    }

    List<PanelMember> findDistinctByPanelReviewSessionsIsNotEmpty() {
        TypedQuery<PanelMember> query = entityManager.createQuery("SELECT DISTINCT pm FROM PanelMember pm JOIN pm.panelReviewSessions prm", PanelMember.class);
        return query.getResultList();

    }

    Optional<PanelMember> findByEmailId(String emailId) {
        TypedQuery<PanelMember> query = entityManager.createQuery("Select p from PanelMember p where p.email=:emailId", PanelMember.class);
        query.setParameter("emailId", emailId);
        List<PanelMember> results = query.getResultList();
        if (results.isEmpty())
            return Optional.empty();
        else
            return Optional.of(results.get(0));
    }

    public List<PanelMember> findByEmailIdIn(List<String> emailIds) {
        TypedQuery<PanelMember> query = entityManager.createQuery(
                "SELECT pm FROM PanelMember pm WHERE pm.email IN :emailIds",
                PanelMember.class
        );
        query.setParameter("emailIds", emailIds);
        return query.getResultList();

    }

    public boolean existsPanelMemberByEmailId(String emailId) {
        String hql = "SELECT 1 FROM PanelMember p WHERE p.email = :emailId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("emailId", emailId);
        return !query.getResultList().isEmpty();

    }
}
