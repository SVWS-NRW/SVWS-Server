package de.svws_nrw.test.apitests.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseApiUtil {

	/** HTTP-Methode PROPFIND */
	protected static final String PROPFIND = "PROPFIND";
	/** HTTP-Methode REPORT */
	protected static final String REPORT = "REPORT";
	/** Admin-Benutzer in der Gymabi DB */
	protected static final String user = "Admin";
	/** Admin-Passwort in der Gymabi DB */
	protected static final String password = "password";
	/** Time Limit zum Ausführen von Requests */
	protected static final long RESPONSE_TIME_LIMIT = 2000L;

	/**
	 * Eigenschaften des Servers gegen den getestet werden soll (Hostname, Port,
	 * Schema)
	 */
	protected static ServerProps serverProps;

	/** Konstante für den Antwortstatus 200 OK */
	protected static final String HTTP_200_STRING = "HTTP/1.1 200 OK";
	/** Konstante für den Antwortstatus 404 Not Found */
	protected static final String HTTP_404_STRING = "HTTP/1.1 404 Not Found";

	/**
	 * Utility für RestAssured {@link RequestSpecification} mit user, password.
	 * Als contentType wird 'application/xml' genutzt. Nutzt Host und Port der
	 * {@link #serverProps}
	 *
	 * @param user     der Benutzer für die BasicAuth
	 * @param password das Passwort für die BasicAut
	 * @return die Requestspezifikation
	 */
	public static RequestSpecification given(String user, String password) {
		return given(user, password, "application/xml");
	}


	/**
	 * Utility für RestAssured {@link RequestSpecification} mit user, password,
	 * contentType. Nutzt Host und Port der {@link #serverProps}
	 *
	 * @param user        der Benutzer für die BasicAuth
	 * @param password    das Passwort für die BasicAuth
	 * @param contentType der Contenttype
	 * @return die Requestspezifikation
	 */
	protected static RequestSpecification given(String user, String password, String contentType) {
		return RestAssured.given().baseUri(serverProps.getHost()).port(serverProps.getPort()).auth()
				.basic(user, password).log().all().relaxedHTTPSValidation().contentType(contentType);
	}
	/**
	 * Initialisiert die Server Properties
	 *
	 * @throws FileNotFoundException vgl.
	 *                               {@link ServerProps#createFromSystemProperties()}
	 * @throws IOException           vgl.
	 *                               {@link ServerProps#createFromSystemProperties()}
	 */
	@BeforeAll
	static void initializeProperties() throws FileNotFoundException, IOException {
		serverProps = ServerProps.createFromSystemProperties();
	}
}
