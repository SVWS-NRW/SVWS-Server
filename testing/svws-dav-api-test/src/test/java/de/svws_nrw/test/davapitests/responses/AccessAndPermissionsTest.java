package de.svws_nrw.test.davapitests.responses;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;

import de.svws_nrw.test.apitests.util.APITestUtil;
import de.svws_nrw.test.apitests.util.BaseApiUtil;
import de.svws_nrw.test.davapitests.util.xml.XmlPathWalker;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
/**
 * Testklasse für Zugriffskontrolle/Sichtbarkeit von Kalendern
 */
class AccessAndPermissionsTest extends BaseApiUtil {

	/**
	 * BIConsumer für, um alle API-Methoden aufzurufen und einen Statuscode zu
	 * erwarten.
	 */
	private final BiConsumer<RequestSpecification, Integer> givenGiven_thenStatusCode = (given, expectedStatusCode) -> {
		given.when().body("a").request(PROPFIND, "/db/gymabi/dav/").then().statusCode(expectedStatusCode);
		given.when().body("a").request(PROPFIND, "/db/gymabi/dav/benutzer/-1").then().statusCode(expectedStatusCode);
		given.when().body("a").request(PROPFIND, "/db/gymabi/dav/adressbuecher").then().statusCode(expectedStatusCode);
		given.when().body("a").request(PROPFIND, "/db/gymabi/dav/adressbuecher/-1").then()
				.statusCode(expectedStatusCode);
		given.when().body("a").request(REPORT, "/db/gymabi/dav/adressbuecher/-1").then().statusCode(expectedStatusCode);
		given.when().body("a").request(REPORT, "/db/gymabi/dav/adressbuecher/-1/-1.vcf").then()
				.statusCode(expectedStatusCode);
		given.when().body("").request(PROPFIND, "/db/gymabi/dav/kalender").then().statusCode(expectedStatusCode);
		given.when().body("").request(PROPFIND, "/db/gymabi/dav/kalender/-1").then().statusCode(expectedStatusCode);
		given.when().body("").request(REPORT, "/db/gymabi/dav/kalender/-1").then().statusCode(expectedStatusCode);
		given.when().contentType("Text/Calendar").body("a").header("If-None-Match", "")
				.put("/db/gymabi/dav/kalender/-1/something-something.ics").then().statusCode(expectedStatusCode);
		given.when().contentType("Text/Calendar").body("a").header("If-Match", "*")
				.put("/db/gymabi/dav/kalender/-1/something-something.ics").then().statusCode(expectedStatusCode);
		given.when().contentType("Text/Calendar").header("If-Match", "*")
				.delete("/db/gymabi/dav/kalender/-1/something-something.ics").then().statusCode(expectedStatusCode);
		given.when().contentType("Text/Calendar").delete("/db/gymabi/dav/kalender/-1").then()
				.statusCode(expectedStatusCode);
	};

	/**
	 * Testmethode prüft ob der Endpunkt für falsches Passwort 403 zurück gibt
	 *
	 * ggf. skippen, da dies nicht die DAV-Api erledigt, sondern der Jetty an
	 * sich
	 *
	 */
	@Test
	void givenInvalidPasswordAllEndpoints_then401() {
		final RequestSpecification givenz = given(user, "---");
		givenGiven_thenStatusCode.accept(givenz, 401);
	}

	/**
	 * Testet einen Benutzer, dem die Benutzerkompetenzen für den Kalender
	 * fehlen
	 */
	@Test
	void givenAllEndpointsAuthenticatedUserMissingPrivileges_then403() {
		final RequestSpecification givenz = given("ALEN", "password");
		givenGiven_thenStatusCode.accept(givenz, 403);
	}

	/**
	 * Testet die relevanten Endpunkte darauf, ob ein User den privaten Eigenen
	 * Kalender eines anderen Users Sehen oder Bearbeiten kann
	 */
	@Test
	void givenPrivilegedUserAccessingOtherUsersData() {
		final String kalender = "db/gymabi/dav/kalender";
		String body = APITestUtil.readStringFromResourceFile("gymabi/dav/kalender/propfind_kalender_collection_207.xml",
				this);
		Response response = given("ANDE", "password").when().body(body).request(PROPFIND, kalender);
		XmlPathWalker path = new XmlPathWalker(response.asString());
		final String gemeinsamerKalender = path.getString(
				"multistatus.response.find {it.propstat[0].prop.displayname == 'Gemeinsamer Kalender'} .href");

		body = APITestUtil.readStringFromResourceFile("gymabi/dav/kalender/propfind_kalender_207.xml", this);
		response = given("Admin", "password").when().body(body).request(PROPFIND, gemeinsamerKalender);
		final String responseBody = response.asString();
		path = new XmlPathWalker(responseBody);
		// only error node
		assertThat("error", path.nodeExists());

	}

	/**
	 * Testet, ob der Nutzer mit Lese aber ohne Schreibrechte die entsprechenden
	 * Priviliges erhält.
	 */
	@Test
	void givenUserReadOnlyCalendar_thenNoWritePermission() {
		// check: Nutzer hat nur Leserechte auf den Gemeinsamen Kalender:
		final String kalender = "db/gymabi/dav/kalender";
		final String body = APITestUtil.readStringFromResourceFile("gymabi/dav/kalender/propfind_kalender_collection_207.xml",
				this);

		final Response response = given("BAGI", "password").when().body(body).request(PROPFIND, kalender);
		final String responseString = response.asString();
		response.then().statusCode(207);
		// erwarte 2 Responses in Multistatus für Admin (1 Kalender Root, 1
		// Eigener Kalender
		final XmlPathWalker path = new XmlPathWalker(responseString);
		final int responses = path.getIntAndUp("multistatus.response.size()");
		assertThat(responses, equalTo(3));
		path.up();
		for (int i = 0; i < responses; i++) {
			final String displayname = path.getStringAndUp("response[" + i + "].propstat[0].prop.displayName");
			if ("Gemeinsamer Kalender".equals(displayname)) {
				assertThat("current-user-privilege-set.privilege.write", not(path.nodeExists()));
				assertThat("read", path.nodeExists());
			} else {
				assertThat(displayname, anyOf(equalTo(""), equalTo("Eigener Kalender")));
			}
		}
	}

	/**
	 * Testet, ob der Gemeinsame Kalender nicht zugeordneten Nutzern nicht
	 * angezeigt wird
	 */
	@Test
	void givenUserNoPermissionCalendar_thenCalendarNotShown() {
		final String kalender = "db/gymabi/dav/kalender";
		final String body = APITestUtil.readStringFromResourceFile("gymabi/dav/kalender/propfind_kalender_collection_207.xml",
				this);

		final Response response = given("Admin", "password").when().body(body).request(PROPFIND, kalender);
		final String responseString = response.asString();
		response.then().statusCode(207);
		// erwarte 2 Responses in Multistatus für Admin (1 Kalender Root, 1
		// Eigener Kalender
		final XmlPathWalker path = new XmlPathWalker(responseString);
		final int responses = path.getIntAndUp("multistatus.response.size()");
		assertThat(responses, equalTo(2));
		assertThat(
				path.getIntAndUp(
						"multistatus.response.find {it.propstat[0].prop.displayname == 'Gemeinsamer Kalender'}.size()"),
				equalTo(0));
	}
}
