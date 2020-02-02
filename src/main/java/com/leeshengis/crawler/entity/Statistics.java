package com.leeshengis.crawler.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 全局统计信息
 *
 * @author lisheng
 */
@Data
@Builder
public class Statistics {
    private int id;
    private LocalDateTime time;
    private int confirmedCount;
    private int suspectedCount;
    private int curedCount;
    private int deadCount;
    private LocalDateTime createTime;
    private int status;
}
