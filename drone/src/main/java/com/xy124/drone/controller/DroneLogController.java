package com.xy124.drone.controller;

import com.xy124.drone.service.IDroneLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/drone/api")
@RequiredArgsConstructor
@Slf4j
public class DroneLogController {

    private final IDroneLogService droneLogService;

    @PostMapping("/dronelog")
    public ResponseEntity<?> findAllDroneLog(@RequestBody Map<String,Object> paramMap){
        log.info("paramMap={}",paramMap);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(droneLogService.findAllDroneLog(paramMap));
    }
}
