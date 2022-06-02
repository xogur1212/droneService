package com.xy124.drone.service;


import com.xy124.drone.model.*;
import com.xy124.drone.model.dto.Result;
import com.xy124.drone.model.dto.request.DroneDetailRequest;
import com.xy124.drone.model.dto.ResultCode;
import com.xy124.drone.repository.*;
import com.xy124.drone.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DroneDetailsService implements IDroneDetailsService{

    private final DroneRepository droneRepository;
    private final DroneDetailsRepository droneDetailsRepository;
    private final DroneBaseRepository droneBaseRepository;
    private final MissionRepository missionRepository;
    private final DroneInMissionRepository droneInMissionRepository;

    @Transactional
    @Override
    public DroneDetails saveDroneDetails(DroneDetails droneDetails, Long droneId) {
        Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(droneId);

        if (!optionalDrone.isPresent()) {
            return null;
        }
        droneDetailsRepository.save(droneDetails);
        Drone drone = optionalDrone.get();
        drone.setDroneDetails(droneDetails);
        return droneDetails;

    }

    @Transactional
    @Override
    public Result updateDroneDetails(DroneDetailRequest droneDetailRequest) {

        Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(droneDetailRequest.getDroneSeq());
        Drone updateDrone = null;

        if (!optionalDrone.isPresent())
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        else
            updateDrone = optionalDrone.get();


        updateDrone.setStatus(droneDetailRequest.getDroneStatus());
        updateDrone.setSocketIndex(droneDetailRequest.getDroneSocket());

        Optional<DroneBase> optionalDroneBase = droneBaseRepository.findByDroneBaseSeq(droneDetailRequest.getDroneBaseSeq());
        DroneBase droneBase = null;

        if (!optionalDroneBase.isPresent()) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        } else {
            droneBase = optionalDroneBase.get();
        }

        updateDrone.setDroneBase(droneBase);

        DroneDetails updateDroneDetails = this.findDroneDetails(updateDrone.getDroneDetails().getDroneDetailsSeq());


        if (updateDroneDetails == null) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        }

        DroneDetails droneDetails = droneDetailRequest.getDroneDetails();
        //TODO droneDetails가 입력 안됫을떄 처리 ? 해야하는지 ?

        if (droneDetails.getMasterManager() != null) {
            updateDroneDetails.setMasterManager(droneDetails.getMasterManager());
        }
        if (droneDetails.getSubManager() != null) {
            updateDroneDetails.setSubManager(droneDetails.getSubManager());
        }
        if (droneDetails.getMasterManager() != null) {
            updateDroneDetails.setManufacturer(droneDetails.getManufacturer());
        }
        if (droneDetails.getType() != null) {
            updateDroneDetails.setType(droneDetails.getType());
        }
        if (droneDetails.getWeight() != 0) {
            updateDroneDetails.setWeight(droneDetails.getWeight());
        }
        if (droneDetails.getMaximumOperatingDistance() != 0) {
            updateDroneDetails.setMaximumOperatingDistance(droneDetails.getMaximumOperatingDistance());
        }
        if (droneDetails.getMaximumManagementAltitude() != 0) {
            updateDroneDetails.setMaximumManagementAltitude(droneDetails.getMaximumManagementAltitude());
        }
        if (droneDetails.getMaximumOperatingSpeed() != 0) {
            updateDroneDetails.setMaximumOperatingSpeed(droneDetails.getMaximumOperatingSpeed());
        }
        if (droneDetails.getSimNumber() != null) {
            updateDroneDetails.setSimNumber(droneDetails.getSimNumber());
        }
        if (droneDetails.getMaximumSpeed() != 0) {
            updateDroneDetails.setMaximumSpeed(droneDetails.getMaximumSpeed());
        }
        if (droneDetails.getManufacturer() != null) {
            updateDroneDetails.setManufacturer(droneDetails.getManufacturer());
        }
        if (droneDetails.getThumbnailImg() != null) {
            updateDroneDetails.setThumbnailImg(droneDetails.getThumbnailImg());
        }
        if (droneDetails.getSize1() != 0) {
            updateDroneDetails.setSize1(droneDetails.getSize1());
        }
        if (droneDetails.getSize2() != 0) {
            updateDroneDetails.setSize2(droneDetails.getSize2());
        }
        if (droneDetails.getSize3() != 0) {
            updateDroneDetails.setSize3(droneDetails.getSize3());
        }
        if (droneDetails.getOperationTemperatureRangeMin() != 0f) {
            updateDroneDetails.setOperationTemperatureRangeMin(droneDetails.getOperationTemperatureRangeMin());
        }
        if (droneDetails.getOperationTemperatureRangeMax() != 0f) {
            updateDroneDetails.setOperationTemperatureRangeMax(droneDetails.getOperationTemperatureRangeMax());
        }
        if (droneDetails.getMaximumOperatingWeight() != 0) {
            updateDroneDetails.setMaximumOperatingWeight(droneDetails.getMaximumOperatingWeight());
        }
        if (updateDroneDetails.getInsertDt() == null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            updateDroneDetails.setInsertDt(timestamp);
        }

        Optional<Mission> optionalMission = missionRepository.findByMissionSeq(droneDetailRequest.getMissionSeq());

        Mission mission = null;
        if (!optionalMission.isPresent())
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        else {
            mission = optionalMission.get();
//            mission.setDroneId(1); ?? 왜 1로 설정했찌?
            DroneInMission droneInMission = new DroneInMission();
            droneInMission.setDrone(updateDrone);
            droneInMission.setMission(mission);

            if (droneInMissionRepository.findByDrone(updateDrone) != null) {
                droneInMissionRepository.deleteByDrone(updateDrone);
            }
            droneInMissionRepository.save(droneInMission);

        }
        return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
    }
    @Override
    public DroneDetails findDroneDetails(Long droneDetailsSeq) {

        DroneDetails droneDetails = droneDetailsRepository.findByDroneDetailsSeq(droneDetailsSeq);
        return droneDetails;
    }

    @Transactional
    @Override
    public Result updateDroneDetailsFileName(String fileName, long droneSeq) {

        Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(droneSeq);
        Drone drone = null;

        if (!optionalDrone.isPresent()) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        } else {
            drone = optionalDrone.get();
        }

        DroneDetails updateDroneDetails = droneDetailsRepository.findByDroneDetailsSeq(drone.getDroneDetails().getDroneDetailsSeq());
        if (updateDroneDetails == null) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        }
        updateDroneDetails.setThumbnailImg(fileName);


        return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
    }
}
