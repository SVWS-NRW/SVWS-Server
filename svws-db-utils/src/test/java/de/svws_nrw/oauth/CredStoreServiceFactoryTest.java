package de.svws_nrw.oauth;

import de.svws_nrw.utils.DbConnectionProviderStaticMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit-Tests fuer {@link CredStoreServiceFactory}.
 */
class CredStoreServiceFactoryTest extends DbConnectionProviderStaticMockTest {


	@Test
	@DisplayName("getNewInstance | returns non-null factory")
	void getNewInstanceReturnsNonNullFactory() {
		assertNotNull(CredStoreServiceFactory.getNewInstance());
	}

	@Test
	@DisplayName("getMemoryService | returns non-null service")
	void getCredStoreServiceReturnsNonNullService() {
		final CredStoreServiceFactory factory = new CredStoreServiceFactory();
		assertNotNull(factory.getCredStoreService());
	}
}
