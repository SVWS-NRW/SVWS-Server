package de.svws_nrw.asd.utils.json;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.svws_nrw.asd.validate.ValidatorException;
import de.svws_nrw.asd.validate.ValidatorFehlerartKontext;


/**
 * Eine Klasse, um die JSON-Daten für die Fehlerart-Kontexte der Validatoren zu deserialisieren.
 * Sie stellt dann eine Map von Validator zu dessen Historieneinträgen von Fehlerart-Kontexten zur Verfügung
 */
public class JsonValidatorFehlerartKontextData {

	/** Der Jackson2-Objekt-Mapper für das Konvertieren */
	private static final ObjectMapper mapper = new ObjectMapper();

	/** Eine Map mit der Historie der Kontext-Daten zugeordnet zu dem Klassennamen der Validatoren */
	private final Map<String, List<ValidatorFehlerartKontext>> _mapData = new LinkedHashMap<>();

	/** Eine Map mit den Versionsnummern zugeordnet zu dem Klassennamen der Validatoren */
	private final Map<String, Long> _mapVersions = new LinkedHashMap<>();


	/**
	 * Liest aus den JSON-Dateien die Daten der Fehlerart-Kontexte der Validatoren ein.
	 */
	public JsonValidatorFehlerartKontextData() {
		try {
			final List<Path> jsonFiles = JsonReader.getFilesInPackage("de.svws_nrw.asd.validate", ".json");
			for (final Path path : jsonFiles) {
				// Überspringe Dateien, die nicht mit "Validator" beginnen (z.B. Testdaten)
				if (!path.getFileName().toString().startsWith("Validator")) {
					continue;
				}

				// Lese die JSON-Datei und prüfe die Struktur
				final String fullPath = path.toString().replace('\\', '/');
				final int index = fullPath.indexOf("de/svws_nrw/asd/validate/");
				if (index != -1) {
					final String resourceLocation = fullPath.substring(index);
					final String json = JsonReader.fromResource(resourceLocation);
					readJson(json);
				}
			}
		} catch (final IOException e) {
			throw new ValidatorException("Fehler beim Lesen der JSON-Daten.", e);
		}
	}


	private void readJson(final String json) {
		try {
			final JsonNode eintrag = mapper.readTree(json);
			final JsonNode version = eintrag.findValue("version");
			final JsonNode validator = eintrag.findValue("validator");
			final JsonNode historie = eintrag.findValue("historie");
			// Ignoriere Dateien, welche nicht dieser Struktur haben.
			if ((eintrag.size() != 3) || (version == null) || (validator == null) || (historie == null) || (!historie.isArray())) {
				return;
			}
			if (version.asLong() < 1) {
				throw new ValidatorException("Der Validator-Fehlerart-Kontext muss eine gültige Versionsnummer haben.");
			}
			final var list = new ArrayList<ValidatorFehlerartKontext>();
			this._mapData.put(validator.asText(), list);
			this._mapVersions.put(validator.asText(), version.asLong());
			for (final JsonNode obj : historie) {
				list.add(mapper.readValue(obj.toString(), ValidatorFehlerartKontext.class));
			}
		} catch (final JsonProcessingException e) {
			throw new ValidatorException("Fehler beim Parsen der JSON-Daten.", e);
		}
	}

	/**
	 * Erzeugt eine kombinierte JSON-Datei für API-Zugriffe.
	 *
	 * @return die JSON-Datei als {@link JsonNode}
	 */
	public JsonNode getAsJsonNode() {
		final ObjectNode root = mapper.createObjectNode();
		final ArrayNode datenArray = root.putArray("daten");

		for (final Map.Entry<String, List<ValidatorFehlerartKontext>> entry : _mapData.entrySet()) {
			final com.fasterxml.jackson.databind.node.ObjectNode eintrag = mapper.createObjectNode();
			eintrag.put("version", _mapVersions.get(entry.getKey()));
			eintrag.put("validator", entry.getKey());
			eintrag.set("historie", mapper.valueToTree(entry.getValue()));
			datenArray.add(eintrag);
		}
		return root;
	}

	/**
	 * Gibt die Map zurück, welche den Validatoren die Fehlerart-Kontexte zuordnet.
	 *
	 * @return die Map mit der Zuordnung der Fehlerart-Kontexte zu den Validatoren
	 */
	public Map<String, List<ValidatorFehlerartKontext>> getData() {
		return this._mapData;
	}


	/**
	 * Gibt die Map zurück, welche den Validatoren die Versionen zuordnet.
	 *
	 * @return die Map mit der Zuordnung der Versionen zu den Validatoren
	 */
	public Map<String, Long> getVersions() {
		return this._mapVersions;
	}

}
