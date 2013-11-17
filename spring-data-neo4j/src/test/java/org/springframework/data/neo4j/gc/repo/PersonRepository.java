package org.springframework.data.neo4j.gc.repo;

import org.springframework.data.neo4j.gc.model.Person;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface PersonRepository extends GraphRepository<Person> {

}
