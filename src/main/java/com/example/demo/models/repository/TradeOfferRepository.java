package com.example.demo.models.repository;
import com.example.demo.models.entity.TradeOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TradeOfferRepository extends JpaRepository<TradeOffer, String> {
    List<TradeOffer> findAllByStatus(int status);

}
