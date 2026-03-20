package com.example.aistudio.repository;

import com.example.aistudio.entity.SessionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {
}
