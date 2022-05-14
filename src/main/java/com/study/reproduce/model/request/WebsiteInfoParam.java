package com.study.reproduce.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteInfoParam {
    private String websiteDescription;
    private String websiteIcon;
    private String websiteLogo;
    private String websiteName;
}
