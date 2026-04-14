package de.svws_nrw.core.types.reporting.reportvorlagekonfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp;
import de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils;
import jakarta.validation.constraints.NotNull;

// SONARQUBE WARNUNG: Es sollen Konstanten für wiederkehrende Strings definiert werden. Das ist hier bei betroffenen Elementen nicht zielführend.
@SuppressWarnings("java:S1192")
public final class ReportingReportvorlageKonfigurationLehrer {

	private ReportingReportvorlageKonfigurationLehrer() {
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "LehrerVListeSchuelerLeistungsdaten".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getLehrerVListeSchuelerLeistungsdaten() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 2,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKlassenunterricht", "mit Klassenunterricht",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitKursunterricht", "mit Kursunterricht",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + true, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitPunktenStattNoten", "Punkte statt Noten ausgeben",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitBemerkungen", "mit fachbezogenen Bemerkungen",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1)
						))),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "LehrerVStammdatenliste".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getLehrerVStammdatenliste() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.PDF.getId()), new ArrayList<>(),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}
}
