package de.svws_nrw.asd.utils.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.asd.validate.ValidatorFehlerartKontext;


/**
 * Eine Klasse, um die JSON-Daten für die Fehlerart-Kontexte der Validatoren zu deserialisieren.
 * Sie stellt dann eine Map von Validator zu dessen Historieneinträge von Fehlerart-Kontexten zur Verfügung
 */
public class JsonValidatorFehlerartKontextData {

	/** Der Jackson2-Objekt-Mapper für das Konvertieren */
	private static final ObjectMapper mapper = new ObjectMapper();

	/** Die Version der Core-Type-Daten */
	private final long _version;

	/** Eine Map mit den Bezeichnern und den zugeordneten Daten des Core-Types (Historien) */
	private final Map<String, List<ValidatorFehlerartKontext>> _mapData = new LinkedHashMap<>();


	/**
	 * Liest aus dem übergebenen JSON die Daten der Fehlerart-Kontexte der Validatoren ein.
	 *
	 * @param json    der JSON-String mit den Core-Type-Daten
	 *
	 * @throws IOException   wenn das Erstellen der JSON-Core-Type-Daten fehlschlägt
	 */
	public JsonValidatorFehlerartKontextData(final String json) throws IOException {
		// Lade die JSON-Datei und durchwandere das JSON-Objekt als Baumstruktor
		final JsonNode root = mapper.readTree(json);
		// Prüfe den Aufbau der root-Node
		final JsonNode version = root.findValue("version");
		final JsonNode daten = root.findValue("daten");
		if ((root.size() != 2) || (version == null) || (daten == null))
			throw new IOException("Die JSON-Datei muss auf höchster Ebene ein Objekt mit zwei Attributen \"version\" und \"daten\" sein.");
		this._version = version.asLong(-1);
		if (this._version < 0)
			throw new IOException("Die JSON-Datei muss eine gültige Versionsnummer haben.");
		if (!daten.isArray())
			throw new IOException("Die JSON-Datei muss bei dem Attribut \"daten\" ein Array aufweisen.");
		for (final JsonNode eintrag : daten) {
			final JsonNode validator = eintrag.findValue("validator");
			final JsonNode historie = eintrag.findValue("historie");
			if ((eintrag.size() != 2) || (validator == null) || (historie == null) || (!historie.isArray()))
				throw new IOException("Die JSON-Datei muss bei den Einträgen im Daten-Array jeweils ein Objekt mit zwei Attributen \"validator\" und \"historie\" haben, wobei die Historie eine Array von Objekten sein muss.");
			final var list = new ArrayList<ValidatorFehlerartKontext>();
			this._mapData.put(validator.asText(), list);
			for (final JsonNode obj : historie)
				list.add(mapper.readValue(obj.toString(), ValidatorFehlerartKontext.class));
		}
	}


	/**
	 * Gibt die Version der ValidatorLaufeigenschaften-Daten zurück.
	 *
	 * @return die Version
	 */
	public long getVersion() {
		return this._version;
	}

	/**
	 * Gibt die Map zurück, welche den Valid eine Liste der Daten-Objekte zuordnet.
	 *
	 * @return die Map mit der Zuordnung von Listen von Daten-Objekten zu den Bezeichnern des Core-Types
	 */
	public Map<String, List<ValidatorFehlerartKontext>> getData() {
		return this._mapData;
	}

}
