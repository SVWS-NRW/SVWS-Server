package de.svws_nrw.data.schueler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import jakarta.ws.rs.core.Response;

/**
 * Tests für {@link DBUtilsSchuelerLernabschnittsdaten}.
 */
@DisplayName("DBUtilsSchuelerLernabschnittsdaten")
class DBUtilsSchuelerLernabschnittsdatenTest {

	private static final long ID_SCHUELER = 10L;
	private static final long ID_ABSCHNITT_PREVIOUS = 20L;
	private static final long ID_ABSCHNITT = 21L;
	private static final long ID_KLASSE_PREVIOUS = 30L;
	private static final long ID_JAHRGANG = 40L;
	private static final String KUERZEL = "05a";
	private static final String ASD_KUERZEL = "05A";

	private DBEntityManager conn;
	private KlassenRepository klassenRepository;
	private DTOSchuelerLernabschnittsdaten slaPrevious;
	private Schuljahresabschnitt abschnitt;

	@BeforeEach
	void setUp() {
		conn = mock(DBEntityManager.class);
		klassenRepository = mock(KlassenRepository.class);
		slaPrevious = new DTOSchuelerLernabschnittsdaten(1L, ID_SCHUELER, ID_ABSCHNITT_PREVIOUS, false, false, 1L);
		slaPrevious.WechselNr = 0;
		slaPrevious.Klassen_ID = ID_KLASSE_PREVIOUS;
		abschnitt = createAbschnitt(2);
		when(conn.queryList(anyString(), any(), any(Object[].class))).thenAnswer(inv -> {
			final Class<?> cl = inv.getArgument(1);
			if (cl == DTOSchuelerLernabschnittsdaten.class) {
				final String sql = inv.getArgument(0);
				return sql.contains("<>") ? List.of() : List.of(slaPrevious);
			}
			return List.of();
		});
		when(conn.transactionPersist(any())).thenReturn(true);
	}

	private static Schuljahresabschnitt createAbschnitt(final int nummer) {
		final var result = new Schuljahresabschnitt();
		result.id = ID_ABSCHNITT;
		result.schuljahr = 2026;
		result.abschnitt = nummer;
		result.idVorigerAbschnitt = ID_ABSCHNITT_PREVIOUS;
		return result;
	}

	private static DTOKlassen createKlasse(final long id, final long idAbschnitt, final String kuerzel, final String asdKuerzel) {
		final var klasse = new DTOKlassen(id, idAbschnitt, kuerzel, false, false);
		klasse.ASDKlasse = asdKuerzel;
		klasse.FKlasse = kuerzel;
		klasse.Jahrgang_ID = ID_JAHRGANG;
		return klasse;
	}

	private DTOSchuelerLernabschnittsdaten createByPrevious() {
		final var klassenFactory = mock(KlassenRepositoryFactory.class);
		when(klassenFactory.getKlassenRepository()).thenReturn(klassenRepository);
		final var katalogFactory = mock(KatalogRepositoryFactory.class, RETURNS_DEEP_STUBS);
		when(katalogFactory.getJahrgangRepository().getById(ID_JAHRGANG)).thenReturn(new DTOJahrgang(ID_JAHRGANG));
		try (var klassenStatic = mockStatic(KlassenRepositoryFactory.class); var katalogStatic = mockStatic(KatalogRepositoryFactory.class)) {
			klassenStatic.when(KlassenRepositoryFactory::getNewInstance).thenReturn(klassenFactory);
			katalogStatic.when(KatalogRepositoryFactory::getNewInstance).thenReturn(katalogFactory);
			return DBUtilsSchuelerLernabschnittsdaten.createByPrevious(2L, conn, ID_SCHUELER, abschnitt);
		}
	}

	private static void assertApiError(final Runnable aufruf, final Response.Status status, final String meldung) {
		assertThatThrownBy(aufruf::run).isInstanceOfSatisfying(ApiOperationException.class, e -> {
			assertThat(e.getStatus()).isEqualTo(status);
			assertThat(e.getBody()).isEqualTo(meldung);
		});
	}

	@ParameterizedTest
	@ValueSource(longs = { 1L, 2L, 3L })
	@DisplayName("createByPrevious übernimmt idBetreuung aus dem vorigen Lernabschnitt")
	void createByPrevious_uebernimmtIdBetreuung(final long idBetreuung) {
		slaPrevious.idBetreuung = idBetreuung;
		final var klasseVorig = createKlasse(ID_KLASSE_PREVIOUS, ID_ABSCHNITT_PREVIOUS, KUERZEL, null);
		when(klassenRepository.findById(ID_KLASSE_PREVIOUS)).thenReturn(Optional.of(klasseVorig));
		when(klassenRepository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt(KUERZEL, null, ID_ABSCHNITT))
				.thenReturn(Optional.of(createKlasse(31L, ID_ABSCHNITT, KUERZEL, null)));

		final var result = createByPrevious();

		assertThat(result.idBetreuung).isEqualTo(idBetreuung);
		assertThat(result.Schuljahresabschnitts_ID).isEqualTo(ID_ABSCHNITT);
	}

	@Test
	@DisplayName("createByPrevious: Die Klasse im neuen Abschnitt wird mit Kürzel und ASD-Kürzel gesucht")
	void createByPrevious_sucheMitAsdKuerzel() {
		when(klassenRepository.findById(ID_KLASSE_PREVIOUS)).thenReturn(Optional.of(createKlasse(ID_KLASSE_PREVIOUS, ID_ABSCHNITT_PREVIOUS, KUERZEL, ASD_KUERZEL)));
		when(klassenRepository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt(KUERZEL, ASD_KUERZEL, ID_ABSCHNITT))
				.thenReturn(Optional.of(createKlasse(31L, ID_ABSCHNITT, "05b", ASD_KUERZEL)));

		final var result = createByPrevious();

		assertThat(result.Klassen_ID).isEqualTo(31L);
		verify(klassenRepository).findByKuerzelOrASDKuerzelAndSchuljahresabschnitt(KUERZEL, ASD_KUERZEL, ID_ABSCHNITT);
	}

	@Test
	@DisplayName("createByPrevious: Zum neuen Schuljahr wird die Klasse über das Fachklassen-Kürzel im vorigen Abschnitt bestimmt")
	void createByPrevious_neuesSchuljahrOhneFolgeklasse() {
		abschnitt = createAbschnitt(1);
		when(klassenRepository.findById(ID_KLASSE_PREVIOUS)).thenReturn(Optional.of(createKlasse(ID_KLASSE_PREVIOUS, ID_ABSCHNITT_PREVIOUS, KUERZEL, null)));
		final var klasseGleichesKuerzel = createKlasse(32L, ID_ABSCHNITT_PREVIOUS, "05x", null);
		when(klassenRepository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt(KUERZEL, null, ID_ABSCHNITT_PREVIOUS))
				.thenReturn(Optional.of(klasseGleichesKuerzel));
		when(klassenRepository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt("05x", null, ID_ABSCHNITT))
				.thenReturn(Optional.of(createKlasse(33L, ID_ABSCHNITT, "05x", null)));

		assertThat(createByPrevious().Klassen_ID).isEqualTo(33L);
	}

	@Test
	@DisplayName("createByPrevious: Zum neuen Schuljahr wird die Folgeklasse genutzt, wenn sie gesetzt ist")
	void createByPrevious_neuesSchuljahrMitFolgeklasse() {
		abschnitt = createAbschnitt(1);
		slaPrevious.Folgeklasse_ID = 34L;
		when(klassenRepository.findById(34L)).thenReturn(Optional.of(createKlasse(34L, ID_ABSCHNITT_PREVIOUS, "06a", null)));
		when(klassenRepository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt("06a", null, ID_ABSCHNITT))
				.thenReturn(Optional.of(createKlasse(35L, ID_ABSCHNITT, "06a", null)));

		assertThat(createByPrevious().Klassen_ID).isEqualTo(35L);
	}

	@Test
	@DisplayName("createByPrevious: Die Klasse des vorigen Lernabschnitts existiert nicht, 404")
	void createByPrevious_klasseNichtGefunden() {
		when(klassenRepository.findById(ID_KLASSE_PREVIOUS)).thenReturn(Optional.empty());

		assertApiError(this::createByPrevious, Response.Status.NOT_FOUND, "Keine Klasse zur ID 30 gefunden.");
	}

	@Test
	@DisplayName("createByPrevious: Im neuen Abschnitt gibt es keine Klasse mit dem Kürzel, 404")
	void createByPrevious_kuerzelNichtGefunden() {
		when(klassenRepository.findById(ID_KLASSE_PREVIOUS)).thenReturn(Optional.of(createKlasse(ID_KLASSE_PREVIOUS, ID_ABSCHNITT_PREVIOUS, KUERZEL, null)));
		when(klassenRepository.findByKuerzelOrASDKuerzelAndSchuljahresabschnitt(KUERZEL, null, ID_ABSCHNITT)).thenReturn(Optional.empty());

		assertApiError(this::createByPrevious, Response.Status.NOT_FOUND,
				"Es wurde keine Klasse mit dem Kürzel 05a und dem Halbjahresabschnitt mit der ID 21 gefunden.");
	}

	@Test
	@DisplayName("createByPrevious: Ohne Fachklassen-Kürzel zum neuen Schuljahr, 400")
	void createByPrevious_keinKuerzel() {
		abschnitt = createAbschnitt(1);
		final var klasseOhneFKlasse = createKlasse(ID_KLASSE_PREVIOUS, ID_ABSCHNITT_PREVIOUS, KUERZEL, null);
		klasseOhneFKlasse.FKlasse = null;
		when(klassenRepository.findById(ID_KLASSE_PREVIOUS)).thenReturn(Optional.of(klasseOhneFKlasse));

		assertApiError(this::createByPrevious, Response.Status.BAD_REQUEST,
				"Es muss mindestens ein Kürzel oder ASD-Kürzel angegeben sein. Das Beide Kürzel dürfen nicht null sein.");
	}

	@Test
	@DisplayName("createByPrevious: Ohne Klassen-ID im vorigen Lernabschnitt, 400")
	void createByPrevious_keineKlassenId() {
		slaPrevious.Klassen_ID = null;

		assertApiError(this::createByPrevious, Response.Status.BAD_REQUEST, "Die ID für die Klasse darf nicht null sein.");
	}

}
