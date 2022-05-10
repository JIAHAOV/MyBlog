package com.study.reproduce.handler;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("/common")
public class CaptchaHandler {
    @Resource
    private DefaultKaptcha defaultKaptcha;

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response){
        String verifyCode = defaultKaptcha.createText();
        request.getSession().setAttribute("verifyCode", verifyCode);
        BufferedImage image = defaultKaptcha.createImage(verifyCode);
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try {
            ImageIO.write(image, "jpg", response.getOutputStream());
        } catch (IOException e) {
            //TODO 异常处理
            e.printStackTrace();
        }
    }
}
