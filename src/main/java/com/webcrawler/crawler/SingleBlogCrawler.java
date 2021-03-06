package com.webcrawler.crawler;

import com.data.access.CallerDAO;
import com.data.access.CallerDAOImpl;
import com.tags.extract.IdentifyTagLines;
import com.wepage.info.WebPageInfo;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class SingleBlogCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern
            .compile(".*(\\.(css|js|bmp|gif|jpe?g"
                    + "|png|tiff?|mid|mp2|mp3|mp4"
                    + "|wav|avi|mov|mpeg|ram|m4v|pdf"
                    + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches();// &&
        // href.startsWith("http://blog.vogella.com/");
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);
        CallerDAO dao = new CallerDAOImpl();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            List<WebURL> links = htmlParseData.getOutgoingUrls();
            Document doc = Jsoup.parse(html);
            WebPageInfo info = new WebPageInfo();
            info.setUrl(url);
            info.setTitle(htmlParseData.getTitle());
            System.out.println(htmlParseData.getTitle());
            IdentifyTagLines tagging = new IdentifyTagLines();
            tagging.setDoc(doc);
            tagging.setWebPageInfo(info);

            try {
                info = tagging.process();
            } catch (IOException e) {

                e.printStackTrace();
            }

            dao.write(info);

            System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());

        }
    }
}
