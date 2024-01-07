package de.svws_nrw.data.schueler;

import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link FoerderschwerpunktEintrag}.
 */
public final class DataKatalogSchuelerFoerderschwerpunkte extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link FoerderschwerpunktEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogSchuelerFoerderschwerpunkte(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOFoerderschwerpunkt} in einen Core-DTO {@link FoerderschwerpunktEintrag}.
	 */
	private final Function<DTOFoerderschwerpunkt, FoerderschwerpunktEintrag> dtoMapper = (final DTOFoerderschwerpunkt k) -> {
		final FoerderschwerpunktEintrag eintrag = new FoerderschwerpunktEintrag();
		eintrag.id = k.ID;
		eintrag.kuerzel = "" + k.ID;
		eintrag.text = k.Bezeichnung;
		eintrag.kuerzelStatistik = k.StatistikKrz;
		eintrag.istAenderbar = k.Aenderbar;
		eintrag.istSichtbar = k.Sichtbar;
		return eintrag;
	};

	@Override
	public Response getAll() {
		final List<FoerderschwerpunktEintrag> daten = getAllFromDB();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Ermittelt alle Förderschwerpunkte aus der Datenbank.
	 * @return Liste der Förderschwerpunkte.
	 */
	public List<FoerderschwerpunktEintrag> getAllFromDB() {
		final List<DTOFoerderschwerpunkt> katalog = conn.queryAll(DTOFoerderschwerpunkt.class);
		if (katalog == null)
			throw OperationError.NOT_FOUND.exception();
		return katalog.stream().map(dtoMapper).toList();
	}


	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(final Long id) {
		final DTOFoerderschwerpunkt fs = conn.queryByKey(DTOFoerderschwerpunkt.class, id);
		if (fs == null)
			throw OperationError.NOT_FOUND.exception();
		final FoerderschwerpunktEintrag daten = dtoMapper.apply(fs);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		// TODO
		throw new UnsupportedOperationException();
	}

}
