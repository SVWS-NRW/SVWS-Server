package de.svws_nrw.test.davapitests.responses;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.svws_nrw.test.apitests.util.BaseApiUtil;
/**
 * Testmethode für die Verfügbarkeit der Dav-Api-Endpunkte
 */
class DavApiEndpointsPresentTest extends BaseApiUtil {
	/**
	 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
	 * Body sollte zu den erwarteten Fehlermeldungen führen
	 *
	 */
	@Test
	void givenInvalidPropfindOnRoot_then400() {
		given(user, password).when().body("a").request(PROPFIND, "/db/gymabi/dav/").then().statusCode(400);
	}

	/**
	 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
	 * Body sollte zu den erwarteten Fehlermeldungen führen
	 *
	 */
	@Test
	void givenInvalidPropfindOnPrincipal_then400() {
		given(user, password).when().body("a").request(PROPFIND, "/db/gymabi/dav/benutzer/-1").then().statusCode(400);
	}

	/**
	 * Nested für die Methoden der APIAdressbuch
	 */
	@Nested
	class ApiAdressbuchEndpointsPresentTest {
		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 *
		 */
		@Test
		void givenInvalidPropfindOnAddressbooks_then400() {
			given(user, password).when().body("a").request(PROPFIND, "/db/gymabi/dav/adressbuecher").then()
					.statusCode(400);
		}

		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 *
		 */
		@Test
		void givenInvalidPropfindOnAddressbook_then400() {
			given(user, password).when().body("a").request(PROPFIND, "/db/gymabi/dav/adressbuecher/-1").then()
					.statusCode(400);
		}

		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 *
		 */
		@Test
		void givenInvalidReportOnAddressbook_then404() {
			given(user, password).when().body("a").request(REPORT, "/db/gymabi/dav/adressbuecher/-1").then()
					.statusCode(404);
		}

		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 *
		 */
		@Test
		void givenInvalidReportOnContact_then404() {
			given(user, password).when().body("a").request(REPORT, "/db/gymabi/dav/adressbuecher/-1/-1.vcf").then()
					.statusCode(404);
		}
	}

	/**
	 * Nested für Methoden der APIKalender
	 */
	@Nested
	class APIKalenderEndpointsPresentTest {
		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 */
		@Test
		void givenInvalidPropfindOnCalendarCollection_then400() {
			given(user, password).when().body("").request(PROPFIND, "/db/gymabi/dav/kalender").then().statusCode(400);
		}
		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 */
		@Test
		void givenInvalidPropfindOnCalendar_then400() {
			given(user, password).when().body("").request(PROPFIND, "/db/gymabi/dav/kalender/-1").then()
					.statusCode(400);
		}

		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 */
		@Test
		void givenInvalidReportOnCalendar_then400() {
			given(user, password).when().body("").request(REPORT, "/db/gymabi/dav/kalender/-1").then().statusCode(404);
		}

		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 */
		@Test
		void givenInvalidPutOnCalendarIfNoneMatch_then400() {
			given(user, password).when().contentType("Text/Calendar").body("a").header("If-None-Match", "")
					.put("/db/gymabi/dav/kalender/-1/something-something.ics").then().statusCode(400);
		}

		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 */
		@Test
		void givenInvalidPutOnCalendarIfMatch_then400test() {
			given(user, password).when().contentType("Text/Calendar").body("a").header("If-Match", "*")
					.put("/db/gymabi/dav/kalender/-1/something-something.ics").then().statusCode(500);
		}

		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 */
		@Test
		void givenDeleteOnCalendarInvalidRessourceId_then400() {
			given(user, password).when().contentType("Text/Calendar").header("If-Match", "*")
					.delete("/db/gymabi/dav/kalender/-1/something-something.ics").then().statusCode(400);
		}

		/**
		 * Testmethode prüft ob der Endpunkt vorhanden und erreichbar ist. Dummy
		 * Body sollte zu den erwarteten Fehlermeldungen führen
		 */
		@Test
		void givenDeleteOnCalendarInvalidCollectionId_then400() {
			given(user, password).when().contentType("Text/Calendar").delete("/db/gymabi/dav/kalender/-1").then()
					.statusCode(400);
		}
	}
}
