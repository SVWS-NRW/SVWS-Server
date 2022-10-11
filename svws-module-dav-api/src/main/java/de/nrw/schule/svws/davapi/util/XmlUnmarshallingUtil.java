package de.nrw.schule.svws.davapi.util;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Optional;

import jakarta.xml.bind.JAXB;

/**
 * Utility-Klasse für die Deserialisierung von XML in Java-Objekte mittels
 * Jackson. Die enthaltenen Methoden modifizieren das Verhalten der Jackson
 * ObjectMapper, um die JAXB-annotierten Dav-Objekte
 * ({@link de.nrw.schule.svws.davapi.model.dav.Prop}) besser behandeln zu
 * können.
 */
public class XmlUnmarshallingUtil {

	/**
	 * Statische Klasse, privater Konstruktor
	 */
	private XmlUnmarshallingUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Deserialisiert einen InputStream mit XML-Daten in eine Java-Klasse eines
	 * angegebenen Typs. Fehlende XML-Mappings oder fehlerhaftes XML führen in
	 * dieser Methode zu einer IOException.
	 * 
	 * @param inputstream InputStream mit XML-Daten
	 * @param typeClass   Java-Klasse, die das Ziel der Deserialisierung sein soll
	 *                    (z.B. Profind.class)
	 * @param <T>         Generischer Typ
	 * @return Java-Objekt der angegebenen Typ-Klasse
	 */
	public static <T> T unmarshal(InputStream inputstream, Class<T> typeClass) {
		return JAXB.unmarshal(inputstream, typeClass);
	}

	/**
	 * Deserialisiert einen InputStream mit XML-Daten in eine Java-Klasse eines
	 * angegebenen Typs. Fehlende XML-Mappings oder fehlerhaftes XML führen in
	 * dieser Methode nicht zu einer IOException, sondern zur Rückgabe von
	 * Optional.empty(). Diese Methode kann dazu verwendet werden zu prüfen, ob ein
	 * InputStream in die angegebene Typklasse deserialisiert werden kann.
	 * 
	 * @param inputstream InputStream mit XML-Daten
	 * @param typeClass   Java-Klasse, die das Ziel der Deserialisierung sein soll
	 *                    (z.B. Profind.class)
	 * @param <T>         Generischer Typ
	 * @return Optional der angegebenen Typ-Klasse. Falls die Deserialisierung nicht
	 *         erfolgreich war, wird das Optional.empty() zurückgegeben.
	 */
	public static <T> Optional<T> tryUnmarshal(InputStream inputstream, Class<T> typeClass) {
		return Optional.of(unmarshal(inputstream, typeClass));
	}

	/**
	 * Deserialisiert einen String mit XML-Daten in eine Java-Klasse eines
	 * angegebenen Typs. Fehlende XML-Mappings oder fehlerhaftes XML führen in
	 * dieser Methode nicht zu einer IOException, sondern zur Rückgabe von
	 * Optional.empty(). Diese Methode kann dazu verwendet werden zu prüfen, ob ein
	 * String in die angegebene Typklasse deserialisiert werden kann.
	 * 
	 * @param input     String mit XML-Daten
	 * @param typeClass Java-Klasse, die das Ziel der Deserialisierung sein soll
	 *                  (z.B. Profind.class)
	 * @param <T>       Generischer Typ
	 * @return Optional der angegebenen Typ-Klasse. Falls die Deserialisierung nicht
	 *         erfolgreich war, wird das Optional.empty() zurückgegeben.
	 */
	public static <T> Optional<T> tryUnmarshal(String input, Class<T> typeClass) {
		return Optional.of(unmarshal(input, typeClass));
	}

	/**
	 * Deserialisiert einen String mit XML-Daten in eine Java-Klasse eines
	 * angegebenen Typs. Fehlende XML-Mappings oder fehlerhaftes XML führen in
	 * dieser Methode zu einer IOException.
	 * 
	 * @param input     String mit XML-Daten
	 * @param typeClass Java-Klasse, die das Ziel der Deserialisierung sein soll
	 *                  (z.B. Profind.class)
	 * @param <T>       Generischer Typ
	 * @return Java-Objekt der angegebenen Typ-Klasse
	 */
	public static <T> T unmarshal(String input, Class<T> typeClass) {
		return JAXB.unmarshal(new StringReader(input), typeClass);
	}
}
