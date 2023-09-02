package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanListeEintrag}.
 */
public final class DataStundenplanListe extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanListeEintrag}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataStundenplanListe(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStundenplan} in einen Core-DTO {@link StundenplanListeEintrag}.
	 */
	public static final Function<DTOStundenplan, StundenplanListeEintrag> dtoMapper = (final DTOStundenplan s) -> {
		final StundenplanListeEintrag daten = new StundenplanListeEintrag();
		daten.id = s.ID;
		daten.bezeichnung = s.Beschreibung;
		daten.idSchuljahresabschnitt = s.Schuljahresabschnitts_ID;
		daten.gueltigAb = s.Beginn;
		daten.gueltigBis = s.Ende;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}


	/**
	 * Gibt die Liste der Stundenplänen für einen oder alle Schuljahresabschnitte zurück.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param idSchuljahresabschnitt   die ID des schuljahresabschnitts oder null für alle
	 *
	 * @return die Liste der Stundenpläne
	 */
	public static List<StundenplanListeEintrag> getStundenplaene(final DBEntityManager conn, final Long idSchuljahresabschnitt) {
		final ArrayList<StundenplanListeEintrag> daten = new ArrayList<>();
		final List<DTOStundenplan> plaene = (idSchuljahresabschnitt == null)
			? conn.queryAll(DTOStundenplan.class)
			: conn.queryNamed("DTOStundenplan.schuljahresabschnitts_id", idSchuljahresabschnitt, DTOStundenplan.class);
		if (plaene.isEmpty())
			return daten;
		final List<Long> idsSchuljahresabschnitte = plaene.stream().map(p -> p.Schuljahresabschnitts_ID).distinct().toList();
		final Map<Long, DTOSchuljahresabschnitte> mapAbschnitte = conn
			.queryNamed("DTOSchuljahresabschnitte.id.multiple", idsSchuljahresabschnitte, DTOSchuljahresabschnitte.class)
			.stream()
			.collect(Collectors.toMap(a -> a.ID, a -> a));

		for (final DTOStundenplan s : plaene) {
			final DTOSchuljahresabschnitte a = mapAbschnitte.get(s.Schuljahresabschnitts_ID);
			final StundenplanListeEintrag e = dtoMapper.apply(s);
			e.schuljahr = a.Jahr;
			e.abschnitt = a.Abschnitt;
			daten.add(e);
		}
		return daten.stream().sorted((a, b) -> {
			int cmp = Integer.compare(a.schuljahr, b.schuljahr);
			if (cmp != 0)
				return cmp;
			cmp = Integer.compare(a.abschnitt, b.abschnitt);
			if (cmp != 0)
				return cmp;
			cmp = a.gueltigAb.compareTo(b.gueltigAb);
			if (cmp != 0)
				return cmp;
			return a.gueltigBis.compareTo(b.gueltigBis);
		}).toList();
	}


	@Override
	public Response getList() {
		// Stundenpläne für alle Schuljahresabschnitte
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getStundenplaene(conn, null)).build();
	}


	@Override
	public Response get(final Long idSchuljahresabschnitt) {
		// Stundenpläne für einen speziellen Schuljahresabschnitt
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getStundenplaene(conn, idSchuljahresabschnitt)).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


	/**
     * Fügt einen leeren Stundenplan für den angegebenen Schuljahresabschnitt
     * hinzu und gibt den neuen Listen-Eintrag zurück.
	 *
     * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return Eine Response mit dem neuen Stundenplan-Listeneintrag
	 */
	public Response addEmpty(final long idSchuljahresabschnitt) {
		try {
			// Bestimme den Schuljahresabschnitt
			final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, idSchuljahresabschnitt);
			if (abschnitt == null)
				throw OperationError.NOT_FOUND.exception("Ein Schuljahresabschnitt mit der ID %d konnte nicht gefunden werden.".formatted(idSchuljahresabschnitt));
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			final DTOSchemaAutoInkremente dbStundenplanID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Stundenplan");
			final long idStundenplan = dbStundenplanID == null ? 1 : dbStundenplanID.MaxID + 1;
			// Ermittle, ob bereits Stundenpläne für den Schuljahresabschnitt existieren und bestimme das Start bzw. das Enddatum aus dem Abschnitt
			final List<StundenplanListeEintrag> stundenplaene = DataStundenplanListe.getStundenplaene(conn, idSchuljahresabschnitt);
			String beginn = (abschnitt.Abschnitt == 1) ? "%d-08-01".formatted(abschnitt.Jahr) : "%d-02-01".formatted(abschnitt.Jahr + 1);
			final String ende = (abschnitt.Abschnitt == 1) ? "%d-01-31".formatted(abschnitt.Jahr + 1) : "%d-07-31".formatted(abschnitt.Jahr + 1);
			if (!stundenplaene.isEmpty()) {
				// TODO Bestimme den Folgetag auf das Datum, nicht den Tag selbst
				final String vorherEnde = stundenplaene.get(stundenplaene.size() - 1).gueltigBis;
				if ((vorherEnde != null) && vorherEnde.compareTo(ende) < 0)
					beginn = stundenplaene.get(stundenplaene.size() - 1).gueltigBis;
			}
	    	// Erstelle und persistiere den leeren Stundenplan mit Wochentyp-Modell 0
	    	final DTOStundenplan stundenplan = new DTOStundenplan(idStundenplan, idSchuljahresabschnitt, beginn, "Neuer Stundenplan", 0);
	    	stundenplan.Ende = ende;
	    	conn.transactionPersist(stundenplan);
			final StundenplanListeEintrag daten = DataStundenplanListe.dtoMapper.apply(stundenplan);
			daten.schuljahr = abschnitt.Jahr;
			daten.abschnitt = abschnitt.Abschnitt;
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (final Exception exception) {
			if (exception instanceof IllegalArgumentException)
				return OperationError.NOT_FOUND.getResponse();
			if (exception instanceof final WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}


	/**
     * Entfernt einen Stundenplan mit der angegebenen ID.
	 *
     * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final long idStundenplan) {
		// Bestimme den Stundenplan zu der ID und lösche ihn, falls er vorhanden ist
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Ein Stundenplan mit der ID %d konnte nicht gefunden werden.".formatted(idStundenplan));
		conn.transactionRemove(stundenplan);
		return Response.status(Status.NO_CONTENT).type(MediaType.APPLICATION_JSON).build();
	}


}
