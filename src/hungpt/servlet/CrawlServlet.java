package hungpt.servlet;

import hungpt.crawler.FUCrawler;
import hungpt.utils.CrawlHelper;
import hungpt.utils.HttpHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class CrawlServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void init() throws ServletException {
        try{
            System.out.println("FU_Crawler Init");
            CrawlHelper.parseHTML(HttpHelper.getContent("https://hcmuni.fpt.edu.vn/tin-tuc"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
