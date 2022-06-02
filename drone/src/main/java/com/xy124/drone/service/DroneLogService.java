package com.xy124.drone.service;


import com.xy124.drone.model.DroneLog;
import com.xy124.drone.model.dto.response.DroneLogResponse;
import com.xy124.drone.model.dto.response.DroneLogWithOutDetailsResponse;
import com.xy124.drone.repository.DroneLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DroneLogService implements IDroneLogService{

    private final DroneLogRepository droneLogRepository;

    @Override
    public DroneLog saveDroneLog(DroneLog droneLog) {

        return droneLogRepository.save(droneLog);
    }
    @Override
    public DroneLog findByDroneLogSeq(Long droneLogSeq) {

        return droneLogRepository.findByDroneLogSeq(droneLogSeq);
    }


    //TODO 구조 수정 필요  의미없는 response 두개 paramMap 입력받는 것 수정
    @Override
    public Map<String, Object> findAllDroneLog(Map<String, Object> paramMap) {
        log.info("paramMap={}", paramMap);
        int start = 0;
        int length = 1;
        int count = 1;

        int pageGroupCount = 3;
        int searchType = 0; //1 -> all(db 조회 or )  0 ->  db조회 (and)
        String deviceName = "";
        String missionName = "";
        Date beforeDate = Date.valueOf("2000-01-01");
        Date afterDate = Date.valueOf("9999-12-31");

        if (paramMap.get("start") != null) {
            start = Integer.parseInt(paramMap.get("start").toString());
            if (start == -1)
                start = 0;
        }

        if (paramMap.get("length") != null)
            length = Integer.parseInt(paramMap.get("length").toString());


        if (paramMap.get("deviceName") != null)
            deviceName = paramMap.get("deviceName").toString();
        if (paramMap.get("missionName") != null)
            missionName = paramMap.get("missionName").toString();

        if (paramMap.get("searchType") != null)
            searchType = Integer.parseInt(paramMap.get("searchType").toString());

        if (paramMap.get("beforeDate") != null)
            if (!paramMap.get("beforeDate").equals(""))
                beforeDate = Date.valueOf(paramMap.get("beforeDate").toString());
        if (paramMap.get("afterDate") != null)
            if (!paramMap.get("afterDate").equals(""))
                afterDate = Date.valueOf(paramMap.get("afterDate").toString());


        log.info("beforeDate=" + beforeDate + "afterDate= " + afterDate);
        log.info("searchType={}",searchType);
        Page<DroneLog> droneLogPage = null;
        List<DroneLog> droneLogList = null;
      
        List<DroneLogResponse> droneLogResponseList = null;
        List<DroneLogWithOutDetailsResponse> droneLogWithOutDetailsResponseList = null;

        if (length == 1) {
            if (searchType == 0) {
                droneLogList = droneLogRepository.findByInsertDtBetweenAndDroneDeviceNameIgnoreCaseLikeAndMissionNameIgnoreCaseLike(
                        beforeDate, afterDate, "%" + deviceName + "%", "%" + missionName + "%", Sort.by(Sort.Direction.DESC, "insertDt")
                );
            } else if (searchType == 1) {
                droneLogList = droneLogRepository.findByInsertDtBetweenAndDroneDeviceNameIgnoreCaseLikeOrInsertDtBetweenAndMissionNameIgnoreCaseLike(
                        beforeDate, afterDate, "%" + deviceName + "%", beforeDate, afterDate, "%" + deviceName + "%"
                        , Sort.by(Sort.Direction.DESC, "insertDt"));
            }
            droneLogWithOutDetailsResponseList =droneLogList.stream().map(DroneLogWithOutDetailsResponse::new).collect(Collectors.toList());
        } else {
            if (searchType == 0) {
                PageRequest pageRequest = PageRequest.of(start, length, Sort.by(Sort.Direction.DESC, "insertDt"));
                droneLogPage = droneLogRepository.findByInsertDtBetweenAndDroneDeviceNameIgnoreCaseLikeAndMissionNameIgnoreCaseLike(
                        beforeDate, afterDate, "%" + deviceName + "%", "%" + missionName + "%", pageRequest);
                count = (int) droneLogPage.getTotalElements();
                droneLogList = droneLogPage.toList();
            } else if (searchType == 1) {

                PageRequest pageRequest = PageRequest.of(start, length, Sort.by(Sort.Direction.DESC, "insertDt"));
                droneLogPage = droneLogRepository.findByInsertDtBetweenAndDroneDeviceNameIgnoreCaseLikeOrInsertDtBetweenAndMissionNameIgnoreCaseLike(
                        beforeDate, afterDate, "%" + deviceName + "%", beforeDate, afterDate, "%" + deviceName + "%", pageRequest); //droneDevice로 or 검색함
                count = (int) droneLogPage.getTotalElements();
                droneLogList = droneLogPage.toList();
            }
            droneLogResponseList = droneLogList.stream().map(DroneLogResponse::new).collect(Collectors.toList());
        }


        Map<String, Object> pagingMap = new HashMap<>();
        try {

            int lastPage = (int) Math.ceil(count * 1.0 / length);
            if (length == 1) {          //전체 데이터 엑셀 다운로드 용
                pagingMap.put("data", droneLogWithOutDetailsResponseList); // 페이징 + 검색조건 결과
            } else {
                pagingMap.put("data", droneLogResponseList); // 페이징 + 검색조건 결과
            }

            pagingMap.put("count", count); // 검색조건이 반영된 총 카운트
            pagingMap.put("pages", lastPage);
            pagingMap.put("pageGroupCount", pageGroupCount);

            int startPage = (start / pageGroupCount) * pageGroupCount + 1;
            int endPage = startPage + pageGroupCount - 1;
            if (endPage >= lastPage)
                endPage = lastPage;
            pagingMap.put("startPage", startPage);
            pagingMap.put("endPage", endPage);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return pagingMap;
    }


}
