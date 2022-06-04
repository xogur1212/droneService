package com.xy124.drone.service;

import com.xy124.drone.model.Drone;
import com.xy124.drone.model.dto.request.DroneRequest;
import com.xy124.drone.repository.DroneDetailsRepository;
import com.xy124.drone.repository.DroneRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
//        saveDrone();

    }

    @After
    @Transactional
    public void cleanUp() {


    }

    @Test
    @Transactional
    public void findDrone이름으로검색() {
        //given(준비)
        saveDrone();
        String droneDeviceName = "TestDrone1";
        Drone searchDrone = new Drone();
        searchDrone.setDroneDeviceName(droneDeviceName);
        //when(실행)


        Drone findDrone = new Drone();
        if (searchDrone.getDroneDeviceName() != null) {
            findDrone = droneRepository.findByDroneDeviceName(searchDrone.getDroneDeviceName());
        } else if (searchDrone.getDroneSeq() != null) {
            Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(searchDrone.getDroneSeq());
            if (!optionalDrone.isPresent()) {
                findDrone = null;
            } else {
                optionalDrone.get();
            }

        } else {
            findDrone = null;
        }
        //then(검증)
        if (searchDrone.getDroneDeviceName() != null) {
            assertThat(findDrone.getDroneDeviceName()).isEqualTo(searchDrone.getDroneDeviceName());
        } else if (searchDrone.getDroneSeq() != null) {
            assertThat(findDrone.getDroneDeviceName()).isEqualTo(searchDrone.getDroneDeviceName());
        }
    }

    @Test
    @Transactional
    public void findDroneSeq으로검색() {
        //given(준비)
        saveDrone();

        Drone searchDrone = new Drone();
        String droneDeviceName = "TestDrone1";
        Long droneSeq = droneRepository.findByDroneDeviceName(droneDeviceName).getDroneSeq();
        searchDrone.setDroneSeq(droneSeq);
        //when(실행)


        Drone findDrone = new Drone();
        if (searchDrone.getDroneDeviceName() != null) {
            findDrone = droneRepository.findByDroneDeviceName(searchDrone.getDroneDeviceName());
        } else if (searchDrone.getDroneSeq() != null) {
            Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(searchDrone.getDroneSeq());
            if (!optionalDrone.isPresent()) {
                findDrone = null;
            } else {
                findDrone = optionalDrone.get();
            }

        } else {
            findDrone = null;
        }
        //then(검증)
        if (searchDrone.getDroneDeviceName() != null) {
            assertThat(findDrone.getDroneDeviceName()).isEqualTo(searchDrone.getDroneDeviceName());
        } else if (searchDrone.getDroneSeq() != null) {
            assertThat(findDrone.getDroneSeq()).isEqualTo(searchDrone.getDroneSeq());
        }
    }

    @Test
    @Transactional
    public void findAllDrone() {

        //given
        String droneDeviceName1 = "TestDrone1";
        Drone inputDrone1 = new Drone();
        inputDrone1.setDroneDeviceName(droneDeviceName1);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        inputDrone1.setUpdateDt(timestamp);
        droneRepository.save(inputDrone1);

        String droneDeviceName2 = "TestDrone2";
        Drone inputDrone2 = new Drone();
        inputDrone2.setDroneDeviceName(droneDeviceName2);
        timestamp = new Timestamp(System.currentTimeMillis());
        inputDrone2.setUpdateDt(timestamp);
        droneRepository.save(inputDrone2);


        //when
        List<Drone> droneList = droneRepository.findAllByDroneSeqNot(0l);
        //then
        List<String> droneDeviceNameList = new ArrayList<>();
        droneList.forEach(drone -> {
            droneDeviceNameList.add(drone.getDroneDeviceName());
        });
        assertThat(droneDeviceNameList.contains(droneDeviceName1)).isEqualTo(true);
        assertThat(droneDeviceNameList.contains(droneDeviceName2)).isEqualTo(true);
    }


    @Test
    @Transactional
    public void updateDrone() {
        //given
        String droneDeviceName1 = "TestDrone1";
        Drone inputDrone1 = new Drone();
        inputDrone1.setDroneDeviceName(droneDeviceName1);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        inputDrone1.setUpdateDt(timestamp);
        droneRepository.save(inputDrone1);

        Long updateDroneSeq = droneRepository.findByDroneDeviceName(droneDeviceName1).getDroneSeq();
        String updateUserId = "user135";
        String changeDroneDeviceName = "updateDrone1";
        Drone updateDroneInfo = Drone.builder()
                .droneDeviceName(changeDroneDeviceName)
                .userId(updateUserId)
                .build();
        Drone updateDrone = null;
        //when
        Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(updateDroneSeq);
        if (optionalDrone.isPresent()) {
            timestamp = new Timestamp(System.currentTimeMillis());
            updateDrone = optionalDrone.get();
            //TODO drone Device name 직접 넣은 이유??
            if (updateDroneInfo.getDroneDeviceName() != null) {
                updateDrone.setDroneDeviceName(updateDroneInfo.getDroneDeviceName());
            }
            updateDrone.setUserId(updateDroneInfo.getUserId());
            updateDrone.setUpdateDt(timestamp);

        }

        //then
        Optional<Drone> optionalFindDrone = droneRepository.findByDroneSeq(updateDroneSeq);
        Drone findDrone = null;
        if (optionalFindDrone.isPresent())
            findDrone = optionalFindDrone.get();
        assertThat(findDrone.getDroneDeviceName()).isEqualTo(updateDrone.getDroneDeviceName());
        assertThat(findDrone.getUserId()).isEqualTo(updateDrone.getUserId());

    }

    @Test
    @Transactional //transcational을 걸어야 테스트 끝나고 데이터 초기화
    public void saveDrone() {

        String droneDeviceName = "TestDrone1";
        Drone inputDrone = new Drone();
        inputDrone.setDroneDeviceName(droneDeviceName);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        inputDrone.setUpdateDt(timestamp);
        droneRepository.save(inputDrone);

        Drone findDrone = droneRepository.findByDroneDeviceName(droneDeviceName);
        assertThat(findDrone.getUpdateDt()).isEqualTo(inputDrone.getUpdateDt());

    }

    @Test
    @Transactional
    public void deleteDrone() {

        //given

        String droneDeviceName = "TestDrone2";
        Drone inputDrone = Drone.builder().droneDeviceName(droneDeviceName).build();
        droneRepository.save(inputDrone);

        //when
        Drone findSeqDrone = droneRepository.findByDroneDeviceName(droneDeviceName);
        Optional<Drone> optionalFindDrone = droneRepository.findByDroneSeq(findSeqDrone.getDroneSeq());
        int deleteIdCount = 0;
        if (optionalFindDrone.isPresent())
            deleteIdCount = droneRepository.deleteByDroneSeq(findSeqDrone.getDroneSeq());

        //then
        assertThat(deleteIdCount).isEqualTo(1);

    }


    /**
     * {"droneDeviceName":"" ,"userId":"","droneStatus":""} 드론이름 검색 & 드론 상태로 검색 or
     * *                     {"droneDeviceName":"" ,"userId":""} 드론 이름으로 검색 or
     * *                     {"userId":""} 드론 관리자로 조회 or
     * *                     {"droneDeviceName":"" } 드론 이름으로 검색
     */
    @Test
    @Transactional
    public void findDroneList대기중과userId는user로검색() {
        //given
        Timestamp timestamp = new Timestamp(23123132L);

        String droneDeviceName1 = "TestDrone1";
        String status1 = "대기중";
        Drone inputDrone1 = Drone.builder()
                .droneDeviceName(droneDeviceName1)
                .armStatus(3)
                .status(status1)
                .userId("userA")
                .updateDt(timestamp)
                .build();

        String droneDeviceName2 = "TestDrone2";
        String status2 = "대기중";
        timestamp = new Timestamp(23123133L);
        Drone inputDrone2 = Drone.builder()
                .droneDeviceName(droneDeviceName2)
                .status(status2)
                .userId("userB")
                .armStatus(1)
                .updateDt(timestamp)
                .build();


        String droneDeviceName3 = "TestDroneC";
        String status3 = "대기중";
        timestamp = new Timestamp(23123131L);
        Drone inputDrone3 = Drone.builder()
                .droneDeviceName(droneDeviceName3)
                .status(status3)
                .userId("userC")
                .updateDt(timestamp)
                .armStatus(2)
                .build();


        droneRepository.save(inputDrone1);
        droneRepository.save(inputDrone2);
        droneRepository.save(inputDrone3);

        DroneRequest droneRequest = new DroneRequest();
        droneRequest.setDroneStatus("wait");
        droneRequest.setUserId("user");

        //when

        List<Drone> droneList = null;
        Sort sort = Sort.by(Sort.Direction.DESC, "updateDt");

        String droneStatus = null;
        if (droneRequest.getDroneStatus() != null) {
            if (droneRequest.getDroneStatus().equals("all")) {
                droneStatus = "대기중";
            } else if (droneRequest.getDroneStatus().equals("wait")) {
                droneStatus = "대기중";
            } else if (droneRequest.getDroneStatus().equals("run")) {
                droneStatus = "운영중";
            }
            if (droneRequest.getUserId() != null) {
                //Todo Specification으로 전환 필요
                droneList = droneRepository.findAllByUserIdLikeAndStatus("%" + droneRequest.getUserId() + "%", droneStatus, sort);

            } else if (droneRequest.getDroneDeviceName() != null) {
                droneList = droneRepository.findAllByDroneDeviceNameLikeAndStatus("%" + droneRequest.getDroneDeviceName() + "%",
                        droneStatus, sort);
            }

        } else {
            if (droneRequest.getUserId() != null) {
                droneList = droneRepository.findAllByUserIdLike("%" + droneRequest.getUserId() + "%", sort);

            } else if (droneRequest.getDroneDeviceName() != null) {
                droneList = droneRepository.findAllByDroneDeviceNameLike("%" + droneRequest.getDroneDeviceName() + "%", sort);
            }
        }


        // log.info("droneStatus={}", droneStatus);


        //then


        //sorting
        assertThat(droneList.get(0).getDroneDeviceName()).isEqualTo(droneDeviceName2);
        assertThat(droneList.get(1).getDroneDeviceName()).isEqualTo(droneDeviceName1);
        assertThat(droneList.get(2).getDroneDeviceName()).isEqualTo(droneDeviceName3);


        //data
        List<String> droneDeviceNameList = new ArrayList<>();
        droneDeviceNameList.add(droneDeviceName1);
        droneDeviceNameList.add(droneDeviceName2);
        droneDeviceNameList.add(droneDeviceName3);

        droneList.forEach(drone -> {
            assertThat(droneDeviceNameList.contains(drone.getDroneDeviceName())).isEqualTo(true);
        });
    }



    @Test
    @Transactional
    public void findDroneList전체와userId는user로검색() {
        //given
        Timestamp timestamp = new Timestamp(23123132L);

        String droneDeviceName1 = "TestDrone1";
        String status1 = "대기중";
        Drone inputDrone1 = Drone.builder()
                .droneDeviceName(droneDeviceName1)
                .armStatus(3)
                .status(status1)
                .userId("userA")
                .updateDt(timestamp)
                .build();

        String droneDeviceName2 = "TestDrone2";
        String status2 = "운영중";
        timestamp = new Timestamp(23123133L);
        Drone inputDrone2 = Drone.builder()
                .droneDeviceName(droneDeviceName2)
                .status(status2)
                .userId("userB")
                .armStatus(1)
                .updateDt(timestamp)
                .build();


        String droneDeviceName3 = "TestDroneC";
        String status3 = "대기중";
        timestamp = new Timestamp(23123131L);
        Drone inputDrone3 = Drone.builder()
                .droneDeviceName(droneDeviceName3)
                .status(status3)
                .userId("userC")
                .updateDt(timestamp)
                .armStatus(2)
                .build();


        droneRepository.save(inputDrone1);
        droneRepository.save(inputDrone2);
        droneRepository.save(inputDrone3);

        DroneRequest droneRequest = new DroneRequest();
        droneRequest.setUserId("user");

        //when

        List<Drone> droneList = null;
        Sort sort = Sort.by(Sort.Direction.DESC, "updateDt");

        String droneStatus = null;
        if (droneRequest.getDroneStatus() != null) {
            if (droneRequest.getDroneStatus().equals("wait")) {
                droneStatus = "대기중";
            } else if (droneRequest.getDroneStatus().equals("run")) {
                droneStatus = "운영중";
            }
            if (droneRequest.getUserId() != null) {
                //Todo Specification으로 전환 필요
                droneList = droneRepository.findAllByUserIdLikeAndStatus("%" + droneRequest.getUserId() + "%", droneStatus, sort);

            } else if (droneRequest.getDroneDeviceName() != null) {
                droneList = droneRepository.findAllByDroneDeviceNameLikeAndStatus("%" + droneRequest.getDroneDeviceName() + "%",
                        droneStatus, sort);
            }

        } else {
            if (droneRequest.getUserId() != null) {
                droneList = droneRepository.findAllByUserIdLike("%" + droneRequest.getUserId() + "%", sort);

            } else if (droneRequest.getDroneDeviceName() != null) {
                droneList = droneRepository.findAllByDroneDeviceNameLike("%" + droneRequest.getDroneDeviceName() + "%", sort);
            }
        }


        // log.info("droneStatus={}", droneStatus);


        //then


        //sorting
        assertThat(droneList.get(0).getDroneDeviceName()).isEqualTo(droneDeviceName2);
        assertThat(droneList.get(1).getDroneDeviceName()).isEqualTo(droneDeviceName1);
        assertThat(droneList.get(2).getDroneDeviceName()).isEqualTo(droneDeviceName3);


        //data
        List<String> droneDeviceNameList = new ArrayList<>();
        droneDeviceNameList.add(droneDeviceName1);
        droneDeviceNameList.add(droneDeviceName2);
        droneDeviceNameList.add(droneDeviceName3);

        droneList.forEach(drone -> {
            assertThat(droneDeviceNameList.contains(drone.getDroneDeviceName())).isEqualTo(true);
        });


    }
}