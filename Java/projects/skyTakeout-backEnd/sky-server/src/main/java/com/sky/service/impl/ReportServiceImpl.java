package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.OrderReportDTO;
import com.sky.dto.TurnoverDTO;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.exception.ExportFailedException;
import com.sky.exception.FormValueIsNullException;
import com.sky.mapper.ReportMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.service.UserService;
import com.sky.vo.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Double.NaN;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    /**
     * 统计销量Top10
     * @return 销量Top10
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        if (begin == null || end == null)
            throw new FormValueIsNullException(MessageConstant.DATE_ARE_REQUIRED);
        return reportMapper.getSalesTop10(begin, end);
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        if (begin == null || end == null)
            throw new FormValueIsNullException(MessageConstant.DATE_ARE_REQUIRED);
        // 获取指定日期之间的用户数据
        List<User> users = reportMapper.getUsersByCreateTime(begin, end);
        // 将用户数据按照日期为键，生成一个Map<LocalDate, List<User>>
        Map<LocalDate, List<User>> userMap = users.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getCreateTime().toLocalDate()
                ));
        // 获取begin之前的用户总量
        Integer total = reportMapper.getTotalBeforeBeginDate(begin);
        // 获取所有日期
        List<LocalDate> dateList = getBetweenDates(begin, end);
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        // 遍历日期
        for (LocalDate date : dateList) {
            Integer newUser = 0;
            // 判断当前日期是否包含用户数据
            if (userMap.containsKey(date)) {
                // 包含，则表示是新增用户，对应List<User>的长度即代表新增用户数量
                newUser = userMap.get(date).size();
                total += newUser;
            }
            // 将数据添加到对应的列表中
            newUserList.add(newUser);
            totalUserList.add(total);
        }
        // 转换为VO对象并返回
        return UserReportVO.builder()
                .dateList(
                        dateList.stream()
                                .map(LocalDate::toString)
                                .collect(Collectors.joining(","))
                )
                .newUserList(
                        newUserList.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","))
                )
                .totalUserList(
                        totalUserList.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","))
                )
                .build();
    }

    /**
     * 获取两个日期之间的所有日期，包括begin和end
     * @param begin
     * @param end
     * @return
     */
    private List<LocalDate> getBetweenDates(LocalDate begin, LocalDate end) {
        List<LocalDate> result = new ArrayList<>();
        result.add(begin);
        LocalDate temp = begin;
        while (!temp.equals(end)) {
            temp = temp.plusDays(1);
            result.add(temp);
        }
        return result;
    }

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 获取指定日期的营业额数据
        List<TurnoverDTO> turnoverDTOS = reportMapper.getTurnoverStatistics(begin, end);
        // 获取所有日期
        List<LocalDate> dateList = getBetweenDates(begin, end);
        List<BigDecimal> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            if (!turnoverDTOS.isEmpty() && turnoverDTOS.get(0).getDate().equals(date)) {
                turnoverList.add(turnoverDTOS.get(0).getTurnover());
                turnoverDTOS.remove(0);
            } else {
                turnoverList.add(BigDecimal.ZERO);
            }
        }
        // 封装VO对象并返回
        return TurnoverReportVO.builder()
                .dateList(
                        dateList.stream()
                                .map(LocalDate::toString)
                                .collect(Collectors.joining(","))
                )
                .turnoverList(
                        turnoverList.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","))
                )
                .build();
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // 获取指定日期的订单数据
        List<OrderReportDTO> orderList = reportMapper.getOrdersByDate(begin, end);
        // 生成所有日期
        List<LocalDate> dateList = getBetweenDates(begin, end);
        // 1. 预处理：将订单数据按日期分组
        Map<LocalDate, List<OrderReportDTO>> orderMap = orderList.stream()
                .collect(Collectors.groupingBy(OrderReportDTO::getDate));
        // 2. 初始化结果列表和总计变量
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        Integer totalOrderCount = 0;
        Integer totalValidOrderCount = 0;
        // 3. 单层循环遍历日期，直接从 Map 中获取当天的订单数据
        for (LocalDate date : dateList) {
            Integer dailyOrderCount = 0;
            Integer dailyValidOrderCount = 0;

            if (orderMap.containsKey(date) && !orderMap.get(date).isEmpty()) {
                List<OrderReportDTO> dailyOrders = orderMap.get(date);
                // 计算当天的总订单数
                dailyOrderCount = dailyOrders.stream()
                        .mapToInt(OrderReportDTO::getCount)
                        .sum();
                // 计算当天的有效订单数（状态为 COMPLETED）
                dailyValidOrderCount = dailyOrders.stream()
                        .filter(order -> Objects.equals(order.getStatus(), Orders.COMPLETED))
                        .mapToInt(OrderReportDTO::getCount)
                        .sum();
            }
            orderCountList.add(dailyOrderCount);
            validOrderCountList.add(dailyValidOrderCount);
            // 累加总计
            totalOrderCount += dailyOrderCount;
            totalValidOrderCount += dailyValidOrderCount;
        }
        // 计算订单完成率
        Double orderCompletionRate = totalValidOrderCount.doubleValue() / totalOrderCount;
        // 封装VO对象并返回
        return OrderReportVO.builder()
                .dateList(
                        dateList.stream()
                                .map(LocalDate::toString)
                                .collect(Collectors.joining(","))
                )
                .orderCountList(
                        orderCountList.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","))
                )
                .validOrderCountList(
                        validOrderCountList.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","))
                )
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 导出数据
     * @param response
     */
    @Override
    public void export(HttpServletResponse response) {
        // 加载模板
        ClassPathResource template = new ClassPathResource("/template/运营数据报表模板.xlsx");
        try {
            Workbook workbook = new XSSFWorkbook(template.getInputStream());
            Sheet sheet = workbook.getSheet("Sheet1");
            // 设置时间段
            LocalDate begin = LocalDate.now().minusDays(30);
            LocalDate end = LocalDate.now().minusDays(1);
            sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);
            // 设置概览数据
            List<TurnoverDTO> turnoverDTOS = reportMapper.getTurnoverStatistics(begin, end);
            // 营业额
            Double totalTurnover = turnoverDTOS.stream()
                    // 获取营业额
                    .map(TurnoverDTO::getTurnover)
                    // BigDecimal转换为Double
                    .mapToDouble(BigDecimal::doubleValue)
                    .sum();
            sheet.getRow(3).getCell(2).setCellValue(totalTurnover);
            // 订单完成率
            // 获取指定日期的订单数据
            List<OrderReportDTO> orderList = reportMapper.getOrdersByDate(begin, end);
            // 统计总订单数量
            Integer totalOrderCount = orderList.stream()
                    .mapToInt(OrderReportDTO::getCount)
                    .sum();
            // 统计有效订单数量
            Integer validOrderCount = orderList.stream()
                    .filter(order -> Objects.equals(order.getStatus(), Orders.COMPLETED))
                    .mapToInt(OrderReportDTO::getCount)
                    .sum();
            Double orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            // 保留2位小数
            String orderCompletionRateStr = String.format("%.2f", orderCompletionRate * 100);
            sheet.getRow(3).getCell(4).setCellValue(orderCompletionRateStr + "%");
            // 新增用户数量
            List<User> userList = reportMapper.getUsersByCreateTime(begin, end);
            Integer newUserCount = userList.size();
            sheet.getRow(3).getCell(6).setCellValue(newUserCount);
            // 有效订单数量
            sheet.getRow(4).getCell(2).setCellValue(validOrderCount);
            // 平均客单价
            Double avgOrderPrice = totalTurnover / validOrderCount;
            String avgOrderPriceStr = String.format("%.2f", avgOrderPrice);
            sheet.getRow(4).getCell(4).setCellValue(avgOrderPriceStr);

            // 明细数据
            // 日期列表
            List<LocalDate> dateList = getBetweenDates(begin, end);
            // 生成一个Map<date, BusinessDataVO>
            Map<LocalDate, BusinessDataVO> map = dateList.stream()
                    .collect(Collectors.toMap(
                            key -> key,
                            value -> new BusinessDataVO()
                    ));
            // 遍历map，将对应日期的数据填充到BusinessDataVO中
            for (Map.Entry<LocalDate, BusinessDataVO> entry : map.entrySet()) {
                LocalDate date = entry.getKey();
                BusinessDataVO businessDataVO = entry.getValue();
                // 获取指定日期的营业额
                Double turnover = turnoverDTOS.stream()
                        .filter(turnoverDTO -> Objects.equals(turnoverDTO.getDate(), date))
                        .map(TurnoverDTO::getTurnover)
                        .findFirst()
                        .orElse(BigDecimal.ZERO)
                        .doubleValue();
                businessDataVO.setTurnover(turnover);
                // 获取指定日期的订单数据
                totalOrderCount = orderList.stream()
                        .filter(order -> Objects.equals(order.getDate(), date))
                        .mapToInt(OrderReportDTO::getCount)
                        .sum();
                validOrderCount = orderList.stream()
                        .filter(order -> Objects.equals(order.getDate(), date) && Objects.equals(order.getStatus(), Orders.COMPLETED))
                        .mapToInt(OrderReportDTO::getCount)
                        .sum();
                orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
                businessDataVO.setOrderCompletionRate(orderCompletionRate.isNaN() ? 0.0 : orderCompletionRate);
                businessDataVO.setValidOrderCount(validOrderCount);
                // 获取指定日期的新增用户数量
                newUserCount = (int) userList.stream()
                        .filter(user -> user.getCreateTime().toLocalDate().equals(date))
                        .count();
                businessDataVO.setNewUsers(newUserCount);
                // 平均客单价
                avgOrderPrice = turnover / validOrderCount;
                businessDataVO.setUnitPrice(avgOrderPrice.isNaN() ? 0.0 : avgOrderPrice);
            }
            // 填充数据到sheet中，工作区域是B8到G37
            for (int i = 0; i < 30; i++) {
                // 日期
                sheet.getRow(i + 7).getCell(1).setCellValue(dateList.get(i).toString());
                // 营业额
                sheet.getRow(i + 7).getCell(2).setCellValue(map.get(dateList.get(i)).getTurnover());
                // 有效订单数量
                sheet.getRow(i + 7).getCell(3).setCellValue(map.get(dateList.get(i)).getValidOrderCount());
                // 订单完成率
                sheet.getRow(i + 7).getCell(4).setCellValue(map.get(dateList.get(i)).getOrderCompletionRate());
                // 平均客单价
                sheet.getRow(i + 7).getCell(5).setCellValue(map.get(dateList.get(i)).getUnitPrice());
                // 新增用户数量
                sheet.getRow(i + 7).getCell(6).setCellValue(map.get(dateList.get(i)).getNewUsers());
            }

            // 输出流
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            // 关闭流
            outputStream.close();
            workbook.close();
        } catch (IOException e) {
            throw new ExportFailedException(MessageConstant.EXPORT_EXCEL_FAILED);
        }
    }
}
