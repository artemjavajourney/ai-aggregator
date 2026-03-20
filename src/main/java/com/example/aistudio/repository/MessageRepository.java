package com.example.aistudio.repository;

import com.example.aistudio.entity.MessageEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
}
