package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvKlasse;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvKlasse}.
 */
public final class DataUvKlassen extends DataManagerRevised<Long, DTOUvKlasse, UvKlasse> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link UvKlasse}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvKlassen(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id, idPlanungsabschnitt, idSchuljahresabschnitt");
		super.setAttributesRequiredOnCreation("idPlanungsabschnitt", "idSchuljahresabschnitt", "kuerzel", "parallelitaet", "idSchuelergruppe");
	}

	@Override
	public UvKlasse getById(final Long id) throws ApiOperationException {
		final DTOUvKlasse dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Ermittelt das entsprechende {@link DTOUvKlasse}-Objekt zur angegebenen ID.
	 *
	 * @param id die ID der Klasse
	 * @return das DTO-Objekt
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvKlasse getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "ID darf nicht null sein.");
		}
		final DTOUvKlasse dto = conn.queryByKey(DTOUvKlasse.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Keine UV-Klasse mit ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvKlasse dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvKlasse map(final DTOUvKlasse dto) {
		final UvKlasse k = new UvKlasse();
		k.id = dto.ID;
		k.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		k.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		k.bezeichnung = dto.Bezeichnung;
		k.kuerzel = dto.Kuerzel;
		k.parallelitaet = dto.Parallelitaet;
		k.idStundentafel = dto.Stundentafel_ID;
		k.idSchuelergruppe = dto.Schuelergruppe_ID;
		k.orgFormKrz = dto.OrgFormKrz;
		k.idFachklasse = dto.Fachklasse_ID;
		k.asdSchulformNr = dto.ASDSchulformNr;
		return k;
	}

	@Override
	protected void mapAttribute(final DTOUvKlasse dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idPlanungsabschnitt" -> dto.Planungsabschnitt_ID = JSONMapper.convertToLong(value, false, name);
			case "idSchuljahresabschnitt" -> dto.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false, name);
			case "bezeichnung" -> dto.Bezeichnung = JSONMapper.convertToString(value, true, false,
					Schema.tab_UV_Klassen.col_Bezeichnung.datenlaenge(), name);
			case "kuerzel" -> dto.Kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_UV_Klassen.col_Kuerzel.datenlaenge(), name);
			case "parallelitaet" -> dto.Parallelitaet = JSONMapper.convertToString(value, false, false, Schema.tab_UV_Klassen.col_Parallelitaet.datenlaenge(),
					name);
			case "idStundentafel" -> dto.Stundentafel_ID = JSONMapper.convertToLong(value, true, name);
			case "idSchuelergruppe" -> dto.Schuelergruppe_ID = JSONMapper.convertToLong(value, false, name);
			case "orgFormKrz" -> dto.OrgFormKrz = JSONMapper.convertToString(value, true, false, Schema.tab_UV_Klassen.col_OrgFormKrz.datenlaenge(), name);
			case "idFachklasse" -> dto.Fachklasse_ID = JSONMapper.convertToLong(value, true, name);
			case "asdSchulformNr" -> dto.ASDSchulformNr = JSONMapper.convertToString(value, true, false,
					Schema.tab_UV_Klassen.col_ASDSchulformNr.datenlaenge(), name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Patch-Attribut '%s' wird nicht unterstützt.".formatted(name));
		}
	}
}
