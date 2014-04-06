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
package org.springframework.data.neo4j.lifecycle;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.mapping.Neo4jMappingContext;
import org.springframework.data.support.IsNewStrategyFactory;

/**
 * Unit tests for {@link AuditingEventListener}.
 * 
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class AuditingEventListenerUnitTests {

	IsNewAwareAuditingHandler handler;

	IsNewStrategyFactory factory;
	AuditingEventListener listener;

	@Before
	public void setUp() {

		handler = spy(new IsNewAwareAuditingHandler(new Neo4jMappingContext()));
		listener = new AuditingEventListener(new ObjectFactory<IsNewAwareAuditingHandler>() {

			@Override
			public IsNewAwareAuditingHandler getObject() throws BeansException {
				return handler;
			}
		});
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullAuditingHandler() {
		new AuditingEventListener(null);
	}

	@Test
	public void triggersCreationMarkForObjectWithEmptyId() {

		Sample sample = new Sample();
		listener.onApplicationEvent(new BeforeSaveEvent<Object>(this, sample));

		verify(handler, times(1)).markCreated(sample);
		verify(handler, times(0)).markModified(any(Sample.class));
	}

	@Test
	public void triggersModificationMarkForObjectWithSetId() {

		Sample sample = new Sample();
		sample.id = "id";
		listener.onApplicationEvent(new BeforeSaveEvent<Object>(this, sample));

		verify(handler, times(0)).markCreated(any(Sample.class));
		verify(handler, times(1)).markModified(sample);
	}

	@NodeEntity
	static class Sample {

		@GraphId String id;
	}
}
