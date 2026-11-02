package de.svws_nrw.service.gost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.gost.GostJahrgangMapper;
import de.svws_nrw.repo.gost.GostJahrgangsdatenRepository;
import de.svws_nrw.repo.gost.GostJahrgangsinformation;
import de.svws_nrw.repo.gost.GostJahrgangsinformationenRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für die Bestimmung der Liste der Abiturjahrgänge der gymnasialen Oberstufe.
 */
public final class GostJahrgangslisteService {

	/** Das Abiturjahr des Eintrags für allgemeine Vorlagen, der für keinen konkreten Abiturjahrgang steht */
	private static final int ABITURJAHR_VORLAGE = -1;
	private static final String BEZEICHNUNG_VORLAGE = "Allgemeine Vorlagen";

	private static final String KEINE_GYMNASIALE_OBERSTUFE = "Die Schule hat eine Schulform ohne gymnasiale Oberstufe.";
	private static final String KEINE_JAHRGAENGE = "Es konnten keine Jahrgänge gefunden werden.";

	private final EigeneSchuleService eigeneSchuleService;
	private final SchuljahresabschnittService schuljahresabschnittService;
	private final JahrgangRepository jahrgangRepository;
	private final GostJahrgangsdatenRepository gostJahrgangsdatenRepository;
	private final GostJahrgangsinformationenRepository gostJahrgangsinformationenRepository;
	private final GostJahrgangMapper mapper;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param eigeneSchuleService                    der Service für den Zugriff auf die Schuldaten
	 * @param schuljahresabschnittService            der Service für den Zugriff auf die Schuljahresabschnitte
	 * @param jahrgangRepository                     das Repository für die Jahrgänge der Schule
	 * @param gostJahrgangsdatenRepository           das Repository für die Jahrgangsdaten der gymnasialen Oberstufe
	 * @param gostJahrgangsinformationenRepository   das Repository für die aggregierten Informationen zu den Oberstufenjahrgängen
	 * @param mapper                                 der Mapper für die Daten der Jahrgänge und Abiturjahrgänge
	 */
	public GostJahrgangslisteService(
			final EigeneSchuleService eigeneSchuleService,
			final SchuljahresabschnittService schuljahresabschnittService,
			final JahrgangRepository jahrgangRepository,
			final GostJahrgangsdatenRepository gostJahrgangsdatenRepository,
			final GostJahrgangsinformationenRepository gostJahrgangsinformationenRepository,
			final GostJahrgangMapper mapper
	) {
		this.eigeneSchuleService = eigeneSchuleService;
		this.schuljahresabschnittService = schuljahresabschnittService;
		this.jahrgangRepository = jahrgangRepository;
		this.gostJahrgangsdatenRepository = gostJahrgangsdatenRepository;
		this.gostJahrgangsinformationenRepository = gostJahrgangsinformationenRepository;
		this.mapper = mapper;
	}


	/**
	 * Bestimmt die Liste der Abiturjahrgänge für den angegebenen Schuljahresabschnitt.
	 *
	 * @param abschnitt   der Schuljahresabschnitt, auf den sich die Jahrgangsinformationen beziehen
	 *
	 * @return die Liste der Abiturjahrgänge, beginnend mit dem Eintrag "Allgemeine Vorlagen"
	 *
	 * @throws ApiOperationException   wenn die Schule keine gymnasiale Oberstufe hat oder keine Jahrgänge gefunden werden
	 */
	public List<GostJahrgang> getList(final Schuljahresabschnitt abschnitt) {
		pruefeGymnasialeOberstufe();
		final List<SchulJahrgang> jahrgaenge = getJahrgaengeSchule();

		// Der Eintrag für allgemeine Informationen zu Abiturjahrgängen steht an erster Stelle
		final List<GostJahrgang> daten = new ArrayList<>();
		daten.add(erstelleEintragVorlage());
		daten.addAll(createEintraegeAbiturjahrgaenge(abschnitt, jahrgaenge));
		return daten;
	}


	private void pruefeGymnasialeOberstufe() {
		if (!eigeneSchuleService.hatGymnasialeOberstufe()) {
			throw new ApiOperationException(Status.NOT_FOUND, KEINE_GYMNASIALE_OBERSTUFE);
		}
	}


	/**
	 * Bestimmt die Jahrgänge der Schule.
	 *
	 * @return die Jahrgänge der Schule
	 *
	 * @throws ApiOperationException   wenn keine Jahrgänge gefunden werden
	 */
	private List<SchulJahrgang> getJahrgaengeSchule() {
		final var dtosJahrgaenge = jahrgangRepository.getAll();
		if (dtosJahrgaenge.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, KEINE_JAHRGAENGE);
		}
		return dtosJahrgaenge.stream().map(mapper::toSchulJahrgang).toList();
	}


	private static GostJahrgang erstelleEintragVorlage() {
		final var eintrag = new GostJahrgang();
		eintrag.abiturjahr = ABITURJAHR_VORLAGE;
		eintrag.jahrgang = null;
		eintrag.bezeichnung = BEZEICHNUNG_VORLAGE;
		eintrag.istAbgeschlossen = false;
		return eintrag;
	}


	/**
	 * Erstellt die Einträge aller Abiturjahrgänge aus der Datenbank, aufsteigend sortiert nach dem Abiturjahr,
	 * einschließlich der Informationen zu Blockung und Noten.
	 *
	 * @param abschnitt        der Schuljahresabschnitt, auf den sich die Jahrgangsinformationen beziehen
	 * @param jahrgaenge       die Jahrgänge der Schule
	 *
	 * @return die Einträge der Abiturjahrgänge
	 */
	private List<GostJahrgang> createEintraegeAbiturjahrgaenge(final Schuljahresabschnitt abschnitt, final List<SchulJahrgang> jahrgaenge) {
		final Schulform schulform = eigeneSchuleService.getSchulform();
		final HashMap2D<Long, String, GostJahrgangsinformation> mapJahrgangsinfos = getMapJahrgangsinformationen();
		final List<Integer> abiturjahre = getAbiturjahreSortedAsc();
		final Map<Integer, List<Schuljahresabschnitt>> mapAbschnitteBySchuljahr = getMapAbschnitteOberstufeBySchuljahr(abiturjahre);

		final List<GostJahrgang> eintraege = new ArrayList<>();
		for (final int abiturjahr : abiturjahre) {
			final var eintrag = createAbiturjahrgang(abiturjahr, abschnitt, schulform, jahrgaenge);
			ergaenzeJahrgangsinformationen(eintrag, mapAbschnitteBySchuljahr, mapJahrgangsinfos);
			eintraege.add(eintrag);
		}
		return eintraege;
	}


	/**
	 * Bestimmt die Informationen zu den Oberstufenjahrgängen als Map (ID des Schuljahresabschnittes, Kürzel des ASD-Jahrgangs).
	 *
	 * @return die Map mit den Jahrgangsinformationen
	 */
	private HashMap2D<Long, String, GostJahrgangsinformation> getMapJahrgangsinformationen() {
		final HashMap2D<Long, String, GostJahrgangsinformation> map = new HashMap2D<>();
		gostJahrgangsinformationenRepository.getAll()
				.forEach(info -> map.put(info.idSchuljahresabschnitt(), info.kuerzelASDJahrgang(), info));
		return map;
	}


	/**
	 * Bestimmt die Abiturjahre aller Abiturjahrgänge aus der Datenbank, aufsteigend sortiert.
	 * Der Eintrag für allgemeine Vorlagen (Abiturjahr {@link #ABITURJAHR_VORLAGE}) wird dabei nicht berücksichtigt.
	 *
	 * @return die sortierten Abiturjahre
	 */
	private List<Integer> getAbiturjahreSortedAsc() {
		return gostJahrgangsdatenRepository.getAll().stream()
				.mapToInt(jahrgangsdaten -> jahrgangsdaten.Abi_Jahrgang)
				.filter(abiturjahr -> abiturjahr > ABITURJAHR_VORLAGE)
				.sorted()
				.boxed()
				.toList();
	}


	/**
	 * Bestimmt die Schuljahresabschnitte der Oberstufenzeit aller übergebenen Abiturjahrgänge mit einer einzigen Abfrage
	 * und gruppiert diese nach dem Schuljahr. Die Map enthält die Schuljahre aller Abiturjahrgänge, ein einzelner
	 * Abiturjahrgang besucht davon nur die Schuljahre aus {@link #getSchuljahreDerOberstufe(int)}.
	 *
	 * @param abiturjahre   die Abiturjahre der Abiturjahrgänge
	 *
	 * @return die Map mit den Schuljahresabschnitten je Schuljahr
	 */
	private Map<Integer, List<Schuljahresabschnitt>> getMapAbschnitteOberstufeBySchuljahr(final List<Integer> abiturjahre) {
		final List<Integer> schuljahre = abiturjahre.stream()
				.flatMap(GostJahrgangslisteService::getSchuljahreDerOberstufe)
				.distinct()
				.toList();
		return schuljahresabschnittService.getListBySchuljahre(schuljahre).stream()
				.collect(Collectors.groupingBy(a -> a.schuljahr));
	}


	/**
	 * Bestimmt die Schuljahre, in denen ein Abiturjahrgang die gymnasiale Oberstufe besucht - beginnend mit dem letzten Schuljahr.
	 *
	 * @param abiturjahr   das Abiturjahr
	 *
	 * @return die Schuljahre der Oberstufenzeit
	 */
	private static Stream<Integer> getSchuljahreDerOberstufe(final int abiturjahr) {
		return IntStream.rangeClosed(1, SchulJahrgang.ANZAHL_SCHULJAHRE_OBERSTUFE)
				.map(i -> abiturjahr - i)
				.boxed();
	}


	/**
	 * Erstellt den Eintrag eines Abiturjahrgangs ohne die Informationen zu Blockung und Noten.
	 *
	 * @param abiturjahr       das Abiturjahr
	 * @param abschnitt        der Schuljahresabschnitt, auf den sich die Jahrgangsinformationen beziehen
	 * @param schulform        die Schulform der Schule
	 * @param jahrgaenge       die Jahrgänge der Schule
	 *
	 * @return der Eintrag des Abiturjahrgangs
	 */
	private static GostJahrgang createAbiturjahrgang(
			final int abiturjahr,
			final Schuljahresabschnitt abschnitt,
			final Schulform schulform,
			final List<SchulJahrgang> jahrgaenge
	) {
		final int restjahre = abiturjahr - abschnitt.schuljahr;
		final var eintrag = new GostJahrgang();
		eintrag.abiturjahr = abiturjahr;
		eintrag.jahrgang = bestimmeJahrgang(schulform, jahrgaenge, restjahre);
		eintrag.halbjahr = abschnitt.abschnitt;
		eintrag.bezeichnung = bestimmeBezeichnung(abiturjahr, eintrag.jahrgang);
		eintrag.istAbgeschlossen = (restjahre < 1);
		return eintrag;
	}


	/**
	 * Bestimmt die Bezeichnung eines Abiturjahrgangs, ergänzt um den ASD-Jahrgang, sofern dieser bestimmt werden konnte.
	 *
	 * @param abiturjahr   das Abiturjahr
	 * @param jahrgang     das Kürzel des ASD-Jahrgangs oder null
	 *
	 * @return die Bezeichnung, z.B. "Abi 2027 (Q1)" oder "Abi 2027"
	 */
	private static String bestimmeBezeichnung(final int abiturjahr, final String jahrgang) {
		return "Abi " + abiturjahr + ((jahrgang == null) ? "" : " (" + jahrgang + ")");
	}


	/**
	 * Bestimmt den ASD-Jahrgang, in welchem sich ein Abiturjahrgang mit den angegebenen Restjahren befindet. Gibt es mehrere
	 * passende Jahrgänge, so wird ein Jahrgang der gymnasialen Oberstufe bevorzugt.
	 *
	 * @param schulform        die Schulform der Schule
	 * @param jahrgaenge       die Jahrgänge der Schule
	 * @param restjahre        die Anzahl der Jahre bis zum Abitur
	 *
	 * @return das Kürzel des ASD-Jahrgangs oder null, falls kein Jahrgang passt
	 */
	private static String bestimmeJahrgang(
			final Schulform schulform,
			final List<SchulJahrgang> jahrgaenge,
			final int restjahre
	) {
		final List<SchulJahrgang> passendeJahrgaenge = jahrgaenge.stream()
				.filter(jahrgang -> jahrgang.hatRestjahreBisAbitur(schulform, restjahre))
				.toList();
		if (passendeJahrgaenge.isEmpty()) {
			return null;
		}
		// Ein Jahrgang der gymnasialen Oberstufe wird bevorzugt, ansonsten wird der letzte passende Jahrgang verwendet
		return passendeJahrgaenge.stream()
				.filter(SchulJahrgang::istGymOb)
				.findFirst()
				.orElse(passendeJahrgaenge.getLast())
				.kuerzelASDJahrgang();
	}


	/**
	 * Ergänzt bei dem Eintrag die Information, ob bereits eine Blockung persistiert wurde und ob bereits Noten existieren.
	 * Dies wird anhand der angelegten Kurse in den entsprechenden Lernabschnitten bestimmt. Berücksichtigt werden nur die
	 * Schuljahresabschnitte der Oberstufenzeit des Abiturjahrgangs.
	 *
	 * @param eintrag                    der Eintrag des Abiturjahrgangs
	 * @param mapAbschnitteBySchuljahr   die Schuljahresabschnitte der Oberstufenzeit aller Abiturjahrgänge je Schuljahr
	 * @param mapJahrgangsinfos          die Jahrgangsinformationen
	 */
	private static void ergaenzeJahrgangsinformationen(
			final GostJahrgang eintrag,
			final Map<Integer, List<Schuljahresabschnitt>> mapAbschnitteBySchuljahr,
			final HashMap2D<Long, String, GostJahrgangsinformation> mapJahrgangsinfos
	) {
		getSchuljahreDerOberstufe(eintrag.abiturjahr)
				.flatMap(schuljahr -> mapAbschnitteBySchuljahr.getOrDefault(schuljahr, List.of()).stream())
				.forEach(a -> ergaenzeJahrgangsinformationenFuerAbschnitt(eintrag, a, mapJahrgangsinfos));
	}


	/**
	 * Ergänzt bei dem Eintrag die Information zu Blockung und Noten für das Halbjahr, welches dem Schuljahresabschnitt entspricht.
	 *
	 * @param eintrag             der Eintrag des Abiturjahrgangs
	 * @param abschnitt           der Schuljahresabschnitt in der Oberstufenzeit des Abiturjahrgangs
	 * @param mapJahrgangsinfos   die Jahrgangsinformationen
	 */
	private static void ergaenzeJahrgangsinformationenFuerAbschnitt(
			final GostJahrgang eintrag,
			final Schuljahresabschnitt abschnitt,
			final HashMap2D<Long, String, GostJahrgangsinformation> mapJahrgangsinfos
	) {
		final var halbjahr = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(eintrag.abiturjahr, abschnitt.schuljahr, abschnitt.abschnitt);
		final GostJahrgangsinformation info = mapJahrgangsinfos.getOrNull(abschnitt.id, halbjahr.jahrgang);
		eintrag.istBlockungFestgelegt[halbjahr.id] = (info != null) && (info.anzahlKurse() > 0);
		eintrag.existierenNotenInLeistungsdaten[halbjahr.id] = (info != null) && (info.anzahlNoten() > 0);
	}

}
