package com.leeshengis.crawler.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leeshengis.crawler.common.Constants;
import com.leeshengis.crawler.common.Patterns;
import com.leeshengis.crawler.entity.AreaStat;
import com.leeshengis.crawler.entity.Statistics;
import com.leeshengis.crawler.service.CrawlerService;
import com.leeshengis.crawler.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * crawler
 *
 * @author lisheng
 */
@EnableScheduling
@Slf4j
@Component
public class JsoupCrawler {

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private EmailService emailService;

    @Value("${mail.send.to}")
    private String sendTo;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void crawler() {
        Document document = null;
        try {
            document = Jsoup.connect(Constants.CONNECT_URL).get();
        } catch (IOException e) {
            log.error("try connect {} failed", Constants.CONNECT_URL);
            return;
        }
        if (document == null) {
            log.error("document is null");
            return;
        }

        // 全局统计信息
        Element element = document.getElementById("getStatisticsService");
        Statistics statistics = element == null ? null : processStatistics(element.data());
        if (statistics == null) {
            log.error("statistics format is error");
            return;
        }
        if (crawlerService.isLatest(statistics)) {
            log.info("statistics is latest, no need to update");
            return;
        }

        element = document.getElementById("getAreaStat");
        List<AreaStat> areaStatList = element == null ? null : processAreaStat(element.data());
        try {
            crawlerService.save(statistics, areaStatList);
            new Thread(() -> this.sendEmail(statistics)).start();
        } catch (Exception e) {
            log.error("crawler data save failed.", e);
        }
    }

    private Statistics processStatistics(String statisticsStr) {
        String jsonStr = parse(statisticsStr, Patterns.STATISTICS);
        if (jsonStr == null || jsonStr.trim().length() == 0) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        Statistics statistics = Statistics.builder()
                .time(LocalDateTime.ofInstant(Instant.ofEpochMilli(jsonObject.getLong("modifyTime")), ZoneId.systemDefault()))
                .confirmedCount(jsonObject.getIntValue("confirmedCount"))
                .suspectedCount(jsonObject.getIntValue("suspectedCount"))
                .curedCount(jsonObject.getIntValue("curedCount"))
                .deadCount(jsonObject.getIntValue("deadCount"))
                .build();
        return statistics;
    }

    private List<AreaStat> processAreaStat(String areaStatStr) {
        String jsonStr = parse(areaStatStr, Patterns.AREA_STAT);
        if (jsonStr == null || jsonStr.trim().length() == 0) {
            return null;
        }
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        List<AreaStat> list = new ArrayList<>();
        jsonArray.stream().forEach(itemProvince -> {
            AreaStat province = JSONObject.parseObject(itemProvince.toString(), AreaStat.class);
            list.add(province);
            JSONArray cities = ((JSONObject) itemProvince).getJSONArray("cities");
            cities.stream()
                    .map(itemCity -> JSONObject.parseObject(itemCity.toString(), AreaStat.class))
                    .forEach(itemCity -> {
                        itemCity.setProvinceName(province.getProvinceName());
                        list.add(itemCity);
                    });

        });
        return list;
    }

    private String parse(String data, Pattern pattern) {
        Matcher matcher = pattern.matcher(data);
        return matcher.find() ? matcher.group() : null;
    }

    private void sendEmail(Statistics statistics) {
        String htmlContent = "<html>\n" +
                " <body>\n" +
                "   <div>\n" +
                "        <span>截至 " + statistics.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " 全国数据统计</span>\n" +
                "   </div>\n" +
                "   <ul>\n" +
                "       <li>" + statistics.getConfirmedCount() + "</strong><span>确诊病例</span></li>\n" +
                "       <li>" + statistics.getSuspectedCount() + "</strong><span>疑似病例</span></li>\n" +
                "       <li>" + statistics.getDeadCount() + "</strong><span>死亡人数</span></li>\n" +
                "       <li>" + statistics.getCuredCount() + "</strong><span>治愈人数</span></li>\n" +
                "   </ul>\n" +
                "   <div>\n" +
                "        <a href=\"" + Constants.CONNECT_URL + "\">详情请参见</a>\n" +
                "   </div>\n" +
                " </body>\n" +
                "</html>";
        emailService.sendHtmlEmail(sendTo.split(";"), Constants.EMAIL_TITLE, htmlContent);
    }
}
