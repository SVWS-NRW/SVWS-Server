package de.svws_nrw.asd.validate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.asd.utils.json.JsonReader;


/**
 * Prüft, ob für die in svws-asd definierten Validatoren auch einen Fehlerkontext zugeordnet haben und ob dieser von der
 * Struktur her korrekt aufgebaut ist.
 */
class TestValidatorJsons {

	private final ObjectMapper mapper = new ObjectMapper();

	// Das Package, in welchem die Validatoren gesucht werden
	private static final String BASE_PACKAGE = "de.svws_nrw.asd.validate";


	@Test
	@DisplayName("Prüfe, ob für jeden Validator die JSON-Datei am korrekten Ort liegt und valide ist")
	void testValidatorJsonFiles() throws Exception {
		// Lese die Validatoren ein...
		final List<Class<? extends Validator>> validatorClasses = findValidatorClasses();
		assertThat(validatorClasses)
				.withFailMessage("Keine Validatoren in %s gefunden.", BASE_PACKAGE)
				.isNotEmpty();

		// Gehe alle Validatoren durch
		for (final Class<? extends Validator> clazz : validatorClasses) {

			// Prüfe, ob die JSON-Datei für den Validator existiert und den korrekten Namen hat
			final String expectedJsonPath = clazz.getName().replace('.', '/') + ".json";
			final String jsonContent = JsonReader.fromResourceOrEmptyString(expectedJsonPath);
			assertThat(jsonContent)
					.withFailMessage("JSON fehlt im Classpath oder der Pfad ist falsch: %s", expectedJsonPath)
					.isNotEmpty();

			// Prüfe, ob eine Version größer oder gleich 1 angegeben ist und der korrekte Klassenname eingetragen ist
			final JsonNode root = mapper.readTree(jsonContent);
			assertThat(root.get("version").asInt())
					.withFailMessage("Version fehlt in %s oder ist kleiner als 1", expectedJsonPath)
					.isGreaterThanOrEqualTo(1);
			assertThat(root.get("validator").asText())
					.withFailMessage("Falscher Klassenname im Attribut 'validator' in %s", expectedJsonPath)
					.isEqualTo(clazz.getName());

			// Prüfe, ob die Historie als Array vorliegt und eine korrekte zeitliche Abfolge existiert
			final JsonNode historieNode = root.get("historie");
			assertThat(historieNode.isArray())
					.withFailMessage("Das Attribut 'historie' muss ein Array sein in %s", expectedJsonPath)
					.isTrue();
			final List<ValidatorFehlerartKontext> historieEintraege = new ArrayList<>();
			for (final JsonNode entry : historieNode) {
				historieEintraege.add(mapper.treeToValue(entry, ValidatorFehlerartKontext.class));
			}
			checkHistorie(clazz, historieEintraege, expectedJsonPath);
		}
	}

	/**
	 * Prüft, ob die Liste der Historien-Einträge korrekt aufgebaut ist.
	 * Dabei wird auf eine chronologische Sortierung und eine Überlappungsfreiheit der Einträge Wert gelegt.
	 *
	 * @param clazz      die zu prüfende Klasse
	 * @param historie   die zu prüfende Liste mit den Historieneinträgen
	 * @param path       der Pfad der JSON-Datei für Fehlermeldungen
	 */
	private static void checkHistorie(final Class<?> clazz, final List<ValidatorFehlerartKontext> historie, final String path) {
		final String className = clazz.getSimpleName();

		// Extrahiere das Präfix aus dem Klassennamen - Alles nach "Validator" bis zum nächsten Großbuchstaben
		String expectedPrefix = "";
		if (!"ValidatorGesamt".equals(className)) { // nicht für den Speziallfall ValidatorGesamt, der kein Präfix hat
			final java.util.regex.Matcher matcher = java.util.regex.Pattern
					.compile("^Validator([A-Z][a-z0-9]+|[A-Z]+[a-z0-9]*)(?=[A-Z]|$)")
					.matcher(className);
			if (matcher.find()) {
				expectedPrefix = matcher.group(1).toUpperCase();
			}
		}

		// Durchwandere die Einträge der Historie der Reihe nach
		for (int i = 0; i < historie.size(); i++) {
			final ValidatorFehlerartKontext aktuell = historie.get(i);

			// Das Präfix darf in der Historie variieren, muss aber vorhanden, d.h. nicht null, sein
			assertThat(aktuell.praefix)
					.withFailMessage("Präfix fehlt in %s (Eintrag %d)", path, i)
					.isNotNull();

			// Prüfe, ob der Präfix des letzte Historieneintrags im Dateinamen korrekt vorkommt (Es kann sich durchaus über die Zeit verändern).
			if (i == (historie.size() - 1)) {
				if ("ValidatorGesamt".equals(className)) {
					assertThat(aktuell.praefix)
							.withFailMessage("Spezialfall ValidatorGesamt in %s: Präfix muss leer sein.", path)
							.isEmpty();
				} else {
					assertThat(aktuell.praefix)
							.withFailMessage("Präfix kommt in %s nicht korrekt im Namen des Validators vor: Erwartet '%s' (aus Klasse %s), gefunden '%s'.",
									path, expectedPrefix, className, aktuell.praefix)
							.isEqualTo(expectedPrefix);
				}
			}

			// Ist die Reienfolge von aktuell.gueltigVon und aktuell.gueltigBis korrekt?
			if ((aktuell.gueltigVon != null) && (aktuell.gueltigBis != null)) {
				assertThat(aktuell.gueltigVon)
						.withFailMessage("Falsche Reihenfolge in %s: gueltigVon (%d) liegt nach gueltigBis (%d)", path, aktuell.gueltigVon, aktuell.gueltigBis)
						.isLessThanOrEqualTo(aktuell.gueltigBis);
			}

			// Prüfe den Eintrag in Bezug auf den vorigen Eintrag
			if (i > 0) {
				final ValidatorFehlerartKontext vorherig = historie.get(i - 1);

				// Prüfe, ob das gueltigVon gesetzt ist, dieses muss bei allen Einträgen nach dem ersten Eintrag gesetzt sein
				assertThat(aktuell.gueltigVon)
						.withFailMessage("Überlappung in %s: gueltigVon (%d) muss nach dem ersten Eintrag immer gesetzt sein.",
								path, aktuell.gueltigVon, vorherig.gueltigBis)
						.isNotNull();

				// Prüfe den Beginn des Eintrags
				if ((aktuell.gueltigVon != null) && (vorherig.gueltigVon != null)) {
					// Prüfe auf gleichen Beginn
					assertThat(aktuell.gueltigVon)
							.withFailMessage("Es sind keine zwei Einträge mit gleichen Beginn (gueltigVon) erlaubt (Eintrag %d)", path, i)
							.isNotEqualTo(vorherig.gueltigVon);

					// Prüfe auf die korrekte Reihenfolge bei gueltigVon
					assertThat(aktuell.gueltigVon)
							.withFailMessage("Historie in %s nicht chronologisch sortiert (Eintrag %d, siehe gueltigVon)", path, i)
							.isGreaterThan(vorherig.gueltigVon);
				}

				// Prüfe, ob der vorige Eintrag nicht begrenzt ist
				if (vorherig.gueltigBis == null) {
					fail(String.format(
							"Überlappung in %s: Da Eintrag %d kein Enddatum hat (gueltigBis=null), daher es keine Folge-Eintrag geben.", path, i - 1));
				}

				// Prüfe, ob das Ende mit dem Ende des vorigen Eintrags übereinstimmt (null brauch hier nicht mehr beachtet werden
				assertThat(aktuell.gueltigBis)
						.withFailMessage("Es sind keine zwei Einträge mit gleichen Ende (gueltigBis) erlaubt (Eintrag %d)", path, i)
						.isNotEqualTo(vorherig.gueltigBis);

				// Prüfe, ob der Beginn des aktuellen Eintrags nach dem Ende des vorigen Eintrags liegt
				assertThat(aktuell.gueltigVon)
						.withFailMessage("Überlappung in %s: gueltigVon (%d) des aktuellen Eintrags muss nach gueltigBis (%d) des vorherigen liegen.",
								path, aktuell.gueltigVon, vorherig.gueltigBis)
						.isGreaterThan(vorherig.gueltigBis);
			}
		}
	}

	/**
	 * Ermittelt für das Package BASE_PACKAGE alle Instanzen der von {@link Validator} abgeleiteten Klasse
	 * in diesem Package oder in Subpackages davon.
	 *
	 * @return die Liste mit den von Validator abgeleiteten Klassen
	 *
	 * @throws Exception falls ein Fehler auftritt
	 */
	private static List<Class<? extends Validator>> findValidatorClasses() throws Exception {
		final List<Class<? extends Validator>> classes = new ArrayList<>();
		final List<Path> classFiles = JsonReader.getFilesInPackage(BASE_PACKAGE, ".class");

		for (final Path path : classFiles) {
			final String pathString = path.toString().replace(path.getFileSystem().getSeparator(), ".");
			final int startIndex = pathString.indexOf(BASE_PACKAGE);
			if (startIndex == -1) {
				continue;
			}

			final String className = pathString.substring(startIndex).replace(".class", "");
			try {
				final Class<?> clazz = Class.forName(className);
				if (Validator.class.isAssignableFrom(clazz) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
					@SuppressWarnings("unchecked") final Class<? extends Validator> validatorClass = (Class<? extends Validator>) clazz;
					classes.add(validatorClass);
				}
			} catch (@SuppressWarnings("unused") ClassNotFoundException | NoClassDefFoundError e) {
				// Falls eine Klasse nicht geladen werden kann, überspringen
			}
		}
		return classes;
	}


	@Test
	@DisplayName("Prüfe, ob die Fehlercode-Präfixe über alle Validatoren in jedem Schuljahr eindeutig sind.")
	void testIsValidatorPrefixUnique() throws Exception {
		final List<Class<? extends Validator>> validatorClasses = findValidatorClasses();

		// Erzeuge eine Map mit der Abbildung von einem Präfix auf die Liste aller Zeiträume, in denen dieses Präfix von einem Validator genutzt wird
		final Map<String, List<PrefixInterval>> prefixMap = new HashMap<>();
		for (final Class<? extends Validator> clazz : validatorClasses) {
			final String resourceLocation = clazz.getName().replace('.', '/') + ".json";
			final String jsonContent = JsonReader.fromResourceOrEmptyString(resourceLocation);
			if (jsonContent.isEmpty())
				continue;

			final JsonNode root = JsonReader.mapper.readTree(jsonContent);
			final String validatorName = root.get("validator").asText();
			final JsonNode historie = root.get("historie");

			for (final JsonNode eintrag : historie) {
				final String prefix = eintrag.get("praefix").asText();
				if ((prefix == null) || (prefix.isEmpty()))
					continue;

				final int von = eintrag.get("gueltigVon").isNull() ? Integer.MIN_VALUE : eintrag.get("gueltigVon").asInt();
				final int bis = eintrag.get("gueltigBis").isNull() ? Integer.MAX_VALUE : eintrag.get("gueltigBis").asInt();

				prefixMap.computeIfAbsent(prefix, k -> new ArrayList<>()).add(new PrefixInterval(validatorName, von, bis));
			}
		}

		// Prüfe für jedes Präfix, ob sich die Intervalle unterschiedlicher Validatoren überschneiden (nutze zur Optimierung eine Vorsortierung)
		for (final Map.Entry<String, List<PrefixInterval>> entry : prefixMap.entrySet()) {
			final String prefix = entry.getKey();
			final List<PrefixInterval> intervals = entry.getValue();

			// Sortiere nach 'von', bei Gleichheit nach 'bis', um die Präfix-Duplikatprüfung effizient durchführen zu können.
			intervals.sort((a, b) -> a.von != b.von ? Integer.compare(a.von, b.von) : Integer.compare(a.bis, b.bis));

			// Gehe jetzt die Intervalle per Sweep-Verfahren durch
			for (int i = 0; i < intervals.size() - 1; i++) {
				final PrefixInterval aktuell = intervals.get(i);
				final PrefixInterval naechster = intervals.get(i + 1);

				// Wenn das Ende des aktuellen nach oder auf dem Beginn des nächsten liegt, dann liegt eine Kollision vor
				// - es sei denn, es ist derselbe Validator, dann wurde es bereits in checkHistorie geprüft
				if (!aktuell.validator.equals(naechster.validator) && aktuell.bis >= naechster.von) {
					fail(String.format("Präfix-Kollision: Das Präfix '%s' wird überlappend verwendet von '%s' (bis %s) und '%s' (ab %s).",
							prefix, aktuell.validator, formatYear(aktuell.bis), naechster.validator, formatYear(naechster.von)));
				}
			}
		}
	}

	/**
	 * Hilfsklasse zur Speicherung eines Präfix-Gültigkeitszeitraums.
	 */
	private static class PrefixInterval {
		/** Der Name des Validators */
		final String validator;

		/** Der Beginn (Jahr bzw. MIN_VALUE) */
		final int von;

		/** Das Ende (Jahr bzw. MAX_VALUE) */
		final int bis;

		PrefixInterval(final String validator, final int von, final int bis) {
			this.validator = validator;
			this.von = von;
			this.bis = bis;
		}
	}

	/**
	 * Formatiert die Jahreszahlen für die Fehlermeldung.
	 *
	 * @param year das Jahr.
	 *
	 * @return der String mit der Bezeichnung für Fehlerausgaben
	 */
	private static String formatYear(final int year) {
		if (year == Integer.MIN_VALUE) {
			return "Beginn (null)";
		}
		if (year == Integer.MAX_VALUE) {
			return "Ende (null)";
		}
		return String.valueOf(year);
	}

}
