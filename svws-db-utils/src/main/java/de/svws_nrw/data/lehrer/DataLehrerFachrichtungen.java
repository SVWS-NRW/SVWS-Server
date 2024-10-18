package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtungAnerkennung;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLehramtFachrichtung;
import jakarta.ws.rs.core.Response;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerFachrichtungEintrag}.
 */
public final class DataLehrerFachrichtungen extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerFachrichtungEintrag}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerFachrichtungen(final DBEntityManager conn) {
		super(conn);
	}


	private static LehrerFachrichtungEintrag map(final DBEntityManager conn, final DTOLehrerLehramtFachrichtung l) {
		final int schuljahr = conn.getUser().schuleGetSchuljahr();
		final LehrerFachrichtungEintrag daten = new LehrerFachrichtungEintrag();
		daten.id = l.Lehrer_ID;
		final LehrerFachrichtung fachrichtung = LehrerFachrichtung.data().getWertByKuerzel(l.FachrKrz);
		final LehrerFachrichtungKatalogEintrag eintragFachrichtung = (fachrichtung == null) ? null : fachrichtung.daten(schuljahr);
		daten.idFachrichtung = (eintragFachrichtung == null) ? null : eintragFachrichtung.id;
		final LehrerFachrichtungAnerkennung anerkennung = LehrerFachrichtungAnerkennung.data().getWertByKuerzel(l.FachrAnerkennungKrz);
		final LehrerFachrichtungAnerkennungKatalogEintrag eintragAnerkennung = (anerkennung == null) ? null : anerkennung.daten(schuljahr);
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
	 * Ermittelt die Fachrichtungen für den Lehrer mit der angegebenen ID und gibt diese zurück.
	 *
	 * @param conn       die Datenbankverbindung zur Abfrage der Fachrichtungen
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste mit den Fachrichtungen
	 */
	public static List<LehrerFachrichtungEintrag> getByLehrerId(final DBEntityManager conn, final Long idLehrer) {
		final List<LehrerFachrichtungEintrag> result = new ArrayList<>();
		// Bestimme die Fachrichtungen des Lehrers
		final List<DTOLehrerLehramtFachrichtung> daten =
				conn.queryList(DTOLehrerLehramtFachrichtung.QUERY_BY_LEHRER_ID, DTOLehrerLehramtFachrichtung.class, idLehrer);
		if (daten == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerLehramtFachrichtung l : daten)
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
