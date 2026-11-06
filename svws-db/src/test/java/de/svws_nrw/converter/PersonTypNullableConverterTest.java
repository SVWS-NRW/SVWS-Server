package de.svws_nrw.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.converter.current.PersonTypNullableConverter;

@DisplayName("Diese Testklasse testet die Klasse PersonTypNullableConverter")
class PersonTypNullableConverterTest {

	private final PersonTypNullableConverter converter = new PersonTypNullableConverter();

	@ParameterizedTest(name = "Kürzel {0} wird zu {1}")
	@CsvSource({"L, LEHRER", "S, SCHUELER", "E, ERZIEHER"})
	@DisplayName("convertToEntityAttribute | gültiges Kürzel")
	void testConvertToEntityAttribute(final String kuerzel, final PersonTyp erwartet) {
		assertThat(converter.convertToEntityAttribute(kuerzel)).isEqualTo(erwartet);
	}

	@Test
	@DisplayName("convertToEntityAttribute | null (Altdaten aus Schild2) bleibt null")
	void testConvertToEntityAttributeNull() {
		assertThat(converter.convertToEntityAttribute(null)).isNull();
	}

	@Test
	@DisplayName("convertToEntityAttribute | unbekanntes Kürzel wird zu null")
	void testConvertToEntityAttributeUnbekannt() {
		assertThat(converter.convertToEntityAttribute("X")).isNull();
	}

	@Test
	@DisplayName("convertToDatabaseColumn | null bleibt null")
	void testConvertToDatabaseColumnNull() {
		assertThat(converter.convertToDatabaseColumn(null)).isNull();
	}

	@ParameterizedTest(name = "{0} wird zu Kürzel {1}")
	@CsvSource({"LEHRER, L", "SCHUELER, S", "ERZIEHER, E"})
	@DisplayName("convertToDatabaseColumn | Personentyp")
	void testConvertToDatabaseColumn(final PersonTyp typ, final String erwartet) {
		assertThat(converter.convertToDatabaseColumn(typ)).isEqualTo(erwartet);
	}

}
