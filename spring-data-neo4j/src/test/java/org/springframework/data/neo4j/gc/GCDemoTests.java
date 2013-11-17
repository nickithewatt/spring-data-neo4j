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

package org.springframework.data.neo4j.gc;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.gc.model.*;
import org.springframework.data.neo4j.gc.repo.*;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.neo4j.helpers.collection.IteratorUtil.asCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(defaultRollback = false)
public class GCDemoTests {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TalkRepository talkRepository;
    @Autowired
    private DeliveredTalkRepository deliveredTalkRepository;
    @Autowired
    private SpeakerRepository speakerRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ConferenceRepository confRepository;

    private TalkDeliveryUniverse universe;

    /*
    @BeforeClass
    public static void cleanDBClass() throws Exception {
        FileUtils.deleteDirectory(new File("/Users/nickiwatt/devstuff/neo4j/presentations/graph-connect/neo4j-community-2.0.0-M06/data/graph.db"));
    }
    */

    @BeforeTransaction
    public void cleanDb() {
        Neo4jHelper.cleanDb(neo4jTemplate);
    }

    @Before
    public void setUp() throws Exception {
        universe = new TalkDeliveryUniverse(
                talkRepository,confRepository,
                speakerRepository,deliveredTalkRepository,
                personRepository);
        universe.createData();
    }

    @Test
    @Transactional
    public void basicUniverseIsAsExpected() {

        assertBasicConferenceDetails();
        assertBasicTalkRelatedDetails();

        Iterable<Speaker> allSpeakers = speakerRepository.findAll();
        assertThat(asCollection(allSpeakers), hasItems(universe.michael, universe.nicki, universe.jim, universe.tareq));

        Iterable<Person> allPersons = personRepository.findAll();
        assertThat(asCollection(allPersons), hasItems(
                (Person)universe.michael,
                (Person)universe.nicki,
                (Person)universe.jim,
                (Person)universe.tareq,
                universe.attendeeJoeSmo,
                universe.attendeeSusanSnow));

    }

    private void assertBasicTalkRelatedDetails() {
        Iterable<Talk> allTalks = talkRepository.findAll();
        assertThat(asCollection(allTalks), hasItems(universe.sdnTalk, universe.busyDevTalk, universe.neo4jTheoryPracTalk));

        Iterable<DeliveredTalk> allDeliveredTalks = deliveredTalkRepository.findAll();
        assertThat(asCollection(allDeliveredTalks), hasItems(
                universe.sdnTalkAtLondon,
                universe.busyDevTalkAtLondon,
                universe.busyDevTalkAtNewYork,
                universe.neo4jTheoryPracTalkAtLondon));

        assertThat( universe.sdnTalk.getDeliveredTalks(), hasItems(
                universe.sdnTalkAtLondon));

        assertThat( universe.sdnTalkAtLondon.getTalk(), is( universe.sdnTalk ) );

        assertThat( asCollection(universe.nicki.getTalksDelivered()), hasItems(
                universe.sdnTalkAtLondon
        ));

    }

    private void assertBasicConferenceDetails() {
        Iterable<Conference> allConfs = confRepository.findAll();
        assertThat(asCollection(allConfs), hasItems(universe.gcLondon, universe.gcNewYork));

        assertEquals( universe.gcLondon.getDate() , universe.gcLondonDate);
        assertThat(universe.gcLondon.getAttendees(), hasItems(
                (Person) universe.michael,
                (Person) universe.nicki,
                (Person) universe.jim,
                (Person) universe.tareq,
                universe.attendeeJoeSmo,
                universe.attendeeSusanSnow));

        assertThat( universe.gcNewYork.getAttendees(), hasItems(
                (Person)universe.jim,
                universe.attendeeSusanSnow));

        assertThat( universe.gcNewYork.getAttendees(), not (hasItems(
                (Person)universe.tareq,
                (Person)universe.nicki,
                (Person)universe.michael,
                (Person)universe.attendeeJoeSmo)));

    }

    @Test
    @Transactional
    public void testMoreStuff() {
        Iterable<Conference> confsAttended = universe.attendeeJoeSmo.findAllConferencesAttended;
        assertThat(asCollection(confsAttended), hasItems(universe.gcLondon));

        Iterable<Conference> confsAttended2 = universe.attendeeSusanSnow.findAllConferencesAttended;
        assertThat(asCollection(confsAttended2), hasItems(universe.gcLondon, universe.gcNewYork));

        Iterable<Conference> conferences = confRepository.findAllByDeliveredTalksSpeakersName(universe.jim.getName());
        assertThat(asCollection(conferences), hasItems(universe.gcNewYork, universe.gcLondon));

        Iterable<Conference> conferences2 = confRepository.findAllByDeliveredTalksSpeakersName(universe.nicki.getName());
        assertThat(asCollection(conferences2), hasItems( universe.gcLondon));

    }

}