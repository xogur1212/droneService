package com.xy124.drone.controller;


import com.xy124.drone.service.IDroneDetailsService;
import com.xy124.drone.service.IDroneService;
import com.xy124.drone.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class DroneFileController {

    private final IDroneService droneService;
    private final IDroneDetailsService droneDetailsService;

    @PostMapping("/upload/drone")
    public ResponseEntity<?> fileUpload(MultipartFile[] uploadFile, HttpServletRequest request, long droneSeq) {

        if (droneSeq == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            String fileName = FileUtil.uploadAjaxPost(uploadFile, request);

            droneDetailsService.updateDroneDetailsFileName(fileName,droneSeq);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(fileName);
        }
    }

    @GetMapping(value = "/image/{imageName:.+}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public void getImage(@PathVariable("imageName") String imageName, HttpServletRequest request, HttpServletResponse response) {


        if (!(imageName == null || imageName.isEmpty() || imageName.equals("null"))) {
            FileUtil.getImage(imageName, request, response);
        }

    }


    @ResponseBody
    @RequestMapping(value = "/{fileName:.+}")
    public void fileDownload(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("fileName") String fileName)  {

        FileUtil.fileDownload(request, response, fileName);

    }

    /**
     * excel donwload
     * 엑셀 다운로드
     *
     * @param response
     * @param paramMap
     *                 paramMap = {
     *                 dataMap: resultData,  <- 조회한 결과
     *                 fileName: "Log.xlsx",
     *                 headerList: ["아이디", "드론이름", "미션이름", "입력날짜"]
     *                 };
     *
     *                 dataMap-> List<Map<String,Object>> dataMap
     *                 headerList -> List<String> heartList
     *                 dataMap -> 엑셀에 담을 data map 리스트
     *                 headerList -> 엑셀 첫줄에 해더 부분을 임의로 지정할 경우
     *
     *                 필수 : dataMap ,
     *                 선택 : headerList
     *                 *
     *                 return response에 blob 데이터를 보낸다.
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/excel/download")
    public void excelDownload(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) throws IOException {

        Workbook wb = null;

        wb = FileUtil.excelDownload(paramMap);


        wb.write(response.getOutputStream());
        wb.close();

    }

}
