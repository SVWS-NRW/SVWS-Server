package de.svws_nrw.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.svws_nrw.core.types.schule.StatusSchulwechselAbgang;
import de.svws_nrw.csv.converter.current.StatusSchulwechselAbgangConverterDeserializer;
import de.svws_nrw.csv.converter.current.StatusSchulwechselAbgangConverterSerializer;
import de.svws_nrw.db.converter.current.StatusSchulwechselAbgangConverter;
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

@DisplayName("Diese Testklasse testet die Klasse StatusSchulwechselAbgangConverter und die zugehörigen Serializer und Deseralizer")
@ExtendWith(MockitoExtension.class)
class StatusSchulwechselAbgangConverterTest {

	private StatusSchulwechselAbgangConverter converter;

	@BeforeEach
	void setup() {
		converter = StatusSchulwechselAbgangConverter.getConverterInstance();
		assertNotNull(converter, "Converter-Instanz darf nicht 'null' sein.");
	}

	@Test
	@DisplayName("convertToDatabaseColumn")
	void testConvertToDatabaseColumn() {
		assertEquals("bevorstehend", converter.convertToDatabaseColumn(StatusSchulwechselAbgang.BEVORSTEHEND));
		assertEquals("geplant", converter.convertToDatabaseColumn(StatusSchulwechselAbgang.GEPLANT));
	}

	@Test
	@DisplayName("convertToDatabaseColumn | null")
	void testConvertToDatabaseColumnWithNull() {
		assertThrows(NullPointerException.class, () -> converter.convertToDatabaseColumn(null));
	}

	@Test
	@DisplayName("convertToEntityAttribute")
	void testConvertToEntityAttribute() {
		assertEquals(StatusSchulwechselAbgang.BESTAETIGT, converter.convertToEntityAttribute("bestätigt"));
		assertEquals(StatusSchulwechselAbgang.NICHT_VERSORGT, converter.convertToEntityAttribute("nicht versorgt"));
	}

	@Test
	@DisplayName("convertToEntityAttribute | invalid value")
	void testConvertToEntityAttributeInvalidValue() {
		assertNull(converter.convertToEntityAttribute("invalid value"), "Bei nicht vorhandenem Wert wird 'null' zurückgegeben");
	}

	@Test
	@DisplayName("getConverterInstance | same object")
	void testGetConverterInstanceIsSuperClassConverter() {
		final StatusSchulwechselAbgangConverter c1 = StatusSchulwechselAbgangConverter.getConverterInstance();
		final StatusSchulwechselAbgangConverter c2 = StatusSchulwechselAbgangConverter.getConverterInstance();

		assertSame(c1, c2, "Die Super-Klasse muss immer dasselbe Objekt zurückgeben");
	}

	@Test
	@DisplayName("deserialize | success")
	void testDeserialize() throws JsonProcessingException {
		final String json = "\"bevorstehend\"";

		final ObjectMapper mapper = new ObjectMapper();
		final SimpleModule module = new SimpleModule();
		module.addDeserializer(StatusSchulwechselAbgang.class, new StatusSchulwechselAbgangConverterDeserializer());
		mapper.registerModule(module);

		final StatusSchulwechselAbgang value = mapper.readValue(json, StatusSchulwechselAbgang.class);

		assertEquals(StatusSchulwechselAbgang.BEVORSTEHEND, value);
	}

	@Test
	@DisplayName("serialize | success")
	void testSerialize() throws JsonProcessingException {
		final StatusSchulwechselAbgang status = StatusSchulwechselAbgang.BEVORSTEHEND;

		final ObjectMapper mapper = new ObjectMapper();
		final SimpleModule module = new SimpleModule();
		module.addSerializer(StatusSchulwechselAbgang.class, new StatusSchulwechselAbgangConverterSerializer());
		mapper.registerModule(module);

		final String value = mapper.writeValueAsString(status);

		assertEquals("\"bevorstehend\"", value);
	}

	@Test
	@DisplayName("roundTrip | success")
	void testRoundTrip() {
		final StatusSchulwechselAbgang original = StatusSchulwechselAbgang.BEVORSTEHEND;

		final String db = converter.convertToDatabaseColumn(original);
		final StatusSchulwechselAbgang result = converter.convertToEntityAttribute(db);

		assertEquals(original, result, "Endergebnis muss dem Original-Wert entsprechen");
	}

}
