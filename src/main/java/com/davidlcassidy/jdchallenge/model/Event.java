package com.davidlcassidy.jdchallenge.model;

import lombok.Data;
import java.util.List;

@Data
public class Event {
	private String sessionId;
	private List<EventDetails> events;
}
