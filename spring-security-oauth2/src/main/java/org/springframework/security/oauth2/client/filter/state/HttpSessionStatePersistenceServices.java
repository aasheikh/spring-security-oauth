/*
 * Copyright 2002-2011 the original author or authors.
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

package org.springframework.security.oauth2.client.filter.state;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Dave Syer
 * 
 */
public class HttpSessionStatePersistenceServices implements StatePersistenceServices {

	public static final String STATE_PREFIX = HttpSessionStatePersistenceServices.class.getName() + "#STATE#";

	private boolean allowSessionCreation = true;

	/**
	 * If set to true (the default), a session will be created (if required) to store the token if it is determined that
	 * its contents are different from the default empty context value.
	 * <p>
	 * Note that setting this flag to false does not necessarily prevent this class from storing the token. If your
	 * application (or another filter) creates a session, then the token will still be stored for an authenticated user.
	 * 
	 * @param allowSessionCreation
	 */
	public void setAllowSessionCreation(boolean allowSessionCreation) {
		this.allowSessionCreation = allowSessionCreation;
	}

	public Object loadPreservedState(String id, HttpServletRequest request, HttpServletResponse response) {
		Object state = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			state = session.getAttribute(STATE_PREFIX + id);
			session.removeAttribute(STATE_PREFIX + id);
		}
		return state;
	}

	public void preserveState(String id, Object state, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(allowSessionCreation);
		if (session != null) {
			session.setAttribute(STATE_PREFIX + id, state);
		}
	}

}
