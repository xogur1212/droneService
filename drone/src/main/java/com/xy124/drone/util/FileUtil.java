package com.xy124.drone.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class FileUtil {


    private static String[] STATIC_EXTENSION_LIST;

    @Value("#{'${xy124.file.extension}'.split(',')}")
    public void setStaticExtensionList(String[] extensionList) {
        STATIC_EXTENSION_LIST = extensionList;
    }

    private static String STATIC_EXTERNAL_FILE_PATH;


    @Value("${xy124.path.root}")
    public void setExternalFilePath(String EXTERNAL_FILE_PATH) {
        STATIC_EXTERNAL_FILE_PATH = EXTERNAL_FILE_PATH;
    }


    public static String uploadAjaxPost(MultipartFile[] uploadFile, HttpServletRequest request) {

        String folderPath = "";
        String folder[] = request.getHeader("REFERER").split("/");
        for (int i = 0; i < folder.length; i++) {
            if (i >= 3)
                folderPath += folder[i] + "/";

        }
        String sPath = STATIC_EXTERNAL_FILE_PATH;
        String uploadFileName = null;
        String savedFileName = null;

        File uploadPath = new File(sPath, folderPath);
        log.info("upload path : " + uploadPath);


        if (uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {

            log.info("Upload File Name : " + multipartFile.getOriginalFilename());
            log.info("Upload File Size : " + multipartFile.getSize());

            uploadFileName = multipartFile.getOriginalFilename();
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);


            log.info("file name : " + uploadFileName);


            for (String extension : STATIC_EXTENSION_LIST) {
                if (uploadFileName.contains(extension))
                    return null;
            }

            savedFileName = setFileUUID() + uploadFileName;
            File saveFile = new File(uploadPath, savedFileName);

            try {
                multipartFile.transferTo(saveFile);
            } catch (IOException e) {
                //TODO error 처리 어떻게 ?
                e.printStackTrace();
            }
        }
        return savedFileName;
    }

    private static String setFileUUID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        String str = sdf.format(date);
        return str + "_";
    }

    public static void getImage(String imageName, HttpServletRequest request, HttpServletResponse response) {

        String folderPath = "/";
        String folder[] =request.getHeader("REFERER").split("/");

        for (int i = 0; i < folder.length; i++) {
            if (i >= 3)
                folderPath += folder[i] + "/";
        }
        String imagePath = STATIC_EXTERNAL_FILE_PATH + folderPath + imageName;

        File file = new File(imagePath);
        FileInputStream fis = null;

        BufferedInputStream in = null;
        ByteArrayOutputStream bStream = null;

        try {
            fis = new FileInputStream(file);
            in = new BufferedInputStream(fis);
            bStream = new ByteArrayOutputStream();
            int imgByte;
            while ((imgByte = in.read()) != -1) {
                bStream.write(imgByte);
            }

            String type = "";

            int pos = imagePath.lastIndexOf(".");
            String ext = imagePath.substring(pos + 1);

            type = "image/" + ext.toLowerCase();

            System.out.println(type);

            response.setHeader("Content-Type", type);
            response.setContentLength(bStream.size());
            bStream.writeTo(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (Exception e) {
            //TODO exception 처리
            e.printStackTrace();

        } finally {
            if (bStream != null) {
                try {
                    bStream.close();
                } catch (Exception est) {
//TODO exception 처리
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ei) {
//TODO exception 처리
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception efis) {
//TODO exception 처리
                }
            }
        }
    }

    @Description("데이터를 엑셀로 다운로드")
    //TODO map 네이밍... 수정..
    public static Workbook excelDownload(Map<String, Object> paramMap) {
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
        List<String> headerList = null;
        List<String> headerEn = new ArrayList<>();
        List<String> headerKo = new ArrayList<>();

        if (paramMap.get("dataMap") != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<?> data = (List<?>) paramMap.get("dataMap");
            for(int i = 0; i < data.size(); i++){
                dataMap.add(objectMapper.convertValue(data.get(i), Map.class));
            }
        }

        if (paramMap.get("headerList") != null) {
            headerList = (List<String>) paramMap.get("headerList");
            headerList.forEach(r -> {
                log.info("headerList={}", r);
                String header[] = r.split("\\|");
                log.info("headerList={},{},{}", header, header[0], header[1]);
                headerKo.add(header[0]);
                headerEn.add(header[1]);
            });

            Map<String, Object> map4 = dataMap.get(0);
            Set<String> keySet = map4.keySet();
            List<String> keyList = new ArrayList<>(keySet);

            headerEn.forEach(r -> {
                keyList.remove(r);
            });

            Iterator<Map<String, Object>> iter = dataMap.iterator();
            log.info("headerEn={}", headerEn);

            while (iter.hasNext()) {
                Map<String, Object> map3 = iter.next();
                keyList.forEach(keyListValue -> {
                    map3.remove(keyListValue);
                });
            }
        }

        int rowNum = 0;
        AtomicInteger cellNum = new AtomicInteger();
        //log.info("paramMap={}", paramMap);
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet 1");
        rowNum = 1;

        for (Map<String, Object> data : dataMap) {
            //row 생성
            Integer finalRowNum = rowNum;
            Row row = sheet.createRow(finalRowNum);
            Row headRow = sheet.createRow(0);
            cellNum.set(0);
            if (headerList == null) {
                data.forEach((k, v) -> {
                    Cell cell = headRow.createCell(cellNum.get());
                    if (k != null)
                        cell.setCellValue(k);
                    cellNum.incrementAndGet();
                });
            } else {
                headerKo.forEach((s) -> {
                    Cell cell = headRow.createCell(cellNum.get());
                    if (s != null)
                        cell.setCellValue(s);
                    cellNum.incrementAndGet();
                });
            }

            cellNum.set(0);
            data.forEach((k, v) -> {
                Cell cell = row.createCell(cellNum.get());
                if (v != null)
                    cell.setCellValue(v.toString());

                //cell에 데이터 삽입
                cellNum.incrementAndGet();
            });

            sheet.autoSizeColumn(finalRowNum);
            rowNum++;
        }
        return wb;
    }
    
    @Description("exception 처리 필요")
    public static void fileDownload(HttpServletRequest request, HttpServletResponse response,
                                    String fileName) {
        String folderPath = "/";
        String folder[] = request.getHeader("REFERER").split("/");
        for (int i = 0; i < folder.length; i++) {
            if (i >= 3)
                folderPath += folder[i] + "/";
        }
        File file = new File(STATIC_EXTERNAL_FILE_PATH + folderPath + fileName);
        if (file.exists()) {
            //get the mimetype
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                //unknown mimetype so set the mimetype to application/octet-stream
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            //response.setContentType("application/download; UTF-8");

//            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

            String fileNameOrg = file.getName();
            log.info("file.getName()={}", file.getName());
            try {
                fileNameOrg = new String(fileNameOrg.getBytes("UTF-8"), "ISO-8859-1");
                response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + fileNameOrg + "\""));

                response.setContentLength((int) file.length());

                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

                FileCopyUtils.copy(inputStream, response.getOutputStream());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
