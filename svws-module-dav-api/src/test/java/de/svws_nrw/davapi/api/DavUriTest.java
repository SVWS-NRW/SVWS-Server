package de.svws_nrw.davapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;

/**
 * Tests für die Generierung der URIs für die DAV-API.
 */
@ExtendWith(MockitoExtension.class)
class DavUriTest {

	@Mock
	private DBEntityManager conn;

	/**
	 * Testet das Generieren der URIs für die DAV-API
	 */
	@Test
	void textDispatcherURIGenerator() {
		final DavRequestManager dispatcher = new PropfindDavRootDispatcher(conn);
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
