package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurtermin}.
 */
public final class DataGostKlausurenTermin extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurtermin}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenTermin(final DBEntityManager conn) {
		super(conn);
	}

	private static final Set<String> requiredCreateAttributes = Set.of("abijahr", "halbjahr", "quartal");
	private static final Set<String> patchForbiddenAttributes = Set.of("abijahr", "halbjahr", "istHaupttermin");

	private final Map<String, DataBasicMapper<DTOGostKlausurenTermine>> patchMappings =
		Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, false);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw OperationError.BAD_REQUEST.exception();
			}),
			Map.entry("abijahr", (conn, dto, value, map) -> dto.Abi_Jahrgang = JSONMapper.convertToInteger(value, false)),
			Map.entry("halbjahr", (conn, dto, value, map) -> dto.Halbjahr = DataGostKlausurenVorgabe.checkHalbjahr(JSONMapper.convertToInteger(value, false))),
			Map.entry("quartal", (conn, dto, value, map) -> dto.Quartal = DataGostKlausurenVorgabe.checkQuartal(JSONMapper.convertToInteger(value, false))),
			Map.entry("bemerkung", (conn, dto, value, map) -> dto.Bemerkungen = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Termine.col_Bemerkungen.datenlaenge())),
			Map.entry("bezeichnung", (conn, dto, value, map) -> dto.Bezeichnung = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Klausuren_Termine.col_Bezeichnung.datenlaenge())),
			Map.entry("datum", (conn, dto, value, map) -> dto.Datum = JSONMapper.convertToString(value, true, false, null)),
			Map.entry("startzeit", (conn, dto, value, map) -> dto.Startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440)),
			Map.entry("istHaupttermin", (conn, dto, value, map) -> dto.IstHaupttermin = JSONMapper.convertToBoolean(value, false)),
			Map.entry("nachschreiberZugelassen", (conn, dto, value, map) -> {
				boolean newValue = JSONMapper.convertToBoolean(value, false);
				if (dto.NachschreiberZugelassen != null && dto.NachschreiberZugelassen.booleanValue() && !newValue && !DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuTerminids(conn, ListUtils.create1(dto.ID)).isEmpty())
					throw OperationError.FORBIDDEN.exception("Klausurtermin enthält Nachschreibklausuren");
				dto.NachschreiberZugelassen = newValue;
			})
		);

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenTermine} in einen Core-DTO {@link GostKlausurtermin}.
	 */
	public static final Function<DTOGostKlausurenTermine, GostKlausurtermin> dtoMapper = (final DTOGostKlausurenTermine z) -> {
		final GostKlausurtermin daten = new GostKlausurtermin();
		daten.id = z.ID;
		daten.abijahr = z.Abi_Jahrgang;
		daten.datum = z.Datum;
		daten.halbjahr = z.Halbjahr.id;
		daten.quartal = z.Quartal;
		daten.startzeit = z.Startzeit;
		daten.bezeichnung = z.Bezeichnung;
		daten.bemerkung = z.Bemerkungen;
		daten.nachschreiberZugelassen = z.NachschreiberZugelassen;
		daten.istHaupttermin = z.IstHaupttermin;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurtermine zu den übergebenen Kursklausuren zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param kursKlausuren die Liste der Kursklausuren, zu denen die Klausurtermine gesucht werden.
	 *
	 * @return die Liste der Klausurtermine
	 */
	public static List<GostKlausurtermin> getKlausurtermineZuKursklausuren(final DBEntityManager conn, final List<GostKursklausur> kursKlausuren) {
		if (kursKlausuren.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenTermine> terminDTOs = conn.queryNamed("DTOGostKlausurenTermine.id.multiple", kursKlausuren.stream().map(kk -> kk.idTermin).toList(), DTOGostKlausurenTermine.class);
		return terminDTOs.stream().map(dtoMapper::apply).toList();
	}

	/**
	 * Gibt die Liste der Klausurtermine zu den übergebenen Schülerklausurterminen zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param schuelerklausurTermine die Liste der Schülerklausurterminen, zu denen die Klausurtermine gesucht werden.
	 *
	 * @return die Liste der Klausurtermine
	 */
	public static List<GostKlausurtermin> getKlausurtermineZuSchuelerklausurterminen(final DBEntityManager conn, final List<GostSchuelerklausurTermin> schuelerklausurTermine) {
		if (schuelerklausurTermine.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenTermine> terminDTOs = conn.queryNamed("DTOGostKlausurenTermine.id.multiple", schuelerklausurTermine.stream().map(skt -> skt.idTermin).toList(), DTOGostKlausurenTermine.class);
		return terminDTOs.stream().map(dtoMapper::apply).toList();
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr 	das Abiturjahr
	 * @param halbjahr das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Termine für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 *
	 * @return die Liste der Kursklausuren
	 */
	public static List<GostKlausurtermin> getKlausurtermine(final DBEntityManager conn, final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr) {
		final GostHalbjahr ghj = DataGostKlausurenVorgabe.checkHalbjahr(halbjahr);
		List<GostKlausurtermin> termine = null;
		if (halbjahr <= 0)
			termine = conn.query("SELECT t FROM DTOGostKlausurenTermine t WHERE t.Abi_Jahrgang = :jgid", DTOGostKlausurenTermine.class)
				.setParameter("jgid", abiturjahr)
				.getResultList().stream()
				.map(dtoMapper::apply)
				.toList();
		else {
			termine = conn.query("SELECT t FROM DTOGostKlausurenTermine t WHERE t.Abi_Jahrgang = :jgid AND t.Halbjahr IN :hj", DTOGostKlausurenTermine.class)
			.setParameter("jgid", abiturjahr)
			.setParameter("hj", Arrays.asList(ganzesSchuljahr ? ghj.getSchuljahr() : new GostHalbjahr[]{ghj}))
			.getResultList().stream()
			.map(dtoMapper::apply)
			.toList();
		}
		return termine;
	}

	/**
	 * Gibt den Klausurtermin zur übergebenen ID zurück oder eine Exception, falls er nicht in der DB vorhanden ist.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return der Klausurtermin
	 */
	public static GostKlausurtermin getKlausurterminZuId(final DBEntityManager conn, final long idTermin) {
		final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, idTermin);
		if (termin == null)
			throw OperationError.BAD_REQUEST.exception("Klausurtermin nicht gefunden, ID: " + idTermin);
		return dtoMapper.apply(termin);
	}

	/**
	 * Gibt die Klausurtermine zur übergebenen ID-Liste zurück oder eine Exception, falls er nicht in der DB vorhanden ist.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param listIds	 die Liste von IDs der Klausurtermine
	 *
	 * @return der Klausurtermin
	 */
	public static List<GostKlausurtermin> getKlausurtermineZuIds(final DBEntityManager conn, final List<Long> listIds) {
		if (listIds.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenTermine> termine = conn.queryNamed("DTOGostKlausurenTermine.id.multiple", listIds, DTOGostKlausurenTermine.class);
		return termine.stream().map(DataGostKlausurenTermin.dtoMapper::apply).toList();
	}


	@Override
	public Response get(final Long halbjahr) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasicFiltered(id, is, DTOGostKlausurenTermine.class, patchMappings, patchForbiddenAttributes);
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Erstellt einen neuen Gost-Klausurtermin *
	 *
	 * @param is   Das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Gost-Klausurtermin
	 */
	public Response create(final InputStream is) {
		final ObjLongConsumer<DTOGostKlausurenTermine> initDTO = (dto, id) -> dto.ID = id;
		return super.addBasic(is, DTOGostKlausurenTermine.class, initDTO, dtoMapper, requiredCreateAttributes, patchMappings);
	}


	/**
	 * Löscht eine Gost-Klausurvorgabe *
	 *
	 * @param id die ID der zu löschenden Klausurvorgabe
	 *
	 * @return die Response
	 */
	public Response delete(final Long id) {
		return super.deleteBasic(id, DTOGostKlausurenTermine.class, dtoMapper);
	}

	/**
	 * Löscht einen Gost-Klausurtermin *
	 *
	 * @param ids die IDs der zu löschenden Klausurtermine
	 *
	 * @return die Response
	 */
	public Response delete(final List<Long> ids) {
		return super.deleteBasicMultiple(ids, DTOGostKlausurenTermine.class, dtoMapper);
	}

}
