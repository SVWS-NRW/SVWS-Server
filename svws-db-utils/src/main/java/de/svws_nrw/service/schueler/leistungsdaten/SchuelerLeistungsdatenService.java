package de.svws_nrw.service.schueler.leistungsdaten;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.leistungsdaten.SchuelerLeistungsdatenMapper;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response.Status;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Service für die Leistungsdaten eines Schülers in einem Lernabschnitt.
 */
public final class SchuelerLeistungsdatenService {

	private static final String DATEN_NOT_FOUND_BY_ID = "Die Leistungsdaten mit der ID %d wurden in der Datenbank nicht gefunden.";
	private static final String ENTITY_LABEL = "Leistungsdaten";
	private static final String NICHT_VORHANDEN = "Es existiert kein %s mit der ID %d.";
	private static final String KEIN_SCHULJAHRESABSCHNITT = "Zum Lernabschnitt mit der ID %d wurde kein Schuljahresabschnitt gefunden.";
	private static final String IDS_ZUM_LOESCHEN_NULL = "Die Liste der zu löschenden IDs darf nicht null sein.";
	private static final String ATTRIBUT_NICHT_LESBAR = "Das Attribut %s des Requests konnte nicht gelesen werden.";
	private static final Set<String> FACHLEHRER_ATTRIBUTE = Set.of("note", "noteQuartal", "fehlstundenGesamt", "fehlstundenUnentschuldigt");
	private static final String BEZEICHNUNG_LERNABSCHNITT = "Lernabschnitt";
	private static final String BEZEICHNUNG_FACH = "Fach";
	private static final String BEZEICHNUNG_KURS = "Kurs";
	private static final String BEZEICHNUNG_LEHRER = "Lehrer";

	private final SchuelerLeistungsdatenRepository repository;
	private final SchuelerLernabschnittRepository lernabschnittRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final FachRepository fachRepository;
	private final KurseRepository kurseRepository;
	private final LehrerRepository lehrerRepository;
	private final SchuelerLeistungsdatenMapper mapper;
	private final EigeneSchuleService eigeneSchuleService;
	private final BenutzerKompetenzService benutzerKompetenzService;

	/**
	 * Erstellt einen neuen {@code SchuelerLeistungsdatenService}.
	 *
	 * @param repos                      die vom Service benötigten Repositories
	 * @param mapper                     der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @param eigeneSchuleService        der Service für die eigene Schule (liefert den aktuellen Schuljahresabschnitt)
	 * @param benutzerKompetenzService   der Service für die Benutzerkompetenzen
	 */
	public SchuelerLeistungsdatenService(
			final SchuelerLeistungsdatenRepositories repos,
			final SchuelerLeistungsdatenMapper mapper,
			final EigeneSchuleService eigeneSchuleService,
			final BenutzerKompetenzService benutzerKompetenzService) {
		this.repository = repos.repository();
		this.lernabschnittRepository = repos.lernabschnittRepository();
		this.schuljahresabschnitteRepository = repos.schuljahresabschnitteRepository();
		this.fachRepository = repos.fachRepository();
		this.kurseRepository = repos.kurseRepository();
		this.lehrerRepository = repos.lehrerRepository();
		this.mapper = mapper;
		this.eigeneSchuleService = eigeneSchuleService;
		this.benutzerKompetenzService = benutzerKompetenzService;
	}

	/**
	 * Gibt die {@link SchuelerLeistungsdaten} anhand ihrer ID zurück.
	 *
	 * @param id   die ID der Leistungsdaten
	 *
	 * @return die {@link SchuelerLeistungsdaten} zu der ID
	 */
	public SchuelerLeistungsdaten getById(final long id) {
		return toApi(findLeistungsdaten(id));
	}

	/**
	 * Gibt alle {@link SchuelerLeistungsdaten} der Lernabschnitte mit den übergebenen IDs zurück. Das Schuljahr
	 * wird gebündelt für alle Lernabschnitte bestimmt. Existieren keine Leistungsdaten, so wird eine leere Liste
	 * zurückgegeben.
	 *
	 * @param idsLernabschnitte   die IDs der Lernabschnitte
	 *
	 * @return die Liste der {@link SchuelerLeistungsdaten}, ggf. leer
	 */
	public List<SchuelerLeistungsdaten> getListByLernabschnitte(final Collection<Long> idsLernabschnitte) {
		if (idsLernabschnitte.isEmpty()) {
			return List.of();
		}

		return toApi(repository.findListByLernabschnitt(idsLernabschnitte));
	}

	/**
	 * Legt neue Leistungsdaten an. Das Fach setzt die Sortierung und den Kurs auf {@code null}. Weitere
	 * Attribute des Requests werden übernommen, ein Kurs setzt dabei Fachlehrer und Kursart.
	 * Nicht angegebene Attribute ({@code null}) werden mit ihren Standardwerten angelegt.
	 *
	 * @param dto   der Request mit den Daten der neuen Leistungsdaten
	 *
	 * @return die neu angelegten {@link SchuelerLeistungsdaten}
	 */
	public SchuelerLeistungsdaten create(final SchuelerLeistungsdatenCreateRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var lernabschnitt = findLernabschnitt(dto.lernabschnittID);
			benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(lernabschnitt.Klassen_ID);
			validate(dto);
			final var fach = findFach(dto.fachID);
			final var kurs = loadKurs(dto.kursID);
			final var schuljahr = getSchuljahr(lernabschnitt);
			final var entity = mapper.toDomain(dto);
			applyFach(entity, fach);
			applyAbgeleiteteFelder(dto, entity, kurs, schuljahr);

			return mapper.toApi(repository.create(entity), schuljahr);
		});
	}

	/**
	 * Aktualisiert die {@link SchuelerLeistungsdaten} zu der übergebenen ID teilweise (PATCH). Es werden nur
	 * die Felder übernommen, die im Request enthalten sind. Abgeleitete Felder werden in der Reihenfolge
	 * Fach, Abiturfach, Kurs, Kursart gesetzt, eine gesendete Kursart gewinnt dabei.
	 *
	 * @param id    die ID der Leistungsdaten
	 * @param dto   der Patch-Request mit den zu ändernden Feldern
	 *
	 * @return die aktualisierten {@link SchuelerLeistungsdaten}
	 */
	public SchuelerLeistungsdaten patch(final long id, final SchuelerLeistungsdatenPatchRequest dto) {
		return TransactionSupport.transactional(() -> {
			final var entity = findLeistungsdaten(id);
			final var neuerLernabschnitt = loadNeuerLernabschnitt(dto);
			neuerLernabschnitt.ifPresent(l -> benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(l.Klassen_ID));
			final var lernabschnitt = checkKompetenzPatch(dto, entity);
			validate(dto);
			final var fach = loadFach(dto);
			final var kurs = loadKurs(dto);
			final var schuljahr = getSchuljahr(neuerLernabschnitt.orElse(lernabschnitt));
			patchEntity(dto, entity, fach, kurs, schuljahr);

			return mapper.toApi(entity, schuljahr);
		});
	}

	/**
	 * Löscht die Leistungsdaten mit den angegebenen IDs. Bei nur funktionsbezogener Kompetenz muss der Benutzer für
	 * die Klassen aller Lernabschnitte zuständig sein, sonst wird die gesamte Anfrage abgelehnt. Nicht gefundene IDs werden
	 * nicht gelöscht, sondern mit einer Fehlermeldung markiert.
	 *
	 * @param idsToDelete   die IDs der zu löschenden Leistungsdaten
	 *
	 * @return die Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> deleteMultiple(final List<Long> idsToDelete) {
		if (idsToDelete == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, IDS_ZUM_LOESCHEN_NULL);
		}

		return TransactionSupport.transactional(() -> {
			checkKompetenzLoeschen(idsToDelete);

			return BulkDeleteUtils.delete(idsToDelete, repository, e -> e.ID, ENTITY_LABEL);
		});
	}

	private DTOSchuelerLeistungsdaten findLeistungsdaten(final long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, DATEN_NOT_FOUND_BY_ID.formatted(id)));
	}

	private DTOSchuelerLernabschnittsdaten findLernabschnitt(final long idLernabschnitt) {
		return lernabschnittRepository.findById(idLernabschnitt)
				.orElseThrow(() -> createNichtVorhandenException(BEZEICHNUNG_LERNABSCHNITT, idLernabschnitt));
	}

	private DTOFach findFach(final long idFach) {
		return fachRepository.findById(idFach)
				.orElseThrow(() -> createNichtVorhandenException(BEZEICHNUNG_FACH, idFach));
	}

	private DTOKurs findKurs(final long idKurs) {
		return kurseRepository.findById(idKurs)
				.orElseThrow(() -> createNichtVorhandenException(BEZEICHNUNG_KURS, idKurs));
	}

	private ApiOperationException createNichtVorhandenException(final String bezeichnung, final long id) {
		return new ApiOperationException(Status.CONFLICT, NICHT_VORHANDEN.formatted(bezeichnung, id));
	}

	/**
	 * Lädt den Lernabschnitt, auf den der Patch-Request die Leistungsdaten umhängt.
	 *
	 * @param dto   der Patch-Request
	 *
	 * @return der neue Lernabschnitt oder ein leeres Optional, wenn der Request keine Lernabschnitt-ID enthält
	 */
	private Optional<DTOSchuelerLernabschnittsdaten> loadNeuerLernabschnitt(final SchuelerLeistungsdatenPatchRequest dto) {
		return dto.lernabschnittID.isPresent() ? Optional.of(findLernabschnitt(dto.lernabschnittID.get())) : Optional.empty();
	}

	/**
	 * Lädt das Fach, das der Patch-Request setzt.
	 *
	 * @param dto   der Patch-Request
	 *
	 * @return das geladene Fach oder {@code undefined}, wenn der Request keine Fach-ID enthält
	 */
	private JsonNullable<DTOFach> loadFach(final SchuelerLeistungsdatenPatchRequest dto) {
		return dto.fachID.map(this::findFach);
	}

	/**
	 * Lädt den Kurs mit der übergebenen ID.
	 *
	 * @param idKurs   die ID des Kurses oder {@code null} bei Klassenunterricht
	 *
	 * @return der geladene Kurs oder {@code null}, wenn keine ID angegeben ist
	 */
	private DTOKurs loadKurs(final Long idKurs) {
		return (idKurs == null) ? null : findKurs(idKurs);
	}

	/**
	 * Lädt den Kurs, den der Patch-Request setzt. Ein Kurs mit der ID {@code null} löst die Zuordnung zum Kurs,
	 * daher ist in dem Ergebnis auch {@code null} ein gültiger Wert.
	 *
	 * @param dto   der Patch-Request
	 *
	 * @return der geladene Kurs, {@code null} zum Lösen der Zuordnung oder {@code undefined}, wenn der Request keine Kurs-ID enthält
	 */
	private JsonNullable<DTOKurs> loadKurs(final SchuelerLeistungsdatenPatchRequest dto) {
		if (!dto.kursID.isPresent()) {
			return JsonNullable.undefined();
		}

		return JsonNullable.of(loadKurs(dto.kursID.get()));
	}

	/**
	 * Prüft die Kompetenz zum Ändern der Leistungsdaten im aktuellen Lernabschnitt und gibt diesen zurück.
	 *
	 * @param dto      der Patch-Request
	 * @param entity   die zu ändernden Leistungsdaten
	 *
	 * @return der aktuelle Lernabschnitt der Leistungsdaten
	 */
	private DTOSchuelerLernabschnittsdaten checkKompetenzPatch(final SchuelerLeistungsdatenPatchRequest dto, final DTOSchuelerLeistungsdaten entity) {
		final var lernabschnitt = findLernabschnitt(entity.Abschnitt_ID);
		benutzerKompetenzService.checkKompetenzLeistungsdatenAendern(entity.Fachlehrer_ID, istFachlehrerRegelAnwendbar(dto, lernabschnitt),
				lernabschnitt.Klassen_ID);

		return lernabschnitt;
	}

	private boolean istFachlehrerRegelAnwendbar(final SchuelerLeistungsdatenPatchRequest dto, final DTOSchuelerLernabschnittsdaten lernabschnitt) {
		return (lernabschnitt.Schuljahresabschnitts_ID == eigeneSchuleService.getIdSchuljahresabschnitt()) && hasOnlyFachlehrerAttribute(dto);
	}

	/**
	 * Gibt an, ob der Patch ausschließlich Attribute enthält, die auch ein Fachlehrer mit funktionsbezogener
	 * Kompetenz ändern darf. Das sind die Noten und die Fehlstunden. Es werden alle Attribute des Requests geprüft,
	 * die nicht ausdrücklich erlaubt sind. Neue Attribute sind daher standardmäßig nicht für Fachlehrer änderbar.
	 *
	 * @param dto   der Patch-Request
	 *
	 * @return {@code true}, wenn der Patch keine anderen Attribute als {@code note}, {@code noteQuartal},
	 *         {@code fehlstundenGesamt} und {@code fehlstundenUnentschuldigt} enthält
	 */
	static boolean hasOnlyFachlehrerAttribute(final SchuelerLeistungsdatenPatchRequest dto) {
		return Stream.of(SchuelerLeistungsdatenPatchRequest.class.getFields())
				.filter(feld -> JsonNullable.class.isAssignableFrom(feld.getType()))
				.filter(feld -> !FACHLEHRER_ATTRIBUTE.contains(feld.getName()))
				.noneMatch(feld -> isAttributePresent(dto, feld));
	}

	private static boolean isAttributePresent(final SchuelerLeistungsdatenPatchRequest dto, final Field feld) {
		try {
			return ((JsonNullable<?>) feld.get(dto)).isPresent();
		} catch (final IllegalAccessException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, ATTRIBUT_NICHT_LESBAR.formatted(feld.getName()));
		}
	}

	private void checkKompetenzLoeschen(final List<Long> idsToDelete) {
		final var idsLernabschnitte = repository.findListByIds(idsToDelete).stream()
				.map(e -> e.Abschnitt_ID)
				.distinct()
				.toList();
		final var idsKlassen = lernabschnittRepository.findListByIds(idsLernabschnitte).stream()
				.map(l -> l.Klassen_ID)
				.distinct()
				.toArray(Long[]::new);
		benutzerKompetenzService.pruefeKompetenzLeistungsdatenAendernKlassen(idsKlassen);
	}

	/**
	 * Prüft zuerst die Werte des Create-Requests ohne Datenbankzugriff, danach die referenzierten Lehrer.
	 *
	 * @param dto   der zu prüfende Request
	 */
	private void validate(final SchuelerLeistungsdatenCreateRequest dto) {
		validateWerte(dto.kursart, dto.abifach, dto.note, dto.noteQuartal, dto.umfangLernstandsbericht);
		validateLehrer(dto.lehrerID, dto.zusatzkraftID);
	}

	/**
	 * Prüft zuerst die Werte des Patch-Requests ohne Datenbankzugriff, danach die referenzierten Lehrer.
	 * Nur die im Request enthaltenen Werte werden geprüft.
	 *
	 * @param dto   der zu prüfende Request
	 */
	private void validate(final SchuelerLeistungsdatenPatchRequest dto) {
		validateWerte(dto.kursart.orElse(null), dto.abifach.orElse(null), dto.note.orElse(null), dto.noteQuartal.orElse(null),
				dto.umfangLernstandsbericht.orElse(null));
		validateLehrer(dto.lehrerID.orElse(null), dto.zusatzkraftID.orElse(null));
	}

	private void validateWerte(final String kursart, final Integer abifach, final String note, final String noteQuartal, final String umfang) {
		SchuelerLeistungsdatenWerteUtils.validateKursart(kursart);
		SchuelerLeistungsdatenWerteUtils.validateAbifach(abifach);
		SchuelerLeistungsdatenWerteUtils.validateNotenkuerzel(note);
		SchuelerLeistungsdatenWerteUtils.validateNotenkuerzel(noteQuartal);
		SchuelerLeistungsdatenWerteUtils.validateUmfangLernstandsbericht(umfang);
	}

	/**
	 * Prüft Fachlehrer und Zusatzkraft mit einer einzigen Abfrage auf die Lehrer-Tabelle.
	 *
	 * @param idLehrer         die ID des Fachlehrers oder {@code null}
	 * @param idZusatzkraft    die ID der Zusatzkraft oder {@code null}
	 */
	private void validateLehrer(final Long idLehrer, final Long idZusatzkraft) {
		final var idsLehrer = getIdsLehrer(idLehrer, idZusatzkraft);
		final var idsVorhanden = lehrerRepository.existsByIds(idsLehrer);
		final var idFehlend = idsLehrer.stream()
				.filter(id -> !idsVorhanden.contains(id))
				.findFirst();
		if (idFehlend.isPresent()) {
			throw createNichtVorhandenException(BEZEICHNUNG_LEHRER, idFehlend.get());
		}
	}

	/**
	 * Gibt die IDs von Fachlehrer und Zusatzkraft zurück, die gesetzt sind. Doppelte und {@code null}-Werte entfallen.
	 *
	 * @param idLehrer         die ID des Fachlehrers oder {@code null}
	 * @param idZusatzkraft    die ID der Zusatzkraft oder {@code null}
	 *
	 * @return die IDs der gesetzten Lehrer, ggf. leer
	 */
	private List<Long> getIdsLehrer(final Long idLehrer, final Long idZusatzkraft) {
		return Stream.of(idLehrer, idZusatzkraft)
				.filter(Objects::nonNull)
				.distinct()
				.toList();
	}

	private void patchEntity(final SchuelerLeistungsdatenPatchRequest dto, final DTOSchuelerLeistungsdaten entity, final JsonNullable<DTOFach> fach,
			final JsonNullable<DTOKurs> kurs, final int schuljahr) {
		mapper.patch(dto, entity);
		dto.lernabschnittID.ifPresent(id -> entity.Abschnitt_ID = id);
		fach.ifPresent(f -> applyFach(entity, f));
		applyAbgeleiteteFelder(dto, entity, kurs, schuljahr);
	}

	private void applyFach(final DTOSchuelerLeistungsdaten entity, final DTOFach fach) {
		entity.Fach_ID = fach.ID;
		entity.Sortierung = fach.SortierungAllg;
		entity.Kurs_ID = null;
	}

	private void applyAbgeleiteteFelder(final SchuelerLeistungsdatenCreateRequest dto, final DTOSchuelerLeistungsdaten entity, final DTOKurs kurs,
			final int schuljahr) {
		if (dto.abifach != null) {
			entity.AbiFach = String.valueOf(dto.abifach);
		}
		if (kurs != null) {
			SchuelerLeistungsdatenKursartResolver.assignKurs(entity, kurs, schuljahr);
		}
		if (dto.kursart != null) {
			applyKursart(entity, dto.kursart, schuljahr);
		}
	}

	private void applyAbgeleiteteFelder(final SchuelerLeistungsdatenPatchRequest dto, final DTOSchuelerLeistungsdaten entity,
			final JsonNullable<DTOKurs> kurs, final int schuljahr) {
		dto.abifach.ifPresent(abifach -> entity.AbiFach = (abifach == null) ? null : String.valueOf(abifach));
		kurs.ifPresent(k -> applyKurs(entity, k, schuljahr));
		dto.kursart.ifPresent(kuerzel -> applyKursart(entity, kuerzel, schuljahr));
	}

	/**
	 * Übernimmt den Kurs in die Leistungsdaten. Ein Kurs {@code null} löst die Zuordnung zum Kurs.
	 *
	 * @param entity     die zu ändernden Leistungsdaten
	 * @param kurs       der Kurs oder {@code null} zum Lösen der Zuordnung
	 * @param schuljahr  das Schuljahr der Leistungsdaten
	 */
	private void applyKurs(final DTOSchuelerLeistungsdaten entity, final DTOKurs kurs, final int schuljahr) {
		if (kurs == null) {
			entity.Kurs_ID = null;
		} else {
			SchuelerLeistungsdatenKursartResolver.assignKurs(entity, kurs, schuljahr);
		}
	}

	private void applyKursart(final DTOSchuelerLeistungsdaten entity, final String kuerzel, final int schuljahr) {
		final var kursart = (kuerzel == null) ? ZulaessigeKursart.PUK : ZulaessigeKursart.data().getWertByKuerzel(kuerzel);
		SchuelerLeistungsdatenKursartResolver.assignKursart(entity, kursart, schuljahr);
	}

	private int getSchuljahr(final DTOSchuelerLernabschnittsdaten lernabschnitt) {
		return schuljahresabschnitteRepository.findById(lernabschnitt.Schuljahresabschnitts_ID)
				.map(abschnitt -> abschnitt.Jahr)
				.orElseThrow(() -> createKeinSchuljahresabschnittException(lernabschnitt.ID));
	}

	/**
	 * Ermittelt zu den Lernabschnitten der Leistungsdaten das jeweilige Schuljahr. Die Lernabschnitte und die
	 * Schuljahresabschnitte werden dafür jeweils mit einer Abfrage geladen.
	 *
	 * @param entities   die Leistungsdaten
	 *
	 * @return die Map von der Lernabschnitt-ID auf das Schuljahr
	 */
	private Map<Long, Integer> determineSchuljahrByLernabschnitt(final List<DTOSchuelerLeistungsdaten> entities) {
		final var idsLernabschnitte = entities.stream()
				.map(e -> e.Abschnitt_ID)
				.distinct()
				.toList();
		final var idAbschnittByLernabschnitt = lernabschnittRepository.findListByIds(idsLernabschnitte).stream()
				.collect(Collectors.toMap(l -> l.ID, l -> l.Schuljahresabschnitts_ID));
		final var idsAbschnitte = idAbschnittByLernabschnitt.values().stream()
				.distinct()
				.toList();
		final var schuljahrByAbschnitt = schuljahresabschnitteRepository.findListByIds(idsAbschnitte).stream()
				.collect(Collectors.toMap(a -> a.ID, a -> a.Jahr));

		return idsLernabschnitte.stream()
				.collect(Collectors.toMap(id -> id, id -> requireSchuljahr(id, schuljahrByAbschnitt.get(idAbschnittByLernabschnitt.get(id)))));
	}

	private int requireSchuljahr(final long idLernabschnitt, final Integer schuljahr) {
		if (schuljahr == null) {
			throw createKeinSchuljahresabschnittException(idLernabschnitt);
		}

		return schuljahr;
	}

	private ApiOperationException createKeinSchuljahresabschnittException(final long idLernabschnitt) {
		return new ApiOperationException(Status.INTERNAL_SERVER_ERROR, KEIN_SCHULJAHRESABSCHNITT.formatted(idLernabschnitt));
	}

	private SchuelerLeistungsdaten toApi(final DTOSchuelerLeistungsdaten entity) {
		return toApi(List.of(entity)).getFirst();
	}

	private List<SchuelerLeistungsdaten> toApi(final List<DTOSchuelerLeistungsdaten> entities) {
		if (entities.isEmpty()) {
			return Collections.emptyList();
		}
		final var schuljahrByLernabschnitt = determineSchuljahrByLernabschnitt(entities);

		return entities.stream()
				.map(entity -> mapper.toApi(entity, schuljahrByLernabschnitt.get(entity.Abschnitt_ID)))
				.toList();
	}

}
