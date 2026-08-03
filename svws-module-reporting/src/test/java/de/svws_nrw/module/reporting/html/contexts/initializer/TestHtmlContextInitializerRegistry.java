package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext;

/**
 * Diese Tests prüfen ausschließlich die Konfigurationen der Registry. Die sind ohne Reporting-Context lesbar, weshalb hier weder eine Datenbank noch ein
 * Mocking-Framework nötig ist.
 * <p>Die Sollwerte sind <b>bewusst als Literale</b> hinterlegt und nicht aus dem Produktivcode abgeleitet: Sie stammen aus der Zuordnungstabelle des
 * Umbau-Plans und wurden vor der Umsetzung festgelegt. Würden sie aus den Konstanten des Produktivcodes gezogen, schriebe der Test einen Zuordnungsfehler
 * einfach mit.</p>
 */
class TestHtmlContextInitializerRegistry {

	/** Der Datenaufbau jeder Reportvorlage gemäß Zuordnungstabelle: 28 Vorlagen auf 16 Datenaufbauten. */
	private static final Map<ReportingReportvorlage, ReportingReportvorlageDatenContext> SOLL_DATENAUFBAU_JE_VORLAGE = Map.ofEntries(
			Map.entry(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG, ReportingReportvorlageDatenContext.SCHUELER),
			Map.entry(ReportingReportvorlage.SCHUELER_V_LISTE_KONTAKTDATENERZIEHER, ReportingReportvorlageDatenContext.SCHUELER),
			Map.entry(ReportingReportvorlage.SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN,
					ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG),
			Map.entry(ReportingReportvorlage.SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT,
					ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG),
			Map.entry(ReportingReportvorlage.SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3, ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR),
			Map.entry(ReportingReportvorlage.SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4, ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR),

			Map.entry(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN, ReportingReportvorlageDatenContext.KLASSEN),
			Map.entry(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER, ReportingReportvorlageDatenContext.KLASSEN),
			Map.entry(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN, ReportingReportvorlageDatenContext.KLASSEN),
			Map.entry(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT, ReportingReportvorlageDatenContext.KLASSEN),

			Map.entry(ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER, ReportingReportvorlageDatenContext.KURSE),
			Map.entry(ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_FOTOS_NAMEN, ReportingReportvorlageDatenContext.KURSE),
			Map.entry(ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN, ReportingReportvorlageDatenContext.KURSE),

			Map.entry(ReportingReportvorlage.LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN, ReportingReportvorlageDatenContext.LEHRER),
			Map.entry(ReportingReportvorlage.LEHRER_V_STAMMDATENLISTE, ReportingReportvorlageDatenContext.LEHRER),

			Map.entry(ReportingReportvorlage.STUNDENPLANUNG_V_FACH_STUNDENPLAN, ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH),
			Map.entry(ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN, ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN),
			Map.entry(ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN, ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER),
			Map.entry(ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT, ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER),
			Map.entry(ReportingReportvorlage.STUNDENPLANUNG_V_RAUM_STUNDENPLAN, ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM),
			Map.entry(ReportingReportvorlage.STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN, ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER),

			Map.entry(ReportingReportvorlage.GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN, ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE),
			Map.entry(ReportingReportvorlage.GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN, ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE),
			Map.entry(ReportingReportvorlage.GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN, ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER),
			Map.entry(ReportingReportvorlage.GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN,
					ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER),

			Map.entry(ReportingReportvorlage.GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN,
					ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_SCHUELER),
			Map.entry(ReportingReportvorlage.GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN,
					ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_TERMINE),

			Map.entry(ReportingReportvorlage.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN,
					ReportingReportvorlageDatenContext.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG));

	/** Der Schlüssel des Haupt-Contexts je Datenaufbau gemäß Zuordnungstabelle. Die abweichenden Schreibweisen sind Bestand und bewusst so hinterlegt. */
	private static final Map<ReportingReportvorlageDatenContext, String> SOLL_CONTEXT_SCHLUESSEL = Map.ofEntries(
			Map.entry(ReportingReportvorlageDatenContext.SCHUELER, "Schueler"),
			Map.entry(ReportingReportvorlageDatenContext.SCHUELER_GOST_LAUFBAHNPLANUNG, "Schueler"),
			Map.entry(ReportingReportvorlageDatenContext.SCHUELER_GOST_ABITUR, "Schueler"),
			Map.entry(ReportingReportvorlageDatenContext.KLASSEN, "Klassen"),
			Map.entry(ReportingReportvorlageDatenContext.KURSE, "Kurse"),
			Map.entry(ReportingReportvorlageDatenContext.LEHRER, "Lehrer"),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH, "FaecherStundenplaene"),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN, "KlassenStundenplaene"),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER, "LehrerStundenplaene"),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM, "RaeumeStundenplaene"),
			Map.entry(ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER, "SchuelerStundenplaene"),
			Map.entry(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_KURSE, "GostBlockungsergebnis"),
			Map.entry(ReportingReportvorlageDatenContext.GOST_KURSPLANUNG_SCHUELER, "GostBlockungsergebnis"),
			Map.entry(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_SCHUELER, "GostKlausurplan"),
			Map.entry(ReportingReportvorlageDatenContext.GOST_KLAUSURPLANUNG_TERMINE, "GostKlausurplan"),
			Map.entry(ReportingReportvorlageDatenContext.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG,
					"GostLaufbahnplanungAbiturjahrgangFachwahlStatistiken"));

	/** Die Datenaufbauten ohne Einzelausgabe gemäß Zuordnungstabelle. */
	private static final Set<ReportingReportvorlageDatenContext> SOLL_OHNE_EINZELAUSGABE =
			Set.of(ReportingReportvorlageDatenContext.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG);


	@Test
	void testJederDatenaufbauHatGenauEinenRegistryEintrag() {
		for (final ReportingReportvorlageDatenContext datenContext : ReportingReportvorlageDatenContext.values()) {
			assertNotNull(HtmlContextInitializerRegistry.aufbauOderNull(datenContext),
					"Zum Datenaufbau %s fehlt der Registry-Eintrag.".formatted(datenContext.name()));
		}
	}

	@Test
	void testDieSollwerteDeckenAlleDatenaufbautenAb() {
		final Set<ReportingReportvorlageDatenContext> alle = Set.of(ReportingReportvorlageDatenContext.values());
		assertEquals(alle, SOLL_CONTEXT_SCHLUESSEL.keySet(), "Die Sollwerte der Map-Schlüssel decken nicht genau die vorhandenen Datenaufbauten ab.");
		assertEquals(16, alle.size(), "Die Zuordnungstabelle sieht 16 Datenaufbauten vor.");
	}

	@Test
	void testJedeReportvorlageVerweistAufDenErwartetenDatenaufbau() {
		assertEquals(ReportingReportvorlage.values().length, SOLL_DATENAUFBAU_JE_VORLAGE.size(),
				"Die Sollwerte decken nicht genau die vorhandenen Reportvorlagen ab.");

		final Map<String, String> ist = new TreeMap<>();
		final Map<String, String> soll = new TreeMap<>();
		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			ist.put(reportvorlage.name(), reportvorlage.getReportingReportvorlageDatenContext().name());
			soll.put(reportvorlage.name(), SOLL_DATENAUFBAU_JE_VORLAGE.get(reportvorlage).name());
		}
		assertEquals(soll, ist);
	}

	@Test
	void testJedeKonfigurationLiefertDenErwartetenMapSchluessel() {
		final Map<String, String> ist = new TreeMap<>();
		final Map<String, String> soll = new TreeMap<>();
		for (final ReportingReportvorlageDatenContext datenContext : ReportingReportvorlageDatenContext.values()) {
			ist.put(datenContext.name(), HtmlContextInitializerRegistry.aufbauOderNull(datenContext).contextSchluessel());
			soll.put(datenContext.name(), SOLL_CONTEXT_SCHLUESSEL.get(datenContext));
		}
		assertEquals(soll, ist);
	}

	@Test
	void testNurDieVorgesehenenDatenaufbautenMeldenKeineEinzelausgabe() {
		final Map<String, Boolean> ist = new TreeMap<>();
		final Map<String, Boolean> soll = new TreeMap<>();
		for (final ReportingReportvorlageDatenContext datenContext : ReportingReportvorlageDatenContext.values()) {
			ist.put(datenContext.name(), HtmlContextInitializerRegistry.aufbauOderNull(datenContext).unterstuetztEinzelausgabe());
			soll.put(datenContext.name(), !SOLL_OHNE_EINZELAUSGABE.contains(datenContext));
		}
		assertEquals(soll, ist);
	}

}
