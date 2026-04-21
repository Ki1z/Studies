package com.eiousee.mapper;

import com.eiousee.pojo.ReportCount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<ReportCount> getEmpJobData();

    List<ReportCount> getEmpDeptData();

    List<ReportCount> getEmpSexData();

    List<ReportCount> getClassSizeData();

    List<ReportCount> getEducationData();
}
