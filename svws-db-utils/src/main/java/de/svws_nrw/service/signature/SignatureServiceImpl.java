package de.svws_nrw.service.signature;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.oauth.OAuthHttpClient;
import de.svws_nrw.oauth.OAuthScope;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.HashUtils;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bouncycastle.util.encoders.Base64;

/**
 * Dieser Service stellt Funktionalität bereit, um Daten/Dokumente von einer externen API signieren zu lassen.
 */
public final class SignatureServiceImpl implements SignatureService {

	private static final String API_DEFAULT_POLICY_ID = "default_v1";
	private static final String API_ERROR_MESSAGE = "API request failed with status %d: %s";

	private final OAuthHttpClient httpClient;
	private final EigeneSchuleService eigeneSchuleService;
	private final BenutzerKompetenzService benutzerKompetenzService;
	private final URI signatureServiceURI;
	private final ObjectMapper objectMapper;

	/**
	 * Konstruktor.
	 *
	 * @param httpClient der HTTP-Client
	 * @param eigeneSchuleService der Schule-Service
	 * @param benutzerKompetenzService der BenutzerKompetenz-Service
	 * @param signatureServiceURI die URI des Signatur-Services
	 * @param objectMapper der ObjectMapper
	 */
	public SignatureServiceImpl(final OAuthHttpClient httpClient,
			final EigeneSchuleService eigeneSchuleService,
			final BenutzerKompetenzService benutzerKompetenzService,
			final ObjectMapper objectMapper,
			final URI signatureServiceURI) {
		this.httpClient = httpClient;
		this.eigeneSchuleService = eigeneSchuleService;
		this.benutzerKompetenzService = benutzerKompetenzService;
		this.signatureServiceURI = signatureServiceURI;
		this.objectMapper = objectMapper;
	}

	@Override
	public Map<Object, Signature> sign(final Map<Object, byte[]> payloadById) {
		checkUserHasPermissionToSign();
		return signInternal(payloadById);
	}

	private void checkUserHasPermissionToSign() {
		if (!benutzerKompetenzService.hatBenutzerKompetenz(BenutzerKompetenz.EXTRAS_DIGITALE_SIGNATUREN_AUSSTELLEN)) {
			throw new ApiOperationException(
					Response.Status.FORBIDDEN,
					"User has no permissions to create digital signatures."
			);
		}
	}

	private Map<Object, Signature> signInternal(final Map<Object, byte[]> payloadById) {
		final PayloadMasker masker = PayloadMasker.newInstance();

		final Map<Long, byte[]> maskedPayload = masker.mask(payloadById);

		final HttpRequest request = createRequest(maskedPayload);
		final HttpResponse<SignBatchResponse> response = sendBatchRequest(request);
		final Map<Long, Signature> signatures = handleResponse(response);

		try {
			return masker.unmask(signatures);
		} catch (final MaskerException exception) {
			throw new ApiOperationException(
					Response.Status.INTERNAL_SERVER_ERROR,
					exception,
					"Error during unmasking response to internal ids."
			);
		}
	}

	private HttpResponse<SignBatchResponse> sendBatchRequest(final HttpRequest request) {
		return httpClient.send(request, OAuthScope.DEFAULT, SignBatchResponse.class);
	}

	private static Map<Long, Signature> handleResponse(final HttpResponse<SignBatchResponse> response) {
		validateResponse(response);
		return createSignatureMap(response.body());
	}

	@SuppressWarnings("java:S6916")
	private static void validateResponse(final HttpResponse<SignBatchResponse> response) {
		switch (response.statusCode()) {
			case 200 -> {
				if (response.body() == null) {
					throw new ApiOperationException(
							Response.Status.INTERNAL_SERVER_ERROR,
							API_ERROR_MESSAGE.formatted(response.statusCode(), "Empty response body.")
					);
				}
			}
			case 400 -> throw new ApiOperationException(
					Response.Status.BAD_REQUEST,
					API_ERROR_MESSAGE.formatted(response.statusCode(), "Invalid Request-Body")
			);
			case 401 -> throw new ApiOperationException(
					Response.Status.UNAUTHORIZED,
					API_ERROR_MESSAGE.formatted(response.statusCode(), "Missing authentication")
			);
			case 403 -> throw new ApiOperationException(
					Response.Status.FORBIDDEN,
					API_ERROR_MESSAGE.formatted(response.statusCode(), "Missing permissions")
			);
			case 404 -> throw new ApiOperationException(
					Response.Status.NOT_FOUND,
					API_ERROR_MESSAGE.formatted(response.statusCode(), "API resource not found")
			);
			default -> throw new ApiOperationException(
					Response.Status.INTERNAL_SERVER_ERROR,
					API_ERROR_MESSAGE.formatted(response.statusCode(), "Unexpected error")
			);
		}
	}

	private static Map<Long, Signature> createSignatureMap(final SignBatchResponse response) {
		return response.results().stream().collect(Collectors.toMap(SignResponse::id, SignatureServiceImpl::createSignature));
	}

	private static Signature createSignature(final SignResponse signResponse) {
		final SignatureStatus status = SignatureStatus.getByText(signResponse.status()).orElse(SignatureStatus.UNKNOWN);
		if (status == SignatureStatus.ERROR) {
			return new Signature(null, SignatureStatus.ERROR, signResponse.cms());
		}
		return new Signature(Base64.decode(signResponse.cms()), status, null);
	}

	private HttpRequest createRequest(final Map<Long, byte[]> payloadById) {
		final var schulnummer = eigeneSchuleService.getSchulnummer();
		final List<SignRequestPayload> requestPayloads = payloadById.entrySet().stream()
				.map(e -> new SignRequestPayload(e.getKey(), HashUtils.sha256AsHex(e.getValue())))
				.toList();
		final SignBatchRequest request = new SignBatchRequest(schulnummer, API_DEFAULT_POLICY_ID, requestPayloads);
		return HttpRequest.newBuilder()
				.uri(signatureServiceURI)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.POST(getBodyPublisher(request))
				.build();
	}

	private HttpRequest.BodyPublisher getBodyPublisher(final SignBatchRequest signBatchRequest) {
		try {
			return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(signBatchRequest));
		} catch (final JsonProcessingException e) {
			throw new ApiOperationException(
					Response.Status.INTERNAL_SERVER_ERROR,
					"Failed to prepare API request: %s.".formatted(e)
			);
		}
	}

	/**
	 * Maskiert die Original-IDs durch interne MaskIds, um die Zuordnung zwischen den Signaturen und den Original-IDs zu ermöglichen.
	 */
	static final class PayloadMasker {

		private final Map<Long, Object> originalIdByMaskId = new HashMap<>();

		private PayloadMasker() {
		}

		static PayloadMasker newInstance() {
			return new PayloadMasker();
		}

		Map<Long, byte[]> mask(final Map<Object, byte[]> payloadById) {
			final Map<Long, byte[]> payloadByMaskId = new HashMap<>();
			long maskId = 1;
			for (final Map.Entry<Object, byte[]> entry : payloadById.entrySet()) {
				payloadByMaskId.put(maskId, entry.getValue());
				originalIdByMaskId.put(maskId, entry.getKey());
				maskId++;
			}
			return payloadByMaskId;
		}

		Map<Object, Signature> unmask(final Map<Long, Signature> payload) throws MaskerException {
			final Map<Object, Signature> unmaskedPayload = new HashMap<>();
			for (final Map.Entry<Long, Signature> longSignatureEntry : payload.entrySet()) {
				unmaskedPayload.put(getOriginalIdOrException(longSignatureEntry), longSignatureEntry.getValue());
			}
			return unmaskedPayload;
		}

		private Object getOriginalIdOrException(final Map.Entry<Long, Signature> entry) throws MaskerException {
			final Object originalId = originalIdByMaskId.get(entry.getKey());
			if (originalId == null) {
				throw new MaskerException(String.format("No original id found for mask id: %s", entry.getKey()));
			}
			return originalId;
		}
	}

	static class MaskerException extends Exception {
		MaskerException(final String message) {
			super(message);
		}
	}
}
