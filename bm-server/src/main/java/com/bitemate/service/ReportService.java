package com.bitemate.service;

import com.bitemate.vo.OrderReportVO;
import com.bitemate.vo.SalesTop10ReportVO;
import com.bitemate.vo.TurnoverReportVO;
import com.bitemate.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * Get Turnover Statistics
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * Get User Statistics
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * Get Order Statistics
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * Get sales top 10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}
