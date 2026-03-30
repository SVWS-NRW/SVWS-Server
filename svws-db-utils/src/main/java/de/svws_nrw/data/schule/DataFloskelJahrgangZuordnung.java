package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.schule.FloskelJahrgangZuordnung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelnJahrgaenge;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link FloskelJahrgangZuordnung}.
 */
public final class DataFloskelJahrgangZuordnung extends DataManagerRevised<Long, DTOFloskelnJahrgaenge, FloskelJahrgangZuordnung> {

	private static final String ID_JAHRGANG = "idJahrgang";
	private static final String ID_FLOSKEL = "idFloskel";

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataFloskelJahrgangZuordnung(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation(ID_FLOSKEL, ID_JAHRGANG);
		setAttributesNotPatchable("id", ID_FLOSKEL, ID_JAHRGANG);
	}

	@Override
	protected void initDTO(final DTOFloskelnJahrgaenge dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		ensureFloskelAndJahrgangCombinationIsUnique(initAttributes);
	}

	@Override
	public FloskelJahrgangZuordnung getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID für die Floskel-Jahrgangs-Zuordnung darf nicht null sein.");
		}

		final DTOFloskelnJahrgaenge dto = this.conn.queryByKey(DTOFloskelnJahrgaenge.class, id);
		if (dto == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde keine Floskel-Jahrgangs-Zuordnung mit der ID %d gefunden.".formatted(id));
		}

		return map(dto);
	}

	@Override
	public List<FloskelJahrgangZuordnung> getAll() {
		return this.conn.queryAll(DTOFloskelnJahrgaenge.class).stream()
				.map(this::map)
				.toList();
	}

	/**
	 * Gibt die Liste der Floskel-Jahrgangs_Zuordnungen gefiltert nach der FloskelID zurück
	 *
	 * @param idFloskel		id der Floskel
	 *
	 * @return				Liste der Floskel-Jahrgangs_Zuordnungen gefiltert nach der FloskelID
	 */
	public List<FloskelJahrgangZuordnung> getListByIdFloskel(final Long idFloskel) {
		return this.conn.queryList(DTOFloskelnJahrgaenge.QUERY_BY_FLOSKEL_ID, DTOFloskelnJahrgaenge.class, idFloskel).stream()
				.map(this::map)
				.toList();
	}

	@Override
	protected FloskelJahrgangZuordnung map(final DTOFloskelnJahrgaenge dto) {
		final FloskelJahrgangZuordnung result = new FloskelJahrgangZuordnung();
		result.id = dto.ID;
		result.idFloskel = dto.Floskel_ID;
		result.idJahrgang = dto.Jahrgang_ID;
		return result;
	}

	@Override
	protected void mapAttribute(final DTOFloskelnJahrgaenge dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case ID_FLOSKEL -> updateIdFloskel(dto, name, value);
			case ID_JAHRGANG -> updateIdJahrgang(dto, name, value);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void updateIdFloskel(final DTOFloskelnJahrgaenge dto, final String name, final Object value) throws ApiOperationException {
		dto.Floskel_ID = JSONMapper.convertToLong(value, false, name);
	}

	private void updateIdJahrgang(final DTOFloskelnJahrgaenge dto, final String name, final Object value) throws ApiOperationException {
		final Long idJahrgang = JSONMapper.convertToLong(value, false, name);
		if (jahrgangWithIdNotFound(idJahrgang)) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde kein Jahrgang mit der id %d gefunden.".formatted(idJahrgang));
		}

		dto.Jahrgang_ID = idJahrgang;
	}

	private boolean jahrgangWithIdNotFound(final Long idJahrgang) {
		return this.conn.queryByKey(DTOJahrgang.class, idJahrgang) == null;
	}

	private void ensureFloskelAndJahrgangCombinationIsUnique(final Map<String, Object> initAttributes) throws ApiOperationException {
		final Long idFloskel = JSONMapper.convertToLong(initAttributes.get(ID_FLOSKEL), false, ID_FLOSKEL);
		final Long idJahrgang = JSONMapper.convertToLong(initAttributes.get(ID_JAHRGANG), false, ID_JAHRGANG);
		if (combinationAlreadyExists(idFloskel, idJahrgang)) {
			throw new ApiOperationException(
					Response.Status.BAD_REQUEST, "Die Kombination aus idFloskel %d und idJahrgang %d ist bereits vorhanden.".formatted(idFloskel, idJahrgang));
		}
	}

	private boolean combinationAlreadyExists(final Long idFloskel, final Long idJahrgang) {
		return this.conn
				.queryList(DTOFloskelnJahrgaenge.QUERY_BY_FLOSKEL_ID, DTOFloskelnJahrgaenge.class, idFloskel).stream()
				.anyMatch(dto -> dto.Jahrgang_ID == idJahrgang);
	}

}
