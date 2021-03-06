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
     * ?????? ????????????
     *
     * @param response
     * @param paramMap
     *                 paramMap = {
     *                 dataMap: resultData,  <- ????????? ??????
     *                 fileName: "Log.xlsx",
     *                 headerList: ["?????????", "????????????", "????????????", "????????????"]
     *                 };
     *
     *                 dataMap-> List<Map<String,Object>> dataMap
     *                 headerList -> List<String> heartList
     *                 dataMap -> ????????? ?????? data map ?????????
     *                 headerList -> ?????? ????????? ?????? ????????? ????????? ????????? ??????
     *
     *                 ?????? : dataMap ,
     *                 ?????? : headerList
     *                 *
     *                 return response??? blob ???????????? ?????????.
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
