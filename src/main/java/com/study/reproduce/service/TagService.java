package com.study.reproduce.service;

import com.study.reproduce.model.domain.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;


/**
* @author 18714
* @description 针对表【tb_blog_tag】的数据库操作Service
* @createDate 2022-05-10 19:48:27
*/
public interface TagService extends IService<Tag> {
    Tag getTagByName(String tagName);

    /**
     * 通过 PageQueryUtil 查询 tag
     * @param pageQueryUtil 查询条件
     * @return PageResult
     */
    PageResult<Tag> queryByPageUtil(PageQueryUtil pageQueryUtil);

    /**
     * 新增标签
     * @param tagName 标签名称
     * @return 新增结果
     */
    boolean saveTag(String tagName);

    /**
     * 批量删除标签，有关联关系的不删除
     * @param tagIds 需要删除的标签
     * @return 是否成功
     */
    boolean removeBatchTags(Integer[] tagIds);
}
