package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenRaum;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanZeitraster;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link StundenplanZeitraster}.
 */
public final class DataStundenplanZeitraster extends DataManagerRevised<Long, DTOStundenplanZeitraster, StundenplanZeitraster> {

	private Long stundenplanID = null;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanZeitraster}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Zeitraster abgefragt wird
	 */
	public DataStundenplanZeitraster(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
		setAttributesRequiredOnCreation("wochentag", "unterrichtstunde", "stundenbeginn", "stundenende");
		setAttributesNotPatchable("id", "idStundenplan");
	}

	/**
	 * Gibt die Daten eines Stundenplanzeitrasters zu dessen ID zurück.
	 *
	 * @param id   Die ID des Stundenplanzeitrasters.
	 *
	 * @return die Daten des Stundenplanzeitrasters zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public StundenplanZeitraster getById(final Long id) throws ApiOperationException {
		final DTOStundenplanZeitraster dto = getDTO(id);
		return map(dto);
	}

	@Override
	protected long getLongId(final DTOStundenplanZeitraster dto) throws ApiOperationException {
		return dto.ID;
	}

	@Override
	protected StundenplanZeitraster map(final DTOStundenplanZeitraster z) throws ApiOperationException {
		final StundenplanZeitraster daten = new StundenplanZeitraster();
		daten.id = z.ID;
		daten.wochentag = z.Tag;
		daten.unterrichtstunde = z.Stunde;
		daten.stundenbeginn = z.Beginn;
		daten.stundenende = z.Ende;
		return daten;
	}

	@Override
	protected void initDTO(final DTOStundenplanZeitraster dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Stundenplan_ID = this.stundenplanID;
	}

	@Override
	protected void mapAttribute(final DTOStundenplanZeitraster dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> dto.ID = JSONMapper.convertToLong(value, false);
			case "wochentag" -> dto.Tag = JSONMapper.convertToIntegerInRange(value, false, 1, 8);
			case "unterrichtstunde" -> dto.Stunde = JSONMapper.convertToIntegerInRange(value, false, 0, 30);
			case "stundenbeginn" -> dto.Beginn = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
			case "stundenende" -> dto.Ende = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOStundenplanZeitraster} Objekt zur angegebenen StundenplanZeitraster-ID.
	 *
	 * @param id ID des StundenplanZeitrasters
	 *
	 * @return Ein {@link DTOStundenplanZeitraster} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOStundenplanZeitraster getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für das StundenplanZeitraster darf nicht null sein.");
		final DTOStundenplanZeitraster dto = conn.queryByKey(DTOStundenplanZeitraster.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein StundenplanZeitraster zur ID " + id + " gefunden.");
		return dto;
	}

	/**
	 * Gibt das Zeitraster des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return das Zeitraster
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<StundenplanZeitraster> getZeitraster(final @NotNull DBEntityManager conn, final long idStundenplan) throws ApiOperationException {
		final List<DTOStundenplanZeitraster> zeitraster = conn.queryList(
				DTOStundenplanZeitraster.QUERY_BY_STUNDENPLAN_ID, DTOStundenplanZeitraster.class, idStundenplan);
		return (new DataStundenplanZeitraster(conn, idStundenplan)).mapList(zeitraster);
	}


	/**
	 * Ermittelt zu dem Tupel (wochentag, unterrichtsstunde) des Stundenplans mit der ID idStundenplan den zugehörigen
	 * Zeitrastereintrag in der Datenbank. Existiert ein solcher noch nicht, so wird ein neue Eintrag mit leeren Zeiten erzeugt.
	 *
	 * @param conn                die Datenbankverbindung
	 * @param idStundenplan       die ID des Stundenplans
	 * @param wochentag           der Wochentag
	 * @param unterrichtsstunde   die Unterrichtsstunde
	 *
	 * @return der Zeitrastereintrag
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static StundenplanZeitraster getOrCreateZeitrasterEintrag(final @NotNull DBEntityManager conn, final long idStundenplan, final int wochentag,
			final int unterrichtsstunde) throws ApiOperationException {
		final List<DTOStundenplanZeitraster> eintraege = conn.queryList(
				"SELECT e FROM DTOStundenplanZeitraster e WHERE e.Stundenplan_ID = ?1 AND e.Tag = ?2 AND e.Stunde = ?3",
				DTOStundenplanZeitraster.class, idStundenplan, wochentag, unterrichtsstunde);
		final DTOStundenplanZeitraster eintrag;
		if (eintraege.isEmpty()) {
			final long id = conn.transactionGetNextID(DTOStundenplanZeitraster.class);
			eintrag = new DTOStundenplanZeitraster(id, idStundenplan, wochentag, unterrichtsstunde, 430 + (unterrichtsstunde * 50),
					475 + (unterrichtsstunde * 50));
			conn.transactionPersist(eintrag);
			conn.transactionFlush();
		} else if (eintraege.size() == 1) {
			eintrag = eintraege.get(0);
		} else
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Mehrfach-Einträge für die Kombination Wochentag %d und Stunde %d im Stundenplan mit der ID %d."
							.formatted(wochentag, unterrichtsstunde, idStundenplan));
		return (new DataStundenplanZeitraster(conn, idStundenplan)).map(eintrag);
	}


	@Override
	public List<StundenplanZeitraster> getList() throws ApiOperationException {
		return getZeitraster(conn, this.stundenplanID);
	}

	@Override
	public StundenplanZeitraster add(final Map<String, Object> initAttributes) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, stundenplanID);   // Prüfe, on der Stundenplan existiert
		return super.add(initAttributes);
	}

	@Override
	public List<StundenplanZeitraster> addMultiple(final InputStream is) throws ApiOperationException {
		DataStundenplan.getDTOStundenplan(conn, stundenplanID);   // Prüfe, on der Stundenplan existiert
		return super.addMultiple(is);
	}

	/**
	 * Kopiert die Zeitrastereinträge des allgemeinen Katalogs zu den Zeitrastereinträgen des angegebenen Stundenplans hinzu.
	 * Dabei wird die angegebene Datenbankverbingung genutzt, welche eine offene Transaktion haben muss.
	 *
	 * @param conn             die Datenbankverbindung
	 * @param dtoStundenplan   das DTO des Stundenplans
	 * @param eintraege        die hinzuzufügenden Zeitraster-Einträge
	 */
	public static void addZeitraster(final @NotNull DBEntityManager conn, final DTOStundenplan dtoStundenplan, final List<StundenplanZeitraster> eintraege) {
		long id = conn.transactionGetNextID(DTOStundenplanZeitraster.class);
		for (final StundenplanZeitraster eintrag : eintraege)
			conn.transactionPersist(new DTOStundenplanZeitraster(id++, dtoStundenplan.ID, eintrag.wochentag, eintrag.unterrichtstunde, eintrag.stundenbeginn,
					eintrag.stundenende));
		conn.transactionFlush();
	}


	@Override
	public Response deleteMultipleAsResponse(final List<Long> ids) throws ApiOperationException {
		final List<DTOStundenplanZeitraster> dtos = conn.queryByKeyList(DTOStundenplanZeitraster.class, ids);
		for (final DTOStundenplanZeitraster dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Zeitraster-Eintrag gehört nicht zu dem angegebenen Stundenplan.");
		return super.deleteMultipleAsResponse(ids);
	}

	@Override
	protected void saveDatabaseDTO(final DTOStundenplanZeitraster dto) throws ApiOperationException {
		super.saveDatabaseDTO(dto);
		DataGostKlausurenRaum.dbHookStundenplangueltigkeitPlus(conn, DataStundenplan.getDTOStundenplan(conn, dto.Stundenplan_ID));
	}

}
