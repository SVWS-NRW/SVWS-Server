package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung;
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


	private static LehrerLehramtEintrag map(final DBEntityManager conn, final DTOLehrerLehramt l) {
		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		final LehrerLehramtEintrag daten = new LehrerLehramtEintrag();
		daten.id = l.Lehrer_ID;
		final LehrerLehramt lehramt = LehrerLehramt.data().getWertByKuerzel(l.LehramtKrz);
		final LehrerLehramtKatalogEintrag eintragLehramt = (lehramt == null) ? null : lehramt.daten(schuljahr);
		daten.idLehramt = (eintragLehramt == null) ? null : eintragLehramt.id;
		final LehrerLehramtAnerkennung anerkennung = LehrerLehramtAnerkennung.data().getWertByKuerzel(l.LehramtAnerkennungKrz);
		final LehrerLehramtAnerkennungKatalogEintrag eintragAnerkennung = (anerkennung == null) ? null : anerkennung.daten(schuljahr);
		daten.idAnerkennungsgrund = (eintragAnerkennung == null) ? null : eintragAnerkennung.id;
		return daten;
	}

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
		final List<DTOLehrerLehramt> daten = conn.queryList(DTOLehrerLehramt.QUERY_BY_LEHRER_ID, DTOLehrerLehramt.class, idLehrer);
		if (daten == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerLehramt l : daten)
			result.add(map(conn, l));
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
