package de.svws_nrw.oauth.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit-Tests fuer {@link Credentials}.
 */
class CredentialsTest {

	@Test
	@DisplayName("<init> | allows null default scope")
	void constructorAllowsNullDefaultScope() {
		final URI tokenUrl = URI.create("https://issuer.example/oauth2/token");
		final Credentials creds = new Credentials("client-id", "client-secret", tokenUrl, null, OAuthDomain.IT_NRW);
		assertNotNull(creds);
		assertNull(creds.requestedScope());
	}

	@Test
	@DisplayName("<init> | throws when clientId is null")
	void constructorThrowsWhenClientIdIsNull() {
		final URI tokenUrl = URI.create("https://issuer.example/oauth2/token");
		final NullPointerException ex =
				assertThrows(NullPointerException.class, () -> new Credentials(null, "client-secret", tokenUrl, null, OAuthDomain.IT_NRW));
		assertEquals("clientId", ex.getMessage());
	}

	@Test
	@DisplayName("<init> | throws when clientSecret is null")
	void constructorThrowsWhenClientSecretIsNull() {
		final URI tokenUrl = URI.create("https://issuer.example/oauth2/token");
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> new Credentials("client-id", null, tokenUrl, null, OAuthDomain.IT_NRW));
		assertEquals("clientSecret", ex.getMessage());
	}

	@Test
	@DisplayName("<init> | throws when authServerUrl is null")
	void constructorThrowsWhenTokenUrlIsNull() {
		final NullPointerException ex =
				assertThrows(NullPointerException.class, () -> new Credentials("client-id", "client-secret", null, null, OAuthDomain.IT_NRW));
		assertEquals("authServerUrl", ex.getMessage());
	}
}
