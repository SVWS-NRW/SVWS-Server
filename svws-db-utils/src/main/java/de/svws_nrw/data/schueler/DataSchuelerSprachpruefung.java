package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.data.schueler.Sprachpruefung;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.fach.Sprachpruefungniveau;
import de.svws_nrw.core.types.fach.Sprachreferenzniveau;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link Sprachpruefung}.
 */
public final class DataSchuelerSprachpruefung extends DataManagerRevised<Long, DTOSchuelerSprachpruefungen, Sprachpruefung> {

	private final long idSchueler;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Sprachpruefung}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler   die ID des Schülers
	 */
	public DataSchuelerSprachpruefung(final DBEntityManager conn, final long idSchueler) {
		super(conn);
		setAttributesRequiredOnCreation("sprache");
		this.idSchueler = idSchueler;
	}


	@Override
	protected void initDTO(final DTOSchuelerSprachpruefungen dto, final Long newId) throws ApiOperationException {
		dto.ID = newId;
		dto.Schueler_ID = idSchueler;
	}


	@Override
	protected Sprachpruefung map(final DTOSchuelerSprachpruefungen dto) throws ApiOperationException {
		final Sprachpruefung daten = new Sprachpruefung();
		daten.sprache = dto.Sprache;
		daten.jahrgang = dto.ASDJahrgang;
		daten.anspruchsniveauId = (dto.Anspruchsniveau == null) ? null : dto.Anspruchsniveau.daten.id;
		daten.pruefungsdatum = dto.Pruefungsdatum;
		daten.istHSUPruefung = (dto.IstHSUPruefung != null) && dto.IstHSUPruefung;
		daten.istFeststellungspruefung = (dto.IstFeststellungspruefung != null) && dto.IstFeststellungspruefung;
		daten.kannErstePflichtfremdspracheErsetzen = (dto.KannErstePflichtfremdspracheErsetzen != null) && dto.KannErstePflichtfremdspracheErsetzen;
		daten.kannZweitePflichtfremdspracheErsetzen = (dto.KannZweitePflichtfremdspracheErsetzen != null) && dto.KannZweitePflichtfremdspracheErsetzen;
		daten.kannWahlpflichtfremdspracheErsetzen = (dto.KannWahlpflichtfremdspracheErsetzen != null) && dto.KannWahlpflichtfremdspracheErsetzen;
		daten.kannBelegungAlsFortgefuehrteSpracheErlauben =
				(dto.KannBelegungAlsFortgefuehrteSpracheErlauben != null) && dto.KannBelegungAlsFortgefuehrteSpracheErlauben;
		daten.referenzniveau = (dto.Referenzniveau == null) ? null : dto.Referenzniveau.daten.kuerzel;
		daten.note = (dto.NotePruefung == null) ? null : dto.NotePruefung.getNoteSekI();
		return daten;
	}


	@Override
	protected void mapAttribute(final DTOSchuelerSprachpruefungen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "sprache" -> {
				final String patchSprache = JSONMapper.convertToString(value, false, false, 2);
				if ((patchSprache == null) || (patchSprache.isBlank()))
					throw new ApiOperationException(Status.BAD_REQUEST, "Ein Patch darf kein leeres Sprachkürzel beinhalten");
				// TODO Prüfe, ob es sich um ein gültiges Sprachkürzel handelt.
				dto.Sprache = patchSprache;
			}
			case "jahrgang" -> {
				final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
				if (kuerzel == null) {
					dto.ASDJahrgang = null;
				} else {
					final Jahrgaenge jg = Jahrgaenge.getByKuerzel(kuerzel);
					if (jg == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Ungültiges Jahrgangs-Kürzel verwendet.");
					dto.ASDJahrgang = jg.daten.kuerzel;
				}
			}
			case "anspruchsniveauId" -> {
				final Integer id = JSONMapper.convertToInteger(value, true);
				if (id == null) {
					dto.Anspruchsniveau = null;
				} else {
					final Sprachpruefungniveau niveau = Sprachpruefungniveau.getByID(id);
					if (niveau == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Ungültiges Sprachprüfungsniveau-Kürzel verwendet.");
					dto.Anspruchsniveau = niveau;
				}
			}
			case "pruefungsdatum" -> dto.Pruefungsdatum = JSONMapper.convertToString(value, true, false, null);
			case "istHSUPruefung" -> dto.IstHSUPruefung = JSONMapper.convertToBoolean(value, false);
			case "istFeststellungspruefung" -> dto.IstFeststellungspruefung = JSONMapper.convertToBoolean(value, false);
			case "kannErstePflichtfremdspracheErsetzen" -> dto.KannErstePflichtfremdspracheErsetzen = JSONMapper.convertToBoolean(value, false);
			case "kannZweitePflichtfremdspracheErsetzen" -> dto.KannZweitePflichtfremdspracheErsetzen = JSONMapper.convertToBoolean(value, false);
			case "kannWahlpflichtfremdspracheErsetzen" -> dto.KannWahlpflichtfremdspracheErsetzen = JSONMapper.convertToBoolean(value, false);
			case "kannBelegungAlsFortgefuehrteSpracheErlauben" -> dto.KannBelegungAlsFortgefuehrteSpracheErlauben = JSONMapper.convertToBoolean(value, false);
			case "referenzniveau" -> {
				final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
				if (kuerzel == null) {
					dto.Referenzniveau = null;
				} else {
					final Sprachreferenzniveau niveau = Sprachreferenzniveau.getByKuerzel(kuerzel);
					if (niveau == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Ungültiges Sprachreferenzniveau-Kürzel verwendet.");
					dto.Referenzniveau = niveau;
				}
			}
			case "note" -> {
				final Integer note = JSONMapper.convertToIntegerInRange(value, true, 1, 6);
				if (note == null) {
					dto.NotePruefung = null;
				} else {
					final Note notePruefung = Note.fromNoteSekI(note);
					if (notePruefung == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Ungültige Note angegeben.");
					dto.NotePruefung = notePruefung;
				}
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}


	/**
	 * Prüft, ob der Benutzer mit einer funktionsbezogenen Kompetenz auf den aktuellen Lernabschnitt des Schülers zugreift
	 * und wenn ja, ob dieser dann die Kompetenz auf den Klassen für diesen Lernabschnitt hat. Hat er diese Kompetenz
	 * nicht, so wird eine Exception geschmissen.
	 *
	 * @throws ApiOperationException   im Fehlerfall, wenn der Benutzer nicht alle Rechte zum Zugriff hat (503 - FORBIDDEN)
	 */
	private void checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt() throws ApiOperationException {
		if (checkBenutzerFunktionsbezogeneKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN,
				Set.of(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))) {
			final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
			if (dtoSchueler == null)
				throw new ApiOperationException(Status.NOT_FOUND,
						"Der Schüler mit der ID %d konnte in der Datenbank nicht gefunden werden.".formatted(idSchueler));
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList(
					"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr = 0",
					DTOSchuelerLernabschnittsdaten.class, dtoSchueler.ID, dtoSchueler.Schuljahresabschnitts_ID);
			if (lernabschnitte.size() != 1)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Für den Schüler mit der ID %d konnte kein eindeutiger aktueller Lernabschnitt bestimmt werden.".formatted(dtoSchueler.ID));
			checkBenutzerFunktionsbezogeneKompetenzKlasse(lernabschnitte.get(0).Klassen_ID);
		}
	}


	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der aktuellen Klasse des Schülers hat, um die Sprachprüfung zu erstellen
		checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt();
	}


	@Override
	public void checkBeforePatch(final DTOSchuelerSprachpruefungen dto, final Map<String, Object> patchAttributes) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der aktuellen Klasse des Schülers hat, um die Sprachprüfung zu verändern
		checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt();
		if (patchAttributes.get("sprache") == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Bei einem Patch für die Sprachprüfung muss ein Sprachkürzel angegeben werden.");
		final String patchSprache = JSONMapper.convertToString(patchAttributes.get("sprache"), false, false, 2);
		if (!patchSprache.equals(dto.Sprache))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Bei einem Patch für die Sprachprüfung muss das Sprachkürzel im Patch mit dem Sprachkürzel im DTO übereinstimmen.");
	}


	@Override
	public void checkBeforeDeletion(final List<DTOSchuelerSprachpruefungen> dtos) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der aktuellen Klasse des Schülers hat, um die Sprachprüfung zu löschen
		checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt();
	}


	@Override
	public List<Sprachpruefung> getList() throws ApiOperationException {
		// Überprüfe, ob die Schüler-ID gültig ist.
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		// Bestimme die Sprachprüfungen des Schülers
		final List<DTOSchuelerSprachpruefungen> dtos =
				conn.queryList(DTOSchuelerSprachpruefungen.QUERY_BY_SCHUELER_ID, DTOSchuelerSprachpruefungen.class, idSchueler);
		final List<Sprachpruefung> result = new ArrayList<>();
		for (final DTOSchuelerSprachpruefungen dto : dtos)
			result.add(map(dto));
		return result;
	}



	@Override
	public Sprachpruefung getById(final Long id) throws ApiOperationException {
		// Überprüfe, ob die Schüler-ID gültig ist.
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		// Hole die Sprachbelegung
		final DTOSchuelerSprachpruefungen belegung = conn.queryByKey(DTOSchuelerSprachpruefungen.class, id);
		if (belegung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Sprachprüfung mit der ID %d gefunden.".formatted(id));
		return map(belegung);
	}


	private DTOSchuelerSprachpruefungen getDTO(final @NotNull String kuerzel) throws ApiOperationException {
		if (kuerzel.isBlank())
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein gültiges Kürzel übergeben.");
		// Überprüfe, ob die Schüler-ID gültig ist.
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		// Bestimme die zugehörige Sprachprüfung
		final List<DTOSchuelerSprachpruefungen> belegungen = conn.queryList(
				"SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Schueler_ID = ?1 AND e.Sprache = ?2", DTOSchuelerSprachpruefungen.class, idSchueler,
				kuerzel);
		if (belegungen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Sprachprüfung mit dem Kürzel gefunden.");
		if (belegungen.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Es wurden mehrere Einträge zu dem Schüler mit der ID %d und der Sprache %s gefunden."
					.formatted(idSchueler, kuerzel));
		return belegungen.get(0);
	}

	/**
	 * Bestimmt die Sprachprüfung anhand des übergebenen Sprachkürzels und der Schüler-ID.
	 *
	 * @param kuerzel   das Sprach-Kürzel
	 *
	 * @return die Sprachprüfung
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private Sprachpruefung getByKuerzel(final @NotNull String kuerzel) throws ApiOperationException {
		return map(getDTO(kuerzel));
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
	public Response getByKuerzelAsResponse(final @NotNull String kuerzel) throws ApiOperationException {
		final Sprachpruefung daten = getByKuerzel(kuerzel);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Passt die Informationen der Sprachprüfung mit dem übergebenen Sprach-Kürzel des Schülers mithilfe des
	 * JSON-Patches aus dem übergebenen {@link InputStream} an.
	 *
	 * @param kuerzel   das Sprachkürzel
	 * @param is        der Input-Stream
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response patchByKuerzelAsResponse(final @NotNull String kuerzel, final InputStream is) throws ApiOperationException {
		final DTOSchuelerSprachpruefungen dto = getDTO(kuerzel);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es konnte keine Sprachprüfung zu dem Sprach-Kürzel %s bei dem Schüler mit der ID %d gefunden werden.".formatted(kuerzel, this.idSchueler));
		final Map<String, Object> attributesToPatch = JSONMapper.toMap(is);
		attributesToPatch.put("sprache", kuerzel);
		patch(dto.ID, attributesToPatch);
		return Response.status(Status.NO_CONTENT).build();
	}


	/**
	 * Löscht eine Sprachprüfung anhand des übergebenen Sprachkürzels und der Schüler-ID.
	 *
	 * @param kuerzel   das Kürzel der Sprache
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteByKuerzelAsResponse(final @NotNull String kuerzel) throws ApiOperationException {
		// Bestimme das DTO
		final DTOSchuelerSprachpruefungen dto = getDTO(kuerzel);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return deleteAsResponse(dto.ID);
	}

}
