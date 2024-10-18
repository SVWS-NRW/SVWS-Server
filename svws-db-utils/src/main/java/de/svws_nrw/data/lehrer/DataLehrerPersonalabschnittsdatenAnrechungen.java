package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.lehrer.LehrerAnrechnungsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
 */
public final class DataLehrerPersonalabschnittsdatenAnrechungen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonalabschnittsdatenAnrechungen(final DBEntityManager conn) {
		super(conn);
	}


	private static LehrerPersonalabschnittsdatenAnrechnungsstunden map(final int schuljahr, final DTOLehrerAnrechnungsstunde dto) throws ApiOperationException {
		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;
		final LehrerAnrechnungsgrund art = LehrerAnrechnungsgrund.data().getWertByKuerzel(dto.AnrechnungsgrundKrz);
		final LehrerAnrechnungsgrundKatalogEintrag artEintrag = (art == null) ? null : art.daten(schuljahr);
		if (artEintrag == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Das Kürzel für die Art des Anrechnungsgrundes, welches in der Datenbank gespeichert ist, ist im Schuljahr %d nicht gültig."
							.formatted(schuljahr));
		daten.idGrund = artEintrag.id;
		daten.anzahl = (dto.AnrechnungStd == null) ? 0.0 : dto.AnrechnungStd;
		return daten;
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt die Anrechungen für die Lehrerabschnittsdaten mit der angegebenen ID und und gibt diese zurück.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idSchuljahresabschnitt    die ID des Schuljahresabschnittes der Abschnittsdaten
	 * @param idLehrerabschnittsdaten   die ID der Lehrerabschnittsdaten
	 *
	 * @return die Liste mit den Anrechnungen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getByLehrerabschnittsdatenId(final DBEntityManager conn,
			final long idSchuljahresabschnitt, final long idLehrerabschnittsdaten) throws ApiOperationException {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(idSchuljahresabschnitt);
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> result = new ArrayList<>();
		// Bestimme die Anrechungen für die Lehrerabschnittsdaten
		final List<DTOLehrerAnrechnungsstunde> dtos = conn.queryList(DTOLehrerAnrechnungsstunde.QUERY_BY_ABSCHNITT_ID,
				DTOLehrerAnrechnungsstunde.class, idLehrerabschnittsdaten);
		if (dtos == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerAnrechnungsstunde dto : dtos)
			result.add(map(abschnitt.schuljahr, dto));
		return result;
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOLehrerAnrechnungsstunde dto = conn.queryByKey(DTOLehrerAnrechnungsstunde.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOLehrerAbschnittsdaten dtoAbschnitt = conn.queryByKey(DTOLehrerAbschnittsdaten.class, dto.Abschnitt_ID);
		if (dtoAbschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Lehrerabschnittdaten konnten für die ID %d nicht gefunden werden.".formatted(dto.Abschnitt_ID));
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoAbschnitt.Schuljahresabschnitts_ID);
		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = map(abschnitt.schuljahr, dto);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private final Map<String, DataBasicMapper<DTOLehrerAnrechnungsstunde>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final long patch_id = JSONMapper.convertToLong(value, false);
				if ((patch_id != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("idAbschnittsdaten", (conn, dto, value, map) -> {
				final long patch_id = JSONMapper.convertToLong(value, false);
				if (patch_id != dto.Abschnitt_ID)
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("idGrund", (conn, dto, value, map) -> {
				final long idGrund = JSONMapper.convertToLong(value, false);
				final LehrerAnrechnungsgrund grund = LehrerAnrechnungsgrund.data().getWertByID(idGrund);
				if (grund == null)
					throw new ApiOperationException(Status.NOT_FOUND);
				final DTOLehrerAbschnittsdaten dtoAbschnitt = conn.queryByKey(DTOLehrerAbschnittsdaten.class, dto.Abschnitt_ID);
				if (dtoAbschnitt == null)
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
							"Die Lehrer-Abschnittsdaten für den Abschnitt mit der ID %d konnten nicht geladen werden".formatted(dto.Abschnitt_ID));
				final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoAbschnitt.Schuljahresabschnitts_ID);
				final LehrerAnrechnungsgrundKatalogEintrag eintrag = grund.daten(abschnitt.schuljahr);
				if (eintrag == null)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Der Anrechnungsgrund mit der ID %d ist im Schuljahr %d nicht gültig.".formatted(idGrund, abschnitt.schuljahr));
				dto.AnrechnungsgrundKrz = eintrag.kuerzel;
			}),
			Map.entry("anzahl", (conn, dto, value, map) -> dto.AnrechnungStd = JSONMapper.convertToDouble(value, false)));

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOLehrerAnrechnungsstunde.class, patchMappings);
	}

}
