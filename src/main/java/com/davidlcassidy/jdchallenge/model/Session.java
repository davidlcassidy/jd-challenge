package com.davidlcassidy.jdchallenge.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Session {
	@Id
	private String sessionId;
	private String machineId;
	private String startAt;

	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
	private List<Event> eventList = new ArrayList<>();

	@Override
	public String toString() {
		return "Session{" +
			"sessionId='" + sessionId + '\'' +
			", machineId='" + machineId + '\'' +
			", startAt='" + startAt + '\'' +
			'}';
	}
}
