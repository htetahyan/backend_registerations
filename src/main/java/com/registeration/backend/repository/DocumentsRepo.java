package com.registeration.backend.repository;

import com.registeration.backend.entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentsRepo extends JpaRepository<Documents,Long> {
}
