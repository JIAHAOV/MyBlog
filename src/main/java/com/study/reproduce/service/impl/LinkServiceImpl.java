package com.study.reproduce.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reproduce.model.domain.Link;
import com.study.reproduce.service.LinkService;
import com.study.reproduce.mapper.LinkMapper;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 18714
* @description 针对表【tb_link】的数据库操作Service实现
* @createDate 2022-05-10 20:10:16
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService{
    @Resource
    LinkMapper linkMapper;

    @Override
    public PageResult<Link> queryByPageUtil(PageQueryUtil queryUtil) {
        Page<Link> page = new Page<>(queryUtil.getPage(), queryUtil.getLimit());
        page.addOrder(OrderItem.desc("link_id"));
        Page<Link> linkPage = linkMapper.selectPage(page, null);
        Long totalCount = linkMapper.selectCount(null);
        PageResult<Link> pageResult = new PageResult<>();
        pageResult.setList(linkPage.getRecords());
        pageResult.setCurrPage(queryUtil.getPage());
        pageResult.setPageSize(queryUtil.getLimit());
        pageResult.setTotalCount(totalCount);
        return pageResult;
    }

    @Override
    public Map<Integer, List<Link>> getLinksForLinkPage() {
        List<Link> linkList = linkMapper.selectList(null);
        Map<Integer, List<Link>> linkMap = linkList.stream().collect(Collectors.groupingBy(Link::getLinkType));
        return linkMap;
    }
}




