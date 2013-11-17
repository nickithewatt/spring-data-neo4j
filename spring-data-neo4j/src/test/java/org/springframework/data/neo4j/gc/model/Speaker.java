package org.springframework.data.neo4j.gc.model;


import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.RelatedTo;

import static org.neo4j.graphdb.Direction.INCOMING;

@TypeAlias(value = "Speaker")
public class Speaker extends Person {

    String bio;

    @RelatedTo(type="SPEAKER", direction = INCOMING)
    Iterable<Talk> talksDelivered;

// . . .

    public Iterable<Talk> getTalksDelivered() {
        return talksDelivered;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Speaker other = (Speaker) o;
        if (graphId == null) return super.equals(o);
        return graphId.equals(other.getId());

    }

}
