package com.study.reproduce.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.model.ov.BlogInfo;
import com.study.reproduce.model.ov.BlogOV;
import com.study.reproduce.service.SearchService;
import com.study.reproduce.utils.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.*;

import static com.study.reproduce.constant.EsConstant.*;
import static com.study.reproduce.utils.MakeDownElementRegex.*;

@Slf4j
@Service
public class ElasticSearchService implements SearchService {

    public static final int PAGE_SIZE = 10;

    @Resource
    RestHighLevelClient elasticsearchClient;

    String clearContent(String content) {
        content = content.replaceAll(HTML_ELEMENT_REGEX, "");
        content = content.replaceAll(CODE_ELEMENT_REGEX, "");
        content = content.replaceAll(IMAGE_ELEMENT_REGEX, "");
        content = content.replaceAll("#", "");
        content = content.replaceAll(">", "");
        return content;
    }

    public List<BlogOV> searchByContent(String keyword) {
        if (StrUtil.isBlank(keyword)) {
            return Collections.emptyList();
        }
        TermQueryBuilder queryBuilder = QueryBuilders
                .termQuery(ES_SEARCH_CONTENT, keyword);
        HighlightBuilder highlightBuilder = new HighlightBuilder()//创建highlightBuilder
                .preTags("<foot color='red'>")//指定前缀
                .postTags("</foot>")//指定后缀
                .field(ES_SEARCH_CONTENT);//指定字段
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .highlighter(highlightBuilder)//在构建 SearchSourceBuilder 时调用 highlighter() 方法，传入 highlightBuilder
                .query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest
                .source(sourceBuilder)
                .indices(ES_INDEX_NAME);
        SearchResponse response = null;
        ArrayList<BlogOV> list = new ArrayList<>();
        try {
            response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            for (SearchHit hit : hits) {
                BlogOV blogOv = JSONUtil.toBean(hit.getSourceAsString(), BlogOV.class);
                blogOv.setBlogId(Long.parseLong(hit.getId()));
                list.add(blogOv);
                System.out.println(hit.getHighlightFields());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BlogOV> searchByElement(String keyword) {
        if (StrUtil.isBlank(keyword)) {
            return Collections.emptyList();
        }
        TermQueryBuilder queryBuilder = QueryBuilders
                .termQuery(ES_SEARCH_ELEMENT, keyword);
        HighlightBuilder highlightBuilder = new HighlightBuilder()//创建highlightBuilder
                .preTags("<foot color='red'>")//指定前缀
                .postTags("</foot>")//指定后缀
                .field("title");//指定字段
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .highlighter(highlightBuilder)//在构建 SearchSourceBuilder 时调用 highlighter() 方法，传入 highlightBuilder
                .query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest
                .source(sourceBuilder)
                .indices(ES_INDEX_NAME);
        SearchResponse response = null;
        ArrayList<BlogOV> list = new ArrayList<>();
        try {
            response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            for (SearchHit hit : hits) {
                BlogOV blogOv = JSONUtil.toBean(hit.getSourceAsString(), BlogOV.class);
                blogOv.setBlogId(Long.parseLong(hit.getId()));
                list.add(blogOv);
                System.out.println(hit.getHighlightFields());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BlogOV> search(String keyword) {
        List<BlogOV> content = searchByContent(keyword);
        List<BlogOV> element = searchByElement(keyword);
        TreeSet<BlogOV> searchResult = new TreeSet<>((o1, o2) -> Math.toIntExact(o1.getBlogId() - o2.getBlogId()));
        searchResult.addAll(content);
        searchResult.addAll(element);
        return new ArrayList<>(searchResult);
    }

    @Override
    public PageResult<BlogInfo> search(Integer page, String keyword) {
        if (StrUtil.isBlank(keyword)) {
            return new PageResult<>(0, 0, 1, Collections.emptyList());
        }
        TermQueryBuilder queryBuilder = QueryBuilders
                .termQuery(ES_SEARCH_ELEMENT, keyword);
        HighlightBuilder highlightBuilder = new HighlightBuilder()//创建highlightBuilder
                .preTags("<foot color='red'>")//指定前缀
                .postTags("</foot>")//指定后缀
                .field("title");//指定字段
        int form = (page - 1) * PAGE_SIZE;

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .highlighter(highlightBuilder)//在构建 SearchSourceBuilder 时调用 highlighter() 方法，传入 highlightBuilder
                .query(queryBuilder)
                .from(form)
                .size(PAGE_SIZE);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest
                .source(sourceBuilder)
                .indices(ES_INDEX_NAME);
        SearchResponse response = null;
        ArrayList<BlogOV> list = new ArrayList<>();
        long total = 0;
        try {
            response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            total = response.getHits().getTotalHits().value;
            for (SearchHit hit : hits) {
                BlogOV blogOv = JSONUtil.toBean(hit.getSourceAsString(), BlogOV.class);
                blogOv.setBlogId(Long.parseLong(hit.getId()));
                list.add(blogOv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<BlogInfo> blogInfos = new ArrayList<>();
        for (BlogOV blogOV : list) {
            BlogInfo blogInfo = new BlogInfo();
            blogInfo.setBlogId(blogOV.getBlogId());
            blogInfo.setBlogTitle(blogOV.getTitle());
            blogInfo.setBlogCoverImage(blogOV.getBlogCoverImage());
            blogInfo.setBlogCategoryName(blogOV.getCategory());
            blogInfo.setBlogStatus(1);
            blogInfo.setBlogViews(0L);
            blogInfo.setCreateTime(blogOV.getCreateTime());
            blogInfos.add(blogInfo);
        }
        return new PageResult<>(total, PAGE_SIZE,
                page, blogInfos);
    }

    @Override
    public boolean updateBlog(Blog blog) throws IOException {
        //创建 bulk 请求，请求中可以添加多个其他请求
        BulkRequest bulkRequest = new BulkRequest();
        BlogOV blogOV = new BlogOV();

        blogOV.setBlogId(blog.getBlogId());
        blogOV.setTitle(blog.getBlogTitle());
        blogOV.setCategory(blog.getBlogCategoryName());
        blogOV.setTags(blog.getBlogTags());
        blogOV.setBlogCoverImage(blog.getBlogCoverImage());
        blogOV.setCreateTime(blog.getCreateTime());

        String content = clearContent(blog.getBlogContent());

        blogOV.setContent(content.toLowerCase(Locale.ROOT));
        String jsonStr = JSONUtil.toJsonStr(blogOV);
        //创建 IndexRequest 请求
        IndexRequest indexRequest = new IndexRequest();
        indexRequest
                .index("myblog") //指定索引
                .id(blogOV.getBlogId().toString()) //指定id
                .source(jsonStr, XContentType.JSON);//需要插入的数据，注意不要和修改字段的重载方法搞混了
        bulkRequest.add(indexRequest);
        //批量操作调用 bulk() 方法
        BulkResponse response = elasticsearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return "OK".equals(response.status().toString());
    }

    @Override
    public boolean deleteBlog(List<Long> ids) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (Long id : ids) {
            DeleteRequest request = new DeleteRequest();
            request.index(ES_INDEX_NAME)
                    .id(String.valueOf(id));
            bulkRequest.add(request);
        }
        BulkResponse responses = elasticsearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return "OK".equals(responses.status().toString());
    }
}
