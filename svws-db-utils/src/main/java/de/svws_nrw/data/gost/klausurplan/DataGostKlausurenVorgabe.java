package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.klausurplan.GostKlausurvorgabenManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurvorgabe}.
 */
public final class DataGostKlausurenVorgabe extends DataManager<Long> {

	private final int _abiturjahr;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurtermin}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 */
	public DataGostKlausurenVorgabe(final DBEntityManager conn, final int abiturjahr) {
		super(conn);
		_abiturjahr = abiturjahr;
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Erzeugt die Gost-Kursklausuren aus den Klausurvorlagen einer Jahrgangsstufe
	 * im übergebenen Gost-Halbjahr für die existierenden Kurse zurück.
	 *
	 * @param hj      das Gost-Halbjahr
	 * @param quartal das Quartal
	 *
	 * @return die Anzahl der erzeugten Kursklausuren
	 */
	public Response createKlausuren(final int hj, final int quartal) {
		final GostHalbjahr halbjahr = GostHalbjahr.fromID(hj);
		final List<GostKursklausur> retKlausuren = new ArrayList<>();

		final List<GostKlausurvorgabe> vorgaben = conn.query("SELECT v FROM DTOGostKlausurenVorgaben v WHERE v.Abi_Jahrgang = :jgid AND v.Halbjahr = :hj", DTOGostKlausurenVorgaben.class)
				.setParameter("jgid", _abiturjahr).setParameter("hj", halbjahr).getResultList().stream().map(dtoMapper::apply)
//				.filter(v -> quartal > 0 ? v.quartal == quartal : true)
				.toList();
		if (vorgaben == null)
			throw new NullPointerException();
		if (vorgaben.isEmpty())
			return OperationError.NOT_FOUND.getResponse("Noch keine Klausurvorgaben für dieses Halbjahr definiert.");

		final GostKlausurvorgabenManager manager = new GostKlausurvorgabenManager(vorgaben, null);

		final List<DTOGostKlausurenKursklausuren> existingKlausuren = conn.queryNamed("DTOGostKlausurenKursklausuren.vorgabe_id.multiple",
				vorgaben.stream().map(v -> v.idVorgabe).toList(), DTOGostKlausurenKursklausuren.class);
		final Map<Long, Map<Long, DTOGostKlausurenKursklausuren>> mapKursidVorgabeIdKursklausur = existingKlausuren.stream()
				.collect(Collectors.groupingBy(k -> k.Kurs_ID, Collectors.toMap(k -> k.Vorgabe_ID, Function.identity())));

		// TODO NoResultException fangen und Fehlermeldung, dass Schuljahresabschnitt
		// noch nicht angelegt.
		final List<DTOSchuljahresabschnitte> sjaList = conn.query("SELECT s FROM DTOSchuljahresabschnitte s WHERE s.Jahr = :jahr AND s.Abschnitt = :abschnitt", DTOSchuljahresabschnitte.class)
				.setParameter("jahr", halbjahr.getSchuljahrFromAbiturjahr(_abiturjahr)).setParameter("abschnitt", halbjahr.id % 2 + 1).getResultList();
		if (sjaList == null || sjaList.size() != 1)
			return OperationError.NOT_FOUND.getResponse("Noch kein Schuljahresabschnitt für dieses Halbjahr definiert.");

		final DTOSchuljahresabschnitte sja = sjaList.get(0);
		// Kurse ermitteln
		final List<DTOKurs> kurse = conn.query("SELECT k FROM DTOKurs k WHERE k.Schuljahresabschnitts_ID = :sja AND k.ASDJahrgang = :jg", DTOKurs.class).setParameter("sja", sja.ID) // TODO Quartalsmodus
				.setParameter("jg", halbjahr.jahrgang).getResultList();

		final List<DTOGostKlausurenKursklausuren> kursklausuren = new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausuren> schuelerklausuren = new ArrayList<>();

		// Bestimme die ID, für welche der Datensatz eingefügt wird
		final DTOSchemaAutoInkremente dbNmkID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Klausuren_Kursklausuren");
		long idNMK = dbNmkID == null ? 1 : dbNmkID.MaxID + 1;

		for (final DTOKurs kurs : kurse) {
			final List<GostKlausurvorgabe> listKursVorgaben = manager.gibGostKlausurvorgabenByQuartalKursartFach(quartal, kurs.KursartAllg, kurs.Fach_ID);
			for (final GostKlausurvorgabe vorgabe : listKursVorgaben) {
				if ((vorgabe != null) && (!(mapKursidVorgabeIdKursklausur.containsKey(kurs.ID)
						&& mapKursidVorgabeIdKursklausur.get(kurs.ID).containsKey(vorgabe.idVorgabe)))) {
					final DTOGostKlausurenKursklausuren kursklausur = new DTOGostKlausurenKursklausuren(idNMK++, vorgabe.idVorgabe, kurs.ID);
					kursklausuren.add(kursklausur);
					final List<DTOGostKlausurenSchuelerklausuren> listSk = createSchuelerklausuren(hj, kursklausur, kurs);
					schuelerklausuren.addAll(listSk);
					retKlausuren.add(DataGostKlausurenKursklausur.dtoMapper.apply(kursklausur, vorgabe, kurs, listSk));
				}
			}
		}

		if (!conn.persistAll(kursklausuren))
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();

		final DTOSchemaAutoInkremente dbNmkIDsKlausuren = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Klausuren_Schuelerklausuren");
		Long idNmkIDsKlausuren = dbNmkIDsKlausuren == null ? 1 : dbNmkIDsKlausuren.MaxID + 1;

		for (final DTOGostKlausurenSchuelerklausuren sk : schuelerklausuren)
			sk.ID = idNmkIDsKlausuren++;
		if (!conn.persistAll(schuelerklausuren))
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();

		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(retKlausuren).build();

	}

	private List<DTOGostKlausurenSchuelerklausuren> createSchuelerklausuren(final int hj, final DTOGostKlausurenKursklausuren kursklausur, final DTOKurs kurs) {
		final List<DTOSchuelerLernabschnittsdaten> lernDaten = conn.query(
				"SELECT lad FROM DTOSchuelerLernabschnittsdaten lad JOIN DTOSchuelerLeistungsdaten sld ON sld.Abschnitt_ID = lad.ID JOIN DTOSchueler s ON lad.Schueler_ID = s.ID WHERE sld.Kurs_ID = :kursid AND sld.Kursart IN :kursart AND s.Status = :sstatus AND s.Geloescht = :sgeloescht",
				DTOSchuelerLernabschnittsdaten.class).setParameter("kursid", kurs.ID)
				.setParameter("kursart", Arrays.asList(hj == 5 ? new String[] { "LK1", "LK2", "AB3" } : new String[] { "LK1", "LK2", "AB3", "AB4", "GKS" }))
				.setParameter("sstatus", SchuelerStatus.AKTIV).setParameter("sgeloescht", false).getResultList();

		final List<DTOGostKlausurenSchuelerklausuren> listSchuelerklausuren = new ArrayList<>();
		for (final DTOSchuelerLernabschnittsdaten lad : lernDaten) {
			listSchuelerklausuren.add(new DTOGostKlausurenSchuelerklausuren(-1L, kursklausur.ID, lad.Schueler_ID));
		}
		return listSchuelerklausuren;
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenVorgaben} in einen Core-DTO
	 * {@link GostKlausurvorgabe}.
	 */
	public static final Function<DTOGostKlausurenVorgaben, GostKlausurvorgabe> dtoMapper = (final DTOGostKlausurenVorgaben z) -> {
		final GostKlausurvorgabe daten = new GostKlausurvorgabe();
		daten.idVorgabe = z.ID;
		daten.abiJahrgang = z.Abi_Jahrgang;
		daten.idFach = z.Fach_ID;
		daten.kursart = z.Kursart.kuerzel;
		daten.halbjahr = z.Halbjahr.id;
		daten.quartal = z.Quartal;
		daten.bemerkungVorgabe = z.Bemerkungen;
		daten.auswahlzeit = z.Auswahlzeit;
		daten.dauer = z.Dauer;
		daten.istAudioNotwendig = z.IstAudioNotwendig;
		daten.istVideoNotwendig = z.IstVideoNotwendig;
		daten.istMdlPruefung = z.IstMdlPruefung;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param halbjahr das Gost-Halbjahr
	 *
	 * @return die Liste der Kursklausuren
	 */
	private List<GostKlausurvorgabe> getKlausurvorgaben(final int halbjahr) {
		final List<DTOGostKlausurenVorgaben> vorgaben = conn.query("SELECT v FROM DTOGostKlausurenVorgaben v WHERE v.Abi_Jahrgang = :jgid AND v.Halbjahr = :hj", DTOGostKlausurenVorgaben.class)
				.setParameter("jgid", _abiturjahr).setParameter("hj", GostHalbjahr.fromID(halbjahr)).getResultList();
		final List<GostKlausurvorgabe> daten = new ArrayList<>();
		for (final DTOGostKlausurenVorgaben v : vorgaben)
			daten.add(dtoMapper.apply(v));
		return daten;
	}

	@Override
	public Response get(final Long halbjahr) {
		// Kursklausuren für einen Abiturjahrgang in einem Gost-Halbjahr
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKlausurvorgaben(halbjahr.intValue())).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() > 0) {
			try {
				conn.transactionBegin();
				final DTOGostKlausurenVorgaben vorgabe = conn.queryByKey(DTOGostKlausurenVorgaben.class, id);
				if (vorgabe == null)
					throw OperationError.NOT_FOUND.exception();
				for (final Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					switch (key) {
					case "idVorgabe" -> {
						final Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "abiJahrgang" -> {
						final int patch_abijahr = JSONMapper.convertToInteger(value, false);
						if ((patch_abijahr != vorgabe.Abi_Jahrgang))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "halbjahr" -> {
						final int patch_halbjahr = JSONMapper.convertToInteger(value, false);
						if ((patch_halbjahr != vorgabe.Halbjahr.id))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "quartal" -> vorgabe.Quartal = JSONMapper.convertToInteger(value, false);
					case "idFach" -> vorgabe.Fach_ID = JSONMapper.convertToLong(value, false);
					case "kursart" -> vorgabe.Kursart = GostKursart.fromKuerzel(JSONMapper.convertToString(value, false, false, null)); // TODO check value -> error: BAD_REQUEST
					case "dauer" -> vorgabe.Dauer = JSONMapper.convertToInteger(value, false);
					case "auswahlzeit" -> vorgabe.Auswahlzeit = JSONMapper.convertToInteger(value, false);
					case "istMdlPruefung" -> vorgabe.IstMdlPruefung = JSONMapper.convertToBoolean(value, false);
					case "istAudioNotwendig" -> vorgabe.IstAudioNotwendig = JSONMapper.convertToBoolean(value, false);
					case "istVideoNotwendig" -> vorgabe.IstVideoNotwendig = JSONMapper.convertToBoolean(value, false);
					case "bemerkungVorgabe" -> vorgabe.Bemerkungen = JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Vorgaben.col_Bemerkungen.datenlaenge());

					default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
				conn.transactionPersist(vorgabe);
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

	/**
	 * Erstellt eine neue Gost-Klausurvorgabe
	 *
	 * @param is Das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit der neuen Gost-Klausurvorgabe
	 */
	public Response create(final InputStream is) {
		DTOGostKlausurenVorgaben vorgabe = null;
		try {
			conn.transactionBegin();
			// Bestimme die ID des neuen Klausurtermins
			final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Klausuren_Vorgaben");
			final Long id = lastID == null ? 1 : lastID.MaxID + 1;

			int abi_Jahrgang = -1;
			GostHalbjahr halbjahr = GostHalbjahr.EF1;
			int quartal = -1;
			long fach_ID = -1;
			GostKursart kursart = GostKursart.GK;
			int dauer = 0;
			int auswahlzeit = 0;
			boolean istMdlPruefung = false;
			boolean istAudioNotwendig = false;
			boolean istVideoNotwendig = false;
			String bemerkungen = null;

			final Map<String, Object> map = JSONMapper.toMap(is);
			if (map.size() > 0) {
				for (final Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					switch (key) {
					case "abiJahrgang" -> abi_Jahrgang = JSONMapper.convertToInteger(value, false);
					case "halbjahr" -> halbjahr = GostHalbjahr.fromID(JSONMapper.convertToInteger(value, false));
					case "quartal" -> quartal = JSONMapper.convertToInteger(value, false);
					case "idFach" -> fach_ID = JSONMapper.convertToLong(value, false);
					case "kursart" -> kursart = GostKursart.fromKuerzel(JSONMapper.convertToString(value, false, false, null)); // TODO check value -> error: BAD_REQUEST
					case "dauer" -> dauer = JSONMapper.convertToInteger(value, false);
					case "auswahlzeit" -> auswahlzeit = JSONMapper.convertToInteger(value, false);
					case "istMdlPruefung" -> istMdlPruefung = JSONMapper.convertToBoolean(value, false);
					case "istAudioNotwendig" -> istAudioNotwendig = JSONMapper.convertToBoolean(value, false);
					case "istVideoNotwendig" -> istVideoNotwendig = JSONMapper.convertToBoolean(value, false);
					case "bemerkungVorgabe" -> bemerkungen = JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Vorgaben.col_Bemerkungen.datenlaenge());
					case "idVorgabe" -> { /* do nothing */ }
					default -> throw OperationError.BAD_REQUEST.exception();
					}
				}
			}
			vorgabe = new DTOGostKlausurenVorgaben(id, abi_Jahrgang, halbjahr, quartal, fach_ID, kursart, dauer, auswahlzeit, istMdlPruefung, istAudioNotwendig, istVideoNotwendig);
			vorgabe.Bemerkungen = bemerkungen;
			conn.transactionPersist(vorgabe);
			if (!conn.transactionCommit())
				return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren der Gost-Klausurvorgabe");
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webApplicationException)
				return webApplicationException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			conn.transactionRollback();
		}

		final GostKlausurvorgabe daten = dtoMapper.apply(vorgabe);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();

	}

	/**
	 * Löscht eine Gost-Klausurvorgabe *
	 *
	 * @param id die ID der zu löschenden Klausurvorgabe
	 *
	 * @return die Response
	 */
	public Response delete(final Long id) {
		// TODO use transaction
		// Bestimme den Termin
		final DTOGostKlausurenVorgaben vorgabe = conn.queryByKey(DTOGostKlausurenVorgaben.class, id);
		if (vorgabe == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne den Termin
		conn.remove(vorgabe);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}


	/**
	 * Kopiert die Klausurvorgaben in einen Abiturjahrgang
	 *
	 * @param halbjahr     das Halbjahr der gymnasialen Oberstufe
	 * @param quartal      das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return erfolgreich / nicht erfolgreich
	 */
	public Response copyVorgaben(final GostHalbjahr halbjahr, final int quartal) {
		try {
			conn.transactionBegin();
			final boolean data = copyVorgabenToJahrgang(conn, _abiturjahr, halbjahr, quartal);
	    	conn.transactionCommit();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(data).build();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException wae)
				return wae.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse(e);
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
	}


	/**
	 * Kopiert die Klausurvorgaben in einen Abiturjahrgang
	 *
	 * @param conn         die Datenbankverbindung
	 * @param abiturjahr   das Abiturjahr
	 * @param halbjahr     das Halbjahr der gymnasialen Oberstufe
	 * @param quartal      das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return erfolgreich / nicht erfolgreich
	 */
	public static boolean copyVorgabenToJahrgang(final DBEntityManager conn, final int abiturjahr, final GostHalbjahr halbjahr, final int quartal) {
		final List<DTOGostKlausurenVorgaben> vorgabenVorlage = conn.queryNamed("DTOGostKlausurenVorgaben.abi_jahrgang", -1, DTOGostKlausurenVorgaben.class);
		final List<DTOGostKlausurenVorgaben> vorgabenJg = conn.queryNamed("DTOGostKlausurenVorgaben.abi_jahrgang", abiturjahr, DTOGostKlausurenVorgaben.class);
		// Prüfe, ob die Vorlage eingelesen werden kann
		if (vorgabenVorlage == null)
			throw OperationError.NOT_FOUND.exception("Es wurden keine Vorlage-Daten gefunden.");
		// Wenn keine Vorlage-Daten vorhanden sind, dann ist nichts zu tun und die Operation ist erfolgreich
		if (vorgabenVorlage.isEmpty())
			return true;

		// Bestimme die ID, für welche der Datensatz eingefügt wird
		long idNMK = conn.transactionGetNextID(DTOGostKlausurenVorgaben.class);
		for (final DTOGostKlausurenVorgaben vorgabe : vorgabenVorlage) {
			if (halbjahr != null && vorgabe.Halbjahr != halbjahr || quartal > 0 && quartal != vorgabe.Quartal)
				continue;
			boolean exists = false;
			for (final DTOGostKlausurenVorgaben v : vorgabenJg) {
				if ((vorgabe.Halbjahr.id == v.Halbjahr.id) && (vorgabe.Quartal == v.Quartal) && (vorgabe.Fach_ID == v.Fach_ID)
						&& vorgabe.Kursart.equals(v.Kursart)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				final DTOGostKlausurenVorgaben k = new DTOGostKlausurenVorgaben(idNMK++, abiturjahr, vorgabe.Halbjahr, vorgabe.Quartal,
						vorgabe.Fach_ID, vorgabe.Kursart, vorgabe.Dauer, vorgabe.Auswahlzeit, vorgabe.IstMdlPruefung,
						vorgabe.IstAudioNotwendig, vorgabe.IstVideoNotwendig);
				k.Bemerkungen = vorgabe.Bemerkungen;
				if (!conn.transactionPersist(k))
					throw OperationError.INTERNAL_SERVER_ERROR.exception("Fehler beim Persistieren der Vorlage-Daten.");
			}
		}
		return true;
	}

}
