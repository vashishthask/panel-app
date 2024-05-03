package com.example.service;

import org.example.model.Contest;
import org.example.service.ContestService;
import org.example.util.JPAUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContestServiceTest {
    ContestService service;

    @BeforeAll
    public static void setup(){
        Map<String, String> props = new HashMap<>();
        JPAUtil.init("panel-app", props);
    }

    @BeforeEach
    public void setupTest(){
        service = new ContestService();
    }

    @Test
    public void testSaveContest() {
        Contest contest = service.createContest("Test Contest", "45556666");

        assertNotNull(contest.getId(), "Contest ID should not be null after saving");
        assertEquals("Test Contest", contest.getTitleName(), "Contest title should match after saving");

        Contest retrievedContest = service.getContestEager(contest.getId());
        System.err.println("The returned contest is:"+ retrievedContest.getTitleName());

        assertNotNull(retrievedContest, "Retrieved contest should not be null");
        assertEquals(contest.getId(), retrievedContest.getId(), "Retrieved contest ID should match saved contest ID");
        assertEquals(contest.getTitleName(), retrievedContest.getTitleName(), "Retrieved contest title should match saved contest title");
    }

    @BeforeAll
    public static void tearDown(){
        JPAUtil.close();
    }
}
