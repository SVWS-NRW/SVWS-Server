package de.svws_nrw.service.schule.logoverwaltung;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Logo;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.logoverwaltung.LogoverwaltungMapper;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepository;
import de.svws_nrw.service.schule.SchuleService;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogoverwaltungServiceTest {

	@Mock
	private LogoverwaltungRepository repository;

	@Mock
	private LogoverwaltungMapper mapper;

	@Mock
	private SchuleService schuleService;

	@InjectMocks
	private LogoverwaltungService service;

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

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	// ------------------------------------------------------------------
	// Hilfsmethoden
	// ------------------------------------------------------------------

	private static DTOLogo buildDtoLogo(final long id, final ReportingBildDefinition kennung, final String base64, final String hinzugefuegtAm) {
		return new DTOLogo(id, kennung, base64, hinzugefuegtAm);
	}

	private static Logo buildLogo(final long id) {
		final Logo logo = new Logo();
		logo.id = id;
		return logo;
	}

	// ------------------------------------------------------------------
	// findById
	// ------------------------------------------------------------------

	@Nested
	class GetById {

		@Test
		void gibtLogoZurueck_wennIdExistiert() {
			final DTOLogo entity = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, "base64data", "2024-01-01");
			final Logo expected = buildLogo(1L);

			when(repository.findById(1L)).thenReturn(Optional.of(entity));
			when(mapper.toApi(entity)).thenReturn(expected);

			final Logo result = service.getById(1L);

			assertThat(result).isEqualTo(expected);
			verify(repository).findById(1L);
			verify(mapper).toApi(entity);
		}

		@Test
		void wirftApiOperationException_wennIDNichtExistiert() {
			assertThatThrownBy(() -> service.getById(1L))
					.isInstanceOf(ApiOperationException.class)
					.asInstanceOf(InstanceOfAssertFactories.type(ApiOperationException.class))
					.extracting(ApiOperationException::getStatus, ApiOperationException::getMessage)
					.containsExactly(Response.Status.NOT_FOUND, "Es wurde kein Logo mit der ID %d gefunden.".formatted(1L));
		}
	}

	// ------------------------------------------------------------------
	// getAll
	// ------------------------------------------------------------------

	@Nested
	class GetAll {

		@Test
		void gibtLeereListeZurueck_wennKeineLogosVorhanden() {
			when(repository.getAll()).thenReturn(List.of());

			final List<Logo> result = service.getAll();

			assertThat(result).isEmpty();
		}

		@Test
		void gibtAlleMappedLogosZurueck() {
			final DTOLogo dto1 = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, "base64a", "2024-01-01");
			final DTOLogo dto2 = buildDtoLogo(2L, ReportingBildDefinition.SCHULLOGO_QUADRATISCH, "base64b", "2024-01-02");
			final Logo logo1 = buildLogo(1L);
			final Logo logo2 = buildLogo(2L);

			when(repository.getAll()).thenReturn(List.of(dto1, dto2));
			when(mapper.toApi(dto1)).thenReturn(logo1);
			when(mapper.toApi(dto2)).thenReturn(logo2);

			final List<Logo> result = service.getAll();

			assertThat(result).containsExactly(logo1, logo2);
		}
	}

	// ------------------------------------------------------------------
	// create
	// ------------------------------------------------------------------

	@Nested
	class Create {

		/**
		 * Schulform null → isSchulformGueltig() gibt true zurück (schulform == null),
		 * d.h. alle Bilddefinitionen sind gültig ohne Schulform-Filterung.
		 */
		@Test
		void erstelltLogo_beiGueltigerKennung() {
			final LogoCreateRequest request = new LogoCreateRequest();
			request.kennung = ReportingBildDefinition.SCHULLOGO_SCHILD.getKennung();
			request.logoBase64 = "validBase64";

			final DTOLogo mappedEntity = buildDtoLogo(0L, ReportingBildDefinition.SCHULLOGO_SCHILD, request.logoBase64, "2024-01-01");
			final DTOLogo savedEntity = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, request.logoBase64, "2024-01-01");
			final Logo expected = buildLogo(1L);

			when(schuleService.getSchulform()).thenReturn(null);
			when(mapper.toDomain(request)).thenReturn(mappedEntity);
			when(repository.create(mappedEntity)).thenReturn(savedEntity);
			when(mapper.toApi(savedEntity)).thenReturn(expected);


			final Logo result = service.create(request);

			assertThat(result).isEqualTo(expected);
			verify(repository).create(mappedEntity);

		}

		@ParameterizedTest
		@EnumSource(ReportingBildDefinition.class)
		void erstelltLogo_fuerJedeGueltigeBildDefinition(final ReportingBildDefinition bildDefinition) {
			final LogoCreateRequest request = new LogoCreateRequest();
			request.kennung = bildDefinition.getKennung();
			request.logoBase64 = "validBase64";

			final DTOLogo mappedEntity = buildDtoLogo(0L, bildDefinition, request.logoBase64, "2024-01-01");
			final DTOLogo savedEntity = buildDtoLogo(1L, bildDefinition, request.logoBase64, "2024-01-01");
			final Logo expected = buildLogo(1L);

			when(schuleService.getSchulform()).thenReturn(null);
			when(mapper.toDomain(request)).thenReturn(mappedEntity);
			when(repository.create(mappedEntity)).thenReturn(savedEntity);
			when(mapper.toApi(savedEntity)).thenReturn(expected);

			assertThat(service.create(request)).isEqualTo(expected);
		}

		@Test
		void wirftApiOperationException_beiVoelligUngueltigerKennung() {
			final LogoCreateRequest request = new LogoCreateRequest();
			request.kennung = "KENNUNG_EXISTIERT_NICHT";
			request.logoBase64 = "validBase64";

			when(schuleService.getSchulform()).thenReturn(null);


			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.satisfies(ex -> assertThat(((ApiOperationException) ex).getStatus())
							.isEqualTo(Response.Status.BAD_REQUEST));

		}

		@Test
		void wirftApiOperationException_wennKennungNichtFuerSchulformGueltig() {
			// Wenn du eine Bilddefinition mit eingeschränkter Schulform einführst,
			// kann hier eine inkompatible Schulform getestet werden.
			// Aktuell testen wir mit einer komplett ungültigen Kennung + konkreter Schulform.
			final LogoCreateRequest request = new LogoCreateRequest();
			request.kennung = "UNGUELTIG";
			request.logoBase64 = "validBase64";

			when(schuleService.getSchulform()).thenReturn(Schulform.GY);


			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.satisfies(ex -> assertThat(((ApiOperationException) ex).getStatus())
							.isEqualTo(Response.Status.BAD_REQUEST));
		}

		@Test
		void wirftApiOperationException_wennKennungBereitsExistiert() {
			final LogoCreateRequest request = new LogoCreateRequest();
			request.kennung = ReportingBildDefinition.DIN5008_BRIEFKOPF.getKennung();
			request.logoBase64 = "validBase64";

			when(repository.existsByKennung(ReportingBildDefinition.DIN5008_BRIEFKOPF)).thenReturn(true);
			when(schuleService.getSchulform()).thenReturn(Schulform.GY);

			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.asInstanceOf(InstanceOfAssertFactories.type(ApiOperationException.class))
					.extracting(ApiOperationException::getStatus, ApiOperationException::getMessage)
					.containsExactly(Response.Status.BAD_REQUEST,
							"Es existiert bereits ein Logo mit der Kennung %s. Es kann kein weiteres Logo mit gleicher Kennung hinterlegt werden."
									.formatted(ReportingBildDefinition.DIN5008_BRIEFKOPF));
		}
	}

// ------------------------------------------------------------------
// patch
// ------------------------------------------------------------------

	@Nested
	class Patch {

		@Test
		void aktualisiertLogo_beiGueltigemRequest() {
			final LogoPatchRequest request = new LogoPatchRequest();
			request.logoBase64 = JsonNullable.of("newBase64");

			final DTOLogo existingEntity = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, "newBase64", "2024-01-01");
			final DTOLogo updatedEntity = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, "newBase64", "2024-01-01");
			final Logo expected = buildLogo(1L);

			when(repository.findById(1L)).thenReturn(Optional.of(existingEntity));
			when(repository.update(existingEntity)).thenReturn(updatedEntity);
			when(mapper.toApi(updatedEntity)).thenReturn(expected);


			final Logo result = service.patch(1L, request);

			assertThat(result).isEqualTo(expected);
			verify(repository).update(existingEntity);

		}

		@Test
		void setzt_hinzugefuegtAm_wennLogoBase64SichAendert() {
			final LogoPatchRequest request = new LogoPatchRequest();
			request.logoBase64 = JsonNullable.of("ANDERES_BASE64"); // unterscheidet sich vom Entity-Wert

			// Entity hat altes Base64 → isLogoDifferent() wird true
			final DTOLogo existingEntity = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, "ALTES_BASE64", "2024-01-01");
			final String hinzugefuegtAmVorher = existingEntity.hinzugefuegtAm;

			when(repository.findById(1L)).thenReturn(Optional.of(existingEntity));
			when(repository.update(any(DTOLogo.class))).thenReturn(existingEntity);
			when(mapper.toApi(any())).thenReturn(buildLogo(1L));


			service.patch(1L, request);

			// hinzugefuegtAm wurde auf das aktuelle Datum aktualisiert (nicht mehr der alte Wert)
			assertThat(existingEntity.hinzugefuegtAm)
					.isNotEmpty()
					.isNotEqualTo(hinzugefuegtAmVorher);
		}

		@Test
		void setzt_hinzugefuegtAm_nicht_wennLogoBase64Unveraendert() {
			final String gleichesBase64 = "GLEICHES_BASE64";
			final LogoPatchRequest request = new LogoPatchRequest();
			request.logoBase64 = JsonNullable.of(gleichesBase64);

			// Entity hat dasselbe Base64 → isLogoDifferent() wird false
			final DTOLogo existingEntity = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, gleichesBase64, "2024-01-01");
			final String hinzugefuegtAmVorher = existingEntity.hinzugefuegtAm;

			when(repository.findById(1L)).thenReturn(Optional.of(existingEntity));
			when(repository.update(any(DTOLogo.class))).thenReturn(existingEntity);
			when(mapper.toApi(any())).thenReturn(buildLogo(1L));


			service.patch(1L, request);

			// hinzugefuegtAm bleibt unverändert
			assertThat(existingEntity.hinzugefuegtAm).isEqualTo(hinzugefuegtAmVorher);
		}

		@Test
		void wirftApiOperationException_wennIDNichtExistiert() {
			final LogoPatchRequest request = new LogoPatchRequest();
			request.logoBase64 = JsonNullable.of("test");

			assertThatThrownBy(() -> service.patch(1L, request))
					.isInstanceOf(ApiOperationException.class)
					.asInstanceOf(InstanceOfAssertFactories.type(ApiOperationException.class))
					.extracting(ApiOperationException::getStatus, ApiOperationException::getMessage)
					.containsExactly(Response.Status.NOT_FOUND, "Es wurde kein Logo mit der ID %d gefunden.".formatted(1L));
		}
	}

// ------------------------------------------------------------------
// delete (single)
// ------------------------------------------------------------------

	@Nested
	class DeleteSingle {

		@Test
		void gibtSuccessResponse_wennLogoGefunden() {
			final DTOLogo entity = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, "base64", "2024-01-01");

			when(repository.findById(1L)).thenReturn(Optional.of(entity));
			when(repository.delete(entity)).thenReturn(entity);


			final SimpleOperationResponse response = service.delete(1L);

			assertThat(response.success).isTrue();
			assertThat(response.id).isEqualTo(1L);

		}

		@Test
		void gibtErrorResponse_wennLogoNichtGefunden() {
			when(repository.findById(99L)).thenReturn(Optional.empty());


			final SimpleOperationResponse response = service.delete(99L);

			assertThat(response.success).isFalse();
			assertThat(response.id).isEqualTo(99L);

		}
	}

// ------------------------------------------------------------------
// delete (bulk)
// ------------------------------------------------------------------

	@Nested
	class DeleteBulk {

		@Test
		void loeschtAlleGefundenenLogos_undMeldetNichtGefundene() {
			final DTOLogo entity1 = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, "base64a", "2024-01-01");
			final DTOLogo entity2 = buildDtoLogo(2L, ReportingBildDefinition.SCHULLOGO_QUADRATISCH, "base64b", "2024-01-02");
			// ID 3 existiert nicht in der DB

			when(repository.findListByIds(List.of(1L, 2L, 3L))).thenReturn(List.of(entity1, entity2));
			when(repository.delete(List.of(entity1, entity2))).thenReturn(List.of(entity1, entity2));


			final List<SimpleOperationResponse> responses = service.delete(List.of(1L, 2L, 3L));

			assertThat(responses).hasSize(3);

			final long successCount = responses.stream().filter(r -> r.success).count();
			final long errorCount = responses.stream().filter(r -> !r.success).count();
			assertThat(successCount).isEqualTo(2);
			assertThat(errorCount).isEqualTo(1);

			// Fehler-Response bezieht sich auf die nicht gefundene ID 3
			final SimpleOperationResponse errorResponse = responses.stream()
					.filter(r -> !r.success)
					.findFirst().orElseThrow();
			assertThat(errorResponse.id).isEqualTo(3L);

		}

		@Test
		void gibtNurErrorResponses_wennKeineIdGefunden() {
			when(repository.findListByIds(List.of(10L, 20L))).thenReturn(List.of());
			when(repository.delete(List.of())).thenReturn(List.of());


			final List<SimpleOperationResponse> responses = service.delete(List.of(10L, 20L));

			assertThat(responses)
					.hasSize(2)
					.allMatch(r -> !r.success);
		}


		@Test
		void gibtLeereListeZurueck_beiLeererIdListe() {
			when(repository.findListByIds(List.of())).thenReturn(List.of());
			when(repository.delete(List.of())).thenReturn(List.of());


			final List<SimpleOperationResponse> responses = service.delete(List.of());

			assertThat(responses).isEmpty();

		}

		@Test
		void successResponsesKommenVorErrorResponses() {
			// Laut Service: Stream.concat(deletedResponses, notFoundResponses)
			// → gelöschte Einträge kommen immer zuerst
			final DTOLogo entity1 = buildDtoLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, "base64a", "2024-01-01");

			when(repository.findListByIds(List.of(1L, 99L))).thenReturn(List.of(entity1));
			when(repository.delete(List.of(entity1))).thenReturn(List.of(entity1));


			final List<SimpleOperationResponse> responses = service.delete(List.of(1L, 99L));

			assertThat(responses).hasSize(2);
			assertThat(responses.get(0).success).isTrue();   // deleted zuerst
			assertThat(responses.get(1).success).isFalse();  // notFound danach


		}
	}
}
