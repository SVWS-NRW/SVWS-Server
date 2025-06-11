package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrsData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnisTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmus;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link GostKursklausur}.
 */
public final class DataGostKlausurenKursklausur extends DataManagerRevised<Long, DTOGostKlausurenKursklausuren, GostKursklausur> {

	private GostKlausurenCollectionSkrsKrsData raumDataChanged = new GostKlausurenCollectionSkrsKrsData();

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link GostKursklausur}.
	 *
	 * @param conn                   die Datenbank-Verbindung für den
	 *                               Datenbankzugriff
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public DataGostKlausurenKursklausur(final DBEntityManager conn) throws ApiOperationException {
		super(conn);
		super.setAttributesNotPatchable("id", "idVorgabe", "idKurs");
		super.setAttributesRequiredOnCreation("idVorgabe", "idKurs");
	}

	/**
	 * Gibt die Daten einer {@link GostKursklausur} zu deren ID zurück.
	 *
	 * @param id   Die ID der {@link GostKursklausur}.
	 *
	 * @return die Daten der {@link GostKursklausur} zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public GostKursklausur getById(final Long id) throws ApiOperationException {
		final DTOGostKlausurenKursklausuren klasseDto = getDTO(id);
		return map(klasseDto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOGostKlausurenKursklausuren} Objekt zur angegebenen ID.
	 *
	 * @param id ID der {@link DTOGostKlausurenKursklausuren}
	 *
	 * @return Ein {@link DTOGostKlausurenKursklausuren} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOGostKlausurenKursklausuren getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die GostKursklausur darf nicht null sein.");

		final DTOGostKlausurenKursklausuren klasseDto = conn.queryByKey(DTOGostKlausurenKursklausuren.class, id);
		if (klasseDto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine GostKursklausur zur ID " + id + " gefunden.");

		return klasseDto;
	}

	@Override
	protected void initDTO(final DTOGostKlausurenKursklausuren dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected GostKursklausur map(final DTOGostKlausurenKursklausuren dto) throws ApiOperationException {
		final GostKursklausur kk = new GostKursklausur();
		kk.id = dto.ID;
		kk.idVorgabe = dto.Vorgabe_ID;
		kk.idKurs = dto.Kurs_ID;
		kk.idTermin = dto.Termin_ID;
		kk.startzeit = dto.Startzeit;
		kk.bemerkung = dto.Bemerkungen;
		return kk;
	}

	@Override
	protected void mapAttribute(final DTOGostKlausurenKursklausuren dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idVorgabe" -> dto.Vorgabe_ID = JSONMapper.convertToLong(value, false);
			case "idKurs" -> dto.Kurs_ID = JSONMapper.convertToLong(value, false);
			case "idTermin" -> {
				final Long newTermin = JSONMapper.convertToLong(value, true);
				if (!Objects.equals(newTermin, dto.Termin_ID)) {
					dto.Startzeit = null;
					raumDataChanged = new DataGostKlausurenSchuelerklausurraumstunde(conn).loescheRaumZuSchuelerklausurenTransaction(getSchuelerklausurtermine(dto)); // Auch alle Raumzuweisungen werden gelöscht
				}
				if (newTermin != null) {
					final DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, newTermin);
					final DTOGostKlausurenVorgaben vorgabe = conn.queryByKey(DTOGostKlausurenVorgaben.class, dto.Vorgabe_ID);
					if ((termin.Quartal != 0) && !Objects.equals(termin.Quartal, vorgabe.Quartal))
						throw new ApiOperationException(Status.CONFLICT, "Klausur-Quartal entspricht nicht Termin-Quartal.");
				}
				dto.Termin_ID = newTermin;
			}
			case "startzeit" -> {
				final Integer startzeitNeu = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
				if (((startzeitNeu == null) && (dto.Startzeit != null)) || ((startzeitNeu != null) && !startzeitNeu.equals(dto.Startzeit))) {
					dto.Startzeit = startzeitNeu;
					conn.transactionPersist(dto);
					raumDataChanged = new DataGostKlausurenSchuelerklausurraumstunde(conn).updateRaeumeZuSchuelerklausurterminen(getSchuelerklausurtermine(dto));
				}
			}
			case "bemerkung" -> dto.Bemerkungen =
					DataGostKlausuren.convertEmptyStringToNull(JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Kursklausuren.col_Bemerkungen.datenlaenge()));
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	private List<GostSchuelerklausurTermin> getSchuelerklausurtermine(final DTOGostKlausurenKursklausuren dto) throws ApiOperationException {
		final List<GostSchuelerklausur> sks = new DataGostKlausurenSchuelerklausur(conn).getSchuelerKlausurenZuKursklausuren(ListUtils.create1(map(dto)));
		return new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausuren(sks);
	}

	@Override
	public Response patchAsResponse(final Long id, final InputStream is) throws ApiOperationException {
		patchFromStream(id, is);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(raumDataChanged).build();
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Klausuren für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKursklausur> getKursKlausuren(final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr)
			throws ApiOperationException {
		return getKursklausurenZuVorgaben(new DataGostKlausurenVorgabe(conn).getKlausurvorgaben(abiturjahr, halbjahr, ganzesSchuljahr));
	}

	/**
	 * Gibt die Liste der Kursklausuren zu den übergebenen Klausurvorgaben zurück.
	 *
	 * @param vorgaben die Liste der Klausurvorgaben, zu denen die Kursklausuren gesucht werden.
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKursklausur> getKursklausurenZuVorgaben(final List<GostKlausurvorgabe> vorgaben)
			throws ApiOperationException {
		if (vorgaben.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenKursklausuren> kursKlausurDTOs = conn.queryList(DTOGostKlausurenKursklausuren.QUERY_LIST_BY_VORGABE_ID,
				DTOGostKlausurenKursklausuren.class, vorgaben.stream().map(v -> v.id).toList());
		return mapList(kursKlausurDTOs);
	}

	/**
	 * Gibt die Liste der Kursklausuren zur übergeben Termin-ID zurück.
	 *
	 * @param idTermin 	 die ID des Klausurtermins
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKursklausur> getKursklausurenZuTerminid(final long idTermin) throws ApiOperationException {
		return getKursklausurenZuTerminids(ListUtils.create1(idTermin));
	}

	/**
	 * Gibt die Liste der Kursklausuren zur übergeben Termin-ID zurück.
	 *
	 * @param idsTermin 	 die ID des Klausurtermins
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKursklausur> getKursklausurenZuTerminids(final List<Long> idsTermin) throws ApiOperationException {
		for (final long idTermin : idsTermin)
			if (new DataGostKlausurenTermin(conn).getById(idTermin) == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Klausurtermin mit ID %d existiert nicht.".formatted(idTermin));
		final List<DTOGostKlausurenKursklausuren> kursKlausurDTOs = conn.queryList(DTOGostKlausurenKursklausuren.QUERY_LIST_BY_TERMIN_ID,
				DTOGostKlausurenKursklausuren.class, idsTermin);
		return mapList(kursKlausurDTOs);
	}

	/**
	 * Gibt die Liste der Kursklausuren zu den übergebenen Schülerklausuren zurück.
	 *
	 * @param schuelerklausuren die Liste der Schülerklausuren, zu denen die Kursklausuren gesucht werden.
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKursklausur> getKursklausurenZuSchuelerklausuren(final List<GostSchuelerklausur> schuelerklausuren)
			throws ApiOperationException {
		if (schuelerklausuren.isEmpty())
			return new ArrayList<>();
		return getKursklausurenZuIds(schuelerklausuren.stream().map(sk -> sk.idKursklausur).distinct().toList());
	}

	/**
	 * Gibt die Liste der Kursklausuren zu den übergebenen Schülerklausuren zurück.
	 *
	 * @param kkids die Liste der IDs der gesuchten Kursklausuren
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKursklausur> getKursklausurenZuIds(final List<Long> kkids) throws ApiOperationException {
		if (kkids.isEmpty())
			return new ArrayList<>();
		return mapList(getKursklausurenDTOsZuIds(conn, kkids));
	}

	/**
	 * Gibt die Liste der Kursklausuren-DTOs zu den übergebenen IDs zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param kkids die Liste der IDs der gesuchten Kursklausuren
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<DTOGostKlausurenKursklausuren> getKursklausurenDTOsZuIds(final DBEntityManager conn, final List<Long> kkids)
			throws ApiOperationException {
		if (kkids.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenKursklausuren> kks = conn.queryByKeyList(DTOGostKlausurenKursklausuren.class, kkids);
		if (kks.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Kursklausur-DTOs zu IDs nicht gefunden.");
		return kks;
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Klausuren für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionData getKlausurDataCollection(final int abiturjahr, final int halbjahr,
			final boolean ganzesSchuljahr) throws ApiOperationException {
		final GostKlausurenCollectionData data = new GostKlausurenCollectionData();
		data.vorgaben = new DataGostKlausurenVorgabe(conn).getKlausurvorgaben(abiturjahr, halbjahr, ganzesSchuljahr);
		data.kursklausuren = getKursklausurenZuVorgaben(data.vorgaben);
		data.schuelerklausuren = new DataGostKlausurenSchuelerklausur(conn).getSchuelerKlausurenZuKursklausuren(data.kursklausuren);
		data.schuelerklausurtermine = new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausuren(data.schuelerklausuren);
		data.termine = new DataGostKlausurenTermin(conn).getKlausurtermine(abiturjahr, halbjahr, ganzesSchuljahr, data.schuelerklausurtermine.stream().filter(skt -> skt.idTermin != null).map(skt -> skt.idTermin).toList());
		return data;
	}

	/**
	 * Startet den KlausurterminblockungAlgorithmus mit den übergebenen
	 * GostKlausurterminblockungDaten und persistiert die Blockung in der Datenbank.
	 *
	 * @param blockungDaten das GostKlausurterminblockungDaten-Objekt
	 *
	 * @return true, wenn die Blockung erstellt werden konnte, false, wenn nicht.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionData blocken(final GostKlausurterminblockungDaten blockungDaten)
			throws ApiOperationException {
		final GostKlausurenCollectionData blockung = new GostKlausurenCollectionData();
		blockungDaten.richKlausuren = enrichKursklausuren(conn, blockungDaten.klausuren);
		final GostKlausurterminblockungErgebnis ergebnis = new KlausurterminblockungAlgorithmus().apply(blockungDaten);

		long idNextTermin = conn.transactionGetNextID(DTOGostKlausurenTermine.class);

		for (final GostKlausurterminblockungErgebnisTermin ergebnisTermin : ergebnis.termine) {
			bearbeiteTermin(ergebnisTermin, idNextTermin++, blockung);
		}
		return blockung;
	}

	private void bearbeiteTermin(final GostKlausurterminblockungErgebnisTermin ergebnisTermin, final long terminId,
			final GostKlausurenCollectionData blockung) throws ApiOperationException {
		DTOGostKlausurenTermine termin = null;
		final List<DTOGostKlausurenKursklausuren> listKlausuren = getKursklausurenDTOsZuIds(conn, ergebnisTermin.kursklausuren);
		final List<GostKlausurvorgabe> listVorgaben = new DataGostKlausurenVorgabe(conn).getKlausurvorgabenZuKursklausurDTOs(listKlausuren);
		final GostKlausurplanManager manager = new GostKlausurplanManager(listVorgaben);
		for (final DTOGostKlausurenKursklausuren klausur : listKlausuren) {
			final GostKlausurvorgabe vorgabe = manager.vorgabeGetByIdOrException(klausur.Vorgabe_ID);
			if (termin == null) {
				final GostHalbjahr ghj = GostHalbjahr.fromIDorException(vorgabe.halbjahr);

				final List<DTOSchuljahresabschnitte> sjaList =
						conn.query("SELECT s FROM DTOSchuljahresabschnitte s WHERE s.Jahr = :jahr AND s.Abschnitt = :abschnitt", DTOSchuljahresabschnitte.class)
								.setParameter("jahr", ghj.getSchuljahrFromAbiturjahr(vorgabe.abiJahrgang))
								.setParameter("abschnitt", (vorgabe.halbjahr % 2) + 1)
								.getResultList();
				if ((sjaList == null) || (sjaList.size() != 1))
					throw new ApiOperationException(Status.NOT_FOUND, "Noch kein Schuljahresabschnitt für dieses Halbjahr definiert.");

				termin = new DTOGostKlausurenTermine(terminId, sjaList.getFirst().ID, vorgabe.abiJahrgang, ghj,
						vorgabe.quartal, true, false);
				conn.transactionPersist(termin);
				blockung.termine.add(new DataGostKlausurenTermin(conn).map(termin));
				conn.transactionFlush();
			}
			if ((termin.Abi_Jahrgang != vorgabe.abiJahrgang) || (termin.Halbjahr != GostHalbjahr.fromIDorException(vorgabe.halbjahr))
					|| (termin.Quartal != vorgabe.quartal))
				throw new ApiOperationException(Status.CONFLICT, "Kursklausurn mit unterschiedlichen Jahrgängen, Halbjahren oder Quartalen an einem Termin.");
			klausur.Termin_ID = termin.ID;
			conn.transactionPersist(klausur);
			blockung.kursklausuren.add(map(klausur));
		}
	}

	/**
	 * Erzeugt eine Liste von GostKursklausurRich-Objekten, die für die Klausurblockung benötigte Informationen anreichert.
	 *
	 * @param conn            Connection
	 * @param kursklausuren   die Liste der anzureichernden GostKursklausur-Objekte
	 *
	 * @return die Liste von GostKursklausurRich-Objekten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<GostKursklausurRich> enrichKursklausuren(final DBEntityManager conn, final List<GostKursklausur> kursklausuren)
			throws ApiOperationException {
		final List<GostKursklausurRich> richKlausuren = new ArrayList<>();
		if (kursklausuren.isEmpty())
			return richKlausuren;

		final List<GostKlausurvorgabe> listVorgaben = new DataGostKlausurenVorgabe(conn).getKlausurvorgabenZuKursklausuren(kursklausuren);
		if (listVorgaben.isEmpty())
			return new ArrayList<>();

		final GostKlausurplanManager manager = new GostKlausurplanManager(listVorgaben);

		final Map<Long, List<DTOGostKlausurenSchuelerklausuren>> mapSchuelerklausuren = conn.queryList(
				DTOGostKlausurenSchuelerklausuren.QUERY_LIST_BY_KURSKLAUSUR_ID, DTOGostKlausurenSchuelerklausuren.class,
				kursklausuren.stream().map(k -> k.id).toList()).stream().collect(Collectors.groupingBy(s -> s.Kursklausur_ID));
		if (mapSchuelerklausuren.isEmpty())
			return new ArrayList<>();

		final List<Long> kursIDs = kursklausuren.stream().map(k -> k.idKurs).distinct().toList();
		final Map<Long, DTOKurs> mapKurse = conn.queryByKeyList(DTOKurs.class, kursIDs).stream().collect(Collectors.toMap(k -> k.ID, k -> k));

		for (final var k : kursklausuren) {
			final GostKlausurvorgabe v = manager.vorgabeGetByIdOrException(k.idVorgabe);
			final DTOKurs kurs = mapKurse.get(k.idKurs);
			final List<DTOGostKlausurenSchuelerklausuren> sKlausuren = mapSchuelerklausuren.get(k.id);
			if ((sKlausuren != null) && !sKlausuren.isEmpty()) {
				final GostKursklausurRich kkr = new GostKursklausurRich();
				kkr.abijahr = v.abiJahrgang;
				kkr.bemerkung = k.bemerkung;
				kkr.halbjahr = v.halbjahr;
				kkr.id = k.id;
				kkr.idFach = v.idFach;
				kkr.idKurs = k.idKurs;
				kkr.idLehrer = kurs.Lehrer_ID;
				kkr.idTermin = k.idTermin;
				kkr.idVorgabe = v.id;
				kkr.kursart = v.kursart;
				kkr.kursKurzbezeichnung = kurs.KurzBez;
				try {
					kkr.kursSchiene = Stream.of(kurs.Schienen.split(",")).mapToInt(Integer::parseInt).toArray();
				} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
					kkr.kursSchiene = new int[0];
				}
				kkr.quartal = v.quartal;
				kkr.schuelerIds = sKlausuren.stream().map(s -> s.Schueler_ID).toList();
				kkr.startzeit = k.startzeit;
				richKlausuren.add(kkr);
			}
		}
		return richKlausuren;
	}

}
