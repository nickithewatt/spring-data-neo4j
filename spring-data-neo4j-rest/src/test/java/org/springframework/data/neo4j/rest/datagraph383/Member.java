package org.springframework.data.neo4j.rest.datagraph383;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;


@NodeEntity
public class Member {

    @GraphId Long nodeId;
    @Indexed(unique=true) private Long id;
    private Long facebookUserId;

    @RelatedTo(type = "MY_FB", direction = Direction.BOTH)
    FacebookUser facebookUser;

    public Member() {}

    public Member(String id, String facebookUserId) {
        this.id = Long.valueOf(id);
        this.facebookUserId = Long.valueOf(facebookUserId);
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getFacebookUserId() {
        return facebookUserId;
    }


    public void setFacebookUserId(Long facebookUserId) {
        this.facebookUserId = facebookUserId;
    }



    public FacebookUser getFacebookUser() {
        return facebookUser;
    }



    public void setFacebookUser(FacebookUser facebookUser) {
        this.facebookUser = facebookUser;
    }
}