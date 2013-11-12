/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.neo4j.support.index;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.springframework.data.neo4j.mapping.Neo4jPersistentEntity;
import org.springframework.data.neo4j.mapping.Neo4jPersistentProperty;

public interface IndexProvider {

    <S extends PropertyContainer, T> Index<S> getIndex(Neo4jPersistentEntity<T> type);

    <S extends PropertyContainer, T> Index<S> getIndex(Neo4jPersistentEntity<T> type, String indexName);

    @SuppressWarnings("unchecked")
    <S extends PropertyContainer, T> Index<S> getIndex(Neo4jPersistentEntity<T> persistentEntity, String indexName, IndexType indexType);

    <T extends PropertyContainer> Index<T> getIndex(String indexName);

    boolean isNode(Class<? extends PropertyContainer> type);

    // TODO handle existing indexes
    @SuppressWarnings("unchecked")
    <T extends PropertyContainer> Index<T> createIndex(Class<T> propertyContainerType, String indexName,
            IndexType fullText);

    void createIndexForLabelProperty(Neo4jPersistentProperty property, final Class<?> instanceType);


    <S extends PropertyContainer> Index<S> getIndex(Neo4jPersistentProperty property, final Class<?> instanceType);
    /**
     * adjust your indexName for the "__types__" indices
     * 
     * @return prefixed indexName for Type
     */
    String createIndexValueForType(Object type);
    
    /**
     * possibility to do something with the high level index name 
     */
    String customizeIndexName(String indexName, Class<?> type);

}
