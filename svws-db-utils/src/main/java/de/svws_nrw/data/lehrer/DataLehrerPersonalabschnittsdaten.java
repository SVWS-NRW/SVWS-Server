package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.core.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.core.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.core.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerPersonalabschnittsdaten}.
 */
public final class DataLehrerPersonalabschnittsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerPersonalabschnittsdaten}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonalabschnittsdaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrer} in einen Core-DTO {@link LehrerPersonalabschnittsdaten}.
	 */
	private static final Function<DTOLehrerAbschnittsdaten, LehrerPersonalabschnittsdaten> dtoMapper = (final DTOLehrerAbschnittsdaten lehrer) -> {
		final LehrerPersonalabschnittsdaten daten = new LehrerPersonalabschnittsdaten();
		daten.id = lehrer.ID;
		daten.idLehrer = lehrer.Lehrer_ID;
		daten.idSchuljahresabschnitt = lehrer.Schuljahresabschnitts_ID;
		daten.pflichtstundensoll = lehrer.PflichtstdSoll;
		daten.rechtsverhaeltnis = lehrer.Rechtsverhaeltnis;
		daten.beschaeftigungsart = lehrer.Beschaeftigungsart;
		daten.einsatzstatus = lehrer.Einsatzstatus;
		daten.stammschulnummer = lehrer.StammschulNr;
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
	 * Ermittelt die Abschnittsdaten für den Lehrer mit der angegebenen ID und und gibt diese zurück.
	 *
	 * @param conn       die Datenbankverbindung zur Abfrage der Abschnittsdaten
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste mit den Abschnittsdaten
	 */
	public static List<LehrerPersonalabschnittsdaten> getByLehrerId(final DBEntityManager conn, final Long idLehrer) {
		final List<LehrerPersonalabschnittsdaten> result = new ArrayList<>();
    	// Bestimme die Abschnittsdaten des Lehrers
		final List<DTOLehrerAbschnittsdaten> abschnittsdaten = conn.queryNamed("DTOLehrerAbschnittsdaten.lehrer_id", idLehrer, DTOLehrerAbschnittsdaten.class);
    	if (abschnittsdaten == null)
    		return result;
    	// Konvertiere sie und füge sie zur Liste hinzu
    	for (final DTOLehrerAbschnittsdaten l : abschnittsdaten) {
    		final LehrerPersonalabschnittsdaten daten = dtoMapper.apply(l);
    		daten.anrechnungen.addAll(DataLehrerPersonalabschnittsdatenAnrechungen.getByLehrerabschnittsdatenId(conn, l.ID));
    		daten.mehrleistung.addAll(DataLehrerPersonalabschnittsdatenMehrleistungen.getByLehrerabschnittsdatenId(conn, l.ID));
    		daten.minderleistung.addAll(DataLehrerPersonalabschnittsdatenMinderleistungen.getByLehrerabschnittsdatenId(conn, l.ID));
    		daten.funktionen.addAll(DataLehrerPersonalabschnittsdatenLehrerfunktionen.getByLehrerabschnittsdatenId(conn, l.ID));
    		result.add(daten);
    	}
    	return result;
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOLehrerAbschnittsdaten abschnittsdaten = conn.queryByKey(DTOLehrerAbschnittsdaten.class, id);
    	if (abschnittsdaten == null)
    		return OperationError.NOT_FOUND.getResponse();
		final LehrerPersonalabschnittsdaten daten = dtoMapper.apply(abschnittsdaten);
		daten.anrechnungen.addAll(DataLehrerPersonalabschnittsdatenAnrechungen.getByLehrerabschnittsdatenId(conn, id));
		daten.mehrleistung.addAll(DataLehrerPersonalabschnittsdatenMehrleistungen.getByLehrerabschnittsdatenId(conn, id));
		daten.minderleistung.addAll(DataLehrerPersonalabschnittsdatenMinderleistungen.getByLehrerabschnittsdatenId(conn, id));
		daten.funktionen.addAll(DataLehrerPersonalabschnittsdatenLehrerfunktionen.getByLehrerabschnittsdatenId(conn, id));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private final Map<String, DataBasicMapper<DTOLehrerAbschnittsdaten>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, abschnittsdaten, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != abschnittsdaten.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("pflichtstundensoll", (conn, abschnittsdaten, value, map) -> {
			abschnittsdaten.PflichtstdSoll = JSONMapper.convertToDouble(value, true);
		}),
		Map.entry("rechtsverhaeltnis", (conn, abschnittsdaten, value, map) -> {
			final String strData = JSONMapper.convertToString(value, true, false, null);
			if (strData == null) {
				abschnittsdaten.Rechtsverhaeltnis = null;
			} else {
				final LehrerRechtsverhaeltnis rv = LehrerRechtsverhaeltnis.getByKuerzel(strData);
				if (rv == null)
					throw OperationError.NOT_FOUND.exception();
				abschnittsdaten.Rechtsverhaeltnis = rv.daten.kuerzel;
			}
		}),
		Map.entry("beschaeftigungsart", (conn, abschnittsdaten, value, map) -> {
			final String strData = JSONMapper.convertToString(value, true, false, null);
			if (strData == null) {
				abschnittsdaten.Beschaeftigungsart = null;
			} else {
				final LehrerBeschaeftigungsart ba = LehrerBeschaeftigungsart.getByKuerzel(strData);
				if (ba == null)
					throw OperationError.NOT_FOUND.exception();
				abschnittsdaten.Beschaeftigungsart = ba.daten.kuerzel;
			}
		}),
		Map.entry("einsatzstatus", (conn, abschnittsdaten, value, map) -> {
			final String strData = JSONMapper.convertToString(value, true, false, null);
			if (strData == null) {
				abschnittsdaten.Einsatzstatus = null;
			} else {
				final LehrerEinsatzstatus es = LehrerEinsatzstatus.getByKuerzel(strData);
				if (es == null)
					throw OperationError.NOT_FOUND.exception();
				abschnittsdaten.Einsatzstatus = es.daten.kuerzel;
			}
		}),
		Map.entry("stammschulnummer", (conn, lehrer, value, map) -> {
			lehrer.StammschulNr = JSONMapper.convertToString(value, true, false, Schema.tab_LehrerAbschnittsdaten.col_StammschulNr.datenlaenge());
		})
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOLehrerAbschnittsdaten.class, patchMappings);
	}

}
