package com.study.reproduce.handler.index;

import com.study.reproduce.handler.admin.BlogHandler;
import com.study.reproduce.utils.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/image")
public class ImageHandler {

    @GetMapping("/blogs/{fileName}")
    public void getBlogCover(@PathVariable String fileName, HttpServletResponse response) {
        File file = FileUtil.getLoadFile(BlogHandler.class, fileName);
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try {
            BufferedImage image = ImageIO.read(file);
            ImageIO.write(image, "jpg", response.getOutputStream());
        } catch (IOException e) {
            //TODO 异常处理
            e.printStackTrace();
        }
    }
}
