import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class Test {
    private static final Logger LOG = LoggerFactory
            .getLogger(JSoupBaiduSearcher.class);
    public static void main(String[] args) {
        System.out.print("请输入你要查询的内容：");
        Scanner StringSearch = new Scanner(System.in);
        String Search = StringSearch.next();
        String url = "http://www.baidu.com/s?pn=0&wd=" + Search;

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
}
