package de.svws_nrw.oauth;

import de.svws_nrw.oauth.internal.CredentialStore;
import de.svws_nrw.utils.DbConnectionProviderStaticMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit-Tests fuer {@link CredentialStoreFactory}.
 */
class CredentialStoreFactoryTest extends DbConnectionProviderStaticMockTest {

	@Test
	@DisplayName("getNewInstance | returns non-null factory")
	void getNewInstanceReturnsNonNullFactory() {
		assertNotNull(CredentialStoreFactory.getNewInstance());
	}

	@Test
	@DisplayName("getCredentialStore | returns non-null store")
	void getCredentialStoreReturnsNonNullStore() {
		final CredentialStoreFactory factory = CredentialStoreFactory.getNewInstance();
		final CredentialStore store = factory.getCredentialStore();
		assertNotNull(store);
	}
}
