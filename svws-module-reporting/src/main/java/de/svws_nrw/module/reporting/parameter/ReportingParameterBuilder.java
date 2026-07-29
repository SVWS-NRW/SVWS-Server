package de.svws_nrw.module.reporting.parameter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlage;
import de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlageGruppe;
import de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlagen;
import de.svws_nrw.core.data.reporting.ReportingEinstellungenBenutzerVorlagenParameterWert;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageKonfigurationBenutzerweit;
import de.svws_nrw.core.utils.reporting.ReportingReportvorlageUtils;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DataClientConfig;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;

/**
 * Diese Klasse baut die endgültigen Vorlage-Parameter, Sortier- und Filtergruppen für die Report-Generierung auf. Sie bündelt die gesamte Aufbereitung, die
 * zuvor in der {@code ReportingFactory} verstreut war: das Zusammenfügen der Parameter aus dem Vorlagen-Katalog und dem benutzerweiten Katalog mit den
 * gespeicherten und übermittelten Werten, die Prüfung der Werte auf Typkonformität sowie das Zurücksetzen von Einstellungen, die der aktuelle ServerMode oder
 * die Kompetenzen des angemeldeten Benutzers nicht zulassen.
 * Wert-Vorrang je Parameter: Katalog-Default wird vom gespeicherten Config-Wert überlagert, dieser wiederum vom übermittelten Wert; überlagernde Werte
 * werden nur übernommen, wenn sie für den Parameter-Typ konform sind. Das gilt sowohl für die vorlagenspezifischen Parameter (Config-Ebene: die gespeicherten
 * Vorlagen-Einstellungen des Benutzers) als auch für die benutzerweiten Parameter (Config-Ebene: die gespeicherten benutzerweiten Einstellungen).
 */
public class ReportingParameterBuilder {

	/** Der Name des Vorlage-Parameters, der die Aufteilung der Ausgabe in Einzeldateien steuert. */
	private static final String PARAMETER_EINZELAUSGABE_DATEN = "einzelausgabeDaten";

	/** Logger, über den nicht typkonforme (und damit verworfene) Werte protokolliert werden. */
	private final Logger logger;

	/** Der Modus, in dem der Server betrieben wird. Wird für die ServerMode-Prüfung der Einstellungen benötigt. */
	private final ServerMode serverMode;

	/** Der aktuell angemeldete Benutzer. Wird für die Kompetenzprüfung der Einstellungen benötigt. */
	private final Benutzer benutzer;

	/**
	 * Erzeugt einen neuen Parameter-Builder.
	 *
	 * @param logger     der Logger, über den verworfene Werte protokolliert werden
	 * @param serverMode der Modus, in dem der Server betrieben wird
	 * @param benutzer   der aktuell angemeldete Benutzer
	 */
	public ReportingParameterBuilder(final Logger logger, final ServerMode serverMode, final Benutzer benutzer) {
		this.logger = logger;
		this.serverMode = serverMode;
		this.benutzer = benutzer;
	}

	/**
	 * Baut die endgültigen Parametergruppen sowie die bereinigten Sortier- und Filtergruppen im übergebenen Reporting-Parameter-Objekt auf. Zuerst werden die
	 * gespeicherten Vorlagen-Einstellungen des Benutzers für die übergebene Reportvorlage sowie die gespeicherten benutzerweiten Werte aus der Client-Konfiguration
	 * gelesen und die Parameter beider Kataloge damit sowie mit den übermittelten Werten kombiniert, anschließend werden alle Einstellungen zurückgesetzt, die
	 * im aktuellen ServerMode oder mit den Kompetenzen des Benutzers nicht erlaubt sind.
	 *
	 * @param reportingParameter das aufzubereitende Reporting-Parameter-Objekt (wird verändert)
	 * @param reportvorlage      die Reportvorlage mit der SOLL-Struktur und den Anforderungen
	 * @param conn               die Datenbankverbindung mit dem angemeldeten Benutzer (für das Lesen der gespeicherten Config-Werte)
	 */
	public void baue(final ReportingParameter reportingParameter, final ReportingReportvorlage reportvorlage, final DBEntityManager conn) {
		final ReportingEinstellungenBenutzerVorlage vorlagenEinstellungen = leseGespeicherteVorlagenEinstellungen(conn, reportvorlage);
		final Map<String, String> gespeicherteVorlagenwerte =
				parameterWerteAlsMap((vorlagenEinstellungen == null) ? null : vorlagenEinstellungen.parameterWerte);
		baueParameterGruppen(reportingParameter, reportvorlage, gespeicherteVorlagenwerte, leseGespeicherteBenutzerweiteWerte(conn));
		wendeGespeicherteEinstellungenAuswahlAn(reportingParameter, reportvorlage, vorlagenEinstellungen);
		setzeUnerlaubteEinstellungenZurueck(reportingParameter, reportvorlage);
		// Zuletzt, damit die ausgabeformatabhängigen Zwangswerte von keiner anderen Ebene mehr überschrieben werden können.
		erzwingeAusgabeformatabhaengigeParameter(reportingParameter, reportvorlage);
	}

	/**
	 * Setzt die Parameter, deren Wert das Ausgabeformat zwingend vorgibt, auf dem <b>fertig kombinierten</b> Parametersatz — also nachdem
	 * Katalog-Defaults, gespeicherte und übermittelte Werte zusammengeführt und unerlaubte Einstellungen zurückgesetzt wurden. Nur so gilt die
	 * Zusage unabhängig davon, ob der Client den Parameter überhaupt mitgesendet hat: Fehlt er, wirkte zuvor der Katalog-Default oder ein
	 * gespeicherter Benutzerwert.
	 *
	 * <p>Derzeit betrifft das ausschließlich {@code einzelausgabeDaten}: Bei der HTML-Ausgabe (Browser, keine Einzeldateien) gilt zwingend
	 * {@code false}, beim E-Mail-Versand (je Datensatz eine Datei an einen eigenen Empfänger) zwingend {@code true}. Bei der PDF-Ausgabe bleibt es
	 * beim kombinierten Wert. Weitere ausgabeformatabhängige Festlegungen gehören ebenfalls hierher und an keine andere Stelle.</p>
	 *
	 * @param reportingParameter das Reporting-Parameter-Objekt mit dem fertig kombinierten Parametersatz (wird verändert)
	 * @param reportvorlage      die Reportvorlage, über die der Parameterwert gesetzt wird
	 */
	static void erzwingeAusgabeformatabhaengigeParameter(final ReportingParameter reportingParameter,
			final ReportingReportvorlage reportvorlage) {
		if (reportingParameter.ausgabeformat == ReportingAusgabeformat.HTML.getId()) {
			reportvorlage.setReportingParameterVorlageparameter(reportingParameter, PARAMETER_EINZELAUSGABE_DATEN, "false");
		} else if (reportingParameter.ausgabeformat == ReportingAusgabeformat.EMAIL.getId()) {
			reportvorlage.setReportingParameterVorlageparameter(reportingParameter, PARAMETER_EINZELAUSGABE_DATEN, "true");
		}
	}

	/**
	 * Liest die gespeicherten benutzerweiten Reporting-Einstellungen des an der Verbindung angemeldeten Benutzers aus der Client-Konfiguration und gibt deren
	 * Parameter-Werte als flache Namens-Map zurück. Ein fehlender, leerer oder defekter Konfigurationseintrag führt zu einer leeren Map und einem Log-Hinweis,
	 * aber nie zu einem Fehler nach außen.
	 *
	 * @param conn die Datenbankverbindung mit dem angemeldeten Benutzer
	 *
	 * @return die Map der gespeicherten Parameter-Werte (Name → Wert), ggf. leer
	 */
	private HashMap<String, String> leseGespeicherteBenutzerweiteWerte(final DBEntityManager conn) {
		final ReportingEinstellungenBenutzerVorlagen einstellungen = leseKonfiguration(conn,
				ReportingReportvorlageKonfigurationBenutzerweit.CONFIG_KEY_BENUTZER_VORLAGEN, ReportingEinstellungenBenutzerVorlagen.class,
				"Die gespeicherten benutzerweiten Reporting-Einstellungen konnten nicht gelesen werden. Es werden die Katalog-Defaults verwendet.");
		return parameterWerteAlsMap((einstellungen == null) ? null : einstellungen.parameterWerte);
	}

	/**
	 * Liest die gespeicherten Vorlagen-Einstellungen des an der Verbindung angemeldeten Benutzers für die übergebene Reportvorlage aus der Client-Konfiguration
	 * und gibt sie als {@link ReportingEinstellungenBenutzerVorlage} zurück. Der Config-Key ist je Vorlage individuell (Schema
	 * {@code reporting.einstellungen.benutzer.vorlage.<bezeichnung>}, siehe {@link ReportingReportvorlage#getConfigKeyBenutzerVorlage()}). Ein fehlender,
	 * leerer oder defekter Konfigurationseintrag führt zu {@code null} und einem Log-Hinweis, aber nie zu einem Fehler nach außen.
	 *
	 * @param conn          die Datenbankverbindung mit dem angemeldeten Benutzer
	 * @param reportvorlage die Reportvorlage, für die die Einstellungen gelesen werden
	 *
	 * @return die gespeicherten Vorlagen-Einstellungen oder {@code null}, wenn keine vorhanden oder sie nicht lesbar sind
	 */
	private ReportingEinstellungenBenutzerVorlage leseGespeicherteVorlagenEinstellungen(final DBEntityManager conn,
			final ReportingReportvorlage reportvorlage) {
		return leseKonfiguration(conn, reportvorlage.getConfigKeyBenutzerVorlage(), ReportingEinstellungenBenutzerVorlage.class,
				"Die gespeicherten Vorlagen-Einstellungen der Reportvorlage '" + reportvorlage.getBezeichnung()
						+ "' konnten nicht gelesen werden. Es werden die Katalog-Defaults verwendet.");
	}

	/**
	 * Liest einen Reporting-Konfigurationseintrag der Client-Konfiguration unter dem übergebenen Schlüssel für den angemeldeten Benutzer und deserialisiert ihn
	 * in die angegebene Klasse. Ein fehlender, leerer oder defekter Eintrag führt zu {@code null} und einem Log-Hinweis, aber nie zu einem Fehler nach außen.
	 *
	 * @param <T>              der Zieltyp des Konfigurationseintrags
	 * @param conn             die Datenbankverbindung mit dem angemeldeten Benutzer
	 * @param configKey        der Schlüssel des zu lesenden Konfigurationseintrags
	 * @param clazz            die Zielklasse, in die der JSON-Eintrag deserialisiert wird
	 * @param hinweisBeiFehler der Log-Hinweis, der bei einem defekten oder nicht lesbaren Eintrag protokolliert wird
	 *
	 * @return der deserialisierte Konfigurationseintrag oder {@code null}
	 */
	private <T> T leseKonfiguration(final DBEntityManager conn, final String configKey, final Class<T> clazz, final String hinweisBeiFehler) {
		try {
			final String json = DataClientConfig.getUserKeyOrGlobal(conn, ReportingReportvorlageKonfigurationBenutzerweit.CONFIG_APP_NAME, configKey);
			if ((json == null) || json.isBlank()) {
				return null;
			}
			return JSONMapper.toObject(json.getBytes(StandardCharsets.UTF_8), clazz);
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.INFO, 4, "HINWEIS: " + hinweisBeiFehler + " (" + e.getMessage() + ")");
			return null;
		}
	}

	/**
	 * Wandelt eine Liste gespeicherter Parameter-Werte in eine flache Namens-Map (Name → Wert) um, die als Config-Ebene in die Kombinier-Strecke der
	 * Parametergruppen einfließt. Eine fehlende Liste sowie Einträge ohne Namen oder Wert führen zu einer leeren bzw. um diese Einträge reduzierten Map.
	 *
	 * @param parameterWerte die gespeicherten Parameter-Werte oder {@code null}
	 *
	 * @return die Map der gespeicherten Parameter-Werte (Name → Wert), ggf. leer
	 */
	private static HashMap<String, String> parameterWerteAlsMap(final List<ReportingEinstellungenBenutzerVorlagenParameterWert> parameterWerte) {
		final HashMap<String, String> gespeicherteWerte = new HashMap<>();
		if (parameterWerte == null) {
			return gespeicherteWerte;
		}
		for (final ReportingEinstellungenBenutzerVorlagenParameterWert parameterWert : parameterWerte) {
			if ((parameterWert != null) && (parameterWert.name != null) && (parameterWert.wert != null)) {
				gespeicherteWerte.put(parameterWert.name, parameterWert.wert);
			}
		}
		return gespeicherteWerte;
	}

	/**
	 * Baut die Parametergruppen aus dem Vorlagen-Katalog und dem benutzerweiten Katalog auf und schreibt sie in den Reporting-Parameter. Beide Kataloge laufen
	 * über dieselbe Kombinier-Strecke: der Vorlagen-Katalog mit den gespeicherten Vorlagen-Einstellungen des Benutzers als Config-Ebene, der benutzerweite
	 * Katalog mit den gespeicherten benutzerweiten Werten als Config-Ebene. Die benutzerweiten Gruppen werden unsichtbar gehalten, da benutzerweite Parameter
	 * nicht als Report-Parameter erscheinen sollen. Kommt ein Name in beiden Katalogen vor, gewinnt die zuerst verarbeitete vorlagenspezifische Definition.
	 *
	 * @param reportingParameter          das Reporting-Parameter-Objekt mit den übergebenen Parametergruppen (wird verändert)
	 * @param reportvorlage               die Reportvorlage mit der SOLL-Struktur
	 * @param gespeicherteVorlagenwerte   die gespeicherten Vorlagen-Einstellungswerte je Parametername (ggf. leer, wenn keine Einstellungen gespeichert sind)
	 * @param gespeicherteBenutzerwerte   die gespeicherten benutzerweiten Werte je Parametername
	 */
	private void baueParameterGruppen(final ReportingParameter reportingParameter, final ReportingReportvorlage reportvorlage,
			final Map<String, String> gespeicherteVorlagenwerte, final Map<String, String> gespeicherteBenutzerwerte) {
		final Map<String, String> uebermittelteWerte = sammleUebermittelteWerte(reportingParameter);

		final HashSet<String> vergebeneNamen = new HashSet<>();
		final List<ReportingReportvorlageParameterGruppe> kombinierteGruppen = new ArrayList<>();
		kombinierteGruppen.addAll(kombiniereParameterGruppen(reportvorlage.getReportingParameter().reportvorlageParameterGruppen,
				gespeicherteVorlagenwerte, uebermittelteWerte, vergebeneNamen, null));
		kombinierteGruppen.addAll(kombiniereParameterGruppen(ReportingReportvorlageKonfigurationBenutzerweit.getBenutzerweiteParameterGruppen(),
				gespeicherteBenutzerwerte, uebermittelteWerte, vergebeneNamen, false));

		reportingParameter.reportvorlageParameterGruppen = kombinierteGruppen;
	}

	/**
	 * Sammelt die übergebenen Parameter-Werte rein namensbasiert über alle übergebenen Gruppen. Ausgabeformatabhängige Zwangswerte werden hier
	 * <b>nicht</b> angewendet — sie greifen erst auf dem fertig kombinierten Parametersatz, siehe
	 * {@link #erzwingeAusgabeformatabhaengigeParameter(ReportingParameter, ReportingReportvorlage)}.
	 *
	 * @param reportingParameter das Reporting-Parameter-Objekt mit den übergebenen Parametergruppen
	 *
	 * @return die Map der übergebenen Werte (Name → Wert)
	 */
	public static Map<String, String> sammleUebermittelteWerte(final ReportingParameter reportingParameter) {
		final Map<String, String> werte = new HashMap<>();
		if (reportingParameter.reportvorlageParameterGruppen == null) {
			return werte;
		}
		for (final ReportingReportvorlageParameterGruppe gruppe : reportingParameter.reportvorlageParameterGruppen) {
			sammleUebermittelteWerteDerGruppe(gruppe, werte);
		}
		return werte;
	}

	/**
	 * Sammelt die übergebenen Werte der Parameter einer einzelnen Gruppe namensbasiert in die Zielmap. Ungültige Parameter (null, ohne Namen oder ohne Wert)
	 * werden übersprungen.
	 *
	 * @param gruppe die zu verarbeitende Parametergruppe
	 * @param werte  die Zielmap, in die die Werte (Name → Wert) eingetragen werden
	 */
	private static void sammleUebermittelteWerteDerGruppe(final ReportingReportvorlageParameterGruppe gruppe, final Map<String, String> werte) {
		if ((gruppe == null) || (gruppe.reportvorlageParameter == null)) {
			return;
		}
		for (final ReportingReportvorlageParameter p : gruppe.reportvorlageParameter) {
			if ((p == null) || (p.name == null) || p.name.isBlank() || (p.wert == null)) {
				continue;
			}
			werte.put(p.name, p.wert);
		}
	}

	/**
	 * Sammelt die Standardwerte (Katalog-Defaults) der Parameter einer SOLL-Struktur als Namens-Map. Bereits vorhandene Namen werden nicht überschrieben,
	 * sodass bei Namensüberschneidung zwischen den Katalogen der zuerst eingetragene Standardwert erhalten bleibt.
	 *
	 * @param sollGruppen die SOLL-Struktur der Parametergruppen
	 * @param ziel        die Map, in die die Standardwerte (Name → Wert) eingetragen werden
	 */
	public static void sammleStandardWerte(final List<ReportingReportvorlageParameterGruppe> sollGruppen, final Map<String, String> ziel) {
		for (final ReportingReportvorlageParameterGruppe gruppe : sollGruppen) {
			if ((gruppe == null) || (gruppe.reportvorlageParameter == null)) {
				continue;
			}
			for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
				if ((parameter != null) && (parameter.name != null)) {
					ziel.putIfAbsent(parameter.name, parameter.wert);
				}
			}
		}
	}

	/**
	 * Kombiniert eine SOLL-Struktur von Parametergruppen mit einer Config-Ebene und den übermittelten Werten zu neuen Parametergruppen. Je Parameter gilt die
	 * Vorrang-Regel: Katalog-Default (aus der SOLL-Struktur) wird vom gespeicherten Config-Wert überlagert, dieser wiederum vom übermittelten Wert. Werte aus
	 * Config- und Übermittlungs-Ebene werden nur übernommen, wenn sie für den Parameter-Typ konform sind; andernfalls wird der Wert der jeweils darunter
	 * liegenden Ebene beibehalten. Parameter, deren Name bereits vergeben wurde, werden übersprungen (Schutz vor Dopplungen über beide Kataloge hinweg).
	 *
	 * @param sollGruppen                 die SOLL-Struktur der Parametergruppen (Katalog mit Default-Werten)
	 * @param configWerte                 die gespeicherten Config-Werte je Parametername (ggf. leer, wenn kein Wert gespeichert ist)
	 * @param uebermittelteWerte          die übermittelten Werte je Parametername
	 * @param vergebeneNamen              die bereits vergebenen Parameternamen; wird um die verarbeiteten Namen ergänzt
	 * @param sichtbarkeitUeberschreibung erzwingt die Sichtbarkeit der Ergebnisgruppen (true/false); bei null wird die Sichtbarkeit der SOLL-Gruppe übernommen
	 *
	 * @return die kombinierten Parametergruppen
	 */
	public List<ReportingReportvorlageParameterGruppe> kombiniereParameterGruppen(final List<ReportingReportvorlageParameterGruppe> sollGruppen,
			final Map<String, String> configWerte, final Map<String, String> uebermittelteWerte, final Set<String> vergebeneNamen,
			final Boolean sichtbarkeitUeberschreibung) {
		final List<ReportingReportvorlageParameterGruppe> kombinierteGruppen = new ArrayList<>();
		for (final ReportingReportvorlageParameterGruppe sollGruppe : sollGruppen.stream().filter(Objects::nonNull).toList()) {
			final ReportingReportvorlageParameterGruppe kombinierteGruppe = new ReportingReportvorlageParameterGruppe();
			kombinierteGruppe.name = sollGruppe.name;
			kombinierteGruppe.beschreibung = sollGruppe.beschreibung;
			kombinierteGruppe.uiIstSichtbar = (sichtbarkeitUeberschreibung != null) ? sichtbarkeitUeberschreibung : sollGruppe.uiIstSichtbar;
			kombinierteGruppe.uiAnzahlSpalten = sollGruppe.uiAnzahlSpalten;
			kombinierteGruppe.uiErforderlicherServerMode = sollGruppe.uiErforderlicherServerMode;
			kombinierteGruppe.uiErforderlicheKompetenzen = new ArrayList<>(sollGruppe.uiErforderlicheKompetenzen);

			final List<ReportingReportvorlageParameter> kombinierteParameter = new ArrayList<>();
			if (sollGruppe.reportvorlageParameter != null) {
				for (final ReportingReportvorlageParameter sollParameter : sollGruppe.reportvorlageParameter) {
					if ((sollParameter == null) || (sollParameter.name == null) || vergebeneNamen.contains(sollParameter.name)) {
						continue;
					}
					kombinierteParameter
							.add(kombiniereParameter(sollParameter, configWerte.get(sollParameter.name), uebermittelteWerte.get(sollParameter.name)));
					vergebeneNamen.add(sollParameter.name);
				}
			}
			kombinierteGruppe.reportvorlageParameter = kombinierteParameter;
			kombinierteGruppen.add(kombinierteGruppe);
		}
		return kombinierteGruppen;
	}

	/**
	 * Erzeugt aus einem SOLL-Parameter einen kombinierten Parameter, indem der Katalog-Default nacheinander mit dem Config-Wert und dem übermittelten Wert
	 * überlagert wird. Ein überlagernder Wert wird nur übernommen, wenn er nicht null und für den Parameter-Typ konform ist.
	 *
	 * @param sollParameter      der SOLL-Parameter mit den Metadaten und dem Katalog-Default
	 * @param configWert         der gespeicherte Config-Wert oder null
	 * @param uebermittelterWert der übermittelte Wert oder null
	 *
	 * @return der kombinierte Parameter
	 */
	private ReportingReportvorlageParameter kombiniereParameter(final ReportingReportvorlageParameter sollParameter, final String configWert,
			final String uebermittelterWert) {
		final ReportingReportvorlageParameter kombinierterParameter = new ReportingReportvorlageParameter();
		kombinierterParameter.name = sollParameter.name;
		kombinierterParameter.bezeichnung = sollParameter.bezeichnung;
		kombinierterParameter.typ = sollParameter.typ;
		kombinierterParameter.wert = sollParameter.wert;
		kombinierterParameter.uiIstSichtbar = sollParameter.uiIstSichtbar;
		kombinierterParameter.uiKomponentenTyp = sollParameter.uiKomponentenTyp;
		kombinierterParameter.uiAnzahlSpalten = sollParameter.uiAnzahlSpalten;
		kombinierterParameter.uiErforderlicherServerMode = sollParameter.uiErforderlicherServerMode;
		kombinierterParameter.uiErforderlicheKompetenzen = new ArrayList<>(sollParameter.uiErforderlicheKompetenzen);

		kombinierterParameter.wert = ueberlagereWert(kombinierterParameter, configWert);
		kombinierterParameter.wert = ueberlagereWert(kombinierterParameter, uebermittelterWert);
		return kombinierterParameter;
	}

	/**
	 * Gibt den überlagernden Wert zurück, wenn dieser nicht null und für den Typ des Parameters konform ist, andernfalls den bisherigen Wert des Parameters.
	 * Ein nicht typkonformer Wert wird verworfen und protokolliert, sodass der darunter liegende Wert (Config- bzw. Katalog-Ebene) erhalten bleibt.
	 *
	 * @param parameter     der Parameter mit dem bisherigen Wert und dem Typ
	 * @param ueberlagerung der potentiell überlagernde Wert oder null
	 *
	 * @return der zu verwendende Wert
	 */
	private String ueberlagereWert(final ReportingReportvorlageParameter parameter, final String ueberlagerung) {
		if (ueberlagerung == null) {
			return parameter.wert;
		}
		if (!ReportingVorlageParameterTypisiert.istWertTypkonform(parameter.typ, ueberlagerung)) {
			this.logger.logLn(LogLevel.INFO, 4, "HINWEIS: Der Wert '" + ueberlagerung + "' des Parameters '" + parameter.name
					+ "' ist für dessen Typ ungültig und wird verworfen.");
			return parameter.wert;
		}
		return ueberlagerung;
	}

	/**
	 * Setzt die Werte von Vorlage-Parametern sowie die Auswahl von Sortier- und Filtergruppen auf ihre Standardwerte zurück, wenn der aktuelle ServerMode oder
	 * die Kompetenzen des angemeldeten Benutzers die übergebenen Einstellungen nicht zulassen. So kann eine im Client ausgeblendete (und damit nicht
	 * einstellbare) Einstellung nicht über einen manipulierten Request gesetzt werden.
	 *
	 * @param reportingParameter das Reporting-Parameter-Objekt mit den aufbereiteten Gruppen (wird verändert)
	 * @param reportvorlage      die Reportvorlage mit den SOLL-Definitionen (Standardwerte und Anforderungen)
	 */
	private void setzeUnerlaubteEinstellungenZurueck(final ReportingParameter reportingParameter, final ReportingReportvorlage reportvorlage) {
		// Standardwerte beider Kataloge (vorlagenspezifisch und benutzerweit) als Fallback für nicht erlaubte Parameter aufbauen. Bei Namensüberschneidung
		// gewinnt der zuerst eingetragene vorlagenspezifische Standardwert, konsistent zur Reihenfolge beim Aufbau der Parametergruppen.
		final HashMap<String, String> standardWerte = new HashMap<>();
		sammleStandardWerte(reportvorlage.getReportingParameter().reportvorlageParameterGruppen, standardWerte);
		sammleStandardWerte(ReportingReportvorlageKonfigurationBenutzerweit.getBenutzerweiteParameterGruppen(), standardWerte);

		for (final ReportingReportvorlageParameterGruppe gruppe : reportingParameter.reportvorlageParameterGruppen) {
			setzeUnerlaubteParameterZurueck(standardWerte, gruppe);
		}

		// Sortier- und Filtergruppen: Die Anforderungen werden aus den SOLL-Definitionen gelesen, damit manipulierte Anforderungswerte keine Wirkung haben.
		final ReportingParameter sollParameter = reportvorlage.getReportingParameter();
		setzeUnerlaubteSortierungZurueck(reportingParameter, sollParameter);
		setzeUnerlaubteFilterungZurueck(reportingParameter, sollParameter);
	}

	/**
	 * Setzt die Werte der Parameter einer Parametergruppe auf ihren Standardwert zurück, wenn die Gruppe oder der jeweilige Parameter im aktuellen ServerMode
	 * oder mit den Kompetenzen des angemeldeten Benutzers nicht erlaubt ist.
	 *
	 * @param standardWerte die Standardwerte beider Kataloge (Name → Wert)
	 * @param gruppe        die zu prüfende Parametergruppe
	 */
	void setzeUnerlaubteParameterZurueck(final HashMap<String, String> standardWerte, final ReportingReportvorlageParameterGruppe gruppe) {
		if ((gruppe == null) || (gruppe.reportvorlageParameter == null)) {
			return;
		}
		final boolean gruppeErlaubt = istEinstellungErlaubt(gruppe.uiErforderlicherServerMode, gruppe.uiErforderlicheKompetenzen);
		for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
			if ((parameter == null) || (parameter.name == null)
					|| (gruppeErlaubt && istEinstellungErlaubt(parameter.uiErforderlicherServerMode, parameter.uiErforderlicheKompetenzen))) {
				continue;
			}
			final String standard = standardWerte.get(parameter.name);
			if (standard != null) {
				parameter.wert = standard;
			}
		}
	}

	/**
	 * Setzt die übergebene Auswahl einer Sortiergruppe auf die SOLL-Auswahl der Vorlage zurück, wenn die Gruppe im aktuellen ServerMode oder mit den Kompetenzen
	 * des Benutzers nicht erlaubt ist.
	 *
	 * @param reportingParameter das Reporting-Parameter-Objekt mit den übergebenen Sortiergruppen (wird verändert)
	 * @param sollParameter      die SOLL-Definition der Reportvorlage mit den Anforderungen der Sortiergruppen
	 */
	void setzeUnerlaubteSortierungZurueck(final ReportingParameter reportingParameter, final ReportingParameter sollParameter) {
		final HashMap<String, ReportingSortierungDefinitionGruppe> sollGruppen = new HashMap<>();
		for (final ReportingSortierungDefinitionGruppe sollGruppe : sollParameter.sortierungDefinitionenGruppen) {
			if ((sollGruppe != null) && (sollGruppe.bezeichnung != null)) {
				sollGruppen.put(sollGruppe.bezeichnung, sollGruppe);
			}
		}
		for (final ReportingSortierungDefinitionGruppe gruppe : reportingParameter.sortierungDefinitionenGruppen) {
			if ((gruppe == null) || (gruppe.bezeichnung == null)) {
				continue;
			}
			final ReportingSortierungDefinitionGruppe sollGruppe = sollGruppen.get(gruppe.bezeichnung);
			if ((sollGruppe != null) && !istEinstellungErlaubt(sollGruppe.uiErforderlicherServerMode, sollGruppe.uiErforderlicheKompetenzen)) {
				gruppe.sortierungDefinitionen = new ArrayList<>(sollGruppe.sortierungDefinitionen);
			}
		}
	}

	/**
	 * Setzt die übergebene Auswahl einer Filtergruppe auf die SOLL-Auswahl der Vorlage zurück, wenn die Gruppe im aktuellen ServerMode oder mit den Kompetenzen
	 * des Benutzers nicht erlaubt ist.
	 *
	 * @param reportingParameter das Reporting-Parameter-Objekt mit den übergebenen Filtergruppen (wird verändert)
	 * @param sollParameter      die SOLL-Definition der Reportvorlage mit den Anforderungen der Filtergruppen
	 */
	void setzeUnerlaubteFilterungZurueck(final ReportingParameter reportingParameter, final ReportingParameter sollParameter) {
		final HashMap<String, ReportingFilterDefinitionGruppe> sollGruppen = new HashMap<>();
		for (final ReportingFilterDefinitionGruppe sollGruppe : sollParameter.filterDefinitionenGruppen) {
			if ((sollGruppe != null) && (sollGruppe.bezeichnung != null)) {
				sollGruppen.put(sollGruppe.bezeichnung, sollGruppe);
			}
		}
		for (final ReportingFilterDefinitionGruppe gruppe : reportingParameter.filterDefinitionenGruppen) {
			if ((gruppe == null) || (gruppe.bezeichnung == null)) {
				continue;
			}
			final ReportingFilterDefinitionGruppe sollGruppe = sollGruppen.get(gruppe.bezeichnung);
			if ((sollGruppe != null) && !istEinstellungErlaubt(sollGruppe.uiErforderlicherServerMode, sollGruppe.uiErforderlicheKompetenzen)) {
				gruppe.filterDefinitionen = new ArrayList<>(sollGruppe.filterDefinitionen);
			}
		}
	}

	/**
	 * Wendet die in den Vorlagen-Einstellungen gespeicherten Sortier- und Filterauswahlen auf die übergebenen Sortier- und Filtergruppen an. Als Options-Katalog
	 * für den Bezeichnungs-Abgleich dient die SOLL-Definition der Reportvorlage, sodass nur noch im Katalog vorhandene Definitionen übernommen werden (unbekannte
	 * Bezeichnungen werden ignoriert) und manipulierte Options-Listen keine Wirkung haben. Die Anwendung geschieht vor {@link #setzeUnerlaubteEinstellungenZurueck},
	 * damit der ServerMode-/Kompetenz-Reset das letzte Wort behält.
	 *
	 * @param reportingParameter    das Reporting-Parameter-Objekt mit den übergebenen Sortier- und Filtergruppen (wird verändert)
	 * @param reportvorlage         die Reportvorlage mit der SOLL-Definition der Options-Kataloge
	 * @param vorlagenEinstellungen die gespeicherten Vorlagen-Einstellungen oder {@code null}
	 */
	private void wendeGespeicherteEinstellungenAuswahlAn(final ReportingParameter reportingParameter, final ReportingReportvorlage reportvorlage,
			final ReportingEinstellungenBenutzerVorlage vorlagenEinstellungen) {
		if (vorlagenEinstellungen == null) {
			return;
		}
		final ReportingParameter sollParameter = reportvorlage.getReportingParameter();
		wendeGespeicherteSortierungAn(reportingParameter.sortierungDefinitionenGruppen, sollParameter.sortierungDefinitionenGruppen,
				vorlagenEinstellungen.sortierungsauswahlen);
		wendeGespeicherteFilterungAn(reportingParameter.filterDefinitionenGruppen, sollParameter.filterDefinitionenGruppen,
				vorlagenEinstellungen.filterungsauswahlen);
	}

	/**
	 * Setzt die gespeicherte Sortierauswahl je Gruppe, sofern die übergebene Gruppe noch keine explizit übermittelte Auswahl enthält („explizit gewinnt"). Die
	 * gespeicherten Bezeichnungen werden gegen die SOLL-Optionen der gleichnamigen Gruppe aufgelöst.
	 *
	 * @param zielGruppen die übergebenen Sortiergruppen (werden verändert)
	 * @param sollGruppen die SOLL-Sortiergruppen mit den Options-Katalogen
	 * @param auswahlen   die gespeicherten Sortierauswahlen je Gruppe (ggf. leer)
	 */
	static void wendeGespeicherteSortierungAn(final List<ReportingSortierungDefinitionGruppe> zielGruppen,
			final List<ReportingSortierungDefinitionGruppe> sollGruppen, final List<ReportingEinstellungenBenutzerVorlageGruppe> auswahlen) {
		final Map<String, List<String>> gespeicherteAuswahlen = indexiereAuswahlen(auswahlen);
		if (gespeicherteAuswahlen.isEmpty()) {
			return;
		}
		final Map<String, ReportingSortierungDefinitionGruppe> sollProBezeichnung = new HashMap<>();
		for (final ReportingSortierungDefinitionGruppe sollGruppe : sollGruppen) {
			if ((sollGruppe != null) && (sollGruppe.bezeichnung != null)) {
				sollProBezeichnung.put(sollGruppe.bezeichnung, sollGruppe);
			}
		}
		for (final ReportingSortierungDefinitionGruppe gruppe : zielGruppen) {
			if ((gruppe == null) || (gruppe.bezeichnung == null) || !gruppe.sortierungDefinitionen.isEmpty()) {
				continue;
			}
			final List<String> bezeichnungen = gespeicherteAuswahlen.get(gruppe.bezeichnung);
			final ReportingSortierungDefinitionGruppe sollGruppe = sollProBezeichnung.get(gruppe.bezeichnung);
			if ((bezeichnungen != null) && (sollGruppe != null)) {
				gruppe.sortierungDefinitionen =
						ReportingReportvorlageUtils.waehleGespeicherteAuswahl(sollGruppe.sortierungDefinitionenOptionen, option -> option.bezeichnung,
								bezeichnungen);
			}
		}
	}

	/**
	 * Setzt die gespeicherte Filterauswahl je Gruppe, sofern die übergebene Gruppe noch keine explizit übermittelte Auswahl enthält („explizit gewinnt"). Die
	 * gespeicherten Bezeichnungen werden gegen die SOLL-Optionen der gleichnamigen Gruppe aufgelöst.
	 *
	 * @param zielGruppen die übergebenen Filtergruppen (werden verändert)
	 * @param sollGruppen die SOLL-Filtergruppen mit den Options-Katalogen
	 * @param auswahlen   die gespeicherten Filterauswahlen je Gruppe (ggf. leer)
	 */
	static void wendeGespeicherteFilterungAn(final List<ReportingFilterDefinitionGruppe> zielGruppen,
			final List<ReportingFilterDefinitionGruppe> sollGruppen, final List<ReportingEinstellungenBenutzerVorlageGruppe> auswahlen) {
		final Map<String, List<String>> gespeicherteAuswahlen = indexiereAuswahlen(auswahlen);
		if (gespeicherteAuswahlen.isEmpty()) {
			return;
		}
		final Map<String, ReportingFilterDefinitionGruppe> sollProBezeichnung = new HashMap<>();
		for (final ReportingFilterDefinitionGruppe sollGruppe : sollGruppen) {
			if ((sollGruppe != null) && (sollGruppe.bezeichnung != null)) {
				sollProBezeichnung.put(sollGruppe.bezeichnung, sollGruppe);
			}
		}
		for (final ReportingFilterDefinitionGruppe gruppe : zielGruppen) {
			if ((gruppe == null) || (gruppe.bezeichnung == null) || !gruppe.filterDefinitionen.isEmpty()) {
				continue;
			}
			final List<String> bezeichnungen = gespeicherteAuswahlen.get(gruppe.bezeichnung);
			final ReportingFilterDefinitionGruppe sollGruppe = sollProBezeichnung.get(gruppe.bezeichnung);
			if ((bezeichnungen != null) && (sollGruppe != null)) {
				gruppe.filterDefinitionen =
						ReportingReportvorlageUtils.waehleGespeicherteAuswahl(sollGruppe.filterDefinitionenOptionen, option -> option.bezeichnung,
								bezeichnungen);
			}
		}
	}

	/**
	 * Indexiert die gespeicherten Auswahlen einer Sortier- oder Filterebene nach Gruppenbezeichnung. Einträge ohne Gruppe oder ohne Bezeichnungsliste werden
	 * übersprungen.
	 *
	 * @param auswahlen die gespeicherten Auswahlen je Gruppe (ggf. null)
	 *
	 * @return die Map Gruppenbezeichnung → Liste der ausgewählten Definitions-Bezeichnungen
	 */
	private static Map<String, List<String>> indexiereAuswahlen(final List<ReportingEinstellungenBenutzerVorlageGruppe> auswahlen) {
		final Map<String, List<String>> index = new HashMap<>();
		if (auswahlen == null) {
			return index;
		}
		for (final ReportingEinstellungenBenutzerVorlageGruppe auswahl : auswahlen) {
			if ((auswahl != null) && (auswahl.gruppe != null) && (auswahl.bezeichnungen != null)) {
				index.put(auswahl.gruppe, auswahl.bezeichnungen);
			}
		}
		return index;
	}

	/**
	 * Prüft, ob eine Einstellung (Parameter oder Gruppe) mit den angegebenen Anforderungen im aktuellen ServerMode und mit den Kompetenzen des angemeldeten
	 * Benutzers erlaubt ist.
	 *
	 * @param uiErforderlicherServerMode der mindestens erforderliche ServerMode als Text (leer = in allen Modi erlaubt)
	 * @param uiErforderlicheKompetenzen die IDs der erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 *
	 * @return true, wenn die Einstellung erlaubt ist, ansonsten false
	 */
	boolean istEinstellungErlaubt(final String uiErforderlicherServerMode, final List<Long> uiErforderlicheKompetenzen) {
		// ServerMode: Ein leerer Text wird von getByText als STABLE interpretiert, was in allen Modi erlaubt ist.
		if (!ServerMode.getByText(uiErforderlicherServerMode).checkServerMode(this.serverMode)) {
			return false;
		}
		// Kompetenzen: Ohne Anforderung ist die Einstellung für alle erlaubt.
		if ((uiErforderlicheKompetenzen == null) || uiErforderlicheKompetenzen.isEmpty()) {
			return true;
		}
		final Set<BenutzerKompetenz> kompetenzen = new HashSet<>();
		for (final Long id : uiErforderlicheKompetenzen) {
			if (id != null) {
				final BenutzerKompetenz kompetenz = BenutzerKompetenz.getByID(id);
				if (kompetenz != null) {
					kompetenzen.add(kompetenz);
				}
			}
		}
		return this.benutzer.pruefeKompetenz(kompetenzen);
	}
}
