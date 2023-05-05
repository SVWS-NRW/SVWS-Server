package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
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
	private final Function<DTOSchuljahresabschnitte, Schuljahresabschnitt> dtoMapper = (final DTOSchuljahresabschnitte abschnitt) -> {
		final Schuljahresabschnitt daten = new Schuljahresabschnitt();
		daten.id = abschnitt.ID;
		daten.schuljahr = abschnitt.Jahr;
		daten.abschnitt = abschnitt.Abschnitt;
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

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
