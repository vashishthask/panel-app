package com.example.service;

import org.example.model.Contest;
import org.example.service.ContestService;
import org.example.util.JPAUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContestServiceTest {

    @Test
    public void testSaveContest() {
        Map<String, String> props = new HashMap<>();
        JPAUtil.init("panel-app", props);
        ContestService service = new ContestService();

        Contest contest = service.createContest("Test Contest");

        assertNotNull(contest.getId(), "Contest ID should not be null after saving");
        assertEquals("Test Contest", contest.getTitleName(), "Contest title should match after saving");

        Contest retrievedContest = service.getContest(contest.getId());
        System.err.println("The returned contest is:"+ retrievedContest.getTitleName());

        assertNotNull(retrievedContest, "Retrieved contest should not be null");
        assertEquals(contest.getId(), retrievedContest.getId(), "Retrieved contest ID should match saved contest ID");
        assertEquals(contest.getTitleName(), retrievedContest.getTitleName(), "Retrieved contest title should match saved contest title");
    }
}
