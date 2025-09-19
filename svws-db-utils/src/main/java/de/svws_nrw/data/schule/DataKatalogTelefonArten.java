package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.TelefonArt;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerTelefon;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOTelefonArt;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link TelefonArt}.
 */
public final class DataKatalogTelefonArten extends DataManagerRevised<Long, DTOTelefonArt, TelefonArt> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link TelefonArt}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogTelefonArten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
		setAttributesNotPatchable("id");
	}

	@Override
	protected long getLongId(final DTOTelefonArt dto) {
		return dto.ID;
	}

	@Override
	public TelefonArt map(final DTOTelefonArt dto) {
		final TelefonArt daten = new TelefonArt();
		daten.id = dto.ID;
		daten.bezeichnung = (dto.Bezeichnung == null) ? "" : dto.Bezeichnung;
		daten.sortierung = (dto.Sortierung == null) ? 32000 : dto.Sortierung;
		daten.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		daten.anzahlTelefonnummern = 0;
		return daten;
	}

	/**
	 * Konvertiert ein DTOTelefonArt-Objekt in ein TelefonArt-Objekt und setzt die Anzahl der Telefonnummern.
	 *
	 * @param dtoTelefonArt Das DTOTelefonArt-Objekt, das konvertiert werden soll.
	 * @param anzahlTelefonnummern Die Anzahl der Telefonnummern, die gesetzt werden sollen.
	 *
	 * @return Ein TelefonArt-Objekt, das aus dem DTOTelefonArt-Objekt konvertiert und mit der Anzahl der Telefonnummern gesetzt wurde.
	 */
	public TelefonArt map(final DTOTelefonArt dtoTelefonArt, final int anzahlTelefonnummern) {
		final TelefonArt telefonart = map(dtoTelefonArt);
		telefonart.anzahlTelefonnummern = anzahlTelefonnummern;
		return telefonart;
	}

	@Override
	public List<TelefonArt> getAll() throws ApiOperationException {
		final List<DTOTelefonArt> telefonarten = conn.queryAll(DTOTelefonArt.class);
		final Map<Long, Long> telefonartCountById =
				conn.queryList(DTOSchuelerTelefon.QUERY_ALL.concat("  WHERE e.TelefonArt_ID IS NOT NULL"), DTOSchuelerTelefon.class).stream()
						.collect(Collectors.groupingBy(t -> t.TelefonArt_ID, Collectors.counting()));

		return telefonarten.stream().map(t -> map(t, telefonartCountById.getOrDefault(t.ID, 0L).intValue())).toList();
	}

	@Override
	public TelefonArt getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einer Telefonart mit der ID null ist unzulässig.");

		final DTOTelefonArt telefonart = conn.queryByKey(DTOTelefonArt.class, id);
		if (telefonart == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Die Telefonart mit der ID %d wurde nicht gefunden.".formatted(id));

		final int anzahlTelefonnummern =
				conn.queryList(DTOSchuelerTelefon.QUERY_BY_TELEFONART_ID, DTOSchuelerTelefon.class, telefonart.ID).size();
		return map(telefonart, anzahlTelefonnummern);
	}

	@Override
	protected void initDTO(final DTOTelefonArt dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
	}

	@Override
	protected void mapAttribute(final DTOTelefonArt dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
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
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void validateBezeichnung(final DTOTelefonArt dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_TelefonArt.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOTelefonArt.class).stream()
				.anyMatch(t -> (t.ID != dto.ID) && t.Bezeichnung.equalsIgnoreCase(bezeichnung));

		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.Bezeichnung = bezeichnung;
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOTelefonArt> dtos, final Map<Long, SimpleOperationResponse> responsesById) {
		final List<Long> ids = dtos.stream().map(d -> d.ID).toList();
		final Map<Long, Long> telefonartenCountById = conn.queryList(
				"SELECT t.TelefonArt_ID, COUNT(t) FROM DTOSchuelerTelefon t "
						+ "WHERE t.TelefonArt_ID IN ?1 GROUP BY t.TelefonArt_ID", Object[].class, ids).stream()
				.collect(Collectors.toMap(id -> (Long) id[0], count -> (Long) count[1]));
		for (final DTOTelefonArt dto : dtos) {
			final SimpleOperationResponse response = responsesById.getOrDefault(dto.ID, null);
			if (response == null)
				throw new DeveloperNotificationException("Das SimpleOperationResponse Objekt zu der ID %d existiert nicht.".formatted(dto.ID));

			final long count = telefonartenCountById.getOrDefault(dto.ID, 0L);
			if (count > 0) {
				response.success = false;
				response.log.add("Telefonart %s (ID: %d) hat noch %d verknüpfte SchülerTelefoneinträge.".formatted(dto.Bezeichnung, dto.ID, count));
			}
		}
	}

}
