package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileJpaRepository extends JpaRepository<Profile, UUID> {

}