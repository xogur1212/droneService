package com.xy124.drone.service;

import com.xy124.drone.model.Mission;
import com.xy124.drone.repository.MissionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
class MissionServiceTest {


    @Autowired
    MissionRepository missionRepository;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Transactional
    void findAllMission() {

            //return (List<Mission>) missionRepository.findAll();
    }

    @Test
    void findOneMission() {
    }

    @Test
    void findMissionList() {
    }

    @Test
    void saveMission() {
    }

    @Test
    void updateMission() {
    }

    @Test
    void deleteMission() {
    }
}