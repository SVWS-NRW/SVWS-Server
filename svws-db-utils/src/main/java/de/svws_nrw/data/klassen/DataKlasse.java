package de.svws_nrw.data.klassen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.klassen.KlasseDetails;
import de.svws_nrw.asd.data.klassen.KlasseListItem;
import de.svws_nrw.asd.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.json.JsonDaten;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Data-Manager für Klassen
 */
public final class DataKlasse extends DataManagerRevised<Long, DTOKlassen, KlasseDetails> {

	private static final String ID_SCHULJAHRESABSCHNITT = "idSchuljahresabschnitt";
	private static final String KUERZEL = "kuerzel";

	/**
	 * Erstellt ein neues Objekt dieser Klasse.
	 *
	 * @param conn DBEntityManager
	 */
	public DataKlasse(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation(ID_SCHULJAHRESABSCHNITT, KUERZEL, "idJahrgang");
		setAttributesNotPatchable("id", ID_SCHULJAHRESABSCHNITT, "kuerzelVorgaengerklasse", "kuerzelFolgeklasse", "pruefungsordnung");
	}

	/**
	 * Gibt alle Klassen zu einem Schuljahresabschnitt als {@link KlasseDetails} zurück.
	 *
	 * @param idSchuljahresabschnitt ID des Schuljahresabschnittes
	 *
	 * @return Liste der Klassen
	 */
	public Response getAllDetailsBySchuljahresabschnittId(final Long idSchuljahresabschnitt) {
		final var klassen = this.conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt).stream()
				.map(klasse -> mapToDetails(klasse, true))
				.toList();
		return Response.ok(klassen).build();
	}

	/**
	 * Gibt alle Klassen zu einem Schuljahresabschnitt als {@link KlasseListItem} zurück.
	 *
	 * @param idSchuljahresabschnitt ID des Schuljahresabschnittes
	 *
	 * @return Liste der Klassen
	 */
	public Response getAllListItemsBySchuljahresabschnittId(final Long idSchuljahresabschnitt) {
		final var klassen = this.conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt).stream()
				.map(this::mapToListItem)
				.toList();
		return Response.ok(klassen).build();
	}

	/**
	 * Gibt die Daten einer Klasse zu deren ID zurück.
	 *
	 * @param id   Die ID der Klasse.
	 *
	 * @return die Daten der Klasse zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public KlasseDetails getById(final Long id) throws ApiOperationException {
		final DTOKlassen klasse = getDTO(id);
		return mapToDetails(klasse, true);
	}

	/**
	 * Fall 1 (kuerzel != <code>null</code> und asdKuerzel != <code>null</code>): Die Methode versucht im ersten Schritt ein {@link DTOKlassen} Objekt über das
	 * Kürzel und die Halbjahresabschnitt ID zu ermitteln. Wenn dies zu keinem Ergebnis führt, wird anstelle des Kürzels, das ASD-Kürzel versucht. Sollte
	 * anschließend auch keine Klasse gefunden werden, wird eine {@link ApiOperationException} geworfen.
	 * Fall 2 (kuerzel !=  <code>null</code>): Die Methode versucht ein {@link DTOKlassen} Objekt über das Kürzel und die Halbjahresabschnitt ID zu ermitteln
	 * Führt dies zu keinem Ergebnis wird eine {@link ApiOperationException} geworfen.
	 *
	 * @param kuerzel Kürzel der Klasse
	 * @param asdKuerzel ASD-Kürzel der Klasse
	 * @param idSchuljahresabschnitt ID des Halbjahresabschnittes
	 *
	 * @return ein DTOKlasse Objekt
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOKlassen getDTOByKuerzelOrASDKuerzelAndSchuljahresabschnittId(final String kuerzel, final String asdKuerzel, final Long idSchuljahresabschnitt)
			throws ApiOperationException {
		if ((kuerzel == null) && (asdKuerzel == null)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es muss mindestens ein Kürzel oder ASD-Kürzel angegeben sein. Das Beide Kürzel dürfen nicht null sein.");
		}
		if (idSchuljahresabschnitt == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es muss eine Halbjahresabschnitt-ID angegeben sein. Die Halbjahresabschnitt-ID darf nicht null sein.");
		}

		List<DTOKlassen> klassen = new ArrayList<>();
		if (kuerzel != null) {
			klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Klasse = ?1 AND e.Schuljahresabschnitts_ID = ?2",
					DTOKlassen.class, kuerzel, idSchuljahresabschnitt);
		}

		if ((asdKuerzel != null) && klassen.isEmpty()) {
			klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.ASDKlasse = ?1 AND e.Schuljahresabschnitts_ID = ?2",
					DTOKlassen.class, asdKuerzel, idSchuljahresabschnitt);
		}

		if (klassen.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurde keine Klasse mit dem Kürzel %s und dem Halbjahresabschnitt mit der ID %d gefunden.".formatted(kuerzel, idSchuljahresabschnitt));
		}

		return klassen.getFirst();
	}

	/**
	 * Methode liefert eine Liste von {@link KlasseDetails} zur angegebenen SchuljahresabschnittID.
	 *
	 * @param schuljahresabschnittId ID des Schuljahresabschnittes
	 * @param attachSchueler gibt an, ob die Schüler zu den Klassen mit geladen werden sollen
	 *
	 * @return Liste von KlasseDetails Objekten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<KlasseDetails> getListBySchuljahresabschnittID(final Long schuljahresabschnittId, final boolean attachSchueler) {
		final List<DTOKlassen> klassen = getDTOsBySchuljahresabschnittId(schuljahresabschnittId);
		return mapList(klassen, schuljahresabschnittId, attachSchueler);
	}

	/**
	 * Methode liefert eine Liste von {@link KlasseDetails} zu den angegebenen Klassen IDs. Die Klassen enthalten keine Schüler.
	 *
	 * @param ids IDs der abzufragenden Klassen
	 * @param schuljahresabschnittId ID des Referenz Schuljahresabschnittes
	 *
	 * @return Liste von KlasseDetails Objekten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<KlasseDetails> getListByIdsOhneSchueler(final List<Long> ids, final Long schuljahresabschnittId) {
		final List<DTOKlassen> klassenDTOs = getDTOsByIds(ids);
		return mapList(klassenDTOs, schuljahresabschnittId, false);
	}

	/**
	 * Gibt die Daten einer Klasse zu deren ID ohne Schülerliste zurück.
	 *
	 * @param id   Die ID der Klasse.
	 *
	 * @return die Daten der KLasse zur ID ohne Schülerliste.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public KlasseDetails getByIdOhneSchueler(final Long id) {
		final DTOKlassen klasseDto = getDTO(id);
		return mapToDetails(klasseDto, false);
	}


	@Override
	protected long getLongId(final DTOKlassen klasse) {
		return klasse.ID;
	}


	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOKlassen> dtosKlassen, final Map<Long, SimpleOperationResponse> mapResponses) {
		for (final @NotNull DTOKlassen dtoKlasse : dtosKlassen) {
			final SimpleOperationResponse operationResponse = mapResponses.get(dtoKlasse.ID);
			// Die Klasse darf keine Schüler beinhalten. Dies kann an zugeordneten Lernabschnittsdaten geprüft werden...
			final List<Long> idsSchueler =
					conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_KLASSEN_ID, DTOSchuelerLernabschnittsdaten.class, dtoKlasse.ID).stream()
							.filter(sla -> sla.WechselNr == 0)
							.map(sla -> sla.Schueler_ID)
							.distinct()
							.toList();
			// ... allerdings sollten zuvor die gelöschten Schüler gefiltert werden, da diese auf die Lösch-Operation keinen Einfluss haben sollten
			final List<Long> schuelerIdsGeloescht = conn.queryByKeyList(DTOSchueler.class, idsSchueler).stream()
					.filter(s -> s.Geloescht)
					.map(s -> s.ID)
					.toList();
			// ... und dann darf die Klasse gelöscht werden, wenn keine nicht gelöschten Schüler der Klasse zugeordnet sind...
			if (idsSchueler.size() > schuelerIdsGeloescht.size()) {
				operationResponse.success = false;
				operationResponse.log.add("Klasse %s (ID: %d) hat noch %d verknüpfte(n) Schüler."
						.formatted(dtoKlasse.Klasse, dtoKlasse.ID, idsSchueler.size()));
			}
		}
	}


	/**
	 * Die Methode stellt für die Klassen des angegebenen Schuljahresabschnittes eine Defaultsortierung her, in dem es Default-Werte in das
	 * Feld Sortierung.
	 *
	 * @param schuljahresabschnittId   die ID des Schuljahresabschnitts
	 *
	 * @return die HTTP-Response
	 */
	public Response setDefaultSortierung(final long schuljahresabschnittId) {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		if (jahrgaenge.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Jahrgänge, für das Ermitteln der Default Sortierung, gefunden.");
		}

		final Map<Long, DTOJahrgang> mapJahrgaenge = jahrgaenge.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
		final List<DTOKlassen> klassen = getDTOsBySchuljahresabschnittId(schuljahresabschnittId);
		conn.transactionFlush();
		if (klassen.isEmpty()) {
			return Response.noContent().type(MediaType.APPLICATION_JSON).build();
		}

		// Klassen Liste Default sortieren
		klassen.sort(getComparatorKlassenByJahrgaengeDefault(mapJahrgaenge));

		// Default Sortierung für jede Klasse setzen
		for (int sortIndex = 0; sortIndex < klassen.size(); sortIndex++) {
			klassen.get(sortIndex).Sortierung = sortIndex + 1;
		}
		conn.transactionPersistAll(klassen);

		return Response.noContent().type(MediaType.APPLICATION_JSON).build();
	}

	private static Comparator<DTOKlassen> getComparatorKlassenByJahrgaengeDefault(final Map<Long, DTOJahrgang> mapJahrgaenge) {
		return (final DTOKlassen a, final DTOKlassen b) -> {
			final DTOJahrgang jgA = mapJahrgaenge.get(a.Jahrgang_ID);
			final DTOJahrgang jgB = mapJahrgaenge.get(b.Jahrgang_ID);

			final Integer x = compareJahrgangSortierung(jgA, jgB);
			if (x != null) {
				return x;
			}

			final String parA = ((a.ASDKlasse == null) || (a.ASDKlasse.length() < 3)) ? "" : a.ASDKlasse.substring(2);
			final String parB = ((b.ASDKlasse == null) || (b.ASDKlasse.length() < 3)) ? "" : b.ASDKlasse.substring(2);
			if (parA.length() != parB.length()) {
				return parA.length() - parB.length();
			}

			return parA.compareToIgnoreCase(parB);
		};
	}

	@Nullable
	private static Integer compareJahrgangSortierung(final DTOJahrgang jgA, final DTOJahrgang jgB) {
		if (((jgA == null) || (jgA.Sortierung == null)) && ((jgB == null) || (jgB.Sortierung == null))) {
			return 0;
		}
		if ((jgA == null) || (jgA.Sortierung == null)) {
			return 1;
		}
		if ((jgB == null) || (jgB.Sortierung == null)) {
			return -1;
		}
		if (!Objects.equals(jgA.Sortierung, jgB.Sortierung)) {
			return jgA.Sortierung - jgB.Sortierung;
		}
		return null;
	}

	@Override
	protected void initDTO(final DTOKlassen dtoKlassen, final Long newId, final Map<String, Object> initAttributes) {
		// Wenn ein Schuljahresabschnitt mitgeliefert wurde, wird dieser hinterlegt, ansonsten wird default der aktuelle Schuljahresabschnitt der Schule hinterlegt
		final DTOTeilstandorte teilstandort = getDTOTeilstandort();

		final Schulform schulform = conn.getUser().schuleGetSchulform();
		final long idSchuljahresabschnitt = JSONMapper.convertToLong(initAttributes.get(ID_SCHULJAHRESABSCHNITT), false);
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Eine Klasse kann nur mit einem gültigen Schuljahresabschnitt angelegt werden. Die ID %d ist ungültig.".formatted(idSchuljahresabschnitt));
		}

		dtoKlassen.ID = newId;
		dtoKlassen.Sortierung = 0;
		dtoKlassen.AdrMerkmal = teilstandort.AdrMerkmal;
		OrganisationsformKatalogEintrag orgformEintrag = null;
		if (schulform.istAllgemeinbildend()) {
			orgformEintrag = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(schuljahresabschnitt.schuljahr);
		} else if (schulform.istBerufsbildend()) {
			orgformEintrag = BerufskollegOrganisationsformen.VOLLZEIT.daten(schuljahresabschnitt.schuljahr);
		} else if (schulform.istWeiterbildung()) {
			orgformEintrag = WeiterbildungskollegOrganisationsformen.VOLLZEIT.daten(schuljahresabschnitt.schuljahr);
		}

		if (orgformEintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Anlegen des Default-Wertes für die Organisationsform.");
		}
		dtoKlassen.OrgFormKrz = orgformEintrag.kuerzel;

		Schulgliederung schulgliederung = Schulgliederung.getDefault(schulform);
		if (schulgliederung == null) {
			schulgliederung = Schulgliederung.getBySchuljahrAndSchulform(schuljahresabschnitt.schuljahr, schulform).getFirst();
		}

		final SchulgliederungKatalogEintrag schulgliederungEintrag = schulgliederung.daten(schuljahresabschnitt.schuljahr);
		if (schulgliederungEintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Anlegen des Default-Wertes für die Schulgliederung.");
		}
		dtoKlassen.ASDSchulformNr = schulgliederungEintrag.kuerzel;

		final KlassenartKatalogEintrag klassenartEintrag = Klassenart.getDefault(schulform).daten(schuljahresabschnitt.schuljahr);
		if (klassenartEintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Anlegen des Default-Wertes für die Klassenart.");
		}
		dtoKlassen.Klassenart = klassenartEintrag.kuerzel;
	}

	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) {
		final Long idSchuljahresabschnitt = JSONMapper.convertToLong(initAttributes.get(ID_SCHULJAHRESABSCHNITT), false);
		final String kuerzel = JSONMapper.convertToString(initAttributes.get(KUERZEL), false, false, Schema.tab_Klassen.col_Klasse.datenlaenge());
		if (checkKuerzelExists(conn, idSchuljahresabschnitt, kuerzel)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Klasse %s existiert bereits im Schuljahresabschnitt %d".formatted(kuerzel, idSchuljahresabschnitt));
		}
	}

	@Override
	protected void mapAttribute(final DTOKlassen dto, final String name, final Object value, final Map<String, Object> map) {
		switch (name) {
			case KUERZEL -> mapKuerzel(dto, value);
			case ID_SCHULJAHRESABSCHNITT -> dto.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false);
			case "idJahrgang" -> mapJahrgang(dto, value);
			case "parallelitaet" -> mapParallelitaet(dto, value);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToIntegerInRange(value, false, 0, Integer.MAX_VALUE);
			case "teilstandort" -> mapTeilstandort(dto, value);
			case "beschreibung" -> dto.Bezeichnung = JSONMapper.convertToString(value, true, true, Schema.tab_Klassen.col_Bezeichnung.datenlaenge());
			case "idVorgaengerklasse" -> mapVorgaengerKlasse(dto, value);
			case "idFolgeklasse" -> mapFolgeKlasse(dto, value);
			case "idAllgemeinbildendOrganisationsform" -> mapAllgemeinbildendOrganisationsform(dto, value);
			case "idBerufsbildendOrganisationsform" -> mapBerufsbildendOrganisationsform(dto, value);
			case "idWeiterbildungOrganisationsform" -> mapWeiterbildungOrganisationsform(dto, value);
			case "idSchulgliederung" -> mapIdSchulgliederung(dto, value);
			case "idKlassenart" -> mapIdKlassenart(dto, value);
			case "noteneingabeGesperrt" -> dto.NotenGesperrt = JSONMapper.convertToBoolean(value, false);
			case "verwendungAnkreuzkompetenzen" -> dto.Ankreuzzeugnisse = JSONMapper.convertToBoolean(value, false);
			case "idFachklasse" -> mapIdFachklasse(dto, value);
			case "beginnSommersemester" -> dto.SommerSem = JSONMapper.convertToBoolean(value, false);
			case "klassenLeitungen" -> mapKlassenleitungen(dto, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	private void mapKlassenleitungen(final DTOKlassen dto, final Object value) {
		// Deserialisiere Klassenleitungsliste und filtere doppelte IDs
		final List<Long> idsNewKlassenleitungen = JSONMapper.convertToListOfLong(value, false).stream().distinct().toList();

		// Bestimme alle hinzuzufügenden Lehrkräfte und prüfe, ob alle IDs auch zu einer Lehrkraft gehören
		if ((!idsNewKlassenleitungen.isEmpty()) && (conn.queryByKeyList(DTOLehrer.class, idsNewKlassenleitungen).size() != idsNewKlassenleitungen.size())) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Mindestens zu einer der angegebenen Lehrer-IDs ist keine Lehrkraft bekannt");
		}

		// Entferne alle Klassenleitungen aus der DB, die nicht in der neuen Liste der Klassenleitungen sind
		final List<DTOKlassenLeitung> persistedKlassenleitungen = conn.queryList(DTOKlassenLeitung.QUERY_BY_KLASSEN_ID, DTOKlassenLeitung.class, dto.ID);
		final List<DTOKlassenLeitung> klassenleitungenToRemove = persistedKlassenleitungen.stream()
				.filter(klassenleitung -> !idsNewKlassenleitungen.contains(klassenleitung.Lehrer_ID))
				.toList();
		removeKlassenleitungen(klassenleitungenToRemove);

		// Erstelle bzw. aktualisiere dann die Klassenleitungen, welche in der neuen Liste der Klassenleitungen sind
		final List<DTOKlassenLeitung> klassenleitungenToPersist = new ArrayList<>();
		for (int index = 0; index < idsNewKlassenleitungen.size(); index++) {
			final int reihenfolge = index + 1;  // Der Wert für die Reihenfolge ist 1-indiziert

			// Überprüfe, ob es bereits eine Klassenleitung mit der jeweiligen Klassen-ID und Lehrer-ID gibt
			final List<DTOKlassenLeitung> persistedKlassenleitung =
					conn.queryList(DTOKlassenLeitung.QUERY_PK, DTOKlassenLeitung.class, dto.ID, idsNewKlassenleitungen.get(index));

			// Prüfe, ob die Klassenleitung gefunden wurde...
			final DTOKlassenLeitung dtoKlassenLeitung;
			if (!persistedKlassenleitung.isEmpty()) {
				// ... und aktualisiere diese bei Bedarf
				dtoKlassenLeitung = persistedKlassenleitung.getFirst();
				if (dtoKlassenLeitung.Reihenfolge == reihenfolge) {
					continue; // bereits aktuell
				}
				// Setze den Reihenfolgenwert anhand des Index in der neuen Liste und persistiere die Änderung
				dtoKlassenLeitung.Reihenfolge = reihenfolge;
			} else {
				dtoKlassenLeitung = new DTOKlassenLeitung(dto.ID, idsNewKlassenleitungen.get(index), reihenfolge);
			}

			klassenleitungenToPersist.add(dtoKlassenLeitung);
		}

		persistKlassenleitungen(klassenleitungenToPersist);
	}

	private void persistKlassenleitungen(final List<DTOKlassenLeitung> klassenleitungenToPersist) {
		if (!this.conn.persistAll(klassenleitungenToPersist)) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Klassenleitung konnten nicht persistiert werden.");
		}
		conn.transactionFlush();
	}

	private void removeKlassenleitungen(final List<DTOKlassenLeitung> klassenleitungenToRemove) {
		if (!conn.transactionRemoveAll(klassenleitungenToRemove)) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Klassenleitungen konnten nicht gelöscht werden.");
		}
		conn.transactionFlush();
	}

	private void mapJahrgang(final DTOKlassen dto, final Object value) {
		final Long idJahrgang = JSONMapper.convertToLong(value, true);
		if (idJahrgang == null) {
			// Jahrgangs-übergreifende Klasse -> JU
			dto.Jahrgang_ID = null;
			dto.ASDKlasse = "JU" + (((dto.ASDKlasse != null) && (dto.ASDKlasse.length() > 2)) ? dto.ASDKlasse.charAt(2) : "");
		} else {
			final DTOJahrgang jg = conn.queryByKey(DTOJahrgang.class, idJahrgang);
			if (jg == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "Der Jahrgang mit der ID %d konnte nicht gefunden werden.".formatted(idJahrgang));
			}
			dto.Jahrgang_ID = jg.ID;
			String asdKlassenjahrgang = jg.ASDJahrgang;
			if ("E1".equals(jg.ASDJahrgang)) {
				asdKlassenjahrgang = "1E";
			} else if ("E2".equals(jg.ASDJahrgang)) {
				asdKlassenjahrgang = "2E";
			}
			dto.ASDKlasse = asdKlassenjahrgang + (((dto.ASDKlasse != null) && (dto.ASDKlasse.length() > 2)) ? dto.ASDKlasse.charAt(2) : "");
		}
	}

	private static void mapParallelitaet(final DTOKlassen dto, final Object value) {
		final String parallelitaet = JSONMapper.convertToString(value, true, false, 1);
		if (parallelitaet == null) {
			dto.ASDKlasse = dto.ASDKlasse.substring(0, 2);
		} else {
			final char firstCharacter = parallelitaet.charAt(0);
			if (!Character.isUpperCase(firstCharacter)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Parallelität muss durch einen Buchstaben A-Z in Großschreibung angegeben werden.");
			}
			dto.ASDKlasse = dto.ASDKlasse.substring(0, 2) + firstCharacter;
		}
	}

	private void mapVorgaengerKlasse(final DTOKlassen dto, final Object value) {
		final Long idVorgaengerklasse = JSONMapper.convertToLong(value, true);
		if (idVorgaengerklasse == null) {
			dto.VKlasse = null;
		} else {
			final DTOKlassen vk = conn.queryByKey(DTOKlassen.class, idVorgaengerklasse);
			if (vk == null) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Die Vorgängerklasse mit der ID %d wurde nicht gefunden.".formatted(idVorgaengerklasse));
			}
			final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
			if (schuljahresabschnitt == null) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Die ID des Schuljahresabschnitts %d der Klasse mit der ID %d ist ungültig.".formatted(dto.Schuljahresabschnitts_ID, dto.ID));
			}
			if (vk.Schuljahresabschnitts_ID != schuljahresabschnitt.VorigerAbschnitt_ID) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die ID für die Vorgängerklasse gehört nicht zu einer Klasse aus dem vorigen Schuljahresabschnitt.");
			}
			dto.VKlasse = vk.Klasse;
		}
	}

	private void mapFolgeKlasse(final DTOKlassen dto, final Object value) {
		final Long idFolgeklasse = JSONMapper.convertToLong(value, true);
		if (idFolgeklasse == null) {
			dto.FKlasse = null;
		} else {
			final DTOKlassen fk = conn.queryByKey(DTOKlassen.class, idFolgeklasse);
			if (fk == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "Die Folgeklasse mit der ID %d wurde nicht gefunden.".formatted(idFolgeklasse));
			}
			final DTOSchuljahresabschnitte a = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
			if (a == null) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Die ID des Schuljahresabschnitts %d der Klasse mit der ID %d ist ungültig.".formatted(dto.Schuljahresabschnitts_ID, dto.ID));
			}
			if (fk.Schuljahresabschnitts_ID != a.FolgeAbschnitt_ID) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die ID für die Folgeklasse gehört nicht zu einer Klasse aus dem nachfolgenden Schuljahresabschnitt.");
			}
			dto.FKlasse = fk.Klasse;
		}
	}

	private void mapAllgemeinbildendOrganisationsform(final DTOKlassen dto, final Object value) {
		final boolean istKeineAllgemeinbildendeSchulform = !conn.getUser().schuleGetSchulform().istAllgemeinbildend();
		if (istKeineAllgemeinbildendeSchulform) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine allgemeinbildende Schulform hat.");
		}
		final Long idOrganisationsform = JSONMapper.convertToLong(value, true);
		final AllgemeinbildendOrganisationsformen organisationsform = (idOrganisationsform == null)
				? AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET
				: AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(idOrganisationsform);
		if (organisationsform == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die allgemeinene Organisationform ist ungültig");
		}
		final Schuljahresabschnitt abschnitt = getSchuljahresabschnittOrException(dto.Schuljahresabschnitts_ID);
		final OrganisationsformKatalogEintrag organisationsformEintrag = organisationsform.daten(abschnitt.schuljahr);
		if (organisationsformEintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d für die allgemeinene Organisationform ist für das Schuljahr %d der Klasse ungültig".formatted(idOrganisationsform,
							abschnitt.schuljahr));
		}
		dto.OrgFormKrz = organisationsformEintrag.kuerzel;
	}

	private Schuljahresabschnitt getSchuljahresabschnittOrException(final Long idSchuljahresabschnitt) {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (abschnitt == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Keinen Schuljahresabschnitt für die ID %d gefunden.".formatted(idSchuljahresabschnitt));
		}
		return abschnitt;
	}

	private void mapBerufsbildendOrganisationsform(final DTOKlassen dto, final Object value) {
		final Schuljahresabschnitt abschnitt = getSchuljahresabschnittOrException(dto.Schuljahresabschnitts_ID);
		if (!conn.getUser().schuleGetSchulform().istBerufsbildend()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine berufsbildende Schulform hat.");
		}
		final Long idOrganisationsform = JSONMapper.convertToLong(value, true);
		if (idOrganisationsform == null) {
			final OrganisationsformKatalogEintrag oke = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(abschnitt.schuljahr);
			if (oke == null) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die allgemeinene Organisationform NICHT_ZUGEORDNET ist für das Schuljahr %d der Klasse ungültig".formatted(abschnitt.schuljahr));
			}
			dto.OrgFormKrz = oke.kuerzel;
		} else {
			final BerufskollegOrganisationsformen orgform = BerufskollegOrganisationsformen.data().getWertByIDOrNull(idOrganisationsform);
			if (orgform == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die berufsbildende Organisationform ist ungültig");
			}
			final OrganisationsformKatalogEintrag oke = orgform.daten(abschnitt.schuljahr);
			if (oke == null) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die ID %d für die berufsbildende Organisationform ist für das Schuljahr %d der Klasse ungültig".formatted(idOrganisationsform,
								abschnitt.schuljahr));
			}
			dto.OrgFormKrz = oke.kuerzel;
		}
	}

	private void mapWeiterbildungOrganisationsform(final DTOKlassen dto, final Object value) {
		final Schuljahresabschnitt abschnitt = getSchuljahresabschnittOrException(dto.Schuljahresabschnitts_ID);
		if (!conn.getUser().schuleGetSchulform().istWeiterbildung()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine Schulform für die Weiterbildung hat.");
		}
		final Long idOrganisationsform = JSONMapper.convertToLong(value, true);
		if (idOrganisationsform == null) {
			final OrganisationsformKatalogEintrag organisationsformEintrag = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(abschnitt.schuljahr);
			if (organisationsformEintrag == null) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die allgemeinene Organisationform NICHT_ZUGEORDNET ist für das Schuljahr %d der Klasse ungültig".formatted(abschnitt.schuljahr));
			}
			dto.OrgFormKrz = organisationsformEintrag.kuerzel;
		} else {
			final WeiterbildungskollegOrganisationsformen organisationsform =
					WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(idOrganisationsform);
			if (organisationsform == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die Organisationform am Weiterbildungskolleg ist ungültig");
			}

			final OrganisationsformKatalogEintrag organisationsformEintrag = organisationsform.daten(abschnitt.schuljahr);
			if (organisationsformEintrag == null) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die ID %d für die Organisationform am Weiterbildungskolleg ist für das Schuljahr %d der Klasse ungültig"
								.formatted(idOrganisationsform, abschnitt.schuljahr));
			}
			dto.OrgFormKrz = organisationsformEintrag.kuerzel;
		}
	}

	private void mapKuerzel(final DTOKlassen dto, final Object value) {
		final String kuerzel = JSONMapper.convertToString(value, false, false, Schema.tab_Klassen.col_Klasse.datenlaenge());
		if (checkKuerzelExists(conn, dto.Schuljahresabschnitts_ID, kuerzel)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Klasse %s existiert bereits im Schuljahresabschnitt %d"
					.formatted(kuerzel, dto.Schuljahresabschnitts_ID));
		}
		dto.Klasse = kuerzel;
	}

	private static void mapIdFachklasse(final DTOKlassen dto, final Object value) {
		final Long idFachklasse = JSONMapper.convertToLong(value, true);
		if (idFachklasse != null) {
			final BerufskollegFachklassenKatalogDaten fachklasse = JsonDaten.fachklassenManager.getDatenByID(idFachklasse);
			if (fachklasse == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Es konnte keine Fachklasse für die ID %d gefunden werden.".formatted(idFachklasse));
			}
			dto.Fachklasse_ID = fachklasse.id;
		} else {
			dto.Fachklasse_ID = null;
		}
	}

	private void mapIdKlassenart(final DTOKlassen dto, final Object value) {
		final Long idKlassenart = JSONMapper.convertToLong(value, true);
		final Klassenart klassenart = Klassenart.data().getWertByIDOrNull(idKlassenart);
		if (klassenart == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es konnte keine Klassenart für die ID %d gefunden werden.".formatted(idKlassenart));
		}
		final int schuljahr = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID).schuljahr;
		final KlassenartKatalogEintrag eintrag = klassenart.daten(schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Klassenart mit der ID %d ist für das Schuljahr %d ungültig.".formatted(idKlassenart, schuljahr));
		}
		dto.Klassenart = eintrag.kuerzel;
	}

	private void mapIdSchulgliederung(final DTOKlassen dto, final Object value) {
		final Schulform schulform = conn.getUser().schuleGetSchulform();
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final Long idSchulgliederung = JSONMapper.convertToLong(value, true);
		if (((idSchulgliederung == null) || (idSchulgliederung == -1)) && (Schulgliederung.getDefault(schulform) == null)) {
			dto.ASDSchulformNr = null;
			return;
		}

		final Schulgliederung schulgliederung = ((idSchulgliederung == null) || (idSchulgliederung == -1))
				? Schulgliederung.getDefault(schulform)
				: Schulgliederung.data().getWertByID(idSchulgliederung);
		if (!schulgliederung.hatSchulform(abschnitt.schuljahr, schulform)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulgliederung wird von der angegeben Schulform nicht unterstützt.");
		}

		dto.ASDSchulformNr = schulgliederung.daten(abschnitt.schuljahr).kuerzel;
	}

	private void mapTeilstandort(final DTOKlassen dto, final Object value) {
		final String teilstandortStr = JSONMapper.convertToString(value, false, false, 1);
		final DTOTeilstandorte teilstandort = conn.queryByKey(DTOTeilstandorte.class, teilstandortStr);
		if (teilstandort == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Teilstandort %s wurde nicht gefunden.".formatted(teilstandortStr));
		}

		dto.AdrMerkmal = teilstandortStr;
	}


	/**
	 * Bestimmt zu den übergebenen Klassen-IDs die jeweils zugehörigen Klassenlehrer aus der Datenbank und gib eine
	 * Map mit der Zuordnung zurück.
	 *
	 * @param conn         die aktuelle Datenbank-Verbindung
	 * @param idsKlassen   die IDs der Klassen
	 *
	 * @return die Zuordnung der Klassenlehrer zu den Klassen-IDs
	 */
	public static Map<Long, List<DTOLehrer>> getDTOMapKlassenlehrerByKlassenID(final @NotNull DBEntityManager conn, final @NotNull List<Long> idsKlassen) {
		if (idsKlassen.isEmpty()) {
			return new HashMap<>();
		}
		final List<DTOKlassenLeitung> klassenleitungen = conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, idsKlassen);
		if (klassenleitungen.isEmpty()) {
			return new HashMap<>();
		}
		final List<Long> idsLehrer = klassenleitungen.stream().map(kl -> kl.Lehrer_ID).distinct().toList();
		if (idsLehrer.isEmpty()) {
			return new HashMap<>();
		}

		final Map<Long, DTOLehrer> mapLehrerById = conn.queryByKeyList(DTOLehrer.class, idsLehrer).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		final List<DTOKlassenLeitung> klassenleitungenSorted = klassenleitungen.stream()
				.sorted((kll1, kll2) -> {
					final int compareIndex = Long.compare(kll1.Klassen_ID, kll2.Klassen_ID);
					return (compareIndex != 0) ? compareIndex : Integer.compare(kll1.Reihenfolge, kll2.Reihenfolge);
				})
				.toList();

		final Map<Long, List<DTOLehrer>> mapKlassenlehrerByKlassenId = new HashMap<>();
		for (final DTOKlassenLeitung klassenleitung : klassenleitungenSorted) {
			final DTOLehrer lehrer = mapLehrerById.get(klassenleitung.Lehrer_ID);
			if (lehrer != null) {
				mapKlassenlehrerByKlassenId.computeIfAbsent(klassenleitung.Klassen_ID, l -> new ArrayList<>()).add(lehrer);
			}
		}
		return mapKlassenlehrerByKlassenId;
	}

	/**
	 * Die Methode ermittelt eine Liste von {@link DTOKlassen} Objekten zu den angegebenen Klassen IDs.
	 *
	 * @param klassenIds ID der Klasse
	 *
	 * @return Ein {@link DTOKlassen} Objekt.
	 */
	public List<DTOKlassen> getDTOsByIds(final List<Long> klassenIds) {
		if (klassenIds == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die IDs für die Klassen dürfen nicht null sein.");
		}

		final List<DTOKlassen> klassenDtos = conn.queryList(DTOKlassen.QUERY_LIST_BY_ID, DTOKlassen.class, klassenIds);
		if (klassenDtos.size() != klassenIds.size()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden nicht alle Klassen zu den IDs gefunden.");
		}

		return klassenDtos;
	}

	/**
	 * Die Methode ermittelt eine Liste von {@link DTOKlassen} Objekten zu der angegebenen Schuljahresabschnitt ID.
	 *
	 * @param schuljahresabschnittId ID des Schuljahresabschnittes
	 *
	 * @return Liste von Klassen zu einem Schuljahresabschnitt
	 */
	public List<DTOKlassen> getDTOsBySchuljahresabschnittId(final Long schuljahresabschnittId) {
		if (schuljahresabschnittId == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Schuljahresabschnitt darf nicht null sein.");
		}
		return conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnittId);
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOKlassen} Objekt zur angegebenen Klassen ID.
	 *
	 * @param id ID der Klasse
	 *
	 * @return Ein {@link DTOKlassen} Objekt.
	 */
	public DTOKlassen getDTO(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Klasse darf nicht null sein.");
		}

		final DTOKlassen klasseDto = conn.queryByKey(DTOKlassen.class, id);
		if (klasseDto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klasse zur ID " + id + " gefunden.");
		}

		return klasseDto;
	}

	/**
	 * Die Methode ermittelt die entsprechende Schüler IDs zur angegebenen Klassen ID.
	 *
	 * @param klassenId   die ID der Klasse
	 *
	 * @return die List von Schüler IDs, welche der Klasse zugeordnet sind
	 */
	List<Long> getSchuelerIDsByKlassenID(final Long klassenId) {
		return conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_KLASSEN_ID, DTOSchuelerLernabschnittsdaten.class, klassenId).stream()
				.filter(sla -> sla.WechselNr == 0).map(sla -> sla.Schueler_ID).distinct().toList();
	}

	/**
	 * Gibt eine Liste mit nicht gelöschten {@link DTOSchueler} Objekten zu einer Klasse zurück.
	 *
	 * @param klassenId   die ID der Klasse
	 *
	 * @return die List von Schülern, welche der Klasse zugeordnet sind
	 */
	private List<DTOSchueler> getSchuelerDtosNichtGeloeschtByKlassenID(final Long klassenId) {
		final List<Long> schuelerIDs = getSchuelerIDsByKlassenID(klassenId);
		return conn.queryByKeyList(DTOSchueler.class, schuelerIDs).stream()
				.filter(schueler -> Boolean.FALSE.equals(schueler.Geloescht))
				.toList();
	}

	/**
	 * Wandelt ein DTOKlassen Objekt in ein KlasseDetails Objekt um.
	 *
	 * @param dto   DTOKlassen Objekt
	 *
	 * @return das neu erstellte KlasseDetails Objekt
	 */
	@Override
	protected KlasseDetails map(final DTOKlassen dto) {
		return mapToDetails(dto, true);
	}

	/**
	 * Methode liefert eine Liste von {@link KlasseDetails} zu einem Schuljahresabschnitt zurück.
	 *
	 * @param dtos zu mappende DTOs
	 * @param schuljahresabschnittId ID des Schuljahresabschnitts
	 * @param attachSchueler gibt an, ob die Schueler zu den Klassen geladen werden sollen
	 *
	 * @return Liste von KlasseDetails
	 */
	private List<KlasseDetails> mapList(final List<DTOKlassen> dtos, final Long schuljahresabschnittId, final boolean attachSchueler) {
		// Bestimme die Information zum Schuljahresabschnitt
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(schuljahresabschnittId);
		final Map<String, DTOKlassen> klassenVorher = getKlassenBySchuljahresabschnittId(schuljahresabschnitt.idVorigerAbschnitt);
		final Map<String, DTOKlassen> klassenNachher = getKlassenBySchuljahresabschnittId(schuljahresabschnitt.idFolgeAbschnitt);

		final List<KlasseDetails> klasseDetailsList = new ArrayList<>();
		for (final DTOKlassen dto : dtos) {
			klasseDetailsList.add(mapInternal(dto, schuljahresabschnitt, klassenVorher, klassenNachher, attachSchueler));
		}

		return klasseDetailsList;
	}

	private KlasseDetails mapToDetails(final DTOKlassen dto, final boolean attachSchueler) {
		// Bestimme die Informationen zur Schule und zu den Schuljahresabschnitten
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(dto.Schuljahresabschnitts_ID);

		final Map<String, DTOKlassen> klassenVorher = getKlassenBySchuljahresabschnittId(schuljahresabschnitt.idVorigerAbschnitt);
		final Map<String, DTOKlassen> klassenNachher = getKlassenBySchuljahresabschnittId(schuljahresabschnitt.idFolgeAbschnitt);

		return mapInternal(dto, schuljahresabschnitt, klassenVorher, klassenNachher, attachSchueler);
	}

	private KlasseListItem mapToListItem(final DTOKlassen klasse) {
		final var klasseListeEintrag = new KlasseListItem();
		klasseListeEintrag.id = klasse.ID;
		klasseListeEintrag.idSchuljahresabschnitt = klasse.Schuljahresabschnitts_ID;
		klasseListeEintrag.idJahrgang = klasse.Jahrgang_ID;
		klasseListeEintrag.kuerzel = klasse.Klasse;
		klasseListeEintrag.beschreibung = klasse.Bezeichnung;
		klasseListeEintrag.parallelitaet = getParallelitaet(klasse);
		return klasseListeEintrag;
	}

	private static String getParallelitaet(final DTOKlassen klasse) {
		if ((klasse.ASDKlasse == null) || (klasse.ASDKlasse.length() < 3)) {
			return null;
		}
		return klasse.ASDKlasse.substring(2);
	}

	private KlasseDetails mapInternal(final DTOKlassen dto, final Schuljahresabschnitt schuljahresabschnitt,
			final Map<String, DTOKlassen> klassenVorher, final Map<String, DTOKlassen> klassenNachher, final boolean attachSchueler) {
		final Schulform schulform = conn.getUser().schuleGetSchulform();
		final KlasseDetails klasseDetails = new KlasseDetails();

		final List<DTOKlassenLeitung> klassenLeitungen = conn.queryList(DTOKlassenLeitung.QUERY_BY_KLASSEN_ID + " ORDER BY e.Reihenfolge",
				DTOKlassenLeitung.class, dto.ID);
		klasseDetails.klassenLeitungen.addAll(klassenLeitungen.stream().map(kl -> kl.Lehrer_ID).toList());

		klasseDetails.id = dto.ID;
		klasseDetails.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		klasseDetails.kuerzel = dto.Klasse;
		klasseDetails.idSchulgliederung = getSchulgliederungIdByKlasseAndSchulform(dto, schulform);
		klasseDetails.idJahrgang = dto.Jahrgang_ID;
		klasseDetails.parallelitaet = getParallelitaet(dto);
		klasseDetails.sortierung = dto.Sortierung;
		klasseDetails.teilstandort = Objects.toString(dto.AdrMerkmal, "");
		klasseDetails.beschreibung = Objects.toString(dto.Bezeichnung, "");

		klasseDetails.idAllgemeinbildendOrganisationsform = (AllgemeinbildendOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz) == null)
				? null : AllgemeinbildendOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz).daten(schuljahresabschnitt.schuljahr).id;
		klasseDetails.idBerufsbildendOrganisationsform = (BerufskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz) == null)
				? null : BerufskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz).daten(schuljahresabschnitt.schuljahr).id;
		klasseDetails.idWeiterbildungOrganisationsform = (WeiterbildungskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz) == null)
				? null : WeiterbildungskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz).daten(schuljahresabschnitt.schuljahr).id;
		klasseDetails.pruefungsordnung = dto.PruefOrdnung;

		final KlassenartKatalogEintrag klassenart = getKlassenart(dto, schuljahresabschnitt, schulform);
		klasseDetails.idKlassenart = klassenart.id;
		klasseDetails.noteneingabeGesperrt = (dto.NotenGesperrt != null) && dto.NotenGesperrt;
		klasseDetails.verwendungAnkreuzkompetenzen = (dto.Ankreuzzeugnisse != null) && dto.Ankreuzzeugnisse;
		klasseDetails.kuerzelVorgaengerklasse = dto.VKlasse;
		klasseDetails.kuerzelFolgeklasse = dto.FKlasse;
		klasseDetails.idFachklasse = dto.Fachklasse_ID;
		klasseDetails.beginnSommersemester = Boolean.TRUE.equals(dto.SommerSem);

		// Bestimme die IDs der Vorgänger- und der Nachfolge-Klassen dieser Klasse, sofern möglich und berücksichtige dabei den Semesterbetrieb i, Weiterbildungskolleg
		if (klasseDetails.kuerzelVorgaengerklasse != null) {
			final String kuerzelVorgaenger = ((schulform != Schulform.WB) && (schuljahresabschnitt.abschnitt == 2))
					? klasseDetails.kuerzel : klasseDetails.kuerzelVorgaengerklasse;
			klasseDetails.idVorgaengerklasse = Optional.ofNullable(klassenVorher.get(kuerzelVorgaenger)).map(e -> e.ID).orElse(null);
			klasseDetails.kuerzelVorgaengerklasse = kuerzelVorgaenger;
		}

		if (klasseDetails.kuerzelFolgeklasse != null) {
			final String kuerzelNachfolger = ((schulform != Schulform.WB) && (schuljahresabschnitt.abschnitt == 1))
					? klasseDetails.kuerzel : klasseDetails.kuerzelFolgeklasse;
			klasseDetails.idFolgeklasse = Optional.ofNullable(klassenNachher.get(kuerzelNachfolger)).map(e -> e.ID).orElse(null);
			klasseDetails.kuerzelFolgeklasse = kuerzelNachfolger;
		}

		if (attachSchueler) {
			final List<Schueler> schuelerNichtGeloescht = getSchuelerDtosNichtGeloeschtByKlassenID(dto.ID).stream()
					.map(schuelerDto -> DataSchuelerliste.mapToSchueler(schuelerDto, null))
					.toList();
			klasseDetails.schueler.addAll(schuelerNichtGeloescht);
		}

		return klasseDetails;
	}

	private static KlassenartKatalogEintrag getKlassenart(final DTOKlassen dto, final Schuljahresabschnitt schuljahresabschnitt, final Schulform schulform) {
		final Klassenart klassenart = Klassenart.data().getWertByKuerzel(dto.Klassenart);
		KlassenartKatalogEintrag eintragKlassenart = ((klassenart != null) && klassenart.hatSchulform(schuljahresabschnitt.schuljahr, schulform))
				? klassenart.daten(schuljahresabschnitt.schuljahr) : null;
		if (eintragKlassenart == null) {
			eintragKlassenart = Klassenart.RK.daten(schuljahresabschnitt.schuljahr);
		}
		return eintragKlassenart;
	}

	Map<String, DTOKlassen> getKlassenBySchuljahresabschnittId(final Long schuljahresabschnittId) {
		if (schuljahresabschnittId == null) {
			return new HashMap<>();
		}

		return conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnittId).stream()
				.collect(Collectors.toMap(k -> k.Klasse, k -> k));
	}

	Long getSchulgliederungIdByKlasseAndSchulform(final DTOKlassen dto, final Schulform schulform) {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		Schulgliederung gliederung = Schulgliederung.getBySchuljahrAndSchulformAndSchluessel(abschnitt.schuljahr, schulform, dto.ASDSchulformNr);
		if (gliederung == null) {
			gliederung = Schulgliederung.getDefault(schulform);
		}
		return (gliederung != null) ? gliederung.daten(abschnitt.schuljahr).id : -1;
	}

	@NotNull DTOTeilstandorte getDTOTeilstandort() {
		final DTOTeilstandorte teilstandort = conn.querySingle(DTOTeilstandorte.class);
		if (teilstandort == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es ist kein Teilstandort definiert, es muss mindestens ein Teilstandort hinterlegt sein.");
		}
		return teilstandort;
	}


	/**
	 * Prüft, ob das übergebene Klassenkürzel bereits in dem angebenen Schuljahresabschnitt existiert.
	 *
	 * @param conn                     die aktuelle Datenbank-Verbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param kuerzel                  das Kürzel der Klasse
	 *
	 * @return true, wenn das Kürzel bereits existiert und ansonsten false
	 */
	public static boolean checkKuerzelExists(final DBEntityManager conn, final Long idSchuljahresabschnitt, final String kuerzel) {
		final List<DTOKlassen> klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klasse = ?2", DTOKlassen.class,
				idSchuljahresabschnitt, kuerzel);
		return !klassen.isEmpty();
	}

}
