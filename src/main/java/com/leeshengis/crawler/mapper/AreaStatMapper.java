package com.leeshengis.crawler.mapper;

import com.leeshengis.crawler.entity.AreaStat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lisheng
 */
@Mapper
@Repository
public interface AreaStatMapper {

    /**
     * batch insert
     *
     * @param areaStatList
     * @return
     */
    @Insert("<script>"
            + "insert into tb_area_stat(province_name, city_name, confirmed_count, suspected_count, "
            + "cured_count, dead_count, comment, statistics_id) "
            + " values "
            + " <foreach collection='list' item='areaStat' separator=','>"
            + "    (#{areaStat.provinceName}, #{areaStat.cityName}, #{areaStat.confirmedCount}, "
            + "     #{areaStat.suspectedCount}, #{areaStat.curedCount}, #{areaStat.deadCount},"
            + "     #{areaStat.comment}, #{areaStat.statisticsId})"
            + " </foreach>"
            + "</script>")
    int batchInsert(List<AreaStat> areaStatList);

    /**
     * update status
     *
     * @return
     */
    @Update("update tb_area_stat set status=1 where status=0")
    int updateStatus();
}
