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

package org.springframework.security.oauth2.provider.refresh;

import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientCredentialsChecker;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 * @author Dave Syer
 * 
 */
public class RefreshTokenGranter implements TokenGranter {

	private static final String GRANT_TYPE = "refresh_token";

	private final AuthorizationServerTokenServices tokenServices;

	private final ClientCredentialsChecker clientCredentialsChecker;

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		clientCredentialsChecker.setPasswordEncoder(passwordEncoder);
	}

	public RefreshTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService) {
		this.clientCredentialsChecker = new ClientCredentialsChecker(clientDetailsService);
		this.tokenServices = tokenServices;
	}
	
	public OAuth2AccessToken grant(String grantType, Map<String, String> parameters, String clientId,
			String clientSecret, Set<String> scope) {
		if (!GRANT_TYPE.equals(grantType)) {
			return null;
		}
		clientCredentialsChecker.validateCredentials(grantType, clientId, clientSecret);
		String refreshToken = parameters.get("refresh_token");
		return tokenServices.refreshAccessToken(refreshToken, scope);
	}

}
