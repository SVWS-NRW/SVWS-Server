package de.svws_nrw.data.erzieher;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.erzieher.Erzieherart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOErzieherart;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link Erzieherart}.
 */
public final class DataErzieherarten extends DataManagerRevised<Long, DTOErzieherart, Erzieherart> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Erzieherart}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataErzieherarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	public Erzieherart map(final DTOErzieherart dto) {
		final Erzieherart daten = new Erzieherart();
		daten.id = dto.ID;
		daten.bezeichnung = (dto.Bezeichnung == null) ? "" : dto.Bezeichnung;
		daten.sortierung = dto.Sortierung;
		daten.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		daten.exportBez = (dto.ExportBez == null) ? "" : dto.ExportBez;
		return daten;
	}

	@Override
	public List<Erzieherart> getAll() throws ApiOperationException {
		final List<DTOErzieherart> mapErzieherart = conn.queryAll(DTOErzieherart.class);
		return mapErzieherart.stream().map(this::map).toList();
	}

	@Override
	public Erzieherart getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einer Erzieherart mit der ID null ist unzulässig.");
		final DTOErzieherart erzieherart = conn.queryByKey(DTOErzieherart.class, id);
		if (erzieherart == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Die Erzieherart mit der ID %d wurde nicht gefunden.".formatted(id));
		return map(erzieherart);
	}

	@Override
	protected void initDTO(final DTOErzieherart dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
	}

	@Override
	protected void mapAttribute(final DTOErzieherart dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Response.Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(id, dto.ID));
			}
			case "bezeichnung" -> dto.Bezeichnung = validateBezeichnung(value, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "exportBez" -> dto.ExportBez = JSONMapper.convertToString(value, false, false, Schema.tab_K_ErzieherArt.col_ExportBez.datenlaenge(), name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}

	private String validateBezeichnung(final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_ErzieherArt.col_Bezeichnung.datenlaenge(), name);
		final List<DTOErzieherart> bezeichnungenFiltered = conn.queryList(
				"SELECT e FROM DTOErzieherart e WHERE e.Bezeichnung = ?1", DTOErzieherart.class, bezeichnung);
		if (!bezeichnungenFiltered.isEmpty())
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die Bezeichnung '%s' wird bereits für eine andere Erzieherart genutzt.".formatted(bezeichnung));
		return bezeichnung;
	}

	@Override
	public Response deleteMultipleAsResponse(final List<Long> ids) {
		final List<SimpleOperationResponse> responses =
				conn.queryByKeyList(DTOErzieherart.class, ids)
						.stream()
						.map(dto -> {
							final boolean success = conn.transactionRemove(dto);
							final SimpleOperationResponse r = new SimpleOperationResponse();
							r.id = dto.ID;
							r.success = success;
							return r;
						}).toList();
		return Response.ok(responses).build();
	}


}
