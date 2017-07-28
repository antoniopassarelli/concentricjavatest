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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import global.concentric.javatest.model.User;

import java.util.List;

@ApplicationScoped
public class UserRepository {

	@Inject
	private EntityManager em;

	public User findById(Long id) {
		return em.find(User.class, id);
	}

	public User findByUsername(String username) {
		User userDB = null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<User> criteria = cb.createQuery(User.class);
			Root<User> user = criteria.from(User.class);
			// Swap criteria statements if you would like to try out type-safe criteria
			// queries, a new
			// feature in JPA 2.0
			// criteria.select(user).where(cb.equal(user.get(User_.username), username));
			criteria.select(user).where(cb.equal(user.get("username"), username));
			userDB = em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return userDB;
	}

	public List<User> findAllOrderedByUsername() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> user = criteria.from(User.class);
		// Swap criteria statements if you would like to try out type-safe criteria
		// queries, a new
		// feature in JPA 2.0
		// criteria.select(user).orderBy(cb.asc(user.get(User_.username)));
		criteria.select(user).orderBy(cb.asc(user.get("username")));
		return em.createQuery(criteria).getResultList();
	}
}
