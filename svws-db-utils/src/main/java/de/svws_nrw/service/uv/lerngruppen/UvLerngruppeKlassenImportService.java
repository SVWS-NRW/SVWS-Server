package de.svws_nrw.service.uv.lerngruppen;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppe;
import de.svws_nrw.core.data.uv.UvStundentafelFach;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppeRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import de.svws_nrw.service.uv.klassen.UvKlasseService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachService;

/**
 * Ein Service für das Erstellen von UV-Lerngruppen auf Basis von UV-Klassen und deren Stundentafeln.
 */
public final class UvLerngruppeKlassenImportService {

	private final UvKlasseService uvKlasseService;
	private final SchuljahresabschnittService schuljahresabschnittService;
	private final UvStundentafelFachService uvStundentafelFachService;
	private final UvLerngruppeRepository uvLerngruppeRepository;
	private final UvLerngruppeService uvLerngruppeService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param uvKlasseService                   der CRUD-Service für UV-Klassen
	 * @param schuljahresabschnittService       der Service für Schuljahresabschnitte
	 * @param uvStundentafelFachService         der CRUD-Service für UV-Stundentafel-Fächer
	 * @param uvLerngruppeRepository            das Repository für UV-Lerngruppen
	 * @param uvLerngruppeService               der CRUD-Service für UV-Lerngruppen
	 */
	public UvLerngruppeKlassenImportService(final UvKlasseService uvKlasseService,
			final SchuljahresabschnittService schuljahresabschnittService,
			final UvStundentafelFachService uvStundentafelFachService,
			final UvLerngruppeRepository uvLerngruppeRepository,
			final UvLerngruppeService uvLerngruppeService) {
		this.uvKlasseService = uvKlasseService;
		this.schuljahresabschnittService = schuljahresabschnittService;
		this.uvStundentafelFachService = uvStundentafelFachService;
		this.uvLerngruppeRepository = uvLerngruppeRepository;
		this.uvLerngruppeService = uvLerngruppeService;
	}

	private record KlassenKontext(
			List<UvKlasse> klassen,
			Map<Long, Integer> abschnittBySchuljahresabschnitt,
			Map<Long, List<UvStundentafelFach>> faecherByStundentafel,
			Set<String> bestehendeKlasseFachKombinationen) {
	}

	/**
	 * Erstellt für die angegebenen UV-Klassen Lerngruppen anhand der zugeordneten UV-Stundentafeln.
	 *
	 * @param idsKlassen   die IDs der Klassen
	 *
	 * @return die neu erstellten Lerngruppen
	 */
	public List<UvLerngruppe> createByKlassen(final Collection<Long> idsKlassen) {
		return transactional(() -> doCreateByKlassen(normalizeKlassenIds(idsKlassen)));
	}

	private List<UvLerngruppe> doCreateByKlassen(final List<Long> idsKlassen) {
		if (idsKlassen.isEmpty()) {
			return List.of();
		}

		final KlassenKontext kontext = ladeKontext(idsKlassen);
		final List<UvLerngruppeCreateRequest> requests = createRequests(kontext);
		return requests.isEmpty() ? List.of() : uvLerngruppeService.createMultiple(requests);
	}

	private List<Long> normalizeKlassenIds(final Collection<Long> idsKlassen) {
		if ((idsKlassen == null) || idsKlassen.isEmpty()) {
			return List.of();
		}
		return new ArrayList<>(new LinkedHashSet<>(idsKlassen));
	}

	private KlassenKontext ladeKontext(final List<Long> idsKlassen) {
		final List<UvKlasse> klassen = uvKlasseService.getList(idsKlassen);
		if (klassen.isEmpty()) {
			return new KlassenKontext(List.of(), Map.of(), Map.of(), Set.of());
		}

		return new KlassenKontext(
				klassen,
				mapAbschnittBySchuljahresabschnitt(klassen),
				mapFaecherByStundentafel(klassen),
				getBestehendeKlasseFachKombinationen(idsKlassen));
	}

	private Map<Long, Integer> mapAbschnittBySchuljahresabschnitt(final List<UvKlasse> klassen) {
		final Set<Long> idsSchuljahresabschnitte = klassen.stream()
				.map(klasse -> klasse.idSchuljahresabschnitt)
				.collect(LinkedHashSet::new, Set::add, Set::addAll);
		if (idsSchuljahresabschnitte.isEmpty()) {
			return Map.of();
		}

		final Map<Long, Integer> result = new HashMap<>();
		for (final Long idSchuljahresabschnitt : idsSchuljahresabschnitte) {
			final Schuljahresabschnitt abschnitt = schuljahresabschnittService.getById(idSchuljahresabschnitt);
			result.put(abschnitt.id, abschnitt.abschnitt);
		}
		return result;
	}

	private Map<Long, List<UvStundentafelFach>> mapFaecherByStundentafel(final List<UvKlasse> klassen) {
		final Set<Long> idsStundentafeln = klassen.stream()
				.map(klasse -> klasse.idStundentafel)
				.filter(Objects::nonNull)
				.collect(Collectors.toCollection(LinkedHashSet::new));
		if (idsStundentafeln.isEmpty()) {
			return Map.of();
		}

		final Map<Long, List<UvStundentafelFach>> result = new HashMap<>();
		for (final UvStundentafelFach fach : uvStundentafelFachService.getListByStundentafelIds(idsStundentafeln)) {
			result.computeIfAbsent(fach.idStundentafel, id -> new ArrayList<>()).add(fach);
		}
		return result;
	}

	private Set<String> getBestehendeKlasseFachKombinationen(final Collection<Long> idsKlassen) {
		final Set<String> result = new HashSet<>();
		for (final DTOUvLerngruppe lerngruppe : uvLerngruppeRepository.getListByKlassenIds(idsKlassen)) {
			if ((lerngruppe.Klasse_ID != null) && (lerngruppe.Fach_ID != null)) {
				result.add(createKlasseFachKey(lerngruppe.Klasse_ID, lerngruppe.Fach_ID));
			}
		}
		return result;
	}

	private List<UvLerngruppeCreateRequest> createRequests(final KlassenKontext kontext) {
		if (kontext.klassen().isEmpty()) {
			return List.of();
		}

		final Set<String> bekannteKombinationen = new HashSet<>(kontext.bestehendeKlasseFachKombinationen());
		final List<UvLerngruppeCreateRequest> result = new ArrayList<>();
		for (final UvKlasse klasse : kontext.klassen()) {
			result.addAll(createRequestsForKlasse(klasse, kontext, bekannteKombinationen));
		}
		return result;
	}

	private List<UvLerngruppeCreateRequest> createRequestsForKlasse(final UvKlasse klasse, final KlassenKontext kontext,
			final Set<String> bekannteKombinationen) {
		if (klasse.idStundentafel == null) {
			return List.of();
		}

		final Integer abschnitt = kontext.abschnittBySchuljahresabschnitt().get(klasse.idSchuljahresabschnitt);
		if (abschnitt == null) {
			return List.of();
		}

		final List<UvStundentafelFach> stundentafelFaecher = kontext.faecherByStundentafel().getOrDefault(klasse.idStundentafel, List.of());
		final List<UvLerngruppeCreateRequest> result = new ArrayList<>();
		for (final UvStundentafelFach stundentafelFach : stundentafelFaecher) {
			if (!isPassendesFach(stundentafelFach, abschnitt)) {
				continue;
			}
			final String key = createKlasseFachKey(klasse.id, stundentafelFach.idFach);
			if (!bekannteKombinationen.add(key)) {
				continue;
			}
			result.add(createRequest(klasse, stundentafelFach));
		}
		return result;
	}

	private boolean isPassendesFach(final UvStundentafelFach stundentafelFach, final int abschnitt) {
		return (stundentafelFach.abschnitt == abschnitt)
				&& (stundentafelFach.wochenstunden > 0);
	}

	private UvLerngruppeCreateRequest createRequest(final UvKlasse klasse, final UvStundentafelFach stundentafelFach) {
		final UvLerngruppeCreateRequest request = new UvLerngruppeCreateRequest();
		request.idPlanungsabschnitt = klasse.idPlanungsabschnitt;
		request.idKlasse = klasse.id;
		request.idFach = stundentafelFach.idFach;
		request.wochenstunden = stundentafelFach.wochenstunden;
		request.wochenstundenUnterrichtet = stundentafelFach.wochenstunden;
		request.koopAnzahlExterne = 0;
		return request;
	}

	private String createKlasseFachKey(final long idKlasse, final long idFach) {
		return idKlasse + "_" + idFach;
	}

}
