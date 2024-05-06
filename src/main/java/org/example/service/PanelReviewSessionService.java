package org.example.service;

import jakarta.persistence.EntityManager;
import org.example.dao.PanelReviewSessionDao;
import org.example.model.PanelReviewSession;
import org.example.util.JPAUtil;

import java.util.List;

public class PanelReviewSessionService {
    
//    final CSVUtil csvUtil;
//
//    public PanelReviewSessionService(){
//        this.csvUtil = new CSVUtil();
//    }

    public PanelReviewSession savePanelReviewSession(PanelReviewSession panelReviewSession) {
        try(EntityManager em = JPAUtil.getEntityManager()) {
            PanelReviewSessionDao panelReviewSessionDao = new PanelReviewSessionDao(em);
            return panelReviewSessionDao.save(panelReviewSession);
        }
    }

	public List<PanelReviewSession> findRemainingSessionsForPanelMember(Long panelMemberId){
        try(EntityManager em = JPAUtil.getEntityManager()) {
            PanelReviewSessionDao panelReviewSessionDao = new PanelReviewSessionDao(em);
            return panelReviewSessionDao.findPendingSessionsForPanelMember(panelMemberId);
        }
	}

    public PanelReviewSession getPanelReviewSessionByIdWithEagerPanelMembers(long panelReviewSessionId) {
        try(EntityManager em = JPAUtil.getEntityManager()){
            return new  PanelReviewSessionDao(em).getPanelReviewSessionByIdWithEagerPanelMembers(panelReviewSessionId);
        }
    }

//    public List<PanelReviewSessionDto> findAll() {
//        try(EntityManager em = JPAUtil.getEntityManager()) {
//            PanelReviewSessionDao panelReviewSessionDao = new PanelReviewSessionDao(em);
//            List<PanelReviewSession> panelReviewSessions = panelReviewSessionDao.findAll();
//            List<PanelReviewSessionDto> panelReviewSessionDtos = new ArrayList<>();
//            for (PanelReviewSession session : panelReviewSessions) {
//                String teamName = session.getTeam().getProgrammeName() + " | " + session.getTeam().getName();
//                Set<PanelMember> panelMembers = session.getPanelMembers();
//                List<String> panelMemberNames = new ArrayList<>();
//                for (PanelMember member : panelMembers) {
//                    panelMemberNames.add(member.getName());
//                }
//                PanelReviewSessionDto panelReviewSessionDto = new PanelReviewSessionDto();
//                panelReviewSessionDto.setTeamName(teamName);
//                panelReviewSessionDto.setPanelMemberNames(panelMemberNames);
//                panelReviewSessionDtos.add(panelReviewSessionDto);
//            }
//            return panelReviewSessionDtos;
//        }
//    }

//    public void save(UploadedFile file) {
//        try {
//            List<PanelReviewSession> panelReviewSessions = csvUtil.csvToPanelReviewSessions(file.asStream());
//            System.err.println("The panelReviewMeets:"+ panelReviewSessions);
//            //panelReviewMeetDao.saveAll(panelReviewMeets);
//        } catch (IOException e) {
//            throw new RuntimeException("fail to store csv data: " + e.getMessage());
//        }
//    }
//    public List<PanelReviewSessionDto> findByReviewCycle(ReviewCycle rc) {
//        try(EntityManager em = JPAUtil.getEntityManager()) {
//            PanelReviewSessionDao panelReviewSessionDao = new PanelReviewSessionDao(em);
//            List<PanelReviewSession> panelReviewSessions = panelReviewSessionDao.findByReviewCycle(rc);
//            List<PanelReviewSessionDto> panelReviewSessionDtos = new ArrayList<>();
//            for (PanelReviewSession session : panelReviewSessions) {
//                String teamName = session.getTeam().getProgrammeName() + " | " + session.getTeam().getName();
//                Set<PanelMember> panelMembers = session.getPanelMembers();
//                List<String> panelMemberNames = new ArrayList<>();
//                for (PanelMember member : panelMembers) {
//                    panelMemberNames.add(member.getName());
//                }
//                PanelReviewSessionDto panelReviewSessionDto = new PanelReviewSessionDto();
//                panelReviewSessionDto.setTeamName(teamName);
//                panelReviewSessionDto.setPanelMemberNames(panelMemberNames);
//                panelReviewSessionDtos.add(panelReviewSessionDto);
//            }
//            return panelReviewSessionDtos;
//        }
//    }
}