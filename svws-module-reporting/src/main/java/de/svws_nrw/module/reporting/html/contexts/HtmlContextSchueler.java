package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.html.base.HtmlContext;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
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
	private ArrayList<ReportingSchueler> schueler = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 * @param reportingSchueler		Liste der Schüler, die berücksichtigt werden sollen.
	 */
	public HtmlContextSchueler(final List<ReportingSchueler> reportingSchueler) {
		erzeugeContext(reportingSchueler);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 * @param schuelerIDs			Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 */
	public HtmlContextSchueler(final ReportingRepository reportingRepository, final List<Long> schuelerIDs) {
		erzeugeContext(reportingRepository, schuelerIDs);
	}


	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 * @param reportingSchueler   	Liste der Schüler, die berücksichtigt werden sollen.
	 */
	private void erzeugeContext(final List<ReportingSchueler> reportingSchueler) throws WebApplicationException {

		if (reportingSchueler == null || reportingSchueler.isEmpty())
			throw OperationError.NOT_FOUND.exception("Keine Schueler übergeben.");

		// Sortiere die übergebene Liste der Schüler
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		reportingSchueler.sort(Comparator.comparing(ReportingSchueler::nachname, colGerman)
			.thenComparing(ReportingSchueler::vorname, colGerman)
			.thenComparing(ReportingSchueler::id));

		schueler = new ArrayList<>();
		schueler.addAll(reportingSchueler);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Schueler", schueler);

		super.setContext(context);
	}


	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 * @param idsSchueler   		Liste der IDs der Schüler, die berücksichtigt werden sollen.
	 */
	private void erzeugeContext(final ReportingRepository reportingRepository, final List<Long> idsSchueler) throws WebApplicationException {

		final DBEntityManager conn = reportingRepository.conn();

		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Keine Datenbankverbindung übergeben.");

		if (idsSchueler == null || idsSchueler.isEmpty())
			throw OperationError.NOT_FOUND.exception("Keine Schueler-IDs übergeben.");

		// Erzeuge Maps, damit auch später leicht auf die Schülerdaten zugegriffen werden kann.
		final Map<Long, SchuelerStammdaten> mapSchueler = new HashMap<>();
		final List<Long> fehlendeSchueler = new ArrayList<>();
		for (final Long idSchueler : idsSchueler) {
			if (reportingRepository.mapSchuelerStammdaten().containsKey(idSchueler))
				mapSchueler.put(idSchueler, reportingRepository.mapSchuelerStammdaten().get(idSchueler));
			else
				fehlendeSchueler.add(idSchueler);
		}
		if (!fehlendeSchueler.isEmpty())
			mapSchueler.putAll(DataSchuelerStammdaten.getListStammdaten(reportingRepository.conn(), fehlendeSchueler).stream().collect(Collectors.toMap(s -> s.id, s -> s)));

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

		super.setContext(context);
	}

	/**
	 * Eine interne Liste des Contexts mit den Daten der Schüler.
	 * @return	Liste mit den Daten Schüler.
	 */
	public List<ReportingSchueler> getSchueler() {
		return schueler;
	}


	/**
	 * Teil diesen Context mit allen Schülern in eine Liste von Contexts auf, die jeweils einen Schüler enthalten.
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextSchueler> getEinzelSchuelerContexts() {
		final List<HtmlContextSchueler> resultContexts = new ArrayList<>();

		for (final ReportingSchueler reportingSchueler : schueler) {
			final List<ReportingSchueler> einSchueler = new ArrayList<>();
			einSchueler.add(reportingSchueler);
			resultContexts.add(new HtmlContextSchueler(einSchueler));
		}

		return resultContexts;
	}
}
