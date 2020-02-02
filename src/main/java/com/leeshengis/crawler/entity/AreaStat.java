package com.leeshengis.crawler.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 地区统计信息
 *
 * @author lisheng
 */
@Data
@NoArgsConstructor
public class AreaStat {
    private int id;
    private String provinceName;
    private String cityName;
    private int confirmedCount;
    private int suspectedCount;
    private int curedCount;
    private int deadCount;
    private String comment;
    private int status;
    private LocalDateTime createTime;
    private int statisticsId;
}
