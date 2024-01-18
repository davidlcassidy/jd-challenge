package com.davidlcassidy.jdchallenge.model;

import lombok.Data;

@Data
public class Session {
	private String sessionId;
	private String machineId;
	private String startAt;
}
