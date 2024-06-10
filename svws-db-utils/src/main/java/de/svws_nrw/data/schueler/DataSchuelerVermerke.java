package de.svws_nrw.data.schueler;

import de.svws_nrw.core.data.schueler.SchuelerVermerke;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;

import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerVermerke;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerVermerke}.
 */
public final class DataSchuelerVermerke extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerVermerke}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerVermerke(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln des Vermerkes in eines Datenbank-DTOs {@link DTOSchuelerVermerke}
	 * in einen Core-DTO {@link SchuelerVermerke}.
	 */
	public static final DTOMapper<DTOSchuelerVermerke, SchuelerVermerke> dtoMapper = (final DTOSchuelerVermerke e) -> {
		final SchuelerVermerke vermerk = new SchuelerVermerke();
		vermerk.id = e.ID;
		vermerk.idSchueler = e.Schueler_ID;
		vermerk.idVermerkart = e.VermerkArt_ID;
		vermerk.datum = e.Datum;
		vermerk.bemerkung = e.Bemerkung;
		vermerk.angelegtVon = e.AngelegtVon;
		vermerk.geaendertVon = e.GeaendertVon;
		return vermerk;
	};

	/**
	 * Mapper Funktion zum Umwandeln des Vermerkes in eines Datenbank-DTOs {@link DTOSchuelerVermerke}
	 * in einen Core-DTO {@link SchuelerVermerke}.
	 */
	public static final Function<DTOSchuelerVermerke, SchuelerVermerke> mapDTO = (final DTOSchuelerVermerke e) -> {
		final SchuelerVermerke vermerk = new SchuelerVermerke();
		vermerk.id = e.ID;
		vermerk.idSchueler = e.Schueler_ID;
		vermerk.idVermerkart = e.VermerkArt_ID;
		vermerk.datum = e.Datum;
		vermerk.bemerkung = e.Bemerkung;
		vermerk.angelegtVon = e.AngelegtVon;
		vermerk.geaendertVon = e.GeaendertVon;
		return vermerk;

	};


	@Override
	public Response getAll() throws ApiOperationException {
		final List<DTOSchuelerVermerke> vermerke = conn.queryAll(DTOSchuelerVermerke.class);
		if (vermerke == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<SchuelerVermerke> daten = vermerke.stream().map(mapDTO).toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt ide Vermerke des Schülers mit der angegebenen ID als Response zurück
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response für die Liste der Schüler
	 */
	public Response getBySchuelerIDasResponse(final Long idSchueler) {
		List<SchuelerVermerke> daten = this.getBySchuelerID(idSchueler);
		if ((daten == null) || daten.isEmpty())
			daten = new ArrayList<>();
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt die Vermerke zum Schüler mit der angebebenen ID als Liste zurück
	 *
	 * @param idSchueler   die ID des Schuelers
	 *
	 * @return die Liste der Vermerke zum Schüler
	 */
	public List<SchuelerVermerke> getBySchuelerID(final Long idSchueler) {
		final List<DTOSchuelerVermerke> daten = conn.queryList(DTOSchuelerVermerke.QUERY_BY_SCHUELER_ID, DTOSchuelerVermerke.class, idSchueler);
		return daten.stream().map(mapDTO).toList();
	}


	/**
	 * Bestimmt die IDs der Vermerke, welche zu der übergebenen ID der Vermerkart gehören.
	 *
	 * @param conn   die zu verwendende Datenbankverbindung
	 * @param id     die ID der Vermerkart
	 *
	 * @return die List von Vermerken IDs, welche der entsprechenden Vermerkart zugeordnet sind
	 */
	public static List<Long> getIDsByVermerkart(final DBEntityManager conn, final Long id) {
		return conn.queryList(DTOSchuelerVermerke.QUERY_BY_VERMERKART_ID, DTOSchuelerVermerke.class, id)
			.stream().map(v -> v.Schueler_ID).toList();
	}


	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die erwartete ID zur Anfrage ist nicht vorhanden.");
		final DTOSchuelerVermerke schuelerVermerk = conn.queryByKey(DTOSchuelerVermerke.class, id);
		if (schuelerVermerk == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert mit der ID kein Vermerk.");
		final SchuelerVermerke daten = dtoMapper.apply(schuelerVermerk);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static final Set<String> requiredCreateAttributes = Set.of("idSchueler");


	private static final ObjLongConsumer<DTOSchuelerVermerke> initDTO = (dto, id) -> dto.ID = id;


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOSchuelerVermerke.class, patchMappings);
	}


	private static final Map<String, DataBasicMapper<DTOSchuelerVermerke>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (Long.compare(patch_id, dto.ID) != 0))
				throw new ApiOperationException(Status.BAD_REQUEST, "Die angegebene ID %d ist null oder stimmt nicht mit der ID %d im DTO überein.".formatted(patch_id, dto.ID));
		}),
		Map.entry("bemerkung", (conn, dto, value, map) -> {
			dto.Bemerkung = JSONMapper.convertToString(value, false, true, Schema.tab_SchuelerVermerke.col_Bemerkung.datenlaenge());
			dto.GeaendertVon = conn.getUser().getUsername();
			dto.Datum = String.valueOf(Date.valueOf(LocalDate.now()));
		}),
		Map.entry("idVermerkart", (conn, dto, value, map) -> {
			final long idVermerkart = JSONMapper.convertToLong(value, false);
			if (conn.queryByKey(DTOVermerkArt.class, idVermerkart) == null)
				throw new ApiOperationException(Status.CONFLICT);
			dto.VermerkArt_ID = idVermerkart;
			dto.GeaendertVon = conn.getUser().getUsername();
			dto.Datum = String.valueOf(Date.valueOf(LocalDate.now()));
		})
	);

	private static final Map<String, DataBasicMapper<DTOSchuelerVermerke>> addMappings = Map.ofEntries(
		Map.entry("idSchueler", (conn, dto, value, map) -> {
			dto.Schueler_ID = JSONMapper.convertToLong(value, false);
			dto.VermerkArt_ID = 1L;
			dto.Bemerkung = "";
			dto.AngelegtVon = conn.getUser().getUsername();
			dto.Datum = String.valueOf(Date.valueOf(LocalDate.now()));
		})
	);


	/**
	 * Erstellt einen neuen Schülervermerk
	 *
	 * @param is   		der InputStream mit den JSON-Daten.
	 *
	 * @return Eine Response mit dem neuen Schülerbetrieb
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {

		return super.addBasic(is, DTOSchuelerVermerke.class, initDTO, dtoMapper, requiredCreateAttributes, addMappings);
	}


	/**
	 * Löscht einen Vermerk
	 *
	 * @param idVermerk   die ID des Vermerks
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final Long idVermerk) throws ApiOperationException {
		// Erhalte Delete Kandidaten mit der übermittelten ID
		final DTOSchuelerVermerke deleteCandidate = conn.queryByKey(DTOSchuelerVermerke.class, idVermerk);
		final SchuelerVermerke daten = dtoMapper.apply(deleteCandidate);

		// Entferne den Vermerk
		conn.transactionRemove(deleteCandidate);

		return Response.ok().entity(daten).build();
	}
}

