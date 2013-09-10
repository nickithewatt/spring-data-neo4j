package org.springframework.data.neo4j.rest.datagraph383;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

/**
 *
 */
public interface FacebookUserRepository extends GraphRepository<FacebookUser>{

    //@Query("start n=node:Member(id ={0}) match n-[:MY_FB*1]-p-[:FRIEND*1]-k return p,collect(k) as kcol")
    @Query("start n=node:Member(id ={0}) match n-[:MY_FB*1]-p-[:FRIEND*1]-k-[:FRIEND*1]-m where NOT(p=m) return k as directFriend,  collect(m) as fofs")
    Iterable<FirstDegreeFriend> collectFriendOfFriendsBy1stDegree(Long memberId);

    @Query("start n=node:Member(id ={0}) match n-[:MY_FB*1]-p-[:FRIEND*1]-k-[:FRIEND*1]-m where NOT(p=m) return distinct m")
    List<FacebookUser> getAllFriendOfFriendsAsList(Long memberId);

    @Query("start n=node:Member(id ={0}) match n-[:MY_FB*1]-p-[:FRIEND*1]-k-[:FRIEND*1]-m where NOT(p=m) return distinct m")
    Iterable<FacebookUser> getAllFriendOfFriendsAsIterable(Long memberId);

}
