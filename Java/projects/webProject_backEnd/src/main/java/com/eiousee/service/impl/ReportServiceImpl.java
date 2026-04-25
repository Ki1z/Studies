package com.eiousee.service.impl;

import com.eiousee.pojo.ReportData;
import com.eiousee.service.ReportService;
import org.springframework.stereotype.Service;
import com.eiousee.mapper.ReportMapper;
import com.eiousee.pojo.ReportCount;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Override
    public ReportData getEmpJobData() {
        List<ReportCount> reportCountList = reportMapper.getEmpJobData();
        List<String> jobList = reportCountList.stream().map(ReportCount::getTitle).toList();
        List<Integer> dataList = reportCountList.stream().map(ReportCount::getObjectCount).toList();
        return new ReportData(jobList, dataList);
    }

    @Override
    public ReportData getEmpDeptData() {
        List<ReportCount> reportCountList = reportMapper.getEmpDeptData();
        List<String> deptList = reportCountList.stream().map(ReportCount::getTitle).toList();
        List<Integer> dataList = reportCountList.stream().map(ReportCount::getObjectCount).toList();
        return new ReportData(deptList, dataList);
    }

    @Override
    public ReportData getEmpSexData() {
        List<ReportCount> reportCountList = reportMapper.getEmpSexData();
        List<String> sexList = reportCountList.stream().map(ReportCount::getTitle).toList();
        List<Integer> dataList = reportCountList.stream().map(ReportCount::getObjectCount).toList();
        return new ReportData(sexList, dataList);
    }

    @Override
    public ReportData getClassSizeData() {
        List<ReportCount> reportCountList = reportMapper.getClassSizeData();
        List<String> classList = reportCountList.stream().map(ReportCount::getTitle).toList();
        List<Integer> dataList = reportCountList.stream().map(ReportCount::getObjectCount).toList();
        return new ReportData(classList, dataList);
    }

    @Override
    public ReportData getEducationData() {
        List<ReportCount> reportCountList = reportMapper.getEducationData();
        List<String> educationList = reportCountList.stream().map(ReportCount::getTitle).toList();
        List<Integer> dataList = reportCountList.stream().map(ReportCount::getObjectCount).toList();
        return new ReportData(educationList, dataList);
    }
}
