package com.eiousee.service.impl;

import com.eiousee.mapper.JobMapper;
import com.eiousee.service.JobService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

    public JobServiceImpl(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    @Override
    public Integer getJobIdByName(String jobName) {
        return jobMapper.getJobIdByName(jobName);
    }

    @Override
    public List<String> getJobList() {
        return jobMapper.getJobList();
    }
}
