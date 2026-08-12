package de.svws_nrw.service.signature;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;
import de.svws_nrw.service.schule.EigeneSchuleService;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignatureServiceImplTest {

	private SignatureServiceImpl cut;

	private OAuthHttpClientImplMock httpClient;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	@Mock
	private BenutzerKompetenzService benutzerKompetenzService;

	private static final Map<Long, Pair<String, String>> BASE64_ENCODINGS = Map.of(
			1L, Pair.of("Y21zLW9r", "cms-ok"),
			2L, Pair.of("Y21zMi1vaw==", "cms2-ok"),
			3L, Pair.of("Y21zw7bDpMO8LW9r", "cmsöäü-ok"),
			4L, Pair.of("Y21zLWVycm9y", "cms-error")
	);

	private static final URI SIGNATURE_SERVICE_URI = URI.create("https://test.signature-service.de/sign");

	@BeforeEach
	void setUp() {
		httpClient = new OAuthHttpClientImplMock();
		cut = new SignatureServiceImpl(httpClient, eigeneSchuleService, benutzerKompetenzService, new ObjectMapper(), SIGNATURE_SERVICE_URI);
		when(benutzerKompetenzService.hatBenutzerKompetenz(BenutzerKompetenz.EXTRAS_DIGITALE_SIGNATUREN_AUSSTELLEN)).thenReturn(true);
	}

	// -------------------------------------------------------------------------
	// Erfolgreiche Signierung
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("Erfolgreiche Signierung (HTTP 200)")
	class Success {

		@Test
		@DisplayName("sign() gibt korrekte Signaturen zurück bei Status 200")
		void sign_shouldReturnSignatures_whenResponseIsSuccessful() {
			httpClient.respondWithSuccess(successResponse(List.of(
					okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft()),
					okResponse(2L, BASE64_ENCODINGS.get(2L).getLeft())
			)));

			final Map<Object, Signature> result = cut.sign(Map.of(
					1L, "payload1".getBytes(),
					2L, "payload2".getBytes()
			));

			assertThat(result)
					.hasSize(2)
					.containsValues(
							new Signature(BASE64_ENCODINGS.get(1L).getRight().getBytes(StandardCharsets.UTF_8), SignatureStatus.OK, null),
							new Signature(BASE64_ENCODINGS.get(2L).getRight().getBytes(StandardCharsets.UTF_8), SignatureStatus.OK, null)
					);
		}

		@Test
		@DisplayName("sign() gibt leere Map zurück bei leerem Payload")
		void sign_shouldReturnEmptyMap_whenPayloadIsEmpty() {
			httpClient.respondWithSuccess(successResponse(List.of()));

			final Map<Object, Signature> result = cut.sign(Map.of());

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("sign() setzt Status OK korrekt")
		void sign_shouldSetStatusOk() {
			httpClient.respondWithSuccess(successResponse(List.of(okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft()))));

			final Signature signature = cut.sign(Map.of(1L, "payload".getBytes())).get(1L);

			assertThat(signature.status()).isEqualTo(SignatureStatus.OK);
		}

		@Test
		@DisplayName("sign() setzt errorMessage=null bei Status OK")
		void sign_shouldSetEmptyErrorMessage_whenStatusIsOk() {
			httpClient.respondWithSuccess(successResponse(List.of(okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft()))));

			final Signature signature = cut.sign(Map.of(1L, "payload".getBytes())).get(1L);

			assertThat(signature.errorMessage()).isNull();
		}

		@Test
		@DisplayName("sign() setzt Status ERROR und errorMessage korrekt")
		void sign_shouldSetErrorMessageAndStatus_whenStatusIsError() {
			httpClient.respondWithSuccess(successResponse(List.of(errorResponse(1L))));

			final Signature signature = cut.sign(Map.of(1L, "payload".getBytes())).get(1L);

			assertThat(signature).extracting(Signature::content, Signature::status, Signature::errorMessage)
							.containsExactly(null, SignatureStatus.ERROR, "Fehler");
		}

		@Test
		@DisplayName("sign() setzt Status UNKNOWN bei unbekanntem Signaturstatus")
		void sign_shouldSetUnknownStatus_whenStatusIsUnrecognized() {
			httpClient.respondWithSuccess(successResponse(List.of(
					new SignResponse(1L, "hash", BASE64_ENCODINGS.get(1L).getLeft(), "INVALID_STATUS")
			)));

			final Signature signature = cut.sign(Map.of(1L, "payload".getBytes())).get(1L);

			assertThat(signature.status()).isEqualTo(SignatureStatus.UNKNOWN);
		}

		@Test
		@DisplayName("sign() kodiert CMS-Content korrekt als UTF-8")
		void sign_shouldEncodeContentAsUtf8() {
			httpClient.respondWithSuccess(successResponse(List.of(okResponse(1L, BASE64_ENCODINGS.get(3L).getLeft()))));

			final Signature signature = cut.sign(Map.of(1L, "payload".getBytes())).get(1L);

			assertThat(signature.content())
					.isEqualTo(BASE64_ENCODINGS.get(3L).getRight().getBytes(StandardCharsets.UTF_8));
		}

		@Test
		@DisplayName("sign() verarbeitet mehrere Payloads mit gemischten Stati korrekt")
		void sign_shouldHandleMixedStatuses() {
			cut = spy(cut);
			httpClient.respondWithSuccess(successResponse(List.of(
					okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft()),
					errorResponse(2L),
					new SignResponse(3L, "hash", BASE64_ENCODINGS.get(2L).getLeft(), "INVALID_STATUS")
			)));

			final Map<Object, Signature> result = cut.sign(Map.of(
					1L, "p1".getBytes(),
					2L, "p2".getBytes(),
					3L, "p3".getBytes()
			));

			assertThat(result)
					.hasSize(3)
					.extractingFromEntries(e -> e.getValue().status())
					.containsExactlyInAnyOrder(SignatureStatus.OK, SignatureStatus.ERROR, SignatureStatus.UNKNOWN);
		}
	}

	// -------------------------------------------------------------------------
	// Fehlerhafte Signierung
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("Fehler beim Signieren")
	class Errors {

		@Test
		@DisplayName("sign() wirft ApiOperationException bei fehlender Berechtigung")
		void sign_shouldThrowException_whenUserHasNoPermission() {
			when(benutzerKompetenzService.hatBenutzerKompetenz(BenutzerKompetenz.EXTRAS_DIGITALE_SIGNATUREN_AUSSTELLEN)).thenReturn(false);

			final Map<Object, byte[]> payloadMap = Map.of(1L, "payload".getBytes());
			assertThatThrownBy(() -> cut.sign(payloadMap))
					.isInstanceOf(ApiOperationException.class)
					.satisfies(e -> assertThat(((ApiOperationException) e).getStatus())
							.isEqualTo(Response.Status.FORBIDDEN));
		}
	}

	// -------------------------------------------------------------------------
	// HTTP-Fehlerbehandlung
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("HTTP-Fehlerbehandlung")
	class HttpErrors {

		private void assertThrowsWithStatus(final int httpStatus, final Response.Status expectedStatus) {
			httpClient.respondWithError(httpStatus);

			final Map<Object, byte[]> payloadMap = Map.of(1L, "payload".getBytes());
			assertThatThrownBy(() -> cut.sign(payloadMap))
					.isInstanceOf(ApiOperationException.class)
					.satisfies(e -> assertThat(((ApiOperationException) e).getStatus())
							.isEqualTo(expectedStatus));
		}

		@Test
		@DisplayName("sign() wirft BAD_REQUEST bei Status 400")
		void sign_shouldThrowBadRequest_whenStatus400() {
			assertThrowsWithStatus(400, Response.Status.BAD_REQUEST);
		}

		@Test
		@DisplayName("sign() wirft UNAUTHORIZED bei Status 401")
		void sign_shouldThrowUnauthorized_whenStatus401() {
			assertThrowsWithStatus(401, Response.Status.UNAUTHORIZED);
		}

		@Test
		@DisplayName("sign() wirft FORBIDDEN bei Status 403")
		void sign_shouldThrowForbidden_whenStatus403() {
			assertThrowsWithStatus(403, Response.Status.FORBIDDEN);
		}

		@Test
		@DisplayName("sign() wirft NOT_FOUND bei Status 404")
		void sign_shouldThrowNotFound_whenStatus404() {
			assertThrowsWithStatus(404, Response.Status.NOT_FOUND);
		}

		@Test
		@DisplayName("sign() wirft INTERNAL_SERVER_ERROR bei Status 500")
		void sign_shouldThrowInternalServerError_whenStatus500() {
			assertThrowsWithStatus(500, Response.Status.INTERNAL_SERVER_ERROR);
		}

		@Test
		@DisplayName("sign() wirft INTERNAL_SERVER_ERROR bei unerwartetem Status 503")
		void sign_shouldThrowInternalServerError_whenStatusIsUnexpected() {
			assertThrowsWithStatus(503, Response.Status.INTERNAL_SERVER_ERROR);
		}

		@Test
		@DisplayName("sign() wirft INTERNAL_SERVER_ERROR bei Status 200 mit leerem Body")
		void sign_shouldThrowInternalServerError_whenBodyIsEmpty() {
			httpClient.respondWithEmptyBody();

			final Map<Object, byte[]> payloadMap = Map.of(1L, "payload".getBytes());
			assertThatThrownBy(() -> cut.sign(payloadMap))
					.isInstanceOf(ApiOperationException.class)
					.satisfies(e -> assertThat(((ApiOperationException) e).getStatus())
							.isEqualTo(Response.Status.INTERNAL_SERVER_ERROR));
		}

		@Test
		@DisplayName("sign() Fehlermeldung enthält HTTP-Statuscode")
		void sign_shouldIncludeStatusCodeInErrorMessage() {
			httpClient.respondWithError(403);

			final Map<Object, byte[]> payloadMap = Map.of(1L, "payload".getBytes());
			assertThatThrownBy(() -> cut.sign(payloadMap))
					.isInstanceOf(ApiOperationException.class)
					.hasMessageContaining("403");
		}

		@Test
		@DisplayName("sign() wirft INTERNAL_SERVER_ERROR wenn Request-Body nicht serialisiert werden kann")
		void sign_shouldThrowInternalServerError_whenSerializationFails() throws Exception {
			final ObjectMapper brokenMapper = mock(ObjectMapper.class);
			when(brokenMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Serialization failed") {
			});

			final SignatureServiceImpl serviceWithBrokenMapper = new SignatureServiceImpl(httpClient, eigeneSchuleService, benutzerKompetenzService, brokenMapper,
					SIGNATURE_SERVICE_URI);

			final Map<Object, byte[]> payloadMap = Map.of(1L, "payload".getBytes());
			assertThatThrownBy(() -> serviceWithBrokenMapper.sign(payloadMap))
					.isInstanceOf(ApiOperationException.class)
					.satisfies(e -> assertThat(((ApiOperationException) e).getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR))
					.hasMessageContaining("Failed to prepare API request");
		}
	}

// -------------------------------------------------------------------------
// Masking / ID-Mapping
// -------------------------------------------------------------------------

	@Nested
	@DisplayName("Masking: ID-Zuordnung über interne MaskIds")
	class Masking {

		@Test
		@DisplayName("String-IDs werden korrekt auf Signaturen zurückgemappt")
		void sign_shouldMapBackToStringIds() {
			httpClient.respondWithSuccess(successResponse(List.of(
					okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft())
			)));

			final Map<Object, Signature> result = cut.sign(Map.of(
					"string-id", "payload".getBytes()
			));

			assertThat(result).containsKey("string-id");
		}

		@Test
		@DisplayName("Beliebige Object-IDs (z.B. UUID) werden korrekt zurückgemappt")
		void sign_shouldMapBackToArbitraryObjectIds() {
			final Object arbitraryId = java.util.UUID.randomUUID();
			httpClient.respondWithSuccess(successResponse(List.of(
					okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft())
			)));

			final Map<Object, Signature> result = cut.sign(Map.of(
					arbitraryId, "payload".getBytes()
			));

			assertThat(result).containsKey(arbitraryId);
		}

		@Test
		@DisplayName("Originale IDs sind im Ergebnis enthalten, keine internen MaskIds")
		void sign_resultShouldNotContainInternalMaskIds() {
			final Object originalId = "my-original-id";
			httpClient.respondWithSuccess(successResponse(List.of(
					okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft())
			)));

			final Map<Object, Signature> result = cut.sign(Map.of(
					originalId, "payload".getBytes()
			));

			assertThat(result).doesNotContainKey(1L)
					.containsKey(originalId);
		}

		@Test
		@DisplayName("Mehrere Payloads: jede originale ID bekommt die richtige Signatur")
		void sign_shouldAssignCorrectSignatureToEachOriginalId() {
			// LinkedHashMap sichert stabile Iterationsreihenfolge → maskId 1 und 2 sind deterministisch
			final java.util.Map<Object, byte[]> payloadById = new java.util.LinkedHashMap<>();
			payloadById.put("first", "payload-first".getBytes());
			payloadById.put("second", "payload-second".getBytes());

			httpClient.respondWithSuccess(successResponse(List.of(
					okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft()),
					okResponse(2L, BASE64_ENCODINGS.get(2L).getLeft())
			)));

			final Map<Object, Signature> result = cut.sign(payloadById);

			assertThat(new String(result.get("first").content(), StandardCharsets.UTF_8))
					.isEqualTo(BASE64_ENCODINGS.get(1L).getRight());
			assertThat(new String(result.get("second").content(), StandardCharsets.UTF_8))
					.isEqualTo(BASE64_ENCODINGS.get(2L).getRight());
		}

		@Test
		@DisplayName("Anzahl der Einträge im Ergebnis entspricht der Anzahl der Eingabe-Payloads")
		void sign_resultSizeShouldMatchInputSize() {
			httpClient.respondWithSuccess(successResponse(List.of(
					okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft()),
					okResponse(2L, BASE64_ENCODINGS.get(1L).getLeft()),
					okResponse(3L, BASE64_ENCODINGS.get(1L).getLeft())
			)));

			final Map<Object, Signature> result = cut.sign(Map.of(
					"a", new byte[] { 1 },
					"b", new byte[] { 2 },
					"c", new byte[] { 3 }
			));

			assertThat(result).hasSize(3);
		}

		@Test
		@DisplayName("Dieselbe Payload mit unterschiedlichen IDs ergibt separate Einträge im Ergebnis")
		void sign_samePayloadDifferentIds_producesDistinctEntries() {
			final byte[] sharedPayload = "same-content".getBytes();
			httpClient.respondWithSuccess(successResponse(List.of(
					okResponse(1L, BASE64_ENCODINGS.get(1L).getLeft()),
					okResponse(2L, BASE64_ENCODINGS.get(2L).getLeft())
			)));

			final java.util.Map<Object, byte[]> payloadById = new java.util.LinkedHashMap<>();
			payloadById.put("id-A", sharedPayload);
			payloadById.put("id-B", sharedPayload);

			final Map<Object, Signature> result = cut.sign(payloadById);

			assertThat(result)
					.containsKey("id-A")
					.containsKey("id-B")
					.hasSize(2);
		}
	}

// -------------------------------------------------------------------------
// Hilfsmethoden
// -------------------------------------------------------------------------

	private static SignBatchResponse successResponse(final List<SignResponse> results) {
		return new SignBatchResponse(results);
	}

	private static SignResponse okResponse(final long id, final String cms) {
		return new SignResponse(id, "hash-" + id, cms, "OK");
	}

	private static SignResponse errorResponse(final long id) {
		return new SignResponse(id, "hash-" + id, "Fehler", "ERROR");
	}

}
