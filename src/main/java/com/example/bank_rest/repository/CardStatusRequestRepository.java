package com.example.bank_rest.repository;

import com.example.bank_rest.entity.CardStatusRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardStatusRequestRepository extends JpaRepository<CardStatusRequest, Long> {
}
