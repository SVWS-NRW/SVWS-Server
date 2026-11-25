package de.svws_nrw.service.kurse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Arrays;
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
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrerPK;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.kurse.KurslehrerMapper;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.kurse.KurslehrerRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für den {@link KurslehrerService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für den KurslehrerService")
class KurslehrerServiceTest {

	private static final long ID_KURS = 1L;
	private static final long ID_LEHRER = 10L;

	@Mock
	private KurslehrerRepository kurslehrerRepository;

	@Mock
	private KurseRepository kurseRepository;

	@Mock
	private LehrerRepository lehrerRepository;

	@Mock
	private KurslehrerMapper mapper;

	private KurslehrerService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new KurslehrerService(kurslehrerRepository, kurseRepository, lehrerRepository, mapper);
		transactionSupportMock = mockStatic(TransactionSupport.class, withSettings().lenient());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private static KurslehrerCreateRequest createRequest(final long idLehrer) {
		final var request = new KurslehrerCreateRequest();
		request.idLehrer = idLehrer;
		request.wochenstundenLehrer = 2.0;
		return request;
	}

	@Test
	@DisplayName("getListByIdKurs | Mappt die Zusatzkräfte des Kurses")
	void getListByIdKurs_maps() {
		final var entity1 = new DTOKursLehrer(ID_KURS, 10L);
		final var entity2 = new DTOKursLehrer(ID_KURS, 11L);
		final var api1 = new KursLehrer();
		final var api2 = new KursLehrer();
		when(kurslehrerRepository.getListByIdKurs(ID_KURS)).thenReturn(List.of(entity1, entity2));
		when(mapper.toApi(entity1)).thenReturn(api1);
		when(mapper.toApi(entity2)).thenReturn(api2);

		assertThat(service.getListByIdKurs(ID_KURS)).containsExactly(api1, api2);
	}

	@Test
	@DisplayName("getListByIdKurs | Leere Liste, wenn keine Zusatzkräfte vorhanden sind")
	void getListByIdKurs_empty() {
		when(kurslehrerRepository.getListByIdKurs(99L)).thenReturn(List.of());

		assertThat(service.getListByIdKurs(99L)).isEmpty();
	}

	@Test
	@DisplayName("create | Legt die Zusatzkraft an und liefert das gemappte Ergebnis")
	void create_success() {
		final var request = createRequest(ID_LEHRER);
		final var entity = new DTOKursLehrer(ID_KURS, ID_LEHRER);
		final var api = new KursLehrer();
		when(kurseRepository.findById(ID_KURS)).thenReturn(Optional.of(new DTOKurs(ID_KURS, 10L, "K1", 3L)));
		when(lehrerRepository.existsById(ID_LEHRER)).thenReturn(true);
		when(kurslehrerRepository.findById(new DTOKursLehrerPK(ID_KURS, ID_LEHRER))).thenReturn(Optional.empty());
		when(mapper.toDomain(ID_KURS, request)).thenReturn(entity);
		when(kurslehrerRepository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(api);

		assertThat(service.create(ID_KURS, request)).isSameAs(api);

		verify(kurslehrerRepository).create(entity);
	}

	@Test
	@DisplayName("create | Ein nicht vorhandener Kurs führt zu 404")
	void create_kursNotFound() {
		when(kurseRepository.findById(ID_KURS)).thenReturn(Optional.empty());
		final var request = createRequest(ID_LEHRER);

		assertThatThrownBy(() -> service.create(ID_KURS, request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("Kurs mit der ID 1");

		verify(kurslehrerRepository, never()).create(any(DTOKursLehrer.class));
	}

	@Test
	@DisplayName("create | Ein nicht vorhandener Lehrer führt zu 404")
	void create_lehrerNotFound() {
		when(kurseRepository.findById(ID_KURS)).thenReturn(Optional.of(new DTOKurs(ID_KURS, 10L, "K1", 3L)));
		when(lehrerRepository.existsById(ID_LEHRER)).thenReturn(false);
		final var request = createRequest(ID_LEHRER);

		assertThatThrownBy(() -> service.create(ID_KURS, request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("Lehrer mit der ID 10");

		verify(kurslehrerRepository, never()).create(any(DTOKursLehrer.class));
	}

	@Test
	@DisplayName("create | Eine bereits vorhandene Zusatzkraft führt zu 400")
	void create_alreadyExists() {
		when(kurseRepository.findById(ID_KURS)).thenReturn(Optional.of(new DTOKurs(ID_KURS, 10L, "K1", 3L)));
		when(lehrerRepository.existsById(ID_LEHRER)).thenReturn(true);
		when(kurslehrerRepository.findById(new DTOKursLehrerPK(ID_KURS, ID_LEHRER))).thenReturn(Optional.of(new DTOKursLehrer(ID_KURS, ID_LEHRER)));
		final var request = createRequest(ID_LEHRER);

		assertThatThrownBy(() -> service.create(ID_KURS, request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.BAD_REQUEST))
				.hasMessageContaining("Kurs-ID 1")
				.hasMessageContaining("Lehrer-ID 10");

		verify(kurslehrerRepository, never()).create(any(DTOKursLehrer.class));
	}

	@Test
	@DisplayName("patch | Wendet den Patch auf die Zusatzkraft an und liefert das gemappte Ergebnis")
	void patch_success() {
		final var request = new KurslehrerPatchRequest();
		request.wochenstundenLehrer = JsonNullable.of(4.0);
		final var entity = new DTOKursLehrer(ID_KURS, ID_LEHRER);
		final var api = new KursLehrer();
		when(kurslehrerRepository.findById(new DTOKursLehrerPK(ID_KURS, ID_LEHRER))).thenReturn(Optional.of(entity));
		when(mapper.toApi(entity)).thenReturn(api);

		assertThat(service.patch(ID_KURS, ID_LEHRER, request)).isSameAs(api);

		verify(mapper).patch(request, entity);
	}

	@Test
	@DisplayName("patch | Eine nicht vorhandene Zusatzkraft führt zu 404")
	void patch_notFound() {
		when(kurslehrerRepository.findById(new DTOKursLehrerPK(ID_KURS, ID_LEHRER))).thenReturn(Optional.empty());
		final var request = new KurslehrerPatchRequest();

		assertThatThrownBy(() -> service.patch(ID_KURS, ID_LEHRER, request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("Kurs-ID 1")
				.hasMessageContaining("Lehrer-ID 10");

		verify(mapper, never()).patch(any(), any());
	}

	@Test
	@DisplayName("delete | Löscht die vorhandenen Zusatzkräfte und meldet nicht vorhandene als Fehler, sortiert nach ID")
	void delete_mixedResult() {
		final var entity = new DTOKursLehrer(ID_KURS, 11L);
		final var nichtAngefragt = new DTOKursLehrer(ID_KURS, 13L);
		when(kurslehrerRepository.getListByIdKurs(ID_KURS)).thenReturn(List.of(nichtAngefragt, entity));
		when(kurslehrerRepository.delete(List.of(entity))).thenReturn(List.of(entity));

		assertThat(service.delete(ID_KURS, List.of(12L, 11L))).satisfiesExactly(
				r -> assertThat(r).extracting("id", "success").containsExactly(11L, true),
				r -> {
					assertThat(r).extracting("id", "success").containsExactly(12L, false);
					assertThat(r.log).singleElement().asString().contains("Kurs-ID 1").contains("Lehrer-ID 12");
				});
	}

	@Test
	@DisplayName("delete | Doppelte Lehrer-IDs werden nur einmal verarbeitet")
	void delete_duplicateIds() {
		final var entity = new DTOKursLehrer(ID_KURS, ID_LEHRER);
		when(kurslehrerRepository.getListByIdKurs(ID_KURS)).thenReturn(List.of(entity));
		when(kurslehrerRepository.delete(List.of(entity))).thenReturn(List.of(entity));

		assertThat(service.delete(ID_KURS, List.of(ID_LEHRER, ID_LEHRER)))
				.extracting("id", "success")
				.containsExactly(tuple(ID_LEHRER, true));

		verify(kurslehrerRepository).getListByIdKurs(ID_KURS);
	}

	@Test
	@DisplayName("delete | Ohne vorhandene Zusatzkräfte wird nur je ID ein Fehler gemeldet")
	void delete_nothingFound() {
		when(kurslehrerRepository.getListByIdKurs(ID_KURS)).thenReturn(List.of());
		when(kurslehrerRepository.delete(List.<DTOKursLehrer>of())).thenReturn(List.of());

		assertThat(service.delete(ID_KURS, List.of(ID_LEHRER)))
				.extracting("id", "success")
				.containsExactly(tuple(ID_LEHRER, false));
	}

	@Test
	@DisplayName("delete | Eine Liste null führt zu 400")
	void delete_idsNull() {
		assertThatThrownBy(() -> service.delete(ID_KURS, null))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.BAD_REQUEST))
				.hasMessageContaining("null");

		verify(kurslehrerRepository, never()).delete(ArgumentMatchers.<List<DTOKursLehrer>>any());
	}

	@Test
	@DisplayName("delete | Eine Liste mit einem null-Wert führt zu 400")
	void delete_idsContainNull() {
		final var ids = Arrays.asList(ID_LEHRER, null);

		assertThatThrownBy(() -> service.delete(ID_KURS, ids))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.BAD_REQUEST))
				.hasMessageContaining("null");

		verify(kurslehrerRepository, never()).delete(ArgumentMatchers.<List<DTOKursLehrer>>any());
	}

}
