package de.svws_nrw.schuldatei.v1;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.schuldatei.v1.data.Schuldatei;
import de.svws_nrw.schuldatei.v1.utils.SchuldateiManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden für den Zugriff auf die Schuldatei
 * entweder lokal über die Resourcen oder über den Webservice zur
 * Verfügung.
 */
public final class SchuldateiReader {

	/** Der Dateipfad in den Ressourcen, wo die lokale Version der Schuldatei liegt */
	public static final @NotNull String filenameSchuldatei = "de/svws_nrw/schuldatei/v1/data/Schuldatei.json";


	/**
	 * Erstellt einen neuen {@link SchuldateiManager} mithilfe der Daten aus
	 * den Resourcen.
	 *
	 * @return der Manager oder im Fehlerfall null
	 */
	public static SchuldateiManager getManagerLokal() {
		try {
			final String json = ResourceUtils.text(filenameSchuldatei);
			final ObjectMapper mapper = new ObjectMapper();
			final Schuldatei daten = mapper.readValue(json, Schuldatei.class);
			return new SchuldateiManager(daten);
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
