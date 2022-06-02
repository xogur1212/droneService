package com.xy124.drone.service;

import com.xy124.drone.model.Drone;
import com.xy124.drone.model.DroneInMission;
import com.xy124.drone.model.Mission;
import com.xy124.drone.model.dto.Result;
import com.xy124.drone.model.dto.ResultCode;
import com.xy124.drone.repository.DroneInMissionRepository;
import com.xy124.drone.repository.MissionDetailsRepository;
import com.xy124.drone.repository.MissionRepository;
import com.xy124.drone.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionService implements IMissionService{

    private final MissionRepository missionRepository;
    private final MissionDetailsRepository missionDetailsRepository;
    private final DroneInMissionRepository droneInMissionRepository;

    @Override
    public List<Mission> findAllMission() {
        return (List<Mission>) missionRepository.findAll();
    }

    @Override
    public Mission findOneMission(Long missionSeq) {

        Optional<Mission> optionalMission = missionRepository.findById(missionSeq);
        if (!optionalMission.isPresent()) {
            return null;
        }
        Mission mission = optionalMission.get();


        return mission;
    }

    private Sort sortByupdateDt() {
        return Sort.by(Sort.Direction.DESC, "updateDt");
    }

    @Override
    public List<Mission> findMissionList(Map<String, Object> paramMap) {

        List<Mission> missionList = null;
        Sort sort = sortByupdateDt();
        Long id = null;
        String name = null;

        if (paramMap.get("missionName") != null) {
            name = paramMap.get("missionName").toString();
        }
        if (paramMap.get("name") != null) {
            name = paramMap.get("name").toString();
        }
        if (paramMap.get("droneId") != null && !paramMap.get("droneId").equals("")) {

            id = Long.parseLong(paramMap.get("droneId").toString());
        }



        if (name != null) {
            if (id == null) {

                missionList = missionRepository.findAllByNameLike("%" + name + "%", sort);
            } else {
                Drone drone = new Drone();
                if (id == 0l) {
                    missionList = missionRepository.findAllByNameLikeAndDroneId("%" + name + "%", id.intValue(), sort);
                } else {
                    missionList = missionRepository.findAllByNameLikeAndDroneIdNot("%" + name + "%", 0, sort);
                }
            }
        }
        //TODO 수정 필요 adminUserId 와 droneId 같이 넣었을때 에러 나는것 수정 필요
        if (paramMap.get("adminUserId") != null) {
            String userId = paramMap.get("adminUserId").toString();
            if (id == null) {
                missionList = missionRepository.findAllByUserIdLike("%" + userId + "%", sort);
            } else {

                Drone drone = new Drone();
                //TODO if else 문 수정 필요 위 아래가 똑같은 얘기임ㄷ ;
                if (id == 0l) {
                    drone.setDroneSeq(id);
                    DroneInMission droneInMission = new DroneInMission();
                    droneInMission.setDrone(drone);
                    missionList = missionRepository.findAllByUserIdLikeAndDroneInMission("%" + userId + "%", droneInMission, sort);

                } else {
                    drone.setDroneSeq(0l);
                    DroneInMission droneInMission = new DroneInMission();
                    droneInMission.setDrone(drone);
                    missionList = missionRepository.findAllByUserIdLikeAndDroneInMissionNot("%" + userId + "%", droneInMission, sort);
                }
            }
        }


        if (missionList == null) {
            return null;
        }


        return missionList;
    }

    @Override
    public Long saveMission(Mission mission) {

        Mission findMission = missionRepository.findByName(mission.getName());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        mission.setUpdateDt(timestamp);
        if (findMission == null) {
            missionRepository.save(mission);
            return mission.getMissionSeq();
        }
        return 0l;

    }

    @Transactional
    @Override
    public int updateMission(Mission mission) {

        Optional<Mission> optionalMission = missionRepository.findByMissionSeq(mission.getMissionSeq());
        if (!optionalMission.isPresent())
            return 0;
        Mission updateMission = optionalMission.get();
        if (updateMission.getName() != null) {
            updateMission.setName(mission.getName());
            updateMission.setUserId(mission.getUserId());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            updateMission.setUpdateDt(timestamp);

        }

        return updateMission.getMissionSeq().intValue();
    }

    @Transactional
    public Result deleteMission(Mission mission) {

        Optional<Mission> optionalMission = missionRepository.findByMissionSeq(mission.getMissionSeq());

        if (!optionalMission.isPresent()) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        }

        Long deleteResult = missionDetailsRepository.deleteByMission(mission);

        droneInMissionRepository.deleteDroneInMission(mission.getMissionSeq());
        missionRepository.deleteByMissionSeq(mission.getMissionSeq());


        return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
    }
}
