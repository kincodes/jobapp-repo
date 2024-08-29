package com.careers.jobapp.controllers;

import com.careers.jobapp.domain.Jobs;
import com.careers.jobapp.services.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JobsController {

    @Autowired
    private JobsService jobsService;

    @PostMapping("/add")
    public String add(@RequestBody Jobs job){
        jobsService.saveJob(job);
        return "new job added successfully!";
    }

    @GetMapping("/getAll")
    public List<Jobs> getAllJobs(){
        return  jobsService.getAllJobs();
    }

    @PutMapping("/update/{jobId}")
    public String updateJob(@RequestBody Jobs job, @PathVariable Integer jobId){

        jobsService.updateJob(job, jobId);

        return "job updated successfully!";
    }

    @DeleteMapping("/delete/{jobId}")
    public String deleteJob(@PathVariable("jobId") Integer jobId){
        jobsService.deleteJob(jobId);
        return "job deleted!";
    }

}
