package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostSchuelerklausur}.
 */
public final class DataGostKlausurenSchuelerklausur extends DataManagerRevised<Long, DTOGostKlausurenSchuelerklausuren, GostSchuelerklausur> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostSchuelerklausur}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenSchuelerklausur(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("idKursklausur", "idSchueler");
		super.setAttributesRequiredOnCreation("idKursklausur", "idSchueler");
	}

	/**
	 * Gibt die Daten einer Klasse zu deren ID zurück.
	 *
	 * @param id   Die ID der Klasse.
	 *
	 * @return die Daten der KLasse zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public GostSchuelerklausur getById(final Long id) throws ApiOperationException {
		final DTOGostKlausurenSchuelerklausuren klasseDto = getDTO(id);
		return map(klasseDto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOKlassen} Objekt zur angegebenen Klassen ID.
	 *
	 * @param id ID der Klasse
	 *
	 * @return Ein {@link DTOKlassen} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOGostKlausurenSchuelerklausuren getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Klasse darf nicht null sein.");

		final DTOGostKlausurenSchuelerklausuren klasseDto = conn.queryByKey(DTOGostKlausurenSchuelerklausuren.class, id);
		if (klasseDto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klasse zur ID " + id + " gefunden.");

		return klasseDto;
	}

	@Override
	protected void initDTO(final DTOGostKlausurenSchuelerklausuren dto, final Long id) {
		dto.ID = id;
	}

	@Override
	protected GostSchuelerklausur map(final DTOGostKlausurenSchuelerklausuren dto) throws ApiOperationException {
		final GostSchuelerklausur daten = new GostSchuelerklausur();
		daten.idKursklausur = dto.Kursklausur_ID;
		daten.idSchueler = dto.Schueler_ID;
		daten.id = dto.ID;
		daten.bemerkung = dto.Bemerkungen;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOGostKlausurenSchuelerklausuren dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
//			case "idSchuelerklausur" -> dto.Schueler_ID = JSONMapper.convertToLong(value, false);
			case "idKursklausur" -> dto.Kursklausur_ID = JSONMapper.convertToLong(value, false);
			case "idSchueler" -> dto.Schueler_ID = JSONMapper.convertToLong(value, false);
			case "bemerkung" -> dto.Bemerkungen =
					JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Schuelerklausuren.col_Bemerkungen.datenlaenge());
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}



	/**
	 * Erstellt einen neuen Gost-Klausurtermin *
	 *
	 * @param is   Das JSON-Objekt mit den Daten
	 *
	 * @return Eine Response mit dem neuen Gost-Klausurtermin
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@Override
	public Response addMultipleAsResponse(final InputStream is) throws ApiOperationException {
		final GostKlausurenCollectionAllData ergebnis = new GostKlausurenCollectionAllData();
		ergebnis.schuelerklausuren.addAll(addMultiple(is));
		final Map<String, Object> initAttributes = new HashMap<>();
		initAttributes.put("folgeNr", 0);
		for (final GostSchuelerklausur sk : ergebnis.schuelerklausuren) {
			initAttributes.put("idSchuelerklausur", sk.id);
			ergebnis.schuelerklausurtermine.add(new DataGostKlausurenSchuelerklausurTermin(conn).add(initAttributes));
		}
		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(ergebnis).build();
	}

	/**
	 * Gibt die Liste der Schülerklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück, die eine Nachschreibklausur beinhalten.
	 *
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 *
	 * @return die Liste der Schülerklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionAllData getCollectionSkSktNachschreiber(final int abiturjahr, final GostHalbjahr halbjahr)
			throws ApiOperationException {
		final GostKlausurenCollectionAllData ergebnis = new GostKlausurenCollectionAllData();
		final List<GostKursklausur> kursKlausuren = DataGostKlausurenKursklausur.getKursKlausuren(conn, abiturjahr, halbjahr.id, false);
		if (!kursKlausuren.isEmpty()) {
			final List<DTOGostKlausurenSchuelerklausuren> schuelerKlausurDTOs = conn.query(
					"SELECT DISTINCT sk FROM DTOGostKlausurenSchuelerklausuren sk JOIN DTOGostKlausurenSchuelerklausurenTermine skt ON sk.ID = skt.Schuelerklausur_ID AND sk.Kursklausur_ID IN :kkids WHERE skt.Folge_Nr > 0",
					DTOGostKlausurenSchuelerklausuren.class).setParameter("kkids", kursKlausuren.stream().map(kk -> kk.id).toList()).getResultList();
			ergebnis.schuelerklausuren = mapList(schuelerKlausurDTOs);
			ergebnis.schuelerklausurtermine =
					new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausuren(ergebnis.schuelerklausuren);
		}
		return ergebnis;
	}


	/**
	 * Gibt die Liste der Schülerklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück, die eine Nachschreibklausur beinhalten.
	 *
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Klausuren für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 *
	 * @return die Liste der Schülerklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionAllData getCollectionSkSkt(final int abiturjahr, final GostHalbjahr halbjahr,
			final boolean ganzesSchuljahr) throws ApiOperationException {
		final GostKlausurenCollectionAllData ergebnis = new GostKlausurenCollectionAllData();
		final List<GostKursklausur> kursKlausuren = DataGostKlausurenKursklausur.getKursKlausuren(conn, abiturjahr, halbjahr.id, ganzesSchuljahr);
		ergebnis.schuelerklausuren = getSchuelerKlausurenZuKursklausuren(kursKlausuren);
		ergebnis.schuelerklausurtermine = new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausuren(ergebnis.schuelerklausuren);
		return ergebnis;
	}

	/**
	 * Gibt die Liste der Schülerklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück, die eine Nachschreibklausur beinhalten.
	 *
	 * @param kursKlausuren die Liste der Kursklausuren, für die die Schülerklausuren gesucht werden
	 *
	 * @return die Liste der Schülerklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostSchuelerklausur> getSchuelerKlausurenZuKursklausuren(final List<GostKursklausur> kursKlausuren)
			throws ApiOperationException {
		if (kursKlausuren.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenSchuelerklausuren> schuelerKlausurDTOs = conn.queryList(DTOGostKlausurenSchuelerklausuren.QUERY_LIST_BY_KURSKLAUSUR_ID,
				DTOGostKlausurenSchuelerklausuren.class, kursKlausuren.stream().map(kk -> kk.id).toList());
		return mapList(schuelerKlausurDTOs);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * GostSchuelerklausur-Objekte zurück.
	 *
	 * @param termine die Liste der GostSchuelerklausurterminen
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausur-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostSchuelerklausur> getSchuelerklausurenZuSchuelerklausurterminen(final List<GostSchuelerklausurTermin> termine) throws ApiOperationException {
		if (termine.isEmpty())
			return new ArrayList<>();
		final List<GostSchuelerklausur> sks = mapList(conn.queryByKeyList(DTOGostKlausurenSchuelerklausuren.class,
				termine.stream().map(sk -> sk.idSchuelerklausur).toList()));
		if (sks.isEmpty())
			throw new ApiOperationException(Status.CONFLICT, "Schülerklausuren zu Schülerklausurterminen nicht gefunden.");
		return sks;
	}

	/**
	 * Weist die übergebenen Schülerklausuren dem entsprechenden Klausurraum zu.
	 *
	 * @param idSchueler die IDs der zuzuweisenden Schülerklausuren
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr   das Gost-Halbjahr
	 *
	 * @return die Antwort
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public GostKlausurenCollectionAllData getGostKlausurenCollectionBySchuelerid(final long idSchueler, final int abiturjahr,
			final int halbjahr) throws ApiOperationException {
		final GostKlausurenCollectionAllData result = new GostKlausurenCollectionAllData();

		result.schuelerklausuren = mapList(
				conn.query(
						"SELECT sk FROM DTOGostKlausurenSchuelerklausuren sk JOIN DTOGostKlausurenKursklausuren kk ON (sk.Schueler_ID = :sId AND sk.Kursklausur_ID = kk.ID) JOIN DTOGostKlausurenVorgaben v ON (kk.Vorgabe_ID = v.ID AND v.Abi_Jahrgang = :abiturjahr AND v.Halbjahr = :halbjahr)",
						DTOGostKlausurenSchuelerklausuren.class)
						.setParameter("sId", idSchueler).setParameter("abiturjahr", abiturjahr)
						.setParameter("halbjahr", GostHalbjahr.fromIDorException(halbjahr))
						.getResultList());

		if (!result.schuelerklausuren.isEmpty()) {
			result.schuelerklausurtermine = new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuSchuelerklausuren(result.schuelerklausuren);
			result.kursklausuren = DataGostKlausurenKursklausur.getKursklausurenZuSchuelerklausuren(conn, result.schuelerklausuren);
			result.vorgaben = DataGostKlausurenVorgabe.getKlausurvorgabenZuKursklausuren(conn, result.kursklausuren);
			final List<Long> terminIds = new ArrayList<>();
			terminIds.addAll(result.schuelerklausurtermine.stream().filter(skt -> skt.idTermin != null).map(skt -> skt.idTermin).toList());
			terminIds.addAll(result.kursklausuren.stream().filter(kk -> kk.idTermin != null).map(kk -> kk.idTermin).toList());
			result.termine = DataGostKlausurenTermin.getKlausurtermineZuIds(conn, terminIds);
		}

		return result;
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * GostSchuelerklausur-Objekte zurück.
	 *
	 * @param terminIds die Liste der GostSchuelerklausurterminen
	 * @param includeAbwesend inkludiert auch GostSchuelerklausurtermine, die als abwesend gemeldet sind
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausur-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostSchuelerklausurTermin> getSchuelerKlausurenZuTerminIds(final List<Long> terminIds,
			final boolean includeAbwesend) throws ApiOperationException {
		if (terminIds.isEmpty())
			return new ArrayList<>();
		final List<GostKursklausur> kursklausuren = DataGostKlausurenKursklausur.getKursklausurenZuTerminids(conn, terminIds);
		final List<GostSchuelerklausur> schuelerklausuren = getSchuelerKlausurenZuKursklausuren(kursklausuren);
		final List<Long> kkSkIds = schuelerklausuren.stream().map(sk -> sk.id).toList();
		String skFilter = "";
		if (!kkSkIds.isEmpty()) {
			skFilter += " OR (skt.Schuelerklausur_ID IN :skIds AND skt.Folge_Nr = 0";
			if (!includeAbwesend)
				skFilter +=
						" AND NOT EXISTS (SELECT sktInner FROM DTOGostKlausurenSchuelerklausurenTermine sktInner WHERE sktInner.Schuelerklausur_ID = skt.Schuelerklausur_ID AND sktInner.Folge_Nr > 0)";
			skFilter += ")";
		}
		final TypedQuery<DTOGostKlausurenSchuelerklausurenTermine> query =
				conn.query("SELECT skt FROM DTOGostKlausurenSchuelerklausurenTermine skt WHERE skt.Termin_ID IN :tids" + skFilter,
						DTOGostKlausurenSchuelerklausurenTermine.class);
		if (!kkSkIds.isEmpty())
			query.setParameter("skIds", kkSkIds);
		final List<DTOGostKlausurenSchuelerklausurenTermine> skts = query.setParameter("tids", terminIds).getResultList();
		return new DataGostKlausurenSchuelerklausurTermin(conn).mapList(skts);
	}

	/**
	 * Liefert die zu einer Liste von GostSchuelerklausurterminen gehörigen
	 * GostSchuelerklausur-Objekte zurück.
	 *
	 * @param terminIds die Liste der GostSchuelerklausurterminen
	 *
	 * @return die Liste der zugehörigen GostSchuelerklausur-Objekte
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostSchuelerklausurTermin> getSchuelerKlausurenZuTerminIds(final List<Long> terminIds)
			throws ApiOperationException {
		return getSchuelerKlausurenZuTerminIds(terminIds, false);
	}



}
