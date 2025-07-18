package de.svws_nrw.data.lehrer;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link LehrerLehramtEintrag}.
 */
public final class DataLehrerLehramt extends DataManagerRevised<Long, DTOLehrerPersonaldatenLehramt, LehrerLehramtEintrag> {

	/** Die ID des Lehrers für die Daten zum Lehramt */
	private final Long idLehrer;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerLehramtEintrag}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idLehrer   die ID des Lehrers für den die Lehrämter verwaltet werden
	 */
	public DataLehrerLehramt(final DBEntityManager conn, final Long idLehrer) {
		super(conn);
		this.idLehrer = idLehrer;
		setAttributesRequiredOnCreation("idLehrer");
	}

	@Override
	protected Long getID(final Map<String, Object> attributes) throws ApiOperationException {
		if (!attributes.containsKey("id"))
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Datensatzes ist nicht angegeben.");
		return JSONMapper.convertToLong(attributes.get("id"), false, "id");
	}

	@Override
	protected LehrerLehramtEintrag map(final DTOLehrerPersonaldatenLehramt dto) throws ApiOperationException {
		final List<LehrerLehrbefaehigungEintrag> lehrbefaehigungen = DataLehrerLehrbefaehigung.getListByLehramtId(conn, dto.ID);
		final List<LehrerFachrichtungEintrag> fachrichtungen = DataLehrerFachrichtungen.getListByLehramtId(conn, dto.ID);
		return mapInternal(dto, lehrbefaehigungen, fachrichtungen);
	}

	@Override
	public LehrerLehramtEintrag getById(final @NotNull Long id) throws ApiOperationException {
		return map(getDatabaseDTOByID(id));
	}

	@Override
	public List<LehrerLehramtEintrag> getList() throws ApiOperationException {
		return getListByLehrerId(conn, idLehrer);
	}

	@Override
	protected void initDTO(final DTOLehrerPersonaldatenLehramt dto, final @NotNull Long id, final Map<String, Object> initAttributes)
			throws ApiOperationException {
		dto.ID = id;
		dto.Lehrer_ID = JSONMapper.convertToLong(initAttributes.get("idLehrer"), false, "idLehrer");
	}


	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final long tmpIdLehrer = JSONMapper.convertToLong(initAttributes.get("idLehrer"), false, "idLehrer");
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, tmpIdLehrer);
		if (lehrer == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Lehrer für die übergebene Lehrer-ID in der Datenbank gefunden werden.");
	}


	@Override
	protected void mapAttribute(final DTOLehrerPersonaldatenLehramt dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, true, "id");
				if ((id == null) || (id != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST, "ID von Patch und Datenbank-Objekt stimmen nicht überein.");
			}
			case "idLehrer" -> {
				final long tmpIdLehrer = JSONMapper.convertToLong(map.get("idLehrer"), false, "idLehrer");
				if (tmpIdLehrer != dto.Lehrer_ID)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Lehrer-ID von Patch und Datenbank-Objekt stimmen nicht überein.");
			}
			case "idKatalogLehramt" -> {
				final long tmpIdLehramt = JSONMapper.convertToLong(map.get("idKatalogLehramt"), false, "idKatalogLehramt");
				final LehrerLehramtKatalogEintrag tmpLehramt = LehrerLehramt.data().getEintragByID(tmpIdLehramt);
				if (tmpLehramt == null)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Es existiert kein Lehramt im Katalog der Lehrämter mit der ID %d.".formatted(tmpIdLehramt));
				dto.Lehramt_Katalog_ID = tmpIdLehramt;
			}
			case "idAnerkennungsgrund" -> {
				final Long tmpIdAnerkennungsgrund = JSONMapper.convertToLong(map.get("idAnerkennungsgrund"), true, "idAnerkennungsgrund");
				if (tmpIdAnerkennungsgrund == null) {
					dto.LehramtAnerkennung_Katalog_ID = null;
				} else {
					final LehrerLehramtAnerkennungKatalogEintrag eintrag = LehrerLehramtAnerkennung.data().getEintragByID(tmpIdAnerkennungsgrund);
					if (eintrag == null)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Es existiert kein Anerkennungsgrund für das Lehramt im Katalog der Anerkennungsgründe mit der ID %d."
										.formatted(tmpIdAnerkennungsgrund));
					dto.LehramtAnerkennung_Katalog_ID = tmpIdAnerkennungsgrund;
				}
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s ist nicht implementiert.".formatted(name));
		}
	}



	/**
	 * Erstellt ein Core-DTO LehrerLehramtEintrag auf Basis des zugehörigen Database-DTOs.
	 *
	 * @param dto                 das Database-DTO
	 * @param lehrbefaehigungen   die Lehrbefähigungen für das Lehramt des Lehrers
	 * @param fachrichtungen      die Fachrichtungen für das Lehramt des Lehrers
	 *
	 * @return das Core-DTO
	 */
	private static LehrerLehramtEintrag mapInternal(final DTOLehrerPersonaldatenLehramt dto, final List<LehrerLehrbefaehigungEintrag> lehrbefaehigungen,
			final List<LehrerFachrichtungEintrag> fachrichtungen) {
		final LehrerLehramtEintrag daten = new LehrerLehramtEintrag();
		daten.id = dto.ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.idKatalogLehramt = dto.Lehramt_Katalog_ID;
		daten.idAnerkennungsgrund = dto.LehramtAnerkennung_Katalog_ID;
		daten.lehrbefaehigungen.addAll(lehrbefaehigungen);
		daten.fachrichtungen.addAll(fachrichtungen);
		return daten;
	}


	/**
	 * Ermittelt die Lehrämter für den Lehrer mit der angegebenen ID und gibt diese zurück.
	 *
	 * @param conn       die Datenbankverbindung zur Abfrage der Lehrämter
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste mit den Lehrämtern
	 */
	public static List<LehrerLehramtEintrag> getListByLehrerId(final DBEntityManager conn, final Long idLehrer) {
		final List<LehrerLehramtEintrag> result = new ArrayList<>();
		// Bestimme die Lehrämter des Lehrers
		final List<DTOLehrerPersonaldatenLehramt> daten =
				conn.queryList(DTOLehrerPersonaldatenLehramt.QUERY_BY_LEHRER_ID, DTOLehrerPersonaldatenLehramt.class, idLehrer);
		if (daten == null)
			return result;
		// Bestimme die Lehrbefähigungen und die Fachrichtungen zu den Lehrämtern
		final List<Long> idsLehraemter = daten.stream().map(l -> l.ID).toList();
		final Map<Long, List<LehrerLehrbefaehigungEintrag>> mapLehrbefaehigungen = DataLehrerLehrbefaehigung.getMapByLehramtIds(conn, idsLehraemter);
		final Map<Long, List<LehrerFachrichtungEintrag>> mapFachrichtungen = DataLehrerFachrichtungen.getMapByLehramtIds(conn, idsLehraemter);
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerPersonaldatenLehramt l : daten)
			result.add(mapInternal(l, mapLehrbefaehigungen.get(l.ID), mapFachrichtungen.get(l.ID)));
		return result;
	}

}
