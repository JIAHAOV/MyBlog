package com.study.reproduce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.Tag;
import com.study.reproduce.mapper.TagMapper;
import com.study.reproduce.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author 18714
* @description 针对表【tb_blog_tag】的数据库操作Service实现
* @createDate 2022-05-10 19:48:27
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {

}




