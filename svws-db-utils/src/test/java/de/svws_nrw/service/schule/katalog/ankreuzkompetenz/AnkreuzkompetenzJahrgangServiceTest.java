package de.svws_nrw.service.schule.katalog.ankreuzkompetenz;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangMapper;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzJahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnkreuzkompetenzJahrgangServiceTest {

	@Mock
	private AnkreuzkompetenzJahrgangRepository repository;

	@Mock
	private AnkreuzkompetenzRepository ankreuzkompetenzRepository;

	@Mock
	private JahrgangRepository jahrgangRepository;

	@Mock
	private AnkreuzkompetenzJahrgangMapper mapper;

	private AnkreuzkompetenzJahrgangService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOAnkreuzkompetenzJahrgang entity;
	private AnkreuzkompetenzJahrgangszuordnung apiModel;

	@BeforeEach
	void setUp() {
		entity = new DTOAnkreuzkompetenzJahrgang(1L, 10L, 3L);

		apiModel = new AnkreuzkompetenzJahrgangszuordnung();
		apiModel.id = 1L;
		apiModel.idAnkreuzkompetenz = 10L;
		apiModel.idJahrgang = 3L;

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		service = new AnkreuzkompetenzJahrgangService(repository, ankreuzkompetenzRepository, jahrgangRepository, mapper);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private AnkreuzkompetenzJahrgangCreateRequest createRequest() {
		final var request = new AnkreuzkompetenzJahrgangCreateRequest();
		request.idAnkreuzkompetenz = 10L;
		request.idJahrgang = 3L;
		return request;
	}

	// -------------------------------------------------------------------------
	// getAllByIdAnkreuzkompetenz
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getAllByIdAnkreuzkompetenz")
	class GetAllByIdAnkreuzkompetenz {

		@Test
		@DisplayName("Gibt leere Map zurück wenn keine Zuordnungen vorhanden")
		void getAll_leer() {
			when(repository.getAll()).thenReturn(List.of());

			assertThat(service.getAllByIdAnkreuzkompetenz()).isEmpty();
			verify(mapper, never()).toApi(any());
		}

		@Test
		@DisplayName("Gruppiert Zuordnungen nach idAnkreuzkompetenz")
		void getAll_gruppiertNachIdAnkreuzkompetenz() {
			final var entity2 = new DTOAnkreuzkompetenzJahrgang(2L, 20L, 5L);

			final var apiModel2 = new AnkreuzkompetenzJahrgangszuordnung();
			apiModel2.id = 2L;
			apiModel2.idAnkreuzkompetenz = 20L;
			apiModel2.idJahrgang = 5L;

			when(repository.getAll()).thenReturn(List.of(entity, entity2));
			when(mapper.toApi(entity)).thenReturn(apiModel);
			when(mapper.toApi(entity2)).thenReturn(apiModel2);

			final var result = service.getAllByIdAnkreuzkompetenz();

			assertThat(result)
					.hasSize(2)
					.containsKey(10L)
					.containsKey(20L);
			assertThat(result.get(10L)).containsExactly(apiModel);
			assertThat(result.get(20L)).containsExactly(apiModel2);
		}

		@Test
		@DisplayName("Fasst mehrere Zuordnungen derselben Ankreuzkompetenz in einer Liste zusammen")
		void getAll_mehrereZuordnungenProAnkreuzkompetenz() {
			final var entity2 = new DTOAnkreuzkompetenzJahrgang(2L, 10L, 5L);

			final var apiModel2 = new AnkreuzkompetenzJahrgangszuordnung();
			apiModel2.id = 2L;
			apiModel2.idAnkreuzkompetenz = 10L;
			apiModel2.idJahrgang = 5L;

			when(repository.getAll()).thenReturn(List.of(entity, entity2));
			when(mapper.toApi(entity)).thenReturn(apiModel);
			when(mapper.toApi(entity2)).thenReturn(apiModel2);

			final var result = service.getAllByIdAnkreuzkompetenz();

			assertThat(result).hasSize(1);
			assertThat(result.get(10L)).containsExactlyInAnyOrder(apiModel, apiModel2);
		}

		@Test
		@DisplayName("Verwendet idAnkreuzkompetenz des API-Modells als Map-Schlüssel")
		void getAll_verwendetIdAnkreuzkompetenzAlsSchluessel() {
			when(repository.getAll()).thenReturn(List.of(entity));
			when(mapper.toApi(entity)).thenReturn(apiModel);

			final var result = service.getAllByIdAnkreuzkompetenz();

			assertThat(result).containsKey(apiModel.idAnkreuzkompetenz);
		}
	}

	// -------------------------------------------------------------------------
	// createMultiple
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("createMultiple")
	class CreateMultiple {

		@Test
		@DisplayName("Legt mehrere Zuordnungen korrekt an")
		void createMultiple_legtZuordnungenAn() {
			final var request = createRequest();

			when(ankreuzkompetenzRepository.existsById(10L)).thenReturn(true);
			when(jahrgangRepository.existsById(3L)).thenReturn(true);
			when(mapper.toDomain(request)).thenReturn(entity);
			when(repository.create(List.of(entity))).thenReturn(List.of(entity));
			when(mapper.toApi(entity)).thenReturn(apiModel);

			final var result = service.createMultiple(List.of(request));

			assertThat(result).containsExactly(apiModel);
			verify(repository).create(List.of(entity));
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Ankreuzkompetenz nicht existiert")
		void createMultiple_ankreuzkompetenzNichtGefunden() {
			final var request = createRequest();

			when(ankreuzkompetenzRepository.existsById(10L)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.createMultiple(List.of(request)))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("10")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repository, never()).create(Collections.singleton(any()));
			verify(mapper, never()).toDomain(any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Jahrgang nicht existiert")
		void createMultiple_jahrgangNichtGefunden() {
			final var request = createRequest();

			when(ankreuzkompetenzRepository.existsById(10L)).thenReturn(true);
			when(jahrgangRepository.existsById(3L)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.createMultiple(List.of(request)))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("3")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repository, never()).create(Collections.singleton(any()));
			verify(mapper, never()).toDomain(any());
		}

		@Test
		@DisplayName("Gibt leere Liste zurück bei leerer Eingabe")
		void createMultiple_leereListe() {
			final var result = service.createMultiple(List.of());

			assertThat(result).isEmpty();
			verify(repository).create(List.of());
		}
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("delete")
	class Delete {

		@Test
		@DisplayName("Delegiert an BulkDeleteUtils.delete(...) und liefert Ergebnis zurück")
		void delete_delegiert() {
			final var ids = List.of(2L, 1L);

			final var r1 = new SimpleOperationResponse();
			r1.id = 1L;
			r1.success = true;

			final var r2 = new SimpleOperationResponse();
			r2.id = 2L;
			r2.success = false;

			try (MockedStatic<BulkDeleteUtils> bulkDeleteUtils = mockStatic(BulkDeleteUtils.class)) {
				bulkDeleteUtils.when(() -> BulkDeleteUtils.delete(
								eq(ids),
								eq(repository),
								any(),
								eq("AnkreuzkompetenzJahrgangzuordnung")
						))
						.thenReturn(List.of(r1, r2));

				final var result = service.delete(ids);

				assertThat(result).containsExactly(r1, r2);
				bulkDeleteUtils.verify(() -> BulkDeleteUtils.delete(
						eq(ids),
						eq(repository),
						any(),
						eq("AnkreuzkompetenzJahrgangzuordnung")
				));
			}
		}
	}
}
