package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.fach.SprachreferenzniveauKatalogEintrag;
import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.fach.Sprachreferenzniveau;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.Strings;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link Sprachbelegung}.
 */
public final class DataSchuelerSprachbelegung extends DataManagerRevised<Long, DTOSchuelerSprachenfolge, Sprachbelegung> {

	private static final String SPRACHE = "sprache";

	private final long idSchueler;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Sprachbelegung}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler   die ID des Schülers
	 */
	public DataSchuelerSprachbelegung(final DBEntityManager conn, final long idSchueler) {
		super(conn);
		this.idSchueler = idSchueler;
		setAttributesRequiredOnCreation(SPRACHE);
	}

	@Override
	protected void initDTO(final DTOSchuelerSprachenfolge dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.ID = newId;
		dto.Schueler_ID = idSchueler;
	}

	private static Sprachbelegung mapInternal(final DBEntityManager conn, final DTOSchueler dtoSchueler, final DTOSchuelerSprachenfolge dto) {
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoSchueler.Schuljahresabschnitts_ID);
		final Sprachbelegung sprachbelegung = new Sprachbelegung();
		sprachbelegung.id = dto.ID;
		sprachbelegung.sprache = dto.Sprache;
		sprachbelegung.istNachweis = Boolean.TRUE.equals(dto.IstNachweis);
		sprachbelegung.reihenfolge = dto.ReihenfolgeNr;
		sprachbelegung.belegungVonJahrgang = dto.ASDJahrgangVon;
		sprachbelegung.belegungVonAbschnitt = dto.AbschnittVon;
		sprachbelegung.belegungBisJahrgang = dto.ASDJahrgangBis;
		sprachbelegung.belegungBisAbschnitt = dto.AbschnittBis;
		sprachbelegung.referenzniveau = getSchluesselSprachreferenzniveau(dto, schuljahresabschnitt);
		sprachbelegung.hatKleinesLatinum = Boolean.TRUE.equals(dto.KleinesLatinumErreicht);
		sprachbelegung.hatLatinum = Boolean.TRUE.equals(dto.LatinumErreicht);
		sprachbelegung.hatGraecum = Boolean.TRUE.equals(dto.GraecumErreicht);
		sprachbelegung.hatHebraicum = Boolean.TRUE.equals(dto.HebraicumErreicht);
		return sprachbelegung;
	}

	private static String getSchluesselSprachreferenzniveau(final DTOSchuelerSprachenfolge dto, final Schuljahresabschnitt schuljahresabschnitt) {
		if (dto.Referenzniveau == null) {
			return null;
		}

		final SprachreferenzniveauKatalogEintrag referenzniveauEintrag =
				Sprachreferenzniveau.data().getEintragBySchuljahrUndSchluessel(schuljahresabschnitt.schuljahr, dto.Referenzniveau);
		if (referenzniveauEintrag == null) {
			return null;
		}

		return referenzniveauEintrag.schluessel;
	}

	@Override
	public Sprachbelegung map(final DTOSchuelerSprachenfolge dto) {
		final DTOSchueler dtoSchueler = getSchuelerById(dto.Schueler_ID);
		return mapInternal(conn, dtoSchueler, dto);
	}

	@Override
	protected void mapAttribute(final DTOSchuelerSprachenfolge dto, final String name, final Object value, final Map<String, Object> map) {
		final DTOSchueler dtoSchueler = getSchuelerById(dto.Schueler_ID);
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoSchueler.Schuljahresabschnitts_ID);
		switch (name) {
			case SPRACHE -> updateSprache(dto, value, schuljahresabschnitt);
			case "reihenfolge" -> dto.ReihenfolgeNr = JSONMapper.convertToIntegerInRange(value, true, 0, 9);
			case "belegungVonJahrgang" -> updateBelegungVonJahrgang(dto, value, schuljahresabschnitt);
			case "belegungVonAbschnitt" -> updateBelegungVonAbschnitt(dto, value);
			case "belegungBisJahrgang" -> updateBelegungBisJahrgang(dto, value, schuljahresabschnitt);
			case "belegungBisAbschnitt" -> updateBelegungBisAbschnitt(dto, value);
			case "referenzniveau" -> updateReferenzniveau(dto, value, schuljahresabschnitt);
			case "hatKleinesLatinum" -> dto.KleinesLatinumErreicht = JSONMapper.convertToBoolean(value, false, "hatKleinesLatinum");
			case "hatLatinum" -> dto.LatinumErreicht = JSONMapper.convertToBoolean(value, false, "hatLatinum");
			case "hatGraecum" -> dto.GraecumErreicht = JSONMapper.convertToBoolean(value, false, "hatGraecum");
			case "hatHebraicum" -> dto.HebraicumErreicht = JSONMapper.convertToBoolean(value, false, "hatHebraicum");
			case "istNachweis" -> dto.IstNachweis = JSONMapper.convertToBoolean(value, false, "istNachweis");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}

	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der aktuellen Klasse des Schülers hat, um die Sprachbelegung zu erstellen
		checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt();

		final String kuerzel = JSONMapper.convertToString(initAttributes.get(SPRACHE), false, false, 2);
		if (existsByKuerzel(kuerzel)) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Es existiert bereits eine Sprachenbelegung mit dem Kürzel %s für den Schüler mit der ID %d".formatted(kuerzel, this.idSchueler)
			);
		}
	}

	@Override
	public void checkBeforePatch(final DTOSchuelerSprachenfolge dto, final Map<String, Object> patchAttributes) {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der aktuellen Klasse des Schülers hat, um die Sprachbelegung zu verändern
		checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt();

		if (patchAttributes.get(SPRACHE) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Bei einem Patch für die Sprachbelegung muss ein Sprachkürzel angegeben werden.");
		}

		final String patchSprache = JSONMapper.convertToString(patchAttributes.get(SPRACHE), false, false, 2);
		if (!patchSprache.equals(dto.Sprache)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Bei einem Patch für die Sprachbelegung muss das Sprachkürzel im Patch mit dem Sprachkürzel im DTO übereinstimmen.");
		}
	}

	@Override
	public void checkBeforeDeletion(final List<DTOSchuelerSprachenfolge> dtos) {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der aktuellen Klasse des Schülers hat, um die Sprachbelegung zu löschen
		checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt();
	}

	@Override
	public List<Sprachbelegung> getList() {
		checkSchuelerExists();

		final List<DTOSchuelerSprachenfolge> dtos = conn.queryList(DTOSchuelerSprachenfolge.QUERY_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, idSchueler);
		final List<Sprachbelegung> result = new ArrayList<>();
		for (final DTOSchuelerSprachenfolge dto : dtos) {
			result.add(map(dto));
		}
		return result;
	}

	private boolean existsByKuerzel(final String kuerzel) {
		return conn.existsBy("SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID = ?1 AND e.Sprache = ?2",
				DTOSchuelerSprachenfolge.class, idSchueler, kuerzel);
	}

	@Nonnull
	private DTOSchuelerSprachenfolge getByKuerzel(final String kuerzel) {
		if (kuerzel.isBlank()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde kein gültiges Kürzel übergeben.");
		}

		checkSchuelerExists();

		// Bestimme die zugehörige Sprachbelegung
		final List<DTOSchuelerSprachenfolge> belegungen = conn.queryList("SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID = ?1 AND e.Sprache = ?2",
				DTOSchuelerSprachenfolge.class, idSchueler, kuerzel);
		if (belegungen.isEmpty()) {
			throw new ApiOperationException(
					Status.NOT_FOUND,
					"Keine Sprachbelegung mit dem Kürzel %s für den Schüler mit der ID %d gefunden.".formatted(kuerzel, idSchueler)
			);
		}

		if (belegungen.size() > 1) {
			throw new ApiOperationException(
					Status.INTERNAL_SERVER_ERROR,
					"Es wurden mehrere Einträge zu dem Schüler mit der ID %d und der Sprache %s gefunden.".formatted(idSchueler, kuerzel)
			);
		}

		return belegungen.getFirst();
	}

	@Override
	public Sprachbelegung getById(final Long id) {
		checkSchuelerExists();

		final DTOSchuelerSprachenfolge belegung = conn.queryByKey(DTOSchuelerSprachenfolge.class, id);
		if (belegung == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Sprachbelegung mit der ID %d gefunden.".formatted(id));
		}
		return map(belegung);
	}

	/**
	 * Bestimmt das DTO anhand des übergebenen Kürzels der Sprachbelegung und der Schüler-ID
	 *
	 * @param kuerzel   das Kürzel der Sprachbelegung
	 *
	 * @return die Sprachbelegung als Response
	 *
	 * @throws ApiOperationException im FehlerFall
	 */
	public Response getByKuerzelAsResponse(final @NotNull String kuerzel) {
		final DTOSchuelerSprachenfolge dtoBelegung = getByKuerzel(kuerzel);
		final Sprachbelegung belegung = map(dtoBelegung);

		return Response.ok(belegung).build();
	}

	/**
	 * Passt die Informationen der Sprachbelegung mit dem übergebenen Sprach-Kürzel des Schülers mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an.
	 *
	 * @param kuerzel   das Sprachkürzel
	 * @param is        der Input-Stream
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response patchByKuerzelAsResponse(final @NotNull String kuerzel, final InputStream is) {
		final DTOSchuelerSprachenfolge dto = getByKuerzel(kuerzel);
		final Map<String, Object> attributesToPatch = JSONMapper.toMap(is);
		attributesToPatch.put(SPRACHE, kuerzel);
		patch(dto.ID, attributesToPatch);
		return Response.status(Status.NO_CONTENT).build();
	}

	/**
	 * Löscht eine Sprachbelegung anhand des übergebenen Sprachkürzels und der Schüler-ID.
	 *
	 * @param kuerzel   das Kürzel der Sprache
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteByKuerzelAsResponse(final String kuerzel) {
		final DTOSchuelerSprachenfolge dto = getByKuerzel(kuerzel);
		return deleteAsResponse(dto.ID);
	}

	/**
	 * Ermittelt die Map der Sprachbelegungen für die angegebenen IDs von Schülern.
	 *
	 * @param conn          die Datenbankverbindung
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Map der Sprachbelegungen
	 */
	public static Map<Long, List<Sprachbelegung>> getMapBySchuelerIDs(final DBEntityManager conn, final List<Long> idsSchueler) {
		if (idsSchueler.isEmpty()) {
			return Collections.emptyMap();
		}
		final List<DTOSchueler> listSchueler = conn.queryByKeyList(DTOSchueler.class, idsSchueler.stream().filter(Objects::nonNull).toList());
		if (listSchueler.isEmpty()) {
			return Collections.emptyMap();
		}
		final List<Long> listIdsDtoSchueler = listSchueler.stream().map(s -> s.ID).distinct().toList();
		final List<DTOSchuelerSprachenfolge> listSprachenfolgen =
				conn.queryList(DTOSchuelerSprachenfolge.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, listIdsDtoSchueler);
		if (listSprachenfolgen.isEmpty()) {
			return listIdsDtoSchueler.stream().collect(Collectors.toMap(s -> s, s -> new ArrayList<>()));
		}
		// Erstelle die Maps der Schüler und der Sprachbelegungen zur Schüler-ID.
		final Map<Long, DTOSchueler> mapSchueler = listSchueler.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		final Map<Long, List<Sprachbelegung>> result = listSprachenfolgen.stream().collect(Collectors.groupingBy(sf -> sf.Schueler_ID,
				Collectors.mapping(sf -> mapInternal(conn, mapSchueler.get(sf.Schueler_ID), sf), Collectors.toList())));
		// Ergänze leere Liste für Schüler ohne Sprachen in der Sprachenfolge.
		for (final DTOSchueler dtoSchueler : listSchueler) {
			result.computeIfAbsent(dtoSchueler.ID, s -> new ArrayList<>());
		}
		return result;
	}

	private void checkSchuelerExists() {
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		}
	}

	private static void updateSprache(final DTOSchuelerSprachenfolge dto, final Object value, final Schuljahresabschnitt schuljahresabschnitt) {
		final String kuerzelSprache = JSONMapper.convertToString(value, false, false, 2);

		final List<String> allowedFremdsprachen = Fach.getListFremdsprachenKuerzelAtomar(schuljahresabschnitt.schuljahr);
		final var kuerzelNotAllowed = allowedFremdsprachen.stream().noneMatch(sprache -> Strings.CI.equals(kuerzelSprache, sprache));
		if (kuerzelNotAllowed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Sprache mit dem Kürzel %s ist nicht zulässig.".formatted(kuerzelSprache));
		}

		dto.Sprache = kuerzelSprache;
	}

	private static void updateReferenzniveau(final DTOSchuelerSprachenfolge dto, final Object value, final Schuljahresabschnitt schuljahresabschnitt) {
		final String schluessel = JSONMapper.convertToString(value, true, false, 10);
		if (schluessel == null) {
			dto.Referenzniveau = null;
			return;
		}

		final Sprachreferenzniveau niveau = Sprachreferenzniveau.data().getWertBySchluessel(schluessel);
		if (niveau == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Sprachreferenzniveau-Kürzel %s ist ungültig.".formatted(schluessel));
		}

		final SprachreferenzniveauKatalogEintrag niveauEintrag = niveau.daten(schuljahresabschnitt.schuljahr);
		if (niveauEintrag == null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Das Sprachreferenzniveau-Kürzel %s ist dem Schuljahr %d ungültig.".formatted(schluessel, schuljahresabschnitt.schuljahr)
			);
		}

		dto.Referenzniveau = niveauEintrag.schluessel;
	}

	private void updateBelegungBisJahrgang(final DTOSchuelerSprachenfolge dto, final Object value, final Schuljahresabschnitt schuljahresabschnitt) {
		final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
		if (kuerzel == null) {
			dto.ASDJahrgangBis = null;
			return;
		}

		final JahrgaengeKatalogEintrag jahrgangEintrag = getJahrgaengeKatalogEintrag(schuljahresabschnitt, kuerzel);

		pruefeBelegungszeitraum(dto.ASDJahrgangVon, dto.AbschnittVon, jahrgangEintrag.kuerzel, dto.AbschnittBis);

		dto.ASDJahrgangBis = jahrgangEintrag.kuerzel;
	}

	private static JahrgaengeKatalogEintrag getJahrgaengeKatalogEintrag(final Schuljahresabschnitt schuljahresabschnitt, final String kuerzel) {
		final Jahrgaenge jahrgang = Jahrgaenge.data().getWertByKuerzel(kuerzel);
		if (jahrgang == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Jahrgangs-Kürzel %s ist ungültig.".formatted(kuerzel));
		}

		final JahrgaengeKatalogEintrag jahrgangEintrag = jahrgang.daten(schuljahresabschnitt.schuljahr);
		if (jahrgangEintrag == null) {
			throw new ApiOperationException(
					Status.BAD_REQUEST,
					"Das Jahrgangs-Kürzel %s ist dem Schuljahr %d ungültig.".formatted(kuerzel, schuljahresabschnitt.schuljahr)
			);
		}
		return jahrgangEintrag;
	}

	private void updateBelegungVonJahrgang(final DTOSchuelerSprachenfolge dto, final Object value, final Schuljahresabschnitt schuljahresabschnitt) {
		final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
		if (kuerzel == null) {
			dto.ASDJahrgangVon = null;
			return;
		}

		final JahrgaengeKatalogEintrag jahrgangEintrag = getJahrgaengeKatalogEintrag(schuljahresabschnitt, kuerzel);

		pruefeBelegungszeitraum(jahrgangEintrag.kuerzel, dto.AbschnittVon, dto.ASDJahrgangBis, dto.AbschnittBis);

		dto.ASDJahrgangVon = jahrgangEintrag.kuerzel;
	}

	private void updateBelegungVonAbschnitt(final DTOSchuelerSprachenfolge dto, final Object value) {
		final DTOEigeneSchule schule = getEigeneSchule();

		final var upperBound = (schule.AnzahlAbschnitte == null) ? 3 : (schule.AnzahlAbschnitte + 1);
		final var abschnittVon = JSONMapper.convertToIntegerInRange(value, true, 1, upperBound);

		pruefeBelegungszeitraum(dto.ASDJahrgangVon, abschnittVon, dto.ASDJahrgangBis, dto.AbschnittBis);

		dto.AbschnittVon = abschnittVon;
	}

	private void updateBelegungBisAbschnitt(final DTOSchuelerSprachenfolge dto, final Object value) {
		final DTOEigeneSchule schule = getEigeneSchule();

		final var upperBound = (schule.AnzahlAbschnitte == null) ? 3 : (schule.AnzahlAbschnitte + 1);
		final var abschnittBis = JSONMapper.convertToIntegerInRange(value, true, 1, upperBound);

		pruefeBelegungszeitraum(dto.ASDJahrgangVon, dto.AbschnittVon, dto.ASDJahrgangBis, abschnittBis);

		dto.AbschnittBis = abschnittBis;
	}

	private void pruefeBelegungszeitraum(final String vonJahrgangASDKuerzel, final Integer vonAbschnitt, final String bisJahrgangASDKuerzel,
			final Integer bisAbschnitt) {
		if ((vonJahrgangASDKuerzel == null) || (bisJahrgangASDKuerzel == null) || (vonAbschnitt == null) || (bisAbschnitt == null)) {
			return;
		}

		if (Strings.CS.equals(vonJahrgangASDKuerzel, bisJahrgangASDKuerzel) && (bisAbschnitt == 1) && (vonAbschnitt == 2)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der AbschnittVon darf nicht nach dem AbschnittBis im selben Jahrgang liegen");
		}
	}

	private DTOEigeneSchule getEigeneSchule() {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Daten der Schule konnten nicht bestimmt werden.");
		}
		return schule;
	}

	private DTOSchueler getSchuelerById(final Long idSchueler) {
		return conn.queryByKey(DTOSchueler.class, idSchueler);
	}

	/**
	 * Prüft, ob der Benutzer mit einer funktionsbezogenen Kompetenz auf den aktuellen Lernabschnitt des Schülers zugreift
	 * und wenn ja, ob dieser dann die Kompetenz auf den Klassen für diesen Lernabschnitt hat. Hat er diese Kompetenz
	 * nicht, so wird eine Exception geschmissen.
	 *
	 * @throws ApiOperationException   im Fehlerfall, wenn der Benutzer nicht alle Rechte zum Zugriff hat (503 - FORBIDDEN)
	 */
	private void checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt() {
		if (hatBenutzerNurFunktionsbezogeneKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN,
				Set.of(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))) {
			final DTOSchueler dtoSchueler = getSchuelerById(idSchueler);
			if (dtoSchueler == null) {
				throw new ApiOperationException(
						Status.NOT_FOUND,
						"Der Schüler mit der ID %d konnte nicht gefunden werden.".formatted(idSchueler)
				);
			}

			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(
					"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0",
					DTOSchuelerLernabschnittsdaten.class, dtoSchueler.ID, dtoSchueler.Schuljahresabschnitts_ID);
			if (lernabschnitte.size() != 1) {
				throw new ApiOperationException(
						Status.NOT_FOUND,
						"Für den Schüler mit der ID %d konnte kein eindeutiger aktueller Lernabschnitt bestimmt werden.".formatted(dtoSchueler.ID)
				);
			}

			checkBenutzerFunktionsbezogeneKompetenzKlasse(lernabschnitte.getFirst().Klassen_ID);
		}
	}

}
