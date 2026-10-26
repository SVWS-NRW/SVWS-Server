package de.svws_nrw.repo.schule.schulleitung;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Tabelle für die Schulleitung.
 */
public final class SchulleitungRepositoryImpl extends RepositoryImpl<DTOSchulleitung> implements SchulleitungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn die aktuelle Datenbank-Verbindung
	 */
	public SchulleitungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchulleitung.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOSchulleitung> getAllByIdLehrer(final long idLehrer) {
		return conn.queryList(DTOSchulleitung.QUERY_BY_LEHRERID, DTOSchulleitung.class, idLehrer);
	}

	@Override
	public List<DTOSchulleitung> getAllByIdLeitungsfunktion(final long idLeitungsfunktion) {
		return conn.queryList(DTOSchulleitung.QUERY_BY_LEITUNGSFUNKTIONID, DTOSchulleitung.class, idLeitungsfunktion);
	}

	/**
	 * Für die interne Nutzung in dieser Klasse: Sortiert Schulleitungseinträge nach ihrer Aktualität.
	 */
	private static Comparator<DTOSchulleitung> nachAktualitaet = Comparator.comparing(l -> {
		try {
			return (l.Bis == null) ? DateManager.fromValues(9999, 12, 31) : DateManager.from(l.Bis);
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			try {
				return DateManager.fromValues(1900, 1, 1);
			} catch (@SuppressWarnings("unused") final InvalidDateException e1) {
				return null;
			}
		}
	});

	/**
	 * Bestimmt die Einträge der Leitungsfunktionen Schulleitung und stellvertretende Schulleitung, welche den angegebenen Zeitraum überschneiden.
	 *
	 * @param vonBereich   das Anfangsdatum - bei Null wird vom 1.1.1900 ausgegangen
	 * @param bisBereich   das Enddatum - bei Null wird vom 31.12.9999 ausgegangen
	 *
	 * @return die Einträge im Zeitraum
	 *
	 * @throws InvalidDateException   falls die Standard-Datumswerte nicht erzeugt werden können
	 */
	private List<DTOSchulleitung> getAllByDates(final DateManager vonBereich, final DateManager bisBereich) throws InvalidDateException {
		final DateManager abschnittVon = (vonBereich == null) ? DateManager.fromValues(1900, 1, 1) : vonBereich;
		final DateManager abschnittBis = (bisBereich == null) ? DateManager.fromValues(9999, 12, 31) : bisBereich;
		return getAll().stream()
				.filter(l -> (l.LeitungsfunktionID == 1) || (l.LeitungsfunktionID == 2))
				.filter(l -> {
					try {
						final DateManager von = (l.Von == null) ? DateManager.fromValues(1900, 1, 1) : DateManager.from(l.Von);
						final DateManager bis = (l.Bis == null) ? DateManager.fromValues(9999, 12, 31) : DateManager.from(l.Bis);
						return (von.compareTo(abschnittBis) <= 0) && (bis.compareTo(abschnittVon) >= 0);
					} catch (@SuppressWarnings("unused") final InvalidDateException e) {
						return false;
					}
				})
				.toList();
	}

	@Override
	public Map<Long, DTOSchulleitung> getMapByDates(final DateManager vonBereich, final DateManager bisBereich) {
		try {
			return getAllByDates(vonBereich, bisBereich).stream()
					.collect(Collectors.groupingBy(
							l -> l.LeitungsfunktionID,
							Collectors.collectingAndThen(Collectors.maxBy(nachAktualitaet), opt -> opt.orElse(null))
					));
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			return new HashMap<>();
		}
	}

	@Override
	public Set<Long> getIdsLehrerByDates(final DateManager vonBereich, final DateManager bisBereich) {
		try {
			return getAllByDates(vonBereich, bisBereich).stream().map(l -> l.LehrerID).collect(Collectors.toSet());
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			return new HashSet<>();
		}
	}

}
