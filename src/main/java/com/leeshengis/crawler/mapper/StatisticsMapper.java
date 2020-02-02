package com.leeshengis.crawler.mapper;

import com.leeshengis.crawler.entity.Statistics;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lisheng
 */
@Mapper
@Repository
public interface StatisticsMapper {

    /**
     * insert record
     *
     * @param statistics
     * @return
     */
    @Insert("insert into tb_statistics(time, confirmed_count, suspected_count, cured_count, dead_count) values(#{time}, #{confirmedCount}, #{suspectedCount}, #{curedCount}, #{deadCount})")
    @SelectKey(statement = "select LAST_INSERT_ID()", before = false, keyProperty = "id", resultType = int.class)
    int insert(Statistics statistics);

    /**
     * get latest statistics
     *
     * @return
     */
    @Select("select * from tb_statistics where status=0")
    List<Statistics> latest();

    /**
     * update record
     *
     * @return
     */
    @Update("update tb_statistics set status=1 where status=0")
    int updateStatus();
}
