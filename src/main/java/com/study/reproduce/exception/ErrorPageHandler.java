package com.study.reproduce.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorPageHandler {

    @RequestMapping("/error_404")
    public String notFound() {
        return "error/error_404";
    }

    @RequestMapping("/error_400")
    public String badRequest() {
        return "error/error_400";
    }

    @RequestMapping("/error_5xx")
    public String systemError() {
        return "error/error_5xx";
    }
}
