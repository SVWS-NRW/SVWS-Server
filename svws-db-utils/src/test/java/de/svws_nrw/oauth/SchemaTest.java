package de.svws_nrw.oauth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit-Tests fuer {@link Schema}.
 */
class SchemaTest {

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", " ", "\t", "\n", " \t\n" })
	void constructorThrowsWhenSchemaNameIsBlank(final String name) {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Schema(name));
		assertEquals("schema must not be blank", ex.getMessage());
	}

	@Test
	@DisplayName("<init> | stores schema name")
	void constructorStoresSchemaName() {
		final Schema schema = new Schema("tenant_schema_a");
		assertEquals("tenant_schema_a", schema.name());
	}
}
