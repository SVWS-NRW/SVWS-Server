package de.svws_nrw.data.klassen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.json.JsonDaten;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Data-Manager für Klassen {@link KlassenDaten}
 */
public final class DataKlassendaten extends DataManagerRevised<Long, DTOKlassen, KlassenDaten> {

	/**
	 * @param conn DBEntityManager
	 */
	public DataKlassendaten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("idSchuljahresabschnitt", "kuerzel", "idJahrgang");
		setAttributesNotPatchable("id", "idSchuljahresabschnitt", "kuerzelVorgaengerklasse", "kuerzelFolgeklasse", "pruefungsordnung");
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
	public KlassenDaten getById(final Long id) throws ApiOperationException {
		final DTOKlassen klasseDto = getDTO(id);
		return map(klasseDto, true);
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
	 * @param halbjahresabschnittId ID des Halbjahresabschnittes
	 *
	 * @return ein DTOKlasse Objekt
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOKlassen getDTOByKuerzelOrASDKuerzelAndHalbjahresabschnittId(final String kuerzel, final String asdKuerzel, final Long halbjahresabschnittId)
			throws ApiOperationException {
		if ((kuerzel == null) && (asdKuerzel == null))
			throw new ApiOperationException(Status.BAD_REQUEST, "Es muss mindestens ein Kürzel oder ASD-Kürzel angegeben sein. Das Beide Kürzel dürfen nicht "
					+ "null sein.");
		if (halbjahresabschnittId == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Es muss eine Halbjahresabschnitt ID angegeben sein. Die Halbjahresabschnitt ID darf nicht "
					+ "null sein.");

		List<DTOKlassen> klassen = new ArrayList<>();
		if (kuerzel != null)
			klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Klasse = ?1 AND e.Schuljahresabschnitts_ID = ?2",
					DTOKlassen.class, kuerzel, halbjahresabschnittId);

		if ((asdKuerzel != null) && klassen.isEmpty())
			klassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.ASDKlasse = ?1 AND e.Schuljahresabschnitts_ID = ?2", DTOKlassen.class,
					asdKuerzel, halbjahresabschnittId);

		if (klassen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurde keine Klasse mit dem Kürzel %s und der Halbjahresabschnitt ID %d gefunden.".formatted(kuerzel, halbjahresabschnittId));

		return klassen.getFirst();
	}

	/**
	 * Methode liefert eine Liste von {@link KlassenDaten} zur angegebenen SchuljahresabschnittID.
	 *
	 * @param schuljahresabschnittId ID des Schuljahresabschnittes
	 *
	 * @return Liste von KlassenDaten Objekten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response getListBySchuljahresabschnittIDAsResponse(final Long schuljahresabschnittId) throws ApiOperationException {
		final List<KlassenDaten> klassenDatenList = getListBySchuljahresabschnittID(schuljahresabschnittId, true);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(klassenDatenList).build();
	}

	/**
	 * Methode liefert eine Liste von {@link KlassenDaten} zur angegebenen SchuljahresabschnittID.
	 *
	 * @param schuljahresabschnittId ID des Schuljahresabschnittes
	 * @param attachSchueler gibt an, ob die Schüler zu den Klassen mit geladen werden sollen
	 *
	 * @return Liste von KlassenDaten Objekten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<KlassenDaten> getListBySchuljahresabschnittID(final Long schuljahresabschnittId, final boolean attachSchueler) throws ApiOperationException {
		final List<DTOKlassen> dtos = getDTOsBySchuljahresabschnittId(schuljahresabschnittId);
		return mapList(dtos, schuljahresabschnittId, attachSchueler);
	}

	/**
	 * Methode liefert eine Liste von {@link KlassenDaten} zu den angegebenen Klassen IDs. Die Klassen enthalten keine Schüler.
	 *
	 * @param ids IDs der abzufragenden Klassen
	 * @param schuljahresabschnittId ID des Referenz Schuljahresabschnittes
	 *
	 * @return Liste von KlassenDaten Objekten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<KlassenDaten> getListByIdsOhneSchueler(final List<Long> ids, final Long schuljahresabschnittId) throws ApiOperationException {
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
	public KlassenDaten getByIdOhneSchueler(final Long id) throws ApiOperationException {
		final DTOKlassen klasseDto = getDTO(id);
		return map(klasseDto, false);
	}

	/**
	 * Löscht mehrere Klassen und gibt das Ergebnis der Lösch-Operationen als Liste von {@link SimpleOperationResponse} zurück.
	 *
	 * @param ids   die IDs der zu löschenden Klassen
	 *
	 * @return die Response mit einer Liste von {@link SimpleOperationResponse} zu den angefragten Lösch-Operationen.
	 */
	@Override
	public Response deleteMultipleAsResponse(final List<Long> ids) {
		// Bestimme die Datenbank-DTOs der Klassen
		final List<DTOKlassen> klassen = this.conn.queryByKeyList(DTOKlassen.class, ids).stream().toList();

		// Prüfe ob das Löschen der Klassen erlaubt ist
		final Map<Long, SimpleOperationResponse> mapResponses = klassen.stream()
				.collect(Collectors.toMap(r -> r.ID, this::checkDeletePreConditions));

		// Lösche die Klassen und gib den Erfolg in der Response zurück
		for (final DTOKlassen klasse : klassen) {
			final SimpleOperationResponse operationResponse = mapResponses.get(klasse.ID);
			if (operationResponse == null)
				throw new DeveloperNotificationException("Das SimpleOperationResponse Objekt zu der ID %d existiert nicht.".formatted(klasse.ID));

			if (operationResponse.log.isEmpty())
				operationResponse.success = this.conn.transactionRemove(klasse);
		}

		return Response.ok().entity(mapResponses.values()).build();
	}

	/**
	 * Die Methode stellt für die Klassen des angegebenen Schuljahresabschnittes eine Defaultsortierung her, in dem es Default-Werte in das
	 * Feld Sortierung.
	 *
	 * @param schuljahresabschnittId   die ID des Schuljahresabschnitts
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response setDefaultSortierung(final long schuljahresabschnittId) throws ApiOperationException {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		if (jahrgaenge.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Jahrgänge, für das Ermitteln der Default Sortierung, gefunden.");

		final Map<Long, DTOJahrgang> mapJahrgaenge = jahrgaenge.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
		final List<DTOKlassen> klassen = getDTOsBySchuljahresabschnittId(schuljahresabschnittId);
		conn.transactionFlush();

		// Klassen Liste Default sortieren
		klassen.sort((final DTOKlassen a, final DTOKlassen b) -> {
			final DTOJahrgang jgA = mapJahrgaenge.get(a.Jahrgang_ID);
			final DTOJahrgang jgB = mapJahrgaenge.get(b.Jahrgang_ID);
			if (((jgA == null) || (jgA.Sortierung == null)) && ((jgB == null) || (jgB.Sortierung == null)))
				return 0;
			if ((jgA == null) || (jgA.Sortierung == null))
				return 1;
			if ((jgB == null) || (jgB.Sortierung == null))
				return -1;
			if (!Objects.equals(jgA.Sortierung, jgB.Sortierung))
				return jgA.Sortierung - jgB.Sortierung;
			final String parA = ((a.ASDKlasse == null) || (a.ASDKlasse.length() < 3)) ? "" : a.ASDKlasse.substring(2);
			final String parB = ((b.ASDKlasse == null) || (b.ASDKlasse.length() < 3)) ? "" : b.ASDKlasse.substring(2);
			if (parA.length() != parB.length())
				return parA.length() - parB.length();
			return parA.compareToIgnoreCase(parB);
		});

		// Default Sortierung für jede Klasse setzen
		for (int sortIndex = 0; sortIndex < klassen.size(); sortIndex++) {
			klassen.get(sortIndex).Sortierung = sortIndex + 1;
		}
		conn.transactionPersistAll(klassen);

		return Response.status(Status.NO_CONTENT).type(MediaType.APPLICATION_JSON).build();
	}

	@Override
	protected void initDTO(final DTOKlassen dtoKlassen, final Long newId) throws ApiOperationException {
		// Wenn ein Schuljahresabschnitt mitgeliefert wurde, wird dieser hinterlegt, ansonsten wird default der aktuelle Schuljahresabschnitt der Schule hinterlegt
		final DTOTeilstandorte teilstandort = getDTOTeilstandort();

		final Schulform schulform = conn.getUser().schuleGetSchulform();
		final int schuljahr = conn.getUser().schuleGetSchuljahr(); // TODO hier muss das Schuljahr mithilfe des idSchuljahresabschnitt aus der Klasse erzeugt werden! (siehe Defaults unten)

		dtoKlassen.ID = newId;
		dtoKlassen.Sichtbar = true;
		dtoKlassen.Sortierung = 32000;
		dtoKlassen.AdrMerkmal = teilstandort.AdrMerkmal;
		dtoKlassen.OrgFormKrz = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(schuljahr).kuerzel;
		dtoKlassen.ASDSchulformNr = Schulgliederung.getDefault(schulform).daten(schuljahr).kuerzel;
		dtoKlassen.Klassenart = Klassenart.getDefault(schulform).daten(schuljahr).kuerzel;
	}



	/**
	 * Führt das Mapping der Klassenleitungen des Core-DTOs auf das zugehörige Datenbank-DTO durch.
	 *
	 * @param dto     das Datenbank-DTO
	 * @param value   der Wert des Core-DTO-Attributes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	private void mapKlassenleitungen(final DTOKlassen dto, final Object value) throws ApiOperationException {
		// Deserialisiere Klassenleitungsliste
		final List<Long> klassenLeitungList = JSONMapper.convertToListOfLong(value, false);

		// Validiere, dass keine ID-Duplikate vorhanden sind
		if (klassenLeitungList.stream().distinct().toArray().length != klassenLeitungList.size())
			throw new ApiOperationException(Status.BAD_REQUEST, "Es dürfen keine Lehrer-IDs doppelt in der Klassenleitungs-Liste vorhanden sein");

		// Bestimme alle hinzuzufügenden Lehrkräfte und prüfe, ob alle IDs auch zu einer Lehrkraft gehören
		if ((!klassenLeitungList.isEmpty()) && (conn.queryByKeyList(DTOLehrer.class, klassenLeitungList).size() != klassenLeitungList.size()))
			throw new ApiOperationException(Status.BAD_REQUEST, "Mindestens zu einer der angegebenen Lehrer-IDs ist keine Lehrkraft bekannt");

		// Bestimme die bisherigen Klassenleitungen zu der Klasse
		final List<DTOKlassenLeitung> klassenleitungenPersistence = conn.queryList(DTOKlassenLeitung.QUERY_BY_KLASSEN_ID, DTOKlassenLeitung.class, dto.ID);

		// Entferne alle Klassenleitungen, die nicht in der neuen Liste der Klassenleitungen sind
		for (final DTOKlassenLeitung entry : klassenleitungenPersistence) {
			final long lehrerIdEntry = entry.Lehrer_ID;
			if (!klassenLeitungList.contains(lehrerIdEntry)) {
				if (!conn.transactionRemove(entry))
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
							"Die Klassenleitung mit der ID %d konnte bei der Klasse %d nicht entfernt werden.".formatted(entry.Lehrer_ID, entry.Klassen_ID));
				conn.transactionFlush();
			}
		}

		// Erstelle bzw. aktualisiere dann die Klassenleitungen, welche in der neuen Liste der Klassenleitungen sind
		for (int i = 0; i < klassenLeitungList.size(); i++) {
			// Überprüfe, ob es bereits eine Klassenleitung mit der jeweiligen Klassen-ID und Lehrer-ID gibt
			final List<DTOKlassenLeitung> persistierteKlassenleitung =
					conn.queryList(DTOKlassenLeitung.QUERY_PK, DTOKlassenLeitung.class, dto.ID, klassenLeitungList.get(i));

			// Prüfe, ob die Klassenleitung gefunden wurde...
			if (!persistierteKlassenleitung.isEmpty()) {
				// ... und aktualisiere diese bei Bedarf
				final DTOKlassenLeitung dtoKlassenLeitung = persistierteKlassenleitung.getFirst();
				if (dtoKlassenLeitung.Reihenfolge == i)
					continue; // bereits aktuell
				// Setze den Reihenfolgenwert anhand des Index in der neuen Liste und persistiere die Änderung
				dtoKlassenLeitung.Reihenfolge = i;
				if (!conn.transactionPersist(dtoKlassenLeitung))
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Klassenleitung konnte nicht geupdatet werden");
			} else {
				// ... und erstelle eine neue Klassenleitung, welche dann persistiert wird
				final DTOKlassenLeitung dtoKlassenLeitung = new DTOKlassenLeitung(dto.ID, klassenLeitungList.get(i), i);
				if (!conn.transactionPersist(dtoKlassenLeitung))
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Klassenleitung konnte nicht persistiert werden");
			}
			conn.transactionFlush();
		}
	}


	/**
	 * Führt das Mapping des Jahrgangs des Core-DTOs auf das zugehörige Datenbank-DTO durch.
	 *
	 * @param dto     das Datenbank-DTO
	 * @param value   der Wert des Core-DTO-Attributes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	private void mapJahrgang(final DTOKlassen dto, final Object value) throws ApiOperationException {
		final Long idJahrgang = JSONMapper.convertToLong(value, true);
		if (idJahrgang == null) {
			// Jahrgangs-übergreifende Klasse -> JU
			dto.Jahrgang_ID = null;
			dto.ASDKlasse = "JU" + (((dto.ASDKlasse != null) && (dto.ASDKlasse.length() > 2)) ? dto.ASDKlasse.charAt(2) : "");
		} else {
			final DTOJahrgang jg = conn.queryByKey(DTOJahrgang.class, idJahrgang);
			if (jg == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Der Jahrgang mit der ID %d konnte nicht gefunden werden.".formatted(idJahrgang));
			dto.Jahrgang_ID = jg.ID;
			String asdKlassenjahrgang = jg.ASDJahrgang;
			if ("E1".equals(jg.ASDJahrgang))
				asdKlassenjahrgang = "1E";
			else if ("E2".equals(jg.ASDJahrgang))
				asdKlassenjahrgang = "2E";
			dto.ASDKlasse = asdKlassenjahrgang + (((dto.ASDKlasse != null) && (dto.ASDKlasse.length() > 2)) ? dto.ASDKlasse.charAt(2) : "");
		}
	}


	/**
	 * Führt das Mapping der Parallelität des Core-DTOs auf das zugehörige Datenbank-DTO durch.
	 *
	 * @param dto     das Datenbank-DTO
	 * @param value   der Wert des Core-DTO-Attributes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	private static void mapParallelitaet(final DTOKlassen dto, final Object value) throws ApiOperationException {
		final String parallelitaet = JSONMapper.convertToString(value, true, false, 1);
		if (parallelitaet == null) {
			dto.ASDKlasse = dto.ASDKlasse.substring(0, 2);
		} else {
			final char p = parallelitaet.charAt(0);
			if ((p < 'A') || (p > 'Z'))
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die Parallelität muss durch einen Buchstaben A-Z in Großschreibung angegeben werden.");
			dto.ASDKlasse = dto.ASDKlasse.substring(0, 2) + p;
		}
	}


	/**
	 * Führt das Mapping der Vorgänger-Klasse des Core-DTOs auf das zugehörige Datenbank-DTO durch.
	 *
	 * @param dto     das Datenbank-DTO
	 * @param value   der Wert des Core-DTO-Attributes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	private void mapVorgaengerKlasse(final DTOKlassen dto, final Object value) throws ApiOperationException {
		final Long idVorgaengerklasse = JSONMapper.convertToLong(value, true);
		if (idVorgaengerklasse == null) {
			dto.VKlasse = null;
		} else {
			final DTOKlassen vk = conn.queryByKey(DTOKlassen.class, idVorgaengerklasse);
			if (vk == null)
				throw new ApiOperationException(Status.NOT_FOUND,
						"Die Vorgängerklasse mit der ID %d wurde nicht gefunden.".formatted(idVorgaengerklasse));
			final DTOSchuljahresabschnitte a = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
			if (a == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Die ID des Schuljahresabschnitts %d der Klasse mit der ID %d ist ungültig.".formatted(dto.Schuljahresabschnitts_ID, dto.ID));
			if (vk.Schuljahresabschnitts_ID != a.VorigerAbschnitt_ID)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die ID für die Vorgängerklasse gehört nicht zu einer Klasse aus dem vorigen Schuljahresabschnitt.");
			dto.VKlasse = vk.Klasse;
		}
	}


	/**
	 * Führt das Mapping der Folge-Klasse des Core-DTOs auf das zugehörige Datenbank-DTO durch.
	 *
	 * @param dto     das Datenbank-DTO
	 * @param value   der Wert des Core-DTO-Attributes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	private void mapFolgeKlasse(final DTOKlassen dto, final Object value) throws ApiOperationException {
		final Long idFolgeklasse = JSONMapper.convertToLong(value, true);
		if (idFolgeklasse == null) {
			dto.FKlasse = null;
		} else {
			final DTOKlassen fk = conn.queryByKey(DTOKlassen.class, idFolgeklasse);
			if (fk == null)
				throw new ApiOperationException(Status.NOT_FOUND, "Die Folgeklasse mit der ID %d wurde nicht gefunden.".formatted(idFolgeklasse));
			final DTOSchuljahresabschnitte a = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
			if (a == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Die ID des Schuljahresabschnitts %d der Klasse mit der ID %d ist ungültig.".formatted(dto.Schuljahresabschnitts_ID, dto.ID));
			if (fk.Schuljahresabschnitts_ID != a.FolgeAbschnitt_ID)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die ID für die Folgeklasse gehört nicht zu einer Klasse aus dem nachfolgenden Schuljahresabschnitt.");
			dto.FKlasse = fk.Klasse;
		}
	}


	/**
	 * Führt das Mapping der allgemeinbildenden Organisationsform des Core-DTOs auf das zugehörige Datenbank-DTO durch.
	 *
	 * @param dto     das Datenbank-DTO
	 * @param value   der Wert des Core-DTO-Attributes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	private void mapAllgemeinbildendOrganisationsform(final DTOKlassen dto, final Object value) throws ApiOperationException {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetAbschnittById(dto.Schuljahresabschnitts_ID);
		if (abschnitt == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Keinen Schuljahresabschnitt für die ID %d gefunden.".formatted(dto.Schuljahresabschnitts_ID));
		if (!conn.getUser().schuleGetSchulform().istAllgemeinbildend())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine allgemeinbildende Schulform hat.");
		final Long idOrgform = JSONMapper.convertToLong(value, true);
		AllgemeinbildendOrganisationsformen orgform = AllgemeinbildendOrganisationsformen.data().getWertByID(idOrgform);
		if (idOrgform == null)
			orgform = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET;
		if (orgform == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die allgemeinene Organisationform ist ungültig");
		final OrganisationsformKatalogEintrag oke = orgform.daten(abschnitt.schuljahr);
		if (oke == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d für die allgemeinene Organisationform ist für das Schuljahr %d der Klasse ungültig".formatted(idOrgform, abschnitt.schuljahr));
		dto.OrgFormKrz = oke.kuerzel;
	}


	/**
	 * Führt das Mapping der berufsbildenden Organisationsform des Core-DTOs auf das zugehörige Datenbank-DTO durch.
	 *
	 * @param dto     das Datenbank-DTO
	 * @param value   der Wert des Core-DTO-Attributes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	private void mapBerufsbildendOrganisationsform(final DTOKlassen dto, final Object value) throws ApiOperationException {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetAbschnittById(dto.Schuljahresabschnitts_ID);
		if (abschnitt == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Keinen Schuljahresabschnitt für die ID %d gefunden.".formatted(dto.Schuljahresabschnitts_ID));
		if (!conn.getUser().schuleGetSchulform().istBerufsbildend())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine berufsbildende Schulform hat.");
		final Long idOrgform = JSONMapper.convertToLong(value, true);
		if (idOrgform == null) {
			final OrganisationsformKatalogEintrag oke = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(abschnitt.schuljahr);
			if (oke == null)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die allgemeinene Organisationform NICHT_ZUGEORDNET ist für das Schuljahr %d der Klasse ungültig".formatted(abschnitt.schuljahr));
			dto.OrgFormKrz = oke.kuerzel;
		} else {
			final BerufskollegOrganisationsformen orgform = BerufskollegOrganisationsformen.data().getWertByID(idOrgform);
			if (orgform == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die berufsbildende Organisationform ist ungültig");
			final OrganisationsformKatalogEintrag oke = orgform.daten(abschnitt.schuljahr);
			if (oke == null)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die ID %d für die berufsbildende Organisationform ist für das Schuljahr %d der Klasse ungültig".formatted(idOrgform,
								abschnitt.schuljahr));
			dto.OrgFormKrz = oke.kuerzel;
		}
	}


	/**
	 * Führt das Mapping der WBK-Organisationsform des Core-DTOs auf das zugehörige Datenbank-DTO durch.
	 *
	 * @param dto     das Datenbank-DTO
	 * @param value   der Wert des Core-DTO-Attributes
	 *
	 * @throws ApiOperationException   wenn ein Fehler bei dem Mapping auftritt
	 */
	private void mapWeiterbildungOrganisationsform(final DTOKlassen dto, final Object value) throws ApiOperationException {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetAbschnittById(dto.Schuljahresabschnitts_ID);
		if (abschnitt == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Keinen Schuljahresabschnitt für die ID %d gefunden.".formatted(dto.Schuljahresabschnitts_ID));
		if (!conn.getUser().schuleGetSchulform().istWeiterbildung())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine Schulform für die Weiterbildung hat.");
		final Long idOrgform = JSONMapper.convertToLong(value, true);
		if (idOrgform == null) {
			final OrganisationsformKatalogEintrag oke = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(abschnitt.schuljahr);
			if (oke == null)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die allgemeinene Organisationform NICHT_ZUGEORDNET ist für das Schuljahr %d der Klasse ungültig".formatted(abschnitt.schuljahr));
			dto.OrgFormKrz = oke.kuerzel;
		} else {
			final WeiterbildungskollegOrganisationsformen orgform = WeiterbildungskollegOrganisationsformen.data().getWertByID(idOrgform);
			if (orgform == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d für die Organisationform am Weiterbildungskolleg ist ungültig");
			final OrganisationsformKatalogEintrag oke = orgform.daten(abschnitt.schuljahr);
			if (oke == null)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die ID %d für die Organisationform am Weiterbildungskolleg ist für das Schuljahr %d der Klasse ungültig".formatted(idOrgform,
								abschnitt.schuljahr));
			dto.OrgFormKrz = oke.kuerzel;
		}
	}


	@Override
	protected void mapAttribute(final DTOKlassen dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		final Schulform schulform = conn.getUser().schuleGetSchulform();
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		switch (name) {
			case "kuerzel" -> {
				// Prüfen, ob das Klassenkürzel bereits im Schuljahresabschnitt existiert
				final String existingKlasseQuery = "SELECT e FROM DTOKlassen e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klasse = ?2";
				final boolean klasseAlreadyExists = !conn.query(existingKlasseQuery, DTOKlassen.class)
						.setParameter(1, dto.Schuljahresabschnitts_ID)
						.setParameter(2, value)
						.getResultList()
						.isEmpty();
				if (klasseAlreadyExists)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Klasse %s existiert bereits im definierten Schuljahresabschnitt".formatted(value));

				dto.Klasse = JSONMapper.convertToString(value, false, false, 15);
			}
			case "idJahrgang" -> mapJahrgang(dto, value);
			case "parallelitaet" -> mapParallelitaet(dto, value);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToIntegerInRange(value, false, 0, Integer.MAX_VALUE);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false);
			case "teilstandort" -> {
				final String t = JSONMapper.convertToString(value, false, false, 1);
				final DTOTeilstandorte teilstandort = conn.queryByKey(DTOTeilstandorte.class, t);
				if (teilstandort == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Der Teilstandort %s wurde nicht gefunden.".formatted(t));
				dto.AdrMerkmal = t;
			}
			case "beschreibung" -> dto.Bezeichnung = JSONMapper.convertToString(value, true, true, 151);
			case "idVorgaengerklasse" -> mapVorgaengerKlasse(dto, value);
			case "idFolgeklasse" -> mapFolgeKlasse(dto, value);
			case "idAllgemeinbildendOrganisationsform" -> mapAllgemeinbildendOrganisationsform(dto, value);
			case "idBerufsbildendOrganisationsform" -> mapBerufsbildendOrganisationsform(dto, value);
			case "idWeiterbildungOrganisationsform" -> mapWeiterbildungOrganisationsform(dto, value);
			case "idSchulgliederung" -> {
				final Long idSchulgliederung = JSONMapper.convertToLong(value, true);
				if (((idSchulgliederung == null) || (idSchulgliederung == -1)) && (Schulgliederung.getDefault(schulform) == null)) {
					dto.ASDSchulformNr = null;
					return;
				}
				final Schulgliederung sgl = ((idSchulgliederung == null) || (idSchulgliederung == -1))
						? Schulgliederung.getDefault(schulform)
						: Schulgliederung.data().getWertByID(idSchulgliederung);
				if (!sgl.hatSchulform(abschnitt.schuljahr, schulform))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulgliederung wird von der angegeben Schulform nicht unterstützt.");
				dto.ASDSchulformNr = sgl.daten(abschnitt.schuljahr).kuerzel;
			}
			case "idKlassenart" -> {
				final Long idKlassenart = JSONMapper.convertToLong(value, true);
				final Klassenart k = Klassenart.data().getWertByID(idKlassenart);
				if (k == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Klassenart für die ID %d konnte nicht gefunden werden.".formatted(idKlassenart));
				dto.Klassenart = k.daten(abschnitt.schuljahr).kuerzel;
			}
			case "noteneingabeGesperrt" -> dto.NotenGesperrt = JSONMapper.convertToBoolean(value, false);
			case "verwendungAnkreuzkompetenzen" -> dto.Ankreuzzeugnisse = JSONMapper.convertToBoolean(value, false);
			case "idFachklasse" -> {
				final Long idFachklasse = JSONMapper.convertToLong(value, true);
				if (idFachklasse == null) {
					dto.Fachklasse_ID = null;
				} else {
					final BerufskollegFachklassenKatalogDaten fachklasse = JsonDaten.fachklassenManager.getDatenByID(idFachklasse);
					if (fachklasse == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Keine Fachklasse die ID %d gefunden.".formatted(idFachklasse));
					dto.Fachklasse_ID = fachklasse.id;
				}
			}
			case "beginnSommersemester" -> dto.SommerSem = JSONMapper.convertToBoolean(value, false);
			case "klassenLeitungen" -> mapKlassenleitungen(dto, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	/**
	 * Die Methode ermittelt eine Liste von {@link DTOKlassen} Objekten zu den angegebenen Klassen IDs.
	 *
	 * @param klassenIds ID der Klasse
	 *
	 * @return Ein {@link DTOKlassen} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<DTOKlassen> getDTOsByIds(final List<Long> klassenIds) throws ApiOperationException {
		if (klassenIds == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die IDs für die Klassen dürfen nicht null sein.");

		final List<DTOKlassen> klassenDtos = conn.queryList(DTOKlassen.QUERY_LIST_BY_ID, DTOKlassen.class, klassenIds);
		if (klassenDtos.size() != klassenIds.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden nicht alle Klassen zu den IDs gefunden.");

		return klassenDtos;
	}

	/**
	 * Die Methode ermittelt eine Liste von {@link DTOKlassen} Objekten zu der angegebenen Schuljahresabschnitt ID.
	 *
	 * @param schuljahresabschnittId ID des Schuljahresabschnittes
	 *
	 * @return Liste von Klassen zu einem Schuljahresabschnitt
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<DTOKlassen> getDTOsBySchuljahresabschnittId(final Long schuljahresabschnittId) throws ApiOperationException {
		if (schuljahresabschnittId == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Schuljahresabschnitt darf nicht null sein.");

		final List<DTOKlassen> klassenDtos = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnittId);
		if (klassenDtos.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden keine Klassen zu der Schuljahresabschnitt ID %d gefunden.".formatted(schuljahresabschnittId));

		return klassenDtos;
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
	public DTOKlassen getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Klasse darf nicht null sein.");

		final DTOKlassen klasseDto = conn.queryByKey(DTOKlassen.class, id);
		if (klasseDto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Klasse zur ID " + id + " gefunden.");

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
				.filter(sla -> sla.WechselNr == 0)
				.map(sla -> sla.Schueler_ID)
				.toList();
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
	 * Wandelt ein DTOKlassen Objekt in ein KlassenDaten Objekt um.
	 *
	 * @param dto   DTOKlassen Objekt
	 *
	 * @return das neu erstellte KlassenDaten Objekt
	 */
	@Override
	protected KlassenDaten map(final DTOKlassen dto) throws ApiOperationException {
		return map(dto, true);
	}

	/**
	 * Methode liefert eine Liste von {@link KlassenDaten} zu einem Schuljahresabschnitt zurück.
	 *
	 * @param dtos zu mappende DTOs
	 * @param schuljahresabschnittId ID des Schuljahresabschnitts
	 * @param attachSchueler gibt an, ob die Schueler zu den Klassen geladen werden sollen
	 *
	 * @return Liste von KlassenDaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private List<KlassenDaten> mapList(final List<DTOKlassen> dtos, final Long schuljahresabschnittId, final boolean attachSchueler)
			throws ApiOperationException {
		// Bestimme die Information zum Schuljahresabschnitt
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(schuljahresabschnittId);
		final Map<String, DTOKlassen> klassenVorher = getKlassenBySchuljahresabschnittId(schuljahresabschnitt.idVorigerAbschnitt);
		final Map<String, DTOKlassen> klassenNachher = getKlassenBySchuljahresabschnittId(schuljahresabschnitt.idFolgeAbschnitt);

		final List<KlassenDaten> klassenDatenList = new ArrayList<>();
		for (final DTOKlassen dto : dtos)
			klassenDatenList.add(mapInternal(dto, schuljahresabschnitt, klassenVorher, klassenNachher, attachSchueler));

		return klassenDatenList;
	}

	private KlassenDaten map(final DTOKlassen dto, final boolean attachSchueler) throws ApiOperationException {
		// Bestimme die Informationen zur Schule und zu den Schuljahresabschnitten
		final Schuljahresabschnitt schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(dto.Schuljahresabschnitts_ID);

		final Map<String, DTOKlassen> klassenVorher = getKlassenBySchuljahresabschnittId(schuljahresabschnitt.idVorigerAbschnitt);
		final Map<String, DTOKlassen> klassenNachher = getKlassenBySchuljahresabschnittId(schuljahresabschnitt.idFolgeAbschnitt);

		return mapInternal(dto, schuljahresabschnitt, klassenVorher, klassenNachher, attachSchueler);
	}

	private KlassenDaten mapInternal(final DTOKlassen dto, final Schuljahresabschnitt schuljahresabschnitt,
			final Map<String, DTOKlassen> klassenVorher, final Map<String, DTOKlassen> klassenNachher, final boolean attachSchueler) {
		final Schulform schulform = conn.getUser().schuleGetSchulform();
		final KlassenDaten klassenDaten = new KlassenDaten();

		final List<DTOKlassenLeitung> klassenLeitungen = conn.queryList(DTOKlassenLeitung.QUERY_BY_KLASSEN_ID + " ORDER BY e.Reihenfolge",
				DTOKlassenLeitung.class, dto.ID);
		for (final DTOKlassenLeitung klassenLeitungDto : klassenLeitungen)
			klassenDaten.klassenLeitungen.add(klassenLeitungDto.Lehrer_ID);

		klassenDaten.id = dto.ID;
		klassenDaten.idSchuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		klassenDaten.kuerzel = dto.Klasse;
		klassenDaten.idSchulgliederung = getSchulgliederungIdByKlasseAndSchulform(dto, schulform);
		klassenDaten.idJahrgang = dto.Jahrgang_ID;
		klassenDaten.parallelitaet = ((dto.ASDKlasse == null) || (dto.ASDKlasse.length() < 3)) ? null : dto.ASDKlasse.substring(2);
		klassenDaten.sortierung = dto.Sortierung;
		klassenDaten.istSichtbar = dto.Sichtbar;
		klassenDaten.teilstandort = Objects.toString(dto.AdrMerkmal, "");
		klassenDaten.beschreibung = Objects.toString(dto.Bezeichnung, "");

		klassenDaten.idAllgemeinbildendOrganisationsform = (AllgemeinbildendOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz) == null)
				? null : AllgemeinbildendOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz).daten(schuljahresabschnitt.schuljahr).id;
		klassenDaten.idBerufsbildendOrganisationsform = (BerufskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz) == null)
				? null : BerufskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz).daten(schuljahresabschnitt.schuljahr).id;
		klassenDaten.idWeiterbildungOrganisationsform = (WeiterbildungskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz) == null)
				? null : WeiterbildungskollegOrganisationsformen.data().getWertByKuerzel(dto.OrgFormKrz).daten(schuljahresabschnitt.schuljahr).id;
		klassenDaten.pruefungsordnung = dto.PruefOrdnung;

		final Klassenart klassenart = Klassenart.data().getWertByKuerzel(dto.Klassenart);
		klassenDaten.idKlassenart = ((klassenart != null) && (klassenart.hatSchulform(schuljahresabschnitt.schuljahr, schulform)))
				? klassenart.daten(schuljahresabschnitt.schuljahr).id
				: Klassenart.UNDEFINIERT.daten(schuljahresabschnitt.schuljahr).id;
		klassenDaten.noteneingabeGesperrt = (dto.NotenGesperrt != null) && dto.NotenGesperrt;
		klassenDaten.verwendungAnkreuzkompetenzen = (dto.Ankreuzzeugnisse != null) && dto.Ankreuzzeugnisse;
		klassenDaten.kuerzelVorgaengerklasse = dto.VKlasse;
		klassenDaten.kuerzelFolgeklasse = dto.FKlasse;
		klassenDaten.idFachklasse = dto.Fachklasse_ID;
		klassenDaten.beginnSommersemester = Boolean.TRUE.equals(dto.SommerSem);

		// Bestimme die IDs der Vorgänger- und der Nachfolge-Klassen dieser Klasse, sofern möglich und berücksichtige dabei den Semesterbetrieb i, Weiterbildungskolleg
		if (klassenDaten.kuerzelVorgaengerklasse != null) {
			final String kuerzelVorgaenger = ((schulform != Schulform.WB) && (schuljahresabschnitt.abschnitt == 2))
					? klassenDaten.kuerzel : klassenDaten.kuerzelVorgaengerklasse;
			klassenDaten.idVorgaengerklasse = Optional.ofNullable(klassenVorher.get(kuerzelVorgaenger)).map(e -> e.ID).orElse(null);
		}

		if (klassenDaten.kuerzelFolgeklasse != null) {
			final String kuerzelNachfolger = ((schulform != Schulform.WB) && (schuljahresabschnitt.abschnitt == 1))
					? klassenDaten.kuerzel : klassenDaten.kuerzelFolgeklasse;
			klassenDaten.idFolgeklasse = Optional.ofNullable(klassenNachher.get(kuerzelNachfolger)).map(e -> e.ID).orElse(null);
		}

		if (attachSchueler) {
			final List<DTOSchueler> schuelerDtosNichtGeloescht = getSchuelerDtosNichtGeloeschtByKlassenID(dto.ID);
			for (final DTOSchueler schuelerDto : schuelerDtosNichtGeloescht)
				klassenDaten.schueler.add(DataSchuelerliste.mapToSchueler(schuelerDto, null));  // TODO Bestimme den Abschlussjahrgang
		}

		return klassenDaten;
	}

	// TODO: Methode sollte später durch eigene Methode in zugehöriger DataManager Klasse abgelöst werden
	Map<String, DTOKlassen> getKlassenBySchuljahresabschnittId(final Long schuljahresabschnittId) {
		if (schuljahresabschnittId == null)
			return new HashMap<>();

		return conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schuljahresabschnittId).stream()
				.collect(Collectors.toMap(k -> k.Klasse, k -> k));
	}

	// TODO: Methode sollte später durch eigene Methode in zugehöriger DataManager Klasse abgelöst werden
	Long getSchulgliederungIdByKlasseAndSchulform(final DTOKlassen dto, final Schulform schulform) {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		Schulgliederung gliederung = Schulgliederung.getBySchuljahrAndSchulformAndSchluessel(abschnitt.schuljahr, schulform, dto.ASDSchulformNr);
		if (gliederung == null)
			gliederung = Schulgliederung.getDefault(schulform);
		return (gliederung != null) ? gliederung.daten(abschnitt.schuljahr).id : -1;
	}


	/**
	 * Diese Methode prüft, ob alle Vorbedingungen zum Löschen einer Klasse erfüllt sind.
	 * Es wird eine {@link SimpleOperationResponse} zurückgegeben.
	 *
	 * @param dtoKlasse   das DTO der Klasse, die gelöscht werden soll
	 *
	 * @return Liefert eine Response mit dem Log der Vorbedingungsprüfung zurück.
	 */
	SimpleOperationResponse checkDeletePreConditions(final @NotNull DTOKlassen dtoKlasse) {
		final SimpleOperationResponse operationResponse = new SimpleOperationResponse();
		operationResponse.id = dtoKlasse.ID;

		// Die Klasse darf keine Schüler beinhalten. Dies kann an zugeordneten Lernabschnittsdaten geprüft werden
		final List<Long> schuelerIds = getSchuelerIDsByKlassenID(dtoKlasse.ID);
		if (!schuelerIds.isEmpty())
			operationResponse.log.add("Klasse %s (ID: %d) hat noch %d verknüpfte(n) Schüler.".formatted(dtoKlasse.Klasse, dtoKlasse.ID, schuelerIds.size()));

		return operationResponse;
	}

	// TODO: Methode sollte später durch eigene Methode in zugehöriger DataManager Klasse abgelöst werden
	@NotNull
	DTOEigeneSchule getDTOEigeneSchule() throws ApiOperationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnten keine Informationen zur Schule gefunden werden.");
		return schule;
	}

	// TODO: Methode sollte später durch eigene Methode in zugehöriger DataManager Klasse abgelöst werden
	@NotNull
	DTOTeilstandorte getDTOTeilstandort() throws ApiOperationException {
		final DTOTeilstandorte teilstandort = conn.querySingle(DTOTeilstandorte.class);
		if (teilstandort == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es ist kein Teilstandort definiert, es muss mindestens ein Teilstandort hinterlegt sein.");
		return teilstandort;
	}
}
