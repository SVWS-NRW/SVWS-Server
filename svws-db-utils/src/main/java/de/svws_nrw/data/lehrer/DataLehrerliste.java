package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerListeEintrag}.
 */
public final class DataLehrerliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerliste(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrer} in einen Core-DTO {@link LehrerListeEintrag}.
	 */
	private static final Function<DTOLehrer, LehrerListeEintrag> dtoMapper = (final DTOLehrer l) -> {
		final LehrerListeEintrag eintrag = new LehrerListeEintrag();
		eintrag.id = l.ID;
		eintrag.kuerzel = l.Kuerzel;
		eintrag.titel = (l.Titel == null) ? "" : l.Titel;
		eintrag.nachname = l.Nachname;
		eintrag.vorname = (l.Vorname == null) ? "" : l.Vorname;
		eintrag.personTyp = l.PersonTyp.kuerzel;
		eintrag.sortierung = (l.Sortierung == null) ? 32000 : l.Sortierung;
		eintrag.istSichtbar = (l.Sichtbar == null) || l.Sichtbar;
		eintrag.istRelevantFuerStatistik = (l.statistikRelevant == null) || l.statistikRelevant;
		return eintrag;
	};

	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link LehrerListeEintrag}.
	 */
	private static final Comparator<LehrerListeEintrag> dataComparator = (a, b) -> {
		final Collator collator = Collator.getInstance(Locale.GERMAN);
		if ((a.kuerzel == null) && (b.kuerzel != null))
			return -1;
		else if ((a.kuerzel != null) && (b.kuerzel == null))
			return 1;
		else if ((a.kuerzel == null) && (b.kuerzel == null))
			return 0;
		int result = collator.compare(a.kuerzel, b.kuerzel);
		if (result == 0) {
			if ((a.nachname == null) && (b.nachname != null))
				return -1;
			else if ((a.nachname != null) && (b.nachname == null))
				return 1;
			else if ((a.nachname == null) && (b.nachname == null))
				return 0;
			result = collator.compare(a.nachname, b.nachname);
		}
		if (result == 0) {
			if ((a.vorname == null) && (b.vorname != null))
				return -1;
			else if ((a.vorname != null) && (b.vorname == null))
				return 1;
			else if ((a.vorname == null) && (b.vorname == null))
				return 0;
			result = collator.compare(a.vorname, b.vorname);
		}
		return result;
	};


	/**
	 * Bestimmt die Liste aller Lehrer.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste der Lehrer
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<LehrerListeEintrag> getLehrerListe(final DBEntityManager conn) throws ApiOperationException {
		final List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		if (lehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Lehrer in der Datenbank gefunden.");
		return lehrer.stream().map(dtoMapper).sorted(dataComparator).toList();
	}


	@Override
	public Response getAll() throws ApiOperationException {
		final List<LehrerListeEintrag> daten = getLehrerListe(conn);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() throws ApiOperationException {
		final List<DTOLehrer> lehrer = conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true);
		if (lehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<LehrerListeEintrag> daten = lehrer.stream().map(dtoMapper).sorted(dataComparator).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
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
