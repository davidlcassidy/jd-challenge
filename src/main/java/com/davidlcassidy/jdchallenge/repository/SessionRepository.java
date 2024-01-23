package com.davidlcassidy.jdchallenge.repository;

import com.davidlcassidy.jdchallenge.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {

    Session findBySessionId(String sessionId);

    Optional<Session> findTopByMachineIdAndSessionIdOrderByStartAtDesc(String machineId, String sessionId);

    Optional<Session> findTopByMachineIdOrderByStartAtDesc(String machineId);

    List<Session> findAll();
}