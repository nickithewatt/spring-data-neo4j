package org.springframework.data.neo4j.gc;

import org.springframework.data.neo4j.gc.model.Conference;
import org.springframework.data.neo4j.gc.model.DeliveredTalk;
import org.springframework.data.neo4j.gc.model.Speaker;
import org.springframework.data.neo4j.gc.model.Talk;
import org.springframework.data.neo4j.gc.model.Person;
import org.springframework.data.neo4j.gc.repo.*;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class TalkDeliveryUniverse {

    public Date gcLondonDate;
    public Date gcNewYorkDate;

    public Conference gcLondon;
    public Conference gcNewYork;

    public Speaker nicki;
    public Speaker michael;
    public Speaker jim;
    public Speaker tareq;

    public Person attendeeJoeSmo;
    public Person attendeeSusanSnow;

    public Talk sdnTalk;
    public DeliveredTalk sdnTalkAtLondon;

    public Talk neo4jTheoryPracTalk;
    public DeliveredTalk neo4jTheoryPracTalkAtLondon;

    public Talk busyDevTalk;
    public DeliveredTalk busyDevTalkAtLondon;
    public DeliveredTalk busyDevTalkAtNewYork;

    TalkRepository talkRepo;
    ConferenceRepository confRepo;
    SpeakerRepository speakerRepo;
    DeliveredTalkRepository deliveredTalkRepo;
    PersonRepository personRepository;

    public TalkDeliveryUniverse(TalkRepository talkRepo,
                                ConferenceRepository confRepo,
                                SpeakerRepository speakerRepo,
                                DeliveredTalkRepository deliveredTalkRepo,
                                PersonRepository personRepository) {
        this.talkRepo = talkRepo;
        this.confRepo = confRepo;
        this.speakerRepo = speakerRepo;
        this.deliveredTalkRepo = deliveredTalkRepo;
        this.personRepository = personRepository;

    }

    public void createData() {
        createConferences();
        createTalks();
        createSpeakersAndAttendees();
        createDeliveredTalks();

        addAttendeesToConferencesAndTalks();

        reloadAllDataFromGraph();
    }

    private void reloadAllDataFromGraph() {
        gcLondon = confRepo.findOne(gcLondon.getId());
        sdnTalk = talkRepo.findOne(sdnTalk.getId());

        nicki = speakerRepo.findOne(nicki.getId());
        michael = speakerRepo.findOne(michael.getId());
        jim = speakerRepo.findOne(jim.getId());
        tareq = speakerRepo.findOne(tareq.getId());

        attendeeJoeSmo = personRepository.findOne(attendeeJoeSmo.getId());
        attendeeSusanSnow = personRepository.findOne(attendeeSusanSnow.getId());

        sdnTalk = talkRepo.findOne(sdnTalk.getId());
        sdnTalkAtLondon = deliveredTalkRepo.findOne(sdnTalkAtLondon.getId());

        neo4jTheoryPracTalk = talkRepo.findOne(neo4jTheoryPracTalk.getId());
        neo4jTheoryPracTalkAtLondon = deliveredTalkRepo.findOne(neo4jTheoryPracTalkAtLondon.getId());

        busyDevTalk = talkRepo.findOne(busyDevTalk.getId());
        busyDevTalkAtLondon = deliveredTalkRepo.findOne(busyDevTalkAtLondon.getId());
        busyDevTalkAtNewYork = deliveredTalkRepo.findOne(busyDevTalkAtNewYork.getId());

    }

    private void addAttendeesToConferencesAndTalks() {
        gcLondon = confRepo.findOne(gcLondon.getId());
        gcNewYork = confRepo.findOne(gcNewYork.getId());

        gcLondon.registerAttendee(attendeeJoeSmo);
        gcLondon.registerAttendee(attendeeSusanSnow);
        gcNewYork.registerAttendee(attendeeSusanSnow);

        gcLondon = confRepo.save(gcLondon);
        gcNewYork = confRepo.save(gcNewYork);
        gcLondon = confRepo.findOne(gcLondon.getId());
        gcNewYork = confRepo.findOne(gcNewYork.getId());

        sdnTalkAtLondon.addAttendee(attendeeSusanSnow);
        sdnTalkAtLondon.addAttendee(attendeeJoeSmo);
        sdnTalkAtLondon = deliveredTalkRepo.save(sdnTalkAtLondon);

    }

    private void createDeliveredTalks() {
        sdnTalkAtLondon = createNewDeliveredTalk(sdnTalk, gcLondon, nicki, michael);
        sdnTalkAtLondon = deliveredTalkRepo.save(sdnTalkAtLondon);
        sdnTalk = talkRepo.findOne(sdnTalk.getId());
        gcLondon = confRepo.findOne(gcLondon.getId());

        neo4jTheoryPracTalkAtLondon = createNewDeliveredTalk(neo4jTheoryPracTalk, gcLondon, tareq);
        neo4jTheoryPracTalkAtLondon = deliveredTalkRepo.save(neo4jTheoryPracTalkAtLondon);
        neo4jTheoryPracTalk = talkRepo.findOne(neo4jTheoryPracTalk.getId());
        gcLondon = confRepo.findOne(gcLondon.getId());

        busyDevTalkAtNewYork = createNewDeliveredTalk(busyDevTalk, gcNewYork, jim);
        busyDevTalkAtNewYork = deliveredTalkRepo.save(busyDevTalkAtNewYork);
        busyDevTalk = talkRepo.findOne(busyDevTalk.getId());
        gcNewYork = confRepo.findOne(gcNewYork.getId());

        busyDevTalkAtLondon = createNewDeliveredTalk(busyDevTalk, gcLondon, jim);
        busyDevTalkAtLondon = deliveredTalkRepo.save(busyDevTalkAtLondon);
        busyDevTalk = talkRepo.findOne(busyDevTalk.getId());
        gcLondon = confRepo.findOne(gcLondon.getId());
    }

    private void createSpeakersAndAttendees() {
        nicki    = createSpeaker("Nicki Watt", "Lead OpenCredo consultant and ...");
        michael  = createSpeaker("Michael Hunger", "Lead SDN Developer and ...");
        jim      = createSpeaker("Jim Webber", "Head Techie Honcho ...");
        tareq    = createSpeaker("Tareq Abedrabbo", "CTO of OpenCredo and ...");

        speakerRepo.save(nicki);
        speakerRepo.save(michael);
        speakerRepo.save(jim);
        speakerRepo.save(tareq);

        attendeeJoeSmo = createAttendee("Joe Smo");
        attendeeSusanSnow = createAttendee("Susan Snow");

        personRepository.save(attendeeJoeSmo);
        personRepository.save(attendeeSusanSnow);
    }

    private Person createAttendee(String name) {
        Person p = new Person();
        p.setName(name);
        return p;
    }

    private void createTalks() {
        sdnTalk  = createNewTalk("OGM with SDN 3.0");
        talkRepo.save(sdnTalk);

        neo4jTheoryPracTalk = createNewTalk("Neo4j Theory and Practice");
        talkRepo.save(neo4jTheoryPracTalk);

        busyDevTalk = createNewTalk("A Little Graph Theory for the Busy Developer");
        talkRepo.save(busyDevTalk);

    }

    private void createConferences() {
        gcLondonDate = new GregorianCalendar(2013,10,19).getTime();
        gcNewYorkDate = new GregorianCalendar(2013,10,6).getTime();

        gcLondon = createConference("GC London", gcLondonDate);
        gcNewYork = createConference("GC new York" , gcNewYorkDate);
        gcLondon = confRepo.save(gcLondon);
        gcNewYork = confRepo.save(gcNewYork);
    }

    private DeliveredTalk createNewDeliveredTalk(Talk talk, Conference conference, Speaker... speakers) {
        HashSet<Speaker> speakerSet = new HashSet<Speaker>();
        for (Speaker s: speakers) {
            speakerSet.add(s);
            conference.registerAttendee(s);
        }
        confRepo.save(conference);

        DeliveredTalk dt = new DeliveredTalk();
        dt.setName(talk.getName() + "@" + conference.getName());
        dt.setTalk(talk);
        dt.setConference(conference);
        deliveredTalkRepo.save(dt);


        for (Speaker s : speakers) {
            dt.addSpeaker(s);
        }
        deliveredTalkRepo.save(dt);

        return dt;

    }

    private Speaker createSpeaker(String name, String bio) {
        Speaker speaker = new Speaker();
        speaker.setName(name);
        speaker.setBio(bio);
        return speaker;
    }

    private Conference createConference(String name, Date date) {
        Conference conference = new Conference();
        conference.setName(name);
        conference.setDate(date);
        return conference;
    }

    private Talk createNewTalk(String name) {
        Talk talk = new Talk();
        talk.setName(name);
        return talk;
    }
}
