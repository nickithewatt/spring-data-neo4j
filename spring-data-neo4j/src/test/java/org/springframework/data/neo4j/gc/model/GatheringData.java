package org.springframework.data.neo4j.gc.model;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import java.util.Set;


@QueryResult
public class GatheringData {

    @ResultColumn("name")
    public String name;

    @ResultColumn("labels")
    public Set<String> labels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }
}
