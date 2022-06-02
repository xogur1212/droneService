package com.xy124.drone.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xy124.drone.model.Drone;
import com.xy124.drone.model.Mission;
import com.xy124.drone.model.MissionDetails;
import com.xy124.drone.model.dto.ResultCode;
import com.xy124.drone.model.dto.response.DroneMissionDetailsResponse;
import com.xy124.drone.model.dto.response.MissionDetailsDto;
import com.xy124.drone.service.IDroneService;
import com.xy124.drone.service.IMissionDetailsService;
import com.xy124.drone.service.IMissionService;
import com.xy124.drone.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/drone/api")
@Slf4j
@RequiredArgsConstructor
public class MissionCurdController {

    private final IMissionService missionService;
    private final IMissionDetailsService missionDetailsService;
    private final IDroneService droneService;

    /**
     * @param mission ex
     *                {"name":"미션테스트357",
     *                "userId":"test100"
     *                }
     * @return 생성된 mission의 seq
     */
    @PutMapping("/mission")
    @Description("Test O")
    public ResponseEntity<?> saveMission(@RequestBody Mission mission) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(missionService.saveMission(mission));

    }

    /**
     * @param mission 필수 : missionSeq
     * @return 바뀐 missionSeq
     * 해당 missionSeq에 mission이 없을경우 0 리턴
     */
    @PatchMapping("/mission")
    @Description("Test O")
    public ResponseEntity<?> updateMission(@RequestBody Mission mission) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(missionService.updateMission(mission));

    }


    /**
     * @param mission {"missionSeq":"1"}
     * @return 성공 실패 여부에 따른 resultCode
     */
    @DeleteMapping("/mission")
    @Description("연관관계 다 맺은상태에서 테스트 추가 필요")
    public ResponseEntity<?> deleteMission(@RequestBody Mission mission) {


        return ResponseEntity.status(HttpStatus.OK)
                .body(missionService.deleteMission(mission));

    }

    /**
     * @param id
     * @return 해당 미션이 있을경우
     * {
     * "missionSeq": 2,
     * "name": "미션테스트358",
     * "userId": "test100",
     * "droneId": 0,
     * "updateDt": "2022-05-28 20:45:10",
     * "totalDistance": 0,
     * "estimatedTime": 0,
     * "missionDetails": []
     * }
     * 해당 미션이 없을경우
     * null 리턴
     */
    @GetMapping("/mission/{id}")
    @Description("missionDetails 까지 넣은 상태에서 테스트 필요 ")
    public ResponseEntity<?> findOneMission(@PathVariable Long id) {
        if (id != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(missionService.findOneMission(id));
        }
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(ResultUtil.makeSuccessResult(ResultCode.FAIL));
    }

    /**
     * @param paramMap {"droneId": "0" ,"name": ""} or
     *                 {"name" :""}
     *                 {"adminUserId": ""} or
     *                 {"droneId" :"0" ,"adminUserId : ""}
     * @return 조회한 리스트 배열로 출력
     * [
     * {
     * "missionSeq": 2,
     * "name": "미션테스트358",
     * "userId": "test100",
     * "droneId": 0,
     * "updateDt": "2022-05-28 20:45:10",
     * "totalDistance": 0,
     * "estimatedTime": 0,
     * "missionDetails": []
     * }
     * ]
     */
    @PostMapping("/mission")
    @Description("droneId adminUserId 넣었을때 에러 떨어지는 것 수정해야됨")
    public ResponseEntity<?> findMissionList(@RequestBody Map<String, Object> paramMap) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(missionService.findMissionList(paramMap));

    }

    /**
     * @param missionList {
     *                    "missionList":[
     *                    {
     *                    "index":0,
     *                    "name":"takeoff",
     *                    "alt":105.55
     *                    <p>
     *                    <p>
     *                    },
     *                    {
     *                    "index":1,
     *                    "name":"waypoint",
     *                    "gpsY":37.4456813,
     *                    "gpsX":126.8917251,
     *                    "yaw":360,
     *                    "alt":100,
     *                    "speed":5
     *                    <p>
     *                    },
     *                    {
     *                    "index":2,
     *                    "name":"waypoint",
     *                    "gpsY":37.4435347,
     *                    "gpsX":126.8931842,
     *                    "alt":100,
     *                    "speed":5
     *                    <p>
     *                    },
     *                    {
     *                    "index":3,
     *                    "name":"loiter",
     *                    "gpsY":37.4435347,
     *                    "gpsX":126.8931842,
     *                    "alt":100,
     *                    "radius":10,
     *                    "time":30
     *                    <p>
     *                    },
     *                    {
     *                    "index":4,
     *                    "name":"return",
     *                    "gpsY":37.4435347,
     *                    "gpsX":126.8931842,
     *                    "alt":100
     *                    <p>
     *                    }
     *                    ],
     *                    "missionId":3,
     *                    "totalDistance" :1500
     *                    }
     * @return resultCode
     */
    @PutMapping("/mission-details")
    public ResponseEntity<?> saveMissionDetails(@RequestBody Map<String, Object> missionList) {


        ObjectMapper objectMapper = new ObjectMapper();
        List<MissionDetails> missionDetailsList =
                objectMapper.convertValue(missionList.get("missionList"), new TypeReference<List<MissionDetails>>() {
                });
        //  missionDetailsList = (List<MissionDetails>) missionList.get("missionList");

        int missionId = 0;
        missionId = Integer.parseInt(missionList.get("missionId").toString());

        double totalDistance = 0;
        totalDistance = Double.parseDouble(missionList.get("totalDistance").toString());


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(missionDetailsService.saveMission(missionDetailsList, missionId, totalDistance));

    }


//    @GetMapping("/mission-details/{name}")
//    public ResponseEntity<?> findOneMissionDetails(@PathVariable final String name) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(missionDetailsService.findOneMissionDetails(name));
//    }

    /**
     * @param paramMap {"missionSeq":3} or
     *                 {"name":"미션테스트3518"}
     * @return 해당하는 missionDetails 가 없는 경우
     * fail resultCode
     * 있을 경우
     * [
     * {
     * "missionDetailsSeq": 36,
     * "name": "takeoff",
     * "index": 0,
     * "gpsX": 0.0,
     * "gpsY": 0.0,
     * "alt": 105,
     * "speed": 0,
     * "time": 0,
     * "yaw": 0.0,
     * "radius": 0,
     * "koName": "이륙"
     * },
     * {
     * "missionDetailsSeq": 37,
     * "name": "waypoint",
     * "index": 1,
     * "gpsX": 126.8917251,
     * "gpsY": 37.4456813,
     * "alt": 100,
     * "speed": 5,
     * "time": 0,
     * "yaw": 360.0,
     * "radius": 0,
     * "koName": "경유지"
     * },
     * {
     * "missionDetailsSeq": 38,
     * "name": "waypoint",
     * "index": 2,
     * "gpsX": 126.8931842,
     * "gpsY": 37.4435347,
     * "alt": 100,
     * "speed": 5,
     * "time": 0,
     * "yaw": 0.0,
     * "radius": 0,
     * "koName": "경유지"
     * },
     * {
     * "missionDetailsSeq": 39,
     * "name": "loiter",
     * "index": 3,
     * "gpsX": 126.8931842,
     * "gpsY": 37.4435347,
     * "alt": 100,
     * "speed": 0,
     * "time": 30,
     * "yaw": 0.0,
     * "radius": 10,
     * "koName": "로이터"
     * },
     * {
     * "missionDetailsSeq": 40,
     * "name": "return",
     * "index": 4,
     * "gpsX": 126.8931842,
     * "gpsY": 37.4435347,
     * "alt": 100,
     * "speed": 0,
     * "time": 0,
     * "yaw": 0.0,
     * "radius": 0,
     * "koName": "귀환"
     * }
     * ]
     */
    @PostMapping("/mission-details")
    public ResponseEntity<?> findMissionDetailsList(@RequestBody Map<String, Object> paramMap) {


        List<MissionDetails> missionDetailsList = missionDetailsService.findMissionDetailsList(paramMap);
        if (missionDetailsList == null) {
            //TODO 없는걸 fail이라고 봐야될까?? nodata resultcode가 하나더 필요하다
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResultUtil.makeSuccessResult(ResultCode.FAIL));
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(missionDetailsList);

    }

    /**
     * @return 총 미션 갯수
     */

    @GetMapping("/mission-count")
    public ResponseEntity<?> getMissionCount() {
        List<Mission> missionList = missionService.findAllMission();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(missionList.size());
    }

    /**
     * @return [
     * {
     * "id": 6,
     * "droneDeviceName": "드론11",
     * "mission": {
     * "id": null,
     * "name": null,
     * "userId": null,
     * "updateDt": null,
     * "totalDistance": 0,
     * "estimatedTime": 0,
     * "missionDetails": []
     * },
     * "droneBase": "드론기지1"
     * }
     * ]
     */
    @PostMapping("/drone-mission-details")
    @Transactional
    //TODO DroneMissionDEtailsResponse에서 다른걸로 변환 할것
    public ResponseEntity<?> getListDroneMissionDetails() {

        List<Drone> droneList = droneService.findAllDrone();
        List<DroneMissionDetailsResponse> droneMissionDetailsResponses = droneList.stream().map(DroneMissionDetailsResponse::new).collect(Collectors.toList());

        droneMissionDetailsResponses.forEach(r -> {
            if (r.getMission() != null)
                r.getMission()
                        .setMissionDetails(r.getMission()
                                .getMissionDetails()
                                .stream()
                                .sorted(Comparator.comparing((MissionDetailsDto d) -> d.getIndex()))
                                .collect(Collectors.toList())
                        );
        });


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(droneMissionDetailsResponses);

    }


}
