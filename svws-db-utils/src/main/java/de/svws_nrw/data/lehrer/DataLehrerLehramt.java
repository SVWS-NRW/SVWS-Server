package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.core.types.lehrer.LehrerLehramt;
import de.svws_nrw.core.types.lehrer.LehrerLehramtAnerkennung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLehramt;
import jakarta.ws.rs.core.Response;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerLehramtEintrag}.
 */
public final class DataLehrerLehramt extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerLehramtEintrag}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerLehramt(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrerLehramt} in einen Core-DTO {@link LehrerLehramtEintrag}.
	 */
	private static final Function<DTOLehrerLehramt, LehrerLehramtEintrag> dtoMapper = (final DTOLehrerLehramt l) -> {
		final LehrerLehramtEintrag daten = new LehrerLehramtEintrag();
		daten.id = l.Lehrer_ID;
		final LehrerLehramt lehramt = LehrerLehramt.getByKuerzel(l.LehramtKrz);
		daten.idLehramt = (lehramt == null) ? null : lehramt.daten.id;
		final LehrerLehramtAnerkennung anerkennung = LehrerLehramtAnerkennung.getByKuerzel(l.LehramtAnerkennungKrz);
		daten.idAnerkennungsgrund = (anerkennung == null) ? null : anerkennung.daten.id;
		return daten;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt die Lehrämter für den Lehrer mit der angegebenen ID und gibt diese zurück.
	 *
	 * @param conn       die Datenbankverbindung zur Abfrage der Lehrämter
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste mit den Lehrämtern
	 */
	public static List<LehrerLehramtEintrag> getByLehrerId(final DBEntityManager conn, final Long idLehrer) {
		final List<LehrerLehramtEintrag> result = new ArrayList<>();
    	// Bestimme die Lehrämter des Lehrers
		final List<DTOLehrerLehramt> daten = conn.queryNamed("DTOLehrerLehramt.lehrer_id", idLehrer, DTOLehrerLehramt.class);
    	if (daten == null)
    		return result;
    	// Konvertiere sie und füge sie zur Liste hinzu
    	for (final DTOLehrerLehramt l : daten)
    		result.add(dtoMapper.apply(l));
    	return result;
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
