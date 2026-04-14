package com.recruitment.repository;

import com.recruitment.model.Profile;
import com.recruitment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    
    Optional<Profile> findByApplicant(User applicant);
    
    Optional<Profile> findByApplicantId(Long applicantId);
    
    boolean existsByApplicant(User applicant);
}
