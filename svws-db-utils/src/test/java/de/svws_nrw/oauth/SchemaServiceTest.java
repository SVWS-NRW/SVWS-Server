package de.svws_nrw.oauth;


import de.svws_nrw.db.DBEntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit-Tests fuer {@link SchemaService}.
 */
@ExtendWith(MockitoExtension.class)
class SchemaServiceTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchemaService cut;

	@Test
	@DisplayName("getActiveSchema | returns schema from db entity manager")
	void getActiveSchemaReturnsSchemaFromDbEntityManager() {
		when(conn.getDBSchema()).thenReturn("tenant_schema_a");

		final String schema = cut.getActiveSchema();

		assertEquals("tenant_schema_a", schema);
		verify(conn, times(1)).getDBSchema();
		verifyNoMoreInteractions(conn);
	}

	@Test
	@DisplayName("getActiveSchema | returns null when db entity manager returns null")
	void getActiveSchemaReturnsNullWhenDbEntityManagerReturnsNull() {
		when(conn.getDBSchema()).thenReturn(null);

		final String schema = cut.getActiveSchema();

		assertNull(schema);
		verify(conn, times(1)).getDBSchema();
		verifyNoMoreInteractions(conn);
	}
}
