package org.springframework.data.neo4j.gc.repo;

import org.springframework.data.neo4j.gc.model.Talk;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.Set;

public interface TalkRepository extends GraphRepository<Talk> {

    Set<Talk> findTalksBySpeakersName(String name);

}
