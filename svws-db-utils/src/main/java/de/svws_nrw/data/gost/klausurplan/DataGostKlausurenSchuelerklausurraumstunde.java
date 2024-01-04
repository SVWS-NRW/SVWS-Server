package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrs;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurraumManager;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurvorgabenManager;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKursklausurManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanListUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeume;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermineRaumstunden;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostSchuelerklausurterminraumstunde}.
 */
public final class DataGostKlausurenSchuelerklausurraumstunde extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostSchuelerklausurterminraumstunde}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausurraumstunde(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	private static long ermittleRaumidAusSchuelerklausuren(final DBEntityManager conn, final List<Long> idsSchuelerklausuren) {
		final List<Long> idsRaumstunden = conn
				.queryNamed("DTOGostKlausurenSchuelerklausurenTermineRaumstunden.schuelerklausurtermin_id.multiple", idsSchuelerklausuren, DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class).stream()
				.map(skrs -> skrs.Raumstunde_ID).distinct().toList();
		final List<Long> idsRaeume = conn.queryNamed("DTOGostKlausurenRaumstunden.id.multiple", idsRaumstunden, DTOGostKlausurenRaumstunden.class).stream().map(krs -> krs.Klausurraum_ID)
				.distinct().toList();
		if (idsRaeume.size() != 1)
			return -1;
		return idsRaeume.get(0);
	}

	private static List<GostSchuelerklausurTermin> ermittleSchuelerklausurenSchonImRaum(final DBEntityManager conn, final long idRaum) {
		final List<DTOGostKlausurenRaumstunden> listKlausurraumstunden = conn.queryNamed("DTOGostKlausurenRaumstunden.klausurraum_id", idRaum, DTOGostKlausurenRaumstunden.class);
		if (!listKlausurraumstunden.isEmpty()) {
			final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> listSchuelerklausurtermineSchonImRaumRaumstunden = conn.queryNamed(
					"DTOGostKlausurenSchuelerklausurenTermineRaumstunden.raumstunde_id.multiple", listKlausurraumstunden.stream().map(krs -> krs.ID).toList(),
					DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class);
			if (!listSchuelerklausurtermineSchonImRaumRaumstunden.isEmpty()) {
				return DataGostKlausurenSchuelerklausur.preprocessSchuelerklausurenTermine(conn, conn.queryNamed("DTOGostKlausurenSchuelerklausurenTermine.id.multiple", listSchuelerklausurtermineSchonImRaumRaumstunden.stream().map(skrs -> skrs.Schuelerklausurtermin_ID).distinct().toList(), DTOGostKlausurenSchuelerklausurenTermine.class));
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn                 x
	 * @param idsSchuelerklausuren die IDs der zuzuweisenden Schülerklausuren
	 *
	 * @return die Antwort
	 */
	public static Response loescheRaumZuSchuelerklausuren(final DBEntityManager conn, final List<Long> idsSchuelerklausuren) {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(loescheRaumZuSchuelerklausurenTransaction(conn, idsSchuelerklausuren)).build();
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn                 x
	 * @param idsSchuelerklausuren die IDs der zuzuweisenden Schülerklausuren
	 *
	 * @return die Antwort
	 */
	public static GostKlausurenCollectionSkrsKrs loescheRaumZuSchuelerklausurenTransaction(final DBEntityManager conn, final List<Long> idsSchuelerklausuren) {
		final GostKlausurenCollectionSkrsKrs result = new GostKlausurenCollectionSkrsKrs();
		result.idsSchuelerklausuren = idsSchuelerklausuren;

		final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> stundenAlt = conn.queryList("SELECT e FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden e WHERE e.Schuelerklausurtermin_ID IN ?1",
				DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class, idsSchuelerklausuren);
		conn.transactionRemoveAll(stundenAlt);
		conn.transactionFlush();
		result.raumstundenGeloescht = removeRaumStundenInDb(conn);

		return result;
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn                 x
	 * @param _idRaum              die ID des Klausurraums
	 * @param idsSchuelerklausuren die IDs der zuzuweisenden Schülerklausuren
	 * @param idAbschnitt          die ID des Schuljahresabschnitts
	 *
	 * @return die Antwort
	 */
	public static Response setzeRaumZuSchuelerklausuren(final DBEntityManager conn, final Long _idRaum, final List<Long> idsSchuelerklausuren, final long idAbschnitt) {
		final GostKlausurenCollectionSkrsKrs result = transactionSetzeRaumZuSchuelerklausuren(conn, _idRaum, idsSchuelerklausuren, idAbschnitt);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param conn                 x
	 * @param _idRaum              die ID des Klausurraums
	 * @param idsSchuelerklausuren die IDs der zuzuweisenden Schülerklausuren
	 * @param idAbschnitt          die ID des Schuljahresabschnitts
	 *
	 * @return die Antwort
	 */
	public static GostKlausurenCollectionSkrsKrs transactionSetzeRaumZuSchuelerklausuren(final DBEntityManager conn, final Long _idRaum, final List<Long> idsSchuelerklausuren,
			final long idAbschnitt) {
		if (idsSchuelerklausuren.isEmpty())
			throw OperationError.NOTHING_TO_DO.exception();
		final long idRaum = _idRaum != null ? _idRaum : ermittleRaumidAusSchuelerklausuren(conn, idsSchuelerklausuren);
		if (idRaum == -1)
			throw OperationError.CONFLICT.exception("Verschiedene Raumids in Schuelerklausuren gefunden.");

		// Raum und Termin holen
		final GostKlausurraum raum = DataGostKlausurenRaum.dtoMapper.apply(conn.queryByKey(DTOGostKlausurenRaeume.class, idRaum));
		final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, raum.idTermin);

		// Neue Schülerklausuren ermitteln
		final List<GostSchuelerklausurTermin> listSchuelerklausurenNeu = DataGostKlausurenSchuelerklausur.preprocessSchuelerklausurenTermine(conn, conn.queryNamed("DTOGostKlausurenSchuelerklausurenTermine.id.multiple", idsSchuelerklausuren, DTOGostKlausurenSchuelerklausurenTermine.class));
		if (listSchuelerklausurenNeu.isEmpty())
			throw OperationError.NOTHING_TO_DO.exception();

		// Schon im Raum existente Schülerklausuren ermitteln
		final List<GostSchuelerklausurTermin> listSchuelerklausuren = ermittleSchuelerklausurenSchonImRaum(conn, idRaum);

		final List<GostKursklausur> listKursklausuren = conn.queryNamed("DTOGostKlausurenKursklausuren.id.multiple",
				Stream.concat(listSchuelerklausuren.stream().map(sk -> sk.idKursklausur).distinct(), listSchuelerklausurenNeu.stream().map(sk -> sk.idKursklausur).distinct()).distinct().toList(),
				DTOGostKlausurenKursklausuren.class).stream().map(DataGostKlausurenKursklausur.dtoMapper2::apply).toList();

		if (_idRaum != null) // überarbeiten! null bedeutet: Zeit der Kursklausur wird neu gesetzt, nicht
								// null bedeutet: Raum wird Schülerklausuren zugewiesen
			listSchuelerklausuren.addAll(listSchuelerklausurenNeu);

		final List<GostKlausurraumstunde> listRaumstunden = conn.queryNamed("DTOGostKlausurenRaumstunden.klausurraum_id", idRaum, DTOGostKlausurenRaumstunden.class).stream()
				.map(DataGostKlausurenRaumstunde.dtoMapper::apply).toList();

		// Manager erzeugen
		final List<GostKlausurvorgabe> listVorgaben = conn
				.queryNamed("DTOGostKlausurenVorgaben.id.multiple", listKursklausuren.stream().map(k -> k.idVorgabe).distinct().toList(), DTOGostKlausurenVorgaben.class).stream()
				.map(DataGostKlausurenVorgabe.dtoMapper::apply).toList();
		final GostKlausurvorgabenManager vorgabenManager = new GostKlausurvorgabenManager(listVorgaben, null);
		final GostKursklausurManager kursklausurManager = new GostKursklausurManager(vorgabenManager, listKursklausuren, null, null);
		final GostKlausurraumManager raumManager = new GostKlausurraumManager(raum, listRaumstunden, listSchuelerklausuren, kursklausurManager, DataGostKlausurenTermin.dtoMapper.apply(termin));
		final StundenplanListeEintrag sle = StundenplanListUtils.get(DataStundenplanListe.getStundenplaene(conn, idAbschnitt), termin.Datum);
		final StundenplanManager stundenplanManager = new StundenplanManager(DataStundenplan.getStundenplan(conn, sle.id), new ArrayList<>(), new ArrayList<>(), null);

		// Zeitraster_min und _max ermitteln
		int minStart = 1440;
		int maxEnd = -1;
		for (final GostSchuelerklausurTermin sk : listSchuelerklausuren) {
			final GostKursklausur kk = kursklausurManager.kursklausurGetByIdOrException(sk.idKursklausur);
			int skStartzeit = -1;
			if (sk.startzeit != null)
				skStartzeit = sk.startzeit;
			else if (kk.startzeit != null)
				skStartzeit = kk.startzeit;
			else
				skStartzeit = termin.Startzeit;
			if (skStartzeit < minStart)
				minStart = skStartzeit;
			final GostKlausurvorgabe v = vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
			final int endzeit = skStartzeit + v.dauer + v.auswahlzeit;
			if (endzeit > maxEnd)
				maxEnd = endzeit;
		}

		final LocalDate klausurdatum = LocalDate.parse(termin.Datum);
		final List<StundenplanZeitraster> zeitrasterRaum = stundenplanManager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()), minStart,
				maxEnd - minStart);
		if (zeitrasterRaum.isEmpty())
			throw OperationError.NOTHING_TO_DO.exception("Zeitraster konnte nicht ermittelt werden");

		final GostKlausurenCollectionSkrsKrs result = new GostKlausurenCollectionSkrsKrs();
		result.idsSchuelerklausuren = idsSchuelerklausuren;
		result.idKlausurraum = idRaum;

		result.raumstunden = createRaumStundenInDb(conn, idRaum, zeitrasterRaum, raumManager);
		conn.transactionFlush();
		result.skRaumstunden = createSchuelerklausurraumstundenInDb(conn, listSchuelerklausurenNeu, idRaum, termin, kursklausurManager, vorgabenManager, stundenplanManager, raumManager);
		conn.transactionFlush();
		result.raumstundenGeloescht = removeRaumStundenInDb(conn);

		return result;
	}

	/**
	 * Legt die Raumstunden für den gesamten Raum an (Befüllen der Tabelle
	 * Gost_Klausuren_Raeume_Stunden)
	 *
	 * @param conn           x
	 * @param idRaum         x
	 * @param zeitrasterRaum x
	 * @param raumManager    x
	 * @return Liste
	 */
	private static List<GostKlausurraumstunde> createRaumStundenInDb(final DBEntityManager conn, final long idRaum, final List<StundenplanZeitraster> zeitrasterRaum,
			final GostKlausurraumManager raumManager) {
		// Bestimme die ID der ersten neuen Klausurraumstunde
		long idNextKrs = conn.transactionGetNextID(DTOGostKlausurenRaumstunden.class);
		final List<GostKlausurraumstunde> result = new ArrayList<>();
		for (final StundenplanZeitraster stunde : zeitrasterRaum) {
			if (raumManager.klausurraumstundeGetByRaumidAndZeitrasterid(idRaum, stunde.id) == null) {
				final DTOGostKlausurenRaumstunden dtoStundeNeu = new DTOGostKlausurenRaumstunden(idNextKrs++, idRaum, stunde.id);
				final GostKlausurraumstunde stundeNeu = DataGostKlausurenRaumstunde.dtoMapper.apply(dtoStundeNeu);
				raumManager.raumstundeAdd(stundeNeu);
				result.add(stundeNeu);
				conn.transactionPersist(dtoStundeNeu);
			}
		}
		return result;
	}

	/**
	 * Ermittelt und löscht die nicht mehr benötigten Raumstunden (Aufräumen der
	 * Tabelle Gost_Klausuren_Raeume_Stunden)
	 *
	 * @param conn x
	 * @return Liste x
	 */
	private static List<GostKlausurraumstunde> removeRaumStundenInDb(final DBEntityManager conn) {
		final List<DTOGostKlausurenRaumstunden> stundenAlt = conn.queryList(
				"SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID NOT IN (SELECT w.Raumstunde_ID FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden w)",
				DTOGostKlausurenRaumstunden.class);
		conn.transactionRemoveAll(stundenAlt);
		return stundenAlt.stream().map(DataGostKlausurenRaumstunde.dtoMapper::apply).toList();

	}

	/**
	 * Legt die Schuelerklausurraumstunden für jede Klausur an (Befüllen der Tabelle
	 * Gost_Klausuren_Schuelerklausuren_Raeume_Stunden)
	 *
	 * @param conn                     x
	 * @param listSchuelerklausurenNeu x
	 * @param idRaum                   x
	 * @param termin                   x
	 * @param kursklausurManager       x
	 * @param vorgabenManager          x
	 * @param raumManager              x
	 * @param stundenplanManager       x
	 * @return List
	 */
	private static List<GostSchuelerklausurterminraumstunde> createSchuelerklausurraumstundenInDb(final DBEntityManager conn, final List<GostSchuelerklausurTermin> listSchuelerklausurenNeu, final long idRaum,
			final DTOGostKlausurenTermine termin, final GostKursklausurManager kursklausurManager, final GostKlausurvorgabenManager vorgabenManager, final StundenplanManager stundenplanManager,
			final GostKlausurraumManager raumManager) {
		final List<GostSchuelerklausurterminraumstunde> result = new ArrayList<>();
		final LocalDate klausurdatum = LocalDate.parse(termin.Datum);
		for (final GostSchuelerklausurTermin sk : listSchuelerklausurenNeu) {
			final GostKursklausur kk = kursklausurManager.kursklausurGetByIdOrException(sk.idKursklausur);
			final GostKlausurvorgabe v = vorgabenManager.vorgabeGetByIdOrException(kk.idVorgabe);
			final int startzeit = sk.startzeit != null ? sk.startzeit : (kk.startzeit != null ? kk.startzeit : termin.Startzeit);
			final List<StundenplanZeitraster> zeitrasterSk = stundenplanManager.getZeitrasterByWochentagStartVerstrichen(Wochentag.fromIDorException(klausurdatum.getDayOfWeek().getValue()), startzeit,
					v.dauer);
			if (zeitrasterSk.isEmpty())
				throw OperationError.NOTHING_TO_DO.exception("Zeitraster konnte nicht ermittelt werden");
			conn.transactionExecuteDelete("DELETE FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden v WHERE v.Schuelerklausurtermin_ID = %d".formatted(sk.idSchuelerklausur));
			for (final StundenplanZeitraster stunde : zeitrasterSk) {
				final DTOGostKlausurenSchuelerklausurenTermineRaumstunden skRaumStundeNeu = new DTOGostKlausurenSchuelerklausurenTermineRaumstunden(sk.idSchuelerklausur,
						raumManager.klausurraumstundeGetByRaumidAndZeitrasterid(idRaum, stunde.id).id);
				conn.transactionPersist(skRaumStundeNeu);
				result.add(DataGostKlausurenSchuelerklausurraumstunde.dtoMapper.apply(skRaumStundeNeu));
			}
		}
		return result;
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaumstunden} in einen Core-DTO
	 * {@link GostKlausurraumstunde}.
	 */
	public static final Function<DTOGostKlausurenSchuelerklausurenTermineRaumstunden, GostSchuelerklausurterminraumstunde> dtoMapper = (final DTOGostKlausurenSchuelerklausurenTermineRaumstunden z) -> {
		final GostSchuelerklausurterminraumstunde daten = new GostSchuelerklausurterminraumstunde();
		daten.idRaumstunde = z.Raumstunde_ID;
		daten.idSchuelerklausurtermin = z.Schuelerklausurtermin_ID;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param idTermin die ID des Klausurtermins
	 *
	 * @return die Liste der Klausurraumstunden
	 */
	private GostKlausurenCollectionSkrsKrs getSchuelerklausurraumstunden(final Long idTermin) {
		final GostKlausurenCollectionSkrsKrs retCollection = new GostKlausurenCollectionSkrsKrs();

		final List<GostKlausurraum> listRaeume = new DataGostKlausurenRaum(conn).getKlausurraeume(idTermin);

		if (listRaeume.isEmpty()) {
			return retCollection;
		}

		final List<DTOGostKlausurenRaumstunden> raumStunden = conn.queryNamed("DTOGostKlausurenRaumstunden.klausurraum_id.multiple", listRaeume.stream().map(s -> s.id).toList(),
				DTOGostKlausurenRaumstunden.class);

		if (!raumStunden.isEmpty()) {
			for (final DTOGostKlausurenRaumstunden s : raumStunden)
				retCollection.raumstunden.add(DataGostKlausurenRaumstunde.dtoMapper.apply(s));

			final List<DTOGostKlausurenSchuelerklausurenTermineRaumstunden> skrStunden = conn.queryNamed("DTOGostKlausurenSchuelerklausurenTermineRaumstunden.raumstunde_id.multiple",
					raumStunden.stream().map(s -> s.ID).toList(), DTOGostKlausurenSchuelerklausurenTermineRaumstunden.class);

			for (final DTOGostKlausurenSchuelerklausurenTermineRaumstunden s : skrStunden)
				retCollection.skRaumstunden.add(DataGostKlausurenSchuelerklausurraumstunde.dtoMapper.apply(s));
		}

		return retCollection;
	}

	@Override
	public Response get(final Long idTermin) {
		// Schuelerklausurraumstunden zu einem Klausurtermin
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getSchuelerklausurraumstunden(idTermin)).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
