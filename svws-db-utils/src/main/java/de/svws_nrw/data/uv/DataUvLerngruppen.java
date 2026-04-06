package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppe;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchiene;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvLerngruppe}.
 */
public final class DataUvLerngruppen extends DataManagerRevised<Long, DTOUvLerngruppe, UvLerngruppe> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link UvLerngruppe}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvLerngruppen(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idPlanungsabschnitt");
		super.setAttributesRequiredOnCreation("idPlanungsabschnitt", "wochenstunden", "wochenstundenUnterrichtet");
	}

	/**
	 * Gibt die Daten einer {@link UvLerngruppe} zu deren ID zurück.
	 *
	 * @param id die ID der {@link UvLerngruppe}
	 * @return die Daten der {@link UvLerngruppe} zur ID
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvLerngruppe getById(final Long id) throws ApiOperationException {
		final DTOUvLerngruppe dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOUvLerngruppe}-Objekt zur angegebenen ID.
	 *
	 * @param id die ID des {@link DTOUvLerngruppe}-Objekts
	 * @return ein {@link DTOUvLerngruppe}-Objekt
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvLerngruppe getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die UV-Lerngruppe darf nicht null sein.");
		}
		final DTOUvLerngruppe dto = conn.queryByKey(DTOUvLerngruppe.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lerngruppe zur ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvLerngruppe dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvLerngruppe map(final DTOUvLerngruppe dto) throws ApiOperationException {
		final UvLerngruppe daten = new UvLerngruppe();
		daten.id = dto.ID;
		daten.idKlasse = dto.Klasse_ID;
		daten.idFach = dto.Fach_ID;
		daten.idKurs = dto.Kurs_ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.wochenstunden = dto.Wochenstunden;
		daten.wochenstundenUnterrichtet = dto.WochenstundenUnterrichtet;
		daten.koopSchulNr = dto.KoopSchulNr;
		daten.koopAnzahlExterne = dto.KoopAnzahlExterne;
		daten.idsSchienen = conn.queryList(DTOUvLerngruppeSchiene.QUERY_BY_LERNGRUPPE_ID,
				DTOUvLerngruppeSchiene.class, daten.id)
				.stream()
				.map(s -> s.Schiene_ID)
				.toList();
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvLerngruppe dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idKlasse" -> dto.Klasse_ID = JSONMapper.convertToLong(value, true, name);
			case "idFach" -> dto.Fach_ID = JSONMapper.convertToLong(value, true, name);
			case "idKurs" -> dto.Kurs_ID = JSONMapper.convertToLong(value, true, name);
			case "idPlanungsabschnitt" -> dto.Planungsabschnitt_ID = JSONMapper.convertToLong(value, false, name);
			case "wochenstunden" -> dto.Wochenstunden = JSONMapper.convertToDouble(value, false, name);
			case "wochenstundenUnterrichtet" -> dto.WochenstundenUnterrichtet = JSONMapper.convertToDouble(value, false, name);
			case "koopSchulNr" -> dto.KoopSchulNr = JSONMapper.convertToString(value, true, false,
					Schema.tab_UV_Lerngruppen.col_KoopSchulNr.datenlaenge(), name);
			case "koopAnzahlExterne" -> dto.KoopAnzahlExterne = JSONMapper.convertToInteger(value, false, name);
			case "idsSchienen" -> DataUvSchuelergruppen.updateJoinTable(
					conn,
					"Lerngruppe_ID",
					dto.ID,
					value,
					name,
					"DTOUvLerngruppeSchiene",
					"Schiene_ID",
					DTOUvLerngruppeSchiene.class,
					DTOUvLerngruppeSchiene.QUERY_BY_LERNGRUPPE_ID,
					s -> s.Schiene_ID,
					id -> new DTOUvLerngruppeSchiene(dto.ID, id, dto.Planungsabschnitt_ID)
			);
			default -> throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	/**
	 * Methode prüft vor dem Persistieren eines Datenbank-DTOs, ob alle Vorbedingungen zum Patch erfüllt sind.
	 * Standardmäßig hat diese Methode keine Implementierung.
	 * Wenn eine Prüfung durchgeführt werden soll, muss diese Methode überschrieben werden.
	 *
	 * @param dto               das DTO
	 * @param patchedAttributes   die Map mit den gepatchenden Attributen für das DTO
	 *
	 * @throws ApiOperationException wird geworfen, wenn eine Vorbedingung nicht erfüllt wurde
	 */
	@Override
	public void checkBeforePersist(final DTOUvLerngruppe dto, final Map<String, Object> patchedAttributes) throws ApiOperationException {
		if ((dto.Kurs_ID != null) && ((dto.Klasse_ID != null) || (dto.Fach_ID != null))) {
			throw new ApiOperationException(Status.CONFLICT,
					"Eine Lerngruppe darf entweder einer Klasse und einem Fach zugeordnet sein oder einem Kurs, aber nicht beidem.");
		}
	}
}
