package de.svws_nrw.data.erzieher;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOErzieherart;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO {@link ErzieherStammdaten}.
 * Verwaltet die Erzieher eines Schülers, dabei wird anhand der Namensfelder im DTO unterschieden, ob es sich um den ersten oder zweiten Erzieher handelt.
 * Für jede Position wird ein eindeutiges Suffix an die interne ID angehängt (1 bzw. 2), um Mehrdeutigkeiten zu vermeiden.
 */
public final class DataErzieherStammdaten extends DataManagerRevised<Long, DTOSchuelerErzieherAdresse, ErzieherStammdaten> {

	// Die ID des Erziehers (mit Suffix-Logik)
	private final Long idErzieher;
	// Die ID des Schülers, zu dem die Erzieherdaten gehören
	private final Long idSchueler;
	// Die Position des Erziehers (1 = erster Erzieher, 2 = zweiter Erzieher)
	private final int pos;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link ErzieherStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataErzieherStammdaten(final DBEntityManager conn) {
		super(conn);
		this.idSchueler = 0L;
		this.idErzieher = 0L;
		this.pos = 0;
		setAttributesNotPatchable("id");
	}

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link ErzieherStammdaten}.
	 * Konstruktor für einen normalen Patch
	 *
	 * @param conn           die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idErzieher     die ID der Erzieher (mit Suffix)
	 */
	public DataErzieherStammdaten(final DBEntityManager conn, final long idErzieher) {
		super(conn);
		this.idSchueler = 0L;
		this.idErzieher = idErzieher;
		this.pos = 0;
		setAttributesNotPatchable("id");
	}

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link ErzieherStammdaten}.
	 * Konstruktor, für das Patchen eines Erziehers an zweiter Position in einem Eintrag eines Schülers
	 *
	 * @param conn           die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler     die ID des Schülers
	 * @param pos            die Position der Erzieher (hier immer an der zweiten Position)
	 */
	public DataErzieherStammdaten(final DBEntityManager conn, final long idSchueler, final int pos) {
		super(conn);
		this.idSchueler = idSchueler;
		this.pos = pos;
		this.idErzieher = 0L;
		setAttributesNotPatchable("id");
	}

	@Override
	protected ErzieherStammdaten map(final DTOSchuelerErzieherAdresse dto) throws ApiOperationException {
		// Entscheidet anhand belegter Namensfelder, ob es sich um Erzieher 1 oder 2 handelt
		if ((dto.Name1 != null) && !dto.Name1.isBlank())
			return dtoMapperErzieher1.apply(dto);
		if ((dto.Name2 != null) && !dto.Name2.isBlank())
			return dtoMapperErzieher2.apply(dto);
		throw new ApiOperationException(Status.NOT_FOUND, "Erzieher konnte nicht gemappt werden: weder Name1 noch Name2 sind befüllt.");
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln des ersten Erziehers eines Datenbank-DTOs {@link DTOSchuelerErzieherAdresse} in einen Core-DTO {@link ErzieherStammdaten}.
	 */
	private final Function<DTOSchuelerErzieherAdresse, ErzieherStammdaten> dtoMapperErzieher1 = (final DTOSchuelerErzieherAdresse e) -> {
		final ErzieherStammdaten eintrag = new ErzieherStammdaten();
		// Für den ersten Erzieher desselben Eintrags wird das Suffix "1" angehängt
		eintrag.id = (e.ID * 10) + 1;
		eintrag.idSchueler = e.Schueler_ID;
		eintrag.idErzieherArt = e.ErzieherArt_ID;
		eintrag.titel = e.Titel1;
		eintrag.anrede = e.Anrede1;
		eintrag.nachname = e.Name1;
		eintrag.vorname = e.Vorname1;
		eintrag.strassenname = e.ErzStrassenname;
		eintrag.hausnummer = e.ErzHausNr;
		eintrag.hausnummerZusatz = e.ErzHausNrZusatz;
		eintrag.wohnortID = e.ErzOrt_ID;
		eintrag.ortsteilID = e.ErzOrtsteil_ID;
		eintrag.eMail = e.ErzEmail;
		eintrag.staatsangehoerigkeitID = (e.Erz1StaatKrz == null) ? null : e.Erz1StaatKrz.historie().getLast().iso3;
		eintrag.erhaeltAnschreiben = e.ErzAnschreiben;
		eintrag.bemerkungen = e.Bemerkungen;
		return eintrag;
	};

	/**
	 * Lambda-Ausdruck zum Umwandeln des zweiten Erziehers eines Datenbank-DTOs {@link DTOSchuelerErzieherAdresse} in einen Core-DTO {@link ErzieherStammdaten}.
	 */
	private final Function<DTOSchuelerErzieherAdresse, ErzieherStammdaten> dtoMapperErzieher2 = (final DTOSchuelerErzieherAdresse e) -> {
		final ErzieherStammdaten eintrag = new ErzieherStammdaten();
		// Für den zweiten Erzieher desselben Eintrags wird das Suffix "2" angehängt
		eintrag.id = (e.ID * 10) + 2;
		eintrag.idSchueler = e.Schueler_ID;
		eintrag.idErzieherArt = e.ErzieherArt_ID;
		eintrag.titel = e.Titel2;
		eintrag.anrede = e.Anrede2;
		eintrag.nachname = e.Name2;
		eintrag.vorname = e.Vorname2;
		eintrag.strassenname = e.ErzStrassenname;
		eintrag.hausnummer = e.ErzHausNr;
		eintrag.hausnummerZusatz = e.ErzHausNrZusatz;
		eintrag.wohnortID = e.ErzOrt_ID;
		eintrag.ortsteilID = e.ErzOrtsteil_ID;
		eintrag.eMail = e.ErzEmail2;
		eintrag.staatsangehoerigkeitID = (e.Erz2StaatKrz == null) ? null : e.Erz2StaatKrz.historie().getLast().iso3;
		eintrag.erhaeltAnschreiben = e.ErzAnschreiben;
		eintrag.bemerkungen = e.Bemerkungen;
		return eintrag;
	};

	@Override
	protected void initDTO(final DTOSchuelerErzieherAdresse dto, final Long id, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = id;
		dto.Schueler_ID = this.idSchueler;
	}

	/**
	 * Ermittelt eine Liste der {@link ErzieherStammdaten} für den Schüler mit der angegebenen ID.
	 *
	 * @param schuelerID   die ID des Schülers, dessen {@link ErzieherStammdaten} ermittelt werden sollen
	 *
	 * @return eine Liste mit den {@link ErzieherStammdaten} für den Schüler mit der angegebenen ID
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getListBySchuelerIdAsResponse(final long schuelerID) throws ApiOperationException {
		final List<ErzieherStammdaten> daten = getListBySchuelerId(schuelerID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * Ermittelt eine Liste der {@link ErzieherStammdaten} für den Schüler mit der angegebenen ID.
	 *
	 * @param schuelerID   die ID des Schülers, dessen {@link ErzieherStammdaten} ermittelt werden sollen
	 *
	 * @return ErieherStammdaten zur SchuelerID
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<ErzieherStammdaten> getListBySchuelerId(final long schuelerID) throws ApiOperationException {
		final List<DTOSchuelerErzieherAdresse> erzieher = conn.queryList(DTOSchuelerErzieherAdresse.QUERY_BY_SCHUELER_ID, DTOSchuelerErzieherAdresse.class,
				schuelerID);
		if (erzieher == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Erzieher für den Schüler mit der ID " + schuelerID + " nicht gefunden.");
		final List<ErzieherStammdaten> daten = new ArrayList<>();
		daten.addAll(erzieher.stream().filter(e -> ((e.Name1 != null) && !e.Name1.trim().isEmpty())).map(dtoMapperErzieher1).toList());
		daten.addAll(erzieher.stream().filter(e -> ((e.Name2 != null) && !e.Name2.trim().isEmpty())).map(dtoMapperErzieher2).toList());
		return daten;
	}

	/**
	 * Ermittelt eine Liste der {@link ErzieherStammdaten} für die Schüler mit den angegebenen IDs.
	 *
	 * @param schuelerIDs   die IDs der Schüler, deren {@link ErzieherStammdaten} ermittelt werden sollen
	 *
	 * @return ErieherStammdaten zun SchuelerIDs
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<ErzieherStammdaten> getListBySchuelerIds(final List<Long> schuelerIDs) throws ApiOperationException {
		final List<DTOSchuelerErzieherAdresse> erzieher = conn.queryList(DTOSchuelerErzieherAdresse.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerErzieherAdresse.class,
				schuelerIDs);
		if (erzieher == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Erzieher konnten nicht gefunden werden.");
		final List<ErzieherStammdaten> daten = new ArrayList<>();
		daten.addAll(erzieher.stream().filter(e -> ((e.Name1 != null) && !e.Name1.trim().isEmpty())).map(dtoMapperErzieher1).toList());
		daten.addAll(erzieher.stream().filter(e -> ((e.Name2 != null) && !e.Name2.trim().isEmpty())).map(dtoMapperErzieher2).toList());
		return daten;
	}

	/**
	 * Wandelt eine API-ID (mit Suffix 1 oder 2) in die echte Datenbank‑ID um, da der Client nur eine zusammengesetzte ID liefert.
	 *
	 * @param apiId   die vom Client übergebene API-ID mit 1 oder 2 als Suffix
	 *
	 * @return  die Datenbank‑ID (ohne Suffix), oder 0, wenn das Suffix ungültig ist
	 */
	public static long getDatabaseErzieherIdFromApiId(final long apiId) {
		// Extrahieren vom Positions-Suffix
		final long suffix = apiId % 10;
		// Position 1: Suffix abziehen und durch 10 teilen, um auf die Datenbank‑ID zu kommen
		if (suffix == 1)
			return (apiId - 1) / 10;
		// Position 2: Analoges Vorgehen
		else if (suffix == 2)
			return (apiId - 2) / 10;
		// falls kein gültiges Suffix
		else
			return 0;
	}

	@Override
	public ErzieherStammdaten getById(final Long tmpid) throws ApiOperationException {
		if (tmpid == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Erzieher‑ID fehlt");

		final long id;
		final int nr;
		final long suffix = tmpid % 10;

		// Wenn tmpid eine 1 oder 2 am Ende der ID besitzt -> Überprüfung, ob es sich um einen Patch handelt
		if ((this.pos != 1) && (((tmpid % 10) == 1) || ((tmpid % 10) == 2))) {
			// tmpid hat ein Suffix 1 oder 2
			id = tmpid / 10;
			nr = (int) suffix;
			// Ansonsten wird das Erstellen behandelt
		} else {
			// Neuer Datensatz wird an der Postion 1 angelegt
			id = tmpid;
			nr = 1;
		}

		final DTOSchuelerErzieherAdresse dto = conn.queryByKey(DTOSchuelerErzieherAdresse.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Erzieher mit ID " + id + " nicht gefunden");
		// Falls das Suffix 1 ist, wird der Datensatz an Position 1 gemappt, ansonsten an Position 2
		return (nr == 1) ? dtoMapperErzieher1.apply(dto) : dtoMapperErzieher2.apply(dto);
	}

	@Override
	protected void mapAttribute(final DTOSchuelerErzieherAdresse dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {

		// Prüfen, ob ein neuer Erzieher-Datensatz angelegt wird. idErzieher == 0L signalisiert den Create-Fall
		final boolean isCreate = (this.idErzieher == 0L);
		/*
		* Hier wird geprüft, an welcher Position das Attribut gemappt werden muss:
		*  - im Create-Fall wird das vom Client übergebene this.pos (1) verwendet
		*  - im normalen Patch-Fall (API-Call: patchErzieherStammdaten) das Suffix der bestehenden Erzieher-ID
		*  - beim Patchen/Anlegen eines weiteren Erziehers im selben Datenbankeintrags (API-Call: patchErzieherStammdatenZweitePosition) wird für this.pos eine
		*  "2" aus
		* dem Client mitgeschickt
		*/
		final int targetNr = isCreate ? this.pos : (int) (this.idErzieher % 10);

		switch (name) {
			case "idSchueler" -> {
				final Long idSchueler = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.Schueler_ID, idSchueler))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(idSchueler, dto.Schueler_ID));
			}
			case "idErzieherArt" -> {
				final Long artID = JSONMapper.convertToLong(value, true, name);
				if (artID == null) {
					dto.ErzieherArt_ID = null;
				} else {
					final DTOErzieherart art = conn.queryByKey(DTOErzieherart.class, artID);
					if (art == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Erzieherart mit ID " + artID + " nicht gefunden");
					dto.ErzieherArt_ID = artID;
				}
			}
			case "titel" -> {
				final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Titel1.datenlaenge(), name);
				if (targetNr == 1)
					dto.Titel1 = tmp;
				else
					dto.Titel2 = tmp;
			}
			case "anrede" -> {
				final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Anrede1.datenlaenge(), name);
				if (targetNr == 1)
					dto.Anrede1 = tmp;
				else
					dto.Anrede2 = tmp;
			}
			case "nachname" -> {
				final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Name1.datenlaenge(), name);
				if (targetNr == 1)
					dto.Name1 = tmp;
				else
					dto.Name2 = tmp;
			}
			case "vorname" -> {
				final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Vorname1.datenlaenge(), name);
				if (targetNr == 1)
					dto.Vorname1 = tmp;
				else
					dto.Vorname2 = tmp;
			}
			case "eMail" -> {
				final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_ErzEmail.datenlaenge(), name);
				if (targetNr == 1)
					dto.ErzEmail = tmp;
				else
					dto.ErzEmail2 = tmp;
			}
			case "strassenname" -> dto.ErzStrassenname = JSONMapper.convertToString(value, true, true,
					Schema.tab_SchuelerErzAdr.col_ErzStrassenname.datenlaenge(), name);
			case "hausnummer" -> dto.ErzHausNr = JSONMapper.convertToString(value, true, true,
					Schema.tab_SchuelerErzAdr.col_ErzHausNr.datenlaenge(), name);
			case "hausnummerZusatz" -> dto.ErzHausNrZusatz = JSONMapper.convertToString(value, true, true,
					Schema.tab_SchuelerErzAdr.col_ErzHausNrZusatz.datenlaenge(), name);
			case "wohnortID" -> setWohnort(dto, JSONMapper.convertToLong(value, true, name),
					Optional.ofNullable(JSONMapper.convertToLong(map.get("ortsteilID"), true, name)).orElse(dto.ErzOrtsteil_ID));
			case "ortsteilID" -> setWohnort(dto,
					Optional.ofNullable(JSONMapper.convertToLong(map.get("wohnortID"), true, name)).orElse(dto.ErzOrt_ID),
					JSONMapper.convertToLong(value, true, name));
			case "staatsangehoerigkeitID" -> {
				final String staatsangehoerigkeitID = JSONMapper.convertToString(value, true, true, null, name);
				if ((staatsangehoerigkeitID == null) || (staatsangehoerigkeitID.isEmpty())) {
					if (targetNr == 1)
						dto.Erz1StaatKrz = null;
					else
						dto.Erz2StaatKrz = null;
				} else {
					final Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeitID);
					if (nat == null)
						throw new ApiOperationException(Status.NOT_FOUND, "Staatsangehörigkeit mit der ID " + staatsangehoerigkeitID + " nicht gefunden");
					if (targetNr == 1)
						dto.Erz1StaatKrz = nat;
					else
						dto.Erz2StaatKrz = nat;
				}
			}
			case "erhaeltAnschreiben" -> dto.ErzAnschreiben = JSONMapper.convertToBoolean(value, true, name);
			case "bemerkungen" -> dto.Bemerkungen = JSONMapper.convertToString(value, true, true,
					Schema.tab_SchuelerErzAdr.col_Bemerkungen.datenlaenge(), name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Unbekanntes Feld: " + name);
		}
	}

	@Override
	public Response deleteMultipleAsResponse(final List<Long> apiIds) throws ApiOperationException {
		if (apiIds == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");

		// Extrahiere die echten Datenbank-IDs und zugehörigen Suffixe
		final Map<Long, List<Integer>> idMap = apiIds.stream()
				.collect(Collectors.groupingBy(
						DataErzieherStammdaten::getDatabaseErzieherIdFromApiId,
						Collectors.mapping(apiId -> (int) (apiId % 10), Collectors.toList())
				));

		// Alle DTOs laden
		final List<Long> dbIds = new ArrayList<>(idMap.keySet());
		final List<DTOSchuelerErzieherAdresse> dtos = conn.queryByKeyList(DTOSchuelerErzieherAdresse.class, dbIds);
		if (dtos.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Entitäten zu den IDs gefunden.");

		// Teile der DTOs werden auf null gesetzt oder komplett gelöscht
		final List<ErzieherStammdaten> deleted = new ArrayList<>();
		for (final DTOSchuelerErzieherAdresse dto : dtos) {
			final long dtoId = dto.ID;
			final List<Integer> suffixes = idMap.get(dtoId);
			if (suffixes == null)
				continue;
			// Für jede angefragte Position
			for (final int suffix : suffixes) {
				// Core-DTO zur gelöschten Position erzeugen
				final ErzieherStammdaten removed = (suffix == 1) ? dtoMapperErzieher1.apply(dto) : dtoMapperErzieher2.apply(dto);
				deleted.add(removed);
				// Felder der jeweiligen Position werden auf null gesetzt
				if (suffix == 1) {
					dto.Name1 = null;
					dto.Vorname1 = null;
					dto.Titel1 = null;
					dto.Anrede1 = null;
					dto.ErzEmail = null;
					dto.Erz1StaatKrz = null;
				} else {
					dto.Name2 = null;
					dto.Vorname2 = null;
					dto.Titel2 = null;
					dto.Anrede2 = null;
					dto.ErzEmail2 = null;
					dto.Erz2StaatKrz = null;
				}
			}
			// Falls beide Positionen leer sind, dann wird das gesamte DTO aus der Datenbank gelöscht
			final boolean pos1Empty = ((dto.Name1 == null) || dto.Name1.isBlank());
			final boolean pos2Empty = ((dto.Name2 == null) || dto.Name2.isBlank());
			if (pos1Empty && pos2Empty) {
				if (!conn.transactionRemove(dto))
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Entfernen der Entität.");
			} else {
				// Andernfalls nur flushen, damit die Änderungen persistiert werden
				conn.transactionFlush();
			}
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(deleted).build();
	}

	/**
	 * Setzt den Wohnort bei den Erzieherdaten und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
	 * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
	 *
	 * @param dto das Schüler-DTO der Datenbank
	 * @param wohnortID die zu setzende Wohnort-ID
	 * @param ortsteilID die zu setzende O	eil-ID
	 *
	 * @throws ApiOperationException eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
	 */
	void setWohnort(final DTOSchuelerErzieherAdresse dto, final Long wohnortID, final Long ortsteilID) throws ApiOperationException {
		if ((wohnortID != null) && (wohnortID < 0))
			throw new ApiOperationException(Status.CONFLICT);
		if ((ortsteilID != null) && (ortsteilID < 0))
			throw new ApiOperationException(Status.CONFLICT);

		// Prüfe, ob die Ortsteil ID in Bezug auf die WohnortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		dto.ErzOrtsteil_ID = isOrtsteilGueltig(ortsteilID, wohnortID) ? ortsteilID : null;
		dto.ErzOrt_ID = wohnortID;
	}

	/**
	 * Prüft, ob der Ortsteil mit der übergebenen ID für den Wohnort mit der übergebenen ID gültig ist.
	 *
	 * @param ortsteilID   die ID des Ortsteils
	 * @param wohnortID    die ID des Wohnortes
	 *
	 * @return true, falls der Ortsteil für den Wohnort gültig ist, und ansonsten false
	 */
	boolean isOrtsteilGueltig(final Long ortsteilID, final Long wohnortID) {
		if (ortsteilID == null)
			return false;
		final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, ortsteilID);
		return (ortsteil != null) && Objects.equals(ortsteil.Ort_ID, wohnortID);
	}

}
