package com.careers.jobapp.repositories;

import com.careers.jobapp.domain.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobsRepository extends JpaRepository<Jobs, Integer> {

    Optional<Jobs> findById(Integer jobId);
}
