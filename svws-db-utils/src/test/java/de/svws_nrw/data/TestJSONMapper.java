package de.svws_nrw.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.schule.Schulgliederung;


/**
 * Diese Klasse prüft die Methoden der {@link JSONMapper}-Klasse.
 */
public class TestJSONMapper {
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
			original.status = 2;
			original.idSchuljahresabschnitt = 4L;
			original.idKlasse = 43L;
			original.abiturjahrgang = 2050;
			original.schulgliederung = Schulgliederung.GY8.daten.kuerzel;
			original.kurse.add(142L);
			original.kurse.add(1433L);
			final byte[] encoded = JSONMapper.gzipByteArrayFromObject(original);
			final SchuelerListeEintrag decoded = JSONMapper.toObjectGZip(encoded, SchuelerListeEintrag.class);
			assertEquals(original.id, decoded.id);
			assertEquals(original.vorname, decoded.vorname);
			assertEquals(original.nachname, decoded.nachname);
			assertEquals(original.status, decoded.status);
			assertEquals(original.idSchuljahresabschnitt, decoded.idSchuljahresabschnitt);
			assertEquals(original.idKlasse, decoded.idKlasse);
			assertEquals(original.abiturjahrgang, decoded.abiturjahrgang);
			assertEquals(original.schulgliederung, decoded.schulgliederung);
			assertEquals(original.kurse.size(), decoded.kurse.size());
			for (int i = 0; i < original.kurse.size(); i++)
				assertEquals(original.kurse.get(i), decoded.kurse.get(i));
		} catch (final CompressionException e) {
			fail(e);
		}
	}

}
