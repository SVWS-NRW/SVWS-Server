package de.svws_nrw.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.base.compression.GZip;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse enthält Routinen für das Mapping von einfachen Datentypen in das JSON-Format
 * und umgekehrt. Diese Routinen dienen der RFC 8259-Kompatibiltät, welche von der RestEasy-Implementierung
 * in der Standard-Konfiguration nicht korrekt umgesetzt wird.
 */
public final class JSONMapper {

	private JSONMapper() {
		throw new IllegalStateException("Instantiation of " + JSONMapper.class.getName() + " not allowed");
	}

	/** Der Jackson2-Objekt-Mapper für das Konvertieren */
	public static final ObjectMapper mapper = new ObjectMapper();

	/** Der Formatter für TimeStamp-Strings */
	public static final DateTimeFormatter tsFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss")
			.appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true).toFormatter();


	/**
	 * Formatiert die Ausgabe und gibt den Namen des Attributes dabei mit an.
	 *
	 * @param message    die Nachricht
	 * @param attrName   der Name des Attributes, sofern bekannt - sonst null
	 *
	 * @return die ggf. angepasste Nachricht
	 */
	private static String formatMessage(final String message, final String attrName) {
		if ((attrName == null) || (attrName.isBlank()))
			return message;
		return "Attribut %s: %s".formatted(attrName, message);
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-String um.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return der Java-String
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static String toString(final InputStream in) throws ApiOperationException {
		try {
			return mapper.readValue(in, String.class);
		} catch (@SuppressWarnings("unused") final IOException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren des JSON-Textes");
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-Long um.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return der Java-Long
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Long toLong(final InputStream in) throws ApiOperationException {
		final String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
		try {
			return Long.parseLong(text);
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren des JSON-Textes in einen Long-Wert");
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-Boolean um.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return der Java-Boolean
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Boolean toBoolean(final InputStream in) throws ApiOperationException {
		final String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
		if ("true".equals(text))
			return true;
		if ("false".equals(text))
			return false;
		throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren des JSON-Textes in einen Boolean-Wert");
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-Double um.
	 *
	 * @param in                  der Input-Stream mit dem JSON-Input
	 * @param rfc8259compliance   gibt an, ob nur Zahlwerte laut RFC8259 zugelassen sind oder alle
	 *                            Java-Double-Werte laut {@link Double#valueOf(String)}
	 *
	 * @return der Java-Double
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Double toDouble(final InputStream in, final boolean rfc8259compliance) throws ApiOperationException {
		final String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
		try {
			if (rfc8259compliance && !text.matches("-*(0|[1-9]\\d*)([.]\\d+)?([eE][+-]\\d*)?"))
				throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren des JSON-Textes nach RFC 8259 in einen Double-Wert");
			return Double.valueOf(text);
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren des JSON-Textes in einen Double-Wert");
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-Integer um.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return der Java-Integer
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Integer toInteger(final InputStream in) throws ApiOperationException {
		final String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
		try {
			return Integer.parseInt(text);
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren des JSON-Textes");
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in eine Map von Key-Value-Paaren um
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return die Map
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Map<String, Object> toMap(final InputStream in) throws ApiOperationException {
		final String json = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		try {
			return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
				/**/ });
		} catch (final JsonProcessingException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Fehler beim Parsen des JSON-Strings.");
		}
	}


	/**
	 * Liest die Daten aus dem InputStream ein und gibt das JSON als String zurück.
	 *
	 * @param in   der Input-Stream
	 *
	 * @return der String mit dem JSON
	 */
	public static String toJsonString(final InputStream in) {
		return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
	}


	/**
	 * Liest die Daten aus dem InputStream ein und gibt das JSON als String zurück.
	 *
	 * @param in   der Input-Stream
	 *
	 * @return der String mit dem JSON
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static JsonNode toJsonNode(final InputStream in) throws ApiOperationException {
		final String json = toJsonString(in);
		try {
			return mapper.readTree(json);
		} catch (final JsonProcessingException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Fehler beim Parsen des JSON-Strings.");
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in eine Liste von Maps von Key-Value-Paaren um.
	 * Dabei müssen die JSON-Daten ein Array sein.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return die Liste von Maps
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<Map<String, Object>> toMultipleMaps(final InputStream in) throws ApiOperationException {
		try {
			final JsonNode node = toJsonNode(in);
			final List<Map<String, Object>> result = new ArrayList<>();
			if (!node.isArray())
				throw new ApiOperationException(Status.BAD_REQUEST, "Das übergebene JSON ist kein Array bzw. keine Liste");
			for (final JsonNode element : node) {
				final String json = element.toString();
				result.add(mapper.readValue(json, new TypeReference<Map<String, Object>>() {
					/**/
				}));
			}
			return result;
		} catch (final JsonProcessingException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Fehler beim Parsen des JSON-Strings.");
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in eine Liste von Long-Werten um, sofern dies
	 * möglich ist. Ist dies nicht möglich, so wird eine entsprechende ApiOperationException erzeugt.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return die Liste von Long-Werten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<Long> toListOfLong(final InputStream in) throws ApiOperationException {
		final JsonNode node = toJsonNode(in);
		final List<Long> result = new ArrayList<>();
		if (!node.isArray())
			throw new ApiOperationException(Status.BAD_REQUEST, "Das übergebene JSON ist kein Array bzw. keine Liste");
		for (final JsonNode element : node) {
			if (!element.canConvertToLong())
				throw new ApiOperationException(Status.BAD_REQUEST, "Das übergebene JSON-Array enthält auch nicht-Long-Werte");
			result.add(element.asLong());
		}
		return result;
	}


	/**
	 * Konvertiert das übergebene Objekt in einen Double-Wert, sofern es sich um ein
	 * Number-Objekt handelt.
	 *
	 * @param obj   das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 *
	 * @return das konvertierte Double-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Double convertToDouble(final Object obj, final boolean nullable) throws ApiOperationException {
		if (obj == null) {
			if (nullable)
				return null;
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist nicht erlaubt.");
		}
		if (obj instanceof final Float f)
			return f.doubleValue();
		if (obj instanceof final Double d)
			return d.doubleValue();
		if (obj instanceof final Byte b)
			return b.doubleValue();
		if (obj instanceof final Short s)
			return s.doubleValue();
		if (obj instanceof final Integer i)
			return i.doubleValue();
		if (obj instanceof final Long l)
			return l.doubleValue();
		throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren zu Long");
	}

	/**
	 * Konvertiert das übergebene Objekt in einen Long-Wert, sofern es sich um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * @param attrName   der Name des Attributes oder null
	 *
	 * @return das konvertierte Long-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Long convertToLong(final Object obj, final boolean nullable, final String attrName) throws ApiOperationException {
		if ((obj == null) && nullable)
			return null;
		return switch (obj) {
			case final Byte b -> b.longValue();
			case final Short s -> s.longValue();
			case final Integer i -> i.longValue();
			case final Long l -> l;
			case null -> throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Der Wert null ist nicht erlaubt", attrName));
			default -> throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Fehler beim Konvertieren zu Long", attrName));
		};
	}


	/**
	 * Konvertiert das übergebene Objekt in einen Long-Wert, sofern es sich um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 *
	 * @param obj           das zu konvertierende Objekt
	 * @param nullable      gibt an, ob das Ergebnis auch null sein darf oder nicht
	 *
	 * @return das konvertierte Long-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Long convertToLong(final Object obj, final boolean nullable) throws ApiOperationException {
		return convertToLong(obj, nullable, null);
	}


	/**
	 * Konvertiert das übergebene Objekt in einen Integer-Wert, sofern es sich um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * @param attrName   der Name des Attributes oder null
	 *
	 * @return das konvertierte Integer-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Integer convertToInteger(final Object obj, final boolean nullable, final String attrName) throws ApiOperationException {
		if ((obj == null) && nullable)
			return null;
		return switch (obj) {
			case final Byte b -> b.intValue();
			case final Short s -> s.intValue();
			case final Integer i -> i;
			case final Long l -> l.intValue();
			case null -> throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Der Wert null ist nicht erlaubt", attrName));
			default -> throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Fehler beim Konvertieren zu Integer", attrName));
		};
	}

	/**
	 * Konvertiert das übergebene Objekt in einen Integer-Wert, sofern es sich um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 *
	 * @return das konvertierte Integer-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Integer convertToInteger(final Object obj, final boolean nullable) throws ApiOperationException {
		return convertToInteger(obj, nullable, null);
	}


	/**
	 * Konvertiert das übergebene Objekt in einen Integer-Wert, sofern es sich um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 * Dabei wird geprüft, ob der wert innerhalb des übergebenen Intervalls [lower, upper[ liegt
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * @param lower      die untere Intervallgrenze (einschließlich)
	 * @param upper      die obere Intervallgrenze (außschließlich) oder null, wenn es keine
	 *                   obere Grenze gibt
	 *
	 * @return das konvertierte Integer-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Integer convertToIntegerInRange(final Object obj, final boolean nullable, final int lower, final Integer upper) throws ApiOperationException {
		return convertToIntegerInRange(obj, nullable, lower, upper, null);
	}

	/**
	 * Konvertiert das übergebene Objekt in einen Integer-Wert, sofern es sich um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 * Dabei wird geprüft, ob der wert innerhalb des übergebenen Intervalls [lower, upper[ liegt
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * @param lower      die untere Intervallgrenze (einschließlich)
	 * @param upper      die obere Intervallgrenze (außschließlich) oder null, wenn es keine
	 *                   obere Grenze gibt
	 * @param attrName   der Name des Attributes oder null
	 *
	 * @return das konvertierte Integer-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Integer convertToIntegerInRange(final Object obj, final boolean nullable, final int lower, final Integer upper, final String attrName) throws ApiOperationException {
		if (obj == null) {
			if (nullable)
				return null;
			throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Der Wert null ist nicht erlaubt.", attrName));
		}
		if (obj instanceof final Number n) {
			if ((obj instanceof Float) || (obj instanceof Double))
				throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Fehler beim Konvertieren zu Integer:"
						+ " Es handelt sich um einen Fließkommawert, obwohl eine Ganzzahl erwartet wird.", attrName));
			final int value = n.intValue();
			if ((value >= lower) && ((upper == null) || (value < upper)))
				return value;
			throw new ApiOperationException(Status.BAD_REQUEST, formatMessage(
					"Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.", attrName));
		}
		throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Fehler beim Konvertieren zu Integer: Das Objekt ist keine Zahl.", attrName));
	}


	/**
	 * Konvertiert das übergebene Objekt in einen Boolean-Wert.
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 *
	 * @return das konvertierte Boolean-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Boolean convertToBoolean(final Object obj, final boolean nullable) throws ApiOperationException {
		return convertToBoolean(obj, nullable, null);
	}

	/**
	 * Konvertiert das übergebene Objekt in einen Boolean-Wert.
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * @param attrName   der Name des Attributes oder null
	 *
	 * @return das konvertierte Boolean-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Boolean convertToBoolean(final Object obj, final boolean nullable, final String attrName) throws ApiOperationException {
		if (obj == null) {
			if (nullable)
				return null;
			throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Der Wert null ist nicht erlaubt", attrName));
		}
		if (obj instanceof final Boolean b)
			return b;
		throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Fehler beim Konvertieren zu Boolean", attrName));
	}


	/**
	 * Konvertiert das übergebene Objekt in ein LocalDateTime, sofern es sich um ein
	 * LocalDateTime-Objekt handelt.
	 *
	 * @param obj          das zu konvertierende Objekt
	 * @param nullable     gibt an, ob das Ergebnis auch null sein darf oder nicht
	 *
	 * @return das konvertierte LocalDateTime-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static LocalDateTime convertToLocalDateTime(final Object obj, final boolean nullable)
			throws ApiOperationException {
		if (obj == null) {
			if (nullable)
				return null;
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist nicht erlaubt.");
		}
		if (!(obj instanceof String))
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde ein String erwartet, aber keiner übergeben.");
		if ("".equals(obj))
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein leerer String ist hier nicht erlaubt.");
		try {
			return LocalDateTime.parse((String) obj, tsFormatter);
		} catch (final DateTimeParseException dtpe) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Format des Zeitstempels ist ungültig: %s".formatted(dtpe.getMessage()));
		}
	}


	/**
	 * Konvertiert das übergebene Objekt in einen String, sofern es sich um ein
	 * String-Objekt handelt.
	 *
	 * @param obj          das zu konvertierende Objekt
	 * @param nullable     gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * @param allowEmpty   gibt an, ob auch leere Strings erlaubt sind oder nicht
	 * @param maxLength    die maximal erlaubte Länge des Strings, null falls keine Begrenzung vorliegt
	 * @param attrName     der Name des Attributs oder null
	 *
	 * @return das konvertierte Long-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static String convertToString(final Object obj, final boolean nullable, final boolean allowEmpty, final Integer maxLength, final String attrName)
			throws ApiOperationException {
		if (obj == null) {
			if (nullable)
				return null;
			throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Der Wert null ist nicht erlaubt.", attrName));
		}
		if (!(obj instanceof String))
			throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Es wurde ein String erwartet, aber keiner übergeben.", attrName));
		if ("".equals(obj) && !allowEmpty)
			throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Ein leerer String ist hier nicht erlaubt.", attrName));
		final String result = (String) obj;
		if ((maxLength != null) && (result.length() > maxLength))
			throw new ApiOperationException(Status.BAD_REQUEST, formatMessage("Die Länge des Strings ist auf %d Zeichen limitiert."
					.formatted(maxLength), attrName));
		return result;
	}


	/**
	 * Konvertiert das übergebene Objekt in einen String, sofern es sich um ein
	 * String-Objekt handelt.
	 *
	 * @param obj          das zu konvertierende Objekt
	 * @param nullable     gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * @param allowEmpty   gibt an, ob auch leere Strings erlaubt sind oder nicht
	 * @param maxLength    die maximal erlaubte Länge des Strings, null falls keine Begrenzung vorliegt
	 *
	 * @return das konvertierte Long-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static String convertToString(final Object obj, final boolean nullable, final boolean allowEmpty, final Integer maxLength)
			throws ApiOperationException {
		return convertToString(obj, nullable, allowEmpty, maxLength, null);
	}


	/**
	 * Konvertiert das übergebene Objekt in einen boolean-Array, sofern es sich um ein
	 * boolean-Array handelt.
	 *
	 * @param obj          das zu konvertierende Objekt
	 * @param nullable     gibt an, ob Elemente auch null sein dürfen oder nicht
	 * @param size         die verlangte Größe des Arrays
	 *
	 * @return das konvertierte boolean-Array
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@SuppressWarnings("unchecked")
	public static Boolean[] convertToBooleanArray(final Object obj, final boolean nullable, final Integer size)
			throws ApiOperationException {
		if (obj == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist nicht erlaubt.");
		if (!(obj instanceof List))
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde ein Arrays erwartet, aber keines übergeben.");
		final List<Boolean> params = (List<Boolean>) obj;
		if ((size != null) && (size != params.size()))
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde ein Array der Länge " + size + " erwartet, aber eines der Länge " + params.size()
					+ " übergeben.");
		if ((params.isEmpty()) && ((size == null) || (size == 0)))
			return new Boolean[0];
		final Boolean[] result = new Boolean[params.size()];
		for (int i = 0; i < params.size(); i++) {
			final Boolean pvalue = params.get(i);
			if (!nullable && (pvalue == null))
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist in diesem Array nicht erlaubt.");
			result[i] = pvalue;
		}
		return result;
	}


	/**
	 * Konvertiert das übergebene Objekt in einen String-Array, sofern es sich um ein
	 * String-Array handelt.
	 *
	 * @param obj          das zu konvertierende Objekt
	 * @param nullable     gibt an, ob Elemente auch null sein dürfen oder nicht
	 * @param size         die verlangte Größe des Arrays
	 *
	 * @return das konvertierte String-Array
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@SuppressWarnings("unchecked")
	public static String[] convertToStringArray(final Object obj, final boolean nullable, final Integer size)
			throws ApiOperationException {
		if (obj == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist nicht erlaubt.");
		if (!(obj instanceof List))
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde ein Arrays erwartet, aber keines übergeben.");
		final List<String> params = (List<String>) obj;
		if ((size != null) && (size != params.size()))
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde ein Array der Länge " + size + " erwartet, aber eines der Länge " + params.size()
					+ " übergeben.");
		if ((params.isEmpty()) && ((size == null) || (size == 0)))
			return new String[0];
		final String[] result = new String[params.size()];
		for (int i = 0; i < params.size(); i++) {
			final String pvalue = params.get(i);
			if (!nullable && (pvalue == null))
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist in diesem Array nicht erlaubt.");
			result[i] = pvalue;
		}
		return result;
	}


	/**
	 * Konvertiert das übergebene Objekt in eine Liste von Long-Werten, sofern es sich beim Inhalt um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 *
	 * @param listObj    das zu konvertierende ListenObjekt
	 * @param nullable   falls null für das Listen-Objekt gültig ist
	 *
	 * @return das konvertierte Listen-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<Long> convertToListOfLong(final Object listObj, final boolean nullable) throws ApiOperationException {
		if (listObj == null) {
			if (nullable)
				return null;
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist nicht erlaubt.");
		}
		final List<Long> result = new ArrayList<>();
		if (listObj instanceof final List<?> liste) {
			for (final Object obj : liste) {
				if (obj == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist innerhalb der Liste nicht erlaubt.");
				switch (obj) {
					case final Byte b -> result.add(b.longValue());
					case final Short s -> result.add(s.longValue());
					case final Integer i -> result.add(i.longValue());
					case final Long l -> result.add(l);
					default -> throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren zu Long");
				}
			}
		} else
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wird eine Array von Long-Werten erwartet.");
		return result;
	}


	/**
	 * Konvertiert das übergebene Objekt in eine Liste von Integer-Werten, sofern es sich beim Inhalt um ein
	 * Number-Objekt handelt, welches keinen long, float oder double-Wert repräsentiert.
	 *
	 * @param listObj    das zu konvertierende ListenObjekt
	 * @param nullable   falls null für das Listen-Objekt gültig ist
	 *
	 * @return das konvertierte Listen-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<Integer> convertToListOfInteger(final Object listObj, final boolean nullable) throws ApiOperationException {
		if (listObj == null) {
			if (nullable)
				return null;
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist nicht erlaubt.");
		}
		final List<Integer> result = new ArrayList<>();
		if (listObj instanceof final List<?> liste) {
			for (final Object obj : liste) {
				if (obj == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist innerhalb der Liste nicht erlaubt.");
				switch (obj) {
					case final Byte b -> result.add(b.intValue());
					case final Short s -> result.add(s.intValue());
					case final Integer i -> result.add(i);
					default -> throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren zu Integer");
				}
			}
		} else
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wird eine Array von Integer-Werten erwartet.");
		return result;
	}


	/**
	 * Konvertiert das übergebene Objekt in eine Liste von DTOs, sofern es sich beim Inhalt um ein
	 * ensprechendes DTO-Objekt handelt.
	 *
	 * @param <T>        der DTO-Typ
	 * @param dtoClass   das Klassen-Objekt des DTO-Typs
	 * @param listObj    das zu konvertierende ListenObjekt
	 * @param nullable   falls null für das Listen-Objekt gültig ist
	 *
	 * @return das konvertierte Listen-Objekt
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static <T> List<T> convertToList(final Class<T> dtoClass, final Object listObj, final boolean nullable) throws ApiOperationException {
		if (listObj == null) {
			if (nullable)
				return null;
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist nicht erlaubt.");
		}
		final List<T> result = new ArrayList<>();
		if (listObj instanceof final List<?> liste) {
			for (final Object obj : liste) {
				try {
					final String str = mapper.writer().writeValueAsString(obj);
					final T value = mapper.reader().forType(dtoClass).readValue(str);
					result.add(value);
				} catch (final IOException e) {
					throw new ApiOperationException(Status.BAD_REQUEST, e, "Fehler beim Konvertieren zu dem DTO-Typ");
				}
			}
		} else
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wird eine Array von DTOs erwartet.");
		return result;
	}


	/**
	 * Konvertiert den Java-String in eine {@link Response} mit dem JSON-String.
	 *
	 * @param data   der zu konvertierende String
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response fromString(final String data) throws ApiOperationException {
		try {
			return Response.ok((data == null) ? null : mapper.writeValueAsString(data), MediaType.APPLICATION_JSON).build();
		} catch (final JsonProcessingException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler bei der Umwandlung des Strings in einen JSON-String");
		}
	}


	/**
	 * Konvertiert den Java-Long-Wert in eine {@link Response} mit dem JSON-String.
	 *
	 * @param data   der zu konvertierende Long-Wert
	 *
	 * @return die Response
	 */
	public static Response fromLong(final Long data) {
		return Response.ok((data == null) ? null : data.toString(), MediaType.APPLICATION_JSON).build();
	}


	/**
	 * Konvertiert den Java-Boolean-Wert in eine {@link Response} mit dem JSON-String.
	 *
	 * @param data   der zu konvertierende Boolean-Wert
	 *
	 * @return die Response
	 */
	public static Response fromBoolean(final Boolean data) {
		return Response.ok((data == null) ? null : data.toString(), MediaType.APPLICATION_JSON).build();
	}


	/**
	 * Konvertiert den Java-Long-Wert in eine {@link Response} mit dem JSON-String.
	 *
	 * @param data                der zu konvertierende Double-Wert
	 * @param rfc8259compliance   gibt an, ob nur Zahlwerte laut RFC8259 zugelassen sind oder alle
	 *                            Java-Double-Werte laut {@link Double#valueOf(String)}
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response fromDouble(final Double data, final boolean rfc8259compliance) throws ApiOperationException {
		String text = null;
		if (data != null) {
			text = data.toString();
			if (!text.matches("-*(0|[1-9]\\d*)([.]\\d+)?([eE][+-]\\d*)?"))
				throw new ApiOperationException(Status.BAD_REQUEST, "Fehler beim Konvertieren des Double-Wertes in einen JSON-Text nach RFC 8259");
		}
		return Response.ok((data == null) ? null : data.toString(), MediaType.APPLICATION_JSON).build();
	}


	/**
	 * Konvertiert den Java-Long-Wert in eine {@link Response} mit dem JSON-String.
	 *
	 * @param data   der zu konvertierende Integer-Wert
	 *
	 * @return die Response
	 */
	public static Response fromInteger(final Integer data) {
		return Response.ok((data == null) ? null : data.toString(), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Konvertiert das Object in ein byte-Array mit dem GZIP-komprimierten JSON-String.
	 *
	 * @param obj        das nach JSON zu serialisierende Objekt
	 *
	 * @return die Response
	 *
	 * @throws CompressionException   falls ein Fehler beim Komprimieren auftritt
	 */
	public static byte[] gzipByteArrayFromObject(final Object obj) throws CompressionException {
		return GZip.encodeData(gzipOut -> mapper.writeValue(gzipOut, obj));
	}

	/**
	 * Konvertiert das Object in eine {@link Response} mit einer GZIP-komprimierten JSON-Datei.
	 *
	 * @param obj        das nach JSON zu serialisierende Objekt
	 * @param filename   der Name, der JSON-Datei ohne ".json.gz"-Endung
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response gzipFileResponseFromObject(final Object obj, final String filename) throws ApiOperationException {
		try {
			return Response.ok(gzipByteArrayFromObject(obj)).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
		} catch (final CompressionException ce) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ce);
		}
	}


	/**
	 * Wandelt die JSON-Daten, welche GZip-komprimiert sein müssen,
	 * in das Object vom Typ T um
	 *
	 * @param <T>         der Typ des Objekts
	 * @param encoded     die komprimierten JSON-Daten
	 * @param valueType   die Klasse des Typs T
	 *
	 * @return das Object vom Typ T
	 *
	 * @throws CompressionException falls beim Dekomprimieren der Daten ein Fehler auftritt.
	 */
	public static <T> T toObjectGZip(final byte[] encoded, final Class<T> valueType) throws CompressionException {
		try {
			final byte[] daten = GZip.decode(encoded);
			return mapper.readValue(daten, valueType);
		} catch (IOException | CompressionException e) {
			if (e instanceof final CompressionException ce)
				throw ce;
			throw new CompressionException("Fehler beim Deserialisieren der JSON-Daten.", e);
		}
	}

}
