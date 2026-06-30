package de.svws_nrw.service.signature;

import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.oauth.OAuthHttpClientFactory;
import de.svws_nrw.service.benutzer.BenutzerServiceFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;
import jakarta.ws.rs.core.Response;

/**
 * Factory zur Erzeugung von {@link SignatureService}-Instanzen.
 */
public final class SignatureServiceFactory {

	private static final String ENV_PROPERTY_SIGNATURE_SERVICE_URL = "svws.itnrw.signature.url";

	private final OAuthHttpClientFactory httpClientFactory;
	private final SchuleServiceFactory schuleServiceFactory;
	private final BenutzerServiceFactory benutzerServiceFactory;

	private SignatureServiceFactory(
			final OAuthHttpClientFactory httpClientFactory,
			final SchuleServiceFactory schuleServiceFactory,
			final BenutzerServiceFactory benutzerServiceFactory) {
		this.httpClientFactory = httpClientFactory;
		this.schuleServiceFactory = schuleServiceFactory;
		this.benutzerServiceFactory = benutzerServiceFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param httpClientFactory   die Factory für HTTP-Clients
	 * @param schuleServiceFactory   die Factory für Schule-Services
	 * @param benutzerServiceFactory   die Factory für Benutzer-Services
	 *
	 * @return die neue Factory-Instanz
	 */
	public static SignatureServiceFactory getNewInstance(final OAuthHttpClientFactory httpClientFactory, final SchuleServiceFactory schuleServiceFactory,
			final BenutzerServiceFactory benutzerServiceFactory) {
		return new SignatureServiceFactory(httpClientFactory, schuleServiceFactory, benutzerServiceFactory);
	}

	/**
	 * Erzeugt einen {@link SignatureService} mit dem angegebenen HTTP-Client und einem konfigurierten {@link ObjectMapper}.
	 *
	 * @return eine neue {@link SignatureServiceImpl}-Instanz
	 */
	public SignatureService getSignatureService() {
		return new SignatureServiceImpl(
				httpClientFactory.getClient(),
				schuleServiceFactory.getSchuleService(),
				benutzerServiceFactory.getBenutzerKompetenzService(),
				new ObjectMapper(),
				getSignatureServiceURI()
		);
	}

	// TODO: Das hier ist nur eine unschöne Zwischenlösung
	URI getSignatureServiceURI() {
		final String signatureServiceUrl = System.getenv(ENV_PROPERTY_SIGNATURE_SERVICE_URL);
		if (signatureServiceUrl == null) {
			throw new ApiOperationException(
					Response.Status.INTERNAL_SERVER_ERROR,
					"Missing env variable for signature service url: %s".formatted(ENV_PROPERTY_SIGNATURE_SERVICE_URL)
			);
		}
		return URI.create(signatureServiceUrl);
	}
}

