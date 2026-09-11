package de.svws_nrw.service.uv.lerngruppen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppe;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppeRepository;
import de.svws_nrw.service.uv.kurse.UvKursService;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UvLerngruppeServiceTest {

	private UvLerngruppeRepository repository;
	private UvLerngruppeService service;
	private MockedStatic<TransactionSupport> transactions;

	@BeforeEach
	void setUp() {
		repository = mock(UvLerngruppeRepository.class);
		service = new UvLerngruppeService(repository, mock(UvKursService.class));
		transactions = mockStatic(TransactionSupport.class);
		transactions.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(inv -> inv.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactions.close();
	}

	@Test
	void createWithoutAssignmentReturnsBadRequest() {
		final UvLerngruppeCreateRequest request = new UvLerngruppeCreateRequest();
		request.idPlanungsabschnitt = 1L;
		final ApiOperationException error = assertThrows(ApiOperationException.class, () -> service.create(request));
		assertEquals(Status.BAD_REQUEST, error.getStatus());
		verify(repository, never()).flush();
	}

	private DTOUvLerngruppe existingKlassenunterricht() {
		final DTOUvLerngruppe dto = new DTOUvLerngruppe(10L, 1L, 2.0, 0.0, 0);
		dto.Klasse_ID = 20L;
		dto.Fach_ID = 30L;
		when(repository.findListByIds(List.of(10L))).thenReturn(List.of(dto));
		when(repository.getById(10L)).thenReturn(dto);
		return dto;
	}

	@Test
	void patchOnlyHoursPreservesAssignment() {
		existingKlassenunterricht();
		final UvLerngruppePatchRequest patch = new UvLerngruppePatchRequest();
		patch.id = 10L;
		patch.wochenstunden = JsonNullable.of(3.0);
		final var result = service.patch(patch);
		assertEquals(20L, result.idKlasse.longValue());
		assertEquals(30L, result.idFach.longValue());
		assertEquals(3.0, result.wochenstunden);
		verify(repository).flush();
	}

	@Test
	void patchRemovingRequiredFieldRejectsBeforeMutation() {
		final DTOUvLerngruppe dto = existingKlassenunterricht();
		final UvLerngruppePatchRequest patch = new UvLerngruppePatchRequest();
		patch.id = 10L;
		patch.idFach = JsonNullable.of(null);
		final ApiOperationException error = assertThrows(ApiOperationException.class, () -> service.patch(patch));
		assertEquals(Status.BAD_REQUEST, error.getStatus());
		assertEquals(30L, dto.Fach_ID.longValue());
		verify(repository, never()).flush();
	}

	@Test
	void patchCanSwitchToKursunterricht() {
		existingKlassenunterricht();
		final UvLerngruppePatchRequest patch = new UvLerngruppePatchRequest();
		patch.id = 10L;
		patch.idKlasse = JsonNullable.of(null);
		patch.idFach = JsonNullable.of(null);
		patch.idKurs = JsonNullable.of(40L);
		final var result = service.patch(patch);
		assertNull(result.idKlasse);
		assertNull(result.idFach);
		assertEquals(40L, result.idKurs.longValue());
		verify(repository).flush();
	}
}
