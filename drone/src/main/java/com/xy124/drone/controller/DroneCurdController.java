package com.xy124.drone.controller;

import com.xy124.drone.model.Drone;
import com.xy124.drone.model.DroneBase;
import com.xy124.drone.model.DroneDetails;
import com.xy124.drone.model.dto.Result;
import com.xy124.drone.model.dto.ResultCode;
import com.xy124.drone.model.dto.request.DroneDetailRequest;
import com.xy124.drone.model.dto.request.DroneRequest;
import com.xy124.drone.service.IDroneDetailsService;
import com.xy124.drone.service.IDroneService;
import com.xy124.drone.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drone/api")
@RequiredArgsConstructor
public class DroneCurdController {


    private final IDroneService droneService;
    private final IDroneDetailsService droneDetailsService;

    /**
     * @param {"droneDeviceName":"드론10"}
     * @return 생성한 드론에 DroneDetails
     */
    @PutMapping("/drone")
    @Description("Test O")
    public ResponseEntity<?> saveDrone(@RequestBody Drone drone) {


        Drone findDrone = droneService.findDrone(drone);


        if (findDrone != null) {


            Result result = ResultUtil.makeSuccessResult(ResultCode.FAIL);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(result);
        }

        DroneBase droneBase = new DroneBase();
        droneBase.setDroneBaseSeq(1l);
        drone.setDroneBase(droneBase);
        drone.setStatus("임시저장");

        droneService.saveDrone(drone);
        DroneDetails droneDetails = new DroneDetails();
        DroneDetails saveDroneDetails = droneDetailsService.saveDroneDetails(droneDetails, drone.getDroneSeq());
        if (saveDroneDetails == null) {
            Result result = ResultUtil.makeSuccessResult(ResultCode.FAIL);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(result);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saveDroneDetails);


    }

    /**
     * @param { "droneDeviceName":"abc",
     *          "userId":5,
     *          "droneSeq":2
     *          }
     * @return code ,message
     */
    @PatchMapping("/drone")
    @Description("Test O")
    public ResponseEntity<?> updateDrone(@RequestBody Drone drone) {


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(droneService.updateDrone(drone));
    }

    /**
     * @param {droneSeq":4}
     * @return code ,message
     */
    @DeleteMapping("/drone")
    @Description("Test O")
    public ResponseEntity<?> deleteDrone(@RequestBody Drone drone) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(droneService.deleteDrone(drone));
    }

    /**
     * @param droneRequest {"droneDeviceName":"" ,"userId":"","droneStatus":""} 드론이름 검색 & 드론 상태로 검색 or
     *                     {"droneDeviceName":"" ,"userId":""} 드론 이름으로 검색 or
     *                     {"userId":""} 드론 관리자로 조회 or
     *                     {"droneDeviceName":"" } 드론 이름으로 검색
     * @return [
     * {
     * "droneSeq": 6,
     * "droneDeviceName": "드론11",
     * "userId": "admin",
     * "socketIndex": null,
     * "updateDt": "2022-05-26 23:58:04",
     * "armStatus": 0,
     * "droneDetails": {
     * "droneDetailsSeq": 1,
     * "size": null,
     * "weight": 0.0,
     * "maximumOperatingDistance": 0.0,
     * "operationTemperatureRangeMin": 0.0,
     * "operationTemperatureRangeMax": 0.0,
     * "simNumber": null,
     * "masterManager": null,
     * "subManager": null,
     * "manufacturer": null,
     * "type": null,
     * "maximumManagementAltitude": 0,
     * "maximumOperatingSpeed": 0,
     * "maximumSpeed": 0,
     * "insertUserId": null,
     * "insertDt": null,
     * "updateUserId": null,
     * "updateDt": null,
     * "thumbnailImg": null,
     * "thumbnailRealImg": null,
     * "size1": 0,
     * "size2": 0,
     * "size3": 0,
     * "maximumOperatingWeight": 0,
     * "flightTime": 0
     * },
     * "status": "임시저장",
     * "droneInMission": null,
     * "droneBase": {
     * "droneBaseSeq": 1,
     * "baseName": "드론기지1"
     * }
     * }
     * ]
     */
    @PostMapping("/drone")
    @Description("Test 50% status가 있을때 테스트 다시 해볼것")
    public ResponseEntity<?> findAllDrone(@RequestBody DroneRequest droneRequest) {

        List<?> droneList = droneService.findDroneList(droneRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(droneList);

    }

    /**
     *
     * @param droneId
     * @return
     * {
     *     "droneSeq": 6,
     *     "droneDeviceName": "드론11",
     *     "userId": "admin",
     *     "socketIndex": null,
     *     "updateDt": "2022-05-26 23:58:04",
     *     "armStatus": 0,
     *     "droneDetails": {
     *         "droneDetailsSeq": 1,
     *         "size": null,
     *         "weight": 0.0,
     *         "maximumOperatingDistance": 0.0,
     *         "operationTemperatureRangeMin": 0.0,
     *         "operationTemperatureRangeMax": 0.0,
     *         "simNumber": null,
     *         "masterManager": null,
     *         "subManager": null,
     *         "manufacturer": null,
     *         "type": null,
     *         "maximumManagementAltitude": 0,
     *         "maximumOperatingSpeed": 0,
     *         "maximumSpeed": 0,
     *         "insertUserId": null,
     *         "insertDt": null,
     *         "updateUserId": null,
     *         "updateDt": null,
     *         "thumbnailImg": null,
     *         "thumbnailRealImg": null,
     *         "size1": 0,
     *         "size2": 0,
     *         "size3": 0,
     *         "maximumOperatingWeight": 0,
     *         "flightTime": 0
     *     },
     *     "status": "임시저장",
     *     "droneInMission": null,
     *     "droneBase": {
     *         "droneBaseSeq": 1,
     *         "baseName": "드론기지1"
     *     }
     * }
     */
    @GetMapping("/drone/{droneId}")
    @Description("Test O")
    public ResponseEntity<?> findOneDrone(@PathVariable long droneId) {
        Drone findDrone = droneService.findOneDrone(droneId);
        if (findDrone == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(ResultUtil.makeSuccessResult(ResultCode.FAIL));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findDrone);
    }

    /**
     *
     * @param droneDetailRequest
     *    Long droneSeq;
     *     DroneDetails droneDetails;
     *     Long droneBaseSeq;
     *     Long missionSeq;
     *     String droneStatus;
     *     Long droneSocket;
     * @return 성공 실패 여부 코드 
     */
    
    //TODO 성공 실패 말고도 그외 다른 코드가 필요함
    @PatchMapping("/drone-details")
    @Description("mission curd 작업이후에 추가로 테스트 필요")
    public ResponseEntity<?> updateDroneDetails(@RequestBody DroneDetailRequest droneDetailRequest){


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(droneDetailsService.updateDroneDetails(droneDetailRequest));
    }



}
