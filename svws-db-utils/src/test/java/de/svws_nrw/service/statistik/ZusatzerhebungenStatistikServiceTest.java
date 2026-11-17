package de.svws_nrw.service.statistik;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schild.schule.DTOStatistikZusatzerhebungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.statistik.ZusatzerhebungenStatistikMapper;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.ZusatzerhebungenStatistikRepository;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class ZusatzerhebungenStatistikServiceTest {

	private static final long ID_ABSCHNITT = 11L;

	@Mock
	private ZusatzerhebungenStatistikRepository repository;

	@Mock
	private SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	private final ZusatzerhebungenStatistikMapper mapper = ZusatzerhebungenStatistikMapper.INSTANCE;

	private ZusatzerhebungenStatistikService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	@BeforeAll
	static void setupCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
		service = new ZusatzerhebungenStatistikService(repository, schuljahresabschnitteRepository, mapper);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private void abschnittExistiert() {
		when(schuljahresabschnitteRepository.findById(ID_ABSCHNITT)).thenReturn(Optional.of(new DTOSchuljahresabschnitte(ID_ABSCHNITT, 2026, 1)));
	}

	// -------------------------------------------------------------------------
	// get
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("get")
	class Get {

		@Test
		@DisplayName("Liefert Standardwerte mit der ID des Abschnitts, wenn kein Datensatz existiert, ohne einen Datensatz anzulegen")
		void get_standardwerteOhneDatensatz() {
			when(repository.findById(ID_ABSCHNITT)).thenReturn(Optional.empty());

			final var result = service.get(ID_ABSCHNITT);

			final var expected = new ZusatzerhebungenStatistikGesamt();
			expected.idSchuljahresabschnitt = ID_ABSCHNITT;
			assertThat(result).usingRecursiveComparison().isEqualTo(expected);
			verify(repository, never()).create(any(DTOStatistikZusatzerhebungen.class));
			verify(repository, never()).update(any(DTOStatistikZusatzerhebungen.class));
		}

		@Test
		@DisplayName("Liefert die gespeicherten Daten, wenn ein Datensatz existiert")
		void get_vorhandenerDatensatz() {
			final var entity = new DTOStatistikZusatzerhebungen(ID_ABSCHNITT, true);
			entity.anzahlSchulanmeldungen = 113;
			when(repository.findById(ID_ABSCHNITT)).thenReturn(Optional.of(entity));

			final var result = service.get(ID_ABSCHNITT);

			assertThat(result.idSchuljahresabschnitt).isEqualTo(ID_ABSCHNITT);
			assertThat(result.hatInternationaleKontakte).isTrue();
			assertThat(result.anzahlSchulanmeldungen).isEqualTo(113);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Legt beim ersten Patch einen Datensatz für den Schuljahresabschnitt an")
		void patch_legtDatensatzAn() {
			abschnittExistiert();
			when(repository.findById(ID_ABSCHNITT)).thenReturn(Optional.empty());
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.anzahlSchulanmeldungen = JsonNullable.of(113);

			final var result = service.patch(ID_ABSCHNITT, request);

			final var captor = ArgumentCaptor.forClass(DTOStatistikZusatzerhebungen.class);
			verify(repository).create(captor.capture());
			assertThat(captor.getValue().idSchuljahresabschnitt).isEqualTo(ID_ABSCHNITT);
			assertThat(captor.getValue().hatInternationaleKontakte).isFalse();
			assertThat(captor.getValue().anzahlSchulanmeldungen).isEqualTo(113);
			assertThat(result.idSchuljahresabschnitt).isEqualTo(ID_ABSCHNITT);
			assertThat(result.anzahlSchulanmeldungen).isEqualTo(113);
		}

		@Test
		@DisplayName("Ändert bei einem vorhandenen Datensatz nur die übergebenen Felder")
		void patch_aendertVorhandenenDatensatz() {
			abschnittExistiert();
			final var entity = new DTOStatistikZusatzerhebungen(ID_ABSCHNITT, true);
			entity.anzahlSchulanmeldungen = 113;
			entity.anzahlZurueckstellungsverfahren = 17;
			when(repository.findById(ID_ABSCHNITT)).thenReturn(Optional.of(entity));
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.anzahlSchulanmeldungen = JsonNullable.of(120);

			final var result = service.patch(ID_ABSCHNITT, request);

			verify(repository).update(entity);
			verify(repository, never()).create(any(DTOStatistikZusatzerhebungen.class));
			assertThat(result.anzahlSchulanmeldungen).isEqualTo(120);
			assertThat(result.anzahlZurueckstellungsverfahren).isEqualTo(17);
			assertThat(result.hatInternationaleKontakte).isTrue();
		}

		@Test
		@DisplayName("Liefert 404, wenn der Schuljahresabschnitt nicht existiert")
		void patch_unbekannterAbschnitt() {
			when(schuljahresabschnitteRepository.findById(ID_ABSCHNITT)).thenReturn(Optional.empty());
			final var request = new ZusatzerhebungenStatistikPatchRequest();

			assertThatThrownBy(() -> service.patch(ID_ABSCHNITT, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
			verify(repository, never()).create(any(DTOStatistikZusatzerhebungen.class));
		}

		@Test
		@DisplayName("Akzeptiert eine Internatsart aus dem Katalog")
		void patch_gueltigeInternatArt() {
			abschnittExistiert();
			when(repository.findById(ID_ABSCHNITT)).thenReturn(Optional.empty());
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.idInternatArt = JsonNullable.of(2L);

			final var result = service.patch(ID_ABSCHNITT, request);

			assertThat(result.idInternatArt).isEqualTo(2L);
		}

		@Test
		@DisplayName("Akzeptiert das Entfernen der Internatsart")
		void patch_internatArtNull() {
			abschnittExistiert();
			final var entity = new DTOStatistikZusatzerhebungen(ID_ABSCHNITT, false);
			entity.idInternatArt = 2L;
			when(repository.findById(ID_ABSCHNITT)).thenReturn(Optional.of(entity));
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.idInternatArt = JsonNullable.of(null);

			final var result = service.patch(ID_ABSCHNITT, request);

			assertThat(result.idInternatArt).isNull();
		}

		@Test
		@DisplayName("Liefert 400 bei einer Internatsart, die nicht im Katalog existiert")
		void patch_ungueltigeInternatArt() {
			abschnittExistiert();
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.idInternatArt = JsonNullable.of(999L);

			assertThatThrownBy(() -> service.patch(ID_ABSCHNITT, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			verify(repository, never()).create(any(DTOStatistikZusatzerhebungen.class));
			verify(repository, never()).update(any(DTOStatistikZusatzerhebungen.class));
		}
	}

}
