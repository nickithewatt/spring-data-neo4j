package org.springframework.data.neo4j.gc.model;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;


@RelationshipEntity(type = "LISTENED_TO")
public class ListenedToRelationship {

    @GraphId
    Long graphId;
    Integer rating;

    @StartNode
    Person attendee;
    @EndNode
    Talk talk;

// . . .


    public ListenedToRelationship() { }

    public ListenedToRelationship(Person attendee, Talk talk, Integer rating) {
        this.rating = rating;
        this.attendee = attendee;
        this.talk = talk;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Person getAttendee() {
        return attendee;
    }

    public void setAttendee(Person attendee) {
        this.attendee = attendee;
    }

    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListenedToRelationship other = (ListenedToRelationship) o;
        if (graphId == null) return super.equals(o);
        return graphId.equals(other.graphId);

    }

    @Override
    public int hashCode() {
        return graphId != null ? graphId.hashCode() : super.hashCode();
    }
}
