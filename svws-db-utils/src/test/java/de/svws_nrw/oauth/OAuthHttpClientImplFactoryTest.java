package de.svws_nrw.oauth;

import de.svws_nrw.utils.DbConnectionProviderStaticMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit-Tests fuer {@link OAuthHttpClientFactory}.
 */
class OAuthHttpClientImplFactoryTest extends DbConnectionProviderStaticMockTest {

	@Test
	@DisplayName("getNewInstance | returns non-null factory")
	void getNewInstanceReturnsNonNullFactory() {
		assertNotNull(OAuthHttpClientFactory.getNewInstance());
	}

	@Test
	@DisplayName("getClient | returns non-null client")
	void getClientReturnsNonNullClient() {
		final OAuthHttpClientFactory factory = OAuthHttpClientFactory.getNewInstance();
		final OAuthHttpClientImpl client = factory.getClient();
		assertNotNull(client);
	}
}
