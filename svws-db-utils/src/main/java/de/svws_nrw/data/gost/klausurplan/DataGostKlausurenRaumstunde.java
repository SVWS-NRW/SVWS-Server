package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaumstunden;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurraumstunde}.
 */
public final class DataGostKlausurenRaumstunde extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurraumstunde}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenRaumstunde(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaumstunden} in einen Core-DTO
	 * {@link GostKlausurraumstunde}.
	 */
	public static final Function<DTOGostKlausurenRaumstunden, GostKlausurraumstunde> dtoMapper = (final DTOGostKlausurenRaumstunden z) -> {
		final GostKlausurraumstunde daten = new GostKlausurraumstunde();
		daten.id = z.ID;
		daten.idRaum = z.Klausurraum_ID;
		daten.idZeitraster = z.Zeitraster_ID;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der Klausurraumstunden
	 */
	private List<GostKlausurraumstunde> getKlausurraumstunden(final Long idTermin) {

		final List<GostKlausurraum> listRaeume = new DataGostKlausurenRaum(conn).getKlausurraeume(idTermin);

		if (listRaeume.isEmpty()) {
			return new ArrayList<>();
		}

		final List<DTOGostKlausurenRaumstunden> stunden = conn.queryNamed("DTOGostKlausurenRaeumeStunden.klausurraum_id.multiple", listRaeume.stream().map(s -> s.idTermin).toList(), DTOGostKlausurenRaumstunden.class);

		final List<GostKlausurraumstunde> daten = new ArrayList<>();
		for (final DTOGostKlausurenRaumstunden s : stunden)
			daten.add(dtoMapper.apply(s));
		return daten;
	}

	@Override
	public Response get(final Long idTermin) {
		// Klausurraumstunden zu einem Klausurtermin
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKlausurraumstunden(idTermin)).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


}
