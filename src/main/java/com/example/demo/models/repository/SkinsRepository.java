package com.example.demo.models.repository;

import com.example.demo.models.entity.Skins;
import com.example.demo.models.entity.UsersProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkinsRepository extends JpaRepository<Skins, String> {

    List<Skins> findAllByOwnerID (String ownerID);
}
