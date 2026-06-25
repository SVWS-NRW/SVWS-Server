package de.svws_nrw.oauth;

import de.svws_nrw.utils.DbConnectionProviderStaticMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit-Tests fuer {@link SchemaServiceFactory}.
 */
@ExtendWith(MockitoExtension.class)
class SchemaServiceFactoryTest extends DbConnectionProviderStaticMockTest {


	@Test
	@DisplayName("getNewInstance | returns non-null factory")
	void getNewInstanceReturnsNonNullFactory() {
		assertNotNull(SchemaServiceFactory.getNewInstance());
	}

	@Test
	@DisplayName("getService | returns non-null schema service")
	void getServiceReturnsNonNullSchemaService() {
		final SchemaServiceFactory factory = SchemaServiceFactory.getNewInstance();
		final SchemaService service = factory.getService();
		assertNotNull(service);
	}
}
