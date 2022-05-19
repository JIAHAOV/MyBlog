package com.study.reproduce.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class URIUtil {
    /**
     * 获取一个标准的显示图片的URL
     * @param uri 基础url
     * @param filename 文件名
     * @return 结果
     */
    public static URI getResponseURI(URI uri, String filename) {
        URI result = null;
        try {
            result = new URI(uri.getScheme(), null, uri.getHost(),
                    uri.getPort(), "/image/blogs/" + filename, null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
}
