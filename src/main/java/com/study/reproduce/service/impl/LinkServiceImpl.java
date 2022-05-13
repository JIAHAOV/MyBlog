package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.Link;
import com.study.reproduce.service.LinkService;
import com.study.reproduce.mapper.LinkMapper;
import org.springframework.stereotype.Service;

/**
* @author 18714
* @description 针对表【tb_link】的数据库操作Service实现
* @createDate 2022-05-10 20:10:16
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService{

}




