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
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.svws_nrw.db.dto.current.svws.db.DTODBAutoInkremente;
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
public final class DataKatalogBeschaeftigunsarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogBeschaeftigunsarten(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOBeschaeftigungsart} in einen Core-DTO {@link KatalogEintrag}.
	 */
	private final Function<DTOBeschaeftigungsart, KatalogEintrag> dtoMapper = (final DTOBeschaeftigungsart k) -> {
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
		final List<DTOBeschaeftigungsart> katalog = conn.queryAll(DTOBeschaeftigungsart.class);
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
			return OperationError.NOT_FOUND.getResponse("Die Id der gesuchten Beshäftigungart darf nicht null sein.");
		final DTOBeschaeftigungsart art = conn.queryByKey(DTOBeschaeftigungsart.class, id);
		if (art == null)
			return OperationError.NOT_FOUND.getResponse("Die Beschäftigungsart mit der ID" + id + " existiert nicht.");
		final KatalogEintrag eintrag = dtoMapper.apply(art);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(eintrag).build();
	}

	/**
     * Estellt eine neue Beschäftigungsart
     *
     * @param is             das JSON-Objekt
     *
     * @return  die HTTP-Antwort mit der neuen Beschäftigungsart
     */
    public Response create(final InputStream is) {
        DTOBeschaeftigungsart dtoObjekt = null;
     // Bestimme die ID der neuen Beschäftigungsart
        final DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "K_BeschaeftigungsArt");
        final Long id = lastID == null ? 1 : lastID.MaxID + 1;

        // Beschätigungsart anlegen
        dtoObjekt = new DTOBeschaeftigungsart(id, "");
        return persistDTO(is, dtoObjekt, null);
    }
    @Override
    public Response patch(final Long id, final InputStream is) {
        final DTOBeschaeftigungsart dtoObjekt = conn.queryByKey(DTOBeschaeftigungsart.class, id);
        return persistDTO(is, dtoObjekt, id);
     }

	/**
     * Erstellet eine DTO-Objekt aus dem JSON-Objekt und persistiert es in der Datanbenk.
     *
     * @param is            das JSON-Objekt
     * @param dtoObjekt   das neue oder bereits vorhandene DTO-Objekt
     * @param id            die ID des DTO-Objekts bei einem Patch, null bei create
     *
     * @return die HTTP-Antwort mit der neuen bzw. angepassten Beschäftigungsart.
     */

    public Response  persistDTO(final InputStream is, final DTOBeschaeftigungsart dtoObjekt, final Long id) {
        final Map<String, Object> map = JSONMapper.toMap(is);
        if (map.size() > 0) {
            try {
                conn.transactionBegin();
                if (dtoObjekt == null)
                    throw OperationError.NOT_FOUND.exception();
                for (final Entry<String, Object> entry : map.entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    switch (key) {
                        case "id" -> {
                            if (id != null) {
                                final Long patch_id = JSONMapper.convertToLong(value, true);
                                if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
                                    throw OperationError.BAD_REQUEST.exception();
                            }
                        }
                        // TODO  Überbrüfe bei create Duplikate in DB mit dem gleichem "text"
                        case "text" -> dtoObjekt.Bezeichnung = JSONMapper.convertToString(value, true, true, Schema.tab_K_BeschaeftigungsArt.col_Bezeichnung.datenlaenge());
                        case "istSichtbar" -> dtoObjekt.Sichtbar = JSONMapper.convertToBoolean(value, true);
                        case "istAenderbar" -> dtoObjekt.Aenderbar = JSONMapper.convertToBoolean(value, true);
                        default -> throw OperationError.BAD_REQUEST.exception();
                    }
                }
                conn.transactionPersist(dtoObjekt);
                if (!conn.transactionCommit())
                    return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren der Beschäftigungsarten.");
            } catch (final Exception e) {
                if (e instanceof final WebApplicationException webAppException)
                    return webAppException.getResponse();
                return OperationError.INTERNAL_SERVER_ERROR.getResponse();
            } finally {
                conn.transactionRollback();
            }
        }
        if (id != null)
            return Response.status(Status.OK).build();
         final KatalogEintrag daten = dtoMapper.apply(dtoObjekt);
         return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
