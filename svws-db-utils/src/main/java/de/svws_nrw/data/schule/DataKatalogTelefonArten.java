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
import de.svws_nrw.data.schueler.DataSchuelerTelefon;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerTelefon;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOTelefonArt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO {@link TelefonArt}.
 */
public final class DataKatalogTelefonArten extends DataManagerRevised<Long, DTOTelefonArt, TelefonArt> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link TelefonArt}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogTelefonArten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	public TelefonArt map(final DTOTelefonArt dtoTelefonArt) {
		final TelefonArt daten = new TelefonArt();
		daten.id = dtoTelefonArt.ID;
		daten.bezeichnung = (dtoTelefonArt.Bezeichnung == null) ? "" : dtoTelefonArt.Bezeichnung;
		daten.sortierung = dtoTelefonArt.Sortierung;
		daten.istSichtbar = (dtoTelefonArt.Sichtbar == null) || dtoTelefonArt.Sichtbar;
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
		final TelefonArt et = map(dtoTelefonArt);
		et.anzahlTelefonnummern = anzahlTelefonnummern;
		return et;
	}

	@Override
	public List<TelefonArt> getAll() throws ApiOperationException {
		final List<DTOTelefonArt> mapTelefonarten = conn.queryAll(DTOTelefonArt.class);
		final Map<Long, Long> mapSchuelerTelefone = conn.queryList(DTOSchuelerTelefon.QUERY_ALL.concat("  WHERE e.TelefonArt_ID IS NOT NULL"),
				DTOSchuelerTelefon.class).stream().collect(Collectors.groupingBy(t -> t.TelefonArt_ID, Collectors.counting()));
		return mapTelefonarten.stream().map(et -> {
			final int anzahlTelefone =  mapSchuelerTelefone.computeIfAbsent(et.ID, a -> 0L).intValue();
			return map(et, anzahlTelefone);
		}).toList();
	}

	@Override
	public TelefonArt getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einer Telefonart mit der ID null ist unzulässig.");
		final DTOTelefonArt telefonArt = conn.queryByKey(DTOTelefonArt.class, id);
		if (telefonArt == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Die Telefonart mit der ID %d wurde nicht gefunden.".formatted(id));
		final int anzahlTelefone = conn.queryList(DTOSchuelerTelefon.QUERY_BY_TELEFONART_ID.replace("SELECT e ", "SELECT COUNT(e) "),
				DTOSchuelerTelefon.class, telefonArt.ID).size();
		return map(telefonArt, anzahlTelefone);
	}

	@Override
	protected void initDTO(final DTOTelefonArt dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.Sichtbar = true;
		dto.Sortierung = 32000;
	}

	private String validateBezeichnung(final Object vlaue, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(vlaue, false, false, 2500, name);
		final List<DTOTelefonArt> bezeichnungenFiltered = conn.queryList(
				"SELECT e FROM DTOTelefonArt e WHERE e.Bezeichnung = ?1", DTOTelefonArt.class, bezeichnung);
		if (!bezeichnungenFiltered.isEmpty())
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die Bezeichnung '%s' wird bereits für eine andere Telefonart genutzt.".formatted(bezeichnung));
		return bezeichnung;
	}

	@Override
	protected void mapAttribute(final DTOTelefonArt dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Response.Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(id, dto.ID));
			}
			case "bezeichnung" -> dto.Bezeichnung = validateBezeichnung(value, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}

	@Override
	public Response deleteMultipleAsResponse(final List<Long> ids) {
		// Bestimme die Datenbank-DTOs der Telefonarten
		final List<DTOTelefonArt> telefonArten = this.conn.queryByKeyList(DTOTelefonArt.class, ids).stream().toList();
		// Prüfe, ob das Löschen der Telefonarten erlaubt ist
		final Map<Long, SimpleOperationResponse> mapResponses = telefonArten.stream()
				.collect(Collectors.toMap(r -> r.ID, this::checkDeletePreConditions));
		// Lösche die Telefonarten und gib den Erfolg in der Response zurück
		for (final DTOTelefonArt dtotelefonArt : telefonArten) {
			final SimpleOperationResponse operationResponse = mapResponses.get(dtotelefonArt.ID);
			if (operationResponse == null)
				throw new DeveloperNotificationException("Das SimpleOperationResponse Objekt zu der ID %d existiert nicht.".formatted(dtotelefonArt.ID));
			if (operationResponse.log.isEmpty())
				operationResponse.success = this.conn.transactionRemove(dtotelefonArt);
		}
		return Response.ok().entity(mapResponses.values()).build();
	}

	/**
	 * Diese Methode prüft, ob alle Vorbedingungen zum Löschen einer Telefonart erfüllt sind.
	 * Es wird eine {@link SimpleOperationResponse} zurückgegeben.
	 *
	 * @param dtoTelefonArt   das DTO der Telefonart, die gelöscht werden soll
	 *
	 * @return Liefert eine Response mit dem Log der Vorbedingungsprüfung zurück.
	 */
	private SimpleOperationResponse checkDeletePreConditions(final @NotNull DTOTelefonArt dtoTelefonArt) {
		final SimpleOperationResponse operationResponse = new SimpleOperationResponse();
		operationResponse.id = dtoTelefonArt.ID;

		// Kein Schüler darf Telefoneinträge dieser Telefonart haben
		final List<Long> schuelerTelefonIds = new DataSchuelerTelefon(conn, 1L).getIDsByTelefonArtId(dtoTelefonArt.ID);
		if (!schuelerTelefonIds.isEmpty())
			operationResponse.log.add("Telefonart %s (ID: %d) hat noch %d verknüpfte(n) Schlertelefoneinträge.".formatted(dtoTelefonArt.Bezeichnung,
					dtoTelefonArt.ID,	schuelerTelefonIds.size()));
		return operationResponse;
	}
}
