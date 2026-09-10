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
import de.svws_nrw.core.utils.reporting.ReportingSortierungDefinitionFactory;
import jakarta.validation.constraints.NotNull;

// SONARQUBE WARNUNG: Es sollen Konstanten für wiederkehrende Strings definiert werden. Das ist hier bei betroffenen Elementen nicht zielführend.
@SuppressWarnings("java:S1192")
public final class ReportingReportvorlageKonfigurationLehrer {

	private ReportingReportvorlageKonfigurationLehrer() {
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "LehrerVListeKontaktdaten".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getLehrerVListeKontaktdaten() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
				ReportingReportvorlageUtils.erzeugeReportingvorlageParameterGruppe("Inhaltsoptionen",
						"Die folgenden Optionen definieren in Teilen die Inhalte sowie deren Darstellung in der zu erzeugenden Ausgabedatei.", true, 3,
						Arrays.asList(
								ReportingReportvorlageUtils.erzeugeVorlageParameter("ueberschrift", "Überschrift (Standard: Lehrerliste)",
										ReportingReportvorlageParameterTyp.STRING,
										"Lehrerliste", true, ReportingUIKomponentenTyp.INPUT, 3),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("bemerkung", "Bemerkung",
										ReportingReportvorlageParameterTyp.STRING,
										"", true, ReportingUIKomponentenTyp.INPUT, 3),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerAmtsbezeichnung", "mit Amtsbezeichnung",
										ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
										true, ReportingUIKomponentenTyp.CHECKBOX, 3),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitFoto", "mit Foto",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerGeschlecht", "mit Geschlecht",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerGebDat", "mit Geburtsdatum",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerStaat", "mit Staatsangehörigkeit",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerAnschrift", "mit Anschrift",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerTelefon", "mit Telefon",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerTelefonMobil", "mit Mobiltelefon",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerEmailDienstlich", "mit E-Mail (dienstlich)",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 1),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitLehrerEmailPrivat", "mit E-Mail (privat)",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, true, ReportingUIKomponentenTyp.CHECKBOX, 2),
								// Platzhalter für spätere Datenbereiche: solange die Daten im Reporting fehlen, bleiben die Optionen ausgeblendet.
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSpalteLehrerLehraemter", "mit Lehrämtern",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, false, ReportingUIKomponentenTyp.CHECKBOX, 3),
								ReportingReportvorlageUtils.erzeugeVorlageParameter("mitSpalteLehrerBeschaeftigung", "mit Beschäftigung",
										ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, false, ReportingUIKomponentenTyp.CHECKBOX, 3)
						))),
				new ReportingEMailDaten(),
				List.of(ReportingReportvorlageUtils.erzeugeSortierungDefinitionGruppe("Lehrersortierung", "ReportingLehrer", true,
						ReportingSortierungDefinitionFactory.definitionen(
								ReportingSortierungDefinitionFactory.standard("Sortierung nach Name und Vorname (Standard)", "ReportingLehrer"),
								ReportingSortierungDefinitionFactory.definition("Sortierung nach Kürzel", "ReportingLehrer", false,
										List.of("kuerzel", "nachname", "vorname"))))
				),
				List.of(ReportingReportvorlageUtils.erzeugeLehrerPersonaltypfilterGruppe()), false, true);
	}

	/**
	 * Erstellt die Reportparamater für die Vorlage "LehrerVListeSchuelerLeistungsdaten".
	 *
	 * @return Ein ReportingParameter-Objekt mit den entsprechenden Parametern
	 */
	public static @NotNull ReportingParameter getLehrerVListeSchuelerLeistungsdaten() {
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()), List.of(
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
		return ReportingReportvorlageUtils.erzeugeReportingParameter(List.of(ReportingAusgabeformat.HTML.getId(), ReportingAusgabeformat.PDF.getId()),
				new ArrayList<>(),
				new ReportingEMailDaten(),
				new ArrayList<>(), new ArrayList<>(), true, true);
	}
}
