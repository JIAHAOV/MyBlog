package com.study.reproduce.handler.admin;

import com.study.reproduce.exception.ExceptionManager;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class UploadFileHandler {

    @PostMapping({"/upload/file"})
    public Result upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile multipartFile) throws URISyntaxException {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        File file = new File(fileName);
        try {
            if (!file.createNewFile()) {
                throw ExceptionManager.genException("创建文件出现异常");
            }
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传错误");
        }
        return ResultGenerator.getSuccessResult(new URI(file.getPath()));
    }
}
