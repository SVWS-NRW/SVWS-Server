package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Prüfungen aus {@link HtmlContextValidierung}, die <b>keine Daten nachladen</b>: die Prüfungen einzelner Werte für Abiturjahrgang und
 * GOSt-Halbjahr. Die Schleife über die übergebenen Parameter führt ihr Aufrufer; sie ist Gegenstand von
 * {@link TestHtmlContextValidierungMitContext}.
 * Diese Methoden benötigen weder Datenbank noch Reporting-Context. Sie protokollieren nicht: Ein Abbruch hat eine Meldungsquelle - die Meldung der
 * Exception -, und protokolliert wird an der Abschlussgrenze.
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
			assertEquals("### FEHLER: Ein angegebenes GOSt-Halbjahr ist ungültig.", aoe.getBody());
		}
	}



	@Test
	void testEinVorhandenerAbiturjahrgangIstGueltig() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereAbiturjahrgang(2025, VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testEinUnbekannterAbiturjahrgangGiltAlsNotFound() {
		// Der Abiturjahrgang ist in dieser Rolle die Hauptressource des Reports: Ein formal gültiges, aber nicht vorhandenes Jahr ist die Auskunft
		// "gibt es nicht" und kein Parameterfehler.
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgang(2026, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.NOT_FOUND, aoe.getStatus());
		assertEquals("### FEHLER: Der Abiturjahrgang '2026' ist an dieser Schule nicht vorhanden.", aoe.getBody());
	}

	@Test
	void testEinAbiturjahrUnterhalbDesWertebereichsGiltAlsBadRequest() {
		// Die Gegenprobe zur 404-Antwort: Ein Wert unterhalb von 1900 ist kein adressierbarer Jahrgang, sondern ein unzulässiger Eingabewert.
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgang(202, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("### FEHLER: Das Abiturjahr '202' liegt außerhalb des zulässigen Bereichs.", aoe.getBody());
	}

	@Test
	void testDieWertebereichspruefungGehtDerExistenzpruefungVoraus() {
		// Ein Wert unterhalb von 1900 wird auch dann als Wertebereichsverletzung abgewiesen, wenn er zufällig in der Liste der vorhandenen Jahrgänge
		// stünde - sonst hinge der Status vom Datenbestand ab.
		final List<Integer> vorhandeneMitDemGepruefen = List.of(202, 2025);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgang(202, vorhandeneMitDemGepruefen));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
	}

}
