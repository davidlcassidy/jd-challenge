package com.davidlcassidy.jdchallenge.repository;

import com.davidlcassidy.jdchallenge.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, String> {

	Session findBySessionId(String sessionId);

	Session save(Session session);
}