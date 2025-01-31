package de.svws_nrw.module.reporting.html.contexts;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "Lehrer", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextLehrer extends HtmlContext {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Liste, die die im Context ermitteln Daten speichert und den Zugriff auf die Daten abseits des html-Templates ermöglicht. */
	@JsonIgnore
	private ArrayList<ReportingLehrer> lehrer = new ArrayList<>();

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Lehrern.
	 *
	 * @param reportingRepository 	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param reportingLehrer		Liste der Lehrer, die berücksichtigt werden sollen.
	 */
	public HtmlContextLehrer(final ReportingRepository reportingRepository, final List<ReportingLehrer> reportingLehrer) {
		this.reportingRepository = reportingRepository;
		erzeugeContextFromLehrer(reportingLehrer);
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Lehrer-IDs.
	 *
	 * @param reportingRepository   Repository mit Parametern, Logger und Daten zum Reporting.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public HtmlContextLehrer(final ReportingRepository reportingRepository) throws ApiOperationException {
		this.reportingRepository = reportingRepository;
		erzeugeContextFromIds(this.reportingRepository.reportingParameter().idsHauptdaten);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Lehrern.
	 *
	 * @param reportingLehrer   	Liste der Lehrer, die berücksichtigt werden sollen.
	 */
	private void erzeugeContextFromLehrer(final List<ReportingLehrer> reportingLehrer) {

		// Sortiere die übergebene Liste der Lehrer
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		reportingLehrer.sort(Comparator.comparing(ReportingLehrer::nachname, colGerman)
				.thenComparing(ReportingLehrer::vorname, colGerman)
				.thenComparing(ReportingLehrer::id));

		lehrer = new ArrayList<>();
		lehrer.addAll(reportingLehrer);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Lehrer", lehrer);

		super.setContext(context);
	}


	/**
	 * Erzeugt den Context aus einer Liste von Lehrer-IDs.
	 *
	 * @param idsLehrer	Liste der IDs der Lehrer, die berücksichtigt werden sollen.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void erzeugeContextFromIds(final List<Long> idsLehrer) throws ApiOperationException {

		// Erzeuge Maps, damit auch später leicht auf die Lehrerdaten zugegriffen werden kann.
		final Map<Long, LehrerStammdaten> mapLehrer = new HashMap<>();
		final List<Long> fehlendeLehrer = new ArrayList<>();
		for (final Long idLehrer : idsLehrer) {
			if (reportingRepository.mapLehrerStammdaten().containsKey(idLehrer))
				mapLehrer.put(idLehrer, reportingRepository.mapLehrerStammdaten().get(idLehrer));
			else
				fehlendeLehrer.add(idLehrer);
		}
		if (!fehlendeLehrer.isEmpty()) {
			final List<LehrerStammdaten> fehlendeLehrerStammdaten =
					new DataLehrerStammdaten(reportingRepository.conn()).getListByIDs(fehlendeLehrer);
			fehlendeLehrerStammdaten.forEach(l -> this.reportingRepository.mapLehrerStammdaten().putIfAbsent(l.id, l));
			mapLehrer.putAll(fehlendeLehrerStammdaten.stream().collect(Collectors.toMap(s -> s.id, s -> s)));
		}

		// Die Lehrer bzw. ihre IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe sollten
		// sie aber in alphabetischer Reihenfolge der Lehrer sein.
		// Erzeuge daher eine Liste mit Lehrern, die in der alphabetischen Reihenfolge der Lehrer sortiert ist.
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		final List<LehrerStammdaten> sortierteLehrer = mapLehrer.values().stream()
				.sorted(Comparator.comparing((final LehrerStammdaten l) -> l.nachname, colGerman)
						.thenComparing((final LehrerStammdaten l) -> l.vorname, colGerman)
						.thenComparing((final LehrerStammdaten l) -> l.id))
				.toList();
		final List<Long> sortierteLehrerIDs = sortierteLehrer.stream().map(s -> s.id).toList();

		// Erzeuge nun die einzelnen Lehrerobjekte. Alle weiteren Daten werden später dynamisch nachgeladen.
		lehrer = new ArrayList<>();

		for (final Long lehrerID : sortierteLehrerIDs) {
			final ProxyReportingLehrer proxyReportingLehrer = new ProxyReportingLehrer(reportingRepository, mapLehrer.get(lehrerID));
			lehrer.add(proxyReportingLehrer);
		}

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("Lehrer", lehrer);

		super.setContext(context);
	}


	/**
	 * Teile diesen Context mit allen Lehrern in eine Liste von Contexts auf, die jeweils einen Lehrer enthalten.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextLehrer> getEinzelLehrerContexts() {
		final List<HtmlContextLehrer> resultContexts = new ArrayList<>();

		for (final ReportingLehrer reportingLehrer : lehrer) {
			final List<ReportingLehrer> einLehrer = new ArrayList<>();
			einLehrer.add(reportingLehrer);
			resultContexts.add(new HtmlContextLehrer(this.reportingRepository, einLehrer));
		}

		return resultContexts;
	}
}
