package org.springframework.data.neo4j.gc.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.*;

import java.util.HashSet;


@NodeEntity
@TypeAlias(value = "Person")
public class Person {

    @GraphId protected Long graphId;
    @Indexed(unique = true) String name;

    @RelatedToVia(type="LISTENED_TO")
    Iterable<ListenedToRelationship> talksAttended;

//  . . .


        /*
        //Without Alias
        //@Query("start person=node({self}) match (person)<-[:ATTENDEE]-(conf) where conf:`org.springframework.data.neo4j.gc.model.Conference` return conf")
          public Iterable<Conference> findAllConferencesAttended;
        */

                /*
        // With Index Based TRS
        @Query("start person=node({self}) " +
                "match (person)<-[:ATTENDEE]-(gathering) " +
                "where gathering.__type__ IN ['Conference']" +
                "return gathering")
        public Iterable<Conference> findAllConferencesAttendedOld;
        */


        // With Label Based TRS
        @Query("start person=node({self}) " +
                "match (person)<-[:ATTENDEE]-(gathering) " +
                "where gathering:Conference " +
                "return gathering")
        public Iterable<Conference> findAllConferencesAttended;

        public Long getId() {
            return graphId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Iterable<ListenedToRelationship> getTalksAttended() {
            if (talksAttended == null) {
                talksAttended = new HashSet<>();
            }
            return talksAttended;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person other = (Person) o;
            if (graphId == null) return super.equals(o);
            return graphId.equals(other.getId());

        }

        @Override
        public int hashCode() {
            return graphId != null ? graphId.hashCode() : super.hashCode();
        }
    }
