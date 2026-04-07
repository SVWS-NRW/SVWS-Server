package de.svws_nrw.data.klassen;

import java.util.List;
import java.util.Objects;

import de.svws_nrw.asd.data.klassen.KlassenListeEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dieser DataManager stellt Funktionalität zum Laden von {@link KlassenListeEintrag} Objekten bereit.
 */
public final class DataKlassenliste extends DataManagerRevised<Long, DTOKlassen, KlassenListeEintrag> {

	/**
	 * Default Konstruktor
	 *
	 * @param conn DBEntityManager
	 */
	public DataKlassenliste(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Methode liefert eine Liste von {@link KlassenListeEintrag} zur angegebenen SchuljahresabschnittID.
	 *
	 * @param schuljahresabschnittId ID des Schuljahresabschnittes
	 *
	 * @return Liste von KlassenListeEintrag Objekten
	 */
	public Response getListBySchuljahresabschnittIDAsResponse(final Long schuljahresabschnittId) {
		final List<DTOKlassen> klassen = getDTOsBySchuljahresabschnittId(schuljahresabschnittId);
		final List<KlassenListeEintrag> klassenliste = klassen.stream()
				.map(this::map)
				.toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(klassenliste).build();
	}

	@Override
	protected KlassenListeEintrag map(final DTOKlassen dto) {
		final KlassenListeEintrag klassenListeEintrag = new KlassenListeEintrag();
		klassenListeEintrag.id = dto.ID;
		klassenListeEintrag.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		klassenListeEintrag.kuerzel = dto.Klasse;
		klassenListeEintrag.idJahrgang = dto.Jahrgang_ID;
		klassenListeEintrag.parallelitaet = getParallelitaet(dto);
		klassenListeEintrag.beschreibung = Objects.toString(dto.Bezeichnung, "");
		return klassenListeEintrag;
	}

	private List<DTOKlassen> getDTOsBySchuljahresabschnittId(final Long idSchuljahresabschnitt) {
		if (idSchuljahresabschnitt == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Schuljahresabschnitt darf nicht null sein.");
		}
		return conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt);
	}

	private static String getParallelitaet(final DTOKlassen klasse) {
		return ((klasse.ASDKlasse == null) || (klasse.ASDKlasse.length() < 3)) ? null : klasse.ASDKlasse.substring(2);
	}

}
