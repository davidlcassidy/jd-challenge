package com.davidlcassidy.jdchallenge.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
public class Session {
    @Id
    private String sessionId;
    private String machineId;
    private String startAt;

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
