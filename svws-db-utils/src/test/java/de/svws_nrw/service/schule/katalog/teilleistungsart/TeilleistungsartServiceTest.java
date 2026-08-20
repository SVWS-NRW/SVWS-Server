package de.svws_nrw.service.schule.katalog.teilleistungsart;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
		when(teilleistungsartRepository.getReferencedIds(List.of(0L))).thenReturn(Set.of());

		final var created = teilleistungsartService.create(restInput);

		Assertions.assertThat(created)
				.hasFieldOrPropertyWithValue("bezeichnung", restInput.bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", restInput.istSichtbar)
				.hasFieldOrPropertyWithValue("sortierung", restInput.sortierung)
				.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false);
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
		when(teilleistungsartRepository.getReferencedIds(List.of(1L))).thenReturn(Set.of());
		final var patch = createPatch();

		final var created = teilleistungsartService.patch(1, patch);

		Assertions.assertThat(created)
				.hasFieldOrPropertyWithValue("bezeichnung", "bezeichnung")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false);
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
		when(teilleistungsartRepository.getReferencedIds(List.of(restInput.id))).thenReturn(Set.of());
		final var patch = createPatch();

		teilleistungsartService.patch(1, patch);

		verify(teilleistungsartRepository, times(0)).existsBy(anyString());
	}

	@Test
	@DisplayName("delete | referenzierte ID wird nicht gelöscht, nicht referenzierte schon")
	void testDelete() {
		final long firstId = 1;
		final long secondId = 2;
		final var savedFirst = createEntity(firstId, "leistungsart 1", true, 2);
		final var savedSecond = createEntity(secondId, "leistungsart 2", true, 2);

		when(teilleistungsartRepository.getReferencedIds(List.of(firstId, secondId))).thenReturn(Set.of(firstId));
		when(teilleistungsartRepository.findListByIds(List.of(firstId, secondId))).thenReturn(List.of(savedFirst, savedSecond));
		when(teilleistungsartRepository.delete(List.of(savedSecond))).thenReturn(List.of(savedSecond));

		final var result = teilleistungsartService.delete(List.of(firstId, secondId));

		assertThat(result)
				.hasSize(2)
				.extracting("id", "success")
				.containsExactlyInAnyOrder(
						tuple(firstId, false),
						tuple(secondId, true)
				);
		verify(teilleistungsartRepository).delete(List.of(savedSecond));
	}

	@Test
	@DisplayName("delete | alle referenziert - keine Löschung")
	void testDelete_alleReferenziert() {
		final long firstId = 1;
		final long secondId = 2;
		final var savedFirst = createEntity(firstId, "leistungsart 1", true, 2);
		final var savedSecond = createEntity(secondId, "leistungsart 2", true, 2);

		when(teilleistungsartRepository.getReferencedIds(List.of(firstId, secondId))).thenReturn(Set.of(firstId, secondId));
		when(teilleistungsartRepository.findListByIds(List.of(firstId, secondId))).thenReturn(List.of(savedFirst, savedSecond));
		when(teilleistungsartRepository.delete(List.of())).thenReturn(List.of());

		final var result = teilleistungsartService.delete(List.of(firstId, secondId));

		assertThat(result)
				.hasSize(2)
				.allSatisfy(r -> {
					assertThat(r.success).isFalse();
					assertThat(r.log).anyMatch(m -> m.contains("referenziert"));
				});
		verify(teilleistungsartRepository).delete(List.of());
	}

	@Test
	@DisplayName("delete | ID nicht gefunden - Error-Response")
	void testDelete_nichtGefunden() {
		when(teilleistungsartRepository.getReferencedIds(List.of(99L))).thenReturn(Set.of());
		when(teilleistungsartRepository.findListByIds(List.of(99L))).thenReturn(List.of());
		when(teilleistungsartRepository.delete(List.of())).thenReturn(List.of());

		final var result = teilleistungsartService.delete(List.of(99L));

		assertThat(result)
				.hasSize(1)
				.satisfiesExactly(r -> {
					assertThat(r.success).isFalse();
					assertThat(r.id).isEqualTo(99L);
					assertThat(r.log).anyMatch(m -> m.contains("nicht gefunden"));
				});
	}

	@Test
	@DisplayName("delete | leere Eingabe - leere Rückgabe")
	void testDelete_leereEingabe() {
		when(teilleistungsartRepository.getReferencedIds(List.of())).thenReturn(Set.of());
		when(teilleistungsartRepository.findListByIds(List.of())).thenReturn(List.of());
		when(teilleistungsartRepository.delete(List.of())).thenReturn(List.of());

		final var result = teilleistungsartService.delete(List.of());

		assertThat(result).isEmpty();
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
