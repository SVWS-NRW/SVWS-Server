package de.svws_nrw.core.types.reporting;

import java.util.List;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import jakarta.validation.constraints.NotNull;

/**
 * Sammelt die Angaben zu einer Report-Vorlage, die das Enum {@link ReportingReportvorlage} bei seiner Erzeugung übernimmt.
 * Die Setter lassen sich verketten. So bleibt jeder Enum-Eintrag lesbar und optionale Angaben dürfen fehlen.
 * Die Setter tragen das Präfix "set", weil TypeScript nach dem Transpilieren kein Feld und keine Methode gleichen Namens erlaubt.
 */
public final class ReportingReportvorlageDefinition {

	/** Die eindeutige Bezeichnung der Report-Vorlage. */
	private final @NotNull String bezeichnung;

	/** Der Titel der Vorlage in der UI. */
	private String uiTitel = null;

	/** Die Beschreibung der Vorlage in der UI. */
	private String uiBeschreibung = null;

	/** Die Bereiche im Client, in denen die Vorlage angeboten wird. */
	private @NotNull List<ReportingReportvorlageUiNutzung> uiNutzungen = List.of();

	/** Der Datenaufbau, den die Vorlage verwendet. */
	private ReportingReportvorlageDatenContext datenContext = null;

	/** Der Pfad zur HTML-Template-Datei, relativ zum Root-Pfad der Vorlagen. */
	private String pfadHtmlTemplate = null;

	/** Der statische Dateiname ohne Dateiendung. */
	private String dateiname = null;

	/** Die Benutzerkompetenzen, von denen der Benutzer mindestens eine benötigt. */
	private List<BenutzerKompetenz> benutzerKompetenzen = null;

	/** Die Schulformen, an denen die Vorlage genutzt werden darf. Eine leere Liste gilt für alle Schulformen. */
	private @NotNull List<Schulform> schulformen = List.of();

	/** Der mindestens erforderliche ServerMode. */
	private @NotNull ServerMode erforderlicherServerMode = ServerMode.STABLE;

	/** Die Parameter der Vorlage mit ihren Parametergruppen. */
	private ReportingParameter reportingParameter = null;


	/**
	 * Erzeugt eine Definition mit der Bezeichnung der Report-Vorlage. Alle weiteren Angaben folgen über die Setter.
	 *
	 * @param bezeichnung   die eindeutige Bezeichnung der Report-Vorlage
	 */
	public ReportingReportvorlageDefinition(final @NotNull String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Setzt den Titel, den die UI zur Vorlage anzeigt, z. B. als Card-Titel.
	 *
	 * @param uiTitel   der Titel
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setUiTitel(final @NotNull String uiTitel) {
		this.uiTitel = uiTitel;
		return this;
	}

	/**
	 * Setzt die Beschreibung, die die UI zur Vorlage anzeigt, z. B. als Card-Untertitel.
	 *
	 * @param uiBeschreibung   die Beschreibung
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setUiBeschreibung(final @NotNull String uiBeschreibung) {
		this.uiBeschreibung = uiBeschreibung;
		return this;
	}

	/**
	 * Setzt die Bereiche im Client, in denen die Vorlage angeboten wird. Ohne Aufruf bindet der Client die Vorlage nur dort ein, wo er sie ausdrücklich nennt.
	 *
	 * @param uiNutzungen   die Bereiche im Client
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setUiNutzungen(final @NotNull List<ReportingReportvorlageUiNutzung> uiNutzungen) {
		this.uiNutzungen = uiNutzungen;
		return this;
	}

	/**
	 * Setzt den Datenaufbau. Er bestimmt, welche Daten der Server für die Vorlage lädt.
	 *
	 * @param datenContext   der Datenaufbau
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setDatenContext(final @NotNull ReportingReportvorlageDatenContext datenContext) {
		this.datenContext = datenContext;
		return this;
	}

	/**
	 * Setzt den Pfad zur HTML-Template-Datei, relativ zum Root-Pfad der Vorlagen.
	 *
	 * @param pfadHtmlTemplate   der Pfad
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setPfadHtmlTemplate(final @NotNull String pfadHtmlTemplate) {
		this.pfadHtmlTemplate = pfadHtmlTemplate;
		return this;
	}

	/**
	 * Setzt den statischen Dateinamen ohne Dateiendung.
	 *
	 * @param dateiname   der Dateiname
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setDateiname(final @NotNull String dateiname) {
		this.dateiname = dateiname;
		return this;
	}

	/**
	 * Setzt die Benutzerkompetenzen. Der Benutzer benötigt mindestens eine davon.
	 *
	 * @param benutzerKompetenzen   die Benutzerkompetenzen
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setBenutzerKompetenzen(final @NotNull List<BenutzerKompetenz> benutzerKompetenzen) {
		this.benutzerKompetenzen = benutzerKompetenzen;
		return this;
	}

	/**
	 * Beschränkt die Vorlage auf die übergebenen Schulformen. Ohne Aufruf gilt die Vorlage für alle Schulformen.
	 *
	 * @param schulformen   die Schulformen
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setSchulformen(final @NotNull List<Schulform> schulformen) {
		this.schulformen = schulformen;
		return this;
	}

	/**
	 * Setzt den mindestens erforderlichen ServerMode. Ohne Aufruf gilt STABLE und die Vorlage ist in allen Modi nutzbar.
	 *
	 * @param erforderlicherServerMode   der ServerMode
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setErforderlicherServerMode(final @NotNull ServerMode erforderlicherServerMode) {
		this.erforderlicherServerMode = erforderlicherServerMode;
		return this;
	}

	/**
	 * Setzt die Parameter der Vorlage mit ihren Parametergruppen.
	 *
	 * @param reportingParameter   die Parameter
	 *
	 * @return diese Definition
	 */
	public @NotNull ReportingReportvorlageDefinition setReportingParameter(final @NotNull ReportingParameter reportingParameter) {
		this.reportingParameter = reportingParameter;
		return this;
	}

	@NotNull String getBezeichnung() {
		return bezeichnung;
	}

	@NotNull String getUiTitel() {
		if (uiTitel == null) {
			throw fehlendeAngabe("uiTitel");
		}
		return uiTitel;
	}

	@NotNull String getUiBeschreibung() {
		if (uiBeschreibung == null) {
			throw fehlendeAngabe("uiBeschreibung");
		}
		return uiBeschreibung;
	}

	@NotNull List<ReportingReportvorlageUiNutzung> getUiNutzungen() {
		return uiNutzungen;
	}

	@NotNull ReportingReportvorlageDatenContext getDatenContext() {
		if (datenContext == null) {
			throw fehlendeAngabe("datenContext");
		}
		return datenContext;
	}

	@NotNull String getPfadHtmlTemplate() {
		if (pfadHtmlTemplate == null) {
			throw fehlendeAngabe("pfadHtmlTemplate");
		}
		return pfadHtmlTemplate;
	}

	@NotNull String getDateiname() {
		if (dateiname == null) {
			throw fehlendeAngabe("dateiname");
		}
		return dateiname;
	}

	@NotNull List<BenutzerKompetenz> getBenutzerKompetenzen() {
		if (benutzerKompetenzen == null) {
			throw fehlendeAngabe("benutzerKompetenzen");
		}
		return benutzerKompetenzen;
	}

	@NotNull List<Schulform> getSchulformen() {
		return schulformen;
	}

	@NotNull ServerMode getErforderlicherServerMode() {
		return erforderlicherServerMode;
	}

	@NotNull ReportingParameter getReportingParameter() {
		if (reportingParameter == null) {
			throw fehlendeAngabe("reportingParameter");
		}
		return reportingParameter;
	}

	/**
	 * Erzeugt den Fehler für eine fehlende Pflichtangabe. Er tritt schon beim Laden des Enums auf und fällt damit in jedem Test auf.
	 *
	 * @param angabe   der Name der fehlenden Angabe
	 *
	 * @return der Fehler mit Vorlage und fehlender Angabe
	 */
	private @NotNull IllegalStateException fehlendeAngabe(final @NotNull String angabe) {
		return new IllegalStateException("Der Report-Vorlage '" + bezeichnung + "' fehlt die Pflichtangabe '" + angabe + "'.");
	}

}
