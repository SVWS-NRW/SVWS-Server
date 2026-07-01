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
public final class ReportingReportvorlageKonfigurationStundenplanung {

	private ReportingReportvorlageKonfigurationStundenplanung() {
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVFachStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getStundenplanungVFachStundenplan() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2,
						List.of(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 1)
						))),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVKlassenStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getStundenplanungVKlassenStundenplan() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(
				List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()),
				List.of(
						ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
								"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true,
								2, Arrays.asList(
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung",
												"Fachkürzel statt Fachbezeichnung",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
								))),
				ReportingReportvorlageUtils.erzeugeEmailParameter(
						ReportingEMailEmpfaengerTyp.KLASSENLEHRER,
						false,
						"",
						""),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVLehrerStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getStundenplanungVLehrerStundenplan() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(
				List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()),
				List.of(
						ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
								"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true,
								2, Arrays.asList(
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung",
												"Fachkürzel statt Fachbezeichnung",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
								))),
				ReportingReportvorlageUtils.erzeugeEmailParameter(
						ReportingEMailEmpfaengerTyp.LEHRER,
						false,
						"",
						""),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVLehrerStundenplanKombiniert".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getStundenplanungVLehrerStundenplanKombiniert() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenaufsichten", "mit Pausenaufsichten",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung", "Fachkürzel statt Fachbezeichnung",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
						))),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVRaumStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getStundenplanungVRaumStundenplan() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2,
						List.of(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 1)
						))),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "StundenplanungVSchuelerStundenplan".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getStundenplanungVSchuelerStundenplan() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(
				List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()),
				List.of(
						ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
								"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true,
								2, Arrays.asList(
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPausenzeiten", "mit Pausenzeiten",
												ReportingReportvorlageParameterTyp.BOOLEAN,
												"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachStattKursbezeichnung", "Fach statt Kursbezeichnung",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFachkuerzelStattFachbezeichnung",
												"Fachkürzel statt Fachbezeichnung",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
										ReportingReportvorlageUtils.erzeugeVorlageParameter("mitExternerSchuleKuerzel", "mit Schulkürzel bei externen Schülern",
													ReportingReportvorlageParameterTyp.BOOLEAN, "" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
											ReportingReportvorlageUtils.erzeugeVorlageParameter("mitIndividuelleKursart", "mit individueller Kursart",
												ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
								))),
				ReportingReportvorlageUtils.erzeugeEmailParameter(
						ReportingEMailEmpfaengerTyp.SCHUELER,
						false,
						"",
						""),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}
}
