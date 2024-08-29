package com.careers.jobapp.services;

import com.careers.jobapp.domain.Jobs;
import com.careers.jobapp.exception.ApiRequestException;
import com.careers.jobapp.repositories.JobsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class JobsServiceImpl implements JobsService{

    @Autowired
    private JobsRepository jobsRepository;


    @Override
    public Jobs saveJob(Jobs job) {

        if(Objects.equals(job.getPosition(), "") || job.getPosition() == null) {
            throw new ApiRequestException((
                    "position is mandatory"));
        }
        if(Objects.equals(job.getCompany(), "") || job.getCompany() == null) {
            throw new ApiRequestException((
                    "company is mandatory"));
        }

        if (job.getDateApplied()== null) {
            throw new ApiRequestException((
                    "date of application is mandatory"));
        }
        if (job.getLastModified()== null) {
            throw new ApiRequestException((
                    "last modified date is mandatory"));
        }

        if(Objects.equals(job.getStatus(), "") || job.getStatus() == null) {
            throw new ApiRequestException((
                    "status is mandatory"));
        }

        return jobsRepository.save(job);
    }

    @Override
    public List<Jobs> getAllJobs() {
        return jobsRepository.findAll();
    }

    @Override
    public Jobs updateJob(Jobs jobUpdate, Integer jobId) {

        Jobs jobRecord = jobsRepository.findById(jobId)
                .orElseThrow(() -> new ApiRequestException((
                        "job with id " + jobId + " does not exists")));

        if (jobUpdate.getPosition() != null &&
                !Objects.equals(jobRecord.getPosition(), jobUpdate.getPosition())) {
            jobRecord.setPosition(jobUpdate.getPosition());
        }
        if (jobUpdate.getCompany() != null &&
                !Objects.equals(jobRecord.getCompany(), jobUpdate.getCompany())) {
            jobRecord.setCompany(jobUpdate.getCompany());
        }
        if (jobUpdate.getDateApplied() != null &&
                !Objects.equals(jobRecord.getDateApplied(), jobUpdate.getDateApplied())) {
            jobRecord.setDateApplied(jobUpdate.getDateApplied());
        }
        if (jobUpdate.getLastModified() != null &&
                !Objects.equals(jobRecord.getLastModified(), jobUpdate.getLastModified())) {
            jobRecord.setLastModified(jobUpdate.getLastModified());
        }
        if (jobUpdate.getStatus() != null &&
                !Objects.equals(jobRecord.getStatus(), jobUpdate.getStatus())) {
            jobRecord.setStatus(jobUpdate.getStatus());
        }

        return jobsRepository.save(jobUpdate);

    }

    @Override
    public void deleteJob(Integer jobId) {

        boolean exists = jobsRepository.existsById(jobId);
        if (!exists) {
            throw new ApiRequestException(
                    "job with id " + jobId + " does not exists");
        }
        jobsRepository.deleteById(jobId);
    }
}
