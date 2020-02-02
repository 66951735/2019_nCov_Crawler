package com.leeshengis.crawler.service;

import com.leeshengis.crawler.entity.AreaStat;
import com.leeshengis.crawler.entity.Statistics;
import com.leeshengis.crawler.mapper.AreaStatMapper;
import com.leeshengis.crawler.mapper.StatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lisheng
 */
@Service
@Slf4j
public class CrawlerService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private AreaStatMapper areaStatMapper;

    /**
     * save data
     *
     * @param statistics
     * @param areaStatList
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(Statistics statistics, List<AreaStat> areaStatList) {
        if (statistics == null || CollectionUtils.isEmpty(areaStatList)) {
            return;
        }
        statisticsMapper.updateStatus();
        statisticsMapper.insert(statistics);
        areaStatList.stream().forEach(areaStat -> areaStat.setStatisticsId(statistics.getId()));
        areaStatMapper.updateStatus();
        areaStatMapper.batchInsert(areaStatList);
    }

    /**
     * get latest statistics
     *
     * @param current
     * @return
     */
    public boolean isLatest(Statistics current) {
        List<Statistics> list = statisticsMapper.latest();
        if (list == null || list.size() == 0) {
            return false;
        }
        Statistics latest = list.get(0);
        return latest.getTime().isEqual(current.getTime());
    }


}
