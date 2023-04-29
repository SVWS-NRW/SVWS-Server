package de.svws_nrw.data.kataloge;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.kataloge.KatalogEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KatalogEintrag}.
 */
public final class DataHaltestellen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataHaltestellen(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOHaltestellen} in einen Core-DTO {@link KatalogEintrag}.
	 */
	private final Function<DTOHaltestellen, KatalogEintrag> dtoMapper = (final DTOHaltestellen k) -> {
		final KatalogEintrag daten = new KatalogEintrag();
		daten.id = k.ID;
		daten.kuerzel = "" + k.ID;
		daten.text = k.Bezeichnung;
		daten.istSichtbar = true;
		daten.istAenderbar = true;
		return daten;
	};

	@Override
	public Response getAll() {
    	final List<DTOHaltestellen> katalog = conn.queryAll(DTOHaltestellen.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final List<KatalogEintrag> daten = katalog.stream().map(dtoMapper).sorted((a, b) -> {
    		if ((a.text == null) && (b.text == null))
    			return 0;
    		if (a.text == null)
    			return -1;
    		if (b.text == null)
    			return 1;
    		return a.text.compareTo(b.text);
    	}).collect(Collectors.toList());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
