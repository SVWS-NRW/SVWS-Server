package de.svws_nrw.data.schule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
	protected void initDTO(final DTOAbteilungsKlassen dtoAbteilungsKlassen, final Long newID, final Map<String, Object> initAttributes)
			throws ApiOperationException {
		dtoAbteilungsKlassen.ID = newID;
	}

	@Override
	public AbteilungKlassenzuordnung map(final DTOAbteilungsKlassen dtoAbteilungsKlassen) {
		final AbteilungKlassenzuordnung abteilungKlassenzuordnung = new AbteilungKlassenzuordnung();
		abteilungKlassenzuordnung.id = dtoAbteilungsKlassen.ID;
		abteilungKlassenzuordnung.idAbteilung = dtoAbteilungsKlassen.Abteilung_ID;
		abteilungKlassenzuordnung.idKlasse = dtoAbteilungsKlassen.Klassen_ID;
		return abteilungKlassenzuordnung;
	}

	@Override
	protected void mapAttribute(final DTOAbteilungsKlassen dtoAbteilungsKlassen, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (id != dtoAbteilungsKlassen.ID)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Id %d der PatchMap ist ungleich der id %d vom Dto" .formatted(id, dtoAbteilungsKlassen.ID));
			}
			case "idAbteilung" -> {

				final Long idAbteilung = JSONMapper.convertToLong(value, false, "idAbteilung");
				if (checkIfAbteilungExistsById(idAbteilung).isPresent()) {
					dtoAbteilungsKlassen.Abteilung_ID = idAbteilung;
				} else {
					throw new ApiOperationException(Status.BAD_REQUEST, "Für die %d wurde keine Abteilung gefunden" .formatted(idAbteilung));
				}
			}
			case "idKlasse" -> {
				final Long idKlasse = JSONMapper.convertToLong(value, false, "idKlasse");
				if (checkIfKlasseExistsById(idKlasse).isPresent()) {
					dtoAbteilungsKlassen.Klassen_ID = idKlasse;
				} else {
					throw new ApiOperationException(Status.BAD_REQUEST, "Für die %d wurde keine Klasse gefunden" .formatted(idKlasse));
				}
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt." .formatted(name));
		}
	}

	private Optional<DTOAbteilungen> checkIfAbteilungExistsById(final Long idAbteilung) {
		return Optional.ofNullable(conn.queryByKey(DTOAbteilungen.class, idAbteilung));
	}

	private Optional<DTOKlassen> checkIfKlasseExistsById(final Long idKlassen) {
		return Optional.ofNullable(conn.queryByKey(DTOKlassen.class, idKlassen));
	}

	@Override
	public AbteilungKlassenzuordnung getById(final Long id) throws ApiOperationException {
		final DTOAbteilungsKlassen zuordnung = conn.queryByKey(DTOAbteilungsKlassen.class, id);
		if (zuordnung == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Eine Zuordnung mit der ID %d von einer Klasse zu einer Abteilung konnte nicht gefunden werden." .formatted(id));
		return map(zuordnung);
	}

	@Override
	public List<AbteilungKlassenzuordnung> getAll() throws ApiOperationException {
		return getListMappedToAbteilungsKlassenzuordnung(conn.queryList(DTOAbteilungsKlassen.QUERY_ALL, DTOAbteilungsKlassen.class));
	}

	/**
	 * Gibt eine Liste von {@link AbteilungKlassenzuordnung} zurück für die Id der Abteilung.
	 * @param idAbteilung            Id für die Abteilung
	 * @return Liste von AbteilungsKlassenZuordnungen
	 */
	public List<AbteilungKlassenzuordnung> getListByIdAbteilung(final Long idAbteilung) throws ApiOperationException {
		if (idAbteilung == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen muss eine ID angegeben werden. Null ist nicht zulässig.");

		return getListMappedToAbteilungsKlassenzuordnung(conn.queryList(DTOAbteilungsKlassen.QUERY_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, idAbteilung));
	}

	private List<AbteilungKlassenzuordnung> getListMappedToAbteilungsKlassenzuordnung(final List<DTOAbteilungsKlassen> dtoAbteilungsKlassen) {
		final List<AbteilungKlassenzuordnung> result = new ArrayList<>();
		for (final DTOAbteilungsKlassen zuordnung : dtoAbteilungsKlassen)
			result.add(map(zuordnung));
		return result;
	}

	/**
	 *  Diese Methode erstellt eine Liste von AbteilungKlassenzuordnungen zu deren zugehörigen Id´s der Abteilungen.
	 *
	 * @param conn					die Datenbankverbindung
	 * @param idsAbteilungen		Id´s der Abteilungen zu denen die AbteilungenKlassenzuordnungen gemappt werden sollen
	 * @return eine gemappte Liste von AbteilungKlassenzuordnungen zu der Id der Abteilungen
	 */
	public static Map<Long, List<AbteilungKlassenzuordnung>> getListsByListOfIdAbteilung(final DBEntityManager conn, final List<Long> idsAbteilungen) {
		final List<DTOAbteilungsKlassen> dtosZuordnungen =
				conn.queryList(DTOAbteilungsKlassen.QUERY_LIST_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, idsAbteilungen);
		final List<AbteilungKlassenzuordnung> zuordnungen = new ArrayList<>();
		final DataAbteilungenKlassenzuordnungen dataAbteilungenKlassenzuordnungen = new DataAbteilungenKlassenzuordnungen(conn);
		for (final DTOAbteilungsKlassen dtoZuordnung : dtosZuordnungen)
			zuordnungen.add(dataAbteilungenKlassenzuordnungen.map(dtoZuordnung));
		return zuordnungen.stream().collect(Collectors.groupingBy(z -> z.idAbteilung));
	}

}
