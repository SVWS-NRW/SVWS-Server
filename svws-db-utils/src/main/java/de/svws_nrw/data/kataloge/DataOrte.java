package de.svws_nrw.data.kataloge;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link OrtKatalogEintrag}.
 */
public final class DataOrte extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link OrtKatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataOrte(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOOrt} in einen Core-DTO {@link OrtKatalogEintrag}.
	 */
	private final Function<DTOOrt, OrtKatalogEintrag> dtoMapper = (final DTOOrt k) -> {
		final OrtKatalogEintrag daten = new OrtKatalogEintrag();
		daten.id = k.ID;
		daten.plz = k.PLZ;
		daten.ortsname = k.Bezeichnung;
		daten.kreis = k.Kreis;
		daten.kuerzelBundesland = k.Land;
		daten.sortierung = k.Sortierung;
		daten.istSichtbar = k.Sichtbar;
		daten.istAenderbar = k.Aenderbar;
		return daten;
	};

	@Override
	public Response getAll() {
		final List<DTOOrt> katalog = conn.queryAll(DTOOrt.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final List<OrtKatalogEintrag> daten = katalog.stream().map(dtoMapper).collect(Collectors.toList());
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
