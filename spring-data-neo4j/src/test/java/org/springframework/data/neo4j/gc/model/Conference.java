package org.springframework.data.neo4j.gc.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.neo4j.graphdb.Direction.INCOMING;


@NodeEntity
@TypeAlias(value = "Conference")
public class Conference  {

    @GraphId
    Long graphId;

    @Indexed
    String name;
    Date date;

    @RelatedTo(type="ATTENDEE")
    Set<Person> attendees;
    @RelatedTo(type="TALK")
    Set<Talk> talks;

// . . .

    public Long getId() {
        return graphId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Person> getAttendees() {
        if (attendees == null) {
            attendees = new HashSet<>();
        }
        return attendees;
    }

    public Set<Talk> getTalks() {
        if (talks == null) {
            talks = new HashSet<>();
        }
        return talks;
    }

    public void registerAttendee(Person person) {
        getAttendees().add(person);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conference other = (Conference) o;
        if (graphId == null) return super.equals(o);
        return graphId.equals(other.getId());

    }

    @Override
    public int hashCode() {
        return graphId != null ? graphId.hashCode() : super.hashCode();
    }


}
