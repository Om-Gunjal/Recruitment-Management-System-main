package com.recruitment.service;

import com.recruitment.dto.JobRequest;
import com.recruitment.model.Job;
import com.recruitment.model.User;
import com.recruitment.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;
    
    public Job createJob(JobRequest jobRequest, User postedBy) {
        Job job = new Job(
            jobRequest.getTitle(),
            jobRequest.getDescription(),
            jobRequest.getCompanyName(),
            postedBy
        );
        return jobRepository.save(job);
    }
    
    public List<Job> getAllJobs() {
        return jobRepository.findAllByOrderByPostedOnDesc();
    }
    
    public Optional<Job> getJobById(Long jobId) {
        return jobRepository.findByIdWithApplicants(jobId);
    }
    
    public List<Job> getJobsByPostedBy(User postedBy) {
        return jobRepository.findByPostedByOrderByPostedOnDesc(postedBy);
    }
    
    public Job applyToJob(Long jobId, User applicant) {
        Job job = jobRepository.findByIdWithApplicants(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        
        job.addApplicant(applicant);
        return jobRepository.save(job);
    }
    
    public List<Job> getAppliedJobsByUser(User user) {
        return jobRepository.findByApplicantId(user.getId());
    }
}
