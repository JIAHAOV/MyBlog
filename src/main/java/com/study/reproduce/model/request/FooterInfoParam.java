package com.study.reproduce.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FooterInfoParam {
    private String footerAbout;
    private String footerCopyRight;
    private String footerICP;
    private String footerPoweredBy;
    private String footerPoweredByURL;
}
