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
import de.svws_nrw.core.types.oauth2.OAuthResourceType;
import de.svws_nrw.csv.converter.current.OAuthResourceTypeConverterDeserializer;
import de.svws_nrw.csv.converter.current.OAuthResourceTypeConverterSerializer;
import de.svws_nrw.db.converter.current.OAuthResourceTypeConverter;

@DisplayName("Tests für OAuthResourceTypeConverter samt Serializer und Deserializer")
class OAuthResourceTypeConverterTest {

	private OAuthResourceTypeConverter converter;

	@BeforeEach
	void setup() {
		converter = OAuthResourceTypeConverter.getConverterInstance();
		assertNotNull(converter, "Der Converter muss in DBAttributeConverter.init() registriert sein.");
	}

	@Test
	@DisplayName("convertToDatabaseColumn")
	void toDatabaseColumn() {
		assertEquals("SIGNIERDIENST", converter.convertToDatabaseColumn(OAuthResourceType.IT_NRW_SIGNIERDIENST));
		assertEquals("ERFOLGREICHE_BEWERBUNGEN", converter.convertToDatabaseColumn(OAuthResourceType.SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN));
	}

	@Test
	@DisplayName("convertToEntityAttribute")
	void toEntityAttribute() {
		assertEquals(OAuthResourceType.IT_NRW_SIGNIERDIENST, converter.convertToEntityAttribute("SIGNIERDIENST"));
		assertEquals(OAuthResourceType.SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN, converter.convertToEntityAttribute("ERFOLGREICHE_BEWERBUNGEN"));
	}

	@Test
	@DisplayName("convertToEntityAttribute | unbekannter Wert wirft eine DeveloperNotificationException")
	void toEntityAttributeUnknown() {
		assertThrows(DeveloperNotificationException.class, () -> converter.convertToEntityAttribute("unbekannt"));
	}

	@Test
	@DisplayName("getResultType und getDBType")
	void types() {
		assertEquals(OAuthResourceType.class, converter.getResultType());
		assertEquals(String.class, converter.getDBType());
	}

	@Test
	@DisplayName("getConverterInstance | liefert immer dasselbe Objekt")
	void sameInstance() {
		assertSame(OAuthResourceTypeConverter.getConverterInstance(), OAuthResourceTypeConverter.getConverterInstance());
	}

	@Test
	@DisplayName("roundTrip über Serializer und Deserializer")
	void csvRoundTrip() throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final SimpleModule module = new SimpleModule();
		module.addSerializer(OAuthResourceType.class, new OAuthResourceTypeConverterSerializer());
		module.addDeserializer(OAuthResourceType.class, new OAuthResourceTypeConverterDeserializer());
		mapper.registerModule(module);

		for (final OAuthResourceType type : OAuthResourceType.values()) {
			final String json = mapper.writeValueAsString(type);
			assertEquals("\"" + type.getDbValue() + "\"", json);
			assertEquals(type, mapper.readValue(json, OAuthResourceType.class));
		}
	}
}
