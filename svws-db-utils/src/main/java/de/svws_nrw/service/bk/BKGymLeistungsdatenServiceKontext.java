package de.svws_nrw.service.bk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schueler.Sprachendaten;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.bk.abi.BKGymLeistungenFach;
import de.svws_nrw.core.data.bk.abi.BKGymLeistungenFachHalbjahr;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.bk.BKGymFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.service.lehrer.LehrerAnrechnungsstundenService;
import de.svws_nrw.service.schueler.SchuelerSprachenfolgeService;
import de.svws_nrw.service.schueler.SchuelerSprachpruefungenService;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse stellt einen Daten-Kontext für den Service {@link LehrerAnrechnungsstundenService} bereit.
 */
public final class BKGymLeistungsdatenServiceKontext {

	/** Das Repository für den Zugriff auf die Schuldaten */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	/** Das Repository für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	/** Das Repository für den Zugriff auf die Fachdaten */
	private final FachRepository fachRepository;

	/** Das Repository für den Zugriff auf die Schüler-Lernabschnitte */
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;

	/** Das Repository für den Zugriff auf die Schüler-Leistungsdaten */
	private final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;

	/** Das Repository für den Zugriff auf die Jahrgänge */
	private final JahrgangRepository jahrgangRepository;

	/** Der Schuljahresabschnitt, in welchem die Leistungsdaten benötigt werden */
	private DTOSchuljahresabschnitte schuljahresabschnitt;

	/** Eine Map mit den Jahrgangsdaten zugeordnet zu der Jahrgangs-ID */
	private Map<Long, DTOJahrgang> mapJahrgaenge;

	/** Eine Map mit den Fachdaten zugeordnet zu der Fach-ID */
	private Map<Long, DTOFach> mapFaecher;

	/** Eine Map mit den Schuljahresabschnittsdaten zugeordnet zu der Schuljahresabschnitts-ID */
	private Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = new HashMap<>();

	/** Eine Map mit den Lernabschnitts-IDs zugeordnet zu der Schüler-ID */
	private Map<Long, List<Long>> mapLernabschnittIDs = new HashMap<>();

	/** Eine Map mit den Lernabschnittsdaten zugeordnet zu der Lernabschnitt-ID */
	private Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnittsdaten = new HashMap<>();

	/** Eine Map mit den Leistungsdaten nach Abschnitt-ID und Fach-ID */
	private HashMap2D<Long, Long, DTOSchuelerLeistungsdaten> mapLeistungsdaten = new HashMap2D<>();

	/** Eine Map mit den Sprachbelegungen nach der Schüler-ID */
	private Map<Long, List<Sprachbelegung>> mapSprachenfolgen = new HashMap<>();

	/** Eine Map mit den Sprachprüfungen nach der Schüler-ID */
	private Map<Long, List<Sprachpruefung>> mapSprachenpruefungen = new HashMap<>();


	/** Service zur Konvertierung der Schueler-Sprachenfolge in API-Objekte */
	private final SchuelerSprachenfolgeService schuelerSprachenfolgeService;

	/** Service zur Konvertierung der Schueler-Sprachpruefungen in API-Objekte */
	private final SchuelerSprachpruefungenService schuelerSprachpruefungenService;


	private BKGymLeistungsdatenServiceKontext(
			final EigeneSchuleRepository eigeneSchuleRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final FachRepository fachRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final SchuelerSprachenfolgeService schuelerSprachenfolgeService,
			final SchuelerSprachpruefungenService schuelerSprachpruefungenService,
			final JahrgangRepository jahrgangRepository) {
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.fachRepository = fachRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
		this.schuelerLeistungsdatenRepository = schuelerLeistungsdatenRepository;
		this.jahrgangRepository = jahrgangRepository;
		this.schuelerSprachenfolgeService = schuelerSprachenfolgeService;
		this.schuelerSprachpruefungenService = schuelerSprachpruefungenService;
	}


	/**
	 * Erstellt einen neuen Service-Kontext.
	 *
	 * @param eigeneSchuleRepository                     das Repository für die Schule
	 * @param schuljahresabschnitteRepository      das Repository für die Schuljahresabschnitte
	 * @param fachRepository                       das Repository für die Fächer
	 * @param schuelerLernabschnittRepository      das Repository für die Schüler-Lernabschnitte
	 * @param schuelerLeistungsdatenRepository     das Repository für die Schüler-Leistungs
	 * @param schuelerSprachenfolgeService         der Service für die Schüler-Sprachenfolge
	 * @param schuelerSprachpruefungenService      der Service für die Schüler-Sprachprüfungen
	 * @param jahrgangRepository                 das Repository für die Jahrgänge
	 *
	 * @return der neue Service-Kontext.
	 */
	public static BKGymLeistungsdatenServiceKontext of(final EigeneSchuleRepository eigeneSchuleRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final FachRepository fachRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final SchuelerSprachenfolgeService schuelerSprachenfolgeService,
			final SchuelerSprachpruefungenService schuelerSprachpruefungenService,
			final JahrgangRepository jahrgangRepository) {
		return new BKGymLeistungsdatenServiceKontext(eigeneSchuleRepository, schuljahresabschnitteRepository, fachRepository,
				schuelerLernabschnittRepository, schuelerLeistungsdatenRepository, schuelerSprachenfolgeService,
				schuelerSprachpruefungenService,
				jahrgangRepository);
	}


	/**
	 * Lädt die grundlegenden Daten für die Erstellung der BKGymLeistungsdaten aus der Datenbank.
	 * Die Reihenfolge der Repository-Aufrufe ist so gestaltet, dass die Zugriffe möglichst gebündelt stattfinden.
	 *
	 * @param idsSchueler   die Schueler-IDs
	 */
	public void fetchData(final Collection<Long> idsSchueler) {
		mapJahrgaenge = jahrgangRepository.getAll().stream().collect(Collectors.toMap(e -> e.ID, e -> e));
		// Bestimme zunächst die Schulspezifischen Informationen, insbesondere zum Schuljahresabschnitt
		final long idSchuljahresabschnitt = eigeneSchuleRepository.getFirst().Schuljahresabschnitts_ID;
		schuljahresabschnitt = schuljahresabschnitteRepository.getById(idSchuljahresabschnitt);
		final Schuljahresabschnitt schuljahresabschnittApi = SchuljahresabschnittService.toApi(schuljahresabschnitt);

		// Lade die Schuljahresabschnitte in den Cache, damit diese für die Sortierung der Lernabschnitte zur Verfügung stehen.
		mapSchuljahresabschnitte = schuljahresabschnitteRepository.getMap();

		// Bestimme die Lernabschnitts-IDs zu den Schüler-IDs, damit die Leistungsdaten der Schüler ermittelt werden können.
		ladeLernabschnittsIds(idsSchueler);

		// Lade die Lernabschnittsdaten in den Cache
		mapLernabschnittsdaten = schuelerLernabschnittRepository.getMapByLernabschnittID(idsSchueler);

		// Lade die SchuelerSprachenfolge in den Cache des zugehörigen Services
		mapSprachenfolgen = schuelerSprachenfolgeService.getMapSprachenfolgen(idsSchueler);

		// Lade die SchuelerSprachpruefungen in den Cache des zugehörigen Services
		mapSprachenpruefungen = schuelerSprachpruefungenService.getMapSprachenfolgen(idsSchueler, schuljahresabschnittApi);

		final var alleLernabschnittIDs = mapLernabschnittIDs.values().stream().flatMap(List::stream).toList();
		mapLeistungsdaten = schuelerLeistungsdatenRepository.getMapByLernabschnittsIds(alleLernabschnittIDs);

		// Lade die Fachdaten in den Cache
		mapFaecher = fachRepository.getMap();
	}


	/**
	 * Sortiert die Lernabschnitts-IDs in der Map mapLernabschnittIDs nach Schuljahr und Abschnitt, damit die Leistungsdaten in der richtigen Reihenfolge zurückgegeben werden können.
	 *
	 * @param idsSchueler   die IDs der Schüler, für welche die Leistungsdaten ermittelt werden sollen
	 */
	private void ladeLernabschnittsIds(final Collection<Long> idsSchueler) {
		mapLernabschnittIDs = schuelerLernabschnittRepository.getMapAllLernabschnittIDsBySchuelerIDs(idsSchueler);
		// Sortiere jede Liste nach Schuljahr, bei Gleichheit nach Abschnitt
		for (final var list : mapLernabschnittIDs.values()) {
			list.sort((id1, id2) -> {
				final var sja1 = schuljahresabschnitteRepository.getById(schuelerLernabschnittRepository.getById(id1).Schuljahresabschnitts_ID);
				final var sja2 = schuljahresabschnitteRepository.getById(schuelerLernabschnittRepository.getById(id2).Schuljahresabschnitts_ID);
				return (sja1.Jahr != sja2.Jahr) ? Integer.compare(sja1.Jahr, sja2.Jahr) : Integer.compare(sja1.Abschnitt, sja2.Abschnitt);
			});
			// Bereinigung: Entferne wiederholte Jahre, die anhand des gleichen Jahrgangs ermittelt werden.
			final Set<Long> toRemove = new HashSet<>();
			for (int i = 0; i <= list.size() - 3; i++) {
				final var dto1 = schuelerLernabschnittRepository.getById(list.get(i));
				final var dto3 = schuelerLernabschnittRepository.getById(list.get(i + 2));
				if (Objects.equals(dto1.Jahrgang_ID, dto3.Jahrgang_ID)) {
					//TODO: Meldung ausgeben, wenn die Flags Wiederholung nicht auf '+' und SemesterWertung nicht auf '-' gesetzt sind,
					// da dies eigentlich nicht der Fall sein sollte.
					toRemove.add(list.get(i));
				}
			}
			list.removeIf(toRemove::contains);
		}
	}


	/**
	 * Gibt das aktuelle Schuljahr zurück, für welches die Leistungsdaten geladen wurden.
	 *
	 * @return das aktuelle Schuljahr
	 */
	public Integer getAktuellesSchuljahr() {
		return schuljahresabschnitt.Jahr;
	}


	/**
	 * Gibt den aktuellen Jahrgang des Schülers zurück
	 *
	 * @param idSchueler   die ID des Schülers, für welchen der aktuelle Jahrgang ermittelt werden soll
	 *
	 * @return der aktuelle Jahrgang
	 */
	public String getAktuellerJahrgang(final Long idSchueler) {
		// aktueller LernabschnittId
		final var lernabschnittIDs = mapLernabschnittIDs.get(idSchueler);
		if ((lernabschnittIDs == null) || lernabschnittIDs.isEmpty()) {
			return null;
		}
		final var idAbschnitt = lernabschnittIDs.getLast();
		final var dtoAbschnitt = schuelerLernabschnittRepository.getById(idAbschnitt);
		final DTOJahrgang dtoAktJahrgang = jahrgangRepository.getById(dtoAbschnitt.Jahrgang_ID);
		final Jahrgaenge aktJahrgang = (dtoAktJahrgang.ASDJahrgang == null) ? null : Jahrgaenge.data().getWertBySchluessel(dtoAktJahrgang.ASDJahrgang);
		return (aktJahrgang == null) ? null : aktJahrgang.daten(schuljahresabschnitt.Jahr).kuerzel;
	}


	/**
	 * Gibt die Sprachbelegungen (Sprachenfolge) des Schülers zurück.
	 *
	 * @param idSchueler   die ID des Schülers, für welchen die Sprachbelegungen ermittelt werden sollen
	 *
	 * @return die Sprachbelegungen (Sprachenfolge) des Schülers
	 */
	public @NotNull List<Sprachbelegung> getSprachenfolge(final Long idSchueler) {
		return mapSprachenfolgen.getOrDefault(idSchueler, new ArrayList<>());
	}


	/**
	 * Gibt die Sprachprüfungen des Schülers zurück.
	 *
	 * @param idSchueler   die ID des Schülers, für welchen die Sprachprüfungen ermittelt werden sollen
	 *
	 * @return die Sprachprüfungen des Schülers
	 */
	public @NotNull List<Sprachpruefung> getSprachpruefungen(final Long idSchueler) {
		return mapSprachenpruefungen.getOrDefault(idSchueler, new ArrayList<>());
	}


	/**
	 * Bestimmt die bewerteten Halbjahre in der gymnasialen Oberstufe
	 *
	 * @param idSchueler   die ID des Schülers, für welchen die bewerteten Halbjahre ermittelt werden sollen
	 *
	 * @return die bewerteten Halbjahre in der gymnasialen Oberstufe
	 */
	public @NotNull boolean[] getBewerteteHalbjahre(final Long idSchueler) {
		final boolean[] bewertetesHalbjahr = new boolean[GostHalbjahr.maxHalbjahre];
		final var lernabschnittIDs = mapLernabschnittIDs.get(idSchueler);
		if ((lernabschnittIDs == null) || lernabschnittIDs.isEmpty()) {
			return bewertetesHalbjahr;
		}
		for (final var idAbschnitt : lernabschnittIDs) {
			final var abschnitt = mapLernabschnittsdaten.get(idAbschnitt);
			if (abschnitt == null) {
				continue;
			}
			final DTOJahrgang jahrgang = mapJahrgaenge.get(abschnitt.Jahrgang_ID);
			final GostHalbjahr halbjahr = GostHalbjahr.fromBkJahrgangUndHalbjahr(jahrgang.ASDJahrgang,
					mapSchuljahresabschnitte.get(abschnitt.Schuljahresabschnitts_ID).Abschnitt);
			if (halbjahr != null) {
				final Map<Long, DTOSchuelerLeistungsdaten> mapLeistungsdatenAbschnitt = mapLeistungsdaten.getSubMapOrNull(abschnitt.ID);
				bewertetesHalbjahr[halbjahr.id] = (mapLeistungsdatenAbschnitt != null) && (!mapLeistungsdatenAbschnitt.isEmpty());
			}
		}
		return bewertetesHalbjahr;
	}


	/**
	 * Gibt die Fächer des Schülers zurück, für welche Leistungsdaten in der gymnasialen Oberstufe vorliegen.
	 * Zu jedem Fach werden die Informationen zum Fach und die Informationen zu den Belegungen in jedem Halbjahr zurückgegeben.
	 *
	 * @param idSchueler   die ID des Schülers, für welchen die Fächer ermittelt werden sollen
	 * @param sprachendaten
	 *
	 * @return die Fächer des Schülers, für welche Leistungsdaten in der gymnasialen Oberstufe vorliegen
	 */
	public List<BKGymLeistungenFach> getFaecher(final Long idSchueler, final Sprachendaten sprachendaten) {
		final var lernabschnittIDs = mapLernabschnittIDs.get(idSchueler);
		if ((lernabschnittIDs == null) || lernabschnittIDs.isEmpty()) {
			return List.of();
		}

		final Map<Long, List<DTOSchuelerLeistungsdaten>> mapLeistungsdatenJeFach = getMapLeistungsdatenJeFach(lernabschnittIDs);

		if (mapLeistungsdatenJeFach.isEmpty()) {
			return List.of();
		}

		// Erzeuge pro Fach ein API-Objekt. Weitere fach-/halbjahresspezifische Details folgen in einem separaten Schritt.
		return mapLeistungsdatenJeFach.entrySet().stream().map(entry -> {
			final var fach = new BKGymLeistungenFach();
			setFachDaten(fach, entry.getKey());
			fach.abiturfach = getAbiturfachNummer(entry.getValue());
			// Prüfe ggf., ob eine Sprache fortgeführt wurde oder nicht
			final String fremdsprache = BKGymFaecherManager.getFremdsprache(fach.fach);
			if (fremdsprache != null) {
				fach.istFSNeu = (SprachendatenUtils.istNeueinsetzbareSpracheInGOSt(sprachendaten, fremdsprache));
			}

			fach.belegungen.addAll(getBelegungen(entry.getValue()));
			return fach;
		}).toList();
	}


	/**
	 * Gruppiert die Leistungsdaten eines Schülers aus allen Lernabschnitten in zeitlicher Reihenfolge nach Fach-ID.
	 *
	 * @param lernabschnittIDs   die Lernabschnitts-IDs des Schülers
	 *
	 * @return eine Map von Fach-ID auf die zugehörigen Leistungsdaten
	 */
	private Map<Long, List<DTOSchuelerLeistungsdaten>> getMapLeistungsdatenJeFach(final List<Long> lernabschnittIDs) {
		final Map<Long, List<DTOSchuelerLeistungsdaten>> mapLeistungsdatenJeFach = new LinkedHashMap<>();
		for (final var idAbschnitt : lernabschnittIDs) {
			final Map<Long, DTOSchuelerLeistungsdaten> mapLeistungsdatenAbschnitt = mapLeistungsdaten.getSubMapOrNull(idAbschnitt);
			if ((mapLeistungsdatenAbschnitt == null) || mapLeistungsdatenAbschnitt.isEmpty()) {
				continue;
			}
			for (final var leistungsdaten : mapLeistungsdatenAbschnitt.values()) {
				mapLeistungsdatenJeFach.computeIfAbsent(leistungsdaten.Fach_ID, idFach -> new ArrayList<>()).add(leistungsdaten);
			}
		}
		return mapLeistungsdatenJeFach;
	}


	/**
	 * Setzt die Informationen zum Fach in dem API-Objekt, insbesondere die Informationen zum Fach aus der Fachdatenbank.
	 *
	 * @param fach      das API-Objekt, in welchem die Informationen zum Fach gesetzt werden sollen
	 * @param idFach    die ID des Faches, für welches die Informationen ermittelt und gesetzt werden sollen
	 */
	private void setFachDaten(final BKGymLeistungenFach fach, final long idFach) {
		fach.fach.id = idFach;
		final DTOFach dtoFach = mapFaecher.get(idFach);
		if (dtoFach == null) {
			return;
		}
		fach.fach.kuerzel = (dtoFach.StatistikKuerzel == null) ? "" : dtoFach.StatistikKuerzel;
		fach.fach.kuerzelAnzeige = dtoFach.Kuerzel;
		fach.fach.bezeichnung = dtoFach.Bezeichnung;
		fach.fach.sortierung = dtoFach.SortierungAllg;
		fach.fach.istFremdsprache = Boolean.TRUE.equals(dtoFach.IstFremdsprache);
		fach.fach.istFremdSpracheNeuEinsetzend = Boolean.TRUE.equals(dtoFach.IstMoeglichAlsNeueFremdspracheInSekII);
		final var sprache = dtoFach.Unterrichtssprache;
		fach.fach.biliSprache = (StringUtils.isNotBlank(sprache) && !"D".equals(sprache)) ? sprache.substring(0, 1) : null;
	}

	/**
	 * Gibt die Nummer des Abiturfaches zurück, sofern es sich bei einem der Leistungsdatensätze um ein Abiturfach handelt. Ansonsten wird null zurückgegeben.
	 *
	 * @param leistungsdatenFach   die Leistungsdaten eines Faches über alle Lernabschnitte
	 *
	 * @return die Nummer des Abiturfaches oder null
	 */
	private static Integer getAbiturfachNummer(final List<DTOSchuelerLeistungsdaten> leistungsdatenFach) {
		if (!leistungsdatenFach.isEmpty()) {
			final var leistungsdaten = leistungsdatenFach.get(leistungsdatenFach.size() - 1);
			final GostAbiturFach abiFach = GostAbiturFach.fromIDString(leistungsdaten.AbiFach);
			if (abiFach != null) {
				return abiFach.id;
			}
		}
		return null;
	}


	/**
	 * Gibt die Belegungen eines Faches in den verschiedenen Halbjahren zurück.
	 *
	 * @param leistungsdatenFach   die Leistungsdaten eines Faches über alle Lernabschnitte
	 *
	 * @return die Belegungen eines Faches in den verschiedenen Halbjahre
	 */
	private List<BKGymLeistungenFachHalbjahr> getBelegungen(final List<DTOSchuelerLeistungsdaten> leistungsdatenFach) {
		final List<BKGymLeistungenFachHalbjahr> belegungen = new ArrayList<>();
		for (final var leistungsdaten : leistungsdatenFach) {
			final GostKursart kursart = GostKursart.fromKuerzel(leistungsdaten.KursartAllg);
			final DTOSchuelerLernabschnittsdaten abschnitt = mapLernabschnittsdaten.get(leistungsdaten.Abschnitt_ID);
			final DTOFach dtoFach = mapFaecher.get(leistungsdaten.Fach_ID);
			final DTOSchuljahresabschnitte sjAbschnitt = (abschnitt != null) ? mapSchuljahresabschnitte.get(abschnitt.Schuljahresabschnitts_ID) : null;
			if ((abschnitt == null) || (kursart == null) || (dtoFach == null) || (sjAbschnitt == null)) {
				continue;
			}
			final var belegung = new BKGymLeistungenFachHalbjahr();
			belegung.id = leistungsdaten.ID;
			belegung.schuljahr = sjAbschnitt.Jahr;
			final GostHalbjahr halbjahr = GostHalbjahr.fromBkJahrgangUndHalbjahr(abschnitt.ASDJahrgang, sjAbschnitt.Abschnitt);
			if (halbjahr != null) {
				belegung.halbjahrKuerzel = halbjahr.kuerzel;
				belegung.abschnittGewertet = Boolean.TRUE.equals(abschnitt.SemesterWertung);
				belegung.jahrgang = getJahrgangKuerzel(abschnitt);
				belegung.idKurs = leistungsdaten.Kurs_ID;
				belegung.idFachlehrer = leistungsdaten.Fachlehrer_ID;
				belegung.notenKuerzel = leistungsdaten.NotenKrz;
				belegung.kursartKuerzel = kursart.kuerzel;
				belegung.istSchriftlich = BKGymUtils.istSchriftlich(kursart, leistungsdaten.Kursart, halbjahr);
				belegung.bilingualeSprache = BKGymUtils.getBilingualeSprache(dtoFach.Unterrichtssprache);
				belegung.wochenstunden = getIntOrZeroOnNull(leistungsdaten.Wochenstunden);
				belegung.fehlstundenGesamt = getIntOrZeroOnNull(leistungsdaten.FehlStd);
				belegung.fehlstundenUnentschuldigt = getIntOrZeroOnNull(leistungsdaten.uFehlStd);
				belegungen.add(belegung);
			}
		}
		return belegungen;
	}


	/**
	 * Gibt den übergebenen Integer-Wert zurück oder 0, wenn der übergebene Wert null ist.
	 *
	 * @param value der Integer-Wert, welcher auf null geprüft werden soll
	 *
	 * @return der übergebene Integer-Wert oder 0, wenn der übergebene Wert null ist
	 */
	private static int getIntOrZeroOnNull(final Integer value) {
		return (value == null) ? 0 : value;
	}


	/**
	 * Gibt das Kürzel des Jahrgangs zurück, welches in den Lernabschnittsdaten hinterlegt ist.
	 *
	 * @param abschnitt   die Lernabschnittsdaten, für welche das Jahrgangs-Kürzel ermittelt werden soll
	 *
	 * @return das Kürzel des Jahrgangs
	 */
	private String getJahrgangKuerzel(final DTOSchuelerLernabschnittsdaten abschnitt) {
		if (abschnitt.Jahrgang_ID == null) {
			return null;
		}
		final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(abschnitt.Jahrgang_ID);
		if ((dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null)) {
			return null;
		}
		final Jahrgaenge jahrgang = Jahrgaenge.data().getWertBySchluessel(dtoJahrgang.ASDJahrgang);
		if (jahrgang == null) {
			return null;
		}
		final DTOSchuljahresabschnitte abschnittSchuljahr = mapSchuljahresabschnitte.get(abschnitt.Schuljahresabschnitts_ID);
		if (abschnittSchuljahr == null) {
			return null;
		}
		return jahrgang.daten(abschnittSchuljahr.Jahr).kuerzel;
	}
}
