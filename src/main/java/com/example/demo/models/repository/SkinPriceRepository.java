package com.example.demo.models.repository;

import com.example.demo.models.entity.SkinPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkinPriceRepository extends JpaRepository<SkinPrice,String> {
}
