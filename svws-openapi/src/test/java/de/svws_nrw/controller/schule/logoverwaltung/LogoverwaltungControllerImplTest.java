package de.svws_nrw.controller.schule.logoverwaltung;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.svws_nrw.base.compression.Zip;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Logo;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.data.Responses;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.logoverwaltung.DataUrl;
import de.svws_nrw.service.schule.logoverwaltung.DataUrlResolver;
import de.svws_nrw.service.schule.logoverwaltung.LogoCreateRequest;
import de.svws_nrw.service.schule.logoverwaltung.LogoPatchRequest;
import de.svws_nrw.service.schule.logoverwaltung.LogoverwaltungService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LogoverwaltungController")
class LogoverwaltungControllerImplTest {

	@Mock
	private LogoverwaltungService service;

	@InjectMocks
	private LogoverwaltungControllerImpl controller;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private static Logo aLogo() {
		final var logo = new Logo();
		logo.id = 1L;
		logo.kennung = ReportingBildDefinition.DIN5008_BRIEFKOPF.getKennung();
		logo.logoBase64 = "data:image/png;base64,iVBORw0KGgo=";
		return logo;
	}

	private static Logo bLogo() {
		final var logo = new Logo();
		logo.id = 2L;
		logo.kennung = ReportingBildDefinition.SCHULLOGO_QUADRATISCH.getKennung();
		logo.logoBase64 = "data:image/png;base64,iVBORw0KGgo=";
		return logo;
	}

	private static LogoCreateRequest validCreateRequest() {
		final var request = new LogoCreateRequest();
		request.kennung = ReportingBildDefinition.DIN5008_BRIEFKOPF.getKennung();
		request.logoBase64 = "data:image/png;base64,iVBORw0KGgo=";
		return request;
	}

	private static LogoPatchRequest validPatchRequest() {
		final var request = new LogoPatchRequest();
		request.logoBase64 = JsonNullable.of("data:image/png;base64,iVBORw0KGgo=");
		return request;
	}

	private static LogoPatchRequest undefinedPatchRequest() {
		// logoBase64 bleibt JsonNullable.undefined() → kein Update gewünscht
		return new LogoPatchRequest();
	}

	private static SimpleOperationResponse successResponse(final long id) {
		return SimpleOperationResponse.ofSuccess(id);
	}

	private static SimpleOperationResponse errorResponse(final long id) {
		return SimpleOperationResponse.ofError(id, "Es wurde kein Logo mit der ID %d gefunden.".formatted(id));
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getAll()")
	class GetAll {

		@Test
		@DisplayName("gibt 200 OK mit Liste aller Logos zurück")
		void getAll_returnsOkWithLogoList() {
			final var logos = List.of(aLogo(), aLogo());
			when(service.getAll()).thenReturn(logos);

			try (MockedStatic<Responses> responses = mockStatic(Responses.class)) {
				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.ok(logos)).thenReturn(expectedResponse);

				final var result = controller.getAll();

				assertThat(result).isEqualTo(expectedResponse);
				verify(service).getAll();
				responses.verify(() -> Responses.ok(logos));
			}
		}

		@Test
		@DisplayName("gibt 200 OK mit leerer Liste zurück wenn keine Logos vorhanden")
		void getAll_returnsEmptyLogoList() {
			final List<Logo> logos = List.of();
			when(service.getAll()).thenReturn(logos);

			try (MockedStatic<Responses> responses = mockStatic(Responses.class)) {
				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.ok(logos)).thenReturn(expectedResponse);

				final var result = controller.getAll();

				assertThat(result).isEqualTo(expectedResponse);
				verify(service).getAll();
			}
		}
	}

	@Nested
	@DisplayName("getByIdsAsZip()")
	class GetByIdsAsZip {

		@Test
		void getByIdsAsZip_throwsBadRequest_whenIdsIsNull() {
			assertThatThrownBy(() -> controller.getByIdsAsZip(null))
					.isInstanceOf(ApiOperationException.class)
					.satisfies(ex -> {
						final var apiEx = (ApiOperationException) ex;
						assertThat(apiEx.getStatus()).isEqualTo(Response.Status.BAD_REQUEST);
						assertThat(apiEx.getMessage())
								.contains("mindestens eine Logo ID");
					});

			verifyNoInteractions(service);
		}

		@Test
		void getByIdsAsZip_throwsBadRequest_whenIdsIsEmpty() {
			assertThatThrownBy(() -> controller.getByIdsAsZip(Collections.emptyList()))
					.isInstanceOf(ApiOperationException.class)
					.satisfies(ex -> {
						final var apiEx = (ApiOperationException) ex;
						assertThat(apiEx.getStatus()).isEqualTo(Response.Status.BAD_REQUEST);
					});

			verifyNoInteractions(service);
		}

		// -------------------------------------------------------------------------
		// getByIdsAsZip – Happy Path
		// -------------------------------------------------------------------------

		@Test
		void getByIdsAsZip_returnsZip_forSingleLogo() {
			final var logo = aLogo();
			when(service.getByIds(List.of(1L))).thenReturn(List.of(logo));

			try (MockedStatic<Zip> zipMock = mockStatic(Zip.class)) {
				final byte[] expectedZip = new byte[] { 0x50, 0x4B, 0x03, 0x04 }; // PK-Header
				zipMock.when(() -> Zip.createArchive(anyMap())).thenReturn(expectedZip);

				final Response response = controller.getByIdsAsZip(List.of(1L));

				assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
				assertThat(response.getEntity()).isEqualTo(expectedZip);
				assertThat(response.getHeaderString("Content-Disposition"))
						.isEqualTo("attachment; filename=\"logos.zip\"");
				assertThat(response.getHeaderString("Content-Length"))
						.isEqualTo(String.valueOf(expectedZip.length));
			}
		}

		@Test
		void getByIdsAsZip_returnsZip_forMultipleLogos() {
			final var logo = aLogo();
			final var logo2 = bLogo();

			when(service.getByIds(List.of(1L, 2L))).thenReturn(List.of(logo, logo2));

			try (MockedStatic<Zip> zipMock = mockStatic(Zip.class)) {
				final byte[] expectedZip = new byte[] { 0x50, 0x4B, 0x05, 0x06 };
				zipMock.when(() -> Zip.createArchive(anyMap())).thenReturn(expectedZip);

				final Response response = controller.getByIdsAsZip(List.of(1L, 2L));

				zipMock.verify(() -> Zip.createArchive(argThat(map ->
						map.containsKey(ReportingBildDefinition.DIN5008_BRIEFKOPF.getKennung() + ".png")
								&& map.containsKey(ReportingBildDefinition.SCHULLOGO_QUADRATISCH.getKennung() + ".png")
				)));

				assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			}
		}

		@Test
		void getByIdsAsZip_usesKennungWithoutExtension_whenFileExtensionIsNull() {
			// Base64 ohne erkennbaren MIME-Type → kein fileExtension
			final var logo = aLogo();
			logo.logoBase64 = "data:application/octet-stream;base64,AAAA";

			when(service.getByIds(List.of(1L))).thenReturn(List.of(logo));

			try (MockedStatic<DataUrlResolver> resolverMock = mockStatic(DataUrlResolver.class);
					MockedStatic<Zip> zipMock = mockStatic(Zip.class)) {

				final var dataUrl = mock(DataUrl.class);
				when(dataUrl.fileExtension()).thenReturn(null);
				when(dataUrl.payload()).thenReturn("AAAA");

				resolverMock.when(() -> DataUrlResolver.resolve(logo.logoBase64))
						.thenReturn(Optional.of(dataUrl));

				zipMock.when(() -> Zip.createArchive(anyMap())).thenReturn(new byte[0]);

				controller.getByIdsAsZip(List.of(1L));

				zipMock.verify(() -> Zip.createArchive(argThat(map ->
						map.containsKey(ReportingBildDefinition.DIN5008_BRIEFKOPF.getKennung())
								&& !map.containsKey(ReportingBildDefinition.DIN5008_BRIEFKOPF.getKennung() + ".") // kein Punkt
				)));
			}
		}

		// -------------------------------------------------------------------------
		// getByIdsAsZip – Fehlerfall: Base64 nicht parsebar
		// -------------------------------------------------------------------------

		@Test
		void getByIdsAsZip_throwsInternalServerError_whenBase64CannotBeResolved() {
			final var logo = aLogo();
			logo.logoBase64 = "kein-gueltiges-data-url";

			final var ids = List.of(1L);
			when(service.getByIds(ids)).thenReturn(List.of(logo));

			try (MockedStatic<DataUrlResolver> resolverMock = mockStatic(DataUrlResolver.class)) {
				resolverMock.when(() -> DataUrlResolver.resolve(logo.logoBase64))
						.thenReturn(Optional.empty());

				assertThatThrownBy(() -> controller.getByIdsAsZip(ids))
						.isInstanceOf(ApiOperationException.class)
						.satisfies(ex -> {
							final var apiEx = (ApiOperationException) ex;
							assertThat(apiEx.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR);
							assertThat(apiEx.getMessage()).contains("1"); // Logo-ID im Text
						});
			}
		}

		// -------------------------------------------------------------------------
		// getByIdsAsZip – Service gibt leere Liste zurück
		// -------------------------------------------------------------------------

		@Test
		void getByIdsAsZip_returnsEmptyZip_whenServiceReturnsNoLogos() {
			when(service.getByIds(List.of(99L))).thenReturn(Collections.emptyList());

			try (MockedStatic<Zip> zipMock = mockStatic(Zip.class)) {
				final byte[] emptyZip = new byte[22]; // leeres ZIP-Archiv hat 22 Byte
				zipMock.when(() -> Zip.createArchive(anyMap())).thenReturn(emptyZip);

				final Response response = controller.getByIdsAsZip(List.of(99L));

				zipMock.verify(() -> Zip.createArchive(argThat(Map::isEmpty)));
				assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
			}
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create()")
	class Create {

		@Test
		@DisplayName("validiert den Request und gibt 201 Created mit dem erstellten Logo zurück")
		void create_validRequest_returnsCreatedLogo() {
			final var createRequest = validCreateRequest();
			final var created = aLogo();
			when(service.create(createRequest)).thenReturn(created);

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class);
					MockedStatic<Responses> responses = mockStatic(Responses.class)) {

				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.created(created)).thenReturn(expectedResponse);

				final var result = controller.create(createRequest);

				assertThat(result).isEqualTo(expectedResponse);
				validator.verify(() -> BeanValidator.validate(createRequest));
				verify(service).create(createRequest);
				responses.verify(() -> Responses.created(created));
			}
		}

		@Test
		@DisplayName("wirft ValidationException wenn kennung blank ist — Service wird nicht aufgerufen")
		void create_blankKennung_throwsValidationException() {
			final var createRequest = validCreateRequest();
			createRequest.kennung = "  ";

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class)) {
				validator.when(() -> BeanValidator.validate(createRequest))
						.thenThrow(new ValidationException("kennung darf nicht blank sein"));

				assertThatThrownBy(() -> controller.create(createRequest))
						.isInstanceOf(ValidationException.class)
						.hasMessageContaining("kennung darf nicht blank sein");

				verify(service, never()).create(any());
			}
		}

		@Test
		@DisplayName("wirft ValidationException wenn kennung null ist — Service wird nicht aufgerufen")
		void create_nullKennung_throwsValidationException() {
			final var createRequest = validCreateRequest();
			createRequest.kennung = null;

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class)) {
				validator.when(() -> BeanValidator.validate(createRequest))
						.thenThrow(new ValidationException("kennung darf nicht null sein"));

				assertThatThrownBy(() -> controller.create(createRequest))
						.isInstanceOf(ValidationException.class);

				verify(service, never()).create(any());
			}
		}

		@Test
		@DisplayName("wirft ValidationException wenn kennung länger als 100 Zeichen ist — Service wird nicht aufgerufen")
		void create_kennungTooLong_throwsValidationException() {
			final var createRequest = validCreateRequest();
			createRequest.kennung = "A".repeat(101);

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class)) {
				validator.when(() -> BeanValidator.validate(createRequest))
						.thenThrow(new ValidationException("kennung darf maximal 100 Zeichen haben"));

				assertThatThrownBy(() -> controller.create(createRequest))
						.isInstanceOf(ValidationException.class);

				verify(service, never()).create(any());
			}
		}

		@Test
		@DisplayName("wirft ValidationException wenn logoBase64 blank ist — Service wird nicht aufgerufen")
		void create_blankLogoBase64_throwsValidationException() {
			final var createRequest = validCreateRequest();
			createRequest.logoBase64 = "  ";

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class)) {
				validator.when(() -> BeanValidator.validate(createRequest))
						.thenThrow(new ValidationException("logoBase64 darf nicht blank sein"));

				assertThatThrownBy(() -> controller.create(createRequest))
						.isInstanceOf(ValidationException.class);

				verify(service, never()).create(any());
			}
		}

		@Test
		@DisplayName("wirft ValidationException wenn logoBase64 null ist — Service wird nicht aufgerufen")
		void create_nullLogoBase64_throwsValidationException() {
			final var createRequest = validCreateRequest();
			createRequest.logoBase64 = null;

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class)) {
				validator.when(() -> BeanValidator.validate(createRequest))
						.thenThrow(new ValidationException("logoBase64 darf nicht null sein"));

				assertThatThrownBy(() -> controller.create(createRequest))
						.isInstanceOf(ValidationException.class);

				verify(service, never()).create(any());
			}
		}

		@Test
		@DisplayName("hinzugefuegtAm wird automatisch auf das heutige Datum gesetzt")
		void create_hinzugefuegtAmIsSetAutomatically() {
			final var createRequest = new LogoCreateRequest();
			assertThat(createRequest.hinzugefuegtAm).isNotNull().isNotBlank();
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch()")
	class Patch {

		private static final long ID = 42L;

		@Test
		@DisplayName("validiert den Request und gibt 200 OK mit dem gepatchten Logo zurück")
		void patch_validRequest_returnsUpdatedLogo() {
			final var patchRequest = validPatchRequest();
			final var patched = aLogo();
			when(service.patch(ID, patchRequest)).thenReturn(patched);

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class);
					MockedStatic<Responses> responses = mockStatic(Responses.class)) {

				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.ok(patched)).thenReturn(expectedResponse);

				final var result = controller.patch(ID, patchRequest);

				assertThat(result).isEqualTo(expectedResponse);
				validator.verify(() -> BeanValidator.validate(patchRequest));
				verify(service).patch(ID, patchRequest);
				responses.verify(() -> Responses.ok(patched));
			}
		}

		@Test
		@DisplayName("akzeptiert JsonNullable.undefined() — kein Patch, aber kein Fehler")
		void patch_undefinedLogoBase64_isValid() {
			final var patchRequest = undefinedPatchRequest();
			assertThat(patchRequest.logoBase64.isPresent()).isFalse();

			final var patched = aLogo();
			when(service.patch(ID, patchRequest)).thenReturn(patched);

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class);
					MockedStatic<Responses> responses = mockStatic(Responses.class)) {

				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.ok(patched)).thenReturn(expectedResponse);

				final var result = controller.patch(ID, patchRequest);

				assertThat(result).isEqualTo(expectedResponse);
				validator.verify(() -> BeanValidator.validate(patchRequest));
				verify(service).patch(ID, patchRequest);
			}
		}

		@Test
		@DisplayName("wirft ValidationException wenn logoBase64 blank ist — Service wird nicht aufgerufen")
		void patch_blankLogoBase64_throwsValidationException() {
			final var patchRequest = new LogoPatchRequest();
			patchRequest.logoBase64 = JsonNullable.of("  ");

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class)) {
				validator.when(() -> BeanValidator.validate(patchRequest))
						.thenThrow(new ValidationException("logoBase64 darf nicht blank sein"));

				assertThatThrownBy(() -> controller.patch(ID, patchRequest))
						.isInstanceOf(ValidationException.class)
						.hasMessageContaining("logoBase64 darf nicht blank sein");

				verify(service, never()).patch(anyLong(), any());
			}
		}

		@Test
		@DisplayName("wirft ValidationException wenn logoBase64 leer ist — Service wird nicht aufgerufen")
		void patch_emptyLogoBase64_throwsValidationException() {
			final var patchRequest = new LogoPatchRequest();
			patchRequest.logoBase64 = JsonNullable.of("");

			try (MockedStatic<BeanValidator> validator = mockStatic(BeanValidator.class)) {
				validator.when(() -> BeanValidator.validate(patchRequest))
						.thenThrow(new ValidationException("logoBase64 darf nicht blank sein"));

				assertThatThrownBy(() -> controller.patch(ID, patchRequest))
						.isInstanceOf(ValidationException.class);

				verify(service, never()).patch(anyLong(), any());
			}
		}
	}

	// -------------------------------------------------------------------------
	// delete(Long)
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("delete(Long)")
	class DeleteSingle {

		private static final Long ID = 1L;

		@Test
		@DisplayName("löscht ein vorhandenes Logo und gibt SimpleOperationResponse mit Erfolg zurück")
		void delete_existingId_returnsSuccessResponse() {
			final var deleteResult = successResponse(ID);
			when(service.delete(ID)).thenReturn(deleteResult);

			try (MockedStatic<Responses> responses = mockStatic(Responses.class)) {
				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.ok(deleteResult)).thenReturn(expectedResponse);

				final var result = controller.delete(ID);

				assertThat(result).isEqualTo(expectedResponse);
				verify(service).delete(ID);
				responses.verify(() -> Responses.ok(deleteResult));
			}
		}

		@Test
		@DisplayName("gibt SimpleOperationResponse mit Fehler zurück wenn Logo nicht gefunden")
		void delete_nonExistingId_returnsErrorResponse() {
			final var deleteResult = errorResponse(ID);
			when(service.delete(ID)).thenReturn(deleteResult);

			try (MockedStatic<Responses> responses = mockStatic(Responses.class)) {
				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.ok(deleteResult)).thenReturn(expectedResponse);

				final var result = controller.delete(ID);

				assertThat(result).isEqualTo(expectedResponse);
				verify(service).delete(ID);
			}
		}

		@Test
		@DisplayName("delegiert die ID korrekt an den Service")
		void delete_delegatesIdToService() {
			controller.delete(ID);
			verify(service).delete(ID);
		}
	}

	// -------------------------------------------------------------------------
	// delete(List<Long>)
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("delete(List<Long>)")
	class DeleteMultiple {

		@Test
		@DisplayName("löscht vorhandene Logos und gibt Liste mit SimpleOperationResponses zurück")
		void delete_existingIds_returnsSuccessResponses() {
			final var ids = List.of(1L, 2L, 3L);
			final var deleteResults = List.of(
					successResponse(1L),
					successResponse(2L),
					successResponse(3L)
			);
			when(service.delete(ids)).thenReturn(deleteResults);

			try (MockedStatic<Responses> responses = mockStatic(Responses.class)) {
				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.ok(deleteResults)).thenReturn(expectedResponse);

				final var result = controller.delete(ids);

				assertThat(result).isEqualTo(expectedResponse);
				verify(service).delete(ids);
				responses.verify(() -> Responses.ok(deleteResults));
			}
		}

		@Test
		@DisplayName("gibt gemischte Erfolg/Fehler-Responses zurück wenn nur ein Teil der IDs existiert")
		void delete_partiallyExistingIds_returnsMixedResponses() {
			final var ids = List.of(1L, 99L);
			final var deleteResults = List.of(
					successResponse(1L),
					errorResponse(99L)
			);
			when(service.delete(ids)).thenReturn(deleteResults);

			try (MockedStatic<Responses> responses = mockStatic(Responses.class)) {
				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.ok(deleteResults)).thenReturn(expectedResponse);

				final var result = controller.delete(ids);

				assertThat(result).isEqualTo(expectedResponse);
				verify(service).delete(ids);
			}
		}

		@Test
		@DisplayName("gibt 200 OK mit leerer Liste zurück wenn keine IDs übergeben")
		void delete_emptyIds_returnsEmptyResponse() {
			final List<Long> ids = List.of();
			final List<SimpleOperationResponse> deleteResults = List.of();
			when(service.delete(ids)).thenReturn(deleteResults);

			try (MockedStatic<Responses> responses = mockStatic(Responses.class)) {
				final var expectedResponse = mock(Response.class);
				responses.when(() -> Responses.ok(deleteResults)).thenReturn(expectedResponse);

				final var result = controller.delete(ids);

				assertThat(result).isEqualTo(expectedResponse);
				verify(service).delete(ids);
			}
		}

		@Test
		@DisplayName("delegiert die IDs korrekt an den Service")
		void delete_delegatesIdsToService() {
			final var ids = List.of(10L, 20L);
			controller.delete(ids);
			verify(service).delete(ids);
		}
	}
}
