package org.example.service;

import io.muserver.UploadedFile;
import jakarta.persistence.EntityManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.dao.*;
import org.example.model.*;
import org.example.util.JPAUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.StreamSupport;

public class ReviewCycleCSVUploadService {
    public static String TYPE = "text/csv";

    public void save(UploadedFile file) {
        try {
            InputStream stream = file.asStream();
            List<PanelReviewSession> panelReviewSessions = savePanelReviewSessions(stream);
            System.err.println("The panelReviewMeets:"+ panelReviewSessions);
            //panelReviewMeetDao.saveAll(panelReviewMeets);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public boolean hasCSVFormat(UploadedFile file) {
        return TYPE.equals(file.contentType());
    }

    public List<PanelReviewSession> savePanelReviewSessions(InputStream is) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<PanelReviewSession> panelReviewSessions = new ArrayList<>();
            Iterable<CSVRecord> csvRecs = csvParser.getRecords();
            entityManager.getTransaction().begin();


            List<CSVRecord> csvRecords = StreamSupport.stream(csvRecs.spliterator(), false)
                    .toList();
            String contestName = csvRecords.get(0).get("ContestName");
            String reviewCycleName = csvRecords.get(0).get("ReviewCycleName");
            ContestDAO contestDAO = new ContestDAO(entityManager);
            Contest contest = contestDAO.getContestByNameByReviewCycleNameWithTeamsAndPanelMembers(contestName, reviewCycleName);
            ReviewCycle reviewCycle = new ReviewCycleDao(entityManager).getReviewCycleByContestTitleAndReviewCycleName(contestName, reviewCycleName);
            Set<Team> teams = contest.getTeams();

            for (CSVRecord csvRecord : csvRecords) {
                System.err.println(csvRecord.toString());
                Team team = new Team(csvRecord.get("TeamName"), csvRecord.get("ProgrammeName"));

                Team teamForPanelSession = isTeamAlreadySaved(team, teams);
                if(teamForPanelSession == null) {
                    contest.getTeams().add(team);
                    team.setContest(contest);
                    teamForPanelSession = new TeamDao(entityManager).save(team);
                }

                List<PanelMember> panelMembers = null;
                try {
                    panelMembers = getPanelMembers(csvRecord.get("PanelEmails"));
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("The PanelEmails values in the CSV record " + csvRecord.toMap().values() + " are not proper.");
                }
                List<String> emailIds = new ArrayList<>();
                PanelMemberDao panelMemberDao = new PanelMemberDao(entityManager);
                for (PanelMember panel : panelMembers) {
                    boolean exists = panelMemberDao.existsPanelMemberByEmailId(panel.getEmail());
                    if (!exists)
                        panelMemberDao.save(panel);
                    emailIds.add(panel.getEmail());
                }

                //panelMemberRepository.saveAll(panelMembers);
                List<PanelMember> panels = panelMemberDao.findByEmailIdIn(emailIds);


                PanelReviewSession panelReviewSession = populatePanelReviewSession(teamForPanelSession, panels, reviewCycle);
                panelReviewSessions.add(panelReviewSession);
            }
            PanelReviewSessionDao panelReviewSessionDao = new PanelReviewSessionDao(entityManager);
            System.out.println("Panel Review Session size is:"+ panelReviewSessions.size());
            panelReviewSessionDao.saveAll(panelReviewSessions);
            entityManager.getTransaction().commit();

            return panelReviewSessions;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    private PanelReviewSession populatePanelReviewSession(Team teamForPanelSession, List<PanelMember> panels, ReviewCycle reviewCycle) {
        PanelReviewSession panelReviewSession = new PanelReviewSession();
        panelReviewSession.setTeam(teamForPanelSession);
        panelReviewSession.setPanelMembers(new HashSet<>(panels));
        panelReviewSession.setReviewCycle(reviewCycle);
        return panelReviewSession;
    }

    private Team isTeamAlreadySaved(Team team, Set<Team> teams) {
        Iterator<Team> iterator = teams.iterator();
        if(iterator.hasNext()){
            Team savedTeam = iterator.next();
            String name = savedTeam.getName();
            String programmeName = savedTeam.getProgrammeName();
            if(name.equals(team.getName())){
                if((programmeName == null && team.getProgrammeName() == null) || (programmeName != null && programmeName.equals(team.getProgrammeName()) ))
                    return savedTeam;
            }

        }
        return null;
    }

    private List<PanelMember> getPanelMembers(String panelEmails) {
        String[] emails = panelEmails.split(";");
        List<PanelMember> panelMembers = new ArrayList<>();
        for (String part : emails) {
            String[] nameAndEmail = part.trim().split("\\s*<\\s*");
            for (String detail :
                    nameAndEmail) {
                //System.err.println("The detail is:" + detail);
            }
            String name = nameAndEmail[0].trim();
            if (nameAndEmail.length == 1) {
                throw new IllegalArgumentException();
            }
            String email = nameAndEmail[1].trim().replace(">", "");
            //System.out.println("****The name is:"+ name + " email is:"+email);
            PanelMember panelMember = new PanelMember(name, email);
            panelMembers.add(panelMember);
        }
        return panelMembers;

    }
}
