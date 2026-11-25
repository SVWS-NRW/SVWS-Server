package de.svws_nrw.service.schueler.lernabschnitt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.types.fach.BilingualeSprache;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schueler.Betreuungsart;
import de.svws_nrw.asd.types.schueler.Versetzungsvermerk;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittBemerkungenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittsdatenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittsdatenMappingContext;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;
import de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt.FoerderschwerpunktRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.schuelerschwerpunkt.SchuelerschwerpunktRepository;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;
import de.svws_nrw.service.schule.EigeneSchuleService;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;


/**
 * Service für die Lernabschnittsdaten eines Schülers.
 */
public final class SchuelerLernabschnittService {

	private static final String DATEN_NOT_FOUND_BY_ID = "Keine Lernabschnittsdaten zur Abschnitt-ID %d gefunden.";

	/** Die Meldung für eine nicht existierende Referenz, z. B. "Es existiert kein Lehrer mit der ID 1." */
	private static final String REFERENZ_NOT_FOUND_BY_ID = "Es existiert %s mit der ID %d.";

	private final SchuelerLernabschnittRepository repository;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittBemerkungRepository bemerkungRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepo;
	private final LehrerRepository lehrerRepository;
	private final KlassenRepository klassenRepository;
	private final FoerderschwerpunktRepository foerderschwerpunktRepository;
	private final FachklasseRepository fachklasseRepository;
	private final JahrgangRepository jahrgangRepository;
	private final SchuelerschwerpunktRepository schuelerschwerpunktRepository;
	private final SchuelerLernabschnittsdatenMapper mapper;
	private final SchuelerLernabschnittBemerkungenMapper bemerkungenMapper;
	private final SchuelerLernabschnittMappingContextLoader kontextLoader;
	private final SchuelerLernabschnittEpJahre epJahre;
	private final EigeneSchuleService eigeneSchuleService;
	private final BenutzerKompetenzService benutzerKompetenzService;

	/**
	 * Erstellt einen neuen {@code SchuelerLernabschnittService}.
	 *
	 * @param repos                      die vom Service benötigten Repositories
	 * @param mapper                     der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @param bemerkungenMapper          der Mapper für den Patch der Bemerkungen
	 * @param kontextLoader              der Loader für die Zusatzdaten (Bemerkungen, Fachklassen, Leistungsdaten) beim Mapping
	 * @param epJahre                    die Regeln zu den EP-Jahren (Prüfung und Fortschreibung)
	 * @param eigeneSchuleService        der Service für die eigene Schule (liefert die Schulform)
	 * @param benutzerKompetenzService   der Service für die Benutzerkompetenzen
	 */
	public SchuelerLernabschnittService(
			final SchuelerLernabschnittsdatenRepos repos,
			final SchuelerLernabschnittsdatenMapper mapper,
			final SchuelerLernabschnittBemerkungenMapper bemerkungenMapper,
			final SchuelerLernabschnittMappingContextLoader kontextLoader,
			final SchuelerLernabschnittEpJahre epJahre,
			final EigeneSchuleService eigeneSchuleService,
			final BenutzerKompetenzService benutzerKompetenzService) {
		this.repository = repos.repository();
		this.schuelerRepository = repos.schuelerRepository();
		this.bemerkungRepository = repos.bemerkungRepository();
		this.schuljahresabschnitteRepo = repos.schuljahresabschnitteRepo();
		this.lehrerRepository = repos.lehrerRepository();
		this.klassenRepository = repos.klassenRepository();
		this.foerderschwerpunktRepository = repos.foerderschwerpunktRepository();
		this.fachklasseRepository = repos.fachklasseRepository();
		this.jahrgangRepository = repos.jahrgangRepository();
		this.schuelerschwerpunktRepository = repos.schuelerschwerpunktRepository();
		this.mapper = mapper;
		this.bemerkungenMapper = bemerkungenMapper;
		this.kontextLoader = kontextLoader;
		this.epJahre = epJahre;
		this.eigeneSchuleService = eigeneSchuleService;
		this.benutzerKompetenzService = benutzerKompetenzService;
	}

	/**
	 * Gibt die {@link SchuelerLernabschnittsdaten} inklusive Bemerkungen und Leistungsdaten anhand der ID des Lernabschnitts zurück.
	 *
	 * @param id   die ID des Lernabschnitts
	 *
	 * @return die {@link SchuelerLernabschnittsdaten} zu der ID
	 */
	public SchuelerLernabschnittsdaten get(final long id) {
		return toApi(loadLernabschnitt(id));
	}

	/**
	 * Gibt alle {@link SchuelerLernabschnittsdaten} eines Schülers in einem Schuljahresabschnitt zurück, d. h. alle Lernabschnitte
	 * unabhängig von der WechselNr, sortiert nach ihrer ID. Existieren keine Lernabschnitte oder existiert der Schüler nicht,
	 * so wird eine leere Liste zurückgegeben.
	 *
	 * @param idSchueler               die ID des Schülers
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die Liste der {@link SchuelerLernabschnittsdaten}, ggf. leer
	 */
	public List<SchuelerLernabschnittsdaten> getListBySchuelerAndSchuljahresabschnitt(final long idSchueler, final long idSchuljahresabschnitt) {
		final var entities = repository.findListBySchuelerAndSchuljahresabschnitt(idSchueler, idSchuljahresabschnitt);
		return toApi(entities, true);
	}

	/**
	 * Gibt alle {@link SchuelerLernabschnittsdaten} der Schüler mit den übergebenen IDs über alle Schuljahresabschnitte zurück,
	 * sortiert nach Schüler-ID, Schuljahresabschnitt und Wechsel-Nr. Die Bemerkungen sind enthalten, die Leistungsdaten nicht,
	 * da diese z. B. vom Reporting separat nachgeladen werden. Existieren keine Lernabschnitte, so wird eine leere Liste zurückgegeben.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Liste der {@link SchuelerLernabschnittsdaten} ohne Leistungsdaten, ggf. leer
	 */
	public List<SchuelerLernabschnittsdaten> getListBySchuelerIDsOhneLeistungsdaten(final Collection<Long> idsSchueler) {
		final var entities = repository.findListBySchuelerIDs(idsSchueler);
		return toApi(entities, false);
	}

	/**
	 * Erstellt einen neuen Lernabschnitt (Wechsel-Nr. 0) für den Schüler im angegebenen Schuljahresabschnitt. Alle weiteren
	 * Attribute des Requests sind optional, nicht angegebene Attribute werden mit ihren Standardwerten angelegt.
	 *
	 * @param dto   der Request mit den Daten des neuen Lernabschnitts
	 *
	 * @return der neu angelegte {@link SchuelerLernabschnittsdaten}
	 */
	public SchuelerLernabschnittsdaten create(final SchuelerLernabschnittCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			final int schuljahr = loadSchuljahr(dto.schuljahresabschnitt);
			checkKompetenz(dto);
			validateSchuelerOhneLernabschnitt(dto.schuelerID, dto.schuljahresabschnitt);
			final var schulform = eigeneSchuleService.getSchulform();
			validateCreateRequest(dto, schuljahr, schulform);
			final var neu = createLernabschnitt(dto, schuljahr, schulform);
			epJahre.propagate(neu, schulform, hasLaufbahnChange(dto));
			return toApi(neu);
		});
	}

	/**
	 * Aktualisiert die {@link SchuelerLernabschnittsdaten} zu der übergebenen ID teilweise (PATCH). Es werden nur
	 * die Felder übernommen, die im Request enthalten sind. Ein enthaltener Wert {@code null} entfernt den Wert.
	 *
	 * @param id    die ID des Lernabschnitts
	 * @param dto   der Patch-Request mit den zu ändernden Feldern
	 *
	 * @return die aktualisierten {@link SchuelerLernabschnittsdaten}
	 */
	public SchuelerLernabschnittsdaten patch(final long id, final SchuelerLernabschnittsdatenPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = loadLernabschnitt(id);
			checkKompetenz(dto, entity);
			final int schuljahr = loadSchuljahr(entity.Schuljahresabschnitts_ID);
			final var schulform = eigeneSchuleService.getSchulform();
			validatePatchRequest(dto, schuljahr, schulform, entity);
			applyPatch(dto, entity, schuljahr, schulform);
			epJahre.propagate(entity, schulform, hasLaufbahnChange(dto));
			return toApi(entity);
		});
	}

	/**
	 * Aktualisiert die Bemerkungen zu dem Lernabschnitt mit der übergebenen ID teilweise (PATCH). Die allgemeine
	 * Zeugnisbemerkung wird beim Lernabschnitt gespeichert, die weiteren Bemerkungen in den Fach-Bemerkungen.
	 * Existiert noch kein Datensatz mit Fach-Bemerkungen, so wird dieser angelegt, sofern der Patch eine dieser
	 * Bemerkungen enthält. Ein leerer Patch ändert nichts.
	 *
	 * @param id    die ID des Lernabschnitts
	 * @param dto   der Patch-Request mit den zu ändernden Bemerkungen
	 */
	public void patchBemerkungen(final long id, final SchuelerLernabschnittBemerkungenPatchRequest dto) {
		TransactionSupport.transactional(() -> {
			final var entity = loadLernabschnitt(id);
			checkKompetenz(entity);
			bemerkungenMapper.patchBemerkungen(dto, entity);
			if (containsFachBemerkungen(dto)) {
				patchFachBemerkungen(id, dto);
			}
		});
	}

	private DTOSchuelerLernabschnittsdaten loadLernabschnitt(final long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, DATEN_NOT_FOUND_BY_ID.formatted(id)));
	}

	private DTOSchuelerLernabschnittsdaten createLernabschnitt(final SchuelerLernabschnittCreateRequest dto, final int schuljahr,
			final Schulform schulform) {
		final var entity = mapper.toDomain(dto, schuljahr, schulform);
		validateAndResolveJahrgang(entity, isKlasseOderJahrgangGesetzt(dto), dto.jahrgangID != null);
		return repository.create(entity);
	}

	private void applyPatch(final SchuelerLernabschnittsdatenPatchRequest dto, final DTOSchuelerLernabschnittsdaten entity,
			final int schuljahr, final Schulform schulform) {
		mapper.patch(dto, entity, schuljahr, schulform);
		validateAndResolveJahrgang(entity, isKlasseOderJahrgangGesetzt(dto), dto.jahrgangID.isPresent());
	}

	private void checkKompetenz(final SchuelerLernabschnittCreateRequest dto) {
		benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(dto.klassenID);
	}

	private void checkKompetenz(final SchuelerLernabschnittsdatenPatchRequest dto, final DTOSchuelerLernabschnittsdaten entity) {
		if (dto.klassenID.isPresent() && (dto.klassenID.get() != null)) {
			benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(entity.Klassen_ID, dto.klassenID.get());
		} else {
			checkKompetenz(entity);
		}
	}

	private void checkKompetenz(final DTOSchuelerLernabschnittsdaten entity) {
		benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(entity.Klassen_ID);
	}

	private static boolean containsFachBemerkungen(final SchuelerLernabschnittBemerkungenPatchRequest dto) {
		return dto.zeugnisASV.isPresent()
				|| dto.zeugnisLELS.isPresent()
				|| dto.zeugnisAUE.isPresent()
				|| dto.uebergangESF.isPresent()
				|| dto.foerderschwerpunkt.isPresent()
				|| dto.versetzungsentscheidung.isPresent();
	}

	private void patchFachBemerkungen(final long idLernabschnitt, final SchuelerLernabschnittBemerkungenPatchRequest dto) {
		final var vorhanden = bemerkungRepository.findMapByLernabschnittID(List.of(idLernabschnitt)).get(idLernabschnitt);
		if (vorhanden == null) {
			createFachBemerkungen(idLernabschnitt, dto);
		} else {
			bemerkungenMapper.patchFachBemerkungen(dto, vorhanden);
		}
	}

	private void createFachBemerkungen(final long idLernabschnitt, final SchuelerLernabschnittBemerkungenPatchRequest dto) {
		final var neu = new DTOSchuelerPSFachBemerkungen(-1, idLernabschnitt);
		bemerkungenMapper.patchFachBemerkungen(dto, neu);
		bemerkungRepository.create(neu);
	}

	// Request-spezifische Einstiege: lesen nur aus, welche Felder gesetzt sind, und delegieren an die gemeinsamen Prüfungen

	private void validateCreateRequest(final SchuelerLernabschnittCreateRequest dto, final int schuljahr, final Schulform schulform) {
		validateReferenzen(dto, schuljahr, schulform);
		validateKlasseImSchuljahresabschnitt(dto.klassenID, dto.schuljahresabschnitt);
		validateEpJahreAenderbar(dto, schulform);
		validateDatumEndeNichtVorAnfang(dto.datumAnfang, dto.datumEnde);
		validateFehlstundenUnentschuldigtNichtUeberGesamt(dto.fehlstundenGesamt, dto.fehlstundenUnentschuldigt);
	}

	private void validateEpJahreAenderbar(final SchuelerLernabschnittCreateRequest dto, final Schulform schulform) {
		if (dto.idEpJahre != null) {
			epJahre.validateAenderbar(dto.idEpJahre, null, schulform, () -> getASDJahrgang(dto.jahrgangID));
		}
	}

	private void validateReferenzen(final SchuelerLernabschnittCreateRequest dto, final int schuljahr, final Schulform schulform) {
		validateIdLehrer(dto.tutorID);
		validateIdLehrer(dto.sonderpaedagogeID);
		validateIdKlasse(dto.folgeklassenID);
		validateIdKlasse(dto.klassenID);
		validateIdFoerderschwerpunkt(dto.foerderschwerpunkt1ID);
		validateIdFoerderschwerpunkt(dto.foerderschwerpunkt2ID);
		validateIdSchuelerschwerpunkt(dto.schwerpunktID);
		validateIdKlassenart(dto.idKlassenart);
		validateIdBetreuung(dto.idBetreuung);
		validateIdSchulgliederung(dto.idSchulgliederung);
		validateIdOrganisationsform(dto.idOrganisationsform, schulform);
		validateIdEpJahre(dto.idEpJahre);
		validateIdFachklasse(dto.fachklasseID);
		validateIdVersetzungsvermerk(dto.idVersetzungsvermerk, schuljahr, schulform);
		validateBilingualerZweig(dto.bilingualerZweig, schuljahr, schulform);
		validateIdJahrgang(dto.jahrgangID);
	}

	private void validatePatchRequest(final SchuelerLernabschnittsdatenPatchRequest dto, final int schuljahr, final Schulform schulform,
			final DTOSchuelerLernabschnittsdaten entity) {
		validateReferenzen(dto, schuljahr, schulform);
		dto.klassenID.ifPresent(idKlasse -> validateKlasseImSchuljahresabschnitt(idKlasse, entity.Schuljahresabschnitts_ID));
		validateEpJahreAenderbar(dto, schulform, entity);
		validateDatum(dto, entity);
		validateFehlstunden(dto, entity);
	}

	private void validateEpJahreAenderbar(final SchuelerLernabschnittsdatenPatchRequest dto, final Schulform schulform,
			final DTOSchuelerLernabschnittsdaten entity) {
		dto.idEpJahre.ifPresent(idEpJahre -> epJahre.validateAenderbar(idEpJahre, entity.EPJahre, schulform,
				() -> getASDJahrgangNachPatch(dto, entity)));
	}

	private static void validateDatum(final SchuelerLernabschnittsdatenPatchRequest dto, final DTOSchuelerLernabschnittsdaten entity) {
		if (dto.datumAnfang.isPresent() || dto.datumEnde.isPresent()) {
			validateDatumEndeNichtVorAnfang(dto.datumAnfang.orElse(entity.DatumVon), dto.datumEnde.orElse(entity.DatumBis));
		}
	}

	private static void validateFehlstunden(final SchuelerLernabschnittsdatenPatchRequest dto, final DTOSchuelerLernabschnittsdaten entity) {
		if (dto.fehlstundenGesamt.isPresent() || dto.fehlstundenUnentschuldigt.isPresent()) {
			validateFehlstundenUnentschuldigtNichtUeberGesamt(dto.fehlstundenGesamt.orElse(entity.SumFehlStd),
					dto.fehlstundenUnentschuldigt.orElse(entity.SumFehlStdU));
		}
	}

	private void validateReferenzen(final SchuelerLernabschnittsdatenPatchRequest dto, final int schuljahr, final Schulform schulform) {
		dto.tutorID.ifPresent(this::validateIdLehrer);
		dto.sonderpaedagogeID.ifPresent(this::validateIdLehrer);
		dto.folgeklassenID.ifPresent(this::validateIdKlasse);
		dto.klassenID.ifPresent(this::validateIdKlasse);
		dto.foerderschwerpunkt1ID.ifPresent(this::validateIdFoerderschwerpunkt);
		dto.foerderschwerpunkt2ID.ifPresent(this::validateIdFoerderschwerpunkt);
		dto.schwerpunktID.ifPresent(this::validateIdSchuelerschwerpunkt);
		dto.idKlassenart.ifPresent(SchuelerLernabschnittService::validateIdKlassenart);
		dto.idBetreuung.ifPresent(SchuelerLernabschnittService::validateIdBetreuung);
		dto.idSchulgliederung.ifPresent(SchuelerLernabschnittService::validateIdSchulgliederung);
		dto.idOrganisationsform.ifPresent(id -> validateIdOrganisationsform(id, schulform));
		dto.idEpJahre.ifPresent(SchuelerLernabschnittService::validateIdEpJahre);
		dto.fachklasseID.ifPresent(this::validateIdFachklasse);
		dto.idVersetzungsvermerk.ifPresent(id -> validateIdVersetzungsvermerk(id, schuljahr, schulform));
		dto.bilingualerZweig.ifPresent(kuerzel -> validateBilingualerZweig(kuerzel, schuljahr, schulform));
		dto.jahrgangID.ifPresent(this::validateIdJahrgang);
	}

	private String getASDJahrgangNachPatch(final SchuelerLernabschnittsdatenPatchRequest dto, final DTOSchuelerLernabschnittsdaten entity) {
		return dto.jahrgangID.isPresent() ? getASDJahrgang(dto.jahrgangID.get()) : entity.ASDJahrgang;
	}

	private static boolean isKlasseOderJahrgangGesetzt(final SchuelerLernabschnittCreateRequest dto) {
		return (dto.klassenID != null) || (dto.jahrgangID != null);
	}

	private static boolean isKlasseOderJahrgangGesetzt(final SchuelerLernabschnittsdatenPatchRequest dto) {
		return dto.klassenID.isPresent() || dto.jahrgangID.isPresent();
	}

	private static boolean hasLaufbahnChange(final SchuelerLernabschnittCreateRequest dto) {
		return (dto.idEpJahre != null) || (dto.jahrgangID != null);
	}

	private static boolean hasLaufbahnChange(final SchuelerLernabschnittsdatenPatchRequest dto) {
		return dto.idEpJahre.isPresent() || dto.jahrgangID.isPresent();
	}

	// Gemeinsame Prüfungen für Create und Patch

	private void validateSchuelerOhneLernabschnitt(final Long idSchueler, final Long idSchuljahresabschnitt) {
		validateSchuelerExists(idSchueler);
		validateKeinLernabschnittVorhanden(idSchueler, idSchuljahresabschnitt);
	}

	private void validateSchuelerExists(final Long idSchueler) {
		if (!schuelerRepository.existsById(idSchueler)) {
			throw new ApiOperationException(Status.BAD_REQUEST, REFERENZ_NOT_FOUND_BY_ID.formatted("kein Schüler", idSchueler));
		}
	}

	private void validateKeinLernabschnittVorhanden(final long idSchueler, final long idSchuljahresabschnitt) {
		// Das Anlegen vergibt immer die Wechsel-Nr. 0, der Unique-Index auf Schüler, Abschnitt und Wechsel-Nr. würde sonst verletzt
		final boolean vorhanden = repository.findListBySchuelerAndSchuljahresabschnitt(idSchueler, idSchuljahresabschnitt).stream()
				.anyMatch(lernabschnitt -> (lernabschnitt.WechselNr != null) && (lernabschnitt.WechselNr == 0));
		if (vorhanden) {
			throw new ApiOperationException(Status.CONFLICT,
					"Der Schüler mit der ID %d hat im Schuljahresabschnitt mit der ID %d bereits einen Lernabschnitt.".formatted(idSchueler, idSchuljahresabschnitt));
		}
	}

	private void validateKlasseImSchuljahresabschnitt(final Long idKlasse, final Long idSchuljahresabschnitt) {
		if (idKlasse == null) {
			return;
		}
		klassenRepository.findById(idKlasse)
				.filter(klasse -> klasse.Schuljahresabschnitts_ID != idSchuljahresabschnitt)
				.ifPresent(klasse -> {
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die Klasse mit der ID %d gehört nicht zum Schuljahresabschnitt des Lernabschnitts.".formatted(idKlasse));
				});
	}

	/**
	 * Prüft nach der Anwendung des Requests, ob Klasse und Jahrgang zueinander passen, und übernimmt bei einem geänderten
	 * Jahrgang dessen ASD-Jahrgang in den Lernabschnitt.
	 *
	 * @param entity                       der Lernabschnitt nach der Anwendung des Requests
	 * @param isKlasseOderJahrgangGesetzt  gibt an, ob der Request die Klasse oder den Jahrgang enthält
	 * @param isJahrgangGesetzt            gibt an, ob der Request den Jahrgang enthält
	 */
	private void validateAndResolveJahrgang(final DTOSchuelerLernabschnittsdaten entity, final boolean isKlasseOderJahrgangGesetzt,
			final boolean isJahrgangGesetzt) {
		if (isKlasseOderJahrgangGesetzt) {
			validateKlasseUndJahrgangKompatibel(entity);
		}
		if (isJahrgangGesetzt) {
			entity.ASDJahrgang = getASDJahrgang(entity.Jahrgang_ID);
		}
	}

	private void validateKlasseUndJahrgangKompatibel(final DTOSchuelerLernabschnittsdaten entity) {
		if ((entity.Klassen_ID == null) || (entity.Jahrgang_ID == null)) {
			return;
		}
		final var klasse = klassenRepository.findById(entity.Klassen_ID).orElse(null);
		if ((klasse == null) || (klasse.Jahrgang_ID == null)) {
			return;
		}
		// Derselbe Jahrgang ist immer kompatibel, dafür muss der Katalog nicht befragt werden
		if (Objects.equals(klasse.Jahrgang_ID, entity.Jahrgang_ID)) {
			return;
		}
		final var asdJahrgangKlasse = getASDJahrgang(klasse.Jahrgang_ID);
		final var asdJahrgangLernabschnitt = getASDJahrgang(entity.Jahrgang_ID);
		if (!Objects.equals(asdJahrgangKlasse, asdJahrgangLernabschnitt)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Jahrgang mit der ID %d ist für die Klasse mit der ID %d nicht zulässig.".formatted(entity.Jahrgang_ID, entity.Klassen_ID));
		}
	}

	private String getASDJahrgang(final Long idJahrgang) {
		if (idJahrgang == null) {
			return null;
		}
		return jahrgangRepository.findById(idJahrgang)
				.map(jahrgang -> jahrgang.ASDJahrgang)
				.orElse(null);
	}

	private void validateIdLehrer(final Long idLehrer) {
		validateExists(idLehrer, lehrerRepository::existsById, "kein Lehrer");
	}

	private void validateIdKlasse(final Long idKlasse) {
		validateExists(idKlasse, klassenRepository::existsById, "keine Klasse");
	}

	private void validateIdFoerderschwerpunkt(final Long idFoerderschwerpunkt) {
		validateExists(idFoerderschwerpunkt, foerderschwerpunktRepository::existsById, "kein Förderschwerpunkt");
	}

	private void validateIdSchuelerschwerpunkt(final Long idSchuelerschwerpunkt) {
		validateExists(idSchuelerschwerpunkt, schuelerschwerpunktRepository::existsById, "kein Schülerschwerpunkt");
	}

	private void validateIdFachklasse(final Long idFachklasse) {
		validateExists(idFachklasse, fachklasseRepository::existsById, "keine Fachklasse");
	}

	private void validateIdJahrgang(final Long idJahrgang) {
		validateExists(idJahrgang, jahrgangRepository::existsById, "kein Jahrgang");
	}

	private static void validateIdKlassenart(final Long idKlassenart) {
		validateKatalogEintrag(idKlassenart, id -> Klassenart.data().getEintragByID(id), "keine gültige Klassenart");
	}

	private static void validateIdBetreuung(final Long idBetreuung) {
		validateKatalogEintrag(idBetreuung, id -> Betreuungsart.data().getEintragByID(id), "keine gültige Betreuungsart");
	}

	private static void validateIdSchulgliederung(final Long idSchulgliederung) {
		validateKatalogEintrag(idSchulgliederung, id -> Schulgliederung.data().getEintragByID(id), "keine gültige Schulgliederung");
	}

	private static void validateIdEpJahre(final Long idEpJahre) {
		validateKatalogEintrag(idEpJahre, id -> PrimarstufeSchuleingangsphaseBesuchsjahre.data().getEintragByID(id),
				"keine gültige Anzahl an Besuchsjahren in der Schuleingangsphase");
	}

	/**
	 * Prüft, ob zu der ID ein Datensatz existiert. Bei der ID {@code null} gibt es nichts zu prüfen.
	 *
	 * @param id            die zu prüfende ID, ggf. {@code null}
	 * @param existsById    die Prüfung auf Existenz, i. d. R. die Methode {@code existsById} des Repositories
	 * @param bezeichnung   die Bezeichnung für die Fehlermeldung, z. B. "kein Lehrer"
	 */
	private static void validateExists(final Long id, final LongPredicate existsById, final String bezeichnung) {
		if ((id != null) && !existsById.test(id)) {
			throw new ApiOperationException(Status.BAD_REQUEST, REFERENZ_NOT_FOUND_BY_ID.formatted(bezeichnung, id));
		}
	}

	/**
	 * Prüft, ob zu der ID ein Eintrag im Core-Type-Katalog existiert. Bei der ID {@code null} gibt es nichts zu prüfen.
	 *
	 * @param id               die zu prüfende ID, ggf. {@code null}
	 * @param getEintragByID   liefert den Katalogeintrag zu der ID oder {@code null}
	 * @param bezeichnung      die Bezeichnung für die Fehlermeldung, z. B. "keine gültige Klassenart"
	 */
	private static void validateKatalogEintrag(final Long id, final LongFunction<?> getEintragByID, final String bezeichnung) {
		if ((id != null) && (getEintragByID.apply(id) == null)) {
			throw new ApiOperationException(Status.BAD_REQUEST, REFERENZ_NOT_FOUND_BY_ID.formatted(bezeichnung, id));
		}
	}

	private static void validateIdOrganisationsform(final Long idOrganisationsform, final Schulform schulform) {
		if (idOrganisationsform == null) {
			return;
		}
		final var organisationsform = SchuelerLernabschnittsdatenMapper.getOrganisationsformKatalog(schulform).getEintragByID(idOrganisationsform);
		if (organisationsform == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert keine gültige Organisationsform mit der ID %d für die Schulform %s.".formatted(idOrganisationsform, schulform.name()));
		}
	}

	private static void validateIdVersetzungsvermerk(final Long idVersetzungsvermerk, final int schuljahr, final Schulform schulform) {
		if (idVersetzungsvermerk == null) {
			return;
		}
		final var versetzungsvermerk = Versetzungsvermerk.data().getWertByIDOrNull(idVersetzungsvermerk);
		if ((versetzungsvermerk == null) || !(Versetzungsvermerk.data().hatSchulform(schuljahr, schulform, versetzungsvermerk))) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert kein gültiger Versetzungsvermerk mit der ID %d im Schuljahr %d für die Schulform %s."
							.formatted(idVersetzungsvermerk, schuljahr, schulform.name()));
		}
	}

	private static void validateBilingualerZweig(final String kuerzel, final int schuljahr, final Schulform schulform) {
		if (kuerzel == null) {
			return;
		}
		final var bilingualerZweig = BilingualeSprache.data().getWertByKuerzel(kuerzel);
		if ((bilingualerZweig == null) || !(BilingualeSprache.data().hatSchulform(schuljahr, schulform, bilingualerZweig))) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert kein gültiger bilingualer Zweig mit dem Kürzel %s im Schuljahr %d für die Schulform %s."
							.formatted(kuerzel, schuljahr, schulform.name()));
		}
	}

	private static void validateDatumEndeNichtVorAnfang(final String datumVon, final String datumBis) {
		if (StringUtils.isNotBlank(datumVon) && StringUtils.isNotBlank(datumBis) && (datumBis.compareTo(datumVon) < 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Enddatum %s darf nicht vor dem Anfangsdatum %s liegen.".formatted(datumBis, datumVon));
		}
	}

	private static void validateFehlstundenUnentschuldigtNichtUeberGesamt(final Integer fehlstundenGesamt, final Integer fehlstundenUnentschuldigt) {
		if ((fehlstundenGesamt != null) && (fehlstundenUnentschuldigt != null) && (fehlstundenUnentschuldigt > fehlstundenGesamt)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die unentschuldigten Fehlstunden (%d) dürfen die gesamten Fehlstunden (%d) nicht übersteigen."
							.formatted(fehlstundenUnentschuldigt, fehlstundenGesamt));
		}
	}

	// Schuljahr und Mapping ins API-Modell

	private int loadSchuljahr(final long idSchuljahresabschnitt) {
		return loadSchuljahrByIdAbschnitt(List.of(idSchuljahresabschnitt)).get(idSchuljahresabschnitt);
	}

	/**
	 * Ermittelt zu den übergebenen Schuljahresabschnitt-IDs das jeweilige Schuljahr. Alle Schuljahresabschnitte müssen existieren.
	 *
	 * @param idsSchuljahresabschnitte   die IDs der Schuljahresabschnitte (Duplikate sind erlaubt)
	 *
	 * @return die Map von der Schuljahresabschnitt-ID auf das Schuljahr (z. B. 2024)
	 */
	private Map<Long, Integer> loadSchuljahrByIdAbschnitt(final List<Long> idsSchuljahresabschnitte) {
		if (CollectionUtils.isEmpty(idsSchuljahresabschnitte)) {
			return Collections.emptyMap();
		}

		final var uniqueIds = idsSchuljahresabschnitte.stream()
				.distinct()
				.toList();
		final var schuljahrByIdAbschnitt = schuljahresabschnitteRepo.findListByIds(uniqueIds).stream()
				.collect(Collectors.toMap(
						abschnitt -> abschnitt.ID,
						abschnitt -> abschnitt.Jahr
				));
		validateAllAbschnitteFound(uniqueIds, schuljahrByIdAbschnitt);
		return schuljahrByIdAbschnitt;
	}

	private static void validateAllAbschnitteFound(final List<Long> uniqueIds, final Map<Long, Integer> schuljahrByIdAbschnitt) {
		final var missing = uniqueIds.stream()
				.filter(id -> !schuljahrByIdAbschnitt.containsKey(id))
				.toList();
		if (!missing.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine Schuljahresabschnitte gefunden für IDs: " + missing);
		}
	}

	private SchuelerLernabschnittsdaten toApi(final DTOSchuelerLernabschnittsdaten entity) {
		return toApi(List.of(entity), true).getFirst();
	}

	/**
	 * Wandelt eine Liste von Entities in API-Modelle um. Schuljahre, Schulform und Zusatzdaten werden dabei gebündelt
	 * für alle Entities geladen, um einzelne Datenbankabfragen pro Lernabschnitt zu vermeiden.
	 *
	 * @param entities            die Entities der Lernabschnitte
	 * @param mitLeistungsdaten   gibt an, ob die Leistungsdaten geladen werden sollen
	 *
	 * @return die API-Modelle in der Reihenfolge der Entities, leer bei leerer Entity-Liste
	 */
	private List<SchuelerLernabschnittsdaten> toApi(final List<DTOSchuelerLernabschnittsdaten> entities, final boolean mitLeistungsdaten) {
		if (entities.isEmpty()) {
			return List.of();
		}

		final var idsSchuljahresabschnitte = entities.stream()
				.map(entity -> entity.Schuljahresabschnitts_ID)
				.toList();
		final var schuljahrByIdAbschnitt = loadSchuljahrByIdAbschnitt(idsSchuljahresabschnitte);
		final var schulform = eigeneSchuleService.getSchulform();
		final var kontextById = kontextLoader.load(entities, schuljahrByIdAbschnitt, mitLeistungsdaten);

		return entities.stream()
				.map(entity -> toApi(entity, schuljahrByIdAbschnitt, schulform, kontextById))
				.toList();
	}

	private SchuelerLernabschnittsdaten toApi(final DTOSchuelerLernabschnittsdaten entity, final Map<Long, Integer> schuljahrByIdAbschnitt,
			final Schulform schulform, final Map<Long, SchuelerLernabschnittsdatenMappingContext> kontextById) {
		final var schuljahr = schuljahrByIdAbschnitt.get(entity.Schuljahresabschnitts_ID);
		final var kontext = kontextById.getOrDefault(entity.ID, SchuelerLernabschnittsdatenMappingContext.empty());
		return mapper.toApi(entity, schuljahr, schulform, kontext);
	}

}
