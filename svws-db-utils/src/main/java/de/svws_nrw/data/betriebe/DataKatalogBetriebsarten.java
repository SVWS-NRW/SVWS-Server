package de.svws_nrw.data.betriebe;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.kataloge.KatalogEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAdressart;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KatalogEintrag}.
 */
public final class DataKatalogBetriebsarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogBetriebsarten(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKatalogAdressart} in einen Core-DTO {@link KatalogEintrag}.
	 */
	private final Function<DTOKatalogAdressart, KatalogEintrag> dtoMapper = (final DTOKatalogAdressart k) -> {
		final KatalogEintrag eintrag = new KatalogEintrag();
		eintrag.id = k.ID;
		eintrag.kuerzel = "" + k.ID;
		eintrag.text = k.Bezeichnung;
		eintrag.istSichtbar = true;
		eintrag.istAenderbar = true;
		return eintrag;
	};


	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link KatalogEintrag}.
	 */
	private final Comparator<KatalogEintrag> dataComparator = (a, b) -> {
		final Collator collator = Collator.getInstance(Locale.GERMAN);
		return collator.compare(a.text, b.text);
	};


	@Override
	public Response getAll() {
		final List<DTOKatalogAdressart> katalog = conn.queryAll(DTOKatalogAdressart.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final List<KatalogEintrag> daten = katalog.stream().map(dtoMapper).sorted(dataComparator).toList();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse("Die ID der gesuchten Beshäftigungart darf nicht null sein.");
		final DTOKatalogAdressart art = conn.queryByKey(DTOKatalogAdressart.class, id);
		if (art == null)
			return OperationError.NOT_FOUND.getResponse("Die Beschäftigungsart mit der ID " + id + " existiert nicht.");
		final KatalogEintrag eintrag = dtoMapper.apply(art);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(eintrag).build();
	}

	/**
     * Erstellt eine neue Betriebsart
     *
     * @param is            das JSON-Objekt mit den Daten
     *
     * @return Eine Response mit der neuen Betriebsart
     */
    public Response create(final InputStream is) {
        DTOKatalogAdressart k_addressart = null;
        final Map<String, Object> map = JSONMapper.toMap(is);
        if (map.size() > 0) {
            try {
                conn.transactionBegin();

                // Bestimme die ID der neuen Ansprechpartners
                final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "K_Adressart");
                final Long id = lastID == null ? 1 : lastID.MaxID + 1;

                // Schülerbetrieb anlegen
                k_addressart = new DTOKatalogAdressart(id, "");

                for (final Entry<String, Object> entry : map.entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    switch (key) {
                        case "id" -> {
                            // ignoriere eine angegebene ID
                        }
                        case "text" -> k_addressart.Bezeichnung = JSONMapper.convertToString(value, true, true, Schema.tab_K_Adressart.col_Bezeichnung.datenlaenge());
                        case "istSichtbar" -> k_addressart.Sichtbar = JSONMapper.convertToBoolean(value, true);
                        case "istAenderbar" -> k_addressart.Aenderbar = JSONMapper.convertToBoolean(value, true);
                        default -> throw OperationError.BAD_REQUEST.exception();
                    }
                }
                conn.transactionPersist(k_addressart);
                if (!conn.transactionCommit())
                    return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Betriebansprechpartners");
            } catch (final Exception e) {
                if (e instanceof final WebApplicationException webApplicationException)
                    return webApplicationException.getResponse();
                return OperationError.INTERNAL_SERVER_ERROR.getResponse();
            } finally {
                conn.transactionRollback();
            }
        }
        final KatalogEintrag daten = dtoMapper.apply(k_addressart);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

	@Override
	public Response patch(final Long id, final InputStream is) {
		if (id == null)
            return OperationError.NOT_FOUND.getResponse("Die Id der zu ändernden Beshäftigungart darf nicht null sein.");
        final Map<String, Object> map = JSONMapper.toMap(is);
        if (map.size() > 0) {
            try {
                conn.transactionBegin();
                final DTOKatalogAdressart art = conn.queryByKey(DTOKatalogAdressart.class, id);
                if (art == null)
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
                        case "text" -> art.Bezeichnung = JSONMapper.convertToString(value, true, true, Schema.tab_K_Adressart.col_Bezeichnung.datenlaenge());
						case "istSichtbar" -> art.Sichtbar = JSONMapper.convertToBoolean(value, true);
						case "istAenderbar" -> art.Aenderbar = JSONMapper.convertToBoolean(value, true);
                       	default -> throw OperationError.BAD_REQUEST.exception();
                    }
                }
                conn.transactionPersist(art);
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

}
