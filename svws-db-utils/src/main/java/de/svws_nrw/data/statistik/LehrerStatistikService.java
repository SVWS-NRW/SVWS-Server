package de.svws_nrw.data.statistik;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.data.lehrer.LehrerLehramtService;
import de.svws_nrw.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstundenService;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.repo.lehrer.LehrerAbschnittsdatenRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schule.SchuleRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Lehrerdaten für die Statistik
 */
public final class LehrerStatistikService {

	/** Das Repository für den Zugriff auf die Klassen-Daten */
	private final SchuleRepository schuleRepository;

	/** Das Repository für den Zugriff auf die Lehrer-Daten */
	private final LehrerRepository lehrerRepository;

	/** Das Repository für den Zugriff auf Abschnittsspezifische Lehrer-Daten */
	private final LehrerAbschnittsdatenRepository lehrerAbschnittsdatenRepository;

	/** Der Service für den Zugriff auf die Lehrämter */
	private final LehrerLehramtService lehrerLehramtService;

	/** Der Service für den Zugriff auf die Anrechnungsstunden, Mehr- und Minderleistungen */
	private final LehrerPersonalabschnittsdatenAnrechnungsstundenService lehrerPersonalabschnittsdatenAnrechnungsstundenService;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuleRepository                                         das Repository für die Schuldaten
	 * @param lehrerRepository                                         das Repository für die Lehrerdaten
	 * @param lehrerAbschnittsdatenRepository                          das Repository für den die Lehrerabschnittsdaten
	 * @param lehrerLehramtService                                     der Service für die Lehrämter
	 * @param lehrerPersonalabschnittsdatenAnrechnungsstundenService   der Service für die Lehrer-Anrechnungsstunden
	 */
	public LehrerStatistikService(final SchuleRepository schuleRepository,
			final LehrerRepository lehrerRepository,
			final LehrerAbschnittsdatenRepository lehrerAbschnittsdatenRepository,
			final LehrerLehramtService lehrerLehramtService,
			final LehrerPersonalabschnittsdatenAnrechnungsstundenService lehrerPersonalabschnittsdatenAnrechnungsstundenService) {
		this.schuleRepository = schuleRepository;
		this.lehrerRepository = lehrerRepository;
		this.lehrerAbschnittsdatenRepository = lehrerAbschnittsdatenRepository;
		this.lehrerLehramtService = lehrerLehramtService;
		this.lehrerPersonalabschnittsdatenAnrechnungsstundenService = lehrerPersonalabschnittsdatenAnrechnungsstundenService;
	}


	private static LehrerStatistikGesamt map(final DTOLehrer dtoLehrer, final DTOLehrerAbschnittsdaten dtoAbschnittsdaten,
			final List<LehrerLehramtEintrag> lehraemter, final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> anrechnungen,
			final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> mehrleistung,
			final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> minderleistung) {
		// Erzeuge das Core-DTO mit den grundlegenden Daten
		final var daten = new LehrerStatistikGesamt();
		daten.id = dtoLehrer.ID;
		daten.kuerzel = dtoLehrer.Kuerzel;
		daten.vorname = (dtoLehrer.Vorname == null) ? "" : dtoLehrer.Vorname;
		daten.nachname = (dtoLehrer.Nachname == null) ? "" : dtoLehrer.Nachname;
		daten.geburtsdatum = dtoLehrer.Geburtsdatum;
		daten.geschlecht = (dtoLehrer.Geschlecht == null) ? -1 : dtoLehrer.Geschlecht.id;
		daten.staatsangehoerigkeitID = (dtoLehrer.staatsangehoerigkeit == null) ? null : dtoLehrer.staatsangehoerigkeit.historie().getLast().iso3;
		daten.lehraemter.addAll(lehraemter);

		// Hänge die abschnittsrelevanten Lehrer-Daten an
		daten.rechtsverhaeltnis = dtoAbschnittsdaten.Rechtsverhaeltnis;
		daten.beschaeftigungsart = dtoAbschnittsdaten.Beschaeftigungsart;
		daten.einsatzstatus = dtoAbschnittsdaten.Einsatzstatus;
		daten.pflichtstundensoll = dtoAbschnittsdaten.PflichtstdSoll;
		daten.anrechnungen.addAll(anrechnungen);
		daten.mehrleistung.addAll(mehrleistung);
		daten.minderleistung.addAll(minderleistung);
		return daten;
	}


	/**
	 * Gibt die Liste aller Statistik-relevanten Lehrer für den an der Schule aktuellen Schuljahresabschnitt zurück.
	 *
	 * @return die Liste aller Statistik-relevanten Lehrer
	 */
	public @NotNull List<LehrerStatistikGesamt> getList() {
		// Bestimme den aktuellen Schuljahresabschnitt der Schule
		final long idSchuljahresabschnitt = schuleRepository.getSchuljahresabschnitt();

		// Bestimme zunächst die Statistik-Relevanten Lehrkräfte und deren IDs
		final List<DTOLehrer> listLehrer = lehrerRepository.getAllStatistikRelevant();
		final List<Long> listLehrerIDs = listLehrer.stream().map(l -> l.ID).toList();

		// Bestimme die zugehörigen Lernabschnittsdaten und speichere diese in einer Map mit Bezug zu der Lehrer-ID
		final Map<Long, DTOLehrerAbschnittsdaten> mapAbschnittsdaten =
				lehrerAbschnittsdatenRepository.getListByLehrerIdsAndSchuljahresabschnitt(listLehrerIDs, idSchuljahresabschnitt).stream()
						.collect(Collectors.toMap(a -> a.Lehrer_ID, a -> a));

		// Bestimme die Lehrämter
		final var mapLehraemter = lehrerLehramtService.getMapByLehrer(listLehrerIDs);

		// Bestimme die Anrechnungsstunden, Mehr- und Minderleistungen
		final var mapAnrechnungen = lehrerPersonalabschnittsdatenAnrechnungsstundenService.getMapAnrechungen(mapAbschnittsdaten.values());
		final var mapMehrleistungen = lehrerPersonalabschnittsdatenAnrechnungsstundenService.getMapMehrleistungen(mapAbschnittsdaten.values());
		final var mapMinderleistungen = lehrerPersonalabschnittsdatenAnrechnungsstundenService.getMapMinderleistungen(mapAbschnittsdaten.values());

		// Erstelle die Liste der Core-DTOs anhand der zuvor geladenen Daten
		return listLehrer.stream().map(dtoLehrer -> {
			final var dtoAbschnitt = mapAbschnittsdaten.getOrDefault(dtoLehrer.ID, new DTOLehrerAbschnittsdaten(-1, dtoLehrer.ID, idSchuljahresabschnitt));
			final var lehraemter = mapLehraemter.getOrDefault(dtoLehrer.ID, Collections.emptyList());
			final var anrechnungen = mapAnrechnungen.getOrDefault(dtoAbschnitt.ID, Collections.emptyList());
			final var mehrleistungen = mapMehrleistungen.getOrDefault(dtoAbschnitt.ID, Collections.emptyList());
			final var minderleistungen = mapMinderleistungen.getOrDefault(dtoAbschnitt.ID, Collections.emptyList());
			return map(dtoLehrer, dtoAbschnitt, lehraemter, anrechnungen, mehrleistungen, minderleistungen);
		}).toList();
	}

}
