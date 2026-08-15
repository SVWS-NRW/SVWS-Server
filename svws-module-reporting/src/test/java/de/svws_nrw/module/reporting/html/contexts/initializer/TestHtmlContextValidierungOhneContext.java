package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
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
	void testEinVorhandenesAbiturjahrIstGueltig() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereAbiturjahr(2025, VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testEinNichtVorhandenesAbiturjahrIstUngueltig() {
		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereAbiturjahr(2026, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testEinAbiturjahrVor1900IstAuchDannUngueltigWennEsVorhandenIst() {
		// Die untere Wertebereichsgrenze wird vor der Prüfung auf den Bestand ausgewertet und entscheidet damit allein.
		final List<Integer> abiturjahrgaenge = List.of(1899);
		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereAbiturjahr(1899, abiturjahrgaenge));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
	}

	@Test
	void testOhneVorhandeneAbiturjahrgaengeIstJedesAbiturjahrUngueltig() {
		final List<Integer> abiturjahrgaenge = List.of();
		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereAbiturjahr(2025, abiturjahrgaenge));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
	}

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
	void testPaarweiseParameterZerlegenAbiturjahrUndHalbjahr() {
		// 20253 steht für den Abiturjahrgang 2025 und das GOSt-Halbjahr mit der ID 3.
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterPaarweise(List.of(20253L, 20240L), VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testPaarweiseParameterMeldenEinUngueltigesHalbjahr() {
		final List<Long> parameterDaten = List.of(20256L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterPaarweise(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testPaarweiseParameterMeldenEinenUnbekanntenAbiturjahrgang() {
		final List<Long> parameterDaten = List.of(20263L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterPaarweise(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testEineZuLangeKombinierteIdWirdAbgewiesen() {
		// 202503 sieht nach "Abiturjahr 2025, Halbjahr 3" aus, wird von der Zerlegung aber zu Abiturjahr 20250 und Halbjahr 3. Da die Wertprüfung nur eine
		// untere Grenze kennt, fängt allein der Abgleich mit den vorhandenen Abiturjahrgängen diesen Fall ab.
		final List<Long> parameterDaten = List.of(202503L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterPaarweise(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testEineZuKurzeKombinierteIdWirdAbgewiesen() {
		// Der Gegenfall: Ein Abiturjahr ohne angehängtes Halbjahr zerfällt zu Abiturjahr 202 und Halbjahr 5 und scheitert an der unteren Grenze.
		final List<Long> parameterDaten = List.of(2025L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterPaarweise(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testPaarweiseParameterUeberspringenNullEintraege() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterPaarweise(Arrays.asList(20253L, null), VORHANDENE_ABITURJAHRGAENGE));
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
	void testEinzelneParameterMeldenEinenUnbekanntenAbiturjahrgang() {
		final List<Long> parameterDaten = List.of(2026L, 0L);
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
