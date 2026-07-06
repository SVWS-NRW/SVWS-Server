package de.svws_nrw.module.reporting.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * <p>Diese Klasse stellt nach dem Aufruf über die API den Einstiegspunkt in die Report-Generierung dar.</p>
 * <p>Über die Reporting-Parameter werden unter anderem das Report-Format, das zu verwendende Template und die zu druckenden Daten definiert.</p>
 * <p>Rückgabe ist eine dem Zielformat entsprechende Response oder im Fehlerfall eine SimpleOperationResponse mit Log-Informationen.</p>
 */
public final class ReportingFactory {

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameter reportingParameter;

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingContext reportingContext;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt. Dieser wird in den Reporting-Context übergeben, um auch während der Generierung der Ausgabe Fehler festzuhalten und auszugeben. */
	private final Logger logger = new Logger();

	/** Die Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log = new LogConsumerList();


	/**
	 * Erzeugt eine neue Reporting-Factory, um einen Report zu erzeugen.
	 *
	 * @param conn 						Die Verbindung zur Datenbank.
	 * @param reportingParameter 		Einstellungen und Daten zum Steuern der Report-Generierung.
	 * @param reportingAusgabeformat	Ds Ausgabeformat, das verwendet werden soll. Dient zum Abgleich mit den ReportingParameter-Werten.
	 *
	 * @throws ApiOperationException   	im Fehlerfall
	 */
	public ReportingFactory(final DBEntityManager conn, final ReportingParameter reportingParameter, final ReportingAusgabeformat reportingAusgabeformat)
			throws ApiOperationException {

		try {
			// Initialisiere Log für Status- und Fehlermeldungen
			this.logger.addConsumer(log);

			this.logger.logLn(LogLevel.DEBUG, 0, ">>> Beginn des Initialisierens der Reporting-Factory und des Validierens übergebener Daten.");

			// Validiere Datenbankverbindung
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Datenbankverbindung.");
			if (conn == null) {
				this.logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Es wurde keine Verbindung zur Datenbank für die Initialisierung der Reporting-Factory "
						+ "übergeben.");
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"### FEHLER: Es wurde keine Verbindung zur Datenbank für die Initialisierung der Reporting-Factory übergeben.");
			}

			// Validiere Reporting-Parameter
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Reporting-Parameter.");
			if (reportingParameter == null) {
				this.logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Es wurden keine Reporting-Parameter für die Initialisierung der Reporting-Factory übergeben"
						+ ".");
				throw new ApiOperationException(Status.BAD_REQUEST,
						"### FEHLER: Es wurden keine Reporting-Parameter für die Initialisierung der Reporting-Factory übergeben.");
			}
			this.reportingParameter = reportingParameter;

			// Validiere das Ausgabeformat, insbesondere, ob dieses mit dem von der API vorgegebenen Ausgabeformat übereinstimmt.
			if ((reportingAusgabeformat == null) || reportingAusgabeformat.equals(ReportingAusgabeformat.UNDEFINED)
					|| (ReportingAusgabeformat.getByID(this.reportingParameter.ausgabeformat) == ReportingAusgabeformat.UNDEFINED)
					|| (ReportingAusgabeformat.getByID(this.reportingParameter.ausgabeformat) != reportingAusgabeformat)) {
				this.logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Es wurde kein gültiges Ausgabeformat definiert oder in den Reporting-Parametern ist ein "
						+ "anderes als für den API-Aufruf gültiges Ausgabeformat definiert worden.");
				throw new ApiOperationException(Status.BAD_REQUEST,
						"### FEHLER: Es wurde kein gültiges Ausgabeformat definiert oder in den Reporting-Parametern ist ein anderes als für den API-Aufruf "
								+ "gültiges Ausgabeformat definiert worden.");
			}

			// Validiere die Angaben zur Vorlage für den Report.
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Report-Vorlage.");
			final ReportingReportvorlage reportvorlage = ReportingReportvorlage.getByBezeichnung(this.reportingParameter.reportvorlage);
			if (reportvorlage == null) {
				this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Es wurde keine gültige Report-Vorlage für die Initialisierung der Reporting-Factory übergeben.");
				throw new ApiOperationException(Status.BAD_REQUEST,
						"### FEHLER: Es wurde keine gültige Report-Vorlage für die Initialisierung der Reporting-Factory übergeben.");
			}

			// Logge für einen evtl. späteren Fehlerfall das Format und das Template.
			this.logger.logLn(LogLevel.DEBUG, 4, "Übergebenes und validiertes Ausgabeformat: " + reportingAusgabeformat.name());
			this.logger.logLn(LogLevel.DEBUG, 4, "Übergebene und validierte Report-Vorlage: " + reportvorlage.getBezeichnung());

			// Validiere Hauptdaten-Angabe
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Hauptdaten.");
			if (this.reportingParameter.idsHauptdaten == null) {
				this.reportingParameter.idsHauptdaten = new ArrayList<>();
			} else {
				// Evtl. vorhandene null-Elemente in der Liste entfernen.
				this.reportingParameter.idsHauptdaten = new ArrayList<>(reportingParameter.idsHauptdaten.stream().filter(Objects::nonNull).distinct().toList());
			}
			if (this.reportingParameter.idsHauptdaten.isEmpty()) {
				this.logger.logLn(LogLevel.INFO, 4, "HINWEIS: Die Liste der Hauptdaten ist leer an die Reporting-Factory übergeben worden.");
			}

			// Stelle sicher, dass bei nicht vorhandenen Detaildaten eine leere Liste statt null vorhanden ist.
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Detaildaten.");
			if (this.reportingParameter.idsDetaildaten == null) {
				this.reportingParameter.idsDetaildaten = new ArrayList<>();
			} else {
				// Evtl. vorhandene null-Elemente in der Liste entfernen.
				this.reportingParameter.idsDetaildaten =
						new ArrayList<>(reportingParameter.idsDetaildaten.stream().filter(Objects::nonNull).distinct().toList());
			}

			// Validiere die Liste mit den Vorlage-Parametern. Lade dazu die definierten Vorlagen aus der ReportingReportvorlage-Klasse und weise diesen
			// definierten Parametern evtl. übergebene Werte zu. So ist sichergestellt, dass imm die richtigen Vorlagen-Parameter vorhanden sind.
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Vorlage-Parameter.");
			validiereVorlageParameter(reportingParameter);

			this.logger.logLn(LogLevel.DEBUG, 4, "Erzeugung des Reporting-Context");
			this.reportingContext = new ReportingContext(conn, this.reportingParameter, this.logger, this.log);

			// Setze über die API übergebene Einstellungen zurück, die der aktuelle ServerMode oder die Benutzerkompetenzen nicht zulassen. Dies verhindert,
			// dass eine im Client deaktivierte (und damit nicht einstellbare) Option über einen manipulierten Request gesetzt werden kann.
			this.logger.logLn(LogLevel.DEBUG, 4, "Prüfe ServerMode und Benutzerkompetenzen der übergebenen Einstellungen.");
			setzeUnerlaubteEinstellungenZurueck(reportvorlage);

			this.logger.logLn(LogLevel.DEBUG, 0, "<<< Ende des Initialisierens der Reporting-Factory und des Validierens übergebener Daten.");
		} catch (final ApiOperationException aoe) {
			// Die ApiOperationException wird unverändert weitergereicht, damit der ursprüngliche Status-Code nach außen erhalten bleibt.
			// Stacktrace und Log werden dennoch wie im allgemeinen catch-Zweig protokolliert und auf der Konsole ausgegeben.
			ReportingExceptionUtils.logException(
					"### FEHLER: Während der Initialisierung und Validierung der Daten der Reporting-Factory ist ein Fehler aufgetreten.", aoe, logger,
					LogLevel.ERROR, 0);
			final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
			// Gebe das Log, das in der SimpleOperationResponse für Entwicklungszwecke auf der Console aus.
			sop.log.forEach(Logger.global()::logLn);
			// Wirf die Exception mit dem ursprünglichen Status-Code neu, aber mit dem vollständigen Log als Body
			throw new ApiOperationException(aoe.getStatus(), aoe, sop, MediaType.APPLICATION_JSON);
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"### FEHLER: Während der Initialisierung und Validierung der Daten der Reporting-Factory ist ein Fehler aufgetreten.", e, logger,
					LogLevel.ERROR, 0);
			final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
			// Gebe das Log, das in der SimpleOperationResponse für Entwicklungszwecke auf der Console aus.
			sop.log.forEach(Logger.global()::logLn);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, sop, MediaType.APPLICATION_JSON);
		}
	}

	/**
	 * Validiert die übergebenen Vorlage-Parameter und deren Gruppen gegen die definierte Vorlage und kombiniert diese bei Übereinstimmung.
	 * Fehlende oder ungültige Parameter werden ignoriert. Die kombinierten Parameter und Gruppen werden anschließend zurückgeschrieben.
	 *
	 * @param reportingParameter Das ReportingParameter-Objekt, welches die zu validierenden und kombinierten Vorlage-Parameter beinhaltet.
	 */
	private void validiereVorlageParameter(final ReportingParameter reportingParameter) {
		final ReportingReportvorlage reportvorlage = ReportingReportvorlage.getByBezeichnung(this.reportingParameter.reportvorlage);
		if (reportvorlage == null) {
			// Sollte durch die vorherige Validierung im Konstruktor eigentlich nicht passieren.
			this.reportingParameter.reportvorlageParameterGruppen = new ArrayList<>();
			return;
		}

		// Übergebene Parameter-Werte (IST-Werte) aus den übergebenen Gruppen als Map ablegen: Key = gruppenname#parametername
		final List<ReportingReportvorlageParameterGruppe> uebergebeneParameterGruppen =
				(reportingParameter.reportvorlageParameterGruppen == null)
						? new ArrayList<>()
						: reportingParameter.reportvorlageParameterGruppen.stream().filter(Objects::nonNull).toList();

		final HashMap<String, ReportingReportvorlageParameter> mapUebergebeneReportvorlageParameter = new HashMap<>();
		for (final ReportingReportvorlageParameterGruppe g : uebergebeneParameterGruppen) {
			if ((g.name == null) || g.name.isBlank() || (g.reportvorlageParameter == null)) {
				continue;
			}
			validiereParameterDerGruppe(reportingParameter, g, mapUebergebeneReportvorlageParameter);
		}

		// Definierte Struktur aus der Reportvorlage laden (SOLL-Werte) und Ergebnisliste nach Vorlage mit den evtl. übergebenen Werten kombinieren.
		// Das Ergebnis wird in das Reporting-Parameter-Gruppen-Objekt zurückgeschrieben.
		this.reportingParameter.reportvorlageParameterGruppen = getKombinierteReportvorlageParameter(reportvorlage, mapUebergebeneReportvorlageParameter);
	}

	/**
	 * Validiert die Parameter der übergebenen Parametergruppe und überprüft, ob sie mit den übergebenen Werten übereinstimmen.
	 * Hierbei wird für die HTML-Ausgabe die Einzelausgabe deaktiviert, während beim E-Mail-Versand aus Datenschutzgründen die Einzelausgabe der Daten
	 * automatisch aktiviert wird.
	 *
	 * @param reportingParameter                    das Reporting-Parameter-Objekt
	 * @param reportingReportvorlageParameterGruppe die Parametergruppe
	 * @param mapUebergebeneReportvorlageParameter  die Map mit den übergebenen Reportvorlage-Parametern
	 */
	private static void validiereParameterDerGruppe(final ReportingParameter reportingParameter,
			final ReportingReportvorlageParameterGruppe reportingReportvorlageParameterGruppe,
			final HashMap<String, ReportingReportvorlageParameter> mapUebergebeneReportvorlageParameter) {
		for (final ReportingReportvorlageParameter p : reportingReportvorlageParameterGruppe.reportvorlageParameter) {
			if ((p == null) || (p.name == null) || p.name.isBlank()) {
				continue;
			}
			// Die HTML-Ausgabe erfolgt im Browser und kann daher keine Einzeldateien anzeigen. Es darf daher keine Einzelausgabe angefordert werden.
			// Stelle dies hier sicher.
			if ((reportingParameter.ausgabeformat == ReportingAusgabeformat.HTML.getId()) && p.name.equalsIgnoreCase("einzelausgabeDaten")) {
				p.wert = "false";
			}
			// Um Datenschutz zu gewährleisten, wird beim E-Mail-Versand nur die Einzelausgabe von Daten unterstützt (ein einzelnes PDF pro Datenelement).
			// Stelle dies hier sicher.
			if ((reportingParameter.ausgabeformat == ReportingAusgabeformat.EMAIL.getId()) && p.name.equalsIgnoreCase("einzelausgabeDaten")) {
				p.wert = "true";
			}
			mapUebergebeneReportvorlageParameter.put(reportingReportvorlageParameterGruppe.name + "#" + p.name, p);
		}
	}

	/**
	 * Kombiniert die Reportvorlage-Parametergruppen aus der Reportvorlage mit den evtl. übergebenen Werten der Parameter.
	 * Dazu wird die definierte Struktur aus der Reportvorlage geladen (SOLL-Werte) und die Ergebnisliste nach Vorlage aufgebaut.
	 *
	 * @param reportvorlage die Reportvorlage, aus der die Reportvorlage-Parametergruppen geladen werden sollen
	 * @param mapUebergebeneReportvorlageParameter die übergebenen Parameter-Werte mit Keys in der Form gruppenname#parametername
	 *
	 * @return die Reportvorlage-Parametergruppen aus der Reportvorlage kombiniert mit den evtl. übergebenen Werten der Parameter.
	 */
	private List<ReportingReportvorlageParameterGruppe> getKombinierteReportvorlageParameter(final ReportingReportvorlage reportvorlage,
			final HashMap<String, ReportingReportvorlageParameter> mapUebergebeneReportvorlageParameter) {
		final List<ReportingReportvorlageParameterGruppe> definierteGruppen =
				reportvorlage.getReportingParameter().reportvorlageParameterGruppen.stream().filter(Objects::nonNull).toList();
		final List<ReportingReportvorlageParameterGruppe> kombinierteGruppen = new ArrayList<>();

		for (final ReportingReportvorlageParameterGruppe definierteGruppe : definierteGruppen) {
			final ReportingReportvorlageParameterGruppe kombinierteGruppe = new ReportingReportvorlageParameterGruppe();
			kombinierteGruppe.name = definierteGruppe.name;
			kombinierteGruppe.beschreibung = definierteGruppe.beschreibung;
			kombinierteGruppe.uiIstSichtbar = definierteGruppe.uiIstSichtbar;
			kombinierteGruppe.uiAnzahlSpalten = definierteGruppe.uiAnzahlSpalten;
			kombinierteGruppe.uiErforderlicherServerMode = definierteGruppe.uiErforderlicherServerMode;
			kombinierteGruppe.uiErforderlicheKompetenzen = new ArrayList<>(definierteGruppe.uiErforderlicheKompetenzen);

			final List<ReportingReportvorlageParameter> kombinierteReportvorlageParameter = new ArrayList<>();
			if (definierteGruppe.reportvorlageParameter != null) {
				for (final ReportingReportvorlageParameter definierterReportvorlageParameter : definierteGruppe.reportvorlageParameter) {
					if (definierterReportvorlageParameter == null) {
						continue;
					}

					final ReportingReportvorlageParameter kombinierterReportvorlageParameter = new ReportingReportvorlageParameter();
					// Daten zunächst aus der Vorlage übernehmen ...
					kombinierterReportvorlageParameter.name = definierterReportvorlageParameter.name;
					kombinierterReportvorlageParameter.bezeichnung = definierterReportvorlageParameter.bezeichnung;
					kombinierterReportvorlageParameter.typ = definierterReportvorlageParameter.typ;
					kombinierterReportvorlageParameter.wert = definierterReportvorlageParameter.wert;
					kombinierterReportvorlageParameter.uiIstSichtbar = definierterReportvorlageParameter.uiIstSichtbar;
					kombinierterReportvorlageParameter.uiKomponentenTyp = definierterReportvorlageParameter.uiKomponentenTyp;
					kombinierterReportvorlageParameter.uiAnzahlSpalten = definierterReportvorlageParameter.uiAnzahlSpalten;
					kombinierterReportvorlageParameter.uiErforderlicherServerMode = definierterReportvorlageParameter.uiErforderlicherServerMode;
					kombinierterReportvorlageParameter.uiErforderlicheKompetenzen =
							new ArrayList<>(definierterReportvorlageParameter.uiErforderlicheKompetenzen);

					// ... und wenn ein gültiger Wert ungleich null übergeben wurde, so wird dieser gesetzt.
					final ReportingReportvorlageParameter uebergebenerReportvorlageParameter =
							mapUebergebeneReportvorlageParameter.get(definierteGruppe.name + "#" + definierterReportvorlageParameter.name);
					if ((uebergebenerReportvorlageParameter != null) && (uebergebenerReportvorlageParameter.wert != null)) {
						kombinierterReportvorlageParameter.wert = uebergebenerReportvorlageParameter.wert;
					}

					kombinierteReportvorlageParameter.add(kombinierterReportvorlageParameter);
				}
			}

			kombinierteGruppe.reportvorlageParameter = kombinierteReportvorlageParameter;
			kombinierteGruppen.add(kombinierteGruppe);
		}
		return kombinierteGruppen;
	}


	/**
	 * Setzt die Werte von Vorlage-Parametern sowie die Auswahl von Sortier- und Filtergruppen auf ihre Standardwerte zurück, wenn der aktuelle ServerMode oder
	 * die Kompetenzen des angemeldeten Benutzers die übergebenen Einstellungen nicht zulassen. So kann eine im Client ausgeblendete (und damit nicht
	 * einstellbare) Einstellung nicht über einen manipulierten Request gesetzt werden.
	 *
	 * @param reportvorlage die Reportvorlage mit den SOLL-Definitionen (Standardwerte und Anforderungen)
	 */
	private void setzeUnerlaubteEinstellungenZurueck(final ReportingReportvorlage reportvorlage) {
		// Vorlage-Parameter je Gruppe prüfen und ggf. auf den Standardwert der Vorlage zurücksetzen.
		for (final ReportingReportvorlageParameterGruppe gruppe : this.reportingParameter.reportvorlageParameterGruppen) {
			setzeUnerlaubteParameterZurueck(reportvorlage, gruppe);
		}

		// Sortier- und Filtergruppen: Die Anforderungen werden aus den SOLL-Definitionen gelesen, damit manipulierte Anforderungswerte keine Wirkung haben.
		final ReportingParameter sollParameter = reportvorlage.getReportingParameter();
		setzeUnerlaubteSortierungZurueck(sollParameter);
		setzeUnerlaubteFilterungZurueck(sollParameter);
	}


	/**
	 * Setzt die Werte der Parameter einer Parametergruppe auf den Standardwert der Vorlage zurück, wenn die Gruppe oder der jeweilige Parameter im aktuellen
	 * ServerMode oder mit den Kompetenzen des angemeldeten Benutzers nicht erlaubt ist.
	 *
	 * @param reportvorlage die Reportvorlage mit den Standardwerten
	 * @param gruppe        die zu prüfende Parametergruppe
	 */
	private void setzeUnerlaubteParameterZurueck(final ReportingReportvorlage reportvorlage, final ReportingReportvorlageParameterGruppe gruppe) {
		if ((gruppe == null) || (gruppe.reportvorlageParameter == null)) {
			return;
		}
		final boolean gruppeErlaubt = istEinstellungErlaubt(gruppe.uiErforderlicherServerMode, gruppe.uiErforderlicheKompetenzen);
		for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
			if ((parameter == null) || (parameter.name == null)
					|| (gruppeErlaubt && istEinstellungErlaubt(parameter.uiErforderlicherServerMode, parameter.uiErforderlicheKompetenzen))) {
				continue;
			}
			final ReportingReportvorlageParameter standard = reportvorlage.getDefaultVorlageparameter(parameter.name);
			if (standard != null) {
				parameter.wert = standard.wert;
			}
		}
	}


	/**
	 * Setzt die übergebene Auswahl einer Sortiergruppe auf die SOLL-Auswahl der Vorlage zurück, wenn die Gruppe im aktuellen ServerMode oder mit den Kompetenzen
	 * des Benutzers nicht erlaubt ist.
	 *
	 * @param sollParameter die SOLL-Definition der Reportvorlage mit den Anforderungen der Sortiergruppen
	 */
	private void setzeUnerlaubteSortierungZurueck(final ReportingParameter sollParameter) {
		final HashMap<String, ReportingSortierungDefinitionGruppe> sollGruppen = new HashMap<>();
		for (final ReportingSortierungDefinitionGruppe sollGruppe : sollParameter.sortierungDefinitionenGruppen) {
			if ((sollGruppe != null) && (sollGruppe.bezeichnung != null)) {
				sollGruppen.put(sollGruppe.bezeichnung, sollGruppe);
			}
		}
		for (final ReportingSortierungDefinitionGruppe gruppe : this.reportingParameter.sortierungDefinitionenGruppen) {
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
	 * @param sollParameter die SOLL-Definition der Reportvorlage mit den Anforderungen der Filtergruppen
	 */
	private void setzeUnerlaubteFilterungZurueck(final ReportingParameter sollParameter) {
		final HashMap<String, ReportingFilterDefinitionGruppe> sollGruppen = new HashMap<>();
		for (final ReportingFilterDefinitionGruppe sollGruppe : sollParameter.filterDefinitionenGruppen) {
			if ((sollGruppe != null) && (sollGruppe.bezeichnung != null)) {
				sollGruppen.put(sollGruppe.bezeichnung, sollGruppe);
			}
		}
		for (final ReportingFilterDefinitionGruppe gruppe : this.reportingParameter.filterDefinitionenGruppen) {
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
	 * Prüft, ob eine Einstellung (Parameter oder Gruppe) mit den angegebenen Anforderungen im aktuellen ServerMode und mit den Kompetenzen des angemeldeten
	 * Benutzers erlaubt ist.
	 *
	 * @param uiErforderlicherServerMode der mindestens erforderliche ServerMode als Text (leer = in allen Modi erlaubt)
	 * @param uiErforderlicheKompetenzen die IDs der erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 *
	 * @return true, wenn die Einstellung erlaubt ist, ansonsten false
	 */
	private boolean istEinstellungErlaubt(final String uiErforderlicherServerMode, final List<Long> uiErforderlicheKompetenzen) {
		// ServerMode: Ein leerer Text wird von getByText als STABLE interpretiert, was in allen Modi erlaubt ist.
		if (!ServerMode.getByText(uiErforderlicherServerMode).checkServerMode(this.reportingContext.serverMode())) {
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
		return this.reportingContext.benutzer().pruefeKompetenz(kompetenzen);
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen Datei oder ZIP-Datei mit den mehreren generierten Report-Dateien.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das Dokument oder die ZIP-Datei.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public Response createReportResponse() throws ApiOperationException {

		try {
			this.logger.logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung einer API-Response zur Report-Generierung.");

			final Response reportResponse;

			switch (ReportingAusgabeformat.getByID(reportingParameter.ausgabeformat)) {
				case ReportingAusgabeformat.UNDEFINED -> {
					logger.logLn(LogLevel.ERROR, 4, "FEHLER: Das Ausgabeformat UNDEFINIERT wurde für die Report-Generierung übergeben.");
					final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
					throw new ApiOperationException(Status.BAD_REQUEST, null, sop, MediaType.APPLICATION_JSON);
				}
				case ReportingAusgabeformat.HTML -> {
					this.logger.logLn(LogLevel.DEBUG, 4, "HTML als Ausgabeformat für die Report-Generierung gewählt.");
					final HtmlFactory htmlFactory = new HtmlFactory(reportingContext);
					// Erzeuge im try-Block eine temporäre Response, die bei einem Fehler automatisch geschlossen wird (SonarCube-Angabe)
					try (Response autocloseResponse = htmlFactory.createHtmlResponse()) {
						if (!log.getText(LogLevel.ERROR).isEmpty()) {
							logger.logLn(LogLevel.ERROR, 0,
									"### FEHLER: Während der Erzeugung einer HTML-Response zur Report-Generierung ist ein Fehler geloggt worden.");
							final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
							throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
						}
						// Response klonen, damit die zurückgegebene Response nicht die Auto-Close-Ressource ist
						reportResponse = Response.fromResponse(autocloseResponse).build();
					}
				}
				case ReportingAusgabeformat.PDF -> {
					this.logger.logLn(LogLevel.DEBUG, 4, "PDF als Ausgabeformat für die Report-Generierung gewählt.");
					final HtmlFactory htmlFactory = new HtmlFactory(reportingContext);
					final List<ReportBuilderHtml> htmlBuilders = htmlFactory.createHtmlBuilders();
					this.logger.logLn(LogLevel.DEBUG, 4, "HTML-Builder wurden erzeugt.");
					final PdfFactory pdfFactory = new PdfFactory(htmlBuilders, reportingContext);
					// Erzeuge im try-Block eine temporäre Response, die bei einem Fehler automatisch geschlossen wird (SonarCube-Angabe)
					try (Response autocloseResponse = pdfFactory.createPdfResponse()) {
						if (!log.getText(LogLevel.ERROR).isEmpty()) {
							logger.logLn(LogLevel.ERROR, 0,
									"### FEHLER: Während der Erzeugung einer PDF-Response zur Report-Generierung ist ein Fehler geloggt worden.");
							final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
							throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
						}
						// Response klonen, damit die zurückgegebene Response nicht die Auto-Close-Ressource ist
						reportResponse = Response.fromResponse(autocloseResponse).build();
					}
				}
				case ReportingAusgabeformat.EMAIL -> {
					this.logger.logLn(LogLevel.DEBUG, 4, "EMAIL als Ausgabeformat für die Report-Generierung gewählt.");
					final HtmlFactory htmlFactory = new HtmlFactory(reportingContext);
					final List<ReportBuilderHtml> htmlBuilders = htmlFactory.createHtmlBuilders();
					this.logger.logLn(LogLevel.DEBUG, 4, "HTML-Builder wurden erzeugt.");
					final PdfFactory pdfFactory = new PdfFactory(htmlBuilders, reportingContext);
					final EmailFactory emailFactory = new EmailFactory(reportingContext);
					// Erzeuge im try-Block eine temporäre Response, die bei einem Fehler automatisch geschlossen wird (SonarQube-Angabe)
					try (Response autocloseResponse = emailFactory.sendEmails(pdfFactory)) {
						if (!log.getText(LogLevel.ERROR).isEmpty()) {
							logger.logLn(LogLevel.ERROR, 0, "### FEHLER: Während des E-Mail-Versands (Response) wurde ein Fehler geloggt.");
							final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
							throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
						}
						// Response klonen, damit die zurückgegebene Response nicht die Auto-Close-Ressource ist
						reportResponse = Response.fromResponse(autocloseResponse).build();
					}
				}
				case null, default -> {
					logger.logLn(LogLevel.ERROR, 4, "FEHLER: Kein bekanntes Ausgabeformat für die Report-Generierung übergeben.");
					final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
				}
			}
			// Prüfe nun, ob während der Report-Generierung ein Fehler aufgetreten ist, der als Error ins Log geschrieben wurde, aber nicht als Fehler
			// geworfen wurde.
			if (!log.getText(LogLevel.ERROR).isEmpty()) {
				logger.logLn(LogLevel.ERROR, 0, "### FEHLER: Während der Erzeugung einer API-Response zur Report-Generierung ist ein Fehler geloggt worden.");
				final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
			}
			// Wenn kein Fehler vermerkt wurde, kann der Report zurückgegeben werden.
			this.logger.logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung einer API-Response zur Report-Generierung.");
			return reportResponse;
		} catch (final ApiOperationException aoe) {
			// Die ApiOperationException wird unverändert weitergereicht, damit der ursprüngliche Status-Code nach außen erhalten bleibt.
			// Stacktrace und Log werden dennoch wie im allgemeinen catch-Zweig protokolliert und auf der Konsole ausgegeben.
			ReportingExceptionUtils.logException(
					"### FEHLER: Während der Erzeugung einer API-Response zur Report-Generierung ist ein Fehler aufgetreten.", aoe, logger,
					LogLevel.ERROR, 0);
			final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
			// Gebe das Log, das in der SimpleOperationResponse für Entwicklungszwecke auf der Console aus.
			sop.log.forEach(Logger.global()::logLn);
			// Wirf die Exception mit dem ursprünglichen Status-Code neu, aber mit dem vollständigen Log als Body
			throw new ApiOperationException(aoe.getStatus(), aoe, sop, MediaType.APPLICATION_JSON);
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"### FEHLER: Während der Erzeugung einer API-Response zur Report-Generierung ist ein Fehler aufgetreten.", e, logger,
					LogLevel.ERROR, 0);
			final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
			// Gebe das Log, das in der SimpleOperationResponse für Entwicklungszwecke auf der Console aus.
			sop.log.forEach(Logger.global()::logLn);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, sop, MediaType.APPLICATION_JSON);
		}
	}
}
