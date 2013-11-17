package org.springframework.data.neo4j.gc.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.gc.model.Talk;
import org.springframework.data.neo4j.repository.GraphRepository;
import java.util.Set;

public interface TalkRepository extends GraphRepository<Talk> {

    // Repository Query Method
    @Query(value =
            "MATCH (talk:Talk)-[:SPEAKER]->(speaker:Speaker) " +
            "WHERE speaker.name={0} " +
            "RETURN talk")
    Set<Talk> findTalksBySpeakersNameQuery(String name);

    // Dynamically Derived Finder Method
    Set<Talk> findTalksBySpeakersName(String name);

    /*
       Generated Query ...

            START `talk_speakers`=node:`Person`(`name`={0})
            MATCH (`talk`)-[:`SPEAKER`]->(`talk_speakers`)
            RETURN `talk`
     */

}
