package com.myvamp.vamp.controller;


import com.myvamp.vamp.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件的上传和下载
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${vamp.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String filName = UUID.randomUUID().toString()+suffix;

        File dir = new File(basePath);
        if(!dir.exists()) dir.mkdirs();
        try {
//            将临时文件转存到目标位置
            file.transferTo(new File(basePath+filName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(file.toString());
        return R.success(filName);

    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            ServletOutputStream servletOutputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int length = 0;


            response.setContentType("image/jpeg");
            while ((length=fileInputStream.read(bytes))!=-1){
                servletOutputStream.write(bytes,0,length);
                servletOutputStream.flush();
            }

            servletOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
