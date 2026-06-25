package de.svws_nrw.oauth;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import de.svws_nrw.db.utils.ApiOperationException;

public interface OAuthHttpClient {

	/**
	 * Sendet einen HTTP-Request an einen OAuth-gesicherten Endpunkt.
	 *
	 * <p>Das aktive Schema wird automatisch ueber den {@link SchemaService} aufgeloest.
	 * Der Bearer-Token wird transparent gesetzt. Bei HTTP 401 wird der gecachte Token
	 * einmalig invalidiert und der Request wiederholt.
	 *
	 * @param <T>         Typ des Response-Body
	 * @param baseRequest der fachliche Request ohne Authorization-Header
	 * @param scope       OAuth-Scope fuer diesen Request;
	 *                    {@code null} oder leer nutzt den Default-Scope der Credentials
	 * @param bodyHandler JDK-BodyHandler fuer den Response-Typ
	 * @return die HTTP-Response des Endpunkts
	 * @throws ApiOperationException bei Netzwerk- oder Interrupt-Fehlern
	 */
	<T> HttpResponse<T> send(HttpRequest baseRequest, String scope, HttpResponse.BodyHandler<T> bodyHandler);

	/**
	 * Wie {@link #send(HttpRequest, String, HttpResponse.BodyHandler)},
	 * deserialisiert den Response-Body jedoch direkt nach {@code T} via Jackson.
	 *
	 * @param <T>         Zieltyp der Jackson-Deserialisierung
	 * @param baseRequest der fachliche Request ohne Authorization-Header
	 * @param scope       OAuth-Scope fuer diesen Request;
	 *                    {@code null} oder leer nutzt den Default-Scope der Credentials
	 * @param type        Zieltyp der Jackson-Deserialisierung
	 * @return die HTTP-Response mit deserialisiertem Body
	 * @throws ApiOperationException bei Netzwerk- oder Interrupt-Fehlern
	 */
	<T> HttpResponse<T> send(HttpRequest baseRequest, String scope, Class<T> type);
}
