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
package global.concentric.javatest.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Pattern;

import global.concentric.javatest.beans.CardBean;
import global.concentric.javatest.beans.UserBean;
import global.concentric.javatest.data.CardListProducer;
import global.concentric.javatest.data.CardRepository;
import global.concentric.javatest.model.Card;
import global.concentric.javatest.service.CardRegistration;
import global.concentric.javatest.util.Utils;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class CardController {

	@Inject
	private FacesContext facesContext;

	@Inject
	private CardRegistration cardRegistration;

	@Inject
	private CardRepository cardRepository;

	@Inject
	private CardListProducer cardListProducer;

	@Inject
	private UserBean userBean;

	@Inject
	private CardBean cardBean;

	@Produces
	@Named
	private Card newCard;

	@Produces
	@Named
	@Pattern(regexp = "^[0-9]{1,19}$", message = "Please enter at least 1 and at most 19 digits")
	private String cardNumberSearch;

	@PostConstruct
	public void initNewCard() {
		newCard = new Card();
		cardNumberSearch = new String();
	}

	public String add() throws Exception {
		try {
			Card matchedCard = cardRepository.findByNumber(newCard.getNumber());
			if (null != matchedCard) {
				/*
				 * update case
				 */
				if (!userBean.getCurrentUser().isAdmin()) {
					/*
					 * if not admin, check card's ownership
					 */
					if (matchedCard.getUser().getId() != userBean.getCurrentUser().getId()) {
						FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Check card number",
								"Card number invalid");
						facesContext.addMessage(null, m);
						return null;
					}
				}
				matchedCard.setExpDate(newCard.getExpDate());
				cardRegistration.update(matchedCard);
				cardListProducer.retrieveAllCardsByUser();
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Card updated",
						"Card expiry date successfully updated");
				facesContext.addMessage(null, m);
			} else {
				/*
				 * new card case
				 */
				newCard.setUser(userBean.getCurrentUser());
				cardRegistration.register(newCard);
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Card added",
						"New card successfully added");
				facesContext.addMessage(null, m);
			}
			initNewCard();
			return "added";
		} catch (Exception e) {
			String errorMessage = Utils.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Unable to add the new card");
			facesContext.addMessage(null, m);
			return null;
		}
	}

	public String search() throws Exception {
		String cardNumberSearch = newCard.getCardNumberSearch();
		boolean isAdmin = userBean.getCurrentUser().isAdmin();
		Long userId = userBean.getCurrentUser().getId();
		try {
			cardListProducer.searchCards(cardNumberSearch, isAdmin, userId);
			cardBean.setSearchExecuted(true);
			return null;
		} catch (Exception e) {
			String errorMessage = Utils.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Unable to add the new card");
			facesContext.addMessage(null, m);
			return null;
		}
	}

	public String initSearch() {
		cardBean.setSearchExecuted(false);
		return "search";
	}

	// public String update() {
	// return "";
	// }

	public Card getNewCard() {
		return newCard;
	}

	public void setNewCard(Card newCard) {
		this.newCard = newCard;
	}

	public String getCardNumberSearch() {
		return cardNumberSearch;
	}

	public void setCardNumberSearch(String cardNumberSearch) {
		this.cardNumberSearch = cardNumberSearch;
	}

}
