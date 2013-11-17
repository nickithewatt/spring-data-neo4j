package org.springframework.data.neo4j.gc.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

import static org.neo4j.graphdb.Direction.INCOMING;

    @NodeEntity
    @TypeAlias(value = "Talk")
    public class Talk {

        @GraphId
        private Long graphId;

        @Indexed(unique = true)
        private String name;
        private String description;

        @RelatedTo(type="IS_A_DELIVERY_OF", direction = INCOMING)
        Set<DeliveredTalk> deliveredTalks;

    // . . .

    public DeliveredTalk addDeliveredTalk(DeliveredTalk dt) {
        getDeliveredTalks().add(dt);
        return dt;
    }

    public Set<DeliveredTalk> getDeliveredTalks() {
        if (deliveredTalks == null) {
            deliveredTalks = new HashSet<DeliveredTalk>();
        }
        return deliveredTalks;
    }

    public Long getId() {
        return graphId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Talk talk = (Talk) o;
        if (graphId == null) return super.equals(o);
        return graphId.equals(talk.getId());

    }

    @Override
    public int hashCode() {
        return graphId != null ? graphId.hashCode() : super.hashCode();
    }

}
