package de.svws_nrw.service.lehrer.lernplattform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

import de.svws_nrw.core.data.lehrer.LehrerLernplattform;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattformPK;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.lernplattform.LehrerLernplattformMapper;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.lernplattform.LehrerLernplattformRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformCredentialRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für den {@link LehrerLernplattformService}.
 */
@ExtendWith(MockitoExtension.class)
class LehrerLernplattformServiceTest {

	@Mock
	private LehrerLernplattformRepository repository;

	@Mock
	private LehrerRepository lehrerRepository;

	@Mock
	private LernplattformRepository lernplattformRepository;

	@Mock
	private LernplattformCredentialRepository credentialRepository;

	@Mock
	private LehrerLernplattformMapper mapper;

	@Captor
	private ArgumentCaptor<List<DTOLehrerLernplattform>> captor;

	private LehrerLernplattformService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new LehrerLernplattformService(repository, lehrerRepository, lernplattformRepository, credentialRepository, mapper);
		transactionSupportMock = mockStatic(TransactionSupport.class, withSettings().lenient());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private static DTOLehrerLernplattform dto(final long idLehrer, final long idLernplattform) {
		return new DTOLehrerLernplattform(idLehrer, idLernplattform, false, false, false, false);
	}

	// -------------------------------------------------------------------------
	// getListByIdLehrer
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getListByIdLehrer | Mappt die Zuordnungen mit den Zugangsdaten zur jeweiligen Credential-ID")
	void getListByIdLehrer_mapsWithCredentials() {
		final var mitCredential = dto(1L, 10L);
		mitCredential.CredentialID = 5L;
		final var ohneCredential = dto(1L, 11L);
		final var credentials = new DTOCredentialsLernplattformen(5L, 10L, "benutzer");
		final var apiMit = new LehrerLernplattform();
		final var apiOhne = new LehrerLernplattform();
		when(repository.getListByIdLehrer(1L)).thenReturn(List.of(mitCredential, ohneCredential));
		when(credentialRepository.findMapByIds(Set.of(5L))).thenReturn(Map.of(5L, credentials));
		when(mapper.toApi(mitCredential, credentials)).thenReturn(apiMit);
		when(mapper.toApi(ohneCredential, null)).thenReturn(apiOhne);

		assertThat(service.getListByIdLehrer(1L)).containsExactly(apiMit, apiOhne);
	}

	@Test
	@DisplayName("getListByIdLehrer | Leere Liste, wenn keine Zuordnungen vorhanden sind")
	void getListByIdLehrer_empty() {
		when(repository.getListByIdLehrer(99L)).thenReturn(List.of());
		when(credentialRepository.findMapByIds(Set.of())).thenReturn(Map.of());

		assertThat(service.getListByIdLehrer(99L)).isEmpty();
	}

	@Test
	@DisplayName("getListByIdLehrer | Liefert null als Zugangsdaten, wenn die Credential-ID nicht aufgelöst werden kann")
	void getListByIdLehrer_unknownCredential() {
		final var entity = dto(1L, 10L);
		entity.CredentialID = 5L;
		final var api = new LehrerLernplattform();
		when(repository.getListByIdLehrer(1L)).thenReturn(List.of(entity));
		when(credentialRepository.findMapByIds(Set.of(5L))).thenReturn(Map.of());
		when(mapper.toApi(entity, null)).thenReturn(api);

		assertThat(service.getListByIdLehrer(1L)).containsExactly(api);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Wendet den Patch auf die Zuordnung an und liefert sie mit Zugangsdaten zurück")
	void patch_success() {
		final var entity = dto(1L, 10L);
		entity.CredentialID = 5L;
		final var credentials = new DTOCredentialsLernplattformen(5L, 10L, "benutzer");
		final var api = new LehrerLernplattform();
		final var request = new LehrerLernplattformPatchRequest();
		request.einwilligungNutzung = JsonNullable.of(true);
		when(repository.findById(new DTOLehrerLernplattformPK(1L, 10L))).thenReturn(Optional.of(entity));
		when(credentialRepository.findMapByIds(Set.of(5L))).thenReturn(Map.of(5L, credentials));
		when(mapper.toApi(entity, credentials)).thenReturn(api);

		assertThat(service.patch(1L, 10L, request)).isSameAs(api);
		verify(mapper).patch(request, entity);
	}

	@Test
	@DisplayName("patch | Wirft NOT_FOUND, wenn keine Zuordnung existiert")
	void patch_notFound() {
		final var request = new LehrerLernplattformPatchRequest();
		when(repository.findById(new DTOLehrerLernplattformPK(1L, 10L))).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.patch(1L, 10L, request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("LehrerID 1")
				.hasMessageContaining("LernplattformID 10");
		verify(mapper, never()).patch(any(), any());
	}

	// -------------------------------------------------------------------------
	// addForLehrer
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("addForLehrer | Legt nur für die noch fehlenden Lernplattformen eine Zuordnung an")
	void addForLehrer_createsOnlyMissing() {
		final var vorhanden = dto(1L, 10L);
		final var neu = dto(1L, 11L);
		final var api = new LehrerLernplattform();
		when(lehrerRepository.existsById(1L)).thenReturn(true);
		when(repository.getListByIdLehrer(1L)).thenReturn(List.of(vorhanden));
		when(lernplattformRepository.getAllIds()).thenReturn(List.of(10L, 11L));
		when(mapper.toDomain(1L, 11L)).thenReturn(neu);
		when(mapper.toApi(neu, null)).thenReturn(api);

		assertThat(service.addForLehrer(1L)).containsExactly(api);

		verify(repository).create(captor.capture());
		assertThat(captor.getValue()).containsExactly(neu);
		verify(mapper, never()).toDomain(1L, 10L);
	}

	@Test
	@DisplayName("addForLehrer | Legt nichts an, wenn der Lehrer bereits allen Lernplattformen zugeordnet ist")
	void addForLehrer_nothingToCreate() {
		when(lehrerRepository.existsById(1L)).thenReturn(true);
		when(repository.getListByIdLehrer(1L)).thenReturn(List.of(dto(1L, 10L)));
		when(lernplattformRepository.getAllIds()).thenReturn(List.of(10L));

		assertThat(service.addForLehrer(1L)).isEmpty();

		verify(repository, never()).create(ArgumentMatchers.<List<DTOLehrerLernplattform>>any());
	}

	@Test
	@DisplayName("addForLehrer | Wirft NOT_FOUND, wenn der Lehrer nicht existiert")
	void addForLehrer_lehrerNotFound() {
		when(lehrerRepository.existsById(1L)).thenReturn(false);

		assertThatThrownBy(() -> service.addForLehrer(1L))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("ID 1");
	}

	// -------------------------------------------------------------------------
	// addForLernplattform
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("addForLernplattform | Legt nur für die noch fehlenden Lehrer eine Zuordnung an")
	void addForLernplattform_createsOnlyMissing() {
		final var vorhanden = dto(1L, 10L);
		final var neu = dto(2L, 10L);
		final var api = new LehrerLernplattform();
		when(lernplattformRepository.existsById(10L)).thenReturn(true);
		when(repository.getListByIdLernplattform(10L)).thenReturn(List.of(vorhanden));
		when(lehrerRepository.getAllIds()).thenReturn(List.of(1L, 2L));
		when(mapper.toDomain(2L, 10L)).thenReturn(neu);
		when(mapper.toApi(neu, null)).thenReturn(api);

		assertThat(service.addForLernplattform(10L)).containsExactly(api);

		verify(repository).create(captor.capture());
		assertThat(captor.getValue()).containsExactly(neu);
	}

	@Test
	@DisplayName("addForLernplattform | Wirft NOT_FOUND, wenn die Lernplattform nicht existiert")
	void addForLernplattform_lernplattformNotFound() {
		when(lernplattformRepository.existsById(10L)).thenReturn(false);

		assertThatThrownBy(() -> service.addForLernplattform(10L))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("ID 10");
	}

}
