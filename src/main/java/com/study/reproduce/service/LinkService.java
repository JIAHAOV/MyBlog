package com.study.reproduce.service;

import com.study.reproduce.model.domain.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;

/**
* @author 18714
* @description 针对表【tb_link】的数据库操作Service
* @createDate 2022-05-10 20:10:16
*/
public interface LinkService extends IService<Link> {

    /**
     * 通过 PageQueryUtil 查询链接
     * @param queryUtil 用来查询的条件
     * @return PageResult
     */
    PageResult<Link> queryByPageUtil(PageQueryUtil queryUtil);
}
