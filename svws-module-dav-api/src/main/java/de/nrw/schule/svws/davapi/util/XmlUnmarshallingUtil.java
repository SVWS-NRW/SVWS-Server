package de.nrw.schule.svws.davapi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import de.nrw.schule.svws.core.logger.LogConsumerConsole;
import de.nrw.schule.svws.core.logger.LogLevel;
import de.nrw.schule.svws.core.logger.Logger;

/**
 * Utility-Klasse für die Deserialisierung von XML in Java-Objekte mittels
 * Jackson. Die enthaltenen Methoden modifizieren das Verhalten der Jackson
 * ObjectMapper, um die JAXB-annotierten Dav-Objekte
 * ({@link de.nrw.schule.svws.davapi.model.dav.Prop}) besser behandeln zu
 * können.
 */
public class XmlUnmarshallingUtil {

	/**
	 * Logger für diese Klasse
	 */
	private static final Logger logger = createLogger();

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
	 * @throws IOException Fehlende XML-Mappings oder fehlerhaftes XML führen in
	 *                     dieser Methode zu einer IOException
	 */
	public static <T> T unmarshal(InputStream inputstream, Class<T> typeClass) throws IOException {
		if (typeClass == String.class) {
			return typeClass.cast(unmarshalToString(inputstream));
		}
		ObjectMapper mapper = getMapper();
		return mapper.readValue(inputstream, typeClass);
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
		try {
			return Optional.of(unmarshal(inputstream, typeClass));
		} catch (IOException e) {
			logger.log("Error beim Unmarshalling des Inputstreams: " + e.getMessage());
			return Optional.empty();
		}
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
		try {
			T unmarshal = unmarshal(input, typeClass);
			return Optional.of(unmarshal);
		} catch (IOException e) {
			StringWriter out = new StringWriter();
			PrintWriter pw = new PrintWriter(out);
			e.printStackTrace(pw);
			// info level log, weil hier Fehler regelmäßig zu erwarten sind (bspw. weil
			// bewusst verschiedene typeClass geprüft werden)
			logger.log(LogLevel.INFO, "Bei der Anfrage ist folgende IOException aufgetreten:" + pw.toString());
			return Optional.empty();
		}
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
	 * @throws IOException Fehlende XML-Mappings oder fehlerhaftes XML führen in
	 *                     dieser Methode zu einer IOException
	 */
	public static <T> T unmarshal(String input, Class<T> typeClass) throws IOException {
		ObjectMapper mapper = getMapper();
		return mapper.readValue(input, typeClass);
	}

	/**
	 * Konvetiert einen InputStream in einen String.
	 *
	 * @param inputstream InputStream
	 * @return String-Repräsentation des InputStreams
	 * @throws IOException Fehlende XML-Mappings oder fehlerhaftes XML führen in
	 *                     dieser Methode zu einer IOException
	 */
	private static String unmarshalToString(InputStream inputstream) {
		return new BufferedReader(new InputStreamReader(inputstream)).lines().collect(Collectors.joining("\n"));
	}

	/**
	 * Erstellt einen neuen ObjectMapper zum (de-)serialisieren von Xmls
	 */
	private static ObjectMapper getMapper() {
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		ObjectMapper mapper = new XmlMapper(module);
		mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
		mapper.registerModule(new JakartaXmlBindAnnotationModule());
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		return mapper;
	}

	private static Logger createLogger() {
		Logger logger = new Logger();
		logger.addConsumer(new LogConsumerConsole(true, false));
		return logger;
	}
}
