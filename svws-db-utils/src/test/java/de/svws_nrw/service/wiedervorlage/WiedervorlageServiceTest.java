package de.svws_nrw.service.wiedervorlage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.svws_nrw.db.dto.current.schule.DTOWiedervorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.WiedervorlageMapper;
import de.svws_nrw.repo.benutzer.BenutzerRepository;
import de.svws_nrw.repo.benutzer.BenutzergruppeRepository;
import de.svws_nrw.repo.benutzer.BenutzergruppenMitgliedRepository;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepository;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Response.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WiedervorlageServiceTest {

	@Mock
	private WiedervorlageRepository wiedervorlageRepository;
	@Mock
	private BenutzergruppeRepository benutzergruppeRepository;
	@Mock
	private BenutzergruppenMitgliedRepository benutzergruppenMitgliedRepository;
	@Mock
	private BenutzerRepository benutzerRepository;

	private final WiedervorlageMapper wiedervorlageMapper = Mappers.getMapper(WiedervorlageMapper.class);

	private WiedervorlageService cut;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		cut = new WiedervorlageService(
				wiedervorlageRepository,
				benutzergruppenMitgliedRepository,
				benutzergruppeRepository,
				benutzerRepository,
				wiedervorlageMapper
		);
		transactionSupportMock = Mockito.mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(inv -> inv.getArgument(0, Supplier.class).get());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.any(Runnable.class)))
				.thenAnswer(inv -> {
					inv.getArgument(0, Runnable.class).run();
					return null;
				});
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}


	@Test
	@DisplayName("get | Eintrag wird zurueckgegeben wenn Benutzer Eigentuemer ist")
	void getSuccess() throws ApiOperationException {
		final var dto = buildEntity(1L, 42L, null);
		final long benutzerId = 42L;

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(benutzerId);
		Mockito.when(wiedervorlageRepository.findByIdAndBenutzerId(1L, benutzerId)).thenReturn(Optional.of(dto));

		final var result = cut.get(1L);

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", dto.id)
				.hasFieldOrPropertyWithValue("idBenutzer", dto.idBenutzer)
				.hasFieldOrPropertyWithValue("bemerkung", dto.bemerkung)
				.hasFieldOrPropertyWithValue("automatischErledigt", dto.automatischErledigt)
				.hasFieldOrPropertyWithValue("idBenutzergruppe", dto.idBenutzergruppe)
				.hasFieldOrPropertyWithValue("tsWiedervorlage", dto.tsWiedervorlage);
	}

	@Test
	@DisplayName("get | NOT_FOUND wenn kein Eintrag mit der ID existiert")
	void getNotFound() {
		final long benutzerId = 42L;

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(benutzerId);
		Mockito.when(wiedervorlageRepository.findByIdAndBenutzerId(99L, benutzerId)).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> cut.get(99L))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | gibt deduplizierte Liste aller zugaenglichen Eintraege zurueck")
	void getAllSuccess() throws ApiOperationException {
		final var firstEntity = buildEntity(1L, 42L, null);
		final var secondEntity = buildEntity(2L, 42L, 5L);

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(42L);
		Mockito.when(wiedervorlageRepository.findAllByBenutzerId(42L)).thenReturn(List.of(firstEntity, secondEntity));

		final var result = cut.getAll();

		assertThat(result)
				.hasSize(2)
				.anySatisfy(e -> assertThat(e)
						.hasFieldOrPropertyWithValue("id", firstEntity.id)
						.hasFieldOrPropertyWithValue("idBenutzer", firstEntity.idBenutzer))
				.anySatisfy(e -> assertThat(e)
						.hasFieldOrPropertyWithValue("id", secondEntity.id)
						.hasFieldOrPropertyWithValue("idBenutzergruppe", secondEntity.idBenutzergruppe));
	}

	@Test
	@DisplayName("create | CreateRequest wird korrekt auf Entity und Response gemappt")
	void createSuccess() throws ApiOperationException {
		final var request = buildCreateRequest();

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(42L);

		final var result = cut.create(request);

		Mockito.verify(wiedervorlageRepository).flush();
		assertThat(result)
				.hasFieldOrPropertyWithValue("bemerkung", request.bemerkung)
				.hasFieldOrPropertyWithValue("automatischErledigt", request.automatischErledigt)
				.hasFieldOrPropertyWithValue("idBenutzer", 42L)
				.hasFieldOrPropertyWithValue("idBenutzergruppe", null)
				.hasFieldOrPropertyWithValue("tsWiedervorlage", request.tsWiedervorlage)
				.satisfies(r -> assertThat(r.tsAngelegt).isNotNull());
	}

	@Test
	@DisplayName("create | Lehrer Success")
	void createSuccessLehrer() throws ApiOperationException {
		final int typPerson = 1;
		final long idPerson = 2L;
		final var request = buildCreateRequest(typPerson, idPerson);

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(42L);

		final var result = cut.create(request);

		Mockito.verify(wiedervorlageRepository).flush();
		assertThat(result)
				.hasFieldOrPropertyWithValue("bemerkung", request.bemerkung)
				.hasFieldOrPropertyWithValue("automatischErledigt", request.automatischErledigt)
				.hasFieldOrPropertyWithValue("idBenutzer", 42L)
				.hasFieldOrPropertyWithValue("idBenutzergruppe", null)
				.hasFieldOrPropertyWithValue("typPerson", typPerson)
				.hasFieldOrPropertyWithValue("idPerson", idPerson)
				.hasFieldOrPropertyWithValue("tsWiedervorlage", request.tsWiedervorlage)
				.satisfies(r -> assertThat(r.tsAngelegt).isNotNull());
	}

	@Test
	@DisplayName("create | BAD_REQUEST wenn Benutzergruppe ungueltig ist")
	void createBadRequestBenutzergruppe() {
		final var request = buildCreateRequest();
		request.idBenutzergruppe = 99L;

		Mockito.when(benutzergruppeRepository.findById(99L)).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> cut.create(request))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | CONFLICT bei PersistenceException (FK Verletzung)")
	void createConflictPersistenceException() {
		final var request = buildCreateRequest();

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(42L);
		Mockito.doThrow(PersistenceException.class)
				.when(wiedervorlageRepository).create(ArgumentMatchers.any(DTOWiedervorlage.class));

		Assertions.assertThatThrownBy(() -> cut.create(request))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(Status.CONFLICT);
	}

	@Test
	@DisplayName("patch | PatchRequest wird korrekt auf Entity und Response gemappt")
	void patchSuccess() throws ApiOperationException {
		final long idBenutzer = 42L;
		final var dto = buildEntity(1L, idBenutzer, 2L);
		final var request = buildPatchRequest();

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(idBenutzer);
		Mockito.when(wiedervorlageRepository.findByIdAndBenutzerId(1L, idBenutzer)).thenReturn(Optional.of(dto));
		Mockito.when(benutzergruppeRepository.findById(2L)).thenReturn(Optional.of(new DTOBenutzergruppe(2L, "x", true)));

		final var result = cut.patch(request, 1L);

		assertThat(result)
				.hasFieldOrPropertyWithValue("bemerkung", request.bemerkung.get())
				.hasFieldOrPropertyWithValue("automatischErledigt", request.automatischErledigt.get())
				.hasFieldOrPropertyWithValue("tsWiedervorlage", request.tsWiedervorlage.get())
				.hasFieldOrPropertyWithValue("idBenutzergruppe", request.idBenutzergruppe.get());
	}

	@Test
	@DisplayName("patch | FORBIDDEN bei GruppenID not in Eigene Gruppen IDs")
	void patchFehlendeBerechtigung() throws ApiOperationException {
		final long idBenutzer = 42L;
		final var dto = buildEntity(1L, idBenutzer, 2L);
		final var request = buildPatchRequest();

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(43L);
		Mockito.when(wiedervorlageRepository.findByIdAndBenutzerId(1L, 43L)).thenReturn(Optional.of(dto));
		Mockito.when(benutzergruppeRepository.findById(2L)).thenReturn(Optional.of(new DTOBenutzergruppe(2L, "x", true)));
		Mockito.when(benutzergruppenMitgliedRepository.hasGroupRights(43L, 2L)).thenReturn(false);

		Assertions.assertThatThrownBy(() -> cut.patch(request, 1L))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(Status.FORBIDDEN);
	}

	@Test
	@DisplayName("patch | Nicht-present Felder werden nicht ueberschrieben")
	void patchIgnoriertNichtPresenteFelder() throws ApiOperationException {
		final long idBenutzer = 42L;
		final var dto = buildEntity(1L, 42L, null);

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(idBenutzer);
		Mockito.when(wiedervorlageRepository.findByIdAndBenutzerId(1L, idBenutzer)).thenReturn(Optional.of(dto));

		final var result = cut.patch(new WiedervorlagePatchRequest(), 1L);

		assertThat(result)
				.hasFieldOrPropertyWithValue("bemerkung", dto.bemerkung)
				.hasFieldOrPropertyWithValue("automatischErledigt", dto.automatischErledigt)
				.hasFieldOrPropertyWithValue("tsWiedervorlage", dto.tsWiedervorlage);
	}

	@Test
	@DisplayName("patch | NOT_FOUND wenn kein Eintrag mit der ID existiert")
	void patchNotFound() {
		final long idBenutzer = 42L;
		final var request = new WiedervorlagePatchRequest();

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(idBenutzer);
		Mockito.when(wiedervorlageRepository.findByIdAndBenutzerId(99L, idBenutzer)).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> cut.patch(request, 99L))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(Status.NOT_FOUND);
	}

	@Test
	@DisplayName("delete | Eintrag wird geloescht und gemappte Response zurueckgegeben")
	void deleteSuccess() throws ApiOperationException {
		final long idBenutzer = 42L;
		final long idWiederVorlage = 1L;

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(idBenutzer);

		final var result = cut.delete(idWiederVorlage);

		assertThat(result)
				.isNotNull();
	}

	@Test
	@DisplayName("delete | Mehrere Eintraege werden per ID-Set geloescht")
	void deleteByIdsSuccess() throws ApiOperationException {
		final long successId = 1L;
		final long notFoundId = 2L;
		final var ids = Set.of(successId, notFoundId);
		final var idBenutzer = 1L;

		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(idBenutzer);
		Mockito.when(wiedervorlageRepository.findAllByIdsAndBenutzerId(ids, idBenutzer)).thenReturn(List.of(buildEntity(successId, idBenutzer, null)));

		final var logs = cut.delete(ids);

		assertThat(logs)
				.satisfiesExactlyInAnyOrder(
						l1 -> assertThat(l1)
								.isInstanceOf(SimpleOperationResponse.class)
								.hasFieldOrPropertyWithValue("id", successId)
								.hasFieldOrPropertyWithValue("success", true),
						l2 -> assertThat(l2)
								.isInstanceOf(SimpleOperationResponse.class)
								.hasFieldOrPropertyWithValue("id", notFoundId)
								.hasFieldOrPropertyWithValue("success", false)
				);
	}

	@Test
	@DisplayName("markiereAlsErledigt | tsErledigt und idBenutzerErledigt werden gesetzt")
	void markiereAlsErledigtSuccess() throws ApiOperationException {
		final long idBenutzer = 42L;
		final var dto = buildEntity(1L, idBenutzer, null);

		Mockito.when(wiedervorlageRepository.findByIdAndBenutzerId(1L, idBenutzer)).thenReturn(Optional.of(dto));
		Mockito.when(benutzerRepository.getAktuellerBenutzerId()).thenReturn(idBenutzer);

		final var result = cut.markiereAlsErledigt(1L);

		// Entity-Seiteneffekte
		assertThat(dto)
				.hasFieldOrPropertyWithValue("idBenutzerErledigt", idBenutzer)
				.satisfies(d -> assertThat(d.tsErledigt).isNotNull());

		// gemappte Response
		assertThat(result)
				.hasFieldOrPropertyWithValue("id", dto.id)
				.hasFieldOrPropertyWithValue("idBenutzerErledigt", idBenutzer)
				.satisfies(r -> assertThat(r.tsErledigt).isNotNull());
	}

	@Test
	@DisplayName("markiereAlsErledigt | NOT_FOUND wenn kein Eintrag mit der ID existiert")
	void markiereAlsErledigtNotFound() {

		Assertions.assertThatThrownBy(() -> cut.markiereAlsErledigt(99L))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(Status.NOT_FOUND);
	}


	private static DTOWiedervorlage buildEntity(final long id, final long benutzerId, final Long gruppeId) {
		final var dto = new DTOWiedervorlage(id, "Testbemerkung", false);
		dto.idBenutzer = benutzerId;
		dto.idBenutzergruppe = gruppeId;
		dto.bemerkung = "Testbemerkung";
		dto.automatischErledigt = false;
		dto.tsWiedervorlage = "2026-04-14 08:00:00";
		dto.tsAngelegt = "2026-01-01 00:00:00";
		dto.idBenutzerErledigt = null;
		dto.tsErledigt = null;
		return dto;
	}

	private static WiedervorlageCreateRequest buildCreateRequest() {
		return buildCreateRequest(null, null);
	}

	private static WiedervorlageCreateRequest buildCreateRequest(final Integer typPerson, final Long idPerson) {
		final var request = new WiedervorlageCreateRequest();
		request.bemerkung = "Testbemerkung";
		request.automatischErledigt = false;
		request.idBenutzergruppe = null;
		request.tsWiedervorlage = "2026-04-14 08:00:00";
		request.typPerson = typPerson;
		request.idPerson = idPerson;
		return request;
	}

	private static WiedervorlagePatchRequest buildPatchRequest() {
		final var request = new WiedervorlagePatchRequest();
		request.bemerkung = JsonNullable.of("Neue Bemerkung");
		request.automatischErledigt = JsonNullable.of(true);
		request.tsWiedervorlage = JsonNullable.of("2026-06-01 09:00:00");
		request.idBenutzergruppe = JsonNullable.of(2L);
		return request;
	}
}
