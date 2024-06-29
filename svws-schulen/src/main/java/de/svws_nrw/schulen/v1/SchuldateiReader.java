package de.svws_nrw.schulen.v1;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.schulen.ResourceUtils;
import de.svws_nrw.schulen.v1.data.Schuldatei;
import de.svws_nrw.schulen.v1.data.SchuldateiKataloge;
import de.svws_nrw.schulen.v1.utils.SchuldateiManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden für den Zugriff auf die Schuldatei
 * entweder lokal über die Resourcen oder über den Webservice zur
 * Verfügung.
 */
public final class SchuldateiReader {

	/** Der Dateipfad in den Ressourcen, wo die lokale Version der Schuldatei liegt */
	public static final @NotNull String filenameSchuldatei = "de/svws_nrw/schulen/v1/data/Schuldatei.json";

	/** Der Dateipfad in den Ressourcen, wo die lokale Version der Katalgeinträge der Schuldatei liegt */
	public static final @NotNull String filenameSchuldateiKataloge = "de/svws_nrw/schulen/v1/data/SchuldateiKataloge.json";


	/**
	 * Erstellt einen neuen {@link SchuldateiManager} mithilfe der Daten aus
	 * den Resourcen.
	 *
	 * @return der Manager oder im Fehlerfall null
	 */
	public static SchuldateiManager getManagerLokal() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonSchuldatei = ResourceUtils.text(filenameSchuldatei);
			final Schuldatei schuldatei = mapper.readValue(jsonSchuldatei, Schuldatei.class);
			final String jsonSchuldateiKataloge = ResourceUtils.text(filenameSchuldateiKataloge);
			final SchuldateiKataloge schuldateiKataloge = mapper.readValue(jsonSchuldateiKataloge, SchuldateiKataloge.class);
			return new SchuldateiManager(schuldatei, schuldateiKataloge);
		} catch (@SuppressWarnings("unused") final IOException e) {
			return null;
		}
	}


	// TODO Methoden für den Zugriff auf den Webservice


	/**
	 * Der Standard-Konstruktor
	 */
	private SchuldateiReader() {
		// dieser private Konstruktur verhindert das Instantiieren dieser Klasse über den Standard-Konstruktor
	}

}
