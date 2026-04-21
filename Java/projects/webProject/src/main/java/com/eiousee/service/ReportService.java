package com.eiousee.service;

import com.eiousee.pojo.ReportData;

public interface ReportService {
    ReportData getEmpJobData();

    ReportData getEmpDeptData();

    ReportData getEmpSexData();

    ReportData getClassSizeData();

    ReportData getEducationData();
}
