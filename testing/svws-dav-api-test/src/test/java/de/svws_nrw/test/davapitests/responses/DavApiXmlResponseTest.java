package de.svws_nrw.test.davapitests.responses;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Every.everyItem;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.svws_nrw.test.apitests.util.APITestUtil;
import de.svws_nrw.test.apitests.util.BaseApiUtil;
import de.svws_nrw.test.davapitests.util.xml.XmlPathWalker;
import io.restassured.response.Response;
/**
 * Testet die XML-Antworten der DavApi auf enthaltene Informationen
 */
class DavApiXmlResponseTest extends BaseApiUtil {

	/**
	 * Testet mit PROPFIND aus resources/dav/propfind_dav_207.xml auf davRoot
	 * <code><db/{schema}/dav></code> und erwartet eine Beschreibung der Wurzel
	 * sowie Links und Beschreibung zu den Wurzeln von Kalender und
	 * Adressbuechern
	 *
	 */
	@Test
	void whenPropfindOnDavRoot_thenMultiStatusWith3Responses() {
		String davRoot = "db/gymabi/dav";
		final Response response = given(user, password).when()
				.body(APITestUtil.readStringFromResourceFile("gymabi/dav/propfind_dav_207.xml", this))
				.request(PROPFIND, davRoot);
		davRoot = "/" + davRoot;
		final String responseString = response.asString();
		response.then().statusCode(207);
		final XmlPathWalker path = new XmlPathWalker(responseString);
		// erwarte 3 Responses in Multistatus
		assertThat(path.getIntAndUp("multistatus.response.size()"), equalTo(3));
		path.up();
		// die 3 Responses sollten davRoot, kalenderRoot und AdressbuecherRoot
		// sein
		final List<String> hrefs = new ArrayList<>();
		for (int responseIdx = 0; responseIdx < 3; responseIdx++) {
			final String href = path.getStringAndUp("response[" + responseIdx + "].href");
			hrefs.add(href);
			if (href.equals(davRoot)) {
				// generell sollten 200 als erstes und 404 als 2. ergebnis
				// auftauchen
				assertThat(path.getStringAndUp("propstat[0].status"), equalTo("HTTP/1.1 200 OK"));
				path.get("prop");
				assertThat(path.getStringAndUp("current-user-principal.href"), equalTo(davRoot + "/benutzer/1"));
				path.up();
				path.get("resourcetype");
				assertThat(List.of("collection", "principal"), everyItem(path.nodeExists()));
				assertThat(List.of("calendar-color", "owner", "displayname"), everyItem(not(path.nodeExists())));
				path.up().up().up();
				assertThat(path.getStringAndUp("propstat[1].status"), equalTo("HTTP/1.1 404 Not Found"));
				path.get("prop");
				assertThat(List.of("calendar-color", "owner", "displayname"), everyItem(path.nodeExists()));
				assertThat(List.of("collection", "principal"), everyItem(not(path.nodeExists())));
			} else if (href.equals(davRoot + "/adressbuecher")) {
				assertThat(path.getStringAndUp("propstat[0].status"), equalTo("HTTP/1.1 200 OK"));
				path.get("prop");
				assertThat(path.getStringAndUp("current-user-principal.href"), equalTo(davRoot + "/benutzer/1"));
				path.up();
				path.get("resourcetype");
				assertThat(List.of("collection"), everyItem(path.nodeExists()));
				assertThat(List.of("principal", "calendar-color", "owner", "displayname", "calendar-home-set"),
						everyItem(not(path.nodeExists())));

				path.up().up().up();
				assertThat(path.getStringAndUp("propstat[1].status"), equalTo("HTTP/1.1 404 Not Found"));
				path.get("prop");
				assertThat(List.of("calendar-color", "owner", "displayname", "calendar-home-set"),
						everyItem(path.nodeExists()));
				assertThat(List.of("collection"), everyItem(not(path.nodeExists())));
			} else if (href.equals(davRoot + "/kalender")) {
				assertThat(path.getStringAndUp("propstat[0].status"), equalTo("HTTP/1.1 200 OK"));
				path.get("prop");
				assertThat(path.getStringAndUp("current-user-principal.href"), equalTo(davRoot + "/benutzer/1"));
				path.up();
				path.get("resourcetype");
				assertThat(List.of("collection"), everyItem(path.nodeExists()));
				assertThat(List.of("principal", "calendar-color", "owner", "displayname"),
						everyItem(not(path.nodeExists())));

				path.up().up().up();
				assertThat(path.getStringAndUp("propstat[1].status"), equalTo("HTTP/1.1 404 Not Found"));
				path.get("prop");
				assertThat(List.of("calendar-color", "owner", "displayname"), everyItem(path.nodeExists()));
				assertThat(List.of("calendar-home-set", "collection"), everyItem(not(path.nodeExists())));
			} else {
				fail("Expected a valid href, but was " + href);
			}
			path.backToDocRoot();
			path.get("multistatus");
		}
		assertThat(hrefs,
				org.hamcrest.Matchers.containsInAnyOrder(davRoot, davRoot + "/adressbuecher", davRoot + "/kalender"));
	}

	/**
	 * Nested für Methoden der APIAdressbuch
	 */
	@Nested
	class APIAdressbuchXmlResponseTest {

		/**
		 * Testet auf Anfrage nach vorhandenen Adressbüchern
		 */
		@Test
		void whenPropfindOnAddressbooks_thenMultistatusWith3Addressbooks() {
			String adressbuecher = "dav/gymabi/adressbuecher";
			final Response response = given(user, password).when()
					.body(APITestUtil.readStringFromResourceFile(
							"gymabi/dav/adressbuecher/propfind_adressbuecher_207.xml", this))
					.request(PROPFIND, adressbuecher);
			adressbuecher = "/" + adressbuecher;
			final String responseString = response.asString();
			response.then().statusCode(207);
			final XmlPathWalker path = new XmlPathWalker(responseString);
			// erwarte 3 Responses in Multistatus
			assertThat(path.getIntAndUp("multistatus.response.size()"), equalTo(3));
			path.up();
			// die 3 Responses sollten davRoot, kalenderRoot und
			// AdressbuecherRoot
			// sein
			final List<String> hrefs = new ArrayList<>();
			for (int responseIdx = 0; responseIdx < 3; responseIdx++) {
				final String href = path.getStringAndUp("response[" + responseIdx + "].href");
				hrefs.add(href);
				if (href.equals(adressbuecher + "/schueler")) {
					assertThat(path.getStringAndUp("propstat.prop.displayname"), equalTo("Schüler"));
				} else if (href.equals(adressbuecher + "/lehrer")) {
					assertThat(path.getStringAndUp("propstat.prop.displayname"), equalTo("Lehrer"));
				} else if (href.equals(adressbuecher + "/erzieher")) {
					assertThat(path.getStringAndUp("propstat.prop.displayname"), equalTo("Erzieher"));
				} else {
					fail("unexpected href: " + href);
				}
				path.get("resourcetype");
				assertThat(List.of("collection", "addressbook"), everyItem(path.nodeExists()));
				path.up();
				assertThat(path.getStringAndUp("supported-address-data.addressDataTypes.@content-type"),
						equalTo("text/vcard"));
				assertThat(path.getStringAndUp("@version"), equalTo("4.0"));
				path.up().up().up();
				assertThat(path.getStringAndUp("status"), equalTo("HTTP/1.1 200 OK"));
				path.backToDocRoot();
				path.get("multistatus");
			}
			assertThat(hrefs, org.hamcrest.Matchers.containsInAnyOrder(adressbuecher + "/schueler",
					adressbuecher + "/lehrer", adressbuecher + "/erzieher"));
		}

		/**
		 * Testet Anfrage auf ein Adressbuch auf notwendige Antworten
		 */
		@Test
		void givenPropfindOnAddressbook_thenMultistatus() {
			String adressbuchHref = "dav/gymabi/adressbuecher/schueler";
			final Response response = given(user, password).when()
					.body(APITestUtil.readStringFromResourceFile(
							"gymabi/dav/adressbuecher/propfind_adressbuch_schueler_207.xml", this))
					.request(PROPFIND, adressbuchHref);
			adressbuchHref = "/" + adressbuchHref;
			final String responseString = response.asString();
			response.then().statusCode(207);
			final XmlPathWalker path = new XmlPathWalker(responseString);
			// erwarte Responses in Multistatus - eine Response sollte auf die
			// Collection verweisen, alle anderen auf je ein .vcf
			final int responseCount = path.getIntAndUp("multistatus.response.size()");
			path.up();

			final List<String> hrefs = new ArrayList<>();
			for (int responseIdx = 0; responseIdx < responseCount; responseIdx++) {
				final String href = path.getStringAndUp("response[" + responseIdx + "].href");
				hrefs.add(href);
				if (adressbuchHref.equals(href)) {
					// die Beschreibung der Collection
					assertThat(path.getStringAndUp("propstat[0].status"), equalTo("HTTP/1.1 200 OK"));
					path.get("prop.resourcetype");
					assertThat(List.of("collection", "addressbook"), everyItem(path.nodeExists()));
					assertThat(List.of("principal", "calendar-color", "owner", "displayname", "calendar-home-set"),
							everyItem(not(path.nodeExists())));
					path.up();
					assertThat("getctag", path.nodeExists());

					path.up().up();
					assertThat(path.getStringAndUp("propstat[1].status"), equalTo("HTTP/1.1 404 Not Found"));
					path.get("prop");
					assertThat(List.of("getetag"), everyItem(path.nodeExists()));
					assertThat(List.of("resourcetype", "getctag"), everyItem(not(path.nodeExists())));

				} else {
					// die Beschreibung der VCF-Files
					assertThat(href, startsWith(adressbuchHref + "/Schueler_"));
					assertThat(href, endsWith(".vcf"));
					assertThat(path.getStringAndUp("propstat[0].status"), equalTo("HTTP/1.1 200 OK"));
					assertThat(path.getStringAndUp("prop.getetag"), not(emptyOrNullString()));
					assertThat("resourcetype", path.nodeExists());
					path.up().up();
					assertThat(path.getStringAndUp("propstat[1].status"), equalTo("HTTP/1.1 404 Not Found"));
					assertThat("prop.getctag", path.nodeExists());
				}
				path.backToDocRoot();
				path.get("multistatus");
			}
		}

		/**
		 * Testet Report-Anfrage auf notwendige Informationen
		 */
		@Test
		void givenReportOnAddressbook_thenMultistatusAndVcf() {
			String adressbuchHref = "dav/gymabi/adressbuecher/schueler";
			final Response response = given(user, password).when()
					.body(APITestUtil.readStringFromResourceFile(
							"gymabi/dav/adressbuecher/report_adressbuch_schueler_207.xml", this))
					.request(REPORT, adressbuchHref);
			adressbuchHref = "/" + adressbuchHref;
			final String responseString = response.asString();
			response.then().statusCode(207);
			final XmlPathWalker path = new XmlPathWalker(responseString);
			assertThat(path.getIntAndUp("multistatus.response.size()"), equalTo(2));
			for (int i = 0; i < 2; i++) {
				path.up().get("response[" + i + "]");
				assertThat(path.getStringAndUp("href"),
						anyOf(equalTo("/dav/gymabi/adressbuecher/schueler/Schueler_1001.vcf"),
								equalTo("/dav/gymabi/adressbuecher/schueler/Schueler_1002.vcf")));
				assertThat(path.getStringAndUp("propstat.prop.address-data"),
						allOf(startsWith("BEGIN:VCARD"), endsWith("END:VCARD")));
				assertThat("getetag", path.nodeExists());
				path.backToDocRoot();
				path.get("multistatus");
			}
		}

		/**
		 * Testet, wie der Server mit Sync-Collection-Anfragen zum
		 * Synchronisieren von Adressbüchern umgeht.
		 */
		@Test
		@Disabled("SyncCollection ist noch nicht implementiert")
		void givenPropfindSyncCollectionOnAddressbook_then207() {
			final var body = """
					<sync-collection xmlns="DAV:" xmlns:card="urn:ietf:params:xml:ns:carddav" xmlns:cs="http://calendarserver.org/ns/" xmlns:d="DAV:">
					  <sync-token>0</sync-token>
					  <sync-level>1</sync-level>
					  <prop>
					    <getetag/>
					    <card:address-data/>
					  </prop>
					</sync-collection>
					""";
			given(user, password).when().body(body).request(PROPFIND, "dav/gymabi/adressbuecher/schueler").then()
					.statusCode(207);
		}
	}

	/**
	 * Nested für API-Methoden von APIKalender
	 */
	@Nested
	class APIKalenderXmlResponseTest {

		/**
		 * Testet Property-Suche auf die Liste von Kalendern
		 */
		@Test
		void givenPropfindOnCalendarCollection_then207() {
			String kalender = "dav/gymabi/kalender";
			final String body = APITestUtil
					.readStringFromResourceFile("gymabi/dav/kalender/propfind_kalender_collection_207.xml", this);

			Response response = given("ANDE", "password").when().body(body).request(PROPFIND, kalender);
			String responseString = response.asString();
			response.then().statusCode(207);
			// erwarte 2 Responses in Multistatus für Admin (1 Kalender Root, 1
			// Eigener Kalender
			XmlPathWalker path = new XmlPathWalker(responseString);
			int responses = path.getIntAndUp("multistatus.response.size()");
			assertThat(responses, equalTo(3));
			path.up();
			kalender = "/" + kalender;
			String hrefGemeinsamerKalender = null;
			for (int responseIdx = 0; responseIdx < responses; responseIdx++) {
				path.get("response[" + responseIdx + "]");
				final String href = path.getStringAndUp("href");
				final String name = path.getStringAndUp("propstat[0].prop.displayname");
				if ((name == null || name.isBlank()) && kalender.equals(href)) {
					// referenz auf die collection
					path.get("resourcetype");
					assertThat("collection", path.nodeExists());
					assertThat("calendar", not(path.nodeExists()));
					path.up();
					assertThat(path.getIntAndUp("current-user-privilege-set.privilege.size()"), equalTo(3));
					assertThat(List.of("read-current-user-privilege-set", "read", "read-acl"),
							everyItem(path.nodeExists()));
					assertThat(List.of("all", "write"), everyItem(not(path.nodeExists())));
				} else if ("Eigener Kalender".equals(name)) {
					hrefGemeinsamerKalender = href;
					path.get("resourcetype");
					assertThat(List.of("collection", "calendar"), everyItem(path.nodeExists()));
					path.up();
					assertThat(path.getIntAndUp("current-user-privilege-set.privilege.size()"), equalTo(5));
					assertThat(List.of("read-current-user-privilege-set", "read", "read-acl", "all", "write"),
							everyItem(path.nodeExists()));
				} else {
					assertThat(name, equalTo("Gemeinsamer Kalender"));
					path.get("resourcetype");
					assertThat(List.of("collection", "calendar"), everyItem(path.nodeExists()));
					path.up();
					assertThat(path.getIntAndUp("current-user-privilege-set.privilege.size()"), equalTo(5));
					assertThat(List.of("read-current-user-privilege-set", "read", "read-acl", "all", "write"),
							everyItem(path.nodeExists()));

				}
				path.backToDocRoot();
				path.get("multistatus");
			}
			// check: Nutzer hat nur Leserechte auf den Gemeinsamen Kalender:
			response = given("BAGI", "password").when().body(body).request(PROPFIND, kalender);
			responseString = response.asString();
			response.then().statusCode(207);
			// erwarte 2 Responses in Multistatus für Admin (1 Kalender Root, 1
			// Eigener Kalender
			path = new XmlPathWalker(responseString);
			responses = path.getIntAndUp("multistatus.response.size()");
			assertThat(responses, equalTo(3));
			path.up();
			for (int i = 0; i < responses; i++) {
				final String displayname = path.getStringAndUp("response[" + i + "].propstat[0].prop.displayName");
				if ("Gemeinsamer Kalender".equals(displayname)) {
					assertThat("current-user-privilege-set.privilege.write", not(path.nodeExists()));
				} else {
					assertThat(displayname, anyOf(equalTo(""), equalTo("Eigener Kalender")));
				}
			}

			givenPropfindOnCalendar_then207(hrefGemeinsamerKalender);
		}

		/**
		 * Ruft ein Propfind auf den Gemeinsamen Kalender auf. Durch
		 * Abhängigkeit vom vorigen Propfind keine eigene Testmethode
		 *
		 * @param hrefGemeinsamerKalender
		 */
		void givenPropfindOnCalendar_then207(final String hrefGemeinsamerKalender) {
			String kalender = hrefGemeinsamerKalender;
			if (hrefGemeinsamerKalender.indexOf('/') == 0) {
				kalender = hrefGemeinsamerKalender.substring(1);
			}
			final String body = APITestUtil.readStringFromResourceFile("gymabi/dav/kalender/propfind_kalender_207.xml", this);

			final Response response = given("ANDE", "password").when().body(body).request(PROPFIND, kalender);
			final String responseString = response.asString();
			response.then().statusCode(207);
			// erwarte 2 Responses in Multistatus für Admin (1 Kalender Root, 1
			// Eigener Kalender
			final XmlPathWalker path = new XmlPathWalker(responseString);
			assertThat(path.getIntAndUp("multistatus.response.size()"), equalTo(1));
			path.get("propstat.prop");
			assertThat(List.of("resourcetype.collection", "resourcetype.calendar",
					"supported-report-set.supported-report.report.calendar-multiget",
					"supported-report-set.supported-report.report.calendar-query",
					"supported-report-set.supported-report.report.sync-collection",
					"current-user-privilege-set.privilege.read", "current-user-privilege-set.privilege.write",
					"getctag"), everyItem(path.nodeExists()));
			assertThat(path.getStringAndUp("supported-calendar-component-set.calendarComponents.@name"),
					equalTo("VEVENT"));
		}
	}
}
