package com.study.reproduce.service;

import com.study.reproduce.model.domain.WebSiteConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

/**
* @author 18714
* @description 针对表【tb_config】的数据库操作Service
* @createDate 2022-05-10 21:27:59
*/
public interface WebSiteConfigService extends IService<WebSiteConfig> {
    boolean setConfigMessage(HttpSession session, Model model);
}
