package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link AbteilungKlassenzuordnung}.
 */
public final class DataAbteilungenKlassenzuordnungen extends DataManagerRevised<Long, DTOAbteilungsKlassen, AbteilungKlassenzuordnung> {
	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link AbteilungKlassenzuordnung}.
	 *
	 * @param conn          die Datenbankverbindung
	 */
	public DataAbteilungenKlassenzuordnungen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("idAbteilung", "idKlasse");
	}

	@Override
	protected void initDTO(final DTOAbteilungsKlassen dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	public AbteilungKlassenzuordnung getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Zuordnung darf nicht null sein.");
		}
		final DTOAbteilungsKlassen zuordnung = this.conn.queryByKey(DTOAbteilungsKlassen.class, id);
		if (zuordnung == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Zuordnung mit der ID %d wurde nicht gefunden.".formatted(id));
		}
		return map(zuordnung);
	}

	@Override
	public AbteilungKlassenzuordnung map(final DTOAbteilungsKlassen dto) {
		final AbteilungKlassenzuordnung abteilungKlassenzuordnung = new AbteilungKlassenzuordnung();
		abteilungKlassenzuordnung.id = dto.ID;
		abteilungKlassenzuordnung.idAbteilung = dto.Abteilung_ID;
		abteilungKlassenzuordnung.idKlasse = dto.Klassen_ID;
		return abteilungKlassenzuordnung;
	}

	@Override
	public List<AbteilungKlassenzuordnung> getAll() {
		return this.conn.queryAll(DTOAbteilungsKlassen.class)
				.stream()
				.map(this::map)
				.toList();
	}

	@Override
	protected void mapAttribute(final DTOAbteilungsKlassen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "idAbteilung" -> updateIdAbteilung(dto, value, name);
			case "idKlasse" -> updateIdKlasse(dto, name, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOAbteilungsKlassen dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (id != dto.ID) {
			throw new ApiOperationException(
					Status.BAD_REQUEST, "Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void updateIdAbteilung(final DTOAbteilungsKlassen dto, final Object value, final String name) throws ApiOperationException {
		final Long idAbteilung = JSONMapper.convertToLong(value, false, name);
		final DTOAbteilungen abteilung = this.conn.queryByKey(DTOAbteilungen.class, idAbteilung);
		if (abteilung == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für die ID %d wurde keine Abteilung gefunden.".formatted(idAbteilung));
		}
		dto.Abteilung_ID = idAbteilung;
	}

	private void updateIdKlasse(final DTOAbteilungsKlassen dto, final String name, final Object value) throws ApiOperationException {
		final Long idKlasse = JSONMapper.convertToLong(value, false, name);
		final DTOKlassen klasse = this.conn.queryByKey(DTOKlassen.class, idKlasse);
		if (klasse == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für die ID %d wurde keine Klasse gefunden.".formatted(idKlasse));
		}
		dto.Klassen_ID = idKlasse;
	}

}
