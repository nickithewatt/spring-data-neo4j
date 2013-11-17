package org.springframework.data.neo4j.gc.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.*;

import java.util.HashSet;
import java.util.Set;

import static org.neo4j.graphdb.Direction.INCOMING;

@NodeEntity
@TypeAlias(value = "Talk")
public class Talk {

    @GraphId
    Long graphId;

    @Indexed
    String name;
    String description;

    @RelatedTo(type="TALK",direction = INCOMING)
    Conference conference;
    @RelatedTo(type="SPEAKER")
    Set<Speaker> speakers;
    @RelatedToVia(direction = INCOMING)
    Set<ListenedToRelationship> attendees;

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

    public ListenedToRelationship addAttendee(Person attendee, Integer rating) {
        ListenedToRelationship ltr = new ListenedToRelationship(attendee,this,rating);
        getAttendees().add(ltr);
        return ltr;
    }

    public Set<Speaker> getSpeakers() {
        if (speakers == null) {
            speakers = new HashSet<>();
        }
        return speakers;
    }

    public Set<ListenedToRelationship> getAttendees() {
        if (attendees == null) {
            attendees = new HashSet<ListenedToRelationship>();
        }
        return attendees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Talk other = (Talk) o;
        if (graphId == null) return super.equals(o);
        return graphId.equals(other.getId());

    }

    @Override
    public int hashCode() {
        return graphId != null ? graphId.hashCode() : super.hashCode();
    }
}
