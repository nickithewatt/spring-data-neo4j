package org.springframework.data.neo4j.rest.datagraph383;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@NodeEntity
public class FacebookUser {

    public FacebookUser(){}

    public FacebookUser(String id, String firstName, String lastName) {
        this.id = Long.valueOf(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static final String FRIEND = "FRIEND";

    @GraphId Long nodeId;

    @Indexed(unique=true) private Long id;
    private String firstName;
    private String lastName;

    @RelatedTo(type = FRIEND, direction = Direction.BOTH)
    private Set<FacebookUser> friends = new HashSet<FacebookUser>();

    public Long getId() {
        return id;
    }

    public Set<FacebookUser> getFriends() {
        return friends;
    }

    public void setFriends(Set<FacebookUser> friends) {
        this.friends = friends;
    }

    public String getName() {
        return firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FacebookUser person = (FacebookUser) o;
        if (nodeId == null) return super.equals(o);
        return nodeId.equals(person.nodeId);

    }

    @Override
    public int hashCode() {
        return nodeId != null ? nodeId.hashCode() : super.hashCode();
    }



}
