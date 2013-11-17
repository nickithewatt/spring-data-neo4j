package org.springframework.data.neo4j.gc.model.old;

import org.neo4j.graphdb.Node;


    public class Person {

        private final Node underlyingNode;

        public Person(final Node node) {
            underlyingNode = node;
        }
        public Node getUnderlyingNode() {
            return underlyingNode;
        }
        public final String getName() {
            return (String) underlyingNode.getProperty( "name" );
        }
        public void setName( final String name ) {
            underlyingNode.setProperty( "name", name );
        }
    }
