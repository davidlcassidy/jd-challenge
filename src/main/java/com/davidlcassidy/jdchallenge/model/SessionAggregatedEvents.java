package com.davidlcassidy.jdchallenge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionAggregatedEvents {
    private String sessionId;
    private List<EventAggregation> events;

    @Data
    public static class EventAggregation {
        private String eventType;
        private double totalEventValue;
    }
}
