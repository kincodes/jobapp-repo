package com.careers.jobapp.services;

import com.careers.jobapp.domain.Jobs;

import java.util.List;

public interface JobsService {
    Jobs saveJob(Jobs job);

    List<Jobs> getAllJobs();

    Jobs updateJob(Jobs job, Integer jobId);

    void deleteJob( Integer jobId);

}
