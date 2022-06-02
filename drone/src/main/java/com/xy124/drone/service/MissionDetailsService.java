package com.xy124.drone.service;


import com.xy124.drone.model.Mission;
import com.xy124.drone.model.MissionDetails;
import com.xy124.drone.model.dto.Result;
import com.xy124.drone.model.dto.ResultCode;
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
@Transactional
@Slf4j
public class MissionDetailsService implements IMissionDetailsService {

    private final MissionDetailsRepository missionDetailsRepository;
    private final MissionRepository missionRepository;

    private int estimatedTime = 0;
    private int timeCountNumber = 0;
    private double totalDistance = 0;

    @Transactional
    @Override
    public Result saveMission(List<MissionDetails> missionDetails, long mission_id, double Distance) {

        totalDistance = (int) Distance;
        Optional<Mission> optionalMission = missionRepository.findByMissionSeq(mission_id);


        if (!optionalMission.isPresent()) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        }
        Mission mission = optionalMission.get();
        Timestamp timeStamp=new Timestamp(System.currentTimeMillis());
        mission.setUpdateDt(timeStamp);


        Long deleteResult = missionDetailsRepository.deleteByMission(mission);

        missionDetails.forEach(r -> {
            if (r.getName().equals("waypoint") || r.getName().equals("return")) {
                if (r.getSpeed() == 0)
                    estimatedTime += 10;
                else
                    estimatedTime += r.getSpeed();

                timeCountNumber++;
            }
            if (r.getName().equals("loiter")) {
                if (r.getSpeed() == 0)
                    estimatedTime += 10;
                else
                    estimatedTime += r.getSpeed();


                if (r.getRadius() != 0 && r.getTime() != 0) {
                    this.increaseTotalDistance(r.getRadius() * 2 * 3.14 * r.getTime());
                }
                timeCountNumber++;
            }

            r.setMission(mission);
        });


        mission.setTotalDistance((int) totalDistance);
        mission.setEstimatedTime((int) Math.round(totalDistance / estimatedTime * timeCountNumber / 60));

        
        //TODO 변수명 수정
        List<MissionDetails> isExist = missionDetailsRepository.findAllByMission(mission);
        // log.info("isExist={}", isExist);
        if (isExist.isEmpty()) {
            missionDetails.forEach(r -> {
                if (r.getAlt()==0)
                    r.setAlt(50);
                if (r.getName().equals("takeoff"))
                    r.setKoName("이륙");
                else if (r.getName().equals("loiter"))
                    r.setKoName("로이터");
                else if (r.getName().equals("return"))
                    r.setKoName("귀환");
                else if (r.getName().equals("waypoint"))
                    r.setKoName("경유지");
                else if (r.getName().equals("roi"))
                    r.setKoName("관심영역");
                missionDetailsRepository.save(r);
            });
         return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);
        }

        return ResultUtil.makeSuccessResult(ResultCode.FAIL);
    }

    @Transactional
    @Override
    public Result updateMissionDetails(List<MissionDetails> missionDetails, long mission_id) {


        Optional<Mission> mission = missionRepository.findByMissionSeq(mission_id);



        if (!mission.isPresent()) {
            return ResultUtil.makeSuccessResult(ResultCode.FAIL);
        }


        missionDetails.forEach(r -> {
            r.setMission(mission.get());
        });

        missionDetails.forEach(r -> {
            missionDetailsRepository.save(r);
        });

        return ResultUtil.makeSuccessResult(ResultCode.SUCCESS);


    }

    public List<MissionDetails> findOneMissionDetails(String name) {
        List<MissionDetails> missionDetails = missionDetailsRepository.findAllByName(name);

        return missionDetails;

    }

    private Sort sortByIndex() {
        return Sort.by(Sort.Direction.ASC, "index");
    }

    //TODO name과 missionSeq 같이 입력했을떄 ?? 따로 처리해야되는건지 ?
    @Override
    public List<MissionDetails> findMissionDetailsList(Map<String, Object> paramMap) {
        String name = null;
        Mission mission = null;
        Optional<Mission> optionalMission = null;
        Long missionSeq = null;
        Sort sort = sortByIndex();
        int inputid = 0;
        if (paramMap.get("name") != null) {
            name = paramMap.get("name").toString();
            mission = missionRepository.findByName(name);
        }
        if (paramMap.get("missionSeq") != null) {
            inputid = Integer.parseInt(paramMap.get("missionSeq").toString());
            missionSeq = Long.valueOf(inputid);
            optionalMission = missionRepository.findByMissionSeq(missionSeq);
            if (!optionalMission.isPresent()) return null;
            mission = optionalMission.get();
        }

        if (name == null && missionSeq == 0) {
            return null;
        }


        List<MissionDetails> missionDetails = missionDetailsRepository.findAllByMission(mission, sort);

        if (missionDetails.isEmpty()) {
            log.info("비었음");
            return null;

        }
        return missionDetails;

    }
    private void increaseTotalDistance(double increaseDistance) {

        this.totalDistance += increaseDistance;
    }
}
