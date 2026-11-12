package de.svws_nrw.service.lehrer.liste;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.liste.LehrerListeEintragMapper;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests für den {@link LehrerListeService}.
 */
@ExtendWith(MockitoExtension.class)
class LehrerListeServiceTest {

	private static final long ID_ABSCHNITT = 20L;

	@Mock
	private LehrerRepository lehrerRepository;

	@Mock
	private SchuljahresabschnittService schuljahresabschnittService;

	private MockedStatic<TransactionSupport> transactionSupport;

	private LehrerListeService service;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
		service = new LehrerListeService(lehrerRepository, schuljahresabschnittService, LehrerListeEintragMapper.INSTANCE);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private static DTOLehrer lehrer(final long id, final String kuerzel, final String nachname) {
		final var lehrer = new DTOLehrer(id, kuerzel, nachname);
		lehrer.Vorname = "Max";
		lehrer.PersonTyp = PersonalTyp.LEHRKRAFT;
		lehrer.Sortierung = 100;
		lehrer.Sichtbar = true;
		lehrer.statistikRelevant = true;
		return lehrer;
	}

	private static Schuljahresabschnitt abschnitt(final int schuljahr, final int halbjahr) {
		final var abschnitt = new Schuljahresabschnitt();
		abschnitt.id = ID_ABSCHNITT;
		abschnitt.schuljahr = schuljahr;
		abschnitt.abschnitt = halbjahr;
		return abschnitt;
	}

	@Test
	@DisplayName("getListe | ohne Referenzinformation | sortiert nach Kürzel und fragt keine Referenzen ab")
	void getListe_ohneReferenzInfo() {
		when(lehrerRepository.getAll()).thenReturn(List.of(lehrer(2L, "XYZ", "Zander"), lehrer(1L, "ABC", "Adler")));

		assertThat(service.getList(false))
				.extracting(e -> e.id, e -> e.kuerzel, e -> e.istAktiv, e -> e.referenziertInAnderenTabellen)
				.containsExactly(
						tuple(1L, "ABC", true, null),
						tuple(2L, "XYZ", true, null));

		verify(lehrerRepository, never()).getReferencedIds(any());
		verifyNoInteractions(schuljahresabschnittService);
	}

	@Test
	@DisplayName("getListe | mit Referenzinformation | ergänzt die Referenzierung mit einer einzigen Abfrage")
	void getListe_mitReferenzInfo() {
		when(lehrerRepository.getAll()).thenReturn(List.of(lehrer(1L, "ABC", "Adler"), lehrer(2L, "XYZ", "Zander")));
		when(lehrerRepository.getReferencedIds(List.of(1L, 2L))).thenReturn(Set.of(2L));

		assertThat(service.getList(true))
				.extracting(e -> e.id, e -> e.referenziertInAnderenTabellen)
				.containsExactly(
						tuple(1L, false),
						tuple(2L, true));

		verify(lehrerRepository).getReferencedIds(List.of(1L, 2L));
	}

	@Test
	@DisplayName("getListe | ohne Lehrer | leere Liste ohne Referenzabfrage")
	void getListe_leer() {
		when(lehrerRepository.getAll()).thenReturn(List.of());

		assertThat(service.getList(true)).isEmpty();

		verify(lehrerRepository, never()).getReferencedIds(any());
	}

	@Test
	@DisplayName("getListeFuerAbschnitt | setzt die Aktivität anhand des Schuljahresabschnitts")
	void getListeFuerAbschnitt_aktivitaet() {
		final var abgegangen = lehrer(1L, "ABC", "Adler");
		abgegangen.DatumAbgang = "2023-07-31";
		final var aktiv = lehrer(2L, "XYZ", "Zander");
		when(schuljahresabschnittService.getById(ID_ABSCHNITT)).thenReturn(abschnitt(2024, 1));
		when(lehrerRepository.getAll()).thenReturn(List.of(abgegangen, aktiv));

		assertThat(service.getListBySchuljahresabschnitt(ID_ABSCHNITT, false))
				.extracting(e -> e.id, e -> e.istAktiv)
				.containsExactly(
						tuple(1L, false),
						tuple(2L, true));
	}

	@Test
	@DisplayName("getListeFuerAbschnitt | ermittelt den Schuljahresabschnitt nur einmal und ergänzt die Referenzinformation")
	void getListeFuerAbschnitt_einmalAbschnittUndReferenzen() {
		when(schuljahresabschnittService.getById(ID_ABSCHNITT)).thenReturn(abschnitt(2024, 1));
		when(lehrerRepository.getAll()).thenReturn(List.of(lehrer(1L, "ABC", "Adler"), lehrer(2L, "XYZ", "Zander")));
		when(lehrerRepository.getReferencedIds(List.of(1L, 2L))).thenReturn(Set.of(1L));

		assertThat(service.getListBySchuljahresabschnitt(ID_ABSCHNITT, true))
				.extracting(e -> e.referenziertInAnderenTabellen)
				.containsExactly(true, false);

		verify(schuljahresabschnittService).getById(ID_ABSCHNITT);
	}

	@Test
	@DisplayName("getListeFuerAbschnitt | unbekannter Abschnitt führt auch ohne Lehrer zu einem Fehler")
	void getListeFuerAbschnitt_abschnittNichtGefunden() {
		when(schuljahresabschnittService.getById(ID_ABSCHNITT))
				.thenThrow(new ApiOperationException(Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 20 wurde nicht gefunden."));

		assertThatThrownBy(() -> service.getListBySchuljahresabschnitt(ID_ABSCHNITT, true))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);

		verifyNoInteractions(lehrerRepository);
	}

	@Test
	@DisplayName("delete | löscht nicht referenzierte Lehrer und meldet referenzierte und unbekannte IDs")
	void delete_referenzUndUnbekannt() {
		final var frei = lehrer(1L, "ABC", "Adler");
		final var referenziert = lehrer(2L, "XYZ", "Zander");
		final var ids = List.of(1L, 2L, 3L);
		when(lehrerRepository.getReferencedIds(ids)).thenReturn(Set.of(2L));
		when(lehrerRepository.findListByIds(ids)).thenReturn(List.of(frei, referenziert));
		when(lehrerRepository.delete(List.of(frei))).thenReturn(List.of(frei));

		assertThat(service.delete(ids))
				.hasSize(3)
				.extracting(r -> r.id, r -> r.success)
				.containsExactly(
						tuple(1L, true),
						tuple(2L, false),
						tuple(3L, false));

		verify(lehrerRepository).delete(List.of(frei));
	}

	@Test
	@DisplayName("delete | die Meldungen nennen Lehrer und ID")
	void delete_meldungen() {
		final var referenziert = lehrer(2L, "XYZ", "Zander");
		final var ids = List.of(2L, 3L);
		when(lehrerRepository.getReferencedIds(ids)).thenReturn(Set.of(2L));
		when(lehrerRepository.findListByIds(ids)).thenReturn(List.of(referenziert));
		when(lehrerRepository.delete(List.<DTOLehrer>of())).thenReturn(List.of());

		assertThat(service.delete(ids))
				.extracting(r -> r.log.getFirst())
				.containsExactly(
						"Lehrer mit ID 2 ist referenziert und kann nicht gelöscht werden.",
						"Lehrer mit ID 3 wurde nicht gefunden.");
	}

	@Test
	@DisplayName("delete | leere ID-Liste liefert eine leere Antwort")
	void delete_leereListe() {
		when(lehrerRepository.getReferencedIds(List.of())).thenReturn(Set.of());
		when(lehrerRepository.findListByIds(List.of())).thenReturn(List.of());
		when(lehrerRepository.delete(List.<DTOLehrer>of())).thenReturn(List.of());

		assertThat(service.delete(List.of())).isEmpty();
	}

	@Test
	@DisplayName("Der Eintrag wird über den Mapper erzeugt und enthält die Defaults")
	void getListe_mapperDefaults() {
		final var ohneWerte = new DTOLehrer(1L, "ABC", "Adler");
		when(lehrerRepository.getAll()).thenReturn(List.of(ohneWerte));

		assertThat(service.getList(false))
				.singleElement()
				.isInstanceOf(LehrerListeEintrag.class)
				.hasFieldOrPropertyWithValue("sortierung", LehrerListeEintragMapper.DEFAULT_SORTIERUNG)
				.hasFieldOrPropertyWithValue("istSichtbar", true);
	}

}
