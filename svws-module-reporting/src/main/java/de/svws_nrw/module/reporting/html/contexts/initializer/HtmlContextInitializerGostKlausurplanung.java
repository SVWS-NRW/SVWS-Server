package de.svws_nrw.module.reporting.html.contexts.initializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Initializer für die Datenaufbauten der GOSt-Klausurplanung: gymnasiale Oberstufe prüfen, die Stufen auswählen und den Haupt-Context erzeugen.
 * <p>Die Stufen (Abiturjahrgang und GOSt-Halbjahr) sind die Nutzlast dieses Reports, wie die IDs eines Listenreports: Form und Wertebereich werden als
 * Eingabe geprüft, ein nicht vorhandener Abiturjahrgang wird dagegen ausgelassen und gemeldet. Ohne übergebene Stufen werden alle drei Stufen aus dem
 * ausgewählten Schuljahresabschnitt abgeleitet und durchlaufen dieselbe Auswahl. Den Ausgabeumfang meldet der Context-Aufbau, denn die Zähleinheit dieses
 * Datenaufbaus sind die Schüler bzw. Termine der Stufen und die kennt erst der Klausurplan-Manager.</p>
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
		super(reportingContext, mapHtmlContexts);
		this.aufbau = aufbau;
	}


	/**
	 * Prüft die gymnasiale Oberstufe, wählt die Stufen aus und legt den erzeugten Haupt-Context in der Context-Map ab.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	@Override
	public void init() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Klausurplan für die HTML-Generierung.");

		HtmlContextValidierung.validiereSchuleMitGost(reportingContext);
		final List<GostKlausurenHalbjahresdaten> selection = waehleStufenAus();

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Klausurplanung für die HTML-Generierung mit Template %s.".formatted(reportingReportvorlage.name()));
		mapHtmlContexts.put(aufbau.contextSchluessel(), aufbau.contextErzeuger().apply(reportingContext, selection));
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
	 * Der Schlüssel des Haupt-Contexts in der Context-Map. Die Datenaufbauten der GOSt-Klausurplanung unterstützen die Einzelausgabe.
	 *
	 * @return Der Schlüssel des Haupt-Contexts in der Context-Map.
	 */
	@Override
	public String einzelContextBezeichnung() {
		return aufbau.contextSchluessel();
	}


	/**
	 * Wählt die Stufen des Klausurplans aus: übergebene kombinierte IDs oder ohne Übergabe die drei aus dem ausgewählten Schuljahresabschnitt abgeleiteten
	 * Stufen. Formfehler der übergebenen IDs sind Client-Fehler; ein nicht vorhandener Abiturjahrgang und ein abgeleitetes Paar ohne GOSt-Halbjahr werden
	 * ausgelassen und gemeldet - für sie gibt es keine Ausgabe.
	 *
	 * @return Die ausgewählten Stufen; leer, wenn keine angeforderte oder abgeleitete Stufe vorhanden ist.
	 *
	 * @throws ApiOperationException Falls eine übergebene ID formal ungültig ist oder die vorhandenen Abiturjahrgänge nicht geladen werden konnten.
	 */
	private List<GostKlausurenHalbjahresdaten> waehleStufenAus() throws ApiOperationException {
		final List<long[]> kandidaten = new ArrayList<>();
		final List<Long> parameterDaten = reportingParameter.idsHauptdaten();

		if (!parameterDaten.isEmpty()) {
			for (final Long kombinierteId : parameterDaten) {
				if (kombinierteId != null) {
					kandidaten.add(zerlegeKombinierteId(kombinierteId));
				}
			}
		} else {
			// Ohne übergebene Stufen gilt der Grundfall des Clients: alle drei Stufen gemäß dem ausgewählten Schuljahresabschnitt.
			final int schuljahr = reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr();
			final int abschnitt = reportingContext.repositorySchule().auswahlSchuljahresabschnitt().abschnitt();
			kandidatOderMelde(kandidaten, schuljahr + 3L, abschnitt - 1L);
			kandidatOderMelde(kandidaten, schuljahr + 2L, abschnitt + 1L);
			kandidatOderMelde(kandidaten, schuljahr + 1L, abschnitt + 3L);
		}

		// Der Existenzabgleich folgt nach der Formprüfung; ein Ladefehler der vorhandenen Abiturjahrgänge wirft statustragend als Serverfehler.
		final List<Integer> vorhandeneAbiturjahrgaenge = reportingContext.repositoryGost().abiturjahrgaenge();
		final List<GostKlausurenHalbjahresdaten> selection = new ArrayList<>();
		for (final long[] kandidat : kandidaten) {
			final int abiturjahr = (int) kandidat[0];
			final int halbjahr = (int) kandidat[1];
			if (vorhandeneAbiturjahrgaenge.contains(abiturjahr)) {
				selection.add(new GostKlausurenHalbjahresdaten(abiturjahr, halbjahr));
			} else {
				meldeAusgelasseneStufe(abiturjahr, halbjahr,
						"Der Abiturjahrgang %d ist nicht vorhanden; die Stufe mit dem GOSt-Halbjahr %s wird in der Ausgabe ausgelassen."
								.formatted(abiturjahr, GostHalbjahr.fromID(halbjahr).kuerzel));
			}
		}

		return selection;
	}

	/**
	 * Zerlegt eine übergebene kombinierte ID (z. B. 20253 für Abitur 2025 in Q1.2) und prüft ihre Form vor jeder Existenzprüfung: Das Abiturjahr muss
	 * vierstellig sein, das Halbjahr 0 bis 5. Eine zu lange oder zu kurze ID würde sonst still als unbekannter Abiturjahrgang ausgelassen.
	 *
	 * @param kombinierteId Die kombinierte ID aus Abiturjahr und GOSt-Halbjahr.
	 *
	 * @return Das Paar aus Abiturjahr und Halbjahres-ID.
	 *
	 * @throws ApiOperationException Falls Abiturjahr oder Halbjahr außerhalb des Wertebereichs liegen.
	 */
	private static long[] zerlegeKombinierteId(final long kombinierteId) throws ApiOperationException {
		final long abiturjahr = kombinierteId / 10;
		if ((abiturjahr < 1900) || (abiturjahr > 9999)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.");
		}
		HtmlContextValidierung.validiereHalbjahr((int) (kombinierteId % 10));
		return new long[] { abiturjahr, kombinierteId % 10 };
	}

	/**
	 * Übernimmt ein aus dem Schuljahresabschnitt abgeleitetes Paar in die Kandidatenliste, sofern die Halbjahres-ID ein GOSt-Halbjahr bezeichnet. Ein
	 * Abschnitt jenseits der beiden Schulhalbjahre erzeugt IDs ohne GOSt-Halbjahr; diese Stufe wird ausgelassen und gemeldet, denn der Anwender hat hier
	 * nichts übergeben, das sich abweisen ließe.
	 *
	 * @param kandidaten Die Kandidatenliste der Auswahl.
	 * @param abiturjahr Das abgeleitete Abiturjahr.
	 * @param halbjahr   Die abgeleitete Halbjahres-ID.
	 */
	private void kandidatOderMelde(final List<long[]> kandidaten, final long abiturjahr, final long halbjahr) {
		if (GostHalbjahr.fromID((int) halbjahr) == null) {
			meldeAusgelasseneStufe((int) abiturjahr, (int) halbjahr,
					"Zum ausgewählten Schuljahresabschnitt gehört kein GOSt-Halbjahr; die abgeleitete Stufe des Abiturjahrgangs %d wird in der Ausgabe ausgelassen."
							.formatted(abiturjahr));
			return;
		}
		kandidaten.add(new long[] { abiturjahr, halbjahr });
	}

	/**
	 * Meldet eine ausgelassene Stufe als Ausgabeproblem. Der Schlüssel trägt die kombinierte ID, sodass dieselbe Stufe je Aufruf einmal zählt.
	 *
	 * @param abiturjahr   Das Abiturjahr der Stufe.
	 * @param halbjahr     Die Halbjahres-ID der Stufe.
	 * @param beschreibung Der Sachverhalt für das Log.
	 */
	private void meldeAusgelasseneStufe(final int abiturjahr, final int halbjahr, final String beschreibung) {
		reportingContext.meldeAusgabeproblem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(GostKlausurenHalbjahresdaten.class, (abiturjahr * 10L) + halbjahr), beschreibung, null);
	}

}
