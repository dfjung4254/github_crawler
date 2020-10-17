package com.example.devjk.github_crawler.service.impl;

import com.example.devjk.github_crawler.service.interfaces.GithubCrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service("githubCrawlerService")
public class GithubCrawlerServiceImpl implements GithubCrawlerService {

    @Autowired
    ServletContext servletContext;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final String GITHUBURL = "https://github.com/";
    private final String REPOSITORYCOUNT_QUERY = "h2[class=f4 text-normal mb-2]";
    private final String REPOSITORYTITLES_QUERY = "div[class=d-flex width-full flex-items-center position-relative]";
    private final String REPOSITORYNAME_QUERY = "span[class=p-nickname vcard-username d-block]";
    private final String PATH = "crawledFile/";

    public String[] crawlGithub(String githubId) {

        StringBuilder crawledContents = new StringBuilder();
        String[] ret = new String[2];
        try{

            Document siteDocument = Jsoup.connect(GITHUBURL + githubId).get();
            writeRepositoryCount(siteDocument, crawledContents);
            writeRepositoryTitles(siteDocument, crawledContents);
            ret[0] = getRepositoryName(siteDocument);
            ret[1] = crawledContents.toString();

        }catch(IOException ioException){
            logger.info(ioException.getMessage());
            ret = null;
        }catch(Exception e){
            logger.info(e.getMessage());
            logger.info(e.getLocalizedMessage());
            throw e;
        }
        return ret;
    }

    private void writeRepositoryTitles(Document document, StringBuilder crawledContents){
        Elements repositoryPageElements = document.select(REPOSITORYTITLES_QUERY);
        for(Element repositoryElement : repositoryPageElements){
            crawledContents.append(repositoryElement.text());
            crawledContents.append('\n');
        }
    }

    private void writeRepositoryCount(Document document, StringBuilder crawledContents){
        Element repositoryCountingElement = document.selectFirst(REPOSITORYCOUNT_QUERY);
        crawledContents.append(repositoryCountingElement.text());
        crawledContents.append('\n');
    }

    private String getRepositoryName(Document document){
        return document.selectFirst(REPOSITORYNAME_QUERY).text().trim();
    }

    private boolean saveGithubFile(String title, String contents) {

        boolean ret;
        try{

            File file = new File(PATH + title + ".info");
            logger.info(file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            writer.write(contents);
            writer.close();
            ret = true;

        }catch(Exception e){
            logger.info(e.getMessage());
            logger.info(e.getLocalizedMessage());
            ret = false;
        }

        return ret;
    }

    @Override
    public boolean execService(String githubId) {

        boolean ret;
        try{
            String[] crawledContents = crawlGithub(githubId);

            if(crawledContents == null){
                throw new Exception();
            }

            ret = saveGithubFile(crawledContents[0], crawledContents[1]);
        }catch(Exception e){
            logger.info(e.getMessage());
            logger.info(e.getLocalizedMessage());
            ret = false;
        }

        return ret;
    }
}
