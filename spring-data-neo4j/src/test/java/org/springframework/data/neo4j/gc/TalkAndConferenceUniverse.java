package org.springframework.data.neo4j.gc;

import org.springframework.data.neo4j.gc.model.Conference;
import org.springframework.data.neo4j.gc.model.Person;
import org.springframework.data.neo4j.gc.model.Speaker;
import org.springframework.data.neo4j.gc.model.Talk;
import org.springframework.data.neo4j.gc.repo.ConferenceRepository;
import org.springframework.data.neo4j.gc.repo.PersonRepository;
import org.springframework.data.neo4j.gc.repo.SpeakerRepository;
import org.springframework.data.neo4j.gc.repo.TalkRepository;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

/**
 *
 */
public class TalkAndConferenceUniverse {

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


    public Talk sdnTalkAtLondon;
    public Talk neo4jTheoryPracTalkAtLondon;
    public Talk busyDevTalkAtLondon;
    public Talk busyDevTalkAtNewYork;

    ConferenceRepository confRepo;
    SpeakerRepository speakerRepo;
    TalkRepository talkRepo;
    PersonRepository personRepository;

    public TalkAndConferenceUniverse(ConferenceRepository confRepo,
                                     SpeakerRepository speakerRepo,
                                     TalkRepository talkRepo,
                                     PersonRepository personRepository) {

        this.confRepo = confRepo;
        this.speakerRepo = speakerRepo;
        this.talkRepo = talkRepo;
        this.personRepository = personRepository;

    }

    public void createData() {
        createConferences();
        createSpeakersAndAttendees();
        createTalks();


        addAttendeesToConferencesAndTalks();

        reloadAllDataFromGraph();
    }

    private void reloadAllDataFromGraph() {
        gcLondon = confRepo.findOne(gcLondon.getId());

        nicki = speakerRepo.findOne(nicki.getId());
        michael = speakerRepo.findOne(michael.getId());
        jim = speakerRepo.findOne(jim.getId());
        tareq = speakerRepo.findOne(tareq.getId());

        attendeeJoeSmo = personRepository.findOne(attendeeJoeSmo.getId());
        attendeeSusanSnow = personRepository.findOne(attendeeSusanSnow.getId());
        sdnTalkAtLondon = talkRepo.findOne(sdnTalkAtLondon.getId());

        neo4jTheoryPracTalkAtLondon = talkRepo.findOne(neo4jTheoryPracTalkAtLondon.getId());
        busyDevTalkAtLondon = talkRepo.findOne(busyDevTalkAtLondon.getId());
        busyDevTalkAtNewYork = talkRepo.findOne(busyDevTalkAtNewYork.getId());

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

        sdnTalkAtLondon.addAttendee(attendeeSusanSnow, 4);
        sdnTalkAtLondon.addAttendee(attendeeJoeSmo, 5);
        sdnTalkAtLondon = talkRepo.save(sdnTalkAtLondon);

    }

    private void createTalks() {
        sdnTalkAtLondon = createNewTalk("OGM with SDN 3.0", gcLondon, nicki, michael);
        sdnTalkAtLondon = talkRepo.save(sdnTalkAtLondon);
        sdnTalkAtLondon = talkRepo.findOne(sdnTalkAtLondon.getId());
        gcLondon = confRepo.findOne(gcLondon.getId());

        neo4jTheoryPracTalkAtLondon = createNewTalk("Neo4j Theory and Practice", gcLondon, tareq);
        neo4jTheoryPracTalkAtLondon = talkRepo.save(neo4jTheoryPracTalkAtLondon);
        neo4jTheoryPracTalkAtLondon = talkRepo.findOne(neo4jTheoryPracTalkAtLondon.getId());
        gcLondon = confRepo.findOne(gcLondon.getId());

        busyDevTalkAtNewYork = createNewTalk("A Little Graph Theory for the Busy Developer", gcNewYork, jim);
        busyDevTalkAtNewYork = talkRepo.save(busyDevTalkAtNewYork);
        busyDevTalkAtNewYork = talkRepo.findOne(busyDevTalkAtNewYork.getId());
        gcNewYork = confRepo.findOne(gcNewYork.getId());

        busyDevTalkAtLondon = createNewTalk("A Little Graph Theory for the Busy Developer", gcLondon, jim);
        busyDevTalkAtLondon = talkRepo.save(busyDevTalkAtLondon);
        busyDevTalkAtLondon = talkRepo.findOne(busyDevTalkAtLondon.getId());
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


    private void createConferences() {
        gcLondonDate = new GregorianCalendar(2013,10,19).getTime();
        gcNewYorkDate = new GregorianCalendar(2013,10,6).getTime();

        gcLondon = createConference("GC London", gcLondonDate);
        gcNewYork = createConference("GC new York" , gcNewYorkDate);
        gcLondon = confRepo.save(gcLondon);
        gcNewYork = confRepo.save(gcNewYork);
    }

    private Talk createNewTalk(String name, Conference conference, Speaker... speakers) {
        HashSet<Speaker> speakerSet = new HashSet<Speaker>();
        for (Speaker s: speakers) {
            speakerSet.add(s);
            conference.registerAttendee(s);
        }
        confRepo.save(conference);

        Talk dt = new Talk();
        dt.setName(name);
        dt.setConference(conference);
        talkRepo.save(dt);


        for (Speaker s : speakers) {
            dt.addSpeaker(s);
        }
        talkRepo.save(dt);

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

}
