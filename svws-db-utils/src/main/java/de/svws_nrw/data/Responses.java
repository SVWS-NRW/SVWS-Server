package de.svws_nrw.data;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Utility-Class für das Erzeugen von Responses
 */
public final class Responses {

	private Responses() {
		// keine Implementierung erlaubt
	}

	/**
	 * Erzeugt eine neue Http-Response 200 mit den übergebenen Datenobjekt als JSON.
	 *
	 * @param <T>     der Typ der Daten
	 * @param daten   die Daten
	 *
	 * @return die HTTP-Response
	 */
	public static <T> Response ok(final T daten) {
		return Response.status(Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(daten)
				.build();
	}


	/**
	 * Erzeugt eine neue Http-Response 201 mit den übergebenen Datenobjekt als JSON.
	 *
	 * @param <T>     der Typ der Daten
	 * @param daten   die Daten
	 *
	 * @return die HTTP-Response
	 */
	public static <T> Response created(final T daten) {
		return Response.status(Status.CREATED)
				.type(MediaType.APPLICATION_JSON)
				.entity(daten)
				.build();
	}


	/**
	 * Erzeugt eine neue Http-Response 204.
	 *
	 * @return die HTTP-Response
	 */
	public static Response noContent() {
		return Response.status(Status.NO_CONTENT).build();
	}

}
