package org.springframework.data.neo4j.gc.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

import static org.neo4j.graphdb.Direction.INCOMING;

    @NodeEntity
    @TypeAlias(value = "DeliveredTalk")
    public class DeliveredTalk {

        @GraphId
        Long graphId;

        @Indexed(unique = true)
        String name;
        String description;

        @RelatedTo(type="IS_A_DELIVERY_OF")
        Talk talk;
        @RelatedTo(type="DELIVERED_AT")
        Conference conference;
        @RelatedTo(type="SPEAKER")
        Set<Speaker> speakers;
        @RelatedTo(type="LISTENED_TO", direction = INCOMING)
        Set<Person> attendees;

    // . . .


    public Long getId() {
        return graphId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Conference getConference() {
        return conference;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public void addSpeaker(Speaker speaker) {
        getSpeakers().add(speaker);
    }

    public void addAttendee(Person attendee) {
        getAttendees().add(attendee);
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    public Talk getTalk() {
        return talk;
    }

    public Set<Speaker> getSpeakers() {
        if (speakers == null) {
            speakers = new HashSet<>();
        }
        return speakers;
    }

    public Set<Person> getAttendees() {
        if (attendees == null) {
            attendees = new HashSet<>();
        }
        return attendees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliveredTalk other = (DeliveredTalk) o;
        if (graphId == null) return super.equals(o);
        return graphId.equals(other.getId());

    }

    @Override
    public int hashCode() {
        return graphId != null ? graphId.hashCode() : super.hashCode();
    }
}
