package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.kataloge.KatalogEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KatalogEintrag}.
 */
public final class DataKatalogSchuelerFahrschuelerarten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogSchuelerFahrschuelerarten(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOFahrschuelerart} in einen Core-DTO {@link KatalogEintrag}.
	 */
	private final Function<DTOFahrschuelerart, KatalogEintrag> dtoMapper = (final DTOFahrschuelerart k) -> {
		final KatalogEintrag eintrag = new KatalogEintrag();
		eintrag.id = k.ID;
		eintrag.kuerzel = "" + k.ID;
		eintrag.text = k.Bezeichnung;
		eintrag.istAenderbar = k.Aenderbar;
		eintrag.istSichtbar = k.Sichtbar;
		return eintrag;
	};

	@Override
	public Response getAll() {
		final List<DTOFahrschuelerart> katalog = conn.queryAll(DTOFahrschuelerart.class);
    	if (katalog == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final List<KatalogEintrag> daten = katalog.stream().map(dtoMapper).toList();
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
