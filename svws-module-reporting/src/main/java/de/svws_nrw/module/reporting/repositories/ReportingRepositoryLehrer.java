package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schule.DataEinwilligungsarten;
import de.svws_nrw.data.schule.DataLernplattformen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.types.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Domänen-Repository für Lehrkräfte (Stammdaten und Reporting-Objekte).
 * Die Lehrerstammdaten werden erst bei Bedarf aus der Datenbank geladen: per einzelner ID über {@link #lehrer(long)},
 * per Liste über {@link #lehrer(List)} mit Bulk-Nachladen oder als Vollbestand über {@link #alleLehrer()}.
 */
public class ReportingRepositoryLehrer {

	private static final String QUERY_LEISTUNGSDATEN_FACHLEHRER_KLASSENUNTERRICHT =
			"SELECT ld, a.Schueler_ID FROM DTOSchuelerLeistungsdaten ld, DTOSchuelerLernabschnittsdaten a "
					+ "WHERE ld.Abschnitt_ID = a.ID "
					+ "AND a.Schuljahresabschnitts_ID = ?1 "
					+ "AND a.WechselNr = 0 "
					+ "AND ld.Fachlehrer_ID = ?2 "
					+ "AND ld.Kurs_ID IS NULL";

	private static final String QUERY_LEISTUNGSDATEN_ZUSATZLEHRER_KLASSENUNTERRICHT =
			"SELECT ld, a.Schueler_ID FROM DTOSchuelerLeistungsdaten ld, DTOSchuelerLernabschnittsdaten a "
					+ "WHERE ld.Abschnitt_ID = a.ID "
					+ "AND a.Schuljahresabschnitts_ID = ?1 "
					+ "AND a.WechselNr = 0 "
					+ "AND ld.Zusatzkraft_ID = ?2 "
					+ "AND ld.Kurs_ID IS NULL";

	private static final String QUERY_LEISTUNGSDATEN_FACHLEHRER_KURSUNTERRICHT =
			"SELECT ld, a.Schueler_ID FROM DTOSchuelerLeistungsdaten ld, DTOSchuelerLernabschnittsdaten a "
					+ "WHERE ld.Abschnitt_ID = a.ID "
					+ "AND a.Schuljahresabschnitts_ID = ?1 "
					+ "AND a.WechselNr = 0 "
					+ "AND ld.Kurs_ID IS NOT NULL "
					+ "AND ld.Fachlehrer_ID = ?2";

	private static final String QUERY_LEISTUNGSDATEN_ZUSATZLEHRER_KURSUNTERRICHT =
			"SELECT ld, a.Schueler_ID FROM DTOSchuelerLeistungsdaten ld, DTOSchuelerLernabschnittsdaten a "
					+ "WHERE ld.Abschnitt_ID = a.ID "
					+ "AND a.Schuljahresabschnitts_ID = ?1 "
					+ "AND a.WechselNr = 0 "
					+ "AND ld.Kurs_ID IN (SELECT k.ID FROM DTOKurs k, DTOKursLehrer kl WHERE k.ID = kl.Kurs_ID "
					+ "AND k.Schuljahresabschnitts_ID = ?1 AND kl.Lehrer_ID = ?2) "
					+ "AND ld.Fachlehrer_ID = ?2";

	private final ReportingContext reportingContext;

	private final Map<Long, LehrerStammdaten> mapLehrerStammdaten = new HashMap<>();
	private final Map<Long, ReportingLehrer> mapLehrer = new HashMap<>();

	/** Markiert, ob die Stammdaten aller Lehrkräfte bereits einmal vollständig aus der Datenbank geladen wurden. */
	private boolean alleLehrerStammdatenGeladen = false;

	/**
	 * Erstellt ein neues ReportingLehrerRepository. Die Stammdaten werden erst bei Bedarf geladen.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 */
	public ReportingRepositoryLehrer(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}


	// ##### Lehrer (Reporting-Objekte und Stammdaten) #####

	/**
	 * Gibt das ReportingLehrer-Objekt zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank nachgeladen.
	 * Die Methode delegiert an {@link #lehrer(List, boolean)}, damit auch die Map der Lehrerstammdaten konsistent gefüllt wird.
	 * Schlägt das Nachladen fehl oder existiert die Lehrkraft nicht, wird ein nicht-gecachter Fallback-Lehrer mit leeren
	 * Stammdaten zurückgegeben, damit Reports keine NPE auslösen, sondern Leerstrings rendern.
	 *
	 * @param idLehrer Die ID des Lehrers.
	 *
	 * @return Das ReportingLehrer-Objekt, ein Fallback-Objekt mit leeren Stammdaten bei DB-Fehler oder fehlendem Eintrag.
	 */
	public ReportingLehrer lehrer(final long idLehrer) {
		final List<ReportingLehrer> result = lehrer(List.of(idLehrer), false);
		if (!result.isEmpty()) {
			return result.getFirst();
		}
		// Liste leer: entweder Lehrkraft existiert nicht oder wurde durch den User-Filter ausgeschlossen.
		// Existiert ein Eintrag im Cache, wurde sie gefiltert → null. Sonst Fallback-Objekt mit leeren Stammdaten.
		if (mapLehrer.containsKey(idLehrer)) {
			return null;
		}
		final LehrerStammdaten fallback = new LehrerStammdaten();
		fallback.id = idLehrer;
		return new ProxyReportingLehrer(this.reportingContext, fallback);
	}

	/**
	 * Gibt eine sortierte Liste von ReportingLehrer-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsLehrer Liste der Lehrer-IDs.
	 *
	 * @return Sortierte Liste von ReportingLehrer-Objekten.
	 */
	public List<ReportingLehrer> lehrer(final List<Long> idsLehrer) {
		return lehrer(idsLehrer, true);
	}

	/**
	 * Gibt eine Liste von ReportingLehrer-Objekten zu den übergebenen IDs zurück, optional sortiert.
	 *
	 * @param idsLehrer     Liste der Lehrer-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 *
	 * @return Liste von ReportingLehrer-Objekten.
	 */
	public List<ReportingLehrer> lehrer(final List<Long> idsLehrer, final boolean sortiereListe) {
		final Comparator<ReportingLehrer> comparator = ComparatorFactory.buildComparator(this.reportingContext.sortierungService(),
				this.reportingContext.logger(), ReportingLehrer.class.getSimpleName(),
				ReportingLehrer.SORTIERUNG, sortiereListe);
		final Predicate<ReportingLehrer> filter = ReportingLehrer.FILTER.bedingung(
				this.reportingContext.filterService().getFilter(ReportingLehrer.class.getSimpleName()), null);

		return ReportingRepositoryUtils.erstelleReportingListe(idsLehrer, mapLehrerStammdaten, mapLehrer,
				fehlendeIds -> new DataLehrerStammdaten(this.reportingContext.conn(), new DataLernplattformen(this.reportingContext.conn()),
						new DataEinwilligungsarten(this.reportingContext.conn())).getListByIDs(fehlendeIds),
				key -> new ProxyReportingLehrer(this.reportingContext, mapLehrerStammdaten.get(key)),
				stammdaten -> stammdaten.id,
				comparator, filter,
				"Lehrer", this.reportingContext.logger());
	}

	/**
	 * Gibt alle Lehrkräfte der Schule als Reporting-Objekte zurück. Beim ersten Aufruf werden die Stammdaten aller Lehrkräfte aus der
	 * Datenbank geladen, damit der Vollbestand für aggregierte Auswertungen verfügbar ist; bereits per Einzel- oder Bulk-Zugriff
	 * geladene Einträge bleiben erhalten. Sortierung und Benutzerfilter werden wie bei {@link #lehrer(List)} angewendet.
	 *
	 * @return Sortierte Liste aller Lehrkräfte.
	 */
	public List<ReportingLehrer> alleLehrer() {
		ladeAlleLehrerStammdaten();
		return lehrer(List.copyOf(mapLehrerStammdaten.keySet()));
	}

	/**
	 * Prüft, ob zur übergebenen ID eine Lehrkraft existiert. Beim ersten Aufruf wird dafür der Vollbestand der Lehrerstammdaten
	 * geladen. Die Prüfung ist <b>unabhängig vom Benutzerfilter</b> und liefert daher auch für ausgeblendete Lehrkräfte
	 * {@code true} — anders als {@link #lehrer(long)}, das für sie {@code null} zurückgibt.
	 *
	 * @param idLehrer Die ID der Lehrkraft.
	 *
	 * @return true, wenn zur ID eine Lehrkraft existiert, andernfalls false.
	 */
	public boolean existiertLehrer(final long idLehrer) {
		ladeAlleLehrerStammdaten();
		return mapLehrerStammdaten.containsKey(idLehrer);
	}

	private void ladeAlleLehrerStammdaten() {
		if (alleLehrerStammdatenGeladen) {
			return;
		}
		try {
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade alle Lehrerstammdaten.");
			final List<LehrerStammdaten> alle = new DataLehrerStammdaten(this.reportingContext.conn(),
					new DataLernplattformen(this.reportingContext.conn()),
					new DataEinwilligungsarten(this.reportingContext.conn())).getAll();
			for (final LehrerStammdaten ls : alle) {
				mapLehrerStammdaten.putIfAbsent(ls.id, ls);
			}
			alleLehrerStammdatenGeladen = true;
		} catch (final Exception e) {
			final String meldung = "FEHLER: Die Lehrerstammdaten konnten nicht ermittelt werden.";
			ReportingExceptionUtils.logException(meldung, e, this.reportingContext.logger(), LogLevel.ERROR, 0);
			throw new IllegalStateException(meldung, e);
		}
	}

	/**
	 * Übernimmt die Stammdaten einer Lehrkraft in den Cache dieses Repositories, sofern dort noch kein Eintrag zu dieser ID vorliegt.
	 * Die Methode ist für die Selbstregistrierung des {@link ProxyReportingLehrer} bei dessen Konstruktion vorgesehen; sie ersetzt
	 * den früheren direkten Schreibzugriff auf die Cache-Map.
	 * Wie dieser stellt sie zuvor den Vollbestand der Lehrerstammdaten sicher. Das ist <b>bewusst</b> so: Erzeugt
	 * {@link #lehrer(long)} einen Fallback-Lehrer mit leeren Stammdaten, bliebe dieser andernfalls dauerhaft im Cache stehen, da der
	 * spätere Vollbestand-Load die vorhandenen Einträge nicht überschreibt.
	 *
	 * @param idLehrer   Die ID der Lehrkraft.
	 * @param stammdaten Die Stammdaten der Lehrkraft.
	 */
	public void registriereStammdaten(final long idLehrer, final LehrerStammdaten stammdaten) {
		ladeAlleLehrerStammdaten();
		mapLehrerStammdaten.putIfAbsent(idLehrer, stammdaten);
	}


	// ##### Leistungsdaten zu Klassen- und Kursunterricht #####

	/**
	 * Gibt die Leistungsdaten zurück, in denen der übergebene Lehrer als Fachlehrer für Klassenunterricht (ohne Kurs-Zuordnung)
	 * im übergebenen Schuljahresabschnitt eingetragen ist.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts.
	 * @param idLehrer               Die ID des Lehrers.
	 *
	 * @return Liste der Leistungsdaten als {@link Object} {@code []} mit {@link DTOSchuelerLeistungsdaten} und Schüler-ID.
	 */
	public List<Object[]> leistungsdatenAlsFachlehrerKlassenunterricht(final long idSchuljahresabschnitt, final long idLehrer) {
		return queryLeistungsdaten(QUERY_LEISTUNGSDATEN_FACHLEHRER_KLASSENUNTERRICHT, idSchuljahresabschnitt, idLehrer);
	}

	/**
	 * Gibt die Leistungsdaten zurück, in denen der übergebene Lehrer als Zusatzlehrer für Klassenunterricht (ohne Kurs-Zuordnung)
	 * im übergebenen Schuljahresabschnitt eingetragen ist.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts.
	 * @param idLehrer               Die ID des Lehrers.
	 *
	 * @return Liste der Leistungsdaten als {@link Object} {@code []} mit {@link DTOSchuelerLeistungsdaten} und Schüler-ID.
	 */
	public List<Object[]> leistungsdatenAlsZusatzlehrerKlassenunterricht(final long idSchuljahresabschnitt, final long idLehrer) {
		return queryLeistungsdaten(QUERY_LEISTUNGSDATEN_ZUSATZLEHRER_KLASSENUNTERRICHT, idSchuljahresabschnitt, idLehrer);
	}

	/**
	 * Gibt die Leistungsdaten zurück, in denen der übergebene Lehrer als Fachlehrer für Kursunterricht
	 * im übergebenen Schuljahresabschnitt eingetragen ist.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts.
	 * @param idLehrer               Die ID des Lehrers.
	 *
	 * @return Liste der Leistungsdaten als {@link Object} {@code []} mit {@link DTOSchuelerLeistungsdaten} und Schüler-ID.
	 */
	public List<Object[]> leistungsdatenAlsFachlehrerKursunterricht(final long idSchuljahresabschnitt, final long idLehrer) {
		return queryLeistungsdaten(QUERY_LEISTUNGSDATEN_FACHLEHRER_KURSUNTERRICHT, idSchuljahresabschnitt, idLehrer);
	}

	/**
	 * Gibt die Leistungsdaten zurück, in denen der übergebene Lehrer als Zusatzlehrer eines Kurses geführt wird,
	 * für die er gleichzeitig als Fachlehrer eingetragen ist, im übergebenen Schuljahresabschnitt.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts.
	 * @param idLehrer               Die ID des Lehrers.
	 *
	 * @return Liste der Leistungsdaten als {@link Object} {@code []} mit {@link DTOSchuelerLeistungsdaten} und Schüler-ID.
	 */
	public List<Object[]> leistungsdatenAlsZusatzlehrerKursunterricht(final long idSchuljahresabschnitt, final long idLehrer) {
		return queryLeistungsdaten(QUERY_LEISTUNGSDATEN_ZUSATZLEHRER_KURSUNTERRICHT, idSchuljahresabschnitt, idLehrer);
	}

	private List<Object[]> queryLeistungsdaten(final String query, final long idSchuljahresabschnitt, final long idLehrer) {
		try {
			return this.reportingContext.conn().queryList(query, Object[].class, idSchuljahresabschnitt, idLehrer);
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Fehler bei der Ermittlung von Leistungsdaten für Lehrer-ID %d im Schuljahresabschnitt %d.".formatted(idLehrer,
							idSchuljahresabschnitt),
					e, this.reportingContext.logger(), LogLevel.ERROR, 0);
			return new ArrayList<>();
		}
	}
}
