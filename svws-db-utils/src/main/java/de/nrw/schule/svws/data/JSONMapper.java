package de.nrw.schule.svws.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Diese Klasse enthält Routinen für das Mapping von einfachen Datentypen in das JSON-Format 
 * und umgekehrt. Diese Routinen dienen der RFC 8259-Kompatibiltät, welche von der RestEasy-Implementierung
 * in der Standard-Konfiguration nicht korrekt umgesetzt wird.     
 */
public class JSONMapper {


	/** Der Jackson2-Objekt-Mapper für das Konvertieren */
	public static ObjectMapper mapper = new ObjectMapper(); 	


	/**
	 * Wandelt die JSON-Daten aus dem {@link InputStream} in einen einfache Java-String um.
	 * 
	 * @param in   der Input-Stream mit dem JSON-Input
	 * 
	 * @return der Java-String
	 */
	public static String toString(InputStream in) {
	    try {
			return mapper.readValue(in, String.class);
		} catch (@SuppressWarnings("unused") IOException e) {
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
	public static Long toLong(InputStream in) {
		String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
	    try {
	    	return Long.parseLong(text);
		} catch (@SuppressWarnings("unused") NumberFormatException e) {
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
	public static Boolean toBoolean(InputStream in) {
		String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
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
	public static Double toDouble(InputStream in, boolean rfc8259compliance) {
		String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
	    try {
			if (rfc8259compliance && !text.matches("-*(0|[1-9][0-9]*)([.][0-9]+)?([eE][+-][0-9]*)?"))
				throw new WebApplicationException("Fehler beim Konvertieren des JSON-Textes nach RFC 8259 in einen Double-Wert", Response.Status.BAD_REQUEST); 
	    	return Double.valueOf(text);
		} catch (@SuppressWarnings("unused") NumberFormatException e) {
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
	public static Integer toInteger(InputStream in) {
		String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		if ((text == null) || "".equals(text) || "null".equals(text))
			return null;
	    try {
			return Integer.parseInt(text);
		} catch (@SuppressWarnings("unused") NumberFormatException e) {
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
	public static Map<String, Object> toMap(InputStream in) {
		String json = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("")).trim();
		try {
			return mapper.readValue(json, new TypeReference<Map<String,Object>>(){/**/});
		} catch (JsonProcessingException e) {
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
	public static Double convertToDouble(Object obj, boolean nullable) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (obj instanceof Float f)
			return f.doubleValue();
		if (obj instanceof Double d)
			return d.doubleValue();
		if (obj instanceof Byte b)
			return b.doubleValue();
		if (obj instanceof Short s)
			return s.doubleValue();
		if (obj instanceof Integer i)
			return i.doubleValue();
		if (obj instanceof Long l)
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
	public static Long convertToLong(Object obj, boolean nullable) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (obj instanceof Byte b)
			return b.longValue();
		if (obj instanceof Short s)
			return s.longValue();
		if (obj instanceof Integer i)
			return i.longValue();
		if (obj instanceof Long l)
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
	public static Integer convertToInteger(Object obj, boolean nullable) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (obj instanceof Byte b)
			return b.intValue();
		if (obj instanceof Short s)
			return s.intValue();
		if (obj instanceof Integer i)
			return i.intValue();
		if (obj instanceof Long l)
			return l.intValue();
		throw new WebApplicationException("Fehler beim Konvertieren zu Integer", Response.Status.BAD_REQUEST);
	}

	
	/**
	 * Konvertiert das übergeben Objekt in einen Boolean-Wert.
	 * 
	 * @param obj        das zu konvertierende Objekt
	 * @param nullable   gibt an, ob das Ergebnis auch null sein darf oder nicht
	 * 
	 * @return das konvertierte Boolean-Objekt
	 */
	public static Boolean convertToBoolean(Object obj, boolean nullable) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (obj instanceof Boolean b)
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
	 * 
	 * @return das konvertierte Long-Objekt
	 */
	public static String convertToString(Object obj, boolean nullable, boolean allowEmpty) {
		if (obj == null) {
			if (nullable)
				return null;
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		}
		if (!(obj instanceof String))
			throw new WebApplicationException("Es wurde ein String erwartet, aber keiner übergeben.", Response.Status.BAD_REQUEST);
		if ("".equals(obj) && !allowEmpty)
			throw new WebApplicationException("Ein leerer String ist hier nicht erlaubt.", Response.Status.BAD_REQUEST);
		return (String)obj;
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
	public static Boolean[] convertToBooleanArray(Object obj, boolean nullable, boolean allowEmpty, Integer size) {
		if (obj == null)
			throw new WebApplicationException("Der Wert null ist nicht erlaubt.", Response.Status.BAD_REQUEST);
		if (!(obj instanceof List))
			throw new WebApplicationException("Es wurde ein Arrays erwartet, aber keines übergeben.", Response.Status.BAD_REQUEST);
		@SuppressWarnings("unchecked")
		List<? extends Boolean> params = (List<? extends Boolean>)obj;
		if ((size != null) && (size != params.size()))
			throw new WebApplicationException("Es wurde ein Array der Länge " + size + " erwartet, aber eines der Länge " + params.size() + " übergeben.", Response.Status.BAD_REQUEST);
		if ((params.size() == 0) && ((size == null) || (size == 0)))
			return new Boolean[0];
		Boolean[] result = new Boolean[params.size()];
		for (int i = 0; i < params.size(); i++) {
			Boolean pvalue = params.get(i);
			if (!nullable && (pvalue == null))
				throw new WebApplicationException("Der Wert null ist in diesem Array nicht erlaubt.", Response.Status.BAD_REQUEST);
			result[i] = pvalue;
		}
		return result;
	}
	

	/**
	 * Konvertiert den Java-String in eine {@link Response} mit dem JSON-String.
	 * 
	 * @param data   der zu konvertierende String
	 * 
	 * @return die Response
	 */
	public static Response fromString(String data) {
		return Response.ok((data == null) ? null : "\"" + data + "\"", MediaType.APPLICATION_JSON).build();
	}

	
	/**
	 * Konvertiert den Java-Long-Wert in eine {@link Response} mit dem JSON-String.
	 * 
	 * @param data   der zu konvertierende Long-Wert
	 * 
	 * @return die Response
	 */
	public static Response fromLong(Long data) {
		return Response.ok((data == null) ? null : data.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	
	/**
	 * Konvertiert den Java-Boolean-Wert in eine {@link Response} mit dem JSON-String.
	 * 
	 * @param data   der zu konvertierende Boolean-Wert
	 * 
	 * @return die Response
	 */
	public static Response fromBoolean(Boolean data) {
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
	public static Response fromDouble(Double data, boolean rfc8259compliance) {
		String text = null;
		if (data != null) {
			text = data.toString();
			if (!text.matches("-*(0|[1-9][0-9]*)([.][0-9]+)?([eE][+-][0-9]*)?"))
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
	public static Response fromInteger(Integer data) {
		return Response.ok((data == null) ? null : data.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	
}
