package com.yjq.knowledge.util;

import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail;

import java.util.List;

import rx.schedulers.Schedulers;

/**
 * 文件： HtmlUtil
 * 描述：
 * 作者： YangJunQuan   2018/1/11.
 */

public class HtmlUtil {

    //css样式,隐藏header
    private static final String HIDE_HEADER_STYLE = "<style>div.headline{display:none;}</style>";
    //css style tag,需要格式化
    private static final String NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\"/>";
    // js script tag,需要格式化
    private static final String NEEDED_FORMAT_JS_TAG = "<script src=\"%s\"></script>";
    public static final String MIME_TYPE = "text/html; charset=utf-8";
    public static final String ENCODING = "utf-8";

    private HtmlUtil() {
    }

    /**
     * 根据css链接Url引入单个css样式文件到webView中
     *
     * @param url
     * @return
     */
    public static String createCssTag(String url) {
        return String.format(NEEDED_FORMAT_CSS_TAG, url);
    }


    /**
     * 根据多个css链接Url引入多个css样式文件到WebView中
     *
     * @param urls
     * @return
     */
    public static String createCssTag(List<String> urls) {
        final StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(createCssTag(url));
        }
        return sb.toString();
    }

    /**
     * 根据js链接生成Script标签
     *
     * @param url
     * @return
     */
    public static String createJsTag(String url) {
        return String.format(NEEDED_FORMAT_JS_TAG, url);
    }


    /**
     * 根据多个js链接生成Script标签
     *
     * @param urls List<String>
     * @return String
     */
    public static String createJsTag(List<String> urls) {
        final StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(createJsTag(url));
        }
        return sb.toString();
    }

    /**
     * 根据css标签，html的<body/>部分，js标签 生成完整的HTML文档
     *
     * @param html
     * @param css
     * @param js
     * @return
     */
    private static String createHtmlData(String html, String css, String js) {
        return css.concat(HIDE_HEADER_STYLE).concat(html).concat(js);
    }


    public static String createHtmlData(ZhihuNewsDetail newsDetail, boolean isNight) {
        final String css = HtmlUtil.createCssTag(newsDetail.getCss());
        final String js = HtmlUtil.createJsTag(newsDetail.getJs());
        final String body = handleHtml(newsDetail.getBody(), isNight).toString();
        return createHtmlData(body, css, js);
    }

    public static StringBuffer handleHtml(String body, boolean isNight) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/detail.css\" ></head>");
        stringBuffer.append(isNight ? "<body class=\"night\">" : "<body>");
        stringBuffer.append(body);
        stringBuffer.append("</body></html>");
        return stringBuffer;
    }
}
