package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.data.gost.GostSchuelerWiederholung;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholung;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholungFachwahlen;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.gost.GostJahrgangsdatenRepository;
import de.svws_nrw.repo.gost.GostSchuelerFachbelegungenRepository;
import de.svws_nrw.repo.gost.GostSchuelerPlanungFachbelegungenRepositoryImpl;
import de.svws_nrw.repo.gost.GostSchuelerRepository;
import de.svws_nrw.repo.gost.GostSchuelerWiederholungFachwahlenRepository;
import de.svws_nrw.repo.gost.GostSchuelerWiederholungRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für die Schüler des Gymnasialen Oberstufe.
 * Er bestimmt, welche Schüler zu einem Abiturjahrgang gehören, und verwaltet die Wiederholung im folgenden Abiturjahrgang.
 * Vor dem Einstiegshalbjahr der Wiederholung gehört der Schüler beiden Jahrgängen an, ab dem Einstieg dem neuen Jahrgang.
 * Die Wiederholung besteht, bis der Schüler im neuen Jahrgang angekommen ist und seine Planungsdaten übernommen sind.
 */
public class GostSchuelerService {

	private final BenutzerAllgemeinRepository benutzerRepository;
	private final JahrgangRepository jahrgangRepository;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;
	private final GostSchuelerRepository gostSchuelerRepository;
	private final GostSchuelerFachbelegungenRepository gostSchuelerFachbelegungenRepository;
	private final GostSchuelerWiederholungRepository gostSchuelerWiederholungRepository;
	private final GostSchuelerWiederholungFachwahlenRepository gostSchuelerWiederholungFachwahlenRepository;
	private final GostJahrgangsdatenRepository gostJahrgangsdatenRepository;
	private final BenutzerKompetenzService benutzerKompetenzService;
	private final boolean aktuellePlanung;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                             das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param jahrgangRepository                             das Repository für den Zugriff auf Jahrgänge
	 * @param schuelerRepository                             das Repository für den Zugriff auf Schülerdaten
	 * @param schuelerLernabschnittRepository                das Repository für den Zugriff auf die Schüler-Lernabschnittsdaten
	 * @param gostSchuelerRepository                         das Repository für den Zugriff auf die Schüler-Informationen zur Gymnasialen Oberstufe
	 * @param gostSchuelerFachbelegungenRepository           das Repository für den Zugriff auf die Fachwahlen der aktuellen Planung
	 * @param gostSchuelerWiederholungRepository             das Repository für den Zugriff auf die Wiederholungen
	 * @param gostSchuelerWiederholungFachwahlenRepository   das Repository für den Zugriff auf die Fachwahlen der Wiederholungen
	 * @param gostJahrgangsdatenRepository                   das Repository für den Zugriff auf die Jahrgangsdaten der gymnasialen Oberstufe
	 * @param benutzerKompetenzService                       der Service für den Zugriff auf die Benutzer-Kompetenzen des aktuellen Benutzers
	 * @param aktuellePlanung                                true, wenn die Planung einer Wiederholung unberücksichtigt bleibt und
	 *                                                       die Daten der aktuellen Planung gelten
	 */
	public GostSchuelerService(final BenutzerAllgemeinRepository benutzerRepository,
			final JahrgangRepository jahrgangRepository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final GostSchuelerRepository gostSchuelerRepository,
			final GostSchuelerFachbelegungenRepository gostSchuelerFachbelegungenRepository,
			final GostSchuelerWiederholungRepository gostSchuelerWiederholungRepository,
			final GostSchuelerWiederholungFachwahlenRepository gostSchuelerWiederholungFachwahlenRepository,
			final GostJahrgangsdatenRepository gostJahrgangsdatenRepository,
			final BenutzerKompetenzService benutzerKompetenzService,
			final boolean aktuellePlanung) {
		this.benutzerRepository = benutzerRepository;
		this.jahrgangRepository = jahrgangRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
		this.gostSchuelerRepository = gostSchuelerRepository;
		this.gostSchuelerFachbelegungenRepository = gostSchuelerFachbelegungenRepository;
		this.gostSchuelerWiederholungRepository = gostSchuelerWiederholungRepository;
		this.gostSchuelerWiederholungFachwahlenRepository = gostSchuelerWiederholungFachwahlenRepository;
		this.gostJahrgangsdatenRepository = gostJahrgangsdatenRepository;
		this.benutzerKompetenzService = benutzerKompetenzService;
		this.aktuellePlanung = aktuellePlanung;
	}


	/**
	 * Gibt zurück, ob der Service für die aktuelle Planung arbeitet. Dann gelten die Fachwahlen und GKL-Wahlen des bisherigen
	 * Jahrgangs, auch wenn eine Wiederholung besteht. Zugehörigkeit und Kompetenzen berücksichtigen die Wiederholung weiterhin.
	 *
	 * @return true, wenn der Service für die aktuelle Planung arbeitet
	 */
	public boolean istAktuellePlanung() {
		return aktuellePlanung;
	}


	/**
	 * Bestimmt alle aufgenommenen Schüler, die in der Gymnasialen Oberstufe waren, sind oder potentiell noch dorthin kommen können. <br>
	 * - die Schüler dürfen nicht gelöscht sein. <br>
	 * - bei Status 10 (Ehemalige) muss gefiltert werden, da diese keine weiteren Schuldaten und damit auch keine Oberstufendaten haben <br>
	 * - Status 8, 9 (Abschluss, Abgänger) muss gefiltert werden, da nur Schüler mit einem Abschluss in einem der Entlassjahrgänge der Oberstufe zurückgegeben werden sollen <br>
	 * Alle anderen Schüler werden zunächst eingelesen, da sie entweder in der Oberstufe sind oder noch dahin kommen können
	 *
	 * @param mapJahrgaenge   eine Map mit den an der Schule definierten Jahrgängen - aus diesen werden die Jahrgänge der gymnasialen Oberstufe gefiltert
	 *
	 * @return die Liste mit den Schülern
	 */
	private List<DTOSchueler> getPotentielleGostSchueler(final Map<Long, DTOJahrgang> mapJahrgaenge) {
		// Bestimme alle Jahrgänge der Schule, welche als ASD-Jahrgang 'EF', 'Q1', 'Q2' haben
		final List<Long> idsGostJahrgaenge = mapJahrgaenge.values().stream()
				.filter(j -> "EF".equals(j.ASDJahrgang) || "Q1".equals(j.ASDJahrgang) || "Q2".equals(j.ASDJahrgang))
				.map(j -> j.ID).toList();

		return schuelerRepository.getAll().stream().filter(
				s -> (!s.hatLoeschvermerk) && (s.idStatus != 10) && (((s.idStatus != 8) && (s.idStatus != 9)) || (idsGostJahrgaenge.contains(s.Entlassjahrgang_ID))))
				.toList();
	}


	/**
	 * Bestimmt die Entitäten aller Schüler des angebebenen Abiturjahrgangs für die Planung künftiger Halbjahre.
	 * Ein Schüler mit Wiederholung gehört hier zum neuen Jahrgang und bis zum Einstieg auch noch zu seinem bisherigen.
	 *
	 * @param abijahrgang   der Abiturjahrgang
	 *
	 * @return die Liste mit den Schülern
	 */
	public List<DTOSchueler> getByAbiturjahrgang(final int abijahrgang) {
		return getByAbiturjahrgang(abijahrgang, null);
	}


	/**
	 * Bestimmt die Entitäten aller Schüler des angebebenen Abiturjahrgangs in dem angegebenen Halbjahr dieses Jahrgangs.
	 * Eine Wiederholung zählt erst ab dem Einstiegshalbjahr, davor bleibt der Schüler im Jahrgang seines Lernabschnitts.
	 * Ohne Halbjahr gilt die Zuordnung für die Planung: Der Schüler gehört zum neuen Jahrgang und bis zum Einstieg auch
	 * noch zu seinem bisherigen.
	 *
	 * @param abijahrgang   der Abiturjahrgang
	 * @param halbjahr      das Halbjahr des Abiturjahrgangs oder null für die Planung
	 *
	 * @return die Liste mit den Schülern
	 */
	public List<DTOSchueler> getByAbiturjahrgang(final int abijahrgang, final GostHalbjahr halbjahr) {
		// Bestimme zunächst informationen zur Schule
		final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
		final Schuljahresabschnitt schuleSchuljahresabschnitt = benutzer.schuleGetSchuljahresabschnitt();
		final Schulform schulform = benutzer.schuleGetSchulform();
		final Map<Long, DTOJahrgang> mapJahrgaenge = jahrgangRepository.getMap();

		// Bestimme Schüler, welche in den Abiturjahrgängen sein können - auch in zukünftigen.
		final List<DTOSchueler> listSchueler = this.getPotentielleGostSchueler(mapJahrgaenge);
		final List<Long> idsSchueler = listSchueler.stream().map(s -> s.ID).toList();

		// Bestimme die aktuellen SchuelerLernabschnitte der Schüler und die Wiederholungen
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte = schuelerLernabschnittRepository.getMapAktuelleBySchuelerIDs(idsSchueler);
		final Map<Long, GostSchuelerWiederholung> mapWiederholungen = getMapWiederholungen(idsSchueler);

		// Filtere die Schüler auf die Schüler des konkreten Abiturjahrgangs
		final List<DTOSchueler> result = new ArrayList<>();
		for (final DTOSchueler schueler : listSchueler) {
			final GostSchuelerWiederholung wiederholung = mapWiederholungen.get(schueler.ID);
			if ((wiederholung != null) && entscheidetWiederholung(wiederholung, abijahrgang, halbjahr, schuleSchuljahresabschnitt.schuljahr,
					schuleSchuljahresabschnitt.abschnitt)) {
				if (wiederholung.abiturjahr == abijahrgang) {
					result.add(schueler);
				}
				continue;
			}
			final Schuljahresabschnitt schuljahresabschnitt = benutzer.schuleGetAbschnittById(schueler.Schuljahresabschnitts_ID);
			if (schuljahresabschnitt == null) {
				continue;
			}
			final DTOSchuelerLernabschnittsdaten lernabschnitt = mapLernabschnitte.get(schueler.ID);
			if (lernabschnitt == null) {
				continue;
			}
			final DTOJahrgang jahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
			if (jahrgang == null) {
				continue;
			}
			// Filtere Schüler, die vor der Oberstufe die Schule verlassen haben
			final @NotNull SchuelerStatus status = SchuelerStatus.data().getWertByID(schueler.idStatus == null ? null : schueler.idStatus.longValue());
			if ((schuljahresabschnitt.schuljahr < schuleSchuljahresabschnitt.schuljahr)
					&& !"EF".equals(jahrgang.ASDJahrgang) && !"Q1".equals(jahrgang.ASDJahrgang) && !"Q2".equals(jahrgang.ASDJahrgang)
					&& ((status == SchuelerStatus.ABGANG) || (status == SchuelerStatus.ABSCHLUSS) || (status == SchuelerStatus.EHEMALIGE)
							|| (status == SchuelerStatus.WARTELISTE))) {
				continue;
			}
			// Bestimme die Restjahre in Bezug auf den Abiturjahrgang und den Schuljahresabschnitt
			final int restjahreNachAbiturjahr = abijahrgang - schuljahresabschnitt.schuljahr;
			final Integer restjahreNachJahrgang =
					JahrgaengeUtils.getRestlicheJahreBisAbitur(schulform, Schulgliederung.data().getWertByKuerzel(jahrgang.GliederungKuerzel),
							schuljahresabschnitt.schuljahr, jahrgang.ASDJahrgang);
			if ((restjahreNachJahrgang != null) && (restjahreNachAbiturjahr == restjahreNachJahrgang)) {
				result.add(schueler);
			}
		}
		return result;
	}


	/**
	 * Bestimmt die Entitäten der Schüler, die in einer Blockung geplant werden. Das sind die Schüler des Abiturjahrgangs
	 * im Halbjahr der Blockung.
	 *
	 * @param blockung   die Blockung
	 *
	 * @return die Liste mit den Schülern
	 */
	public List<DTOSchueler> getByBlockung(final @NotNull DTOGostBlockung blockung) {
		return getByAbiturjahrgang(blockung.Abi_Jahrgang, blockung.Halbjahr);
	}


	/**
	 * Prüft, ob eine Wiederholung für das angegebene Halbjahr eines Abiturjahrgangs maßgeblich ist.
	 * Das ist ab dem Einstiegshalbjahr der Fall und für die Planung ohne Halbjahr immer.
	 *
	 * @param wiederholung   die Wiederholung
	 * @param abijahrgang    der Abiturjahrgang, auf den sich das Halbjahr bezieht
	 * @param halbjahr       das Halbjahr oder null für die Planung
	 *
	 * @return true, wenn die Wiederholung maßgeblich ist
	 */
	public static boolean istWiederholungMassgeblich(final @NotNull GostSchuelerWiederholung wiederholung, final int abijahrgang, final GostHalbjahr halbjahr) {
		if (halbjahr == null) {
			return true;
		}
		final GostHalbjahr halbjahrWiederholung = GostHalbjahr.fromIDorException(wiederholung.gostHalbjahr);
		return !GostAbiturjahrUtils.liegtVorWiederholung(wiederholung.abiturjahr, halbjahrWiederholung, halbjahr.getSchuljahrFromAbiturjahr(abijahrgang),
				halbjahr.halbjahr);
	}


	/**
	 * Prüft, ob die Wiederholung über die Zugehörigkeit zum Abiturjahrgang entscheidet. Sonst entscheidet der Lernabschnitt.
	 * Für die Planung ohne Halbjahr gehört der Schüler bis zum Einstieg auch noch zu seinem bisherigen Jahrgang.
	 *
	 * @param wiederholung      die Wiederholung
	 * @param abijahrgang       der Abiturjahrgang
	 * @param halbjahr          das Halbjahr des Abiturjahrgangs oder null für die Planung
	 * @param schuljahrSchule   das Schuljahr des aktuellen Schuljahresabschnitts der Schule
	 * @param abschnittSchule   der Abschnitt des aktuellen Schuljahresabschnitts der Schule
	 *
	 * @return true, wenn die Wiederholung über die Zugehörigkeit entscheidet
	 */
	public static boolean entscheidetWiederholung(final @NotNull GostSchuelerWiederholung wiederholung, final int abijahrgang,
			final GostHalbjahr halbjahr, final int schuljahrSchule, final int abschnittSchule) {
		if (!istWiederholungMassgeblich(wiederholung, abijahrgang, halbjahr)) {
			return false;
		}
		if ((halbjahr != null) || (wiederholung.abiturjahr == abijahrgang)) {
			return true;
		}
		return istWechselFaellig(wiederholung, schuljahrSchule, abschnittSchule);
	}


	/**
	 * Prüft, ob ein Schüler mit Wiederholung in dem angegebenen Halbjahr zum Abiturjahrgang gehört. Zum neuen Jahrgang
	 * gehört er ab dem Einstiegshalbjahr, zum bisherigen Jahrgang davor.
	 *
	 * @param wiederholung   die Wiederholung
	 * @param abijahrgang    der Abiturjahrgang
	 * @param halbjahr       das Halbjahr des Abiturjahrgangs
	 *
	 * @return true, wenn der Schüler in dem Halbjahr zum Abiturjahrgang gehört
	 */
	public static boolean gehoertImHalbjahrZumJahrgang(final @NotNull GostSchuelerWiederholung wiederholung, final int abijahrgang,
			final @NotNull GostHalbjahr halbjahr) {
		return (wiederholung.abiturjahr == abijahrgang) == istWiederholungMassgeblich(wiederholung, abijahrgang, halbjahr);
	}


	/**
	 * Prüft, ob die Schule das Einstiegshalbjahr der Wiederholung erreicht hat.
	 *
	 * @param wiederholung   die Wiederholung
	 * @param schuljahr      das Schuljahr des aktuellen Schuljahresabschnitts der Schule
	 * @param abschnitt      der Abschnitt des aktuellen Schuljahresabschnitts der Schule
	 *
	 * @return true, wenn der Wechsel fällig ist
	 */
	public static boolean istWechselFaellig(final @NotNull GostSchuelerWiederholung wiederholung, final int schuljahr, final int abschnitt) {
		return !GostAbiturjahrUtils.liegtVorWiederholung(wiederholung.abiturjahr, GostHalbjahr.fromIDorException(wiederholung.gostHalbjahr),
				schuljahr, abschnitt);
	}


	/**
	 * Bestimmt für die angegebenen Schüler die Wiederholungen mit Abiturjahrgang und Einstiegshalbjahr.
	 * Schüler ohne Wiederholung fehlen in der Map.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Map mit den Wiederholungen zu den Schüler-IDs
	 */
	public Map<Long, GostSchuelerWiederholung> getMapWiederholungen(final Collection<Long> idsSchueler) {
		final Map<Long, GostSchuelerWiederholung> result = new HashMap<>();
		if ((idsSchueler == null) || idsSchueler.isEmpty()) {
			return result;
		}
		for (final DTOGostSchuelerWiederholung dto : gostSchuelerWiederholungRepository.findMapByIds(idsSchueler).values()) {
			final GostSchuelerWiederholung wiederholung = new GostSchuelerWiederholung();
			wiederholung.idSchueler = dto.Schueler_ID;
			wiederholung.abiturjahr = dto.Abi_Jahrgang;
			wiederholung.gostHalbjahr = dto.Halbjahr.id;
			result.put(dto.Schueler_ID, wiederholung);
		}
		return result;
	}


	/**
	 * Bestimmt die Schüler, deren Wiederholung in einen anderen als den angegebenen Abiturjahrgang führt. Für den angegebenen Jahrgang
	 * gilt bei ihnen die aktuelle Planung, bei einer Wiederholung in diesen Jahrgang die zukünftige Planung.
	 *
	 * @param idsSchueler      die IDs der Schüler
	 * @param abiturjahrgang   der Abiturjahrgang, dessen Laufbahnplanung angezeigt oder gedruckt wird, z. B. der Jahrgang einer
	 *                         Laufbahnen-Liste, oder null, wenn bei jeder Wiederholung die zukünftige Planung gelten soll
	 *
	 * @return die IDs der Schüler, für die die aktuelle Planung gilt
	 */
	public Set<Long> getIdsWiederholungAndererJahrgang(final Collection<Long> idsSchueler, final Integer abiturjahrgang) {
		if (abiturjahrgang == null) {
			return new HashSet<>();
		}
		return getMapWiederholungen(idsSchueler).values().stream().filter(w -> !abiturjahrgang.equals(w.abiturjahr)).map(w -> w.idSchueler)
				.collect(Collectors.toSet());
	}


	/**
	 * Bestimmt den Abiturjahrgang, der sich aus dem aktuellen Lernabschnitt des Schülers ergibt.
	 *
	 * @param benutzer   der angemeldete Benutzer mit den Informationen zur Schule
	 * @param schueler   der Schüler
	 *
	 * @return der Abiturjahrgang oder null, wenn er nicht bestimmt werden kann
	 */
	private Integer getAbiturjahrLernabschnitt(final @NotNull Benutzer benutzer, final @NotNull DTOSchueler schueler) {
		final Schuljahresabschnitt abschnitt = (schueler.Schuljahresabschnitts_ID == null) ? null
				: benutzer.schuleGetAbschnittById(schueler.Schuljahresabschnitts_ID);
		final DTOSchuelerLernabschnittsdaten lernabschnitt = schuelerLernabschnittRepository.findAktuellBySchuelerID(schueler.ID).orElse(null);
		if ((abschnitt == null) || (lernabschnitt == null) || (lernabschnitt.Jahrgang_ID == null)) {
			return null;
		}
		final DTOJahrgang jahrgang = jahrgangRepository.findById(lernabschnitt.Jahrgang_ID).orElse(null);
		if ((jahrgang == null) || (jahrgang.ASDJahrgang == null)) {
			return null;
		}
		final Schulform schulform = benutzer.schuleGetSchulform();
		final Schulgliederung gliederung = (lernabschnitt.Schulgliederung == null) ? Schulgliederung.getDefault(schulform)
				: Schulgliederung.data().getWertByKuerzel(lernabschnitt.Schulgliederung);
		if (gliederung == null) {
			return null;
		}
		return GostAbiturjahrUtils.getGostAbiturjahr(schulform, gliederung, abschnitt.schuljahr, jahrgang.ASDJahrgang);
	}


	/**
	 * Ergänzt bei einer Wiederholung die Angaben zum Lernabschnitt und zum Stand des Wechsels. Fällig ist der Wechsel, wenn die Schule
	 * das Einstiegshalbjahr erreicht hat. Vollzogen ist er, wenn der Lernabschnitt zum neuen Jahrgang passt.
	 *
	 * @param benutzer       der angemeldete Benutzer mit den Informationen zur Schule
	 * @param schueler       der Schüler
	 * @param wiederholung   die Wiederholung, die ergänzt wird
	 */
	private void ergaenzeWechsel(final @NotNull Benutzer benutzer, final @NotNull DTOSchueler schueler, final @NotNull GostSchuelerWiederholung wiederholung) {
		wiederholung.abiturjahrLernabschnitt = getAbiturjahrLernabschnitt(benutzer, schueler);
		if ((wiederholung.abiturjahr == null) || (wiederholung.gostHalbjahr == null)) {
			return;
		}
		final Schuljahresabschnitt schule = benutzer.schuleGetSchuljahresabschnitt();
		wiederholung.wechselFaellig = istWechselFaellig(wiederholung, schule.schuljahr, schule.abschnitt);
		wiederholung.wechselVollzogen = wiederholung.wechselFaellig && wiederholung.abiturjahr.equals(wiederholung.abiturjahrLernabschnitt);
	}


	/**
	 * Bestimmt die Abiturjahrgänge, zu denen der Schüler gehört. Mit Wiederholung sind das der Jahrgang laut Lernabschnitt
	 * und der Jahrgang der Wiederholung, sonst nur der Jahrgang laut Lernabschnitt.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Liste der Abiturjahrgänge, ggf. leer, wenn keiner bestimmt werden kann
	 */
	public List<Integer> getAbiturjahrgaenge(final long idSchueler) {
		return transactional(() -> {
			final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
			final DTOSchueler schueler = getSchuelerOrException(idSchueler);
			final List<Integer> result = new ArrayList<>();
			final Integer abiturjahrLernabschnitt = getAbiturjahrLernabschnitt(benutzer, schueler);
			if (abiturjahrLernabschnitt != null) {
				result.add(abiturjahrLernabschnitt);
			}
			final GostSchuelerWiederholung wiederholung = getMapWiederholungen(List.of(idSchueler)).get(idSchueler);
			if ((wiederholung != null) && !result.contains(wiederholung.abiturjahr)) {
				result.add(wiederholung.abiturjahr);
			}
			return result;
		});
	}


	/**
	 * Gibt die Wiederholung des Schülers im folgenden Abiturjahrgang zurück. Besteht keine Wiederholung, so sind Abiturjahr
	 * und Halbjahr null. Lesen darf, wer auf einen der beiden Abiturjahrgänge zugreifen darf.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Wiederholung
	 */
	public GostSchuelerWiederholung getWiederholung(final long idSchueler) {
		return transactional(() -> {
			final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
			pruefeGymOb(benutzer);
			final GostSchuelerWiederholung result = ermittleWiederholung(benutzer, idSchueler);
			benutzerKompetenzService.pruefeKompetenzGost(Arrays.asList(result.abiturjahrLernabschnitt, result.abiturjahr));
			return result;
		});
	}


	/**
	 * Bestimmt die Wiederholung des Schülers samt Stand des Wechsels, ohne die Kompetenz zu prüfen. Nach dem Entfernen oder
	 * Übernehmen kennt die Kompetenzprüfung nur noch einen Jahrgang und würde den Beratungslehrer des anderen abweisen.
	 *
	 * @param benutzer     der angemeldete Benutzer mit den Informationen zur Schule
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Wiederholung, ohne Abiturjahr und Halbjahr, wenn keine besteht
	 */
	private GostSchuelerWiederholung ermittleWiederholung(final @NotNull Benutzer benutzer, final long idSchueler) {
		final DTOSchueler schueler = getSchuelerOrException(idSchueler);
		final GostSchuelerWiederholung wiederholung = getMapWiederholungen(List.of(idSchueler)).get(idSchueler);
		final GostSchuelerWiederholung result = (wiederholung == null) ? new GostSchuelerWiederholung() : wiederholung;
		result.idSchueler = idSchueler;
		ergaenzeWechsel(benutzer, schueler, result);
		return result;
	}


	/**
	 * Gibt alle Wiederholungen der Schule zurück, damit Laufbahnen-Liste und Kursplanung die betroffenen Schüler kennzeichnen können.
	 * Bei funktionsbezogenen Kompetenzen fehlen die Wiederholungen, bei denen der Benutzer auf keinen der beiden Jahrgänge zugreifen darf.
	 *
	 * @return die Liste der Wiederholungen
	 */
	public List<GostSchuelerWiederholung> getListWiederholungen() {
		return transactional(() -> {
			final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
			pruefeGymOb(benutzer);
			final List<Long> idsSchueler = gostSchuelerWiederholungRepository.getAll().stream().map(v -> v.Schueler_ID).toList();
			final Map<Long, GostSchuelerWiederholung> mapWiederholungen = getMapWiederholungen(idsSchueler);
			final Map<Long, DTOSchueler> mapSchueler = schuelerRepository.findMapByIds(mapWiederholungen.keySet());
			final List<GostSchuelerWiederholung> result = new ArrayList<>();
			for (final GostSchuelerWiederholung wiederholung : mapWiederholungen.values()) {
				final DTOSchueler schueler = mapSchueler.get(wiederholung.idSchueler);
				if (schueler == null) {
					continue;
				}
				ergaenzeWechsel(benutzer, schueler, wiederholung);
				try {
					benutzerKompetenzService.pruefeKompetenzGost(Arrays.asList(wiederholung.abiturjahrLernabschnitt, wiederholung.abiturjahr));
				} catch (@SuppressWarnings("unused") final ApiOperationException e) {
					continue;
				}
				result.add(wiederholung);
			}
			return result;
		});
	}


	/**
	 * Legt für den Schüler eine Wiederholung im folgenden Abiturjahrgang an. Er steigt dort mit dem Halbjahr ein, das auf den
	 * aktuellen Schuljahresabschnitt der Schule folgt. Anlegen dürfen die Beratungslehrer des bisherigen und des neuen Jahrgangs,
	 * da der Schüler bis zum Wechsel zu beiden gehört.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die angelegte Wiederholung
	 */
	public GostSchuelerWiederholung erstelleWiederholung(final long idSchueler) {
		transactional(() -> {
			final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
			pruefeGymOb(benutzer);
			final DTOSchueler schueler = getSchuelerOrException(idSchueler);
			final Integer abiturjahrLernabschnitt = getAbiturjahrLernabschnitt(benutzer, schueler);
			if (abiturjahrLernabschnitt == null) {
				throw new ApiOperationException(Status.CONFLICT, "Der Abiturjahrgang des Schülers kann nicht bestimmt werden.");
			}
			final int abiturjahr = abiturjahrLernabschnitt + 1;
			benutzerKompetenzService.pruefeKompetenzLaufbahnplanung(List.of(abiturjahrLernabschnitt, abiturjahr));
			if (gostSchuelerWiederholungRepository.findById(idSchueler).isPresent()) {
				throw new ApiOperationException(Status.CONFLICT, "Für den Schüler ist bereits eine Wiederholung geplant.");
			}
			legeWiederholungAn(idSchueler, abiturjahr, pruefeWiederholung(benutzer, schueler, abiturjahr));
		});
		return getWiederholung(idSchueler);
	}


	/**
	 * Entfernt die Wiederholung des Schülers samt der für sie geplanten Fachwahlen. Entfernen dürfen die Beratungslehrer
	 * des bisherigen und des neuen Jahrgangs.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return der Stand nach dem Entfernen, also ohne Abiturjahr und Halbjahr
	 */
	public GostSchuelerWiederholung loescheWiederholung(final long idSchueler) {
		return transactional(() -> {
			final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
			pruefeGymOb(benutzer);
			final DTOSchueler schueler = getSchuelerOrException(idSchueler);
			final DTOGostSchuelerWiederholung wiederholung = gostSchuelerWiederholungRepository.findById(idSchueler).orElseThrow(
					() -> new ApiOperationException(Status.NOT_FOUND, "Für den Schüler ist keine Wiederholung geplant."));
			benutzerKompetenzService.pruefeKompetenzLaufbahnplanung(Arrays.asList(getAbiturjahrLernabschnitt(benutzer, schueler),
					wiederholung.Abi_Jahrgang));
			entferneWiederholung(idSchueler);
			return ermittleWiederholung(benutzer, idSchueler);
		});
	}


	/**
	 * Legt eine neue Wiederholung an. Die Fachwahlen der aktuellen Planung dienen als Grundlage für die neue Planung.
	 * Die GKL-Wahlen bleiben leer, da sie auf Klausurvorgaben des bisherigen Jahrgangs verweisen.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param abiturjahr   das Abiturjahr des Jahrgangs der Wiederholung
	 * @param halbjahr     das Halbjahr, mit dem der Schüler im neuen Jahrgang einsteigt
	 */
	private void legeWiederholungAn(final long idSchueler, final int abiturjahr, final @NotNull GostHalbjahr halbjahr) {
		gostSchuelerWiederholungRepository.create(new DTOGostSchuelerWiederholung(idSchueler, abiturjahr, halbjahr));
		gostSchuelerWiederholungRepository.flush();
		final List<DTOGostSchuelerFachbelegungen> fachwahlen = gostSchuelerFachbelegungenRepository.getMapBySchuelerID(List.of(idSchueler))
				.getOrDefault(idSchueler, List.of());
		for (final DTOGostSchuelerFachbelegungen fb : fachwahlen) {
			final DTOGostSchuelerWiederholungFachwahlen fw = new DTOGostSchuelerWiederholungFachwahlen(idSchueler, fb.Fach_ID);
			GostSchuelerPlanungFachbelegungenRepositoryImpl.copyToWiederholung(fb, fw);
			gostSchuelerWiederholungFachwahlenRepository.create(fw);
		}
		gostSchuelerWiederholungFachwahlenRepository.flush();
	}


	/**
	 * Entfernt die Wiederholung eines Schülers samt der für ihn geplanten Fachwahlen.
	 *
	 * @param idSchueler   die ID des Schülers
	 */
	private void entferneWiederholung(final long idSchueler) {
		final DTOGostSchuelerWiederholung wiederholung = gostSchuelerWiederholungRepository.findById(idSchueler).orElse(null);
		if (wiederholung == null) {
			return;
		}
		// Die Einträge werden einzeln entfernt, damit keine veralteten Objekte im Persistenzkontext verbleiben
		gostSchuelerWiederholungFachwahlenRepository.delete(gostSchuelerWiederholungFachwahlenRepository.getMapBySchuelerID(List.of(idSchueler))
				.getOrDefault(idSchueler, new ArrayList<>()));
		gostSchuelerWiederholungRepository.delete(wiederholung);
		gostSchuelerWiederholungRepository.flush();
	}


	/**
	 * Übernimmt die Planung der Wiederholungen der angegebenen Schüler, sofern der Wechsel vollzogen ist. Dabei ersetzen die Fachwahlen und
	 * GKL-Wahlen der Wiederholung die der aktuellen Planung. Wiederholungen ohne vollzogenen Wechsel bleiben bestehen.
	 * Für die aktuelle Planung ist ein vollzogener Wechsel ein Fehler, da sie dann nicht mehr geändert werden darf.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 */
	public void uebernehmeWiederholungen(final Collection<Long> idsSchueler) {
		transactional(() -> {
			final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
			final Map<Long, GostSchuelerWiederholung> mapWiederholungen = getMapWiederholungen(idsSchueler);
			if (mapWiederholungen.isEmpty()) {
				return;
			}
			final Map<Long, DTOSchueler> mapSchueler = schuelerRepository.findMapByIds(mapWiederholungen.keySet());
			for (final GostSchuelerWiederholung wiederholung : mapWiederholungen.values()) {
				final DTOSchueler schueler = mapSchueler.get(wiederholung.idSchueler);
				if (schueler == null) {
					continue;
				}
				ergaenzeWechsel(benutzer, schueler, wiederholung);
				if (!wiederholung.wechselVollzogen) {
					continue;
				}
				if (aktuellePlanung) {
					throw new ApiOperationException(Status.CONFLICT, "Der Schüler ist bereits im Abiturjahrgang %d angekommen. Die aktuelle Planung kann nicht mehr geändert werden."
							.formatted(wiederholung.abiturjahr));
				}
				uebertrageWiederholung(wiederholung.idSchueler);
			}
		});
	}


	/**
	 * Übernimmt die Planung der Wiederholung des Schülers, sofern der Wechsel vollzogen ist, und gibt den Stand danach zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return der Stand nach der Übernahme, ohne Abiturjahr, wenn die Planung übernommen wurde
	 */
	public GostSchuelerWiederholung uebernehmeWiederholung(final long idSchueler) {
		final GostSchuelerWiederholung wiederholung = getWiederholung(idSchueler);
		if (wiederholung.abiturjahr == null) {
			return wiederholung;
		}
		if (!wiederholung.wechselVollzogen) {
			throw new ApiOperationException(Status.CONFLICT, "Die geplante Wiederholung kann erst übernommen werden, wenn der Schüler im Abiturjahrgang %d angekommen ist."
					.formatted(wiederholung.abiturjahr));
		}
		return transactional(() -> {
			uebertrageWiederholung(idSchueler);
			return ermittleWiederholung(benutzerRepository.getAktuellerBenutzer(), idSchueler);
		});
	}


	/**
	 * Überträgt die Planungsdaten einer Wiederholung in die Daten der Laufbahnplanung und entfernt die Wiederholung.
	 *
	 * @param idSchueler   die ID des Schülers
	 */
	public void uebertrageWiederholung(final long idSchueler) {
		final DTOGostSchuelerWiederholung wiederholung = gostSchuelerWiederholungRepository.findById(idSchueler).orElse(null);
		if (wiederholung == null) {
			return;
		}
		// Die bisherigen Fachwahlen werden einzeln entfernt, da gleich Einträge mit denselben Schlüsseln angelegt werden
		gostSchuelerFachbelegungenRepository.delete(gostSchuelerFachbelegungenRepository.getMapBySchuelerID(List.of(idSchueler))
				.getOrDefault(idSchueler, new ArrayList<>()));
		gostSchuelerFachbelegungenRepository.flush();
		final List<DTOGostSchuelerWiederholungFachwahlen> fachwahlen = gostSchuelerWiederholungFachwahlenRepository.getMapBySchuelerID(List.of(idSchueler))
				.getOrDefault(idSchueler, List.of());
		for (final DTOGostSchuelerWiederholungFachwahlen fw : fachwahlen) {
			gostSchuelerFachbelegungenRepository.create(GostSchuelerPlanungFachbelegungenRepositoryImpl.toFachbelegung(fw));
		}
		final DTOGostSchueler gostSchueler = gostSchuelerRepository.findById(idSchueler).orElse(new DTOGostSchueler(idSchueler, false));
		gostSchueler.GKL_EF_AF1_Klausurvorgabe_ID = wiederholung.GKL_EF_AF1_Klausurvorgabe_ID;
		gostSchueler.GKL_EF_AF2_Klausurvorgabe_ID = wiederholung.GKL_EF_AF2_Klausurvorgabe_ID;
		gostSchueler.GKL_EF_AF3_Klausurvorgabe_ID = wiederholung.GKL_EF_AF3_Klausurvorgabe_ID;
		gostSchueler.GKL_Q_AF1_Klausurvorgabe_ID = wiederholung.GKL_Q_AF1_Klausurvorgabe_ID;
		gostSchueler.GKL_Q_AF2_Klausurvorgabe_ID = wiederholung.GKL_Q_AF2_Klausurvorgabe_ID;
		gostSchueler.GKL_Q_AF3_Klausurvorgabe_ID = wiederholung.GKL_Q_AF3_Klausurvorgabe_ID;
		gostSchueler.ZulassungSimulation = wiederholung.ZulassungSimulation;
		gostSchuelerRepository.update(gostSchueler);
		gostSchuelerFachbelegungenRepository.flush();
		entferneWiederholung(idSchueler);
	}


	/**
	 * Gibt die GKL-Wahlen der angegebenen Schüler zurück. Bei einer Wiederholung gelten außerhalb der aktuellen Planung die Wahlen
	 * für den neuen Jahrgang, da die Klausurvorgaben zum jeweiligen Abiturjahrgang gehören. Schüler ohne Eintrag erhalten leere Wahlen.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Map mit den GKL-Wahlen zu den Schüler-IDs
	 */
	public Map<Long, GostSchuelerGKLWahl> getMapGKLWahlen(final Collection<Long> idsSchueler) {
		final Map<Long, GostSchuelerGKLWahl> result = new HashMap<>();
		if ((idsSchueler == null) || idsSchueler.isEmpty()) {
			return result;
		}
		final Map<Long, DTOGostSchuelerWiederholung> mapWiederholungen = aktuellePlanung ? Map.of()
				: gostSchuelerWiederholungRepository.findMapByIds(idsSchueler);
		final Map<Long, DTOGostSchueler> mapGostSchueler = gostSchuelerRepository.findMapByIds(idsSchueler);
		for (final Long idSchueler : idsSchueler) {
			final GostSchuelerGKLWahl wahl = new GostSchuelerGKLWahl();
			wahl.idSchueler = idSchueler;
			final DTOGostSchuelerWiederholung wiederholung = mapWiederholungen.get(idSchueler);
			final DTOGostSchueler gostSchueler = mapGostSchueler.get(idSchueler);
			if (wiederholung != null) {
				wahl.idKlausurvorgabeEF_Sprachen = wiederholung.GKL_EF_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_GW = wiederholung.GKL_EF_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_NW = wiederholung.GKL_EF_AF3_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_Sprachen = wiederholung.GKL_Q_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_GW = wiederholung.GKL_Q_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_NW = wiederholung.GKL_Q_AF3_Klausurvorgabe_ID;
			} else if (gostSchueler != null) {
				wahl.idKlausurvorgabeEF_Sprachen = gostSchueler.GKL_EF_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_GW = gostSchueler.GKL_EF_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_NW = gostSchueler.GKL_EF_AF3_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_Sprachen = gostSchueler.GKL_Q_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_GW = gostSchueler.GKL_Q_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_NW = gostSchueler.GKL_Q_AF3_Klausurvorgabe_ID;
			}
			result.put(idSchueler, wahl);
		}
		return result;
	}


	/**
	 * Gibt die GKL-Wahlen der angegebenen Schüler für einen Abiturjahrgang zurück. Bei einer Wiederholung in einen anderen
	 * Jahrgang gelten die Wahlen der aktuellen Planung, sonst wie bei {@link #getMapGKLWahlen(Collection)}.
	 *
	 * @param idsSchueler      die IDs der Schüler
	 * @param abiturjahrgang   der Abiturjahrgang, dessen Laufbahnplanung angezeigt oder gedruckt wird, z. B. der Jahrgang einer
	 *                         Laufbahnen-Liste, oder null, wenn bei jeder Wiederholung die zukünftige Planung gelten soll
	 *
	 * @return die Map mit den GKL-Wahlen zu den Schüler-IDs
	 */
	public Map<Long, GostSchuelerGKLWahl> getMapGKLWahlen(final Collection<Long> idsSchueler, final Integer abiturjahrgang) {
		final Set<Long> idsLaufend = getIdsWiederholungAndererJahrgang(idsSchueler, abiturjahrgang);
		final Map<Long, GostSchuelerGKLWahl> result = getMapGKLWahlen(idsSchueler.stream().filter(id -> !idsLaufend.contains(id)).toList());
		if (idsLaufend.isEmpty()) {
			return result;
		}
		final Map<Long, DTOGostSchueler> mapGostSchueler = gostSchuelerRepository.findMapByIds(idsLaufend);
		for (final Long idSchueler : idsLaufend) {
			final GostSchuelerGKLWahl wahl = new GostSchuelerGKLWahl();
			wahl.idSchueler = idSchueler;
			final DTOGostSchueler gostSchueler = mapGostSchueler.get(idSchueler);
			if (gostSchueler != null) {
				wahl.idKlausurvorgabeEF_Sprachen = gostSchueler.GKL_EF_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_GW = gostSchueler.GKL_EF_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_NW = gostSchueler.GKL_EF_AF3_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_Sprachen = gostSchueler.GKL_Q_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_GW = gostSchueler.GKL_Q_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_NW = gostSchueler.GKL_Q_AF3_Klausurvorgabe_ID;
			}
			result.put(idSchueler, wahl);
		}
		return result;
	}


	/**
	 * Speichert die GKL-Wahlen eines Schülers. Bei einer Wiederholung gehören sie außerhalb der aktuellen Planung zur Wiederholung,
	 * sonst zu den Schülerdaten der Oberstufe.
	 *
	 * @param wahl   die GKL-Wahlen
	 */
	public void setGKLWahl(final @NotNull GostSchuelerGKLWahl wahl) {
		final DTOGostSchuelerWiederholung wiederholung = aktuellePlanung ? null
				: gostSchuelerWiederholungRepository.findById(wahl.idSchueler).orElse(null);
		if (wiederholung != null) {
			wiederholung.GKL_EF_AF1_Klausurvorgabe_ID = wahl.idKlausurvorgabeEF_Sprachen;
			wiederholung.GKL_EF_AF2_Klausurvorgabe_ID = wahl.idKlausurvorgabeEF_GW;
			wiederholung.GKL_EF_AF3_Klausurvorgabe_ID = wahl.idKlausurvorgabeEF_NW;
			wiederholung.GKL_Q_AF1_Klausurvorgabe_ID = wahl.idKlausurvorgabeQ_Sprachen;
			wiederholung.GKL_Q_AF2_Klausurvorgabe_ID = wahl.idKlausurvorgabeQ_GW;
			wiederholung.GKL_Q_AF3_Klausurvorgabe_ID = wahl.idKlausurvorgabeQ_NW;
			gostSchuelerWiederholungRepository.update(wiederholung);
			return;
		}
		final DTOGostSchueler gostSchueler = gostSchuelerRepository.findById(wahl.idSchueler).orElse(new DTOGostSchueler(wahl.idSchueler, false));
		gostSchueler.GKL_EF_AF1_Klausurvorgabe_ID = wahl.idKlausurvorgabeEF_Sprachen;
		gostSchueler.GKL_EF_AF2_Klausurvorgabe_ID = wahl.idKlausurvorgabeEF_GW;
		gostSchueler.GKL_EF_AF3_Klausurvorgabe_ID = wahl.idKlausurvorgabeEF_NW;
		gostSchueler.GKL_Q_AF1_Klausurvorgabe_ID = wahl.idKlausurvorgabeQ_Sprachen;
		gostSchueler.GKL_Q_AF2_Klausurvorgabe_ID = wahl.idKlausurvorgabeQ_GW;
		gostSchueler.GKL_Q_AF3_Klausurvorgabe_ID = wahl.idKlausurvorgabeQ_NW;
		gostSchuelerRepository.update(gostSchueler);
	}


	private static void pruefeGymOb(final @NotNull Benutzer benutzer) {
		if (!benutzer.schuleHatGymOb()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulform der Schule hat keine gymnasiale Oberstufe.");
		}
	}


	private DTOSchueler getSchuelerOrException(final long idSchueler) {
		return schuelerRepository.findById(idSchueler).orElseThrow(
				() -> new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler)));
	}


	/**
	 * Prüft eine neue Wiederholung und erzeugt im Fehlerfall eine Exception. Der Schüler muss im aktuellen Schuljahresabschnitt
	 * der Schule sein, da der Einstieg im folgenden Abschnitt erfolgt. Der neue Jahrgang muss angelegt sein und in diesem
	 * Abschnitt in der gymnasialen Oberstufe liegen.
	 *
	 * @param benutzer     der angemeldete Benutzer mit den Informationen zur Schule
	 * @param schueler     der Schüler
	 * @param abiturjahr   das Abiturjahr des Jahrgangs der Wiederholung
	 *
	 * @return das Halbjahr, mit dem der Schüler im neuen Jahrgang einsteigt
	 */
	private GostHalbjahr pruefeWiederholung(final @NotNull Benutzer benutzer, final @NotNull DTOSchueler schueler, final int abiturjahr) {
		if (gostJahrgangsdatenRepository.findById(abiturjahr).isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Abiturjahrgang %d ist nicht angelegt.".formatted(abiturjahr));
		}
		final Schuljahresabschnitt abschnitt = benutzer.schuleGetSchuljahresabschnitt();
		if ((schueler.Schuljahresabschnitts_ID == null) || (schueler.Schuljahresabschnitts_ID != abschnitt.id)) {
			throw new ApiOperationException(Status.CONFLICT, "Eine Wiederholung kann nur für Schüler im aktuellen Schuljahresabschnitt der Schule geplant werden.");
		}
		final GostHalbjahr halbjahr = GostAbiturjahrUtils.getWiederholungHalbjahr(abiturjahr, abschnitt.schuljahr, abschnitt.abschnitt);
		if (halbjahr == null) {
			throw new ApiOperationException(Status.CONFLICT,
					"Der Abiturjahrgang %d ist im folgenden Schuljahresabschnitt nicht in der gymnasialen Oberstufe.".formatted(abiturjahr));
		}
		return halbjahr;
	}

}
