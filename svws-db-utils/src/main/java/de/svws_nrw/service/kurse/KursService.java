package de.svws_nrw.service.kurse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.kurse.KursMapper;
import de.svws_nrw.mapper.schueler.SchuelerMapper;
import de.svws_nrw.repo.kurse.KursSchuelerRepository;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.kurse.KurslehrerRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff auf Kurse mit den Core-DTOs {@link KursDaten}. Er liest, legt an, ändert und löscht Kurse.
 */
public final class KursService {

	private static final String KEIN_KURS_GEFUNDEN = "Kein Kurs mit der ID %d gefunden.";
	private static final String KEIN_ABSCHNITT_GEFUNDEN = "Es konnte kein Schuljahresabschnitt mit der ID %d gefunden werden.";
	private static final String FACH_FEHLT = "Die ID des Faches darf nicht null sein.";
	private static final String KEIN_FACH_GEFUNDEN = "Es konnte kein Fach mit der ID %d gefunden werden.";
	private static final String KEIN_LEHRER_GEFUNDEN = "Es konnte kein Lehrer mit der angegebenen ID %d gefunden werden.";
	private static final String KEINE_SCHULE_GEFUNDEN = "Für die Schulnummer %d konnte keine passende Schule gefunden werden.";
	private static final String JAHRGANG_NICHT_GEFUNDEN = "Mindestens einer der angegebenen Jahrgang-IDs existiert nicht in der SVWS-Datenbank";
	private static final String ENTITAET_KURS = "Kurs";

	private final KurseRepository kurseRepository;
	private final KursSchuelerRepository kursSchuelerRepository;
	private final KurslehrerRepository kurslehrerRepository;
	private final SchuelerRepository schuelerRepository;
	private final JahrgangRepository jahrgangRepository;
	private final FachRepository fachRepository;
	private final LehrerRepository lehrerRepository;
	private final SchuleRepository schuleRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;
	private final KursMapper kursMapper;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repositories   die Repositories, die der Service benötigt
	 * @param kursMapper     der Mapper für die Konvertierung der Kurse
	 */
	public KursService(final KursRepositories repositories, final KursMapper kursMapper) {
		this.kurseRepository = repositories.kurseRepository();
		this.kursSchuelerRepository = repositories.kursSchuelerRepository();
		this.kurslehrerRepository = repositories.kurslehrerRepository();
		this.schuelerRepository = repositories.schuelerRepository();
		this.jahrgangRepository = repositories.jahrgangRepository();
		this.fachRepository = repositories.fachRepository();
		this.lehrerRepository = repositories.lehrerRepository();
		this.schuleRepository = repositories.schuleRepository();
		this.schuljahresabschnitteRepository = repositories.schuljahresabschnitteRepository();
		this.schuelerLeistungsdatenRepository = repositories.schuelerLeistungsdatenRepository();
		this.kursMapper = kursMapper;
	}

	/**
	 * Gibt die Daten des Kurses mit der angegebenen ID einschließlich der Schüler zurück.
	 *
	 * @param id   die ID des Kurses
	 *
	 * @return die Daten des Kurses
	 */
	public KursDaten getById(final long id) {
		final var kurs = loadKurs(id);
		return mapKurse(List.of(kurs), true).getFirst();
	}

	/**
	 * Gibt die Kurse des Schuljahresabschnitts mit der angegebenen ID zurück. Ist der Abschnitt null, so
	 * werden die Kurse aller Abschnitte zurückgegeben. Die Kurse sind nach ihrer Sortierung sortiert und
	 * enthalten nur Jahrgänge, die noch existieren.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts oder null für alle Abschnitte
	 * @param mitSchueler              gibt an, ob die Schüler der Kurse mit geladen werden sollen
	 *
	 * @return die Liste der Kurse, ggf. leer
	 */
	public List<KursDaten> getListBySchuljahresabschnitt(final Long idSchuljahresabschnitt, final boolean mitSchueler) {
		final var kurse = mapKurse(loadKurse(idSchuljahresabschnitt), mitSchueler);
		removeUnbekannteJahrgaenge(kurse);
		kurse.sort(Comparator.comparingInt(k -> k.sortierung));

		return kurse;
	}

	/**
	 * Gibt die Kurse mit den angegebenen IDs zurück. Zu unbekannten IDs wird kein Kurs geliefert.
	 *
	 * @param ids             die IDs der Kurse, null wird wie eine leere Liste behandelt
	 * @param mitSchueler     gibt an, ob die Schüler der Kurse mit geladen werden sollen
	 *
	 * @return die Liste der Kurse, ggf. leer
	 */
	public List<KursDaten> getListByIds(final Collection<Long> ids, final boolean mitSchueler) {
		if ((ids == null) || ids.isEmpty()) {
			return new ArrayList<>();
		}
		return mapKurse(kurseRepository.findListByIds(ids), mitSchueler);
	}

	/**
	 * Legt einen neuen Kurs an. Nicht angegebene optionale Felder werden mit den Standardwerten belegt.
	 * Die Operation läuft in einer Transaktion.
	 *
	 * @param request   die Daten des neuen Kurses
	 *
	 * @return die Daten des angelegten Kurses ohne Schüler
	 */
	public KursDaten create(final KursCreateRequest request) {
		return TransactionSupport.transactional(() -> {
			validateCreate(request);
			final var jahrgaenge = loadJahrgaenge(request.idJahrgaenge);
			final var kurs = kursMapper.toDomain(request, jahrgaenge);
			return mapKurse(List.of(kurseRepository.create(kurs)), false).getFirst();
		});
	}

	/**
	 * Ändert die im Request gesetzten Attribute des Kurses mit der angegebenen ID. Wird der Lehrer geändert, so
	 * wird er auch bei den Leistungsdaten des Kurses als Fachlehrer übernommen. Die Operation läuft in einer Transaktion.
	 *
	 * @param id        die ID des Kurses
	 * @param request   die zu ändernden Attribute
	 *
	 * @return die Daten des geänderten Kurses einschließlich der Schüler
	 */
	public KursDaten patch(final long id, final KursPatchRequest request) {
		return TransactionSupport.transactional(() -> {
			final var kurs = loadKurs(id);
			validatePatch(request);
			applyPatch(request, kurs);
			kurseRepository.update(kurs);
			updateFachlehrerIfLehrerPatched(request, kurs);
			return mapKurse(List.of(kurs), true).getFirst();
		});
	}

	/**
	 * Löscht die Kurse mit den angegebenen IDs. Abhängige Daten werden von der Datenbank mit gelöscht bzw. auf null gesetzt.
	 * Die Operation läuft in einer Transaktion.
	 *
	 * @param ids   die IDs der zu löschenden Kurse
	 *
	 * @return je ID das Ergebnis der Löschoperation, aufsteigend nach ID sortiert
	 */
	public List<SimpleOperationResponse> delete(final List<Long> ids) {
		return TransactionSupport.transactional(() -> BulkDeleteUtils.delete(ids, kurseRepository, k -> k.ID, ENTITAET_KURS));
	}

	private DTOKurs loadKurs(final long id) {
		return kurseRepository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, KEIN_KURS_GEFUNDEN.formatted(id)));
	}

	private void validateCreate(final KursCreateRequest request) {
		validateSchuljahresabschnitt(request.idSchuljahresabschnitt);
		validateFach(request.idFach);
		validateLehrer(request.idLehrer);
		validateSchulnummer(request.schulnummer);
	}

	private void validatePatch(final KursPatchRequest request) {
		request.idFach.ifPresent(this::validateFach);
		request.idLehrer.ifPresent(this::validateLehrer);
		request.schulnummer.ifPresent(this::validateSchulnummer);
	}

	private void validateSchuljahresabschnitt(final Long idSchuljahresabschnitt) {
		if (!schuljahresabschnitteRepository.existsById(idSchuljahresabschnitt)) {
			throw new ApiOperationException(Status.NOT_FOUND, KEIN_ABSCHNITT_GEFUNDEN.formatted(idSchuljahresabschnitt));
		}
	}

	private void validateFach(final Long idFach) {
		if (idFach == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, FACH_FEHLT);
		}
		if (fachRepository.findById(idFach).isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, KEIN_FACH_GEFUNDEN.formatted(idFach));
		}
	}

	private void validateLehrer(final Long idLehrer) {
		if ((idLehrer != null) && !lehrerRepository.existsById(idLehrer)) {
			throw new ApiOperationException(Status.NOT_FOUND, KEIN_LEHRER_GEFUNDEN.formatted(idLehrer));
		}
	}

	private void validateSchulnummer(final Integer schulnummer) {
		if ((schulnummer != null) && !schuleRepository.existsBySchulnummer(String.valueOf(schulnummer))) {
			throw new ApiOperationException(Status.NOT_FOUND, KEINE_SCHULE_GEFUNDEN.formatted(schulnummer));
		}
	}

	private List<DTOJahrgang> loadJahrgaenge(final List<Long> idsJahrgaenge) {
		if (idsJahrgaenge == null) {
			return List.of();
		}
		final var jahrgaenge = jahrgangRepository.findMapByIds(idsJahrgaenge);
		if (jahrgaenge.size() != idsJahrgaenge.size()) {
			throw new ApiOperationException(Status.BAD_REQUEST, JAHRGANG_NICHT_GEFUNDEN);
		}
		return idsJahrgaenge.stream()
				.map(jahrgaenge::get)
				.toList();
	}

	private void applyPatch(final KursPatchRequest request, final DTOKurs kurs) {
		final var jahrgaenge = request.idJahrgaenge.map(this::loadJahrgaenge).orElse(List.of());
		kursMapper.patch(request, kurs, jahrgaenge);
	}

	/**
	 * Übernimmt den geänderten Lehrer des Kurses als Fachlehrer in die Leistungsdaten der Schüler des Kurses.
	 * Ohne geänderten Lehrer im Request passiert nichts.
	 *
	 * @param request   die zu ändernden Attribute
	 * @param kurs      der bereits geänderte Kurs
	 */
	private void updateFachlehrerIfLehrerPatched(final KursPatchRequest request, final DTOKurs kurs) {
		if (request.idLehrer.isPresent()) {
			updateFachlehrer(kurs);
		}
	}

	private void updateFachlehrer(final DTOKurs kurs) {
		final var abweichend = schuelerLeistungsdatenRepository.findListByKurs(kurs.ID).stream()
				.filter(l -> !Objects.equals(l.Fachlehrer_ID, kurs.Lehrer_ID))
				.toList();
		if (abweichend.isEmpty()) {
			return;
		}
		abweichend.forEach(l -> l.Fachlehrer_ID = kurs.Lehrer_ID);
		schuelerLeistungsdatenRepository.update(abweichend);
	}

	private List<DTOKurs> loadKurse(final Long idSchuljahresabschnitt) {
		return (idSchuljahresabschnitt == null)
				? kurseRepository.getAll()
				: kurseRepository.getListBySchuljahresabschnitt(idSchuljahresabschnitt);
	}

	private List<KursDaten> mapKurse(final List<DTOKurs> kurse, final boolean mitSchueler) {
		if (kurse.isEmpty()) {
			return new ArrayList<>();
		}

		final var idsKurse = kurse.stream()
				.map(k -> k.ID)
				.toList();
		final var zusatzkraefte = kurslehrerRepository.getMapZusatzkraefte(idsKurse);
		final var schueler = mitSchueler ? loadSchueler(idsKurse) : Map.<Long, List<Schueler>>of();

		return kurse.stream()
				.map(kurs -> mapKurs(
						kurs,
						zusatzkraefte.getOrDefault(kurs.ID, Collections.emptyList()),
						schueler.getOrDefault(kurs.ID, Collections.emptyList())))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private KursDaten mapKurs(final DTOKurs dtoKurs, final List<DTOKursLehrer> zusatzkraefte, final List<Schueler> schueler) {
		final var kurs = kursMapper.toApi(dtoKurs);

		zusatzkraefte.stream()
				.map(kursMapper::toApi)
				.forEach(kurs.weitereLehrer::add);
		kurs.schueler.addAll(schueler);

		return kurs;
	}

	private Map<Long, List<Schueler>> loadSchueler(final List<Long> idsKurse) {
		final var zuordnung = kursSchuelerRepository.getMapByKurse(idsKurse);
		final var idsSchueler = zuordnung.values().stream()
				.flatMap(List::stream)
				.map(ks -> ks.Schueler_ID)
				.distinct()
				.toList();
		final var dtosSchueler = schuelerRepository.findMapByIds(idsSchueler);

		return zuordnung.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> mapSchueler(e.getValue(), dtosSchueler)));
	}

	private List<Schueler> mapSchueler(final List<DTOKursSchueler> kursSchueler, final Map<Long, DTOSchueler> dtosSchueler) {
		return kursSchueler.stream()
				.map(ks -> dtosSchueler.get(ks.Schueler_ID))
				.filter(Objects::nonNull)
				.filter(dto -> !hatSchuelerLoeschvermerk(dto))
				// TODO Abschlussjahrgang bestimmen
				.map(SchuelerMapper.INSTANCE::toSchueler)
				.toList();
	}

	private boolean hatSchuelerLoeschvermerk(final DTOSchueler schueler) {
		return Boolean.TRUE.equals(schueler.hatLoeschvermerk);
	}

	private void removeUnbekannteJahrgaenge(final List<KursDaten> kurse) {
		final var idsJahrgaenge = jahrgangRepository.getAll().stream()
				.map(j -> j.ID)
				.collect(Collectors.toSet());
		kurse.forEach(kurs -> kurs.idJahrgaenge.removeIf(id -> !idsJahrgaenge.contains(id)));
	}

}
