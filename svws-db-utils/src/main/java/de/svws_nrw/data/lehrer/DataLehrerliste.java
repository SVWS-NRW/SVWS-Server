package de.svws_nrw.data.lehrer;

import java.text.Collator;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Termin;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link LehrerListeEintrag}.
 */
public final class DataLehrerliste extends DataManagerRevised<Long, DTOLehrer, LehrerListeEintrag> {

	/** Die ID des Schuljahresabschnittes auf welchen sich die Abfrage bezieht. */
	private final Long idSchuljahresabschnitt;


	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link LehrerListeEintrag}.
	 *
	 * @param conn                     die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes für die Lehrerliste oder null, falls keine Einschränkung
	 *                                 auf einen Schuljahresabschnitt gewünscht ist
	 */
	public DataLehrerliste(final DBEntityManager conn, final Long idSchuljahresabschnitt) {
		super(conn);
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
	}


	/**
	 * Prüft, ob der übergebene Lehrer in dem Schuljahresabschnitt, auf welchen sich die Abfrage bezieht aktiv ist oder nicht.
	 * In dem Fall, dass die Abfrage keinen Bezug zu einem Schuljahresabschnitt hat, wird immer true zurückgegeben.
	 *
	 * @param l   das DTO des Lehrers
	 *
	 * @return true, wenn der Lehrer in dem Schuljahresabschnitt aktiv war, und ansonsten false
	 */
	private boolean pruefeAktiv(final DTOLehrer l) {
		if (idSchuljahresabschnitt == null)
			return true;
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		// Prüfe ggf. das Zugangsdatum
		if (l.DatumZugang != null) {
			final LocalDate dateZugang = LocalDate.parse(l.DatumZugang);
			final int year = dateZugang.getYear();
			if (year > schuljahresabschnitt.schuljahr + 1)
				return false;
			if (year == schuljahresabschnitt.schuljahr + 1) {
				final int month = dateZugang.getMonthValue();
				if (month >= 8)
					return false;
				if (schuljahresabschnitt.abschnitt == 1) {
					final DateManager date = Termin.getLetzterUnterrichtstagImErstenHalbjahr(schuljahresabschnitt.schuljahr);
					if (date != null) {
						final int day = dateZugang.getDayOfMonth();
						if ((month > date.getMonat()) || ((month == date.getMonat()) && (day > date.getTag())))
							return false;
					}
				}
			}
		}
		// Prüfe ggf. das Abgangsdatum
		if (l.DatumAbgang != null) {
			final LocalDate dateAbgang = LocalDate.parse(l.DatumAbgang);
			final int year = dateAbgang.getYear();
			if ((year < schuljahresabschnitt.schuljahr) || ((schuljahresabschnitt.abschnitt == 2) && (year == schuljahresabschnitt.schuljahr)))
				return false;
			if (year <= schuljahresabschnitt.schuljahr + 1) {
				final int month = dateAbgang.getMonthValue();
				if ((schuljahresabschnitt.abschnitt == 1) && (year == schuljahresabschnitt.schuljahr) && (month <= 7))
					return false;
				if ((schuljahresabschnitt.abschnitt == 2) && (year == schuljahresabschnitt.schuljahr + 1)) {
					final DateManager date = Termin.getLetzterUnterrichtstagImErstenHalbjahr(schuljahresabschnitt.schuljahr);
					if (date != null) {
						final int day = dateAbgang.getDayOfMonth();
						if ((month < date.getMonat()) || ((month == date.getMonat()) && (day <= date.getTag())))
							return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	protected LehrerListeEintrag map(final DTOLehrer dtoLehrer) {
		final LehrerListeEintrag eintrag = new LehrerListeEintrag();
		eintrag.id = dtoLehrer.ID;
		eintrag.kuerzel = dtoLehrer.Kuerzel;
		eintrag.titel = (dtoLehrer.Titel == null) ? "" : dtoLehrer.Titel;
		eintrag.nachname = (dtoLehrer.Nachname == null) ? "" : dtoLehrer.Nachname;
		eintrag.vorname = (dtoLehrer.Vorname == null) ? "" : dtoLehrer.Vorname;
		eintrag.personTyp = (dtoLehrer.PersonTyp == null) ? "" : dtoLehrer.PersonTyp.kuerzel;
		eintrag.sortierung = (dtoLehrer.Sortierung == null) ? 32000 : dtoLehrer.Sortierung;
		eintrag.istAktiv = this.pruefeAktiv(dtoLehrer);
		eintrag.istSichtbar = (dtoLehrer.Sichtbar == null) || dtoLehrer.Sichtbar;
		eintrag.istRelevantFuerStatistik = (dtoLehrer.statistikRelevant == null) || dtoLehrer.statistikRelevant;
		return eintrag;
	}

	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link LehrerListeEintrag}.
	 */
	private static final Comparator<LehrerListeEintrag> dataComparator = (a, b) -> {
		final Collator collator = Collator.getInstance(Locale.GERMAN);
		if ((a.kuerzel == null) && (b.kuerzel != null))
			return -1;
		else if ((a.kuerzel != null) && (b.kuerzel == null))
			return 1;
		else if (a.kuerzel == null)
			return 0;
		int result = collator.compare(a.kuerzel, b.kuerzel);
		if (result == 0) {
			if ((a.nachname == null) && (b.nachname != null))
				return -1;
			else if ((a.nachname != null) && (b.nachname == null))
				return 1;
			else if (a.nachname == null)
				return 0;
			result = collator.compare(a.nachname, b.nachname);
		}
		if (result == 0) {
			if ((a.vorname == null) && (b.vorname != null))
				return -1;
			else if ((a.vorname != null) && (b.vorname == null))
				return 1;
			else if (a.vorname == null)
				return 0;
			result = collator.compare(a.vorname, b.vorname);
		}
		return result;
	};

	/**
	 * Bestimmt die Liste aller Lehrer.
	 *
	 * @param includeReferenzInfo   wenn True erhalten die Daten die Information, ob der Lehrer in anderen Datenbanktabellen referenziert ist oder nicht.
	 * 								Dies erfordert eine zusätzliche Datenbankabfrage und ist aus daher Performancegründen nur empfohlen, wenn diese
	 * 								Information benötigt wird.
	 *
	 * @return die Liste der Lehrer oder leere Liste
	 */
	public List<LehrerListeEintrag> getLehrerListe(final boolean includeReferenzInfo) {
		// Bestimme zunächst die Lehrer aus der Datenbank, ggf. nur sichtbare Lehrer
		final List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		if (lehrer.isEmpty())
			return Collections.emptyList();

		// Ergänze ggf. die Information, ob Lehrer irgendwo in der Datenbank referenziert wurden... Dies ist die DB-Anfrage dafür
		final Set<Long> idsOfReferencedLehrer = includeReferenzInfo
				? getIdsOfReferencedLehrer(lehrer.stream().map(l -> l.ID).collect(Collectors.toSet()))
				: Collections.emptySet();

		// Erstelle die Einträge für die Lehrerliste, filtere ggf. Einträge anhand des Schuljahresabschnittes und ergänze ggf. die Informationen, ob die Lehrer an anderer Stelle referenziert wurden
		return lehrer.stream().map(l -> {
			final LehrerListeEintrag lehrerListeEintrag = map(l);
			if (includeReferenzInfo)
				lehrerListeEintrag.referenziertInAnderenTabellen = idsOfReferencedLehrer.contains(lehrerListeEintrag.id);
			return lehrerListeEintrag;
		}).sorted(dataComparator).toList();
	}

	@Override
	public List<LehrerListeEintrag> getAll() {
		return getLehrerListe(true);
	}

	@Override
	public List<LehrerListeEintrag> getList() {
		return getLehrerListe(true);
	}

	/**
	 * Bestimmt zu den übergebenen Lehrer-IDs die jeweils zugehörigen Abschnittsdaten aus der Datenbank für
	 * den angegebenen Schuljahresabschnitt und gib eine Map mit der Zuordnung zurück.
	 *
	 * @param idsLehrer                die IDs der Lehrer
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Zuordnung der Abschnittsdaten zu den Lehrer-IDs
	 */
	public Map<Long, DTOLehrerAbschnittsdaten> getDTOAbschnittsdatenByID(final @NotNull List<Long> idsLehrer, final long idSchuljahresabschnitt) {
		if (idsLehrer.isEmpty())
			return new HashMap<>();
		final List<DTOLehrerAbschnittsdaten> listAbschnitte =
				conn.queryList(DTOLehrerAbschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID + " AND e.Lehrer_ID IN ?2", DTOLehrerAbschnittsdaten.class,
						idSchuljahresabschnitt, idsLehrer);
		return listAbschnitte.stream().collect(Collectors.toMap(a -> a.Lehrer_ID, a -> a));
	}

	@Override
	protected long getLongId(final DTOLehrer lehrer) {
		return lehrer.ID;
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOLehrer> lehrer, final Map<Long, SimpleOperationResponse> mapResponses) {
		final Set<Long> result = getIdsOfReferencedLehrer(lehrer.stream().map(l -> l.ID).collect(Collectors.toSet()));
		lehrer.stream().filter(l -> result.contains(l.ID)).forEach(l -> {
			final SimpleOperationResponse response = mapResponses.get(l.ID);
			response.success = false;
			response.log.add("Der Lehrer mit dem Kuerzel %s und der id %d ist in der Datenbank referenziert und kann daher nicht gelöscht werden"
					.formatted(l.Kuerzel, l.ID));
		});
	}

	private Set<Long> getIdsOfReferencedLehrer(final Set<Long> idsLehrer) {
		final String queryKurse = "SELECT DISTINCT a.Lehrer_ID FROM DTOKurs a WHERE a.Lehrer_ID IN :idsLehrer";
		final String querySchuelerLeistungsdaten = "SELECT DISTINCT b.Fachlehrer_ID FROM DTOSchuelerLeistungsdaten b WHERE b.Fachlehrer_ID IN :idsLehrer";
		final String querySchuelerLernabschnittsdaten = "SELECT DISTINCT c.Tutor_ID FROM DTOSchuelerLernabschnittsdaten c WHERE c.Tutor_ID IN :idsLehrer";
		final String queryKlassenleitung = "SELECT DISTINCT d.Lehrer_ID FROM DTOKlassenLeitung d WHERE d.Lehrer_ID IN :idsLehrer";
		final String queryKursLehrer = "SELECT DISTINCT e.Lehrer_ID FROM DTOKursLehrer e WHERE e.Lehrer_ID IN :idsLehrer";
		final String querySchulleitung = "SELECT DISTINCT f.LehrerID FROM DTOSchulleitung f WHERE f.LehrerID IN :idsLehrer";

		final String query = String.join("\nUNION ALL\n", queryKurse, querySchuelerLeistungsdaten, querySchuelerLernabschnittsdaten,
				queryKlassenleitung, queryKursLehrer, querySchulleitung
		);
		final List<Long> results = conn.query(query, Long.class).setParameter("idsLehrer", idsLehrer).getResultList();
		return new HashSet<>(results);
	}
}
