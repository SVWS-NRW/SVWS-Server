package de.svws_nrw.module.reporting.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlageGruppe;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinition;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinition;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp;
import de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils;
import de.svws_nrw.db.Benutzer;

class TestReportingParameterBuilder {

	/** Die Kombinier-Methoden nutzen ServerMode und Benutzer nicht; daher genügt hier STABLE und null. */
	private final ReportingParameterBuilder builder = new ReportingParameterBuilder(new Logger(), ServerMode.STABLE, null);

	@Test
	void testOhneConfigUndUebermitteltLiefertKatalogDefaults() {
		final List<ReportingReportvorlageParameterGruppe> soll = List.of(gruppe("G", true, boolParam("a", true), boolParam("b", false)));
		final List<ReportingReportvorlageParameterGruppe> ergebnis =
				builder.kombiniereParameterGruppen(soll, new HashMap<>(), new HashMap<>(), new HashSet<>(), null);
		assertEquals(Map.of("a", "true", "b", "false"), flach(ergebnis));
	}

	@Test
	void testConfigUeberlagertDefault() {
		final List<ReportingReportvorlageParameterGruppe> soll = List.of(gruppe("G", true, boolParam("a", true)));
		final List<ReportingReportvorlageParameterGruppe> ergebnis =
				builder.kombiniereParameterGruppen(soll, Map.of("a", "false"), new HashMap<>(), new HashSet<>(), null);
		assertEquals("false", flach(ergebnis).get("a"));
	}

	@Test
	void testUebermitteltUeberlagertConfigUndDefault() {
		final List<ReportingReportvorlageParameterGruppe> soll = List.of(gruppe("G", true, boolParam("a", true)));
		final List<ReportingReportvorlageParameterGruppe> ergebnis =
				builder.kombiniereParameterGruppen(soll, Map.of("a", "false"), Map.of("a", "true"), new HashSet<>(), null);
		assertEquals("true", flach(ergebnis).get("a"), "Der übermittelte Wert muss Config- und Default-Ebene überlagern.");
	}

	@Test
	void testNichtTypkonformerUebermittelterWertWirdVerworfen() {
		final List<ReportingReportvorlageParameterGruppe> soll = List.of(gruppe("G", true, boolParam("a", true)));
		final List<ReportingReportvorlageParameterGruppe> ergebnis =
				builder.kombiniereParameterGruppen(soll, new HashMap<>(), Map.of("a", "vielleicht"), new HashSet<>(), null);
		assertEquals("true", flach(ergebnis).get("a"), "Ein nicht typkonformer BOOLEAN-Wert muss verworfen werden, sodass der Default gilt.");
	}

	@Test
	void testNichtTypkonformerConfigWertWirdVerworfenUebermitteltGiltWeiter() {
		final List<ReportingReportvorlageParameterGruppe> soll = List.of(gruppe("G", true, boolParam("a", true)));
		final List<ReportingReportvorlageParameterGruppe> ergebnis =
				builder.kombiniereParameterGruppen(soll, Map.of("a", "ja"), Map.of("a", "false"), new HashSet<>(), null);
		assertEquals("false", flach(ergebnis).get("a"), "Ein defekter Config-Wert wird verworfen, der typkonforme übermittelte Wert greift.");
	}

	@Test
	void testBereitsVergebeneNamenWerdenUebersprungen() {
		final List<ReportingReportvorlageParameterGruppe> soll = List.of(gruppe("G", true, boolParam("a", true), boolParam("b", false)));
		final Set<String> vergebeneNamen = new HashSet<>(Set.of("a"));
		final List<ReportingReportvorlageParameterGruppe> ergebnis =
				builder.kombiniereParameterGruppen(soll, new HashMap<>(), new HashMap<>(), vergebeneNamen, null);
		final Map<String, String> flach = flach(ergebnis);
		assertFalse(flach.containsKey("a"), "Ein bereits vergebener Name darf nicht erneut kombiniert werden.");
		assertTrue(flach.containsKey("b"), "Noch nicht vergebene Namen werden weiterhin kombiniert.");
		assertTrue(vergebeneNamen.contains("b"), "Neu verarbeitete Namen werden zu den vergebenen Namen ergänzt.");
	}

	@Test
	void testSichtbarkeitUeberschreibung() {
		final List<ReportingReportvorlageParameterGruppe> soll = List.of(gruppe("G", true, boolParam("a", true)));

		final List<ReportingReportvorlageParameterGruppe> erzwungen =
				builder.kombiniereParameterGruppen(soll, new HashMap<>(), new HashMap<>(), new HashSet<>(), false);
		assertFalse(erzwungen.get(0).uiIstSichtbar, "Bei Überschreibung mit false muss die Gruppe unsichtbar sein.");

		final List<ReportingReportvorlageParameterGruppe> ausSoll =
				builder.kombiniereParameterGruppen(soll, new HashMap<>(), new HashMap<>(), new HashSet<>(), null);
		assertTrue(ausSoll.get(0).uiIstSichtbar, "Bei null wird die Sichtbarkeit der SOLL-Gruppe (true) übernommen.");
	}

	@Test
	void testSammleUebermittelteWerteNamensbasiert() {
		final ReportingParameter rp = new ReportingParameter();
		rp.ausgabeformat = ReportingAusgabeformat.HTML.getId();
		rp.reportvorlageParameterGruppen = List.of(
				gruppe("G1", true, stringParam("titel", "X")),
				gruppe("G2", true, boolParam("einzelausgabeDaten", true)));

		final Map<String, String> werte = ReportingParameterBuilder.sammleUebermittelteWerte(rp);
		assertEquals("X", werte.get("titel"), "Werte werden namensbasiert über alle Gruppen gesammelt.");
		assertEquals("true", werte.get("einzelausgabeDaten"),
				"Beim Sammeln der übermittelten Werte greift keine ausgabeformatabhängige Regel mehr - der Wert bleibt unverändert.");
	}

	@Test
	void testEinzelausgabeWirdAusgabeformatabhaengigErzwungen() {
		// Die Vorlage dient hier nur als Träger der Setter-Methode; erzwungen wird auf dem übergebenen Parametersatz.
		final ReportingReportvorlage vorlage = ReportingReportvorlage.LEHRER_V_STAMMDATENLISTE;

		final ReportingParameter html = parameterMitEinzelausgabe(ReportingAusgabeformat.HTML.getId(), true);
		ReportingParameterBuilder.erzwingeAusgabeformatabhaengigeParameter(html, vorlage);
		assertEquals("false", einzelausgabeWert(html), "Bei HTML wird die Einzelausgabe auf false erzwungen.");

		final ReportingParameter email = parameterMitEinzelausgabe(ReportingAusgabeformat.EMAIL.getId(), false);
		ReportingParameterBuilder.erzwingeAusgabeformatabhaengigeParameter(email, vorlage);
		assertEquals("true", einzelausgabeWert(email), "Beim E-Mail-Versand wird die Einzelausgabe auf true erzwungen.");

		final ReportingParameter pdf = parameterMitEinzelausgabe(ReportingAusgabeformat.PDF.getId(), true);
		ReportingParameterBuilder.erzwingeAusgabeformatabhaengigeParameter(pdf, vorlage);
		assertEquals("true", einzelausgabeWert(pdf), "Bei PDF bleibt der kombinierte Wert unverändert.");
	}


	@Test
	void testSammleStandardWerteErsteQuelleGewinnt() {
		final HashMap<String, String> ziel = new HashMap<>();
		ReportingParameterBuilder.sammleStandardWerte(List.of(gruppe("Vorlage", true, boolParam("a", true))), ziel);
		ReportingParameterBuilder.sammleStandardWerte(List.of(gruppe("Benutzer", false, boolParam("a", false), boolParam("b", false))), ziel);
		assertEquals("true", ziel.get("a"), "Bei Namensüberschneidung bleibt der zuerst eingetragene Standardwert erhalten.");
		assertEquals("false", ziel.get("b"), "Neue Namen werden ergänzt.");
	}

	@Test
	void testGespeicherteSortierungWirdBeiLeererAuswahlAngewendet() {
		final ReportingSortierungDefinition nachname = sortierung("Nachname");
		final ReportingSortierungDefinition klasse = sortierung("Klasse");
		final ReportingSortierungDefinitionGruppe soll = sortierGruppe("Schülersortierung", nachname, klasse);
		final ReportingSortierungDefinitionGruppe ziel = sortierGruppe("Schülersortierung", sortierung("Nachname"), sortierung("Klasse"));

		ReportingParameterBuilder.wendeGespeicherteSortierungAn(List.of(ziel), List.of(soll),
				List.of(auswahl("Schülersortierung", "Klasse")));

		assertEquals(1, ziel.sortierungDefinitionen.size());
		assertSame(klasse, ziel.sortierungDefinitionen.get(0), "Es muss die SOLL-Options-Instanz übernommen werden.");
	}

	@Test
	void testExplizitUebermittelteSortierungGewinnt() {
		final ReportingSortierungDefinition nachname = sortierung("Nachname");
		final ReportingSortierungDefinitionGruppe soll = sortierGruppe("Schülersortierung", nachname, sortierung("Klasse"));
		final ReportingSortierungDefinitionGruppe ziel = sortierGruppe("Schülersortierung", nachname);
		ziel.sortierungDefinitionen = new ArrayList<>(List.of(nachname));

		ReportingParameterBuilder.wendeGespeicherteSortierungAn(List.of(ziel), List.of(soll),
				List.of(auswahl("Schülersortierung", "Klasse")));

		assertEquals(List.of("Nachname"), ziel.sortierungDefinitionen.stream().map(sd -> sd.bezeichnung).toList(),
				"Eine explizit übermittelte Auswahl darf nicht von den gespeicherten Einstellungen überschrieben werden.");
	}

	@Test
	void testUnbekannteSortierBezeichnungWirdIgnoriert() {
		final ReportingSortierungDefinition klasse = sortierung("Klasse");
		final ReportingSortierungDefinitionGruppe soll = sortierGruppe("Schülersortierung", klasse);
		final ReportingSortierungDefinitionGruppe ziel = sortierGruppe("Schülersortierung", sortierung("Klasse"));

		ReportingParameterBuilder.wendeGespeicherteSortierungAn(List.of(ziel), List.of(soll),
				List.of(auswahl("Schülersortierung", "Klasse", "Weggefallen")));

		assertEquals(List.of("Klasse"), ziel.sortierungDefinitionen.stream().map(sd -> sd.bezeichnung).toList(),
				"Eine nicht mehr im Katalog vorhandene Bezeichnung wird übersprungen.");
	}

	@Test
	void testGespeicherteFilterungMehrfachauswahlInReihenfolge() {
		final ReportingFilterDefinition aktiv = filter("Aktiv");
		final ReportingFilterDefinition extern = filter("Extern");
		final ReportingFilterDefinitionGruppe soll = filterGruppe("Schülerstatus", aktiv, extern, filter("Ehemalig"));
		final ReportingFilterDefinitionGruppe ziel = filterGruppe("Schülerstatus", filter("Aktiv"), filter("Extern"), filter("Ehemalig"));

		ReportingParameterBuilder.wendeGespeicherteFilterungAn(List.of(ziel), List.of(soll),
				List.of(auswahl("Schülerstatus", "Extern", "Aktiv")));

		assertEquals(List.of("Extern", "Aktiv"), ziel.filterDefinitionen.stream().map(fd -> fd.bezeichnung).toList(),
				"Die Mehrfachauswahl muss in der gespeicherten Reihenfolge übernommen werden.");
		assertSame(extern, ziel.filterDefinitionen.get(0), "Es muss die SOLL-Options-Instanz übernommen werden.");
	}

	@Test
	void testOhneGespeicherteAuswahlBleibtSortierungUnveraendert() {
		final ReportingSortierungDefinitionGruppe soll = sortierGruppe("Schülersortierung", sortierung("Klasse"));
		final ReportingSortierungDefinitionGruppe ziel = sortierGruppe("Schülersortierung", sortierung("Klasse"));

		ReportingParameterBuilder.wendeGespeicherteSortierungAn(List.of(ziel), List.of(soll), List.of());

		assertTrue(ziel.sortierungDefinitionen.isEmpty(), "Ohne gespeicherte Auswahl bleibt die (leere) Auswahl unverändert.");
	}

	@Test
	void testIstEinstellungErlaubtServerModePruefung() {
		final ReportingParameterBuilder builderBeta = new ReportingParameterBuilder(new Logger(), ServerMode.BETA, null);
		assertTrue(builderBeta.istEinstellungErlaubt("", List.of()), "Ohne Anforderung ist die Einstellung erlaubt.");
		assertTrue(builderBeta.istEinstellungErlaubt("beta", List.of()), "Im BETA-Modus ist eine BETA-Anforderung erlaubt.");
		assertFalse(builderBeta.istEinstellungErlaubt("alpha", List.of()), "Im BETA-Modus ist eine ALPHA-Anforderung nicht erlaubt.");
		assertFalse(builderBeta.istEinstellungErlaubt("dev", List.of()), "Im BETA-Modus ist eine DEV-Anforderung nicht erlaubt.");
	}

	@Test
	void testIstEinstellungErlaubtKompetenzPruefung() {
		final Benutzer benutzer = Benutzer.create(null);
		benutzer.getKompetenzen().add(BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
		final ReportingParameterBuilder builderMitKompetenz = new ReportingParameterBuilder(new Logger(), ServerMode.STABLE, benutzer);

		assertTrue(builderMitKompetenz.istEinstellungErlaubt("", List.of(BenutzerKompetenz.LEHRERDATEN_ANSEHEN.daten.id)),
				"Mit vorhandener Kompetenz ist die Einstellung erlaubt.");
		assertFalse(builderMitKompetenz.istEinstellungErlaubt("", List.of(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN.daten.id)),
				"Ohne die geforderte Kompetenz ist die Einstellung nicht erlaubt.");
	}

	@Test
	void testIstEinstellungErlaubtServerModeGewinntVorKompetenz() {
		final Benutzer benutzer = Benutzer.create(null);
		benutzer.getKompetenzen().add(BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
		final ReportingParameterBuilder builderBeta = new ReportingParameterBuilder(new Logger(), ServerMode.BETA, benutzer);

		assertFalse(builderBeta.istEinstellungErlaubt("dev", List.of(BenutzerKompetenz.LEHRERDATEN_ANSEHEN.daten.id)),
				"Ein nicht erfüllter ServerMode sperrt die Einstellung, auch wenn die Kompetenz vorhanden ist.");
	}

	@Test
	void testSetzeUnerlaubteParameterZurueckNurGesperrtenParameter() {
		final ReportingParameterBuilder builderBeta = new ReportingParameterBuilder(new Logger(), ServerMode.BETA, null);
		final ReportingReportvorlageParameterGruppe gruppe = ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("G", "", true, 1,
				List.of(boolParamMitServerMode("erlaubt", ServerMode.STABLE), boolParamMitServerMode("gesperrt", ServerMode.ALPHA)));
		gruppe.reportvorlageParameter.get(0).wert = "false";
		gruppe.reportvorlageParameter.get(1).wert = "false";

		final HashMap<String, String> standardWerte = new HashMap<>();
		standardWerte.put("erlaubt", "true");
		standardWerte.put("gesperrt", "true");
		builderBeta.setzeUnerlaubteParameterZurueck(standardWerte, gruppe);

		assertEquals("false", gruppe.reportvorlageParameter.get(0).wert, "Ein im ServerMode erlaubter Parameter behält seinen übermittelten Wert.");
		assertEquals("true", gruppe.reportvorlageParameter.get(1).wert, "Ein im ServerMode gesperrter Parameter wird auf den Standardwert zurückgesetzt.");
	}

	@Test
	void testSetzeUnerlaubteParameterZurueckBeiGesperrterGruppe() {
		final ReportingParameterBuilder builderBeta = new ReportingParameterBuilder(new Logger(), ServerMode.BETA, null);
		final ReportingReportvorlageParameterGruppe gruppe = ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("G", "", true, 1,
				ServerMode.ALPHA, List.of(), List.of(boolParamMitServerMode("a", ServerMode.STABLE)));
		gruppe.reportvorlageParameter.get(0).wert = "false";

		final HashMap<String, String> standardWerte = new HashMap<>();
		standardWerte.put("a", "true");
		builderBeta.setzeUnerlaubteParameterZurueck(standardWerte, gruppe);

		assertEquals("true", gruppe.reportvorlageParameter.get(0).wert,
				"Ist die Gruppe im ServerMode gesperrt, wird auch ein an sich erlaubter Parameter zurückgesetzt.");
	}

	@Test
	void testSetzeUnerlaubteSortierungZurueckBeiGesperrterGruppe() {
		final ReportingParameterBuilder builderBeta = new ReportingParameterBuilder(new Logger(), ServerMode.BETA, null);
		final ReportingSortierungDefinition sollAuswahl = sortierung("Klasse");
		final ReportingSortierungDefinitionGruppe sollGruppe = ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe(
				"Schülersortierung", "ReportingSchueler", true, ServerMode.ALPHA, List.of(), List.of(sollAuswahl, sortierung("Nachname")));
		sollGruppe.sortierungDefinitionen = new ArrayList<>(List.of(sollAuswahl));

		final ReportingSortierungDefinitionGruppe zielGruppe = sortierGruppe("Schülersortierung", sortierung("Klasse"), sortierung("Nachname"));
		zielGruppe.sortierungDefinitionen = new ArrayList<>(List.of(zielGruppe.sortierungDefinitionenOptionen.get(1)));

		builderBeta.setzeUnerlaubteSortierungZurueck(parameterMitSortierung(zielGruppe), parameterMitSortierung(sollGruppe));

		assertEquals(List.of("Klasse"), zielGruppe.sortierungDefinitionen.stream().map(sd -> sd.bezeichnung).toList(),
				"Bei gesperrter Gruppe wird die übermittelte Auswahl auf die SOLL-Auswahl zurückgesetzt.");
	}

	@Test
	void testSetzeUnerlaubteSortierungZurueckLaesstErlaubteGruppeUnveraendert() {
		final ReportingParameterBuilder builderBeta = new ReportingParameterBuilder(new Logger(), ServerMode.BETA, null);
		final ReportingSortierungDefinitionGruppe sollGruppe = ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe(
				"Schülersortierung", "ReportingSchueler", true, ServerMode.STABLE, List.of(), List.of(sortierung("Klasse"), sortierung("Nachname")));
		sollGruppe.sortierungDefinitionen = new ArrayList<>(List.of(sollGruppe.sortierungDefinitionenOptionen.get(0)));

		final ReportingSortierungDefinitionGruppe zielGruppe = sortierGruppe("Schülersortierung", sortierung("Klasse"), sortierung("Nachname"));
		zielGruppe.sortierungDefinitionen = new ArrayList<>(List.of(zielGruppe.sortierungDefinitionenOptionen.get(1)));

		builderBeta.setzeUnerlaubteSortierungZurueck(parameterMitSortierung(zielGruppe), parameterMitSortierung(sollGruppe));

		assertEquals(List.of("Nachname"), zielGruppe.sortierungDefinitionen.stream().map(sd -> sd.bezeichnung).toList(),
				"Eine im ServerMode erlaubte Gruppe behält ihre übermittelte Auswahl.");
	}

	@Test
	void testSetzeUnerlaubteFilterungZurueckBeiGesperrterGruppe() {
		final ReportingParameterBuilder builderBeta = new ReportingParameterBuilder(new Logger(), ServerMode.BETA, null);
		final ReportingFilterDefinition sollAuswahl = filter("Aktiv");
		final ReportingFilterDefinitionGruppe sollGruppe = ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe(
				"Schülerstatus", "ReportingSchueler", true, true, ReportingFilterVerknuepfung.OR, ServerMode.ALPHA, List.of(),
				List.of(sollAuswahl, filter("Extern")));
		sollGruppe.filterDefinitionen = new ArrayList<>(List.of(sollAuswahl));

		final ReportingFilterDefinitionGruppe zielGruppe = filterGruppe("Schülerstatus", filter("Aktiv"), filter("Extern"));
		zielGruppe.filterDefinitionen = new ArrayList<>(List.of(zielGruppe.filterDefinitionenOptionen.get(1)));

		builderBeta.setzeUnerlaubteFilterungZurueck(parameterMitFilterung(zielGruppe), parameterMitFilterung(sollGruppe));

		assertEquals(List.of("Aktiv"), zielGruppe.filterDefinitionen.stream().map(fd -> fd.bezeichnung).toList(),
				"Bei gesperrter Gruppe wird die übermittelte Filterauswahl auf die SOLL-Auswahl zurückgesetzt.");
	}

	private static ReportingReportvorlageParameter boolParamMitServerMode(final String name, final ServerMode serverMode) {
		return ReportingReportvorlageUtils.erzeugeVorlageParameter(name, name, ReportingReportvorlageParameterTyp.BOOLEAN, "true", true,
				ReportingUIKomponentenTyp.CHECKBOX, 1, serverMode, List.of());
	}

	private static ReportingParameter parameterMitSortierung(final ReportingSortierungDefinitionGruppe gruppe) {
		final ReportingParameter parameter = new ReportingParameter();
		parameter.sortierungDefinitionenGruppen = new ArrayList<>(List.of(gruppe));
		return parameter;
	}

	private static ReportingParameter parameterMitFilterung(final ReportingFilterDefinitionGruppe gruppe) {
		final ReportingParameter parameter = new ReportingParameter();
		parameter.filterDefinitionenGruppen = new ArrayList<>(List.of(gruppe));
		return parameter;
	}

	private static ReportingSortierungDefinition sortierung(final String bezeichnung) {
		final ReportingSortierungDefinition sd = new ReportingSortierungDefinition();
		sd.bezeichnung = bezeichnung;
		sd.typ = "ReportingSchueler";
		return sd;
	}

	private static ReportingSortierungDefinitionGruppe sortierGruppe(final String bezeichnung, final ReportingSortierungDefinition... optionen) {
		return ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe(bezeichnung, "ReportingSchueler", true, List.of(optionen));
	}

	private static ReportingFilterDefinition filter(final String bezeichnung) {
		final ReportingFilterDefinition fd = new ReportingFilterDefinition();
		fd.bezeichnung = bezeichnung;
		fd.typ = "ReportingSchueler";
		return fd;
	}

	private static ReportingFilterDefinitionGruppe filterGruppe(final String bezeichnung, final ReportingFilterDefinition... optionen) {
		return ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe(bezeichnung, "ReportingSchueler", true, true, ReportingFilterVerknuepfung.OR,
				List.of(optionen));
	}

	private static ReportingEinstellungenBenutzerVorlageGruppe auswahl(final String gruppe, final String... bezeichnungen) {
		final ReportingEinstellungenBenutzerVorlageGruppe a = new ReportingEinstellungenBenutzerVorlageGruppe();
		a.gruppe = gruppe;
		a.bezeichnungen = new ArrayList<>(List.of(bezeichnungen));
		return a;
	}

	private static ReportingReportvorlageParameterGruppe gruppe(final String name, final boolean sichtbar,
			final ReportingReportvorlageParameter... parameter) {
		return ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe(name, "", sichtbar, 1, List.of(parameter));
	}

	private static ReportingReportvorlageParameter boolParam(final String name, final boolean wert) {
		return ReportingReportvorlageUtils.erzeugeVorlageParameter(name, name, ReportingReportvorlageParameterTyp.BOOLEAN, "" + wert, true,
				ReportingUIKomponentenTyp.CHECKBOX, 1);
	}

	private static ReportingParameter parameterMitEinzelausgabe(final int ausgabeformat, final boolean wert) {
		final ReportingParameter rp = new ReportingParameter();
		rp.ausgabeformat = ausgabeformat;
		rp.reportvorlageParameterGruppen = List.of(gruppe("Ausgabeoptionen", true, boolParam("einzelausgabeDaten", wert)));
		return rp;
	}

	private static String einzelausgabeWert(final ReportingParameter reportingParameter) {
		return reportingParameter.reportvorlageParameterGruppen.getFirst().reportvorlageParameter.getFirst().wert;
	}

	private static ReportingReportvorlageParameter stringParam(final String name, final String wert) {
		return ReportingReportvorlageUtils.erzeugeVorlageParameter(name, name, ReportingReportvorlageParameterTyp.STRING, wert, true,
				ReportingUIKomponentenTyp.INPUT, 1);
	}

	private static Map<String, String> flach(final List<ReportingReportvorlageParameterGruppe> gruppen) {
		final Map<String, String> werte = new LinkedHashMap<>();
		for (final ReportingReportvorlageParameterGruppe gruppe : gruppen) {
			for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
				werte.put(parameter.name, parameter.wert);
			}
		}
		return werte;
	}
}
