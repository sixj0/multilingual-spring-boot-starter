/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.sixj.multilingual.controller;

import cn.hutool.poi.excel.ExcelWriter;
import com.sixj.multilingual.service.MultilingualService;
import com.sixj.multilingual.enums.MultilingualClientTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * @author sixiaojie
 * @date 2019-12-14 15:14
 */
@RestController
@RequestMapping("/language")
public class MultilingualController {
    private static final String SEPARATOR = ".";
    private static final String UNDERLINE = "_";

    @Autowired
    private MultilingualService multilingualService;


    /**
     * 下载多语言配置模板
     * @param response
     * @throws Exception
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response,Integer clientType) throws Exception {
        // 生成Excel
        ExcelWriter writer = multilingualService.createExcel(clientType);

        String fileName = "多语言配置.xlsx";
        fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        response.reset();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        OutputStream out = response.getOutputStream();
        writer.flush(out);

        out.close();
        writer.close();
    }

    /**
     * 将填写之后多语言模板上传，解析到数据库
     * @param file
     */
    @PostMapping(value = "/uploadTemplate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadTemplate(@RequestParam("file") MultipartFile file) {
        multilingualService.uploadTemplate(file);
    }


    /**
     * 下载语言包，PC端解析为json格式文件，ios解析为txt文件，Android解析为xml文件
     * @param response
     */
    @GetMapping("/downloadLangPackage")
    public void downloadLangPackage(HttpServletResponse response,
                                    @RequestParam("clientType") String clientType,
                                    @RequestParam("languageType") String languageType){
        List<String> contentList = multilingualService.downloadLangPackage(clientType, languageType);
        String fileName = clientType + UNDERLINE + languageType + SEPARATOR ;
        if(MultilingualClientTypeEnum.FRONT.getDesc().equals(clientType)){
            fileName = fileName + "json";
        }
        if(MultilingualClientTypeEnum.APP_IOS.getDesc().equals(clientType)){
            fileName = fileName + "txt";
        }
        if(MultilingualClientTypeEnum.APP_ANDROID.getDesc().equals(clientType)){
            fileName = fileName + "xml";
        }
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        BufferedOutputStream buff = null;
        OutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            String newLine = System.getProperty("line.separator");
            for (String content : contentList) {
                buff.write(content.getBytes("UTF-8"));
                buff.write(newLine.getBytes());
            }
            buff.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(!Objects.isNull(buff)){
                    buff.close();
                }
                if(!Objects.isNull(outStr)){
                    outStr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
