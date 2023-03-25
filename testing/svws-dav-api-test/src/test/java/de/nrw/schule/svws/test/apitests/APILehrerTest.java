package de.svws_nrw.test.apitests;

import static org.hamcrest.Matchers.lessThan;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.svws_nrw.test.apitests.util.ServerProps;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * Beispielklasse zum Einsatz von RestAssured zum Testen der API-Endpunkte
 * 
 */
class APILehrerTest {
	/**
	 * Eigenschaften des Servers gegen den getestet werden soll (Hostname, Port,
	 * Schema)
	 */
	private static ServerProps serverProps;
	
	/**
	 * Initialisiert die Server Properties
	 * 
	 * @throws FileNotFoundException vgl. {@link ServerProps#createFromSystemProperties()}
	 * @throws IOException vgl. {@link ServerProps#createFromSystemProperties()}
	 */
	@BeforeAll
	static void initializeProperties() throws FileNotFoundException, IOException {
		serverProps = ServerProps.createFromSystemProperties();
	}

	/**
	 * Test für die GET-Methode auf /lehrer
	 */
	@Test
	void testGetLehrer() {
		RestAssured.given()
				.baseUri(serverProps.getHost())
				.port(serverProps.getPort())
				.basePath("/db/" + serverProps.getSchema())
				.auth().basic("Admin", "password")
				.log().all()
				.relaxedHTTPSValidation()
				.when()
				.get("/lehrer")
				.then()
				// prüfe Statuscode
				.assertThat().statusCode(200)
				// antwort in unter 2 Sekunden
				.time(lessThan(2000L))
				.contentType(ContentType.JSON);
//		.body("", hasItem(null));
	}

	/**
	 * Testmethode für Zugriffsschutz bei GET /lehrer
	 */
	@Test
	void testNoAccess() {
		RestAssured.given()
				.baseUri(serverProps.getHost())
				.port(serverProps.getPort())
				.basePath("/db/" + serverProps.getSchema())
				.log().all()
				.relaxedHTTPSValidation()
				.when()
				.get("/lehrer")
				.then()
				.statusCode(401);

	}
}
