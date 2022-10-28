package com.study.reproduce.handler.admin;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.study.reproduce.constant.RedisConstant.VERIFY_CODE;

@Controller
@RequestMapping("/common")
public class CaptchaHandler {
    @Resource
    private DefaultKaptcha defaultKaptcha;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String verifyCode = defaultKaptcha.createText();
        String sessionId = request.getSession().getId();
        String key = VERIFY_CODE + sessionId;
        stringRedisTemplate.opsForValue().set(key, verifyCode, 3, TimeUnit.MINUTES);
        BufferedImage image = defaultKaptcha.createImage(verifyCode);
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ImageIO.write(image, "jpg", response.getOutputStream());
    }
}
