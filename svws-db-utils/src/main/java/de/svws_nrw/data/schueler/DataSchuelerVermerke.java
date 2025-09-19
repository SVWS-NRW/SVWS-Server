package de.svws_nrw.data.schueler;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.schueler.SchuelerVermerke;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerVermerke;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerVermerke}.
 */
public final class DataSchuelerVermerke extends DataManagerRevised<Long, DTOSchuelerVermerke, SchuelerVermerke> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerVermerke}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 *
	 */
	public DataSchuelerVermerke(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
	}


	@Override
	protected void initDTO(final DTOSchuelerVermerke dtoSchuelerVermerke, final Long idVermerk, final Map<String, Object> initAttributes) {
		dtoSchuelerVermerke.ID = idVermerk;
		dtoSchuelerVermerke.Bemerkung = "";
		dtoSchuelerVermerke.AngelegtVon = conn.getUser().getUsername();
		dtoSchuelerVermerke.Datum = String.valueOf(Date.valueOf(LocalDate.now()));
	}


	@Override
	public SchuelerVermerke map(final DTOSchuelerVermerke e) {
		final SchuelerVermerke vermerk = new SchuelerVermerke();
		vermerk.id = e.ID;
		vermerk.idSchueler = e.Schueler_ID;
		vermerk.idVermerkart = e.VermerkArt_ID;
		vermerk.datum = e.Datum;
		vermerk.bemerkung = e.Bemerkung;
		vermerk.angelegtVon = e.AngelegtVon;
		vermerk.geaendertVon = e.GeaendertVon;
		return vermerk;
	}


	@Override
	public List<SchuelerVermerke> getAll() throws ApiOperationException {
		final List<DTOSchuelerVermerke> vermerke = conn.queryAll(DTOSchuelerVermerke.class);
		return vermerke.stream().map(this::map).toList();
	}


	/**
	 * Gibt die Vermerke zum Schüler mit der angebebenen ID als Liste zurück
	 *
	 * @param idSchueler   die ID des Schuelers
	 *
	 * @return die Liste der Vermerke zum Schüler
	 *
	 */
	public List<SchuelerVermerke> getBySchuelerId(final Long idSchueler) {
		final List<DTOSchuelerVermerke> vermerke = conn.queryList(DTOSchuelerVermerke.QUERY_BY_SCHUELER_ID, DTOSchuelerVermerke.class, idSchueler);
		return vermerke.stream().map(this::map).toList();
	}


	/**
	 * Gibt die Vermerke des Schülers mit der angegebenen ID als Response zurück
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Response für die Liste der Schüler
	 *
	 */
	public Response getBySchuelerIdAsResponse(final Long idSchueler) {
		final List<SchuelerVermerke> daten = this.getBySchuelerId(idSchueler);
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt die Vermerke zu einer VermerkArt mit der angebebenen ID als Liste zurück
	 *
	 * @param idVermerkart   die ID der VermerkARt
	 *
	 * @return die Liste der Vermerke zur Vermerkart
	 *
	 *
	 */
	public List<SchuelerVermerke> getListByVermerkartId(final Long idVermerkart) {
		final List<DTOSchuelerVermerke> vermerke = conn.queryList(DTOSchuelerVermerke.QUERY_BY_VERMERKART_ID, DTOSchuelerVermerke.class, idVermerkart);
		return vermerke.stream().map(this::map).toList();
	}


	/**
	 * Gibt die Vermerke zu einer VermerkArt mit der angebebenen ID als Response mit einer Liste zurück
	 *
	 * @param idVermerkart   die ID der VermerkARt
	 *
	 * @return die Response für die List der Vermerke, passend zu einer Vermerkart
	 *
	 */
	public Response getListByVermerkartIdAsResponse(final Long idVermerkart) {
		final List<SchuelerVermerke> daten = this.getListByVermerkartId(idVermerkart);
		return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public SchuelerVermerke getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die erwartete ID zur Anfrage ist nicht vorhanden.");
		final DTOSchuelerVermerke schuelerVermerk = conn.queryByKey(DTOSchuelerVermerke.class, id);
		if (schuelerVermerk == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es existiert mit der ID kein Vermerk.");
		return map(schuelerVermerk);
	}

	@Override
	protected void mapAttribute(final DTOSchuelerVermerke dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die angegebene ID %d ist null oder stimmt nicht mit der ID %d im DTO überein."
							.formatted(patch_id, dto.ID));
			}
			case "idSchueler" -> dto.Schueler_ID = JSONMapper.convertToLong(value, false);
			case "bemerkung" -> {
				dto.Bemerkung = JSONMapper.convertToString(value, false, true, Schema.tab_SchuelerVermerke.col_Bemerkung.datenlaenge());
				dto.GeaendertVon = conn.getUser().getUsername();
				dto.Datum = String.valueOf(Date.valueOf(LocalDate.now()));
			}
			case "idVermerkart" -> {
				final long idVermerkart = JSONMapper.convertToLong(value, false);
				if (conn.queryByKey(DTOVermerkArt.class, idVermerkart) == null)
					throw new ApiOperationException(Status.CONFLICT);
				dto.VermerkArt_ID = idVermerkart;
				dto.GeaendertVon = conn.getUser().getUsername();
				dto.Datum = String.valueOf(Date.valueOf(LocalDate.now()));
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Attribut %s wird beim Patchen nicht unterstützt".formatted(name));
		}
	}
}

