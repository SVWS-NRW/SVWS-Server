package de.svws_nrw.data;

import static de.svws_nrw.data.util.TestUtils.fromObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;


/**
 * Diese Klasse prüft die Methoden der {@link JSONMapper}-Klasse.
 */
class TestJSONMapper {

	/**
	 * Initialisierung der Core-Types
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Tests für das Komprimieren und Dekomprimieren von Core-DTOs mit dem GZip-Verfahren
	 */
	@Test
	void testStringCompression() {
		try {
			final SchuelerListeEintrag original = new SchuelerListeEintrag();
			original.id = 42;
			original.vorname = "Max";
			original.nachname = "Mustermann";
			original.geschlecht = "m";
			original.status = 2;
			original.idSchuljahresabschnitt = 4L;
			original.idSchuljahresabschnittSchueler = 5L;
			original.idKlasse = 43L;
			original.abiturjahrgang = 2050;
			original.idSchulgliederung = Schulgliederung.GY8.daten(2024).id;
			original.kurse.add(142L);
			original.kurse.add(1433L);
			final byte[] encoded = JSONMapper.gzipByteArrayFromObject(original);
			final SchuelerListeEintrag decoded = JSONMapper.toObjectGZip(encoded, SchuelerListeEintrag.class);
			assertEquals(original.id, decoded.id);
			assertEquals(original.vorname, decoded.vorname);
			assertEquals(original.nachname, decoded.nachname);
			assertEquals(original.geschlecht, decoded.geschlecht);
			assertEquals(original.status, decoded.status);
			assertEquals(original.idSchuljahresabschnitt, decoded.idSchuljahresabschnitt);
			assertEquals(original.idSchuljahresabschnittSchueler, decoded.idSchuljahresabschnittSchueler);
			assertEquals(original.idKlasse, decoded.idKlasse);
			assertEquals(original.abiturjahrgang, decoded.abiturjahrgang);
			assertEquals(original.idSchulgliederung, decoded.idSchulgliederung);
			assertEquals(original.kurse.size(), decoded.kurse.size());
			for (int i = 0; i < original.kurse.size(); i++)
				assertEquals(original.kurse.get(i), decoded.kurse.get(i));
		} catch (final CompressionException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("ToListOfString | from List")
	void toListOfString_fromList() throws ApiOperationException {
		final var inputStream = fromObject(List.of("AB", "cd"));
		assertThat(JSONMapper.toListOfString(inputStream))
				.hasSize(2)
				.satisfiesExactly(
						a -> assertThat(a).isEqualTo("AB"),
						b -> assertThat(b).isEqualTo("cd")
				);
	}

	@Test
	@DisplayName("ToListOfString | from Array")
	void toListOfString_fromArray() throws ApiOperationException {
		final var inputStream = fromObject(new String[] { "AB", "cd" });
		assertThat(JSONMapper.toListOfString(inputStream))
				.hasSize(2)
				.satisfiesExactly(
						a -> assertThat(a).isEqualTo("AB"),
						b -> assertThat(b).isEqualTo("cd")
				);
	}

	@Test
	@DisplayName("ToListOfString | from Map | Exception")
	void toListOfString_fromMap() throws ApiOperationException {
		final var inputStream = fromObject(Map.of("ab", "ve"));
		assertThatException()
				.isThrownBy(() -> JSONMapper.toListOfString(inputStream))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Das übergebene JSON ist kein Array bzw. keine Liste")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("ToListOfString | from List but numbers | Exception")
	void toListOfString_fromListButNumbers() throws ApiOperationException {
		final var inputStream = fromObject(List.of(1, 2));
		assertThatException()
				.isThrownBy(() -> JSONMapper.toListOfString(inputStream))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Das übergebene JSON-Array enthält keine Strings")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
