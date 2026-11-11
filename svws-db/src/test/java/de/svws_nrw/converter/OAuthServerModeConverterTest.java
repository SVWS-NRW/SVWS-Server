package de.svws_nrw.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.oauth2.OAuthServerMode;
import de.svws_nrw.csv.converter.current.OAuthServerModeConverterDeserializer;
import de.svws_nrw.csv.converter.current.OAuthServerModeConverterSerializer;
import de.svws_nrw.db.converter.current.OAuthServerModeConverter;

@DisplayName("Tests für OAuthServerModeConverter samt Serializer und Deserializer")
class OAuthServerModeConverterTest {

	private OAuthServerModeConverter converter;

	@BeforeEach
	void setup() {
		converter = OAuthServerModeConverter.getConverterInstance();
		assertNotNull(converter, "Der Converter muss in DBAttributeConverter.init() registriert sein.");
	}

	@Test
	@DisplayName("convertToDatabaseColumn")
	void toDatabaseColumn() {
		assertEquals("DEVELOPMENT", converter.convertToDatabaseColumn(OAuthServerMode.DEV));
		assertEquals("PRODUCTION", converter.convertToDatabaseColumn(OAuthServerMode.PROD));
	}

	@Test
	@DisplayName("convertToEntityAttribute")
	void toEntityAttribute() {
		assertEquals(OAuthServerMode.DEV, converter.convertToEntityAttribute("DEVELOPMENT"));
		assertEquals(OAuthServerMode.PROD, converter.convertToEntityAttribute("PRODUCTION"));
	}

	@Test
	@DisplayName("convertToEntityAttribute | unbekannter Wert wirft eine DeveloperNotificationException")
	void toEntityAttributeUnknown() {
		assertThrows(DeveloperNotificationException.class, () -> converter.convertToEntityAttribute("unbekannt"));
	}

	@Test
	@DisplayName("getResultType und getDBType")
	void types() {
		assertEquals(OAuthServerMode.class, converter.getResultType());
		assertEquals(String.class, converter.getDBType());
	}

	@Test
	@DisplayName("getConverterInstance | liefert immer dasselbe Objekt")
	void sameInstance() {
		assertSame(OAuthServerModeConverter.getConverterInstance(), OAuthServerModeConverter.getConverterInstance());
	}

	@Test
	@DisplayName("roundTrip über Serializer und Deserializer")
	void csvRoundTrip() throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final SimpleModule module = new SimpleModule();
		module.addSerializer(OAuthServerMode.class, new OAuthServerModeConverterSerializer());
		module.addDeserializer(OAuthServerMode.class, new OAuthServerModeConverterDeserializer());
		mapper.registerModule(module);

		for (final OAuthServerMode mode : OAuthServerMode.values()) {
			final String json = mapper.writeValueAsString(mode);
			assertEquals("\"" + mode.getDbValue() + "\"", json);
			assertEquals(mode, mapper.readValue(json, OAuthServerMode.class));
		}
	}
}
