package org.example.service;


import jakarta.persistence.EntityManager;
import org.example.dao.PanelReviewUserDao;
import org.example.model.Contest;
import org.example.util.JPAUtil;

import java.util.List;

public class PanelReviewUserService {
    public List<Contest> findContestsByPanelReviewUserId(String userId){
        try(EntityManager em= JPAUtil.getEntityManager()){
            return new PanelReviewUserDao(em).findContestsByPanelReviewUserId(userId);
        }
    }
}
