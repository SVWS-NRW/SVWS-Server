package de.svws_nrw.service.schueler.liste;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.gost.GostJahrgangslisteService;
import de.svws_nrw.service.klassen.KlassenService;
import de.svws_nrw.service.kurse.KursService;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für den {@link SchuelerListeService}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerListeServiceTest {

	private static final long ID_ABSCHNITT = 20L;

	@Mock
	private SchuelerListeEintragService schuelerListeEintragService;

	@Mock
	private KlassenService klassenService;

	@Mock
	private KursService kursService;

	@Mock
	private JahrgangService jahrgangService;

	@Mock
	private GostJahrgangslisteService gostJahrgangslisteService;

	@Mock
	private SchuljahresabschnittService schuljahresabschnittService;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	private SchuelerListeService service;

	private Schuljahresabschnitt abschnitt;

	@BeforeEach
	void setUp() {
		service = new SchuelerListeService(schuelerListeEintragService, klassenService, kursService, jahrgangService, gostJahrgangslisteService,
				schuljahresabschnittService, eigeneSchuleService);
		abschnitt = new Schuljahresabschnitt();
		abschnitt.id = ID_ABSCHNITT;
	}

	private static SchuelerListeEintrag createSchueler(final long id, final Long idKlasse) {
		final var eintrag = new SchuelerListeEintrag();
		eintrag.id = id;
		eintrag.idKlasse = idKlasse;
		return eintrag;
	}

	private static KlassenDaten createKlasse(final long id) {
		final var klasse = new KlassenDaten();
		klasse.id = id;
		return klasse;
	}

	private void prepareGrunddaten(final List<SchuelerListeEintrag> schueler, final List<KlassenDaten> klassen) {
		when(schuljahresabschnittService.getById(ID_ABSCHNITT)).thenReturn(abschnitt);
		when(schuelerListeEintragService.getListe(ID_ABSCHNITT, false)).thenReturn(schueler);
		when(klassenService.getListBySchuljahresabschnitt(ID_ABSCHNITT, false)).thenReturn(klassen);
		when(kursService.getListBySchuljahresabschnitt(ID_ABSCHNITT, true)).thenReturn(List.of(new KursDaten()));
		when(jahrgangService.getAll()).thenReturn(List.of(new JahrgangsDaten()));
	}

	@Test
	@DisplayName("getBySchuljahresabschnitt | Schule ohne gymnasiale Oberstufe liefert keine Gost-Jahrgänge")
	void getBySchuljahresabschnitt_ohneGymnasialeOberstufe() {
		final var schueler = List.of(createSchueler(1L, 5L));
		prepareGrunddaten(schueler, List.of(createKlasse(5L)));
		when(eigeneSchuleService.hatGymnasialeOberstufe()).thenReturn(false);

		final var result = service.getBySchuljahresabschnitt(ID_ABSCHNITT);

		assertThat(result.idSchuljahresabschnitt).isEqualTo(ID_ABSCHNITT);
		assertThat(result.schueler).isEqualTo(schueler);
		assertThat(result.klassen).hasSize(1);
		assertThat(result.kurse).hasSize(1);
		assertThat(result.jahrgaenge).hasSize(1);
		assertThat(result.jahrgaengeGost).isEmpty();
		verifyNoInteractions(gostJahrgangslisteService);
	}

	@Test
	@DisplayName("getBySchuljahresabschnitt | Schule mit gymnasialer Oberstufe liefert die Gost-Jahrgänge")
	void getBySchuljahresabschnitt_mitGymnasialerOberstufe() {
		prepareGrunddaten(List.of(), List.of());
		when(eigeneSchuleService.hatGymnasialeOberstufe()).thenReturn(true);
		when(gostJahrgangslisteService.getList(abschnitt)).thenReturn(List.of(new GostJahrgang(), new GostJahrgang()));

		final var result = service.getBySchuljahresabschnitt(ID_ABSCHNITT);

		assertThat(result.jahrgaengeGost).hasSize(2);
	}

	@Test
	@DisplayName("getBySchuljahresabschnitt | Ungültiger Abschnitt wirft 404 und lädt keine weiteren Daten")
	void getBySchuljahresabschnitt_ungueltigerAbschnitt() {
		when(schuljahresabschnittService.getById(ID_ABSCHNITT))
				.thenThrow(new ApiOperationException(Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 20 wurde nicht gefunden."));

		assertThatThrownBy(() -> service.getBySchuljahresabschnitt(ID_ABSCHNITT))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);

		verifyNoInteractions(schuelerListeEintragService, klassenService, kursService, jahrgangService, gostJahrgangslisteService);
	}

	@Test
	@DisplayName("getBySchuljahresabschnitt | Fehlende Klassen werden ohne Schüler nachgeladen und nicht doppelt aufgenommen")
	void getBySchuljahresabschnitt_fehlendeKlassenWerdenNachgeladen() {
		final var schueler = List.of(createSchueler(1L, 5L), createSchueler(2L, 7L), createSchueler(3L, 7L), createSchueler(4L, 8L));
		prepareGrunddaten(schueler, List.of(createKlasse(5L)));
		when(eigeneSchuleService.hatGymnasialeOberstufe()).thenReturn(false);
		when(klassenService.getListByIds(List.of(7L, 8L), false)).thenReturn(List.of(createKlasse(7L), createKlasse(8L)));

		final var result = service.getBySchuljahresabschnitt(ID_ABSCHNITT);

		assertThat(result.klassen).extracting(k -> k.id).containsExactly(5L, 7L, 8L);
		verify(klassenService).getListByIds(List.of(7L, 8L), false);
	}

	@Test
	@DisplayName("getBySchuljahresabschnitt | Schüler ohne Klassenzuordnung lösen kein Nachladen aus")
	void getBySchuljahresabschnitt_schuelerOhneKlasse() {
		final var schueler = List.of(createSchueler(1L, -1L), createSchueler(2L, 5L));
		prepareGrunddaten(schueler, List.of(createKlasse(5L)));
		when(eigeneSchuleService.hatGymnasialeOberstufe()).thenReturn(false);

		final var result = service.getBySchuljahresabschnitt(ID_ABSCHNITT);

		assertThat(result.klassen).hasSize(1);
		verify(klassenService, never()).getListByIds(anyList(), anyBoolean());
	}

}
