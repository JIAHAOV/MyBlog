package com.study.reproduce.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MakeDownUtil {
    /**
     * 将 MakeDown 格式的文本内容转换成 html 格式的文本内容
     * @param md MakeDown 格式的文本内容
     * @return html 格式的文本内容
     */
    public static String convert(String md) {
        //创建将输入文本解析器
        Parser parser = Parser.builder().build();
        //将输入文本解析为节点树。
        Node document = parser.parse(md);
        //创建html渲染器
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        //将节点树呈现为HTML。
        String html = renderer.render(document);
        return html;
    }
}
