package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.Sprachendaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.data.gost.GostLeistungenFachbelegung;
import de.svws_nrw.core.data.gost.GostLeistungenFachwahl;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFachUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.gost.GostSchuelerFachbelegungenRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;
import de.svws_nrw.service.schueler.SchuelerSprachdatenService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Ein Service für den Zugriff auf die Abiturdaten von Schülern basierend auf den
 * Leistungsdaten der Schüler - nicht aus den Abiturtabellen
 */
public class GostAbiturdatenService {

	private final BenutzerAllgemeinRepository benutzerRepository;
	private final JahrgangRepository jahrgangRepository;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;
	private final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;
	private final GostSchuelerFachbelegungenRepository gostSchuelerFachbelegungenRepository;

	private final BenutzerKompetenzService benutzerKompetenzService;
	private final SchuelerSprachdatenService schuelerSprachdatenService;
	private final GostFaecherService gostFaecherService;
	private final GostSchuelerService gostSchuelerService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                     das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param jahrgangRepository                   das Repository für den Zugriff auf die Jahrgänge
	 * @param schuelerRepository                     das Repository für den Zugriff auf die Schülerdaten
	 * @param schuelerLernabschnittRepository        das Repository für den Zugriff auf die Schüler-Lernabschnittsdaten
	 * @param schuelerLeistungsdatenRepository       das Repository für den Zugriff auf die Schüler-Leistungsdaten
	 * @param gostSchuelerFachbelegungenRepository   das Repository für den Zugriff auf die Schüler-Fachbelegungen
	 *
	 * @param benutzerKompetenzService               der Service für den Zugriff auf die Benutzerkompetenzen des angemeldeten Benutzers
	 * @param schuelerSprachdatenService             der Service für den Zugriff auf die Sprachdaten von Schülern
	 * @param gostFaecherService                     der Service für den Zugriff auf die Fächer der Oberstufe
	 * @param gostSchuelerService                    der Service für den Zugriff auf die Schüler der Oberstufe
	 */
	public GostAbiturdatenService(final BenutzerAllgemeinRepository benutzerRepository, final JahrgangRepository jahrgangRepository,
			final SchuelerRepository schuelerRepository, final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final GostSchuelerFachbelegungenRepository gostSchuelerFachbelegungenRepository,
			final BenutzerKompetenzService benutzerKompetenzService,
			final SchuelerSprachdatenService schuelerSprachdatenService,
			final GostFaecherService gostFaecherService,
			final GostSchuelerService gostSchuelerService) {
		this.benutzerRepository = benutzerRepository;
		this.jahrgangRepository = jahrgangRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
		this.schuelerLeistungsdatenRepository = schuelerLeistungsdatenRepository;
		this.gostSchuelerFachbelegungenRepository = gostSchuelerFachbelegungenRepository;
		this.benutzerKompetenzService = benutzerKompetenzService;
		this.schuelerSprachdatenService = schuelerSprachdatenService;
		this.gostFaecherService = gostFaecherService;
		this.gostSchuelerService = gostSchuelerService;
	}


	/**
	 * Ermittelt die für das Abitur relevanten Daten der gymnasialen Oberstufe für den Schüler
	 * mit der angegebenen ID aus den in der Datenbank gespeicherten Leistungsdaten.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die für das Abiturdaten
	 */
	public Abiturdaten get(final long idSchueler) {
		final var list = getList(List.of(idSchueler));
		if (list.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Es wurden keine Abiturdaten für einen Schüler mit der ID %d gefunden.".formatted(idSchueler));
		}
		return list.getFirst();
	}


	/**
	 * Bestimmt anhand der Menge der Lernabschnitte die Zuordnung der Abiturjahrgänge zu den einzelnen Schülern.
	 *
	 * @param lernabschnitte   die Lernabschnitte
	 * @param mapJahrgaenge    eine Map mit den Jahrgängen der Schule
	 *
	 * @return die Zuordnung der Abiturjahrgänge zu den IDs der Schüler
	 */
	private Map<Long, Integer> getSchuelerAbiturjahrgaenge(final Collection<DTOSchuelerLernabschnittsdaten> lernabschnitte,
			final Map<Long, DTOJahrgang> mapJahrgaenge) {
		// Lese die Schulform der Schule und die Jahrgänge an der Schule für die Berechnung des Abiturjahres ein
		final Benutzer user = this.benutzerRepository.getAktuellerBenutzer();
		final Schulform schulform = user.schuleGetSchulform();

		final Map<Long, Integer> result = new HashMap<>();
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			final Schulgliederung schulgliederung = (lernabschnitt.Schulgliederung == null)
					? Schulgliederung.getDefault(schulform)
					: Schulgliederung.data().getWertByKuerzel(lernabschnitt.Schulgliederung);
			final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
			final Jahrgaenge jahrgang = ((dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null))
					? null : Jahrgaenge.data().getWertBySchluessel(dtoJahrgang.ASDJahrgang);
			final Integer abiturjahr = DBUtilsGost.getAbiturjahr(schulform, schulgliederung,
					user.schuleGetSchuljahresabschnittByIdOrDefault(lernabschnitt.Schuljahresabschnitts_ID).schuljahr, jahrgang);
			if (abiturjahr == null) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Konnte für den Schüler mit der ID %d den Abiturjahrgang nicht bestimmen.".formatted(lernabschnitt.Schueler_ID));
			}
			result.put(lernabschnitt.Schueler_ID, abiturjahr);
		}
		return result;
	}


	/**
	 * Ermittelt die für das Abitur relevanten Daten der gymnasialen Oberstufe für die Schüler
	 * mit den angegebenen IDs aus den in der Datenbank gespeicherten Leistungsdaten und
	 * gibt eine Map zurück, welche der Schüler-ID die zugehrörigen Daten zuordnet
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Map mit den Abiturdaten, jeweiles zugeordnet zu den Schüler-IDs
	 */
	public Map<Long, Abiturdaten> getMap(final Collection<Long> idsSchueler) {
		return this.getList(idsSchueler).stream().collect(Collectors.toMap(a -> a.schuelerID, a -> a));
	}


	/**
	 * Ermittelt die für das Abitur relevanten Daten der gymnasialen Oberstufe für die Schüler
	 * mit dem angegebenen Abiturjahrgang aus den in der Datenbank gespeicherten Leistungsdaten.
	 *
	 * @param abijahrgang   der Abiturjahrgang
	 *
	 * @return die Liste mit den Abiturdaten
	 */
	public List<Abiturdaten> getListByAbiturjahrgang(final int abijahrgang) {
		return transactional(() -> {
			final List<Long> idsSchueler = gostSchuelerService.getByAbiturjahrgang(abijahrgang).stream().map(s -> s.ID).toList();
			return this.getList(idsSchueler);
		});
	}


	/**
	 * Ermittelt die für das Abitur relevanten Daten der gymnasialen Oberstufe für die Schüler
	 * mit dem angegebenen Abiturjahrgang aus den in der Datenbank gespeicherten Leistungsdaten und
	 * gibt eine Map zurück, welche der Schüler-ID die zugehrörigen Daten zuordnet
	 *
	 * @param abijahrgang   der Abiturjahrgang
	 *
	 * @return die Map mit den Abiturdaten, jeweiles zugeordnet zu den Schüler-IDs
	 */
	public Map<Long, Abiturdaten> getMapByAbiturjahrgang(final int abijahrgang) {
		return transactional(() -> {
			final List<Long> idsSchueler = gostSchuelerService.getByAbiturjahrgang(abijahrgang).stream().map(s -> s.ID).toList();
			return this.getList(idsSchueler).stream().collect(Collectors.toMap(a -> a.schuelerID, a -> a));
		});
	}


	/**
	 * Ermittelt die für das Abitur relevanten Daten der gymnasialen Oberstufe für die Schüler
	 * mit den angegebenen IDs aus den in der Datenbank gespeicherten Leistungsdaten.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die für das Abitur relevanten Daten für die Schüler mit den angegebenen IDs
	 */
	public List<Abiturdaten> getList(final Collection<Long> idsSchueler) {
		return transactional(() -> {
			if (idsSchueler.isEmpty()) {
				return new ArrayList<>();
			}

			if (!benutzerRepository.getAktuellerBenutzer().schuleHatGymOb()) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Schule hat eine Schulform ohne gymnasiale Oberstufe.");
			}

			// Prüfe zunächst die Schüler auf Existenz.
			final Map<Long, DTOSchueler> mapDtoSchueler = schuelerRepository.findMapByIds(idsSchueler);
			if (mapDtoSchueler.isEmpty() || (mapDtoSchueler.size() != idsSchueler.size())) {
				throw new ApiOperationException(Status.NOT_FOUND);
			}

			// Bestimme die aktuellen Lernabschnitte der Schüler zum übergebenen Schuljahresabschnitt und sammle sie in einer Map.
			final Map<Long, DTOSchuelerLernabschnittsdaten> mapSchuelerAktuellerLernabschnitt =
					schuelerLernabschnittRepository.getMapAktuelleBySchuelerIDs(mapDtoSchueler.keySet());
			if (mapDtoSchueler.size() != mapSchuelerAktuellerLernabschnitt.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht für alle Schüler wurde ein zugehöriger Lernabschnitt gefunden.");
			}

			// Bestimme alle Lernabschnitte der Oberstufe der einzelnen Schüler und sammle sie in einer Map, hole auch die Leistungsdaten dazu
			final Map<Long, List<DTOSchuelerLernabschnittsdaten>> mapSchuelerLernabschnitte = schuelerLernabschnittRepository
					.getMapGostLernabschnitteBySchuelerIDs(idsSchueler);
			final List<Long> idsSchuelerLernabschnitte = mapSchuelerLernabschnitte.values().stream().flatMap(l -> l.stream()).map(la -> la.ID).toList();
			final Map<Long, List<DTOSchuelerLeistungsdaten>> mapAbschnittsLeistungen =
					schuelerLeistungsdatenRepository.getMapListByLernabschnittsIds(idsSchuelerLernabschnitte);

			// Bestimme die Abiturjahrgänge für die Schüler und die zugehörigen Fächer-Manager
			final Map<Long, DTOJahrgang> mapJahrgaenge = this.jahrgangRepository.getMap();
			final Map<Long, Integer> mapAbiturjahrgaenge = this.getSchuelerAbiturjahrgaenge(mapSchuelerAktuellerLernabschnitt.values(), mapJahrgaenge);
			final List<Integer> abiturjahrgaenge = mapAbiturjahrgaenge.values().stream().distinct().toList();
			for (final int abiturjahrgang : abiturjahrgaenge) {
				benutzerKompetenzService.pruefeKompetenzGost(abiturjahrgang);
			}
			final Map<Integer, GostFaecherManager> mapGostFaecherManager = gostFaecherService.getMapGostFaecherManager(abiturjahrgaenge, false);

			// Bestimme die Sprachendaten der Schüler
			final Map<Long, Sprachendaten> mapSchuelerSpachendaten = schuelerSprachdatenService.getMap(idsSchueler);

			// Bestimme die bereits vorhandenen Leistungsdaten der Schülerlaufbahn
			final Map<Long, GostLeistungen> mapLeistungen = getMapLeistungsdaten(mapDtoSchueler.values(), mapSchuelerAktuellerLernabschnitt,
					mapSchuelerLernabschnitte, mapAbschnittsLeistungen, mapJahrgaenge, mapAbiturjahrgaenge, mapSchuelerSpachendaten, mapGostFaecherManager);

			// Bestimme die bisherigen Fachwahlen des Schülers
			final HashMap2D<Long, Long, DTOGostSchuelerFachbelegungen> mapFachwahlenBySchueler =
					gostSchuelerFachbelegungenRepository.getMap2DBySchuelerIDAndFachID(idsSchueler);

			// Die Liste für die zurückzugebenden Abiturdaten.
			final List<Abiturdaten> listAbiturdaten = new ArrayList<>();

			// Durchlaufe die Schüler und bestimme die Abiturdaten.
			for (final DTOSchueler schueler : mapDtoSchueler.values()) {
				final int abiturjahr = mapAbiturjahrgaenge.get(schueler.ID);
				final DTOSchuelerLernabschnittsdaten aktuellerLernabschnitt = mapSchuelerAktuellerLernabschnitt.get(schueler.ID);
				final GostFaecherManager gostFaecher = mapGostFaecherManager.get(abiturjahr);
				final GostLeistungen leistungen = mapLeistungen.get(schueler.ID);
				Map<Long, DTOGostSchuelerFachbelegungen> mapFachwahlen = mapFachwahlenBySchueler.getSubMapOrNull(schueler.ID);
				if (mapFachwahlen == null) {
					mapFachwahlen = new HashMap<>();
				}

				final Abiturdaten abidaten = new Abiturdaten();
				abidaten.schuelerID = schueler.ID;
				abidaten.abiturjahr = abiturjahr;
				abidaten.schuljahrAbitur = abidaten.abiturjahr - 1;
				abidaten.jahrgang = aktuellerLernabschnitt.ASDJahrgang;
				abidaten.sprachendaten = leistungen.sprachendaten;
				abidaten.bilingualeSprache = leistungen.bilingualeSprache;
				abidaten.projektKursThema = leistungen.projektkursThema;
				abidaten.projektkursLeitfach1Kuerzel = leistungen.projektkursLeitfach1Kuerzel;
				abidaten.projektkursLeitfach2Kuerzel = leistungen.projektkursLeitfach2Kuerzel;

				for (final GostHalbjahr hj : GostHalbjahr.values()) {
					abidaten.bewertetesHalbjahr[hj.id] = leistungen.bewertetesHalbjahr[hj.id];
				}

				for (final GostLeistungenFachwahl leistungenFach : leistungen.faecher) {
					GostHalbjahr letzteBelegungHalbjahr = null;   // das Halbjahr der letzten Belegung
					final AbiturFachbelegung fach = new AbiturFachbelegung();
					fach.fachID = leistungenFach.fach.id;
					fach.istFSNeu = leistungenFach.istFSNeu;
					fach.abiturFach = (GostAbiturFach.fromID(leistungenFach.abiturfach) == null) ? null : leistungenFach.abiturfach;
					fach.idReferenzfach = null;
					for (final GostLeistungenFachbelegung leistungenBelegung : leistungenFach.belegungen) {
						if (!leistungenBelegung.abschnittGewertet) {
							continue;
						}
						// Nehme jeweils die Kursart, welche beim letzten gewerteten Abschnitt eingetragen ist
						if (((letzteBelegungHalbjahr == null)
								|| (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).compareTo(letzteBelegungHalbjahr) > 0))
								&& (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) != null)) {
							letzteBelegungHalbjahr = GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel);
							fach.letzteKursart = (GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null) ? null
									: GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
						}

						// Erzeuge die zugehörige Belegung
						final AbiturFachbelegungHalbjahr belegung = new AbiturFachbelegungHalbjahr();
						belegung.halbjahrKuerzel = (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) == null) ? null
								: GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).kuerzel;
						belegung.kursartKuerzel = (GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null) ? null
								: GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
						if ("AT".equals(leistungenBelegung.notenKuerzel)) {
							final GostFach gostFach = gostFaecher.get(fach.fachID);
							if (Fach.SP == Fach.getBySchluesselOrDefault(gostFach.kuerzel)) {
								belegung.kursartKuerzel = "AT";
							}
						}
						belegung.schriftlich = leistungenBelegung.istSchriftlich;
						belegung.biliSprache = leistungenBelegung.bilingualeSprache;
						belegung.idKurs = leistungenBelegung.idKurs;
						belegung.lehrer = leistungenBelegung.lehrer;
						belegung.wochenstunden = leistungenBelegung.wochenstunden;
						belegung.fehlstundenGesamt = leistungenBelegung.fehlstundenGesamt;
						belegung.fehlstundenUnentschuldigt = leistungenBelegung.fehlstundenUnentschuldigt;
						belegung.notenkuerzel = (leistungenBelegung.notenKuerzel == null) ? "" : leistungenBelegung.notenKuerzel;
						fach.belegungen[GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).id] = belegung;
					}
					// Prüfe, ob das Fach in einem gewerteten Abschnitt belegt wurde. Wenn ja, dann füge es zu es den Fachbelegungen hinzu
					if (letzteBelegungHalbjahr != null) {
						abidaten.fachbelegungen.add(fach);
					}
				}

				// Bestimmt die Fehlstunden-Summe für den Block I (Qualifikationsphase) anhand der Fehlstunden bei den einzelnen Kurs-Belegungen.
				int block1FehlstundenGesamt = 0;
				int block1FehlstundenUnentschuldigt = 0;
				for (final AbiturFachbelegung fach : abidaten.fachbelegungen) {
					for (final AbiturFachbelegungHalbjahr belegung : fach.belegungen) {
						if ((belegung == null) || !GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).istQualifikationsphase()) {
							continue;
						}
						block1FehlstundenGesamt += belegung.fehlstundenGesamt;
						block1FehlstundenUnentschuldigt += belegung.fehlstundenUnentschuldigt;
					}
				}
				abidaten.block1FehlstundenGesamt = block1FehlstundenGesamt;
				abidaten.block1FehlstundenUnentschuldigt = block1FehlstundenUnentschuldigt;

				// Belegte Fächer aus den Leistungsdaten überprüfen und Abiturfach bzw. Referenzfach-ID setzen
				for (final AbiturFachbelegung fach : abidaten.fachbelegungen) {
					final DTOGostSchuelerFachbelegungen belegungPlanung = mapFachwahlen.get(fach.fachID);
					if (belegungPlanung == null) {
						fach.abiturFach = null;
						fach.idReferenzfach = null;
					} else {
						final GostAbiturFach tmpAbiturFach = GostAbiturFach.fromID(belegungPlanung.AbiturFach);
						fach.abiturFach = (tmpAbiturFach == null) ? null : tmpAbiturFach.id;
						fach.idReferenzfach = belegungPlanung.Referenzfach_ID;
					}
				}

				// Füge gewählte Fächer ohne Leistungsdaten hinzu
				for (final DTOGostSchuelerFachbelegungen belegungPlanung : mapFachwahlen.values()) {
					// filtere leere Belegungen aus der Planung
					if ((belegungPlanung.EF1_Kursart == null) && (belegungPlanung.EF2_Kursart == null)
							&& (belegungPlanung.Q11_Kursart == null) && (belegungPlanung.Q12_Kursart == null)
							&& (belegungPlanung.Q21_Kursart == null) && (belegungPlanung.Q22_Kursart == null)) {
						continue;
					}

					// Korrigiere ggf. falsche Werte bei Markiert_Q1, Markiert_Q2, Markiert_Q3 und Markiert_Q4
					if (belegungPlanung.Markiert_Q1 == null) {
						belegungPlanung.Markiert_Q1 = false;
					}
					if (belegungPlanung.Markiert_Q2 == null) {
						belegungPlanung.Markiert_Q2 = false;
					}
					if (belegungPlanung.Markiert_Q3 == null) {
						belegungPlanung.Markiert_Q3 = false;
					}
					if (belegungPlanung.Markiert_Q4 == null) {
						belegungPlanung.Markiert_Q4 = false;
					}

					// Prüfe, ob die Fachbelegung aufgrund von Leistungsdaten schon vorhanden ist
					AbiturFachbelegung fach = null;
					for (final AbiturFachbelegung fb : abidaten.fachbelegungen) {
						if (fb.fachID == belegungPlanung.Fach_ID) {
							fach = fb;
							break;
						}
					}
					// Es wurde keine Fachbelegung gefunden, also muss eine neue ergänzt werden.
					if (fach == null) {
						fach = new AbiturFachbelegung();
						fach.fachID = belegungPlanung.Fach_ID;
						abidaten.fachbelegungen.add(fach);
					}

					final GostFach gostFach = gostFaecher.get(fach.fachID);
					if (gostFach == null) {
						continue;
					}
					final Fach zulFach = Fach.getBySchluesselOrDefault(gostFach.kuerzel);
					fach.istFSNeu = zulFach.daten(abidaten.schuljahrAbitur).istFremdsprache && zulFach.daten(abidaten.schuljahrAbitur).nurSII;
					final GostAbiturFach tmpAbiturFach = GostAbiturFach.fromID(belegungPlanung.AbiturFach);
					fach.abiturFach = (tmpAbiturFach == null) ? null : tmpAbiturFach.id;
					fach.idReferenzfach = belegungPlanung.Referenzfach_ID;

					GostKursart fachKursart = GostKursart.GK;
					if ("PX".equals(gostFach.kuerzel)) {
						fachKursart = GostKursart.PJK;
					} else if ("VX".equals(gostFach.kuerzel)) {
						fachKursart = GostKursart.VTF;
					}
					if ((fach.belegungen[GostHalbjahr.EF1.id] == null) && (belegungPlanung.EF1_Kursart != null)) {
						setFachbelegung(fach, GostHalbjahr.EF1, belegungPlanung.EF1_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
					}
					if ((fach.belegungen[GostHalbjahr.EF2.id] == null) && (belegungPlanung.EF2_Kursart != null)) {
						setFachbelegung(fach, GostHalbjahr.EF2, belegungPlanung.EF2_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
					}
					if ((fach.belegungen[GostHalbjahr.Q11.id] == null) && (belegungPlanung.Q11_Kursart != null)) {
						setFachbelegung(fach, GostHalbjahr.Q11, belegungPlanung.Q11_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase,
								belegungPlanung.Markiert_Q1);
					}
					if ((fach.belegungen[GostHalbjahr.Q12.id] == null) && (belegungPlanung.Q12_Kursart != null)) {
						setFachbelegung(fach, GostHalbjahr.Q12, belegungPlanung.Q12_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase,
								belegungPlanung.Markiert_Q2);
					}
					if ((fach.belegungen[GostHalbjahr.Q21.id] == null) && (belegungPlanung.Q21_Kursart != null)) {
						setFachbelegung(fach, GostHalbjahr.Q21, belegungPlanung.Q21_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase,
								belegungPlanung.Markiert_Q3);
					}
					if ((fach.belegungen[GostHalbjahr.Q22.id] == null) && (belegungPlanung.Q22_Kursart != null)) {
						setFachbelegung(fach, GostHalbjahr.Q22, belegungPlanung.Q22_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase,
								belegungPlanung.Markiert_Q4);
					}
				}
				listAbiturdaten.add(abidaten);
			}

			return listAbiturdaten;
		});
	}


	private static void setFachbelegung(final AbiturFachbelegung fach, final GostHalbjahr halbjahr,
			final String belegungPlanungKursart, final GostKursart fachKursart, final int wochenstunden, final boolean istInAbiwertung) {
		final AbiturFachbelegungHalbjahr belegung = new AbiturFachbelegungHalbjahr();
		belegung.halbjahrKuerzel = halbjahr.kuerzel;
		belegung.kursartKuerzel = (belegungPlanungKursart == null) ? null : switch (belegungPlanungKursart) {
			case "AT" -> "AT";
			case "LK" -> "LK";
			case "ZK" -> "ZK";
			default -> fachKursart.toString();
		};
		belegung.schriftlich = (belegungPlanungKursart != null) && ("LK".equals(belegungPlanungKursart) || "S".equals(belegungPlanungKursart));
		belegung.wochenstunden = "LK".equals(belegungPlanungKursart) ? 5 : wochenstunden;
		belegung.block1gewertet = istInAbiwertung;
		belegung.block1kursAufZeugnis = true;
		belegung.notenkuerzel = null;
		fach.belegungen[halbjahr.id] = belegung;
		boolean isLetzte = true;
		for (GostHalbjahr hj = halbjahr.next(); hj != null; hj = hj.next()) {
			if (fach.belegungen[hj.id] != null) {
				isLetzte = false;
				break;
			}
		}
		if (isLetzte) {
			fach.letzteKursart = belegung.kursartKuerzel;
		}
	}


	private Map<Long, GostLeistungen> getMapLeistungsdaten(final Collection<DTOSchueler> listSchueler,
			final Map<Long, DTOSchuelerLernabschnittsdaten> mapSchuelerAktuellerLernabschnitt,
			final Map<Long, List<DTOSchuelerLernabschnittsdaten>> mapSchuelerLernabschnitte,
			final Map<Long, List<DTOSchuelerLeistungsdaten>> mapAbschnittsLeistungen,
			final Map<Long, DTOJahrgang> mapJahrgaenge,
			final Map<Long, Integer> mapAbiturjahrgaenge,
			final Map<Long, Sprachendaten> mapSchuelerSpachendaten,
			final Map<Integer, GostFaecherManager> mapGostFaecherManager) {

		final Map<Long, GostLeistungen> result = new HashMap<>();
		for (final DTOSchueler schueler : listSchueler) {
			final DTOSchuelerLernabschnittsdaten aktLernabschnitt = mapSchuelerAktuellerLernabschnitt.get(schueler.ID);
			final List<DTOSchuelerLernabschnittsdaten> lernabschnitte = mapSchuelerLernabschnitte.get(schueler.ID);
			final int abiturjahr = mapAbiturjahrgaenge.get(schueler.ID);
			final Sprachendaten sprachendaten = mapSchuelerSpachendaten.get(schueler.ID);
			final GostFaecherManager gostFaecher = mapGostFaecherManager.get(abiturjahr);
			final GostLeistungen leistungen =
					this.getLeistungsdaten(schueler, mapJahrgaenge, aktLernabschnitt, lernabschnitte, mapAbschnittsLeistungen, abiturjahr, sprachendaten,
							gostFaecher);
			result.put(schueler.ID, leistungen);
		}
		return result;
	}


	private GostLeistungen getLeistungsdaten(final DTOSchueler schueler, final Map<Long, DTOJahrgang> mapJahrgaenge,
			final DTOSchuelerLernabschnittsdaten aktLernabschnitt, final List<DTOSchuelerLernabschnittsdaten> lernabschnitte,
			final Map<Long, List<DTOSchuelerLeistungsdaten>> mapAbschnittsLeistungen,
			final int abiturjahr, final Sprachendaten sprachendaten, final GostFaecherManager gostFaecher) {
		final Schuljahresabschnitt abschnittSchueler =
				benutzerRepository.getAktuellerBenutzer().schuleGetAbschnittById(schueler.Schuljahresabschnitts_ID);

		final DTOJahrgang dtoAktJahrgang = mapJahrgaenge.get(aktLernabschnitt.Jahrgang_ID);
		final Jahrgaenge aktJahrgang =
				((dtoAktJahrgang == null) || (dtoAktJahrgang.ASDJahrgang == null)) ? null : Jahrgaenge.data().getWertBySchluessel(dtoAktJahrgang.ASDJahrgang);

		// Ermittle nun die Leistungsdaten aus den Lernabschnitten
		final GostLeistungen daten = new GostLeistungen();
		daten.id = schueler.ID;
		daten.aktuellesSchuljahr = abschnittSchueler.schuljahr;
		daten.aktuellerJahrgang = (aktJahrgang == null) ? null : aktJahrgang.daten(abschnittSchueler.schuljahr).kuerzel;
		daten.sprachendaten = sprachendaten;
		final String biliZweig = aktLernabschnitt.BilingualerZweig;
		if ((biliZweig != null) && (!"".equals(biliZweig))) {
			daten.bilingualeSprache = biliZweig.toUpperCase().substring(0, 1);
		}
		// eine Map zur temporären Speicherung der Fächer -> muss später noch sortiert werden
		final Map<String, GostLeistungenFachwahl> faecher = new HashMap<>();
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			final Schuljahresabschnitt abschnittLeistungsdaten =
					benutzerRepository.getAktuellerBenutzer().schuleGetAbschnittById(lernabschnitt.Schuljahresabschnitts_ID);

			final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
			final Jahrgaenge jahrgang =
					((dtoJahrgang == null) || (dtoJahrgang.ASDJahrgang == null)) ? null : Jahrgaenge.data().getWertBySchluessel(dtoJahrgang.ASDJahrgang);
			if (jahrgang == null) {
				continue;
			}
			final GostHalbjahr halbjahr =
					GostHalbjahr.fromJahrgangUndHalbjahr(jahrgang.daten(abschnittSchueler.schuljahr).kuerzel, abschnittLeistungsdaten.abschnitt);
			if (halbjahr == null) {
				continue;
			}
			if (Boolean.TRUE.equals(lernabschnitt.SemesterWertung)) {
				daten.bewertetesHalbjahr[halbjahr.id] = true;
			}
			final List<DTOSchuelerLeistungsdaten> leistungen = mapAbschnittsLeistungen.get(lernabschnitt.ID);
			if (leistungen.isEmpty()) {
				daten.bewertetesHalbjahr[halbjahr.id] = false;
			}
			for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
				getLeistung(abiturjahr, daten, lernabschnitt, leistung, abschnittLeistungsdaten, jahrgang, halbjahr, sprachendaten, gostFaecher, faecher);
			}
		}
		// Sortiere Fächer anhand der SII-Sortierung der Fächer
		faecher.values().stream()
				.sorted((a, b) -> Integer.compare(a.fach.sortierung, b.fach.sortierung))
				.forEach(daten.faecher::add);
		return daten;
	}


	private static void getLeistung(final Integer abiturjahr, final GostLeistungen daten, final DTOSchuelerLernabschnittsdaten lernabschnitt,
			final DTOSchuelerLeistungsdaten leistung, final Schuljahresabschnitt abschnittLeistungsdaten,
			final Jahrgaenge jahrgang, final GostHalbjahr halbjahr, final Sprachendaten sprachendaten,
			final GostFaecherManager gostFaecher, final Map<String, GostLeistungenFachwahl> faecher) {
		// Prüfe, ob die Kursart eine Kursart der Oberstufe ist.
		final GostKursart kursart = GostKursart.fromKuerzel(leistung.KursartAllg);
		if (kursart == null) {
			return;
		}
		// Prüfe, ob das Fach ein Fach der Oberstufe ist
		final GostFach gostFach = gostFaecher.get(leistung.Fach_ID);
		if (gostFach == null) {
			return;
		}
		// Füge die Fächer aus den Leistungsdaten zunächst in die HashMap ein...
		GostLeistungenFachwahl fach = faecher.get(gostFach.kuerzelAnzeige);
		if (fach == null) {
			fach = new GostLeistungenFachwahl();
			fach.fach = gostFach;
			faecher.put(gostFach.kuerzelAnzeige, fach);
		}
		// Prüfe ggf., ob eine Sprache fortgeführt wurde oder nicht
		final String fremdsprache = GostFachUtils.getFremdsprache(gostFach);
		if (fremdsprache != null) {
			fach.istFSNeu = (SprachendatenUtils.istNeueinsetzbareSpracheInGOSt(sprachendaten, fremdsprache));
		}

		final GostAbiturFach tmpAbiFach = GostAbiturFach.fromIDString(leistung.AbiFach);
		fach.abiturfach = (tmpAbiFach == null) ? null : tmpAbiFach.id;

		// Füge eine Belegung der Kurse für die einzelnen Fächer in dem Halbjahr ein
		final GostLeistungenFachbelegung belegung = new GostLeistungenFachbelegung();
		belegung.id = leistung.ID;
		belegung.schuljahr = abschnittLeistungsdaten.schuljahr;
		belegung.halbjahrKuerzel = halbjahr.kuerzel;
		belegung.abschnitt = abschnittLeistungsdaten.abschnitt;
		belegung.abschnittGewertet = lernabschnitt.SemesterWertung;
		belegung.jahrgang = jahrgang.daten(belegung.schuljahr).kuerzel;
		belegung.idKurs = leistung.Kurs_ID;
		belegung.lehrer = leistung.Fachlehrer_ID;
		belegung.notenKuerzel = leistung.NotenKrz;
		belegung.kursartKuerzel = kursart.kuerzel;
		belegung.istSchriftlich = (kursart == GostKursart.LK)
				|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart))
						|| ("AB3".equals(leistung.Kursart))
						|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))))
				|| (AbiturdatenManager.istAbitur2030(abiturjahr) && (kursart == GostKursart.PJK));
		belegung.bilingualeSprache = gostFach.biliSprache;
		belegung.wochenstunden = (leistung.Wochenstunden == null)
				? kursart.getWochenstunden(fach.istFSNeu)
				: leistung.Wochenstunden;
		belegung.fehlstundenGesamt = (leistung.FehlStd == null) ? 0 : leistung.FehlStd;
		belegung.fehlstundenUnentschuldigt = (leistung.uFehlStd == null) ? 0 : leistung.uFehlStd;
		fach.belegungen.add(belegung);

		// Ermittle ggf. das Projektkursthema und die zughörigen Leitfächer
		if (kursart == GostKursart.PJK) {
			daten.projektkursLeitfach1Kuerzel = gostFach.projektKursLeitfach1Kuerzel;
			daten.projektkursLeitfach2Kuerzel = gostFach.projektKursLeitfach2Kuerzel;
			if ((leistung.Lernentw != null) && (!"".equals(leistung.Lernentw))) {
				daten.projektkursThema = leistung.Lernentw;
			}
		}
	}

}
