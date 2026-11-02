package de.svws_nrw.service.schule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.SchuljahresabschnittMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response;

/**
 * Testet den Service für den Zugriff auf und das Anlegen von Schuljahresabschnitten
 */
@ExtendWith(MockitoExtension.class)
class SchuljahresabschnittServiceTest {

	private static final long ID_NEU = 99L;

	@Mock
	private SchuljahresabschnitteRepository repoMock;

	@Mock
	private EigeneSchuleRepository eigeneSchuleRepoMock;

	private SchuljahresabschnittService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new SchuljahresabschnittService(repoMock, eigeneSchuleRepoMock, SchuljahresabschnittMapper.INSTANCE);
		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private void stubCreate() {
		when(repoMock.create(any(DTOSchuljahresabschnitte.class))).thenAnswer(invocation -> {
			final DTOSchuljahresabschnitte dto = invocation.getArgument(0);
			dto.ID = ID_NEU;
			return dto;
		});
	}

	private void stubAktiverAbschnitt(final DTOSchuljahresabschnitte aktiv) {
		final var schule = new DTOEigeneSchule(1L);
		schule.Schuljahresabschnitts_ID = aktiv.ID;
		when(eigeneSchuleRepoMock.findFirst()).thenReturn(Optional.of(schule));
		when(repoMock.findById(aktiv.ID)).thenReturn(Optional.of(aktiv));
	}

	private void assertApiError(final Runnable aufruf, final Response.Status status, final String meldung) {
		assertThatThrownBy(aufruf::run)
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(status);
					assertThat(e.getBody()).isEqualTo(meldung);
				});
	}


	// --- getById ---

	@Test
	@DisplayName("getById | liefert korrekt gemappte Daten")
	void getById_success() {
		final long id = 42L;
		final var dto = new DTOSchuljahresabschnitte(id, 2023, 1);
		dto.VorigerAbschnitt_ID = 123L;
		dto.FolgeAbschnitt_ID = 19L;
		when(repoMock.findById(id)).thenReturn(Optional.of(dto));

		final var result = service.getById(id);

		assertThat(result)
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(id, 2023, 1, 123L, 19L);
		verify(repoMock, times(1)).findById(id);
	}

	@Test
	@DisplayName("getById | unbekannte ID liefert NOT_FOUND mit deutscher Meldung")
	void getById_notFound() {
		when(repoMock.findById(42L)).thenReturn(Optional.empty());

		assertApiError(() -> service.getById(42L), Response.Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 42 wurde nicht gefunden.");
	}


	// --- findBySchuljahrAndAbschnitt ---

	@Test
	@DisplayName("findBySchuljahrAndAbschnitt | liefert korrekt gemappte Daten")
	void findBySchuljahrAndAbschnitt_success() {
		final var dto = new DTOSchuljahresabschnitte(7L, 2025, 2);
		dto.VorigerAbschnitt_ID = 6L;
		when(repoMock.findBySchuljahrAndAbschnitt(2025, 2)).thenReturn(Optional.of(dto));

		final var result = service.findBySchuljahrAndAbschnitt(2025, 2);

		assertThat(result).get()
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(7L, 2025, 2, 6L, null);
	}

	@Test
	@DisplayName("findBySchuljahrAndAbschnitt | unbekannte Kombination liefert ein leeres Optional")
	void findBySchuljahrAndAbschnitt_empty() {
		when(repoMock.findBySchuljahrAndAbschnitt(2025, 2)).thenReturn(Optional.empty());

		assertThat(service.findBySchuljahrAndAbschnitt(2025, 2)).isEmpty();
	}


	// --- getList ---

	@Test
	@DisplayName("getList | mappt die DTOs und sortiert nach Schuljahr und Abschnitt")
	void getList_sortedAndMapped() {
		final var dto1 = new DTOSchuljahresabschnitte(1L, 2026, 1);
		dto1.FolgeAbschnitt_ID = 2L;
		final var dto2 = new DTOSchuljahresabschnitte(2L, 2026, 2);
		dto2.VorigerAbschnitt_ID = 1L;
		dto2.FolgeAbschnitt_ID = 3L;
		final var dto3 = new DTOSchuljahresabschnitte(3L, 2027, 1);
		dto3.VorigerAbschnitt_ID = 2L;
		when(repoMock.getAll()).thenReturn(List.of(dto3, dto1, dto2));

		final var result = service.getList();

		assertThat(result).hasSize(3);
		assertThat(result).extracting("id").containsExactly(1L, 2L, 3L);
		assertThat(result.get(0)).extracting("schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(2026, 1, null, 2L);
		assertThat(result.get(2)).extracting("schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(2027, 1, 2L, null);
	}

	@Test
	@DisplayName("getList | leere Tabelle liefert eine leere Liste")
	void getList_empty() {
		when(repoMock.getAll()).thenReturn(List.of());

		assertThat(service.getList()).isEmpty();
	}


	// --- getListBySchuljahre ---

	@Test
	@DisplayName("getListBySchuljahre | fragt die Schuljahre ab, mappt die DTOs und sortiert nach Schuljahr und Abschnitt")
	void getListBySchuljahre_sortedAndMapped() {
		final var dto1 = new DTOSchuljahresabschnitte(1L, 2026, 1);
		final var dto2 = new DTOSchuljahresabschnitte(2L, 2026, 2);
		final var dto3 = new DTOSchuljahresabschnitte(3L, 2027, 1);
		when(repoMock.findListBySchuljahre(List.of(2027, 2026))).thenReturn(List.of(dto3, dto2, dto1));

		final var result = service.getListBySchuljahre(List.of(2027, 2026));

		assertThat(result).extracting("id").containsExactly(1L, 2L, 3L);
		assertThat(result).extracting("schuljahr", "abschnitt").containsExactly(tuple(2026, 1), tuple(2026, 2), tuple(2027, 1));
	}

	@Test
	@DisplayName("getListBySchuljahre | ohne Treffer wird eine leere Liste geliefert")
	void getListBySchuljahre_empty() {
		when(repoMock.findListBySchuljahre(List.of(2030))).thenReturn(List.of());

		assertThat(service.getListBySchuljahre(List.of(2030))).isEmpty();
	}


	// --- createVorErstem ---

	@Test
	@DisplayName("createVorErstem | legt den ersten Abschnitt im selben Schuljahr an und verknüpft den bisher ersten Abschnitt")
	void createVorErstem_imSelbenSchuljahr() {
		final var erster = new DTOSchuljahresabschnitte(10L, 2025, 2);
		when(repoMock.getAll()).thenReturn(List.of(erster));
		when(repoMock.findBySchuljahrAndAbschnitt(2025, 1)).thenReturn(Optional.empty());
		stubCreate();

		final var result = service.createVorErstem();

		assertThat(result).extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(ID_NEU, 2025, 1, null, 10L);
		assertThat(erster.VorigerAbschnitt_ID).isEqualTo(ID_NEU);
		verify(repoMock).update(erster);
	}

	@Test
	@DisplayName("createVorErstem | wechselt beim Anlegen vor Abschnitt 1 in den letzten Abschnitt des Vorjahres")
	void createVorErstem_jahreswechsel() {
		final var erster = new DTOSchuljahresabschnitte(10L, 2025, 1);
		when(repoMock.getAll()).thenReturn(List.of(erster));
		when(repoMock.findBySchuljahrAndAbschnitt(2024, 2)).thenReturn(Optional.empty());
		stubCreate();

		final var result = service.createVorErstem();

		assertThat(result).extracting("schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(2024, 2, null, 10L);
		assertThat(erster.VorigerAbschnitt_ID).isEqualTo(ID_NEU);
	}

	@Test
	@DisplayName("createVorErstem | bestimmt den ersten Abschnitt unabhängig von der Reihenfolge der Datenbank")
	void createVorErstem_waehltErstenAus() {
		final var neu = new DTOSchuljahresabschnitte(12L, 2026, 2);
		final var alt = new DTOSchuljahresabschnitte(10L, 2025, 2);
		final var mittel = new DTOSchuljahresabschnitte(11L, 2026, 1);
		when(repoMock.getAll()).thenReturn(List.of(neu, alt, mittel));
		when(repoMock.findBySchuljahrAndAbschnitt(2025, 1)).thenReturn(Optional.empty());
		stubCreate();

		final var result = service.createVorErstem();

		assertThat(result.idFolgeAbschnitt).isEqualTo(10L);
		assertThat(alt.VorigerAbschnitt_ID).isEqualTo(ID_NEU);
		assertThat(mittel.VorigerAbschnitt_ID).isNull();
		assertThat(neu.VorigerAbschnitt_ID).isNull();
	}

	@Test
	@DisplayName("createVorErstem | ohne vorhandenen Abschnitt wird abgelehnt")
	void createVorErstem_keineAbschnitte() {
		when(repoMock.getAll()).thenReturn(List.of());

		assertApiError(service::createVorErstem, Response.Status.BAD_REQUEST,
				"Es existiert noch kein Schuljahresabschnitt, vor oder nach dem ein neuer Schuljahresabschnitt angelegt werden könnte.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
	}

	@Test
	@DisplayName("createVorErstem | ungültige Halbjahresabschnittsnummer des ersten Schuljahresabschnitts wird abgelehnt")
	void createVorErstem_ungueltigeHalbjahresabschnittsnummer() {
		when(repoMock.getAll()).thenReturn(List.of(new DTOSchuljahresabschnitte(10L, 2025, 3)));

		assertApiError(service::createVorErstem, Response.Status.BAD_REQUEST,
				"Der Schuljahresabschnitt mit der ID 10 hat die Halbjahresabschnittsnummer 3. Erlaubt sind nur die Nummern 1 bis 2.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
	}

	@Test
	@DisplayName("createVorErstem | bereits vorhandener Abschnitt wird abgelehnt")
	void createVorErstem_existiertBereits() {
		final var erster = new DTOSchuljahresabschnitte(10L, 2025, 2);
		when(repoMock.getAll()).thenReturn(List.of(erster));
		when(repoMock.findBySchuljahrAndAbschnitt(2025, 1)).thenReturn(Optional.of(new DTOSchuljahresabschnitte(5L, 2025, 1)));

		assertApiError(service::createVorErstem, Response.Status.BAD_REQUEST,
				"Der Schuljahresabschnitt für das Schuljahr 2025 und den Halbjahresabschnitt 1 existiert bereits.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
		verify(repoMock, never()).update(any(DTOSchuljahresabschnitte.class));
	}


	// --- createNachLetztem ---

	@Test
	@DisplayName("createNachLetztem | legt den letzten Abschnitt im selben Schuljahr an und verknüpft den bisher letzten Schuljahresabschnitt")
	void createNachLetztem_imSelbenSchuljahr() {
		final var aktiv = new DTOSchuljahresabschnitte(10L, 2025, 1);
		when(repoMock.getAll()).thenReturn(List.of(aktiv));
		stubAktiverAbschnitt(aktiv);
		when(repoMock.findBySchuljahrAndAbschnitt(2025, 2)).thenReturn(Optional.empty());
		stubCreate();

		final var result = service.createNachLetztem();

		assertThat(result).extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(ID_NEU, 2025, 2, 10L, null);
		assertThat(aktiv.FolgeAbschnitt_ID).isEqualTo(ID_NEU);
		verify(repoMock).update(aktiv);
	}

	@Test
	@DisplayName("createNachLetztem | wechselt beim Anlegen nach dem letzten Abschnitt eines Schuljahres in das Folgejahr")
	void createNachLetztem_jahreswechsel() {
		final var aktiv = new DTOSchuljahresabschnitte(10L, 2025, 2);
		when(repoMock.getAll()).thenReturn(List.of(aktiv));
		stubAktiverAbschnitt(aktiv);
		when(repoMock.findBySchuljahrAndAbschnitt(2026, 1)).thenReturn(Optional.empty());
		stubCreate();

		final var result = service.createNachLetztem();

		assertThat(result).extracting("schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(2026, 1, 10L, null);
		assertThat(aktiv.FolgeAbschnitt_ID).isEqualTo(ID_NEU);
	}

	@Test
	@DisplayName("createNachLetztem | erlaubt den dritten Abschnitt hinter dem aktiven Abschnitt")
	void createNachLetztem_dritterAbschnittNachAktivem() {
		final var aktiv = new DTOSchuljahresabschnitte(10L, 2025, 1);
		final var a2 = new DTOSchuljahresabschnitte(11L, 2025, 2);
		final var a3 = new DTOSchuljahresabschnitte(12L, 2026, 1);
		when(repoMock.getAll()).thenReturn(List.of(aktiv, a2, a3));
		stubAktiverAbschnitt(aktiv);
		when(repoMock.findBySchuljahrAndAbschnitt(2026, 2)).thenReturn(Optional.empty());
		stubCreate();

		final var result = service.createNachLetztem();

		assertThat(result).extracting("schuljahr", "abschnitt", "idVorigerAbschnitt").containsExactly(2026, 2, 12L);
		assertThat(a3.FolgeAbschnitt_ID).isEqualTo(ID_NEU);
	}

	@Test
	@DisplayName("createNachLetztem | lehnt den Abschnitt ab, wenn bereits drei Abschnitte hinter dem aktiven Abschnitt existieren")
	void createNachLetztem_zukunftsgrenzeErreicht() {
		final var aktiv = new DTOSchuljahresabschnitte(10L, 2025, 1);
		final var a2 = new DTOSchuljahresabschnitte(11L, 2025, 2);
		final var a3 = new DTOSchuljahresabschnitte(12L, 2026, 1);
		final var a4 = new DTOSchuljahresabschnitte(13L, 2026, 2);
		when(repoMock.getAll()).thenReturn(List.of(aktiv, a2, a3, a4));
		stubAktiverAbschnitt(aktiv);

		assertApiError(service::createNachLetztem, Response.Status.BAD_REQUEST,
				"Es dürfen höchstens 3 Schuljahresabschnitte nach dem aktiven Schuljahresabschnitt angelegt werden.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
		verify(repoMock, never()).update(any(DTOSchuljahresabschnitte.class));
	}

	@Test
	@DisplayName("createNachLetztem | zählt die Zukunftsgrenze über den Jahreswechsel hinweg")
	void createNachLetztem_zukunftsgrenzeUeberJahreswechsel() {
		final var aktiv = new DTOSchuljahresabschnitte(10L, 2025, 2);
		final var letzter = new DTOSchuljahresabschnitte(13L, 2027, 1);
		when(repoMock.getAll()).thenReturn(List.of(aktiv, letzter));
		stubAktiverAbschnitt(aktiv);

		assertApiError(service::createNachLetztem, Response.Status.BAD_REQUEST,
				"Es dürfen höchstens 3 Schuljahresabschnitte nach dem aktiven Schuljahresabschnitt angelegt werden.");
	}

	@Test
	@DisplayName("createNachLetztem | ohne vorhandenen Abschnitt wird abgelehnt")
	void createNachLetztem_keineAbschnitte() {
		when(repoMock.getAll()).thenReturn(List.of());

		assertApiError(service::createNachLetztem, Response.Status.BAD_REQUEST,
				"Es existiert noch kein Schuljahresabschnitt, vor oder nach dem ein neuer Schuljahresabschnitt angelegt werden könnte.");
	}

	@Test
	@DisplayName("createNachLetztem | ohne Eintrag für die eigene Schule wird abgelehnt")
	void createNachLetztem_keineSchule() {
		when(repoMock.getAll()).thenReturn(List.of(new DTOSchuljahresabschnitte(10L, 2025, 1)));
		when(eigeneSchuleRepoMock.findFirst()).thenReturn(Optional.empty());

		assertApiError(service::createNachLetztem, Response.Status.BAD_REQUEST, "Für die Schule ist kein aktiver Schuljahresabschnitt gesetzt.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
	}

	@Test
	@DisplayName("createNachLetztem | ohne gesetzten aktiven Abschnitt wird abgelehnt")
	void createNachLetztem_keinAktiverAbschnitt() {
		when(repoMock.getAll()).thenReturn(List.of(new DTOSchuljahresabschnitte(10L, 2025, 1)));
		when(eigeneSchuleRepoMock.findFirst()).thenReturn(Optional.of(new DTOEigeneSchule(1L)));

		assertApiError(service::createNachLetztem, Response.Status.BAD_REQUEST, "Für die Schule ist kein aktiver Schuljahresabschnitt gesetzt.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
	}

	@Test
	@DisplayName("createNachLetztem | nicht vorhandener aktiver Abschnitt wird abgelehnt")
	void createNachLetztem_aktiverAbschnittNichtGefunden() {
		when(repoMock.getAll()).thenReturn(List.of(new DTOSchuljahresabschnitte(10L, 2025, 1)));
		final var schule = new DTOEigeneSchule(1L);
		schule.Schuljahresabschnitts_ID = 77L;
		when(eigeneSchuleRepoMock.findFirst()).thenReturn(Optional.of(schule));
		when(repoMock.findById(77L)).thenReturn(Optional.empty());

		assertApiError(service::createNachLetztem, Response.Status.BAD_REQUEST, "Der aktive Schuljahresabschnitt mit der ID 77 wurde nicht gefunden.");
	}

	@Test
	@DisplayName("createNachLetztem | ungültige Halbjahresabschnittsnummer des letzten Schuljahresabschnitts wird abgelehnt")
	void createNachLetztem_ungueltigeHalbjahresabschnittsnummerLetzter() {
		final var aktiv = new DTOSchuljahresabschnitte(10L, 2025, 1);
		final var letzter = new DTOSchuljahresabschnitte(11L, 2025, 3);
		when(repoMock.getAll()).thenReturn(List.of(aktiv, letzter));
		stubAktiverAbschnitt(aktiv);

		assertApiError(service::createNachLetztem, Response.Status.BAD_REQUEST,
				"Der Schuljahresabschnitt mit der ID 11 hat die Halbjahresabschnittsnummer 3. Erlaubt sind nur die Nummern 1 bis 2.");
	}

	@Test
	@DisplayName("createNachLetztem | ungültige Halbjahresabschnittsnummer des aktiven Schuljahresabschnitts wird abgelehnt")
	void createNachLetztem_ungueltigeHalbjahresabschnittsnummerAktiver() {
		final var aktiv = new DTOSchuljahresabschnitte(10L, 2025, 0);
		final var letzter = new DTOSchuljahresabschnitte(11L, 2025, 2);
		when(repoMock.getAll()).thenReturn(List.of(aktiv, letzter));
		stubAktiverAbschnitt(aktiv);

		assertApiError(service::createNachLetztem, Response.Status.BAD_REQUEST,
				"Der Schuljahresabschnitt mit der ID 10 hat die Halbjahresabschnittsnummer 0. Erlaubt sind nur die Nummern 1 bis 2.");
	}

	@Test
	@DisplayName("createNachLetztem | bereits vorhandener Abschnitt wird abgelehnt")
	void createNachLetztem_existiertBereits() {
		final var aktiv = new DTOSchuljahresabschnitte(10L, 2025, 1);
		when(repoMock.getAll()).thenReturn(List.of(aktiv));
		stubAktiverAbschnitt(aktiv);
		when(repoMock.findBySchuljahrAndAbschnitt(2025, 2)).thenReturn(Optional.of(new DTOSchuljahresabschnitte(5L, 2025, 2)));

		assertApiError(service::createNachLetztem, Response.Status.BAD_REQUEST,
				"Der Schuljahresabschnitt für das Schuljahr 2025 und den Halbjahresabschnitt 2 existiert bereits.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
		verify(repoMock, never()).update(any(DTOSchuljahresabschnitte.class));
	}

}
