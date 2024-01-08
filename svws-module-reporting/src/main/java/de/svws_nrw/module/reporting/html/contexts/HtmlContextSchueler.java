package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.html.base.HtmlContext;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import jakarta.ws.rs.WebApplicationException;
import org.thymeleaf.context.Context;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "Laufbahnplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextSchueler extends HtmlContext {

	/**
	 * Liste, die die im Context ermitteln Daten speichert und den Zugriff auf die Daten abseits des html-Templates ermöglicht.
	 */
	private ArrayList<ProxyReportingSchueler> schueler = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Das Repository zur Schuldatenbank.
	 * @param schuelerIDs			Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 * @param mitGostDaten 			Legt fest, ob der Daten zur gymnasialen Oberstufe mit in den Kontext geladen werden sollen.
	 */
	public HtmlContextSchueler(final ReportingRepository reportingRepository, final List<Long> schuelerIDs, final boolean mitGostDaten) {
		erzeugeContext(reportingRepository, schuelerIDs, mitGostDaten);
	}

	/**
	 * Eine interne Liste des Contexts mit den Daten der Schüler.
	 * @return	Liste mit den Daten Schüler.
	 */
	public List<ProxyReportingSchueler> getSchueler() {
		return schueler;
	}



	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param reportingRepository	Das Repository zur Schuldatenbank.
	 * @param schuelerIDs   		Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 * @param mitGostDaten 			Legt fest, ob der Daten zur gymnasialen Oberstufe mit in den Kontext geladen werden sollen.
	 */
	private void erzeugeContext(final ReportingRepository reportingRepository, final List<Long> schuelerIDs, final boolean mitGostDaten) throws WebApplicationException {

		// ####### Daten validieren. Wirft eine Exception bei Fehlern, andernfalls werden die Manager für die Blockung erzeugt. ###############################
		final DBEntityManager conn = reportingRepository.conn();

		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Datenbankverbindung ungültig.");

		if (schuelerIDs == null)
			throw OperationError.NOT_FOUND.exception("Keine Schueler-IDs übergeben.");

		// Prüfe die Schüler-IDs. Erzeuge Maps, damit auch später leicht auf die Schülerdaten zugegriffen werden kann.
		final Map<Long, SchuelerStammdaten> mapSchueler = DataSchuelerStammdaten.getListStammdaten(conn, schuelerIDs).stream().collect(Collectors.toMap(s -> s.id, s -> s));
		for (final Long sID : schuelerIDs)
			if (mapSchueler.get(sID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden ungültige Schüler-IDs übergeben.");


		// Nur, wenn Daten zur gymnasialen Oberstufe mit angefordert werden.
        if (mitGostDaten) {
			// Schule hat eine gym. Oberstufe? pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
			try {
				DBUtilsGost.pruefeSchuleMitGOSt(conn);
			} catch (WebApplicationException ex) {
				throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
			}

            final Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten = new HashMap<>(new DataGostSchuelerLaufbahnplanungBeratungsdaten(conn).getMapFromIDs(schuelerIDs));

			for (final Long sID : schuelerIDs)
				if (mapGostBeratungsdaten.get(sID) == null)
					throw OperationError.NOT_FOUND.exception("Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");

			reportingRepository.mapGostBeratungsdaten().putAll(mapGostBeratungsdaten);
		}

		// ####### Daten sind valide. Bereite nun Daten vor, um den Daten-Context später erzeugen zu können. #################################################################

		// Initialisieren des Repository für das Einlesen der Daten mehrerer Schüler
		reportingRepository.mapSchuelerStammdaten().putAll(mapSchueler);
		reportingRepository.mapAktuelleLernabschnittsdaten().putAll(new DataSchuelerLernabschnittsdaten(conn).getListFromSchuelerIDsUndSchuljahresabschnittID(schuelerIDs, reportingRepository.aktuellerSchuljahresabschnitt().id, false).stream().collect(Collectors.toMap(l -> l.schuelerID, l -> l)));

		// Die Schüler bzw. ihre IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe sollten
		// sie aber in alphabetischer Reihenfolge der Schüler sein.
		// Erzeuge daher eine Liste mit Schülern, die in der alphabetischen Reihenfolge der Schüler sortiert ist
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		final List<SchuelerStammdaten> sortierteSchueler = mapSchueler.values().stream()
				.sorted(Comparator.comparing((final SchuelerStammdaten s) -> s.nachname, colGerman)
						.thenComparing((final SchuelerStammdaten s) -> s.vorname, colGerman)
						.thenComparing((final SchuelerStammdaten s) -> s.id))
				.toList();
		final List<Long> sortierteSchuelerIDs = sortierteSchueler.stream().map(s -> s.id).toList();

		// Erzeuge nun die einzelnen Schülerobjekte. Alle weiteren Daten werden später dynamisch nachgeladen.
		schueler = new ArrayList<>();

		for (final Long schuelerID : sortierteSchuelerIDs) {
			final ProxyReportingSchueler proxyReportingSchueler = new ProxyReportingSchueler(reportingRepository, mapSchueler.get(schuelerID));
			schueler.add(proxyReportingSchueler);
		}

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Schueler", schueler);

		// Die folgenden Variablen werden zur Steuerung von Ausdrucken mit diesem Context benötigt.
		// Ihre Werte werden durch Werte aus der API überschrieben.
		context.setVariable("DruckparameterNurBelegteFaecher", false);
		context.setVariable("DruckparameterDetaillevel", 2);

		super.setContext(context);
	}
}
