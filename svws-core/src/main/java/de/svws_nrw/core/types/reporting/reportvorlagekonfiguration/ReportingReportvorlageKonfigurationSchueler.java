package de.svws_nrw.core.types.reporting.reportvorlagekonfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp;
import de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils;
import de.svws_nrw.core.utils.reporting.ReportingSortierungDefinitionFactory;
import jakarta.validation.constraints.NotNull;

// SONARQUBE WARNUNG: Es sollen Konstanten für wiederkehrende Strings definiert werden. Das ist hier bei betroffenen Elementen nicht zielführend.
@SuppressWarnings("java:S1192")
public final class ReportingReportvorlageKonfigurationSchueler {

	private ReportingReportvorlageKonfigurationSchueler() {
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVGostAbiturApoAnlage12A4".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getSchuelerVGostAbiturApoAnlage12A4() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Unterschriftenoptionen", "", true, 1, Arrays.asList(
						ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPersoenlichenUnterschriften",
								"mit persönlichen Unterschriften gemäß Datenbestand",
								ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift",
								"mit Unterschrift 2. Beratungslehrkraft statt Schulträger",
								ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1)
				))),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVGostAbiturApoAnlage12A3".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getSchuelerVGostAbiturApoAnlage12A3() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Unterschriftenoptionen", "", true, 1, Arrays.asList(
						ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPersoenlichenUnterschriften",
								"mit persönlichen Unterschriften gemäß Datenbestand",
								ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("mitZweiterBeratungslehrerUnterschrift",
								"mit Unterschrift 2. Beratungslehrkraft statt Schulträger",
								ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textZAAVorsitzUnterschrift", "Unterschrift ZAA-Vorsitz",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textZAAVorsitzUnterschriftBezeichnung", "Bezeichnung ZAA-Vorsitz",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchulleitungUnterschrift", "Unterschrift Schulleitung",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchulleitungUnterschriftBezeichnung", "Bezeichnung Schulleitung",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchultraegerUnterschrift", "Unterschrift Schulträger",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textSchultraegerUnterschriftBezeichnung", "Bezeichnung Schulträger",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textBeratungslehrerUnterschrift", "Unterschrift Beratungslehrer",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1),
						ReportingReportvorlageUtils.erzeugeVorlageParameter("textBeratungslehrerUnterschriftBezeichnung", "Bezeichnung Beratungslehrer",
								ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 1)
				))),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVGostLaufbahnplanungErgebnisuebersicht".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getSchuelerVGostLaufbahnplanungErgebnisuebersicht() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFehlernKommentaren", "mit Fehlern/Kommentaren",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitHinweisen", "mit Hinweisen", ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 1)
						))),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVGostLaufbahnplanungWahlbogen".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getSchuelerVGostLaufbahnplanungWahlbogen() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(
				List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()),
				List.of(ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1,
						List.of(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("nurBelegteFaecher", "nur belegte Fächer",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
						))),
				ReportingReportvorlageUtils.erzeugeEmailParameter(
						ReportingEMailEmpfaengerTyp.SCHUELER,
						false,
						"Persönlicher Wahlbogen zur Laufbahnplanung in der GOSt",
						"Im Anhang dieser automatisch generierten E-Mail befindet sich dein persönlicher Wahlbogen zur Laufbahnplanung in der gymnasialen Oberstufe."),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVSchulbescheinigung".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getSchuelerVSchulbescheinigung() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 3,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("fuerErzieher", "für Erzieher", ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 3),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchullogo", "mit Schullogo", ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitBildBriefkopf", "mit Bild im Briefkopf",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("keineAnschrift", "ohne Anschrift",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("keinInfoblock", "ohne Infoblock",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("keineUnterschrift", "ohne Unterschrift",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("versandinformationen", "Versandinformationen",
										ReportingReportvorlageParameterTyp.STRING, "", true, ReportingUIKomponentenTyp.INPUT, 3)
						))),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "SchuelerVListeKontaktdatenerzieher".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getSchuelerVListeKontaktdatenerzieher() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 3,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 3),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("nurSchuelerRufname", "nur Rufname",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerGeschlecht", "mit Geschlecht",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerGebDat", "mit Geburtsdatum",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerStaat", "mit Staatsangehörigkeit",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSpalteSchuelerTelefonKontakte", "mit Telefonkontakten",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitErzieher", "mit Erziehern", ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitErzieherAnschrift", "mit Erzieher-Anschrift",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitErzieherEmailPrivat", "mit Erzieher-E-Mail",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
						))),
				new ReportingEMailDaten(),
				List.of(ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe("Schülersortierung", "ReportingSchueler", true,
						ReportingSortierungDefinitionFactory.definitionen(
								ReportingSortierungDefinitionFactory.standard("Standardsortierung der Schüler", "ReportingSchueler"),
								ReportingSortierungDefinitionFactory.definition("Sortierung nach Klasse, Name, Vorname", "ReportingSchueler", false,
										List.of("Klasse, Nachname, Vorname, Vornamen")))
				)),
				new ArrayList<>(), false, true);
	}
}
