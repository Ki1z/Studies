package com.sky.mapper;

import com.sky.dto.OrderReportDTO;
import com.sky.dto.TurnoverDTO;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    /**
     * 统计销量Top10
     * @return 销量Top10
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return 用户统计数据
     */
    @Select("select * from user where create_time between #{begin} and #{end}")
    List<User> getUsersByCreateTime(LocalDate begin, LocalDate end);

    /**
     * 根据日期统计用户数量
     * @param begin
     * @return
     */
    @Select("select count(*) from user where create_time < #{begin}")
    Integer getTotalBeforeBeginDate(LocalDate begin);

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    List<TurnoverDTO> getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取指定日期的订单
     * @param begin
     * @param end
     * @return
     */
    List<OrderReportDTO> getOrdersByDate(LocalDate begin, LocalDate end);
}
