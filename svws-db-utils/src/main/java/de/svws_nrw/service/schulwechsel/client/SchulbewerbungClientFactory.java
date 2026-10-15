package de.svws_nrw.service.schulwechsel.client;

import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.svws_nrw.oauth.OAuthHttpClientFactory;
import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.service.signature.SignatureService;
import de.svws_nrw.service.signature.SignatureServiceImpl;

/**
 * Factory zur Erzeugung von {@link SignatureService}-Instanzen.
 */
public final class SchulbewerbungClientFactory {

	private static final String URI_ERFOLGREICHE_BEWERBUNGEN = "https://schulen-anbindungen.demo.schulbewerbung.de/v1/erfolgreiche-bewerbungen";

	private final OAuthHttpClientFactory httpClientFactory;

	private SchulbewerbungClientFactory(final OAuthHttpClientFactory httpClientFactory) {
		this.httpClientFactory = httpClientFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param httpClientFactory   die Factory für HTTP-Clients
	 *
	 * @return die neue Factory-Instanz
	 */
	public static SchulbewerbungClientFactory getNewInstance(final OAuthHttpClientFactory httpClientFactory) {
		return new SchulbewerbungClientFactory(httpClientFactory);
	}

	/**
	 * Erzeugt einen {@link SignatureService} mit dem angegebenen HTTP-Client und einem konfigurierten {@link ObjectMapper}.
	 *
	 * @return eine neue {@link SignatureServiceImpl}-Instanz
	 */
	public ErfolgreicheBewerbungenClient getErfolgreicheBewerbungenClient() {
		return new ErfolgreicheBewerbungenClient(
				httpClientFactory.getClient(OAuthDomain.SCHULBEWERBUNG),
				getURI(URI_ERFOLGREICHE_BEWERBUNGEN)
		);
	}

	URI getURI(final String address) {
		return URI.create(address);
	}
}

