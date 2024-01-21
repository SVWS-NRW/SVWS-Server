package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde;
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
	 * Gibt die Liste der Klausurraumstunden zu einem Klausurtermin zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der Klausurraumstunden
	 */
	public static List<GostKlausurraumstunde> getKlausurraumstundenZuTermin(final DBEntityManager conn, final Long idTermin) {
		final List<GostKlausurraum> listRaeume = DataGostKlausurenRaum.getKlausurraeumeZuTermin(conn, idTermin);
		if (listRaeume.isEmpty())
			return new ArrayList<>();
		return getKlausurraumstundenZuRaeumen(conn, listRaeume);
	}

	/**
	 * Gibt die Liste der Klausurraumstunden zu einer Liste von Räumen zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param listRaeume die Liste der Klausurräume
	 *
	 * @return die Liste der Klausurraumstunden
	 */
	public static List<GostKlausurraumstunde> getKlausurraumstundenZuRaeumen(final DBEntityManager conn, final List<GostKlausurraum> listRaeume) {
		if (listRaeume.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenRaumstunden> stunden = conn.queryNamed("DTOGostKlausurenRaumstunden.klausurraum_id.multiple", listRaeume.stream().map(s -> s.id).toList(), DTOGostKlausurenRaumstunden.class);
		return stunden.stream().map(dtoMapper::apply).toList();
	}

	/**
	 * Gibt die Liste der Klausurraumstunden zu einer Raumid zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idRaum	 die ID des Raums
	 *
	 * @return die Liste der Klausurraumstunden
	 */
	public static List<GostKlausurraumstunde> getKlausurraumstundenZuRaumid(final DBEntityManager conn, final long idRaum) {
		final List<DTOGostKlausurenRaumstunden> listKlausurraumstunden = conn.queryNamed("DTOGostKlausurenRaumstunden.klausurraum_id", idRaum, DTOGostKlausurenRaumstunden.class);
		return listKlausurraumstunden.stream().map(dtoMapper::apply).toList();
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminraumstunden die Klausurraumstunden
	 *
	 * @param conn    x
	 * @param listSktrs die Liste der GostSchuelerklausurterminraumstunden
	 *
	 * @return die Liste der zugehörigen Klausurraumstunden-Objekte
	 */
	public static List<GostKlausurraumstunde> getKlausurraumstundenZuSchuelerklausurterminraumstunden(final DBEntityManager conn, final List<GostSchuelerklausurterminraumstunde> listSktrs) {
		if (listSktrs.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenRaumstunden> sks = conn.queryNamed("DTOGostKlausurenRaumstunden.id.multiple", listSktrs.stream().map(sktrs -> sktrs.idRaumstunde).toList(), DTOGostKlausurenRaumstunden.class);
		return sks.stream().map(dtoMapper::apply).toList();
	}

	@Override
	public Response get(final Long idTermin) {
		// Klausurraumstunden zu einem Klausurtermin
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getKlausurraumstundenZuTermin(conn, idTermin)).build();
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
