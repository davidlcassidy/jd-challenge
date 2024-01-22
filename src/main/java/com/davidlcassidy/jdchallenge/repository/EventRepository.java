package com.davidlcassidy.jdchallenge.repository;

import com.davidlcassidy.jdchallenge.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findBySession_SessionId(String sessionId);
}