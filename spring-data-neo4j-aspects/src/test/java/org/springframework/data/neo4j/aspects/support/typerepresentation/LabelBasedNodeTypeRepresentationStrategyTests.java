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

package org.springframework.data.neo4j.aspects.support.typerepresentation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.springframework.data.neo4j.support.typerepresentation.LabelBasedNodeTypeRepresentationStrategy;
import org.springframework.test.context.CleanContextCacheTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Tests to ensure that all scenarios involved in entity creation / reading etc
 * behave as expected, specifically where the Label Type Representation Strategy
 * is being used.
 *
 * The common scenarios/tests are defined in the superclass and each subclass, which
 * represents a specific strategy, needs to ensure that all is when then they
 * are used
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:org/springframework/data/neo4j/aspects/support/Neo4jGraphPersistenceTests-context.xml",
        "classpath:org/springframework/data/neo4j/aspects/support/LabelingTypeRepresentationStrategyOverride-context.xml"})
@TestExecutionListeners({CleanContextCacheTestExecutionListener.class, DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class LabelBasedNodeTypeRepresentationStrategyTests extends AbstractNodeTypeRepresentationStrategyTestBase {


    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        assertThat("The tests in this class should be configured to use the Label " +
                   "based Type Representation Strategy, however it is not ... ",
                   nodeTypeRepresentationStrategy,
                   instanceOf(LabelBasedNodeTypeRepresentationStrategy.class));
    }

    private void testLabelIndexesExistPostEntityCreation() throws Exception {
        assertPropertyIndexedLabelsExist((String) thingType.getAlias(), "labelIndexedThingName");
        assertPropertyIndexedLabelsExist((String) subThingType.getAlias(), "labelIndexedThingName", "labelIndexedSubThingName");
        assertPropertyIndexedLabelsExist((String) subSubThingType.getAlias(), "labelIndexedThingName", "labelIndexedSubThingName", "labelIndexedSubSubThingName");
    }

    private void testLabelIndexesCanBeFoundPostEntityCreation() throws Exception {
        // properties indexed against Thing
        ResourceIterable<Node> nodes = graphDatabaseService.findNodesByLabelAndProperty(
               DynamicLabel.label((String)thingType.getAlias()),
               "labelIndexedThingName","thing-theLabelIndexedThingName");
        assertNotNull(nodes);
        assertEquals( nodes.iterator().next() , node(thing));

        /*

        THIS IS NOT WORKING / FINISHED YET

        // properties indexed against SubThing
        ResourceIterable<Node> nodes21 = graphDatabaseService.findNodesByLabelAndProperty(
                DynamicLabel.label((String)subThingType.getAlias()),
                "labelIndexedThingName","subThing-theLabelIndexedThingName");
        assertNotNull(nodes21);
        assertEquals( nodes21.iterator().next() , node(subThing));


        ResourceIterable<Node> nodes22 = graphDatabaseService.findNodesByLabelAndProperty(
                DynamicLabel.label((String)subThingType.getAlias()),
                "labelIndexedSubThingName","subThing-theLabelIndexedSubThingName");
        assertNotNull(nodes22);
        assertEquals( nodes22.iterator().next() , node(subThing));
        */
    }

    @Test
	@Transactional
	public void testPostEntityCreation() throws Exception {
        testLabelIndexesExistPostEntityCreation();
        testLabelIndexesCanBeFoundPostEntityCreation();
    }

	@Test
    @Transactional
	public void testPreEntityRemoval() throws Exception {
        // preEntityRemoval is a no op method, so nothing to test here!
	}

    private void assertPropertyIndexedLabelsExist(String label, String... propertyNames) {
        Iterable<IndexDefinition> idefs = graphDatabaseService.schema().getIndexes(DynamicLabel.label(label));
        Set<String> propNames2Find = new HashSet<String>();
        propNames2Find.addAll( Arrays.asList(propertyNames));

        for (IndexDefinition idef: idefs) {
            assertEquals( idef.getLabel().name() , label  );
            for (String key : idef.getPropertyKeys()) {
                propNames2Find.remove(key);
            }
        }

        // We remove all the property names we find so by this point,
        // all we are looking for should be removed
        assertThat( propNames2Find, hasSize(0));
    }

}
