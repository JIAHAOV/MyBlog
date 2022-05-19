package com.study.reproduce.service;

import com.study.reproduce.model.domain.WebSiteConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
* @author 18714
* @description 针对表【tb_config】的数据库操作Service
* @createDate 2022-05-10 21:27:59
*/
public interface WebSiteConfigService extends IService<WebSiteConfig> {
    /**
     * 用来将默认设置存储到作用域中，方便在前端页面获取
     * @param session session
     * @param model 用来存储数据
     * @return 是否成功
     */
    boolean setConfigMessage(HttpSession session, Model model);

    /**
     * 获取所有的配置
     * @return 查询结果
     */
    Map<String, String> getAllConfigs();
}
