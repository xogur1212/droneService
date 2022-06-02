package com.xy124.drone.controller;

import com.xy124.drone.service.IDroneBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
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
public class DroneBaseController {

    private final IDroneBaseService droneBaseService;

    /**
     *
     * @param paramMap
     *
     * @return
     * [
     *     {
     *         "droneBaseSeq": 1,
     *         "baseName": "드론기지1"
     *     },
     *     {
     *         "droneBaseSeq": 2,
     *         "baseName": "드론기지2"
     *     }
     * ]
     */
    @PostMapping("/drone-base")
    @Description("Test O")
    public ResponseEntity<?> findAllDroneBase(@RequestBody Map<String, Object> paramMap) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(droneBaseService.findAllDroneBase());
    }
}
