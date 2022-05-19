package com.study.reproduce.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorPageHandler {

    @RequestMapping("/404")
    public String notFound() {
        return "error/404";
    }

    @RequestMapping("/400")
    public String badRequest() {
        return "error/400";
    }

    @RequestMapping("/5xx")
    public String systemError() {
        return "error/5xx";
    }
}
