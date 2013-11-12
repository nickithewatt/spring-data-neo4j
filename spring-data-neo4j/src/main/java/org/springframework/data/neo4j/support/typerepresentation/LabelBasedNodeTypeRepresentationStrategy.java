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

package org.springframework.data.neo4j.support.typerepresentation;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.ClosableIterable;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.core.NodeTypeRepresentationStrategy;
import org.springframework.data.neo4j.repository.query.CypherQuery;
import org.springframework.data.neo4j.support.mapping.StoredEntityType;
import org.springframework.data.neo4j.support.mapping.WrappedIterableClosableIterable;
import org.springframework.data.neo4j.support.query.QueryEngine;

/**
 * Provides a Node Type Representation Strategy which makes use of Labels, and specifically
 * uses Cypher as the mechanism for interacting with the graph database.
 *
 * @author Nicki Watt
 * @since 24-09-2013
 */
public class LabelBasedNodeTypeRepresentationStrategy implements NodeTypeRepresentationStrategy {

    public static final Label SDN_LABEL_STRATEGY = DynamicLabel.label("SDN_LABEL_STRATEGY");
    public static final String LABELSTRATEGY_PREFIX = "__TYPE__";
    public static final long   REFERENCE_NODE_ID = 0L;

    protected GraphDatabase graphDb;
    protected final Class<Node> clazz;
    protected final LabelBasedStrategyCypherHelper cypherHelper;
    protected QueryEngine<CypherQuery> queryEngine;

    public LabelBasedNodeTypeRepresentationStrategy(GraphDatabase graphDb) {
        this.graphDb = graphDb;
        this.clazz = Node.class;
        this.queryEngine = graphDb.queryEngineFor(QueryType.Cypher);
        this.cypherHelper = new LabelBasedStrategyCypherHelper(queryEngine);
        markSDNLabelStrategyInUse();
    }

    @Override
    public void writeTypeTo(Node state, StoredEntityType type) {
        if (type == null || !type.isNodeEntity()) return;

        Label sdnLabel = DynamicLabel.label(LABELSTRATEGY_PREFIX + type.getAlias());
        if (state.hasLabel(sdnLabel)) {
            return; // already there
        }
        addLabelsForEntityHierarchy(state,type);
    }

    /**
     * For each level in the entity hierarchy, this method will assign a
     * label (the label name is based on the alias associated with the
     * entity type at each level). Additionally, a special label is added
     * as the primary SDN marker Label.
     */
    private void addLabelsForEntityHierarchy(Node state, StoredEntityType type) {
        String labels = cypherHelper.buildLabelString(LABELSTRATEGY_PREFIX + type.getAlias(), (String)type.getAlias());
        labels = buildLabelStringIncludingEachEntityInHierarchy(labels, type);
        cypherHelper.setLabelsOnNode(state.getId(), labels);
    }

    private String buildLabelStringIncludingEachEntityInHierarchy(String labels, StoredEntityType type) {
        for (StoredEntityType superType : type.getSuperTypes()) {
            labels += cypherHelper.buildLabelString((String)superType.getAlias());
            labels += buildLabelStringIncludingEachEntityInHierarchy(labels, superType);
        }
        return labels;
    }

    /**
     * Ensures that a special label (SDN_LABEL_STRATEGY) exists against the
     * reference node, and if it does not, it is added. This label serves
     * as an indicator that the Labeling strategy has/is going to be used on this
     * data set.
     */
    private void markSDNLabelStrategyInUse() {
        cypherHelper.setLabelOnNode(REFERENCE_NODE_ID, SDN_LABEL_STRATEGY.name());
    }

    @Override
    public <U> ClosableIterable<Node> findAll(StoredEntityType type) {
        Iterable<Node> rin = cypherHelper.getNodesWithLabel(type.getAlias().toString());
        return new WrappedIterableClosableIterable<Node>(rin);
    }

    @Override
    public long count(StoredEntityType type) {
        return cypherHelper.countNodesWithLabel(type.getAlias().toString());
    }

    @Override
    public Object readAliasFrom(Node state) {
        if (state == null)
            throw new IllegalArgumentException("Node is null");
        Iterable<String> labels = cypherHelper.getLabelsForNode(state.getId());
        for (String label: labels) {
            if (label.startsWith(LABELSTRATEGY_PREFIX)) {
                return label.substring(LABELSTRATEGY_PREFIX.length());
            }
        }
        throw new IllegalStateException("No primary SDN label exists .. (i.e one with starting with " + LABELSTRATEGY_PREFIX + ") ");

    }

    @Override
    public void preEntityRemoval(Node state) {
        // don't think we need to do anything here!
    }

    public static boolean isStrategyAlreadyInUse(GraphDatabase graphDatabaseService) {
       return graphDatabaseService.getReferenceNode().hasLabel(SDN_LABEL_STRATEGY);

    }


    @Override
    public boolean isLabelBased() {
        return true;
    }
}