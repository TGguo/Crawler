import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.naming.directory.SearchResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSoupBaiduSearcher implements Searcher {
    private static final Logger LOG = LoggerFactory
            .getLogger(JSoupBaiduSearcher.class);

    @Override
    public List<Webpage> search(String url) {
        List<Webpage> webpages = new ArrayList<>();
        try {

            Document document = Jsoup.connect(url).get();
            // System.out.println(document.text());
            String cssQuery = "html body div#wrapper div#wrapper_wrapper div#container div.head_nums_cont_outer div.head_nums_cont_inner div.nums ";
            LOG.debug("total cssQuery:" + cssQuery);
            Element totalElement = document.select(cssQuery).first();
            if (totalElement == null) {
                for(int i=1;i<11;i++ ){
                    document = Jsoup.connect(url).get();
                    totalElement = document.select(cssQuery).first();
                    System.out.println("第"+(i)+"次重试连接搜索");
                    i++;
                    if(totalElement != null){
                        break;
                    }else{
                        System.out.println("第"+i+"次暂未搜索到内容，正在重试");
                    }
                }
            }
            String totalText = totalElement.text();
            LOG.info("搜索结果：" + totalText);
            int start = 10;
            if (totalText.indexOf("约") != -1) {
                start = 15;
            }
            int total = Integer.parseInt(totalText.substring(start)
                    .replace(",", "").replace("个", ""));
            //System.out.println("搜索结果数：" + total);
            int len = 10;
            if (total < 1) {
                return null;
            }
            if (total < 10) {
                len = total;
            }
            for (int i = 0; i < len; i++) {
                String titleCssQuery = "html body div#wrapper div#wrapper_wrapper div#container div#content_left div#"
                        + (i + 1) + "  h3.t a";
                String summaryCssQuery = "html body div#wrapper div#wrapper_wrapper div#container div#content_left div#"
                        + (i + 1) + " div.c-abstract";
                LOG.debug("titleCssQuery:" + titleCssQuery);
                LOG.debug("summaryCssQuery:" + summaryCssQuery);
                String summaryCssQuery2 = "html body div#wrapper div#wrapper_wrapper div#container div#content_left div#"
                        + (i + 1) + " div.c-row";
                Element titleElement2 = document.select(summaryCssQuery2)
                        .first();
                Element titleElement = document.select(titleCssQuery).first();
                String href = "";
                String titleText = "";
                if (titleElement2 == null) {
                    titleText = titleElement.text();
                    href = titleElement.attr("href");
                    // System.out.println(titleText+"     链接："+href);
                    // System.out.println();
                } else {
                    // 处理百度百科
                    titleCssQuery = "html body div#wrapper div#wrapper_wrapper div#container div#content_left div#"
                            + (i + 1) + "  h3.t a";
                    summaryCssQuery = "html body div#wrapper div#wrapper_wrapper div#container div#content_left div#"
                            + (i + 1) + " div.c-row";
                    // System.out.println("处理百度百科 titleCssQuery:"+
                    // titleCssQuery);
                    LOG.debug("处理百度百科 summaryCssQuery:" + summaryCssQuery);
                    titleElement = document.select(titleCssQuery).first();
                    if (titleElement != null) {
                        titleText = titleElement.text();
                        href = titleElement.attr("href");
                    }
                }

                LOG.debug(titleText);
                Element summaryElement = document.select(summaryCssQuery)
                        .first();
                // 处理百度知道
                if (summaryElement == null) {
                    summaryCssQuery = summaryCssQuery.replace("div.c-abstract",
                            "font");
                    LOG.debug("处理百度知道 summaryCssQuery:" + summaryCssQuery);
                    summaryElement = document.select(summaryCssQuery).first();
                }
                String summaryText = "";
                if (summaryElement != null) {
                    summaryText = summaryElement.text();
                }
                // System.out.println("简介"+summaryText);
                LOG.debug(summaryText);

                if (titleText != null && !"".equals(titleText.trim())
                        && summaryText != null
                        && !"".equals(summaryText.trim())) {
                    Webpage webpage = new Webpage();
                    webpage.setTitle(titleText);
                    webpage.setUrl(href);
                    webpage.setSummary(summaryText);
                    if (href != null) {
                        String content = Tools.getHTMLContent(href);
                        webpage.setContent(content);
                    } else {
                        System.out.println("页面正确提取失败");
                    }
                    webpages.add(webpage);
                } else {
                    //System.out.println("获取搜索结果列表项出错:" + titleText + "-" + summaryText);
                }
            }

        } catch (IOException ex) {
            System.out.println("搜索出错");
        }
        return webpages;
    }

    public static void main(String[] args) {
        System.out.print("请输入你要查询的内容：");
        Scanner StringSearch = new Scanner(System.in);
        String Search = StringSearch.next();
        String url = "http://www.baidu.com/s?pn=0&wd=" + Search;
        System.out.println(url);

        Searcher searcher = new JSoupBaiduSearcher();
        List<Webpage> webpages = searcher.search(url);
        if (webpages != null) {
            int i = 1;
            System.out.println("百度搜索：");
            for (Webpage webpage : webpages) {
                System.out.println("搜索结果" + (i++) + "：");
                System.out.println("标题：" + webpage.getTitle());
                System.out.println("URL：" + webpage.getUrl());
                System.out.println("摘要：" + webpage.getSummary());
            }
        } else {
            LOG.error("没有搜索到结果");
        }
    }

    @Override
    public SearchResult search(String keyword, int page) {
        // TODO Auto-generated method stub
        return null;
    }
}
