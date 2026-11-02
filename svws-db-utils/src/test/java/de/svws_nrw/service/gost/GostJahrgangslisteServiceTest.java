package de.svws_nrw.service.gost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
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
 * Testet den Service für die Liste der Abiturjahrgänge der gymnasialen Oberstufe
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GostJahrgangslisteService")
class GostJahrgangslisteServiceTest {

	private static final int SCHULJAHR = 2025;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	@Mock
	private SchuljahresabschnittService schuljahresabschnittService;

	@Mock
	private JahrgangRepository jahrgangRepository;

	@Mock
	private GostJahrgangsdatenRepository gostJahrgangsdatenRepository;

	@Mock
	private GostJahrgangsinformationenRepository gostJahrgangsinformationenRepository;

	private GostJahrgangslisteService service;

	@BeforeEach
	void setUp() {
		service = new GostJahrgangslisteService(eigeneSchuleService, schuljahresabschnittService, jahrgangRepository,
				gostJahrgangsdatenRepository, gostJahrgangsinformationenRepository, GostJahrgangMapper.INSTANCE);
	}

	private static Schuljahresabschnitt abschnitt(final long id, final int schuljahr, final int abschnitt) {
		final var result = new Schuljahresabschnitt();
		result.id = id;
		result.schuljahr = schuljahr;
		result.abschnitt = abschnitt;
		return result;
	}

	private static DTOJahrgang jahrgang(final long id, final String asdJahrgang) {
		final var result = new DTOJahrgang(id);
		result.ASDJahrgang = asdJahrgang;
		result.GliederungKuerzel = Schulgliederung.DEFAULT.daten(SCHULJAHR).kuerzel;
		return result;
	}

	private static List<DTOJahrgang> jahrgaenge(final String... asdJahrgaenge) {
		return IntStream.range(0, asdJahrgaenge.length)
				.mapToObj(i -> jahrgang(i + 1L, asdJahrgaenge[i]))
				.toList();
	}

	private static List<DTOGostJahrgangsdaten> jahrgangsdaten(final int... abiturjahre) {
		return Arrays.stream(abiturjahre).mapToObj(DTOGostJahrgangsdaten::new).toList();
	}

	private void bereiteVor(final Schulform schulform, final List<DTOJahrgang> jahrgaenge, final List<DTOGostJahrgangsdaten> jahrgangsdaten) {
		when(eigeneSchuleService.hatGymnasialeOberstufe()).thenReturn(true);
		when(jahrgangRepository.getAll()).thenReturn(jahrgaenge);
		when(eigeneSchuleService.getSchulform()).thenReturn(schulform);
		when(gostJahrgangsdatenRepository.getAll()).thenReturn(jahrgangsdaten);
	}


	@Test
	@DisplayName("Schule ohne gymnasiale Oberstufe: 404 mit Meldung")
	void getList_schuleOhneOberstufe_wirft404() {
		when(eigeneSchuleService.hatGymnasialeOberstufe()).thenReturn(false);

		assertThatThrownBy(() -> service.getList(abschnitt(1, SCHULJAHR, 1)))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND);
					assertThat(e.getBody()).isEqualTo("Die Schule hat eine Schulform ohne gymnasiale Oberstufe.");
				});
	}

	@Test
	@DisplayName("Keine Jahrgänge vorhanden: 404 mit Meldung")
	void getList_keineJahrgaenge_wirft404() {
		when(eigeneSchuleService.hatGymnasialeOberstufe()).thenReturn(true);
		when(jahrgangRepository.getAll()).thenReturn(List.of());

		assertThatThrownBy(() -> service.getList(abschnitt(1, SCHULJAHR, 1)))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND);
					assertThat(e.getBody()).isEqualTo("Es konnten keine Jahrgänge gefunden werden.");
				});
	}

	@Test
	@DisplayName("Ohne Abiturjahrgänge: Liste enthält nur den Eintrag 'Allgemeine Vorlagen'")
	void getList_ohneAbiturjahrgaenge_liefertNurVorlage() {
		bereiteVor(Schulform.GY, jahrgaenge("EF", "Q1", "Q2"), List.of());

		final var result = service.getList(abschnitt(1, SCHULJAHR, 1));

		assertThat(result).hasSize(1);
		final var vorlage = result.getFirst();
		assertThat(vorlage.abiturjahr).isEqualTo(-1);
		assertThat(vorlage.jahrgang).isNull();
		assertThat(vorlage.bezeichnung).isEqualTo("Allgemeine Vorlagen");
		assertThat(vorlage.istAbgeschlossen).isFalse();
	}

	@Test
	@DisplayName("Vorlage steht an erster Stelle, Abiturjahrgänge sind aufsteigend sortiert, die Vorlage aus der DB wird übersprungen")
	void getList_sortiertUndVorlageZuerst() {
		bereiteVor(Schulform.GY, jahrgaenge("EF", "Q1", "Q2"), jahrgangsdaten(2028, -1, 2026));

		final var result = service.getList(abschnitt(1, SCHULJAHR, 1));

		assertThat(result).extracting(j -> j.abiturjahr).containsExactly(-1, 2026, 2028);
		assertThat(result.getFirst().bezeichnung).isEqualTo("Allgemeine Vorlagen");
	}

	@ParameterizedTest(name = "Gymnasium: Abitur {0} -> Jahrgang {1}")
	@CsvSource({ "2026, Q2", "2027, Q1", "2028, EF", "2029, 09" })
	@DisplayName("Zuordnung Abiturjahr zu Jahrgang am Gymnasium (Sekundarstufe I und II)")
	void getList_gymnasium_ordnetJahrgangZu(final int abiturjahr, final String erwarteterJahrgang) {
		bereiteVor(Schulform.GY, jahrgaenge("09", "EF", "Q1", "Q2"), jahrgangsdaten(abiturjahr));

		final var result = service.getList(abschnitt(1, SCHULJAHR, 1));

		final var eintrag = result.get(1);
		assertThat(eintrag.jahrgang).isEqualTo(erwarteterJahrgang);
		assertThat(eintrag.bezeichnung).isEqualTo("Abi " + abiturjahr + " (" + erwarteterJahrgang + ")");
	}

	@ParameterizedTest(name = "Gesamtschule: Abitur {0} -> Jahrgang {1}")
	@CsvSource({ "2026, Q2", "2027, Q1", "2028, EF", "2030, 09" })
	@DisplayName("Zuordnung Abiturjahr zu Jahrgang an anderen Schulformen (Sekundarstufe I mit Zuschlag von 3 Jahren)")
	void getList_andereSchulform_ordnetJahrgangZu(final int abiturjahr, final String erwarteterJahrgang) {
		bereiteVor(Schulform.GE, jahrgaenge("09", "EF", "Q1", "Q2"), jahrgangsdaten(abiturjahr));

		final var result = service.getList(abschnitt(1, SCHULJAHR, 1));

		assertThat(result.get(1).jahrgang).isEqualTo(erwarteterJahrgang);
	}

	@Test
	@DisplayName("Passt kein Jahrgang, so ist der Jahrgang null und die Bezeichnung enthält keine Klammer")
	void getList_keinPassenderJahrgang() {
		bereiteVor(Schulform.GY, jahrgaenge("EF", "Q1", "Q2"), jahrgangsdaten(2040));

		final var eintrag = service.getList(abschnitt(1, SCHULJAHR, 1)).get(1);

		assertThat(eintrag.jahrgang).isNull();
		assertThat(eintrag.bezeichnung).isEqualTo("Abi 2040");
	}

	@ParameterizedTest(name = "Jahrgänge in der Reihenfolge {0}")
	@CsvSource({ "'11,EF'", "'EF,11'" })
	@DisplayName("Bei mehreren passenden Jahrgängen wird ein Jahrgang der gymnasialen Oberstufe bevorzugt")
	void getList_bevorzugtOberstufenjahrgang(final String jahrgaengeKommagetrennt) {
		bereiteVor(Schulform.GY, jahrgaenge(jahrgaengeKommagetrennt.split(",")), jahrgangsdaten(2028));

		assertThat(service.getList(abschnitt(1, SCHULJAHR, 1)).get(1).jahrgang).isEqualTo("EF");
	}

	@Test
	@DisplayName("Ist nur ein passender Jahrgang außerhalb der gymnasialen Oberstufe vorhanden, so wird dieser verwendet")
	void getList_nurAlterOberstufenjahrgang() {
		bereiteVor(Schulform.GY, jahrgaenge("11"), jahrgangsdaten(2028));

		assertThat(service.getList(abschnitt(1, SCHULJAHR, 1)).get(1).jahrgang).isEqualTo("11");
	}

	@Test
	@DisplayName("Das Halbjahr des Eintrags entspricht dem Abschnitt der Anfrage")
	void getList_halbjahrEntsprichtAbschnitt() {
		bereiteVor(Schulform.GY, jahrgaenge("EF", "Q1", "Q2"), jahrgangsdaten(2027));

		assertThat(service.getList(abschnitt(1, SCHULJAHR, 2)).get(1).halbjahr).isEqualTo(2);
	}

	@Test
	@DisplayName("Ein Abiturjahrgang ist abgeschlossen, wenn keine Restjahre mehr verbleiben")
	void getList_abgeschlossenerAbiturjahrgang() {
		bereiteVor(Schulform.GY, jahrgaenge("EF", "Q1", "Q2"), jahrgangsdaten(2024, 2025, 2026));

		final var result = service.getList(abschnitt(1, SCHULJAHR, 1));

		assertThat(result).extracting(j -> j.abiturjahr, j -> j.istAbgeschlossen)
				.containsExactly(tuple(-1, false), tuple(2024, true), tuple(2025, true), tuple(2026, false));
	}

	@Test
	@DisplayName("Die Flags für Blockung und Noten werden je Halbjahr aus den Jahrgangsinformationen bestimmt")
	void getList_setztFlagsJeHalbjahr() {
		bereiteVor(Schulform.GY, jahrgaenge("EF", "Q1", "Q2"), jahrgangsdaten(2027));
		// Abiturjahr 2027: 2024/2 = EF.2, 2025/1 = Q1.1, 2025/2 = Q1.2
		when(schuljahresabschnittService.getListBySchuljahre(List.of(2026, 2025, 2024)))
				.thenReturn(List.of(abschnitt(9, 2024, 2), abschnitt(10, 2025, 1), abschnitt(11, 2025, 2)));
		when(gostJahrgangsinformationenRepository.getAll()).thenReturn(List.of(
				new GostJahrgangsinformation(2, "Q1", 10, 3, 0),
				new GostJahrgangsinformation(2, "Q1", 11, 0, 5),
				new GostJahrgangsinformation(1, "EF", 9, 0, 0)));

		final var eintrag = service.getList(abschnitt(10, SCHULJAHR, 1)).get(1);

		assertThat(eintrag.istBlockungFestgelegt[GostHalbjahr.Q11.id]).isTrue();
		assertThat(eintrag.existierenNotenInLeistungsdaten[GostHalbjahr.Q11.id]).isFalse();
		assertThat(eintrag.istBlockungFestgelegt[GostHalbjahr.Q12.id]).isFalse();
		assertThat(eintrag.existierenNotenInLeistungsdaten[GostHalbjahr.Q12.id]).isTrue();
		assertThat(eintrag.istBlockungFestgelegt[GostHalbjahr.EF2.id]).isFalse();
		assertThat(eintrag.existierenNotenInLeistungsdaten[GostHalbjahr.EF2.id]).isFalse();
		assertThat(eintrag.istBlockungFestgelegt[GostHalbjahr.EF1.id]).isFalse();
	}

	@Test
	@DisplayName("Fehlt zu einem Abschnitt die Jahrgangsinformation, so sind die Flags false")
	void getList_ohneJahrgangsinformation_flagsFalse() {
		bereiteVor(Schulform.GY, jahrgaenge("EF", "Q1", "Q2"), jahrgangsdaten(2027));
		when(schuljahresabschnittService.getListBySchuljahre(List.of(2026, 2025, 2024))).thenReturn(List.of(abschnitt(10, 2025, 1)));

		final var eintrag = service.getList(abschnitt(10, SCHULJAHR, 1)).get(1);

		assertThat(eintrag.istBlockungFestgelegt[GostHalbjahr.Q11.id]).isFalse();
		assertThat(eintrag.existierenNotenInLeistungsdaten[GostHalbjahr.Q11.id]).isFalse();
	}

	@Test
	@DisplayName("Die Schuljahresabschnitte aller Abiturjahrgänge werden mit einer Abfrage bestimmt und je Abiturjahrgang auf dessen Oberstufenzeit begrenzt")
	void getList_mehrereAbiturjahrgaenge_eineAbfrageFuerAbschnitte() {
		bereiteVor(Schulform.GY, jahrgaenge("EF", "Q1", "Q2"), jahrgangsdaten(2027, 2028));
		// Abiturjahr 2027: Schuljahre 2024 bis 2026, Abiturjahr 2028: Schuljahre 2025 bis 2027
		when(schuljahresabschnittService.getListBySchuljahre(List.of(2026, 2025, 2024, 2027)))
				.thenReturn(List.of(abschnitt(9, 2024, 2), abschnitt(10, 2025, 1), abschnitt(12, 2027, 1)));
		when(gostJahrgangsinformationenRepository.getAll()).thenReturn(List.of(
				new GostJahrgangsinformation(1, "EF", 9, 4, 0),
				new GostJahrgangsinformation(3, "Q2", 12, 2, 0)));

		final var result = service.getList(abschnitt(10, SCHULJAHR, 1));

		verify(schuljahresabschnittService, times(1)).getListBySchuljahre(List.of(2026, 2025, 2024, 2027));
		final var abi2027 = result.get(1);
		final var abi2028 = result.get(2);
		assertThat(abi2027.istBlockungFestgelegt[GostHalbjahr.EF2.id]).isTrue();
		assertThat(abi2028.istBlockungFestgelegt[GostHalbjahr.Q21.id]).isTrue();
		// Der Abschnitt 2024/2 liegt vor der Oberstufenzeit des Abiturjahrgangs 2028 und wird dort nicht berücksichtigt
		assertThat(abi2028.istBlockungFestgelegt[GostHalbjahr.EF2.id]).isFalse();
		// Der Abschnitt 2027/1 liegt nach der Oberstufenzeit des Abiturjahrgangs 2027 und wird dort nicht berücksichtigt
		assertThat(abi2027.istBlockungFestgelegt[GostHalbjahr.Q21.id]).isFalse();
	}

}
