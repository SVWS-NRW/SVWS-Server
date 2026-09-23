package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.Map;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Initializer für die Datenaufbauten der GOSt-Klausurplanung: gymnasiale Oberstufe prüfen, die übergebenen Stufen auf ihre Form prüfen und den
 * Haupt-Context erzeugen.
 * <p>Die Stufen (Abiturjahrgang und GOSt-Halbjahr) sind die Nutzlast dieses Reports, wie die IDs eines Listenreports: Form und Wertebereich werden hier als
 * Eingabe geprüft und ein Verstoß abgewiesen. Die Auswahl selbst trifft das Repository beim Aufbau des Klausurplans; dort werden auch die Stufen
 * abgeleitet, wenn keine übergeben wurden. Den Ausgabeumfang meldet der Context-Aufbau, denn die Zähleinheit dieses Datenaufbaus sind die Schüler bzw.
 * Termine der Stufen und die kennt erst der Klausurplan-Manager.</p>
 * <p>Die beiden Sichtweisen — Schüler und Klausurtermine — teilen diesen Ablauf und unterscheiden sich allein in ihrer
 * {@link HtmlContextAufbauGostKlausurplanung}.</p>
 */
final class HtmlContextInitializerGostKlausurplanung extends HtmlContextInitializerBasis {

	/** Die Konfiguration des Datenaufbaus, den dieser Initializer ausführt. */
	private final HtmlContextAufbauGostKlausurplanung aufbau;


	/**
	 * Erzeugt den Initializer für einen konkreten Request.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param mapHtmlContexts  Die Map, in der die erzeugten HTML-Contexts gesammelt werden.
	 * @param aufbau           Die Konfiguration des auszuführenden Datenaufbaus.
	 */
	HtmlContextInitializerGostKlausurplanung(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts,
			final HtmlContextAufbauGostKlausurplanung aufbau) {
		super(reportingContext, mapHtmlContexts, aufbau);
		this.aufbau = aufbau;
	}


	/**
	 * Prüft die gymnasiale Oberstufe und die Form der übergebenen Stufen und legt den erzeugten Haupt-Context in der Context-Map ab.
	 *
	 * @throws ApiOperationException Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	@Override
	public void init() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Klausurplan für die HTML-Generierung.");

		HtmlContextValidierung.validiereSchuleMitGost(reportingContext);
		pruefeUebergebeneStufen();

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Klausurplanung für die HTML-Generierung mit Template %s.".formatted(reportingReportvorlage.name()));
		mapHtmlContexts.put(aufbau.contextSchluessel(), aufbau.contextErzeuger().apply(reportingContext));
	}


	/**
	 * Die Zähleinheit dieses Datenaufbaus sind die Schüler bzw. Termine der ausgewählten Stufen; sie stehen erst nach dem Manager-Aufbau im Context fest.
	 *
	 * @return true, denn der Context-Aufbau meldet den Ausgabeumfang.
	 */
	@Override
	public boolean meldetAusgabeumfangImContextAufbau() {
		return true;
	}


	/**
	 * Prüft die Form der übergebenen Stufen. Ein Formfehler ist ein Client-Fehler und wird abgewiesen, bevor Daten geladen werden.
	 *
	 * @throws ApiOperationException Falls eine übergebene ID formal ungültig ist.
	 */
	private void pruefeUebergebeneStufen() throws ApiOperationException {
		for (final Long kombinierteId : reportingParameter.idsHauptdaten()) {
			if (kombinierteId != null) {
				try {
					pruefeKombinierteId(kombinierteId);
				} catch (final ApiOperationException aoe) {
					// Welche der übergebenen Stufen beanstandet wird, trägt weder die Meldung noch das Eingangsprotokoll: Dieses zeigt nur einen
					// Auszug der Rohwerte, und die Prüfung läuft auf der bereinigten Liste.
					reportingContext.logger().logLn(LogLevel.ERROR, 4, "Beanstandete Stufe: " + kombinierteId);
					throw aoe;
				}
			}
		}
	}

	/**
	 * Prüft die Form einer übergebenen kombinierten ID (z. B. 20253 für Abitur 2025 in Q1.2): Das Abiturjahr muss zwischen 1900 und 9999 liegen, das
	 * Halbjahr zwischen 0 und 5. Eine zu lange oder zu kurze ID würde sonst still als unbekannter Abiturjahrgang ausgelassen. Das Repository prüft
	 * dieselben Grenzen noch einmal, wenn es die Angabe für den Aufbau liest.
	 *
	 * @param kombinierteId Die kombinierte ID aus Abiturjahr und GOSt-Halbjahr.
	 *
	 * @throws ApiOperationException Falls Abiturjahr oder Halbjahr außerhalb des Wertebereichs liegen.
	 */
	private static void pruefeKombinierteId(final long kombinierteId) throws ApiOperationException {
		final long abiturjahr = kombinierteId / 10;
		if ((abiturjahr < 1900) || (abiturjahr > 9999)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "### FEHLER: Die Angabe zum Abiturjahrgang ist ungültig.");
		}
		HtmlContextValidierung.validiereHalbjahr((int) (kombinierteId % 10));
	}

}
