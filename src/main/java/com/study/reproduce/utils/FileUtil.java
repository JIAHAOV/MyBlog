package com.study.reproduce.utils;

import com.study.reproduce.constant.FileConstant;
import com.study.reproduce.exception.ExceptionManager;
import com.study.reproduce.handler.admin.UploadFileHandler;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public class FileUtil {
    public static File getUploadFile(Class location, MultipartFile multipartFile) {
        String originalPath = getOriginalPath(location);
        String originalFilename = multipartFile.getOriginalFilename();
        File path = new File(originalPath + File.separator + FileConstant.FILE_UPLOAD_DIC);
        if (!path.exists()) {
            boolean mkdirs = path.mkdirs();
            if (!mkdirs) {
                throw ExceptionManager.genException("文件创建失败");
            }
        }
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        File file = new File(path, fileName);
        return file;
    }

    public static File getLoadFile(Class location, String fileName) {
        String originalPath = getOriginalPath(location);
        String filePath = originalPath + File.separator + FileConstant.FILE_UPLOAD_DIC + File.separator + fileName;
        File file = new File(filePath);
        return file;
    }

    public static String getOriginalPath(Class location) {
        return new ApplicationHome(location).getSource().getParentFile().toString();
    }
}
