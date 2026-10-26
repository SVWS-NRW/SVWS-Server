package de.svws_nrw.service.schueler.lernabschnitt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.types.fach.BilingualeSprache;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schueler.Versetzungsvermerk;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.leistungsdaten.SchuelerLeistungsdatenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittBemerkungenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittsdatenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittsdatenMappingContext;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
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

	private final SchuelerLernabschnittRepository repository;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittBemerkungRepository bemerkungRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepo;
	private final SchuelerLeistungsdatenRepository leistungsdatenRepository;
	private final LehrerRepository lehrerRepository;
	private final KlassenRepository klassenRepository;
	private final FoerderschwerpunktRepository foerderschwerpunktRepository;
	private final FachklasseRepository fachklasseRepository;
	private final JahrgangRepository jahrgangRepository;
	private final SchuelerschwerpunktRepository schuelerschwerpunktRepository;
	private final SchuelerLernabschnittsdatenMapper mapper;
	private final SchuelerLeistungsdatenMapper leistungsdatenMapper;
	private final SchuelerLernabschnittBemerkungenMapper bemerkungenMapper;
	private final EigeneSchuleService eigeneSchuleService;
	private final BenutzerKompetenzService benutzerKompetenzService;

	private static final String DATEN_NOT_FOUND_BY_ID = "Keine Lernabschnittsdaten zur Abschnitt-ID %d gefunden.";

	/**
	 * Erstellt einen neuen {@code SchuelerLernabschnittService}.
	 *
	 * @param repos                  die vom Service benötigten Repositories
	 * @param mapper                 der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @param leistungsdatenMapper   der Mapper zur Konvertierung zwischen Entity und API-Modell für Leistungsdaten
	 * @param bemerkungenMapper      der Mapper für die Bemerkungen (Patch und Konvertierung ins API-Modell)
	 * @param eigeneSchuleService    der Service für die eigene Schule (liefert die Schulform)
	 * @param benutzerKompetenzService    der Service für die Benutzerkompetenzen
	 */
	public SchuelerLernabschnittService(
			final SchuelerLernabschnittsdatenRepos repos,
			final SchuelerLernabschnittsdatenMapper mapper,
			final SchuelerLeistungsdatenMapper leistungsdatenMapper,
			final SchuelerLernabschnittBemerkungenMapper bemerkungenMapper,
			final EigeneSchuleService eigeneSchuleService,
			final BenutzerKompetenzService benutzerKompetenzService) {
		this.repository = repos.repository();
		this.schuelerRepository = repos.schuelerRepository();
		this.bemerkungRepository = repos.bemerkungRepository();
		this.schuljahresabschnitteRepo = repos.schuljahresabschnitteRepo();
		this.leistungsdatenRepository = repos.leistungsdatenRepository();
		this.lehrerRepository = repos.lehrerRepository();
		this.klassenRepository = repos.klassenRepository();
		this.foerderschwerpunktRepository = repos.foerderschwerpunktRepository();
		this.fachklasseRepository = repos.fachklasseRepository();
		this.jahrgangRepository = repos.jahrgangRepository();
		this.schuelerschwerpunktRepository = repos.schuelerschwerpunktRepository();
		this.mapper = mapper;
		this.leistungsdatenMapper = leistungsdatenMapper;
		this.bemerkungenMapper = bemerkungenMapper;
		this.eigeneSchuleService = eigeneSchuleService;
		this.benutzerKompetenzService = benutzerKompetenzService;
	}

	/**
	 * Gibt die {@link SchuelerLernabschnittsdaten} inklusive Bemerkungen und Leistungsdaten anhand der ID des Lernabschnitts zurück.
	 *
	 * @param id   die ID des Lernabschnitts
	 *
	 * @return die {@link SchuelerLernabschnittsdaten} zu der ID
	 *
	 * @throws ApiOperationException   NOT_FOUND, wenn kein Lernabschnitt mit der ID existiert,
	 *                                 BAD_REQUEST, wenn der zugehörige Schuljahresabschnitt nicht existiert
	 */
	public SchuelerLernabschnittsdaten get(final long id) {
		final var entity = repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(
						Status.NOT_FOUND,
						DATEN_NOT_FOUND_BY_ID.formatted(id)
				));
		return toApi(entity);
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
	 *
	 * @throws ApiOperationException   BAD_REQUEST, wenn der Schuljahresabschnitt eines Lernabschnitts nicht existiert
	 */
	public List<SchuelerLernabschnittsdaten> getListBySchuelerAndSchuljahresabschnitt(final long idSchueler, final long idSchuljahresabschnitt) {
		final var entities = repository.findListBySchuelerAndSchuljahresabschnitt(idSchueler, idSchuljahresabschnitt);
		return toApi(entities);
	}

	/**
	 * Gibt alle {@link SchuelerLernabschnittsdaten} der Schüler mit den übergebenen IDs über alle Schuljahresabschnitte zurück,
	 * sortiert nach Schüler-ID, Schuljahresabschnitt und Wechsel-Nr. Die Bemerkungen sind enthalten, die Leistungsdaten nicht,
	 * da diese z. B. vom Reporting separat nachgeladen werden. Existieren keine Lernabschnitte, so wird eine leere Liste zurückgegeben.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Liste der {@link SchuelerLernabschnittsdaten} ohne Leistungsdaten, ggf. leer
	 *
	 * @throws ApiOperationException   BAD_REQUEST, wenn der Schuljahresabschnitt eines Lernabschnitts nicht existiert
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
	 *
	 * @throws ApiOperationException   FORBIDDEN, wenn der Benutzer keine Rechte auf die Klasse hat,
	 *                                 BAD_REQUEST, wenn der Schüler, der Schuljahresabschnitt oder eine referenzierte ID (z. B. Lehrer, Klasse,
	 *                                 Jahrgang, Förderschwerpunkt, Schwerpunkt, Fachklasse) oder ein Katalogwert nicht existiert,
	 *                                 die Klasse nicht zum Schuljahresabschnitt oder nicht zum Jahrgang passt, das Enddatum
	 *                                 vor dem Anfangsdatum liegt oder die unentschuldigten Fehlstunden die gesamten
	 *                                 Fehlstunden übersteigen
	 */
	public SchuelerLernabschnittsdaten create(final SchuelerLernabschnittCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			final int schuljahr = getSchuljahrByIdAbschnitt(List.of(dto.schuljahresabschnitt)).get(dto.schuljahresabschnitt);
			benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(dto.klassenID.orElse(null));
			validateIdSchueler(dto.schuelerID);
			final Schulform schulform = eigeneSchuleService.getSchulform();
			final var entity = mapper.toDomain(dto);
			validateRequest(dto, schuljahr, schulform, entity);
			// Der Create-Request erbt vom PatchRequest: toDomain setzt nur IDs und Standardwerte,
			// die optionalen Felder werden hier mit der Patch-Logik auf die neue Entity angewendet.
			mapper.patch(dto, entity, schuljahr, schulform);
			validateAndResolveASDJahrgang(dto, entity);
			return toApi(repository.create(entity));
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
	 *
	 * @throws ApiOperationException   NOT_FOUND, wenn kein Lernabschnitt mit der ID existiert,
	 *                                 FORBIDDEN, wenn der Benutzer keine Rechte auf die bisherige oder neue Klasse hat,
	 *                                 BAD_REQUEST, wenn eine referenzierte ID (z. B. Lehrer, Klasse, Jahrgang,
	 *                                 Förderschwerpunkt, Schwerpunkt, Fachklasse) oder ein Katalogwert nicht existiert,
	 *                                 die Klasse nicht zum Schuljahresabschnitt oder nicht zum Jahrgang passt, das Enddatum
	 *                                 vor dem Anfangsdatum liegt oder die unentschuldigten Fehlstunden die gesamten
	 *                                 Fehlstunden übersteigen
	 */
	public SchuelerLernabschnittsdaten patch(final long id, final SchuelerLernabschnittsdatenPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, DATEN_NOT_FOUND_BY_ID.formatted(id)));
			pruefeKompetenz(dto, entity);
			final int schuljahr = getSchuljahrByIdAbschnitt(List.of(entity.Schuljahresabschnitts_ID)).get(entity.Schuljahresabschnitts_ID);
			final Schulform schulform = eigeneSchuleService.getSchulform();
			validateRequest(dto, schuljahr, schulform, entity);
			mapper.patch(dto, entity, schuljahr, schulform);
			validateAndResolveASDJahrgang(dto, entity);
			return toApi(entity);
		});
	}

	/**
	 * Ermittelt zu den übergebenen Schuljahresabschnitt-IDs das jeweilige Schuljahr.
	 *
	 * @param schuljahresabschnittIds   die IDs der Schuljahresabschnitte (Duplikate sind erlaubt)
	 *
	 * @return die Map von der Schuljahresabschnitt-ID auf das Schuljahr (z. B. 2024)
	 *
	 * @throws ApiOperationException   BAD_REQUEST, wenn zu einer ID kein Schuljahresabschnitt existiert
	 */
	private Map<Long, Integer> getSchuljahrByIdAbschnitt(final List<Long> schuljahresabschnittIds) {
		if (CollectionUtils.isEmpty(schuljahresabschnittIds)) {
			return Collections.emptyMap();
		}

		final var uniqueSchuljahresabschnittIds = schuljahresabschnittIds.stream()
				.distinct()
				.toList();
		final var result = schuljahresabschnitteRepo.findListByIds(uniqueSchuljahresabschnittIds).stream()
				.collect(Collectors.toMap(
						dto -> dto.ID,
						dto -> dto.Jahr
				));
		validateAbschnitteForAllIdsWhereFound(uniqueSchuljahresabschnittIds, result);
		return result;
	}

	private void validateIdSchueler(final Long idSchueler) {
		if (!schuelerRepository.existsById(idSchueler)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein Schüler mit der ID %d.".formatted(idSchueler));
		}
	}

	private void validateIdKlasse(final Long idKlasse) {
		if (idKlasse == null) {
			return;
		}
		if (!klassenRepository.existsById(idKlasse)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert keine Klasse mit der ID %d.".formatted(idKlasse));
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

	private void validateAndResolveASDJahrgang(
			final SchuelerLernabschnittsdatenPatchRequest dto,
			final DTOSchuelerLernabschnittsdaten entity
	) {
		if (dto.klassenID.isPresent() || dto.jahrgangID.isPresent()) {
			validateKlasseUndJahrgangKompatibel(entity);
		}
		if (dto.jahrgangID.isPresent()) {
			entity.ASDJahrgang = getASDJahrgang(entity.Jahrgang_ID);
		}
	}

	/**
	 * Aktualisiert die Bemerkungen zu dem Lernabschnitt mit der übergebenen ID teilweise (PATCH). Die allgemeine
	 * Zeugnisbemerkung wird beim Lernabschnitt gespeichert, die weiteren Bemerkungen in den Fach-Bemerkungen.
	 * Existiert noch kein Datensatz mit Fach-Bemerkungen, so wird dieser angelegt, sofern der Patch eine dieser
	 * Bemerkungen enthält. Ein leerer Patch ändert nichts.
	 *
	 * @param id    die ID des Lernabschnitts
	 * @param dto   der Patch-Request mit den zu ändernden Bemerkungen
	 *
	 * @throws ApiOperationException   NOT_FOUND, wenn kein Lernabschnitt mit der ID existiert,
	 *                                 FORBIDDEN, wenn der Benutzer keine Rechte auf die Klasse des Lernabschnitts hat
	 */
	public void patchBemerkungen(final long id, final SchuelerLernabschnittBemerkungenPatchRequest dto) {
		TransactionSupport.transactional(() -> {
			final var entity = repository.findById(id)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, DATEN_NOT_FOUND_BY_ID.formatted(id)));
			benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(entity.Klassen_ID);
			bemerkungenMapper.patchBemerkungen(dto, entity);
			if (!enthaeltFachBemerkungen(dto)) {
				return;
			}
			final var fachBem = bemerkungRepository.findMapByLernabschnittID(List.of(id)).get(id);
			if (fachBem != null) {
				bemerkungenMapper.patchFachBemerkungen(dto, fachBem);
				return;
			}
			final var neu = new DTOSchuelerPSFachBemerkungen(-1, id);
			bemerkungenMapper.patchFachBemerkungen(dto, neu);
			bemerkungRepository.create(neu);
		});
	}

	private static boolean enthaeltFachBemerkungen(final SchuelerLernabschnittBemerkungenPatchRequest dto) {
		return dto.zeugnisASV.isPresent()
				|| dto.zeugnisLELS.isPresent()
				|| dto.zeugnisAUE.isPresent()
				|| dto.uebergangESF.isPresent()
				|| dto.foerderschwerpunkt.isPresent()
				|| dto.versetzungsentscheidung.isPresent();
	}

	private void validateRequest(
			final SchuelerLernabschnittsdatenPatchRequest dto,
			final int schuljahr,
			final Schulform schulform,
			final DTOSchuelerLernabschnittsdaten entity
	) {
		dto.tutorID.ifPresent(this::validateIdLehrer);
		dto.sonderpaedagogeID.ifPresent(this::validateIdLehrer);
		dto.folgeklassenID.ifPresent(this::validateIdKlasse);
		dto.klassenID.ifPresent(this::validateIdKlasse);
		dto.foerderschwerpunkt1ID.ifPresent(this::validateIdFoerderschwerpunkt);
		dto.foerderschwerpunkt2ID.ifPresent(this::validateIdFoerderschwerpunkt);
		dto.schwerpunktID.ifPresent(this::validateIdSchuelerschwerpunkt);
		dto.idKlassenart.ifPresent(this::validateIdKlassenart);
		dto.idSchulgliederung.ifPresent(this::validateIdSchulgliederung);
		dto.idOrganisationsform.ifPresent(id -> validateIdOrganisationsform(id, schulform));
		dto.idEpJahre.ifPresent(this::validateIdEpJahre);
		dto.fachklasseID.ifPresent(this::validateIdFachklasse);
		dto.idVersetzungsvermerk.ifPresent(id -> validateIdVersetzungsvermerk(id, schuljahr, schulform));
		dto.bilingualerZweig.ifPresent(kuerzel -> validateBilingualerZweig(kuerzel, schuljahr, schulform));
		dto.jahrgangID.ifPresent(this::validateIdJahrgang);
		dto.klassenID.ifPresent(klassenID -> validateKlasseImSchuljahresabschnitt(klassenID, entity.Schuljahresabschnitts_ID));

		if (dto.datumAnfang.isPresent() || dto.datumEnde.isPresent()) {
			validateDatumEndeNichtVorAnfang(dto.datumAnfang.orElse(entity.DatumVon), dto.datumEnde.orElse(entity.DatumBis));
		}
		if (dto.fehlstundenGesamt.isPresent() || dto.fehlstundenUnentschuldigt.isPresent()) {
			validateFehlstundenUnentschuldigtNichtUeberGesamt(dto.fehlstundenGesamt.orElse(entity.SumFehlStd),
					dto.fehlstundenUnentschuldigt.orElse(entity.SumFehlStdU));
		}

	}

	private void pruefeKompetenz(final SchuelerLernabschnittsdatenPatchRequest dto, final DTOSchuelerLernabschnittsdaten entity) {
		if (dto.klassenID.isPresent() && (dto.klassenID.get() != null)) {
			benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(entity.Klassen_ID, dto.klassenID.get());
		} else {
			benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(entity.Klassen_ID);
		}
	}

	private void validateIdLehrer(final Long idLehrer) {
		if (idLehrer == null) {
			return;
		}
		if (!lehrerRepository.existsById(idLehrer)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein Lehrer mit der ID %d.".formatted(idLehrer));
		}
	}

	private void validateIdFoerderschwerpunkt(final Long idFoerderschwerpunkt) {
		if (idFoerderschwerpunkt == null) {
			return;
		}
		if (!foerderschwerpunktRepository.existsById(idFoerderschwerpunkt)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert kein Förderschwerpunkt mit der ID %d.".formatted(idFoerderschwerpunkt));
		}
	}

	private void validateIdSchuelerschwerpunkt(final Long idSchuelerschwerpunkt) {
		if (idSchuelerschwerpunkt == null) {
			return;
		}
		if (!schuelerschwerpunktRepository.existsById(idSchuelerschwerpunkt)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert kein Schülerschwerpunkt mit der ID %d.".formatted(idSchuelerschwerpunkt));
		}
	}

	private void validateIdKlassenart(final Long idKlassenart) {
		if (idKlassenart == null) {
			return;
		}
		if (Klassenart.data().getEintragByID(idKlassenart) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert keine gültige Klassenart mit der ID %d.".formatted(idKlassenart));
		}
	}

	private void validateIdSchulgliederung(final Long idSchulgliederung) {
		if (idSchulgliederung == null) {
			return;
		}
		if (Schulgliederung.data().getEintragByID(idSchulgliederung) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert keine gültige Schulgliederung mit der ID %d.".formatted(idSchulgliederung));
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

	private void validateIdEpJahre(final Long idEpJahre) {
		if (idEpJahre == null) {
			return;
		}
		if (PrimarstufeSchuleingangsphaseBesuchsjahre.data().getEintragByID(idEpJahre) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert keine gültige Anzahl an Besuchsjahren in der Schuleingangsphase mit der ID %d.".formatted(idEpJahre));
		}
	}

	private void validateIdFachklasse(final Long idFachklasse) {
		if (idFachklasse == null) {
			return;
		}
		if (!fachklasseRepository.existsById(idFachklasse)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert keine Fachklasse mit der ID %d.".formatted(idFachklasse));
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

	private void validateIdJahrgang(final Long idJahrgang) {
		if (idJahrgang == null) {
			return;
		}
		if (!jahrgangRepository.existsById(idJahrgang)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein Jahrgang mit der ID %d.".formatted(idJahrgang));
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

	private void validateKlasseUndJahrgangKompatibel(final DTOSchuelerLernabschnittsdaten entity) {
		if ((entity.Klassen_ID == null) || (entity.Jahrgang_ID == null)) {
			return;
		}
		final DTOKlassen klasse = klassenRepository.findById(entity.Klassen_ID).orElse(null);
		if ((klasse == null) || (klasse.Jahrgang_ID == null)) {
			return;
		}
		// Derselbe Jahrgang ist immer kompatibel, dafür muss der Katalog nicht befragt werden
		if (Objects.equals(klasse.Jahrgang_ID, entity.Jahrgang_ID)) {
			return;
		}
		final String asdJahrgangKlasse = getASDJahrgang(klasse.Jahrgang_ID);
		final String asdJahrgangLernabschnitt = getASDJahrgang(entity.Jahrgang_ID);
		if (!Objects.equals(asdJahrgangKlasse, asdJahrgangLernabschnitt)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Jahrgang mit der ID %d ist für die Klasse mit der ID %d nicht zulässig.".formatted(entity.Jahrgang_ID, entity.Klassen_ID));
		}
	}

	private static void validateDatumEndeNichtVorAnfang(final String datumVon, final String datumBis) {
		if ((StringUtils.isNotBlank(datumVon)) && (StringUtils.isNotBlank(datumBis))	 && (datumBis.compareTo(datumVon) < 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Enddatum %s darf nicht vor dem Anfangsdatum %s liegen.".formatted(datumBis, datumVon));
		}
	}

	private static void validateFehlstundenUnentschuldigtNichtUeberGesamt(final Integer fehlstundenGesamt, final Integer fehltstundenUnentschuldigt) {
		if ((fehlstundenGesamt != null)
				&& (fehltstundenUnentschuldigt != null)
				&& (fehltstundenUnentschuldigt > fehlstundenGesamt)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die unentschuldigten Fehlstunden (%d) dürfen die gesamten Fehlstunden (%d) nicht übersteigen."
							.formatted(fehltstundenUnentschuldigt, fehlstundenGesamt));
		}
	}

	/**
	 * Wandelt eine einzelne Entity in das API-Modell um.
	 *
	 * @param entity   die Entity des Lernabschnitts
	 *
	 * @return das API-Modell
	 */
	private SchuelerLernabschnittsdaten toApi(final DTOSchuelerLernabschnittsdaten entity) {
		return toApi(List.of(entity)).getFirst();
	}

	/**
	 * Wandelt eine Liste von Entities inklusive Leistungsdaten in API-Modelle um.
	 *
	 * @param entities   die Entities der Lernabschnitte
	 *
	 * @return die API-Modelle in der Reihenfolge der Entities, leer bei leerer Entity-Liste
	 */
	private List<SchuelerLernabschnittsdaten> toApi(final List<DTOSchuelerLernabschnittsdaten> entities) {
		return toApi(entities, true);
	}

	/**
	 * Wandelt eine Liste von Entities in API-Modelle um. Schuljahre, Schulform, Bemerkungen und ggf. Leistungsdaten werden dabei
	 * gebündelt für alle Entities geladen, um einzelne Datenbankabfragen pro Lernabschnitt zu vermeiden.
	 *
	 * @param entities             die Entities der Lernabschnitte
	 * @param mitLeistungsdaten    gibt an, ob die Leistungsdaten geladen werden sollen
	 *
	 * @return die API-Modelle in der Reihenfolge der Entities, leer bei leerer Entity-Liste
	 */
	private List<SchuelerLernabschnittsdaten> toApi(final List<DTOSchuelerLernabschnittsdaten> entities, final boolean mitLeistungsdaten) {
		if (entities.isEmpty()) {
			return List.of();
		}

		final var schuljahresabschnittIds = entities.stream()
				.map(e -> e.Schuljahresabschnitts_ID)
				.toList();

		final var schuljahrByIdAbschnitt = getSchuljahrByIdAbschnitt(schuljahresabschnittIds);
		final var schulform = eigeneSchuleService.getSchulform();

		final var ctxById = ContextLoader.load(
				entities,
				bemerkungRepository,
				leistungsdatenRepository,
				leistungsdatenMapper,
				bemerkungenMapper,
				schuljahrByIdAbschnitt,
				mitLeistungsdaten
		);

		return entities.stream()
				.map(entity -> {
					final var schuljahr = schuljahrByIdAbschnitt.get(entity.Schuljahresabschnitts_ID);
					final var ctx = ctxById.getOrDefault(entity.ID, SchuelerLernabschnittsdatenMappingContext.empty());
					return mapper.toApi(entity, schuljahr, schulform, ctx);
				})
				.toList();
	}

	/**
	 * Prüft, ob zu allen angefragten Schuljahresabschnitt-IDs ein Schuljahresabschnitt gefunden wurde.
	 *
	 * @param uniqueSchuljahresabschnittIds   die angefragten IDs ohne Duplikate
	 * @param result                          die gefundenen Schuljahre je Schuljahresabschnitt-ID
	 *
	 * @throws ApiOperationException   BAD_REQUEST mit den fehlenden IDs, wenn nicht alle gefunden wurden
	 */
	private void validateAbschnitteForAllIdsWhereFound(final List<Long> uniqueSchuljahresabschnittIds, final Map<Long, Integer> result) {
		final var missing = uniqueSchuljahresabschnittIds.stream()
				.filter(id -> !result.containsKey(id))
				.toList();

		if (!missing.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine Schuljahresabschnitte gefunden für IDs: " + missing);
		}
	}

	/**
	 * Lädt die Zusatzdaten (Bemerkungen und Leistungsdaten) für mehrere Lernabschnitte mit jeweils einer Abfrage
	 * und stellt sie als {@link SchuelerLernabschnittsdatenMappingContext} für den Mapper bereit.
	 */
	private static final class ContextLoader {

		private ContextLoader() {

		}

		/**
		 * Erstellt für jeden Lernabschnitt den Mapping-Kontext mit den bereits gemappten Bemerkungen und Leistungsdaten.
		 *
		 * @param entities                   die Entities der Lernabschnitte
		 * @param bemerkungRepository        das Repository für die Bemerkungen
		 * @param leistungsdatenRepository   das Repository für die Leistungsdaten
		 * @param leistungsdatenMapper       der Mapper für die Leistungsdaten
		 * @param bemerkungenMapper          der Mapper für die Bemerkungen
		 * @param schuljahrByIdAbschnitt     die Schuljahre je Schuljahresabschnitt-ID
		 * @param mitLeistungsdaten          gibt an, ob die Leistungsdaten geladen werden sollen
		 *
		 * @return die Map von der Lernabschnitt-ID auf den Mapping-Kontext, leer bei leerer Entity-Liste
		 */
		static Map<Long, SchuelerLernabschnittsdatenMappingContext> load(
				final List<DTOSchuelerLernabschnittsdaten> entities,
				final SchuelerLernabschnittBemerkungRepository bemerkungRepository,
				final SchuelerLeistungsdatenRepository leistungsdatenRepository,
				final SchuelerLeistungsdatenMapper leistungsdatenMapper,
				final SchuelerLernabschnittBemerkungenMapper bemerkungenMapper,
				final Map<Long, Integer> schuljahrByIdAbschnitt,
				final boolean mitLeistungsdaten) {

			if (CollectionUtils.isEmpty(entities)) {
				return Collections.emptyMap();
			}
			final var ids = entities.stream().map(e -> e.ID).toList();
			final var bemerkungenById = bemerkungRepository.findMapByLernabschnittID(ids);
			final Map<Long, List<DTOSchuelerLeistungsdaten>> leistungenById = mitLeistungsdaten
					? leistungsdatenRepository.getMapListByLernabschnittsIds(ids)
					: Collections.emptyMap();

			return entities.stream()
					.collect(Collectors.toMap(
							e -> e.ID,
							e -> {
								final int schuljahr = schuljahrByIdAbschnitt.get(e.Schuljahresabschnitts_ID);
								final List<SchuelerLeistungsdaten> leistungen = leistungenById.getOrDefault(e.ID, List.of()).stream()
										.map(dto -> leistungsdatenMapper.toApi(dto, schuljahr))
										.toList();
								final DTOSchuelerPSFachBemerkungen bemerkung = bemerkungenById.get(e.ID);
								return new SchuelerLernabschnittsdatenMappingContext(
										(bemerkung == null) ? new SchuelerLernabschnittBemerkungen() : bemerkungenMapper.toApi(bemerkung),
										leistungen
								);
							}
					));
		}
	}
}
