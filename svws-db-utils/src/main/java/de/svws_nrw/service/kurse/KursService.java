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
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.kurse.KursMapper;
import de.svws_nrw.mapper.schueler.SchuelerMapper;
import de.svws_nrw.repo.kurse.KursSchuelerRepository;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.kurse.KurslehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den lesenden Zugriff auf Kurse mit den Core-DTOs {@link KursDaten}.
 */
public final class KursService {

	private static final String KEIN_KURS_GEFUNDEN = "Kein Kurs mit der ID %d gefunden.";

	private final KurseRepository kurseRepository;
	private final KursSchuelerRepository kursSchuelerRepository;
	private final KurslehrerRepository kurslehrerRepository;
	private final SchuelerRepository schuelerRepository;
	private final JahrgangRepository jahrgangRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param kurseRepository            das Repository für den Zugriff auf die Kurse
	 * @param kursSchuelerRepository     das Repository für den Zugriff auf die Kurs-Schüler-Zuordnung
	 * @param kurslehrerRepository       das Repository für den Zugriff auf die Zusatzkräfte der Kurse
	 * @param schuelerRepository         das Repository für den Zugriff auf die Schüler
	 * @param jahrgangRepository         das Repository für den Zugriff auf die Jahrgänge
	 */
	public KursService(
			final KurseRepository kurseRepository,
			final KursSchuelerRepository kursSchuelerRepository,
			final KurslehrerRepository kurslehrerRepository,
			final SchuelerRepository schuelerRepository,
			final JahrgangRepository jahrgangRepository) {
		this.kurseRepository = kurseRepository;
		this.kursSchuelerRepository = kursSchuelerRepository;
		this.kurslehrerRepository = kurslehrerRepository;
		this.schuelerRepository = schuelerRepository;
		this.jahrgangRepository = jahrgangRepository;
	}

	/**
	 * Gibt die Daten des Kurses mit der angegebenen ID einschließlich der Schüler zurück.
	 *
	 * @param id   die ID des Kurses
	 *
	 * @return die Daten des Kurses
	 */
	public KursDaten getById(final long id) {
		final var kurs = kurseRepository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, KEIN_KURS_GEFUNDEN.formatted(id)));
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
		final var kurs = KursMapper.INSTANCE.toKursDaten(dtoKurs);

		zusatzkraefte.stream()
				.map(KursMapper.INSTANCE::toKursLehrer)
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
