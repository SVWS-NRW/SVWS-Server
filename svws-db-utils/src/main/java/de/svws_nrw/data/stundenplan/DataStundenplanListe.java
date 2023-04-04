package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
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
	private final Function<DTOStundenplan, StundenplanListeEintrag> dtoMapper = (final DTOStundenplan s) -> {
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
	 * @param idSchuljahresabschnitt    die ID des schuljahresabschnitts oder null für alle
	 *
	 * @return die Liste der Stundenpläne
	 */
	public List<StundenplanListeEintrag> getStundenplaene(final Long idSchuljahresabschnitt) {
		final Vector<StundenplanListeEintrag> daten = new Vector<>();
		final List<DTOStundenplan> plaene = (idSchuljahresabschnitt == null)
			? conn.queryAll(DTOStundenplan.class)
			: conn.queryNamed("DTOStundenplan.schuljahresabschnitts_id", idSchuljahresabschnitt, DTOStundenplan.class);
		if (plaene.size() == 0)
			//throw OperationError.NOT_FOUND.exception("Keine Stundenpläne gefunden.");
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
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getStundenplaene(null)).build();
	}


	@Override
	public Response get(final Long idSchuljahresabschnitt) {
		// Stundenpläne für einen speziellen Schuljahresabschnitt
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getStundenplaene(idSchuljahresabschnitt)).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


}
