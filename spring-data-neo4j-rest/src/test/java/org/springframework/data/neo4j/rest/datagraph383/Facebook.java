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

package org.springframework.data.neo4j.rest.datagraph383;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.Arrays;
import java.util.List;

public class Facebook {
    public FacebookUser adam;
    public FacebookUser barry;
    public FacebookUser carin;
    public FacebookUser debbie;
    public FacebookUser eve;
    public FacebookUser freddie;
    public FacebookUser gary;
    public FacebookUser harry;
    public FacebookUser inga;
    public FacebookUser james;

    public Member adamTheMember;

    private FacebookUserRepository facebookUserRepository;
    private MemberRepository memberRepository;

    public Facebook(FacebookUserRepository facebookUserRepository,MemberRepository memberRepository) {
        this.facebookUserRepository = facebookUserRepository;
        this.memberRepository = memberRepository;
    }

    public Facebook createFacebookUsers() {
        adamTheMember = new Member("1","1");

        adam = new FacebookUser("1","Adam","Jones");
        barry = new FacebookUser("2","Barry","Jones");
        carin = new FacebookUser("3","Carin","Smith");
        debbie = new FacebookUser("4","Debbie","Higgins");
        eve = new FacebookUser("5","Eve","Disney");
        freddie = new FacebookUser("6","Freddie","James");
        gary = new FacebookUser("7","Gary","Booth");
        harry = new FacebookUser("8","Harry","Watson");
        inga = new FacebookUser("9","Inga","Barry");
        james = new FacebookUser("10","James","Master");

        facebookUserRepository.save((List)Arrays.asList(adam, barry, carin, debbie, eve, freddie, gary, harry, inga, james));

        // Adam's direct friends (inga,barry,carin,debbie)
        adam.getFriends().add(inga);    inga.getFriends().add(adam);
        adam.getFriends().add(barry);   barry.getFriends().add(adam);
        adam.getFriends().add(carin);   carin.getFriends().add(adam);
        adam.getFriends().add(debbie);  debbie.getFriends().add(adam);

        // Friends of adam's direct friends
        //  - eve (through inga, barry and carin)
        //  - freddie (through barry)
        //  - gary (through debbie)
        inga.getFriends().add(eve);      eve.getFriends().add(inga);
        carin.getFriends().add(eve);     eve.getFriends().add(carin);
        barry.getFriends().add(eve);     eve.getFriends().add(barry);
        barry.getFriends().add(freddie); freddie.getFriends().add(barry);
        debbie.getFriends().add(gary);   gary.getFriends().add(debbie);


        facebookUserRepository.save((List)Arrays.asList(adam, barry, carin, debbie, eve, freddie, gary, harry, inga, james));
        adamTheMember.setFacebookUser(adam);

        memberRepository.save(adamTheMember);




        return this;
    }


}
