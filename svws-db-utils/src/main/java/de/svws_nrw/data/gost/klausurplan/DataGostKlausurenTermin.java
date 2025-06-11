package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionRaumData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrsData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link GostKlausurtermin}.
 */
public final class DataGostKlausurenTermin extends DataManagerRevised<Long, DTOGostKlausurenTermine, GostKlausurtermin> {

	private GostKlausurenCollectionSkrsKrsData raumDataChanged = null;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO
	 * {@link GostKlausurtermin}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostKlausurenTermin(final DBEntityManager conn) {
		super(conn);
		super.setAttributesNotPatchable("id", "idSchuljahresabschnitt", "abijahr", "halbjahr", "istHaupttermin");
		super.setAttributesRequiredOnCreation("idSchuljahresabschnitt", "abijahr", "halbjahr", "quartal");
	}

	/**
	 * Gibt die Daten eines {@link GostKlausurtermin}s zu deren ID zurück.
	 *
	 * @param id   Die ID des {@link GostKlausurtermin}s.
	 *
	 * @return die Daten des {@link GostKlausurtermin}s zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public GostKlausurtermin getById(final Long id) throws ApiOperationException {
		final DTOGostKlausurenTermine klasseDto = getDTO(id);
		return map(klasseDto);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOGostKlausurenTermine} Objekt zur angegebenen Klassen ID.
	 *
	 * @param id ID des {@link DTOGostKlausurenTermine} Objekts.
	 *
	 * @return Ein {@link DTOGostKlausurenTermine} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOGostKlausurenTermine getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den GostKlausurtermin darf nicht null sein.");

		final DTOGostKlausurenTermine klasseDto = conn.queryByKey(DTOGostKlausurenTermine.class, id);
		if (klasseDto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine GostKlausurtermin zur ID " + id + " gefunden.");

		return klasseDto;
	}

	@Override
	protected void initDTO(final DTOGostKlausurenTermine dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected GostKlausurtermin map(final DTOGostKlausurenTermine dto) throws ApiOperationException {
		final GostKlausurtermin daten = new GostKlausurtermin();
		daten.id = dto.ID;
		daten.idSchuljahresabschnitt = dto.Schuljahresabschnitt_ID;
		daten.abijahr = dto.Abi_Jahrgang;
		daten.datum = dto.Datum;
		daten.halbjahr = dto.Halbjahr.id;
		daten.quartal = dto.Quartal;
		daten.startzeit = dto.Startzeit;
		daten.bezeichnung = dto.Bezeichnung;
		daten.bemerkung = dto.Bemerkungen;
		daten.nachschreiberZugelassen = dto.NachschreiberZugelassen;
		daten.istHaupttermin = dto.IstHaupttermin;
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOGostKlausurenTermine dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idSchuljahresabschnitt" -> dto.Schuljahresabschnitt_ID = JSONMapper.convertToLong(value, false);
			case "abijahr" -> dto.Abi_Jahrgang = JSONMapper.convertToInteger(value, false);
			case "halbjahr" -> dto.Halbjahr = DataGostKlausurenVorgabe.checkHalbjahr(JSONMapper.convertToInteger(value, false));
			case "quartal" -> dto.Quartal =	DataGostKlausurenVorgabe.checkQuartal(JSONMapper.convertToInteger(value, false));
			case "bemerkung" -> dto.Bemerkungen = DataGostKlausuren.convertEmptyStringToNull(JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Termine.col_Bemerkungen.datenlaenge()));
			case "bezeichnung" -> dto.Bezeichnung =	DataGostKlausuren.convertEmptyStringToNull(JSONMapper.convertToString(value, true, true, Schema.tab_Gost_Klausuren_Termine.col_Bezeichnung.datenlaenge()));
			case "datum" -> {
				final String newDate = JSONMapper.convertToString(value, true, false, null);
				final boolean change = !Objects.equals(newDate, dto.Datum);
				if (change)
					handleRaumzuweisungenBeiTerminverschiebung(dto);
				dto.Datum = newDate;
				if (newDate == null)
					dto.Startzeit = null;
				else if (change && raumDataChanged == null)
					raumDataChanged = new GostKlausurenCollectionSkrsKrsData(); // neu berechnen
			}
			case "startzeit" -> {
				final Integer startzeit = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
				final boolean change = startzeit != null && !startzeit.equals(dto.Startzeit);
				dto.Startzeit = startzeit;
				if (change && raumDataChanged == null)
					raumDataChanged = new GostKlausurenCollectionSkrsKrsData(); // neu berechnen
			}
			case "istHaupttermin" -> dto.IstHaupttermin = JSONMapper.convertToBoolean(value, false);
			case "nachschreiberZugelassen" -> {
				final boolean newValue = JSONMapper.convertToBoolean(value, false);
				if ((dto.NachschreiberZugelassen != null) && dto.NachschreiberZugelassen.booleanValue() && !newValue
						&& !new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuTerminIds(ListUtils.create1(dto.ID)).stream().filter(skt -> skt.folgeNr > 0).toList().isEmpty())
					throw new ApiOperationException(Status.FORBIDDEN, "Klausurtermin enthält Nachschreibklausuren");
				dto.NachschreiberZugelassen = newValue;
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	@Override
	public Response patchAsResponse(final Long id, final InputStream is) throws ApiOperationException {
		patchFromStream(id, is);
		if (raumDataChanged == null)
			raumDataChanged = new GostKlausurenCollectionSkrsKrsData();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(raumDataChanged).build();
	}

	@Override
	protected void saveDatabaseDTO(final DTOGostKlausurenTermine dto) throws ApiOperationException {
		if (raumDataChanged != null)
			raumDataChanged.addAll(new DataGostKlausurenSchuelerklausurraumstunde(conn).updateRaeumeZuKlausurtermin(map(dto)));
	}

	/**
	 * Gibt die Liste der Klausurtermine zu den übergebenen Kursklausuren zurück.
	 *
	 * @param kursKlausuren die Liste der Kursklausuren, zu denen die Klausurtermine gesucht werden.
	 *
	 * @return die Liste der Klausurtermine
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurtermin> getKlausurtermineZuKursklausuren(final List<GostKursklausur> kursKlausuren)
			throws ApiOperationException {
		if (kursKlausuren.isEmpty())
			return new ArrayList<>();
		final List<DTOGostKlausurenTermine> terminDTOs = conn.queryByKeyList(DTOGostKlausurenTermine.class,
				kursKlausuren.stream().map(kk -> kk.idTermin).toList());
		return mapList(terminDTOs);
	}

	/**
	 * Gibt die Liste der Klausurtermine zu den übergebenen Schülerklausurterminen zurück.
	 *
	 * @param schuelerklausurTermine die Liste der Schülerklausurterminen, zu denen die Klausurtermine gesucht werden.
	 *
	 * @return die Liste der Klausurtermine
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Set<GostKlausurtermin> getKlausurtermineZuSchuelerklausurterminen(final Collection<GostSchuelerklausurTermin> schuelerklausurTermine) throws ApiOperationException {
		final Set<GostKlausurtermin> ergebnis = new HashSet<>();
		if (schuelerklausurTermine.isEmpty())
			return new HashSet<>(ergebnis);
		final List<GostSchuelerklausurTermin> schuelerklausurTermineNullTermin = schuelerklausurTermine.stream().filter(skt -> skt.folgeNr == 0).toList();
		if (!schuelerklausurTermineNullTermin.isEmpty()) {
			final List<GostSchuelerklausur> schuelerklausurNullTermin =
					new DataGostKlausurenSchuelerklausur(conn).getSchuelerklausurenZuSchuelerklausurterminen(schuelerklausurTermineNullTermin);
			final List<GostKursklausur> kursklausurNullTermin =
					new DataGostKlausurenKursklausur(conn).getKursklausurenZuSchuelerklausuren(schuelerklausurNullTermin);
			ergebnis.addAll(getKlausurtermineZuKursklausuren(kursklausurNullTermin));
		}

		// TODO Termine von Kursklausuren ergänzen

		final List<DTOGostKlausurenTermine> terminDTOs = conn.queryByKeyList(DTOGostKlausurenTermine.class,
				schuelerklausurTermine.stream().map(skt -> skt.idTermin).toList());
		ergebnis.addAll(mapList(terminDTOs));
		return new HashSet<>(ergebnis);
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param abiturjahr 	das Abiturjahr
	 * @param halbjahr das Gost-Halbjahr
	 * @param ganzesSchuljahr true, um Termine für das gesamte Schuljahr zu erhalten, false nur für das übergeben Halbjahr
	 * @param plusTerminIds diese IDs werden zusätzlich berücksichtigt
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurtermin> getKlausurtermine(final int abiturjahr, final int halbjahr, final boolean ganzesSchuljahr, final List<Long> plusTerminIds)
			throws ApiOperationException {
		final GostHalbjahr ghj = (halbjahr < 0) ? null : DataGostKlausurenVorgabe.checkHalbjahr(halbjahr);

		final String plusHJ = (ghj == null ? "" : " AND t.Halbjahr IN :hj");
		final String plusTermine = ((plusTerminIds == null || plusTerminIds.isEmpty()) ? "" : " OR t.ID IN :plusIds");
		final TypedQuery<DTOGostKlausurenTermine> query = conn.query("SELECT t FROM DTOGostKlausurenTermine t WHERE t.Abi_Jahrgang = :jgid" + plusHJ + plusTermine, DTOGostKlausurenTermine.class)
				.setParameter("jgid", abiturjahr);
		if (ghj != null)
			query.setParameter("hj", Arrays.asList(ganzesSchuljahr ? ghj.getSchuljahr() : new GostHalbjahr[] { ghj }));
		if (plusTermine.length() > 0)
			query.setParameter("plusIds", plusTerminIds);
		return mapList(query.getResultList());
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param termin Termin
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurtermin> getKlausurterminmengeSelbesDatumZuTermin(final GostKlausurtermin termin)
			throws ApiOperationException {
		if (termin.datum == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Klausurtermin hat kein Datum gesetzt, ID: " + termin.id);
		return mapList(conn.queryList(DTOGostKlausurenTermine.QUERY_BY_DATUM, DTOGostKlausurenTermine.class, termin.datum));
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param terminId Termin
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurtermin> getKlausurterminmengeSelbesDatumZuId(final long terminId)
			throws ApiOperationException {
		return getKlausurterminmengeSelbesDatumZuTermin(getById(terminId));
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 *
	 * @param termine Termin-Liste
	 *
	 * @return die Liste der Kursklausuren
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurtermin> getKlausurterminmengeSelbesDatumZuTerminMenge(final List<GostKlausurtermin> termine)
			throws ApiOperationException {
		if (termine.isEmpty())
			return new ArrayList<>();
		return mapList(conn.queryList(DTOGostKlausurenTermine.QUERY_LIST_BY_DATUM, DTOGostKlausurenTermine.class, termine.stream().map(t -> t.datum).toList()));
	}

	/**
	 * Gibt die Klausurtermine zur übergebenen ID-Liste zurück oder eine Exception, falls er nicht in der DB vorhanden ist.
	 *
	 * @param listIds	 die Liste von IDs der Klausurtermine
	 *
	 * @return der Klausurtermin
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<GostKlausurtermin> getKlausurtermineZuIds(final List<Long> listIds) throws ApiOperationException {
		final List<DTOGostKlausurenTermine> terminDTOs = getKlausurterminDTOsZuIds(conn, listIds);
		return mapList(terminDTOs);
	}

	/**
	 * Gibt die Klausurtermin-DTOs zur übergebenen ID-Liste zurück oder eine Exception, falls er nicht in der DB vorhanden ist.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param listIds	 die Liste von IDs der Klausurtermine
	 *
	 * @return der Klausurtermin
	 */
	public static List<DTOGostKlausurenTermine> getKlausurterminDTOsZuIds(final DBEntityManager conn, final List<Long> listIds) {
		if (listIds.isEmpty())
			return new ArrayList<>();
		return conn.queryByKeyList(DTOGostKlausurenTermine.class, listIds);
	}

	private void handleRaumzuweisungenBeiTerminverschiebung(final DTOGostKlausurenTermine dtoCopyRaeume) throws ApiOperationException {
	    final GostKlausurtermin referenzTermin = map(dtoCopyRaeume);
	    final List<GostKlausurtermin> termineAmGleichenDatum = new ArrayList<>();
	    if (dtoCopyRaeume.Datum != null) {
	    	termineAmGleichenDatum.addAll(new DataGostKlausurenTermin(conn).getKlausurterminmengeSelbesDatumZuId(dtoCopyRaeume.ID));
	    } else {
		    termineAmGleichenDatum.add(referenzTermin);
	    }

	    final List<Long> terminIds = termineAmGleichenDatum.stream().map(t -> t.id).toList();

	    final GostKlausurenCollectionRaumData raumDataOriginal = new DataGostKlausurenSchuelerklausurraumstunde(conn).getRaumDataByTerminids(terminIds);
	    final List<GostSchuelerklausurTermin> schuelerKlausurTermine = new DataGostKlausurenSchuelerklausurTermin(conn).getSchuelerklausurtermineZuTerminIds(terminIds);
	    final List<GostSchuelerklausur> schuelerKlausuren = new DataGostKlausurenSchuelerklausur(conn).getSchuelerklausurenZuSchuelerklausurterminen(schuelerKlausurTermine);
	    final List<GostKursklausur> kursklausuren = new DataGostKlausurenKursklausur(conn).getKursklausurenZuSchuelerklausuren(schuelerKlausuren);

	    final GostKlausurplanManager manager = new GostKlausurplanManager(
	        new DataGostKlausurenVorgabe(conn).getKlausurvorgabenZuKursklausuren(kursklausuren),
	        kursklausuren,
	        termineAmGleichenDatum,
	        schuelerKlausuren,
	        schuelerKlausurTermine
	    );

	    manager.addRaumData(raumDataOriginal);

	    final List<GostKlausurraumRich> neueRaumListeRich = new ArrayList<>();
	    final Set<GostKlausurraum> neueRaumListe = new HashSet<>();

	    for (final GostKlausurraum raum : raumDataOriginal.raeume) {
	    	if (raum.idTermin == referenzTermin.id && raum.idStundenplanRaum != null) {
	    	    final Map<String, Object> patchAttributes = new HashMap<>();
	    	    patchAttributes.put("idStundenplanRaum", null);
	    	    new DataGostKlausurenRaum(conn).patch(raum.id, patchAttributes);
	    	    raum.idStundenplanRaum = null;
	    	    neueRaumListe.add(raum);
	    	}
	        if (istTerminraumMitTerminfremdenKlausuren(raum, dtoCopyRaeume.ID, manager))
	            bearbeiteTerminraumMitTerminfremdenKlausuren(raum, manager, neueRaumListe, neueRaumListeRich);
	        else if (istTerminfremderRaumMitTerminklausuren(raum, manager, referenzTermin))
	            bearbeiteTerminfremdenRaumMitTerminklausuren(raum, manager, neueRaumListe, neueRaumListeRich, referenzTermin);
	        // Sonstige Fälle: keine Änderung
	    }
		if (raumDataChanged == null)
			raumDataChanged = new GostKlausurenCollectionSkrsKrsData();
	    if (!neueRaumListeRich.isEmpty())
		    raumDataChanged.addAll(new DataGostKlausurenSchuelerklausurraumstunde(conn).setzeRaumZuSchuelerklausurterminen(neueRaumListeRich));
	    if (!neueRaumListe.isEmpty())
	    	raumDataChanged.raumdata.raeume.addAll(neueRaumListe);
	}

	private static boolean istTerminraumMitTerminfremdenKlausuren(final GostKlausurraum raum, final long terminId, final GostKlausurplanManager manager) {
	    return raum.idTermin == terminId && manager.raumEnthaeltTerminfremdeKlausuren(raum);
	}

	private static boolean istTerminfremderRaumMitTerminklausuren(final GostKlausurraum raum, final GostKlausurplanManager manager, final GostKlausurtermin referenzTermin) {
	    return raum.idTermin != referenzTermin.id && !manager.schuelerklausurterminGetMengeByRaumAndTermin(raum, referenzTermin).isEmpty();
	}

	private void bearbeiteTerminraumMitTerminfremdenKlausuren(final GostKlausurraum raum, final GostKlausurplanManager manager,
			final Collection<GostKlausurraum> raumListe, final List<GostKlausurraumRich> raumListeNeu) throws ApiOperationException {
	    final List<GostSchuelerklausurTermin> fremdTerminSkts = manager.schuelerklausurterminFremdterminGetMengeByRaum(raum);
	    final GostKlausurtermin fremdTermin = manager.terminOrExceptionBySchuelerklausurTermin(fremdTerminSkts.get(0));

	    // Erstelle neuen Raum für Terminfremde Klausuren
	    final Map<String, Object> initAttributes = new HashMap<>();
	    initAttributes.put("idTermin", fremdTermin.id);
	    initAttributes.put("bemerkung", raum.bemerkung);

	    final GostKlausurraum neuerRaum = new DataGostKlausurenRaum(conn).add(initAttributes);

	    raumListe.add(neuerRaum);
	    raumListe.add(raum);

	    final GostKlausurraumRich neuerRaumRich = new GostKlausurraumRich(neuerRaum, null);
	    neuerRaumRich.schuelerklausurterminIDs = fremdTerminSkts.stream().map(skt -> skt.id).toList();
	    raumListeNeu.add(neuerRaumRich);
	}

	private void bearbeiteTerminfremdenRaumMitTerminklausuren(final GostKlausurraum raum, final GostKlausurplanManager manager,
			final Collection<GostKlausurraum> raumListe, final List<GostKlausurraumRich> raumListeNeu, final GostKlausurtermin termin) throws ApiOperationException {
	    final Map<String, Object> initAttributes = new HashMap<>();

	    initAttributes.put("idTermin", termin.id);
	    initAttributes.put("bemerkung", raum.bemerkung);

	    final GostKlausurraum neuerRaum = new DataGostKlausurenRaum(conn).add(initAttributes);
	    raumListe.add(neuerRaum);

	    final GostKlausurraumRich neuerRaumRich = new GostKlausurraumRich(neuerRaum, null);
	    neuerRaumRich.schuelerklausurterminIDs = manager.schuelerklausurterminGetMengeByRaumAndTermin(raum, termin).stream().map(skt -> skt.id).toList();
	    raumListeNeu.add(neuerRaumRich);
	}

}
