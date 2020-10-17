package com.example.devjk.github_crawler.controller;

import com.example.devjk.github_crawler.service.interfaces.GithubCrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GithubCrawlerController {

    @Autowired
    private GithubCrawlerService githubCrawlerService;

    @RequestMapping("/")
    @ResponseBody
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index.html");
        return mv;
    }

    @RequestMapping("/crawler")
    @ResponseBody
    public Map<String, Boolean> crawler(@RequestParam("githubId") String githubId) {
        Map<String, Boolean> ret = new HashMap<>();

        try{
            ret.put("result", githubCrawlerService.execService(githubId));
        }catch(Exception e){
            ret.put("result", false);
        }

        return ret;
    }

}
