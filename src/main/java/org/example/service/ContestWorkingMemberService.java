package org.example.service;

import jakarta.persistence.EntityManager;
import org.example.dao.ContestWorkingMemberDao;
import org.example.model.Contest;
import org.example.util.JPAUtil;

import java.util.List;

public class ContestWorkingMemberService {
    public List<Contest> findContestsByContestWorkingMemberId(String userId) {
        try(EntityManager em = JPAUtil.getEntityManager()){
            return new ContestWorkingMemberDao(em).findContestsByContestWorkingMemberId(userId);
        }
    }
}
