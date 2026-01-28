package de.svws_nrw.data.faecher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.Strings;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link FachDaten}.
 */
public final class DataFaecher extends DataManagerRevised<Long, DTOFach, FachDaten> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link FachDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataFaecher(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", "referenziertInAnderenTabellen");
		setAttributesRequiredOnCreation("kuerzel", "kuerzelStatistik", "bezeichnung");
	}

	@Override
	protected void initDTO(final DTOFach dto, final Long newId, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newId;
	}

	@Override
	protected long getLongId(final DTOFach fach) {
		return fach.ID;
	}

	@Override
	public FachDaten getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Fachs darf nicht null sein.");
		}
		final DTOFach fach = conn.queryByKey(DTOFach.class, id);
		if (fach == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Fach mit der ID %d gefunden.".formatted(id));
		}
		return map(fach);
	}

	@Override
	public List<FachDaten> getAll() {
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		final Set<Long> idsOfReferencedFaecher = this.getIdsOfReferencedFaecher(mapToIds(faecher));

		return faecher
				.stream()
				.map(this::map)
				.map(f -> setReferenceFlag(f, idsOfReferencedFaecher))
				.sorted(Comparator.comparing(f -> f.id))
				.toList();
	}

	@Override
	protected FachDaten map(final DTOFach dto) {
		final FachDaten fach = new FachDaten();
		fach.id = dto.ID;
		fach.kuerzel = dto.Kuerzel;
		fach.bezeichnung = dto.Bezeichnung;
		fach.kuerzelStatistik = dto.StatistikKuerzel;
		fach.aufgabenfeld = dto.Aufgabenfeld;
		fach.bilingualeSprache = dto.Unterrichtssprache;
		fach.aufZeugnis = Boolean.TRUE.equals(dto.AufZeugnis);
		fach.bezeichnungZeugnis = dto.BezeichnungZeugnis;
		fach.bezeichnungUeberweisungszeugnis = dto.BezeichnungUeberweisungsZeugnis;
		fach.istOberstufenFach = Boolean.TRUE.equals(dto.IstOberstufenFach);
		fach.istPruefungsordnungsRelevant = Boolean.TRUE.equals(dto.IstPruefungsordnungsRelevant);
		fach.istFremdsprache = Boolean.TRUE.equals(dto.IstFremdsprache);
		fach.istMoeglichAlsNeueFremdspracheInSekII = Boolean.TRUE.equals(dto.IstMoeglichAlsNeueFremdspracheInSekII);
		fach.istNachpruefungErlaubt = Boolean.TRUE.equals(dto.IstNachpruefungErlaubt);
		fach.istSchriftlichZK = Boolean.TRUE.equals(dto.IstSchriftlichZK);
		fach.istSchriftlichBA = Boolean.TRUE.equals(dto.IstSchriftlichBA);
		fach.istFHRFach = (dto.GewichtungFHR != null) && (dto.GewichtungFHR != 0);
		fach.holeAusAltenLernabschnitten = Boolean.TRUE.equals(dto.AbgeschlFaecherHolen);
		fach.maxZeichenInFachbemerkungen = dto.MaxBemZeichen;
		fach.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		fach.sortierung = Objects.requireNonNullElse(dto.SortierungAllg, 32000);
		return fach;
	}

	@Override
	protected void mapAttribute(final DTOFach dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "kuerzel" -> updateKuerzel(dto, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			case "kuerzelStatistik" -> updateKuerzelStatistik(dto, name, value);
			case "aufgabenfeld" -> dto.Aufgabenfeld =
					JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Faecher.col_Aufgabenfeld.datenlaenge(), name);
			case "bilingualeSprache" -> dto.Unterrichtssprache =
					JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Faecher.col_Unterichtssprache.datenlaenge(), name);
			case "aufZeugnis" -> dto.AufZeugnis = JSONMapper.convertToBoolean(value, true, name);
			case "bezeichnungZeugnis" -> dto.BezeichnungZeugnis =
					JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Faecher.col_ZeugnisBez.datenlaenge(), name);
			case "bezeichnungUeberweisungszeugnis" -> dto.BezeichnungUeberweisungsZeugnis =
					JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Faecher.col_UeZeugnisBez.datenlaenge(), name);
			case "istOberstufenFach" -> dto.IstOberstufenFach = JSONMapper.convertToBoolean(value, true, name);
			case "istPruefungsordnungsRelevant" -> dto.IstPruefungsordnungsRelevant = JSONMapper.convertToBoolean(value, true, name);
			case "istFremdsprache" -> dto.IstFremdsprache = JSONMapper.convertToBoolean(value, true, name);
			case "istMoeglichAlsNeueFremdspracheInSekII" -> dto.IstMoeglichAlsNeueFremdspracheInSekII = JSONMapper.convertToBoolean(value, true, name);
			case "istNachpruefungErlaubt" -> dto.IstNachpruefungErlaubt = JSONMapper.convertToBoolean(value, true, name);
			case "istSchriftlichZK" -> dto.IstSchriftlichZK = JSONMapper.convertToBoolean(value, true, name);
			case "istSchriftlichBA" -> dto.IstSchriftlichBA = JSONMapper.convertToBoolean(value, true, name);
			case "istFHRFach" -> dto.GewichtungFHR = Boolean.TRUE.equals(JSONMapper.convertToBoolean(value, true, name)) ? 1 : 0;
			case "holeAusAltenLernabschnitten" -> dto.AbgeschlFaecherHolen = JSONMapper.convertToBoolean(value, true, name);
			case "maxZeichenInFachbemerkungen" -> dto.MaxBemZeichen = JSONMapper.convertToIntegerInRange(value, true, 0, null, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.SortierungAllg = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOFach> faecher, final Map<Long, SimpleOperationResponse> responses) {
		final Set<Long> idsOfReferencedFaecher = getIdsOfReferencedFaecher(mapToIds(faecher));
		faecher.stream()
				.filter(f -> idsOfReferencedFaecher.contains(f.ID))
				.forEach(f -> markResponseAsFailed(responses.get(f.ID), f.Bezeichnung));
	}

	private static void validateId(final DTOFach dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void updateKuerzel(final DTOFach dto, final String name, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_EigeneSchule_Faecher.col_FachKrz.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Kuerzel, kuerzel)) {
			return;
		}
		final boolean notUnqiue = this.conn.queryAll(DTOFach.class)
				.stream()
				.anyMatch(f -> (f.ID != dto.ID) && Strings.CI.equals(kuerzel, f.Kuerzel));
		if (notUnqiue) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Kürzel %s wird bereits verwendet.".formatted(kuerzel));
		}
		dto.Kuerzel = kuerzel;
	}

	private void updateBezeichnung(final DTOFach dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_EigeneSchule_Faecher.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung)) {
			return;
		}
		final boolean notUnqiue = this.conn.queryAll(DTOFach.class)
				.stream()
				.anyMatch(f -> (f.ID != dto.ID) && Strings.CI.equals(bezeichnung, f.Bezeichnung));
		if (notUnqiue) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s wird bereits verwendet.".formatted(bezeichnung));
		}
		dto.Bezeichnung = bezeichnung;
	}

	private static void updateKuerzelStatistik(final DTOFach dto, final String name, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_EigeneSchule_Faecher.col_StatistikKrz.datenlaenge(), name);
		final Fach fach = Fach.data().getWertBySchluessel(kuerzel);
		if (fach == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Fach mit dem Kuerzel %s wurde nicht gefunden.".formatted(kuerzel));
		}
		dto.StatistikKuerzel = kuerzel;
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || ((newValue != null) && newValue.isBlank());
	}

	private static Set<Long> mapToIds(final List<DTOFach> faecher) {
		return faecher
				.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private static FachDaten setReferenceFlag(final FachDaten fach, final Set<Long> idsOfReferencedFaecher) {
		fach.referenziertInAnderenTabellen = idsOfReferencedFaecher.contains(fach.id);
		return fach;
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String name) {
		response.success = false;
		response.log.add(("Das Fach mit dem Name %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden.").formatted(name));
	}

	private Set<Long> getIdsOfReferencedFaecher(final Set<Long> idsFaecher) {
		if ((idsFaecher == null) || idsFaecher.isEmpty()) {
			return Collections.emptySet();
		}
		final String queryGostJahrgangFachwahlen = "SELECT DISTINCT a.Fach_ID FROM DTOGostJahrgangFachbelegungen a WHERE a.Fach_ID IN :idsFaecher";
		final String queryGostSchuelerFachwahlen = "SELECT DISTINCT b.Fach_ID FROM DTOGostSchuelerFachbelegungen b WHERE b.Fach_ID IN :idsFaecher";
		final String queryKurse = "SELECT DISTINCT c.Fach_ID FROM DTOKurs c WHERE c.Fach_ID IN :idsFaecher";
		final String querySchuelerAbiFaecher = "SELECT DISTINCT d.Fach_ID FROM DTOSchuelerAbiturFach d WHERE d.Fach_ID IN :idsFaecher";
		final String querySchuelerBKFaecher = "SELECT DISTINCT e.Fach_ID FROM DTOSchuelerBKFach e WHERE e.Fach_ID IN :idsFaecher";
		final String querySchuelerFehlstunden = "SELECT DISTINCT f.Fach_ID FROM DTOSchuelerFehlstunden f WHERE f.Fach_ID IN :idsFaecher";
		final String querySchuelerFHRFaecher = "SELECT DISTINCT g.Fach_ID FROM DTOSchuelerFHRFach g WHERE g.Fach_ID IN :idsFaecher";
		final String querySchuelerLeistungsdaten = "SELECT DISTINCT h.Fach_ID FROM DTOSchuelerLeistungsdaten h WHERE h.Fach_ID IN :idsFaecher";
		final String querySchuelerLernabschnittsdaten = "SELECT i.Fachklasse_ID FROM DTOSchuelerLernabschnittsdaten i WHERE i.Fachklasse_ID IN :idsFaecher";
		final String querySchuelerZP10 = "SELECT DISTINCT j.Fach_ID FROM DTOSchuelerZP10 j WHERE j.Fach_ID IN :idsFaecher";
		final String querySchuelerZuweisungen = "SELECT DISTINCT k.Fach_ID FROM DTOSchuelerZuweisung k WHERE k.Fach_ID IN :idsFaecher";

		final String query = String.join("\nUNION ALL\n", queryGostJahrgangFachwahlen, queryGostSchuelerFachwahlen, queryKurse, querySchuelerAbiFaecher,
				querySchuelerBKFaecher, querySchuelerFehlstunden, querySchuelerFHRFaecher, querySchuelerLeistungsdaten, querySchuelerLernabschnittsdaten,
				querySchuelerZP10, querySchuelerZuweisungen);
		final List<Long> results = conn.query(query, Long.class).setParameter("idsFaecher", idsFaecher).getResultList();
		return new HashSet<>(results);
	}

	/**
	 * Setzt für die Fächer der Fächerliste Default-Werte in das Feld Sortierung.
	 * Diese orientieren sich an der Sortierreihenfolge der Fächer der Oberstufe.
	 * Fächer, die nicht der Oberstufe zugeordnet werden können werden mit
	 * der ursprünglichen Sortierung angehangen.
	 *
	 * @param conn                   die Datenbankverbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response setDefaultSortierungSekII(final DBEntityManager conn) throws ApiOperationException {
		// Bestimme zunächst die Schulform
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}
		final Schulform schulform = Schulform.data().getWertByKuerzel(schule.SchulformKuerzel);
		if (schulform == null) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine gültiger Schuljahresabschnitt vorhanden.");
		}
		// Bestimme die Fächer
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if ((faecher == null) || (faecher.isEmpty())) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Fächer gefunden.");
		}
		if (!schulform.daten(schuljahresabschnitt.Jahr).hatGymOb) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Default-Sortierung für die Sekundarstufe II erfordert eine entsprechende Schulform.");
		}
		// Lege Datenstrukturen für die Zuordnung zu den einzelnen Statistik-Fächern an und befülle diese
		final Set<Fach> setGostFaecher = GostFachbereich.getAlleFaecher().keySet();
		final ArrayMap<Fach, List<DTOFach>> map = new ArrayMap<>(Fach.values());
		final List<DTOFach> nichtZugeordnet = new ArrayList<>();
		for (final DTOFach fach : faecher) {
			final Fach tmpFach = Fach.data().getWertBySchluessel(fach.StatistikKuerzel);
			if (setGostFaecher.contains(tmpFach)) {
				map.computeIfAbsent(tmpFach, k -> new ArrayList<>()).add(fach);
			} else {
				nichtZugeordnet.add(fach);
			}
		}
		// Bestimme die Fächer der Oberstufe in Standard-Sortierung
		final List<Fach> gostFaecher = GostFachbereich.getAlleFaecherSortiert();
		final List<DTOFach> faecherSortiert = new ArrayList<>();
		for (final Fach gostFach : gostFaecher) {
			final List<DTOFach> tmpFach = map.get(gostFach);
			if (tmpFach == null) {
				continue;
			}
			tmpFach.sort((final DTOFach a, final DTOFach b) -> a.Kuerzel.compareToIgnoreCase(b.Kuerzel));
			faecherSortiert.addAll(tmpFach);
		}
		faecherSortiert.addAll(nichtZugeordnet);
		int i = 1;
		for (final DTOFach fach : faecherSortiert) {
			fach.SortierungAllg = i++;
			fach.SortierungSekII = fach.SortierungAllg;
			conn.transactionPersist(fach);
		}
		return Response.status(Status.NO_CONTENT).type(MediaType.APPLICATION_JSON).build();
	}

}
