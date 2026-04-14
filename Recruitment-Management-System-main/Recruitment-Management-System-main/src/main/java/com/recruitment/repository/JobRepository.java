package com.recruitment.repository;

import com.recruitment.model.Job;
import com.recruitment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    List<Job> findByPostedBy(User postedBy);
    
    List<Job> findByPostedByOrderByPostedOnDesc(User postedBy);
    
    List<Job> findAllByOrderByPostedOnDesc();
    
    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.applicants WHERE j.id = :jobId")
    Optional<Job> findByIdWithApplicants(@Param("jobId") Long jobId);
    
    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.applicants a WHERE a.id = :applicantId")
    List<Job> findByApplicantId(@Param("applicantId") Long applicantId);
}
