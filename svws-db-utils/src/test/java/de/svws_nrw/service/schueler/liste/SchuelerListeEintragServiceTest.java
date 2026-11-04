package de.svws_nrw.service.schueler.liste;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholung;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
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
 * Tests für den {@link SchuelerListeEintragService}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerListeEintragServiceTest {

	private static final long ID_ABSCHNITT_GEWAEHLT = 20L;
	private static final long ID_ABSCHNITT_SCHUELER = 19L;
	private static final long ID_JAHRGANG = 7L;
	private static final long ID_KLASSE_1 = 1L;
	private static final long ID_KLASSE_2 = 2L;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private SchuelerLernabschnittRepository lernabschnittRepository;

	@Mock
	private KursSchuelerRepository kursSchuelerRepository;

	@Mock
	private JahrgangRepository jahrgangRepository;

	@Mock
	private GostSchuelerWiederholungRepository wiederholungRepository;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	@Mock
	private SchuljahresabschnittService schuljahresabschnittService;

	private SchuelerListeEintragService service;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		service = new SchuelerListeEintragService(schuelerRepository, lernabschnittRepository, kursSchuelerRepository, jahrgangRepository,
				wiederholungRepository, eigeneSchuleService, schuljahresabschnittService, SchuelerListeEintragMapper.INSTANCE);
	}

	private static DTOSchueler schueler(final long id, final String nachname, final String vorname) {
		final var s = new DTOSchueler(id, "GU-" + id, false);
		s.Nachname = nachname;
		s.Vorname = vorname;
		s.Geschlecht = Geschlecht.M;
		s.Schuljahresabschnitts_ID = ID_ABSCHNITT_SCHUELER;
		s.idStatus = 2;
		s.Duplikat = false;
		return s;
	}

	private static DTOSchuelerLernabschnittsdaten lernabschnitt(final long idSchueler, final long idAbschnitt, final long idKlasse) {
		final var l = new DTOSchuelerLernabschnittsdaten(1000L + idSchueler + idAbschnitt, idSchueler, idAbschnitt, false, false, 1L);
		l.Klassen_ID = idKlasse;
		l.Jahrgang_ID = ID_JAHRGANG;
		return l;
	}

	private static Schuljahresabschnitt abschnitt(final long id, final int schuljahr, final int abschnitt) {
		final var a = new Schuljahresabschnitt();
		a.id = id;
		a.schuljahr = schuljahr;
		a.abschnitt = abschnitt;
		return a;
	}

	private static DTOJahrgang jahrgang(final String asdJahrgang) {
		final var j = new DTOJahrgang(ID_JAHRGANG);
		j.ASDJahrgang = asdJahrgang;
		return j;
	}

	/**
	 * Konfiguriert die Mocks für einen Durchlauf von getListe mit mindestens einem Schüler.
	 *
	 * @param nurAktive    gibt an, ob nur aktive Schüler geliefert werden
	 * @param schueler     die Schüler, die das Repository liefert
	 * @param aktuelle     die aktuellen Lernabschnitte der Schüler, zugeordnet über die Schüler-ID
	 * @param gewaehlte    die Lernabschnitte im gewählten Schuljahresabschnitt, zugeordnet über die Schüler-ID
	 * @param schulform    die Schulform der eigenen Schule
	 * @param asdJahrgang  der ASD-Jahrgang des Jahrgangs, den das Jahrgangs-Repository liefert
	 */
	private void stubListe(final boolean nurAktive, final List<DTOSchueler> schueler, final Map<Long, DTOSchuelerLernabschnittsdaten> aktuelle,
			final Map<Long, DTOSchuelerLernabschnittsdaten> gewaehlte, final Schulform schulform, final String asdJahrgang) {
		when(schuljahresabschnittService.getById(ID_ABSCHNITT_GEWAEHLT)).thenReturn(abschnitt(ID_ABSCHNITT_GEWAEHLT, 2025, 1));
		when(schuljahresabschnittService.getList())
				.thenReturn(List.of(abschnitt(ID_ABSCHNITT_SCHUELER, 2024, 2), abschnitt(ID_ABSCHNITT_GEWAEHLT, 2025, 1)));
		when(schuelerRepository.getListNichtGeloescht(nurAktive)).thenReturn(schueler);
		when(lernabschnittRepository.getMapAktuelleBySchuelerIDs(anyCollection())).thenReturn(aktuelle);
		when(lernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(anyCollection(), eq(ID_ABSCHNITT_GEWAEHLT))).thenReturn(gewaehlte);
		when(eigeneSchuleService.getSchulform()).thenReturn(schulform);
		when(jahrgangRepository.getAll()).thenReturn(List.of(jahrgang(asdJahrgang)));
		lenient().when(kursSchuelerRepository.getMapBySchueler(anyCollection(), eq(ID_ABSCHNITT_GEWAEHLT))).thenReturn(new HashMap<>());
	}

	@Test
	@DisplayName("getListe | Der Lernabschnitt des gewählten Abschnitts hat Vorrang vor dem aktuellen Abschnitt des Schülers")
	void getListe_gewaehlterAbschnittHatVorrang() {
		final var mitBeiden = schueler(1L, "Alpha", "Anna");
		final var nurAktuell = schueler(2L, "Beta", "Bernd");
		stubListe(false, List.of(mitBeiden, nurAktuell),
				Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_SCHUELER, ID_KLASSE_1), 2L, lernabschnitt(2L, ID_ABSCHNITT_SCHUELER, ID_KLASSE_1)),
				Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_2)),
				Schulform.G, "03");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).hasSize(2);
		final var eintrag1 = liste.stream().filter(e -> e.id == 1L).findFirst().orElseThrow();
		final var eintrag2 = liste.stream().filter(e -> e.id == 2L).findFirst().orElseThrow();
		assertThat(eintrag1.idSchuljahresabschnitt).isEqualTo(ID_ABSCHNITT_GEWAEHLT);
		assertThat(eintrag1.idKlasse).isEqualTo(ID_KLASSE_2);
		assertThat(eintrag1.idJahrgang).isEqualTo(ID_JAHRGANG);
		assertThat(eintrag1.jahrgang).isEqualTo("03");
		assertThat(eintrag2.idSchuljahresabschnitt).isEqualTo(ID_ABSCHNITT_SCHUELER);
		assertThat(eintrag2.idKlasse).isEqualTo(ID_KLASSE_1);
	}

	@Test
	@DisplayName("getListe | Schüler ohne Lernabschnitt werden übersprungen")
	void getListe_schuelerOhneLernabschnittWirdUebersprungen() {
		stubListe(false, List.of(schueler(1L, "Alpha", "Anna"), schueler(2L, "Beta", "Bernd")),
				Map.of(), Map.of(2L, lernabschnitt(2L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)), Schulform.G, "03");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).extracting(e -> e.id).containsExactly(2L);
	}

	@Test
	@DisplayName("getListe | Schüler mit einem nicht auflösbaren Schuljahresabschnitt im Lernabschnitt werden übersprungen")
	void getListe_unbekannterAbschnittImLernabschnittWirdUebersprungen() {
		stubListe(false, List.of(schueler(1L, "Alpha", "Anna"), schueler(2L, "Beta", "Bernd")),
				Map.of(1L, lernabschnitt(1L, 999L, ID_KLASSE_1)), Map.of(2L, lernabschnitt(2L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)), Schulform.G, "03");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).extracting(e -> e.id).containsExactly(2L);
	}

	@Test
	@DisplayName("getListe | nurAktive wird an das Schüler-Repository weitergereicht")
	void getListe_nurAktive() {
		stubListe(true, List.of(schueler(1L, "Alpha", "Anna")),
				Map.of(), Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)), Schulform.G, "03");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, true);

		assertThat(liste).hasSize(1);
		verify(schuelerRepository).getListNichtGeloescht(true);
		verify(schuelerRepository, never()).getListNichtGeloescht(false);
	}

	@Test
	@DisplayName("getListe | Ohne Schüler wird eine leere Liste ohne weitere Abfragen zurückgegeben")
	void getListe_leereSchuelerliste() {
		when(schuljahresabschnittService.getById(ID_ABSCHNITT_GEWAEHLT)).thenReturn(abschnitt(ID_ABSCHNITT_GEWAEHLT, 2025, 1));
		when(schuelerRepository.getListNichtGeloescht(false)).thenReturn(List.of());

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).isEmpty();
		verifyNoInteractions(lernabschnittRepository, kursSchuelerRepository, jahrgangRepository, eigeneSchuleService);
	}

	@Test
	@DisplayName("getListe | Ein unbekannter Schuljahresabschnitt führt zu einer ApiOperationException mit dem Status NOT_FOUND")
	void getListe_unbekannterAbschnitt() {
		when(schuljahresabschnittService.getById(ID_ABSCHNITT_GEWAEHLT))
				.thenThrow(new ApiOperationException(Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 20 wurde nicht gefunden."));

		assertThatThrownBy(() -> service.getListe(ID_ABSCHNITT_GEWAEHLT, false))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND));
		verifyNoInteractions(schuelerRepository);
	}

	@Test
	@DisplayName("getListe | Bei einer Schulform mit gymnasialer Oberstufe wird der Abiturjahrgang bestimmt")
	void getListe_schuleMitGymnasialerOberstufe() {
		stubListe(false, List.of(schueler(1L, "Alpha", "Anna")),
				Map.of(), Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)), Schulform.GY, "EF");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).hasSize(1);
		final SchuelerListeEintrag eintrag = liste.get(0);
		assertThat(eintrag.jahrgang).isEqualTo("EF");
		assertThat(eintrag.abiturjahrgang)
				.isEqualTo(GostAbiturjahrUtils.getGostAbiturjahr(Schulform.GY, Schulgliederung.data().getWertByIDOrNull(eintrag.idSchulgliederung), 2025, "EF"))
				.isEqualTo(2028);
	}

	@Test
	@DisplayName("getListe | Bei einer Schulform mit gymnasialer Oberstufe wird der Abiturjahrgang der Wiederholung ergänzt")
	void getListe_wiederholung() {
		stubListe(false, List.of(schueler(1L, "Alpha", "Anna"), schueler(2L, "Beta", "Ben")), Map.of(),
				Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1), 2L, lernabschnitt(2L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)),
				Schulform.GY, "EF");
		when(wiederholungRepository.findMapByIds(anyCollection())).thenReturn(Map.of(2L, new DTOGostSchuelerWiederholung(2L, 2029, GostHalbjahr.Q11)));

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).hasSize(2)
				.satisfiesExactly(
						a -> assertThat(a.abiturjahrgangWiederholung).isNull(),
						b -> assertThat(b.abiturjahrgangWiederholung).isEqualTo(2029));
	}

	@Test
	@DisplayName("getListe | Bei einer Schulform ohne gymnasiale Oberstufe werden keine Wiederholungen geladen")
	void getListe_keineWiederholungOhneGymnasialeOberstufe() {
		stubListe(false, List.of(schueler(1L, "Alpha", "Anna")),
				Map.of(), Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)), Schulform.G, "EF");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).hasSize(1).allSatisfy(e -> assertThat(e.abiturjahrgangWiederholung).isNull());
		verifyNoInteractions(wiederholungRepository);
	}

	@Test
	@DisplayName("getListe | Bei einer leeren Schülerliste werden keine Wiederholungen geladen")
	void getListe_keineWiederholungBeiLeererListe() {
		stubListe(false, List.of(schueler(1L, "Alpha", "Anna")), Map.of(), Map.of(), Schulform.GY, "EF");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).isEmpty();
		verifyNoInteractions(wiederholungRepository);
	}

	@Test
	@DisplayName("getListe | Bei einer Schulform ohne gymnasiale Oberstufe wird kein Abiturjahrgang bestimmt")
	void getListe_schuleOhneGymnasialeOberstufe() {
		stubListe(false, List.of(schueler(1L, "Alpha", "Anna")),
				Map.of(), Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)), Schulform.G, "EF");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).hasSize(1);
		assertThat(liste.get(0).abiturjahrgang).isNull();
	}

	@Test
	@DisplayName("getListe | Die belegten Kurse des Abschnitts werden je Schüler ergänzt")
	void getListe_belegteKurse() {
		stubListe(false, List.of(schueler(1L, "Alpha", "Anna"), schueler(2L, "Beta", "Bernd")),
				Map.of(), Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1), 2L, lernabschnitt(2L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)),
				Schulform.G, "03");
		final var kurse = new HashMap<Long, List<DTOKursSchueler>>();
		kurse.put(1L, List.of(new DTOKursSchueler(100L, 1L, 1L), new DTOKursSchueler(101L, 1L, 2L)));
		kurse.put(2L, List.of());
		when(kursSchuelerRepository.getMapBySchueler(anyCollection(), eq(ID_ABSCHNITT_GEWAEHLT))).thenReturn(kurse);

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		final var eintrag1 = liste.stream().filter(e -> e.id == 1L).findFirst().orElseThrow();
		final var eintrag2 = liste.stream().filter(e -> e.id == 2L).findFirst().orElseThrow();
		assertThat(eintrag1.kurse).containsExactlyInAnyOrder(100L, 101L);
		assertThat(eintrag2.kurse).isEmpty();
	}

	@Test
	@DisplayName("getListe | Schüler ohne Eintrag in der Kurs-Zuordnung erhalten keine Kurse")
	void getListe_keineKursZuordnung() {
		stubListe(false, List.of(schueler(1L, "Alpha", "Anna")),
				Map.of(), Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)), Schulform.G, "03");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste.get(0).kurse).isEmpty();
		verify(kursSchuelerRepository).getMapBySchueler(List.of(1L), ID_ABSCHNITT_GEWAEHLT);
	}

	@Test
	@DisplayName("getListe | Die Liste ist nach Klasse, Nachname und Vorname sortiert")
	void getListe_sortierung() {
		stubListe(false,
				List.of(schueler(1L, "Zorn", "Anna"), schueler(2L, "Adler", "Zoe"), schueler(3L, "Adler", "Alex"), schueler(4L, "Maier", "Max")),
				Map.of(),
				Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1), 2L, lernabschnitt(2L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1),
						3L, lernabschnitt(3L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1), 4L, lernabschnitt(4L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_2)),
				Schulform.G, "03");

		final var liste = service.getListe(ID_ABSCHNITT_GEWAEHLT, false);

		assertThat(liste).extracting(e -> e.id).containsExactly(3L, 2L, 1L, 4L);
	}

	@Test
	@DisplayName("getListeAktuell | Verwendet den aktuellen Schuljahresabschnitt der Schule")
	void getListeAktuell_success() {
		when(eigeneSchuleService.getIdSchuljahresabschnitt()).thenReturn(ID_ABSCHNITT_GEWAEHLT);
		stubListe(true, List.of(schueler(1L, "Alpha", "Anna")),
				Map.of(), Map.of(1L, lernabschnitt(1L, ID_ABSCHNITT_GEWAEHLT, ID_KLASSE_1)), Schulform.G, "03");

		final var liste = service.getListeAktuell(true);

		assertThat(liste).extracting(e -> e.id).containsExactly(1L);
		verify(schuljahresabschnittService).getById(ID_ABSCHNITT_GEWAEHLT);
		verify(schuelerRepository).getListNichtGeloescht(true);
	}

	@Test
	@DisplayName("getListeAktuell | Ohne Schule wird eine ApiOperationException mit dem Status NOT_FOUND geworfen")
	void getListeAktuell_keineSchule() {
		when(eigeneSchuleService.getIdSchuljahresabschnitt()).thenThrow(new RepositoryException("Keine Schule vorhanden"));

		assertThatThrownBy(() -> service.getListeAktuell(false))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND);
					assertThat(e.getBody()).isEqualTo("Die Daten der eigenen Schule konnten nicht ermittelt werden.");
				});
		verify(schuljahresabschnittService, never()).getById(anyLong());
		verifyNoInteractions(schuelerRepository);
	}

}
