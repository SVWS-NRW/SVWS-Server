package de.svws_nrw.davapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Generierung der URIs für die DAV-API.
 */
class DavUriTest {

	/**
	 * Testet das Generieren der URIs für die DAV-API
	 */
	@Test
	void textDispatcherURIGenerator() {
		final DavDispatcher dispatcher = new PropfindDavRootDispatcher();
		dispatcher.setParameterSchema("SSS");
		dispatcher.setParameterBenutzerId("BBB");
		dispatcher.setParameterResourceCollectionId("AAA");
		dispatcher.setParameterResourceId("KKK");
		assertEquals("/dav/SSS", dispatcher.getRootUri());
		assertEquals("/dav/SSS/benutzer/BBB", dispatcher.getBenutzerUri());
		assertEquals("/dav/SSS/kalender", dispatcher.getKalenderUri());
		assertEquals("/dav/SSS/kalender/AAA", dispatcher.getKalenderResourceCollectionUri());
		assertEquals("/dav/SSS/kalender/AAA/KKK.ics", dispatcher.getKalenderResourceUri());
		assertEquals("/dav/SSS/adressbuecher", dispatcher.getCardDavUri());
		assertEquals("/dav/SSS/adressbuecher/AAA", dispatcher.getCardDavResourceCollectionUri());
		assertEquals("/dav/SSS/adressbuecher/AAA/KKK.vcf", dispatcher.getCardDavResourceUri());
	}

}
