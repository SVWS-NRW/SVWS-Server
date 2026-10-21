package de.svws_nrw.service.schueler.lernplattform;

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

import de.svws_nrw.core.data.schueler.SchuelerLernplattform;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattformPK;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.lernplattform.SchuelerLernplattformMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.lernplattform.SchuelerLernplattformRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformCredentialRepository;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für den {@link SchuelerLernplattformService}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerLernplattformServiceTest {

	@Mock
	private SchuelerLernplattformRepository repository;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private LernplattformRepository lernplattformRepository;

	@Mock
	private LernplattformCredentialRepository credentialRepository;

	@Mock
	private SchuelerLernplattformMapper mapper;

	@Captor
	private ArgumentCaptor<List<DTOSchuelerLernplattform>> captor;

	private SchuelerLernplattformService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new SchuelerLernplattformService(repository, schuelerRepository, lernplattformRepository, credentialRepository, mapper);
		transactionSupportMock = mockStatic(TransactionSupport.class, withSettings().lenient());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private static DTOSchuelerLernplattform dto(final long idSchueler, final long idLernplattform) {
		return new DTOSchuelerLernplattform(idSchueler, idLernplattform, false, false, false, false);
	}

	// -------------------------------------------------------------------------
	// getListByIdSchueler
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getListByIdSchueler | Mappt die Zuordnungen mit den Zugangsdaten zur jeweiligen Credential-ID")
	void getListByIdSchueler_mapsWithCredentials() {
		final var mitCredential = dto(1L, 10L);
		mitCredential.CredentialID = 5L;
		final var ohneCredential = dto(1L, 11L);
		final var credentials = new DTOCredentialsLernplattformen(5L, 10L, "benutzer");
		final var apiMit = new SchuelerLernplattform();
		final var apiOhne = new SchuelerLernplattform();
		when(repository.getListByIdSchueler(1L)).thenReturn(List.of(mitCredential, ohneCredential));
		when(credentialRepository.findMapByIds(Set.of(5L))).thenReturn(Map.of(5L, credentials));
		when(mapper.toApi(mitCredential, credentials)).thenReturn(apiMit);
		when(mapper.toApi(ohneCredential, null)).thenReturn(apiOhne);

		assertThat(service.getListByIdSchueler(1L)).containsExactly(apiMit, apiOhne);
	}

	@Test
	@DisplayName("getListByIdSchueler | Leere Liste, wenn keine Zuordnungen vorhanden sind")
	void getListByIdSchueler_empty() {
		when(repository.getListByIdSchueler(99L)).thenReturn(List.of());
		when(credentialRepository.findMapByIds(Set.of())).thenReturn(Map.of());

		assertThat(service.getListByIdSchueler(99L)).isEmpty();
	}

	@Test
	@DisplayName("getListByIdSchueler | Liefert null als Zugangsdaten, wenn die Credential-ID nicht aufgelöst werden kann")
	void getListByIdSchueler_unknownCredential() {
		final var entity = dto(1L, 10L);
		entity.CredentialID = 5L;
		final var api = new SchuelerLernplattform();
		when(repository.getListByIdSchueler(1L)).thenReturn(List.of(entity));
		when(credentialRepository.findMapByIds(Set.of(5L))).thenReturn(Map.of());
		when(mapper.toApi(entity, null)).thenReturn(api);

		assertThat(service.getListByIdSchueler(1L)).containsExactly(api);
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
		final var api = new SchuelerLernplattform();
		final var request = new SchuelerLernplattformPatchRequest();
		request.einwilligungNutzung = JsonNullable.of(true);
		when(repository.findById(new DTOSchuelerLernplattformPK(1L, 10L))).thenReturn(Optional.of(entity));
		when(credentialRepository.findMapByIds(Set.of(5L))).thenReturn(Map.of(5L, credentials));
		when(mapper.toApi(entity, credentials)).thenReturn(api);

		assertThat(service.patch(1L, 10L, request)).isSameAs(api);
		verify(mapper).patch(request, entity);
	}

	@Test
	@DisplayName("patch | Wirft NOT_FOUND, wenn keine Zuordnung existiert")
	void patch_notFound() {
		final var request = new SchuelerLernplattformPatchRequest();
		when(repository.findById(new DTOSchuelerLernplattformPK(1L, 10L))).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.patch(1L, 10L, request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("SchuelerID 1")
				.hasMessageContaining("LernplattformID 10");
		verify(mapper, never()).patch(any(), any());
	}

	// -------------------------------------------------------------------------
	// addForSchueler
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("addForSchueler | Legt nur für die noch fehlenden Lernplattformen eine Zuordnung an")
	void addForSchueler_createsOnlyMissing() {
		final var vorhanden = dto(1L, 10L);
		final var neu = dto(1L, 11L);
		final var api = new SchuelerLernplattform();
		when(schuelerRepository.existsById(1L)).thenReturn(true);
		when(repository.getListByIdSchueler(1L)).thenReturn(List.of(vorhanden));
		when(lernplattformRepository.getAllIds()).thenReturn(List.of(10L, 11L));
		when(mapper.toDomain(1L, 11L)).thenReturn(neu);
		when(mapper.toApi(neu, null)).thenReturn(api);

		assertThat(service.addForSchueler(1L)).containsExactly(api);

		verify(repository).create(captor.capture());
		assertThat(captor.getValue()).containsExactly(neu);
		verify(mapper, never()).toDomain(1L, 10L);
	}

	@Test
	@DisplayName("addForSchueler | Legt nichts an, wenn der Schüler bereits allen Lernplattformen zugeordnet ist")
	void addForSchueler_nothingToCreate() {
		when(schuelerRepository.existsById(1L)).thenReturn(true);
		when(repository.getListByIdSchueler(1L)).thenReturn(List.of(dto(1L, 10L)));
		when(lernplattformRepository.getAllIds()).thenReturn(List.of(10L));

		assertThat(service.addForSchueler(1L)).isEmpty();

		verify(repository, never()).create(ArgumentMatchers.<List<DTOSchuelerLernplattform>>any());
	}

	@Test
	@DisplayName("addForSchueler | Wirft NOT_FOUND, wenn der Schüler nicht existiert")
	void addForSchueler_schuelerNotFound() {
		when(schuelerRepository.existsById(1L)).thenReturn(false);

		assertThatThrownBy(() -> service.addForSchueler(1L))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("ID 1");
	}

	// -------------------------------------------------------------------------
	// addForLernplattform
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("addForLernplattform | Legt nur für die noch fehlenden Schüler eine Zuordnung an")
	void addForLernplattform_createsOnlyMissing() {
		final var vorhanden = dto(1L, 10L);
		final var neu = dto(2L, 10L);
		final var api = new SchuelerLernplattform();
		when(lernplattformRepository.existsById(10L)).thenReturn(true);
		when(repository.getListByIdLernplattform(10L)).thenReturn(List.of(vorhanden));
		when(schuelerRepository.getAllIds()).thenReturn(List.of(1L, 2L));
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
