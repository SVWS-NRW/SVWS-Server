package de.svws_nrw.service.katalog.teilleistungsart;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.core.data.kataloge.Teilleistungsart;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schule.kataloge.teilleistungsart.TeilleistungsartRepository;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeilleistungsartServiceTest {


	@Mock
	private TeilleistungsartRepository teilleistungsartRepository;

	@InjectMocks
	private TeilleistungsartService teilleistungsartService;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void beforeEach() {
		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any())).thenAnswer(invocation -> {
			final java.util.function.Supplier<?> supplier = invocation.getArgument(0);
			return supplier.get();
		});
	}

	@AfterEach
	void afterEach() {
		transactionSupportMock.close();
	}

	@Test
	@DisplayName("create | Erfolg")
	void testCreateSuccess() {
		final var restInput = createRestRequest();
		final DTOTeilleistungsarten entity = createEntity(0, restInput.bezeichnung, restInput.istSichtbar, restInput.sortierung);
		when(teilleistungsartRepository.existsBy(anyString())).thenReturn(false);
		when(teilleistungsartRepository.create(any(DTOTeilleistungsarten.class))).thenReturn(entity);

		final var created = teilleistungsartService.create(restInput);

		Assertions.assertThat(created)
				.hasFieldOrPropertyWithValue("bezeichnung", restInput.bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", restInput.istSichtbar)
				.hasFieldOrPropertyWithValue("sortierung", restInput.sortierung);

	}

	@Test
	@DisplayName("create | Failed - Bezeichnung exists")
	void testCreateFailedBezeichnungExists() {
		final var restInput = createRestRequest();
		when(teilleistungsartRepository.existsBy(anyString())).thenReturn(true);

		final var throwable = catchThrowable(() -> teilleistungsartService.create(restInput));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung bezeichnung wird bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

	}

	@Test
	@DisplayName("patch | Erfolg")
	void testPatchSuccess() {
		final DTOTeilleistungsarten entity = createEntity(1, "old", true, 1);
		when(teilleistungsartRepository.getById(1L)).thenReturn(entity);
		when(teilleistungsartRepository.existsBy(anyString())).thenReturn(false);
		final var bezeichnung = "bezeichnung";
		final var sortierung = 32000;
		final var patch = createPatch();

		final var created = teilleistungsartService.patch(1, patch);

		Assertions.assertThat(created)
				.hasFieldOrPropertyWithValue("bezeichnung", bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", sortierung);

	}

	private static TeilleistungsartPatchRequest createPatch() {
		final var patchRequest = new TeilleistungsartPatchRequest();
		patchRequest.sortierung = JsonNullable.of(32000);
		patchRequest.bezeichnung = JsonNullable.of("bezeichnung");

		return patchRequest;
	}

	@Test
	@DisplayName("patch | Failed Bean Validation")
	void testPatchFailedUnchanged() {
		final Teilleistungsart restInput = createResponse();
		final DTOTeilleistungsarten entity = createEntity(restInput.id, restInput.bezeichnung, restInput.istSichtbar, restInput.sortierung);
		when(teilleistungsartRepository.getById(1L)).thenReturn(entity);
		final var patch = createPatch();

		teilleistungsartService.patch(1, patch);

		Mockito.verify(teilleistungsartRepository, Mockito.times(0)).existsBy(anyString());

	}

	@Test
	@DisplayName("getAll | Erfolg")
	void testGetAllReferenced() {
		final long firstId = 1;
		final long secondId = 2;
		final DTOTeilleistungsarten first = createEntity(firstId, "1", true, 1);
		final DTOTeilleistungsarten second = createEntity(secondId, "2", true, 2);
		when(teilleistungsartRepository.getAll()).thenReturn(List.of(first, second));
		when(teilleistungsartRepository.getReferencedIds(List.of(firstId))).thenReturn(Set.of(firstId));
		when(teilleistungsartRepository.getReferencedIds(List.of(secondId))).thenReturn(Set.of());

		final var results = teilleistungsartService.getAll();

		assertThat(results)
				.hasSize(2)
				.extracting(
						"id",
						"bezeichnung",
						"istSichtbar",
						"sortierung",
						"referenziertInAnderenTabellen"
				)
				.containsExactlyInAnyOrder(
						tuple(firstId, "1", true, 1, true),
						tuple(2L, "2", true, 2, false)
				);

	}

	@Test
	@DisplayName("delete | Erfolg")
	void testDelete() {
		final long firstId = 1;
		final long secondId = 2;
		when(teilleistungsartRepository.getReferencedIds(List.of(firstId, secondId))).thenReturn(Set.of(firstId));
		final DTOTeilleistungsarten savedFirst = createEntity(1, "leistungsart 1", true, 2);
		final DTOTeilleistungsarten savedSecond = createEntity(2, "leistungsart 2", true, 2);
		when(teilleistungsartRepository.findListByIds(List.of(firstId, secondId))).thenReturn(List.of(savedFirst, savedSecond));
		when(teilleistungsartRepository.delete(List.of(savedSecond))).thenReturn(List.of(savedSecond));

		final var resultLogs = teilleistungsartService.delete(List.of(firstId, secondId));

		assertThat(resultLogs)
				.hasSize(2)
				.extracting(
						"id",
						"success",
						"log"
				)
				.containsExactlyInAnyOrder(
						tuple(firstId, false, List.of("Teilleistungsart mit der Bezeichnung leistungsart 1 ist referenziert")),
						tuple(secondId, true, List.of())
				);
	}

	private DTOTeilleistungsarten createEntity(final long id, final String bezeichnung, final boolean sichtbar, final int sortierung) {
		final var entity = new DTOTeilleistungsarten(id);
		entity.Bezeichnung = bezeichnung;
		entity.Sichtbar = sichtbar;
		entity.Sortierung = sortierung;

		return entity;
	}

	private TeilleistungsartCreateRequest createRestRequest() {
		final var restInput = new TeilleistungsartCreateRequest();
		restInput.bezeichnung = "bezeichnung";
		restInput.istSichtbar = true;
		restInput.sortierung = 42;

		return restInput;
	}

	private Teilleistungsart createResponse() {
		final var restInput = new Teilleistungsart();
		restInput.bezeichnung = "bezeichnung";
		restInput.istSichtbar = true;
		restInput.sortierung = 42;

		return restInput;
	}

}
