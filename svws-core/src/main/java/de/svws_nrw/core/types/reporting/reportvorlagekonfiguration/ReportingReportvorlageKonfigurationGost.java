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
public final class ReportingReportvorlageKonfigurationGost {

	private ReportingReportvorlageKonfigurationGost() {
	}

	/** Erstellt die Reportparamater für die Vorlage GostKlausurplanungVKlausurtermineMitKursen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getGostKlausurplanungVKlausurtermineMitKursen() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 1,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKursklausuren", "mit Kursklausuren",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitNachschreibern", "mit Nachschreibern",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKlausurschreiberNamen", "mit Namen der Klausurschreiber",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
						))),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKlausurplanungVSchuelerMitKlausuren.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getGostKlausurplanungVSchuelerMitKlausuren() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()),
				new ArrayList<>(),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVKursMitKursschuelern.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getGostKursplanungVKursMitKursschuelern() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(
				List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId(), ReportingAusgabeformat.EMAIL.getId()),
				new ArrayList<>(),
				ReportingReportvorlageUtils.erzeugeEmailParameter(
						ReportingEMailEmpfaengerTyp.GOSTKURSPLANUNG_KURSLEHRER,
						false,
						"Kurslisten zur Kursplanung",
						"Im Anhang dieser automatisch generierten E-Mail befinden sich Kurslisten aus der Kursplanung."),
				new ArrayList<>(), new ArrayList<>(), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVKurseMitStatistikwerten.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getGostKursplanungVKurseMitStatistikwerten() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()),
				new ArrayList<>(),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVSchuelerMitKursen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getGostKursplanungVSchuelerMitKursen() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()),
				new ArrayList<>(),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostKursplanungVSchuelerMitSchienenKursen.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getGostKursplanungVSchuelerMitSchienenKursen() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()),
				new ArrayList<>(),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage GostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken.
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getGostLaufbahnplanungAbiturjahrgangVFachwahlstatistiken() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()),
				new ArrayList<>(),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), false, true);
	}
}
