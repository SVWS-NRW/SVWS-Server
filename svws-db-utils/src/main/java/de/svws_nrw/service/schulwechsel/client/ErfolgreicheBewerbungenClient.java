package de.svws_nrw.service.schulwechsel.client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.oauth.OAuthHttpClient;
import de.svws_nrw.oauth.OAuthScope;
import jakarta.ws.rs.core.Response;

/**
 * Client für den Abruf erfolgreicher Bewerbungen vom Schulbewerbungs-Service.
 */
public final class ErfolgreicheBewerbungenClient {

	private static final String VERSION_XSCHULE = "1.2";

	private static final String ERROR_MESSAGE = "Fehler bei der Kommunikation mit dem Remote-Server (Status-Code: %d): %s";
	private static final String ERROR_MESSAGE_UNAUTHORIZED = "Nicht authentifiziert";
	private static final String ERROR_MESSAGE_FORBIDDEN = "Fehlende Berechtigung";
	private static final String ERROR_MESSAGE_NOT_FOUND = "API-Ressource nicht gefunden";
	private static final String ERROR_MESSAGE_UNDEFINED = "Unerwarteter Fehler";
	private static final String ERROR_MESSAGE_EMPTY_BODY = "Leerer Response-Body";

	private static final String ERROR_MESSAGE_INVALID_SCHULKIND_ID = "Eine schulkindId fehlt oder ist keine gültige UUID.";

	private final OAuthHttpClient httpClient;
	private final URI uriErfolgreicheBewerbungen;

	/**
	 * Konstruktor.
	 *
	 * @param httpClient                 der OAuth-gesicherte HTTP-Client
	 * @param uriErfolgreicheBewerbungen die URI des Endpunkts für erfolgreiche Bewerbungen
	 */
	public ErfolgreicheBewerbungenClient(final OAuthHttpClient httpClient, final URI uriErfolgreicheBewerbungen) {
		this.httpClient = httpClient;
		this.uriErfolgreicheBewerbungen = uriErfolgreicheBewerbungen;
	}

	/**
	 * Ruft alle erfolgreichen Bewerbungen vom Schulbewerbungs-Service ab.
	 * <p>
	 * Jede schulkindId der gelieferten Bewerbungen ist garantiert eine gültige UUID.
	 *
	 * @return Liste der erfolgreichen Bewerbungen
	 * @throws ApiOperationException bei HTTP-Fehlern oder unerwartetem Antwortverhalten,
	 *                               sowie mit Status 500 wenn eine schulkindId fehlt oder keine gültige UUID ist
	 */
	public List<ErfolgreicheBewerbungResponse> getAllBewerbungen() {
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(uriErfolgreicheBewerbungen)
				.GET()
				.build();

		final HttpResponse<ErfolgreicheBewerbungResponse[]> response =
				httpClient.send(request, OAuthScope.SCHULBEWERBUNG, ErfolgreicheBewerbungResponse[].class);

		final List<ErfolgreicheBewerbungResponse> bewerbungen = handleResponseBewerbungen(response);
		bewerbungen.forEach(b -> validateSchulkindId((b == null) ? null : b.schulkindId()));
		return bewerbungen;
	}

	/**
	 * Ruft das XSchule-Dokument für das Schulkind mit der angegebenen ID ab.
	 *
	 * @param schulkindId die UUID des Schulkinds bei schulbewerbung.de
	 * @return das XSchule-Dokument als XML-String
	 * @throws ApiOperationException bei HTTP-Fehlern oder unerwartetem Antwortverhalten,
	 *                               sowie mit Status 500 wenn die schulkindId fehlt oder keine gültige UUID ist
	 */
	public String getXSchule(final String schulkindId) {
		validateSchulkindId(schulkindId);
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uriErfolgreicheBewerbungen + "/" + schulkindId + "/xschule"))
				.header("XSchule-Version", VERSION_XSCHULE)
				.GET()
				.build();

		final HttpResponse<String> response =
				httpClient.send(request, OAuthScope.SCHULBEWERBUNG, HttpResponse.BodyHandlers.ofString());

		return handleResponseXSchule(response);
	}

	private static List<ErfolgreicheBewerbungResponse> handleResponseBewerbungen(final HttpResponse<ErfolgreicheBewerbungResponse[]> response) {
		return handleResponse(response, Objects::isNull, Arrays::asList);
	}

	private static String handleResponseXSchule(final HttpResponse<String> response) {
		return handleResponse(response, body -> (body == null) || body.isBlank(), Function.identity());
	}

	/**
	 * Wertet die Antwort des Remote-Servers aus. Bei Status 200 wird der Body geprüft und umgewandelt,
	 * andernfalls wird eine passende {@link ApiOperationException} geworfen.
	 *
	 * @param response     die Antwort des Remote-Servers
	 * @param isBodyEmpty  Prüfung, ob der Body bei Status 200 als leer gilt
	 * @param bodyMapper   Umwandlung des Bodys in den Rückgabetyp
	 * @param <B>          Typ des Response-Bodys
	 * @param <R>          Rückgabetyp
	 * @return das umgewandelte Ergebnis
	 * @throws ApiOperationException bei HTTP-Fehlern oder leerem Body
	 */
	private static <B, R> R handleResponse(final HttpResponse<B> response, final Predicate<B> isBodyEmpty, final Function<B, R> bodyMapper) {
		final int statusCode = response.statusCode();
		if (statusCode != 200) {
			throw createApiOperationException(statusCode);
		}
		if (isBodyEmpty.test(response.body())) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_MESSAGE.formatted(statusCode, ERROR_MESSAGE_EMPTY_BODY));
		}
		return bodyMapper.apply(response.body());
	}

	private static ApiOperationException createApiOperationException(final int statusCode) {
		return switch (statusCode) {
			case 401 -> new ApiOperationException(Response.Status.UNAUTHORIZED, ERROR_MESSAGE.formatted(statusCode, ERROR_MESSAGE_UNAUTHORIZED));
			case 403 -> new ApiOperationException(Response.Status.FORBIDDEN, ERROR_MESSAGE.formatted(statusCode, ERROR_MESSAGE_FORBIDDEN));
			case 404 -> new ApiOperationException(Response.Status.NOT_FOUND, ERROR_MESSAGE.formatted(statusCode, ERROR_MESSAGE_NOT_FOUND));
			default -> new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_MESSAGE.formatted(statusCode, ERROR_MESSAGE_UNDEFINED));
		};
	}

	private static void validateSchulkindId(final String schulkindId) {
		if (!isUuid(schulkindId)) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_MESSAGE_INVALID_SCHULKIND_ID);
		}
	}

	private static boolean isUuid(final String value) {
		if (value == null) {
			return false;
		}
		try {
			return UUID.fromString(value).toString().equalsIgnoreCase(value);
		} catch (final IllegalArgumentException e) {
			return false;
		}
	}
}
