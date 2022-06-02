package com.xy124.drone.service;

import com.xy124.drone.model.Drone;
import com.xy124.drone.model.dto.Result;
import com.xy124.drone.model.dto.ResultCode;
import com.xy124.drone.model.dto.request.DroneRequest;
import com.xy124.drone.repository.DroneRepository;
import com.xy124.drone.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DroneService implements IDroneService {

    private final DroneRepository droneRepository;

    @Override
    public Drone findDrone(Drone drone) {

        if (drone.getDroneDeviceName() != null) {
            return droneRepository.findByDroneDeviceName(drone.getDroneDeviceName());
        } else if (drone.getDroneSeq() != null) {
            Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(drone.getDroneSeq());
            if (!optionalDrone.isPresent()) {
                return null;
            }
            return optionalDrone.get();
        } else {
            return null;
        }

    }

    @Override
    public Drone findOneDrone(Long droneId) {
        Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(droneId);
        if (!optionalDrone.isPresent())
            return null;
        Drone drone = optionalDrone.get();
        return drone;
    }

    @Override
    public List<Drone> findAllDrone() {

        return droneRepository.findAllByDroneSeqNot(0l);
    }

    @Override
    public Object getSocketDrone() {

//        ServerSocket server_socket = null;
//        try {
//            server_socket = new ServerSocket(8300);
//
//            int systemId = 1;
//            int componentId = 1;
//            int linkId = 1;
//            long timestamp = System.currentTimeMillis();
//
//            byte[] secretKey = new byte[0];
//            secretKey = MessageDigest.getInstance("SHA-256").digest("xy124".getBytes(StandardCharsets.UTF_8));
//            Socket socket = server_socket.accept();
//
//
//        } catch (IOException e) {
//            //TODO exception 처리를 어떻게 할건지??
//            log.info("해당 포트가 열려있습니다.");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }


        return null;
    }

    @Override
    @Transactional
    public Result updateDrone(Drone drone) {
        Optional<Drone> optionalDrone = droneRepository.findByDroneSeq(drone.getDroneSeq());
        if (!optionalDrone.isPresent()) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Drone updateDrone = optionalDrone.get();
        //TODO drone Device name 직접 넣은 이유??
        if (drone.getDroneDeviceName() != null) {
            updateDrone.setDroneDeviceName(drone.getDroneDeviceName());
        }
        updateDrone.setUserId(drone.getUserId());
        updateDrone.setUpdateDt(timestamp);

        return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
    }

    @Override
    public Result changeStatus(Drone drone, int armStatus) {
        return null;
    }

    @Override
    @Transactional
    public Result saveDrone(Drone drone) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        drone.setUpdateDt(timestamp);
        droneRepository.save(drone);


        return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
    }

    @Override
    @Transactional
    public Result deleteDrone(Drone drone) {


        Optional<Drone> optionalFindDrone = droneRepository.findByDroneSeq(drone.getDroneSeq());
        if(!optionalFindDrone.isPresent())
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);



        int deleteIdCount = droneRepository.deleteByDroneSeq(drone.getDroneSeq());

        if (deleteIdCount == 0) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        } else {
            return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
        }


    }


    @Override
    public List<?> findDroneList(DroneRequest droneRequest) {

        List<Drone> droneList = null;
        Sort sort = sortByUpdateDt();

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
                log.info("id로검색");
                //Todo Specification으로 전환 필요
                droneList = droneRepository.findAllByUserIdLikeAndStatus("%" + droneRequest.getUserId() + "%", droneStatus, sort);

                droneList.forEach(r -> {
                    log.info("droneId={},{},{},{}", r.getUserId(), r.getDroneDeviceName(),
                            r.getDroneSeq(), r.getStatus());

                });
            } else if (droneRequest.getDroneDeviceName() != null) {
                log.info("devicename으로검색");
                droneList = droneRepository.findAllByDroneDeviceNameLikeAndStatus("%" + droneRequest.getDroneDeviceName() + "%",
                        droneStatus, sort);
            }

        } else {
            if (droneRequest.getUserId() != null) {
                log.info("id로검색");
                droneList = droneRepository.findAllByUserIdLike("%" + droneRequest.getUserId() + "%", sort);

            } else if (droneRequest.getDroneDeviceName() != null) {
                log.info("devicename으로검색");
                droneList = droneRepository.findAllByDroneDeviceNameLike("%" + droneRequest.getDroneDeviceName() + "%", sort);
            }
        }


        // log.info("droneStatus={}", droneStatus);


        if (droneList == null)
            return null;
        return droneList;
    }

    @Override
    public Sort sortByUpdateDt() {
        return Sort.by(Sort.Direction.DESC, "updateDt");
    }
}
