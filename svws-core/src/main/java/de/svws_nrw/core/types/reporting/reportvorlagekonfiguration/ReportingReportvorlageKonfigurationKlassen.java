package de.svws_nrw.core.types.reporting.reportvorlagekonfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp;
import de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp;
import de.svws_nrw.core.utils.reporting.ReportingFilterDefinitionFactory;
import de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils;
import de.svws_nrw.core.utils.reporting.ReportingSortierungDefinitionFactory;
import jakarta.validation.constraints.NotNull;

// SONARQUBE WARNUNG: Es sollen Konstanten für wiederkehrende Strings definiert werden. Das ist hier bei betroffenen Elementen nicht zielführend.
@SuppressWarnings("java:S1192")
public final class ReportingReportvorlageKonfigurationKlassen {

	private ReportingReportvorlageKonfigurationKlassen() {
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage KlassenVListeSchuelerFotosNamen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getKlassenVListeSchuelerFotosNamen() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(
				List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), List.of(
						ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
								"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1,
								List.of(ReportingReportvorlageUtils.erzeugeVorlageParameter("anzahlBilderProZeile", "Anzahl Bilder pro Spalte",
										ReportingReportvorlageParameterTyp.INTEGER, "" + 4, true, ReportingUIKomponentenTyp.NUMBERPICKER, 1)
								))),
				ReportingReportvorlageUtils.erzeugeEmailParameter(
						ReportingEMailEmpfaengerTyp.KLASSENLEHRER,
						false,
						"",
						""),
				new ArrayList<>(), List.of(ReportingReportvorlageUtils.erzeugeSchuelerStatusfilterGruppe()), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage KlassenVListeSchuelerKontaktdatenerzieher.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getKlassenVListeSchuelerKontaktdatenerzieher() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(
				List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), List.of(
						ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
								"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 3,
								Arrays.asList(
										ReportingReportvorlageUtils.erzeugeVorlageParameter("ueberschrift", "Überschrift (Standard: Klassenliste <Kürzel>)",
												ReportingReportvorlageParameterTyp.STRING, "Klassenliste <Kürzel>", true, ReportingUIKomponentenTyp.INPUT, 3),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("bemerkung", "Bemerkung",
												ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 3),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFoto", "mit Foto",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerAlleVornamen", "mit allen Vornamen",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 3),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitErzieher", "mit Erziehern",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
								))),
				ReportingReportvorlageUtils.erzeugeEmailParameter(
						ReportingEMailEmpfaengerTyp.KLASSENLEHRER,
						false,
						"",
						""),
				new ArrayList<>(), List.of(ReportingReportvorlageUtils.erzeugeSchuelerStatusfilterGruppe()), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage KlassenVListeSchuelerLeistungsdaten.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getKlassenVListeSchuelerLeistungsdaten() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()),
				List.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitIndividuellerKursart", "mit individueller Kursart",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitQuartalsnote", "Quartalsnoten statt Noten ausgeben",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitGesamtfehlstunden", "mit Gesamtfehlstunden",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachbezogenenFehlstunden", "mit fachbezogenen Fehlstunden",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachbezogenenBemerkungen", "mit fachbezogenen Bemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFoerderschwerpunkt", "mit Angaben zum Förderschwerpunkt",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLernentwicklung", "mit Angabe zur Lernentwicklung",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitASVBemerkung", "mit ASV-Bemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitAUEBemerkung", "mit AUE-Bemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitZeugnisbemerkung", "mit Zeugnisbemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitZuweisung", "mit Zuweisungen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitVersetzungAbschluss", "mit Versetzung und Abschluss",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitVersetzungsentscheidung", "mit Text zur Versetzungsentscheidung",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitUebergangsempfehlung", "mit Übergangsempfehlung",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitQuerformat", "Ausgabe im Querformat erzeugen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2)))),
				new ReportingEMailDaten(),
				List.of(ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe("Leistungsdatensortierung", "ReportingSchuelerLeistungsdaten", true,
						ReportingSortierungDefinitionFactory.definitionen(
								ReportingSortierungDefinitionFactory.standard("Gemäß Fächersortierung", "ReportingSchuelerLeistungsdaten"),
								ReportingSortierungDefinitionFactory.definition("Gemäß GOSt-Fach-Sortierung", "ReportingSchuelerLeistungsdaten", false,
										List.of("fach.gostSortierung")),
								ReportingSortierungDefinitionFactory.definition("Gemäß Fachkürzeln", "ReportingSchuelerLeistungsdaten", false,
										List.of("fach.kuerzel")))
				)),
				List.of(ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe("Leistungsdatenfilter", "ReportingSchuelerLeistungsdaten", true, true,
						ReportingFilterVerknuepfung.AND,
						ReportingFilterDefinitionFactory.definitionen(
								ReportingFilterDefinitionFactory.definition("Nur zeugnisrelevante Einträge", "ReportingSchuelerLeistungsdaten",
										ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("aufZeugnis", "true"))),
								ReportingFilterDefinitionFactory.definition(
										"Nur Einträge mit prüfungsordnungsrelevanten Fächern", "ReportingSchuelerLeistungsdaten",
										ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("fach.istPruefungsordnungsRelevant", "true"))))
				), ReportingReportvorlageUtils.erzeugeSchuelerStatusfilterGruppe()), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage KlassenVListeSchuelerLeistungsdatenDetailliert.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getKlassenVListeSchuelerLeistungsdatenDetailliert() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()),
				List.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitIndividuellerKursart", "mit individueller Kursart",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitGesamtfehlstunden", "mit Gesamtfehlstunden",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachbezogenenFehlstunden", "mit fachbezogenen Fehlstunden",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachbezogenenBemerkungen", "mit fachbezogenen Bemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitAnkreuzkompetenzen", "mit Ankreuzkompetenzen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFoerderschwerpunkt", "mit Angaben zum Förderschwerpunkt",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLernentwicklung", "mit Angabe zur Lernentwicklung",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitASVBemerkung", "mit ASV-Bemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitAUEBemerkung", "mit AUE-Bemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitZeugnisbemerkung", "mit Zeugnisbemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitVersetzungAbschluss", "mit Versetzung und Abschluss",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitVersetzungsentscheidung", "mit Text zur Versetzungsentscheidung",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitUebergangsempfehlung", "mit Übergangsempfehlung",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSeitenumbruch", "Seitenumbruch nach jedem Schüler",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 2)))),
				new ReportingEMailDaten(),
				List.of(ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe("Leistungsdatensortierung", "ReportingSchuelerLeistungsdaten", true,
						ReportingSortierungDefinitionFactory.definitionen(
								ReportingSortierungDefinitionFactory.standard("Gemäß Fächersortierung", "ReportingSchuelerLeistungsdaten"),
								ReportingSortierungDefinitionFactory.definition("Gemäß GOSt-Fach-Sortierung", "ReportingSchuelerLeistungsdaten", false,
										List.of("fach.gostSortierung")),
								ReportingSortierungDefinitionFactory.definition("Gemäß Fachkürzeln", "ReportingSchuelerLeistungsdaten", false,
										List.of("fach.kuerzel")))
				)),
				List.of(ReportingReportvorlageUtils.erzeugeFilterDefinitionGruppe("Leistungsdatenfilter", "ReportingSchuelerLeistungsdaten", true, true,
						ReportingFilterVerknuepfung.AND,
						ReportingFilterDefinitionFactory.definitionen(
								ReportingFilterDefinitionFactory.definition("Nur zeugnisrelevante Einträge", "ReportingSchuelerLeistungsdaten",
										ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("aufZeugnis", "true"))),
								ReportingFilterDefinitionFactory.definition(
										"Nur Einträge mit prüfungsordnungsrelevanten Fächern", "ReportingSchuelerLeistungsdaten",
										ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("fach.istPruefungsordnungsRelevant", "true"))))
				), ReportingReportvorlageUtils.erzeugeSchuelerStatusfilterGruppe()), true, true);
	}
}
