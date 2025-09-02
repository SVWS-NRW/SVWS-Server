package de.svws_nrw.data.erzieher;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.erzieher.Erzieherart;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOErzieherart;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
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
		daten.anzahlErziehungsberechtigte = 0;
		return daten;
	}

	/**
	 * Konvertiert ein DTOErzieherart-Objekt in ein Erzieherart-Objekt und setzt die Anzahl der Erziehungsberechtigte.
	 *
	 * @param dtoErzieherart Das DTOErzieherart-Objekt, das konvertiert werden soll.
	 * @param anzahlErziehungsberechtigte Die Anzahl der Erziehungsberechtigte, die gesetzt werden sollen.
	 *
	 * @return Ein Erzieherart-Objekt, das aus dem DTOErzieherart-Objekt konvertiert und mit der Anzahl der Erziehungsberechtigte gesetzt wurde.
	 */
	public Erzieherart map(final DTOErzieherart dtoErzieherart, final int anzahlErziehungsberechtigte) {
		final Erzieherart et = map(dtoErzieherart);
		et.anzahlErziehungsberechtigte = anzahlErziehungsberechtigte;
		return et;
	}

	@Override
	public List<Erzieherart> getAll() throws ApiOperationException {
		final List<DTOErzieherart> listErzieherart = conn.queryAll(DTOErzieherart.class);
		final Map<Long, Long> mapSchuelerErz = conn.queryList(DTOSchuelerErzieherAdresse.QUERY_ALL.concat("  WHERE e.ErzieherArt_ID IS NOT NULL"),
				DTOSchuelerErzieherAdresse.class).stream().collect(Collectors.groupingBy(t -> t.ErzieherArt_ID, Collectors.counting()));
		return listErzieherart.stream()
				.map(et -> map(et, mapSchuelerErz.getOrDefault(et.ID, 0L).intValue()))
				.toList();
	}

	@Override
	public Erzieherart getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einer Erzieherart mit der ID null ist unzulässig.");
		final DTOErzieherart erzieherart = conn.queryByKey(DTOErzieherart.class, id);
		if (erzieherart == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Die Erzieherart mit der ID %d wurde nicht gefunden.".formatted(id));
		final int anzahlErz = conn.queryList(DTOSchuelerErzieherAdresse.QUERY_BY_ERZIEHERART_ID.replace("SELECT e ", "SELECT COUNT(e) "),
				DTOSchuelerErzieherAdresse.class, erzieherart.ID).size();
		return map(erzieherart, anzahlErz);
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
	protected long getLongId(final DTOErzieherart erzieherart) {
		return erzieherart.ID;
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOErzieherart> dtos, final Map<Long, SimpleOperationResponse> mapResponses) {

		for (final DTOErzieherart dtoErzieherart : dtos) {
			final SimpleOperationResponse response = mapResponses.get(dtoErzieherart.ID);
			if (response == null)
				throw new DeveloperNotificationException("Das SimpleOperationResponse Objekt zu der ID %d existiert nicht.".formatted(dtoErzieherart.ID));
			final List<Long> schuelerErziehungsberchtigteIds = new DataErzieherStammdaten(conn, 1L).getIDsByErzieherartID(dtoErzieherart.ID);

			if (!schuelerErziehungsberchtigteIds.isEmpty()) {
				response.success = false;
				response.log.add("Erzieherart %s (ID: %d) hat noch %d verknüpfte(n) SchülerErziehereinträge.".formatted(
						dtoErzieherart.Bezeichnung, dtoErzieherart.ID, schuelerErziehungsberchtigteIds.size()
				));
			}
		}
	}

}
