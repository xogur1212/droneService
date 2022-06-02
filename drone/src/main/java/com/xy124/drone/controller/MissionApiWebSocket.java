package com.xy124.drone.controller;


import com.xy124.drone.enumdata.LoggingMapEnum;
import com.xy124.drone.manager.MapManager;
import com.xy124.drone.model.Drone;
import com.xy124.drone.model.DroneLog;
import com.xy124.drone.model.Mission;
import com.xy124.drone.model.MissionDetails;
import com.xy124.drone.service.*;
import com.xy124.drone.socket.CustomServerSocket;
import com.xy124.drone.util.Flight;
import io.dronefleet.mavlink.common.MavCmd;
import io.dronefleet.mavlink.common.MavFrame;
import io.dronefleet.mavlink.common.MavMissionType;
import io.dronefleet.mavlink.common.MissionItemInt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/drone/api")
@Slf4j
@RequiredArgsConstructor
public class MissionApiWebSocket {


    private final IDroneLogService droneLogService;
    private final IDroneService droneService;
    private final CustomServerSocket customServerSocket;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final IDroneLogDetailsService droneLogDetailsService;
    private final IConnectionService connectionService;
    private final MapManager mapManager;
    private final int MULTI_DOUBLE_TO_INT = 10000000;
    @MessageMapping("/logging")
    public void logging(Map<String, Object> paramMap) {

        int droneId = 0;

        if (paramMap.get("droneId") != null) {
            droneId = Integer.parseInt(paramMap.get("droneId").toString());
        }

        if (mapManager.getLoggingMap().getOrDefault(droneId, LoggingMapEnum.NO_DATA.getCode()) != LoggingMapEnum.IN_ACTION.getCode()) {
            mapManager.getLoggingMap().put(droneId, LoggingMapEnum.IN_ACTION.getCode());
            log.info("LoggingDroneId={}", droneId);
            AtomicBoolean alreadyStartMission = new AtomicBoolean(false);
            int finalDroneId = droneId;
            mapManager.getFlightMap().forEach((k, v) -> {
                if (finalDroneId == k)
                    alreadyStartMission.set(true);
            });
            if (alreadyStartMission.get() == false) {
                Flight flight = new Flight(customServerSocket, simpMessagingTemplate, droneLogDetailsService, droneService, connectionService, mapManager);
                mapManager.getFlightMap().put(droneId, flight);
            }

            HashMap<Integer, String> missionIndex = new HashMap<>();

            Drone drone = droneService.findOneDrone(Long.valueOf(droneId));
            Mission mission = drone.getDroneInMission().getMission();
            Iterator iterator = mission.getMissionDetails().iterator();
            HashMap<String, Integer> gpsZs = new HashMap<>();
            while (iterator.hasNext()) {
                MissionDetails missionDetails = (MissionDetails) iterator.next();
                if (missionDetails.getName().equals("takeoff")) {
                    gpsZs.put("takeoff", missionDetails.getAlt());
                    missionIndex.put(missionDetails.getIndex(), "takeoff");
                }
            }
            float takeOffAlt = gpsZs.getOrDefault("takeoff", 50);

            Flight flight = mapManager.getFlightMap().get(droneId);
            flight.logging(droneId, takeOffAlt);
            mapManager.getLoggingMap().put(droneId, LoggingMapEnum.POSSIBLE.getCode());

            alreadyStartMission.set(false);

        }
    }

    /**
     * @param paramMap {droneId : 귀환 시킬 드론 ID}
     */
    @MessageMapping("/return")
    public void returnDrone(Map<String, Object> paramMap) {
        int droneId = 0;
        if (paramMap.get("droneId") != null)
            droneId = Integer.parseInt(paramMap.get("droneId").toString());
        log.info("droneId={}", droneId);
        Flight flight = mapManager.getFlightMap().get(droneId);
        flight.returnDrone();
    }

    /**
     *
     * @param paramMap {
     *                 droneId :
     *                 gpsX :
     *                 gpsY :
     *                 alt :
     *                 yaw :
     *
     *
     * }
     */

    @MessageMapping("/waypoint")
    public void wayPointDrone(Map<String, Object> paramMap) {
        log.info("paramMap={}", paramMap);

        double gpsX = 0;
        double gpsY = 0;
        double gpsZ = 0;
        int intGpsX = 0;
        int intGpsY = 0;
        int intGpsZ = 0;
        int yaw = 0;
        int droneId = 0;
        if (paramMap.get("droneId") != null)
            droneId = Integer.parseInt(paramMap.get("droneId").toString());
        log.info("droneId={}", droneId);
        if (paramMap.get("gpsX") != null)
            gpsX = Double.parseDouble(paramMap.get("gpsX").toString()) * MULTI_DOUBLE_TO_INT;
        if (paramMap.get("gpsY") != null)
            gpsY = Double.parseDouble(paramMap.get("gpsY").toString()) * MULTI_DOUBLE_TO_INT;
        if (paramMap.get("alt") != null && !paramMap.get("alt").equals("")) {
            gpsZ = Double.parseDouble(paramMap.get("alt").toString());
            intGpsZ = (int) gpsZ;
        }
        if (paramMap.get("yaw") != null)
            yaw = Integer.parseInt(paramMap.get("yaw").toString());
        intGpsX = (int) gpsX;
        intGpsY = (int) gpsY;

        Flight flight = mapManager.getFlightMap().get(droneId);
        flight.wayPoint(intGpsX, intGpsY, intGpsZ, yaw);


    }

    /**
     *
     * @param paramMap {
     *                 droneId:
     *                 yaw:
     * }
     */
    @MessageMapping("/change-yaw")
    public void changeYaw(@RequestBody Map<String, Object> paramMap) {

        int yaw = 0;
        int droneId = 0;
        if (paramMap.get("yaw") != null)
            yaw = Integer.parseInt(paramMap.get("yaw").toString());
        if (paramMap.get("droneId") != null)
            droneId = Integer.parseInt(paramMap.get("droneId").toString());
        log.info("droneId={}", droneId);
        Flight flight = mapManager.getFlightMap().get(droneId);
        flight.changeYaw(yaw);
    }

    /**
     *
     * @param paramMap{
     *                droneId:
     *                seq:
     *
     */
    @MessageMapping("/set-mission-current")
    public void setMissionCurrent(@RequestBody Map<String, Object> paramMap) {

        int seq = 0;
        int droneId = 0;
        if (paramMap.get("seq") != null)
            seq = Integer.parseInt(paramMap.get("seq").toString());
        if (paramMap.get("droneId") != null)
            droneId = Integer.parseInt(paramMap.get("droneId").toString());
        log.info("droneId={}", droneId);
        Flight flight = mapManager.getFlightMap().get(droneId);
        flight.setMissionCurrent(seq);
    }

    /**
     *
     * @param paramMap {
     *                 droneId :
     * }
     *
     */
    @MessageMapping("/start-mission")
    public void startMission(@RequestBody Map<String, Object> paramMap) {

        int droneId = 0;
        final String[] isEnd = {""};
        if (paramMap.get("droneId") != null)
            droneId = Integer.parseInt(paramMap.get("droneId").toString());

        Flight flight = mapManager.getFlightMap().get(droneId);
        if (flight.getEOFCheck() == 1) {
            flight.connect(droneId);
            flight.setEOFCheck(0);

        }
        Drone drone = droneService.findOneDrone(Long.valueOf(droneId));
        Mission mission = drone.getDroneInMission().getMission();
        DroneLog inputDroneLog = new DroneLog();

        inputDroneLog.setMissionName(mission.getName());
        String droneName = drone.getDroneDeviceName();
        inputDroneLog.setDroneDeviceName(droneName);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        inputDroneLog.setInsertDt(timestamp);
        DroneLog droneLog = droneLogService.saveDroneLog(inputDroneLog);

        int step = 0;
        int flag = 0;
        HashMap<Integer, String> missionIndex = new HashMap<>();
        HashMap<String, Integer> gpsXs = new HashMap<>();
        HashMap<String, Integer> gpsYs = new HashMap<>();
        HashMap<String, Integer> gpsZs = new HashMap<>();
        HashMap<String, Integer> speeds = new HashMap<>();
        HashMap<String, Integer> times = new HashMap<>();
        HashMap<String, Float> yaws = new HashMap<>();
        HashMap<String, MissionItemInt> missionMap = new HashMap<>();
        HashMap<String, Integer> radiusMap = new HashMap<>();
        Iterator iterator = mission.getMissionDetails().iterator();
        while (iterator.hasNext()) {
            MissionDetails missionDetails = (MissionDetails) iterator.next();
            if (missionDetails.getName().equals("takeoff")) {
                gpsZs.put("takeoff", missionDetails.getAlt());
                missionIndex.put(missionDetails.getIndex(), "takeoff");
            } else if (missionDetails.getName().equals("waypoint")) {
                missionIndex.put(missionDetails.getIndex(), "waypoint" + missionDetails.getIndex());

                gpsXs.put("waypoint" + missionDetails.getIndex(), (int) (missionDetails.getGpsX() * MULTI_DOUBLE_TO_INT));
                speeds.put("waypoint" + missionDetails.getIndex(), missionDetails.getSpeed());
                gpsYs.put("waypoint" + missionDetails.getIndex(), (int) (missionDetails.getGpsY() * MULTI_DOUBLE_TO_INT));
                gpsZs.put("waypoint" + missionDetails.getIndex(), missionDetails.getAlt());
                yaws.put("waypoint" + missionDetails.getIndex(), (float) (missionDetails.getYaw()));
                times.put("waypoint" + missionDetails.getIndex(), missionDetails.getTime());
            } else if (missionDetails.getName().equals("loiter")) {
                missionIndex.put(missionDetails.getIndex(), "loiter" + missionDetails.getIndex());

                times.put("loiter" + missionDetails.getIndex(), missionDetails.getTime());
                gpsXs.put("loiter" + missionDetails.getIndex(), (int) (missionDetails.getGpsX() * MULTI_DOUBLE_TO_INT));
                gpsYs.put("loiter" + missionDetails.getIndex(), (int) (missionDetails.getGpsY() * MULTI_DOUBLE_TO_INT));
                gpsZs.put("loiter" + missionDetails.getIndex(), missionDetails.getAlt());
                radiusMap.put("loiter" + missionDetails.getIndex(), missionDetails.getRadius());
            } else if (missionDetails.getName().equals("return")) {
                missionIndex.put(missionDetails.getIndex(), "return");

                gpsXs.put("return" + missionDetails.getIndex(), (int) (missionDetails.getGpsX() * MULTI_DOUBLE_TO_INT));
                speeds.put("return" + missionDetails.getIndex(), missionDetails.getSpeed());
                gpsYs.put("return" + missionDetails.getIndex(), (int) (missionDetails.getGpsY() * MULTI_DOUBLE_TO_INT));
                gpsZs.put("return" + missionDetails.getIndex(), missionDetails.getAlt());

            }
        }
        while (!missionIndex.getOrDefault(step, "finish").equals("finish")) {
            int x = 0;
            int y = 0;
            int z = 0;
            int time = 0;

            int radius = 0;
            float yaw = 0;

            //x,y 일부로 바꿔져있음 마브링크에서 사용하는 y x가 서로 반대라서;
            y = gpsXs.getOrDefault(missionIndex.get(step), 0);
            x = gpsYs.getOrDefault(missionIndex.get(step), 0);
            z = gpsZs.getOrDefault(missionIndex.get(step), 0);
            yaw = yaws.getOrDefault(missionIndex.get(step), 0f);
            time = times.getOrDefault(missionIndex.get(step), 0);

            radius = radiusMap.getOrDefault(missionIndex.get(step), 0);
            if (missionIndex.getOrDefault(step, "finish").equals("takeoff")) {

                missionMap = flight.missionTakeoff(droneLog, droneId);
                flag++;

            } else if (missionIndex.getOrDefault(step, "finish").contains("waypoint")) {

                MissionItemInt missionItemInt = new MissionItemInt.Builder()
                        .command(MavCmd.MAV_CMD_NAV_WAYPOINT)
                        .param1(time)
                        .param2(0)
                        .param3(0)
                        .param4(yaw)      //yaw
                        .x(x)
                        .y(y)
                        .z(z)
                        .seq(flag)
                        .targetComponent(0)
                        .targetSystem(0)
                        .current(0)
                        .autocontinue(1)
                        .frame(MavFrame.MAV_FRAME_GLOBAL_INT)
                        .missionType(MavMissionType.MAV_MISSION_TYPE_MISSION)
                        .build();
                missionMap.put("missionItemInt" + flag, missionItemInt);
                flag++;

            } else if (missionIndex.getOrDefault(step, "finish").contains("loiter")) {
                MissionItemInt missionItemInt = new MissionItemInt.Builder()
                        .command(MavCmd.MAV_CMD_NAV_LOITER_TURNS)
                        .param1(time)
                        .param2(0)
                        .param3(radius)
                        .param4(0)
                        .x(x)
                        .y(y)
                        .z(z)
                        .seq(flag)
                        .targetComponent(0)
                        .targetSystem(0)
                        .current(0)
                        .autocontinue(1)
                        .frame(MavFrame.MAV_FRAME_GLOBAL_INT)
                        .missionType(MavMissionType.MAV_MISSION_TYPE_MISSION)
                        .build();
                missionMap.put("missionItemInt" + flag, missionItemInt);
                flag++;

            } else if (missionIndex.getOrDefault(step, "finish").contains("return")) {
                MissionItemInt missionItemInt = new MissionItemInt.Builder()
                        .command(MavCmd.MAV_CMD_NAV_RETURN_TO_LAUNCH)
                        .seq(flag)
                        .targetComponent(0)
                        .targetSystem(0)
                        .current(0)
                        .autocontinue(1)
                        .frame(MavFrame.MAV_FRAME_GLOBAL_INT)
                        .missionType(MavMissionType.MAV_MISSION_TYPE_MISSION)
                        .build();
                missionMap.put("missionItemInt" + flag, missionItemInt);
                flag++;

            } else if (missionIndex.getOrDefault(step, "finish").contains("finish")) {
                break;
            }
            step++;

        }

        Timer startMissionTimer = new Timer();
        HashMap<String, MissionItemInt> finalMissionMap = missionMap;
        int finalFlag = flag;
        int finalDroneId = droneId;

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (flight.getIsTakeOffEnd()) {
                    log.info("droneIdTakeOffEnd={}", finalDroneId);
                    isEnd[0] = flight.doMission(finalMissionMap, finalFlag, speeds, yaws, missionIndex);
                    this.cancel();

                }
            }
        };

        startMissionTimer.schedule(timerTask, 0, 1000);

    }

    /**
     * 미션중 일시중지와 재생 같은경우 mission planner 가아닌 실제 드론이나 일부 가상 드론에서만 적용됩니다.
     * @param paramMap { droneId : }
     */
    @MessageMapping("/pause")
    public void pause(@RequestBody Map<String, Object> paramMap) {
        int droneId = 0;
        if (paramMap.get("droneId") != null)
            droneId = Integer.parseInt(paramMap.get("droneId").toString());
        log.info("droneId={}", droneId);
        Flight flight = mapManager.getFlightMap().get(droneId);
        flight.pauseOrPlay(0);

    }

    /**
     * 미션중 일시중지와 재생 같은경우 mission planner 가아닌 실제 드론이나 일부 가상 드론에서만 적용됩니다.
     * @param paramMap { droneId : }
     */
    @MessageMapping("/play")
    public void play(@RequestBody Map<String, Object> paramMap) {
        int droneId = 0;
        if (paramMap.get("droneId") != null)
            droneId = Integer.parseInt(paramMap.get("droneId").toString());
        log.info("droneId={}", droneId);
        Flight flight = mapManager.getFlightMap().get(droneId);
        flight.pauseOrPlay(1);
    }

    /**
     *
     * @param paramMap
     *  {droneId : }
     */
    @MessageMapping("/arm")
    public void arm(@RequestBody Map<String, Object> paramMap) {
        int droneId = 0;
        if (paramMap.get("droneId") != null)
            droneId = Integer.parseInt(paramMap.get("droneId").toString());
        log.info("droneId={}", droneId);

        Flight flight = mapManager.getFlightMap().get(droneId);
        flight.armDisarm(1, droneId);

    }
    /**
     *
     * @param paramMap
     *  {droneId : }
     */
    @MessageMapping("/disarm")
    public void disarm(@RequestBody Map<String, Object> paramMap) {
        int droneId = 0;
        if (paramMap.get("droneId") != null)
            droneId = Integer.parseInt(paramMap.get("droneId").toString());
        log.info("droneId={}", droneId);
        Flight flight = mapManager.getFlightMap().get(droneId);
        flight.armDisarm(0, droneId);
    }


}
