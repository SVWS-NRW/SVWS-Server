package de.svws_nrw.data.erzieher;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Erzieherart}.
 */
public final class DataErzieherarten extends DataManagerRevised<Long, DTOErzieherart, Erzieherart> {

	/**
	 * patching or deleting these entries is not aloud according to SchILDzentral
	 */
	static final Set<Long> IDS_OF_NON_PATCHABLE_ENTRIES = Set.of(1L, 2L, 3L, 4L, 5L);

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Erzieherart}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataErzieherarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
		setAttributesNotPatchable("id");
	}

	@Override
	protected long getLongId(final DTOErzieherart dto) {
		return dto.ID;
	}

	@Override
	public Erzieherart map(final DTOErzieherart dto) {
		final Erzieherart daten = new Erzieherart();
		daten.id = dto.ID;
		daten.bezeichnung = (dto.Bezeichnung == null) ? "" : dto.Bezeichnung;
		daten.sortierung = (dto.Sortierung == null) ? 32000 : dto.Sortierung;
		daten.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		daten.exportBez = (dto.ExportBez == null) ? "" : dto.ExportBez;
		daten.anzahlErziehungsberechtigte = 0;
		return daten;
	}

	/**
	 * Konvertiert ein DTOErzieherart-Objekt in ein Erzieherart-Objekt und setzt die Anzahl der Erziehungsberechtigte.
	 *
	 * @param dto Das DTOErzieherart-Objekt, das konvertiert werden soll.
	 * @param anzahlErziehungsberechtigte Die Anzahl der Erziehungsberechtigte, die gesetzt werden sollen.
	 *
	 * @return Ein Erzieherart-Objekt, das aus dem DTOErzieherart-Objekt konvertiert und mit der Anzahl der Erziehungsberechtigte gesetzt wurde.
	 */
	public Erzieherart map(final DTOErzieherart dto, final int anzahlErziehungsberechtigte) {
		final Erzieherart erzieherart = map(dto);
		erzieherart.anzahlErziehungsberechtigte = anzahlErziehungsberechtigte;
		return erzieherart;
	}

	@Override
	public List<Erzieherart> getAll() throws ApiOperationException {
		final List<DTOErzieherart> erzieherarten = conn.queryAll(DTOErzieherart.class);
		final Map<Long, Long> erzieherartCountById =
				conn.queryList(DTOSchuelerErzieherAdresse.QUERY_ALL.concat("  WHERE e.ErzieherArt_ID IS NOT NULL"), DTOSchuelerErzieherAdresse.class).stream()
						.collect(Collectors.groupingBy(t -> t.ErzieherArt_ID, Collectors.counting()));
		return erzieherarten.stream().map(e -> map(e, erzieherartCountById.getOrDefault(e.ID, 0L).intValue())).toList();
	}

	@Override
	public Erzieherart getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einer Erzieherart mit der ID null ist unzulässig.");

		final DTOErzieherart erzieherart = conn.queryByKey(DTOErzieherart.class, id);
		if (erzieherart == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Die Erzieherart mit der ID %d wurde nicht gefunden.".formatted(id));

		final int anzahlErziehungsberechtigte =
				conn.queryList(DTOSchuelerErzieherAdresse.QUERY_BY_ERZIEHERART_ID, DTOSchuelerErzieherAdresse.class, erzieherart.ID).size();
		return map(erzieherart, anzahlErziehungsberechtigte);
	}

	@Override
	protected void initDTO(final DTOErzieherart dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
	}

	@Override
	public void checkBeforePatch(final DTOErzieherart dto, final Map<String, Object> patchAttributes) throws ApiOperationException {
		if (IDS_OF_NON_PATCHABLE_ENTRIES.contains(dto.ID))
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Der Eintrag %s mit der id %d darf nicht verändert werden.".formatted(dto.Bezeichnung, dto.ID));
	}

	@Override
	protected void mapAttribute(final DTOErzieherart dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Response.Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "bezeichnung" -> validateBezeichnung(dto, value, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "exportBez" -> dto.ExportBez = JSONMapper.convertToString(value, false, false, Schema.tab_K_ErzieherArt.col_ExportBez.datenlaenge(), name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));

		}
	}

	private void validateBezeichnung(final DTOErzieherart dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(
				value, false, false, Schema.tab_K_ErzieherArt.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOErzieherart.class).stream()
				.anyMatch(e -> (e.ID != dto.ID) && e.Bezeichnung.equalsIgnoreCase(bezeichnung));

		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.Bezeichnung =  bezeichnung;
	}


	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOErzieherart> dtos, final Map<Long, SimpleOperationResponse> mapResponses) {
		final List<Long> ids = dtos.stream().map(d -> d.ID).toList();
		final Map<Long, Long> erziehungsberechtigteCountById = conn.queryList(
						"SELECT e.ErzieherArt_ID, COUNT(e) FROM DTOSchuelerErzieherAdresse e "
								+ "WHERE e.ErzieherArt_ID IN ?1 GROUP BY e.ErzieherArt_ID", Object[].class, ids).stream()
				.collect(Collectors.toMap(id -> (Long) id[0], count -> (Long) count[1]));
		for (final DTOErzieherart dto : dtos) {
			final SimpleOperationResponse response = mapResponses.getOrDefault(dto.ID, null);
			if (response == null)
				throw new DeveloperNotificationException("Das SimpleOperationResponse Objekt zu der ID %d existiert nicht.".formatted(dto.ID));

			if (IDS_OF_NON_PATCHABLE_ENTRIES.contains(dto.ID)) {
				response.success = false;
				response.log.add("Erzieherart %s (ID: %d) darf weder gelöscht noch verändert werden".formatted(dto.Bezeichnung, dto.ID));
				continue;
			}

			final long count = erziehungsberechtigteCountById.getOrDefault(dto.ID, 0L);
			if (count > 0) {
				response.success = false;
				response.log.add("Erzieherart %s (ID: %d) hat noch %d verknüpfte SchülerErziehereinträge.".formatted(dto.Bezeichnung, dto.ID, count));
			}
		}
	}

}
