package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigungAnerkennung;
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


	private static LehrerLehrbefaehigungEintrag map(final DBEntityManager conn, final DTOLehrerLehramtBefaehigung l) {
		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		final LehrerLehrbefaehigungEintrag daten = new LehrerLehrbefaehigungEintrag();
		daten.id = l.Lehrer_ID;
		final LehrerLehrbefaehigung lehrbefaehigung = LehrerLehrbefaehigung.data().getWertByKuerzel(l.LehrbefKrz);
		final LehrerLehrbefaehigungKatalogEintrag eintragLehrbefaehigung = (lehrbefaehigung == null) ? null : lehrbefaehigung.daten(schuljahr);
		daten.idLehrbefaehigung = (eintragLehrbefaehigung == null) ? null : eintragLehrbefaehigung.id;
		final LehrerLehrbefaehigungAnerkennung anerkennung = LehrerLehrbefaehigungAnerkennung.data().getWertByKuerzel(l.LehrbefAnerkennungKrz);
		final LehrerLehrbefaehigungAnerkennungKatalogEintrag eintragAnerkennung = (anerkennung == null) ? null : anerkennung.daten(schuljahr);
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
		final List<DTOLehrerLehramtBefaehigung> daten =
				conn.queryList(DTOLehrerLehramtBefaehigung.QUERY_BY_LEHRER_ID, DTOLehrerLehramtBefaehigung.class, idLehrer);
		if (daten == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerLehramtBefaehigung l : daten)
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
