package de.svws_nrw.data.lehrer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigungAnerkennung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerLehrbefaehigungEintrag}.
 */
public final class DataLehrerLehrbefaehigung extends DataManagerRevised<Long, DTOLehrerPersonaldatenLehramtBefaehigung, LehrerLehrbefaehigungEintrag> {

	/** Die ID des Lehramtes */
	private final Long idLehramt;


	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerLehrbefaehigungEintrag}.
	 *
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idLehramt   die ID des Lehramtes oder null
	 */
	public DataLehrerLehrbefaehigung(final DBEntityManager conn, final Long idLehramt) {
		super(conn);
		this.idLehramt = idLehramt;
		setAttributesRequiredOnCreation("idLehramt");
	}


	@Override
	protected Long getID(final Map<String, Object> attributes) throws ApiOperationException {
		if (!attributes.containsKey("id"))
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Datensatzes ist nicht angegeben.");
		return JSONMapper.convertToLong(attributes.get("id"), false, "id");
	}


	@Override
	protected LehrerLehrbefaehigungEintrag map(final DTOLehrerPersonaldatenLehramtBefaehigung dto) throws ApiOperationException {
		return mapInternal(dto);
	}


	@Override
	public LehrerLehrbefaehigungEintrag getById(final @NotNull Long id) throws ApiOperationException {
		return map(getDatabaseDTOByID(id));
	}

	@Override
	public List<LehrerLehrbefaehigungEintrag> getList() throws ApiOperationException {
		return getListByLehramtId(conn, idLehramt);
	}


	@Override
	protected void initDTO(final DTOLehrerPersonaldatenLehramtBefaehigung dto, final @NotNull Long id, final Map<String, Object> initAttributes)
			throws ApiOperationException {
		dto.ID = id;
		dto.Lehreramt_ID = JSONMapper.convertToLong(initAttributes.get("idLehramt"), false, "idLehramt");
	}


	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final long tmpIdLehramt = JSONMapper.convertToLong(initAttributes.get("idLehramt"), false, "idLehramt");
		final DTOLehrerPersonaldatenLehramt lehramt = conn.queryByKey(DTOLehrerPersonaldatenLehramt.class, tmpIdLehramt);
		if (lehramt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Lehramt für die übergebene Lehramts-ID in der Datenbank gefunden werden.");
	}


	@Override
	protected void mapAttribute(final DTOLehrerPersonaldatenLehramtBefaehigung dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, true, "id");
				if ((id == null) || (id != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST, "ID von Patch und Datenbank-Objekt stimmen nicht überein.");
			}
			case "idLehramt" -> {
				final long tmpIdLehramt = JSONMapper.convertToLong(map.get("idLehramt"), false, "idLehramt");
				if (tmpIdLehramt != dto.Lehreramt_ID)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Lehramts-ID von Patch und Datenbank-Objekt stimmen nicht überein.");
			}
			case "idLehrbefaehigung" -> {
				final long tmpIdLehrbefaehigung = JSONMapper.convertToLong(map.get("idLehrbefaehigung"), false, "idLehrbefaehigung");
				final LehrerLehrbefaehigungKatalogEintrag tmpLehrbefaehigung = LehrerLehrbefaehigung.data().getEintragByID(tmpIdLehrbefaehigung);
				if (tmpLehrbefaehigung == null)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Es existiert keine Lehrbefähigung im Katalog der Lehrbefähigungen mit der ID %d.".formatted(tmpIdLehrbefaehigung));
				dto.Lehrbefaehigung_Katalog_ID = tmpIdLehrbefaehigung;
			}
			case "idAnerkennungsgrund" -> {
				final Long tmpIdAnerkennungsgrund = JSONMapper.convertToLong(map.get("idAnerkennungsgrund"), true, "idAnerkennungsgrund");
				if (tmpIdAnerkennungsgrund == null) {
					dto.LehrbefaehigungAnerkennung_Katalog_ID = null;
				} else {
					final LehrerLehrbefaehigungAnerkennungKatalogEintrag eintrag =
							LehrerLehrbefaehigungAnerkennung.data().getEintragByID(tmpIdAnerkennungsgrund);
					if (eintrag == null)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Es existiert kein Anerkennungsgrund für die Lehrbefähigung im Katalog der Anerkennungsgründe mit der ID %d."
										.formatted(tmpIdAnerkennungsgrund));
					dto.LehrbefaehigungAnerkennung_Katalog_ID = tmpIdAnerkennungsgrund;
				}
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s ist nicht implementiert.".formatted(name));
		}
	}


	private static LehrerLehrbefaehigungEintrag mapInternal(final DTOLehrerPersonaldatenLehramtBefaehigung l) {
		final LehrerLehrbefaehigungEintrag daten = new LehrerLehrbefaehigungEintrag();
		daten.id = l.ID;
		daten.idLehramt = l.Lehreramt_ID;
		daten.idLehrbefaehigung = l.Lehrbefaehigung_Katalog_ID;
		daten.idAnerkennungsgrund = l.LehrbefaehigungAnerkennung_Katalog_ID;
		return daten;
	}


	/**
	 * Ermittelt die Lehrbefähigungen für das Lehramt mit den angegebenen ID.
	 *
	 * @param conn        die Datenbankverbindung zur Abfrage der Lehrbefähigungen
	 * @param idLehramt   die IDs der Lehrämter
	 *
	 * @return die Liste mit den Lehrbefähigungen zu dem Lehramt
	 */
	public static List<LehrerLehrbefaehigungEintrag> getListByLehramtId(final DBEntityManager conn, final long idLehramt) {
		final List<LehrerLehrbefaehigungEintrag> result = new ArrayList<>();
		// Bestimme die Lehrbefähigungen des Lehramtes
		final List<DTOLehrerPersonaldatenLehramtBefaehigung> daten =
				conn.queryList(DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_BY_LEHRERAMT_ID, DTOLehrerPersonaldatenLehramtBefaehigung.class, idLehramt);
		if (daten == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerPersonaldatenLehramtBefaehigung l : daten)
			result.add(mapInternal(l));
		return result;
	}


	/**
	 * Ermittelt die Lehrbefähigungen für den Lehrer mit der angegebenen ID und gibt eine Map mit diesen zurück.
	 *
	 * @param conn            die Datenbankverbindung zur Abfrage der Lehrbefähigungen
	 * @param idsLehraemter   die IDs der Lehrämter
	 *
	 * @return die Map mit den Lehrbefähigungen zu den Lehrämtern
	 */
	public static Map<Long, List<LehrerLehrbefaehigungEintrag>> getMapByLehramtIds(final DBEntityManager conn, final List<Long> idsLehraemter) {
		final Map<Long, List<LehrerLehrbefaehigungEintrag>> result = new HashMap<>();
		if (idsLehraemter.isEmpty())
			return result;
		for (final Long idLehramt : idsLehraemter)
			result.put(idLehramt, new ArrayList<>());
		// Bestimme die Lehrbefähigungen zu den Lehrämtern
		final List<DTOLehrerPersonaldatenLehramtBefaehigung> daten =
				conn.queryList(DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_LIST_BY_LEHRERAMT_ID, DTOLehrerPersonaldatenLehramtBefaehigung.class,
						idsLehraemter);
		if (daten == null)
			return result;
		// Konvertiere sie und füge sie zur Map hinzu
		for (final DTOLehrerPersonaldatenLehramtBefaehigung l : daten)
			result.get(l.Lehreramt_ID).add(mapInternal(l));
		return result;
	}

}
