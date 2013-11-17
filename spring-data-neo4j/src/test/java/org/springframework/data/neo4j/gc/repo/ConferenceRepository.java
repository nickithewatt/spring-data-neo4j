package org.springframework.data.neo4j.gc.repo;

import org.springframework.data.neo4j.gc.model.Conference;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.Set;

    public interface ConferenceRepository extends GraphRepository<Conference> {

        Set<Conference> findAllByDeliveredTalksSpeakersName(String speakerName);

    }

        /*

        Generated Query

        @Query(value = "START `conference_deliveredTalks_speakers`=" +
                       "node:`Person`(`name`={0}) " +
                       "MATCH (`conference`)<-[:`DELIVERED_AT`]-(`conference_deliveredTalks`)" +
                       "-[:`SPEAKER`]->(`conference_deliveredTalks_speakers`) " +
                "RETURN `conference`")
        */

