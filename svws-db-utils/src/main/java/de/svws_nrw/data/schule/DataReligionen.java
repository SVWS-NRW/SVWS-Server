package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.schule.ReligionKatalogEintrag;
import de.svws_nrw.core.types.schule.Religion;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.dto.current.svws.db.DTODBAutoInkremente;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ReligionEintrag}.
 */
public final class DataReligionen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ReligionEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataReligionen(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOStatkueNationalitaeten} in einen Core-DTO {@link ReligionEintrag}.
	 */
	private final Function<DTOKonfession, ReligionEintrag> dtoMapper = (final DTOKonfession k) -> {
		final ReligionEintrag daten = new ReligionEintrag();
		daten.id = k.ID;
		daten.text = k.Bezeichnung;
		daten.textZeugnis = k.ZeugnisBezeichnung;
		daten.kuerzel = k.StatistikKrz;
		daten.sortierung = k.Sortierung;
		daten.istSichtbar = k.Sichtbar;
		daten.istAenderbar = k.Aenderbar;
		return daten;
	};

	@Override
	public Response getAll() {
    	final List<DTOKonfession> katalog = conn.queryAll(DTOKonfession.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final List<ReligionEintrag> daten = katalog.stream().map(dtoMapper).collect(Collectors.toList());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(final Long id) {
		final DTOKonfession reli = conn.queryByKey(DTOKonfession.class, id);
		if (reli == null)
			throw OperationError.NOT_FOUND.exception();
		final ReligionEintrag daten = dtoMapper.apply(reli);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		if (id == null)
            return OperationError.NOT_FOUND.getResponse("Die Id der zu ändernden Religion darf nicht null sein.");
        final Map<String, Object> map = JSONMapper.toMap(is);
        if (map.size() > 0) {
            try {
                conn.transactionBegin();
                final DTOKonfession reli = conn.queryByKey(DTOKonfession.class, id);
                if (reli == null)
                    return OperationError.NOT_FOUND.getResponse("Die Beschäftigungsart mit der ID" + id + " existiert nicht.");
                for (final Entry<String, Object> entry : map.entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    switch (key) {
						case "id" -> {
							final Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
								throw OperationError.BAD_REQUEST.exception();
						}
						case "kuerzel" -> reli.StatistikKrz = JSONMapper.convertToString(value, true, true);
                        case "text" -> reli.Bezeichnung = JSONMapper.convertToString(value, true, true);
                        case "textZeugnis" -> reli.ZeugnisBezeichnung = JSONMapper.convertToString(value, true, true);
						case "istSichtbar" -> reli.Sichtbar = JSONMapper.convertToBoolean(value, true);
						case "istAenderbar" -> reli.Aenderbar = JSONMapper.convertToBoolean(value, true);
						case "sortierung" -> reli.Sortierung = JSONMapper.convertToInteger(value, true);
                       	default -> throw OperationError.BAD_REQUEST.exception();
                    }
                }
                conn.transactionPersist(reli);
                conn.transactionCommit();
            } catch (final Exception e) {
    			if (e instanceof final WebApplicationException webAppException)
    				return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    		} finally {
    			// Perform a rollback if necessary
    			conn.transactionRollback();
    		}
        }
        return Response.status(Status.OK).build();
	}

	/**
	 * Erstellt eine neue Religion
	 *
	 * @param  is					JSON-Objekt mit den Daten
	 * @return Eine Response mit der neuen Religion
	 */
	public Response create(final InputStream is) {
		DTOKonfession reli = null;
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			try {
				conn.transactionBegin();
				// Bestimme die ID der neuen Religion
				final DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "K_Religion");
				final Long id = lastID == null ? 1 : lastID.MaxID + 1;
				// Religion anlegen
				reli = new DTOKonfession(id, "");
				for (final Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					switch (key) {
						case "id" -> {
							final Long create_id = JSONMapper.convertToLong(value, true);
							if ((create_id != null) && (create_id >= 0))
								throw OperationError.BAD_REQUEST.exception("Die ID für die Religion darf bei der Erstellung nicht gültig gesetzt sein.");
						}
						case "kuerzel" -> {
							reli.StatistikKrz = JSONMapper.convertToString(value, true, true);
							if (reli.StatistikKrz != null) {
								final ReligionKatalogEintrag rke = Religion.getByKuerzel(reli.StatistikKrz).daten;
								if (rke == null)
									throw OperationError.NOT_FOUND.exception("Eine Religion mit dem  Küruel " + reli.StatistikKrz + " existiert in der amtlichen Statistik nicht.");
							}
						}
                        case "text" -> reli.Bezeichnung = JSONMapper.convertToString(value, true, true);
                        case "textZeugnis" -> reli.ZeugnisBezeichnung = JSONMapper.convertToString(value, true, true);
						case "istSichtbar" -> reli.Sichtbar = JSONMapper.convertToBoolean(value, true);
						case "istAenderbar" -> reli.Aenderbar = JSONMapper.convertToBoolean(value, true);
						case "sortierung" -> reli.Sortierung = JSONMapper.convertToInteger(value, true);
                       	default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(reli);
				if (!conn.transactionCommit())
					return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren der Religion");
			} catch (final Exception e) {
				if (e instanceof final WebApplicationException webApplicationException)
					return webApplicationException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
			} finally {
				conn.transactionRollback();
			}
		}
		final ReligionEintrag daten = dtoMapper.apply(reli);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
