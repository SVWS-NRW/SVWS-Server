package de.svws_nrw.service.lehrer.personaldaten;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.personaldaten.LehrerPersonaldatenMapper;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.service.lehrer.lehramt.LehrerLehramtService;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests für den Service zu den Personaldaten von Lehrkräften.
 */
@ExtendWith(MockitoExtension.class)
class LehrerPersonaldatenServiceTest {

	private static final long ID                   = 1L;
	private static final long ID_2                 = 2L;
	private static final long ID_UNBEKANNT         = 999L;
	private static final String GUELTIGES_DATUM    = "2020-08-01";
	private static final String ZUGANGSGRUND       = "WIEDER";
	private static final String ABGANGSGRUND       = "RUHEST";
	private static final String UNGUELTIGER_GRUND  = "GIBTSNICHT";

	@Mock
	private LehrerRepository repository;

	@Mock
	private LehrerPersonaldatenMapper mapper;

	@Mock
	private LehrerPersonalabschnittsdatenService personalabschnittsdatenService;

	@Mock
	private LehrerLehramtService lehramtService;

	private LehrerPersonaldatenService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOLehrer lehrer;

	private DTOLehrer lehrer2;

	private LehrerPersonaldaten apiModel;

	private LehrerPersonaldaten apiModel2;

	private List<LehrerPersonalabschnittsdaten> personalabschnittsdaten;

	private List<LehrerLehramtEintrag> lehraemter;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		lehrer = new DTOLehrer(ID, "MUS", "Mustermann");
		lehrer2 = new DTOLehrer(ID_2, "BEI", "Beispiel");

		apiModel = new LehrerPersonaldaten();
		apiModel.id = ID;

		apiModel2 = new LehrerPersonaldaten();
		apiModel2.id = ID_2;

		personalabschnittsdaten = List.of(new LehrerPersonalabschnittsdaten());
		lehraemter = List.of(new LehrerLehramtEintrag());

		service = new LehrerPersonaldatenService(repository, mapper, personalabschnittsdatenService, lehramtService);
	}

	@AfterEach
	void tearDown() {
		if (transactionSupport != null) {
			transactionSupport.close();
		}
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private void mockTransactions() {
		transactionSupport = Mockito.mockStatic(TransactionSupport.class);
		transactionSupport
				.when(() -> TransactionSupport.transactional(
						ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation ->
						invocation.getArgument(0, Supplier.class).get());
	}

	private void stubToApi(
			final DTOLehrer dto,
			final List<LehrerPersonalabschnittsdaten> abschnitte,
			final List<LehrerLehramtEintrag> lehraemterListe,
			final LehrerPersonaldaten result) {
		when(mapper.toApi(
				same(dto),
				argThat(ctx -> ctx.personalabschnittsdaten().equals(abschnitte)
						&& ctx.lehraemter().equals(lehraemterListe))))
				.thenReturn(result);
	}

	private void stubDependentServices(final DTOLehrer dto) {
		when(personalabschnittsdatenService.getByIdLehrer(dto.ID)).thenReturn(personalabschnittsdaten);
		when(lehramtService.getByIdLehrer(dto.ID)).thenReturn(lehraemter);
	}

	private LehrerPersonaldatenPatchRequest createPatchRequest() {
		final var request = new LehrerPersonaldatenPatchRequest();
		request.zugangsdatum = JsonNullable.of(GUELTIGES_DATUM);
		request.zugangsgrund = JsonNullable.of(ZUGANGSGRUND);
		request.abgangsdatum = JsonNullable.of(GUELTIGES_DATUM);
		request.abgangsgrund = JsonNullable.of(ABGANGSGRUND);
		return request;
	}

	private LehrerPersonaldatenBatchPatchRequest createBatchRequest(final long id) {
		final var request = new LehrerPersonaldatenBatchPatchRequest();
		request.id = id;
		request.zugangsdatum = JsonNullable.of(GUELTIGES_DATUM);
		request.zugangsgrund = JsonNullable.of(ZUGANGSGRUND);
		request.abgangsdatum = JsonNullable.of(GUELTIGES_DATUM);
		request.abgangsgrund = JsonNullable.of(ABGANGSGRUND);
		return request;
	}

	// -------------------------------------------------------------------------
	// getById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getById")
	class GetById {

		@Test
		@DisplayName("Liefert die gemappten Personaldaten inklusive Abschnittsdaten und Lehrämtern")
		void getById() {
			when(repository.findById(ID)).thenReturn(Optional.of(lehrer));
			stubDependentServices(lehrer);
			stubToApi(lehrer, personalabschnittsdaten, lehraemter, apiModel);

			final var result = service.getById(ID);

			assertThat(result).isSameAs(apiModel);
		}

		@Test
		@DisplayName("Wirft NOT_FOUND, wenn keine Lehrkraft zur ID existiert")
		void getById_nichtGefunden() {
			when(repository.findById(ID_UNBEKANNT)).thenReturn(Optional.empty());

			assertThatException()
					.isThrownBy(() -> service.getById(ID_UNBEKANNT))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(String.valueOf(ID_UNBEKANNT))
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);

			verifyNoInteractions(personalabschnittsdatenService, lehramtService, mapper);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@BeforeEach
		void initTransaction() {
			mockTransactions();
		}

		@Test
		@DisplayName("Patcht die Lehrkraft und liefert die gemappten Personaldaten")
		void patch() {
			final var request = createPatchRequest();

			when(repository.getById(ID)).thenReturn(lehrer);
			stubDependentServices(lehrer);
			stubToApi(lehrer, personalabschnittsdaten, lehraemter, apiModel);

			final var result = service.patch(ID, request);

			assertThat(result).isSameAs(apiModel);

			final InOrder inOrder = Mockito.inOrder(mapper);
			inOrder.verify(mapper).patch(request, lehrer);
			inOrder.verify(mapper).toApi(same(lehrer), any());
		}

		@Test
		@DisplayName("Lässt undefined Felder ohne Validierungsfehler durch")
		void patch_alleFelderUndefined() {
			final var request = new LehrerPersonaldatenPatchRequest();

			when(repository.getById(ID)).thenReturn(lehrer);
			stubDependentServices(lehrer);
			stubToApi(lehrer, personalabschnittsdaten, lehraemter, apiModel);

			final var result = service.patch(ID, request);

			assertThat(result).isSameAs(apiModel);

			verify(mapper).patch(request, lehrer);
		}

		@Test
		@DisplayName("Erlaubt null als Zugangs- und Abgangsgrund")
		void patch_gruendeNull() {
			final var request = new LehrerPersonaldatenPatchRequest();
			request.zugangsgrund = JsonNullable.of(null);
			request.abgangsgrund = JsonNullable.of(null);

			when(repository.getById(ID)).thenReturn(lehrer);
			stubDependentServices(lehrer);
			stubToApi(lehrer, personalabschnittsdaten, lehraemter, apiModel);

			final var result = service.patch(ID, request);

			assertThat(result).isSameAs(apiModel);

			verify(mapper).patch(request, lehrer);
		}

		@Test
		@DisplayName("Reicht NOT_FOUND des Repositories durch")
		void patch_lehrerNichtGefunden() {
			final var request = createPatchRequest();

			when(repository.getById(ID_UNBEKANNT)).thenThrow(
					new ApiOperationException(Response.Status.NOT_FOUND, "Kein Lehrer zur ID %d gefunden".formatted(ID_UNBEKANNT)));

			assertThatException()
					.isThrownBy(() -> service.patch(ID_UNBEKANNT, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);

			verify(mapper, never()).patch(any(), any());
		}


		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekanntem Zugangsgrund")
		void patch_zugangsgrundUnbekannt() {
			final var request = new LehrerPersonaldatenPatchRequest();
			request.zugangsgrund = JsonNullable.of(UNGUELTIGER_GRUND);

			when(repository.getById(ID)).thenReturn(lehrer);

			assertThatException()
					.isThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(UNGUELTIGER_GRUND)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekanntem Abgangsgrund")
		void patch_abgangsgrundUnbekannt() {
			final var request = new LehrerPersonaldatenPatchRequest();
			request.abgangsgrund = JsonNullable.of(UNGUELTIGER_GRUND);

			when(repository.getById(ID)).thenReturn(lehrer);

			assertThatException()
					.isThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(UNGUELTIGER_GRUND)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any());
		}
	}

	// -------------------------------------------------------------------------
	// patchMultiple
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patchMultiple")
	class PatchMultiple {

		@BeforeEach
		void initTransaction() {
			mockTransactions();
		}

		@Test
		@DisplayName("Patcht mehrere Lehrkräfte und behält die Reihenfolge der Requests bei")
		void patchMultiple() {
			final var request1 = createBatchRequest(ID);
			final var request2 = createBatchRequest(ID_2);
			final var abschnitte2 = List.of(new LehrerPersonalabschnittsdaten());
			final var lehraemter2 = List.of(new LehrerLehramtEintrag());

			when(repository.findMapByIds(List.of(ID_2, ID))).thenReturn(Map.of(ID, lehrer, ID_2, lehrer2));
			when(personalabschnittsdatenService.getMapByIdLehrer(anyList()))
					.thenReturn(Map.of(ID, personalabschnittsdaten, ID_2, abschnitte2));
			when(lehramtService.getMapByIdLehrer(anyList()))
					.thenReturn(Map.of(ID, lehraemter, ID_2, lehraemter2));
			stubToApi(lehrer, personalabschnittsdaten, lehraemter, apiModel);
			stubToApi(lehrer2, abschnitte2, lehraemter2, apiModel2);

			final var result = service.patchMultiple(List.of(request2, request1));

			assertThat(result).containsExactly(apiModel2, apiModel);

			verify(mapper).patch(request1, lehrer);
			verify(mapper).patch(request2, lehrer2);
		}

		@Test
		@DisplayName("Verwendet leere Listen, wenn zu einer Lehrkraft keine Abschnittsdaten oder Lehrämter existieren")
		void patchMultiple_keineAbhaengigenDaten() {
			final var request = createBatchRequest(ID);

			when(repository.findMapByIds(List.of(ID))).thenReturn(Map.of(ID, lehrer));
			when(personalabschnittsdatenService.getMapByIdLehrer(anyList())).thenReturn(Map.of());
			when(lehramtService.getMapByIdLehrer(anyList())).thenReturn(Map.of());
			stubToApi(lehrer, List.of(), List.of(), apiModel);

			final var result = service.patchMultiple(List.of(request));

			assertThat(result).containsExactly(apiModel);

			verify(mapper).patch(request, lehrer);
		}

		@Test
		@DisplayName("Liefert leere Liste bei leerer Eingabe")
		void patchMultiple_leereEingabe() {
			final var result = service.patchMultiple(List.of());

			assertThat(result).isEmpty();

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft NOT_FOUND mit allen fehlenden IDs und ändert nichts")
		void patchMultiple_lehrerNichtGefunden() {
			final var request1 = createBatchRequest(ID);
			final var request2 = createBatchRequest(ID_UNBEKANNT);

			when(repository.findMapByIds(List.of(ID, ID_UNBEKANNT))).thenReturn(Map.of(ID, lehrer));

			assertThatException()
					.isThrownBy(() -> service.patchMultiple(List.of(request1, request2)))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(String.valueOf(ID_UNBEKANNT))
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);

			verify(mapper, never()).patch(any(), any());
			verifyNoInteractions(personalabschnittsdatenService, lehramtService);
		}

		@Test
		@DisplayName("Nennt doppelt angefragte, fehlende IDs nur einmal")
		void patchMultiple_fehlendeIdsDistinct() {
			final var request1 = createBatchRequest(ID_UNBEKANNT);
			final var request2 = createBatchRequest(ID_UNBEKANNT);

			when(repository.findMapByIds(List.of(ID_UNBEKANNT, ID_UNBEKANNT))).thenReturn(Map.of());

			assertThatException()
					.isThrownBy(() -> service.patchMultiple(List.of(request1, request2)))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("[" + ID_UNBEKANNT + "]")
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekanntem Zugangsgrund")
		void patchMultiple_zugangsgrundUnbekannt() {
			final var request = createBatchRequest(ID);
			request.zugangsgrund = JsonNullable.of(UNGUELTIGER_GRUND);

			assertThatException()
					.isThrownBy(() -> service.patchMultiple(List.of(request)))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(UNGUELTIGER_GRUND)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verifyNoInteractions(repository);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekanntem Abgangsgrund")
		void patchMultiple_abgangsgrundUnbekannt() {
			final var request = createBatchRequest(ID);
			request.abgangsgrund = JsonNullable.of(UNGUELTIGER_GRUND);

			assertThatException()
					.isThrownBy(() -> service.patchMultiple(List.of(request)))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(UNGUELTIGER_GRUND)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verifyNoInteractions(repository);
		}
	}
}
