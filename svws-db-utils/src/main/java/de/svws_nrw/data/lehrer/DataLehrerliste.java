package de.svws_nrw.data.lehrer;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link LehrerListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerliste(final DBEntityManager conn) {
		super(conn);
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
	 * @return die Liste der Lehrer oder leere Liste
	 */
	public List<LehrerListeEintrag> getLehrerListe() {
		final List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		return lehrer.stream().map(this::map).sorted(dataComparator).toList();
	}


	@Override
	public List<LehrerListeEintrag> getAll() {
		return getLehrerListe();
	}

	@Override
	public List<LehrerListeEintrag> getList() {
		final List<DTOLehrer> lehrer = conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true);
		return lehrer.stream().map(this::map).sorted(dataComparator).toList();
	}

	/**
	 * Bestimmt zu den übergebenen Lehrer-IDs die jeweils zugehörigen Abschnittsdaten aus der Datenbank für
	 * den angegebenen Schuljahresabschnitt und gib eine Map mit der Zuordnung zurück.
	 *
	 * @param conn                     die aktuelle Datenbank-Verbindung
	 * @param idsLehrer                die IDs der Lehrer
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Zuordnung der Abschnittsdaten zu den Lehrer-IDs
	 */
	public static Map<Long, DTOLehrerAbschnittsdaten> getDTOMapAbschnittsdatenByID(final @NotNull DBEntityManager conn,
			final @NotNull List<Long> idsLehrer, final long idSchuljahresabschnitt) {
		if (idsLehrer.isEmpty())
			return new HashMap<>();
		final List<DTOLehrerAbschnittsdaten> listAbschnitte =
				conn.queryList(DTOLehrerAbschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID + " AND e.Lehrer_ID IN ?2", DTOLehrerAbschnittsdaten.class,
						idSchuljahresabschnitt, idsLehrer);
		return listAbschnitte.stream().collect(Collectors.toMap(a -> a.Lehrer_ID, a -> a));
	}
}
