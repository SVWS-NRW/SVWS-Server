package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link AnkreuzkompetenzJahrgangszuordnung}.
 */
public final class DataAnkreuzkompetenzJahrgangszuordnungen extends DataManagerRevised<Long, DTOAnkreuzkompetenzJahrgang, AnkreuzkompetenzJahrgangszuordnung> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link AnkreuzkompetenzJahrgangszuordnung}.
	 *
	 * @param conn   die Datenbankverbindung
	 */
	public DataAnkreuzkompetenzJahrgangszuordnungen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("idAnkreuzkompetenz", "idJahrgang");
	}

	@Override
	protected void initDTO(final DTOAnkreuzkompetenzJahrgang dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.id = newID;
	}

	@Override
	public AnkreuzkompetenzJahrgangszuordnung getById(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Zuordnung darf nicht null sein.");
		}

		final DTOAnkreuzkompetenzJahrgang zuordnung = this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, id);
		if (zuordnung == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Zuordnung mit der ID %d wurde nicht gefunden.".formatted(id));
		}

		return map(zuordnung);
	}

	@Override
	protected AnkreuzkompetenzJahrgangszuordnung map(final DTOAnkreuzkompetenzJahrgang dto) {
		final AnkreuzkompetenzJahrgangszuordnung ankreuzkompetenzJahrgangszuordnung = new AnkreuzkompetenzJahrgangszuordnung();
		ankreuzkompetenzJahrgangszuordnung.id = dto.id;
		ankreuzkompetenzJahrgangszuordnung.idAnkreuzkompetenz = dto.idAnkreuzkompetenz;
		ankreuzkompetenzJahrgangszuordnung.idJahrgang = dto.idJahrgang;
		return ankreuzkompetenzJahrgangszuordnung;
	}

	@Override
	public List<AnkreuzkompetenzJahrgangszuordnung> getAll() {
		return this.conn.queryAll(DTOAnkreuzkompetenzJahrgang.class)
				.stream()
				.map(this::map)
				.toList();
	}

	@Override
	protected void mapAttribute(final DTOAnkreuzkompetenzJahrgang dto, final String name, final Object value, final Map<String, Object> map) {
		switch (name) {
			case "id" -> ValidationUtils.validateId(dto.id, name, value);
			case "idAnkreuzkompetenz" -> updateIdAnkreuzkompetenz(dto, value, name);
			case "idJahrgang" -> updateIdJahrgang(dto, name, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateIdAnkreuzkompetenz(final DTOAnkreuzkompetenzJahrgang dto, final Object value, final String name) {
		final Long idAnkreuzkompetenz = JSONMapper.convertToLong(value, false, name);
		final DTOAnkreuzfloskeln ankreuzkompetenz = this.conn.queryByKey(DTOAnkreuzfloskeln.class, idAnkreuzkompetenz);
		if (ankreuzkompetenz == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für die ID %d wurde keine Ankreuzkompetenz gefunden.".formatted(idAnkreuzkompetenz));
		}
		dto.idAnkreuzkompetenz = idAnkreuzkompetenz;
	}

	private void updateIdJahrgang(final DTOAnkreuzkompetenzJahrgang dto, final String name, final Object value) {
		final Long idJahrgang = JSONMapper.convertToLong(value, false, name);
		final DTOJahrgang jahrgang = this.conn.queryByKey(DTOJahrgang.class, idJahrgang);
		if (jahrgang == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für die ID %d wurde kein Jahrgang gefunden.".formatted(idJahrgang));
		}
		dto.idJahrgang = idJahrgang;
	}
}
