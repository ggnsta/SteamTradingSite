package com.example.demo.models.repository;

import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.UsersProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SkinsRepository extends JpaRepository<Skins, String> {

    List<Skins> findAllByOwnerID (String ownerID);
}
