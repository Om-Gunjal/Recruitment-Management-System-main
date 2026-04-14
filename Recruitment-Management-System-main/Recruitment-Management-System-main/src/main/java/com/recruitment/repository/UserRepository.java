package com.recruitment.repository;

import com.recruitment.model.User;
import com.recruitment.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<User> findByUserType(UserType userType);
    
    List<User> findByUserTypeOrderByNameAsc(UserType userType);
}
