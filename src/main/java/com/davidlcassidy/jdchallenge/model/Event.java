package com.davidlcassidy.jdchallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;
    private String eventAt;
    private String eventType;
    private double numericEventValue;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    @JsonIgnore
    private Session session;

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventAt, eventType, numericEventValue);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventAt='" + eventAt + '\'' +
                ", eventType='" + eventType + '\'' +
                ", numericEventValue=" + numericEventValue +
                '}';
    }
}
