package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurtermin}.
 */
public final class DataGostKlausurenTermin extends DataManager<Long> {

	private final int _abiturjahr;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurtermin}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 */
	public DataGostKlausurenTermin(final DBEntityManager conn, final int abiturjahr) {
		super(conn);
		_abiturjahr = abiturjahr;
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenTermine} in einen Core-DTO {@link GostKlausurtermin}.
	 */
	private final Function<DTOGostKlausurenTermine, GostKlausurtermin> dtoMapper = (final DTOGostKlausurenTermine z) -> {
		final GostKlausurtermin daten = new GostKlausurtermin();
		daten.id = z.ID;
		daten.abijahr = z.Abi_Jahrgang;
		daten.datum = z.Datum;
		daten.halbjahr = z.Halbjahr.id;
		daten.quartal = z.Quartal;
		daten.startzeit = z.Startzeit;
		daten.bezeichnung = z.Bezeichnung;
		daten.bemerkung = z.Bemerkungen;
		return daten;
	};

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 *
	 * @return die Liste der Kursklausuren
	 */
	private List<GostKlausurtermin> getKlausurtermine(final int halbjahr) {
		final List<DTOGostKlausurenTermine> termine = conn.query("SELECT t FROM DTOGostKlausurenTermine t WHERE t.Abi_Jahrgang = :jgid AND t.Halbjahr = :hj", DTOGostKlausurenTermine.class)
				.setParameter("jgid", _abiturjahr).setParameter("hj", GostHalbjahr.fromID(halbjahr)).getResultList();
		final List<GostKlausurtermin> daten = new ArrayList<>();
		for (final DTOGostKlausurenTermine z : termine)
			daten.add(dtoMapper.apply(z));
		return daten;
	}

	@Override
	public Response get(final Long halbjahr) {
		// Kursklausuren für einen Abiturjahrgang in einem Gost-Halbjahr
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKlausurtermine(halbjahr.intValue())).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			try {
				conn.transactionBegin();
				final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, id);
				if (termin == null)
					throw OperationError.NOT_FOUND.exception();
				for (final Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					switch (key) {
					case "id" -> {
						final Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "abijahr" -> {
						final int patch_abijahr = JSONMapper.convertToInteger(value, false);
						if ((patch_abijahr != termin.Abi_Jahrgang))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "halbjahr" -> {
						final int patch_halbjahr = JSONMapper.convertToInteger(value, false);
						if ((patch_halbjahr != termin.Halbjahr.id))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "quartal" -> {
						final int patch_quartal = JSONMapper.convertToInteger(value, false);
						if ((patch_quartal != termin.Quartal))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "bemerkung" -> termin.Bemerkungen = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Termine.col_Bemerkungen.datenlaenge());
					case "bezeichnung" -> termin.Bezeichnung = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Termine.col_Bezeichnung.datenlaenge());
					case "datum" -> termin.Datum = JSONMapper.convertToString(value, true, false, null);
					case "startzeit" -> termin.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);

					default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(termin);
				conn.transactionCommit();
			} catch (final Exception e) {
				if (e instanceof final WebApplicationException webAppException)
					return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
			} finally {
				// Perform a rollback if necessary
				conn.transactionRollback();
			}
		}
		return Response.status(Status.OK).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

//	public static GostKlausurtermin createTerminNoTransaction() {
//		return null;
//	}

	/**
	 * Erstellt einen neuen Gost-Klausurtermin *
	 *
	 * @param halbjahr das Gost-Halbjahr, in dem der Klausurtermin liegen soll
	 * @param quartal  das Quartal, in dem der Klausurtermin liegen soll. param is
	 *                 Das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Gost-Klausurtermin
	 */
	public Response create(final int halbjahr, final int quartal/* , InputStream is */) {
		final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Klausuren_Termine");
		final Long id = lastID == null ? 1 : lastID.MaxID + 1;
		final DTOGostKlausurenTermine termin = new DTOGostKlausurenTermine(id, _abiturjahr, GostHalbjahr.fromID(halbjahr), quartal);
		conn.persist(termin);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(dtoMapper.apply(termin)).build();
	}

	/**
	 * Löscht einen Gost-Klausurtermin *
	 *
	 * @param ids die IDs der zu löschenden Klausurtermine
	 *
	 * @return die Response
	 */
	public Response delete(final List<Long> ids) {
		// Bestimme den Termin
		try {
			conn.transactionBegin();
			for (final long id : ids) {
				final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, id);
				if (termin == null)
					return OperationError.NOT_FOUND.getResponse();
				// Entferne den Termin
				conn.transactionRemove(termin);
			}
			if (!conn.transactionCommit())
				return OperationError.NOT_FOUND.getResponse("Datenbankfehler beim Löschen der Gost-Klausurtermine");
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webApplicationException)
				return webApplicationException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			conn.transactionRollback();
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ids).build();
	}

}
