package de.svws_nrw.data.lehrer;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.asd.adt.PairNN;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLehramt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link LehrerLehramtEintrag}.
 */
public final class DataLehrerLehramt extends DataManagerRevised<PairNN<Long, String>, DTOLehrerLehramt, LehrerLehramtEintrag> {

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
	}

	@Override
	protected PairNN<Long, String> getID(final Map<String, Object> attributes) throws ApiOperationException {
		if (!attributes.containsKey("id") || !attributes.containsKey("idLehramt"))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID des Datensatzes ist unvollständig. Es müssen sowohl die ID des Lehrers als auch die ID des Lehramtes angegeben werden.");
		final long tmpIdLehrer = JSONMapper.convertToLong(attributes.get("id"), false, "id");
		final long tmpIdLehramt = JSONMapper.convertToLong(attributes.get("idLehramt"), false, "idLehramt");
		final LehrerLehramtKatalogEintrag tmpLehramt = LehrerLehramt.data().getEintragByID(tmpIdLehramt);
		if (tmpLehramt == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein Lehramt mit der ID %d.".formatted(tmpIdLehramt));
		return new PairNN<>(tmpIdLehrer, tmpLehramt.schluessel);
	}

	@Override
	protected PairNN<Long, String> getNextID(final PairNN<Long, String> lastID, final Map<String, Object> initAttributes) throws ApiOperationException {
		return getID(initAttributes);
	}

	@Override
	protected LehrerLehramtEintrag map(final DTOLehrerLehramt dto) throws ApiOperationException {
		return mapInternal(conn, dto);
	}


	@Override
	public DTOLehrerLehramt getDatabaseDTOByID(final @NotNull PairNN<Long, String> id) {
		return conn.queryByKey(DTOLehrerLehramt.class, id.a, id.b);
	}

	@Override
	public LehrerLehramtEintrag getById(final @NotNull PairNN<Long, String> id) throws ApiOperationException {
		return map(getDatabaseDTOByID(id));
	}

	@Override
	public List<LehrerLehramtEintrag> getList() throws ApiOperationException {
		return getListByLehrerId(conn, idLehrer);
	}

	@Override
	protected void initDTO(final DTOLehrerLehramt dto, final @NotNull PairNN<Long, String> id, final Map<String, Object> initAttributes)
			throws ApiOperationException {
		dto.Lehrer_ID = id.a;
		dto.LehramtKrz = id.b;
	}

	@Override
	protected void mapAttribute(final DTOLehrerLehramt dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			// Persönliche Daten
			case "id" -> {
				final long tmpIdLehrer = JSONMapper.convertToLong(map.get("id"), false, "id");
				if (tmpIdLehrer != dto.Lehrer_ID)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Lehrer-ID muss im Patch und in der Datenbank übereinstimmen.");
			}
			case "idLehramt" -> {
				final long tmpIdLehramt = JSONMapper.convertToLong(map.get("idLehramt"), false, "idLehramt");
				final LehrerLehramtKatalogEintrag tmpLehramt = LehrerLehramt.data().getEintragByID(tmpIdLehramt);
				if (tmpLehramt == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein Lehramt mit der ID %d.".formatted(tmpIdLehramt));
				if (!Objects.equals(dto.LehramtKrz, tmpLehramt.schluessel))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Lehramts-ID im Patch muss mit dem Wert in der Datenbank übereinstimmen.");
			}
			case "idAnerkennungsgrund" -> {
				final Long tmpIdLehramt = JSONMapper.convertToLong(map.get("idAnerkennungsgrund"), true, "idAnerkennungsgrund");
				final LehrerLehramtAnerkennungKatalogEintrag eintrag = (tmpIdLehramt == null) ? null
						: LehrerLehramtAnerkennung.data().getEintragByID(tmpIdLehramt);
				dto.LehramtAnerkennungKrz = (eintrag == null) ? null : eintrag.schluessel;
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s ist nicht implementiert.".formatted(name));
		}
	}



	/**
	 * Erstellt ein Core-DTO LehrerLehramtEintrag auf Basis des zugehörigen Database-DTOs.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param dto    das Database-DTO
	 *
	 * @return das Core-DTO
	 */
	private static LehrerLehramtEintrag mapInternal(final DBEntityManager conn, final DTOLehrerLehramt dto) {
		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		final LehrerLehramtEintrag daten = new LehrerLehramtEintrag();
		daten.id = dto.Lehrer_ID;
		final LehrerLehramt lehramt = LehrerLehramt.data().getWertByKuerzel(dto.LehramtKrz);
		final LehrerLehramtKatalogEintrag eintragLehramt = (lehramt == null) ? null : lehramt.daten(schuljahr);
		daten.idLehramt = (eintragLehramt == null) ? -1 : eintragLehramt.id;
		final LehrerLehramtAnerkennung anerkennung = LehrerLehramtAnerkennung.data().getWertByKuerzel(dto.LehramtAnerkennungKrz);
		final LehrerLehramtAnerkennungKatalogEintrag eintragAnerkennung = (anerkennung == null) ? null : anerkennung.daten(schuljahr);
		daten.idAnerkennungsgrund = (eintragAnerkennung == null) ? null : eintragAnerkennung.id;
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
		final List<DTOLehrerLehramt> daten = conn.queryList(DTOLehrerLehramt.QUERY_BY_LEHRER_ID, DTOLehrerLehramt.class, idLehrer);
		if (daten == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerLehramt l : daten)
			result.add(mapInternal(conn, l));
		return result;
	}

}
