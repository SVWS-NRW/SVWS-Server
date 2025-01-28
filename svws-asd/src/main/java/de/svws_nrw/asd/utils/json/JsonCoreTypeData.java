package de.svws_nrw.asd.utils.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.utils.CoreTypeDataManager;

/**
 * Eine Klasse, um die JSON-Daten für einen Core-Type zu deserialisieren.
 * Sie stellt dann die Version des Core-Types zur Verfügung und eine
 * Map mit der Zuordnung von einem Bezeichner zu den einzelnen Daten
 * in der Historie des Core-Types.
 *
 * @param <T> der Typ der Historien-Einträge
 */
public class JsonCoreTypeData<T extends CoreTypeData> {

	/** Der Jackson2-Objekt-Mapper für das Konvertieren */
	private static final ObjectMapper mapper = new ObjectMapper();

	/** Die Version der Core-Type-Daten */
	private long _version;

	/** Die Klasse zum Json, mit der diese Instanz initialisiert wurde */
	private final Class<T> _clazz;

	/** Eine Map mit den Bezeichnern und den zugeordneten Daten des Core-Types (Historien) */
	private final Map<String, List<T>> _mapData = new LinkedHashMap<>();

	/** Eine Map mit den Bezeichnern und den zugeordneten Historien-IDs */
	private final Map<String, Long> _mapHistorienIDs = new LinkedHashMap<>();


	/**
	 * Liest aus dem übergebenen JSON die Daten zu dem Core-Type ein.
	 *
	 * @param json    der JSON-String mit den Core-Type-Daten
	 * @param clazz   die Klasse der zu lesenden Core-Type-Historien-Einträge
	 *
	 * @throws IOException   wenn das Erstellen der JSON-Core-Type-Daten fehlschlägt
	 */
	public JsonCoreTypeData(final String json, final Class<T> clazz) throws IOException {
		_clazz = clazz;
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
		final Set<Long> idsHistorien = new HashSet<>();
		for (final JsonNode eintrag : daten) {
			final JsonNode bezeichner = eintrag.findValue("bezeichner");
			final JsonNode historie = eintrag.findValue("historie");
			final JsonNode idHistorie = eintrag.findValue("idHistorie");
			if ((eintrag.size() != 3) || (bezeichner == null) || (idHistorie == null) || (historie == null) || (!historie.isArray()))
				throw new IOException(
						"Die JSON-Datei muss bei den Einträgen im Daten-Array jeweils ein Objekt mit zwei Attributen \"bezeichner\", \"idHistorie\" und \"historie\" haben, wobei die Historie eine Array von Objekten sein muss.");
			final long tmpIdHistorie = idHistorie.asLong();
			if (idsHistorien.contains(tmpIdHistorie))
				throw new IOException(
						"Die JSON-Datei muss bei den IDs der Historien eindeutige Werte verwenden. Der Wert " + tmpIdHistorie + " kommt mehrfach vor.");
			idsHistorien.add(tmpIdHistorie);
			this._mapHistorienIDs.put(bezeichner.asText(), tmpIdHistorie);
			final var list = new ArrayList<T>();
			this._mapData.put(bezeichner.asText(), list);
			for (final JsonNode obj : historie)
				list.add(mapper.readValue(obj.toString(), clazz));
		}
	}

	/**
	 * Gibt die Version der Core-Type-Daten zurück.
	 *
	 * @return die Version
	 */
	public long getVersion() {
		return this._version;
	}

	/**
	 * Gibt die Map zurück, welche den Bezeichner eine Liste der Daten-Objekte zuordnet.
	 *
	 * @return die Map mit der Zuordnung von Listen von Daten-Objekten zu den Bezeichnern des Core-Types
	 */
	public Map<String, List<T>> getData() {
		return this._mapData;
	}

	/**
	 * Gibt die Map zurück, welche den Bezeichner die ID der zugeordneten Historie zuordnet.
	 *
	 * @return die Map mit der Zuordnung von Historien-IDs zu den Bezeichnern des Core-Types
	 */
	public Map<String, Long> getHistorienIDs() {
		return this._mapHistorienIDs;
	}

	/**
	 * {@link #_version} inkrementieren.
	 */
	public void incVersion() {
		this._version++;
	}

	/**
	 * Serialisiert die enthaltenen Daten in einen JSON-String.
	 *
	 * @return Json-String
	 */
	public String serialize() {
		final OutputStream outputStream = new ByteArrayOutputStream();
		// Identer mit Tab-Einrückung und Unix-LineFeed erstellen.
		DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("\t", "\n");
		// PerryPrinter erstellen und den Indenter zuweisen.
		DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
		printer.indentObjectsWith(indenter);

		// Sorgt für einen formatierten Json-String mittels PrettyPrinter.
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		// Create a new root node
		final ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("version", this._version);
		final ArrayNode datenArrayNode = mapper.createArrayNode();
		final Set<Long> setIDs = new HashSet<>();
		for (final String bezeichner : this._mapData.keySet()) {
			final List<T> listDatenZumBezeichner = this._mapData.get(bezeichner);
			CoreTypeDataManager.checkHistorie(setIDs, _clazz.getSimpleName(), bezeichner, listDatenZumBezeichner);
			final ObjectNode datenNode = mapper.createObjectNode();
			datenNode.put("bezeichner", bezeichner);
			datenNode.put("idHistorie", this._mapHistorienIDs.get(bezeichner));
			datenNode.set("historie", mapper.valueToTree(listDatenZumBezeichner));
			datenArrayNode.add(datenNode);
		}

		rootNode.set("daten", datenArrayNode);
		try {
			// Rausschreiben des RootNode mittels definiertem Printer.
			mapper.writer(printer).writeValue(outputStream, rootNode);
		} catch (final IOException e) {
			throw new CoreTypeException("Serialisieren fehlgeschlagen. Fehler beim schreiben des JSON-String.");
		}

		return outputStream.toString();
	}

	/**
	 * Ersetzt die Daten zu dem übergebenen Bezeichner in der {@link #_mapData} oder
	 * fügt diese hinzu falls der Eintrag neu ist.
	 *
	 * @param bezeichner - Der Bezeichner zu den Daten
	 * @param daten - Dia Daten zum Bezeichner
	 */
	public void setData(final String bezeichner, final List<T> daten) {
		this._mapData.put(bezeichner, daten);
	}

	/**
	 * Ersetzt alle Daten in der {@link #_mapData} durch die übergebenen Daten.
	 *
	 * @param _mapData
	 */
	public void setDataMap(final Map<String, List<T>> _mapData) {
		this._mapData.clear();
		this._mapData.putAll(_mapData);
	}

}
