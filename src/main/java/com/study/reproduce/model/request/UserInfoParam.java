package com.study.reproduce.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoParam {
    private String yourAvatar;
    private String yourEmail;
    private String yourName;
}
