package com.study.reproduce.handler.admin;

import cn.hutool.core.lang.UUID;
import com.study.reproduce.exception.ExceptionGenerator;
import com.study.reproduce.service.UploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/admin")
public class UploadFileHandler {

    @Resource
    private UploadFileService uploadFileService;

//    @PostMapping({"/upload/file"})
    @PostMapping({"/blogs/md/uploadfile"})
    public void upload(@RequestParam("editormd-image-file") MultipartFile multipartFile, HttpServletResponse response)
            throws URISyntaxException, IOException {
        if (multipartFile.isEmpty()) {
            throw ExceptionGenerator.businessError("文件不能为空");
        }
        String filename = multipartFile.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw ExceptionGenerator.businessError("文件名不能为空");
        }
        String substring = filename.substring(filename.lastIndexOf("."));
        String uuid = UUID.randomUUID(true).toString(true);
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
        String key = date + uuid + substring;
        String url = uploadFileService.SimpleUploadFileFromStream(key, multipartFile.getInputStream());
        PrintWriter writer = response.getWriter();
        //回复
        try {
            response.setHeader("Content-Type", "text/html");
            writer.write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + url + "\"}");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("{\"success\":0}");
            writer.flush();
            writer.close();
            throw new RuntimeException("上传错误");
        }
    }
}
