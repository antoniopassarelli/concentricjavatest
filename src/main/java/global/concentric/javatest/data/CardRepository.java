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
package global.concentric.javatest.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import global.concentric.javatest.model.Card;
import global.concentric.javatest.model.User;

@ApplicationScoped
public class CardRepository {

	@Inject
	private EntityManager em;

	public List<Card> findByUser(User user) {
		List<Card> cards = null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Card> criteria = cb.createQuery(Card.class);
			Root<Card> card = criteria.from(Card.class);
			// Swap criteria statements if you would like to try out type-safe criteria
			// queries, a new
			// feature in JPA 2.0
			// criteria.select(card).where(cb.equal(card.get(Card_.user), user));
			criteria.select(card).where(cb.equal(card.join("user").get("id"), user.getId()));
			cards = em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return cards;
	}

	public Card findByNumber(String number) {
		Card card = null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Card> criteria = cb.createQuery(Card.class);
			Root<Card> cardRoot = criteria.from(Card.class);
			// Swap criteria statements if you would like to try out type-safe criteria
			// queries, a new
			// feature in JPA 2.0
			// criteria.select(card).where(cb.equal(card.get(Card_.number), number));
			criteria.select(cardRoot).where(cb.equal(cardRoot.get("number"), number));
			card = em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return card;
	}

	public List<Card> findByNameAndDate(String name, String expDate) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Card> criteria = cb.createQuery(Card.class);
		Root<Card> card = criteria.from(Card.class);
		// Swap criteria statements if you would like to try out type-safe criteria
		// queries, a new
		// feature in JPA 2.0
		// criteria.select(card).where(cb.equal(card.get(Card_.name), name));
		criteria.select(card).where(cb.equal(card.get("name"), name));
		criteria.select(card).where(cb.equal(card.get("expDate"), expDate));
		return em.createQuery(criteria).getResultList();
	}

	public List<Card> search(String cardNumber, boolean isAdmin, Long idUser) {
		List<Card> cards = null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Card> criteria = cb.createQuery(Card.class);
			Root<Card> cardRoot = criteria.from(Card.class);
			Predicate numberPredicate = cb.like(cardRoot.get("number"), "%" + cardNumber + "%");
			Predicate userPredicate = null;
			Predicate predicate = null;
			if (!isAdmin) {
				userPredicate = cb.equal(cardRoot.join("user").get("id"), idUser);
				predicate = cb.and(numberPredicate, userPredicate);
			} else {
				/*
				 * admin can search for all users' card 
				 */
				predicate = numberPredicate;
			}
			criteria.select(cardRoot).where(predicate);
			cards = em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return cards;
	}

}
