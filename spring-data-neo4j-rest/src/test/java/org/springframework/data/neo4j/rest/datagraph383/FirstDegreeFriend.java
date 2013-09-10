package org.springframework.data.neo4j.rest.datagraph383;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

@QueryResult
public interface FirstDegreeFriend {

    @ResultColumn("fofs")
    Iterable<FacebookUser> getAssociatedFriends();

    @ResultColumn("directFriend")
    FacebookUser getDirectFriend();
}