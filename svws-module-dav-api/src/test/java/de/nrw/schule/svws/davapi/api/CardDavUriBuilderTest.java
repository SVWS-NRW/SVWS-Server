package de.nrw.schule.svws.davapi.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse enthält die Testroutinen für die Generierung von URIs von
 * Ressourcen für das DAV-API.
 */
class CardDavUriBuilderTest {

	/**
	 * Erstellt eine Instanz des {@link CardDavUriParameter}
	 * @return
	 */
	private static CardDavUriParameter createParameter() {
		CardDavUriParameter parameter = new CardDavUriParameter();
		parameter.setAdressbuchId("AAA");
		parameter.setBenutzerId("BBB");
		parameter.setSchema("SSS");
		parameter.setAdressbuchEintragId("KKK");
		return parameter;
	}

	/**
	 * Testet {@link CardDavUriBuilder#getAddressbookUri(CardDavUriParameter)}
	 */
	@Test
	void getAddressbookUri_returnsExpectedUri() {
		String result = CardDavUriBuilder.getAddressbookUri(createParameter());
		assertEquals("/db/SSS/carddav/adressbuecher/AAA", result);
	}

	/**
	 * Testet {@link CardDavUriBuilder#getAddressbookCollectionUri(CardDavUriParameter)}
	 */
	@Test
	void getAddressbookCollectionUri_returnsExpectedUri() {
		String result = CardDavUriBuilder.getAddressbookCollectionUri(createParameter());
		assertEquals("/db/SSS/carddav/adressbuecher", result);
	}

	/**
	 * Testet {@link CardDavUriBuilder#getAdressEntryUri(CardDavUriParameter)}
	 */
	@Test
	void getAdressEntryUri_returnsExpectedUri() {
		String result = CardDavUriBuilder.getAdressEntryUri(createParameter());
		assertEquals("/db/SSS/carddav/adressbuecher/AAA/KKK.vcf", result);
	}

	/**
	 * Testet {@link CardDavUriBuilder#getPrincipalUri(CardDavUriParameter)}
	 */
	@Test
	void getPrincipalUri_returnsExpectedUri() {
		String result = CardDavUriBuilder.getPrincipalUri(createParameter());
		assertEquals("/db/SSS/carddav/benutzer/BBB", result);
	}

}
