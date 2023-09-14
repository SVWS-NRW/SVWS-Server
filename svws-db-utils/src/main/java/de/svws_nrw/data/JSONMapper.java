package de.svws_nrw.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.base.compression.GZip;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-String um.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return der Java-String
	 */
	public static String toString(final InputStream in) {
	    try {
			return mapper.readValue(in, String.class);
		} catch (@SuppressWarnings("unused") final IOException e) {
			throw new WebApplicationException("Fehler beim Konvertieren des JSON-Textes", Response.Status.BAD_REQUEST);
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-Long um.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return der Java-Long
	 */
	public static Long toLong(final InputStream in) {
		final String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
	    try {
	    	return Long.parseLong(text);
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			throw new WebApplicationException("Fehler beim Konvertieren des JSON-Textes in einen Long-Wert", Response.Status.BAD_REQUEST);
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-Boolean um.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return der Java-Boolean
	 */
	public static Boolean toBoolean(final InputStream in) {
		final String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
		if ("true".equals(text))
			return true;
		if ("false".equals(text))
			return false;
		throw new WebApplicationException("Fehler beim Konvertieren des JSON-Textes in einen Boolean-Wert", Response.Status.BAD_REQUEST);
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-Double um.
	 *
	 * @param in                  der Input-Stream mit dem JSON-Input
	 * @param rfc8259compliance   gibt an, ob nur Zahlwerte laut RFC8259 zugelassen sind oder alle
	 *                            Java-Double-Werte laut {@link Double#valueOf(String)}
	 *
	 * @return der Java-Double
	 */
	public static Double toDouble(final InputStream in, final boolean rfc8259compliance) {
		final String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
	    try {
			if (rfc8259compliance && !text.matches("-*(0|[1-9]\\d*)([.]\\d+)?([eE][+-]\\d*)?"))
				throw new WebApplicationException("Fehler beim Konvertieren des JSON-Textes nach RFC 8259 in einen Double-Wert", Response.Status.BAD_REQUEST);
	    	return Double.valueOf(text);
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			throw new WebApplicationException("Fehler beim Konvertieren des JSON-Textes in einen Double-Wert", Response.Status.BAD_REQUEST);
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-Integer um.
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return der Java-Integer
	 */
	public static Integer toInteger(final InputStream in) {
		final String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
	    try {
			return Integer.parseInt(text);
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			throw new WebApplicationException("Fehler beim Konvertieren des JSON-Textes", Response.Status.BAD_REQUEST);
		}
	}


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in eine Map von Key-Value-Paaren um
	 *
	 * @param in   der Input-Stream mit dem JSON-Input
	 *
	 * @return die Map
	 */
	public static Map<String, Object> toMap(final InputStream in) {
		final String json = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		try {
			return mapper.readValue(json, new TypeReference<Map<String, Object>>() { /**/ });
		} catch (final JsonProcessingException e) {
			throw new WebApplicationException("Fehler beim Parsen des JSON-Strings.", e, Response.Status.BAD_REQUEST);
		}
	}



	/**
	 * Konvertiert das übergeben Objekt in einen Double-Wert, sofern es sich um ein
	 * Number-Objekt handelt.
	 *
	 * @param obj   das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 *
	 * @return das konvertierte Double-Objekt
	 */
	public static Double convertToDouble(final Object obj, final boolean nullable) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
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
		throw new WebApplicationException("Fehler beim Konvertieren zu Long", Response.Status.BAD_REQUEST);
	}


	/**
	 * Konvertiert das übergeben Objekt in einen Long-Wert, sofern es sich um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 *
	 * @param obj   das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 *
	 * @return das konvertierte Long-Objekt
	 */
	public static Long convertToLong(final Object obj, final boolean nullable) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (obj instanceof final Byte b)
			return b.longValue();
		if (obj instanceof final Short s)
			return s.longValue();
		if (obj instanceof final Integer i)
			return i.longValue();
		if (obj instanceof final Long l)
			return l.longValue();
		throw new WebApplicationException("Fehler beim Konvertieren zu Long", Response.Status.BAD_REQUEST);
	}


	/**
	 * Konvertiert das übergeben Objekt in einen Integer-Wert, sofern es sich um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 *
	 * @return das konvertierte Integer-Objekt
	 */
	public static Integer convertToInteger(final Object obj, final boolean nullable) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (obj instanceof final Byte b)
			return b.intValue();
		if (obj instanceof final Short s)
			return s.intValue();
		if (obj instanceof final Integer i)
			return i.intValue();
		if (obj instanceof final Long l)
			return l.intValue();
		throw new WebApplicationException("Fehler beim Konvertieren zu Integer", Response.Status.BAD_REQUEST);
	}


	/**
	 * Konvertiert das übergeben Objekt in einen Integer-Wert, sofern es sich um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 * Dabei wird geprüft, ob der wert innerhalb des übergebenen Intervalls [lower, upper[ liegt
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * @param lower      die untere Intervallgrenze (einschließlich)
	 * @param upper      die obere Intervallgrenze (außschließlich)
	 *
	 * @return das konvertierte Integer-Objekt
	 */
	public static Integer convertToIntegerInRange(final Object obj, final boolean nullable, final int lower, final int upper) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (obj instanceof final Number n) {
			if ((obj instanceof Float) || (obj instanceof Double))
				throw new WebApplicationException("Fehler beim Konvertieren zu Integer: Es handelt sich um einen Fließkommawert, obwohl eine Ganzzahl erwartet wird.", Response.Status.BAD_REQUEST);
			final int value = n.intValue();
			if ((value >= lower) && (value < upper))
				return value;
			throw new WebApplicationException("Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.", Response.Status.BAD_REQUEST);
		}
		throw new WebApplicationException("Fehler beim Konvertieren zu Integer: Das Objekt ist keine Zahl.", Response.Status.BAD_REQUEST);
	}


	/**
	 * Konvertiert das übergeben Objekt in einen Boolean-Wert.
	 *
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 *
	 * @return das konvertierte Boolean-Objekt
	 */
	public static Boolean convertToBoolean(final Object obj, final boolean nullable) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (obj instanceof final Boolean b)
			return b;
		throw new WebApplicationException("Fehler beim Konvertieren zu Boolean", Response.Status.BAD_REQUEST);
	}


	/**
	 * Konvertiert das übergeben Objekt in einen String, sofern es sich um ein
	 * String-Objekt handelt.
	 *
	 * @param obj          das zu konvertierende Objekt
	 * @param nullable     gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * @param allowEmpty   gibt an, ob auch leere Strings erlaubt sind oder nicht
	 * @param maxLength    die maximal erlaubte Länge des Strings, null falls keine Begrenzung vorliegt
	 *
	 * @return das konvertierte Long-Objekt
	 */
	public static String convertToString(final Object obj, final boolean nullable, final boolean allowEmpty, final Integer maxLength) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (!(obj instanceof String))
			throw new WebApplicationException("Es wurde ein String erwartet, aber keiner übergeben.", Response.Status.BAD_REQUEST);
		if ("".equals(obj) && !allowEmpty)
			throw new WebApplicationException("Ein leerer String ist hier nicht erlaubt.", Response.Status.BAD_REQUEST);
		final String result = (String) obj;
		if ((maxLength != null) && (result.length() > maxLength))
			throw new WebApplicationException("Die Länge des Strings ist auf " + maxLength + " Zeichen limitiert.", Response.Status.BAD_REQUEST);
		return result;
	}


	/**
	 * Konvertiert das übergeben Objekt in einen boolean-Array, sofern es sich um ein
	 * boolean-Array handelt.
	 *
	 * @param obj          das zu konvertierende Objekt
	 * @param nullable     gibt an, ob Elemente auch null sein dürfen oder nicht
	 * @param allowEmpty   gibt an, ob auch leere Arrays erlaubt sind oder nicht
	 * @param size         die verlangte Größe des Arrays
	 *
	 * @return das konvertierte boolean-Array
	 */
	public static Boolean[] convertToBooleanArray(final Object obj, final boolean nullable, final boolean allowEmpty, final Integer size) {
		if (obj == null)
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		if (!(obj instanceof List))
			throw new WebApplicationException("Es wurde ein Arrays erwartet, aber keines übergeben.", Response.Status.BAD_REQUEST);
		@SuppressWarnings("unchecked")
		final
		List<Boolean> params = (List<Boolean>) obj;
		if ((size != null) && (size != params.size()))
			throw new WebApplicationException("Es wurde ein Array der Länge " + size + " erwartet, aber eines der Länge " + params.size() + " übergeben.", Response.Status.BAD_REQUEST);
		if ((params.isEmpty()) && ((size == null) || (size == 0)))
			return new Boolean[0];
		final Boolean[] result = new Boolean[params.size()];
		for (int i = 0; i < params.size(); i++) {
			final Boolean pvalue = params.get(i);
			if (!nullable && (pvalue == null))
				throw new WebApplicationException("Der Wert null ist in diesem Array nicht erlaubt.", Response.Status.BAD_REQUEST);
			result[i] = pvalue;
		}
		return result;
	}


	/**
	 * Konvertiert das übergeben Objekt in einen String-Array, sofern es sich um ein
	 * String-Array handelt.
	 *
	 * @param obj          das zu konvertierende Objekt
	 * @param nullable     gibt an, ob Elemente auch null sein dürfen oder nicht
	 * @param allowEmpty   gibt an, ob auch leere Arrays erlaubt sind oder nicht
	 * @param size         die verlangte Größe des Arrays
	 *
	 * @return das konvertierte String-Array
	 */
	public static String[] convertToStringArray(final Object obj, final boolean nullable, final boolean allowEmpty, final Integer size) {
		if (obj == null)
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		if (!(obj instanceof List))
			throw new WebApplicationException("Es wurde ein Arrays erwartet, aber keines übergeben.", Response.Status.BAD_REQUEST);
		@SuppressWarnings("unchecked")
		final List<String> params = (List<String>) obj;
		if ((size != null) && (size != params.size()))
			throw new WebApplicationException("Es wurde ein Array der Länge " + size + " erwartet, aber eines der Länge " + params.size() + " übergeben.", Response.Status.BAD_REQUEST);
		if ((params.isEmpty()) && ((size == null) || (size == 0)))
			return new String[0];
		final String[] result = new String[params.size()];
		for (int i = 0; i < params.size(); i++) {
			final String pvalue = params.get(i);
			if (!nullable && (pvalue == null))
				throw new WebApplicationException("Der Wert null ist in diesem Array nicht erlaubt.", Response.Status.BAD_REQUEST);
			result[i] = pvalue;
		}
		return result;
	}


	/**
	 * Konvertiert das übergeben Objekt in eine Liste von Long-Werten, sofern es sich beim Inhalt um ein
	 * Number-Objekt handelt, welches keinen float oder double-Wert repräsentiert.
	 *
	 * @param listObj    das zu konvertierende ListenObjekt
	 * @param nullable   falls null für das Listen-Objekt gültig ist
	 *
	 * @return das konvertierte Listen-Objekt
	 */
	public static List<Long> convertToListOfLong(final Object listObj, final boolean nullable) {
		if (listObj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		final List<Long> result = new ArrayList<>();
		if (listObj instanceof final List<?> liste) {
			for (final Object obj : liste) {
				if (obj == null)
					throw new WebApplicationException("Der Wert null ist innerhalb der Liste nicht erlaubt.", Response.Status.BAD_REQUEST);
				if (obj instanceof final Byte b)
					result.add(b.longValue());
				else if (obj instanceof final Short s)
					result.add(s.longValue());
				else if (obj instanceof final Integer i)
					result.add(i.longValue());
				else if (obj instanceof final Long l)
					result.add(l.longValue());
				else
					throw new WebApplicationException("Fehler beim Konvertieren zu Long", Response.Status.BAD_REQUEST);
			}
		} else
			throw new WebApplicationException("Es wird eine Array von Long-Werten erwartet.", Response.Status.BAD_REQUEST);
		return result;
	}


	/**
	 * Konvertiert den Java-String in eine {@link Response} mit dem JSON-String.
	 *
	 * @param data   der zu konvertierende String
	 *
	 * @return die Response
	 */
	public static Response fromString(final String data) {
		try {
			return Response.ok((data == null) ? null : mapper.writeValueAsString(data), MediaType.APPLICATION_JSON).build();
		} catch (final JsonProcessingException e) {
			throw new WebApplicationException("Fehler bei der Umwandlung des Strings in einen JSON-String", e, Response.Status.INTERNAL_SERVER_ERROR);
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
	 */
	public static Response fromDouble(final Double data, final boolean rfc8259compliance) {
		String text = null;
		if (data != null) {
			text = data.toString();
			if (!text.matches("-*(0|[1-9]\\d*)([.]\\d+)?([eE][+-]\\d*)?"))
				throw new WebApplicationException("Fehler beim Konvertieren des Double-Wertes in einen JSON-Text nach RFC 8259", Response.Status.BAD_REQUEST);
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
	 */
	public static Response gzipFileResponseFromObject(final Object obj, final String filename) {
		try {
			return Response.ok(gzipByteArrayFromObject(obj)).header("Content-Disposition", "attachment; filename=\"" + filename + "\"").build();
		} catch (final CompressionException ce) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception(ce);
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
