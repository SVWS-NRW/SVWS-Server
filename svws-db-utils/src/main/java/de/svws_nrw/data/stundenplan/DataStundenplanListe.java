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
import de.svws_nrw.db.utils.ApiOperationException;
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
		daten.wochenTypModell = s.WochentypModell;
		daten.aktiv = s.Aktiv;
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
				: conn.queryList(DTOStundenplan.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOStundenplan.class, idSchuljahresabschnitt);
		if (plaene.isEmpty()) {
			return daten;
		}
		final List<Long> idsSchuljahresabschnitte = plaene.stream().map(p -> p.Schuljahresabschnitts_ID).distinct().toList();
		final Map<Long, DTOSchuljahresabschnitte> mapAbschnitte = conn.queryByKeyList(DTOSchuljahresabschnitte.class, idsSchuljahresabschnitte)
				.stream().collect(Collectors.toMap(a -> a.ID, a -> a));

		for (final DTOStundenplan s : plaene) {
			final DTOSchuljahresabschnitte a = mapAbschnitte.get(s.Schuljahresabschnitts_ID);
			final StundenplanListeEintrag e = dtoMapper.apply(s);
			e.schuljahr = a.Jahr;
			e.abschnitt = a.Abschnitt;
			if (e.gueltigBis == null) {
				e.gueltigBis = "%04d-%02d-%02d".formatted(a.Jahr + 1, 7, 31);
			}
			daten.add(e);
		}
		return daten.stream().sorted((a, b) -> {
			int cmp = Integer.compare(a.schuljahr, b.schuljahr);
			if (cmp != 0) {
				return cmp;
			}
			cmp = Integer.compare(a.abschnitt, b.abschnitt);
			if (cmp != 0) {
				return cmp;
			}
			cmp = a.gueltigAb.compareTo(b.gueltigAb);
			if (cmp != 0) {
				return cmp;
			}
			return a.gueltigBis.compareTo(b.gueltigBis);
		}).toList();
	}

	/**
	 * Gibt die Liste der aktiven Stundenpläne für einen oder alle Schuljahresabschnitte zurück.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param idSchuljahresabschnitt   die ID des schuljahresabschnitts oder null für alle
	 *
	 * @return die Liste der aktiven Stundenpläne
	 */
	public static List<StundenplanListeEintrag> getStundenplaeneAktiv(final DBEntityManager conn, final Long idSchuljahresabschnitt) {
		final List<StundenplanListeEintrag> daten = getStundenplaene(conn, idSchuljahresabschnitt);
		return daten.stream().filter(e -> e.aktiv).toList();
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

	/**
	 * Gibt die Liste der aktiven Stundenpläne für einen speziellen Schuljahresabschnitt zurück.
	 *
	 * @param idSchuljahresabschnitt   die ID des schuljahresabschnitts
	 *
	 * @return die Liste der aktiven Stundenpläne
	 */
	public Response getAktive(final Long idSchuljahresabschnitt) {
		// Stundenpläne für einen speziellen Schuljahresabschnitt
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getStundenplaeneAktiv(conn, idSchuljahresabschnitt)).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Entfernt einen Stundenplan mit der angegebenen ID.
	 *
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final long idStundenplan) throws ApiOperationException {
		// Bestimme den Stundenplan zu der ID und lösche ihn, falls er vorhanden ist
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Ein Stundenplan mit der ID %d konnte nicht gefunden werden.".formatted(idStundenplan));
		}
		conn.transactionRemove(stundenplan);
		return Response.status(Status.NO_CONTENT).type(MediaType.APPLICATION_JSON).build();
	}


}
