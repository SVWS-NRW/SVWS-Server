package de.svws_nrw.data.schule;

import de.svws_nrw.db.schema.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.data.schule.Abteilung;
import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Abteilung}.
 */
public final class DataAbteilungen extends DataManagerRevised<Long, DTOAbteilungen, Abteilung> {

	/** Die ID des Schuljahresabschnittes */
	private final Long idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Abteilung}.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes, auf den sich die Anfragen beziehen
	 */
	public DataAbteilungen(final DBEntityManager conn, final Long idSchuljahresabschnitt) {
		super(conn);
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		setAttributesNotPatchable("id", "idSchuljahresabschnitt");
		setAttributesRequiredOnCreation("bezeichnung", "idSchuljahresabschnitt");
	}

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Abteilung}.
	 *
	 * @param conn                     die Datenbankverbindung
	 */
	public DataAbteilungen(final DBEntityManager conn) {
		this(conn, null);
	}

	@Override
	protected void initDTO(final DTOAbteilungen dtoAbteilungen, final Long newId, final Map<String, Object> initAttributes) throws ApiOperationException {
		dtoAbteilungen.ID = newId;
		dtoAbteilungen.Schuljahresabschnitts_ID = JSONMapper.convertToLong(initAttributes.get("idSchuljahresabschnitt"), false, "idSchuljahresabschnitt");
		dtoAbteilungen.Sichtbar = true;
	}

	@Override
	protected long getLongId(final DTOAbteilungen abteilung) {
		return abteilung.ID;
	}

	@Override
	protected Abteilung map(final DTOAbteilungen dtoAbteilungen) throws ApiOperationException {
		final Abteilung abteilung = new Abteilung();

		abteilung.id = dtoAbteilungen.ID;
		abteilung.bezeichnung =  (dtoAbteilungen.Bezeichnung == null) ? "" : dtoAbteilungen.Bezeichnung;
		abteilung.idSchuljahresabschnitt = dtoAbteilungen.Schuljahresabschnitts_ID;
		abteilung.idAbteilungsleiter = dtoAbteilungen.AbteilungsLeiter_ID;
		abteilung.raum = dtoAbteilungen.Raum;
		abteilung.email = dtoAbteilungen.Email;
		abteilung.durchwahl = dtoAbteilungen.Durchwahl;
		abteilung.sortierung = (dtoAbteilungen.Sortierung == null) ? 32000 : dtoAbteilungen.Sortierung;

		return abteilung;
	}

	@Override
	protected void mapAttribute(final DTOAbteilungen dtoAbteilungen, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id"  -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (id != dtoAbteilungen.ID)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dtoAbteilungen.ID));
			}
			case "idSchuljahresabschnitt" -> dtoAbteilungen.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false, name);
			case "bezeichnung" -> updateBezeichnung(dtoAbteilungen, name, value);
			case "idAbteilungsleiter" -> dtoAbteilungen.AbteilungsLeiter_ID = JSONMapper.convertToLong(value, true, name);
			case "raum" -> dtoAbteilungen.Raum =
					JSONMapper.convertToString(value, true, true,  Schema.tab_EigeneSchule_Abteilungen.col_Raum.datenlaenge(), name);
			case "email" -> dtoAbteilungen.Email =
					JSONMapper.convertToString(value, true, true,  Schema.tab_EigeneSchule_Abteilungen.col_Email.datenlaenge(), name);
			case "durchwahl" -> dtoAbteilungen.Durchwahl =
					JSONMapper.convertToString(value, true, false,  Schema.tab_EigeneSchule_Abteilungen.col_Durchwahl.datenlaenge(), name);
			case "sortierung" -> dtoAbteilungen.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	private void updateBezeichnung(final DTOAbteilungen dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung =
				JSONMapper.convertToString(value, false, false, Schema.tab_EigeneSchule_Abteilungen.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOAbteilungen.class).stream()
				.anyMatch(a -> (a.ID != dto.ID) && a.Bezeichnung.equalsIgnoreCase(bezeichnung));
		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(value));

		dto.Bezeichnung = bezeichnung;
	}

	@Override
	public List<Abteilung> getAll() throws ApiOperationException {
		final List<DTOAbteilungen> abteilungen = getAbteilungen();
		if (abteilungen.isEmpty())
			return new ArrayList<>();

		final List<Abteilung> result = new ArrayList<>();
		for (final DTOAbteilungen abteilung : abteilungen)
			result.add(map(abteilung));

		// ... ermittle die Klassenzuordnungen ...
		final List<Long> idsAbteilungen = abteilungen.stream().map(a -> a.ID).toList();
		final Map<Long, List<AbteilungKlassenzuordnung>> mapZuordnungen = DataAbteilungenKlassenzuordnungen.getListsByListOfIdAbteilung(conn, idsAbteilungen);
		for (final Abteilung abteilung : result)
			if (mapZuordnungen.containsKey(abteilung.id))
				abteilung.klassenzuordnungen.addAll(mapZuordnungen.get(abteilung.id));
		// ... und gib die Abteilungsinformationen zurück.
		return result;
	}

	private List<DTOAbteilungen> getAbteilungen() {
		return (idSchuljahresabschnitt == null) ? conn.queryAll(DTOAbteilungen.class)
				: conn.queryList(DTOAbteilungen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOAbteilungen.class, idSchuljahresabschnitt);
	}

	@Override
	public Abteilung getById(final Long id) throws ApiOperationException {
		final DTOAbteilungen dtoAbteilungen = conn.queryByKey(DTOAbteilungen.class, id);
		if (dtoAbteilungen == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Eine Abteilung mit der ID %d konnte nicht gefunden werden.".formatted(id));

		final Abteilung abteilung = map(dtoAbteilungen);
		final DataAbteilungenKlassenzuordnungen dataAbteilungenKlassenzuordnungen = new DataAbteilungenKlassenzuordnungen(conn);
		abteilung.klassenzuordnungen.addAll(dataAbteilungenKlassenzuordnungen.getListByIdAbteilung(dtoAbteilungen.ID));
		return abteilung;
	}

}
