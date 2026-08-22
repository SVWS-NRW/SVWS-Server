package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Prüfungen aus {@link HtmlContextValidierung}, die <b>keine Daten nachladen</b>: die reinen Wertprüfungen für Abiturjahr und GOSt-Halbjahr.
 * Diese Methoden benötigen weder Datenbank noch Reporting-Context. Sie werfen bewusst <b>ohne</b> eigenen Log-Eintrag; das ist Bestandsverhalten und wird
 * hier nicht mitgeprüft.
 */
class TestHtmlContextValidierungOhneContext {

	/** Die Abiturjahrgänge, die in den Tests als vorhanden gelten. */
	private static final List<Integer> VORHANDENE_ABITURJAHRGAENGE = List.of(2024, 2025);



	@Test
	void testAlleGostHalbjahreSindGueltig() {
		for (int halbjahrId = 0; halbjahrId <= 5; halbjahrId++) {
			final int id = halbjahrId;
			assertDoesNotThrow(() -> HtmlContextValidierung.validiereHalbjahr(id), "Das GOSt-Halbjahr mit der ID %d muss gültig sein.".formatted(id));
		}
	}

	@Test
	void testEinHalbjahrAusserhalbDesWertebereichsIstUngueltig() {
		for (final int halbjahrId : new int[] { -1, 6 }) {
			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereHalbjahr(halbjahrId));
			assertEquals(Status.BAD_REQUEST, aoe.getStatus());
			assertEquals("FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
		}
	}



	@Test
	void testEinzelneParameterAusAbiturjahrgangUndHalbjahrenSindGueltig() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterEinzeln(List.of(2025L, 0L, 1L), VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testEinzelneParameterKommenOhneHalbjahreAus() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterEinzeln(List.of(2025L), VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testEinzelneParameterMeldenEinenUnbekanntenAbiturjahrgangAlsNotFound() {
		// Der Abiturjahrgang ist in dieser Rolle die Hauptressource des Reports: Ein formal gültiges, aber nicht vorhandenes Jahr ist die Auskunft
		// "gibt es nicht" und kein Parameterfehler.
		final List<Long> parameterDaten = List.of(2026L, 0L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterEinzeln(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.NOT_FOUND, aoe.getStatus());
		assertEquals("FEHLER: Der Abiturjahrgang 2026 ist nicht vorhanden.", aoe.getBody());
	}

	@Test
	void testEinzelneParameterMeldenEineWertebereichsverletzungAlsBadRequest() {
		// Die Gegenprobe zur 404-Antwort: Ein Wert unterhalb von 1900 ist kein adressierbarer Jahrgang, sondern ein unzulässiger Eingabewert.
		final List<Long> parameterDaten = List.of(202L, 0L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterEinzeln(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testEinzelneParameterMeldenEinUngueltigesHalbjahr() {
		final List<Long> parameterDaten = List.of(2025L, 9L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterEinzeln(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

}
