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
import jakarta.validation.constraints.NotNull;

// SONARQUBE WARNUNG: Es sollen Konstanten für wiederkehrende Strings definiert werden. Das ist hier bei betroffenen Elementen nicht zielführend.
@SuppressWarnings("java:S1192")
public final class ReportingReportvorlageKonfigurationKurse {

	private ReportingReportvorlageKonfigurationKurse() {
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage KurseVListeSchuelerKontaktdatenerzieher.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getKurseVListeSchuelerKontaktdatenerzieher() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(
				List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), List.of(
						ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
								"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 3,
								Arrays.asList(
										ReportingReportvorlageUtils.erzeugeVorlageParameter("ueberschrift", "Überschrift (Standard: Kursliste <Kürzel>)",
												ReportingReportvorlageParameterTyp.STRING,
												"Kursliste <Kürzel>", true, ReportingUIKomponentenTyp.INPUT, 3),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("bemerkung", "Bemerkung",
												ReportingReportvorlageParameterTyp.STRING,
												"", true, ReportingUIKomponentenTyp.INPUT, 3),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerKlasse", "mit Klasse",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
												true, ReportingUIKomponentenTyp.CHECKBOX, 3),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFoto", "mit Foto",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerAlleVornamen", "mit allen Vornamen",
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
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitExternerSchuleKuerzel", "mit Schulkürzel bei externen Schülern",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerAnschrift", "mit Anschrift",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerTelefonPrivat", "mit Telefon (privat)",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerEmailSchule", "mit E-Mail (Schule)",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSchuelerEmailPrivat", "mit E-Mail (privat)",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
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
						ReportingEMailEmpfaengerTyp.KURSLEHRER,
						false,
						"",
						""),
				new ArrayList<>(), List.of(ReportingReportvorlageUtils.erzeugeSchuelerStatusfilterGruppe()), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage KurseVListeSchuelerFotosNamen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getKurseVListeSchuelerFotosNamen() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(
				List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()), List.of(
						ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
								"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1,
								List.of(
										ReportingReportvorlageUtils.erzeugeVorlageParameter("anzahlBilderProZeile", "Anzahl Bilder pro Spalte",
												ReportingReportvorlageParameterTyp.INTEGER,
												"" + 4, true, ReportingUIKomponentenTyp.NUMBERPICKER, 1)
								))),
				ReportingReportvorlageUtils.erzeugeEmailParameter(
						ReportingEMailEmpfaengerTyp.KURSLEHRER,
						false,
						"",
						""),
				new ArrayList<>(), List.of(ReportingReportvorlageUtils.erzeugeSchuelerStatusfilterGruppe()), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage KurseVListeSchuelerLeistungsdaten.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getKurseVListeSchuelerLeistungsdaten() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitExternerSchuleKuerzel", "mit Schulkürzel bei externen Schülern",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
						))),
				new ReportingEMailDaten(),
				new ArrayList<>(), List.of(ReportingReportvorlageUtils.erzeugeSchuelerStatusfilterGruppe()), true, true);
	}
}
