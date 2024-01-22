package com.davidlcassidy.jdchallenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Session {
    @Id
    private String sessionId;
    private String machineId;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startAt;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @Builder.Default
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
