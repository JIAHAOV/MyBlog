package com.study.reproduce.handler;

import com.study.reproduce.constant.WebSiteConfigId;
import com.study.reproduce.model.domain.WebSiteConfig;
import com.study.reproduce.model.request.FooterInfoParam;
import com.study.reproduce.model.request.UserInfoParam;
import com.study.reproduce.model.request.WebsiteInfoParam;
import com.study.reproduce.service.WebSiteConfigService;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
@RequestMapping("/admin")
public class WebSiteConfigHandler {

    @Resource
    WebSiteConfigService webSiteConfigService;

    @PostMapping("/configurations/website")
    public Result website(WebsiteInfoParam websiteInfoParam) {
        ArrayList<WebSiteConfig> webSiteConfigs = new ArrayList<>();
        if (!websiteInfoParam.getWebsiteDescription().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.WEBSITE_DESCRIPTION);
            webSiteConfig.setConfigValue(websiteInfoParam.getWebsiteDescription());
            webSiteConfigs.add(webSiteConfig);
        }
        if (!websiteInfoParam.getWebsiteIcon().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.WEBSITE_ICON);
            webSiteConfig.setConfigValue(websiteInfoParam.getWebsiteIcon());
            webSiteConfigs.add(webSiteConfig);
        }
        if (!websiteInfoParam.getWebsiteLogo().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.WEBSITE_LOGO);
            webSiteConfig.setConfigValue(websiteInfoParam.getWebsiteLogo());
            webSiteConfigs.add(webSiteConfig);
        }
        if (!websiteInfoParam.getWebsiteName().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.WEBSITE_NAME);
            webSiteConfig.setConfigValue(websiteInfoParam.getWebsiteName());
            webSiteConfigs.add(webSiteConfig);
        }
        boolean result = webSiteConfigService.updateBatchById(webSiteConfigs);
        if (result) {
            return ResultGenerator.getSuccessResult(true);
        } else {
            return ResultGenerator.getFailResult("修改失败");
        }
    }

    @PostMapping("/configurations/userInfo")
    public Result userInfo(UserInfoParam userInfoParam) {
        ArrayList<WebSiteConfig> webSiteConfigs = new ArrayList<>();
        if (!userInfoParam.getYourEmail().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.EMAIL);
            webSiteConfig.setConfigValue(userInfoParam.getYourEmail());
            webSiteConfigs.add(webSiteConfig);
        }
        if (!userInfoParam.getYourAvatar().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.AVATAR);
            webSiteConfig.setConfigValue(userInfoParam.getYourAvatar());
            webSiteConfigs.add(webSiteConfig);
        }
        if (!userInfoParam.getYourName().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.NAME);
            webSiteConfig.setConfigValue(userInfoParam.getYourName());
            webSiteConfigs.add(webSiteConfig);
        }
        boolean result = webSiteConfigService.updateBatchById(webSiteConfigs);
        if (result) {
            return ResultGenerator.getSuccessResult(true);
        } else {
            return ResultGenerator.getFailResult("修改失败");
        }
    }

    @PostMapping("/configurations/footer")
    public Result footer(FooterInfoParam footerInfoParam) {
        ArrayList<WebSiteConfig> webSiteConfigs = new ArrayList<>();
        if (!footerInfoParam.getFooterAbout().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.FOOTER_ABOUT);
            webSiteConfig.setConfigValue(footerInfoParam.getFooterAbout());
            webSiteConfigs.add(webSiteConfig);
        }
        if (!footerInfoParam.getFooterICP().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.FOOTER_ICP);
            webSiteConfig.setConfigValue(footerInfoParam.getFooterICP());
            webSiteConfigs.add(webSiteConfig);
        }
        if (!footerInfoParam.getFooterPoweredBy().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.FOOTER_POWERED_BY);
            webSiteConfig.setConfigValue(footerInfoParam.getFooterPoweredBy());
            webSiteConfigs.add(webSiteConfig);
        }
        if (!footerInfoParam.getFooterCopyRight().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.FOOTER_COPY_RIGHT);
            webSiteConfig.setConfigValue(footerInfoParam.getFooterCopyRight());
            webSiteConfigs.add(webSiteConfig);
        }
        if (!footerInfoParam.getFooterPoweredByURL().isEmpty()) {
            WebSiteConfig webSiteConfig = new WebSiteConfig();
            webSiteConfig.setConfigId(WebSiteConfigId.FOOTER_POWERED_BY_URL);
            webSiteConfig.setConfigValue(footerInfoParam.getFooterPoweredByURL());
            webSiteConfigs.add(webSiteConfig);
        }
        boolean result = webSiteConfigService.updateBatchById(webSiteConfigs);
        if (result) {
            return ResultGenerator.getSuccessResult(true);
        } else {
            return ResultGenerator.getFailResult("修改失败");
        }
    }
}
