package com.study.reproduce.service;

import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.ov.BlogInfo;
import com.study.reproduce.utils.PageResult;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    PageResult<BlogInfo> search(Integer page, String keyword);

    boolean updateBlog(Blog blog) throws IOException;

    boolean deleteBlog(List<Long> ids) throws IOException;
}
