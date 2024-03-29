package de.svws_nrw.data.lehrer;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.core.types.lehrer.LehrerAbgangsgrund;
import de.svws_nrw.core.types.lehrer.LehrerZugangsgrund;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link LehrerPersonaldaten}.
 */
public final class DataLehrerPersonaldaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link LehrerPersonaldaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLehrerPersonaldaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOLehrer} in einen Core-DTO {@link LehrerPersonaldaten}.
	 */
	private final Function<DTOLehrer, LehrerPersonaldaten> dtoMapper = (final DTOLehrer lehrer) -> {
		final LehrerPersonaldaten daten = new LehrerPersonaldaten();
		daten.id = lehrer.ID;
		daten.identNrTeil1 = lehrer.identNrTeil1;
		daten.identNrTeil2SerNr = lehrer.identNrTeil2SerNr;
		daten.personalaktennummer = lehrer.PANr;
		daten.lbvPersonalnummer = lehrer.personalNrLBV;
		daten.lbvVerguetungsschluessel = lehrer.verguetungsSchluessel;
		daten.zugangsdatum = lehrer.DatumZugang;
		daten.zugangsgrund = lehrer.GrundZugang;
		daten.abgangsdatum = lehrer.DatumAbgang;
		daten.abgangsgrund = lehrer.GrundAbgang;
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

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
    	final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
    	if (lehrer == null)
    		throw new ApiOperationException(Status.NOT_FOUND);
		final LehrerPersonaldaten daten = dtoMapper.apply(lehrer);
		daten.abschnittsdaten.addAll(DataLehrerPersonalabschnittsdaten.getByLehrerId(conn, id));
		daten.lehraemter.addAll(DataLehrerLehramt.getByLehrerId(conn, id));
		daten.fachrichtungen.addAll(DataLehrerFachrichtungen.getByLehrerId(conn, id));
		daten.lehrbefaehigungen.addAll(DataLehrerLehrbefaehigung.getByLehrerId(conn, id));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private final Map<String, DataBasicMapper<DTOLehrer>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, lehrer, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != lehrer.ID))
				throw new ApiOperationException(Status.BAD_REQUEST);
		}),
		Map.entry("identNrTeil1", (conn, lehrer, value, map) -> {
			lehrer.identNrTeil1 = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_IdentNr1.datenlaenge());
		}),
		Map.entry("identNrTeil2SerNr", (conn, lehrer, value, map) -> {
			lehrer.identNrTeil2SerNr = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_SerNr.datenlaenge());
		}),
		Map.entry("personalaktennummer", (conn, lehrer, value, map) -> {
			lehrer.PANr = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_PANr.datenlaenge());
		}),
		Map.entry("lbvPersonalnummer", (conn, lehrer, value, map) -> {
			lehrer.personalNrLBV = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_LBVNr.datenlaenge());
		}),
		Map.entry("lbvVerguetungsschluessel", (conn, lehrer, value, map) -> {
			lehrer.verguetungsSchluessel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Lehrer.col_VSchluessel.datenlaenge());
		}),
		Map.entry("zugangsdatum", (conn, lehrer, value, map) -> {
			// TODO Datumsformat überprüfen
			lehrer.DatumZugang = JSONMapper.convertToString(value, true, false, null);
		}),
		Map.entry("zugangsgrund", (conn, lehrer, value, map) -> {
			final String strData = JSONMapper.convertToString(value, true, false, null);
			if (strData == null) {
				lehrer.GrundZugang = null;
			} else {
				final LehrerZugangsgrund zg = LehrerZugangsgrund.getByKuerzel(strData);
				if (zg == null)
					throw new ApiOperationException(Status.NOT_FOUND);
				lehrer.GrundZugang = zg.daten.kuerzel;
			}
		}),
		Map.entry("abgangsdatum", (conn, lehrer, value, map) -> {
			// TODO Datumsformat überprüfen
			lehrer.DatumAbgang = JSONMapper.convertToString(value, true, false, null);
		}),
		Map.entry("abgangsgrund", (conn, lehrer, value, map) -> {
			final String strData = JSONMapper.convertToString(value, true, false, null);
			if (strData == null) {
				lehrer.GrundAbgang = null;
			} else {
				final LehrerAbgangsgrund ag = LehrerAbgangsgrund.getByKuerzel(strData);
				if (ag == null)
					throw new ApiOperationException(Status.NOT_FOUND);
				lehrer.GrundAbgang = ag.daten.kuerzel;
			}
		})
	);

	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOLehrer.class, patchMappings);
	}

}
