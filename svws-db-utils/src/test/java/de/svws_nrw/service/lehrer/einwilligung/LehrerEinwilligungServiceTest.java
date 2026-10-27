package de.svws_nrw.service.lehrer.einwilligung;

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

import de.svws_nrw.core.data.lehrer.LehrerEinwilligung;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutzPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.einwilligung.LehrerEinwilligungMapper;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.einwilligung.LehrerEinwilligungRepository;
import de.svws_nrw.repo.schule.kataloge.einwilligungsart.EinwilligungsartRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für den {@link LehrerEinwilligungService}.
 */
@ExtendWith(MockitoExtension.class)
class LehrerEinwilligungServiceTest {

	@Mock
	private LehrerEinwilligungRepository repository;

	@Mock
	private LehrerRepository lehrerRepository;

	@Mock
	private EinwilligungsartRepository einwilligungsartRepository;

	@Mock
	private LehrerEinwilligungMapper mapper;

	@Captor
	private ArgumentCaptor<List<DTOLehrerDatenschutz>> captor;

	private LehrerEinwilligungService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new LehrerEinwilligungService(repository, lehrerRepository, einwilligungsartRepository, mapper);
		transactionSupportMock = mockStatic(TransactionSupport.class, withSettings().lenient());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private static DTOLehrerDatenschutz dto(final long idLehrer, final long idEinwilligungsart) {
		return new DTOLehrerDatenschutz(idLehrer, idEinwilligungsart, false, false);
	}

	// -------------------------------------------------------------------------
	// getListByIdLehrer
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getListByIdLehrer | Mappt die Einwilligungen des Lehrers")
	void getListByIdLehrer_maps() {
		final var entity1 = dto(1L, 10L);
		final var entity2 = dto(1L, 11L);
		final var api1 = new LehrerEinwilligung();
		final var api2 = new LehrerEinwilligung();
		when(repository.getListByIdLehrer(1L)).thenReturn(List.of(entity1, entity2));
		when(mapper.toApi(entity1)).thenReturn(api1);
		when(mapper.toApi(entity2)).thenReturn(api2);

		assertThat(service.getListByIdLehrer(1L)).containsExactly(api1, api2);
	}

	@Test
	@DisplayName("getListByIdLehrer | Leere Liste, wenn keine Einwilligungen vorhanden sind")
	void getListByIdLehrer_empty() {
		when(repository.getListByIdLehrer(99L)).thenReturn(List.of());

		assertThat(service.getListByIdLehrer(99L)).isEmpty();
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Wendet den Patch auf die Einwilligung an und liefert sie zurück")
	void patch_success() {
		final var entity = dto(1L, 10L);
		final var api = new LehrerEinwilligung();
		final var request = new LehrerEinwilligungPatchRequest();
		request.istZugestimmt = JsonNullable.of(true);
		when(repository.findById(new DTOLehrerDatenschutzPK(1L, 10L))).thenReturn(Optional.of(entity));
		when(mapper.toApi(entity)).thenReturn(api);

		assertThat(service.patch(1L, 10L, request)).isSameAs(api);
		verify(mapper).patch(request, entity);
	}

	@Test
	@DisplayName("patch | Wirft NOT_FOUND, wenn keine Einwilligung existiert")
	void patch_notFound() {
		final var request = new LehrerEinwilligungPatchRequest();
		when(repository.findById(new DTOLehrerDatenschutzPK(1L, 10L))).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.patch(1L, 10L, request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("LehrerID 1")
				.hasMessageContaining("EinwilligungsartID 10");
		verify(mapper, never()).patch(any(), any());
	}

	// -------------------------------------------------------------------------
	// addForLehrer
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("addForLehrer | Legt nur für die noch fehlenden Einwilligungsarten eine Einwilligung an")
	void addForLehrer_createsOnlyMissing() {
		final var vorhanden = dto(1L, 10L);
		final var neu = dto(1L, 11L);
		final var api = new LehrerEinwilligung();
		when(lehrerRepository.existsById(1L)).thenReturn(true);
		when(repository.getListByIdLehrer(1L)).thenReturn(List.of(vorhanden));
		when(einwilligungsartRepository.getAllIdsByPersonTyp(PersonTyp.LEHRER)).thenReturn(List.of(10L, 11L));
		when(mapper.toDomain(1L, 11L)).thenReturn(neu);
		when(mapper.toApi(neu)).thenReturn(api);

		assertThat(service.addForLehrer(1L)).containsExactly(api);

		verify(repository).create(captor.capture());
		assertThat(captor.getValue()).containsExactly(neu);
		verify(mapper, never()).toDomain(1L, 10L);
	}

	@Test
	@DisplayName("addForLehrer | Legt nichts an, wenn der Lehrer bereits zu allen Einwilligungsarten eine Einwilligung hat")
	void addForLehrer_nothingToCreate() {
		when(lehrerRepository.existsById(1L)).thenReturn(true);
		when(repository.getListByIdLehrer(1L)).thenReturn(List.of(dto(1L, 10L)));
		when(einwilligungsartRepository.getAllIdsByPersonTyp(PersonTyp.LEHRER)).thenReturn(List.of(10L));

		assertThat(service.addForLehrer(1L)).isEmpty();

		verify(repository, never()).create(ArgumentMatchers.<List<DTOLehrerDatenschutz>>any());
	}

	@Test
	@DisplayName("addForLehrer | Wirft NOT_FOUND, wenn der Lehrer nicht existiert")
	void addForLehrer_lehrerNotFound() {
		when(lehrerRepository.existsById(1L)).thenReturn(false);

		assertThatThrownBy(() -> service.addForLehrer(1L))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("ID 1");
		verify(repository, never()).create(ArgumentMatchers.<List<DTOLehrerDatenschutz>>any());
	}

	// -------------------------------------------------------------------------
	// addForEinwilligungsart
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("addForEinwilligungsart | Legt nur für die noch fehlenden Lehrer eine Einwilligung an")
	void addForEinwilligungsart_createsOnlyMissing() {
		final var vorhanden = dto(1L, 10L);
		final var neu = dto(2L, 10L);
		final var api = new LehrerEinwilligung();
		when(einwilligungsartRepository.existsByIdAndPersonTyp(10L, PersonTyp.LEHRER)).thenReturn(true);
		when(repository.getListByIdEinwilligungsart(10L)).thenReturn(List.of(vorhanden));
		when(lehrerRepository.getAllIds()).thenReturn(List.of(1L, 2L));
		when(mapper.toDomain(2L, 10L)).thenReturn(neu);
		when(mapper.toApi(neu)).thenReturn(api);

		assertThat(service.addForEinwilligungsart(10L)).containsExactly(api);

		verify(repository).create(captor.capture());
		assertThat(captor.getValue()).containsExactly(neu);
	}

	@Test
	@DisplayName("addForEinwilligungsart | Wirft NOT_FOUND, wenn die Einwilligungsart nicht existiert oder nicht für Lehrer gilt")
	void addForEinwilligungsart_notFound() {
		when(einwilligungsartRepository.existsByIdAndPersonTyp(10L, PersonTyp.LEHRER)).thenReturn(false);

		assertThatThrownBy(() -> service.addForEinwilligungsart(10L))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("ID 10");
		verify(repository, never()).create(ArgumentMatchers.<List<DTOLehrerDatenschutz>>any());
	}

}
