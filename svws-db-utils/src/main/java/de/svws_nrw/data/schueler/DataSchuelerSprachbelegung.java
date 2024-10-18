package de.svws_nrw.data.schueler;

import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.asd.data.fach.SprachreferenzniveauKatalogEintrag;
import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.fach.Sprachreferenzniveau;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link Sprachbelegung}.
 */
public final class DataSchuelerSprachbelegung extends DataManagerRevised<Long, DTOSchuelerSprachenfolge, Sprachbelegung> {

	private final long idSchueler;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Sprachbelegung}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler   die ID des Schülers
	 */
	public DataSchuelerSprachbelegung(final DBEntityManager conn, final long idSchueler) {
		super(conn);
		setAttributesRequiredOnCreation("sprache");
		this.idSchueler = idSchueler;
	}


	@Override
	protected void initDTO(final DTOSchuelerSprachenfolge dto, final Long newId, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newId;
		dto.Schueler_ID = idSchueler;
	}


	@Override
	public Sprachbelegung map(final DTOSchuelerSprachenfolge dto) throws ApiOperationException {
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, dto.Schueler_ID);
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoSchueler.Schuljahresabschnitts_ID);
		final Sprachbelegung daten = new Sprachbelegung();
		daten.sprache = dto.Sprache;
		daten.reihenfolge = dto.ReihenfolgeNr;
		daten.belegungVonJahrgang = dto.ASDJahrgangVon;
		daten.belegungVonAbschnitt = dto.AbschnittVon;
		daten.belegungBisJahrgang = dto.ASDJahrgangBis;
		daten.belegungBisAbschnitt = dto.AbschnittBis;
		final Sprachreferenzniveau niveau = (dto.Referenzniveau == null) ? null : Sprachreferenzniveau.data().getWertByKuerzel(dto.Referenzniveau);
		final SprachreferenzniveauKatalogEintrag niveauEintrag = (niveau == null) ? null : niveau.daten(abschnitt.schuljahr);
		daten.referenzniveau = (niveauEintrag == null) ? null : niveauEintrag.kuerzel;
		daten.hatKleinesLatinum = (dto.KleinesLatinumErreicht != null) && dto.KleinesLatinumErreicht;
		daten.hatLatinum = (dto.LatinumErreicht != null) && dto.LatinumErreicht;
		daten.hatGraecum = (dto.GraecumErreicht != null) && dto.GraecumErreicht;
		daten.hatHebraicum = (dto.HebraicumErreicht != null) && dto.HebraicumErreicht;
		return daten;
	}


	@Override
	protected void mapAttribute(final DTOSchuelerSprachenfolge dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, dto.Schueler_ID);
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoSchueler.Schuljahresabschnitts_ID);
		switch (name) {
			case "sprache" -> {
				final String patchSprache = JSONMapper.convertToString(value, false, false, 2);
				if ((patchSprache == null) || (patchSprache.isBlank()))
					throw new ApiOperationException(Status.BAD_REQUEST, "Ein Patch darf kein leeres Sprachkürzel beinhalten");
				// TODO Prüfe, ob es sich um ein gültiges Sprachkürzel handelt.
				dto.Sprache = patchSprache;
			}
			case "reihenfolge" -> dto.ReihenfolgeNr = JSONMapper.convertToIntegerInRange(value, true, 0, 9);
			case "belegungVonJahrgang" -> {
				final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
				if (kuerzel == null) {
					dto.ASDJahrgangVon = null;
				} else {
					final Jahrgaenge jg = Jahrgaenge.data().getWertByKuerzel(kuerzel);
					if (jg == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Das Jahrgangs-Kürzel %s ist ungültig.".formatted(kuerzel));
					final JahrgaengeKatalogEintrag jgke = jg.daten(abschnitt.schuljahr);
					if (jgke == null)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Das Jahrgangs-Kürzel %s ist dem Schuljahr %d ungültig.".formatted(kuerzel, abschnitt.schuljahr));
					dto.ASDJahrgangVon = jgke.kuerzel;
				}
			}
			case "belegungVonAbschnitt" -> {
				final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
				if (schule == null)
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Daten der Schule konnten nicht bestimmt werden.");
				dto.AbschnittVon = JSONMapper.convertToIntegerInRange(value, true, 1, ((schule.AnzahlAbschnitte == null) ? 2 : schule.AnzahlAbschnitte) + 1);
			}
			case "belegungBisJahrgang" -> {
				final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
				if (kuerzel == null) {
					dto.ASDJahrgangBis = null;
				} else {
					final Jahrgaenge jg = Jahrgaenge.data().getWertByKuerzel(kuerzel);
					if (jg == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Das Jahrgangs-Kürzel %s ist ungültig.".formatted(kuerzel));
					final JahrgaengeKatalogEintrag jgke = jg.daten(abschnitt.schuljahr);
					if (jgke == null)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Das Jahrgangs-Kürzel %s ist dem Schuljahr %d ungültig.".formatted(kuerzel, abschnitt.schuljahr));
					dto.ASDJahrgangBis = jgke.kuerzel;
				}
			}
			case "belegungBisAbschnitt" -> {
				final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
				if (schule == null)
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Daten der Schule konnten nicht bestimmt werden.");
				dto.AbschnittBis = JSONMapper.convertToIntegerInRange(value, true, 1, ((schule.AnzahlAbschnitte == null) ? 2 : schule.AnzahlAbschnitte) + 1);
			}
			case "referenzniveau" -> {
				final String kuerzel = JSONMapper.convertToString(value, true, false, 10);
				if (kuerzel == null) {
					dto.Referenzniveau = null;
				} else {
					final Sprachreferenzniveau niveau = Sprachreferenzniveau.data().getWertByKuerzel(kuerzel);
					if (niveau == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Das Sprachreferenzniveau-Kürzel %s ist ungültig.".formatted(kuerzel));
					final SprachreferenzniveauKatalogEintrag niveauEintrag = niveau.daten(abschnitt.schuljahr);
					if (niveauEintrag == null)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Das Sprachreferenzniveau-Kürzel %s ist dem Schuljahr %d ungültig.".formatted(kuerzel, abschnitt.schuljahr));
					dto.Referenzniveau = niveauEintrag.kuerzel;
				}
			}
			case "hatKleinesLatinum" -> dto.KleinesLatinumErreicht = JSONMapper.convertToBoolean(value, false);
			case "hatLatinum" -> dto.LatinumErreicht = JSONMapper.convertToBoolean(value, false);
			case "hatGraecum" -> dto.GraecumErreicht = JSONMapper.convertToBoolean(value, false);
			case "hatHebraicum" -> dto.HebraicumErreicht = JSONMapper.convertToBoolean(value, false);
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
		if (hatBenutzerNurFunktionsbezogeneKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN,
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
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der aktuellen Klasse des Schülers hat, um die Sprachbelegung zu erstellen
		checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt();
	}


	@Override
	public void checkBeforePatch(final DTOSchuelerSprachenfolge dto, final Map<String, Object> patchAttributes) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der aktuellen Klasse des Schülers hat, um die Sprachbelegung zu verändern
		checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt();
		if (patchAttributes.get("sprache") == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Bei einem Patch für die Sprachbelegung muss ein Sprachkürzel angegeben werden.");
		final String patchSprache = JSONMapper.convertToString(patchAttributes.get("sprache"), false, false, 2);
		if (!patchSprache.equals(dto.Sprache))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Bei einem Patch für die Sprachbelegung muss das Sprachkürzel im Patch mit dem Sprachkürzel im DTO übereinstimmen.");
	}


	@Override
	public void checkBeforeDeletion(final List<DTOSchuelerSprachenfolge> dtos) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der aktuellen Klasse des Schülers hat, um die Sprachbelegung zu löschen
		checkFunktionsbezogeneKompetenzAufAktuellenLernabschnitt();
	}


	@Override
	public List<Sprachbelegung> getList() throws ApiOperationException {
		// Überprüfe, ob die Schüler-ID gültig ist.
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		// Bestimme die Sprachbelegungen des Schülers
		final List<DTOSchuelerSprachenfolge> dtos = conn.queryList(DTOSchuelerSprachenfolge.QUERY_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, idSchueler);
		final List<Sprachbelegung> result = new ArrayList<>();
		for (final DTOSchuelerSprachenfolge dto : dtos)
			result.add(map(dto));
		return result;
	}


	/**
	 * Gibt die Liste der Sprachbelegungen des Schülers zurück. Im Fehlerfall ist diese Liste leer.
	 *
	 * @return Die Liste der Sprachbelegungen des Schülers
	 */
	public List<Sprachbelegung> getListSprachbelegungen() {
		try {
			return this.getList();
		} catch (@SuppressWarnings("unused") final ApiOperationException e) {
			return new ArrayList<>();
		}
	}


	private DTOSchuelerSprachenfolge getDTO(final @NotNull String kuerzel) throws ApiOperationException {
		if (kuerzel.isBlank())
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein gültiges Kürzel übergeben.");
		// Überprüfe, ob die Schüler-ID gültig ist.
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		// Bestimme die zugehörige Sprachbelegung
		final List<DTOSchuelerSprachenfolge> belegungen = conn.queryList("SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID = ?1 AND e.Sprache = ?2",
				DTOSchuelerSprachenfolge.class, idSchueler, kuerzel);
		if (belegungen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Sprachbelegung mit dem Kürzel %s gefunden.".formatted(kuerzel));
		if (belegungen.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Es wurden mehrere Einträge zu dem Schüler mit der ID %d und der Sprache %s gefunden."
					.formatted(idSchueler, kuerzel));
		return belegungen.get(0);
	}


	@Override
	public Sprachbelegung getById(final Long id) throws ApiOperationException {
		// Überprüfe, ob die Schüler-ID gültig ist.
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
		// Hole die Sprachbelegung
		final DTOSchuelerSprachenfolge belegung = conn.queryByKey(DTOSchuelerSprachenfolge.class, id);
		if (belegung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Sprachbelegung mit der ID %d gefunden.".formatted(id));
		return map(belegung);
	}


	/**
	 * Bestimmt die Sprachbelegung anhand des übergebenen Sprachkürzels und der Schüler-ID.
	 *
	 * @param kuerzel   das Sprach-Kürzel
	 *
	 * @return die Sprachbelegung
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Sprachbelegung getByKuerzel(final @NotNull String kuerzel) throws ApiOperationException {
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
		final Sprachbelegung daten = getByKuerzel(kuerzel);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
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
	public Response patchByKuerzelAsResponse(final @NotNull String kuerzel, final InputStream is) throws ApiOperationException {
		final DTOSchuelerSprachenfolge dto = getDTO(kuerzel);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es konnte keine Sprachbelegung zu dem Sprach-Kürzel %s bei dem Schüler mit der ID %d gefunden werden.".formatted(kuerzel,
							this.idSchueler));
		final Map<String, Object> attributesToPatch = JSONMapper.toMap(is);
		attributesToPatch.put("sprache", kuerzel);
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
	public Response deleteByKuerzelAsResponse(final @NotNull String kuerzel) throws ApiOperationException {
		// Bestimme das DTO
		final DTOSchuelerSprachenfolge dto = getDTO(kuerzel);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return deleteAsResponse(dto.ID);
	}

}
