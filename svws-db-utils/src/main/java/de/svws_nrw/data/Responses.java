package de.svws_nrw.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Utility-Class für das Erzeugen von Responses
 */
public final class Responses {

	/** Der Jackson2-Objekt-Mapper für das Konvertieren */
	public static final ObjectMapper mapper = new ObjectMapper();

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
	 * Erzeugt eine neue Http-Response 200 mit den übergebenen Datenobjekt als Datei mit dem übergebenen Dateinamen.
	 *
	 * @param <T>        der Typ der Daten
	 * @param daten      die Daten
	 * @param filename   der Dateiname
	 *
	 * @return die HTTP-Response
	 */
	public static <T> Response okFile(final T daten, final String filename) {
		return Response.status(Status.OK)
				.header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
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
