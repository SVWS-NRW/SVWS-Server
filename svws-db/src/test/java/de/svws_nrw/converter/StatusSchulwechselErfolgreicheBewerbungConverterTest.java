package de.svws_nrw.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.csv.converter.current.StatusSchulwechselErfolgreicheBewerbungConverterDeserializer;
import de.svws_nrw.csv.converter.current.StatusSchulwechselErfolgreicheBewerbungConverterSerializer;
import de.svws_nrw.db.converter.current.StatusSchulwechselErfolgreicheBewerbungConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Diese Testklasse testet die Klasse StatusSchulwechselErfolgreicheBewerbungConverter und die zugehörigen Serializer und Deseralizer")
@ExtendWith(MockitoExtension.class)
class StatusSchulwechselErfolgreicheBewerbungConverterTest {

	private StatusSchulwechselErfolgreicheBewerbungConverter converter;

	@BeforeEach
	void setup() {
		converter = StatusSchulwechselErfolgreicheBewerbungConverter.getConverterInstance();
		assertNotNull(converter, "Converter-Instanz darf nicht 'null' sein.");
	}

	@Test
	@DisplayName("getResultType | gibt StatusSchulwechselErfolgreicheBewerbung.class zurück")
	void testGetResultType() {
		assertSame(StatusSchulwechselErfolgreicheBewerbung.class, converter.getResultType());
	}

	@Test
	@DisplayName("getDBType | gibt String.class zurück")
	void testGetDBType() {
		assertSame(String.class, converter.getDBType());
	}

	@Test
	@DisplayName("convertToDatabaseColumn")
	void testConvertToDatabaseColumn() {
		assertEquals("neu", converter.convertToDatabaseColumn(StatusSchulwechselErfolgreicheBewerbung.NEU));
		assertEquals("aufgenommen", converter.convertToDatabaseColumn(StatusSchulwechselErfolgreicheBewerbung.AUFGENOMMEN));
	}

	@Test
	@DisplayName("convertToDatabaseColumn | null")
	void testConvertToDatabaseColumnWithNull() {
		assertThrows(NullPointerException.class, () -> converter.convertToDatabaseColumn(null));
	}

	@Test
	@DisplayName("convertToEntityAttribute")
	void testConvertToEntityAttribute() {
		assertEquals(StatusSchulwechselErfolgreicheBewerbung.NEU, converter.convertToEntityAttribute("neu"));
		assertEquals(StatusSchulwechselErfolgreicheBewerbung.AUFGENOMMEN, converter.convertToEntityAttribute("aufgenommen"));
	}

	@Test
	@DisplayName("convertToEntityAttribute | invalid value")
	void testConvertToEntityAttributeInvalidValue() {
		assertNull(converter.convertToEntityAttribute("invalid value"), "Bei nicht vorhandenem Wert wird 'null' zurückgegeben");
	}

	@Test
	@DisplayName("getConverterInstance | same object")
	void testGetConverterInstanceIsSuperClassConverter() {
		final StatusSchulwechselErfolgreicheBewerbungConverter c1 = StatusSchulwechselErfolgreicheBewerbungConverter.getConverterInstance();
		final StatusSchulwechselErfolgreicheBewerbungConverter c2 = StatusSchulwechselErfolgreicheBewerbungConverter.getConverterInstance();

		assertSame(c1, c2, "Die Super-Klasse muss immer dasselbe Objekt zurückgeben");
	}

	@Test
	@DisplayName("deserialize | success")
	void testDeserialize() throws JsonProcessingException {
		final String json = "\"neu\"";

		final ObjectMapper mapper = new ObjectMapper();
		final SimpleModule module = new SimpleModule();
		module.addDeserializer(StatusSchulwechselErfolgreicheBewerbung.class, new StatusSchulwechselErfolgreicheBewerbungConverterDeserializer());
		mapper.registerModule(module);

		final StatusSchulwechselErfolgreicheBewerbung value = mapper.readValue(json, StatusSchulwechselErfolgreicheBewerbung.class);

		assertEquals(StatusSchulwechselErfolgreicheBewerbung.NEU, value);
	}

	@Test
	@DisplayName("serialize | success")
	void testSerialize() throws JsonProcessingException {
		final StatusSchulwechselErfolgreicheBewerbung status = StatusSchulwechselErfolgreicheBewerbung.NEU;

		final ObjectMapper mapper = new ObjectMapper();
		final SimpleModule module = new SimpleModule();
		module.addSerializer(StatusSchulwechselErfolgreicheBewerbung.class, new StatusSchulwechselErfolgreicheBewerbungConverterSerializer());
		mapper.registerModule(module);

		final String value = mapper.writeValueAsString(status);

		assertEquals("\"neu\"", value);
	}

	@Test
	@DisplayName("roundTrip | success")
	void testRoundTrip() {
		final StatusSchulwechselErfolgreicheBewerbung original = StatusSchulwechselErfolgreicheBewerbung.NEU;

		final String db = converter.convertToDatabaseColumn(original);
		final StatusSchulwechselErfolgreicheBewerbung result = converter.convertToEntityAttribute(db);

		assertEquals(original, result, "Endergebnis muss dem Original-Wert entsprechen");
	}

}
