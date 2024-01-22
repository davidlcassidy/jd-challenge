package com.davidlcassidy.jdchallenge.service;

import com.davidlcassidy.jdchallenge.model.Session;
import com.davidlcassidy.jdchallenge.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SessionService {

	@Autowired
	private SessionRepository sessionRepository;

	public Session createSession(String sessionId, String machineId, String startAt) {
		Session session = new Session();
		session.setSessionId(sessionId);
		session.setMachineId(machineId);
		session.setStartAt(startAt);
		sessionRepository.save(session);
		return session;
	}
}