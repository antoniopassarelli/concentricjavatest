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

import global.concentric.javatest.beans.UserBean;
import global.concentric.javatest.data.UserRepository;
import global.concentric.javatest.model.User;
import global.concentric.javatest.service.UserRegistration;
import global.concentric.javatest.util.Utils;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class UserController {

	@Inject
	private FacesContext facesContext;

	@Inject
	private UserRegistration userRegistration;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserBean userBean;

	@Produces
	@Named
	private User newUser;

	@PostConstruct
	public void initNewUser() {
		newUser = new User();
	}

	public String login() throws Exception {
		try {
			User userDB = userRepository.findByUsername(newUser.getUsername());
			if (null != userDB) {
				String hashedPassword = Utils.stringToSha512(newUser.getPassword());
				if (userDB.getPasswordDB().equalsIgnoreCase(hashedPassword)) {
					userBean.setCurrentUser(userDB);
					return "loggedIn";
				}
			}
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Check your credentials",
					"Login unsuccessful");
			facesContext.addMessage(null, m);
		} catch (Exception e) {
			String errorMessage = Utils.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Login unsuccessful");
			facesContext.addMessage(null, m);
		}
		return null;
	}

	public String register() throws Exception {
		User userDB = userRepository.findByUsername(newUser.getUsername());
		try {
			if (null == userDB) {
				newUser.setPasswordDB(Utils.stringToSha512(newUser.getPassword()));
				userRegistration.register(newUser);
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered", "Registration successful");
				facesContext.addMessage(null, m);
				initNewUser();
			} else {
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username unavailable",
						"Registration unsuccessful");
				facesContext.addMessage(null, m);
			}
		} catch (Exception e) {
			String errorMessage = Utils.getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
			facesContext.addMessage(null, m);
		}
		return null;
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "index";
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

}
