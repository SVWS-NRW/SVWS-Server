package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Schuljahresabschnitt}.
 */
public final class DataSchuljahresabschnitte extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Schuljahresabschnitt}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuljahresabschnitte(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuljahresabschnitte} in einen Core-DTO {@link Schuljahresabschnitt}.
	 */
	public static final Function<DTOSchuljahresabschnitte, Schuljahresabschnitt> dtoMapper = (final DTOSchuljahresabschnitte abschnitt) -> {
		final Schuljahresabschnitt daten = new Schuljahresabschnitt();
		daten.id = abschnitt.ID;
		daten.schuljahr = abschnitt.Jahr;
		daten.abschnitt = abschnitt.Abschnitt;
		daten.idVorigerAbschnitt = abschnitt.VorigerAbschnitt_ID;
		daten.idFolgeAbschnitt = abschnitt.FolgeAbschnitt_ID;
		return daten;
	};

	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link Schuljahresabschnitt}.
	 */
	private final Comparator<Schuljahresabschnitt> dataComparator = (a, b) -> {
		final int tmp = Integer.compare(a.schuljahr, b.schuljahr);
		return tmp == 0 ? Integer.compare(a.abschnitt, b.abschnitt) : tmp;
	};


	@Override
	public Response getAll() {
        return this.getList();
	}


	@Override
	public Response getList() {
    	final List<Schuljahresabschnitt> daten = this.getAbschnitte();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt die Liste aller Datenbank-DTOs für die Schuljahresabschnitte zurück.
	 *
	 * @param conn   die Datenbank-Verbindung für den Zugriff auf die DTOs
	 *
	 * @return die Liste der Datenbank-DTOs für die Schuljahresabschnitte
	 */
	public static @NotNull List<@NotNull DTOSchuljahresabschnitte> getDTOList(final @NotNull DBEntityManager conn) {
		return conn.queryAll(DTOSchuljahresabschnitte.class);
	}


	/**
	 * Gibt die Map für das Mapping der IDs auf alle Datenbank-DTOs der Schuljahresabschnitte zurück.
	 *
	 * @param conn   die Datenbank-Verbindung für den Zugriff auf die DTOs
	 *
	 * @return die Map für das Mapping der IDs auf alle Datenbank-DTOs der Schuljahresabschnitte
	 */
	public static @NotNull Map<@NotNull Long, @NotNull DTOSchuljahresabschnitte> getDTOMap(final @NotNull DBEntityManager conn) {
		return conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
	}


	/**
	 * Gibt alle Schuljahresabschnitte der Schule in einer sortierten Liste zurück.
	 *
	 * @return die Liste der Schuljahresabschnitt
	 */
	public List<Schuljahresabschnitt> getAbschnitte() {
		// Schuljahresabschnitte aus den Leistungsdaten bestimmen
		final List<DTOSchuljahresabschnitte> abschnitte = conn.queryAll(DTOSchuljahresabschnitte.class);
    	if ((abschnitte == null) || abschnitte.isEmpty())
    		return new ArrayList<>();
    	return abschnitte.stream().map(dtoMapper).sorted(dataComparator).toList();
	}

	/**
	 * Ermittelt den Schuljahresabschnitt für die angegebene ID. Existiert kein Schuljahresabschnitt, so
	 * wird null zurückgegeben.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param id     die ID des Schuljahresabschnitts
	 *
	 * @return der Schuljahresabschnitt
	 */
	public static Schuljahresabschnitt getByID(final DBEntityManager conn, final long id) {
		final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, id);
		if (abschnitt == null)
			return null;
		return dtoMapper.apply(abschnitt);
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			throw OperationError.BAD_REQUEST.exception("Die ID des Abschnitts darf nicht null sein.");
		final Schuljahresabschnitt daten = getByID(conn, id);
		if (daten == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Schuljahresabschnitt mit der ID %d gefunden.".formatted(id));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt den Schuljahresabschnitt für das angegebene Schuljahr und den angegebenen Abschnitt,
	 * sofern dieses existiert.
	 *
	 * @param conn        die Datenbankverbindung
	 * @param schuljahr   das Schuljahr
	 * @param abschnitt   der Abschnitt
	 *
	 * @return der Schuljahresabschnitt oder null
	 */
	public static Schuljahresabschnitt getFromSchuljahrUndAbschnitt(final DBEntityManager conn, final int schuljahr, final int abschnitt) {
		final List<DTOSchuljahresabschnitte> liste = conn.queryList("SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Jahr = ?1 AND e.Abschnitt = ?2", DTOSchuljahresabschnitte.class, schuljahr, abschnitt);
		if ((liste == null) || (liste.size() != 1))
			return null;
		return dtoMapper.apply(liste.get(0));
	}

}
