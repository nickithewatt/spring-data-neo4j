package org.springframework.data.neo4j.rest.datagraph383;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.rest.support.RestTestBase;
import org.springframework.test.context.CleanContextCacheTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datagraph383-context.xml"})
@TestExecutionListeners({
        CleanContextCacheTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class RESTGraphRepositoryTests extends RestTestBase {

    @Autowired
    private FacebookUserRepository facebookUserRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Facebook facebook;

    @Before
    public void setup() {
        facebook = new Facebook(facebookUserRepository, memberRepository);
        facebook.createFacebookUsers();

        // Adam's direct friends  (fofs in brackets)
        //   - inga    (eve               )
        //   - barry   (eve , freddie     )
        //   - carin   (eve               )
        //   - debbie  (              gary)
    }

    @Test  @Transactional
    public void testListValueInQueryReturnedCorrectly() {
        List<FacebookUser> fofs = facebookUserRepository.getAllFriendOfFriendsAsList(facebook.adamTheMember.getId());
        assertThat(fofs, hasItems(facebook.eve, facebook.freddie, facebook.gary));
    }

    @Test  @Transactional
    public void testIterableValueInQueryReturnedCorrectly() {
        Iterable<FacebookUser> fofs = facebookUserRepository.getAllFriendOfFriendsAsIterable(facebook.adamTheMember.getId());
        assertThat(fofs, hasItems(facebook.eve, facebook.freddie, facebook.gary));
    }

    @Test  @Transactional
    public void testCollectedValuesReturnedViaQueryResultConvertedCorrectly() {

        Iterable<FirstDegreeFriend> fofsBy1stDegree = facebookUserRepository.collectFriendOfFriendsBy1stDegree(facebook.adamTheMember.getId());
        Map<FacebookUser, Iterable<FacebookUser>> fofsBy1stDegreeMap = new HashMap<FacebookUser,Iterable<FacebookUser>>();
        for (FirstDegreeFriend fdf: fofsBy1stDegree) {
            fofsBy1stDegreeMap.put(fdf.getDirectFriend(),fdf.getAssociatedFriends());
        }

        // Assert first degree friends
        assertThat(fofsBy1stDegreeMap.keySet(), hasItems(facebook.inga, facebook.carin, facebook.barry, facebook.debbie));

        Iterable<FacebookUser> ingasFriends = fofsBy1stDegreeMap.get(facebook.inga);
        Iterable<FacebookUser> barrysFriends = fofsBy1stDegreeMap.get(facebook.barry);
        Iterable<FacebookUser> carinsFriends = fofsBy1stDegreeMap.get(facebook.carin);
        Iterable<FacebookUser> debbiesFriends = fofsBy1stDegreeMap.get(facebook.debbie);

        /*
           Currently get exception java.lang.RuntimeException:
           Cannot extract single value from Iterable with more than one elements.
         */
        assertThat(ingasFriends, hasItems(facebook.eve));
        assertThat(barrysFriends, hasItems(facebook.eve , facebook.freddie));
        assertThat(carinsFriends, hasItems(facebook.eve));
        assertThat(debbiesFriends, hasItems(facebook.gary));

    }


}
