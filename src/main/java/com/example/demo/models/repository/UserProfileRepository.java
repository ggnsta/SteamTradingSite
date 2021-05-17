package com.example.demo.models.repository;

import com.example.demo.models.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {


    UserProfile findByName(String name);

}
