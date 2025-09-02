package de.svws_nrw.data.lehrer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtungAnerkennung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerFachrichtungEintrag}.
 */
public final class DataLehrerFachrichtungen extends DataManagerRevised<Long, DTOLehrerPersonaldatenLehramtFachrichtung, LehrerFachrichtungEintrag> {

	/** Die ID des Lehramtes */
	private final Long idLehramt;


	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerFachrichtungEintrag}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idLehramt   die ID des Lehramtes oder null
	 */
	public DataLehrerFachrichtungen(final DBEntityManager conn, final Long idLehramt) {
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
	protected LehrerFachrichtungEintrag map(final DTOLehrerPersonaldatenLehramtFachrichtung dto) throws ApiOperationException {
		return mapInternal(dto);
	}


	@Override
	public LehrerFachrichtungEintrag getById(final @NotNull Long id) throws ApiOperationException {
		return map(getDatabaseDTOByID(id));
	}

	@Override
	public List<LehrerFachrichtungEintrag> getList() throws ApiOperationException {
		return getListByLehramtId(conn, idLehramt);
	}


	@Override
	protected void initDTO(final DTOLehrerPersonaldatenLehramtFachrichtung dto, final @NotNull Long id, final Map<String, Object> initAttributes)
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
	protected void mapAttribute(final DTOLehrerPersonaldatenLehramtFachrichtung dto, final String name, final Object value, final Map<String, Object> map)
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
			case "idFachrichtung" -> {
				final long tmpIdFachrichtung = JSONMapper.convertToLong(map.get("idFachrichtung"), false, "idFachrichtung");
				final LehrerFachrichtungKatalogEintrag tmpFachrichtung = LehrerFachrichtung.data().getEintragByID(tmpIdFachrichtung);
				if (tmpFachrichtung == null)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Es existiert keine Fachrichtung im Katalog der Fachrichtungen mit der ID %d.".formatted(tmpIdFachrichtung));
				dto.Fachrichtung_Katalog_ID = tmpIdFachrichtung;
			}
			case "idAnerkennungsgrund" -> {
				final Long tmpIdAnerkennungsgrund = JSONMapper.convertToLong(map.get("idAnerkennungsgrund"), true, "idAnerkennungsgrund");
				if (tmpIdAnerkennungsgrund == null) {
					dto.FachrichtungAnerkennung_Katalog_ID = null;
				} else {
					final LehrerFachrichtungAnerkennungKatalogEintrag eintrag = LehrerFachrichtungAnerkennung.data().getEintragByID(tmpIdAnerkennungsgrund);
					if (eintrag == null)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Es existiert kein Anerkennungsgrund für die Fachrichtung im Katalog der Anerkennungsgründe mit der ID %d."
										.formatted(tmpIdAnerkennungsgrund));
					dto.FachrichtungAnerkennung_Katalog_ID = tmpIdAnerkennungsgrund;
				}
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s ist nicht implementiert.".formatted(name));
		}
	}


	private static LehrerFachrichtungEintrag mapInternal(final DTOLehrerPersonaldatenLehramtFachrichtung l) {
		final LehrerFachrichtungEintrag daten = new LehrerFachrichtungEintrag();
		daten.id = l.ID;
		daten.idLehramt = l.Lehreramt_ID;
		daten.idFachrichtung = l.Fachrichtung_Katalog_ID;
		daten.idAnerkennungsgrund = l.FachrichtungAnerkennung_Katalog_ID;
		return daten;
	}


	/**
	 * Ermittelt die Fachrichtungen für das Lehramt mit den angegebenen ID.
	 *
	 * @param conn        die Datenbankverbindung zur Abfrage der Fachrichtungen
	 * @param idLehramt   die IDs der Lehrämter
	 *
	 * @return die Liste mit den Fachrichtungen zu dem Lehramt
	 */
	public static List<LehrerFachrichtungEintrag> getListByLehramtId(final DBEntityManager conn, final long idLehramt) {
		final List<LehrerFachrichtungEintrag> result = new ArrayList<>();
		// Bestimme die Fachrichtungen des Lehramtes
		final List<DTOLehrerPersonaldatenLehramtFachrichtung> daten =
				conn.queryList(DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_BY_LEHRERAMT_ID, DTOLehrerPersonaldatenLehramtFachrichtung.class, idLehramt);
		if (daten == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerPersonaldatenLehramtFachrichtung l : daten)
			result.add(mapInternal(l));
		return result;
	}


	/**
	 * Ermittelt die Fachrichtungen für die Lehrämter mit den angegebenen IDs und gibt eine Map mit diesen zurück.
	 *
	 * @param conn            die Datenbankverbindung zur Abfrage der Fachrichtungen
	 * @param idsLehraemter   die IDs der Lehrämter
	 *
	 * @return die Map mit den Fachrichtungen zu den Lehrämtern
	 */
	public static Map<Long, List<LehrerFachrichtungEintrag>> getMapByLehramtIds(final DBEntityManager conn, final List<Long> idsLehraemter) {
		final Map<Long, List<LehrerFachrichtungEintrag>> result = new HashMap<>();
		if (idsLehraemter.isEmpty())
			return result;
		for (final Long idLehramt : idsLehraemter)
			result.put(idLehramt, new ArrayList<>());
		// Bestimme die Fachrichtungen zu den Lehrämtern
		final List<DTOLehrerPersonaldatenLehramtFachrichtung> daten =
				conn.queryList(DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_LIST_BY_LEHRERAMT_ID, DTOLehrerPersonaldatenLehramtFachrichtung.class,
						idsLehraemter);
		if (daten == null)
			return result;
		// Konvertiere sie und füge sie zur Map hinzu
		for (final DTOLehrerPersonaldatenLehramtFachrichtung l : daten)
			result.get(l.Lehreramt_ID).add(mapInternal(l));
		return result;
	}

}
