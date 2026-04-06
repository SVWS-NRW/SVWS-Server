package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvKurs;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvKurs}.
 */
public final class DataUvKurse extends DataManagerRevised<Long, DTOUvKurs, UvKurs> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link UvKurs}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvKurse(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idPlanungsabschnitt", "idSchuljahresabschnitt");
		super.setAttributesRequiredOnCreation("idPlanungsabschnitt", "idSchuljahresabschnitt", "idFach", "kursart", "kursnummer", "idSchuelergruppe");
	}

	/**
	 * Gibt die Daten eines UV-Kurses zur angegebenen ID zurück.
	 *
	 * @param id die ID des Kurses
	 * @return das Core-DTO des Kurses
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvKurs getById(final Long id) throws ApiOperationException {
		final DTOUvKurs dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Ermittelt das entsprechende {@link DTOUvKurs}-Objekt zur angegebenen ID.
	 *
	 * @param id die ID des Kurses
	 * @return das DTO-Objekt
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvKurs getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "ID darf nicht null sein.");
		}
		final DTOUvKurs dto = conn.queryByKey(DTOUvKurs.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Kein UV-Kurs mit ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvKurs dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvKurs map(final DTOUvKurs dto) {
		final UvKurs k = new UvKurs();
		k.id = dto.ID;
		k.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		k.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		k.idFach = dto.Fach_ID;
		k.kursart = dto.Kursart.kuerzel;
		k.kursnummer = dto.Kursnummer;
		k.idSchuelergruppe = dto.Schuelergruppe_ID;
		return k;
	}

	@Override
	protected void mapAttribute(final DTOUvKurs dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idPlanungsabschnitt" -> dto.Planungsabschnitt_ID = JSONMapper.convertToLong(value, false, name);
			case "idSchuljahresabschnitt" -> dto.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false, name);
			case "idFach" -> dto.Fach_ID = JSONMapper.convertToLong(value, false, name);
			case "kursart" -> dto.Kursart = GostKursart.fromKuerzelOrException(JSONMapper.convertToString(value, false, true,
					Schema.tab_UV_Kurse.col_Kursart.datenlaenge(), name));
			case "kursnummer" -> dto.Kursnummer = JSONMapper.convertToInteger(value, false, name);
			case "idSchuelergruppe" -> dto.Schuelergruppe_ID = JSONMapper.convertToLong(value, false, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Patch-Attribut '%s' wird nicht unterstützt.".formatted(name));
		}
	}
}
