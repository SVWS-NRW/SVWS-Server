package de.svws_nrw.module.reporting.diagnose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Prüft das {@link ReportingAuswahlergebnis}: Es trennt ausgelassene von ausgefilterten Datensätzen, bleibt bei jeder Einschränkung unverändert und weist eine
 * bewusst leere Auswahl aus.
 * <p>An der Trennung hängt, ob ein fehlender Datensatz als Ausgabeproblem gemeldet wird; am Kennzeichen der leeren Auswahl hängt, ob eine Ausgabe ohne
 * Dokument zulässig ist oder ein Serverfehler bleibt.</p>
 */
class TestReportingAuswahlergebnis {

	/**
	 * Erzeugt eine Auswahl, in der zu jeder ID ihr Name als Objekt steht.
	 *
	 * @param ids Die IDs der ausgewählten Objekte.
	 *
	 * @return Die Auswahl ohne ausgelassene und ohne ausgefilterte Datensätze.
	 */
	private static ReportingAuswahlergebnis<String> auswahlMit(final List<Long> ids) {
		final Map<Long, String> objekte = new LinkedHashMap<>();
		for (final Long id : ids) {
			objekte.put(id, "Objekt " + id);
		}
		return ReportingAuswahlergebnis.aus(ids, objekte, Map.of(), List.of());
	}


	@Test
	void testEineVollstaendigeAuswahlIstNichtLeer() {
		final ReportingAuswahlergebnis<String> auswahl = auswahlMit(List.of(1L, 2L));

		assertEquals(List.of("Objekt 1", "Objekt 2"), auswahl.objekte());
		assertEquals(List.of(1L, 2L), auswahl.idsAusgewaehlt());
		assertEquals(List.of(1L, 2L), auswahl.idsAngefordert());
		assertFalse(auswahl.bewusstLeer());
	}

	@Test
	void testAusgelasseneUndAusgefilterteWerdenGetrenntGefuehrt() {
		// Ein ausgelassener Datensatz ist ein Ausgabeproblem und wird gemeldet; ein ausgefilterter fehlt, weil der Anwender ihn nicht sehen will.
		final ReportingAuswahlergebnis<String> auswahl = ReportingAuswahlergebnis.aus(List.of(1L, 2L, 3L), Map.of(1L, "Objekt 1"),
				Map.of(2L, ReportingLadezustand.nichtVorhanden()), List.of(3L));

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt());
		assertEquals(List.of(2L), List.copyOf(auswahl.ausgelassen().keySet()));
		assertEquals(List.of(3L), auswahl.idsAusgefiltert());
	}

	@Test
	void testDerZustandEinerAusgelassenenIdBleibtErhalten() {
		// Aus ihm entsteht die Ursache des gemeldeten Ausgabeproblems.
		final ReportingAuswahlergebnis<String> auswahl = ReportingAuswahlergebnis.aus(List.of(1L), Map.of(),
				Map.of(1L, ReportingLadezustand.fehlgeschlagen(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, null)), List.of());

		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, auswahl.ausgelassen().get(1L).ursache());
	}

	@Test
	void testEineAuswahlOhneVerbleibendeObjekteIstBewusstLeer() {
		final ReportingAuswahlergebnis<String> auswahl =
				ReportingAuswahlergebnis.aus(List.of(1L), Map.of(), Map.of(1L, ReportingLadezustand.nichtVorhanden()), List.of());

		assertTrue(auswahl.bewusstLeer());
	}

	@Test
	void testEineAuswahlOhneAngeforderteIdsIstNichtBewusstLeer() {
		// Ohne Anforderung gibt es nichts, das bewusst ausgelassen worden wäre; diesen Fall weist die Eingangsprüfung ab.
		assertFalse(ReportingAuswahlergebnis.aus(List.of(), Map.of(), Map.of(), List.of()).bewusstLeer());
	}

	@Test
	void testEineAuswahlIstAuchDannBewusstLeerWennAllesAusgefiltertWurde() {
		// Der Filter ist kein Ausgabeproblem, führt aber ebenso zu einer Ausgabe ohne Datensätze.
		final ReportingAuswahlergebnis<String> auswahl = ReportingAuswahlergebnis.aus(List.of(1L), Map.of(), Map.of(), List.of(1L));

		assertTrue(auswahl.bewusstLeer());
		assertTrue(auswahl.ausgelassen().isEmpty(), "Ein ausgefilterter Datensatz erscheint nicht unter den ausgelassenen.");
	}

	@Test
	void testEineEinschraenkungLaesstDasBisherigeErgebnisUnveraendert() {
		final ReportingAuswahlergebnis<String> auswahl = auswahlMit(List.of(1L, 2L));

		final ReportingAuswahlergebnis<String> eingeschraenkt = auswahl.nurMitGeladenen(
				Map.of(1L, ReportingLadezustand.geladen("Zusatzdaten"), 2L, ReportingLadezustand.nichtVorhanden()));

		assertEquals(List.of(1L, 2L), auswahl.idsAusgewaehlt(), "Das Ergebnis ist unveränderlich; die Einschränkung liefert ein neues.");
		assertEquals(List.of(1L), eingeschraenkt.idsAusgewaehlt());
		assertEquals(List.of(2L), List.copyOf(eingeschraenkt.ausgelassen().keySet()));
	}

	@Test
	void testEineEinschraenkungErhaeltDieZuvorAusgelassenen() {
		final ReportingAuswahlergebnis<String> auswahl = ReportingAuswahlergebnis.aus(List.of(1L, 2L, 3L), Map.of(1L, "Objekt 1", 2L, "Objekt 2"),
				Map.of(3L, ReportingLadezustand.nichtVorhanden()), List.of());

		final ReportingAuswahlergebnis<String> eingeschraenkt = auswahl.nurMitGeladenen(
				Map.of(1L, ReportingLadezustand.geladen("Zusatzdaten"), 2L, ReportingLadezustand.nichtVorhanden()));

		assertEquals(List.of(2L, 3L), List.copyOf(eingeschraenkt.ausgelassen().keySet()).stream().sorted().toList());
	}

	@Test
	void testEineEinschraenkungUebernimmtDieUrsacheDesGescheitertenZugriffs() {
		// Eine fehlende Akte und ein gescheiterter Datenbankzugriff lassen beide den Datensatz entfallen - nur der zweite ist eine Störung.
		final IllegalStateException fehler = new IllegalStateException("Der Dienst antwortet nicht.");
		final ReportingAuswahlergebnis<String> auswahl = auswahlMit(List.of(1L, 2L));

		final ReportingAuswahlergebnis<String> eingeschraenkt = auswahl.nurMitGeladenen(Map.of(
				1L, ReportingLadezustand.nichtVorhanden(),
				2L, ReportingLadezustand.fehlgeschlagen(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, fehler)));

		assertEquals(ReportingProblemursache.NICHT_VORHANDEN, eingeschraenkt.ausgelassen().get(1L).ursache());
		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, eingeschraenkt.ausgelassen().get(2L).ursache());
		assertEquals(fehler, eingeschraenkt.ausgelassen().get(2L).fehler(), "Der auslösende Fehler gehört in die spätere Meldung.");
	}

	@Test
	void testEineIdOhneGepruefteZusatzdatenBleibtInDerAuswahl() {
		// Die übergebenen Zustände beschreiben nur, was geprüft wurde - eine fehlende Angabe ist kein Befund.
		final ReportingAuswahlergebnis<String> auswahl = auswahlMit(List.of(1L, 2L));

		final ReportingAuswahlergebnis<String> eingeschraenkt = auswahl.nurMitGeladenen(Map.of(1L, ReportingLadezustand.geladen("Zusatzdaten")));

		assertEquals(List.of(1L, 2L), eingeschraenkt.idsAusgewaehlt());
	}

	@Test
	void testDieAusgelassenenSindVonAussenNichtVeraenderbar() {
		final ReportingAuswahlergebnis<String> auswahl = auswahlMit(List.of(1L));
		final Map<Long, ReportingLadezustand<String>> ausgelassen = auswahl.ausgelassen();

		assertThrows(UnsupportedOperationException.class, () -> ausgelassen.put(2L, ReportingLadezustand.nichtVorhanden()));
	}

}
