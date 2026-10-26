package de.svws_nrw.service.schueler.einwilligung;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.core.data.schueler.SchuelerEinwilligung;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutzPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.einwilligung.SchuelerEinwilligungMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.einwilligung.SchuelerEinwilligungRepository;
import de.svws_nrw.repo.schule.kataloge.einwilligungsart.EinwilligungsartRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für den {@link SchuelerEinwilligungService}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerEinwilligungServiceTest {

	@Mock
	private SchuelerEinwilligungRepository repository;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private EinwilligungsartRepository einwilligungsartRepository;

	@Mock
	private SchuelerEinwilligungMapper mapper;

	@Captor
	private ArgumentCaptor<List<DTOSchuelerDatenschutz>> captor;

	private SchuelerEinwilligungService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new SchuelerEinwilligungService(repository, schuelerRepository, einwilligungsartRepository, mapper);
		transactionSupportMock = mockStatic(TransactionSupport.class, withSettings().lenient());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private static DTOSchuelerDatenschutz dto(final long idSchueler, final long idEinwilligungsart) {
		return new DTOSchuelerDatenschutz(idSchueler, idEinwilligungsart, false, false);
	}

	// -------------------------------------------------------------------------
	// getListByIdSchueler
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getListByIdSchueler | Mappt die Einwilligungen des Schülers")
	void getListByIdSchueler_maps() {
		final var entity1 = dto(1L, 10L);
		final var entity2 = dto(1L, 11L);
		final var api1 = new SchuelerEinwilligung();
		final var api2 = new SchuelerEinwilligung();
		when(repository.getListByIdSchueler(1L)).thenReturn(List.of(entity1, entity2));
		when(mapper.toApi(entity1)).thenReturn(api1);
		when(mapper.toApi(entity2)).thenReturn(api2);

		assertThat(service.getListByIdSchueler(1L)).containsExactly(api1, api2);
	}

	@Test
	@DisplayName("getListByIdSchueler | Leere Liste, wenn keine Einwilligungen vorhanden sind")
	void getListByIdSchueler_empty() {
		when(repository.getListByIdSchueler(99L)).thenReturn(List.of());

		assertThat(service.getListByIdSchueler(99L)).isEmpty();
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Wendet den Patch auf die Einwilligung an und liefert sie zurück")
	void patch_success() {
		final var entity = dto(1L, 10L);
		final var api = new SchuelerEinwilligung();
		final var request = new SchuelerEinwilligungPatchRequest();
		request.status = JsonNullable.of(true);
		when(repository.findById(new DTOSchuelerDatenschutzPK(1L, 10L))).thenReturn(Optional.of(entity));
		when(mapper.toApi(entity)).thenReturn(api);

		assertThat(service.patch(1L, 10L, request)).isSameAs(api);
		verify(mapper).patch(request, entity);
	}

	@Test
	@DisplayName("patch | Wirft NOT_FOUND, wenn keine Einwilligung existiert")
	void patch_notFound() {
		final var request = new SchuelerEinwilligungPatchRequest();
		when(repository.findById(new DTOSchuelerDatenschutzPK(1L, 10L))).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.patch(1L, 10L, request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("SchuelerID 1")
				.hasMessageContaining("EinwilligungsartID 10");
		verify(mapper, never()).patch(any(), any());
	}

	// -------------------------------------------------------------------------
	// addForSchueler
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("addForSchueler | Legt nur für die noch fehlenden Einwilligungsarten eine Einwilligung an")
	void addForSchueler_createsOnlyMissing() {
		final var vorhanden = dto(1L, 10L);
		final var neu = dto(1L, 11L);
		final var api = new SchuelerEinwilligung();
		when(schuelerRepository.existsById(1L)).thenReturn(true);
		when(repository.getListByIdSchueler(1L)).thenReturn(List.of(vorhanden));
		when(einwilligungsartRepository.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).thenReturn(List.of(10L, 11L));
		when(mapper.toDomain(1L, 11L)).thenReturn(neu);
		when(mapper.toApi(neu)).thenReturn(api);

		assertThat(service.addForSchueler(1L)).containsExactly(api);

		verify(repository).create(captor.capture());
		assertThat(captor.getValue()).containsExactly(neu);
		verify(mapper, never()).toDomain(1L, 10L);
	}

	@Test
	@DisplayName("addForSchueler | Legt nichts an, wenn der Schüler bereits zu allen Einwilligungsarten eine Einwilligung hat")
	void addForSchueler_nothingToCreate() {
		when(schuelerRepository.existsById(1L)).thenReturn(true);
		when(repository.getListByIdSchueler(1L)).thenReturn(List.of(dto(1L, 10L)));
		when(einwilligungsartRepository.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).thenReturn(List.of(10L));

		assertThat(service.addForSchueler(1L)).isEmpty();

		verify(repository, never()).create(ArgumentMatchers.<List<DTOSchuelerDatenschutz>>any());
	}

	@Test
	@DisplayName("addForSchueler | Wirft NOT_FOUND, wenn der Schüler nicht existiert")
	void addForSchueler_schuelerNotFound() {
		when(schuelerRepository.existsById(1L)).thenReturn(false);

		assertThatThrownBy(() -> service.addForSchueler(1L))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("ID 1");
		verify(repository, never()).create(ArgumentMatchers.<List<DTOSchuelerDatenschutz>>any());
	}

	// -------------------------------------------------------------------------
	// addForEinwilligungsart
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("addForEinwilligungsart | Legt nur für die noch fehlenden Schüler eine Einwilligung an")
	void addForEinwilligungsart_createsOnlyMissing() {
		final var vorhanden = dto(1L, 10L);
		final var neu = dto(2L, 10L);
		final var api = new SchuelerEinwilligung();
		when(einwilligungsartRepository.existsByIdAndPersonTyp(10L, PersonTyp.SCHUELER)).thenReturn(true);
		when(repository.getListByIdEinwilligungsart(10L)).thenReturn(List.of(vorhanden));
		when(schuelerRepository.getAllIds()).thenReturn(List.of(1L, 2L));
		when(mapper.toDomain(2L, 10L)).thenReturn(neu);
		when(mapper.toApi(neu)).thenReturn(api);

		assertThat(service.addForEinwilligungsart(10L)).containsExactly(api);

		verify(repository).create(captor.capture());
		assertThat(captor.getValue()).containsExactly(neu);
	}

	@Test
	@DisplayName("addForEinwilligungsart | Wirft NOT_FOUND, wenn die Einwilligungsart nicht existiert oder nicht für Schüler gilt")
	void addForEinwilligungsart_notFound() {
		when(einwilligungsartRepository.existsByIdAndPersonTyp(10L, PersonTyp.SCHUELER)).thenReturn(false);

		assertThatThrownBy(() -> service.addForEinwilligungsart(10L))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("ID 10");
		verify(repository, never()).create(ArgumentMatchers.<List<DTOSchuelerDatenschutz>>any());
	}

}
