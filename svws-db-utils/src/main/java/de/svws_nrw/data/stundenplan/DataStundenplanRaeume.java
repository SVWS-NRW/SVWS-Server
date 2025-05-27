package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Raum;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenRaum;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanRaum;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtRaum;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanRaum}.
 */
public final class DataStundenplanRaeume extends DataManagerRevised<Long, DTOStundenplanRaum, StundenplanRaum> {

	private Long stundenplanID = null;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanRaum}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Räume abgefragt werden
	 */
	public DataStundenplanRaeume(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
		setAttributesRequiredOnCreation("kuerzel", "groesse");
		setAttributesNotPatchable("id", "idStundenplan");
	}

	/**
	 * Gibt die Daten eines Stundenplanraums zu dessen ID zurück.
	 *
	 * @param id   Die ID des Stundenplanraums.
	 *
	 * @return die Daten des Stundenplanraums zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public StundenplanRaum getById(final Long id) throws ApiOperationException {
		final DTOStundenplanRaum dto = getDTO(id);
		return map(dto);
	}

	@Override
	protected long getLongId(final DTOStundenplanRaum dto) throws ApiOperationException {
		return dto.ID;
	}

	@Override
	protected StundenplanRaum map(final DTOStundenplanRaum r) throws ApiOperationException {
		final StundenplanRaum daten = new StundenplanRaum();
		daten.id = r.ID;
		daten.kuerzel = r.Kuerzel;
		daten.beschreibung = r.Beschreibung;
		daten.groesse = r.Groesse;
		return daten;
	}

	@Override
	protected void initDTO(final DTOStundenplanRaum dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Stundenplan_ID = this.stundenplanID;
	}

	@Override
	protected void mapAttribute(final DTOStundenplanRaum dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> dto.ID = JSONMapper.convertToLong(value, false);
			case "kuerzel" -> {
				dto.Kuerzel = JSONMapper.convertToString(value, false, false, 20).trim();
				if ("".equals(dto.Kuerzel))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Das Kürzel darf nicht nur aus Leerzeichen bestehen (diese werden am Anfang und am Ende des Kürzels automatisch entfernt.");
			}
			case "beschreibung" -> dto.Beschreibung = JSONMapper.convertToString(value, false, true, 1000);
			case "groesse" -> dto.Groesse = JSONMapper.convertToInteger(value, false);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOStundenplanRaum} Objekt zur angegebenen StundenplanRaum-ID.
	 *
	 * @param id ID des StundenplanRaums
	 *
	 * @return Ein {@link DTOStundenplanRaum} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOStundenplanRaum getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den StundenplanRaum darf nicht null sein.");
		final DTOStundenplanRaum dto = conn.queryByKey(DTOStundenplanRaum.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein StundenplanRaum zur ID " + id + " gefunden.");
		return dto;
	}

	/**
	 * Gibt die Räume des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Räume
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<StundenplanRaum> getRaeume(final @NotNull DBEntityManager conn, final long idStundenplan) throws ApiOperationException {
		final List<DTOStundenplanRaum> raeume = conn.queryList(DTOStundenplanRaum.QUERY_BY_STUNDENPLAN_ID, DTOStundenplanRaum.class, idStundenplan);
		return (new DataStundenplanRaeume(conn, idStundenplan)).mapList(raeume);
	}

	/**
	 * Mappt die Stundenplanraeume auf die gegebenen UnterrichtIds
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 * @param unterrichtIds   die Unterrichte, für die die Räume gesucht und gemappt werden sollen
	 *
	 * @return eine Map, in der die Räume der jeweiligen UnterrichtId zugeordnet ist
	 * @throws ApiOperationException    im Fehlerfall
	 */
	public static Map<Long, List<StundenplanRaum>> getRaeumeByUnterrichtId(final @NotNull DBEntityManager conn,
			final long idStundenplan, final List<Long> unterrichtIds) throws ApiOperationException {
		final Map<Long, StundenplanRaum> raumById = DataStundenplanRaeume.getRaeume(conn, idStundenplan).stream()
				.collect(Collectors.toMap(r -> r.id, Function.identity()));
		final Map<Long, List<StundenplanRaum>> raeumeByUnterrichtId = new HashMap<>();
		final List<DTOStundenplanUnterrichtRaum> listRaeume = unterrichtIds.isEmpty() ? new ArrayList<>()
				: conn.queryList(DTOStundenplanUnterrichtRaum.QUERY_LIST_BY_UNTERRICHT_ID, DTOStundenplanUnterrichtRaum.class, unterrichtIds);
		for (final DTOStundenplanUnterrichtRaum r : listRaeume) {
			final List<StundenplanRaum> raeume = raeumeByUnterrichtId.computeIfAbsent(r.Unterricht_ID, id -> new ArrayList<>());
			if (raumById.containsKey(r.Raum_ID))
				raeume.add(raumById.get(r.Raum_ID));
		}
		return raeumeByUnterrichtId;
	}

	/**
	 * Ermittelt zu Stundenplans mit der angegebenen ID und dem Raumkürzel den zugehörigen
	 * Raumeintrag in der Datenbank. Existiert ein solcher noch nicht, so wird ein neuer Raum mit Standardinformationen erzeugt.
	 *
	 * @param conn                die Datenbankverbindung
	 * @param idStundenplan       die ID des Stundenplans
	 * @param kuerzel             das Kürzel des Raums
	 *
	 * @return der Zeitrastereintrag
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static StundenplanRaum getOrCreateRaum(final @NotNull DBEntityManager conn, final long idStundenplan, final String kuerzel)
			throws ApiOperationException {
		final List<DTOStundenplanRaum> raeume = conn.queryList(
				"SELECT e FROM DTOStundenplanRaum e WHERE e.Stundenplan_ID = ?1 AND e.Kuerzel = ?2", DTOStundenplanRaum.class, idStundenplan, kuerzel);
		final DTOStundenplanRaum raum;
		final DataStundenplanRaeume data = new DataStundenplanRaeume(conn, idStundenplan);
		if (raeume.isEmpty()) {
			final long id = conn.transactionGetNextID(DTOStundenplanRaum.class);
			raum = new DTOStundenplanRaum(id, idStundenplan, kuerzel, kuerzel, 30);
			conn.transactionPersist(raum);
			conn.transactionFlush();
			return data.map(raum);
		}
		if (raeume.size() == 1)
			return data.map(raeume.get(0));
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
				"Mehrfach-Einträge für das Kürzel %s im Stundenplan mit der ID %d.".formatted(kuerzel, idStundenplan));
	}

	@Override
	public StundenplanRaum add(final Map<String, Object> initAttributes) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, stundenplanID);   // Prüfe, on der Stundenplan existiert
		return super.add(initAttributes);
	}

	@Override
	public List<StundenplanRaum> addMultiple(final InputStream is) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, stundenplanID);   // Prüfe, on der Stundenplan existiert
		return super.addMultiple(is);
	}

	/**
	 * Kopiert die Räume des allgemeinen Katalogs zu den Räumen des angegebenen Stundenplans hinzu. Dabei wird die
	 * angegebene Datenbankverbingung genutzt, welche eine offene Transaktion haben muss.
	 *
	 * @param conn             die Datenbankverbindung
	 * @param dtoStundenplan   das DTO des Stundenplans
	 * @param raeume           die hinzuzufügenden Räume
	 */
	public static void addRaeume(final @NotNull DBEntityManager conn, final DTOStundenplan dtoStundenplan, final List<Raum> raeume) {
		long id = conn.transactionGetNextID(DTOStundenplanRaum.class);
		for (final Raum raum : raeume)
			conn.transactionPersist(new DTOStundenplanRaum(id++, dtoStundenplan.ID, raum.kuerzel, raum.beschreibung, raum.groesse));
		conn.transactionFlush();
	}

	@Override
	public Response deleteMultipleAsResponse(final List<Long> ids) throws ApiOperationException {
		final List<DTOStundenplanRaum> dtos = conn.queryByKeyList(DTOStundenplanRaum.class, ids);
		for (final DTOStundenplanRaum dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Raum-Eintrag gehört nicht zu dem angegebenen Stundenplan.");
		return super.deleteMultipleAsResponse(ids);
	}

	@Override
	protected void saveDatabaseDTO(final DTOStundenplanRaum dto) throws ApiOperationException {
		super.saveDatabaseDTO(dto);
		DataGostKlausurenRaum.dbHookStundenplangueltigkeitPlus(conn, DataStundenplan.getDTOStundenplan(conn, dto.Stundenplan_ID));
	}


}
