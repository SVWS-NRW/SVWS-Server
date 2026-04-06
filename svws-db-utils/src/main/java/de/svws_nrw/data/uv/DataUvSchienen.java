package de.svws_nrw.data.uv;

import java.util.Map;

import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvSchiene;
import de.svws_nrw.db.dto.current.uv.DTOUvSchienenConstraintJahrgang;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link UvSchiene}.
 */
public final class DataUvSchienen extends DataManagerRevised<Long, DTOUvSchiene, UvSchiene> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link UvSchiene}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataUvSchienen(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idPlanungsabschnitt");
		super.setAttributesRequiredOnCreation("idPlanungsabschnitt", "nummer");
	}

	/**
	 * Gibt die Daten einer {@link UvSchiene} zu deren ID zurück.
	 *
	 * @param id die ID der {@link UvSchiene}
	 * @return die Daten der {@link UvSchiene} zur ID
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public UvSchiene getById(final Long id) throws ApiOperationException {
		final DTOUvSchiene dto = getDTO(id);
		return map(dto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOUvSchiene}-Objekt zur angegebenen ID.
	 *
	 * @param id die ID des {@link DTOUvSchiene}-Objekts
	 * @return ein {@link DTOUvSchiene}-Objekt
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOUvSchiene getDTO(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die UV-Schiene darf nicht null sein.");
		}
		final DTOUvSchiene dto = conn.queryByKey(DTOUvSchiene.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schiene zur ID " + id + " gefunden.");
		}
		return dto;
	}

	@Override
	protected void initDTO(final DTOUvSchiene dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected UvSchiene map(final DTOUvSchiene dto) throws ApiOperationException {
		final UvSchiene daten = new UvSchiene();
		daten.id = dto.ID;
		daten.idPlanungsabschnitt = dto.Planungsabschnitt_ID;
		daten.nummer = dto.Nummer;
		daten.bezeichnung = dto.Bezeichnung;
		daten.idsJahrgaengeErlaubt = conn.queryList(DTOUvSchienenConstraintJahrgang.QUERY_BY_SCHIENE_ID,
						DTOUvSchienenConstraintJahrgang.class,	daten.id)
				.stream()
				.map(s -> s.Jahrgang_ID)
				.toList();
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOUvSchiene dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idPlanungsabschnitt" -> dto.Planungsabschnitt_ID = JSONMapper.convertToLong(value, false, name);
			case "nummer" -> dto.Nummer = JSONMapper.convertToInteger(value, false, name);
			case "bezeichnung" -> dto.Bezeichnung = JSONMapper.convertToString(value, false, true,
					Schema.tab_UV_Schienen.col_Bezeichnung.datenlaenge(), name);
			case "idsJahrgaengeErlaubt" -> DataUvSchuelergruppen.updateJoinTable(
					conn,
					"Schiene_ID",
					dto.ID,
					value,
					name,
					"DTOUvSchienenConstraintJahrgang",
					"Jahrgang_ID",
					DTOUvSchienenConstraintJahrgang.class,
					DTOUvSchienenConstraintJahrgang.QUERY_BY_SCHIENE_ID,
					s -> s.Jahrgang_ID,
					id -> new DTOUvSchienenConstraintJahrgang(dto.ID, id, dto.Planungsabschnitt_ID)
			);
			default -> throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}
}
