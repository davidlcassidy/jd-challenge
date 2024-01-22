package com.davidlcassidy.jdchallenge.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SessionAggregatedEvents {
    private String sessionId;
    private List<EventAggregation> events;

    @Data
    public static class EventAggregation {
        private String eventType;
        private double totalEventValue;
    }
}
