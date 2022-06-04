package com.xy124.drone.service;

import com.xy124.drone.model.Drone;
import com.xy124.drone.repository.DroneDetailsRepository;
import com.xy124.drone.repository.DroneRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DroneServiceTest {
    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneDetailsRepository droneDetailsRepository;

    @Before
    @Transactional
    public void initDrone() {
        saveDrone();
    }

    @After
    @Transactional
    public void cleanUp(){


    }
    @Test
    public void findDrone() {
        //given(준비)


        Drone searchDrone = new Drone();

        searchDrone.setDroneDeviceName("abcd");
        //when(실행)


        Drone findDrone = new Drone();
        if (searchDrone.getDroneDeviceName() != null) {
            droneRepository.findByDroneDeviceName(searchDrone.getDroneDeviceName());
        } else if (searchDrone.getDroneSeq() != null) {
            Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(searchDrone.getDroneSeq());
            if (!optionalDrone.isPresent()) {

            }
            optionalDrone.get();
        } else {

        }
        //then(검증)
        if (searchDrone.getDroneDeviceName() != null) {
            assertThat(findDrone.getDroneDeviceName()).isEqualTo(searchDrone.getDroneDeviceName());
        } else if (searchDrone.getDroneSeq() != null) {
            assertThat(findDrone.getDroneDeviceName()).isEqualTo(searchDrone.getDroneDeviceName());
        }
    }

    @Test
    public void findOneDrone() {

    }

    @Test
    public void findAllDrone() {

    }

    @Test
    public void updateDrone() {

    }

    @Test
    @Transactional
    public void saveDrone() {
        Drone inputDrone = new Drone();
        inputDrone.setDroneDeviceName("abcd");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        inputDrone.setUpdateDt(timestamp);
        droneRepository.save(inputDrone);


    }

    @Test
    public void deleteDrone() {


    }

    @Test
    public void findDroneList() {

    }
}
