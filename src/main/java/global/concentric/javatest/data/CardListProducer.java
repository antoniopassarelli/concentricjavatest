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

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import global.concentric.javatest.beans.UserBean;
import global.concentric.javatest.model.Card;

@RequestScoped
public class CardListProducer {

	@Inject
	private CardRepository cardRepository;

	@Inject
	private UserBean userBean;

	private List<Card> cards;

	@Produces
	@Named
	private List<Card> cardsFound;

	// @Named provides access the return value via the EL variable name "cards" in
	// the UI (e.g.
	// Facelets or JSP view)
	@Produces
	@Named
	public List<Card> getCards() {
		return cards;
	}

	public void onCardsListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final List<Card> cards) {
		retrieveAllCardsByUser();
	}

	@PostConstruct
	public void retrieveAllCardsByUser() {
		cards = cardRepository.findByUser(userBean.getCurrentUser());
	}

	public void searchCards(String cardNumber, boolean isAdmin, Long idUser) {
		cardsFound = cardRepository.search(cardNumber, isAdmin, idUser);
	}

	public List<Card> getCardsFound() {
		return cardsFound;
	}

	public void setCardsFound(List<Card> cardsFound) {
		this.cardsFound = cardsFound;
	}

}
