package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.gost.GostBeratungslehrer;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBeratungslehrer}.
 */
public final class DataGostBeratungslehrer extends DataManager<Long> {

	private final int abijahr;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBeratungslehrer}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahr   der Abi-Jahrgang des Beratungslehrers (ein Lehrer mit der ID kann mehreren Jahrgängen zugeordnet sein)
	 */
	public DataGostBeratungslehrer(final DBEntityManager conn, final Integer abijahr) {
		super(conn);
		if (abijahr == null)
			throw new NullPointerException();
		this.abijahr = abijahr;
	}


	@Override
	public Response getAll() {
		final List<DTOGostJahrgangBeratungslehrer> dtosBeratungslehrer = conn.queryAll(DTOGostJahrgangBeratungslehrer.class);
		if (dtosBeratungslehrer == null)
			return OperationError.NOT_FOUND.getResponse();
		final List<GostBeratungslehrer> daten = getBeratungslehrer(conn, dtosBeratungslehrer);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		final List<DTOGostJahrgangBeratungslehrer> dtosBeratungslehrer = conn.queryNamed("DTOGostJahrgangBeratungslehrer.abi_jahrgang", abijahr, DTOGostJahrgangBeratungslehrer.class);
		if (dtosBeratungslehrer == null)
			return OperationError.NOT_FOUND.getResponse();
		final List<GostBeratungslehrer> daten = getBeratungslehrer(conn, dtosBeratungslehrer);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		final DTOGostJahrgangBeratungslehrer dtoBeratungslehrer = conn.queryByKey(DTOGostJahrgangBeratungslehrer.class, abijahr, id);
		if (dtoBeratungslehrer == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOLehrer dtoLehrer = conn.queryByKey(DTOLehrer.class, id);
		final GostBeratungslehrer daten = getBeratungslehrer(dtoBeratungslehrer, dtoLehrer);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Bestimmt die Core-DTOs für die Beratungslehrer anhand der übergebenen DTOs
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 * @param dtosBeratungslehrer   die Datenbank-DTOs für den Beratungslehrer
	 *
	 * @return eine Liste der Core-DTOs für die Beratungslehrer
	 */
	public static List<GostBeratungslehrer> getBeratungslehrer(final DBEntityManager conn, final List<DTOGostJahrgangBeratungslehrer> dtosBeratungslehrer) {
		// TODO Query nur für Lehrer-IDs, die auch in der Liste der Beratungslehrer enthalten ist
		final Map<Long, DTOLehrer> dtosLehrer = conn.queryAll(DTOLehrer.class).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		final Vector<GostBeratungslehrer> result = new Vector<>();
		if (dtosBeratungslehrer != null)
			for (final DTOGostJahrgangBeratungslehrer dto : dtosBeratungslehrer)
				result.add(getBeratungslehrer(dto, dtosLehrer.get(dto.Lehrer_ID)));
		return result;
	}


	/**
	 * Bestimmt den Core-DTO für den Beratungslehrer anhand der übergebenen DTOs
	 *
	 * @param dto      der Datenbank-DTO für den Beratungslehrer
	 * @param lehrer   der Datenbank-DTO für die allgemeinen Informationen zu dem Lehrer
	 *
	 * @return der Core-DTO für den Beratungslehrer
	 */
	private static GostBeratungslehrer getBeratungslehrer(final DTOGostJahrgangBeratungslehrer dto, final DTOLehrer lehrer) {
		final GostBeratungslehrer result = new GostBeratungslehrer();
		result.id = dto.Lehrer_ID;
		if (lehrer == null)
			return result;
		result.kuerzel = lehrer.Kuerzel;
		result.nachname = lehrer.Nachname;
		result.vorname = lehrer.Vorname;
		return result;
	}

}
