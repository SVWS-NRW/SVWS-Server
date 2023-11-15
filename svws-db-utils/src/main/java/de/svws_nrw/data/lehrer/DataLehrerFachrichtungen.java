package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.core.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.core.types.lehrer.LehrerFachrichtungAnerkennung;
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


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrerLehramtFachrichtung} in einen Core-DTO {@link LehrerFachrichtungEintrag}.
	 */
	private static final Function<DTOLehrerLehramtFachrichtung, LehrerFachrichtungEintrag> dtoMapper = (final DTOLehrerLehramtFachrichtung l) -> {
		final LehrerFachrichtungEintrag daten = new LehrerFachrichtungEintrag();
		daten.id = l.Lehrer_ID;
		final LehrerFachrichtung fachrichtung = LehrerFachrichtung.getByKuerzel(l.FachrKrz);
		daten.idFachrichtung = (fachrichtung == null) ? null : fachrichtung.daten.id;
		final LehrerFachrichtungAnerkennung anerkennung = LehrerFachrichtungAnerkennung.getByKuerzel(l.FachrAnerkennungKrz);
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
		final List<DTOLehrerLehramtFachrichtung> daten = conn.queryNamed("DTOLehrerLehramtFachrichtung.lehrer_id", idLehrer, DTOLehrerLehramtFachrichtung.class);
    	if (daten == null)
    		return result;
    	// Konvertiere sie und füge sie zur Liste hinzu
    	for (final DTOLehrerLehramtFachrichtung l : daten)
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
