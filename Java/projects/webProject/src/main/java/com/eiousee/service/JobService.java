package com.eiousee.service;

import java.util.List;

public interface JobService {
    Integer getJobIdByName(String jobName);

    List<String> getJobList();
}
