package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.constant.WebSiteConfigId;
import com.study.reproduce.model.domain.WebSiteConfig;
import com.study.reproduce.service.AdminService;
import com.study.reproduce.service.WebSiteConfigService;
import com.study.reproduce.mapper.WebSiteConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 18714
* @description 针对表【tb_config】的数据库操作Service实现
* @createDate 2022-05-10 21:27:59
*/
@Service
public class WebSiteConfigServiceImpl extends ServiceImpl<WebSiteConfigMapper, WebSiteConfig>
    implements WebSiteConfigService{

    @Resource
    WebSiteConfigMapper webSiteConfigMapper;
    @Override
    public boolean setConfigMessage(HttpSession session, Model model) {
        Object attribute = session.getAttribute(AdminService.GET_ADMIN_KEY);
        if (attribute == null) {
            return false;
        }
        List<WebSiteConfig> webSiteConfigs = webSiteConfigMapper.selectList(null);
        for (WebSiteConfig webSiteConfig : webSiteConfigs) {
            model.addAttribute(webSiteConfig.getConfigName(), webSiteConfig.getConfigValue());
        }
        return true;
    }

    @Override
    public Map<String, String> getAllConfigs() {
        List<WebSiteConfig> configs = webSiteConfigMapper.selectList(null);
        Map<String, String> map = new HashMap<>();
        for (WebSiteConfig config : configs) {
            map.put(config.getConfigName(), config.getConfigValue());
        }
        return map;
    }
}




