package com.sparta3tm.hubserver.domain.repository;

import com.sparta3tm.hubserver.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HubRepository extends JpaRepository<Hub, Long> {

    Optional<Hub> findByIdAndIsDeleteFalse(Long id);


    Page<Hub> findAllByIsDeleteFalse(Pageable pageable);
    List<Hub> findAllByIsDeleteFalse();
}
