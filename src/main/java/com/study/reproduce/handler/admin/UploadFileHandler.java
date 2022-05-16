package com.study.reproduce.handler.admin;

import com.study.reproduce.utils.FileUtil;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import com.study.reproduce.utils.URIUtil;
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
    public Result upload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile) throws URISyntaxException {
        File file = FileUtil.getUploadFile(UploadFileHandler.class, multipartFile);
        System.out.println(file.getAbsolutePath());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传错误");
        }
        String url = request.getRequestURL().toString();
        URI uri = URIUtil.getResponseURI(new URI(url), file.getName());
        return ResultGenerator.getSuccessResult(uri);
    }
}
