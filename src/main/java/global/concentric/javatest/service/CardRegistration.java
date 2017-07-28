/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package global.concentric.javatest.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import global.concentric.javatest.model.Card;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class CardRegistration {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Card> cardEventSrc;

	public void register(Card card) throws Exception {
		log.info("Registering " + card.getNumber());
		em.persist(card);
		cardEventSrc.fire(card);
	}

	public void update(Card card) {
		log.info("Updating " + card.getNumber() +" with expiry date: "+ card.getExpDate());
		em.merge(card);
		cardEventSrc.fire(card);
	}
}
