package de.svws_nrw.service.schueler.liste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholung;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.SchuelerListeEintragComparator;
import de.svws_nrw.mapper.schueler.SchuelerListeEintragMapper;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.gost.GostSchuelerWiederholungRepository;
import de.svws_nrw.repo.kurse.KursSchuelerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service zur Erstellung der Liste der {@link SchuelerListeEintrag}-Objekte für die Schüler der Schule.
 * <p>
 * Die Liste enthält alle Schüler ohne Löschvermerk, für die ein Lernabschnitt im gewählten
 * Schuljahresabschnitt oder im aktuellen Abschnitt des Schülers vorliegt. Der Lernabschnitt
 * des gewählten Schuljahresabschnitts hat dabei Vorrang. Die Liste ist nach Klasse, Nachname und
 * Vorname sortiert. Bei Schulformen mit gymnasialer Oberstufe enthalten die Einträge den Abiturjahrgang
 * und, falls vorhanden, den Abiturjahrgang einer Wiederholung zweier GOSt-Halbjahre.
 * </p>
 */
public final class SchuelerListeEintragService {

	private static final String SCHULE_NICHT_GEFUNDEN = "Die Daten der eigenen Schule konnten nicht ermittelt werden.";

	/**
	 * Die Daten, welche für die Erstellung der Einträge benötigt werden und nicht vom einzelnen Schüler abhängen.
	 *
	 * @param schulform     die Schulform der Schule
	 * @param jahrgaenge    die Jahrgänge des Jahrgangskatalogs zugeordnet zu ihrer ID
	 * @param abschnitte    die Schuljahresabschnitte zugeordnet zu ihrer ID
	 */
	private record ListenKontext(Schulform schulform, Map<Long, DTOJahrgang> jahrgaenge, Map<Long, Schuljahresabschnitt> abschnitte) {
	}

	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittRepository lernabschnittRepository;
	private final KursSchuelerRepository kursSchuelerRepository;
	private final JahrgangRepository jahrgangRepository;
	private final GostSchuelerWiederholungRepository wiederholungRepository;
	private final EigeneSchuleService eigeneSchuleService;
	private final SchuljahresabschnittService schuljahresabschnittService;
	private final SchuelerListeEintragMapper mapper;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuelerRepository            das Repository für die Schüler
	 * @param lernabschnittRepository       das Repository für die Lernabschnitte der Schüler
	 * @param kursSchuelerRepository        das Repository für die Kurs-Schüler-Zuordnung
	 * @param jahrgangRepository            das Repository für den Jahrgangskatalog
	 * @param wiederholungRepository        das Repository für die Wiederholungen im folgenden Abiturjahrgang
	 * @param eigeneSchuleService           der Service für die Daten der eigenen Schule
	 * @param schuljahresabschnittService   der Service für die Schuljahresabschnitte
	 * @param mapper                        der Mapper zur Erstellung der Listeneinträge
	 */
	public SchuelerListeEintragService(
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository lernabschnittRepository,
			final KursSchuelerRepository kursSchuelerRepository,
			final JahrgangRepository jahrgangRepository,
			final GostSchuelerWiederholungRepository wiederholungRepository,
			final EigeneSchuleService eigeneSchuleService,
			final SchuljahresabschnittService schuljahresabschnittService,
			final SchuelerListeEintragMapper mapper) {
		this.schuelerRepository = schuelerRepository;
		this.lernabschnittRepository = lernabschnittRepository;
		this.kursSchuelerRepository = kursSchuelerRepository;
		this.jahrgangRepository = jahrgangRepository;
		this.wiederholungRepository = wiederholungRepository;
		this.eigeneSchuleService = eigeneSchuleService;
		this.schuljahresabschnittService = schuljahresabschnittService;
		this.mapper = mapper;
	}

	/**
	 * Erstellt die sortierte Liste der Schüler ohne Löschvermerk mit den Daten in Bezug auf den
	 * übergebenen Schuljahresabschnitt. Schüler ohne Lernabschnitt oder mit einem nicht auflösbaren
	 * Schuljahresabschnitt werden übersprungen. Bei Schulformen mit gymnasialer Oberstufe wird
	 * zusätzlich der Abiturjahrgang sowie der Abiturjahrgang einer Wiederholung bestimmt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts, dessen Daten verwendet werden
	 * @param nurAktive                gibt an, ob nur Schüler mit dem Status aktiv berücksichtigt werden
	 *
	 * @return die sortierte Liste der Schüler
	 */
	public List<SchuelerListeEintrag> getListe(final long idSchuljahresabschnitt, final boolean nurAktive) {
		final var abschnitt = schuljahresabschnittService.getById(idSchuljahresabschnitt);
		final var schueler = schuelerRepository.getListNichtGeloescht(nurAktive);
		if (schueler.isEmpty()) {
			return new ArrayList<>();
		}
		final var kontext = loadKontext();
		final var lernabschnitte = determineLernabschnitte(schueler, idSchuljahresabschnitt);

		final var liste = createEintraege(schueler, lernabschnitte, kontext);
		liste.sort(SchuelerListeEintragComparator.INSTANCE);
		addKurse(liste, idSchuljahresabschnitt);
		SchuelerListeAbiturjahrgang.addAbiturjahrgang(liste, kontext.schulform(), abschnitt.schuljahr, kontext.abschnitte());
		addAbiturjahrgangWiederholung(liste, kontext.schulform(), abschnitt.schuljahr);
		return liste;
	}

	/**
	 * Ergänzt bei den Einträgen den Abiturjahrgang einer Wiederholung zweier GOSt-Halbjahre im folgenden
	 * Abiturjahrgang. Dies erfolgt nur bei Schulformen mit gymnasialer Oberstufe. Einträge ohne Wiederholung
	 * erhalten keinen Wert.
	 *
	 * @param liste       die Einträge, welche ergänzt werden
	 * @param schulform   die Schulform der Schule
	 * @param schuljahr   das Schuljahr des gewählten Schuljahresabschnitts, für das die gymnasiale Oberstufe geprüft wird
	 */
	private void addAbiturjahrgangWiederholung(final List<SchuelerListeEintrag> liste, final Schulform schulform, final int schuljahr) {
		if (liste.isEmpty() || !SchuelerListeAbiturjahrgang.hasGymnasialeOberstufe(schulform, schuljahr)) {
			return;
		}
		final var idsSchueler = liste.stream()
				.map(e -> e.id)
				.toList();
		final var wiederholungen = wiederholungRepository.findMapByIds(idsSchueler);
		liste.forEach(eintrag -> eintrag.abiturjahrgangWiederholung = determineAbiturjahrgangWiederholung(wiederholungen.get(eintrag.id)));
	}

	/**
	 * Bestimmt den Abiturjahrgang einer Wiederholung.
	 *
	 * @param wiederholung   die Wiederholung des Schülers oder null
	 *
	 * @return der Abiturjahrgang oder null, falls der Schüler keine Wiederholung hat
	 */
	private static Integer determineAbiturjahrgangWiederholung(final DTOGostSchuelerWiederholung wiederholung) {
		return (wiederholung == null) ? null : wiederholung.Abi_Jahrgang;
	}

	/**
	 * Erstellt die sortierte Liste der Schüler ohne Löschvermerk mit den Daten in Bezug auf den
	 * aktuellen Schuljahresabschnitt der Schule.
	 *
	 * @param nurAktive   gibt an, ob nur Schüler mit dem Status aktiv berücksichtigt werden
	 *
	 * @return die sortierte Liste der Schüler
	 */
	public List<SchuelerListeEintrag> getListeAktuell(final boolean nurAktive) {
		return getListe(determineIdAktuellerAbschnitt(), nurAktive);
	}

	/**
	 * Bestimmt die ID des aktuellen Schuljahresabschnitts der Schule.
	 *
	 * @return die ID des aktuellen Schuljahresabschnitts
	 */
	private long determineIdAktuellerAbschnitt() {
		try {
			return eigeneSchuleService.getIdSchuljahresabschnitt();
		} catch (final RepositoryException e) {
			throw new ApiOperationException(Status.NOT_FOUND, e, SCHULE_NICHT_GEFUNDEN);
		}
	}

	/**
	 * Lädt die Daten, welche für alle Einträge der Liste gleich sind.
	 *
	 * @return der Kontext mit Schulform, Jahrgängen und Schuljahresabschnitten
	 */
	private ListenKontext loadKontext() {
		return new ListenKontext(eigeneSchuleService.getSchulform(), loadJahrgaenge(), loadAbschnitte());
	}

	/**
	 * Lädt die Jahrgänge des Jahrgangskatalogs.
	 *
	 * @return die Jahrgänge zugeordnet zu ihrer ID
	 */
	private Map<Long, DTOJahrgang> loadJahrgaenge() {
		return jahrgangRepository.getAll()
				.stream()
				.collect(Collectors.toMap(j -> j.ID, Function.identity()));
	}

	/**
	 * Lädt die Schuljahresabschnitte der Schule.
	 *
	 * @return die Schuljahresabschnitte zugeordnet zu ihrer ID
	 */
	private Map<Long, Schuljahresabschnitt> loadAbschnitte() {
		return schuljahresabschnittService.getList()
				.stream()
				.collect(Collectors.toMap(a -> a.id, Function.identity()));
	}

	/**
	 * Bestimmt die Lernabschnitte (Wechsel-Nr. 0) der Schüler. Der Lernabschnitt des gewählten
	 * Schuljahresabschnitts hat Vorrang vor dem Lernabschnitt im aktuellen Abschnitt des Schülers.
	 *
	 * @param schueler                 die Schüler
	 * @param idSchuljahresabschnitt   die ID des gewählten Schuljahresabschnitts
	 *
	 * @return die Lernabschnitte zugeordnet zur ID des Schülers
	 */
	private Map<Long, DTOSchuelerLernabschnittsdaten> determineLernabschnitte(final List<DTOSchueler> schueler, final long idSchuljahresabschnitt) {
		final var idsSchueler = schueler.stream()
				.map(s -> s.ID)
				.toList();
		final var result = new HashMap<>(lernabschnittRepository.getMapAktuelleBySchuelerIDs(idsSchueler));
		result.putAll(lernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(idsSchueler, idSchuljahresabschnitt));
		return result;
	}

	/**
	 * Erstellt die Einträge für die Schüler. Schüler ohne Lernabschnitt oder mit einem nicht auflösbaren
	 * Schuljahresabschnitt im Lernabschnitt werden übersprungen.
	 *
	 * @param schueler       die Schüler
	 * @param lernabschnitte die Lernabschnitte zugeordnet zur ID des Schülers
	 * @param kontext        die Daten, welche für alle Einträge gleich sind
	 *
	 * @return die Einträge in der Reihenfolge der Schüler
	 */
	private List<SchuelerListeEintrag> createEintraege(final List<DTOSchueler> schueler,
			final Map<Long, DTOSchuelerLernabschnittsdaten> lernabschnitte, final ListenKontext kontext) {
		return schueler.stream()
				.map(s -> createEintrag(s, lernabschnitte.get(s.ID), kontext))
				.flatMap(Optional::stream)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Erstellt den Eintrag für einen Schüler.
	 *
	 * @param schueler       der Schüler
	 * @param lernabschnitt  der Lernabschnitt des Schülers oder null
	 * @param kontext        die Daten, welche für alle Einträge gleich sind
	 *
	 * @return der Eintrag oder ein leeres Optional, falls kein Lernabschnitt vorliegt oder dessen Schuljahresabschnitt nicht aufgelöst werden kann
	 */
	private Optional<SchuelerListeEintrag> createEintrag(final DTOSchueler schueler, final DTOSchuelerLernabschnittsdaten lernabschnitt,
			final ListenKontext kontext) {
		if (lernabschnitt == null) {
			return Optional.empty();
		}
		final var abschnittLernabschnitt = kontext.abschnitte().get(lernabschnitt.Schuljahresabschnitts_ID);
		if (abschnittLernabschnitt == null) {
			return Optional.empty();
		}
		return Optional.of(mapper.toSchuelerListeEintrag(schueler, abschnittLernabschnitt.schuljahr, lernabschnitt, kontext.jahrgaenge(),
				kontext.schulform()));
	}

	/**
	 * Ergänzt bei den Einträgen die IDs der Kurse des Schuljahresabschnitts, welche von dem Schüler belegt wurden.
	 *
	 * @param liste                    die Einträge, welche ergänzt werden
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts, auf dessen Kurse die Zuordnung eingeschränkt wird
	 */
	private void addKurse(final List<SchuelerListeEintrag> liste, final long idSchuljahresabschnitt) {
		if (liste.isEmpty()) {
			return;
		}
		final var idsSchueler = liste.stream()
				.map(e -> e.id)
				.toList();
		final var mapKurse = kursSchuelerRepository.getMapBySchueler(idsSchueler, idSchuljahresabschnitt);
		liste.forEach(eintrag -> addKurseZuEintrag(eintrag, mapKurse.get(eintrag.id)));
	}

	/**
	 * Fügt dem Eintrag die IDs der übergebenen Kurs-Schüler-Zuordnungen hinzu.
	 *
	 * @param eintrag   der Eintrag, welcher ergänzt wird
	 * @param kurse     die Kurs-Schüler-Zuordnungen des Schülers oder null
	 */
	private static void addKurseZuEintrag(final SchuelerListeEintrag eintrag, final List<DTOKursSchueler> kurse) {
		if (kurse != null) {
			kurse.forEach(k -> eintrag.kurse.add(k.Kurs_ID));
		}
	}

}
