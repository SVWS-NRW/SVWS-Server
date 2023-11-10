package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.core.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.core.types.lehrer.LehrerLehrbefaehigungAnerkennung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLehramtBefaehigung;
import jakarta.ws.rs.core.Response;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerLehrbefaehigungEintrag}.
 */
public final class DataLehrerLehrbefaehigung extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerLehrbefaehigungEintrag}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerLehrbefaehigung(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrerLehramtBefaehigung} in einen Core-DTO {@link LehrerLehrbefaehigungEintrag}.
	 */
	private static final Function<DTOLehrerLehramtBefaehigung, LehrerLehrbefaehigungEintrag> dtoMapper = (final DTOLehrerLehramtBefaehigung l) -> {
		final LehrerLehrbefaehigungEintrag daten = new LehrerLehrbefaehigungEintrag();
		daten.id = l.Lehrer_ID;
		final LehrerLehrbefaehigung lehrbefaehigung = LehrerLehrbefaehigung.getByKuerzel(l.LehrbefKrz);
		daten.idLehrbefaehigung = (lehrbefaehigung == null) ? null : lehrbefaehigung.daten.id;
		final LehrerLehrbefaehigungAnerkennung anerkennung = LehrerLehrbefaehigungAnerkennung.getByKuerzel(l.LehrbefAnerkennungKrz);
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
	 * Ermittelt die Lehrbefähigungen für den Lehrer mit der angegebenen ID und gibt diese zurück.
	 *
	 * @param conn       die Datenbankverbindung zur Abfrage der Lehrbefähigungen
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste mit den Lehrbefähigungen
	 */
	public static List<LehrerLehrbefaehigungEintrag> getByLehrerId(final DBEntityManager conn, final Long idLehrer) {
		final List<LehrerLehrbefaehigungEintrag> result = new ArrayList<>();
    	// Bestimme die Lehrbefähigungen des Lehrers
		final List<DTOLehrerLehramtBefaehigung> daten = conn.queryNamed("DTOLehrerLehramtBefaehigung.lehrer_id", idLehrer, DTOLehrerLehramtBefaehigung.class);
    	if (daten == null)
    		return result;
    	// Konvertiere sie und füge sie zur Liste hinzu
    	for (final DTOLehrerLehramtBefaehigung l : daten)
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
